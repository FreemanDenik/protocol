package com.execute.protocol.auth.services;

import com.execute.protocol.core.enums.EnumProviders;
import com.execute.protocol.auth.models.OtherOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class OtherOAuth2UserService extends DefaultOAuth2UserService {
    private final RestTemplate restTemplate;
    private final OtherOAuth2ProviderService otherOAuth2ProviderService;

    @Autowired
    public OtherOAuth2UserService(RestTemplate restTemplate, OtherOAuth2ProviderService otherOAuth2ProviderService) {
        this.restTemplate = restTemplate;
        this.otherOAuth2ProviderService = otherOAuth2ProviderService;
    }

    /**
     * Получаем данные определенного провайдера
     * передаем их в соответствущий метод для получения сформированного
     * соответствущих данных класса OAuth2User.
     * Так же передаем конвертер который на ходу получает необходимые для конверта данные провайдер-атрибутов
     *
     * @param oAuth2UserRequest
     * @return OAuth2User
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // Определяем провайдера
        switch (oAuth2UserRequest.getClientRegistration().getRegistrationId()) {
            case "vkontakte":
                return new OtherOAuth2User(
                        loadVkUser(oAuth2UserRequest),
                        // Конвертируем атрибуты провайдера vkontakte
                        otherOAuth2ProviderService.getAttributes(EnumProviders.VKONTAKTE),
                        EnumProviders.VKONTAKTE);
            case "yandex":
                return new OtherOAuth2User(
                        loadYandexUser(oAuth2UserRequest),
                        // Конвертируем атрибуты провайдера yandex
                        otherOAuth2ProviderService.getAttributes(EnumProviders.YANDEX),
                        EnumProviders.YANDEX);
            case "mail":
                return new OtherOAuth2User(
                        loadMailUser(oAuth2UserRequest),
                        // Конвертируем атрибуты провайдера yandex
                        otherOAuth2ProviderService.getAttributes(EnumProviders.MAIL),
                        EnumProviders.MAIL);
            default:
                throw new OAuth2AuthenticationException("Не определен поставщик аккаунта");
        }
    }

    /**
     * Метод авторизации получения токена и краткой информации по пользователю в Yandex
     *
     * @param oAuth2UserRequest
     * @return OAuth2User
     */
    private OAuth2User loadYandexUser(OAuth2UserRequest oAuth2UserRequest) {
        // Формируем строку токена
        String tokenValue =
                // Bear
                oAuth2UserRequest.getAccessToken().getTokenType().getValue()
                + " " +
                // Token
                oAuth2UserRequest.getAccessToken().getTokenValue();
        // Формируем строку запроса данных пользователя
        String uri = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        // Передаем строку токена и получаем краткую инфо пользователя сервиса yandex
        Map<String, Object> response = getUserInfo(uri, Map.of("Authorization", tokenValue));
        Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(response));
        return new DefaultOAuth2User(authorities, response, "id");
    }

    /**
     * Метод авторизации получения токена и краткой инфы по пользователю в VK
     *
     * @param oAuth2UserRequest
     * @return OAuth2User
     */
    private OAuth2User loadVkUser(OAuth2UserRequest oAuth2UserRequest) {
        // Получаем id приложения vk
        var userId = oAuth2UserRequest.getAdditionalParameters().get("user_id").toString();
        // Получаем токен
        var token = oAuth2UserRequest.getAccessToken().getTokenValue();
        // Формируем строку запроса данных пользователя подставляя в указанные места <***>, id приложения и токен
        String uri = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()
                .replace("<user_id>", userId)
                .replace("<token>", token);

        // Получаем по токену краткую инфо пользователя сервиса vkontakte
        Map<String, Object> response = (Map<String, Object>) ((ArrayList) getUserInfo(uri).get("response")).get(0);
        // У сервиса vkontake нет поля email, там вроде на номер телефона перешли, но и его сервис не предоставляет
        // и дабы поле не оставалось пустым заполняем его чем то, в данном случаи first_name
        response.put("email", response.get("first_name"));
        Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(response));
        return new DefaultOAuth2User(authorities, response, "id");
    }

    /**
     * Метод авторизации получения токена и краткой инфы по пользователю в Mail
     *
     * @param oAuth2UserRequest
     * @return OAuth2User
     */
    private OAuth2User loadMailUser(OAuth2UserRequest oAuth2UserRequest) {
        // Формируем строку запроса данных пользователя подставляя в указанное место <***> токен
        String uri = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()
                .replace("<token>", oAuth2UserRequest.getAccessToken().getTokenValue());

        // Получаем по токену краткую инфо пользователя сервиса mail
        Map<String, Object> response = getUserInfo(uri);
        Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(response));
        return new DefaultOAuth2User(authorities, response, "id");
    }
    /**
     * Метод для отправки запроса у казанные в uri пути сервиса, и получения краткой информации о пользователе
     * @param uri
     * @return
     */
    private Map<String, Object> getUserInfo(String uri) {
        return getUserInfo(uri, new HashMap<>());
    }
    // Проброс вышеуказанного метода
    private Map<String, Object> getUserInfo(String uri, Map<String, String> addHeaders) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add("Content-Type", "application/json");
        // Добавляем header'ы если таковы были переданы
        addHeaders.forEach((k, v) -> headers.add(k, v));
        HttpEntity<?> httpRequest = new HttpEntity(headers);
        // Непосредственно сам запрос в сервис
        ResponseEntity<Map> entity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, Map.class);

        return entity.getBody();
    }
}

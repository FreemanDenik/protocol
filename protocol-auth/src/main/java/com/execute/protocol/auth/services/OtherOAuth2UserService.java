package com.execute.protocol.auth.services;


import com.execute.protocol.auth.enums.EnumProviders;
import com.execute.protocol.auth.models.OtherOAuth2User;
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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Component
public class OtherOAuth2UserService extends DefaultOAuth2UserService {
    private final RestTemplate restTemplate;

    public OtherOAuth2UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        switch (oAuth2UserRequest.getClientRegistration().getRegistrationId()) {
           /* case "vkontakte":
                return new OtherOAuth2User(loadVkUser(oAuth2UserRequest), EnumProviders.VKONTAKTE);*/
            case "yandex":
                return new OtherOAuth2User(loadYandexUser(oAuth2UserRequest), EnumProviders.YANDEX);
            default:
                throw new OAuth2AuthenticationException("Не определен поставщик аккаунта");
        }
        //return null;
    }
    /**
     * Метод авторизации получения токена и краткой инфы по пользователю в Yandex
     *
     * @param oAuth2UserRequest
     * @return OAuth2User
     */
    private OAuth2User loadYandexUser(OAuth2UserRequest oAuth2UserRequest) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", oAuth2UserRequest.getAccessToken().getTokenType().getValue() + " " + oAuth2UserRequest.getAccessToken().getTokenValue());
        HttpEntity<?> httpRequest = new HttpEntity(headers);
        String uri = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        // Получаем по токену краткую инфо пользователя сервиса через которую идет регистрация
        ResponseEntity<Map> entity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, Map.class);
        Map<String, Object> response = entity.getBody();
        Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(response));
        return new DefaultOAuth2User(authorities, response, "default_email");
    }
}

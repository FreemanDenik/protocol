package com.execute.protocol.auth.models;

import com.execute.protocol.auth.enums.EnumProviders;
import com.execute.protocol.auth.services.OtherOAuth2ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class OtherOAuth2User extends AbstractProvider implements OAuth2User {

    private final OAuth2User oAuth2User;
    @Autowired
    private OtherOAuth2ProviderService otherOAuth2ProviderService;
    ;

    /**
     * Отправляем полученные данные провайдера OAuth2User и название провайдера EnumProviders
     * в сервисе OAuth2ProviderService для корректного получения данных провайдера (last_name, email и т.д.),
     * принимает и хранит данные AbstractProvider от которого расширяется данный класс
     *
     * @param oAuth2User
     * @param providers
     */

    public OtherOAuth2User(
            OAuth2User oAuth2User,
            EnumProviders providers) {
        // Передаем данные в родительский конструктор AbstractProvider
        this.setParameters(oAuth2User, otherOAuth2ProviderService.getAttributes(providers));
        this.oAuth2User = oAuth2User;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return this.getFirstName();
    }


}

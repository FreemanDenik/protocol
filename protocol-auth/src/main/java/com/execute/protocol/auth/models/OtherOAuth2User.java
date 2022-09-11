package com.execute.protocol.auth.models;

import com.execute.protocol.core.enums.EnumProviders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class OtherOAuth2User extends AbstractProvider implements OAuth2User {

    private OAuth2User oAuth2User;


    public OtherOAuth2User(
            OAuth2User oAuth2User,
            AbstractProvider provider,
            EnumProviders providerName) {
        // Передаем данные в родительский конструктор AbstractProvider
        this.setParameters(oAuth2User, provider, providerName);
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

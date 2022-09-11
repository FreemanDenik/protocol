package com.execute.protocol.auth.models;

import com.execute.protocol.core.enums.EnumProviders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class OtherOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;
    private long clientId;
    private EnumProviders providerName;
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;

    public OtherOAuth2User(
            OAuth2User oAuth2User,
            AbstractProvider provider) {
        // Процесс конвертации атрибутов провайдера к соответствущим полям
        clientId =  Long.parseLong(oAuth2User.getAttribute(provider.getClientId()).toString());
        providerName = provider.getProviderName();
        firstName = oAuth2User.getAttribute(provider.getFirstName());
        lastName = oAuth2User.getAttribute(provider.getLastName());
        birthday = oAuth2User.getAttribute(provider.getBirthday());
        email = oAuth2User.getAttribute(provider.getEmail());
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
        return this.firstName;
    }


}

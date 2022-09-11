package com.execute.protocol.auth.models;

import com.execute.protocol.core.enums.EnumProviders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
public abstract class AbstractProvider {
    private EnumProviders providerName;
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;

    public AbstractProvider() {
    }
    void setParameters(OAuth2User oAuth2User, AbstractProvider provider, EnumProviders providerName) {
        // Получаем из property название провайдера без конвертации

        this.providerName = providerName;
        firstName = oAuth2User.getAttribute(provider.getFirstName());
        lastName = oAuth2User.getAttribute(provider.getLastName());
        birthday = oAuth2User.getAttribute(provider.getBirthday());
        email = oAuth2User.getAttribute(provider.getEmail());
    }
}

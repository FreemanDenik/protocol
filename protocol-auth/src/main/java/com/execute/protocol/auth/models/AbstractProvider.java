package com.execute.protocol.auth.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
public abstract class AbstractProvider {
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;
    public AbstractProvider(){

    }
    void setParameters(OAuth2User oAuth2User, AbstractProvider provider) {

        this.setFirstName(oAuth2User.getAttribute(provider.getFirstName()));
        this.setLastName(oAuth2User.getAttribute(provider.getLastName()));
        this.setBirthday(oAuth2User.getAttribute(provider.getBirthday()));
        this.setEmail(oAuth2User.getAttribute(provider.getEmail()));

    }
}

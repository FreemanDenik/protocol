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
    private String clientId;
    private EnumProviders providerName;
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;


}

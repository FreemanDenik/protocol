package com.execute.protocol.core.entities.acc;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    USER, ADMIN, MODERATOR;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
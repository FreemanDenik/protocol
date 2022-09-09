package com.execute.protocol.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class TokenService {
    // jwt cookie
    @Value("${jwt.token.cookie.name}")
    private String jwtCookieName;
    final HttpServletRequest httpServletRequest;

    public TokenService(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getToken() {
        try {
            return Arrays.stream(httpServletRequest.getCookies()).filter(w -> w.getName().equals(jwtCookieName)).findFirst().get().getValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

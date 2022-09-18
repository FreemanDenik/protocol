package com.execute.protocol.auth.configs.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OutSuccessHandler implements LogoutSuccessHandler {
    // jwt cookie
    @Value("${jwt.token.cookie.name}")
    private String jwtCookieName;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Cookie cookie = new Cookie(jwtCookieName, null);
        cookie.setMaxAge(-1);
        httpServletResponse.addCookie(cookie);
        httpServletResponse.sendRedirect("/");
    }
}

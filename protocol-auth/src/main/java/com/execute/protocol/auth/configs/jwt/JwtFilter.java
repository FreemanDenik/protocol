package com.execute.protocol.auth.configs.jwt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;
@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";
    private JwtProvider jwtProvider;
    private UserDetailsService userDetailsService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("do filter");
       // SecurityContextHolder.clearContext();
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (Objects.nonNull(token) && jwtProvider.validateToken(token)) {
            String userLogin = jwtProvider.getEmailFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer_")) {
            return bearer.substring(7);
        }
        return null;
    }
}

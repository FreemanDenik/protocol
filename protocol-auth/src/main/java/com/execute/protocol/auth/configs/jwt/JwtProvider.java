package com.execute.protocol.auth.configs.jwt;


import com.execute.protocol.auth.enums.EnumCookie;
import com.execute.protocol.core.entities.AccountId;
import com.execute.protocol.core.enums.EnumProviders;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtProvider {

    // jwt
    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expiredTimeInMinutes}")
    private Long jwtExpiredTimeInMinutes;

    // jwt cookie
    @Value("${jwt.token.cookie.name}")
    private String jwtCookieName;
    @Value("${jwt.token.cookie.path}")
    private String jwtCookiePath;
    @Value("${jwt.token.cookie.age}")
    private int jwtCookieAge;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public JwtProvider(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public String generateToken(String email){
       return generateToken(email, EnumCookie.NO_SET_COOKE);
    }
    public String generateToken(String email, EnumCookie enumCookie) {
        Date date = new Date(System.currentTimeMillis() + jwtExpiredTimeInMinutes * 60 * 1000);
        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        // Устанавливаем токен в куки сразу после создания
        if (EnumCookie.SET_COOKIE == enumCookie) {
            Cookie cookie = new Cookie(jwtCookieName, token);
            cookie.setPath(jwtCookiePath);
            cookie.setMaxAge(jwtCookieAge);
            httpServletResponse.addCookie(cookie);
        }
        return token;
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt");
        } catch (SignatureException sEx) {
            log.error("Invalid signature");
        } catch (Exception e) {
            log.error("invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
//    public AccountId getAccountIdFromToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//
//
//
//        return new AccountId().stringTo(claims.getSubject());
//    }
}

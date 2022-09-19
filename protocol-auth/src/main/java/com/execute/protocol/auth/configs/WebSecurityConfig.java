package com.execute.protocol.auth.configs;

import com.execute.protocol.auth.configs.jwt.JwtFilter;
import com.execute.protocol.auth.configs.jwt.AuthSuccessHandler;
import com.execute.protocol.auth.configs.jwt.OutSuccessHandler;
import com.execute.protocol.auth.converters.OtherTokenResponseConverter;
import com.execute.protocol.auth.services.OtherOAuth2UserService;
import com.execute.protocol.auth.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@SuppressWarnings("deprecation")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // jwt cookie
    @Value("${jwt.token.cookie.name}")
    private String jwtCookieName;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtFilter jwtFilter;
    private final OtherOAuth2UserService otherOAuth2UserService;

    private final AuthSuccessHandler authSuccessHandler;
    private final OutSuccessHandler outSuccessHandler;
    @Autowired
    public WebSecurityConfig(
            UserDetailsServiceImpl userDetailsServiceImpl,
            JwtFilter jwtFilter,
            OtherOAuth2UserService otherOAuth2UserService, AuthSuccessHandler authSuccessHandler, OutSuccessHandler outSuccessHandler) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtFilter = jwtFilter;
        this.otherOAuth2UserService = otherOAuth2UserService;
        this.authSuccessHandler = authSuccessHandler;
        this.outSuccessHandler = outSuccessHandler;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                //.cors().and().csrf().disable()
                .authorizeRequests()
                //.antMatchers("/api/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/game/**").hasRole("USER")
                .antMatchers("/", "/login").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .loginPage("/login")
                .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .userInfoEndpoint().userService(otherOAuth2UserService)
                .and()
                .successHandler(authSuccessHandler);

        http
                .logout()//URL выхода из системы безопасности Spring - только POST.
                // Вы можете поддержать выход из системы без POST, изменив конфигурацию Java
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//выход из системы гет запрос на /logout
                //.deleteCookies(jwtCookieName) // Удаляем куки токен
                //.logoutSuccessUrl("/")//успешный выход из системы
                .logoutSuccessHandler(outSuccessHandler)
                .and().csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    //    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(allowedOrigins);
//        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(/*@Autowired RestTemplate restTemplate*/) {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();

        tokenResponseHttpMessageConverter.setTokenResponseConverter(new OtherTokenResponseConverter());
        //tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new OtherTokenResponseConverter());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));

        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }
}

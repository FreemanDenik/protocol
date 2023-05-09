package com.execute.protocol.app.config;

import com.execute.protocol.auth.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableAutoConfiguration
public class WebConfig implements WebMvcConfigurer{//implements WebMvcConfigurer
//    @Bean
//    public DataSource dataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("org.hibernate.dialect.MySQLDialect");
//        dataSourceBuilder.url("jdbc:mysql://localhost:3390/protocol_db");
//        dataSourceBuilder.username("root");
//        dataSourceBuilder.password("2174");
//        return dataSourceBuilder.build();
//    }
//    @Bean("entityManagerFactory")
//    public EntityManagerFactory entityManagerFactory() {
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(true);
//
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setPackagesToScan("com.execute.protocol");
//        factory.setDataSource(dataSource());
//        factory.afterPropertiesSet();
//
//        return factory.getObject();
//    }
    @Value("${cors.allowed.origins}")
    public String[] allowedOrigins;
    final private JwtFilter jwtFilter;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowedOrigins);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        authz -> authz
                                .antMatchers("/api/auth/register", "/api/auth/login",  "/api/auth/token").permitAll()
                                .antMatchers("/api/game/**").hasAuthority("USER")
                                .antMatchers("/api/admin/**").hasAuthority("ADMIN")
                               // .antMatchers("/api/game/go").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                                .and()
                                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                );
        return http.build();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }


}

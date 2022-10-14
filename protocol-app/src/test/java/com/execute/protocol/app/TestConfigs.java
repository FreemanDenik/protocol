package com.execute.protocol.app;

import com.execute.protocol.auth.configs.AuthConfig;
import com.execute.protocol.core.repositories.EventRepository;
import com.execute.protocol.core.services.EventService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.support.collections.RedisProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import redis.embedded.RedisServer;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@TestConfiguration
// Эта анотация позволяет отключать/включать бины или configuration классы
// На данный моменты включает конфигурацию только если есть в пропертях указанное значение add.redis.server.for.jwt.tests=true
@ConditionalOnProperty(value = "add.redis.server.for.jwt.tests", havingValue = "true")
public class TestConfigs {
    // Настройка встраиваемого сервера Embedded-Redis для тестов
    private RedisServer redisServer;
    public TestConfigs() {
        this.redisServer = new RedisServer(7776);
    }
    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }
    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
//    @Bean
//    // Эта анотация позволяет отключать/включать бины или configuration классы
//    // На данный моменты включает бин только если есть в пропертях указанное значение add.empty.mvc.for.data.jpa.tests=true
//    @ConditionalOnProperty(value = "add.mvc.handler.for.data.jpa.tests", havingValue = "true")
//    // Без этого бина не работает аннотация @DataJpaTest
//    // Причем это спицефично для этого проекта
//    // Проверял в другом проекте без этой анотации и тестовой конфигурации, все работает!
//    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
//        return new HandlerMappingIntrospector();
//    }
}


/*
    Можно добавить тестовую конфигурацию прямо в тест, вот так:

    @TestConfiguration
    static class TestConfig {
        @Bean
        public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
            return new HandlerMappingIntrospector();
        }
    }

 */
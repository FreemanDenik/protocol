package com.execute.protocol.app;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@TestConfiguration
public class TestConfigs {
    // Без этого бина не работает аннотация @DataJpaTest
    // Причем это спицефично для этого проекта
    // Проверял в другом проекте без этой анотации и тестовой конфигурации, все работает!
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
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
package com.execute.protocol.app.configs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.PropertySource;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Настройка встраиваемого Embedded-Redis сервера для тестов
 * Все тесты должны иметь аннотации
 * @SpringBootTest
 * @AutoConfigureTestDatabase
 * т.к. TestConfiguration применяется ко всем тестам,
 * и все они пытаются настроить Embedded-Redis
 */

@TestConfiguration
// В файле test.properties есть строка "spring.redis.port",
// которая переопределяет порт обычного Docker Redis на порт тестового Embedded-Redis.
// Это строка должна именно переопределяться в файле property так как внедрение и запуск тестового Embedded-Redis сервера
// использует настройки Docker Redis (в файле AuthConfig) который в свою очередь получает порт из property
@PropertySource("classpath:test.properties")
public class TestConfigEmbeddedRedisServer {
    private final RedisServer redisServer;
    // порт обязательно нужно указывать в property файле test.properties
    public TestConfigEmbeddedRedisServer(@Value("${spring.redis.port}") int redisPort) {
        this.redisServer = new RedisServer(redisPort);
    }
    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }
    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}



//    @Bean
//    // Эта аннотация позволяет отключать/включать bean'ы или configuration классы
//    // На данный моменты включает бин только если есть в property указанное значение add.empty.mvc.for.data.jpa.tests=true
//    @ConditionalOnProperty(value = "add.mvc.handler.for.data.jpa.tests", havingValue = "true")
//    // Без этого bean'а не работает аннотация @DataJpaTest
//    // Причем это специфично для этого проекта
//    // Проверял в другом проекте без этой аннотации и тестовой конфигурации, все работает!
//    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
//        return new HandlerMappingIntrospector();
//    }
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
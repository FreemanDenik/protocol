package com.execute.protocol.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@ComponentScan("com.execute")
@EntityScan("com.execute.protocol.core")
@EnableRedisRepositories("com.execute.protocol.auth.repositories")
@EnableJpaRepositories("com.execute.protocol.core.repositories")
@PropertySource({
        "classpath:core.properties",
        "classpath:auth.properties",
        "classpath:admin.properties",
})
@Slf4j

public class ProtocolAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProtocolAppApplication.class, args);
    }

}

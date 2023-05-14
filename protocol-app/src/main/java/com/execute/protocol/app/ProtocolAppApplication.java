package com.execute.protocol.app;

import com.execute.protocol.core.systems.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@ComponentScan("com.execute")
@EntityScan("com.execute.protocol.core")
@EnableRedisRepositories("com.execute.protocol.auth.repositories")
@EnableJpaRepositories("com.execute.protocol.core.repositories")
//@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = {
        //"classpath:core.properties",
        "classpath:auth-${spring.profiles.active}.yml"
        //"classpath:admin.properties"
}, factory = YamlPropertySourceFactory.class)
public class ProtocolAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProtocolAppApplication.class, args);
    }

}

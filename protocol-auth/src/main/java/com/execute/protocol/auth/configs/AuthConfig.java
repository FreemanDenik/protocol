package com.execute.protocol.auth.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisRepositories
public class AuthConfig  {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${spring.redis.database}")
    private int redisDatabase;
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // Максимальное время ожидания для установления соединения
        jedisPoolConfig.setMaxWaitMillis(3600);
        // Минимальное время простоя для удаления соединений. По умолчанию 1800000 миллисекунд (30 минут)
//        jedisPoolConfig.setMinEvictableIdleTimeMillis(1800000);
        // Максимальное количество выселений во время каждой проверки выселения. Если это отрицательное число, это: 1 / abs (n), по умолчанию 3
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        // Временной интервал сканирования выселения (миллисекунды) Если это отрицательное число, поток выселения не будет запущен, по умолчанию -1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(-1);
        // Проверять ли перед удалением соединения из пула, если проверка не удалась, удаляем соединение из пула и пытаемся удалить другое
        jedisPoolConfig.setTestOnBorrow(true);
        // Проверяем действительность в режиме ожидания, по умолчанию false
        jedisPoolConfig.setTestWhileIdle(false);
        return jedisPoolConfig;
    }
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(redisDatabase);
        config.setPassword(redisPassword);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setEnableTransactionSupport(true);
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

}

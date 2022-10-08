package com.execute.protocol.auth.configs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration

public class AuthConfig  {
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
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("127.0.0.1", 7000);
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

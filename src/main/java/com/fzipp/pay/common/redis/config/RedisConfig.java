package com.fzipp.pay.common.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author ff
 * @Date 2022/2/18 16:02
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericFastJsonRedisSerializer valueSerializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 序列化 key 和 value
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        return redisTemplate;
    }

}
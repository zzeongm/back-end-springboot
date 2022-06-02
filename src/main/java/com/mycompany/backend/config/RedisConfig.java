package com.mycompany.backend.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Configuration
public class RedisConfig {
  @Value("${spring.redis.hostName}")
  private String hostName;
  @Value("${spring.redis.port}")
  private String port;
  @Value("${spring.redis.password}")
  private String password;
@Bean
public RedisConnectionFactory redisConnectionFactory() {
  log.info("실");
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(hostName);
    config.setPort(Integer.parseInt(port));
    config.setPassword(password);
    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(config);
    return connectionFactory;
}
@Bean
public RedisTemplate<String,String> redisTemplate(){
  log.info("실행");
  RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
  redisTemplate.setConnectionFactory(redisConnectionFactory());
  redisTemplate.setKeySerializer(new StringRedisSerializer());
  redisTemplate.setValueSerializer(new StringRedisSerializer());
  return redisTemplate;
}
}

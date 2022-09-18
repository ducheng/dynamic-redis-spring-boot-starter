package com.ducheng.multi.redis.autoconf;

import com.ducheng.multi.redis.aop.MuLtiRedisInterceptor;
import com.ducheng.multi.redis.aop.MultiRedisMethodPointcutAdvisor;
import com.ducheng.multi.redis.factory.MultiRedisConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisCustomizedConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@Configuration(proxyBeanMethods = false)
@Import(RedisCustomizedConfiguration.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties(MultiRedisProperties.class)
public class RedisCustomizedAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MuLtiRedisInterceptor lockInterceptor(MultiRedisConnectionFactory multiRedisConnectionFactory) {
        return new MuLtiRedisInterceptor(multiRedisConnectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiRedisMethodPointcutAdvisor lockAnnotationAdvisor(MuLtiRedisInterceptor muLtiRedisInterceptor) {
        return new MultiRedisMethodPointcutAdvisor(muLtiRedisInterceptor, Ordered.HIGHEST_PRECEDENCE);
    }
}

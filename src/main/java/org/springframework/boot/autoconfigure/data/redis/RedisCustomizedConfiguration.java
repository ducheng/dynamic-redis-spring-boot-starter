package org.springframework.boot.autoconfigure.data.redis;


import com.ducheng.multi.redis.factory.MultiRedisConnectionFactory;
import com.ducheng.multi.redis.autoconf.MultiRedisProperties;
import io.lettuce.core.resource.ClientResources;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@ConditionalOnProperty(prefix = "spring.redis", value = "enable-multi", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
public class RedisCustomizedConfiguration {


    @Bean
    public MultiRedisConnectionFactory multiRedisConnectionFactory(
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers,
            ClientResources clientResources,
            MultiRedisProperties multiRedisProperties,
            ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
            ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider) {
        Map<String, LettuceConnectionFactory> connectionFactoryMap = new HashMap<>();
        Map<String, RedisProperties> multi = multiRedisProperties.getMulti();
        multi.forEach((k, v) -> {
            LettuceConnectionConfiguration lettuceConnectionConfiguration = new LettuceConnectionConfiguration(
                    v,
                    sentinelConfigurationProvider,
                    clusterConfigurationProvider
            );
            LettuceConnectionFactory lettuceConnectionFactory = lettuceConnectionConfiguration.redisConnectionFactory(builderCustomizers, clientResources);
            connectionFactoryMap.put(k, lettuceConnectionFactory);
        });
        return new MultiRedisConnectionFactory(connectionFactoryMap);
    }

}

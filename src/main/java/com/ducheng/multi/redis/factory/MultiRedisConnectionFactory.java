package com.ducheng.multi.redis.factory;

import com.ducheng.multi.redis.autoconf.MultiRedisProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.ObjectUtils;
import java.util.Map;

public class MultiRedisConnectionFactory
        implements InitializingBean, DisposableBean, RedisConnectionFactory, ReactiveRedisConnectionFactory {
    private final Map<String, LettuceConnectionFactory> connectionFactoryMap;

    /**
     *
     * 当前redis的名字
     *
     */
    public static final ThreadLocal<String> currentRedisName = new ThreadLocal<>();


    public MultiRedisConnectionFactory(Map<String, LettuceConnectionFactory> connectionFactoryMap) {
        this.connectionFactoryMap = connectionFactoryMap;
    }

    public void setCurrentRedis(String currentRedisName) {
        if (!connectionFactoryMap.containsKey(currentRedisName)) {
            throw new RuntimeException("invalid currentRedis: " + currentRedisName + ", it does not exists in configuration");
        }
        MultiRedisConnectionFactory.currentRedisName.set(currentRedisName);
    }

    @Override
    public void destroy() throws Exception {
        connectionFactoryMap.values().forEach(LettuceConnectionFactory::destroy);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connectionFactoryMap.values().forEach(LettuceConnectionFactory::afterPropertiesSet);
    }

    private LettuceConnectionFactory currentLettuceConnectionFactory() {
        String currentRedisName = MultiRedisConnectionFactory.currentRedisName.get();
        if (!ObjectUtils.isEmpty(currentRedisName)) {
            return connectionFactoryMap.get(currentRedisName);
        }
        return connectionFactoryMap.get(MultiRedisProperties.DEFAULT);
    }

    @Override
    public ReactiveRedisConnection getReactiveConnection() {
        return currentLettuceConnectionFactory().getReactiveConnection();
    }

    @Override
    public ReactiveRedisClusterConnection getReactiveClusterConnection() {
        return currentLettuceConnectionFactory().getReactiveClusterConnection();
    }

    @Override
    public RedisConnection getConnection() {
        return   currentLettuceConnectionFactory().getConnection();
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return currentLettuceConnectionFactory().getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return currentLettuceConnectionFactory().getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return currentLettuceConnectionFactory().getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return currentLettuceConnectionFactory().translateExceptionIfPossible(ex);
    }

}

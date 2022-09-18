package com.ducheng.multi.redis.autoconf;


import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;


@ConfigurationProperties(prefix = "spring.redis")
public class MultiRedisProperties {
    /**
     * 默认连接必须配置，配置 key 为 default
     */
    public static final String DEFAULT = "default";

    private boolean enableMulti = false;


    private Map<String, RedisProperties> multi;

    public boolean isEnableMulti() {
        return enableMulti;
    }

    public void setEnableMulti(boolean enableMulti) {
        this.enableMulti = enableMulti;
    }

    public Map<String, RedisProperties> getMulti() {
        return multi;
    }

    public void setMulti(Map<String, RedisProperties> multi) {
        this.multi = multi;
    }

    public MultiRedisProperties() {
    }
}

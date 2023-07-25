package com.ducheng.multi.redis.aop;

import com.ducheng.multi.redis.annotation.RdbSelect;
import com.ducheng.multi.redis.factory.MultiRedisConnectionFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.util.ObjectUtils;

public class MuLtiRedisInterceptor implements MethodInterceptor {

    private final MultiRedisConnectionFactory multiRedisConnectionFactory;

    public MuLtiRedisInterceptor(MultiRedisConnectionFactory multiRedisConnectionFactory) {
        this.multiRedisConnectionFactory = multiRedisConnectionFactory;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> cls = AopProxyUtils.ultimateTargetClass(invocation.getThis());
        if (!cls.equals(invocation.getThis().getClass())) {
            return invocation.proceed();
        }
        RdbSelect  rdbSelect = invocation.getMethod().getAnnotation(RdbSelect.class);
        if (!ObjectUtils.isEmpty(rdbSelect)) {
            multiRedisConnectionFactory.setCurrentRedis(rdbSelect.dataSource());
        }
        Object proceed;
        try {
             proceed = invocation.proceed();
        }finally {
            //一定要释放
            MultiRedisConnectionFactory.currentRedisName.remove();
        }
        //核心方法不变
        return proceed;
    }
}

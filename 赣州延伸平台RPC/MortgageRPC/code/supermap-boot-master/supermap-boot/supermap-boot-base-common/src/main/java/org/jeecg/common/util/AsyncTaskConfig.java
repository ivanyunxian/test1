package org.jeecg.common.util;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @ClassName AsyncTaskConfig
 * @Description 异步请求配置类
 * @Author taochunda
 * @Date 2019-08-30 10:11
 */
@Configuration
@ComponentScan("org.jeecg.modules.mortgagerpc.task")
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer {

    // 线程池维护线程的最少数量
    private static final int corePoolSize = 5;
    // 线程池维护线程的最大数量
    private static final int maxPoolSize = 10;
    // 缓存队列
    private static final int queueCapacity = 25;
    // 允许的空闲时间
    private static final int keepAliveSeconds = 200;

    /**
     * @Author taochunda
     * @Description 配置类实现AsyncConfigurer接口并重写 getAsyncExecutor 方法,
     *              并返回一个 ThreadPoolTaskExecutor,这样我们就获得了一个线程池 taskExecutor
     * @Date 2019-08-30 10:13
     * @Param []
     * @return java.util.concurrent.Executor
     **/
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.initialize();

        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}

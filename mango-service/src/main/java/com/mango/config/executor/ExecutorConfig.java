package com.mango.config.executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 *
 */
@Configuration
@EnableAsync
public class ExecutorConfig implements AsyncConfigurer {

    /**
     * The {@link Executor} instance to be used when processing async
     * method invocations.
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(15);
        // 配置最大线程数
        executor.setMaxPoolSize(50);
        // 线程池所使用的缓冲队列大小
        executor.setQueueCapacity(15);
        // 等待任务在关机时完成，表明等待所有线程执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间（默认为0，此时立即停止），并没等待xx秒后强制停止
        executor.setAwaitTerminationSeconds(60);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("AppExecutor-");

        // 当 Pool 已经达到 MaxPoolSize 的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是使用调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();

        return executor;
    }

    /**
     * The {@link AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
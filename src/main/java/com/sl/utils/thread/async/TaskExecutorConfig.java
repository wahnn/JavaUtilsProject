package com.sl.utils.thread.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *初始化线程池 gaoxueyong
 */
@Configuration
@EnableAsync //利用@EnableAsync注解开启异步任务支持
public class TaskExecutorConfig {
    @Bean("taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);//线程池维护线程的最少数量
        taskExecutor.setMaxPoolSize(30);//线程池维护线程的最大数量
        taskExecutor.setQueueCapacity(50);//线程池所使用的缓冲队列
        taskExecutor.setThreadNamePrefix("taskExecutor-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(300);
        taskExecutor.initialize();
        return taskExecutor;
    }


}

package com.sl.utils.scheduler;

import com.sl.utils.thread.async.ThreadAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class scheduler {

    @Autowired
    private ThreadAsync threadAsync;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void test() throws InterruptedException {
        threadAsync.AsyncTestOne();
        threadAsync.AsyncTestTwo();
        System.out.println("哈哈！");
    }
}

package com.sl.utils.thread.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Future;

@Configuration
@EnableAsync
public class ThreadAsync {

    @Async
    public Future<String> AsyncTestOne() throws InterruptedException {
        for(int i=0;i<5;i++){
            System.out.println("one ==> "+i );
            Thread.sleep(2000);
        }

        return new AsyncResult<>("任务一完成");
    }


    @Async
    public Future<String>  AsyncTestTwo() throws InterruptedException {
        for(int i=0;i<5;i++){
            System.out.println("two ==> "+i );
            Thread.sleep(2000);
        }
        return new AsyncResult<>("任务一完成");
    }

//    public static void main(String[] args) throws InterruptedException {
//        ThreadAsync threadAsync = new ThreadAsync();
//        threadAsync.AsyncTestOne();
//        threadAsync.AsyncTestTwo();
//        System.out.println("哈哈！");
//    }
}

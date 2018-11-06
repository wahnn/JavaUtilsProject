package com.sl.utils;

import com.sl.utils.thread.async.ThreadAsync;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

    @Autowired
    private ThreadAsync threadAsync;

    @Test
    public void AsyncThreadTest() throws InterruptedException {
        Future<String> one = threadAsync.AsyncTestOne();
        Future<String> two = threadAsync.AsyncTestTwo();
        while (true) {

            if (one.isDone() && two.isDone()) {
                System.out.println("任务已完成！");
                break;
            }
        }
    }



}

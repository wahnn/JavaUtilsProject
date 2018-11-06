package com.sl.utils.thread;

import com.sl.utils.thread.task.MyTask;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池demo 二
 * 推荐使用这个 可以避免资源被耗尽
 */
public class ThreadPool {
    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
//        ThreadPoolExecutor executor  = new ThreadPoolExecutor(5, 10, 200,
//                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.AbortPolicy());

        try {
            for (int i = 0; i < 20; i++) {
                MyTask myTask = new MyTask(i);
                executor.execute(myTask);
//                Future feature = executor.submit(myTask);

                System.out.println("线程池中线程数量:TaskExecutorConfig" + executor.getPoolSize() + " 队列中等待执行的数量:" +
                        executor.getQueue().size() + ", 已经执行完毕的任务数量:" + executor.getCompletedTaskCount() + " 时刻:" + new Date());
            }

        } finally {
            executor.shutdown();
        }


    }
}

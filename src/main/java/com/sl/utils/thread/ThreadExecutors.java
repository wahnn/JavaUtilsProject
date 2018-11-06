package com.sl.utils.thread;

import com.sl.utils.thread.task.MyTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池demo
 */
public class ThreadExecutors {
    public static void main(String[] args) {



//        Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
//        Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
        ExecutorService service = Executors.newFixedThreadPool(5);    //创建固定容量大小的缓冲池

        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            service.execute(myTask);

//            System.out.println("线程池中线程数量:"+ obj.+" 队列中等待执行的数量:"+
//            executor.getQueue().size()+", 已经执行完毕的任务数量:" + executor.getCompletedTaskCount());
        }
        service.shutdown();

    }

}

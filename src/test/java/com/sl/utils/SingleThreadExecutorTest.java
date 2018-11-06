package com.sl.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description(这里用一句话描述这个方法的作用)
 * author: Gao Xueyong
 * Create at: 2018/11/2 17:28
 */
public class SingleThreadExecutorTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
         service = Executors.newFixedThreadPool(6);
        for(int i=0;i<5;i++){
            SingleThreadTask st = new SingleThreadTask(i);
            service.execute(st);
        }
        service.shutdown();

    }
}


class SingleThreadTask implements Runnable{
    private  int index ;

    public SingleThreadTask(int index){
        this.index = index;
    }

    @Override
    public void run() {
        System.out.println("线程》 "+Thread.currentThread().getName()+" index 》"+index+"开始启动!");
    }
}
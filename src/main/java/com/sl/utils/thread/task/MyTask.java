package com.sl.utils.thread.task;

import java.util.Date;

/**
 * task 线程
 */
public class MyTask implements Runnable {

    private int taskNum;




    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task " + taskNum + " 时刻:" + new Date());
        try {
            Thread.currentThread().sleep(2000);

            // xie luo jie get(List<Map> list);

        // 获取生成excel的数据  标题、sheet数据
            //






        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task " + taskNum + "执行完毕" + " 时刻:" + new Date());

    }
}

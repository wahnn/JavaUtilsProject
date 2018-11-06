package com.sl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description(这里用一句话描述这个方法的作用)
 * author: Gao Xueyong
 * Create at: 2018/10/31 14:59
 */
public class ThreadTest {

    public static void main(String[] args) {

        ExecutorService service = Executors.newFixedThreadPool(30);
        String type = "green";
        List<Car> listCar = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Car car = new Car("car-" + i, type);
            listCar.add(car);
            service.execute(car);

//            System.out.println("线程池中线程数量:"+ obj.+" 队列中等待执行的数量:"+
//            executor.getQueue().size()+", 已经执行完毕的任务数量:" + executor.getCompletedTaskCount());
        }

        Light light = new Light();
//        红灯
        System.out.println("。。。。。红灯了。。。。。");
        light.waitCar(listCar);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==========================================================");
        for (Car car : listCar) {
            System.out.println("线程》"+car.getName()+"  状态》"+car.getState()+"  isAlive》"+car.isAlive());
        }
        System.out.println("==========================================================");

//        绿灯
        System.out.println("。。。。。绿灯了。。。。。");
        light.nothfyCar(listCar);
        System.out.println("==========================================================");
        for (Car car : listCar) {
            //所有存活的线程进入等待状态
            System.out.println("线程》"+car.getName()+"  状态》"+car.getState()+"  isAlive》"+car.isAlive());
        }
        System.out.println("==========================================================");
        service.shutdown();

    }
}


class Car extends Thread {

    private String type;
    private String carName;


    public Car(String carName, String type) {
        this.carName = carName;
        this.type = type;
    }

    @Override
    public void run() {
        if(this.getState() == State.RUNNABLE){
            System.out.println("车辆 》" + Thread.currentThread().getName() + "正在行驶");
        }else if(this.getState() == State.BLOCKED){

        }else if(this.getState() == State.WAITING){
            System.out.println("车辆 》" + Thread.currentThread().getName() + "正在等红灯");

        }else if(this.getState() == State.TIMED_WAITING){

        }else if(this.getState() == State.TERMINATED){

        }else if(this.getState() == State.NEW){
            System.out.println("车辆 》" + Thread.currentThread().getName() + "正在行驶");
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Light {

//    private String light_red = "red";
//    private String light_green = "green";

//    public synchronized void run(Thread thread, String type) {
//        if (thread != null) {
//            if (StringUtils.equals(light_green, type)) {
//                System.out.println("车辆 》" + Thread.currentThread().getName() + "正在行驶");
//                thread.notify();
//            } else if (StringUtils.equals(light_green, type)) {
//                try {
//                    thread.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                thread.notify();
//            }
//        }
//    }

    public synchronized void waitCar(List<Car> listCar) {
        for (Car car : listCar) {
            //所有存活的线程进入等待状态
            if(car.getState() != Thread.State.TERMINATED){
                try {
                    car.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public synchronized void nothfyCar(List<Car> listCar) {
        for (Car car : listCar) {
            //所有存活的线程进入等待状态
            if(car.getState() != Thread.State.TERMINATED) {
                try {
                    car.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
package com.sl.utils;

import com.sl.utils.thread.ThreadAsync;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

    @Autowired
    private ThreadAsync threadAsync;
    @Test
    public void contextLoads() throws InterruptedException {
//        Future<String> one =  threadAsync.AsyncTestOne();
//        Future<String> two = threadAsync.AsyncTestTwo();
//        if(one.isDone() && two.isDone()){
//            System.out.println("任务已完成！");
//        }
//        System.out.println("哈哈！");

        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map = new HashMap<String, String>();
        map.put("id","1");
        list.add(map);
        for(Map<String,String> m:list){
            m.put("name","小米");
        }
        for(Map<String,String> m:list){
            for(String key:m.keySet()){
                System.out.println("key ==> "+key);
            }
        }
    }

}

package com.sl.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Description(这里用一句话描述这个方法的作用)
 * author: Gao Xueyong
 * Create at: 2018/11/5 14:22
 */
public class AryLinkedListTest {
    public static void main(String[] args) {

        List<Integer> list = new LinkedList<>();
        for(int i=0;i<10;i++){
            list.add(i);
        }
        list.forEach(i -> System.out.println(i));
    }
}

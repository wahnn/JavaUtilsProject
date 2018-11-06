package com.sl.utils;

import org.json.JSONObject;

/**
 * Description(这里用一句话描述这个方法的作用)
 * author: Gao Xueyong
 * Create at: 2018/10/31 14:47
 */
public class NumTest {


    public static void main(String[] args) {

//        int[] numArray = {1,2,3,4,5,6,7,};
//
//        System.out.println(retRes(numArray,5));


        int[][][] numMoreArray = new int[5][6][7];
        numMoreArray = retMoreRes(numMoreArray);
        System.out.println(numMoreArray.length);
        System.out.println(numMoreArray[0].length);
        System.out.println(numMoreArray[0][0].length);
    }


    public static boolean retRes(int [] numArray,int res){

        boolean flag = false;
        for(int i=0;i<numArray.length;i++){
            for(int j=0;i<numArray.length;j++){
                if(numArray[i]+numArray[j]==res){
                    System.out.println("numArray[i] >>"+numArray[i]);
                    System.out.println("numArray[j] >>"+numArray[j]);
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }
        return flag;
    }


    public static int[][][] retMoreRes(int[][][] numMoreArray){
        String numStr = numMoreArray.length+""+numMoreArray[0].length+""+numMoreArray[0][0].length;
        System.out.println("numStr >>"+numStr);
        Integer newNum = Integer.valueOf(numStr)+1;
        System.out.println("newNum >>"+newNum);
        System.out.println("newNum.toString() >>"+newNum.toString());
        char[] newNumStr = (newNum.intValue()+"").toCharArray();
        System.out.println("newNumStr >>"+newNumStr);
        System.out.println("----------------------");
        for(char a:newNumStr){
            System.out.println(">>"+a);
        }
        System.out.println("newNumStr[0] >"+newNumStr[0]);
        System.out.println("newNumStr[1] >"+newNumStr[1]);
        System.out.println("newNumStr[2] >"+newNumStr[2]);
        return new int[newNumStr[0]][newNumStr[1]][newNumStr[2]];

    }




}

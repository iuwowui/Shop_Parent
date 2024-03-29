package com.qfedu.common.util;

import java.util.Random;

public class CodeUtil {
    // 生成指定位数的随机数 len = 4 生成4位数字
    public static int createCode(int len){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 1; i <= len; i++){
            buffer.append(random.nextInt(i));
        }
        return Integer.parseInt(buffer.toString());
    }

    public static int createNum(int len){
        Random random = new Random();
        // len=4 10^4 - 10^3=9000  0-8999   + 1000 = 1000-9999
        return (int)(random.nextInt((int)(Math.pow(10,len) - Math.pow(10,len - 1))) + Math.pow(10,len - 1));
    }

    public static void main(String[] args) {
        for (int i=1;i<10001;i++){
            int n=createNum(4);
            if (n<1000 || n>= 10000){
                System.out.println(n);
            }
        }
    }
}

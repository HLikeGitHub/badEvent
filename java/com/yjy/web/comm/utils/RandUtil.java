package com.yjy.web.comm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 随机数工具类
 * @info RandUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class RandUtil {

    /**
     * 生成一个带时间信息的16位随机数
     * @return HHmmssSSS????????
     */
    public static String getTimeRandNum16(){
        String rand16 = new SimpleDateFormat("HHmmssSSS").format(new Date());
        rand16 += (int)((Math.random() * 9 + 1) * 1000000);
        return rand16;
    }

    /**
     * 生成n位随机数
     * @param n 随机数个数
     * @return ?[n]
     */
    public static String getRandNum(int n) throws IllegalArgumentException{
        if(n < 0){
            throw new IllegalArgumentException("n is negative!");
        }else if(n == 0){
            return "";
        }

        int left = 0;
        if(n > 19){
            left = n - 19;
            n = 19;
        }

        n -= 1;
        if(n == -1){
            n = 0;
        }

        String rand = String.valueOf((long)((Math.random() * 9 + 1) * Math.pow(10, n)));
        if(left != 0){
            rand += getRandNum(left);
        }
        return rand;
    }
}

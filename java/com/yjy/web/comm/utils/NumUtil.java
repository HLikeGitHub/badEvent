package com.yjy.web.comm.utils;

/**
 * @info NumUti
 * @author rwx
 * @email aba121mail@qq.com
 */
public class NumUtil {

    public static long parseLong(String num){
        long l = 0;
        if(num == null || num.isEmpty()){
            return l;
        }

        try {
            l = Long.parseLong(num);
        }catch (Exception e){
            e.printStackTrace();
        }
        return l;
    }

    public static float parseFloat(String num){
        float f = 0;
        if(num == null || num.isEmpty()){
            return f;
        }

        try {
            f = Float.parseFloat(num);
        }catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }
}

package com.yjy.web.comm.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * @info RegexUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class RegexUtil {

    /**
     * 校验号码是否为手机号码
     * @param cellPhone
     * @return
     */
    public static boolean isCellPhone(String cellPhone){
        boolean match = false;
        try{
            match = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]\\d{8}$").matcher(cellPhone).matches();
        }catch (Exception e){
            e.printStackTrace();
        }
        return match;
    }
}

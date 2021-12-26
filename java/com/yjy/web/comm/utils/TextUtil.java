package com.yjy.web.comm.utils;

import java.text.DecimalFormat;

/**
 * 文本工具类
 * @info TextUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class TextUtil {

    public static boolean isNullOrEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static boolean isNullOrEmpty(String...strs){
        if(strs == null || strs.length == 0 || "null".equals(strs)){
            return true;
        }
        for(String str : strs){
            if(isNullOrEmpty(str)){
                return true;
            }
        }
        return false;
    }

    public static String getIdCardBirthday(String idCard){
        if(idCard == null || idCard.length() != 18){
            return "";
        }
        String birthday = idCard.substring(6, 14);
        birthday = birthday.substring(0,4)+"-"+birthday.substring(4,6)+"-"+birthday.substring(6,8);
        return birthday;
    }

    public static String formatFileSize(long byteLength) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String size = "0B";
        if (byteLength == 0) {
            return size;
        }
        if (byteLength < 1024) {
            size = decimalFormat.format((double) byteLength) + "B";
        } else if (byteLength < 1048576) {
            size = decimalFormat.format((double) byteLength / 1024) + "KB";
        } else if (byteLength < 1073741824) {
            size = decimalFormat.format((double) byteLength / 1048576) + "MB";
        } else {
            size = decimalFormat.format((double) byteLength / 1073741824) + "GB";
        }
        return size;
    }

    /**
     * 手机掩码
     * @param phone
     * @return 138*****138
     */
    public static String maskPhone(String phone){
        if(phone == null || phone.isEmpty()){
            return "";
        }

        return phone.substring(0, 3) + "*****" + phone.substring(8);
    }

}

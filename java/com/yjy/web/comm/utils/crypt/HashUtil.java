package com.yjy.web.comm.utils.crypt;

import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;

/**
 * @info HashUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class HashUtil {

    /**
     * md5
     * @param plainText
     * @return
     */
    public static String md5(String plainText){
        if(plainText == null || plainText.isEmpty()){
            return "";
        }

        return DigestUtils.md5DigestAsHex(plainText.getBytes(Charset.forName("UTF-8")));
    }
}

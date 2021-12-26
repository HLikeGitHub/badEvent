package com.yjy.web.comm.utils.crypt.aes;

import org.springframework.core.MethodParameter;

/**
 * @info CryptoAnnotationUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class CryptoAnnotationUtil {

    /**
     * 是否需要对结果加密
     * 1.类上标注或者方法上标注,并且都为true
     * 2.有一个标注为false就不需要加密
     */
    public static boolean need2Encrypt(MethodParameter methodParameter) {
        boolean need2Encrypt = false;

//        //类上标注的是否需要加密
//        boolean isClassAnnotationPresent  = methodParameter.getContainingClass().isAnnotationPresent(EncryptResponse.class);
//        if(isClassAnnotationPresent){
//            need2Encrypt = methodParameter.getContainingClass().getAnnotation(EncryptResponse.class).value();
//            //类不加密，所有都不加密
//            if(!need2Encrypt){
//                return false;
//            }
//        }

        //方法上标注的是否需要加密
        boolean isMethodAnnotationPresent = methodParameter.getMethod().isAnnotationPresent(EncryptResponse.class);
        if(isMethodAnnotationPresent){
            need2Encrypt = methodParameter.getMethod().getAnnotation(EncryptResponse.class).value();
        }
        return need2Encrypt;
    }

    /**
     * 是否需要参数解密
     * 1.类上标注或者方法上标注,并且都为true
     * 2.有一个标注为false就不需要解密
     */
    public static boolean need2Decrypt(MethodParameter methodParameter) {
        boolean need2Decrypt = false;

//        //类上标注的是否需要解密
//        boolean isClassAnnotationPresent  = methodParameter.getContainingClass().isAnnotationPresent(DecryptRequest.class);
//        if(isClassAnnotationPresent){
//            need2Decrypt = methodParameter.getContainingClass().getAnnotation(DecryptRequest.class).value();
//            //类不解密，所有都不解密
//            if(!need2Decrypt){
//                return false;
//            }
//        }

        //方法上标注的是否需要解密
        boolean isMethodAnnotationPresent = methodParameter.getMethod().isAnnotationPresent(DecryptRequest.class);
        if(isMethodAnnotationPresent){
            need2Decrypt = methodParameter.getMethod().getAnnotation(DecryptRequest.class).value();
        }
        return need2Decrypt;
    }
}

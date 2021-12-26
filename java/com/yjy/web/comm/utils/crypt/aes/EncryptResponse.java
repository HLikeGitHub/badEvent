package com.yjy.web.comm.utils.crypt.aes;

import java.lang.annotation.*;

/**
 * @info EncryptResponse
 * @author rwx
 * @email aba121mail@qq.com
 */
@Target({ ElementType.METHOD/*, ElementType.TYPE*/ })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptResponse {
    boolean value() default true;
}

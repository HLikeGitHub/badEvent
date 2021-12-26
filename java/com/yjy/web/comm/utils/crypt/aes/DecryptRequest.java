package com.yjy.web.comm.utils.crypt.aes;

import java.lang.annotation.*;

/**
 * @info DecryptRequest
 * @author rwx
 * @email aba121mail@qq.com
 */
@Target({ ElementType.METHOD/*, ElementType.TYPE*/ })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptRequest {
    boolean value() default true;
}
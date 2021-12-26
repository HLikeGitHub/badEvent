package com.yjy.web.comm.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author rwx
 * @info RedisUtil
 * @email aba121mail@qq.com
 */
public class RedisUtil {

    /**
     * 取值后是否删除
     * @param stringRedisTemplate
     * @param key
     * @param del 取值后是否删除
     * @return
     */
    public static String getStr(StringRedisTemplate stringRedisTemplate, String key, boolean del){
        if(stringRedisTemplate == null){
            return "";
        }

        String value = stringRedisTemplate.opsForValue().get(key);
        if(del){
            stringRedisTemplate.delete(key);
        }

        return value;
    }

    /**
     * 删除String
     * @param stringRedisTemplate
     * @param key
     */
    public static void delStr(StringRedisTemplate stringRedisTemplate, String key){
        if(stringRedisTemplate == null || key == null || key.isEmpty()){
            return;
        }

        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if(hasKey != null && hasKey){
            stringRedisTemplate.delete(key);
        }
    }

}

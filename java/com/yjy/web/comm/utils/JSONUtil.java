package com.yjy.web.comm.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * @info JSONUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class JSONUtil {
    /**
     * Object parse to JSON
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String toJSON(T object) {
        return JSON.toJSONString(object);
    }

    /**
     * JSON parse to Object
     * @param string
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String string, Class<T> clz) {
        return JSON.parseObject(string, clz);
    }

    /**
     * xml转JSONObject
     * @param xml
     * @return
     */
    public static JSONObject xml2JSON(String xml){
        if(xml != null){
            try {
                xml = new XMLSerializer().read(xml).toString();
                return JSONObject.parseObject(xml);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}

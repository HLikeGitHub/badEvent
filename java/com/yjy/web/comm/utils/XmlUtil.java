package com.yjy.web.comm.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @info XmlUtils
 * @author rwx
 * @email aba121mail@qq.com
 */
public class XmlUtil {
	 /**
     * XML转对象
     * @param clazz 对象类
     * @param xml xml字符串
     * @param <T> T
     * @return
     */
     @SuppressWarnings("unchecked")
    public static <T> T xml2Obj(Class<T> clazz, String xml) {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(clazz);
        xStream.autodetectAnnotations(true);
        return (T)xStream.fromXML(xml);
    }

    /**
     * 对象转xml
     * @param obj 对象
     * @return
     */
    public static String obj2Xml(Object obj) {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);
    }
}

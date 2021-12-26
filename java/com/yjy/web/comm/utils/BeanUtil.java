package com.yjy.web.comm.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 用于在非组件类中获取Bean
 * @info ApplicationContextHolder
 * @author rwx
 * @email aba121mail@qq.com
 */
@Component
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 服务器启动，Spring容器初始化时，当加载了当前类为bean组件后，
     * 将会调用下面方法注入ApplicationContext实例
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 外部调用这个getBean方法就可以手动获取到bean
     * @param beanName 用bean组件的name来获取bean
     * @return
     */
    public static <T>T getBean(String beanName){
        return (T)applicationContext.getBean(beanName);
    }

    /**
     * 外部调用这个getBean方法就可以手动获取到bean
     * @param clazz 用bean组件的类来获取bean
     * @return
     */
    public static <T>T getBean(Class<T> clazz){
        return (T)applicationContext.getBean(clazz);
    }
}

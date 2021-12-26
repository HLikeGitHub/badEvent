package com.yjy.web.comm.weixinqyh;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class WeiXinQyhScheduler {
    private static final SimpleDateFormat dateFormat= new SimpleDateFormat("HH:mm:ss");
//    @Scheduled(fixedRate= 2000)
//    public void testTasks() {
//    	System.out.println("定时器执行任务："+dateFormat.format(new Date()));
//    }
}

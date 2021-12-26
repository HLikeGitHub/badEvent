package com.yjy.web.comm.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求拦截器
 */
@Configuration
public class ReqInterceptor implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	 registry.addInterceptor(new ReqRecInterceptor()).addPathPatterns("/**")
                 //静态资源
                 .excludePathPatterns("/css/**")
                 .excludePathPatterns("/js/**")
                 .excludePathPatterns("/image/**")
                 .excludePathPatterns("/images/**")
                 .excludePathPatterns("/bootstrap/**")
                 .excludePathPatterns("/bootstrapztree/**")
                 .excludePathPatterns("/easyui/**")
                 .excludePathPatterns("/inspinia/**")
                 .excludePathPatterns("/plugins/**")
                 .excludePathPatterns("/yjy/**")
                 
                 //api
                 .excludePathPatterns("/comm/**")
                 .excludePathPatterns("/error")
                 .excludePathPatterns("/logins")
                 .excludePathPatterns("/captcha")
                 .excludePathPatterns("/forpub");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

    public static String getLoginUser(String ipaddress) {
        Map<String,String> map=new HashMap<String,String>();
//        map.put("name","123");
//        map.put("password","123");
        URI uri = UriComponentsBuilder.fromHttpUrl("http://192.168.62.101:8080/OA/zxyy/getloginuser")
                .queryParam("ipaddress",ipaddress)
//                .queryParam("token","12122222111")
                .build().encode().toUri();
        RestTemplate restTemplate=new RestTemplate();
        String data=restTemplate.getForObject(uri,String.class);
        System.out.println(data);
        return data;
    }
}
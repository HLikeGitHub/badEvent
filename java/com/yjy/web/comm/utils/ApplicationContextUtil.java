package com.yjy.web.comm.utils;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

public class ApplicationContextUtil {

	public static  <T>T getBean(Class<T> clazz, HttpServletRequest request) {
		WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		return wc.getBean(clazz);
	}
}

package com.yjy.web.comm.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @info ContextUtil
 * @author rwx
 * @email aba121mail@qq.com
 */
public class ContextUtil {

    /**
     * 获取war包tomcat给项目的上下文路径
     * @param request
     * @return "/包名"
     */
    public static String getPath(HttpServletRequest request){
        String path = request.getContextPath();
        if(path == null || path.isEmpty()){
            path = "";
        }
        return path + "/";
    }
}

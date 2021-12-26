package com.yjy.web.comm.utils.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rwx
 * @info desc
 * @mail aba121mail@qq.com
 */
@Component
public class AppPropUtil {

    private static AppProp instance;

    public static AppProp getInstance(){
        return instance;
    }

    @Autowired
    public void init(AppProp appProp) {
        AppPropUtil.instance = appProp;
    }
}
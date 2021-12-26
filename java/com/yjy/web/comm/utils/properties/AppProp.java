package com.yjy.web.comm.utils.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @info ApplicationProperties
 * @author rwx
 * @mail aba121mail@qq.com
 */
@Component
@ConfigurationProperties(prefix = "my.profiles")
public class AppProp {
    private boolean dev;

    private String aes;

    public boolean isDev() {
        return dev;
    }

    public void setDev(boolean dev) {
        this.dev = dev;
    }

    public String getAes() {
        return aes;
    }

    public void setAes(String aes) {
        this.aes = aes;
    }
}
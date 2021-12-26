package com.yjy.web.comm.conf.db;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author rwx
 * @desc 数据源配置
 * @email aba121mail@qq.com
 */
@Configuration
public class DataSourceConfig {

    @Bean("mmsDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mmsDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("appDataSource")
    @ConfigurationProperties(prefix = "spring.app.datasource")
    public DataSource appDataSource(){
        return DataSourceBuilder.create().build();
    }

}

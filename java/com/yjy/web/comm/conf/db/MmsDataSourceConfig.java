package com.yjy.web.comm.conf.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author rwx
 * @desc MMS数据源配置
 * @email aba121mail@qq.com
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "mmsEntityManagerFactory", transactionManagerRef = "mmsTransactionManager", basePackages = {"com.yjy.web.mms"})
public class MmsDataSourceConfig {

    @Autowired
    @Qualifier("mmsDataSource")
    private DataSource mmsDataSource;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Autowired
    private JpaProperties jpaProperties;

    @Primary
    @Bean("mmsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mmsLocalContainerEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(mmsDataSource)
                .properties(properties)
                .packages("com.yjy.web.mms")
                .persistenceUnit("mmsPersistenceUnit")
                .build();

    }

    @Primary
    @Bean("mmsTransactionManager")
    public PlatformTransactionManager mmsTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(mmsLocalContainerEntityManagerFactoryBean(builder).getObject());
    }

    @Primary
    @Bean("mmsEntityManager")
    public EntityManager mmsEntityManager(EntityManagerFactoryBuilder builder) {
        return mmsLocalContainerEntityManagerFactoryBean(builder).getObject().createEntityManager();
    }
}

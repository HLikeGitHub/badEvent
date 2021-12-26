package com.yjy.web.comm.conf.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * @desc APP数据源配置
 * @email aba121mail@qq.com
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "appEntityManagerFactory", transactionManagerRef = "appTransactionManager", basePackages = {"com.yjy.web.app"})
public class AppDataSourceConfig {

    @Autowired
    @Qualifier("appDataSource")
    private DataSource appDataSource;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean("appEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean appLocalContainerEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(appDataSource)
                .properties(properties)
                .packages("com.yjy.web.app")
                .persistenceUnit("appPersistenceUnit")
                .build();

    }

    @Bean("appTransactionManager")
    public PlatformTransactionManager appTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(appLocalContainerEntityManagerFactoryBean(builder).getObject());
    }

    @Bean("appEntityManager")
    public EntityManager appEntityManager(EntityManagerFactoryBuilder builder) {
        return appLocalContainerEntityManagerFactoryBean(builder).getObject().createEntityManager();
    }
}

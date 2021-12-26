package com.yjy.web.app.service.comm;

import com.yjy.web.app.model.dao.comm.AppConfigDao;
import com.yjy.web.app.model.entity.comm.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @desc 系统配置业务
 * @author rwx
 * @email aba121mail@qq.com
 */
@Service
@Transactional
public class AppConfigService {

//    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppConfigDao appConfigDao;

    public AppConfig find(Long id){
        return appConfigDao.findByIdAndEnabled(id, true);
    }

    public AppConfig findByName(String name){
        return appConfigDao.findByNameAndEnabled(name, true);
    }

    public List<AppConfig> find(List<Long> ids){
        return appConfigDao.findByIdInAndEnabled(ids, true);
    }

    public List<AppConfig> find(){
        return appConfigDao.findByEnabled(true);
    }

    public AppConfig save(AppConfig appConfig){
        return appConfigDao.save(appConfig);
    }

    public List<AppConfig> save(List<AppConfig> appConfigs){
        return appConfigDao.saveAll(appConfigs);
    }

    public void delete(AppConfig appConfig){
        appConfig.setEnabled(false);
        save(appConfig);
    }

    public void delete(List<AppConfig> appConfigs){
        appConfigs.forEach(appConfig -> appConfig.setEnabled(false));
        save(appConfigs);
    }

    public void delete(){
        List<AppConfig> appConfigs = find();
        appConfigs.forEach(appConfig -> appConfig.setEnabled(false));
        save(appConfigs);
    }
}
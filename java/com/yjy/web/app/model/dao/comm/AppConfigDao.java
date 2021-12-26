package com.yjy.web.app.model.dao.comm;

import com.yjy.web.app.model.entity.comm.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc AppConfigDao
 * @author rwx
 * @email aba121mail@qq.com
 */
public interface AppConfigDao extends JpaRepository<AppConfig, Long> {

    AppConfig findByIdAndEnabled(long id, boolean enabled);

    AppConfig findByNameAndEnabled(String name, boolean enabled);

    List<AppConfig> findByEnabled(boolean enabled);

    List<AppConfig> findByIdInAndEnabled(List<Long> ids, boolean enabled);
}

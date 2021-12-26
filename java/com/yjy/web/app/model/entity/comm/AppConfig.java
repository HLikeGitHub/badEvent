package com.yjy.web.app.model.entity.comm;

import javax.persistence.*;

/**
 * @desc APP配置
 * @author rwx
 * @email aba121mail@qq.com
 */
@Entity
@Table(name = "app_config")
public class AppConfig {
    /**
     * 配置ID
     */
    private long id;
    /**
     * 配置KEY
     * <p>key命名规范应遵循：app_mo_del_func,即：APP_模_块_功能，如：app_print_mr_sms</p>
     */
    private String name;
    /**
     * 配置值
     */
    private String content;
    /**
     * 配置描述
     */
    private String description;
    /**
     * 是否启用
     */
    private boolean enabled;

    public AppConfig(){}

    public AppConfig(String name, String content, String description){
        this.name = name;
        this.content = content;
        this.description = description;
        this.enabled = true;
    }

    public AppConfig(String name, String content, String description, boolean enabled){
        this.name = name;
        this.content = content;
        this.description = description;
        this.enabled = enabled;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "content", columnDefinition="TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update(AppConfig newConfig){
        if(newConfig == null){
            return;
        }

        this.content = newConfig.getContent();
        this.description = newConfig.getDescription();
        this.enabled = newConfig.isEnabled();
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

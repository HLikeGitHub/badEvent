package com.yjy.web.mms.model.entity.process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yjy.web.mms.model.entity.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * 便签表
 * @author 宋佳
 *
 */
@Entity
@Table(name = "aoa_notepaper")
public class Notepaper {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="notepaper_id")
	private Long  notepaperId;			//主键id
	
	private String title;				//便签标题
	
	@Column(columnDefinition="text")	//便签内容
	private String concent;
	
	
	@ManyToOne
	@JoinColumn(name="notepaper_user_id")
	@JsonIgnore
	private User userId;				//编写便签的用户
	
	@Column(name="create_time")
	private Date createTime;			//便签创建时间

	public Long getNotepaperId() {
		return notepaperId;
	}

	public void setNotepaperId(Long notepaperId) {
		this.notepaperId = notepaperId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getConcent() {
		return concent;
	}

	public void setConcent(String concent) {
		this.concent = concent;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Notepaper [notepaperId=" + notepaperId + ", title=" + title + ", concent=" + concent + ", createTime="
				+ createTime + "]";
	}
	
	
	
	
}

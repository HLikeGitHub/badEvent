package com.yjy.web.mms.model.entity.recommend;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yjy.web.mms.model.entity.user.User;

@Entity
@Table(name="aoa_recommend_user")
//推荐人联系表
public class RecommendUser {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="recom_user_id")
	private Long recomUserId;//推荐人ID
	
	
	@Column(name="status")
	private String status;//是否启用

	@Column(name="name")
	private String name;//推荐人姓名
	
	@Column(name="phone")
	private String phone;//推荐人手机号码  选填
	
	@Column(name="star_time")
	private Date starTime;//注册日期

	public Long getRecomUserId() {
		return recomUserId;
	}

	public void setRecomUserId(Long recomUserId) {
		this.recomUserId = recomUserId;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getStarTime() {
		return starTime;
	}

	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	
	
	
	
}

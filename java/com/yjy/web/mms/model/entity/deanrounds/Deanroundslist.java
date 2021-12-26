package com.yjy.web.mms.model.entity.deanrounds;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name="aoa_deanrounds_list")
//任务表
public class Deanroundslist implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="deanrounds_id")
	private Long deanroundsId;//院长查房id主键
	
//	@Column(name="title")
//	private String title;//任务类型（公事，私事）
	
	@Column(name="publish_date")
	@JSONField(format = "yyyy-MM-dd")
	private Date publishDate;//发布时间
	
	@Column(name="cfks")
	private String cfks;//查房科室
	

	
	@Column(name="ksgk")
	private String ksgk;//科室概况
	
	
	@Column(name="cjfx")
	private String cjfx;//差距分析
	
	@Column(name="fzgh")
	private String fzgh;//发展规划
	
	@Column(name="cffzr")
	private String cffzr;//查房负责人

	@Column(name="cfap")
	private String cfap;//查房安排
	
//	@Column(name="cflx")
//	private String cflx;//查房路线
//	
//	@Column(name="hydd")
//	private String hydd;//会议地点
	
	
	public String getCfap() {
		return cfap;
	}

	public String getCjfx() {
		return cjfx;
	}

	public void setCjfx(String cjfx) {
		this.cjfx = cjfx;
	}

	public void setCfap(String cfap) {
		this.cfap = cfap;
	}

	public String getCffzr() {
		return cffzr;
	}

	public void setCffzr(String cffzr) {
		this.cffzr = cffzr;
	}

	public String getKsgk() {
		return ksgk;
	}

	public void setKsgk(String ksgk) {
		this.ksgk = ksgk;
	}

	public String getFzgh() {
		return fzgh;
	}

	public void setFzgh(String fzgh) {
		this.fzgh = fzgh;
	}

	public Long getDeanroundsId() {
		return deanroundsId;
	}

	public void setDeanroundsId(Long deanroundsId) {
		this.deanroundsId = deanroundsId;
	}



	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getCfks() {
		return cfks;
	}

	public void setCfks(String cfks) {
		this.cfks = cfks;
	}

	
	


	
	
}

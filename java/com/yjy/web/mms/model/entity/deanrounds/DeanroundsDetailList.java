package com.yjy.web.mms.model.entity.deanrounds;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="aoa_deanrounds_detail")
//任务表
public class DeanroundsDetailList implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="deanrounds_detail_id")
	private Long deanroundsDetailId;//建议详情ID
	
	@ManyToOne()
	@JoinColumn(name = "dean_id")
	private Deanroundslist deanId;			//外键关联 院长查房主表
	
	@Column(name="xh")
	private int xh;//序号
	
	@Column(name="czwt")
	private String czwt;//存在问题
	
	@Column(name="zbks")
	private String zbks;//主办科室
	
	@Column(name="xbks")
	private String xbks;//协办科室
	
	



	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public Deanroundslist getDeanId() {
		return deanId;
	}

	public void setDeanId(Deanroundslist deanId) {
		this.deanId = deanId;
	}

	public Long getDeanroundsDetailId() {
		return deanroundsDetailId;
	}

	public void setDeanroundsDetailId(Long deanroundsDetailId) {
		this.deanroundsDetailId = deanroundsDetailId;
	}



	public String getCzwt() {
		return czwt;
	}

	public void setCzwt(String czwt) {
		this.czwt = czwt;
	}

	public String getZbks() {
		return zbks;
	}

	public void setZbks(String zbks) {
		this.zbks = zbks;
	}

	public String getXbks() {
		return xbks;
	}

	public void setXbks(String xbks) {
		this.xbks = xbks;
	}
	
}

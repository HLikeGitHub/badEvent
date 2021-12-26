package com.yjy.web.mms.model.entity.aers;

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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class AersAllStatusCount {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; // 字典id
	private Long allCount;	//全部上报数量
	private Long sumDTJ;	//待提交上报数量
	private Long sumYTJ;	//已提交上报数量
	private Long sumYWC;	//已完成上报数量
	private Long sumHT;	//回退上报数量
	private Long sumZF;	//作废上报数量
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAllCount() {
		return allCount;
	}
	public void setAllCount(Long allCount) {
		this.allCount = allCount;
	}
	public Long getSumDTJ() {
		return sumDTJ;
	}
	public void setSumDTJ(Long sumDTJ) {
		this.sumDTJ = sumDTJ;
	}
	public Long getSumYTJ() {
		return sumYTJ;
	}
	public void setSumYTJ(Long sumYTJ) {
		this.sumYTJ = sumYTJ;
	}
	public Long getSumYWC() {
		return sumYWC;
	}
	public void setSumYWC(Long sumYWC) {
		this.sumYWC = sumYWC;
	}
	public Long getSumHT() {
		return sumHT;
	}
	public void setSumHT(Long sumHT) {
		this.sumHT = sumHT;
	}
	public Long getSumZF() {
		return sumZF;
	}
	public void setSumZF(Long sumZF) {
		this.sumZF = sumZF;
	}
	
}

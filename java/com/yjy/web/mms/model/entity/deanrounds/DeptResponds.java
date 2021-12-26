package com.yjy.web.mms.model.entity.deanrounds;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yjy.web.mms.model.entity.user.Dept;

@Entity
@Table(name="aoa_dept_responds")
//任务表
public class DeptResponds implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="respond_id")
	private Long respondId;//回应主键
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="kshy",columnDefinition="longblob",nullable=true)
	private String kshy;//回应内容

	
	


	@Column(name="zgsj")
	private String zgsj;//整改时间
	
	@Column(name="zgqk")
	private String zgqk;//整改情况
	
	@Column(name="wcqk")
	private String wcqk;//完成情况
	
	@Column(name="lsqk")
	private String lsqk;//落实情况
	
	@Column(name="bz")
	private String bz;//备注
	
	@Column(name="pdfname")
	private String pdfname;//建议PDF名称
	
	@Column(name="pdfurl")
	private String pdfurl;//建议PDF地址
	
	
	public String getPdfname() {
		return pdfname;
	}

	public void setPdfname(String pdfname) {
		this.pdfname = pdfname;
	}

	public String getPdfurl() {
		return pdfurl;
	}

	public void setPdfurl(String pdfurl) {
		this.pdfurl = pdfurl;
	}

	@ManyToOne()
	@JoinColumn(name = "dept_id")
	private Dept dept;//外键关联 部门表
	
	@ManyToOne()
	@JoinColumn(name = "dean_detail_id")
	private DeanroundsDetailList deanDetailId;			//外键关联 科室存在问题表

	

	


	public String getKshy() {
		return kshy;
	}

	public void setKshy(String kshy) {
		this.kshy = kshy;
	}

	public String getZgqk() {
		return zgqk;
	}

	public void setZgqk(String zgqk) {
		this.zgqk = zgqk;
	}

	public Long getRespondId() {
		return respondId;
	}

	public void setRespondId(Long respondId) {
		this.respondId = respondId;
	}


	public String getZgsj() {
		return zgsj;
	}

	public void setZgsj(String zgsj) {
		this.zgsj = zgsj;
	}



	public String getWcqk() {
		return wcqk;
	}

	public void setWcqk(String wcqk) {
		this.wcqk = wcqk;
	}

	public String getLsqk() {
		return lsqk;
	}

	public void setLsqk(String lsqk) {
		this.lsqk = lsqk;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public DeanroundsDetailList getDeanDetailId() {
		return deanDetailId;
	}

	public void setDeanDetailId(DeanroundsDetailList deanDetailId) {
		this.deanDetailId = deanDetailId;
	}

	
	
}

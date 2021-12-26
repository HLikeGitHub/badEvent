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

import com.yjy.web.mms.model.entity.user.Dept;

@Entity
@Table(name="aoa_dept_suggestion")
//任务表
public class DeptSuggestion implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="suggestion_id")
	private Long suggestionId;//建议主键
	
	@Column(name="content")
	private String content;//建议内容
	
	@ManyToOne()
	@JoinColumn(name = "dept_id")
	private Dept dept;			//外键关联 部门表
	
	
	@ManyToOne()
	@JoinColumn(name = "dean_id")
	private Deanroundslist deanId;			//外键关联 院长查房主表
	
	
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

	public Deanroundslist getDeanId() {
		return deanId;
	}

	public void setDeanId(Deanroundslist deanId) {
		this.deanId = deanId;
	}

	public Long getSuggestionId() {
		return suggestionId;
	}

	public void setSuggestionId(Long suggestionId) {
		this.suggestionId = suggestionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}


	
	

	
	


	
	
}

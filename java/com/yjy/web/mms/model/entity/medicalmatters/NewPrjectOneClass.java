package com.yjy.web.mms.model.entity.medicalmatters;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.User;

@Entity
@Table(name="aoa_newprject_oneclass")
//主表
public class NewPrjectOneClass{
	
	@Id
	@Column(name="oneclass_Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long oneClassId;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="medicalmatters_id")
	private MedicalMattersList medicalmattersId;
	
	
	@Column(name="projce_Name")
	private String projceName;			//新项目名称
	


	@ManyToOne()
	@JoinColumn(name = "dept_id")
	private Dept dept;			//申请科室
	
	@Column(name="joint_depts")
	private String jointDepts;    //合作科室
	
	@Column(name="joint_persons")
	private String jointPersons;		//项目开展人员名单
	
	@Column(name="is_newequipment")
	private String isNewequipment;		//是否需要新购设备
	
	@Column(name="is_firstinhos")
	private String isFirstinhos;			//是否为院内首次开展
		
	
	
	@Column(name="projce_content")
	private String projceContent;//项目的一般情况介绍及国内外开展的状况、我院开展项目所具备的条件（仪器设备、人员培训、收费）：
	
	
	@Column(name="start_time" )
	private Date startTime;			//计划开展时间
	
	@Column(name="halfYearCount")
	private String halfYearCount;			//预期半年开展例数
	
	@Column(name="is_expertGuidance")
	private String isExpertGuidance;			//首次开展是否请外院专家指导
	
	@Column(name="joint_experts")
	private String jointExperts;			//专家主要人员资

	
	public MedicalMattersList getMedicalmattersId() {
		return medicalmattersId;
	}

	public void setMedicalmattersId(MedicalMattersList medicalmattersId) {
		this.medicalmattersId = medicalmattersId;
	}

	public String getIsExpertGuidance() {
		return isExpertGuidance;
	}

	public void setIsExpertGuidance(String isExpertGuidance) {
		this.isExpertGuidance = isExpertGuidance;
	}
	
	public Long getOneClassId() {
		return oneClassId;
	}

	public void setOneClassId(Long oneClassId) {
		this.oneClassId = oneClassId;
	}

	public String getProjceName() {
		return projceName;
	}

	public void setProjceName(String projceName) {
		this.projceName = projceName;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public String getJointDepts() {
		return jointDepts;
	}

	public void setJointDepts(String jointDepts) {
		this.jointDepts = jointDepts;
	}

	public String getJointPersons() {
		return jointPersons;
	}

	public void setJointPersons(String jointPersons) {
		this.jointPersons = jointPersons;
	}

	public String getIsNewequipment() {
		return isNewequipment;
	}

	public void setIsNewequipment(String isNewequipment) {
		this.isNewequipment = isNewequipment;
	}



	public String getIsFirstinhos() {
		return isFirstinhos;
	}

	public void setIsFirstinhos(String isFirstinhos) {
		this.isFirstinhos = isFirstinhos;
	}

	public String getProjceContent() {
		return projceContent;
	}

	public void setProjceContent(String projceContent) {
		this.projceContent = projceContent;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getHalfYearCount() {
		return halfYearCount;
	}

	public void setHalfYearCount(String halfYearCount) {
		this.halfYearCount = halfYearCount;
	}

	public String getJointExperts() {
		return jointExperts;
	}

	public void setJointExperts(String jointExperts) {
		this.jointExperts = jointExperts;
	}

	
}

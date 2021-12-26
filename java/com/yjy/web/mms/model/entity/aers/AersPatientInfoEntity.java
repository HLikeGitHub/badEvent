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

@Entity
@Table(name="aers_patientinfo")
//主表
public class AersPatientInfoEntity{
	
	@Id
	@Column(name="patinfo_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long patinfoId;
	
	@Column(name="zyh" )
	private String zyh;			//姓名
	
	@Column(name="name" )
	private String name;			//姓名
	
	@Column(name="occupation" )
	private String occupation;			//职业
	
	@Column(name="sex")
	private String sex;		//性别
	
	
	@Column(name="age_round")
	private String ageRound;			//年龄层
	
	@Column(name="birthday")
	private String birthday;			//出生日期
	
	@Column(name="nation")
	private String nation;    //民族
	
	@Column(name="weight")
	private String weight;			//体重
	
	@Column(name="phone")
	private String phone;			//电话
	
	@Column(name="diagnosis")
	private String diagnosis;			//临床诊断
	
	@Column(name="medical_treatment")
	private String medicalTreatment;			//就医类别
	
	@Column(name="department")
	private String department;			//所在科别

	@Column(name="relevantPersonnelOrDept")
	private String relevantPersonnelOrDept;			//相关人员/科室
	
	
	@Column(name="visitTime")
	private String visitTime;			//就诊日期
	

	
	


	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public String getRelevantPersonnelOrDept() {
		return relevantPersonnelOrDept;
	}

	public void setRelevantPersonnelOrDept(String relevantPersonnelOrDept) {
		this.relevantPersonnelOrDept = relevantPersonnelOrDept;
	}



	public Long getPatinfoId() {
		return patinfoId;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setPatinfoId(Long patinfoId) {
		this.patinfoId = patinfoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAgeRound() {
		return ageRound;
	}

	public void setAgeRound(String ageRound) {
		this.ageRound = ageRound;
	}



	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getMedicalTreatment() {
		return medicalTreatment;
	}

	public void setMedicalTreatment(String medicalTreatment) {
		this.medicalTreatment = medicalTreatment;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	


}

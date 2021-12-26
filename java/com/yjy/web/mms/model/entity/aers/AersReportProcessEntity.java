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
@Table(name="aers_report_process")
//主表
public class AersReportProcessEntity{
	
	@Id
	@Column(name="aersreport_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long aersreportId;
	
	@ManyToOne
	@JoinColumn(name="aersreport_user_id")
	private User userId;			//流程申请人
	
//	@Column(name="status_id")
//	private Long statusId;			//流程审核状态 id
	
	@Column(name="status")
	private String status;			//流程审核状态
	
	
	@Column(name="apply_time")
	private Date applyTime;			//流程申请时间
	
	@Column(name="is_checked")
	private Boolean  rejected=false;		//流程是否被驳回
	
	
	@Column(name="applyType")
	private String  applyType;		//申请类型
	
	private String shenuser;
	
	@Column(name="title")
	private String title;
	
	
	
	
	
	@Column(name="happen_time" )
	private Date happenTime;			//事件发生日期时间
	
	@Column(name="happen_place")
	private String happenPlace;		//事件发生场所
	
	
	@Column(name="level")
	private String level;			//事件发生后对患者健康的影响程度
	
	@Column(name="relation_persons")
	private String relationPersons;			//与事件发生过程中有关联的人员
	
	
	
	@Column(name="patientandinfluence")
	private String patientAndInfluence;    //事件发生后受影响的对象及影响程度
	
	@ManyToOne()
	@JoinColumn(name = "patinfo_id")
	private AersPatientInfoEntity patinfoId;			//外键关联 患者信息
	
	
	@Column(name="aersFiles")
	private String aersFiles;			//附件信息
	
	
	
	
	@Column(name="identitycate_reporter")
	private String identitycateReporter;			//上报人员身份类别
	
	@Column(name="ismanager_reporter")
	private String isManagerReporter;			//上报人员是否为行政主管人员
	
	@Column(name="workyears_reporter")
	private String workyearsReporter;			//上报人员工作年限
	
	@Column(name="entryyears_reporter")
	private String entryyearsReporter;			//上报人员进入现职机构年限
	
	@Column(name="education_reporter")
	private String educationReporter;			//上报人员本人学历

	
	@Column(name="dept_manager")
	private String deptManager;			//科室主任
	
	@Column(name="charge_manager")
	private String chargeManager;			//主管部门

	@Column(name="quality_manager")
	private String qualityManager;			//质控科
	
	
	
	
	public String getDeptManager() {
		return deptManager;
	}

	public void setDeptManager(String deptManager) {
		this.deptManager = deptManager;
	}

	public String getChargeManager() {
		return chargeManager;
	}

	public void setChargeManager(String chargeManager) {
		this.chargeManager = chargeManager;
	}

	public String getQualityManager() {
		return qualityManager;
	}

	public void setQualityManager(String qualityManager) {
		this.qualityManager = qualityManager;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getRelationPersons() {
		return relationPersons;
	}

	public void setRelationPersons(String relationPersons) {
		this.relationPersons = relationPersons;
	}

	public String getAersFiles() {
		return aersFiles;
	}

	public void setAersFiles(String aersFiles) {
		this.aersFiles = aersFiles;
	}

	public Long getAersreportId() {
		return aersreportId;
	}

	public void setAersreportId(Long aersreportId) {
		this.aersreportId = aersreportId;
	}

	public Date getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}

	public String getHappenPlace() {
		return happenPlace;
	}

	public void setHappenPlace(String happenPlace) {
		this.happenPlace = happenPlace;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}



	public String getPatientAndInfluence() {
		return patientAndInfluence;
	}

	public void setPatientAndInfluence(String patientAndInfluence) {
		this.patientAndInfluence = patientAndInfluence;
	}

	public AersPatientInfoEntity getPatinfoId() {
		return patinfoId;
	}

	public void setPatinfoId(AersPatientInfoEntity patinfoId) {
		this.patinfoId = patinfoId;
	}

	public String getIdentitycateReporter() {
		return identitycateReporter;
	}

	public void setIdentitycateReporter(String identitycateReporter) {
		this.identitycateReporter = identitycateReporter;
	}

	public String getIsManagerReporter() {
		return isManagerReporter;
	}

	public void setIsManagerReporter(String isManagerReporter) {
		this.isManagerReporter = isManagerReporter;
	}

	public String getWorkyearsReporter() {
		return workyearsReporter;
	}

	public void setWorkyearsReporter(String workyearsReporter) {
		this.workyearsReporter = workyearsReporter;
	}

	public String getEntryyearsReporter() {
		return entryyearsReporter;
	}

	public void setEntryyearsReporter(String entryyearsReporter) {
		this.entryyearsReporter = entryyearsReporter;
	}


	public String getEducationReporter() {
		return educationReporter;
	}

	public void setEducationReporter(String educationReporter) {
		this.educationReporter = educationReporter;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Boolean getRejected() {
		return rejected;
	}

	public void setRejected(Boolean rejected) {
		this.rejected = rejected;
	}

	public String getShenuser() {
		return shenuser;
	}

	public void setShenuser(String shenuser) {
		this.shenuser = shenuser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	

	

}

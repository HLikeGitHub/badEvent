package com.yjy.web.mms.model.entity.aers;

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
@Table(name="aers_MedicalSafetyEvent")
//主表
public class ApparatusEventReportEntity{
	
	@Id
	@Column(name="medicalSafetyEvent_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long medicalSafetyEventId;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="aersreport_id")
	private AersYJGWReportProcessEntity aersyjgwreportId;
	

	@Column(name = "event_category")
	private String eventCategory;			//不良事件类别
	
	
	@Column(name = "event_level")
	private String eventLevel;			//不良事件的等级
	
	@Column(name="possible_causes")
	private String possibleCauses;    //事件发生后及时处理与分析:导致事件的可能原因
	
	@Column(name="treatment")
	private String treatment;		//事件发生后及时处理与分析:事件处理情况（提供补救措施或改善建议）
	
	@Column(name="statement_opinion")
	private String statementOpinion;		//不良事件评价（主管部门填写）:主管部门意见陈述
	
	@Column(name="improvement_measures")
	private String improvementMeasures;			//持续改进措施（主管部门或医疗质量管理委员会填写）
	
	@Column(name="reporter")
	private String reporter;//选择性填写项目（Ⅰ、Ⅱ级事件必填 ﹡，Ⅲ、Ⅳ级事件建议填写）:报告人
	
	@Column(name="reporter_category")
	private String reporterCategory;//选择性填写项目（Ⅰ、Ⅱ级事件必填 ﹡，Ⅲ、Ⅳ级事件建议填写）:当事人的类别
	
	@Column(name="professor")
	private String professor;//选择性填写项目（Ⅰ、Ⅱ级事件必填 ﹡，Ⅲ、Ⅳ级事件建议填写）:职称
	
	/*上传附件*/
	@Column(name="annexs")
	private String annexs;			//附件

	public Long getMedicalSafetyEventId() {
		return medicalSafetyEventId;
	}

	public void setMedicalSafetyEventId(Long medicalSafetyEventId) {
		this.medicalSafetyEventId = medicalSafetyEventId;
	}

	public AersYJGWReportProcessEntity getAersyjgwreportId() {
		return aersyjgwreportId;
	}

	public void setAersyjgwreportId(AersYJGWReportProcessEntity aersyjgwreportId) {
		this.aersyjgwreportId = aersyjgwreportId;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	public String getEventLevel() {
		return eventLevel;
	}

	public void setEventLevel(String eventLevel) {
		this.eventLevel = eventLevel;
	}

	public String getPossibleCauses() {
		return possibleCauses;
	}

	public void setPossibleCauses(String possibleCauses) {
		this.possibleCauses = possibleCauses;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getStatementOpinion() {
		return statementOpinion;
	}

	public void setStatementOpinion(String statementOpinion) {
		this.statementOpinion = statementOpinion;
	}

	public String getImprovementMeasures() {
		return improvementMeasures;
	}

	public void setImprovementMeasures(String improvementMeasures) {
		this.improvementMeasures = improvementMeasures;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getReporterCategory() {
		return reporterCategory;
	}

	public void setReporterCategory(String reporterCategory) {
		this.reporterCategory = reporterCategory;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getAnnexs() {
		return annexs;
	}

	public void setAnnexs(String annexs) {
		this.annexs = annexs;
	}


	
	
	
	
}

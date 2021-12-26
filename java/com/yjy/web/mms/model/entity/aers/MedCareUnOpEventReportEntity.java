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
@Table(name="aers_MedCareUnOpEvent")
//主表
public class MedCareUnOpEventReportEntity{
	
	@Id
	@Column(name="medCareUnOpEvent_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long medCareUnOpEventId;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="aersreport_id")
	private AersReportProcessEntity aersreportId;
	
	/*上次手术情况*/
	@ManyToOne()
	@JoinColumn(name="op_dept")
	private Dept opDept;			//手术科室
	
	
	@Column(name = "out_date")
	private Date outDate;			//出院日期
	
	@Column(name="mrNo")
	private String mrNo;    //病案号
	
	@Column(name="age")
	private String age;		//年龄
	
	@Column(name="firstOpName")
	private String firstOpName;		//第1次手术名称
	
	@Column(name="firstOpDate")
	private Date firstOpDate;			//是第1次手术日期
	
	@Column(name="firstOpUser")
	private String firstOpUser;//第1次手术者
	
	
	@Column(name="firstOpAssistant" )
	private String firstOpAssistant;			//一助
	@Column(name="isOpReturnReason")
	private String isOpReturnReason;			//导致重返的手术是否择期
	@Column(name="againOpReason")
	private String againOpReason;			//再次手术原因
	
	@Column(name="againOpName")
	private String againOpName;			//再次手术名称
	
	@Column(name="threeOpName")
	private String threeOpName;			//第3次手术名称
	@Column(name="fourOpName")
	private String fourOpName;			//第4次手术名称
	@Column(name="fiveOpName")
	private String fiveOpName;			//第5次手术名称
	@Column(name="opReturnNum")
	private String opReturnNum;			//导致重返手术室的手术编码
	@Column(name="outSituation")
	private String outSituation;			//出院情况
	@Column(name="isReport")
	private String isReport;			//有否上报
	@Column(name="reportDate")
	private String reportDate;			//何时上报
	@Column(name="is48RetrunOp")
	private String is48HourRetrunOp;			//是否术后48小时内重返手术
	@Column(name="is30DayRetrunOp")
	private String is30DayRetrunOp;			//是否术后30天内重返手术
	@Column(name="outDiagnosisName")
	private String outDiagnosisName;			//出院诊断名称
	@Column(name="outDiagnosisCode")
	private String outDiagnosisCode;			//出院诊断编码 
	@Column(name="isComplication")
	private String isComplication;			//是否并发症
	
	
	/*再次手术原因*/
	@Column(name="isIatrogenic")
	private String isIatrogenic;			//是否医源性
	

	/*再次手术原因(具体)*/
	@Column(name="technologyReason")
	private String technologyReason;			//技术原因
	@Column(name="anaesthesiaReason")
	private String anaesthesiaReason;			//麻醉原因
	@Column(name="nisReason")
	private String nisReason;			//院感原因
	@Column(name="otherReason")
	private String otherReason;			//其他原因
	
	
	/*上传附件*/
	@Column(name="annexs")
	private String annexs;			//附件


	public Long getMedCareUnOpEventId() {
		return medCareUnOpEventId;
	}


	public void setMedCareUnOpEventId(Long medCareUnOpEventId) {
		this.medCareUnOpEventId = medCareUnOpEventId;
	}


	public AersReportProcessEntity getAersreportId() {
		return aersreportId;
	}


	public void setAersreportId(AersReportProcessEntity aersreportId) {
		this.aersreportId = aersreportId;
	}


	public Dept getOpDept() {
		return opDept;
	}


	public void setOpDept(Dept opDept) {
		this.opDept = opDept;
	}


	public Date getOutDate() {
		return outDate;
	}


	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}


	public String getMrNo() {
		return mrNo;
	}


	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
	}


	public String getAge() {
		return age;
	}


	public void setAge(String age) {
		this.age = age;
	}


	public String getFirstOpName() {
		return firstOpName;
	}


	public void setFirstOpName(String firstOpName) {
		this.firstOpName = firstOpName;
	}


	public Date getFirstOpDate() {
		return firstOpDate;
	}


	public void setFirstOpDate(Date firstOpDate) {
		this.firstOpDate = firstOpDate;
	}


	public String getFirstOpUser() {
		return firstOpUser;
	}


	public void setFirstOpUser(String firstOpUser) {
		this.firstOpUser = firstOpUser;
	}


	public String getFirstOpAssistant() {
		return firstOpAssistant;
	}


	public void setFirstOpAssistant(String firstOpAssistant) {
		this.firstOpAssistant = firstOpAssistant;
	}


	public String getIsOpReturnReason() {
		return isOpReturnReason;
	}


	public void setIsOpReturnReason(String isOpReturnReason) {
		this.isOpReturnReason = isOpReturnReason;
	}


	public String getAgainOpReason() {
		return againOpReason;
	}


	public void setAgainOpReason(String againOpReason) {
		this.againOpReason = againOpReason;
	}


	public String getThreeOpName() {
		return threeOpName;
	}


	public void setThreeOpName(String threeOpName) {
		this.threeOpName = threeOpName;
	}


	public String getFourOpName() {
		return fourOpName;
	}


	public void setFourOpName(String fourOpName) {
		this.fourOpName = fourOpName;
	}


	public String getFiveOpName() {
		return fiveOpName;
	}


	public void setFiveOpName(String fiveOpName) {
		this.fiveOpName = fiveOpName;
	}


	public String getOpReturnNum() {
		return opReturnNum;
	}


	public void setOpReturnNum(String opReturnNum) {
		this.opReturnNum = opReturnNum;
	}


	public String getOutSituation() {
		return outSituation;
	}


	public void setOutSituation(String outSituation) {
		this.outSituation = outSituation;
	}


	public String getIsReport() {
		return isReport;
	}


	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}


	public String getReportDate() {
		return reportDate;
	}


	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}


	public String getIs48HourRetrunOp() {
		return is48HourRetrunOp;
	}


	public void setIs48HourRetrunOp(String is48HourRetrunOp) {
		this.is48HourRetrunOp = is48HourRetrunOp;
	}


	public String getIs30DayRetrunOp() {
		return is30DayRetrunOp;
	}


	public void setIs30DayRetrunOp(String is30DayRetrunOp) {
		this.is30DayRetrunOp = is30DayRetrunOp;
	}


	public String getOutDiagnosisName() {
		return outDiagnosisName;
	}


	public void setOutDiagnosisName(String outDiagnosisName) {
		this.outDiagnosisName = outDiagnosisName;
	}


	public String getOutDiagnosisCode() {
		return outDiagnosisCode;
	}


	public void setOutDiagnosisCode(String outDiagnosisCode) {
		this.outDiagnosisCode = outDiagnosisCode;
	}


	public String getIsComplication() {
		return isComplication;
	}


	public void setIsComplication(String isComplication) {
		this.isComplication = isComplication;
	}


	public String getIsIatrogenic() {
		return isIatrogenic;
	}


	public void setIsIatrogenic(String isIatrogenic) {
		this.isIatrogenic = isIatrogenic;
	}


	public String getTechnologyReason() {
		return technologyReason;
	}


	public void setTechnologyReason(String technologyReason) {
		this.technologyReason = technologyReason;
	}


	public String getAnaesthesiaReason() {
		return anaesthesiaReason;
	}


	public void setAnaesthesiaReason(String anaesthesiaReason) {
		this.anaesthesiaReason = anaesthesiaReason;
	}


	public String getNisReason() {
		return nisReason;
	}


	public void setNisReason(String nisReason) {
		this.nisReason = nisReason;
	}


	public String getOtherReason() {
		return otherReason;
	}


	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}


	public String getAnnexs() {
		return annexs;
	}


	public void setAnnexs(String annexs) {
		this.annexs = annexs;
	}


	public String getAgainOpName() {
		return againOpName;
	}


	public void setAgainOpName(String againOpName) {
		this.againOpName = againOpName;
	}
	
	
	
}

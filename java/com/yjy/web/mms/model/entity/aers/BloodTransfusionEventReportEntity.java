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
@Table(name="aers_BloodTransfusionEvent")
//主表
public class BloodTransfusionEventReportEntity{
	
	@Id
	@Column(name="bloodTransfusionEvent_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long bloodTransfusionEventId;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="aersreport_id")
	private AersYJGWReportProcessEntity aersyjgwreportId;
	

	@Column(name = "event_category")
	private String eventCategory;			//输血史
	
	
	@Column(name = "event_level")
	private String eventLevel;			//妊娠史
	
	@Column(name="possible_causes")
	private String possibleCauses;    //不良反应史
	
	@Column(name="treatment")
	private String treatment;		//输血时是否处于全麻状态
	
	@Column(name="statement_opinion")
	private String statementOpinion;		//患者ABO血型
	
	@Column(name="improvement_measures")
	private String improvementMeasures;			//患者RhD血型
	
	@Column(name="reporter")
	private String reporter;//其他血型
	
	@Column(name="reporter_category")
	private String reporterCategory;//血袋编号
	
//	@Column(name="professor")
//	private String professor;//输血量
//	
//	@Column(name="professor")
//	private String professor;//输血反应发生时已经输入血量
//	
//	@Column(name="professor")
//	private String professor;//输血量
//	
//	@Column(name="professor")
//	private String professor;//输血量
//	
//	@Column(name="professor")
//	private String professor;//输血量
//	
//	@Column(name="professor")
//	private String professor;//输血量
//	
//	@Column(name="professor")
//	private String professor;//输血量
	/*上传附件*/
	@Column(name="annexs")
	private String annexs;			//附件




	
	
	
	
}

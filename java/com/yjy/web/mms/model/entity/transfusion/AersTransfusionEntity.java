package com.yjy.web.mms.model.entity.transfusion;


import com.yjy.web.mms.model.entity.aers.AersPatientInfoEntity;
import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="aers_transfusion")
public class AersTransfusionEntity {
    @Id
    @Column(name = "aersTransfusion_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long aersTransfusionId; // 字典id

    @Column(name="id_card")
    private String idCard ;  //身份证号

    @Column(name="bed")
    private String bed;   // 床号

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="aersreport_id")
    private AersYJGWReportProcessEntity aersyjgwreportId;

    @Column(name="transfusion_history")
    private String transfusionHistory; //输血史

    @Column(name="pregnancy_history")
    private String pregnancyHistory; // 妊娠史

    @Column(name="bad_reaction")
    private String badReaction;  // 不良反应史

    @Column(name = "dept_cate")
    private String deptCate; // 科别

    @Column(name="general_anesthesia")
    private String generalAnesthesia;  // 输血时是否处于全麻状态

    @Column(name="abo_blood_group")
    private String aboBloodGroup;  // 患者ABO血型

    @Column(name="rhd_blood_group")
    private String rhdBloodGroup;  // 患者RhD血型

    @Column(name="other_blood_group")
    private String otherBloodGroup;  // 其他血型

    @Column(name="blood_bag_code")
    private String bloodBagCode;   //血袋编号

    @Column(name="transfusion_volume")
    private String transfusionVolume;  // 输血量

    @Column(name = "input_blood_volume")
    private String inputBloodVolume; // 输血反应发生时已经输入血量

    @Column(name = "happen_status")
    private String  happenStatus; // 发生状态  1. 输血开始后， 2.输血结束后

    @Column(name = "happen_time")
    private Date happenTime; // 发生时间

    @Column(name = "symptom_and_sign")
    private String symptomAndSign; //症状与体征

    @Column(name = "clinical_treatment")
    private String clinicalTreatment; // 临床处理程序

    @Column(name = "inspection_processing")
    private String inspectionProcessing; // 检验科处理程序

    @Column(name = "turn_over")
    private String turnOver; // 转归

    @Column(name = "conclusion")
    private String conclusion; // 结论

    @Column(name = "track_follow_up")
    private String trackFollowUp; // 追踪随访

    @Column(name = "nurse")
    private String nurse; // 护士

    @Column(name = "physician")
    private String physician; // 经治医师

    @Column(name = "note_taker")
    private String noteTaker; // 记录人

    @Column(name = "section_director")
    private String sectionDirector; // 科主任

    @Column(name = "record_date")
    private Date recordDate; //记录日期

    @Column(name="status")
    private String status; // 操作状态

    public Long getAersTransfusionId() {
        return aersTransfusionId;
    }

    public void setAersTransfusionId(Long aersTransfusionId) {
        this.aersTransfusionId = aersTransfusionId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public AersYJGWReportProcessEntity getAersyjgwreportId() {
        return aersyjgwreportId;
    }

    public void setAersyjgwreportId(AersYJGWReportProcessEntity aersyjgwreportId) {
        this.aersyjgwreportId = aersyjgwreportId;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransfusionHistory() {
        return transfusionHistory;
    }

    public void setTransfusionHistory(String transfusionHistory) {
        this.transfusionHistory = transfusionHistory;
    }

    public String getPregnancyHistory() {
        return pregnancyHistory;
    }

    public void setPregnancyHistory(String pregnancyHistory) {
        this.pregnancyHistory = pregnancyHistory;
    }

    public String getBadReaction() {
        return badReaction;
    }

    public void setBadReaction(String badReaction) {
        this.badReaction = badReaction;
    }

    public String getDeptCate() {
        return deptCate;
    }

    public void setDeptCate(String deptCate) {
        this.deptCate = deptCate;
    }

    public String getGeneralAnesthesia() {
        return generalAnesthesia;
    }

    public void setGeneralAnesthesia(String generalAnesthesia) {
        this.generalAnesthesia = generalAnesthesia;
    }

    public String getHappenStatus() {
        return happenStatus;
    }

    public void setHappenStatus(String happenStatus) {
        this.happenStatus = happenStatus;
    }

    public String getTurnOver() {
        return turnOver;
    }

    public void setTurnOver(String turnOver) {
        this.turnOver = turnOver;
    }

    public String getAboBloodGroup() {
        return aboBloodGroup;
    }

    public void setAboBloodGroup(String aboBloodGroup) {
        this.aboBloodGroup = aboBloodGroup;
    }

    public String getRhdBloodGroup() {
        return rhdBloodGroup;
    }

    public void setRhdBloodGroup(String rhdBloodGroup) {
        this.rhdBloodGroup = rhdBloodGroup;
    }

    public String getOtherBloodGroup() {
        return otherBloodGroup;
    }

    public void setOtherBloodGroup(String otherBloodGroup) {
        this.otherBloodGroup = otherBloodGroup;
    }

    public String getBloodBagCode() {
        return bloodBagCode;
    }

    public void setBloodBagCode(String bloodBagCode) {
        this.bloodBagCode = bloodBagCode;
    }

    public String getTransfusionVolume() {
        return transfusionVolume;
    }

    public void setTransfusionVolume(String transfusionVolume) {
        this.transfusionVolume = transfusionVolume;
    }

    public String getInputBloodVolume() {
        return inputBloodVolume;
    }

    public void setInputBloodVolume(String inputBloodVolume) {
        this.inputBloodVolume = inputBloodVolume;
    }

    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    public String getSymptomAndSign() {
        return symptomAndSign;
    }

    public void setSymptomAndSign(String symptomAndSign) {
        this.symptomAndSign = symptomAndSign;
    }

    public String getClinicalTreatment() {
        return clinicalTreatment;
    }

    public void setClinicalTreatment(String clinicalTreatment) {
        this.clinicalTreatment = clinicalTreatment;
    }

    public String getInspectionProcessing() {
        return inspectionProcessing;
    }

    public void setInspectionProcessing(String inspectionProcessing) {
        this.inspectionProcessing = inspectionProcessing;
    }



    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getTrackFollowUp() {
        return trackFollowUp;
    }

    public void setTrackFollowUp(String trackFollowUp) {
        this.trackFollowUp = trackFollowUp;
    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getPhysician() {
        return physician;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public String getNoteTaker() {
        return noteTaker;
    }

    public void setNoteTaker(String noteTaker) {
        this.noteTaker = noteTaker;
    }

    public String getSectionDirector() {
        return sectionDirector;
    }

    public void setSectionDirector(String sectionDirector) {
        this.sectionDirector = sectionDirector;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
}

package com.yjy.web.mms.model.entity.process;

import javax.persistence.*;
import java.util.Date;
/**
 * @Author:winper001 2020-01-15 15:28
 **/


@Entity
@Table(name="aoa_passport_apply")
public class PassportApply {
    @Id
    @Column(name="passportApplyId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passportApplyId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pro_id")
    private ProcessList proId;

    @Column(name="applyPersonName")
    private String applyPersonName;

    @Column(name="applyPersonEngName")
    private String applyPersonEngName;

    @Column(name="applyTime")
    private Date applyTime;

    @Column(name="startTime")
    private Date startTime;

    @Column(name="endTime")
    private Date endTime;

    @Column(name="actualStartTime")//实际开始时间
    private String actualStartTime;

    @Column(name="actualEndTime")//实际结束时间
    private String actualEndTime;

    @Column(name="applyReason")
    private String applyReason;

    @Column(name="destination")
    private String destination;

    @Column(name="destinationEN")
    private String destinationEN;

    //@Transient
    @Column(name="nameuser")
    private String nameuser;//审核人员

    @Column(name="typeVisa")//签注类型：1、前往台湾六个月一次签注 2、前往澳门一年一次 3、前往香港一年一次
    private String typeVisa;

    @Column(name="bz")
    private String bz;  //备注

    @Column(name="f_iswcmsg") //是否通知申请人
    private String f_iswcmsg;

    @Column(name="isPrint") //是否打印
    private String isPrint;

    @Column(name="typeProve")//证明类型
    private String typeProve;

    @Column(name="typeCertificate")//证件类型
    private String typeCertificate;

    @Column(name="stateCertificate")//证件状态
    private String stateCertificate;


    @Column(name="kskz_advice") //科室科长意见
    private String kskz_advice;

    @Column(name="hr_advice") //人力资源科意见
    private String hr_advice;

    @Column(name="zgld_advice") //主管领导意见
    private String zgld_advice;

    @Column(name="yzdwsj_advice")
    private String yzdwsjAdvice;//院长党委书记意见及说明

    @Column(name="hr_zxry_advice") //人力资源科执行人员意见
    private String hr_zxry_advice;


    @Column(name="zxry")
    private String zxry; //执行查询人员


    @Column(name="takeCertificateTime")//拿证时间
    private String takeCertificateTime;

    @Column(name="returnCertificateTime")//还证时间
    private String returnCertificateTime;

    //新增
    @Column(name="workTime")
    private String workTime;//工作年长

//    @Column(name="workTimeEN")
//    private Long workTimeEN;//工作年长

    @Column(name="holidayTime")//总假期
    private String holidayTime;

//    @Column(name="holidayTimeEN")//总假期
//    private Long holidayTimeEN;


    @Column(name="winterVacation")//年休假
    private String winterVacation;

    @Column(name="sabbatical")//公休假
    private String sabbatical;


    @Column(name="accumulateTime")//积假
    private String accumulateTime;


    @Column(name="passportId")//护照号
    private String passportId;

//    @Column(name="passportIdEN")//护照号
//    private String passportIdEN;

    @Column(name="birthYMD")//申请人的出生年月日
    private String birthYMD;

//    @Column(name="birthYMDEN")//申请人的出生年月日(英文)
//    private String birthYMDEN;

    @Column(name="sexEnglish")//性别 英文
    private String sexEnglish;

    @Column(name="positionEngLish")//职位 英文
    private String positionEngLish;

    @Column(name="income")//月收入
    private String income;

//    @Column(name="incomeEN")//月收入
//    private String incomeEN;

    @Column(name="certificateValidStartTime")//证件有效期:开始日期
    private String certificateValidStartTime;

    @Column(name="certificateValidEndTime")//证件有效期:失效日期
    private String certificateValidEndTime;

    @Column(name="final_checker")//由人事科选择的最终审核人，暂时存起来
    private String finalChecker;

    @Column(name="handle_type")//类型：办理或者申请
    private String handleType;

    public Long getPassportApplyId() {
        return passportApplyId;
    }

    public void setPassportApplyId(Long passportApplyId) {
        this.passportApplyId = passportApplyId;
    }

    public ProcessList getProId() {
        return proId;
    }

    public void setProId(ProcessList proId) {
        this.proId = proId;
    }

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getApplyPersonEngName() {
        return applyPersonEngName;
    }

    public void setApplyPersonEngName(String applyPersonEngName) {
        this.applyPersonEngName = applyPersonEngName;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationEN() {
        return destinationEN;
    }

    public void setDestinationEN(String destinationEN) {
        this.destinationEN = destinationEN;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(String typeVisa) {
        this.typeVisa = typeVisa;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getF_iswcmsg() {
        return f_iswcmsg;
    }

    public void setF_iswcmsg(String f_iswcmsg) {
        this.f_iswcmsg = f_iswcmsg;
    }

    public String getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(String isPrint) {
        this.isPrint = isPrint;
    }

    public String getTypeProve() {
        return typeProve;
    }

    public void setTypeProve(String typeProve) {
        this.typeProve = typeProve;
    }

    public String getTypeCertificate() {
        return typeCertificate;
    }

    public void setTypeCertificate(String typeCertificate) {
        this.typeCertificate = typeCertificate;
    }

    public String getStateCertificate() {
        return stateCertificate;
    }

    public void setStateCertificate(String stateCertificate) {
        this.stateCertificate = stateCertificate;
    }

    public String getKskz_advice() {
        return kskz_advice;
    }

    public void setKskz_advice(String kskz_advice) {
        this.kskz_advice = kskz_advice;
    }

    public String getHr_advice() {
        return hr_advice;
    }

    public void setHr_advice(String hr_advice) {
        this.hr_advice = hr_advice;
    }

    public String getZgld_advice() {
        return zgld_advice;
    }

    public void setZgld_advice(String zgld_advice) {
        this.zgld_advice = zgld_advice;
    }

    public String getYzdwsjAdvice() {
        return yzdwsjAdvice;
    }

    public void setYzdwsjAdvice(String yzdwsjAdvice) {
        this.yzdwsjAdvice = yzdwsjAdvice;
    }

    public String getHr_zxry_advice() {
        return hr_zxry_advice;
    }

    public void setHr_zxry_advice(String hr_zxry_advice) {
        this.hr_zxry_advice = hr_zxry_advice;
    }

    public String getZxry() {
        return zxry;
    }

    public void setZxry(String zxry) {
        this.zxry = zxry;
    }

    public String getTakeCertificateTime() {
        return takeCertificateTime;
    }

    public void setTakeCertificateTime(String takeCertificateTime) {
        this.takeCertificateTime = takeCertificateTime;
    }

    public String getReturnCertificateTime() {
        return returnCertificateTime;
    }

    public void setReturnCertificateTime(String returnCertificateTime) {
        this.returnCertificateTime = returnCertificateTime;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getHolidayTime() {
        return holidayTime;
    }

    public void setHolidayTime(String holidayTime) {
        this.holidayTime = holidayTime;
    }

    public String getWinterVacation() {
        return winterVacation;
    }

    public void setWinterVacation(String winterVacation) {
        this.winterVacation = winterVacation;
    }

    public String getSabbatical() {
        return sabbatical;
    }

    public void setSabbatical(String sabbatical) {
        this.sabbatical = sabbatical;
    }

    public String getAccumulateTime() {
        return accumulateTime;
    }

    public void setAccumulateTime(String accumulateTime) {
        this.accumulateTime = accumulateTime;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getBirthYMD() {
        return birthYMD;
    }

    public void setBirthYMD(String birthYMD) {
        this.birthYMD = birthYMD;
    }

    public String getSexEnglish() {
        return sexEnglish;
    }

    public void setSexEnglish(String sexEnglish) {
        this.sexEnglish = sexEnglish;
    }

    public String getPositionEngLish() {
        return positionEngLish;
    }

    public void setPositionEngLish(String positionEngLish) {
        this.positionEngLish = positionEngLish;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCertificateValidStartTime() {
        return certificateValidStartTime;
    }

    public void setCertificateValidStartTime(String certificateValidStartTime) {
        this.certificateValidStartTime = certificateValidStartTime;
    }

    public String getCertificateValidEndTime() {
        return certificateValidEndTime;
    }

    public void setCertificateValidEndTime(String certificateValidEndTime) {
        this.certificateValidEndTime = certificateValidEndTime;
    }

    public String getFinalChecker() {
        return finalChecker;
    }

    public void setFinalChecker(String finalChecker) {
        this.finalChecker = finalChecker;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    @Override
    public String toString() {
        return "PassportApply{" +
                "passportApplyId=" + passportApplyId +
                ", proId=" + proId +
                ", applyPersonName='" + applyPersonName + '\'' +
                ", applyPersonEngName='" + applyPersonEngName + '\'' +
                ", applyTime=" + applyTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", actualStartTime='" + actualStartTime + '\'' +
                ", actualEndTime='" + actualEndTime + '\'' +
                ", applyReason='" + applyReason + '\'' +
                ", destination='" + destination + '\'' +
                ", destinationEN='" + destinationEN + '\'' +
                ", nameuser='" + nameuser + '\'' +
                ", typeVisa='" + typeVisa + '\'' +
                ", bz='" + bz + '\'' +
                ", f_iswcmsg='" + f_iswcmsg + '\'' +
                ", isPrint='" + isPrint + '\'' +
                ", typeProve='" + typeProve + '\'' +
                ", typeCertificate='" + typeCertificate + '\'' +
                ", stateCertificate='" + stateCertificate + '\'' +
                ", kskz_advice='" + kskz_advice + '\'' +
                ", hr_advice='" + hr_advice + '\'' +
                ", zgld_advice='" + zgld_advice + '\'' +
                ", yzdwsjAdvice='" + yzdwsjAdvice + '\'' +
                ", hr_zxry_advice='" + hr_zxry_advice + '\'' +
                ", zxry='" + zxry + '\'' +
                ", takeCertificateTime='" + takeCertificateTime + '\'' +
                ", returnCertificateTime='" + returnCertificateTime + '\'' +
                ", workTime='" + workTime + '\'' +
                ", holidayTime='" + holidayTime + '\'' +
                ", winterVacation='" + winterVacation + '\'' +
                ", sabbatical='" + sabbatical + '\'' +
                ", accumulateTime='" + accumulateTime + '\'' +
                ", passportId='" + passportId + '\'' +
                ", birthYMD='" + birthYMD + '\'' +
                ", sexEnglish='" + sexEnglish + '\'' +
                ", positionEngLish='" + positionEngLish + '\'' +
                ", income='" + income + '\'' +
                ", certificateValidStartTime='" + certificateValidStartTime + '\'' +
                ", certificateValidEndTime='" + certificateValidEndTime + '\'' +
                '}';
    }
}

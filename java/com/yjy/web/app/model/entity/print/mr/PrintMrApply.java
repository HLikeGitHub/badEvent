package com.yjy.web.app.model.entity.print.mr;

import com.yjy.web.comm.utils.TextUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 病案打印申请实体类
 * @desc PrintMrApply
 * @author rwx
 * @email aba121mail@qq.com
 */
@Entity
@Table(name = "app_print_mr_apply")
public class PrintMrApply {
    /**
     * 申请ID
     */
    private long applyId;
    /**
     * 用户ID
     */
    private String uid;
    /**
     * 是否本人
     */
    private boolean self;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 申请人与病患关系
     */
    private String relationship;
    /**
     * 患者名字
     */
    private String patient;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 住院号（admission number）
     */
    private String adNum;
    /**
     * 住院号ID（admission number id）
     */
    private String adId;
	/**
     * 入院时间
     */
    private String date2Hos;
    /**
     * 出院时间
     */
    private String dateLeftHos;
    /**
     * 出院科室
     */
    private String depLeftHos;
    /**
     * 复印用途ID
     */
    private String mrPrintFor;
    /**
     * 申请复印项目列表
     */
    private String mrPrintItems;
    /**
     * 申请复印项目其他
     */
    private String mrPrintItemsExt;
    /**
     * 身份证正面图片URL
     */
    private String idFrontUrl;
    /**
     * 身份证背面图片URL
     */
    private String idBackUrl;
    /**
     * 申请人身份证正面图片URL
     */
    private String applicantIdFrontUrl;
    /**
     * 申请人身份证背面图片URL
     */
    private String applicantIdBackUrl;

	/**
	 * 快递收件人
	 */
	private String expAddressee;
	/**
	 * 快递收件人电话
	 */
	private String expPhoneNum;
	/**
	 * 快递收件地址
	 */
	private String expAddress;
	/**
	 * 快递公司
	 */
	private String expCompany;
	/**
	 * 快递单号
	 */
	private String expNum;
	/**
	 * 申请状态
	 */
	private String status;
    /**
     * 额外申请状态
     */
    private String statusExt;
    /**
     * 申请时间
     */
    private Date applyDate;
    /**
     * 打印次数
     */
    private int printCount;
    /**
     * 打印费用
     */
    private String printFee;
    
    /**
     * 快递区域
     */
    private String expressArea;
    
    /**
     * 快递费用
     */
    private String expressFee;
    /**
     * 订单ID
     */
    private String orderId;
    
    /*
     * 审核状态
     */
    private String applyState;
    /**
     * 是否启动
     */
    private boolean enabled;
    /**
     * 随机码
     */
    private String token;

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="apply_id")
    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(long applyId) {
        this.applyId = applyId;
    }

    @Column(name="uid")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(name="self")
    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    @Column(name = "applicant")
    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    @Column(name = "relationship")
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Column(name = "patient")
    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "id_card")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Column(name = "ad_num")
    public String getAdNum() {
        return adNum;
    }

    public void setAdNum(String adNum) {
        this.adNum = adNum;
    }

    @Column(name = "ad_id")
    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    @Column(name = "date_2_hos")
    public String getDate2Hos() {
        return date2Hos;
    }

    public void setDate2Hos(String date2Hos) {
        this.date2Hos = date2Hos;
    }

    @Column(name = "date_left_hos")
    public String getDateLeftHos() {
        return dateLeftHos;
    }

    public void setDateLeftHos(String dateLeftHos) {
        this.dateLeftHos = dateLeftHos;
    }

    @Column(name = "dep_left_hos")
    public String getDepLeftHos() {
        return depLeftHos;
    }

    public void setDepLeftHos(String depLeftHos) {
        this.depLeftHos = depLeftHos;
    }

    @Column(name = "mr_print_for")
    public String getMrPrintFor() {
        return mrPrintFor;
    }

    public void setMrPrintFor(String mrPrintFor) {
        this.mrPrintFor = mrPrintFor;
    }

    @Column(name = "mr_print_items")
    public String getMrPrintItems() {
        return mrPrintItems;
    }

    public void setMrPrintItems(String mrPrintItems) {
        this.mrPrintItems = mrPrintItems;
    }

    @Column(name = "mr_print_items_ext")
    public String getMrPrintItemsExt() {
        return mrPrintItemsExt;
    }

    public void setMrPrintItemsExt(String mrPrintItemsExt) {
        this.mrPrintItemsExt = mrPrintItemsExt;
    }

    @Column(name = "id_front_url")
    public String getIdFrontUrl() {
        return idFrontUrl;
    }

    public void setIdFrontUrl(String idFrontUrl) {
        this.idFrontUrl = idFrontUrl;
    }

    @Column(name = "id_back_url")
    public String getIdBackUrl() {
        return idBackUrl;
    }

    @Column(name = "applicant_id_front_url")
    public String getApplicantIdFrontUrl() {
        return applicantIdFrontUrl;
    }

    public void setApplicantIdFrontUrl(String applicantIdFrontUrl) {
        this.applicantIdFrontUrl = applicantIdFrontUrl;
    }

    @Column(name = "applicant_id_back_url")
    public String getApplicantIdBackUrl() {
        return applicantIdBackUrl;
    }

    public void setApplicantIdBackUrl(String applicantIdBackUrl) {
        this.applicantIdBackUrl = applicantIdBackUrl;
    }

    public void setIdBackUrl(String idBackUrl) {
        this.idBackUrl = idBackUrl;
    }

    @Column(name = "exp_addressee")
    public String getExpAddressee() {
        return expAddressee;
    }

    public void setExpAddressee(String expAddressee) {
        this.expAddressee = expAddressee;
    }

    @Column(name = "exp_phone_num")
    public String getExpPhoneNum() {
        return expPhoneNum;
    }

    public void setExpPhoneNum(String expPhoneNum) {
        this.expPhoneNum = expPhoneNum;
    }

    @Column(name = "exp_address")
    public String getExpAddress() {
        return expAddress;
    }

    public void setExpAddress(String expAddress) {
        this.expAddress = expAddress;
    }

    @Column(name = "exp_company")
    public String getExpCompany() {
        return expCompany;
    }

    public void setExpCompany(String expCompany) {
        this.expCompany = expCompany;
    }

    @Column(name = "exp_num")
    public String getExpNum() {
        return expNum;
    }

    public void setExpNum(String expNum) {
        this.expNum = expNum;
    }

    @Column(name = "status")
    public String getStatus() {
        return TextUtil.isNullOrEmpty(status) ? "未审核" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "status_ext")
    public String getStatusExt() {
        return statusExt;
    }

    public void setStatusExt(String statusExt) {
        this.statusExt = statusExt;
    }

    @Column(name = "apply_date")
    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @Column(name = "print_count")
    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    @Column(name = "print_fee")
    public String getPrintFee() {
        return printFee;
    }

    public void setPrintFee(String printFee) {
        this.printFee = printFee;
    }

    @Column(name = "express_fee")
    public String getExpressFee() {
        return expressFee == null || expressFee.isEmpty() ? "0" : expressFee;
    }

    public void setExpressFee(String expressFee) {
        this.expressFee = expressFee;
    }

    @Column(name = "order_id")
    public String getOrderId() {
        return TextUtil.isNullOrEmpty(orderId) ? "" : orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name="express_area")
    public String getExpressArea() {
		return expressArea;
	}

	public void setExpressArea(String expressArea) {
		this.expressArea = expressArea;
	}

    @Transient
	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

    @Transient
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "PrintMrApply{" +
                "applyId=" + applyId +
                ", uid='" + uid + '\'' +
                ", self=" + self +
                ", applicant='" + applicant + '\'' +
                ", relationship='" + relationship + '\'' +
                ", patient='" + patient + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", adNum=" + adNum +
                ", adId='" + adId + '\'' +
                ", date2Hos='" + date2Hos + '\'' +
                ", dateLeftHos='" + dateLeftHos + '\'' +
                ", depLeftHos='" + depLeftHos + '\'' +
                ", mrPrintFor='" + mrPrintFor + '\'' +
                ", mrPrintItems=" + mrPrintItems +
                ", mrPrintItemsExt='" + mrPrintItemsExt + '\'' +
                ", idFrontUrl='" + idFrontUrl + '\'' +
                ", idBackUrl='" + idBackUrl + '\'' +
                ", applicantIdFrontUrl='" + applicantIdFrontUrl + '\'' +
                ", applicantIdBackUrl='" + applicantIdBackUrl + '\'' +
                ", expAddressee='" + expAddressee + '\'' +
                ", expPhoneNum='" + expPhoneNum + '\'' +
                ", expAddress='" + expAddress + '\'' +
                ", expCompany='" + expCompany + '\'' +
                ", expNum='" + expNum + '\'' +
                ", status='" + status + '\'' +
                ", statusExt='" + statusExt + '\'' +
                ", applyDate=" + applyDate +
                ", printCount=" + printCount +
                ", printFee='" + printFee + '\'' +
                ", expressFee='" + expressFee + '\'' +
                ", orderId='" + orderId + '\'' +
                ", expressArea='" + expressArea + '\'' +
                ", applyState='" + applyState + '\'' +
                ", enabled=" + enabled +
                ", token='" + token + '\'' +
                '}';
    }
}

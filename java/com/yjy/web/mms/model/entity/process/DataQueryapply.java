package com.yjy.web.mms.model.entity.process;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="aoa_dataqueryapply")
@Entity
//数据查询
public class DataQueryapply{

	@Id
	@Column(name="dataquery_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long dataqueryapplyId;//
	
	@Column(name="cxyq")
	private String cxyq; //查询要求
	
	
	@Column(name="type_id")
	private Long typeId; //类型
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="pro_id")
	private ProcessList proId;
	
	@Column(name="f_iswcmsg")
	private String f_iswcmsg; //是否通知申请人
	
	@Column(name="bz")
	private String bz; //完成 备注

	@Column(name="zxry")
	private String zxry; //执行查询人员


	public String getZxry() {
		return zxry;
	}

	public void setZxry(String zxry) {
		this.zxry = zxry;
	}

	public String getF_iswcmsg() {
		return f_iswcmsg;
	}

	public void setF_iswcmsg(String f_iswcmsg) {
		this.f_iswcmsg = f_iswcmsg;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name="kskz_advice")
	private String kskzAdvice;//科室科长意见及说明

	@Column(name="bgsbsy_advice")
	private String bgsbsyAdvice;//办公室办事员意见及说明

	@Column(name="bgszr_advice")
	private String bgszrAdvice;//办公室主任意见及说明

	@Column(name="zgld_advice")
	private String zgldAdvice;//主管领导意见及说明

	@Column(name="yzdwsj_advice")
	private String yzdwsjAdvice;//院长党委书记意见及说明

	@Column(name="wlxxkkz_advice")
	private String wlxxkkzAdvice;//网络信息科科长意见及说明
	
	@Column(name="lckz_advice")
	private String lckzAdvice;//临床科室科长意见及说明
	
	@Column(name="znkz_advice")
	private String znkzAdvice;//职能科室科长意见及说明
	
	public String getWlxxkkzAdvice() {
		return wlxxkkzAdvice;
	}

	public void setWlxxkkzAdvice(String wlxxkkzAdvice) {
		this.wlxxkkzAdvice = wlxxkkzAdvice;
	}

	public String getLckzAdvice() {
		return lckzAdvice;
	}

	public void setLckzAdvice(String lckzAdvice) {
		this.lckzAdvice = lckzAdvice;
	}

	public String getZnkzAdvice() {
		return znkzAdvice;
	}

	public void setZnkzAdvice(String znkzAdvice) {
		this.znkzAdvice = znkzAdvice;
	}

	@Transient
	@Column(name="nameuser")
	private String nameuser;//审核人员
	
	
	
	
	

	public String getCxyq() {
		return cxyq;
	}

	public void setCxyq(String cxyq) {
		this.cxyq = cxyq;
	}

	public Long getDataqueryapplyId() {
		return dataqueryapplyId;
	}

	public void setDataqueryapplyId(Long dataqueryapplyId) {
		this.dataqueryapplyId = dataqueryapplyId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public ProcessList getProId() {
		return proId;
	}

	public void setProId(ProcessList proId) {
		this.proId = proId;
	}

	public String getKskzAdvice() {
		return kskzAdvice;
	}

	public void setKskzAdvice(String kskzAdvice) {
		this.kskzAdvice = kskzAdvice;
	}

	public String getBgsbsyAdvice() {
		return bgsbsyAdvice;
	}

	public void setBgsbsyAdvice(String bgsbsyAdvice) {
		this.bgsbsyAdvice = bgsbsyAdvice;
	}

	public String getBgszrAdvice() {
		return bgszrAdvice;
	}

	public void setBgszrAdvice(String bgszrAdvice) {
		this.bgszrAdvice = bgszrAdvice;
	}

	public String getZgldAdvice() {
		return zgldAdvice;
	}

	public void setZgldAdvice(String zgldAdvice) {
		this.zgldAdvice = zgldAdvice;
	}

	public String getYzdwsjAdvice() {
		return yzdwsjAdvice;
	}

	public void setYzdwsjAdvice(String yzdwsjAdvice) {
		this.yzdwsjAdvice = yzdwsjAdvice;
	}

	public String getNameuser() {
		return nameuser;
	}

	public void setNameuser(String nameuser) {
		this.nameuser = nameuser;
	}



	
	
	
}

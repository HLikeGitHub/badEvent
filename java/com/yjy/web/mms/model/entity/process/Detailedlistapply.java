package com.yjy.web.mms.model.entity.process;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="aoa_detailedlistapply")
@Entity
public class Detailedlistapply {

	@Id
	@Column(name="detailedlistapply_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long detailedlistapplyid;
	

    public Long getDetailedlistapplyid() {
		return detailedlistapplyid;
	}

	public void setDetailedlistapplyid(Long detailedlistapplyid) {
		this.detailedlistapplyid = detailedlistapplyid;
	}

	@ManyToOne()
	@JoinColumn(name="mealapply_id")
	private Mealapply mealapply;//对应公务接待
	
	
	
	
	public Mealapply getMealapply() {
		return mealapply;
	}

	public void setMealapply(Mealapply mealapply) {
		this.mealapply = mealapply;
	}

	private Integer allinvoices ;//票据总数
	
	@Column(name="all_money")
	private Double allMoney;//总计金额
	
	@Transient
	private String namemoney;//承担主体


	public Integer getAllinvoices() {
		return allinvoices;
	}

	public void setAllinvoices(Integer allinvoices) {
		this.allinvoices = allinvoices;
	}

	public Double getAllMoney() {
		return allMoney;
	}

	public void setAllMoney(Double allMoney) {
		this.allMoney = allMoney;
	}

	public String getNamemoney() {
		return namemoney;
	}

	public void setNamemoney(String namemoney) {
		this.namemoney = namemoney;
	}


	//来宾单位
	private String lbdw; 
	//接待地点
	private String jddd;
	
	//接待清单明细
	@OneToMany(cascade=CascadeType.ALL,mappedBy="detailedlists",orphanRemoval = true)
	List<DetailsList>  details;
	public List<DetailsList> getDetails() {
		return details;
	}

	public void setDetails(List<DetailsList> details) {
		this.details = details;
	}
	
	public String getJddd() {
		return jddd;
	}

	public void setJddd(String jddd) {
		this.jddd = jddd;
	}

	public String getLbdw() {
		return lbdw;
	}

	public void setLbdw(String lbdw) {
		this.lbdw = lbdw;
	}
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="pro_id")
	private ProcessList proId;
	
	
	@Transient
	private String username;
	
	




	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ProcessList getProId() {
		return proId;
	}

	public void setProId(ProcessList proId) {
		this.proId = proId;
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

	@Override
	public String toString() {
		return "Detailedlistapply [detailedlistapplyid=" + detailedlistapplyid + ", username=" + username + "]";
	}

	

	
	
	
}

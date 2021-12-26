package com.yjy.web.mms.model.entity.process;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="aoa_detailslist")
//报销费用明细表
public class DetailsList {

	@Id
	@Column(name="detailslist_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long detailslistId;
	
	
	
	
	
	
	public Long getDetailslistId() {
		return detailslistId;
	}

	public void setDetailslistId(Long detailslistId) {
		this.detailslistId = detailslistId;
	}

	private Date produceTime;//费用产生时间
	
	private String 	subject;//费用产生科目
	
	private String descript;//费用说明
	
	private Integer invoices ;//票据张数
	
	private double detailmoney;//报销金额
	
	@ManyToOne()
	@JoinColumn(name="detailedlistapply_id")
	private Detailedlistapply detailedlists;//对应报销表
	


	public Detailedlistapply getDetailedlists() {
		return detailedlists;
	}

	public void setDetailedlists(Detailedlistapply detailedlists) {
		this.detailedlists = detailedlists;
	}



	public Date getProduceTime() {
		return produceTime;
	}

	public void setProduceTime(Date produceTime) {
		this.produceTime = produceTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Integer getInvoices() {
		return invoices;
	}

	public void setInvoices(Integer invoices) {
		this.invoices = invoices;
	}

	public double getDetailmoney() {
		return detailmoney;
	}

	public void setDetailmoney(double detailmoney) {
		this.detailmoney = detailmoney;
	}

	@Override
	public String toString() {
		return "DetailsList [detailslistId=" + detailslistId + ", produceTime=" + produceTime + ", subject="
				+ subject + ", descript=" + descript + ", invoices=" + invoices + ", detailmoney=" + detailmoney + "]";
	}
	
	
}

package com.yjy.web.mms.model.entity.system;

import javax.persistence.*;

@Entity
@Table(name="aoa_sysmeetroom")
//领导接待表
public class SysMeetroom{

	@Id
	@Column(name="sysmeetroom_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long sysmeetroomId;//
	
	
	//会议室名称
	@Column(name="hysmc")
	private String hysmc; 
	
	
	
	//会议室地址
	@Column(name="hysdz")
	private String hysdz; 
	
	//邀请嘉宾
	@Column(name="status_id")
	private long statusId;

	public Long getSysmeetroomId() {
		return sysmeetroomId;
	}

	public void setSysmeetroomId(Long sysmeetroomId) {
		this.sysmeetroomId = sysmeetroomId;
	}

	public String getHysmc() {
		return hysmc;
	}

	public void setHysmc(String hysmc) {
		this.hysmc = hysmc;
	}

	public String getHysdz() {
		return hysdz;
	}

	public void setHysdz(String hysdz) {
		this.hysdz = hysdz;
	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	@Override
	public String toString() {
		return "SysMeetroom [sysmeetroomId=" + sysmeetroomId + ", hysmc=" + hysmc + ", hysdz=" + hysdz + ", statusId="
				+ statusId + ", getSysmeetroomId()=" + getSysmeetroomId() + ", getHysmc()=" + getHysmc()
				+ ", getHysdz()=" + getHysdz() + ", getStatusId()=" + getStatusId() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	} 
	
	
	

}

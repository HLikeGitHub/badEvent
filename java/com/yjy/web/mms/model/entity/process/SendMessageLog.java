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

import com.yjy.web.mms.model.entity.user.User;

@Entity
@Table(name="aoa_sendmessagelog")
//审核表
public class SendMessageLog {

	@Id
	@Column(name="sendmessagelog_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long sendmessagelogId;

	@Column(name="dxnr")
	private String dxnr; //短信内容
	
	@Column(name="phone")
	private String phone; //短信内容

	@Column(name="sendtime")
	private Date sendtime;//发送时间
	
	@Column(name="status_id")//发送状态
	private int statusId;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User userId;//发送人员
	

	@ManyToOne
	@JoinColumn(name="pro_id")
	private ProcessList proId;
	
	
	public String getPhone() {
		return phone;
	}





	public void setPhone(String phone) {
		this.phone = phone;
	}


	


	public ProcessList getProId() {
		return proId;
	}





	public void setProId(ProcessList proId) {
		this.proId = proId;
	}





	public Long getSendmessagelogId() {
		return sendmessagelogId;
	}





	public void setSendmessagelogId(Long sendmessagelogId) {
		this.sendmessagelogId = sendmessagelogId;
	}





	public String getDxnr() {
		return dxnr;
	}





	public void setDxnr(String dxnr) {
		this.dxnr = dxnr;
	}





	public Date getSendtime() {
		return sendtime;
	}





	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}





	public int getStatusId() {
		return statusId;
	}





	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}





	public User getUserId() {
		return userId;
	}





	public void setUserId(User userId) {
		this.userId = userId;
	}





	@Override
	public String toString() {
		return "SendMessageLog [sendmessagelogId=" + sendmessagelogId + "]";
	}

	
	
	
}

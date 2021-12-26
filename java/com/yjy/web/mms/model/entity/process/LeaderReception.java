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

@Table(name="aoa_Leader_Reception")
@Entity
//领导接待表
public class LeaderReception{

	@Id
	@Column(name="leaderreception_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long leaderreceptionId;//
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="pro_id")
	private ProcessList proId;
	
	//来访人数
	@Column(name="lfrs")
	private int lfrs; 
	
	//来访车辆数
	@Column(name="lfcls")
	private int lfcls; 
	
	//来访车牌号码
	@Column(name="lfcphm")
	private String lfcphm; 
	
	//来宾人员信息
	@Column(name="lfryxx")
	private String lfryxx; 
	
	//来访事由
	@Column(name="lfsy")
	private String lfsy; 
	
	//是否需要使用会议室
	@Column(name="f_issyhys")
	private int f_issyhys=0; //0 否 1 是
	
	//来访联系人
	@Column(name="lflxr")
	private String lflxr; 
	
	public String getLfcphm() {
		return lfcphm;
	}








	public void setLfcphm(String lfcphm) {
		this.lfcphm = lfcphm;
	}








	//来访联系人电话
	@Column(name="lflxrdh")
	private String lflxrdh; 

	//接待领导
	@Column(name="jdld_username")
	private String jdld_username; 
	
	//是否发送短信
	@Column(name="f_isfsdx")
	private int f_isfsdx=0; //0 否 1 是
	
	//短信内容
	@Column(name="dxnr")
	private String dxnr; 










	public int getF_issyhys() {
		return f_issyhys;
	}








	public void setF_issyhys(int f_issyhys) {
		this.f_issyhys = f_issyhys;
	}








	public int getF_isfsdx() {
		return f_isfsdx;
	}








	public void setF_isfsdx(int f_isfsdx) {
		this.f_isfsdx = f_isfsdx;
	}








	public String getDxnr() {
		return dxnr;
	}








	public void setDxnr(String dxnr) {
		this.dxnr = dxnr;
	}








	public String getJdld_username() {
		return jdld_username;
	}








	public void setJdld_username(String jdld_username) {
		this.jdld_username = jdld_username;
	}








	public String getLfryxx() {
		return lfryxx;
	}








	public void setLfryxx(String lfryxx) {
		this.lfryxx = lfryxx;
	}








	public String getLflxrdh() {
		return lflxrdh;
	}








	public void setLflxrdh(String lflxrdh) {
		this.lflxrdh = lflxrdh;
	}








	public Long getLeaderreceptionId() {
		return leaderreceptionId;
	}








	public void setLeaderreceptionId(Long leaderreceptionId) {
		this.leaderreceptionId = leaderreceptionId;
	}








	public ProcessList getProId() {
		return proId;
	}








	public void setProId(ProcessList proId) {
		this.proId = proId;
	}








	public int getLfrs() {
		return lfrs;
	}








	public void setLfrs(int lfrs) {
		this.lfrs = lfrs;
	}








	public int getLfcls() {
		return lfcls;
	}








	public void setLfcls(int lfcls) {
		this.lfcls = lfcls;
	}












	public String getLfsy() {
		return lfsy;
	}








	public void setLfsy(String lfsy) {
		this.lfsy = lfsy;
	}














	public String getLflxr() {
		return lflxr;
	}








	public void setLflxr(String lflxr) {
		this.lflxr = lflxr;
	}








	@Override
	public String toString() {
		return "LeaderReception [leaderreceptionId=" + leaderreceptionId +  ", lfrs=" + lfrs + ", lfcls=" + lfcls +
			   ", lfryxx=" + lfryxx + ", lfsy=" + lfsy + ", f_issyhys=" + f_issyhys + ", lflxr=" + lflxr +
			   ", lflxrdh=" + lflxrdh + ", jdld_username=" + jdld_username + ", f_isfsdx=" + f_isfsdx + ", dxnr=" + dxnr +
			   "]";
	}

	
	
	
}

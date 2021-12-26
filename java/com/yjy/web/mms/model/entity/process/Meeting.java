package com.yjy.web.mms.model.entity.process;

import com.yjy.web.mms.model.entity.system.SysMeetroom;

import javax.persistence.*;

@Table(name="aoa_Meeting")
@Entity
//领导接待表
public class Meeting{

	@Id
	@Column(name="meeting_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long meetingId;//
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="pro_id")
	private ProcessList proId;


	@ManyToOne
	@JoinColumn(name="sysmeetroom_id")
	private SysMeetroom sysmeetroomId;


	//会议室名称
	@Column(name="hymc")
	private String hymc;


	//出席领导
	@Column(name="cxld")
	private String cxld; 
	
	//邀请嘉宾
	@Column(name="yqjb")
	private String yqjb; 
	
	//协助人员
	@Column(name="xzry")
	private String xzry; 
	
//	//院外参加人员
//	@Column(name="ywcjry")
//	private String ywcjry; 
//	
//	//院外联系人电话
//	@Column(name="ywlxrdh")
//	private String ywlxrdh; 
//
//	//院外参加人员
//	@Column(name="ywlxr")
//	private String ywlxr; 	
	
	
	//参加人员
	@Column(name="cjrys")
	private String cjrys; 

	//是否发送短信
	@Column(name="f_isfsdx")
	private int f_isfsdx=0; //0 否 1 是
	
	//短信内容
	@Column(name="dxnr")
	private String dxnr; 
	
	//申请科室
	@Column(name="sqks")
	private String sqks; 
	
//	
//	//来访联系人电话
//	@Column(name="lflxrdh")
//	private String lflxrdh;

	public String getSqks() {
		return sqks;
	}

	public void setSqks(String sqks) {
		this.sqks = sqks;
	}

	//来访联系人电话
	@Column(name="shenuser")
	private String shenuser;

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public ProcessList getProId() {
		return proId;
	}

	public void setProId(ProcessList proId) {
		this.proId = proId;
	}

	public SysMeetroom getSysmeetroomId() {
		return sysmeetroomId;
	}

	public void setSysmeetroomId(SysMeetroom sysmeetroomId) {
		this.sysmeetroomId = sysmeetroomId;
	}

	public String getHymc() {
		return hymc;
	}

	public void setHymc(String hymc) {
		this.hymc = hymc;
	}

	public String getCxld() {
		return cxld;
	}

	public void setCxld(String cxld) {
		this.cxld = cxld;
	}

	public String getYqjb() {
		return yqjb;
	}

	public void setYqjb(String yqjb) {
		this.yqjb = yqjb;
	}

	public String getXzry() {
		return xzry;
	}

	public void setXzry(String xzry) {
		this.xzry = xzry;
	}

	public String getCjrys() {
		return cjrys;
	}

	public void setCjrys(String cjrys) {
		this.cjrys = cjrys;
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

	public String getShenuser() {
		return shenuser;
	}

	public void setShenuser(String shenuser) {
		this.shenuser = shenuser;
	}




}

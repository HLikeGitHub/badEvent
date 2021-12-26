package com.yjy.web.mms.model.entity.process;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 梁泽民
 * @info 类或者接口的作用概述，如：欢迎页面控制器
 * @email 373791230@qq.com
 */
@Entity
@Table(name="aoa_ProgramApply")
public class ProgramApply {

    @Id
    @Column(name="programApplyId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long programApplyId;


    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="pro_id")
    private ProcessList proId;


    @Column(name="applyPersonName")
    private String applyPersonName;

    @Column(name="applyPersonId")
    private String  applyPersonId;

    @Column(name="applyTime")
    private Date applyTime;

    @Column(name="programName")
    private String programName;

    @Column(name="replaceTime")
    private Date replaceTime;

    @Column(name="programRealname")
    private String programRealname;
    
    @Column(name="risk")
    private long risk;


    @Column(name="reason")
    private String reason;

    
	@Transient
	@Column(name="nameuser")
	private String nameuser;//审核人员
    
    
    public long getRisk() {
		return risk;
	}

	public void setRisk(long risk) {
		this.risk = risk;
	}

	public String getProgramRealname() {
		return programRealname;
	}

	public void setProgramRealname(String programRealname) {
		this.programRealname = programRealname;
	}

	public String getNameuser() {
		return nameuser;
	}

	public void setNameuser(String nameuser) {
		this.nameuser = nameuser;
	}

	public long getProgramApplyId() {
        return programApplyId;
    }

    public void setProgramApplyId(long programApplyId) {
        this.programApplyId = programApplyId;
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

    public String getApplyPersonId() {
        return applyPersonId;
    }

    public void setApplyPersonId(String applyPersonId) {
        this.applyPersonId = applyPersonId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Date getReplaceTime() {
        return replaceTime;
    }

    public void setReplaceTime(Date replaceTime) {
        this.replaceTime = replaceTime;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ProgramApply{" +
                "programApplyId=" + programApplyId +
                ", proId=" + proId +
                ", applyPersonName='" + applyPersonName + '\'' +
                ", applyPersonId='" + applyPersonId + '\'' +
                ", applyTime=" + applyTime +
                ", programName='" + programName + '\'' +
                ", replaceTime=" + replaceTime +
                ", risk='" + risk + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}

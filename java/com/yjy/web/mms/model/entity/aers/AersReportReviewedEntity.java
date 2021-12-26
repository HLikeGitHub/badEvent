package com.yjy.web.mms.model.entity.aers;

import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="aers_reviewed")
//审核表
public class AersReportReviewedEntity {

	@Id
	@Column(name="reviewed_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long reviewedId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User userId;//审核人
	
	private String advice; //审核人意见
	
	private Long statusId;//审核人状态
	
	@Column(name="reviewed_time")
	private Date reviewedTime;//审核时间
	
	@ManyToOne
	@JoinColumn(name="aersreport_id")
	private AersYJGWReportProcessEntity aersreportId;
	
	@Column(name="del")
	private Boolean del=false;
	
	@Transient
	private String username;//传过来的审核人的名字

	public Long getReviewedId() {
		return reviewedId;
	}

	public void setReviewedId(Long reviewedId) {
		this.reviewedId = reviewedId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Date getReviewedTime() {
		return reviewedTime;
	}

	public void setReviewedTime(Date reviewedTime) {
		this.reviewedTime = reviewedTime;
	}





	public AersYJGWReportProcessEntity getAersreportId() {
		return aersreportId;
	}

	public void setAersreportId(AersYJGWReportProcessEntity aersreportId) {
		this.aersreportId = aersreportId;
	}

	public Boolean getDel() {
		return del;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	
	
	
}

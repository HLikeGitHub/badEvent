package com.yjy.web.mms.model.entity.aers;

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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "aers_file_list")
public class AersFileList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long fileId;	//文件id
	
	@Column(name = "FILE_NAME")
	private String fileName;	//文件名字
	
	@Column(name = "URL")
	private String URL;	//文件路径
	
	private Long size;	//文件大小
	
	@Column(name = "FILE_TYPE")
	private String fileType;	//文件类型id
	
	@Column(name = "upload_time")
	private Date uploadTime;	//上传时间
	
	private String model;		//所属模块
	
	@Column(name = "file_shuffix")
	private String fileShuffix;	//文件后缀名
	
	@Column(name = "file_istrash")
	private Long fileIstrash = 0L;
	
	@Column(name = "file_isshare")
	private Long fileIsshare = 0L;

	
	@ManyToOne
	@JoinColumn(name = "file_user_id")
	private User user;			//外键关联用户表  -文件上传者



	

	public Long getFileId() {
		return fileId;
	}


	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getURL() {
		return URL;
	}


	public void setURL(String uRL) {
		URL = uRL;
	}


	public Long getSize() {
		return size;
	}


	public void setSize(Long size) {
		this.size = size;
	}





	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


	public Date getUploadTime() {
		return uploadTime;
	}


	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getFileShuffix() {
		return fileShuffix;
	}


	public void setFileShuffix(String fileShuffix) {
		this.fileShuffix = fileShuffix;
	}


	public Long getFileIstrash() {
		return fileIstrash;
	}


	public void setFileIstrash(Long fileIstrash) {
		this.fileIstrash = fileIstrash;
	}


	public Long getFileIsshare() {
		return fileIsshare;
	}


	public void setFileIsshare(Long fileIsshare) {
		this.fileIsshare = fileIsshare;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
}

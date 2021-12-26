package com.yjy.web.mms.model.entity.aers;

import javax.persistence.*;

import javax.validation.constraints.NotEmpty;

/**
 * 不良事件字典表
 *
 */
@Entity
@Table(name = "aers_dictionarieslist")
public class AersDictionariesListEntity {

	@Id
	@Column(name = "aersstatus_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long aersdictionarieId; // 字典id

	@Column(name = "aersdictionarie_name")
	@NotEmpty(message="字典名称不能为空")
	private String aersdictionarieName; // 字典名称

	@Column(name = "aersdictionarie_json")
	private String aersdictionarieJson; // 字典JSON

	public Long getAersdictionarieId() {
		return aersdictionarieId;
	}

	public void setAersdictionarieId(Long aersdictionarieId) {
		this.aersdictionarieId = aersdictionarieId;
	}

	public String getAersdictionarieName() {
		return aersdictionarieName;
	}

	public void setAersdictionarieName(String aersdictionarieName) {
		this.aersdictionarieName = aersdictionarieName;
	}

	public String getAersdictionarieJson() {
		return aersdictionarieJson;
	}

	public void setAersdictionarieJson(String aersdictionarieJson) {
		this.aersdictionarieJson = aersdictionarieJson;
	}

	
	

}

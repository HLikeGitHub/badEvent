package com.yjy.web.mms.model.entity.note;

import com.yjy.web.mms.model.entity.user.User;

import javax.persistence.*;

/**
 * 此处有一个parentid需要连接
 * @author admin
 * ---目录表----
 *
 */
@Entity
@Table(name="aoa_catalog")
public class Catalog {

	@Id
	@Column(name="catalog_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long catalogId; //目录id
	
	
	
	@Column(name="catalog_name")
	private String catalogName; //目录名字
	
	@ManyToOne
	@JoinColumn(name="cata_user_id")
	private User user;
	
	//判断id
	@Column(name="parent_id")
	private Integer parentId;
    
	
	
	
	
	@Override
	public String toString() {
		return "Catalog [catalogName=" + catalogName + "]";
	}

	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public Catalog() {
		super();
	}


	public Catalog(String catalogName, User user) {
		super();
		this.catalogName = catalogName;
		this.user = user;
	}

	
	
	
}

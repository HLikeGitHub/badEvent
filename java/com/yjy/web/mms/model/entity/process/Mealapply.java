package com.yjy.web.mms.model.entity.process;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="aoa_mealapply")
@Entity
//加班表
public class Mealapply{

	@Id
	@Column(name="mealapply_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long mealapplyId;//
	
	@Column(name="type_id")
	private Long typeId; //类型
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="pro_id")
	private ProcessList proId;
	
	@Column(name="receive_dept")
	private String receiveDept;//接待科室
	//来访请示
	@Column(name="lfqs")
	private String lfqs; 
	//来宾单位
	@Column(name="lbdw")
	private String lbdw; 
	//来宾姓名及职务
	@Column(name="lbxmjzw")
	private String lbxmjzw; 
	//来访事由
	@Column(name="lfsy")
	private String lfsy; 
	
	//工作餐：人数
	@Column(name="gzcrs")
	private int gzcrs; 
	//工作餐：金额
	@Column(name="gzcje")
	private double gzcje; 
	
	//招待餐：就餐时间
	@Column(name="zdcjcsj")
	private Date zdcjcsj;
	
	//招待餐：就餐餐段
	@Column(name="zdcjccd")
	private String zdcjccd;


	//招待餐：地点
	@Column(name="zdcdd")
	private String zdcdd; 
	//招待餐：接待人数：
	@Column(name="zdcjdrs")
	private int zdcjdrs; 
	//招待餐：陪餐人数
	@Column(name="zdcpcrs")
	private int zdcpcrs; 
	//招待餐：金额
	@Column(name="zdcje")
	private double zdcje; 
	
	//住宿：时间
	@Column(name="zszssj")
	private Date zszssj; 
	//住宿：地点
	@Column(name="zsdd")
	private String zsdd; 
	//住宿：人数
	@Column(name="zsrs")
	private int zsrs; 
	//住宿：天数
	@Column(name="zsts")
	private int zsts; 
	//住宿：金额
	@Column(name="zsje")
	private double zsje; 

	//陪餐人员：姓名
	@Column(name="pcryxm")
	private String pcryxm; 
	
	//陪餐人员：职务
	@Column(name="pcryzw")
	private String pcryzw; 
	
	//随行人数
	@Column(name="sxrs")
	private int sxrs; 
	
	@Column(name="kskz_advice")
	private String kskzAdvice;//科室科长意见及说明

	@Column(name="bgsbsy_advice")
	private String bgsbsyAdvice;//办公室办事员意见及说明

	@Column(name="bgszr_advice")
	private String bgszrAdvice;//办公室主任意见及说明

	@Column(name="zgld_advice")
	private String zgldAdvice;//主管领导意见及说明

	@Column(name="yzdwsj_advice")
	private String yzdwsjAdvice;//院长党委书记意见及说明

	@Transient
	@Column(name="nameuser")
	private String nameuser;//审核人员

	//附加清单表
//	接待清单明细
	@OneToMany(cascade=CascadeType.ALL,mappedBy="mealapply",orphanRemoval = true)
	List<Detailedlistapply>  detailedlistapplys;

	
	public String getZdcjccd() {
		return zdcjccd;
	}

	public void setZdcjccd(String zdcjccd) {
		this.zdcjccd = zdcjccd;
	}
	
	public Long getMealapplyId() {
		return mealapplyId;
	}

	public void setMealapplyId(Long mealapplyId) {
		this.mealapplyId = mealapplyId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public ProcessList getProId() {
		return proId;
	}

	public void setProId(ProcessList proId) {
		this.proId = proId;
	}

	public String getReceiveDept() {
		return receiveDept;
	}

	public void setReceiveDept(String receiveDept) {
		this.receiveDept = receiveDept;
	}

	public String getLfqs() {
		return lfqs;
	}

	public void setLfqs(String lfqs) {
		this.lfqs = lfqs;
	}

	public String getLbdw() {
		return lbdw;
	}

	public void setLbdw(String lbdw) {
		this.lbdw = lbdw;
	}

	public String getLbxmjzw() {
		return lbxmjzw;
	}

	public void setLbxmjzw(String lbxmjzw) {
		this.lbxmjzw = lbxmjzw;
	}

	public String getLfsy() {
		return lfsy;
	}

	public void setLfsy(String lfsy) {
		this.lfsy = lfsy;
	}

	public int getGzcrs() {
		return gzcrs;
	}

	public void setGzcrs(int gzcrs) {
		this.gzcrs = gzcrs;
	}

	public double getGzcje() {
		return gzcje;
	}

	public void setGzcje(double gzcje) {
		this.gzcje = gzcje;
	}

	public Date getZdcjcsj() {
		return zdcjcsj;
	}

	public void setZdcjcsj(Date zdcjcsj) {
		this.zdcjcsj = zdcjcsj;
	}

	public String getZdcdd() {
		return zdcdd;
	}

	public void setZdcdd(String zdcdd) {
		this.zdcdd = zdcdd;
	}

	public int getZdcjdrs() {
		return zdcjdrs;
	}

	public void setZdcjdrs(int zdcjdrs) {
		this.zdcjdrs = zdcjdrs;
	}

	public int getZdcpcrs() {
		return zdcpcrs;
	}

	public void setZdcpcrs(int zdcpcrs) {
		this.zdcpcrs = zdcpcrs;
	}

	public double getZdcje() {
		return zdcje;
	}

	public void setZdcje(double zdcje) {
		this.zdcje = zdcje;
	}

	public Date getZszssj() {
		return zszssj;
	}

	public void setZszssj(Date zszssj) {
		this.zszssj = zszssj;
	}

	public String getZsdd() {
		return zsdd;
	}

	public void setZsdd(String zsdd) {
		this.zsdd = zsdd;
	}

	public int getZsrs() {
		return zsrs;
	}

	public void setZsrs(int zsrs) {
		this.zsrs = zsrs;
	}

	public int getZsts() {
		return zsts;
	}

	public void setZsts(int zsts) {
		this.zsts = zsts;
	}

	public double getZsje() {
		return zsje;
	}

	public void setZsje(double zsje) {
		this.zsje = zsje;
	}

	public String getPcryxm() {
		return pcryxm;
	}

	public void setPcryxm(String pcryxm) {
		this.pcryxm = pcryxm;
	}

	public String getPcryzw() {
		return pcryzw;
	}

	public void setPcryzw(String pcryzw) {
		this.pcryzw = pcryzw;
	}

	public int getSxrs() {
		return sxrs;
	}

	public void setSxrs(int sxrs) {
		this.sxrs = sxrs;
	}

	public String getKskzAdvice() {
		return kskzAdvice;
	}

	public void setKskzAdvice(String kskzAdvice) {
		this.kskzAdvice = kskzAdvice;
	}

	public String getBgsbsyAdvice() {
		return bgsbsyAdvice;
	}

	public void setBgsbsyAdvice(String bgsbsyAdvice) {
		this.bgsbsyAdvice = bgsbsyAdvice;
	}

	public String getBgszrAdvice() {
		return bgszrAdvice;
	}

	public void setBgszrAdvice(String bgszrAdvice) {
		this.bgszrAdvice = bgszrAdvice;
	}

	public String getZgldAdvice() {
		return zgldAdvice;
	}

	public void setZgldAdvice(String zgldAdvice) {
		this.zgldAdvice = zgldAdvice;
	}

	public String getYzdwsjAdvice() {
		return yzdwsjAdvice;
	}

	public void setYzdwsjAdvice(String yzdwsjAdvice) {
		this.yzdwsjAdvice = yzdwsjAdvice;
	}

	public String getNameuser() {
		return nameuser;
	}

	public void setNameuser(String nameuser) {
		this.nameuser = nameuser;
	}

	public List<Detailedlistapply> getDetailedlistapplys() {
		return detailedlistapplys;
	}

	public void setDetailedlistapplys(List<Detailedlistapply> detailedlistapplys) {
		this.detailedlistapplys = detailedlistapplys;
	}

	@Override
	public String toString() {
		return "Mealapply [mealapplyId=" + mealapplyId + ", typeId=" + typeId  + ", nameuser=" + nameuser + "]";
	}

	

	
	
	
}

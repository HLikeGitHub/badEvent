package com.yjy.web.mms.model.dao.aersdao;


import com.yjy.web.comm.utils.DateUtil;
import com.yjy.web.mms.model.entity.aers.AersAllStatusCount;
import com.yjy.web.mms.model.entity.aers.AersReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;
import com.yjy.web.mms.model.entity.medicalmatters.MedicalMattersList;
import com.yjy.web.mms.model.entity.process.AubUser;
import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AersReportProcessDao extends PagingAndSortingRepository<AersYJGWReportProcessEntity, Long>{
	
	
	//根据申请人查找流程
//	@Query("select pro from AersYJGWReportProcessEntity as pro "
//			+ "where pro.userId.userId=?1  "
//			+ "and ((pro.applyTime between ?2 and ?3)) "// or (?2='1900-01-01')
//			+ "and (pro.status = ?4 or '全部'=?4) "
//			+ "and (pro.level = ?5 or '全部'=?5) "
//			+ "and (pro.title = ?6 or ''=?6) "
//			+ "order by pro.applyTime desc")
//	Page<AersYJGWReportProcessEntity> findByuserId(
//			Long userid
//			,Date startDate,Date endDate
//			,String status,String eventLevel,String eventCondition,
//			Pageable pa);

	//查询我的申请记录 分页
	@Query(nativeQuery=true,
			value="select shr.real_name,shks.dept_name,a.aersreport_id,a.apply_time,e.dept_name sqrdept,d.real_name sqrname,d.user_name sqrusername,a.apply_type,a.`status`,a.shenuser 	from aers_yjgwreport_process  a "
					+ "left join aers_reviewed b on a.aersreport_id=b.aersreport_id and b.status_id=23 "
					+ "left join aers_medical_safety_event c on c.aersreport_id=a.aersreport_id "
					+ "left join aoa_user d on d.user_id=a.aersreport_user_id "
					+ "left join aoa_dept e on e.dept_id=d.dept_id "
					+ "left join aoa_user shr on shr.user_id=b.user_id "
					+ "left join aoa_dept shks on shks.dept_id=shr.dept_id "
					+ "where  "
					+ "a.aersreport_user_id=?1 "
					+ "and ((a.apply_time between ?2 and ?3))  "
					+ "and (a.status = ?4 or '全部'=?4)  "
					+ "and (a.level = ?5 or '全部'=?5)  "
					+ "and (a.title = ?6 or ''=?6)  "
			)
	Page<Map<String, Object>> queryApplyPaByUserId(
			Long userid
			,Date startDate,Date endDate
			,String status,String eventLevel,String eventCondition,Pageable pa);
	
	//根据人员查询相关的上报数量
//	@Query(nativeQuery=true,value="select 1 as id," + 
//			"count(*) as allCount ,\r\n" + 
//			"ifnull(sum(case when status_id=1 then 1 else 0 end) ,0) sumDTJ,\r\n" + 
//			"ifnull(sum(case when status_id=2 then 1 else 0 end) ,0) sumYTJ,\r\n" + 
//			"ifnull(sum(case when status_id=3 then 1 else 0 end) ,0) sumYWC,\r\n" + 
//			"ifnull(sum(case when status_id=4 then 1 else 0 end) ,0) sumHT,\r\n" + 
//			"ifnull(sum(case when status_id=5 then 1 else 0 end) ,0) sumZF\r\n" + 
//			"from aers_report_process \r\n" + 
//			"WHERE \r\n" + 
//			"aersreport_user_id=?1 "
//			+ "and ((apply_time between ?2 and ?3)) "
//			+ "and (status = ?4 or ''=?4) "
//			+ "and (level = ?5 or ''=?5) "
//			+ "and (title = ?6 or ''=?6) "
//			)
//	Map<String,Object> findCountByUserId(Long userid
//			,Date startDate,Date endDate
//			,String status,String eventLevel,String eventCondition);
	
	
	//根据人员查询相关的上报总数量
	@Query(nativeQuery=true,
			value="select count(*) as allCount 	from aers_yjgwreport_process  a "
					+ "left join aers_reviewed b on a.aersreport_id=b.aersreport_id and b.status_id=23 "
					+ "left join aers_medical_safety_event c on c.aersreport_id=a.aersreport_id "
					+ "left join aoa_user d on d.user_id=a.aersreport_user_id "
					+ "left join aoa_dept e on e.dept_id=d.dept_id "
					+ "left join aoa_user shr on shr.user_id=b.user_id "
					+ "left join aoa_dept shks on shks.dept_id=shr.dept_id "
					+ "where  "
					+ "a.aersreport_user_id=?1 "
					+ "and ((a.apply_time between ?2 and ?3))  "
					+ "and (a.status = ?4 or '全部'=?4)  "
					+ "and (a.level = ?5 or '全部'=?5)  "
					+ "and (a.title = ?6 or ''=?6)  "
			
			)
	Map<String,Object> queryCountByUserId(Long userid
			,Date startDate,Date endDate
			,String status
			,String eventLevel,String eventCondition);
	

	
	//查询待审核记录 不分页
//	@Query(nativeQuery=true,
//			value="select distinct shr.real_name,shks.dept_name,a.*,d.real_name applyry,e.dept_name applyks 	from aers_yjgwreport_process  a  "
//					+ "left join aers_reviewed b on a.aersreport_id=b.aersreport_id and b.status_id=23 "
//					+ "left join aers_medical_safety_event c on c.aersreport_id=a.aersreport_id "
//					+ "left join aoa_user d on d.user_id=a.aersreport_user_id "
//					+ "left join aoa_dept e on e.dept_id=d.dept_id "
//					+ "left join aoa_user shr on shr.user_id=b.user_id "
//					+ "left join aoa_dept shks on shks.dept_id=shr.dept_id "
//					+ "where  "
//					+ "a.shenuser like %?1% "
//					+ "and ((a.apply_time between ?2 and ?3))  "
//					+ "and (b.status_id = ?4 or '0'=?4)  "
//					+ "and (a.level = ?5 or '全部'=?5)  "
//					+ "and (a.title = ?6 or ''=?6)  "
//			)
//	List<Map<String, Object>> queryAuditByUserId(
//			String userName
//			,Date startDate,Date endDate
//			,String status,String eventLevel,String eventCondition);

	//查询待审核记录 分页
	@Query(nativeQuery=true,
			value="select a.*,d.real_name applyry,e.dept_name applyks,case when DATEDIFF(DATE_FORMAT(NOW(),'%Y-%m-%d'),apply_time)>3 then '超时' else '不超时' end   isCS 	from aers_yjgwreport_process  a "
					+ "left join aers_reviewed b on a.aersreport_id=b.aersreport_id and b.status_id=23 "
					+ "left join aers_medical_safety_event c on c.aersreport_id=a.aersreport_id "
					+ "left join aoa_user d on d.user_id=a.aersreport_user_id "
					+ "left join aoa_dept e on e.dept_id=d.dept_id "
					+ "left join aoa_user shr on shr.user_id=b.user_id "
					+ "left join aoa_dept shks on shks.dept_id=shr.dept_id "
					+ "where  "
					+ "a.shenuser like %?1% "
					+ "and ((a.apply_time between ?2 and ?3))  "
					+ "and (b.status_id = ?4 or '0'=?4)  "
					+ "and (a.level = ?5 or '全部'=?5)  "
					+ "and (a.title = ?6 or ''=?6)  "
			)
	Page<Map<String, Object>> queryAuditPaByUserId(
			String userName
			,Date startDate,Date endDate
			,String status,String eventLevel,String eventCondition,Pageable pa);
	
	
	//根据人员查询相关的审核总数量
	@Query(nativeQuery=true,
			value="select count(*) as allCount 	from aers_yjgwreport_process  a "
					+ "left join aers_reviewed b on a.aersreport_id=b.aersreport_id "
					+ "left join aers_medical_safety_event c on c.aersreport_id=a.aersreport_id "
					+ "left join aoa_user d on d.user_id=a.aersreport_user_id "
					+ "left join aoa_dept e on e.dept_id=d.dept_id "
					+ "where  "
					+ "a.shenuser like %?1% "
					+ "and ((a.apply_time between ?2 and ?3))  "
					+ "and (b.status_id = ?4 or '0'=?4)  "
					+ "and (a.level = ?5 or '全部'=?5)  "
					+ "and (a.title = ?6 or ''=?6)  "
			
			)
	Map<String,Object> queryAuditCountByUserId(String userName
			,Date startDate,Date endDate
			,String status
			,String eventLevel,String eventCondition);
	
	
	
	@Query("select pro from AersYJGWReportProcessEntity as pro where  pro.aersreportId=?1")
	AersYJGWReportProcessEntity findbyAersreportId(Long proid);
	
}

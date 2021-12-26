package com.yjy.web.mms.model.dao.processdao;


import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface ProcessListDao extends PagingAndSortingRepository<ProcessList, Long>{
	
	//根据申请人查找流程
	@Query("select pro from ProcessList as pro where pro.userId.userId=?1 or (pro.typeNmae='领导接待'  and 3L=?2 ) order by pro.applyTime desc")
	Page<ProcessList> findByuserId(Long userid,Long roleId,Pageable pa);

	
	//根据申请人查找流程
//	@Query(nativeQuery=true,value="select a.* from aoa_process_list a "
//	        + "left join aoa_user b on a.process_user_id=b.user_id "
//			+ " where (type_name='领导接待'  and 3=?2) or b.user_id=?1 ")
//	List<ProcessList> findByUserAndRole(Long userid,Long roleid,Pageable pa);
	
	//根据申请人和审核人查找流程
	@Query(nativeQuery=true,value="select * from aoa_process_list  where aoa_process_list.process_user_id=?1 ORDER BY aoa_process_list.apply_time DESC LIMIT 0,3")
	List<ProcessList> findlastthree(long userid);
	//根据状态和申请人查找流程
	@Query("select pro from ProcessList as pro where pro.userId.userId=?1 and pro.statusId=?2 order by pro.applyTime desc")
	Page<ProcessList> findByuserIdandstatus(Long userid, Long statusId, Pageable pa);
	
	//根据审核人，类型，标题模糊查询
	@Query("select pro from ProcessList as pro where pro.userId.userId=?1 and (pro.typeNmae like %?2% or pro.processName like %?2% or pro.shenuser like %?2%) order by pro.applyTime desc")
	Page<ProcessList> findByuserIdandstr(Long userid, String val, Pageable pa);

	@Query("select pro from ProcessList as pro where pro.userId.userId=?1 and pro.processId=?2")
	ProcessList findbyuseridandtitle(Long userid,Long proid);

	@Query("select pro from ProcessList as pro where  pro.processId=?1")
	ProcessList findbyuseridandtitle(Long proid);
	
	
	
	
//	@Query("select pro from ProcessList as pro where ( pro.startTime between ?1 and ?2) or ( pro.endTime between ?1 and ?2)")
//	List<ProcessList> findbystartTimeAndendTime(Date startTime,Date endTime);
//	

	


	//根据开始时间结束时间 接待领导查找记录
	@Query(value="select  pro.*   from aoa_process_list as pro,aoa_leader_reception as leader "
			+ "where pro.process_id=leader.pro_id "
			+ "and (( pro.start_time between ?1 and ?2) or ( pro.end_time between ?1 and ?2)) "
			+ "and leader.jdld_username=?3 "
			+ "and pro.process_id<>?4 ",nativeQuery=true)//
	List<ProcessList> findbystartTimeAndendTimeAndLeader(Date startTime,Date endTime,String jdldRealname,long proId);//
	
	//根据开始时间结束时间 查询会议室是否占用
	@Query(value="select  pro.*   from aoa_process_list as pro,aoa_leader_reception as leader "
			+ "where pro.process_id=leader.pro_id "
			+ "and (( pro.start_time between ?1 and ?2) or ( pro.end_time between ?1 and ?2)) "
			+ "and leader.f_issyhys=1 "
			+ "and pro.process_id<>?3 "
			,nativeQuery=true)
	List<ProcessList> findbystartTimeAndendTimeAndHys(Date startTime,Date endTime,long proId);


	//根据开始时间结束时间 查询会议室是否占用
	@Query(value="select  pro.*   from aoa_process_list as pro,aoa_meeting as met,aoa_sysmeetroom as room "
			+ "where pro.process_id=met.pro_id and met.sysmeetroom_id=room.sysmeetroom_id  "
			+ "and (( pro.start_time between ?1 and ?2) or ( pro.end_time between ?1 and ?2)) "
			+ "and pro.process_id<>?3 "
			+ "and room.hysmc=?4"
			,nativeQuery=true)
	List<ProcessList> findbystartTimeAndendTimeAndAllHys(Date startTime,Date endTime,long proId,String hysmc);
	
	
	
	
	
//	@Query("select pro from Mealapply as mealapply "
//			+ " left join Detailedlistapply as detailedlistapply  on mealapply.mealapplyid=detailedlistapply.mealapply.mealapplyid "
//			+ " left join ProcessList as pro on pro.processId=detailedlistapply.proId.processId"
//			+ " where mealapply.mealapplyid=?1 ")
//	@Query("select mealapply from Meal as mealapply  where mealapply.mealId=?1 ")
//	List<Mealapply> findbymealapplyid(Long mealapplyid);
	
	
}

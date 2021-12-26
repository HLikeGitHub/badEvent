package com.yjy.web.mms.model.dao.deanroundsdao;

import java.util.List;

import com.yjy.web.mms.model.entity.deanrounds.DeanroundsDetailList;
import com.yjy.web.mms.model.entity.deanrounds.Deanroundslist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeanroundsDao extends JpaRepository<Deanroundslist, Long>{
  
	
	
	//查找是否置顶的xx
	
	Page<Deanroundslist> findAll( Pageable page);
	
	
	@Query("from Deanroundslist t where t.deanroundsId =?1")
	Deanroundslist findDeansBydeanroundsId(Long deanroundsId);
	
	
	@Query("from DeanroundsDetailList t where t.deanId.deanroundsId =?1")
	List<DeanroundsDetailList> findDeansdetailBydeanroundsId(Long deanId);
	

	//类型排序
//	Page<Deanroundslist> findAllOrderByTypeId(Pageable page);
	
	//时间排序
//	Page<Deanroundslist> findAllOrderByPublishTimeDesc(Pageable pa);
	
	
	//根据用户id 和title的模糊查询
//	@Query("select tl from Deanroundslist tl where  tl.title like %:title%")
//	Page<Deanroundslist> findByTitleLikeAndUsersId(@Param("title")String title,Pageable pa);
	
	
	
	/*
	//查找任务完成的用户
	@Query(nativeQuery=true, value="SELECT COUNT(*) from aoa_task_list where aoa_task_list.status_id=?1 and aoa_task_list.task_push_user_id=?2 ")
	Integer countfinish(Long status,Long userid);
	
	
	@Query("update Deanroundslist ta set ta.statusId=:statusid where ta.taskId=:taskid")
	@Modifying
	int update(@Param("taskid")Long taskid,@Param("statusid")Integer statusid);
	


	
	
	
	//根据任务id和发布时间的模糊查询
	@Query("select tl from Deanroundslist tl where tl.taskId=:taskid and tl.publishTime like  %:title%")
	Deanroundslist findByPublishTimeLikeAndTaskId(@Param("taskid")Long taskid,@Param("title")String title);
	

	
	//状态排序
	Page<Deanroundslist> findByUsersIdOrderByStatusId(User userId,Pageable pa);
	


	@Query("from Deanroundslist t where t.taskId in (?1)")
	Page<Deanroundslist> findTaskByTaskIds(List<Long> taskids,Pageable pa);

	//根据typeid和taskid找任务
	@Query("from Deanroundslist t where t.typeId = ?1  and t.taskId in (?2)")
	Page<Deanroundslist> findtaskTypeIdAndTaskId(Long typeId, List<Long> taskids,Pageable pa);


	//根据statusid和taskid找任务
	@Query("from Deanroundslist t where t.statusId = ?1  and t.taskId in (?2)")
	Page<Deanroundslist> findtaskStatusIdAndCancelAndTaskId(Integer statusId,List<Long> taskids,Pageable pa);


	//根据用户对象和taskid找任务
	@Query("from Deanroundslist t where t.usersId	= ?1  and t.taskId in (?2)")
	Page<Deanroundslist> findtaskUsersIdAndTaskId(User user, List<Long> taskids,Pageable pa);


	@Query("from Deanroundslist t where t.cancel = ?1  and t.taskId in (?2)")
	Page<Deanroundslist> findtaskCancelAndTaskId(Boolean b, List<Long> taskids,Pageable pa);

	//根据任务id和title的模糊查询
	@Query("from Deanroundslist tl where tl.taskId in (?1) and tl.title like  %?2%")
	Page<Deanroundslist> findtaskByTitleLikeAndTaskId(List<Long> taskids,String title,Pageable pa);




	Page<Deanroundslist> findByTickingIsNotNull(Pageable pa);
	/*
	
	*/
	
}

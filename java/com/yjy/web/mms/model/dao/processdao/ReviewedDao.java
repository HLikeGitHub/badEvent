package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.AubUser;
import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.Reviewed;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReviewedDao extends PagingAndSortingRepository<Reviewed, Long>{

	//根据审核人查找流程
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and rev.del=?2 order by rev.statusId")
	Page<AubUser> findByUserIdOrderByStatusId(User user, Boolean bo, Pageable pa);
	
	//根据申请人和审核人查找流程
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and pro.userId=?2 and rev.del=?3 order by rev.statusId")
	Page<AubUser> findprocesslist(User user,User u,Boolean bo,Pageable pa);

	//winper001根据申请人查找出入境申请流程：【适合管理员】
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,pro.statusId) "
			+ "from ProcessList as pro where pro.userId=?1 and pro.typeNmae='出入境申请' order by pro.statusId")
	Page<AubUser> findprocesslist_admin(User user,Pageable pa);

	//winper001根据审核人和申请人查找出入境申请流程：【适合审核者】
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and pro.userId=?2 and rev.del=?3 and pro.typeNmae='出入境申请' order by rev.statusId")
	Page<AubUser> findprocesslist_checker(User user,User u,Boolean bo,Pageable pa);


	//根据状态和审核人查找流程
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and rev.statusId=?2 and rev.del=?3 order by rev.statusId")
	Page<AubUser> findbystatusprocesslist(User user,Long statusid,Boolean bo,Pageable pa);


	//winper001根据状态查找出入境流程【适合管理员】
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,pro.statusId) "
			+ "from ProcessList as pro where pro.statusId=?1 and pro.typeNmae='出入境申请' order by pro.statusId")
	Page<AubUser> findbystatusprocesslist_admin(Long statusid,Pageable pa);

	//winper001根据状态和审核人查找出入境流程【适合审核者】
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and rev.statusId=?2 and rev.del=?3 and pro.typeNmae='出入境申请' order by rev.statusId")
	Page<AubUser> findbystatusprocesslist_checker(User user,Long statusid,Boolean bo,Pageable pa);

	//根据类型名和审核人查找流程
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and pro.typeNmae=?2 and rev.del=?3 ")
	Page<AubUser> findbytypenameprocesslist(User user,String typename,Boolean bo,Pageable pa);

	//winper001根据类型名和审核人查找出入境流程【适合审核者】
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and pro.typeNmae=?2 and rev.del=?3 order by rev.statusId")
	Page<AubUser> findbytypenameprocesslist_checker(User user,String typename,Boolean bo,Pageable pa);

	//winper001根据类型名查找出入境流程【适合管理员】
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,pro.statusId) "
			+ "from ProcessList as pro where pro.typeNmae=?1 order by pro.statusId")
	Page<AubUser> findbytypenameprocesslist_admin(String typename,Pageable pa);


	//根据标题和审核人查找流程
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and pro.processName like %?2% and rev.del=?3 order by rev.statusId")
	Page<AubUser> findbyprocessnameprocesslist(User user,String processname,Boolean bo,Pageable pa);


	//winper001根据标题查找出入境流程【适合管理员】
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,pro.statusId) "
			+ "from ProcessList as pro where pro.processName like %?1% and pro.typeNmae='出入境申请' order by pro.statusId")
	Page<AubUser> findbyprocessnameprocesslist_admin(String processname,Pageable pa);


	//winper001根据标题和审核人查找出入境流程
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,rev.statusId) "
			+ "from ProcessList as pro,Reviewed as rev where rev.proId.processId=pro.processId and rev.userId=?1 and pro.processName like %?2% and rev.del=?3 and pro.typeNmae='出入境申请' order by rev.statusId")
	Page<AubUser> findbyprocessnameprocesslist_checker(User user,String processname,Boolean bo,Pageable pa);

	//查看所有的流程申请（数据查询的申请）
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,pro.statusId) "
			+ "from ProcessList as pro where  pro.typeNmae='数据查询'  ")
	Page<AubUser> findAllOrderByStatusId(User user,Boolean bo, Pageable pa);


	//winper001查看所有的流程申请（出入境申请）
	@Query("select new com.yjy.web.mms.model.entity.process.AubUser(pro.processId,pro.typeNmae,pro.deeply,pro.processName,pro.userId.userName,pro.applyTime,pro.statusId) "
			+ "from ProcessList as pro where pro.typeNmae='出入境申请' order by pro.statusId desc,pro.applyTime desc")
	Page<AubUser> findAllOrderByStatusIdPassportApply(User user,Boolean bo, Pageable pa);






	
	List<Reviewed> findByReviewedTimeNotNullAndProId(ProcessList pro);
	
	@Query(" select re from Reviewed as re where re.proId.processId=?1 and re.userId=?2")
	Reviewed findByProIdAndUserId(Long pro,User u);


	@Query(" select re from Reviewed as re where re.proId.processId=?1 ")
	List<Reviewed> findByProId(Long pro);
}

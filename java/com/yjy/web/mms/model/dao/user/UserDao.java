package com.yjy.web.mms.model.dao.user;

import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long>{
    
	Page<User> findByUserId(Long id,Pageable pa);
	
	
	List<User>  findByUserId(Long id);
	
	List<User>  findByFatherId(Long parentid);
	
	Page<User> findByFatherId(Long parentid,Pageable pa);
	
	//名字模糊查找
	@Query("select u from User u where  (u.userName like %?1% or u.realName like %?1%) and u.fatherId=?2 ")
	Page<User> findbyFatherId(String name,Long parentid,Pageable pa);
	
	@Query("select u from User u where u.userName=:name")
	User findid(@Param("name")String name);
	
	@Query("select tu.pkId from Taskuser tu where tu.taskId.taskId=:taskid and tu.userId.userId=:userid")
	Long findpkId(@Param("taskid")Long taskid,@Param("userid")Long userid);
	
	//根据名字找用户
	User findByUserName(String title);
	
	//根据用户名模糊查找
	@Query("from User u where u.userName like %:name% or u.realName like %:name%")
	Page<User> findbyUserNameLike(@Param("name")String name,Pageable pa);
	
	//根据条件模糊查找姓名或科室
	@Query("from User u where u.userName like %:name% or u.dept.deptName like %:name% or u.realName like %:name%")
	Page<User> findbyUserNameorDeptNameLike(@Param("name")String name,Pageable pa);
	
	//根据真实姓名模糊查找
	Page<User> findByrealNameLike(String title,Pageable pa);
	
	//根据姓名首拼模糊查找，并分页
	Page<User> findByPinyinLike(String pinyin,Pageable pa);
	
	//根据姓名首拼+查找关键字查找(部门、姓名、电话号码)，并分页
	@Query("from User u where (u.userName like ?1 or u.dept.deptName like ?1 or u.userTel like ?1 or u.position.name like ?1) and u.pinyin like ?2")
	Page<User> findSelectUsers(String baseKey,String pinyinm,Pageable pa);
	
	//根据姓名首拼+查找关键字查找(部门、姓名、电话号码)，并分页
	@Query("from User u where u.userName like ?1 or u.dept.deptName like ?1 or u.userTel like ?1 or u.position.name like ?1 or u.pinyin like ?2")
	Page<User> findUsers(String baseKey,String baseKey2,Pageable pa);
	/**
	 * 用户管理查询可用用户
	 * @param isLock
	 * @param pa
	 * @return
	 */
	Page<User> findByIsLock(Integer isLock,Pageable pa);
	
	
	@Query("from User u where u.dept.deptName like %?1% or u.userName like %?1% or u.realName like %?1% or u.userTel like %?1% or u.role.roleName like %?1%  ORDER BY salary ")
	Page<User> findnamelike(String name,Pageable pa);
	
	List<User> findByDept(Dept dept);
	@Query("select u from User u where u.role.roleId=?1  ORDER BY salary ")
	List<User> findrole(Long lid); 
	
	/*通过（用户名或者电话号码）+密码查找用户*/
	@Query("from User u where (u.userName = ?1 or u.userTel = ?1) and u.password =?2  ORDER BY salary ")
	User findOneUser(String userName,String password);

	/*查找会议室管理员*/
	@Query("select u from User u where u.is_hys=1  ORDER BY salary ")
	List<User> findbyhys();

	/*通过用户权限找上级用户*/
	@Query("from User u where u.position.id =?1  ORDER BY salary ")
	Page<User> findByUserFatherId(Long id,Pageable pa);
	
	/*通过用户权限找上级用户*/
	@Query("from User u where u.position.id =?1 and u.dept.deptId =?2   ORDER BY salary ")
	Page<User> findByUserDeptFather(Long id,Long deptId,Pageable pa);

	/*通过用户权限找上级用户*/
	@Query("from User u where u.position.id =?1 or u.position.id =?2 or u.superman=1  ORDER BY salary ")
	Page<User> findByUserFatherIds(Long id1,Long id2,Pageable pa);
	
	/*级别和真实名称查找病人*/
	@Query("from User u where (u.position.id <>7) and u.realName =?1")
	User findByRealNameOfLeader(String realName);
	
	/*级别和真实名称查找r人员*/
	@Query("from User u where u.dept.deptId IN (103,106,107,109) AND u.position.id=6 ORDER BY salary ")//人力资源科、医务科、护理部、科教科且是科长
	Page<User> findByLcks(Pageable pa);
	
	/*级别和真实名称查找r人员*/
	@Query("from User u where u.dept.deptId IN (120) AND u.position.id=6 ORDER BY salary ") //网络信息科长
	Page<User> findByWLXXK(Pageable pa);
	
	/*查找所有用户*/
	@Query("from User u order by salary ")
	Page<User> findByUser(Pageable pa);
	
	User findByUserId(long userId);
	/*通过用户权限找上级用户*/
	@Query("select u from User u where u.userId=?1")
	List<User> findByUId(Long id);



/*winper001：人力资源科科长*/
	@Query("from User u where u.dept.deptId IN (103) AND u.position.id=6")
	Page<User> findByHrChief(Pageable pa);

/*winper001：除人力资源科科长之外的所有科长/主任*/
	@Query("from User u where u.dept.deptId NOT IN (103) AND u.position.id=6")
	Page<User> findByAllChiefButHr(Pageable pa);

/*人力资源科的普通科员winper001*/
	@Query("from User u where u.dept.deptId IN (103) AND u.position.id <>6")
	Page<User> findByHrMember(Pageable pa);

/*分管院长列表winper001*/
	@Query("from User u where u.position.id=3 and u.userName<>'朱建华' and u.userName<>'554' and u.userName<>'957'")
	Page<User> findByVicePresident(Pageable pa);
/*院长纪委书记*/
	@Query("from User u where u.ryzb='院长/纪委书记'")
 	Page<User> findBigBoss(Pageable pa);

/*所有普通职工，副科长（人事科除外）的上级：是对应的正科长*/
	@Query("from User u where u.position.id =?1 and u.dept.deptId=?2 and (u.userType='职能正科长' or u.userType='临床正科长') ORDER BY salary")
	Page<User> findByUserFatherIdAndDeptId(Long id,Long deptId,Pageable pa);

	
	
	/*医院管理流程审批*/
	//查找医务科科长
	@Query("select u from User u where u.dept.deptId=106  ORDER BY salary ")
	Page<User> findbyywk(Pageable pa);
	
	
	
}



package com.yjy.web.mms.model.dao.user;

import com.yjy.web.mms.model.entity.user.Dept;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeptDao extends PagingAndSortingRepository<Dept, Long>{

	List<Dept> findByDeptId(Long id);
	
	
	@Query("select de.deptName from Dept de where de.deptId=:id")
	String findname(@Param("id")Long id);
	
	@Query("select de from Dept de where de.lcks='职能科室'")
	List<Dept>  findznks();
	
	@Query("select de from Dept de where de.deptName=?1")
	Dept  findbydeptName(String deptName);
	
	@Query("select de from Dept de where de.lcks='职能科室'")
	Page<Dept> findznks(Pageable pa);
	
	
	//根据开始时间结束时间 接待领导查找记录
	@Query(value="select distinct a.* from aoa_dept a "
			+ "left join aoa_dept_responds b on a.dept_id=b.dept_id "
			+ "left join aoa_deanrounds_detail d on b.dean_detail_id=d.deanrounds_detail_id "
			+ "left join aoa_dept_suggestion c  on a.dept_id=c.dept_id "
			+ "where (b.dept_id is not null or c.dept_id is not null) and (c.dean_id=?1 or d.dean_id=?1) ",nativeQuery=true)//
	List<Dept> findbydeanrounds(String dean_id);//
	
}

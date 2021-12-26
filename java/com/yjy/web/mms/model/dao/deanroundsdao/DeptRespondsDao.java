package com.yjy.web.mms.model.dao.deanroundsdao;

import java.util.List;

import com.yjy.web.mms.model.entity.deanrounds.DeptResponds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeptRespondsDao extends JpaRepository<DeptResponds, Long>{
  
	
	
	//查找是否置顶的xx
	
	Page<DeptResponds> findAll( Pageable page);
	
//	List<DeptSuggestion> findBydeanId(Long deanId);
	
	@Query("from DeptResponds t where t.deanDetailId.deanroundsDetailId =?1")
	List<DeptResponds> findDeptResByDeanIds(Long deanroundsDetailId);
	
	@Query("from DeptResponds t where t.deanDetailId.deanroundsDetailId =?1 and t.dept.deptId = ?2")
	List<DeptResponds> findDeptResByDeanIds(Long deanroundsDetailId,Long deptId);
	
	
}

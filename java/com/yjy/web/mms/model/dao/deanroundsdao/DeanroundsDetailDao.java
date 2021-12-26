package com.yjy.web.mms.model.dao.deanroundsdao;

import java.util.List;

import com.yjy.web.mms.model.entity.deanrounds.DeanroundsDetailList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeanroundsDetailDao extends JpaRepository<DeanroundsDetailList, Long>{
  
	@Query("from DeanroundsDetailList t where t.deanroundsDetailId =?1")
	DeanroundsDetailList findDeansBydeanroundsDetailId(Long deanroundsId);
	
	@Query("from DeanroundsDetailList t where t.deanId.deanroundsId =?1")
	List<DeanroundsDetailList> findDeansBydeanId(Long deanId);
	
	
}

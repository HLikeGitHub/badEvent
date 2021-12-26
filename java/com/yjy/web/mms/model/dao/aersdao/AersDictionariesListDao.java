package com.yjy.web.mms.model.dao.aersdao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.yjy.web.mms.model.entity.aers.AersDictionariesListEntity;

@Repository
public interface AersDictionariesListDao extends PagingAndSortingRepository<AersDictionariesListEntity, Long>{
	
	//根据字典名称找到字典JSON
	AersDictionariesListEntity findByAersdictionarieName(String aersdictionarieName);
	
	

}

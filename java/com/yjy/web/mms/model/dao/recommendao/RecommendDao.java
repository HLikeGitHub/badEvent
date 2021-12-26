package com.yjy.web.mms.model.dao.recommendao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.yjy.web.mms.model.entity.aers.AersDictionariesListEntity;
import com.yjy.web.mms.model.entity.recommend.RecommendUser;
import com.yjy.web.mms.model.entity.task.Tasklist;
import com.yjy.web.mms.model.entity.user.User;

@Repository
public interface RecommendDao extends JpaRepository<RecommendUser, Long>{
  
	RecommendUser findByRecomUserId(Long recomUserId);
	
	//Page<RecommendUser> findAll(Pageable page);
	
	
}



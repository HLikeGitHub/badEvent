package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Mealapply;
import com.yjy.web.mms.model.entity.process.ProcessList;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MealApplyDao extends PagingAndSortingRepository<Mealapply, Long>{

	Mealapply findByProId(ProcessList pro);
	
	
	Mealapply findBymealapplyId(Long id);
}

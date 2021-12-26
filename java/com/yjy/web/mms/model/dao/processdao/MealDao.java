package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Meal;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MealDao extends PagingAndSortingRepository<Meal, Long>{

	List<Meal> findByMealId(Long id);
}

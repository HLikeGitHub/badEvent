package com.yjy.web.mms.model.entity.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="aoa_meal")
public class Meal{

	@Id
	@Column(name="meal_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long mealId;//


	public Long getMealId() {
		return mealId;
	}


	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}


	@Override
	public String toString() {
		return "Meal [mealId=" + mealId +  "]";
	}

	

	
	
	
}

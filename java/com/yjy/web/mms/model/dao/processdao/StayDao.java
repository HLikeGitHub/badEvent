package com.yjy.web.mms.model.dao.processdao;

import java.util.List;

import com.yjy.web.mms.model.entity.process.EvectionMoney;
import com.yjy.web.mms.model.entity.process.Stay;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StayDao extends PagingAndSortingRepository<Stay, Long>{
 
	List<Stay> findByEvemoney(EvectionMoney money);
}

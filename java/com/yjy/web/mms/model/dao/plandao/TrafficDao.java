package com.yjy.web.mms.model.dao.plandao;

import com.yjy.web.mms.model.entity.process.EvectionMoney;
import com.yjy.web.mms.model.entity.process.Traffic;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TrafficDao extends PagingAndSortingRepository<Traffic, Long>{

	List<Traffic> findByEvection(EvectionMoney money);
}

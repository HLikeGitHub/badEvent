package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Detailedlistapply;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DetailedlistapplyDao extends PagingAndSortingRepository<Detailedlistapply, Long>{

	Detailedlistapply findByProId(ProcessList process);
	
	

}

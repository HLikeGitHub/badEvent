package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.LeaderReception;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LeaderReceptionDao extends PagingAndSortingRepository<LeaderReception, Long>{

	LeaderReception findByProId(ProcessList pro);

	
}

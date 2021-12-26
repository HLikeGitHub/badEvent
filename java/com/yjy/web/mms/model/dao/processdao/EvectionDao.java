package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Evection;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EvectionDao extends PagingAndSortingRepository<Evection, Long> {

	Evection findByProId(ProcessList process);

}

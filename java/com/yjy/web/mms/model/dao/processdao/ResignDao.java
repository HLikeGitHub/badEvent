package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.Resign;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResignDao extends PagingAndSortingRepository<Resign, Long>{

	Resign findByProId(ProcessList process);

}

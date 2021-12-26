package com.yjy.web.mms.model.dao.processdao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yjy.web.mms.model.entity.process.DataQueryapply;
import com.yjy.web.mms.model.entity.process.ProcessList;

public interface DataQueryApplyDao extends PagingAndSortingRepository<DataQueryapply, Long>{

	DataQueryapply findByProId(ProcessList pro);

}

package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Overtime;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OvertimeDao extends PagingAndSortingRepository<Overtime, Long>{

	Overtime findByProId(ProcessList pro);

}

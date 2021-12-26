package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Holiday;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HolidayDao extends PagingAndSortingRepository<Holiday, Long>{

	Holiday findByProId(ProcessList pro);

}

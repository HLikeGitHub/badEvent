package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.Regular;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegularDao extends PagingAndSortingRepository<Regular, Long>{

	Regular findByProId(ProcessList pro);

}

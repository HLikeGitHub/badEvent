package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Detailedlistapply;
import com.yjy.web.mms.model.entity.process.DetailsList;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DetailsListDao extends PagingAndSortingRepository<DetailsList, Long>{

	List<DetailsList> findBydetailedlists(Detailedlistapply bu);
}

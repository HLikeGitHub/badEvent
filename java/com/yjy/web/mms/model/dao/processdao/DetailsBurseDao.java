package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Bursement;
import com.yjy.web.mms.model.entity.process.DetailsBurse;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DetailsBurseDao extends PagingAndSortingRepository<DetailsBurse, Long>{

	List<DetailsBurse> findByBurs(Bursement bu);
}

package com.yjy.web.mms.model.dao.aersdao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.yjy.web.mms.model.entity.aers.AersReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.MedCareUnOpEventReportEntity;

public interface MedCareUnOpEventDao extends PagingAndSortingRepository<MedCareUnOpEventReportEntity, Long>{
	MedCareUnOpEventReportEntity findByAersreportId(AersReportProcessEntity aersReportProcessEntity);

	
}

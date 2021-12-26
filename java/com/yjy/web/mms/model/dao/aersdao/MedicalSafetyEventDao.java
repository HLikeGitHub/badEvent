package com.yjy.web.mms.model.dao.aersdao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.yjy.web.mms.model.entity.aers.AersReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.MedCareUnOpEventReportEntity;
import com.yjy.web.mms.model.entity.aers.MedicalSafetyEventReportEntity;

public interface MedicalSafetyEventDao extends PagingAndSortingRepository<MedicalSafetyEventReportEntity, Long>{
	MedicalSafetyEventReportEntity findByAersyjgwreportId(AersYJGWReportProcessEntity aersYJGWReportProcessEntity);

	MedicalSafetyEventReportEntity findByMedicalSafetyEventId(Long medicalSafetyEventId);
}

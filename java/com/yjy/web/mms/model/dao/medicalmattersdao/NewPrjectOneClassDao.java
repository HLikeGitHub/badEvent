package com.yjy.web.mms.model.dao.medicalmattersdao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.yjy.web.mms.model.entity.medicalmatters.MedicalMattersList;
import com.yjy.web.mms.model.entity.medicalmatters.NewPrjectOneClass;
import com.yjy.web.mms.model.entity.process.Meeting;
import com.yjy.web.mms.model.entity.process.ProcessList;

public interface NewPrjectOneClassDao extends PagingAndSortingRepository<NewPrjectOneClass, Long>{
	NewPrjectOneClass findByMedicalmattersId(MedicalMattersList medicalMattersList);

	
}

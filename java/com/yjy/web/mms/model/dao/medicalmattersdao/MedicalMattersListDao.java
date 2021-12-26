package com.yjy.web.mms.model.dao.medicalmattersdao;


import com.yjy.web.mms.model.entity.medicalmatters.MedicalMattersList;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface MedicalMattersListDao extends PagingAndSortingRepository<MedicalMattersList, Long>{
	
	@Query("select pro from MedicalMattersList as pro where  pro.medicalmattersId=?1")
	MedicalMattersList findbyMedicalmattersId(Long proid);
	
}

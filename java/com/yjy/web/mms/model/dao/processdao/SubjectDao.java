package com.yjy.web.mms.model.dao.processdao;

import java.util.List;

import com.yjy.web.mms.model.entity.process.Subject;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SubjectDao extends PagingAndSortingRepository<Subject, Long>{

	List<Subject> findByParentId(Long id);
	
	List<Subject> findByParentIdNot(Long id);
	
	
}

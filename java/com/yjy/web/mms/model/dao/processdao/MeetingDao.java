package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.Meeting;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MeetingDao extends PagingAndSortingRepository<Meeting, Long>{

	Meeting findByProId(ProcessList pro);

	
}

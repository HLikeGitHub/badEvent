package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.SendMessageLog;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SendMessageLogDao extends PagingAndSortingRepository<SendMessageLog, Long>{

	List<SendMessageLog> findByProId(ProcessList process);
}

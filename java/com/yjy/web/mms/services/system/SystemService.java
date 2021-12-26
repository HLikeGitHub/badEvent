package com.yjy.web.mms.services.system;

import java.util.Date;

import javax.transaction.Transactional;

import com.yjy.web.mms.model.dao.system.SysMeetroomDao;
import com.yjy.web.mms.model.entity.system.SysMeetroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SystemService {
 
	@Autowired
    SysMeetroomDao sysMeetroomDao;
	
	//删除
	public Integer delete(long pid) {
		return null;

		
	}
	
	//分页
	public Page<SysMeetroom> paging(int page, String baseKey, long userid, Object type, Object status, Object time){
		return null;

		
	}
	
	public Integer updateplan(long typeId,long statusId,Date startTime,Date endTime,
			String title,String label,String planContent,String planSummary,long pid) {
				return null;
		
		
	}
}

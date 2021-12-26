package com.yjy.web.mms.model.dao.daymanagedao;

import java.util.List;

import com.yjy.web.mms.model.entity.schedule.ScheduleList;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaymanageDao extends JpaRepository<ScheduleList,Long>{
	
	List<ScheduleList> findByUser(User user);
	
	List<ScheduleList> findByUsers(List<User> users);
	
	Page<ScheduleList> findByUsers(List<User> users,Pageable pa);
	
	Page<ScheduleList> findByUser(User user,Pageable pa);
	
	Page<ScheduleList> findByUserAndUsers(User user,List<User> users,Pageable pa);
}

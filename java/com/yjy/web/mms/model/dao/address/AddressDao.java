package com.yjy.web.mms.model.dao.address;


import com.yjy.web.mms.model.entity.note.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressDao extends JpaRepository<Director, Long> {

	//根据姓名首拼模糊查询
	
}

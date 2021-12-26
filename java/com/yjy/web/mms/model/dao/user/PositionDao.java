package com.yjy.web.mms.model.dao.user;

import com.yjy.web.mms.model.entity.user.Position;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PositionDao extends PagingAndSortingRepository<Position, Long>{

//	@Query("select po.name from Position po where po.id=:id")
//	Position findById(@Param("id")Long id);
	
	List<Position> findByDeptidAndNameNotLike(Long deptid,String name);
	
	List<Position> findByDeptidAndNameLike(Long deptid,String name);

	List<Position> findByDeptid(Long deletedeptid);

	@Query("from Position")
	List<Position> findAllPostion();
}

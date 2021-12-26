package com.yjy.web.mms.model.dao.system;

import com.yjy.web.mms.model.entity.system.SysMeetroom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMeetroomDao extends PagingAndSortingRepository<SysMeetroom, Long>{
	
	
//	//根据模块名和名称查找唯一对象
//	SystemTypeList findByTypeModelAndTypeName(String typeModel,String typeName);
//	
//	//根据模块名查找到类型集合
//	List<SystemTypeList> findByTypeModel(String typeModel);
//	
//	List<SystemTypeList> findByTypeNameLikeOrTypeModelLike(String name,String name2);
//	
//

    @Query("select mtr from SysMeetroom as mtr where  mtr.statusId=?1 ")
    List<SysMeetroom> findbyState(@Param("id")Long id);

    @Query("select mtr from SysMeetroom as mtr where  mtr.hysmc=?1 ")
    List<SysMeetroom> findbyHysmc(String hysmc);

//	@Query(value="select * from aoa_sysmeetroom where status_id=?1",nativeQuery=true)
//	  List<SysMeetroom> findbyState(@Param("id")Long id);


//	List<SysMeetroom> findbyState(@Param("id")Long id);
	
}

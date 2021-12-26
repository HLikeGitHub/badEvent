package com.yjy.web.mms.model.dao.processdao;


import com.yjy.web.mms.model.entity.process.PassportApply;
import com.yjy.web.mms.model.entity.process.ProcessList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
/**
 * @Author:winper001 2020-01-15 18:14
 **/
public interface PassportApplyDao extends PagingAndSortingRepository<PassportApply,Long> {

    PassportApply findByProId(ProcessList pro);

//  按工号和旅游目的地查找申请(港澳台)
    @Query("from PassportApply p where p.applyPersonName=?1 and p.destination like %?2%")
    List<PassportApply> findByPersonNameAndDestination(String personName,String Des);

    //  按工号和旅游目的地查找申请（出国）
    @Query("from PassportApply p where p.applyPersonName=?1 and p.destination not like '%香港%' and p.destination not like '%澳门%' and p.destination not like '%台湾%'")
    List<PassportApply> findByPersonNameAndDestination_foreign(String personName);
    //  按工号、旅游目的地查找本年度的申请
    @Query(value = "select * from aoa_passport_apply where apply_person_name=?1 and destination like %?2% and apply_time>=DATE_SUB(CURDATE(),INTERVAL dayofyear(now( )) - 1 DAY )",nativeQuery = true)
    List<PassportApply> findByPersonNameAndDestination_oneyear(String personName,String Des);




    //----------查找实际完成的申请-------------
    //  按工号和旅游目的地查找申请(港澳台)
    @Query("from PassportApply p where p.applyPersonName=?1 and p.destination like %?2% and p.stateCertificate='领取,已还'")
    List<PassportApply> findByPersonNameAndDestination_finished(String personName,String Des);

    //  按工号和旅游目的地查找申请（出国）
    @Query("from PassportApply p where p.applyPersonName=?1 and p.destination not like '%香港%' and p.destination not like '%澳门%' and p.destination not like '%台湾%' and p.stateCertificate='领取,已还'")
    List<PassportApply> findByPersonNameAndDestination_foreign_finished(String personName);
    //  按工号、旅游目的地查找本年度的申请
    @Query(value = "select * from aoa_passport_apply where apply_person_name=?1 and destination like %?2% and apply_time>=DATE_SUB(CURDATE(),INTERVAL dayofyear(now( )) - 1 DAY ) and state_certificate='领取,已还'",nativeQuery = true)
    List<PassportApply> findByPersonNameAndDestination_oneyear_finished(String personName,String Des);

    // 按工号查找本年度所有的申请
    @Query("from PassportApply p where p.applyPersonName=?1 and p.stateCertificate='领取,已还'")
    List<PassportApply> findByPersonNameAll_finished(String personName);
}

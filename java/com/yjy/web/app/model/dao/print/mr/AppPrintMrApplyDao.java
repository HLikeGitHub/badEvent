package com.yjy.web.app.model.dao.print.mr;

import com.yjy.web.app.model.entity.print.mr.PrintMrApply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @info AppPrintMrApplyDao
 * @author rwx
 * @email aba121mail@qq.com
 */
public interface AppPrintMrApplyDao extends JpaRepository<PrintMrApply, Long> {

    PrintMrApply findByApplyIdAndEnabled(long applyId, boolean enabled);

    List<PrintMrApply> findByUidAndEnabled(String uid, boolean enabled);

    @Query("select mrp from PrintMrApply as mrp where  (mrp.applyDate between ?1 and ?2) and (status=?3 or '全部'=?3)")
    Page<PrintMrApply>  findbyTimeAndStatus(Date begDate, Date endDate, String status, Pageable pa);

    @Query("select apply from PrintMrApply as apply where (apply.applyDate between ?1 and ?2) and apply.status in ('已支付','已邮寄')")
    List<PrintMrApply> findByApplyDateAndStatus4Sta(Date date1, Date date2);

    @Query("select mrp from PrintMrApply as mrp where  (mrp.applyDate between ?1 and ?2) and (adNum = ?3 or ''=?3) and (orderId = ?4 or ''=?4) and (status=?5 or '全部'=?5)")
    Page<PrintMrApply>  findbyTimeAndAdNumAndStatus(Date begDate, Date endDate, String adNum, String orderId, String status, Pageable pa);

//	@Query(nativeQuery=true,value="select * from app_print_mr_apply where ad_num='?1' and express_fee>0 and apply_date> DATE_ADD(NOW(),INTERVAL -2 DAY)")
//	List<Map<String,String>> findadNum(String adNum);
	
    @Query("select mrp from PrintMrApply as mrp where   (mrp.applyDate between ?1 and ?2) and  mrp.adNum = ?3 and mrp.expressFee>0 and mrp.applyId<>?4  ")
    List<PrintMrApply>  findbyAdNum( Date begDate, Date endDate, String adNum,Long applyId);
	

    @Query("select mrp from PrintMrApply as mrp where  (mrp.applyDate between ?1 and ?2) and (adNum = ?3 or ''=?3) and (orderId = ?4 or ''=?4) and (status=?5 or '全部'=?5)")
    List<PrintMrApply>  findbyTimeAndAdNumAndStatus(Date begDate, Date endDate, String adNum, String orderId, String status);
    
	//根据申请人和审核人查找流程
	@Query(nativeQuery=true,value="select sum(a.print_fee+a.express_fee) zfy,sum(a.print_fee) dyf,sum(a.express_fee) yjf,count(*) zsl from app_print_mr_apply a where  (a.apply_date between ?1 and ?2) and (a.ad_num = ?3 or ''=?3) and (a.order_id = ?4 or ''=?4) and (a.status=?5 or '全部'=?5)")
	List<Map<String,String>> findsumbyTimeAndAdNumAndStatus(Date begDate, Date endDate, String adNum, String orderId, String status);
//    AppConfig findByCKeyAndEnabled(String cKey, boolean enabled);

    List<PrintMrApply> findByEnabled(boolean enabled);

//    List<AppConfig> findByCIdInAndEnabled(List<Long> cIds, boolean enabled);
}

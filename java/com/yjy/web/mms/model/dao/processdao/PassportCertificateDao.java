package com.yjy.web.mms.model.dao.processdao;


import com.yjy.web.mms.model.entity.process.PassportCertificate;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Author:winper001 2020-02-17 11:13
 **/
public interface PassportCertificateDao extends PagingAndSortingRepository<PassportCertificate,Long> {

    PassportCertificate findByProcessId(Long process_id);
}

package com.yjy.web.mms.model.dao.transfusiondao;

import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;
import com.yjy.web.mms.model.entity.transfusion.AersTransfusionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.Map;

public interface AersTransfusionDao extends PagingAndSortingRepository<AersTransfusionEntity, Long> {

     AersTransfusionEntity findByAersyjgwreportId(AersYJGWReportProcessEntity aersYJGWReportProcessEntity);

    // AersTransfusionEntity findbyAersTransfusionId(Long aersTransfusionId);
}

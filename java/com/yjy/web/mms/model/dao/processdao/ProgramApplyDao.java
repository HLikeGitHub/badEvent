package com.yjy.web.mms.model.dao.processdao;

import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.ProgramApply;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author 梁泽民
 * @info 类或者接口的作用概述，如：欢迎页面控制器
 * @email 373791230@qq.com
 */
public interface ProgramApplyDao  extends PagingAndSortingRepository<ProgramApply, Long> {

	ProgramApply findByProId(ProcessList pro);
}

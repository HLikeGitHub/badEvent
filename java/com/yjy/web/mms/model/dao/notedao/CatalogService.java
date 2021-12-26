package com.yjy.web.mms.model.dao.notedao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CatalogService {

	@Autowired
	CatalogDao catalogDao;
	
	//删除
		public int delete(long catalogId) {
			return catalogDao.delete(catalogId);
		}
		
	
}

package com.yjy.web.mms.model.dao.aersdao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.yjy.web.mms.model.entity.aers.AersFileList;

@Repository
public interface AersFileListdao extends PagingAndSortingRepository<AersFileList, Long>{

	
	
	AersFileList findByFileName(String filename);
	
	AersFileList findByFileId(Long fileId);
	
	
//	
//	List<AersFileList> findByUserAndContentTypeLikeAndFileIstrash(User user, String contenttype, Long istrash);
//	
//	List<AersFileList> findByUserAndFileIstrash(User user,Long istrash);
//	
//	@Query("from AersFileList f where f.user=?1 and f.fileIstrash=0 and f.contentType NOT LIKE 'image/%' and f.contentType NOT LIKE 'application/x%' and f.contentType NOT LIKE 'video/%' and f.contentType NOT LIKE 'audio/%'")
//	List<AersFileList> finddocument(User user);
//	
//	@Query("from AersFileList f where f.user=?1 and f.fileIstrash=0 and f.fileName LIKE ?2 and f.contentType NOT LIKE 'image/%' and f.contentType NOT LIKE 'application/x%' and f.contentType NOT LIKE 'video/%' and f.contentType NOT LIKE 'audio/%'")
//	List<AersFileList> finddocumentlike(User user ,String likefilename);
//	
//	List<AersFileList> findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(User user,Long istrash,String contenttype,String likefilename);
//	
//	List<AersFileList> findByFileIsshareAndFileIstrash(Long isshare,Long istrash);
//	
//	List<AersFileList> findByFileIsshareAndFileNameLike(Long isshare,String likefile);
//	
//	List<AersFileList> findByUserAndFileIsshareAndFileIstrash(User user,Long isshare,Long istrash);
//	
//	List<AersFileList> findByUserAndFileIstrashAndFileNameLike(User user,Long istrash,String likefile);
	
}

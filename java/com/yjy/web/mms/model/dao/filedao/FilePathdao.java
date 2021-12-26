package com.yjy.web.mms.model.dao.filedao;

import com.yjy.web.mms.model.entity.file.FilePath;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilePathdao extends PagingAndSortingRepository<FilePath, Long> {
	
	List<FilePath> findByParentId(Long parentId);
	
	List<FilePath> findByParentIdAndPathIstrash(Long parentId,Long istrash);
	
	FilePath findByPathNameAndParentId(String pathname,Long parentId);
	
	FilePath findByPathName(String pathname);

	List<FilePath> findByPathUserIdAndPathIstrash(Long userid,Long istrash);
	
	FilePath findByParentIdAndPathUserId(Long parentId,Long userid);
	
	List<FilePath> findByPathUserIdAndPathIstrashAndPathNameLikeAndParentIdNot(Long userid,Long istrash,String likefilename,Long userrootpathid);
	
}

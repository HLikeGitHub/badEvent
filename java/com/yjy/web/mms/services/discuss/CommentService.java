package com.yjy.web.mms.services.discuss;

import com.yjy.web.mms.model.dao.discuss.CommentDao;
import com.yjy.web.mms.model.entity.discuss.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentService {
	@Autowired
	private CommentDao commentDao;
	
	//保存
	public Comment save(Comment comment){
		return commentDao.save(comment);
	}
	
	public void deleteComment(Long comment){
		commentDao.deleteById(comment);
	}

}

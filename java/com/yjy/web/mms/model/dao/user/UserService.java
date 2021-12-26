package com.yjy.web.mms.model.dao.user;

import com.yjy.web.comm.utils.NumUtil;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Transactional
@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	//找到该管理员下面的所有用户并且分页
	public Page<User> findmyemployuser(int page, String baseKey, long parentid) {
		Pageable pa= PageRequest.of(page, 10);
		if (!StringUtils.isEmpty(baseKey)) {
			// 模糊查询
			return userDao.findbyFatherId(baseKey, parentid, pa);
		}
		else{
			return userDao.findByFatherId(parentid, pa);
		}
		
	}

	public User findByUid(String uidStr){
		long uid = 0;
		if(uidStr == null || uidStr.isEmpty() || (uid = NumUtil.parseLong(uidStr)) == 0){
			return null;
		}

		return userDao.findByUserId(uid);
	}
}

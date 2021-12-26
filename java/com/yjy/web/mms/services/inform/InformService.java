package com.yjy.web.mms.services.inform;

import com.yjy.web.mms.model.dao.informdao.InformDao;
import com.yjy.web.mms.model.dao.informdao.InformRelationDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.notice.NoticeUserRelation;
import com.yjy.web.mms.model.entity.notice.NoticesList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class InformService {

	@Autowired
	private InformDao informDao;

	@Autowired
	private InformRelationDao informrelationDao;

	@Autowired
	private TypeDao tydao;

	@Autowired
	private StatusDao sdao;

	@Autowired
	private UserDao udao;

	// 保存通知
	public NoticesList save(NoticesList noticelist) {
		return informDao.save(noticelist);
	}

	// 删除通知
	public void deleteOne(Long noticeId) {
		NoticesList notice = informDao.getOne(noticeId);
		List<NoticeUserRelation> relationList = informrelationDao.findByNoticeId(notice);
		informrelationDao.deleteAll(relationList);
		informDao.deleteById(noticeId);
		System.out.println("通知删除成功！");

	}

	// 封装
	public List<Map<String, Object>> fengZhuang(List<NoticesList> noticelist) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < noticelist.size(); i++) {
			Map<String, Object> result = new HashMap<>();
			result.put("noticeId", noticelist.get(i).getNoticeId());
			result.put("typename", tydao.findname(noticelist.get(i).getTypeId()));
			result.put("statusname", sdao.findname(noticelist.get(i).getStatusId()));
			result.put("statuscolor", sdao.findcolor(noticelist.get(i).getStatusId()));
			result.put("title", noticelist.get(i).getTitle());
			result.put("noticeTime", noticelist.get(i).getNoticeTime());
			result.put("top", noticelist.get(i).getTop());
			result.put("url", noticelist.get(i).getUrl());
			result.put("username", udao.getOne(noticelist.get(i).getUserId()).getUserName());
			result.put("deptname", udao.getOne(noticelist.get(i).getUserId()).getDept().getDeptName());
			list.add(result);
		}
		return list;

	}
	public Page<NoticesList> pageThis(int page,Long userId){
		int size=10;
		Sort sort = getSort();
		Pageable pa = PageRequest.of(page, size, sort);
		return informDao.findByUserId(userId, pa);
	}

	private Sort getSort() {
		List<Order> orders = new ArrayList<>();
		orders.addAll(Arrays.asList(new Order(Direction.DESC, "top"), new Order(Direction.DESC, "modifyTime")));
		Sort sort = Sort.by(orders);
		return sort;
	}
	
	public Page<NoticesList> pageThis(int page, Long userId,String baseKey,Object type,Object status,Object time) {
		int size=10;
		List<Order> orders = new ArrayList<>();
		Pageable pa=null;
		//根据类型排序
		if(!StringUtils.isEmpty(type)){
			if("1".equals(type)){
				orders.add(new Order(Direction.DESC, "typeId"));
			}
			else{
				orders.add(new Order(Direction.ASC, "typeId"));
			}
		}
		//根据状态排序
		else if(!StringUtils.isEmpty(status)){
			if("1".equals(status)){
				orders.add(new Order(Direction.DESC, "statusId"));
			}
			else{
				orders.add(new Order(Direction.ASC, "statusId"));
			}
		}
		//根据时间排序
		else if(!StringUtils.isEmpty(time)){
			if("1".equals(time)){
				orders.add(new Order(Direction.DESC, "modifyTime"));
			}
			else{
				orders.add(new Order(Direction.ASC, "modifyTime"));
			}
		}
		else if (!StringUtils.isEmpty(baseKey)) {
			String key="%"+baseKey+"%";
			Sort sort = getSort();
			pa=PageRequest.of(page, size, sort);
			
			return informDao.findByBaseKey(userId, key,pa);
		}
		System.out.println("orders:"+orders);
		if(orders.size()>0){
			Sort sort = Sort.by(orders);
			 pa= PageRequest.of(page, size, sort);
		}else{
			pa=PageRequest.of(page, size);
		}
		return informDao.findByUserId(userId, pa);
	}

}

package com.yjy.web.mms.controller.reportform;

import com.yjy.web.mms.model.dao.attendcedao.AttendceDao;
import com.yjy.web.mms.model.dao.attendcedao.AttendceService;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.dao.user.UserService;
import com.yjy.web.mms.model.entity.attendce.Attends;
import com.yjy.web.mms.model.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class ReportformController {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	AttendceDao attenceDao;
	@Autowired
	AttendceService attendceService;
	@Autowired
    UserDao uDao;
	@Autowired
    UserService userService;
	@Autowired
    TypeDao typeDao;
	@Autowired
    StatusDao statusDao;

	List<Attends> alist;
	List<User> uList;
    Date start,end;
    String month_;
	// 格式转化导入
	DefaultConversionService service = new DefaultConversionService();

	// 月报表
	@RequestMapping("doctormonth")
	public String test2(HttpServletRequest request, Model model, HttpSession session,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "baseKey", required = false) String baseKey) {
		monthtablepaging(request, model, session, page, baseKey);
		return "reportform/monthtable";
	}

	@RequestMapping("realdoctormonthtable")
	public String dfshe(HttpServletRequest request, Model model, HttpSession session,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "baseKey", required = false) String baseKey) {
		monthtablepaging(request, model, session, page, baseKey);
		return "reportform/realmonthtable";
	}

	

	//月报表
	private void monthtablepaging(HttpServletRequest request, Model model, HttpSession session, int page,
			String baseKey) {
		Integer offnum,toworknum;
		Long userId = Long.parseLong(session.getAttribute("userId") + "");
		List<Long> ids = new ArrayList<>();
		Page<User> userspage =userService.findmyemployuser(page, baseKey, userId);
		for (User user : userspage) {
			ids.add(user.getUserId());
		}
		if (ids.size() == 0) {
			ids.add(0L);
		}
		String month = request.getParameter("month");
		
		if(month!=null)
			month_=month;
		else
			month=month_;
		
		Map<String, List<Integer>> uMap = new HashMap<>();
		List<Integer> result = null;
		
		for (User user : userspage) {
			result = new ArrayList<>();
			//当月该用户下班次数
			offnum=attenceDao.countoffwork(month, user.getUserId());
			//当月该用户上班次数
			toworknum=attenceDao.counttowork(month, user.getUserId());
			for (long statusId = 10; statusId < 13; statusId++) {
				//这里面记录了正常迟到早退等状态
				if(statusId==12)
					result.add(attenceDao.countnum(month, statusId, user.getUserId())+toworknum-offnum);
				else
				result.add(attenceDao.countnum(month, statusId, user.getUserId()));
			}
			//添加请假和出差的记录//应该是查找 使用sql的sum（）函数来统计出差和请假的次数
			System.out.println("请假天数"+attenceDao.countothernum(month, 46L, user.getUserId()));
			if(attenceDao.countothernum(month, 46L, user.getUserId())!=null)
			result.add(attenceDao.countothernum(month, 46L, user.getUserId()));
			else
				result.add(0);
			if(attenceDao.countothernum(month, 47L, user.getUserId())!=null)
			result.add(attenceDao.countothernum(month, 47L, user.getUserId()));
			else
				result.add(0);
			//这里记录了旷工的次数 还有请假天数没有记录 旷工次数=30-8-请假次数-某天签到次数
			//这里还有请假天数没有写
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			String date_month=sdf.format(date);
			if(month!=null){
				if(month.compareTo(date_month)>=0)
					result.add(0);
				else
				result.add(30-8-offnum);
			}
			
			uMap.put(user.getUserName(), result);
		}
		model.addAttribute("uMap", uMap);
		model.addAttribute("ulist", userspage.getContent());
		model.addAttribute("page", userspage);
		model.addAttribute("url", "realmonthtable");
	}
}

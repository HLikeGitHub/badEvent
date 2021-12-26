package com.yjy.web.mms.controller.recommend;

import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.mms.model.dao.recommendao.RecommendDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.taskdao.TaskDao;
import com.yjy.web.mms.model.dao.taskdao.TaskloggerDao;
import com.yjy.web.mms.model.dao.taskdao.TaskuserDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.aers.MedCareUnOpEventReportEntity;
import com.yjy.web.mms.model.entity.recommend.RecommendUser;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.task.Tasklist;
import com.yjy.web.mms.model.entity.task.Tasklogger;
import com.yjy.web.mms.model.entity.task.Taskuser;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.Position;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/")
public class RecommendController {

	@Autowired
	private StatusDao sdao;
	@Autowired
	private RecommendDao recomDao;
	/**
	 * 推荐管理表格
	 * 
	 * @return
	 */
	@RequestMapping("recommendmanage")
	public String recommendmanage(Model model,
			@SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		// 通过发布人id找用户
//		User tu = udao.getOne(userId);
		Pageable pa=PageRequest.of(page, size);
		
		Page<RecommendUser> recommendUserPa=recomDao.findAll(pa);
		List<RecommendUser> recommendUserList= recommendUserPa.getContent();

		// 查询状态表
		Iterable<SystemStatusList> statuslist = sdao.findAll();
		model.addAttribute("statuslist", statuslist);
		
		model.addAttribute("recommendUserList", recommendUserList);
		model.addAttribute("page", recommendUserPa);
		model.addAttribute("url", "paixu");
		return "recommend/recommendmanage";
	}
	
	/**
	 * 各种排序
	 */
//	@RequestMapping("paixu")
//	public String paixu(HttpServletRequest request, 
//			@SessionAttribute("userId") Long userId, Model model,
//			@RequestParam(value = "page", defaultValue = "0") int page,
//			@RequestParam(value = "size", defaultValue = "10") int size) {
//	
//		// 通过发布人id找用户
//		User tu = udao.getOne(userId);
//		String val=null;
//		if(!StringUtil.isEmpty(request.getParameter("val"))){
//			val = request.getParameter("val").trim();
//			model.addAttribute("sort", "&val="+val);
//		}
//		
//		Page<Tasklist> tasklist=tservice.index(page, size, val, tu);
//		List<Map<String, Object>> list=tservice.index2(tasklist, tu);
//		model.addAttribute("tasklist", list);
//		model.addAttribute("page", tasklist);
//		model.addAttribute("url", "paixu");
//		
//		return "task/managetable";
//
//	}


	/**
	 * 点击新增推荐人
	 */
	@RequestMapping("addrecommend")
	public ModelAndView addrecommend(@SessionAttribute("userId") Long userId,HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pa=PageRequest.of(page, size);
		ModelAndView mav = new ModelAndView("recommend/addrecommend");
		// 查询状态表
		Iterable<SystemStatusList> statuslist = sdao.findAll();
		mav.addObject("statuslist", statuslist);
		//判断是否为编辑
		if(StringUtil.objectIsNotNull(request.getParameter("id"))) {
			RecommendUser recommendUser =recomDao.findByRecomUserId(Long.parseLong(request.getParameter("id")));
			mav.addObject("recommendUser", recommendUser);
		}
		

		return mav;
	}

	/**
	 * 新增任务保存
	 */
	@RequestMapping("saverecommend")
	public String addtask(@SessionAttribute("userId") Long userId, HttpServletRequest request,
			@Valid RecommendUser recommendUser
			) {
		//判断是否为编辑
		if(StringUtil.objectIsNotNull(recommendUser.getRecomUserId())) {
			recomDao.save(recommendUser);
		}else {
			recomDao.save(recommendUser);
		}
		return "redirect:/recommendmanage";
	}

	/**
	 * 修改任务
	 */
//	@RequestMapping("edittasks")
//	public ModelAndView index3(HttpServletRequest req, @SessionAttribute("userId") Long userId,
//			@RequestParam(value = "page", defaultValue = "0") int page,
//			@RequestParam(value = "size", defaultValue = "10") int size) {
//		Pageable pa=PageRequest.of(page, size);
//		ModelAndView mav = new ModelAndView("task/edittask");
//		// 得到链接中的任务id
//		String taskid = req.getParameter("id");
//		Long ltaskid = Long.parseLong(taskid);
//		// 通过任务id得到相应的任务
//		Tasklist task = tdao.getOne(ltaskid);
//		// 得到状态id
//		Long statusid = task.getStatusId().longValue();
//		// 得到类型id
//		Long typeid = task.getTypeId();
//		// 查看状态表
//		SystemStatusList status = sdao.findById(statusid).orElse(null);
//		// 查询类型表
//		SystemTypeList type = tydao.findById(typeid).orElse(null);
//
//		// 查询部门下面的员工
//		Page<User> pagelist = udao.findByFatherId(userId,pa);
//		List<User> emplist=pagelist.getContent();
//
//		// 查询部门表
//		Iterable<Dept> deptlist = ddao.findAll();
//		// 查职位表
//		Iterable<Position> poslist = pdao.findAll();
//		mav.addObject("type", type);
//		mav.addObject("status", status);
//		mav.addObject("emplist", emplist);
//		mav.addObject("deptlist", deptlist);
//		mav.addObject("poslist", poslist);
//		mav.addObject("task", task);
//		mav.addObject("page", pagelist);
//		mav.addObject("url", "names");
//		mav.addObject("qufen", "任务");
//		return mav;
//	}

	/**
	 * 修改任务确定
	 */
//	@RequestMapping("update")
//	public String update(Tasklist task, HttpSession session) {
//		String userId = String.valueOf(session.getAttribute("userId")).trim();
//		Long userid = Long.parseLong(userId);
//		User userlist = udao.getOne(userid);
//		task.setUsersId(userlist);
//		task.setPublishTime(new Date());
//		task.setModifyTime(new Date());
//		tservice.save(task);
//
//		// 分割任务接收人 还要查找联系人的主键
//		StringTokenizer st = new StringTokenizer(task.getReciverlist(), ";");
//		while (st.hasMoreElements()) {
//			User reciver = udao.findid(st.nextToken());
//			Long pkid = udao.findpkId(task.getTaskId(), reciver.getUserId());
//			Taskuser tasku = new Taskuser();
//			tasku.setPkId(pkid);
//			tasku.setTaskId(task);
//			tasku.setUserId(reciver);
//			tasku.setStatusId(task.getStatusId());
//			// 存任务中间表
//			tudao.save(tasku);
//
//		}
//
//		return "redirect:/taskmanage";
//
//	}

	/**
	 * 查看任务
	 */
//	@RequestMapping("seetasks")
//	public ModelAndView index4(HttpServletRequest req) {
//		ModelAndView mav = new ModelAndView("task/seetask");
//		// 得到任务的 id
//		String taskid = req.getParameter("id");
//		Long ltaskid = Long.parseLong(taskid);
//		// 通过任务id得到相应的任务
//		Tasklist task = tdao.getOne(ltaskid);
//		Long statusid = task.getStatusId().longValue();
//
//		// 根据状态id查看状态表
//		SystemStatusList status = sdao.findById(statusid).orElse(null);
//		// 查看状态表
//		Iterable<SystemStatusList> statuslist = sdao.findAll();
//		// 查看发布人
//		User user = udao.getOne(task.getUsersId().getUserId());
//		// 查看任务日志表
//		List<Tasklogger> logger = tldao.findByTaskId(ltaskid);
//		mav.addObject("task", task);
//		mav.addObject("user", user);
//		mav.addObject("status", status);
//		mav.addObject("loggerlist", logger);
//		mav.addObject("statuslist", statuslist);
//		return mav;
//	}

	/**
	 * 存反馈日志
	 * 
	 * @return
	 */
//	@RequestMapping("tasklogger")
//	public String tasklogger(Tasklogger logger, @SessionAttribute("userId") Long userId) {
//		User userlist = udao.getOne(userId);
//		logger.setCreateTime(new Date());
//		logger.setUsername(userlist.getUserName());
//		// 存日志
//		tldao.save(logger);
//		// 修改任务状态
//		tservice.updateStatusid(logger.getTaskId().getTaskId(), logger.getLoggerStatusid());
//		// 修改任务中间表状态
//		tservice.updateUStatusid(logger.getTaskId().getTaskId(), logger.getLoggerStatusid());
//
//		return "redirect:/taskmanage";
//
//	}

	/**
	 * 我的任务
	 */
//	@RequestMapping("mytask")
//	public String index5(@SessionAttribute("userId") Long userId, Model model,
//			@RequestParam(value = "page", defaultValue = "0") int page,
//			@RequestParam(value = "size", defaultValue = "10") int size) {
//		Pageable pa=PageRequest.of(page, size);
//		Page<Tasklist> tasklist= tservice.index3(userId, null, page, size);
//		
//		Page<Tasklist> tasklist2=tdao.findByTickingIsNotNull(pa);
//		if(tasklist!=null){
//			List<Map<String, Object>> list=tservice.index4(tasklist, userId);
//			model.addAttribute("page", tasklist);
//			model.addAttribute("tasklist", list);
//		}else{
//			List<Map<String, Object>> list2=tservice.index4(tasklist2, userId);
//			model.addAttribute("page", tasklist2);
//			model.addAttribute("tasklist", list2);
//		}
//		model.addAttribute("url", "mychaxun");
//		return "task/mytask";
//
//	}
	
	/**
	 * 在我的任务里面进行查询
	 * 
	 * @throws ParseException
	 */
//	@RequestMapping("mychaxun")
//	public String select(HttpServletRequest request, @SessionAttribute("userId") Long userId, Model model,
//			@RequestParam(value = "page", defaultValue = "0") int page,
//			@RequestParam(value = "size", defaultValue = "10") int size) throws ParseException {
//	
//		String title =null;
//		if(!StringUtil.isEmpty(request.getParameter("title"))){
//			 title = request.getParameter("title").trim();
//		}
//		Page<Tasklist> tasklist= tservice.index3(userId, title, page, size);
//		List<Map<String, Object>> list=tservice.index4(tasklist, userId);
//		model.addAttribute("tasklist", list);
//		model.addAttribute("page", tasklist);
//		model.addAttribute("url", "mychaxun");
//		model.addAttribute("sort", "&title="+title);
//		return "task/mytasklist";
//	}


//	@RequestMapping("myseetasks")
//	public ModelAndView myseetask(HttpServletRequest req, @SessionAttribute("userId") Long userId) {
//
//		ModelAndView mav = new ModelAndView("task/myseetask");
//		// 得到任务的 id
//		String taskid = req.getParameter("id");
//
//		Long ltaskid = Long.parseLong(taskid);
//		// 通过任务id得到相应的任务
//		Tasklist task = tdao.getOne(ltaskid);
//
//		// 查看状态表
//		Iterable<SystemStatusList> statuslist = sdao.findAll();
//		// 查询接收人的任务状态
//		Long ustatus = tudao.findByuserIdAndTaskId(userId, ltaskid);
//
//		SystemStatusList status = sdao.findById(ustatus).orElse(null);
//		/*System.out.println(status);*/
//
//		// 查看发布人
//		User user = udao.getOne(task.getUsersId().getUserId());
//		// 查看任务日志表
//		List<Tasklogger> logger = tldao.findByTaskId(ltaskid);
//
//		mav.addObject("task", task);
//		mav.addObject("user", user);
//		mav.addObject("status", status);
//		mav.addObject("statuslist", statuslist);
//		mav.addObject("loggerlist", logger);
//		return mav;
//
//	}

	/**
	 * 从我的任务查看里面修改状态和日志
	 */
//	@RequestMapping("uplogger")
//	public String updatelo(Tasklogger logger, @SessionAttribute("userId") Long userId) {
//		System.out.println(logger.getLoggerStatusid());
//		// 获取用户id
//		
//		// 查找用户
//		User user = udao.getOne(userId);
//		// 查任务
//		Tasklist task = tdao.getOne(logger.getTaskId().getTaskId());
//		logger.setCreateTime(new Date());
//		logger.setUsername(user.getUserName());
//		// 存日志
//		tldao.save(logger);
//
//		// 修改任务中间表状态
//		Long pkid = udao.findpkId(logger.getTaskId().getTaskId(), userId);
//		Taskuser tasku = new Taskuser();
//		tasku.setPkId(pkid);
//		tasku.setTaskId(task);
//		tasku.setUserId(user);
//		if (!Objects.isNull(logger.getLoggerStatusid())) {
//
//			tasku.setStatusId(logger.getLoggerStatusid());
//		}
//		// 存任务中间表
//		tudao.save(tasku);
//		
//		// 修改任务状态
//		// 通过任务id查看总状态
//		
//		List<Integer> statu = tudao.findByTaskId(logger.getTaskId().getTaskId());
//		System.out.println(statu);
//		// 选出最小的状态id 修改任务表里面的状态
//		Integer min = statu.get(0);
//		for (Integer integer : statu) {
//			if (integer.intValue() < min) {
//				min = integer;
//			}
//		}
//
//		int up = tservice.updateStatusid(logger.getTaskId().getTaskId(), min);
//		/*System.out.println(logger.getTaskId().getTaskId() + "aaaa");
//		System.out.println(min + "wwww");
//		System.out.println(up + "pppppp");*/
//		if (up > 0) {
//			System.out.println("任务状态修改成功!");
//		}
//
//		return "redirect:/mytask";
//
//	}

	/**
	 * 根据发布人这边删除任务和相关联系
	 * @param req
	 * @param userId
	 * @return
	 */
//	@RequestMapping("shanchu")
//	public String delete(HttpServletRequest req, @SessionAttribute("userId") Long userId) {
//		// 得到任务的 id
//		String taskid = req.getParameter("id");
//		Long ltaskid = Long.parseLong(taskid);
//		
//		// 根据任务id找出这条任务
//		Tasklist task = tdao.getOne(ltaskid);
//		if(task.getUsersId().getUserId().equals(userId)){
//			// 删除日志表
//			int i=tservice.detelelogger(ltaskid);
//			System.out.println(i+"mmmmmmmmmmmm");
//			// 分割任务接收人 还要查找联系人的主键并删除接收人中间表
//			StringTokenizer st = new StringTokenizer(task.getReciverlist(), ";");
//			while (st.hasMoreElements()) {
//				User reciver = udao.findid(st.nextToken());
//				Long pkid = udao.findpkId(task.getTaskId(), reciver.getUserId());
//				int m=tservice.delete(pkid);
//				System.out.println(m+"sssssssssss");
//				
//			}
//			// 删除这条任务
//			tservice.deteletask(task);
//		}else{
//			System.out.println("权限不匹配，不能删除");
//			return "redirect:/notlimit";
//
//		}
//		return "redirect:/taskmanage";
//
//	}


}

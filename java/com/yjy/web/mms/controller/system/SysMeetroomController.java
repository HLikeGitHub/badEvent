package com.yjy.web.mms.controller.system;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.yjy.web.mms.services.file.FileServices;
import com.yjy.web.mms.model.dao.notedao.AttachmentDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.SysMeetroomDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.dao.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yjy.web.mms.model.entity.plan.Plan;
import com.yjy.web.mms.model.entity.system.SysMeetroom;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.system.SystemService;

@Controller
@RequestMapping("/")
public class SysMeetroomController {

	@Autowired
    SysMeetroomDao sysMeetroomDao;
    
	@Autowired
	SystemService systemService;
	
	@Autowired
    TypeDao typeDao;
	@Autowired
    StatusDao statusDao;
	@Autowired
	FileServices fServices;
	@Autowired
    UserDao userDao;
	@Autowired
    UserService userService;
	@Autowired
    AttachmentDao attachmentDao;

	List<Plan> pList;
	List<User> uList;
	Date startDate,endDate;
	String choose2;
	Logger log = LoggerFactory.getLogger(getClass());
	// 格式转化导入
	DefaultConversionService service = new DefaultConversionService();

	@RequestMapping("meetroomdelete")
	public String DSAGec(HttpServletRequest request, HttpSession session) {

		long pid = Long.valueOf(request.getParameter("pid"));
		sysMeetroomDao.deleteById(pid);
		return "redirect:/meetroomview";


	}

	// 会议室管理
	@RequestMapping(value="meetroomview", method = RequestMethod.GET)
	public String meetroomview(Model model, HttpSession session, 
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "baseKey", required = false) String baseKey,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "icon", required = false) String icon,
			@RequestParam(value = "size", defaultValue = "10") int size) {
//		System.out.println("11"+baseKey);
//		sortpaging(model, session, page, baseKey, type, status, time, icon);
		//分页
		Pageable pa=PageRequest.of(page, size);
		Page<SysMeetroom> sysMeetroomPage =sysMeetroomDao.findAll(pa) ;
		List<SysMeetroom> sysMeetroomList=sysMeetroomPage.getContent();
		typestatus(model);
		model.addAttribute("meetroomlist", sysMeetroomList);
		model.addAttribute("page", sysMeetroomPage);
		//model.addAttribute("url", "meetroomviewtable");

		
		return "meetroom/meetroomview";
	}

	
    //
	@RequestMapping(value="meetroomtable", method = RequestMethod.GET)
	public String testdd(Model model, HttpSession session) {
		
		List<SysMeetroom> sysMeetroomList = (List<SysMeetroom>)sysMeetroomDao.findAll() ;
		typestatus(model);
		model.addAttribute("meetroomlist", sysMeetroomList);
//		model.addAttribute("page", page2);
		model.addAttribute("url", "meetroomviewtable");
		return "meetroom/meetroomtable";
	}


	// 我的编辑
	@RequestMapping("meetroomedit")
	public String meetroomEdit(HttpServletRequest request, Model model) {

		if (!StringUtils.isEmpty(request.getAttribute("errormess"))) {
			request.setAttribute("errormess", request.getAttribute("errormess"));
			request.setAttribute("meetroom", request.getAttribute("meetroom2"));
		} else if (!StringUtils.isEmpty(request.getAttribute("success"))) {
			request.setAttribute("success", request.getAttribute("success"));
			request.setAttribute("meetroom", request.getAttribute("meetroom2"));
		}
		if (!StringUtils.isEmpty(request.getAttribute("meetroom2"))) {
			System.out.println("meetroomEdit:"+request.getAttribute("meetroom2").toString());
			System.out.println("pid:"+request.getAttribute("pid").toString());
		}
        
		long pid = 0;
		if (!StringUtils.isEmpty(request.getParameter("pid"))) {
			pid = Long.valueOf(request.getParameter("pid"));
		}else {
			pid = Long.valueOf(request.getAttribute("pid").toString());
		}
		
		// 新建
		if ((pid == -1)) {
			model.addAttribute("meetroom", null);
			model.addAttribute("sysmeetroomId", pid);
		} else if (pid > 0) {
			SysMeetroom sysMeetroom = sysMeetroomDao.findById(pid).orElse(null);
			model.addAttribute("meetroom", sysMeetroom);
			model.addAttribute("sysmeetroomId", pid);
		}

		typestatus(model);
		return "meetroom/meetroomedit";
	}

	@RequestMapping(value = "meetroomsave", method = RequestMethod.GET)
	public void meetroomSave() {
	}
	
	@RequestMapping(value = "meetroomsave", method = RequestMethod.POST)
	public String meetroomSave(HttpServletRequest req, @Valid SysMeetroom sysMeetroom,
			BindingResult br) throws IllegalStateException, IOException {
		try {
			String status = req.getParameter("status");
			long statusid = statusDao.findByStatusModelAndStatusName("aoa_sysmeetroom", status).getStatusId();
			sysMeetroom.setStatusId(statusid);
			System.out.println("sysMeetroom1:"+sysMeetroom.toString());
			sysMeetroom=sysMeetroomDao.save(sysMeetroom);
			System.out.println("sysMeetroom2:"+sysMeetroom.toString());
			req.setAttribute("success", "保存成功!");
		}catch(Exception e) {
			req.setAttribute("errormess", "保存失败！");
		}
		
		req.setAttribute("pid", sysMeetroom.getSysmeetroomId());
		req.setAttribute("meetroom2", sysMeetroom);
		return "forward:/meetroomedit";
//        return "redirect:/meetroomedit";

	}


	private void typestatus(Model model) {
		List<SystemTypeList> type = (List<SystemTypeList>) typeDao.findByTypeModel("aoa_sysmeetroom");
		List<SystemStatusList> status = (List<SystemStatusList>) statusDao.findByStatusModel("aoa_sysmeetroom");
		model.addAttribute("typelist", type);
		model.addAttribute("statuslist", status);
	}
	private void sortpaging(Model model, HttpSession session, int page, String baseKey, String type, String status,
			String time, String icon) {
//		new AttendceController().setSomething(baseKey, type, status, time, icon, model);
//		Long userid = Long.valueOf(session.getAttribute("userId") + "");
//		User user = userDao.findOne(userid);
//		Page<Plan> page2 = planservice.paging(page, baseKey, userid, type, status, time);
//		typestatus(model);
//		model.addAttribute("plist", page2.getContent());
//		model.addAttribute("page", page2);
		model.addAttribute("url", "planviewtable");
	}
	
}

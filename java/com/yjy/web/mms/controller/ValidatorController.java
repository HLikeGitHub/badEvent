package com.yjy.web.mms.controller;

import com.yjy.web.comm.result.ResultUtil;
import com.yjy.web.comm.result.MapToList;
import com.yjy.web.comm.result.Result;
import com.yjy.web.comm.result.ResultCode;
import com.yjy.web.mms.model.dao.roledao.RoleDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.role.Role;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.task.Tasklist;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/")
public class ValidatorController {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private UserDao udao;
	@Autowired
	private DeptDao ddao;
	@Autowired
	private RoleDao rdao;
	
	@RequestMapping("ck_addtask")
	public String addtask(HttpServletRequest request,@Valid Tasklist tlist,BindingResult br,HttpSession session,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size){
		request.setAttribute("tasklist", tlist);
		Pageable pa=PageRequest.of(page, size);
		
		// 这里返回Result对象，如果校验通过，ResultCode.SUCCESS.getCode()返回的值为200；否则就是没有通过；
		Result res = ResultUtil.hasErrors(br);
		System.out.println("tlist:"+tlist);
		if (ResultCode.SUCCESS.getCode() != res.getCode()) {
			List<Object> list = new MapToList<>().mapToList(res.getData());
			request.setAttribute("errormess", list.get(0).toString());
			System.out.println("list错误的实体类信息：" + tlist);
			System.out.println("list错误详情:" + list);
			System.out.println("list错误第一条:" + list.get(0));
			System.out.println("啊啊啊错误的信息——：" + list.get(0).toString());
			// 下面的info信息是打印出详细的信息
			log.info("getData:{}", res.getData());
			log.info("getCode:{}", res.getCode());
			log.info("getMsg:{}", res.getMsg());
			
			
			
			String userId = ((String) session.getAttribute("userId")).trim();
			Long userid = Long.parseLong(userId);

			
			// 查询类型表
			Iterable<SystemTypeList> typelist = tydao.findAll();
			// 查询状态表
			Iterable<SystemStatusList> statuslist = sdao.findAll();
			// 查询部门下面的员工
			Page<User> pagelist = udao.findByFatherId(userid,pa);
			List<User> emplist=pagelist.getContent();
			// 查询部门表
			Iterable<Dept> deptlist = ddao.findAll();
			// 查角色表
			Iterable<Role> rolelist = rdao.findAll();
			request.setAttribute("typelist", typelist);
			request.setAttribute("statuslist", statuslist);
			request.setAttribute("emplist", emplist);
			request.setAttribute("page", pagelist);
			request.setAttribute("deptlist", deptlist);
			request.setAttribute("rolelist", rolelist);
			return "task/addtask";
		}
		else{
			
			return "forward:/addtasks";
		}
		
		
		
		
		
	}

}

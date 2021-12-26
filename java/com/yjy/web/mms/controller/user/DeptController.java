package com.yjy.web.mms.controller.user;


import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.Position;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class DeptController {
	
	@Autowired
    DeptDao deptdao;
	@Autowired
    UserDao udao;
	@Autowired
    PositionDao pdao;
	
	/**
	 * 第一次进入部门管理页面
	 * @return
	 */
	@RequestMapping("deptmanage")
	public String deptmanage(Model model) {
		List<Dept> depts = (List<Dept>) deptdao.findAll();
		System.out.println(depts);
		model.addAttribute("depts",depts);
		return "user/deptmanage";
	}
	
	@RequestMapping(value = "deptedit" ,method = RequestMethod.POST)
	public String adddept(@Valid Dept dept,@RequestParam("xg") String xg,@RequestParam(value="kz",required=false) String kz,
                          BindingResult br,Model model){
		System.out.println(br.hasErrors());
		System.out.println(br.getFieldError());
		String kz_userName=kz.split("/")[1];
		User deptManager=udao.findByUserName(kz_userName);
		if(deptManager!=null){
			dept.setDeptmanager(deptManager.getUserId());
		}
		if(!br.hasErrors()){
			System.out.println("没有错误");
			Dept adddept = deptdao.save(dept);
			/*if("add".equals(xg)){
				System.out.println("新增拉");
				Position jinli = new Position();
				jinli.setDeptid(adddept.getDeptId());
				jinli.setName("经理");
				Position wenyuan = new Position();
				wenyuan.setDeptid(adddept.getDeptId());
				wenyuan.setName("文员");
				pdao.save(jinli);
				pdao.save(wenyuan);
			}*/
			if(adddept!=null){
				System.out.println("插入成功");
				model.addAttribute("success",1);
				return "/deptmanage";
			}
		}
		System.out.println("有错误");
		model.addAttribute("errormess","错误！~");
		return "user/deptedit";
	}
	
	@RequestMapping(value = "deptedit" ,method = RequestMethod.GET)
	public String changedept(@RequestParam(value = "dept",required=false) Long deptId,Model model){
		if(deptId!=null){
		    User kz=null;
			Dept dept = deptdao.findById(deptId).orElse(null);
			model.addAttribute("kz", "");
			if(dept.getDeptmanager()!=null){
                kz=udao.getOne(dept.getDeptmanager());
                model.addAttribute("kz", kz.getRealName()+"/"+kz.getUserName());
            }
			model.addAttribute("dept",dept);

		}
		return "user/deptedit";
	}
	
	@RequestMapping("readdept")
	public String readdept(HttpServletRequest req, Model model){

		Long deptId=Long.parseLong(req.getParameter("deptid"));
		Dept dept = deptdao.findById(deptId).orElse(null);
		User deptmanage = null;
		if(dept.getDeptmanager()!=null){
			deptmanage = udao.getOne(dept.getDeptmanager());
			model.addAttribute("deptmanage",deptmanage);
		}
		List<Dept> depts = (List<Dept>) deptdao.findAll();
		//List<Position> positions = pdao.findByDeptidAndNameNotLike(1L, "%经理");
		List<Position> positions=pdao.findAllPostion();
		System.out.println(deptmanage);
		List<User> formaluser = new ArrayList<>();
		List<User> deptusers = udao.findByDept(dept);
		
		for (User deptuser : deptusers) {
			Position position = deptuser.getPosition();
			System.out.println(deptuser.getRealName()+":"+position.getName());
			//if(!position.getName().endsWith("科长")){
			formaluser.add(deptuser);
			//}
		}
		System.out.println(deptusers);
		model.addAttribute("positions",positions);
		model.addAttribute("depts",depts);
		model.addAttribute("deptuser",formaluser);
		
		model.addAttribute("dept",dept);
		model.addAttribute("isread",1);
		
		return "user/deptread";
		
	}
	
	@RequestMapping("deptandpositionchange")
	public String deptandpositionchange(@RequestParam("positionid") Long positionid,
			@RequestParam("changedeptid") String deptName,
			@RequestParam("userid") Long userid,
			@RequestParam("deptid") Long deptid,
			Model model){
		User user = udao.getOne(userid);
		//Dept changedept = deptdao.findById(changedeptid).orElse(null);
		Dept changedept = deptdao.findbydeptName(deptName);
		Position position = pdao.findById(positionid).orElse(null);
		user.setDept(changedept);
		user.setPosition(position);
		udao.save(user);
		System.out.println(deptid);
		
		model.addAttribute("deptid",deptid);
		return "/readdept";
	}
	
	@RequestMapping("deletdept")
	public String deletdept(@RequestParam("deletedeptid") Long deletedeptid){
		Dept dept = deptdao.findById(deletedeptid).orElse(null);
		List<Position> ps = pdao.findByDeptid(deletedeptid);
		for (Position position : ps) {
			System.out.println(position);
			pdao.delete(position);
		}
		deptdao.delete(dept);
		return "/deptmanage";
		
	}
	
	@RequestMapping("deptmanagerchange")
	public String deptmanagerchange(@RequestParam(value="positionid",required=false) Long positionid,
			@RequestParam(value="changedeptid",required=false) String deptName,
			@RequestParam(value="oldmanageid",required=false) Long oldmanageid,
			@RequestParam(value="newmanageid",required=false) Long newmanageid,
			@RequestParam("deptid") Long deptid,
			Model model){
		System.out.println("oldmanageid:"+oldmanageid);
		System.out.println("newmanageid:"+newmanageid);
		Dept deptnow = deptdao.findById(deptid).orElse(null);
		if(oldmanageid!=null){
			User oldmanage = udao.getOne(oldmanageid);
			
			Position namage = oldmanage.getPosition();
			
			//Dept changedept = deptdao.findById(changedeptid).orElse(null);
			Dept changedept =deptdao.findbydeptName(deptName);
			Position changeposition = pdao.findById(positionid).orElse(null);
			
			oldmanage.setDept(changedept);
			oldmanage.setPosition(changeposition);
			udao.save(oldmanage);
			
			if(newmanageid!=null){
				User newmanage = udao.getOne(newmanageid);
				newmanage.setPosition(namage);
				deptnow.setDeptmanager(newmanageid);
				deptdao.save(deptnow);
				udao.save(newmanage);
			}else{
				deptnow.setDeptmanager(null);
				deptdao.save(deptnow);
			}
			
		}else{
			User newmanage = udao.getOne(newmanageid);
//			Position manage = pdao.findByDeptidAndNameLike(deptid, "%科长").get(0);
//			newmanage.setPosition(manage);
			deptnow.setDeptmanager(newmanageid);
			deptdao.save(deptnow);
			udao.save(newmanage);
		}
		
		
		
		model.addAttribute("deptid",deptid);
		return "/readdept";
	}
}

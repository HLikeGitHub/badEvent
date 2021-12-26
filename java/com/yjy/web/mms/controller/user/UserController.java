package com.yjy.web.mms.controller.user;

import com.yjy.web.mms.model.dao.roledao.RoleDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.dao.user.UserLogRecordDao;
import com.yjy.web.mms.model.entity.role.Role;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.Position;
import com.yjy.web.mms.model.entity.user.User;
import com.github.pagehelper.util.StringUtil;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yjy.web.mms.model.entity.user.LoginRecord;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class UserController {
	
	@Autowired
    UserDao udao;
	@Autowired
    DeptDao ddao;
	@Autowired
    PositionDao pdao;
	@Autowired
    RoleDao rdao;

    @Autowired
    UserLogRecordDao userLogRecordDao;
	
	@RequestMapping("userlogmanage")
	public String userlogmanage() {
		return "user/userlogmanage";
	}
	
	@RequestMapping("usermanage")
	public String usermanage(Model model,@RequestParam(value="page",defaultValue="0") int page,
			@RequestParam(value="size",defaultValue="10") int size
			) {
		Sort sort=Sort.by(new Order(Direction.ASC,"dept"));
		Pageable pa=PageRequest.of(page, size,sort);
		Page<User> userspage = udao.findByIsLock(0, pa);
		List<User> users=userspage.getContent();
		model.addAttribute("users",users);
		model.addAttribute("page", userspage);
		model.addAttribute("url", "usermanagepaging");
		return "user/usermanage";
	}
	
	@RequestMapping("usermanagepaging")
	public String userPaging(Model model,@RequestParam(value="page",defaultValue="0") int page,
			@RequestParam(value="size",defaultValue="10") int size,
			@RequestParam(value="usersearch",required=false) String usersearch
			){
		Sort sort=Sort.by(new Order(Direction.ASC,"dept"));
		Pageable pa=PageRequest.of(page, size,sort);
		Page<User> userspage = null;
		if(StringUtil.isEmpty(usersearch)){
			userspage =  udao.findByIsLock(0, pa);
		}else{
			System.out.println(usersearch);
			userspage = udao.findnamelike(usersearch, pa);
		}
		List<User> users=userspage.getContent();
		model.addAttribute("users",users);
		model.addAttribute("page", userspage);
		model.addAttribute("url", "usermanagepaging");
		
		return "user/usermanagepaging";
	}
	
	
	@RequestMapping(value="useredit",method = RequestMethod.GET)
	public String usereditget(@RequestParam(value = "userid",required=false) Long userid, Model model, HttpServletRequest req) {
		if(userid!=null){
			User user = udao.getOne(userid);
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String hireTime=df.format(user.getHireTime());
			model.addAttribute("hireTime", hireTime);
			model.addAttribute("where","xg");
			model.addAttribute("user",user);
		}
		if(!StringUtils.isEmpty(req.getParameter("curPage"))){
			Long curPage=Long.parseLong(req.getParameter("curPage"));
			model.addAttribute("curPage", curPage);
		}else{
			model.addAttribute("curPage", 1L);
		}

		List<Dept> depts = (List<Dept>) ddao.findAll();
		List<Position> positions = (List<Position>) pdao.findAll();
		List<Role> roles = (List<Role>) rdao.findAll();
		model.addAttribute("depts", depts);
		model.addAttribute("positions", positions);
		model.addAttribute("roles", roles);
		return "user/edituser";
	}
	
	@RequestMapping(value="useredit",method = RequestMethod.POST)
	public String usereditpost(User user,
			@RequestParam("deptName") String deptName,
			@RequestParam("positionid") Long positionid,
			@RequestParam("roleid") Long roleid,
			@RequestParam(value = "isbackpassword",required=false) boolean isbackpassword,
			Model model) throws PinyinException {
		System.out.println("职工："+user);
		System.out.println("科室："+deptName);
		System.out.println("职位："+positionid);
		System.out.println("角色："+roleid);
		Dept dept = ddao.findbydeptName(deptName);
		Position position = pdao.findById(positionid).orElse(null);
		Role role = rdao.getOne(roleid);

		if(user.getUserId()==null){//新增用户
			String pinyin=PinyinHelper.convertToPinyinString(user.getUserName(), "", PinyinFormat.WITHOUT_TONE);
			user.setPinyin(pinyin);
			user.setPassword("123456");
			user.setDept(dept);
			user.setRole(role);
			user.setPosition(position);
			user.setFatherId(dept.getDeptmanager());
			user.setHireTime(user.getHireTime());
			udao.save(user);
		}else{//编辑用户信息
			User user2 = udao.getOne(user.getUserId());
			user2.setUserTel(user.getUserTel());
			user2.setRealName(user.getRealName());
			user2.setEamil(user.getEamil());
			user2.setAddress(user.getAddress());
			user2.setUserEdu(user.getUserEdu());
			user2.setSchool(user.getSchool());
			user2.setIdCard(user.getIdCard());
			user2.setBank(user.getBank());
			user2.setThemeSkin(user.getThemeSkin());
			user2.setSalary(user.getSalary());
			user2.setFatherId(dept.getDeptmanager());
			user2.setHireTime(user.getHireTime());
			if(isbackpassword){
				user2.setPassword("123456");
			}
			user2.setDept(dept);
			user2.setRole(role);
			user2.setPosition(position);
			udao.save(user2);
		}
		
		//model.addAttribute("success",1);
		return "/usermanage";
	}
	
	
	@RequestMapping("deleteuser")
	public String deleteuser(@RequestParam("userid") Long userid,Model model){
		User user = udao.getOne(userid);
		//user.setIsLock(1);
		//udao.save(user);
        //保持数据一致性
        List<LoginRecord> records=userLogRecordDao.findAllByUserId(userid);
        if(!Objects.isNull(records)){
            userLogRecordDao.deleteAll(records);
        }
		udao.delete(user);//winper修改，直接删除user
		model.addAttribute("success",1);
		return "/usermanage";
		
	}
	
	@RequestMapping("useronlyname")
    public @ResponseBody boolean useronlyname(@RequestParam("username") String username){
		System.out.println(username);
		User user = udao.findByUserName(username);
		System.out.println(user);
		if(user==null){
			return true;
		}
		return false;
    }
	
	@RequestMapping("selectdept")
	public @ResponseBody List<Position> selectdept(@RequestParam("selectdeptid") Long deptid){
		
		return pdao.findByDeptidAndNameNotLike(deptid, "%科长");
	}
	
	

}

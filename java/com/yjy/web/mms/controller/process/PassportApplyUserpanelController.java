package com.yjy.web.mms.controller.process;

import com.yjy.web.comm.result.MapToList;
import com.yjy.web.comm.result.Result;
import com.yjy.web.comm.result.ResultCode;
import com.yjy.web.comm.result.ResultUtil;
import com.yjy.web.mms.model.dao.informdao.InformRelationDao;
import com.yjy.web.mms.model.dao.maildao.MailreciverDao;
import com.yjy.web.mms.model.dao.processdao.NotepaperDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.mail.Mailreciver;
import com.yjy.web.mms.model.entity.notice.NoticeUserRelation;
import com.yjy.web.mms.model.entity.process.Notepaper;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.user.NotepaperService;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class PassportApplyUserpanelController {
	@Autowired
	private UserDao udao;
	
	@Autowired
	private DeptDao ddao;
	@Autowired
	private PositionDao pdao;
	@Autowired
	private InformRelationDao irdao;
	@Autowired
	private MailreciverDao mdao;
	@Autowired
	private NotepaperDao ndao;
	@Autowired
	private NotepaperService nservice;
	
	@Value("${img.rootpath}")
	private String rootpath;
	
	@RequestMapping("userpanel_passport")
	public String index(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) throws ParseException{
		
		Pageable pa=PageRequest.of(page, size);
		User user=null;
		if(!StringUtil.isEmpty((String) req.getAttribute("errormess"))){
			 user=(User) req.getAttribute("users");
			 req.setAttribute("errormess",req.getAttribute("errormess"));
		}
		else if(!StringUtil.isEmpty((String) req.getAttribute("success"))){
			user=(User) req.getAttribute("users"); 
			req.setAttribute("success","fds");
		}
		else{
			//找到这个用户
			user=udao.getOne(userId);
		}
		
		//找到部门名称
		String deptname=ddao.findname(user.getDept().getDeptId());
		
		//找到职位名称
		String positionname=pdao.findById(user.getPosition().getId()).orElse(null).getName();
		
		//找未读通知消息
		List<NoticeUserRelation> noticelist=irdao.findByReadAndUserId(false, user);
		
		//找未读邮件
		List<Mailreciver> maillist=mdao.findByReadAndDelAndReciverId(false,false, user);
		
		//找便签
		Page<Notepaper> list=ndao.findByUserIdOrderByCreateTimeDesc(user,pa);
		
		List<Notepaper> notepaperlist=list.getContent();
		
		
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String hireTime=df.format(user.getHireTime());
       
        model.addAttribute("hireTime", hireTime);
		
		model.addAttribute("user", user);
		model.addAttribute("deptname", deptname);
		model.addAttribute("positionname", positionname);
		model.addAttribute("noticelist", noticelist.size());
		model.addAttribute("maillist", maillist.size());
		model.addAttribute("notepaperlist", notepaperlist);
		model.addAttribute("page", list);
		model.addAttribute("url", "panel");
		
	
		return "user/userpanel_passport";
	}
	/**
	 * 上下页
	 */
	@RequestMapping("panel_passport")
	public String index(@SessionAttribute("userId") Long userId,Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size){
		Pageable pa=PageRequest.of(page, size);
		User user=udao.getOne(userId);
		//找便签
		Page<Notepaper> list=ndao.findByUserIdOrderByCreateTimeDesc(user,pa);
		List<Notepaper> notepaperlist=list.getContent();
		model.addAttribute("notepaperlist", notepaperlist);
		model.addAttribute("page", list);
		model.addAttribute("url", "panel");
		return "user/panel_passport";
	}
	/**
	 * 存便签
	 */
	@RequestMapping("writep_passport")
	public String savepaper(Notepaper npaper,@SessionAttribute("userId") Long userId,@RequestParam(value="concent",required=false)String concent){
		User user=udao.getOne(userId);
		npaper.setCreateTime(new Date());
		npaper.setUserId(user);
		System.out.println("内容"+npaper.getConcent());
		if(npaper.getTitle()==null||npaper.getTitle()=="")
			npaper.setTitle("无标题");
		if(npaper.getConcent()==null||npaper.getConcent()=="")
			npaper.setConcent(concent);
		ndao.save(npaper);
		
		return "redirect:/userpanel_passport";
	}
	/**
	 * 删除便签
	 */
	@RequestMapping("notepaper_passport")
	public String deletepaper(HttpServletRequest request,@SessionAttribute("userId") Long userId){
		User user=udao.getOne(userId);
		String paperid=request.getParameter("id");
		Long lpid = Long.parseLong(paperid);
		Notepaper note=ndao.getOne(lpid);
		if(user.getUserId().equals(note.getUserId().getUserId())){
			nservice.delete(lpid);
		}else{
			System.out.println("权限不匹配，不能删除");
			return "redirect:/notlimit";
		}
		return "redirect:/userpanel_passport";
		
	}
	/**
	 * 修改用户
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("saveuser_passport")
	public String saveemp(@RequestParam("filePath")MultipartFile filePath,HttpServletRequest request,@Valid User user,
			BindingResult br,@SessionAttribute("userId") Long userId) throws Exception{
		String imgpath=nservice.upload(filePath);
		User users=udao.getOne(userId);
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		//重新set用户
		users.setRealName(user.getRealName());//真实姓名
		users.setUserTel(user.getUserTel());//电话
		users.setAddress(user.getAddress());//住址
		users.setUserEdu(user.getUserEdu());//学历
		users.setSchool(user.getSchool());//学校
		users.setIdCard(user.getIdCard());//身份证
		users.setSex(user.getSex());//性别
		users.setThemeSkin(user.getThemeSkin());//皮肤
		//users.setBirth(user.getBirth());//生日
        //String hireTime=df.format(user.getHireTime());
		users.setHireTime(user.getHireTime());//入职时间
		if(!StringUtil.isEmpty(user.getUserSign())){
			users.setUserSign(user.getUserSign());
		}
		if(!StringUtil.isEmpty(user.getPassword())){
			users.setPassword(user.getPassword());
		}
		if(!StringUtil.isEmpty(imgpath)){
			users.setImgPath(imgpath);
			
		}
		
		request.setAttribute("users", users);
		
		Result res = ResultUtil.hasErrors(br);
		if (ResultCode.SUCCESS.getCode() != res.getCode()) {
			List<Object> list = new MapToList<>().mapToList(res.getData());
			request.setAttribute("errormess", list.get(0).toString());
			
			System.out.println("list错误的实体类信息：" + user);
			System.out.println("list错误详情:" + list);
			System.out.println("list错误第一条:" + list.get(0));
			System.out.println("啊啊啊错误的信息——：" + list.get(0).toString());
			
		}else{
			udao.save(users);
			request.setAttribute("success", "执行成功！");
		}
		return "forward:/userpanel_passport";
		
	}
	@RequestMapping("image/**_passport")
	public void image(Model model, HttpServletResponse response, @SessionAttribute("userId") Long userId, HttpServletRequest request)
			throws IOException {

		String startpath = new String(URLDecoder.decode(request.getRequestURI(), "utf-8"));
		
		String path = startpath.replace("/image", "");
		
		File f = new File(rootpath, path);
		
		ServletOutputStream sos = response.getOutputStream();
		FileInputStream input = new FileInputStream(f.getPath());
		byte[] data = new byte[(int) f.length()];
		IOUtils.readFully(input, data);
		// 将文件流输出到浏览器
		IOUtils.write(data, sos);
		input.close();
		sos.close();
	}

}

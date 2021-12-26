package com.yjy.web.mms.controller.login;

import com.yjy.web.comm.Interceptor.ReqInterceptor;
import com.yjy.web.comm.utils.IPUtil;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.user.LoginRecord;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.user.UserLongRecordService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;
import java.util.Random;


@Controller
@RequestMapping
public class LoginsController {

	private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDao userDao;

	@Autowired
	UserLongRecordService userLongRecordService;
	
	public static final String CAPTCHA_KEY = "session_captcha";

	private Random rnd = new Random();

	/**
	 * 登录界面的显示
	 * @return
	 */
	@RequestMapping(value={"/", "logins"})
	public String login(HttpSession session,HttpServletRequest req){
		String appname = String.valueOf(req.getParameter("appname"));
		if ("wyzyyoa".equals(appname)) {
			System.out.println("CorsConfig:" + ReqInterceptor.getLoginUser(IPUtil.getReqIP(req)).toString());
			String userName = ReqInterceptor.getLoginUser(IPUtil.getReqIP(req)).toString();
			User user = userDao.findByUserName(userName);
			session.setAttribute("userId", user.getUserId());
			saveLoginRecord(req, user);
			return "redirect:/index";
		} else {
			return "login/login";
		}
	}

	/**
	 * 保存登录记录
	 * @param req
	 * @param user
	 */
	private void saveLoginRecord(HttpServletRequest req, User user){
		Browser browser = UserAgent.parseUserAgentString(req.getHeader("User-Agent")).getBrowser();
		Version version = browser.getVersion(req.getHeader("User-Agent"));
		String info = browser.getName() + "/" + version.getVersion();
		String ip="unknown";
		try{
			ip = InetAddress.getLocalHost().getHostAddress();
		}catch (Exception e){
			e.printStackTrace();
		}
		LoginRecord loginRecord = new LoginRecord(ip, new Date(), info, user);
		logger.info("save login record="+loginRecord);
		userLongRecordService.save(loginRecord);
	}

	/**
	 * 登录检查；
	 * 1、根据(用户名或电话号码)+密码进行查找
	 * 2、判断使用是否被冻结；
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value="logins",method = RequestMethod.POST)
	public String loginCheck(HttpSession session,HttpServletRequest req,Model model) throws UnknownHostException{
		String userName=req.getParameter("userName").trim();
		String password=req.getParameter("password");
//		String ca=req.getParameter("code").toLowerCase();
//		String sesionCode = (String) req.getSession().getAttribute(CAPTCHA_KEY);
		model.addAttribute("userName", userName);
//		if(!ca.equals(sesionCode.toLowerCase())){
//			System.out.println("验证码输入错误!");
//			model.addAttribute("errormess", "验证码输入错误!");
//			req.setAttribute("errormess","验证码输入错误!");
//			return "login/login";
//		}

		User user= userDao.findOneUser(userName, password);

		if(null == user){
			logger.error("name or tel="+userName+", pwd="+password+", miss match!");
			model.addAttribute("errormess", "账号或密码错误");
			return "login/login";
		}

		if(user.getIsLock()==1){
			logger.error("name or tel="+userName+", 账号已被冻结");
			model.addAttribute("errormess", "账号已被冻结!");
			return "login/login";
		}

		logger.info("user="+user);
		Object userId = session.getAttribute("userId");
		if(userId == user.getUserId()){
			logger.warn("name or tel="+userName+", 当前用户已登录");
			model.addAttribute("hasmess", "当前用户已登录");
			session.setAttribute("thisuser", user);
			return "login/login";
		}else{
			session.setAttribute("userId", user.getUserId());
			saveLoginRecord(req, user);
		}
		return "redirect:/index";
	}

	/**
	 * OA直接登录
	 * 登录检查；
	 * 1、根据(用户名或电话号码)+密码进行查找
	 * 2、判断使用是否被冻结；
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value="loginsOA")
	public String loginCheckOA(HttpSession session,HttpServletRequest req,Model model) throws UnknownHostException{
		String userName=req.getParameter("userName").trim();
		String password=req.getParameter("password");
		model.addAttribute("userName", userName);
		/*
		 * 将用户名分开查找；用户名或者电话号码；
		 * */
		User user= userDao.findOneUser(userName, password);
		if(Objects.isNull(user)){
			System.out.println(user);
			System.out.println("账号或密码错误!");
			model.addAttribute("errormess", "账号或密码错误!");
			return "login/login";
		}
		System.out.println("是否被锁："+user.getIsLock());
		if(user.getIsLock()==1){
			System.out.println("账号已被冻结!");
			model.addAttribute("errormess", "账号已被冻结!");
			return "login/login";
		}
		Object sessionId=session.getAttribute("userId");
		System.out.println(user);
		if(sessionId==user.getUserId()){
			System.out.println("当前用户已经登录了；不能重复登录");
			model.addAttribute("hasmess", "当前用户已经登录了；不能重复登录");
			session.setAttribute("thisuser", user);
			return "/index";
			//return "login/login";
		}else{
			session.setAttribute("userId", user.getUserId());
			Browser browser = UserAgent.parseUserAgentString(req.getHeader("User-Agent")).getBrowser();
			Version version = browser.getVersion(req.getHeader("User-Agent"));
			String info = browser.getName() + "/" + version.getVersion();
			String ip=InetAddress.getLocalHost().getHostAddress();
			/*新增登录记录*/
			userLongRecordService.save(new LoginRecord(ip, new Date(), info, user));
		}
		return "/index";//redirect:
	}
	
	@RequestMapping("loginout")
	public String loginout(HttpSession session){
		session.removeAttribute("userId");
		return "redirect:/logins";
	}
	
	
	@RequestMapping("handlehas")
	public String handleHas(HttpSession session){
		if(!StringUtils.isEmpty(session.getAttribute("thisuser"))){
			User user=(User) session.getAttribute("thisuser");
			System.out.println(user);
			session.removeAttribute("userId");
			session.setAttribute("userId", user.getUserId());
		}else{
			System.out.println("有问题！");
			return "login/login";
		}
		return "redirect:/index";
		
	}
	
	
	@RequestMapping("captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException{
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		
		// 生成图片
		int w = 135, h = 40;
		VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);

		// 将验证码存储在session以便登录时校验
		session.setAttribute(CAPTCHA_KEY, verifyCode.toLowerCase());
	}
	

}

package com.yjy.web.comm.Interceptor;

import com.yjy.web.comm.utils.ApplicationContextUtil;
import com.yjy.web.mms.model.dao.roledao.RolepowerlistDao;
import com.yjy.web.mms.model.dao.system.SystemMenuDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.dao.user.UserLogDao;
import com.yjy.web.mms.model.entity.role.Rolemenu;
import com.yjy.web.mms.model.entity.system.SystemMenu;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.model.entity.user.UserLog;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OA请求记录拦截器
 */
@Component
public class ReqRecInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


		HttpSession session = request.getSession();
		UserDao userDao = ApplicationContextUtil.getBean(UserDao.class, request);
		String userName = "";
		Long userId = 0L;
		User loginuser;
		if (!StringUtils.isEmpty(session.getAttribute("userId"))) {
			userId = Long.parseLong(session.getAttribute("userId") + "");
//			userName=request.getParameter("userName");//CorsConfig.getLoginUser(CorsConfig.getIP(request)).toString();//request.getParameter("userName");
			loginuser = userDao.findByUserId(userId).get(0);
			userId = loginuser.getUserId();
			session.setAttribute("userId", userId);
		}
//		else if(!StringUtils.isEmpty(CorsConfig.getLoginUser(CorsConfig.getIP(request)).toString())) {
//			System.out.println(CorsConfig.getLoginUser(CorsConfig.getIP(request)));
//			userName=CorsConfig.getLoginUser(CorsConfig.getIP(request)).toString();//request.getParameter("userName");
//			loginuser=userDao.findid(userName);
//			userId=loginuser.getUserId();
//			session.setAttribute("userId", userId);
//		}
		if (userId != 0) {//session.getAttribute("userId")
			//导入dao类
			UserDao udao = ApplicationContextUtil.getBean(UserDao.class, request);
			RolepowerlistDao rpdao = ApplicationContextUtil.getBean(RolepowerlistDao.class, request);
			Long uid = Long.parseLong(userId + "");//session.getAttribute("userId")

			User user = udao.findById(uid).orElse(null);
			List<Rolemenu> oneMenuAll = rpdao.findbyparentxianall(0L, user.getRole().getRoleId(), true, false);
			List<Rolemenu> twoMenuAll = rpdao.findbyparentsxian(0L, user.getRole().getRoleId(), true, false);
			List<Rolemenu> all = new ArrayList<>();
			//获取当前访问的路径
			String url = request.getRequestURL().toString();
			String zhuan = "notlimit";
			if (oneMenuAll.size() > 0) {
				all.addAll(oneMenuAll);
			}
			if (twoMenuAll.size() > 0) {
				all.addAll(twoMenuAll);
			}
			for (Rolemenu rolemenu : all) {
				if (!rolemenu.getMenuUrl().equals(url)) {
					return true;
				} else {
					request.getRequestDispatcher(zhuan).forward(request, response);
				}
			}
		} else {
			//response.sendRedirect("/logins"); 取消重定向
			
		}

		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,  Object handler, Exception ex) throws Exception {
		HttpSession session = request.getSession();
		//导入dao类
		UserDao userDao = ApplicationContextUtil.getBean(UserDao.class, request);
		SystemMenuDao systemMenuDao = ApplicationContextUtil.getBean(SystemMenuDao.class, request);
		UserLogDao userLogDao = ApplicationContextUtil.getBean(UserLogDao.class, request);

		UserLog userLog = new UserLog();
		//首先就获取ip
		InetAddress inetAddress = null;
		inetAddress = inetAddress.getLocalHost();
		String ip = inetAddress.getHostAddress();
		userLog.setIpAddr(ip);
		//System.out.println(request.getRequestedSessionId());
		userLog.setUrl(request.getServletPath());
		userLog.setLogTime(new Date());

		//还没有登陆不能获取session  Long.valueOf(session.getAttribute("userId")+"")
		String userName = "";
		Long userId = 0L;
		User user;
		if (!StringUtils.isEmpty(session.getAttribute("userId"))) {
			userId = Long.parseLong(session.getAttribute("userId") + "");
		} else if (!StringUtils.isEmpty(request.getParameter("userName"))) {
			userName = request.getParameter("userName");
			user = userDao.findid(userName);
			userId = user.getUserId();
		}
		String path = request.getServletPath();
		userLog.setUser(userDao.getOne(userId));
//		uLog.setUser(userDao.findOne(1l));
		//从菜单表里面匹配
		List<SystemMenu> sMenus = (List<SystemMenu>) systemMenuDao.findAll();
		for (SystemMenu systemMenu : sMenus) {
			if (systemMenu.getMenuUrl().equals(request.getServletPath())) {
				//只有当该记录的路径不等于第一条的时候
				//if (!userLogDao.findByUserlaset(1l).getUrl().equals(systemMenu.getMenuUrl())) {
				//	userLog.setTitle(systemMenu.getMenuName());
					//只要匹配到一个保存咯
					//userLogDao.save(userLog);
				//}
			}
		}
		
		
	}

}

package com.yjy.web.mms.controller.deanrounds;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.comm.utils.crypt.aes.AESUtil;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yjy.web.mms.model.dao.deanroundsdao.DeanroundsDao;
import com.yjy.web.mms.model.dao.deanroundsdao.DeanroundsDetailDao;
import com.yjy.web.mms.model.dao.deanroundsdao.DeptRespondsDao;
import com.yjy.web.mms.model.dao.deanroundsdao.DeptSuggestionDao;
import com.yjy.web.mms.model.entity.deanrounds.DeanroundsDetailList;
import com.yjy.web.mms.model.entity.deanrounds.Deanroundslist;
import com.yjy.web.mms.model.entity.deanrounds.DeptResponds;
import com.yjy.web.mms.model.entity.deanrounds.DeptSuggestion;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.Position;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.deanrounds.DeanroundsService;

@Controller
@RequestMapping("/")
public class DeanroundsController {

	@Autowired
	private DeanroundsDao tdao;
	@Autowired
	private UserDao udao;
	@Autowired
	private DeptDao ddao;
	@Autowired
	private DeptSuggestionDao dsdao;
	@Autowired
	private DeanroundsDetailDao detaildao;
	
	@Autowired
	private DeptRespondsDao drdao;
	
	
	@Autowired
	private DeanroundsService tservice;
	@Autowired
	private PositionDao pdao;
	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("deanroundsmanage")
	public String index(Model model,
			@SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		// ???????????????id?????????
		User tu = udao.getOne(userId);
		// ???????????????id????????????
		Page<Deanroundslist> deanroundslist=tservice.index(page, size, null, tu);
		List<Map<String, Object>> list=tservice.index2(deanroundslist, tu);
	
		model.addAttribute("deanroundslist", list);
		model.addAttribute("page", deanroundslist);
		model.addAttribute("url", "paixu");
		return "deanrounds/deanroundsmanage";
	}
	/**
	 * ??????????????????
	 */
	@RequestMapping("adddeanrounds")
	public String index2(Model model,@SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		
		// ??????????????????
		Iterable<Dept> LcksDeptlist = ddao.findAll();
		//
		Pageable pa1=PageRequest.of(page, size);
		Page<User> pageuser1=udao.findAll(pa1);
		List<User> userlist1=pageuser1.getContent();
		// ????????????
		Iterable<Position> poslist = pdao.findAll();
		
		if(!this.checkJuri(userId, model)) {
			return "common/proce";
		}
		
		
		
		//Pageable pa=PageRequest.of(page, size);
		//ModelAndView mav = new ModelAndView("deanrounds/adddeanrounds");
		// ???????????????
		Iterable<Dept> deptlist = ddao.findznks();
		//???????????????????????????
		//Iterable<DeptSuggestion> deptSuggestion = dsdao.findBydeanId(0L);
		
//		// ???????????????id?????????
		User tu = udao.getOne(userId);
		Page<Deanroundslist> deanroundslist=tservice.index(page, size, null, tu);
		List<Map<String, Object>> list=tservice.index2(deanroundslist, tu);
		model.addAttribute("deanroundslist", list);
		model.addAttribute("deptlist", deptlist);
		//model.addAttribute("deptSuggestion", deptSuggestion);
		
		model.addAttribute("url", "names");
		model.addAttribute("qufen", "????????????");
		
		model.addAttribute("page", pageuser1);
		model.addAttribute("emplist", userlist1);
		model.addAttribute("deptlist", LcksDeptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "namesfather");
		model.addAttribute("username", "");

		
		
		return "deanrounds/adddeanrounds";
	}

	/**
	 * ????????????
	 */
	@RequestMapping("finduser")
	public String finduser(Model model,HttpServletRequest req, @SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "50") int size){
		Pageable pa=PageRequest.of(page, size);
		String name=null;
		String qufen=null;
		Page<User> pageuser=null;
		List<User> userlist=null;
		
		if(StringUtil.stringIsNotNull(req.getParameter("title"))){
			name=req.getParameter("title").trim();
		}
		if(StringUtil.stringIsNotNull(req.getParameter("qufen"))){
			qufen=req.getParameter("qufen").trim();
			
			System.out.println("111");
			if(!StringUtil.stringIsNotNull(name)){
				// ???????????????????????????
				pageuser = udao.findByFatherId(userId,pa);
			}else{
				// ??????????????????????????????
				pageuser = udao.findnamelike(name, pa);
				
			}
			
		}else{
			System.out.println("222");
			if(!StringUtil.stringIsNotNull(name)){
				//?????????????????????
				pageuser=udao.findAll(pa);
			}else{
				pageuser=udao.findbyUserNameorDeptNameLike(name, pa);
			}
		}
		userlist=pageuser.getContent();
		// ???????????????
		Iterable<Dept> deptlist = ddao.findAll();
		// ????????????
		Iterable<Position> poslist = pdao.findAll();
		model.addAttribute("emplist", userlist);
		model.addAttribute("page", pageuser);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "names");
		
		return "common/recivers";
		
	}
	/**
	 * ????????????
	 
	@RequestMapping("paixu")
	public String paixu(HttpServletRequest request, 
			@SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
	
		// ???????????????id?????????
		User tu = udao.getOne(userId);
		String val=null;
		if(!StringUtil.isEmpty(request.getParameter("val"))){
			val = request.getParameter("val").trim();
			model.addAttribute("sort", "&val="+val);
		}
		
		Page<Tasklist> tasklist=tservice.index(page, size, val, tu);
		List<Map<String, Object>> list=tservice.index2(tasklist, tu);
		model.addAttribute("tasklist", list);
		model.addAttribute("page", tasklist);
		model.addAttribute("url", "paixu");
		
		return "task/managetable";

	}*/



	/**
	 * ??????????????????
	 * @throws ParseException 
	 */
	@RequestMapping(value="addcfapsubmit",method=RequestMethod.POST)
	public String addcfapsubmit(@SessionAttribute("userId") Long userId, HttpServletRequest request,Model model) throws ParseException {
		
       
		//???????????????
		User sqr = udao.getOne(userId);
		
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date publishDate=format.parse(String.valueOf(request.getParameter("starTime"))) ;
		String cfks=StringUtil.stringToFormat((String) request.getParameter("cfks"));
		String cfap=StringUtil.stringToFormat((String) request.getParameter("cfap")) ;
		String deanroundsId=String.valueOf(request.getParameter("deanroundsId"));
		String nameuser=(String) request.getParameter("nameuser");
		
		
		
		Deanroundslist deanroundslist ;
		
		
        if(StringUtil.stringIsNotNull(deanroundsId)) {
            deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsId) );

            //??????????????????????????????????????? ||(!deanroundslist.getPublishDate().equals(publishDate) )
            if((!StringUtil.stringToFormat(deanroundslist.getCfks()).equals(cfks) )
             ||(!StringUtil.stringToFormat(deanroundslist.getCfap()).equals(cfap) )
             ||(!StringUtil.stringToFormat(deanroundslist.getCffzr()).equals(nameuser) )
            		) {
            	//??????????????????????????????????????????????????? ?????????????????????
            	if(checkJuri(userId,model)) {
            		deanroundslist.setPublishDate(publishDate);
            		deanroundslist.setCfks(cfks);
            		deanroundslist.setCfap(cfap);
            		deanroundslist.setCffzr(nameuser);
            	}else {
    				model.addAttribute("error", "??????????????????????????????????????????????????????????????????????????????");
    				return "common/proce";
            	}
            	
            }
        }else {
        	deanroundslist = new Deanroundslist();
    		deanroundslist.setPublishDate(publishDate);
    		deanroundslist.setCfks(cfks);
    		deanroundslist.setCfap(cfap);
    		deanroundslist.setCffzr(nameuser);
        }
		String znks=(String) request.getParameter("znks");
		String znksjy=StringUtil.stringToFormat((String) request.getParameter("znksjy")) ;
		Dept dept=ddao.findbydeptName(znks);
		DeptSuggestion deptSuggestion;
		 if(StringUtil.stringIsNotNull(znks)) {
			 if(StringUtil.stringIsNotNull(deanroundsId)) {
				 
				 List<DeptSuggestion> deptSuggestionList = dsdao.findDeptSugByDeanIds(Long.parseLong(deanroundsId),dept.getDeptId());
					if(deptSuggestionList.size()>0) {
						deptSuggestion=deptSuggestionList.get(0);
						//???????????????????????????????????????
						if(!deptSuggestion.getContent().equalsIgnoreCase(znksjy)) {
							//????????????????????????????????????????????????
							if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
								deptSuggestion.setDept(dept);
								deptSuggestion.setContent(znksjy);
								dsdao.save(deptSuggestion);
							}else {
			    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
			    				return "common/proce";
							}
						}
					}else {
						if(!znksjy.equals("")) {
							//???????????????????????????????????????
							if(!znksjy.equals("")) {
								//????????????????????????????????????????????????
								if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
									deptSuggestion = new DeptSuggestion();
									deptSuggestion.setContent(znksjy);
									deptSuggestion.setDept(dept);
									deptSuggestion.setDeanId(deanroundslist);
									dsdao.save(deptSuggestion);
								}else {
				    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
				    				return "common/proce";
								}
							}
						}
					}
			 }
		 }
		tdao.save(deanroundslist);
		
		return "redirect:/deanroundsmanage";
	}
	
	/**
	 * ????????????????????????
	 * @throws ParseException 
	 */
	@RequestMapping(value="addksgksubmit",method=RequestMethod.POST)
	public String addksgksubmit(@SessionAttribute("userId") Long userId, HttpServletRequest request,Model model) throws ParseException {
		
	       
		//???????????????
		User sqr = udao.getOne(userId);
		String fzgh=StringUtil.stringToFormat((String) request.getParameter("fzgh"));
		String cjfx=StringUtil.stringToFormat((String) request.getParameter("cjfx"));
		String ksgk=StringUtil.stringToFormat((String) request.getParameter("ksgk"));
		String deanroundsId=String.valueOf(request.getParameter("deanroundsId"));
		
		
		
		Deanroundslist deanroundslist ;
        if(StringUtil.stringIsNotNull(deanroundsId)) {
            deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsId) );
            //?????????????????? ???????????????????????????
            if((!StringUtil.stringToFormat(deanroundslist.getFzgh()).equals(fzgh) )
            		||(!StringUtil.stringToFormat(deanroundslist.getCjfx()).equals(cjfx) )
            		||(!StringUtil.stringToFormat(deanroundslist.getKsgk()).equals(ksgk) )) {
            	//??????????????????????????????????????????????????? ?????????????????????
            	if(sqr.getUserName().equalsIgnoreCase(deanroundslist.getCffzr().split("/")[0])||checkJuri(userId,model)) {
            		deanroundslist.setFzgh(fzgh);
            		deanroundslist.setCjfx(cjfx);
            		deanroundslist.setKsgk(ksgk);
            	}else {
    				model.addAttribute("error", "???????????????????????????????????????????????????????????????????????????????????????????????????????????????");
    				return "common/proce";
            	}
            }
            tdao.save(deanroundslist);
            return "redirect:/editdeanrounds?id="+deanroundsId+"&tab=1";
            
        }
		return "redirect:/deanroundsmanage";
	}
	
	
	
	
	
	
	
	
	
	/**
	 * ??????????????????
	 * @throws Exception 
	 */
	@RequestMapping(value="addkshysubmit",method=RequestMethod.POST)
	public String addkshysubmit(@RequestParam("filePath")MultipartFile[] filePath,@SessionAttribute("userId") Long userId, HttpServletRequest request,Model model) throws Exception {
		
		
		
		String znksjywjurl=String.valueOf(request.getParameter("znksjywjurl"));
		String znksjywj_url="";
		String znksjywj_file="";	
		
		if(!znksjywjurl.contentEquals("")) {
			JSONObject jsonObject =JSONObject.parseObject(AESUtil.decrypt(znksjywjurl, "www.bjchweb.prod")) ;
			 znksjywj_url=jsonObject.getString("url").toString();
			 znksjywj_file=jsonObject.getString("file").toString();
		}
		//???????????????
		User sqr = udao.getOne(userId);
		String deanroundsId=String.valueOf(request.getParameter("deanroundsId"));
		Deanroundslist deanroundslist ;
        if(StringUtil.stringIsNotNull(deanroundsId)) {
            deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsId) );
    		String znks=(String) request.getParameter("znks");
    		String znksjy=StringUtil.stringToFormat((String) request.getParameter("znksjy")) ;
    		Dept dept=ddao.findbydeptName(znks);
    		DeptSuggestion deptSuggestion;
    		 if(StringUtil.stringIsNotNull(znks)) {
    			 if(StringUtil.stringIsNotNull(deanroundsId)) {
    				 
    				 List<DeptSuggestion> deptSuggestionList = dsdao.findDeptSugByDeanIds(Long.parseLong(deanroundsId),dept.getDeptId());
    					if(deptSuggestionList.size()>0) {
    						deptSuggestion=deptSuggestionList.get(0);
    						//???????????????????????????????????????
    						if((!deptSuggestion.getContent().equalsIgnoreCase(znksjy))||(!znksjywj_file.equals(""))||(!znksjywj_url.equals(""))) {
    							//????????????????????????????????????????????????
    							if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
    								deptSuggestion.setDept(dept);
    								deptSuggestion.setContent(znksjy);
    								if((!znksjywj_file.equals(""))||(!znksjywj_url.equals(""))) {
    									deptSuggestion.setPdfname(znksjywj_file);
        								deptSuggestion.setPdfurl(znksjywj_url);
    								}
    								
    								dsdao.save(deptSuggestion);
    							}else {
    			    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
    			    				return "common/proce";
    							}
    						}
    					}else {
    						if((!znksjy.equals(""))||(!znksjywj_file.equals(""))||(!znksjywj_url.equals(""))) {
    								//????????????????????????????????????????????????
    								if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
    									deptSuggestion = new DeptSuggestion();
    									deptSuggestion.setContent(znksjy);
    									deptSuggestion.setDept(dept);
    									deptSuggestion.setDeanId(deanroundslist);
    									
        								if((!znksjywj_file.equals(""))||(!znksjywj_url.equals(""))) {
            								deptSuggestion.setPdfname(znksjywj_file);
            								deptSuggestion.setPdfurl(znksjywj_url);
        								}

    									dsdao.save(deptSuggestion);
    								}else {
    				    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
    				    				return "common/proce";
    								}
    						}
    					}
    			 }
    		 }
    		tdao.save(deanroundslist);
        }

        return "redirect:/editdeanrounds?id="+deanroundsId+"&tab=2";
		//return "redirect:/deanroundsmanage";
	}
	
	
	
	
	/**
	 * ??????????????????
	 * @throws ParseException 
	 */
	@RequestMapping(value="adddeanroundssubmit",method=RequestMethod.POST)
	public String adddeanrounds(@SessionAttribute("userId") Long userId, HttpServletRequest request,Model model) throws ParseException {
		
       
		//???????????????
		User sqr = udao.getOne(userId);
		
//		String title=(String) request.getParameter("title");
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date publishDate=format.parse(String.valueOf(request.getParameter("starTime"))) ;
		String cfks=(String) request.getParameter("cfks");
//		String cflx=(String) request.getParameter("cflx");
//		String hydd=(String) request.getParameter("hydd");
		String cfap=(String) request.getParameter("cfap");
		String fzgh=(String) request.getParameter("fzgh");
		String ksgk=(String) request.getParameter("ksgk");
		String deanroundsId=String.valueOf(request.getParameter("deanroundsId"));
		String nameuser=(String) request.getParameter("nameuser");
		
		
		
		Deanroundslist deanroundslist ;
		
		
        if(StringUtil.stringIsNotNull(deanroundsId)) {
            deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsId) );
            //?????????????????? ???????????????????????????
            if((!StringUtil.stringToFormat(deanroundslist.getFzgh()).equals(fzgh) )||(!StringUtil.stringToFormat(deanroundslist.getKsgk()).equals(ksgk) )) {
            	//??????????????????????????????????????????????????? ?????????????????????
            	if(sqr.getUserName().equalsIgnoreCase(deanroundslist.getCffzr().split("/")[0])||checkJuri(userId,model)) {
            		deanroundslist.setFzgh(fzgh);
            		deanroundslist.setKsgk(ksgk);
            	}else {
    				model.addAttribute("error", "???????????????????????????????????????????????????????????????????????????????????????????????????????????????");
    				return "common/proce";
            	}
            }
            //??????????????????????????????????????? ||(!deanroundslist.getPublishDate().equals(publishDate) )
            if((!deanroundslist.getCfks().equals(cfks) )
             ||(!deanroundslist.getCfap().equals(cfap) )
             ||(!deanroundslist.getCffzr().equals(nameuser) )
            		) {
            	//??????????????????????????????????????????????????? ?????????????????????
            	if(checkJuri(userId,model)) {
//            		deanroundslist.setTitle(title);
            		deanroundslist.setPublishDate(publishDate);
            		deanroundslist.setCfks(cfks);
            		deanroundslist.setCfap(cfap);
//            		deanroundslist.setCflx(cflx);
//            		deanroundslist.setHydd(hydd);
            		deanroundslist.setCffzr(nameuser);
            	}else {
    				model.addAttribute("error", "??????????????????????????????????????????????????????????????????????????????");
    				return "common/proce";
            	}
            	
            }
        }else {
        	deanroundslist = new Deanroundslist();
//    		deanroundslist.setTitle(title);
    		deanroundslist.setPublishDate(publishDate);
    		deanroundslist.setCfks(cfks);
    		deanroundslist.setCfap(cfap);
//    		deanroundslist.setCflx(cflx);
//    		deanroundslist.setHydd(hydd);
    		deanroundslist.setFzgh(fzgh);
    		deanroundslist.setKsgk(ksgk);
    		deanroundslist.setCffzr(nameuser);
        }
		String znks=(String) request.getParameter("znks");
		String znksjy=StringUtil.stringToFormat((String) request.getParameter("znksjy")) ;
		Dept dept=ddao.findbydeptName(znks);
		DeptSuggestion deptSuggestion;
		 if(StringUtil.stringIsNotNull(znks)) {
			 if(StringUtil.stringIsNotNull(deanroundsId)) {
				 
				 List<DeptSuggestion> deptSuggestionList = dsdao.findDeptSugByDeanIds(Long.parseLong(deanroundsId),dept.getDeptId());
					if(deptSuggestionList.size()>0) {
						deptSuggestion=deptSuggestionList.get(0);
						//???????????????????????????????????????
						if(!deptSuggestion.getContent().equalsIgnoreCase(znksjy)) {
							//????????????????????????????????????????????????
							if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
								deptSuggestion.setDept(dept);
								deptSuggestion.setContent(znksjy);
								dsdao.save(deptSuggestion);
							}else {
			    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
			    				return "common/proce";
							}
						}
					}else {
						if(!znksjy.equals("")) {
							//???????????????????????????????????????
							if(!znksjy.equals("")) {
								//????????????????????????????????????????????????
								if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
									deptSuggestion = new DeptSuggestion();
									deptSuggestion.setContent(znksjy);
									deptSuggestion.setDept(dept);
									deptSuggestion.setDeanId(deanroundslist);
									dsdao.save(deptSuggestion);
								}else {
				    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
				    				return "common/proce";
								}
							}
						}
					}
			 }
		 }
		tdao.save(deanroundslist);
		
		return "redirect:/deanroundsmanage";
	}
	
	
	
	
	

	/**
	 * ??????????????????
 */
	@RequestMapping("editdeanrounds")
	public String index3(HttpServletRequest req, @SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,Model model
			) {
		
		// ????????????????????????id
		String deanroundsid = req.getParameter("id");
		//Pageable pa=PageRequest.of(page, size);
		//ModelAndView mav = new ModelAndView("deanrounds/adddeanrounds");
		
		String tab = req.getParameter("tab");
		if(StringUtil.stringToFormat(tab).equals("")) {
			model.addAttribute("tab", "0");
		}else
		{
			model.addAttribute("tab",tab);
		}
		
		
        if(deanroundsid!="") {
        	
        	Deanroundslist deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsid) );
        	if(deanroundslist!=null) {
        		
        		// ???????????????
        		Iterable<Dept> deptlist = ddao.findznks();
        		//???????????????????????????
        		List<DeptSuggestion> deptSuggestionList = dsdao.findDeptSugByDeanIds(Long.parseLong(deanroundsid));
        		// ???????????????id?????????
        		List<DeanroundsDetailList> deansdetaillist = tdao.findDeansdetailBydeanroundsId(Long.parseLong(deanroundsid));
        		
        		
        		model.addAttribute("deanroundslist", deanroundslist);
        		model.addAttribute("deptSuggestionList", deptSuggestionList);
        		model.addAttribute("deansdetaillist", deansdetaillist);

        		
        		model.addAttribute("deptlist", deptlist);
        		//model.addAttribute("deptSuggestion", deptSuggestion);
        		Pageable pa1=PageRequest.of(page, size);
        		Page<User> pageuser1=udao.findAll(pa1);
        		List<User> userlist1=pageuser1.getContent();

        		// ????????????
        		// ??????????????????
//        		Iterable<Dept> LcksDeptlist = ddao.findAll();
        		Iterable<Position> poslist = pdao.findAll();
        		model.addAttribute("page", pageuser1);
        		model.addAttribute("emplist", userlist1);
//        		model.addAttribute("deptlist", LcksDeptlist);
        		model.addAttribute("poslist", poslist);
        		model.addAttribute("username", "");
        		
        		
        		
        		
        		model.addAttribute("url", "names");
        		model.addAttribute("qufen", "????????????");
        		//??????????????????
        		tservice.setJurisdiction( model, userId,deanroundslist.getCffzr());
        		
        	}else {
    			model.addAttribute("error", "?????????????????????????????????????????????");
    			return "common/proce";
        	}
        }else {
    			model.addAttribute("error", "?????????????????????????????????????????????");
    			return "common/proce";
        }

        
        
        
        
		
		return "deanrounds/editdeanrounds";
	}

	
	/**
	 ??????????????????
 */
	@RequestMapping("browsedeanrounds")
	public String browsedeanrounds(HttpServletRequest req, @SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,Model model
			) {
		
		// ????????????????????????id
		String deanroundsid = req.getParameter("id");
		//Pageable pa=PageRequest.of(page, size);
		//ModelAndView mav = new ModelAndView("deanrounds/adddeanrounds");
		
		String tab = req.getParameter("tab");
		if(StringUtil.stringToFormat(tab).equals("")) {
			model.addAttribute("tab", "0");
		}else
		{
			model.addAttribute("tab",tab);
		}
		
		
		
        if(deanroundsid!="") {
        	
        	Deanroundslist deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsid) );
        	if(deanroundslist!=null) {
        		
        		// ???????????????
        		Iterable<Dept> deptlist = ddao.findznks();
        		//???????????????????????????
        		List<DeptSuggestion> deptSuggestionList = dsdao.findDeptSugByDeanIds(Long.parseLong(deanroundsid));
        		// ???????????????id?????????
        		List<DeanroundsDetailList> deansdetaillist = tdao.findDeansdetailBydeanroundsId(Long.parseLong(deanroundsid));
        		
        		
        		model.addAttribute("deanroundslist", deanroundslist);
        		model.addAttribute("deptSuggestionList", deptSuggestionList);
        		model.addAttribute("deansdetaillist", deansdetaillist);

        		
        		model.addAttribute("deptlist", deptlist);
        		//model.addAttribute("deptSuggestion", deptSuggestion);
        		Pageable pa1=PageRequest.of(page, size);
        		Page<User> pageuser1=udao.findAll(pa1);
        		List<User> userlist1=pageuser1.getContent();

        		// ????????????
        		// ??????????????????
//        		Iterable<Dept> LcksDeptlist = ddao.findAll();
        		Iterable<Position> poslist = pdao.findAll();
        		model.addAttribute("page", pageuser1);
        		model.addAttribute("emplist", userlist1);
//        		model.addAttribute("deptlist", LcksDeptlist);
        		model.addAttribute("poslist", poslist);
        		model.addAttribute("username", "");
        		
        		
        		
        		
        		model.addAttribute("url", "names");
        		model.addAttribute("qufen", "????????????");
        		//??????????????????
        		tservice.setJurisdiction( model, userId,deanroundslist.getCffzr());
        		
        	}else {
    			model.addAttribute("error", "?????????????????????????????????????????????");
    			return "common/proce";
        	}
        }else {
    			model.addAttribute("error", "?????????????????????????????????????????????");
    			return "common/proce";
        }

        
        
        
        
		
		return "deanrounds/browsedeanrounds";
	}

	/**
	 ????????????????????????
*/
	@RequestMapping("browsedeanroundslist")
	public String browsedeanroundslist(HttpServletRequest req, @SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,Model model
			) {
		
		// ????????????????????????id
		String deanroundsid = req.getParameter("id");
		
		String deptname = req.getParameter("deptname");
		Dept dept=ddao.findbydeptName(deptname);
		//Pageable pa=PageRequest.of(page, size);
		//ModelAndView mav = new ModelAndView("deanrounds/adddeanrounds");
       if(deanroundsid!="") {
       	
       	Deanroundslist deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsid) );
       	if(deanroundslist!=null) {
       		
       		// ???????????????
       		Iterable<Dept> deptlist = ddao.findznks();
       		//???????????????????????????
       		List<DeptSuggestion> deptSuggestionList = dsdao.findDeptSugByDeanIds(Long.parseLong(deanroundsid));
       		// ??????????????????
       		List<DeanroundsDetailList> deansdetaillist = tdao.findDeansdetailBydeanroundsId(Long.parseLong(deanroundsid));
       		
       		//????????????????????????
       		
       		List<DeanroundsDetailList> redeansdetaillist = new ArrayList<DeanroundsDetailList>();
       		
       		
       		List<DeptResponds> redeptRespondsList = new ArrayList<DeptResponds>();
       		for(int i=0;i<deansdetaillist.size();i++) {
       			Long deanDetailId=deansdetaillist.get(i).getDeanroundsDetailId();
       			List<DeptResponds> deptRespondsList;
       			if(deptname.equals("????????????")) {
       				deptRespondsList = drdao.findDeptResByDeanIds(deanDetailId);
       			}else {
       				deptRespondsList = drdao.findDeptResByDeanIds(deanDetailId,dept.getDeptId());
       			}
       			
//       			
       			
       			if(deptRespondsList.size()>0) {
       				redeptRespondsList.addAll(deptRespondsList);
       				redeansdetaillist.add(deansdetaillist.get(i));
       			}else
       			{
//       				if(deptname.equals("????????????")) {
//       					redeansdetaillist.add(deansdetaillist.get(i));
//       				}
//       				deansdetaillist.remove(i);
       				//deansdetaillist.get(i).setKshy(""); 
       			}
       			
       		}
       		
       		model.addAttribute("deanroundslist", deanroundslist);
       		model.addAttribute("deptSuggestionList", deptSuggestionList);
       		model.addAttribute("deansdetaillist", redeansdetaillist );//deansdetaillist
       		model.addAttribute("redeptRespondsList", redeptRespondsList);

       		
       		model.addAttribute("deptlist", deptlist);
       		//model.addAttribute("deptSuggestion", deptSuggestion);
       		
       		model.addAttribute("url", "names");
       		model.addAttribute("qufen", "????????????");
       		//??????????????????
       		tservice.setJurisdiction( model, userId,deanroundslist.getCffzr());
       		
       	}else {
   			model.addAttribute("error", "?????????????????????????????????????????????");
   			return "common/proce";
       	}
       }else {
   			model.addAttribute("error", "?????????????????????????????????????????????");
   			return "common/proce";
       }
		return "deanrounds/browsedeanroundslist";
	}
	
	
	/**
	 * ????????????????????????????????????????????????
	 * @param req
	 * @param userId
	 * @return
	 */
	@RequestMapping("deletedeanrounds")
	public String delete(HttpServletRequest req, @SessionAttribute("userId") Long userId,Model model) {
		
		if(!this.checkJuri(userId, model)) {
			
			return "common/proce";
		}
		
		
		// ??????????????? id
		String deanroundsId = req.getParameter("id");
		Deanroundslist deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsId) );
		//???????????????????????????
		List<DeptSuggestion> deptSuggestionList = dsdao.findDeptSugByDeanIds(Long.parseLong(deanroundsId));
		// ???????????????????????????
		List<DeanroundsDetailList> deansdetaillist = tdao.findDeansdetailBydeanroundsId(Long.parseLong(deanroundsId));
	    if(deansdetaillist.size()>0) {
			model.addAttribute("error", "???????????????????????????????????????????????????");
			return "common/proce";
	    }
	    if(deptSuggestionList.size()>0) {
	    	StringBuffer deptSuggestions=new StringBuffer();
	    	for(int i=0;i<deptSuggestionList.size();i++) {
	    		String deptname=String.valueOf(deptSuggestionList.get(i).getDept());
	    		if(StringUtil.stringIsNotNull(deptname)) {
		    		if(i==0) {
		    			deptSuggestions.append(String.valueOf(deptSuggestionList.get(i).getDept().getDeptName()) );
		    		}else {
		    			deptSuggestions.append("???"+String.valueOf(deptSuggestionList.get(i).getDept().getDeptName()));
		    		}
	    		}
	    	}
			model.addAttribute("error", deptSuggestions.toString()+"??????????????????????????????????????????????????????");
			return "common/proce";
	    }
	    tdao.delete(deanroundslist);
		
	    return "redirect:/deanroundsmanage";

	}
	
	
	/**
	 * ??????????????????
	 */
	@RequestMapping("adddeptprobelm")
	public String adddeptproblem(HttpServletRequest req, Model model,@SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "50") int size) {
		// ??????????????? id
		String deanroundsId = req.getParameter("id");
		// ???????????????
		Iterable<Dept> deptlist = ddao.findznks();
		//???????????????????????????
		//Iterable<DeptSuggestion> deptSuggestion = dsdao.findBydeanId(0L);
//		// ???????????????id?????????
//		User tu = udao.getOne(userId);
		//Page<Deanroundslist> deanroundslist=tservice.index(page, size, null, tu);
		//List<Map<String, Object>> list=tservice.index2(deanroundslist, tu);
		//model.addAttribute("deanroundslist", list);
		model.addAttribute("deptlist", deptlist);
		// ????????????
		Pageable pa1=PageRequest.of(page, size);
		Page<Dept> pagedept = ddao.findznks(pa1);
		model.addAttribute("page", pagedept);
		//model.addAttribute("deptSuggestion", deptSuggestion);
		
		model.addAttribute("url", "names");
		model.addAttribute("qufen", "??????????????????");
		model.addAttribute("deanroundsId",deanroundsId);
		

		
		return "deanrounds/addproblems";
	}
	
	/**
	 * ????????????????????????
	 * @throws Exception 
	 */
	@RequestMapping(value="addproblemczwtsubmit",method=RequestMethod.POST)
	public String addproblemczwtsubmit(@SessionAttribute("userId") Long userId, HttpServletRequest request,Model model) throws Exception {
		


		
		//????????????ID
		String deanId=(String) request.getParameter("deanroundsDetailId");
		//??????????????????
		String czwt=(String) request.getParameter("czwt");
		String zbks=(String) request.getParameter("zbks");
		String xbks=(String) request.getParameter("xbks");
		//????????????ID????????????ID???null
		String id=String.valueOf(request.getParameter("deanroundsId"));
		DeanroundsDetailList deanroundsDetailList ;
		Deanroundslist deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(id) );
		
		//???????????????
		User sqr = udao.getOne(userId);
		
		//??????????????????ID
		String deanroundsId=String.valueOf(request.getParameter("deanroundsId"));
		
        if(StringUtil.stringIsNotNull(deanId)) {
            deanroundsDetailList = detaildao.findDeansBydeanroundsDetailId(Long.parseLong(deanId) );
    		deanroundsDetailList.setCzwt(czwt);
    		

    		deanroundsDetailList.setZbks(zbks);
    		deanroundsDetailList.setXbks(xbks);
    		deanroundsDetailList.setXh(deanroundsDetailList.getXh());
    		deanroundsDetailList.setDeanId(deanroundslist);
    		detaildao.save(deanroundsDetailList);

	   		 return "redirect:/editproblem?id="+deanroundsDetailList.getDeanroundsDetailId()+"&deanroundsId="+deanroundsId+"&tab=0";
        }else {
    		//??????????????????????????????
    		User tu = udao.getOne(userId);
    		String xzry=deanroundslist.getCffzr();
    		String[] xzrys = xzry.split(";");
    		boolean isfzr =false;
    		for(int i = 0 ;i<xzrys.length;i++) {
    			if(xzrys[i].trim().length()>0) {
    				if(xzrys[i].split("/")[0].equals(tu.getUserName())) {
    					isfzr =true;
    				}
    			}
    		}
    		
    		
    		
    		if(!isfzr&&!tu.getDept().getDeptName().equals("?????????")) {
    			model.addAttribute("error", "??????????????????????????????????????????????????????????????????");
    			return "common/proce";
    		}
    		//???????????????????????????
    		List<DeanroundsDetailList> deanroundsDetailLists = detaildao.findDeansBydeanId(Long.parseLong(id));
    		int problemCount=deanroundsDetailLists.size();
    		
        	deanroundsDetailList = new DeanroundsDetailList();
    		deanroundsDetailList.setCzwt(czwt);
//    		deanroundsDetailList.setFzgh(fzgh);
//    		deanroundsDetailList.setKsgk(ksgk);
    		deanroundsDetailList.setZbks(zbks);
    		deanroundsDetailList.setXbks(xbks);
    		deanroundsDetailList.setXh(problemCount+1);
    		deanroundsDetailList.setDeanId(deanroundslist);
    		detaildao.save(deanroundsDetailList);

        }

		
		return "redirect:/editdeanrounds?id="+deanroundsId;
	}
	
	/**
	 * ????????????????????????
	 * @throws Exception 
	 */
	@RequestMapping(value="addproblemkshysubmit",method=RequestMethod.POST)
	public String addproblemkshysubmit(@SessionAttribute("userId") Long userId, HttpServletRequest request,Model model) throws Exception {
		
		String znksjywjurl=String.valueOf(request.getParameter("kshywjurl"));
		String znksjywj_url="";
		String znksjywj_file="";	
		
		if(!znksjywjurl.contentEquals("")) {
			JSONObject jsonObject =JSONObject.parseObject(AESUtil.decrypt(znksjywjurl, "www.bjchweb.prod")) ;
			 znksjywj_url=jsonObject.getString("url").toString();
			 znksjywj_file=jsonObject.getString("file").toString();
		}
		
		
		
		//????????????ID
		String deanId=(String) request.getParameter("deanroundsDetailId");
		//????????????ID????????????ID???null
		String id=String.valueOf(request.getParameter("deanroundsId"));
		
		//??????????????????ID
		String deanroundsId=String.valueOf(request.getParameter("deanroundsId"));
		
		DeanroundsDetailList deanroundsDetailList ;
		//???????????????
		User sqr = udao.getOne(userId);
        if(StringUtil.stringIsNotNull(deanId)) {
            deanroundsDetailList = detaildao.findDeansBydeanroundsDetailId(Long.parseLong(deanId) );
		//??????????????????????????????
		String znks=StringUtil.stringToFormat((String) request.getParameter("znks"));
		String kshy= StringUtil.stringToFormat((String) request.getParameter("kshy"));
		String zgsj=StringUtil.stringToFormat((String) request.getParameter("zgsj"));
		zgsj=zgsj.equals("")?"0":zgsj;
		String zgqk=StringUtil.stringToFormat((String) request.getParameter("zgqk"));
		
		String bz=StringUtil.stringToFormat((String) request.getParameter("bz"));
		String wcqk=StringUtil.stringToFormat((String) request.getParameter("wcqk"));
		wcqk=wcqk.equals("")?"0":wcqk;

		String lsqk=StringUtil.stringToFormat((String) request.getParameter("lsqk"));
		lsqk=lsqk.equals("")?"0":lsqk;
		
		Dept dept=ddao.findbydeptName(znks);
		DeptResponds deptResponds;
		 if(StringUtil.stringIsNotNull(deanId)) {
				
			 List<DeptResponds> deptRespondsList = drdao.findDeptResByDeanIds(Long.parseLong(deanId),dept.getDeptId());
				if(deptRespondsList.size()>0) {
					
					deptResponds=deptRespondsList.get(0);
					//???????????????????????????????????????
					if(!deptResponds.getKshy().equalsIgnoreCase(kshy)
					  ||!deptResponds.getZgsj().equalsIgnoreCase(zgsj)
					  ||!deptResponds.getZgqk().equalsIgnoreCase(zgqk)
					  ||!deptResponds.getWcqk().equalsIgnoreCase(wcqk)
					  ||!deptResponds.getLsqk().equalsIgnoreCase(lsqk)
					  ||!deptResponds.getBz().equalsIgnoreCase(bz)
					  ||(!znksjywj_file.equals(""))
					  ||(!znksjywj_url.equals(""))
					  
							) {
						//????????????????????????????????????????????????
						if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
							deptResponds.setKshy(kshy);
							deptResponds.setZgsj(zgsj);
							deptResponds.setZgqk(zgqk);
							deptResponds.setWcqk(wcqk);
							deptResponds.setLsqk(lsqk);
							deptResponds.setBz(bz);
							if((!znksjywj_file.equals(""))||(!znksjywj_url.equals(""))) {
								deptResponds.setPdfname(znksjywj_file);
								deptResponds.setPdfurl(znksjywj_url);
							}
							
							drdao.save(deptResponds);
						}else {
		    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
		    				return "common/proce";
						}
					}
					
				}else {
					
					//????????????????????????????????????????????????
					if(sqr.getDept().getDeptId().equals(dept.getDeptId())||checkJuri(userId,model)) {
						deptResponds = new DeptResponds();
						deptResponds.setDept(dept);
						deptResponds.setKshy(kshy);
						deptResponds.setZgsj(zgsj);
						deptResponds.setZgqk(zgqk);
						deptResponds.setWcqk(wcqk);
						deptResponds.setLsqk(lsqk);
						deptResponds.setBz(bz);
						if((!znksjywj_file.equals(""))||(!znksjywj_url.equals(""))) {
							deptResponds.setPdfname(znksjywj_file);
							deptResponds.setPdfurl(znksjywj_url);
						}
						deptResponds.setDeanDetailId(deanroundsDetailList);
						
						drdao.save(deptResponds);
					}else {
	    				model.addAttribute("error", "?????????"+dept.getDeptName()+"?????????????????????????????????????????????");
	    				return "common/proce";
					}

				}
		 }
		 detaildao.save(deanroundsDetailList);
		 
		 
		 return "redirect:/editproblem?id="+deanroundsDetailList.getDeanroundsDetailId()+"&deanroundsId="+deanroundsId+"&tab=1";
        }

		

		return "redirect:/editdeanrounds?id="+deanroundsId;

         
	}
	
	
	/**
	 * ??????????????????                                                                                                                                                                                                                           
 */
	@RequestMapping("editproblem")
	public String editproblem(HttpServletRequest req, @SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,Model model
			) {
		//????????????ID
		String deanroundsId = req.getParameter("deanroundsId");
		// ????????????????????????id
		String deanroundsDetailId = req.getParameter("id");
		//Pageable pa=PageRequest.of(page, size);
		//ModelAndView mav = new ModelAndView("deanrounds/adddeanrounds");

		String tab = req.getParameter("tab");
		if(StringUtil.stringToFormat(tab).equals("1")) {
			model.addAttribute("tab", "1");
		}else
		{
			model.addAttribute("tab", "0");
		}

		String backtab = req.getParameter("backtab");
		if(StringUtil.stringToFormat(backtab).equals("")) {
			model.addAttribute("backtab", "0");
		}else
		{
			model.addAttribute("backtab", backtab);
		}
		
		
        if(deanroundsDetailId!="") {
        	
        	DeanroundsDetailList deanroundsDetailList = detaildao.findDeansBydeanroundsDetailId(Long.parseLong(deanroundsDetailId) );
        	
        	
    		List<DeanroundsDetailList> deanroundsDetailLists = detaildao.findDeansBydeanId(Long.parseLong(deanroundsId));
    		int problemCount=deanroundsDetailLists.size();
    		
        	if(deanroundsDetailList!=null) {
        		
        		// ???????????????
        		Iterable<Dept> deptlist = ddao.findznks();
        		//???????????????????????????
        		List<DeptResponds> deptDeptRespondsList = drdao.findDeptResByDeanIds(Long.parseLong(deanroundsDetailId) );
        		
        		
        		model.addAttribute("deanroundsDetailList", deanroundsDetailList);
        		model.addAttribute("deptDeptRespondsList", deptDeptRespondsList);
        		//?????????????????????????????????????????????
//        		for(int i=0;i<deptDeptRespondsList.size();i++) {
//        			deptDeptRespondsList.get(i).setKshystr((new String(deptDeptRespondsList.get(i).getKshy())).replaceAll("\r|\n", ""));
//        		}
        		model.addAttribute("deanroundsId", deanroundsId);
        		model.addAttribute("deanroundsDetailId", deanroundsDetailId);
        		model.addAttribute("problemCount", problemCount);
        		
        		
        		model.addAttribute("deptlist", deptlist);
        		// ????????????
        		Pageable pa1=PageRequest.of(page, size);
        		Page<Dept> pagedept = ddao.findznks(pa1);
        		model.addAttribute("page", pagedept);
        		//model.addAttribute("deptSuggestion", deptSuggestion);
        		
        		model.addAttribute("url", "names");
        		model.addAttribute("qufen", "????????????");
        		
        		//??????????????????
        		Deanroundslist deanroundslist = tdao.findDeansBydeanroundsId(Long.parseLong(deanroundsId) );
        		tservice.setJurisdiction( model, userId,deanroundslist.getCffzr());
        		
        	}else {
    			model.addAttribute("error", "?????????????????????????????????????????????");
    			return "common/proce";
        	}
        }else {
    			model.addAttribute("error", "?????????????????????????????????????????????");
    			return "common/proce";
        }
		
		

		return "deanrounds/editproblems";
	}
	
	
	
	
	
	
	
	
	
	/**
	 * ??????????????????
	 * @param req
	 * @param userId
	 * @return
	 */
	@RequestMapping("deleteproblems")
	public String deleteproblems(HttpServletRequest req, @SessionAttribute("userId") Long userId,Model model) {
		//????????????ID
		String deanroundsId = req.getParameter("deanroundsId");
		// ?????????????????????id
		String id = req.getParameter("id");
		
		List<DeptResponds> deptRespondsList = drdao.findDeptResByDeanIds(Long.parseLong(id));
		if(deptRespondsList.size()>0) {
			drdao.deleteAll(deptRespondsList);
		}
		// ????????????
		DeanroundsDetailList deansdetaillist = detaildao.findDeansBydeanroundsDetailId(Long.parseLong(id));
		detaildao.delete(deansdetaillist);
	    return "redirect:/editdeanrounds?id="+deanroundsId;
	}
	
	
	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("deansdetail")
	public String deansdetail(Model model,
			@SessionAttribute("userId") Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		// ???????????????id?????????
//		User tu = udao.getOne(userId);
//		
//        
//		List<DeanroundsDetailList> deanroundslist = tdao.findDeansdetailBydeanroundsId(pa);
//		
//		List<Map<String, Object>> list=tservice.index2(deanroundslist, tu);
//	
//		model.addAttribute("deanroundslist", list);
//		model.addAttribute("page", deanroundslist);
//		model.addAttribute("url", "paixu");
		return "deanrounds/deanroundsmanage";
	}

	
   private boolean checkJuri(Long userId,Model model) {
		//???????????????
		User tu = udao.getOne(userId);
		if((!tu.getDept().getDeptName().equals("?????????"))) {
			if((!tu.getUserName().equals("185006") )) {
				model.addAttribute("error", "???????????????????????????????????????????????????");
				return false;
			}
		}
		return true;
   }


}

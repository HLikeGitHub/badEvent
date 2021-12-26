package com.yjy.web.mms.services.process;

import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.mms.model.dao.notedao.AttachmentDao;
import com.yjy.web.mms.model.dao.roledao.RoleDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.SysMeetroomDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.dao.processdao.PassportApplyDao; //护照申请
import com.yjy.web.mms.model.entity.note.Attachment;
import com.yjy.web.mms.model.entity.process.*;
import com.yjy.web.mms.model.entity.system.SysMeetroom;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.Position;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.mail.MailServices;
import com.yjy.web.comm.utils.SendMessageUntil;
import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.mms.model.dao.processdao.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@Service
@Transactional
public class ProcessService {
	@Autowired
	private UserDao udao;
	@Autowired
	private DeptDao ddao;
	@Autowired
	private RoleDao rdao;
	@Autowired
	private PositionDao pdao;
	@Autowired
	private SubjectDao sudao;
	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private ReviewedDao redao;
	@Autowired
	private AttachmentDao AttDao;
	@Autowired
	private BursementDao budao;
	@Autowired
	private MailServices mservice;
	@Autowired
	private ProcessListDao prodao;
	@Autowired
	private MealApplyDao mealApplyDao;
	@Autowired
	private SendMessageLogDao sendMessageLogDao;
	@Autowired
	private SysMeetroomDao sysMeetroomDao;

	@Autowired
    private PassportApplyDao passportApplyDao;
	@Autowired
	private DetailedlistapplyDao detailedlistapplyDao;
	@Value("${attachment.httppath}")
	private String httppath;
	 /**
     * 汉语中数字大写
     */
  private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖" };
   /**
    * 汉语中货币单位大写，这样的设计类似于占位符
     */
   private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
           "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
           "佰", "仟" };
   /**
     * 特殊字符：整
    */
  private static final String CN_FULL = "整";
 /**
     * 特殊字符：负
    */
  private static final String CN_NEGATIVE = "负";
  /**
     * 金额的精度，默认值为2
    */
  private static final int MONEY_PRECISION = 2;
  /**
     * 特殊字符：零元整
    */
  private static final String CN_ZEOR_FULL = "零元" + CN_FULL;
  

	/**
	 * 写文件 方法
	 * 
	 * @param response
	 * @param file
	 * @throws IOException 
	 */
	public void writefile(HttpServletResponse response, File file) {
		ServletOutputStream sos = null;
		FileInputStream aa = null;
		try {
			aa = new FileInputStream(file);
			sos = response.getOutputStream();
			// 读取文件问字节码
			byte[] data = new byte[(int) file.length()];
			IOUtils.readFully(aa, data);
			// 将文件流输出到浏览器
			IOUtils.write(data, sos);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				sos.close();
				aa.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	
	}
  /**
   * 用户封装
//   * @param user
   * @param page
   * @param size
//   * @param val
   * @return
   */
	public void user(int page,int size,Model model){
		Pageable pa=PageRequest.of(page, size);
		//查看用户并分页
		Page<User> pageuser=udao.findAll(pa);
		List<User> userlist=pageuser.getContent();
		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();
		model.addAttribute("page", pageuser);
		model.addAttribute("emplist", userlist);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "names");
	}
	
	
	public Page<AubUser> index(User user, int page, int size, String val, Model model){
		Pageable pa=PageRequest.of(page, size);
		Page<AubUser> pagelist=null;
		Page<AubUser> pagelist2=null;
		List<Order> orders = new ArrayList<>();
		User  u=udao.findByUserName(val);//找用户
		SystemStatusList status=sdao.findByStatusModelAndStatusName("aoa_process_list", val);
		
		if(TextUtil.isNullOrEmpty(val)){
			orders.add(new Order(Direction.DESC, "applyTime"));
			Sort sort = Sort.by(orders);
			pa=PageRequest.of(page, size,sort);
			pagelist=redao.findByUserIdOrderByStatusId(user,false, pa);
		}else if(!Objects.isNull(u)){
			pagelist=redao.findprocesslist(user,u,false,pa);
			model.addAttribute("sort", "&val="+val);
		}else if(!Objects.isNull(status)){
			pagelist=redao.findbystatusprocesslist(user,status.getStatusId(),false,pa);
			model.addAttribute("sort", "&val="+val);
		}else{
			pagelist2=redao.findbytypenameprocesslist(user, val,false, pa);
			if(!pagelist2.hasContent()){
				pagelist2=redao.findbyprocessnameprocesslist(user, val,false, pa);
			}
			model.addAttribute("sort", "&val="+val);
			return pagelist2;
		}
		
		if(StringUtil.stringIsNotNull(user.getRyzb())&&(user.getRyzb().equals("数据组")||user.getRyzb().equals("监察组"))) {
			orders.add(new Order(Direction.DESC, "applyTime"));
			Sort sort = Sort.by(orders);
			pa=PageRequest.of(page, size,sort);
			pagelist=redao.findAllOrderByStatusId(user,false, pa);
		}

		if(StringUtil.stringIsNotNull(user.getRyzb()) && (user.getRyzb().equals("人事科审核确认组"))){
			orders.add(new Order(Direction.DESC,"applyTime"));
			Sort sort =Sort.by(orders);
			pa=PageRequest.of(page,size,sort);
			pagelist=redao.findAllOrderByStatusIdPassportApply(user,false,pa);
		}

		return pagelist;
	}



	public List<Map<String,Object>> index2(Page<AubUser> page,User user){
		List<Map<String, Object>> list = new ArrayList<>();
		List<AubUser> prolist=page.getContent();
		for (int i = 0; i < prolist.size(); i++) {
			String harryname=tydao.findname(prolist.get(i).getDeeply());
			SystemStatusList status=sdao.findById(prolist.get(i).getStatusId()).orElse(null);
			Map<String, Object> result=new HashMap<>();
			ProcessList processList= prodao.findbyuseridandtitle(prolist.get(i).getProcessId());
			result.put("typename", prolist.get(i).getTypeNmae());
			result.put("processName", prolist.get(i).getProcessName()==null?"":prolist.get(i).getProcessName());
			result.put("pushuser",udao.findByUserName( prolist.get(i).getUserName()).getRealName().toString());
			result.put("applytime",  prolist.get(i).getApplyTime());
			result.put("harry", harryname);
			result.put("statusname", status.getStatusName()==null?"":status.getStatusName());
			result.put("statuscolor", status.getStatusColor());
			result.put("proid", prolist.get(i).getProcessId());
			result.put("startTime", processList.getStartTime());
			result.put("endTime",processList.getEndTime());
			result.put("applyTime", processList.getApplyTime());
			result.put("jobNumber",udao.findByUserName(prolist.get(i).getUserName()).getUserName());
			result.put("processDescribe",processList.getProcessDescribe());
			if(prolist.get(i).getTypeNmae().equals("出入境申请")){
				result.put("stateCertificate", passportApplyDao.findByProId(processList).getStateCertificate()==null?"":passportApplyDao.findByProId(processList).getStateCertificate());

			}
			System.out.println("result:"+result.toString());
			list.add(result);

		}
		return list;
	}

	/**
	 * 审核人封装
	 */
	public List<Map<String,Object>> index4(ProcessList process){
		List<Map<String,Object>> relist=new ArrayList<>();
		List<Reviewed> revie=redao.findByReviewedTimeNotNullAndProId(process);
		for (int i = 0; i <revie.size(); i++) {
			Map<String, Object> result=new HashMap<>();
			User u=udao.getOne(revie.get(i).getUserId().getUserId());
			Position po=pdao.findById(u.getPosition().getId()).orElse(null);
			SystemStatusList status=sdao.findById(revie.get(i).getStatusId()).orElse(null);
			result.put("poname", po.getName());
			result.put("username", u.getUserName());
			result.put("realname", u.getRealName());
			result.put("retime",revie.get(i).getReviewedTime());
			result.put("restatus",status.getStatusName());
			result.put("statuscolor",status.getStatusColor());
			result.put("des", revie.get(i).getAdvice());
			result.put("img",u.getImgPath());
			result.put("positionid",u.getPosition().getId());
			relist.add(result);
		}
		return relist;
	}
	/**
	 * process数据封装
	 */
	
	public Map<String,Object> index3(String name,User user,String typename,ProcessList process){
		System.out.println(name);
		Map<String,Object> result=new HashMap<>();
		String harryname=tydao.findname(process.getDeeply());
		result.put("proId", process.getProcessId());
		result.put("harryname", harryname);
		result.put("processName", process.getProcessName());
		result.put("processDescribe",process.getProcessDescribe());
		if(("审核").equals(name)){
			result.put("username", process.getUserId().getUserName());//提单人员
			result.put("realname", process.getUserId().getRealName());//提单人员
			result.put("deptname", ddao.findname(process.getUserId().getDept().getDeptId()));//部门
			result.put("position",process.getUserId().getPosition().getName());
			result.put("idCard",process.getUserId().getIdCard());
		}else if(("申请").equals(name)){
			result.put("username", user.getUserName());
			result.put("realname", user.getRealName());//提单人员
			result.put("deptname", ddao.findname(process.getUserId().getDept().getDeptId()));
			result.put("position",user.getPosition().getName());
			result.put("idCard",user.getIdCard());
		}
		result.put("applytime", process.getApplyTime());
		List<Attachment> attachmentList =AttDao.findAttachment(process.getProcessId());
		System.out.println("attachmentList:"+attachmentList.toString());
		for(int i=0;i<attachmentList.size();i++) {
			
			
			attachmentList.get(i).setAttachmentAllpath(httppath+String.valueOf(attachmentList.get(i).getAttachmentPath()));
			if (attachmentList.get(i).getAttachmentSize()<1024) {
				attachmentList.get(i).setAttachmentType(attachmentList.get(i).getAttachmentSize()+"B");
			}else if (attachmentList.get(i).getAttachmentSize()<1024*1024) {
				attachmentList.get(i).setAttachmentType(attachmentList.get(i).getAttachmentSize()/1024+"KB");
			}else {
				attachmentList.get(i).setAttachmentType(attachmentList.get(i).getAttachmentSize()/1024/1024+"MB");
			}
		}
//		if(!Objects.isNull(process.getProFileid())){
//			result.put("file", process.getProFileid());
//		}else{
//			result.put("file", "file");
//		}
		result.put("name", name);
		result.put("typename", process.getTypeNmae());
		result.put("startime", process.getStartTime());
		result.put("endtime", process.getEndTime());
		result.put("tianshu", process.getProcseeDays());
		result.put("statusid", process.getStatusId());
		result.put("attachmentList", attachmentList);
//		if( process.getProFileid()!=null){
//		   result.put("filepath", process.getProFileid().getAttachmentPath());
//			if(process.getProFileid().getAttachmentType().startsWith("image")){
//				result.put("filetype", "img");
//			}else{
//				result.put("filetype", "appli");
//			}
//		}
		return result;
	}
	/**
	 * 公用
	 */
	public void  index6(Model model,Long id,int page,int size){
		User lu=udao.getOne(id);//申请人
		Pageable pa=PageRequest.of(page, size);
		List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");
		//查看用户并分页
		Page<User> pageuser=udao.findAll(pa);
		List<User> userlist=pageuser.getContent();
		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();
		
//		Pageable pa1=PageRequest.of(page, size);
//		Page<User> pageuser1=udao.findByUserFatherId(lu.getFatherId(),pa1);
//		List<User> userlist1=pageuser1.getContent();
//		model.addAttribute("emplist1", userlist1);
		
		
		model.addAttribute("page", pageuser);
		model.addAttribute("emplist", userlist);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "names");
		model.addAttribute("username", lu.getUserName());
		model.addAttribute("harrylist", harrylist);
	}

/**
 * @author: winper001
 * @create: 2020-01-15
 * @description: 出入境申请
*/
	public void indexPassportApply(Model model,Long id,int page,int size)
    {
        // 查询部门表
        Iterable<Dept> deptlist = ddao.findAll();
        // 查职位表
        Iterable<Position> poslist = pdao.findAll();
        //加载紧急程度
        List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");


        //winper001:获取证件的类型
		//List<SystemTypeList> certificateTypeList=tydao.findByTypeModel("aoa_type_certificate");

        //获取申请人信息
        User applyPerson=udao.getOne(id);

        Pageable pa1=PageRequest.of(page, size);

        List<User> uselist=new ArrayList<>();

        //普通科员
		Page<User> pageuser=udao.findByUserDeptFather(applyPerson.getFatherId(),applyPerson.getDept().getDeptId(),pa1);
		List<User> userlist=pageuser.getContent();

		// 除人力资源科科长之外的【所有科长/主任】,他们下一步到【人事科科长】
        if(applyPerson.getPosition().getId()==6L && applyPerson.getDept().getDeptId()!=103L){
        	List<User> userlist_temp=new ArrayList<>();
        	userlist_temp=udao.findByHrChief(pa1).getContent();
        	userlist=userlist_temp;
		}
		//【人力资源科科长】,他们下一步到【分管院长】
        if(applyPerson.getPosition().getId()==6L && applyPerson.getDept().getDeptId()==103L){
			List<User> userlist_temp=new ArrayList<>();
			userlist_temp=udao.findByVicePresident(pa1).getContent();
			userlist=userlist_temp;
		}
        //院长书记
        if(applyPerson.getUserName().equals("8988")){
            List<User> userlist_temp=new ArrayList<>();
            userlist_temp=udao.findBigBoss(pa1).getContent();
            userlist=userlist_temp;
        }
		User user = userlist.size() == 0 ? null : userlist.get(0);
		String defaShen = user == null ? "" : (user.getUserName()+"/"+user.getRealName());//默认审核人

        model.addAttribute("page", pageuser);
        model.addAttribute("emplist", userlist);
        model.addAttribute("poslist", poslist);
        model.addAttribute("deptlist", deptlist);
        model.addAttribute("url", "namesfather");
        model.addAttribute("applyPerson",applyPerson);
        model.addAttribute("harrylist", harrylist);
        //model.addAttribute("certificateTypeList",certificateTypeList);
		model.addAttribute("defaShen", defaShen);//默认审核人

    }


	/**
	 * 会议室申请，填单前数据加载
	 */
	public void  indexHYSSQ(Model model,Long id,int page,int size){
		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();
		//加载紧急程度
		List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");
		//获取申请人信息
		User lu=udao.getOne(id);
		//分页查询全员所有人员
		Pageable pa1=PageRequest.of(page, size);
		Page<User> pageuser1=udao.findAll(pa1);
		//返回所有人员列表
		List<User> userlist1=pageuser1.getContent();
		//获取会议室列表
		List<SysMeetroom> sysMeetrooms = sysMeetroomDao.findbyState(33L);

		model.addAttribute("page", pageuser1);
		model.addAttribute("emplist", userlist1);
		model.addAttribute("poslist", poslist);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("url", "namesfather");
		model.addAttribute("username", lu.getUserName());
		model.addAttribute("harrylist", harrylist);
		model.addAttribute("sysmeetrooms", sysMeetrooms);
	}
	
	
	/**
	 * 领导接待
	 */
	public void  indexLDJD(Model model,Long id,int page,int size){
		List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");
		//查看用户并分页
//		Page<User> pageuser=udao.findAll(pa);
//		List<User> userlist=pageuser.getContent();
		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();
		
		User lu=udao.getOne(id);//申请人
		Pageable pa1=PageRequest.of(page, size);
		Page<User> pageuser1=udao.findByUserFatherIds(2L,3L,pa1);
		List<User> userlist1=pageuser1.getContent();
		
		model.addAttribute("page", pageuser1);
		model.addAttribute("emplist", userlist1);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "namesfather");
		model.addAttribute("username", lu.getRealName());
		model.addAttribute("harrylist", harrylist);
	}

	/**
	 * 程序更换申请
	 */
	public void  indexCXGH(Model model,Long id,int page,int size){
		
		List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");

		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();
		
		User lu=udao.getOne(id);//申请人
		Pageable pa1=PageRequest.of(page, size);
		Page<User> pageuser1=udao.findByUserDeptFather(lu.getFatherId(), lu.getDept().getDeptId(), pa1);
		List<User> userlist1=pageuser1.getContent();
		
		List<SystemTypeList> riskList=tydao.findByTypeModel("system_risk");
		
		
		model.addAttribute("page", pageuser1);
		model.addAttribute("emplist", userlist1);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "namesfather");
		model.addAttribute("username", lu.getRealName());
		model.addAttribute("harrylist", harrylist);
		model.addAttribute("riskList", riskList);
		
	}
	
	/**
	 * 数据查询
	 */
	public void  indexSJCX(Model model,Long id,int page,int size){
		List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");
		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();

		List<User> lus= udao.findByUId(id);
		User lu=lus.get(0);
		System.out.println(lu.getDept().getDeptId());
		Pageable pa1=PageRequest.of(page, size);
		Page<User> pageuser1=udao.findByUserDeptFather(lu.getFatherId(),lu.getDept().getDeptId(),pa1);
		List<User> userlist1=pageuser1.getContent();
		model.addAttribute("emplist1", userlist1);
		
		if((lu.getFatherId().equals(5L))&&(StringUtil.stringIsNotNull(lu.getDept().getLcks()))&&(lu.getDept().getLcks().equals("是"))) {
			List<User> userlist2=new ArrayList<User>();
			userlist2=udao.findByLcks(pa1).getContent();
			userlist1=userlist2;
		}
		
		if((lu.getFatherId().equals(5L))&&(StringUtil.stringIsNotNull(lu.getDept().getLcks())&&(lu.getDept().getLcks().equals("职能科室"))   )) {
			List<User> userlist2=new ArrayList<User>();
			userlist2=udao.findByWLXXK(pa1).getContent();
			userlist1=userlist2;
		}
		
		
		
		
//		if(lu.getFatherId().equals(3L)) {
//			List<User> userlist2=new ArrayList<User>();
//			List<Dept> dept2=ddao.findByDeptId(lu.getDept().getDeptId());
//			if(dept2.size()>0) {
//				User user2=udao.findByUserName(dept2.get(0).getZgyzuserName());
//				userlist2.add(user2);
//				if(userlist2.size()>0) {
//					userlist1=userlist2;
//				}
//			}
//		}
		
//		if((lu.getPosition().getId().equals(7L))&&(StringUtil.stringIsNotNull(lu.getDept().getLcks())) &&(lu.getDept().getLcks().equals("是"))) {
//			List<User> userlist2=new ArrayList<User>();
//			userlist2=udao.findByLcks(pa1).getContent();
//			userlist1=userlist2;
//		}
		
		model.addAttribute("page", pageuser1);
		model.addAttribute("emplist", userlist1);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "namesfather");
		model.addAttribute("username", lu.getUserName());
		model.addAttribute("harrylist", harrylist);
		//modify by rwx @2019120616:00 index out of bounds exception
		User user = userlist1.size() == 0 ? null : userlist1.get(0);
		String defaShen = user == null ? "" : (user.getUserName()+"/"+user.getRealName());
		model.addAttribute("defaShen", defaShen);//默认审核人
		
	}
	/**
	 * 公务接待
	 */
	public void  indexGWJD(Model model,Long id,int page,int size,Long mealapplyid){
//		User lu=udao.findOne(id);//申请人
//		Pageable pa=PageRequest.of(page, size);
		List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");
		
		
		//查看用户并分页
//		Page<User> pageuser=udao.findAll(pa);
//		List<User> userlist=pageuser.getContent();
		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();
		
		User lu=udao.getOne(id);//申请人
		Pageable pa1=PageRequest.of(page, size);
		Page<User> pageuser1=udao.findByUserDeptFather(lu.getFatherId(),lu.getDept().getDeptId(),pa1);
		List<User> userlist1=pageuser1.getContent();
		model.addAttribute("emplist1", userlist1);
		
		if((lu.getFatherId().equals(5L))&&(StringUtil.stringIsNotNull(lu.getDept().getLcks()))&&(lu.getDept().getLcks().equals("是"))) {
			List<User> userlist2=new ArrayList<User>();
			userlist2=udao.findByLcks(pa1).getContent();
			userlist1=userlist2;
		}
		if(mealapplyid!=0) {
			Mealapply mealapply= mealApplyDao.findBymealapplyId(mealapplyid);
			if(lu.getFatherId().equals(3L)) {
				List<User> userlist2=new ArrayList<User>();
				List<Dept> dept2=ddao.findByDeptId(mealapply.getProId().getUserId().getDept().getDeptId());
				if(dept2.size()>0) {
					User user2=udao.findByUserName(dept2.get(0).getZgyzuserName());
					userlist2.add(user2);
					if(userlist2.size()>0) {
						userlist1=userlist2;
					}
					
				}

			}
			
			
		}
		//办公室科员申请，申请单要到办公室办事员
		if(lu.getFatherId().equals(6L)&&lu.getDept().getDeptId().equals(101L)) {
			Page<User> userpa=udao.findByUserFatherId(5L,pa1);
			userlist1=userpa.getContent();
		}
		
		
		
		
		

		
//		if((lu.getPosition().getId().equals(7L))&&(StringUtil.stringIsNotNull(lu.getDept().getLcks())) &&(lu.getDept().getLcks().equals("是"))) {
//			List<User> userlist2=new ArrayList<User>();
//			userlist2=udao.findByLcks(pa1).getContent();
//			userlist1=userlist2;
//
//		}
		
		model.addAttribute("page", pageuser1);
		model.addAttribute("emplist", userlist1);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "namesfather");
		model.addAttribute("username", lu.getUserName());
		model.addAttribute("harrylist", harrylist);
		
		User user = userlist1.size() == 0 ? null : userlist1.get(0);
		String defaShen = user == null ? "" : (user.getUserName()+"/"+user.getRealName());
		model.addAttribute("defaShen", defaShen);
	}
	/**
	 * 接待清单
	 */
	public void  indexJDQD(Model model,Long id,int page,int size, long mealapplyid){
		List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");
		// 查询部门表
		Iterable<Dept> deptlist = ddao.findAll();
		// 查职位表
		Iterable<Position> poslist = pdao.findAll();
		
		

		Mealapply mealapply= mealApplyDao.findBymealapplyId(mealapplyid);
		
		if(!StringUtil.objectIsNotNull(mealapply)) {
			ProcessList processlist=prodao.findbyuseridandtitle(mealapplyid);
			Detailedlistapply detailedlistapply=detailedlistapplyDao.findByProId(processlist);
			mealapply= mealApplyDao.findBymealapplyId(detailedlistapply.getMealapply().getMealapplyId());
		}
		
		User lu=udao.getOne(id);//申请人
		Pageable pa1=PageRequest.of(page, size);
		Long fatherId=lu.getFatherId();
		Long deptId=lu.getDept().getDeptId();
		if(lu.getPosition().getId()==7L||lu.getPosition().getId()==5L) {

			deptId=mealapply.getProId().getUserId().getDept().getDeptId();
			fatherId=6L;
			
		}else if(lu.getPosition().getId()==6L) {
			fatherId=4L;
			deptId=101L;
		}
		

		
		Page<User> pageuser1=udao.findByUserDeptFather( fatherId,deptId,pa1);//  lu.getFatherId() , lu.getDept().getDeptId()
		List<User> userlist1=pageuser1.getContent();
		User lcsqr=udao.getOne(mealapply.getProId().getUserId().getUserId());//申请人
		if((lcsqr.getDept().getDeptId()==101L)) {//判断是否为办公室申请 &&(userlist1.size()>0)&&(userlist1.get(0).getPosition().getId()!=2L)
			if(lu.getUserId()==mealapply.getProId().getUserId().getUserId()) {
				pageuser1=udao.findByUserDeptFather(4L,deptId,pa1);
				userlist1=pageuser1.getContent();
			}else {
				pageuser1=udao.findByUserId(mealapply.getProId().getUserId().getUserId(),pa1);
				userlist1=pageuser1.getContent();
			}

			
			
			
		}
		
		
		if(lu.getFatherId().equals(3L)) {
			List<User> userlist2=new ArrayList<User>();
			List<Dept> dept2=ddao.findByDeptId(mealapply.getProId().getUserId().getDept().getDeptId());
			if(dept2.size()>0) {
				User user2=udao.findByUserName(dept2.get(0).getZgyzuserName());
				userlist2.add(user2);
				if(userlist2.size()>0) {
					userlist1=userlist2;
				}
				
			}

		}
		
		
		
		model.addAttribute("page", pageuser1);
		model.addAttribute("emplist", userlist1);
		model.addAttribute("deptlist", deptlist);
		model.addAttribute("poslist", poslist);
		model.addAttribute("url", "namesfather");
		model.addAttribute("username", lu.getUserName());
		model.addAttribute("harrylist", harrylist);
		model.addAttribute("mealapply", mealapply);
		User user = userlist1.size() == 0 ? null : userlist1.get(0);
		String defaShen = user == null ? "" : (user.getUserName()+"/"+user.getRealName());
		model.addAttribute("defaShen", defaShen);
	}
	

	/**
	 * 存表
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public void index5(ProcessList pro,String val,User lu,MultipartFile[] filePath,String name) throws IllegalStateException, IOException{
		
		pro.setTypeNmae(val);
		pro.setApplyTime(new Date());
		pro.setUserId(lu);
		pro.setStatusId(23L);
		pro.setShenuser(name);
//		List<Attachment> attachments=new ArrayList<Attachment>();
//		
//		if(filePath.length>0) {
//			for(int i=0;i<filePath.length;i++) {
//				Attachment attaid=null;
//				if(!StringUtil.isEmpty(filePath[i].getOriginalFilename())){
//					attaid=mservice.upload(filePath[i], lu);
//					attaid.setModel("aoa_detailedlistapply");
//					attaid.setProcess(pro);
//					AttDao.save(attaid);
//					attachments.add(attaid);
//				}
//			}
////			pro.setProFileids(attachments);
//		}
		
	}
	public void saveFile(ProcessList pro,String val,User lu,MultipartFile[] filePath,String name) throws IllegalStateException, IOException{
		List<Attachment> attachments=new ArrayList<Attachment>();
		
		if(filePath.length>0) {
			for(int i=0;i<filePath.length;i++) {
				Attachment attaid=null;
				if(!TextUtil.isNullOrEmpty(filePath[i].getOriginalFilename())){
					attaid=mservice.upload(filePath[i], lu);
					attaid.setModel("aoa_detailedlistapply");
					attaid.setProcess(pro);
					AttDao.save(attaid);
					attachments.add(attaid);
				}
			}
//			pro.setProFileids(attachments);
		}
	}
	public void index8(ProcessList pro,String val,User lu,String name) {
		pro.setTypeNmae(val);//typename
		pro.setApplyTime(new Date());
		pro.setUserId(lu);
		pro.setStatusId(23L);
		pro.setShenuser(name);
	}
	/**
	 * 领导接待写入主表
	 * @param pro
	 * @param val
	 * @param lu
	 * @param name
	 */
	public void indexLDJD(ProcessList pro,String val,User lu,String name) {
		pro.setTypeNmae(val);
		pro.setApplyTime(new Date());
		pro.setUserId(lu);
		pro.setStatusId(25L);
		pro.setShenuser(name);
		
		
	}
	/**
	 * 
	 * @param phone
	 * @param content
	 * @param user
	 */
	public void SendMessage(String phone,String content,User user,ProcessList process) {
		String reStr= SendMessageUntil.getDXFSDX(phone,content+"");//【江门市中心医院】
		SendMessageLog sendMessageLog = new SendMessageLog();
		sendMessageLog.setDxnr(content+"");//【江门市中心医院】
		Date date=new java.sql.Date(new java.util.Date().getTime());
		sendMessageLog.setSendtime(date);
		sendMessageLog.setPhone(phone);
		sendMessageLog.setUserId(user);
		sendMessageLog.setProId(process);
		if(reStr.equals("1")) {
			System.out.println("短信发送成功！");
			sendMessageLog.setStatusId(30);
			sendMessageLogDao.save(sendMessageLog);
		}else {
			System.out.println("短信发送失败！");
			sendMessageLog.setStatusId(31);
			sendMessageLogDao.save(sendMessageLog);
		}
		
	}
	
	
	/**
	 * 存主表
	 */
	public void save(Long proid,User u,Reviewed reviewed,ProcessList pro,User u2){
		Reviewed re=redao.findByProIdAndUserId(proid,u);
		re.setAdvice(reviewed.getAdvice());
		re.setStatusId(reviewed.getStatusId());
		re.setReviewedTime(new Date());
		re.setStatusId(reviewed.getStatusId());
		redao.save(re);
		
		
		Reviewed re2=new Reviewed();
		re2.setProId(pro);
		if(re.getStatusId()==26L){//如果有一个审核人未通过【26L】,则下一级所有的审核人都无法看到该记录。
			re2.setUserId(null);
			//re2.setStatusId(26L);
		}else{
			re2.setUserId(u2);
			re2.setStatusId(23L);
		}
		redao.save(re2);
		
		pro.getShenuser();
		pro.setShenuser(pro.getShenuser()+";"+u2.getUserName());
		pro.setStatusId(24L);//改变主表的状态
		prodao.save(pro);
	}
	/**
	 * 存审核表
	 */
	public void index7(User reuser,ProcessList pro){
		Reviewed revie=new Reviewed();
		revie.setUserId(reuser);
		revie.setStatusId(23L);
		revie.setProId(pro);
		redao.save(revie);
	}
	
	
	/**
	 * 公务接待申请单保存
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public void savemealapply(ProcessList pro,String val,User lu,String name) throws IllegalStateException, IOException{
		
		pro.setTypeNmae(val);
		pro.setApplyTime(new Date());
		pro.setUserId(lu);
		pro.setStatusId(23L);
		pro.setShenuser(name);
	}
	
	
	/**
	      * 把输入的金额转换为汉语中人民币的大写
	       * 
	      * @param numberOfMoney
	      *            输入的金额
	      * @return 对应的汉语大写
	      */
	    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
	         StringBuffer sb = new StringBuffer();
	        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
	       // positive.
	        int signum = numberOfMoney.signum();
	        // 零元整的情况
	         if (signum == 0) {
	             return CN_ZEOR_FULL;
	       }
	        //这里会进行金额的四舍五入
	         long number = numberOfMoney.movePointRight(MONEY_PRECISION)
	                .setScale(0, 4).abs().longValue();
	        // 得到小数点后两位值
	         long scale = number % 100;
	        int numUnit = 0;
	        int numIndex = 0;
	        boolean getZero = false;
	        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
	        if (!(scale > 0)) {
	            numIndex = 2;
	             number = number / 100;
	             getZero = true;
	        }
	        if ((scale > 0) && (!(scale % 10 > 0))) {
	             numIndex = 1;
	            number = number / 10;
	             getZero = true;
	        }
	        int zeroSize = 0;
	        while (true) {
	             if (number <= 0) {
	                break;
	            }
	           // 每次获取到最后一个数
	           numUnit = (int) (number % 10);
	             if (numUnit > 0) {
	                 if ((numIndex == 9) && (zeroSize >= 3)) {
	                     sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
	                }
	               if ((numIndex == 13) && (zeroSize >= 3)) {
	                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
	              }
	                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
	                 sb.insert(0, CN_UPPER_NUMBER[numUnit]);
	                getZero = false;
	                zeroSize = 0;
	            } else {
	                ++zeroSize;
	                if (!(getZero)) {
	                   sb.insert(0, CN_UPPER_NUMBER[numUnit]);
	                }
	               if (numIndex == 2) {
	                   if (number > 0) {
	                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
	                    }
	                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
	                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
	                }
	               getZero = true;
	            }
	             // 让number每次都去掉最后一个数
	             number = number / 10;
	            ++numIndex;
	         }
	        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
	         if (signum == -1) {
	            sb.insert(0, CN_NEGATIVE);
	        }
	        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
	       if (!(scale > 0)) {
	           sb.append(CN_FULL);
	       }
	        return sb.toString();
	    }
	 
	    public static String numbertocn(Double money){
	    	BigDecimal numberOfMoney = new BigDecimal(money);
	        String s = number2CNMontrayUnit(numberOfMoney);
	        System.out.println("你输入的金额为：【"+ money +"】   #--# [" +s.toString()+"]");
	          return s.toString();
	    }	

	
	
}

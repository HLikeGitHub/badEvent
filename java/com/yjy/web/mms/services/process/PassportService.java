package com.yjy.web.mms.services.process;

import com.yjy.web.comm.utils.SendMessageUntil;
import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.mms.model.dao.notedao.AttachmentDao;
import com.yjy.web.mms.model.dao.processdao.PassportApplyDao;
import com.yjy.web.mms.model.dao.processdao.ReviewedDao;
import com.yjy.web.mms.model.dao.system.StatusDao;

import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.note.Attachment;

import com.yjy.web.mms.model.entity.process.*;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.Position;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.mail.MailServices;

import com.yjy.web.mms.model.dao.processdao.ProcessListDao;
import com.yjy.web.mms.model.dao.processdao.SendMessageLogDao;
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

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;


@Service
@Transactional
public class PassportService {
	@Autowired
	private UserDao udao;
	@Autowired
	private DeptDao ddao;

	@Autowired
	private PositionDao pdao;

	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private ReviewedDao redao;
	@Autowired
	private AttachmentDao AttDao;

	@Autowired
	private MailServices mservice;
	@Autowired
	private ProcessListDao prodao;
	@Autowired
	private SendMessageLogDao sendMessageLogDao;


	@Autowired
    private PassportApplyDao passportApplyDao;



	@Value("${attachment.httppath}")
	private String httppath;

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

	//审核者
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
			pagelist=redao.findbytypenameprocesslist_checker(user,"出入境申请",false, pa);//user是审核人，查出该审核人所有审核流程
		}else if(!Objects.isNull(u)){
			pagelist=redao.findprocesslist_checker(user,u,false,pa);//根据审核人和申请人查找
			model.addAttribute("sort", "&val="+val);
		}else if(!Objects.isNull(status)){
			pagelist=redao.findbystatusprocesslist_checker(user,status.getStatusId(),false,pa);//根据审核人和审核状态查找
			model.addAttribute("sort", "&val="+val);
		}else{
			pagelist2=redao.findbytypenameprocesslist_checker(user,val,false,pa);
			if(!pagelist2.hasContent()){
				pagelist2=redao.findbyprocessnameprocesslist_checker(user,val,false,pa);
			}
			model.addAttribute("sort", "&val="+val);
			return pagelist2;
		}
//		if(StringUtil.stringIsNotNull(user.getRyzb()) && (user.getRyzb().equals("人事科审核确认组"))){
//			orders.add(new Order(Direction.DESC,"applyTime"));
//			Sort sort =Sort.by(orders);
//			pa=PageRequest.of(page,size,sort);
//			pagelist=redao.findAllOrderByStatusIdPassportApply(user,false,pa);
//		}

		return pagelist;
	}


	public Page<AubUser> index_HrToSearch(User user, int page, int size, String val, Model model){
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
			pagelist=redao.findAllOrderByStatusIdPassportApply(user,false, pa);
		}else if(!Objects.isNull(u)){//根据申请人查找
			pagelist=redao.findprocesslist_admin(u,pa);
			model.addAttribute("sort", "&val="+val);
		}else if(!Objects.isNull(status)){//根据审核状态查找
			pagelist=redao.findbystatusprocesslist_admin(status.getStatusId(),pa);
			model.addAttribute("sort", "&val="+val);
		}else{
			pagelist2=redao.findbytypenameprocesslist_admin(val, pa);
			if(!pagelist2.hasContent()){
				pagelist2=redao.findbyprocessnameprocesslist_admin(val,pa);
			}
			model.addAttribute("sort", "&val="+val);
			return pagelist2;
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
			result.put("handleType",processList.getHandleType());
			result.put("typeProve",processList.getProcessDescribe().split("【")[0]);
			//if(prolist.get(i).getTypeNmae().equals("出入境申请")){
			result.put("stateCertificate", passportApplyDao.findByProId(processList).getStateCertificate()==null?"":passportApplyDao.findByProId(processList).getStateCertificate());
			//}
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

		Page<User> pageuser=udao.findByUserDeptFather(applyPerson.getFatherId(),applyPerson.getDept().getDeptId(),pa1);
		List<User> userlist=pageuser.getContent();

		//普通科员、副科长（人事科applyPerson.getDept().getDeptId()==103L）的下一步审核人是对应的正科长
		if(applyPerson.getPosition().getId()==7L || (applyPerson.getPosition().getId()==6L && (!StringUtil.stringIsNotNull(applyPerson.getUserType()) ||!applyPerson.getUserType().equals("职能正科长") || !applyPerson.getUserType().equals("临床正科长")))){
			List<User> userlist_temp=new ArrayList<>();
			userlist_temp=udao.findByUserFatherIdAndDeptId(6L,applyPerson.getDept().getDeptId(),pa1).getContent();
			userlist=userlist_temp;
	}
		// 除人力资源科科长之外的【所有正科长/主任】,他们下一步到【人事科科长】
        if(applyPerson.getPosition().getId()==6L && StringUtil.stringIsNotNull(applyPerson.getUserType()) &&(applyPerson.getUserType().equals("职能正科长") || applyPerson.getUserType().equals("临床正科长")) && applyPerson.getDept().getDeptId()!=103L){
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
        if(applyPerson.getPosition().getId()==3L && !applyPerson.getUserName().equals("195777")){
        	List<User> userlist_temp=new ArrayList<>();
			userlist_temp=udao.findBigBoss(pa1).getContent();
			userlist=userlist_temp;
		}
        //院长纪委书记
		if(StringUtil.stringIsNotNull(applyPerson.getRyzb()) && applyPerson.getRyzb().equals("院长/纪委书记")){
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
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(applyPerson.getRyzb())?applyPerson.getRyzb():"");

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
		Date date=new java.sql.Date(new Date().getTime());
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
			pro.setShenuser(pro.getShenuser()+";"+u2.getUserName());
		}
		redao.save(re2);
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
	 * 存审核表winper001
	 */
	public void index7_new(User shen, User lu, ProcessList pro, PassportApply passportApply){
		Reviewed revie=new Reviewed();
		revie.setUserId(shen);
		revie.setStatusId(23L);
		revie.setProId(pro);
		if(StringUtil.stringIsNotNull(lu.getRyzb()) && lu.getRyzb().equals("院长/纪委书记")){//如果是院长书记，则需直接设置review,process的状态
			revie.setStatusId(25L);
			revie.setAdvice("同意");
			revie.setUserId(lu);
			pro.setStatusId(25L);//这里pro的变化，会更新到revie!
			pro.setShenuser(lu.getUserName());
			revie.setReviewedTime(new Date());
			passportApply.setYzdwsjAdvice(revie.getAdvice()+","+lu.getRealName());
		}
		redao.save(revie);
	}

}

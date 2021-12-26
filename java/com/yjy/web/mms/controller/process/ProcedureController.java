package com.yjy.web.mms.controller.process;

import com.yjy.web.comm.utils.DateUtil;
import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.mms.controller.weixinqyh.MessageController;
import com.yjy.web.mms.model.dao.attendcedao.AttendceDao;
import com.yjy.web.mms.model.dao.notedao.AttachmentDao;
import com.yjy.web.mms.model.dao.plandao.TrafficDao;
import com.yjy.web.mms.model.dao.processdao.*;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.SysMeetroomDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.attendce.Attends;
import com.yjy.web.mms.model.entity.note.Attachment;
import com.yjy.web.mms.model.entity.process.*;
import com.yjy.web.mms.model.entity.system.SysMeetroom;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.process.ProcessService;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
import java.util.*;

@Controller
@RequestMapping("/")
public class ProcedureController {

	@Autowired
	private UserDao udao;
	@Autowired
	private SubjectDao sudao;
	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private ReviewedDao redao;
	@Autowired
	private EvectionMoneyDao emdao;
	@Autowired
	private BursementDao budao;
	@Autowired
	private ProcessListDao prodao;
	@Autowired
	private DetailsBurseDao dedao;
	@Autowired
	private ProcessService proservice;
	@Autowired
	private TrafficDao tdao;
	@Autowired
	private AttachmentDao AttDao;
	@Autowired
	private StayDao sadao;
	@Autowired
	private EvectionDao edao;
	@Autowired
	private OvertimeDao odao;
	@Autowired
	private HolidayDao hdao;
	@Autowired
	private RegularDao rgdao;
	@Autowired
	private ResignDao rsdao;
	@Autowired
	private AttendceDao adao;
	@Autowired
	private MealApplyDao mealApplyDao;
	@Autowired
	private DetailedlistapplyDao detailedlistapplyDao;
	@Autowired
	private DetailsListDao detailsListDao;
	@Autowired
	private LeaderReceptionDao leaderReceptionDao;
	@Autowired
	private SendMessageLogDao sendMessageLogDao;

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private SysMeetroomDao sysMeetroomDao;

	@Autowired
	private ProgramApplyDao programApplyDao;

	@Autowired
	private DataQueryApplyDao dataQueryApplyDao;
	
	@Value("${attachment.roopath}")
	private String rootpath;

	@Value("${attachment.httppath}")
	private String httppath;

	@Value("${moblie.url}")
	private String moblieurl;

	@Value("${customer.name}")
	private String customername;


	//新增页面
	@RequestMapping("xinxeng")
	public String index(){

		return "process/procedure";
	}

	
	/**
	 *会议室安排一览
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	//会议室安排一览表数据获取
	@RequestMapping("meetingcalendarload")
	public @ResponseBody List<Meeting> meetingcalendarload(@SessionAttribute("userId") Long userid, HttpServletResponse response) throws IOException{
		List<Meeting> meetings = (List<Meeting>) meetingDao.findAll();

		return meetings;
	}
	//会议室安排一览表入口
	@RequestMapping("meetingcalendar")
	private String meetingcalendar() {
		return "process/meetingcalendar";
	}
	/**
	 * 更换程序申请
	 *
	 **/
	@RequestMapping("programapply")
	public String programapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size)
	{
		proservice.indexCXGH(model, userId, page, size);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			Long proid=Long.parseLong(request.getParameter("id"));
			ProcessList processList=prodao.findbyuseridandtitle(proid);
			ProgramApply programApply=programApplyDao.findByProId(processList);
			List<Attachment> attLists =AttDao.findAttachment(proid);
			String paths="";
			for(int i=0;i<attLists.size();i++) {
				paths=attLists.get(i).getAttachmentName()+";";
			}
//			String startTime=dateFormat.format(processList.getStartTime());
//			String endTime=dateFormat.format(processList.getStartTime());
			model.addAttribute("programApply", programApply);
			model.addAttribute("process", processList);
			model.addAttribute("paths", paths);
//			model.addAttribute("startTime", startTime);
//			model.addAttribute("endTime", endTime);
		}
		String nowTime=dateFormat.format(new Date());
		model.addAttribute("nowTime", nowTime);
		return "process/programapply";
	}
	
	/**
	 *会议室申请
	 *
	 **/
	@RequestMapping("meetingapply")
	public String meetingApply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size){
		User lu=udao.getOne(userId);//申请人
		if(!((lu.getDept().getLcks().equals("职能科室")) )){
			model.addAttribute("error", "您没有相关权限，请联系网络信息科！");
			return "common/proce";
		}
		//查找类型
		proservice.indexHYSSQ(model, userId, page, size);

		//判断是否为编辑
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			
			if(!StringUtil.stringIsNotNull(String.valueOf(lu.getIs_hys()))|| (lu.getIs_hys()!=1) ){
				model.addAttribute("error", "您没有相关权限，请联系会议室管理员进行修改！");
				return "common/proce";
			}
			
			//获取原有单据信息返回到前台
			Long proid=Long.parseLong(request.getParameter("id"));
			ProcessList processList=prodao.findbyuseridandtitle(proid);
			Meeting meeting=meetingDao.findByProId(processList);
			
			model.addAttribute("username", processList.getUserId().getUserName());
			
			
			//自动生成的日期格式无法指定,程序进行处理
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
			String startTime=dateFormat.format(processList.getStartTime());
			String endTime=dateFormat.format(processList.getEndTime());
			
			model.addAttribute("meeting", meeting);
			model.addAttribute("process", processList);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			//读取全局变量单位名称
			model.addAttribute("customername", customername);
		}
		return "process/meetingapply";

	}


	/**
	 *领导接待一览
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	//一览表数据获取
	@RequestMapping("leaderreceptioncalendarload")
	public @ResponseBody List<LeaderReception> leaderreceptioncalendarload(@SessionAttribute("userId") Long userid,HttpServletResponse response) throws IOException{
		System.out.println("leaderreceptioncalendarload");
		List<LeaderReception> leaderReception = (List<LeaderReception>) leaderReceptionDao.findAll();

		return leaderReception;
	}
	//一览表入口
	@RequestMapping("leaderreceptioncalendar")
	private String leaderreceptioncalendar() {
		System.out.println("leaderreceptioncalendarload");
		return "process/leaderreceptioncalendar";
	}

	//领导接待登记
	@RequestMapping("leaderreception")
	public String leaderReception(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
								  @RequestParam(value = "page", defaultValue = "0") int page,
								  @RequestParam(value = "size", defaultValue = "10") int size){


		//查找类型
		List<SystemTypeList> overtype=tydao.findByTypeModel("aoa_mealapply");
		proservice.indexLDJD(model, userId, page, size);
		model.addAttribute("overtype", overtype);

		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			Long proid=Long.parseLong(request.getParameter("id"));
			ProcessList processList=prodao.findbyuseridandtitle(proid);
			LeaderReception leaderReception=leaderReceptionDao.findByProId(processList);
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime=dateFormat.format(processList.getStartTime());
			String endTime=dateFormat.format(processList.getEndTime());

			model.addAttribute("leaderreception", leaderReception);
			model.addAttribute("process", processList);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
		}
		return "process/leaderreception";

	}
	//数据查询申请
	@RequestMapping("dataqueryapply")
	public String dataqueryapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							@RequestParam(value = "page", defaultValue = "0") int page,
							@RequestParam(value = "size", defaultValue = "10") int size){


		//查找类型
		List<SystemTypeList> overtype=tydao.findByTypeModel("aoa_mealapply");
		proservice.indexSJCX(model, userId, page, size);
		model.addAttribute("overtype", overtype);

		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			Long proid=Long.parseLong(request.getParameter("id"));
			ProcessList processList=prodao.findbyuseridandtitle(proid);
			DataQueryapply dataQueryapply=dataQueryApplyDao.findByProId(processList);
			List<Attachment> attLists =AttDao.findAttachment(proid);
			String paths="";
			for(int i=0;i<attLists.size();i++) {
				paths=attLists.get(i).getAttachmentName()+";";
			}

			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime="";//dateFormat.format(processList.getStartTime());
			String endTime="";//dateFormat.format(processList.getStartTime());

			model.addAttribute("dataQueryapply", dataQueryapply);
			model.addAttribute("process", processList);
			model.addAttribute("paths", paths);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
		}
		return "process/dataqueryapply";

	}

	//公务接待用餐申请
	@RequestMapping("mealapply")
	public String mealapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							@RequestParam(value = "page", defaultValue = "0") int page,
							@RequestParam(value = "size", defaultValue = "10") int size){


		//查找类型
		List<SystemTypeList> overtype=tydao.findByTypeModel("aoa_mealapply");
		
		model.addAttribute("overtype", overtype);
		
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			
			proservice.indexGWJD(model, userId, page, size,Long.parseLong(request.getParameter("id")));
			
			Long proid=Long.parseLong(request.getParameter("id"));
			ProcessList processList=prodao.findbyuseridandtitle(proid);
			Mealapply mealApply=mealApplyDao.findByProId(processList);
			List<Attachment> attLists =AttDao.findAttachment(proid);
			String paths="";
			for(int i=0;i<attLists.size();i++) {
				paths=attLists.get(i).getAttachmentName()+";";
			}

			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime=dateFormat.format(processList.getStartTime());
			String endTime=dateFormat.format(processList.getStartTime());

			model.addAttribute("mealApply", mealApply);
			model.addAttribute("process", processList);
			model.addAttribute("paths", paths);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
		}else {
			proservice.indexGWJD(model, userId, page, size,0L);
		}
		return "process/mealapply";

	}
	//公务接待清单
	@RequestMapping("detailedlistapply")
	public String detailedlistapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
									@RequestParam(value = "page", defaultValue = "0") int page,
									@RequestParam(value = "size", defaultValue = "10") int size){
		Long mealapplyid=Long.parseLong(request.getParameter("id").toString());
		//查找类型
		List<SystemTypeList> uplist=tydao.findByTypeModel("aoa_detailedlistapply");
		//查找费用科目生成树
		List<Subject> second=sudao.findByParentId(1L);
		List<Subject> sublist=sudao.findByParentIdNot(1L);
//		proservice.index6(model, userId, page, size);
		proservice.indexJDQD(model, userId, page, size,mealapplyid);
		model.addAttribute("second", second);
		model.addAttribute("sublist", sublist);
		model.addAttribute("uplist", uplist);
		return "process/detailedlistapply";
	}






	/**
	    * 更换程序申请提交
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws ParseException 
	 */
	@RequestMapping("programapplysubmit")
	public String programapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,@Valid ProgramApply programApply,BindingResult rs ,@SessionAttribute("userId") Long userId,HttpServletRequest req) throws IllegalStateException, IOException, ParseException
	{

		User lu=udao.getOne(userId);//申请人

		String xzry=programApply.getNameuser();
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = null;
		String[] xzrys = xzry.split(";");
		for(int i = 0 ;i<xzrys.length;i++) {
			if(xzrys[i].trim().length()>0) {
				shen=udao.findByUserName(xzrys[i].split("/")[0]);
			}
		}
		
//		User shen=udao.findByRealNameOfLeader(mealApply.getNameuser());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		Long fatherpid=shen.getPosition().getId();

		if(StringUtil.objectIsNotNull(programApply.getProgramApplyId())&&(programApply.getProgramApplyId()!=0L)) {
			ProgramApply oldProgramApply=programApplyDao.findById(programApply.getProgramApplyId()).orElse(null);

			ProcessList oldPro=oldProgramApply.getProId();
			oldPro.setProcessName(programApply.getProgramName());
			oldPro.setStartTime(programApply.getProId().getStartTime());
			oldPro.setEndTime(programApply.getProId().getEndTime());
			proservice.index8(oldPro, val, lu,shen.getUserName());
//			programApply.setDetailedlistapplys(oldProgramApply.getDetailedlistapplys());
			programApply.setProId(oldPro);
			programApplyDao.save(programApply);
			//存审核表
			//删除审核记录
			List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
			if(!Objects.isNull(rev)){
				redao.deleteAll(rev);
			}
			proservice.index7(shen, oldPro);
			//保存文件
			if(StringUtil.stringIsNotNull(filePath[0].getOriginalFilename())) {
				//删除附件o
				List<Attachment>attList=AttDao.findAttachment(oldPro.getProcessId());
				if(!Objects.isNull(attList)){
					AttDao.deleteInBatch(attList);
				}
				//保存文件
				proservice.saveFile(oldPro, val, lu, filePath,"");
			}
			//接待消息通知到下级审核人
			sendMessageQYWX(oldPro,programApply.getProgramName()+","+programApply.getReason(),shen);
		}else {
			if(fatherid==fatherpid){
				//set主表
				ProcessList pro=programApply.getProId();
				pro.setProcessName(programApply.getProgramName());
				proservice.index8(pro, val, lu,shen.getUserName());

				programApplyDao.save(programApply);
				//存审核表
				proservice.index7(shen, pro);
				//保存文件
				proservice.saveFile(pro, val, lu, filePath,"");
				//接待消息通知到下级审核人
				sendMessageQYWX(pro,programApply.getProgramName()+","+programApply.getReason(),shen);
			}else{
				return "common/proce";
			}

		}

		return "redirect:/xinxeng";
	}
	
	
	/**
	 *会议室申请单提交
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws ParseException
	 */
	@RequestMapping("meetingapplysubmit")
	public String meetingapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid Meeting meeting,BindingResult br,
									 @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException, ParseException{
		//获取申请人
		User lu=udao.getOne(userId);
		//获取流程名称
		String val=req.getParameter("val");
		//获取审核人
		User shen=udao.findbyhys().get(0);//udao.findByRealNameOfLeader(meeting.getShenuser());
		//会议室名称
		String hysmc=req.getParameter("hysmc");
		//需通知人员
		String xzry = req.getParameter("xzry");
		
		//判断是否为编辑
		if(StringUtil.objectIsNotNull(meeting.getMeetingId())) {
			//找到原本的接待记录,因为新提交的接待没有流程ID
			Meeting oldmeeting=meetingDao.findById(meeting.getMeetingId()).orElse(null);
			//找到对应的流程ID
			ProcessList oldPro=oldmeeting.getProId();

			//检测会议室是否可以用
			String CheckHysTime=CheckHysTime(oldmeeting.getProId(),hysmc,lu);
			if(!CheckHysTime.equals("")){
				model.addAttribute("error", CheckHysTime);
				return "common/proce";
			}
			//更新流程信息表
			oldPro.setProcessName(meeting.getHymc());
			oldPro.setStartTime(meeting.getProId().getStartTime());
			oldPro.setEndTime(meeting.getProId().getEndTime());
			User oldUserid=oldPro.getUserId();
			//保存流程信息
			proservice.indexLDJD(oldPro, val, lu,shen.getUserName());

			oldPro.setUserId(oldUserid);
			
			//设置流程信息到接待记录
			meeting.setProId(oldPro);

			//先删除审核记录,再存审核表，状态直接存为已审

			List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
			if(!Objects.isNull(rev)){
				redao.deleteAll(rev);
				Reviewed revie=new Reviewed();
				revie.setUserId(shen);
				revie.setStatusId(25L);
				revie.setProId(oldPro);
				redao.save(revie);
			}
			//获取会议室
			SysMeetroom sysMeetroom = sysMeetroomDao.findbyHysmc(hysmc).get(0);
			//设置会议室ID
			meeting.setSysmeetroomId(sysMeetroom);
			//保存接待记录
			meetingDao.save(meeting);
			//发送接待确认短信
			if(meeting.getF_isfsdx()==1) {
				//proservice.SendMessage(meeting.getYwlxrdh(), meeting.getDxnr(), lu,meeting.getProId());
				//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
				String[] xzrys = xzry.split(";");
				for(int i = 0 ;i<xzrys.length;i++) {
					if(xzrys[i].trim().length()>0) {
						User tzry=udao.findByUserName(xzrys[i].split("/")[0]);
						sendMessageQYWX(oldPro,meeting.getDxnr(),tzry);
					}
				}
			}

//			sendMessageQYWX(oldPro,meeting.getHymc(),shen);

		}else {
			//检测会议室是否可以用
			String CheckHysTime=CheckHysTime(meeting.getProId(),hysmc,lu);
			if(!CheckHysTime.equals("")){
				model.addAttribute("error", CheckHysTime);
				return "common/proce";
			}
			//保存流程信息
			ProcessList pro=meeting.getProId();
			pro.setProcessName(meeting.getHymc());
			proservice.indexLDJD(pro, val, lu,shen.getUserName());
			//获取会议室
			SysMeetroom sysMeetroom = sysMeetroomDao.findbyHysmc(hysmc).get(0);
			//设置会议室ID
			meeting.setSysmeetroomId(sysMeetroom);
			//保存接待记录
			meetingDao.save(meeting);
			//存审核表,状态直接存为已审
			Reviewed revie=new Reviewed();
			revie.setUserId(shen);
			revie.setStatusId(23L);
			revie.setProId(pro);
			redao.save(revie);
			//发送接待确认短信
			if(meeting.getF_isfsdx()==1) {
//				proservice.SendMessage(meeting.getYwlxrdh(), meeting.getDxnr(), lu,pro);
				//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
				String[] xzrys = xzry.split(";");
				for(int i = 0 ;i<xzrys.length;i++) {
					if(xzrys[i].trim().length()>0) {
						User tzry=udao.findByUserName(xzrys[i].split("/")[0]);
						sendMessageQYWX(pro,meeting.getDxnr(),tzry);
					}
				}
			}
			//sendMessageQYWX(pro,meeting.getHymc(),shen);
		}
		return "redirect:/xinxeng";

	}



	/**
	 *领导接待申请单提交
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws ParseException
	 */
	@RequestMapping("leaderreceptionsubmit")
	public String leaderreceptionsubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid LeaderReception leaderReception,BindingResult br,
										@SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException, ParseException{
		User lu=udao.getOne(userId);//申请人
		if(!((lu.getRole().getRoleId()==3L) || (lu.getPosition().getId()==2L) || (lu.getPosition().getId()==3L))){
			model.addAttribute("error", "您没有相关权限，请联系网络信息科！");
			return "common/proce";
		}

		String val=req.getParameter("val");
		
		String xzry=leaderReception.getJdld_username();
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = null;
		String[] xzrys = xzry.split(";");
		for(int i = 0 ;i<xzrys.length;i++) {
			if(xzrys[i].trim().length()>0) {
				shen=udao.findByUserName(xzrys[i].split("/")[0]);
			}
		}
//		User shen=udao.findByRealNameOfLeader();//审核人

		if(StringUtil.objectIsNotNull(leaderReception.getLeaderreceptionId())) {
			//通过接待ID找到原本的接待记录，因为新提交的接待没有流程ID
			LeaderReception oldLeaderReception=leaderReceptionDao.findById(leaderReception.getLeaderreceptionId()).orElse(null);
			//找到对应的流程ID
			ProcessList oldPro=oldLeaderReception.getProId();
			//检测领导是否有时间
			String checkLeaderTime=CheckLeaderTime(oldLeaderReception.getProId(),leaderReception.getJdld_username());
			if(!checkLeaderTime.equals("")){
				model.addAttribute("error", checkLeaderTime);
				return "common/proce";
			}
			//检测会议室是否可以用
			if(leaderReception.getF_issyhys()==1) {
				String CheckHysTime=CheckJdHysTime(oldLeaderReception.getProId());
				if(!CheckHysTime.equals("")){
					model.addAttribute("error", CheckHysTime);
					return "common/proce";
				}
			}
			//更新流程信息表
			oldPro.setProcessName(leaderReception.getLfsy());
			oldPro.setStartTime(leaderReception.getProId().getStartTime());
			oldPro.setEndTime(leaderReception.getProId().getEndTime());
			//保存流程信息
			proservice.indexLDJD(oldPro, val, lu,shen.getUserName());
			//设置流程信息到接待记录
			leaderReception.setProId(oldPro);

			//先删除审核记录,再存审核表，状态直接存为已审

			List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
			if(!Objects.isNull(rev)){
				redao.deleteAll(rev);
				Reviewed revie=new Reviewed();
				revie.setUserId(shen);
				revie.setStatusId(25L);
				revie.setProId(oldPro);
				redao.save(revie);
			}
			//保存接待记录
			leaderReceptionDao.save(leaderReception);
			//发送接待确认短信
			if(leaderReception.getF_isfsdx()==1) {
				proservice.SendMessage(leaderReception.getLflxrdh(), leaderReception.getDxnr(), lu,leaderReception.getProId());
			}

			//接待消息通知到下级审核人
//			MessageController messageController = new MessageController();
//			String startEndTime=DateUtil.getFormatStr(oldPro.getStartTime(), "")+"至"+DateUtil.getFormatStr(oldPro.getEndTime(), "");
//			String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
//					+ "<div  class=\"blue\">"+startEndTime+","+leaderReception.getLfryxx()+","+leaderReception.getLfsy()+"</div>";
//			messageController.sengCardMessage("185006", "", "", oldPro.getTypeNmae()+"通知",content,moblieurl+"pages/ldjd/ldjd-list?usercode="+shen.getUserName()+"&cxdate="+DateUtil.getFormatStr(oldPro.getStartTime(), "yyyy-MM-dd"));
			sendMessageQYWX(oldPro,leaderReception.getLfryxx()+","+leaderReception.getLfsy(),shen);

		}else {
			//检测领导是否有时间
			String checkLeaderTime=CheckLeaderTime(leaderReception.getProId(),leaderReception.getJdld_username());
			if(!checkLeaderTime.equals("")){
				model.addAttribute("error", checkLeaderTime);
				return "common/proce";
			}
			//检测会议室是否可以用
			if(leaderReception.getF_issyhys()==1) {
				String CheckHysTime=CheckJdHysTime(leaderReception.getProId());
				if(!CheckHysTime.equals("")){
					model.addAttribute("error", CheckHysTime);
					return "common/proce";
				}
			}
			//保存流程信息
			ProcessList pro=leaderReception.getProId();
			pro.setProcessName(leaderReception.getLfsy());
			proservice.indexLDJD(pro, val, lu,shen.getUserName());
			//保存接待记录
			leaderReceptionDao.save(leaderReception);
			//存审核表,状态直接存为已审
			Reviewed revie=new Reviewed();
			revie.setUserId(shen);
			revie.setStatusId(25L);
			revie.setProId(pro);
			redao.save(revie);
			//发送接待确认短信
			if(leaderReception.getF_isfsdx()==1) {
				proservice.SendMessage(leaderReception.getLflxrdh(), leaderReception.getDxnr(), lu,pro);
			}
			//接待消息通知到领导
//				MessageController messageController = new MessageController();
//				String startEndTime=DateUtil.getFormatStr(pro.getStartTime(), "")+"至"+DateUtil.getFormatStr(pro.getEndTime(), "");
//				String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
//						+ "<div class=\"blue\">"+startEndTime+","+leaderReception.getLfryxx()+","+leaderReception.getLfsy()+"</div>";
//				messageController.sengCardMessage("185006", "", "", pro.getTypeNmae()+"通知",content,moblieurl+"pages/ldjd/ldjd-list?usercode="+shen.getUserName()+"&cxdate="+DateUtil.getFormatStr(pro.getStartTime(), "yyyy-MM-dd"));
			sendMessageQYWX(pro,leaderReception.getLfryxx()+","+leaderReception.getLfsy(),shen);
		}
		return "redirect:/xinxeng";

	}

	private String CheckHysTime(ProcessList process,String hysname,User lu) {
		Date startTime=process.getStartTime();
		Date endTime=process.getEndTime();
		Date now=new Date();
		
		if(!StringUtil.stringIsNotNull(String.valueOf(lu.getIs_hys()))|| (lu.getIs_hys()!=1) ){
			if(daysBetween(startTime,endTime)>1) {
				return "会议室使用不能超过一天！";
			}
//			if(daysBetween(now,startTime)<0) {
//				return "会议室申请至少要提前一天！";
//			}
			if(daysBetween(now,startTime)>15) {
				return "会议室申请至多可提前15天！";
			}
		}
		Long processId=process.getProcessId()==null?0:process.getProcessId();
		List<ProcessList> processLists =  prodao.findbystartTimeAndendTimeAndAllHys(startTime, endTime,processId,hysname);
		if(processLists.size()>0) {
			return "该时间会议室已经有安排！";
		}else
		{
			return "";
		}

	}
	public  int daysBetween(Date early, Date late) { 
	     
        java.util.Calendar calst = java.util.Calendar.getInstance();   
        java.util.Calendar caled = java.util.Calendar.getInstance();   
        calst.setTime(early);   
         caled.setTime(late);   
         //设置时间为0时   
         calst.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         calst.set(java.util.Calendar.MINUTE, 0);   
         calst.set(java.util.Calendar.SECOND, 0);   
         caled.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         caled.set(java.util.Calendar.MINUTE, 0);   
         caled.set(java.util.Calendar.SECOND, 0);   
        //得到两个日期相差的天数   
         int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst   
                .getTime().getTime() / 1000)) / 3600 / 24;   
         
        return days;   
   }   
	private String CheckLeaderTime(ProcessList process,String leader) {
		Date startTime=process.getStartTime();
		Date endTime=process.getEndTime();
		Long processId=process.getProcessId()==null?0:process.getProcessId();
		List<ProcessList> processLists =  prodao.findbystartTimeAndendTimeAndLeader(startTime, endTime,leader, processId);//
		if(processLists.size()>0) {
			return "该时间领导已经有接待人员！";
		}else
		{
			return "";
		}

	}

	private String CheckJdHysTime(ProcessList process) {
		Date startTime=process.getStartTime();
		Date endTime=process.getEndTime();
		Long processId=process.getProcessId()==null?0:process.getProcessId();
		List<ProcessList> processLists =  prodao.findbystartTimeAndendTimeAndHys(startTime, endTime,processId);
		if(processLists.size()>0) {
			return "该时间会议室已经有安排！";
		}else
		{
			return "";
		}

	}
	/**
	 * 数据查询申请单提交
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws ParseException
	 */
	@RequestMapping("dataqueryapplysubmit")
	public String dataqueryapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid DataQueryapply dataQueryapply,BindingResult br,
								  @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, ParseException{

		User lu=udao.getOne(userId);//申请人

		String xzry=dataQueryapply.getNameuser();
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = null;
		String[] xzrys = xzry.split(";");
		for(int i = 0 ;i<xzrys.length;i++) {
			if(xzrys[i].trim().length()>0) {
				shen=udao.findByUserName(xzrys[i].split("/")[0]);
			}
		}
		
//		User shen=udao.findByRealNameOfLeader(mealApply.getNameuser());//审核人
//		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
//		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		Long fatherpid=shen.getPosition().getId();

		if(StringUtil.objectIsNotNull(dataQueryapply.getDataqueryapplyId())) {
			DataQueryapply oldDataQueryapply=dataQueryApplyDao.findById(dataQueryapply.getDataqueryapplyId()).orElse(null);

			ProcessList oldPro=oldDataQueryapply.getProId();
			oldPro.setProcessName(dataQueryapply.getCxyq());
			proservice.index8(oldPro, val, lu,shen.getUserName());
//			mealApply.setDetailedlistapplys(oldDataQueryapply.getDetailedlistapplys());
			dataQueryapply.setProId(oldPro);
			dataQueryApplyDao.save(dataQueryapply);
			//存审核表
			//删除审核记录
			List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
			if(!Objects.isNull(rev)){
				redao.deleteAll(rev);
			}
			proservice.index7(shen, oldPro);
			//保存文件
//			if(StringUtil.stringIsNotNull(filePath[0].getOriginalFilename())) {
//				//删除附件o
//				List<Attachment>attList=AttDao.findAttachment(oldPro.getProcessId());
//				if(!Objects.isNull(attList)){
//					AttDao.deleteInBatch(attList);
//				}
//				//保存文件
//				proservice.saveFile(oldPro, val, lu, filePath,"");
//			}
			//接待消息通知到下级审核人
			sendMessageQYWX(oldPro,dataQueryapply.getCxyq(),shen);
		}else {
//			if(fatherid==fatherpid){
				//set主表
				ProcessList pro=dataQueryapply.getProId();
				pro.setProcessName(dataQueryapply.getCxyq());
				proservice.index8(pro, val, lu,shen.getUserName());

				dataQueryApplyDao.save(dataQueryapply);
				//存审核表
				proservice.index7(shen, pro);
				//保存文件
				proservice.saveFile(pro, val, lu, filePath,"");
				//接待消息通知到下级审核人
				sendMessageQYWX(pro,dataQueryapply.getCxyq(),shen);
//			}else{
//				return "common/proce";
//			}

		}

		return "redirect:/xinxeng";

	}
	/**
	 * 公务用餐接待申请单提交
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws ParseException
	 */
	@RequestMapping("mealapplysubmit")
	public String mealapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid Mealapply mealApply,BindingResult br,
								  @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, ParseException{

		User lu=udao.getOne(userId);//申请人

		String xzry=mealApply.getNameuser();
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = null;
		String[] xzrys = xzry.split(";");
		for(int i = 0 ;i<xzrys.length;i++) {
			if(xzrys[i].trim().length()>0) {
				shen=udao.findByUserName(xzrys[i].split("/")[0]);
			}
		}
		
//		User shen=udao.findByRealNameOfLeader(mealApply.getNameuser());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		Long fatherpid=shen.getPosition().getId();

		if(StringUtil.objectIsNotNull(mealApply.getMealapplyId())) {
			Mealapply oldMealapply=mealApplyDao.findById(mealApply.getMealapplyId()).orElse(null);

			ProcessList oldPro=oldMealapply.getProId();
			oldPro.setProcessName(mealApply.getLbdw());
			oldPro.setStartTime(mealApply.getProId().getStartTime());
			oldPro.setEndTime(mealApply.getProId().getEndTime());
			proservice.index8(oldPro, val, lu,shen.getUserName());
			mealApply.setDetailedlistapplys(oldMealapply.getDetailedlistapplys());
			mealApply.setProId(oldPro);
			mealApplyDao.save(mealApply);
			//存审核表
			//删除审核记录
			List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
			if(!Objects.isNull(rev)){
				redao.deleteAll(rev);
			}
			proservice.index7(shen, oldPro);
			//保存文件
			if(StringUtil.stringIsNotNull(filePath[0].getOriginalFilename())) {
				//删除附件o
				List<Attachment>attList=AttDao.findAttachment(oldPro.getProcessId());
				if(!Objects.isNull(attList)){
					AttDao.deleteInBatch(attList);
				}
				//保存文件
				proservice.saveFile(oldPro, val, lu, filePath,"");
			}
			//接待消息通知到下级审核人
			sendMessageQYWX(oldPro,mealApply.getLbdw()+","+mealApply.getLfsy(),shen);
		}else {
			if(fatherid==fatherpid){
				//set主表
				ProcessList pro=mealApply.getProId();
				pro.setProcessName(mealApply.getLbdw());
				proservice.index8(pro, val, lu,shen.getUserName());

				mealApplyDao.save(mealApply);
				//存审核表
				proservice.index7(shen, pro);
				//保存文件
				proservice.saveFile(pro, val, lu, filePath,"");
				//接待消息通知到下级审核人
				sendMessageQYWX(pro,mealApply.getLbdw()+","+mealApply.getLfsy(),shen);
			}else{
				return "common/proce";
			}

		}

		return "redirect:/xinxeng";

	}
		private boolean sendMessageQYWX(ProcessList process,String lfsyAndRy,User shenry) throws ParseException, IOException {
			//接待消息通知到下级审核人
			MessageController messageController = new MessageController();
			
			if(process.getTypeNmae().equals("领导接待")) {
				String startEndTime= DateUtil.getFormatStr(process.getStartTime(), "")+"至"+DateUtil.getFormatStr(process.getEndTime(), "");
				String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
						+ "<div  class=\"blue\">"+startEndTime+","+lfsyAndRy+"</div>";
				messageController.sengCardMessage(shenry.getUserName(), "", "", process.getTypeNmae()+"通知",content,moblieurl+"pages/ldjd/ldjd-list?usercode="+shenry.getUserName()+"&cxdate="+DateUtil.getFormatStr(process.getStartTime(), "yyyy-MM-dd"));
			}else if(process.getTypeNmae().equals("公务接待")) {
				String startEndTime=DateUtil.getFormatStr(process.getStartTime(), "")+"至"+DateUtil.getFormatStr(process.getEndTime(), "");
				String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
						+ "<div  class=\"blue\">"+startEndTime+","+lfsyAndRy+"</div>";
				messageController.sengCardMessage(shenry.getUserName(), "", "", process.getTypeNmae()+"审批",content,moblieurl+"pages/lcsp/lcsp-lcsp-list?usercode="+shenry.getUserName());
			}else if(process.getTypeNmae().equals("接待清单")) {
				String startEndTime=DateUtil.getFormatStr(process.getStartTime(), "")+"至"+DateUtil.getFormatStr(process.getEndTime(), "");
				String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
						+ "<div  class=\"blue\">"+startEndTime+","+lfsyAndRy+"</div>";
				messageController.sengCardMessage(shenry.getUserName(), "", "", process.getTypeNmae()+"审批",content,moblieurl+"pages/lcsp/lcsp-lcsp-list?usercode="+shenry.getUserName());
			}else if(process.getTypeNmae().equals("会议室申请")) {
//				String startEndTime=DateUtil.getFormatStr(process.getStartTime(), "")+"至"+DateUtil.getFormatStr(process.getEndTime(), "");
//				String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
//						+ "<div  class=\"blue\">"+startEndTime+","+lfsyAndRy+"</div>";
				//messageController.sengCardMessage(shenry.getUserName(), "", "", "会议通知",content,"#");
				messageController.sendTextMessage(shenry.getUserName(), "", "", "会议通知:"+lfsyAndRy);
			}else if(process.getTypeNmae().equals("更换程序")) {
				String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
						+ "<div  class=\"blue\">"+lfsyAndRy+"</div>";
				messageController.sengCardMessage(shenry.getUserName(), "", "", "更换程序",content,moblieurl+"pages/lcsp/lcsp-lcsp-list?usercode="+shenry.getUserName());
			}else if(process.getTypeNmae().equals("数据查询")) {
				String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
						+ "<div  class=\"blue\">"+lfsyAndRy+"</div>";
				messageController.sengCardMessage(shenry.getUserName(), "", "", "数据查询",content,moblieurl+"pages/lcsp/lcsp-lcsp-list?usercode="+shenry.getUserName());
			}
			return true;
		}
	/**
	 *公务接待清单接收
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws ParseException
	 */
	@RequestMapping("detailedlistapplysubmit")
	public String detailedlistapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid Detailedlistapply bu,BindingResult br,
										  @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, ParseException{
		User lu=udao.getOne(userId);//申请人
		User reuser = null;
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		String[] xzrys = bu.getUsername().split(";");
		if(xzrys.length>0) {
			if(xzrys[0].trim().length()>0) {
				reuser=udao.findByUserName(xzrys[0].split("/")[0]);
				
			}
		}

		//User reuser=udao.findByRealNameOfLeader(bu.getUsername());//udao.findByUserName(bu.getUsername());//审核人
//		User zhuti=udao.findByUserName(bu.getNamemoney());//证明人
		Integer allinvoice=0;
		Double allmoney=0.0;
		//Long roleid=lu.getRole().getRoleId();//申请人角色id
		//Long fatherid=lu.getFatherId();//申请人父id
		Long positionid=lu.getPosition().getId();//申请人id
		//Long userid=reuser.getUserId();//审核人userid

		String val=req.getParameter("val");

		Long fatherpid=reuser.getPosition().getId();
//		if(roleid>=3L && fatherid==userid){
		if((positionid==5L && fatherpid==6L)||(positionid==5L && fatherpid==7L &&reuser.getDept().getDeptId()==101L )){
// 		if(((fatherid==fatherpid)||(positionid==5L && fatherpid==6L))&&positionid!=7L){

			List<DetailsList> mm=bu.getDetails();
			for (DetailsList detailsList : mm) {
				allinvoice+=detailsList.getInvoices();
				allmoney+=detailsList.getDetailmoney();
				detailsList.setDetailedlists(bu);
			}
			//在报销费用表里面set票据总数和总金额
			bu.setAllinvoices(allinvoice);
			bu.setAllMoney(allmoney);
// 		bu.setUsermoney(zhuti);
			//set主表
			ProcessList pro=bu.getProId();
			pro.setEndTime(pro.getStartTime());
			pro.setProcessName(bu.getLbdw());

			Mealapply mealapply=mealApplyDao.findById(bu.getMealapply().getMealapplyId()).orElse(null);
			bu.setMealapply(mealapply);
			proservice.index5(pro, val, lu, filePath,reuser.getUserName());
			detailedlistapplyDao.save(bu);

			//存审核表
			proservice.index7(reuser, pro);

			proservice.saveFile(pro, val, lu, filePath,reuser.getUserName());

			//接待消息通知到下级审核人
//		MessageController messageController = new MessageController();
//		String startEndTime=DateUtil.getFormatStr(pro.getStartTime(), "")+"至"+DateUtil.getFormatStr(pro.getEndTime(), "");
//		String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
//				+ "<div class=\"highlight\">"+startEndTime+","+mealapply.getLfsy()+"</div>";
//		messageController.sengCardMessage("185006", "", "", pro.getTypeNmae()+"审批",content,moblieurl+"pages/lcsp/lcsp-lcsp-list?usercode="+reuser.getUserName());
			//sendMessageQYWX(pro,mealapply.getLfsy(),reuser);
		}else{
			return "common/proce";
		}
		return "redirect:/xinxeng";
	}


	//费用报销表单
	@RequestMapping("burse")
	public String bursement(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							@RequestParam(value = "page", defaultValue = "0") int page,
							@RequestParam(value = "size", defaultValue = "10") int size){
		//查找类型
		List<SystemTypeList> uplist=tydao.findByTypeModel("aoa_bursement");
		//查找费用科目生成树
		List<Subject> second=sudao.findByParentId(1L);
		List<Subject> sublist=sudao.findByParentIdNot(1L);
//		proservice.index6(model, userId, page, size);
		proservice.indexGWJD(model, userId, page, size,0L);
		model.addAttribute("second", second);
		model.addAttribute("sublist", sublist);
		model.addAttribute("uplist", uplist);
		return "process/bursement";
	}
	/**
	 * 费用表单接收
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("apply")
	public String apply(@RequestParam("filePath")MultipartFile filePath,HttpServletRequest req,@Valid Bursement bu,BindingResult br,
						@SessionAttribute("userId") Long userId) throws IllegalStateException, IOException{
		User lu=udao.getOne(userId);//申请人
		User reuser=udao.findByUserName(bu.getUsername());//审核人
		User zhuti=udao.findByUserName(bu.getNamemoney());//证明人
		Integer allinvoice=0;
		Double allmoney=0.0;
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=reuser.getUserId();//审核人userid

		String val=req.getParameter("val");

		Long fatherpid=reuser.getPosition().getId();
//		if(roleid>=3L && fatherid==userid){
		if(fatherid==fatherpid){

			List<DetailsBurse> mm=bu.getDetails();
			for (DetailsBurse detailsBurse : mm) {
				allinvoice+=detailsBurse.getInvoices();
				allmoney+=detailsBurse.getDetailmoney();
				detailsBurse.setBurs(bu);
			}
			//在报销费用表里面set票据总数和总金额
			bu.setAllinvoices(allinvoice);
			bu.setAllMoney(allmoney);
			bu.setUsermoney(zhuti);
			//set主表
			ProcessList pro=bu.getProId();
//		proservice.index5(pro, val, lu, filePath,reuser.getUserName());
			budao.save(bu);

			//存审核表
			proservice.index7(reuser, pro);
		}else{
			return "common/proce";
		}
		return "redirect:/xinxeng";
	}
	/**
	 * 查找出自己的申请
	 * @return
	 */
	@RequestMapping("flowmanage")
	public String flowManage(@SessionAttribute("userId") Long userId,Model model,
							 @RequestParam(value = "page", defaultValue = "0") int page,
							 @RequestParam(value = "size", defaultValue = "10") int size){
		Pageable pa=PageRequest.of(page, size);
		User user=udao.findByUserId(userId).get(0);
		Page<ProcessList> pagelist=prodao.findByuserId(userId,user.getRole().getRoleId(),pa);
		List<ProcessList> prolist=pagelist.getContent();


		for(int i = 0 ;i<prolist.size();i++) {
			String shry=prolist.get(i).getShenuser();

			String [] shrys=shry.split(";");
			String shryZW="";
			for(int j=0;j<shrys.length;j++) {
				shryZW=shryZW+udao.findByUserName(shrys[j]).getRealName()+";";
			}
			prolist.get(i).setShenuser(shryZW);
		}
		Iterable<SystemStatusList>  statusname=sdao.findByStatusModel("aoa_process_list");
		Iterable<SystemTypeList> typename=tydao.findByTypeModel("aoa_process_list");
		model.addAttribute("typename", typename);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("statusname", statusname);
		model.addAttribute("url", "shenser");
		return "process/flowmanage";
	}
	/**
	 * 申请人查看流程条件查询
	 */
	@RequestMapping("shenser")
	public String ser(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
					  @RequestParam(value = "page", defaultValue = "0") int page,
					  @RequestParam(value = "size", defaultValue = "10") int size){
		Pageable pa=PageRequest.of(page, size);
		String val=null;
		if(!TextUtil.isNullOrEmpty(req.getParameter("val"))){
			val=req.getParameter("val");
		}
		Page<ProcessList> pagelist=null;
		List<ProcessList> prolist=null;
		SystemStatusList status=sdao.findByStatusModelAndStatusName("aoa_process_list", val);
		if(TextUtil.isNullOrEmpty(val)){
			//空查询
			pagelist=prodao.findByuserId(userId,1L,pa);
		}else if(!Objects.isNull(status)){
			//根据状态和申请人查找流程
			pagelist=prodao.findByuserIdandstatus(userId,status.getStatusId(),pa);
			model.addAttribute("sort", "&val="+val);
		}else{
			//根据审核人，类型，标题模糊查询
			pagelist=prodao.findByuserIdandstr(userId,val,pa);
			model.addAttribute("sort", "&val="+val);
		}
		prolist=pagelist.getContent();
		Iterable<SystemStatusList>  statusname=sdao.findByStatusModel("aoa_process_list");
		Iterable<SystemTypeList> typename=tydao.findByTypeModel("aoa_process_list");
		model.addAttribute("typename", typename);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("statusname", statusname);
		model.addAttribute("url", "shenser");

		
		

		return "process/managetable";
	}
	/**
	 * 流程审核
	 * @return
	 */
	@RequestMapping("audit")
	public String auding(@SessionAttribute("userId") Long userId,Model model,
						 @RequestParam(value = "page", defaultValue = "0") int page,
						 @RequestParam(value = "size", defaultValue = "10") int size){
		User user=udao.getOne(userId);
		Page<AubUser> pagelist=proservice.index(user, page, size,null,model);
		List<Map<String, Object>> prolist=proservice.index2(pagelist,user);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("url", "serch");
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(user.getRyzb())?user.getRyzb():""  );
		
		return "process/auditing";
	}
	/**
	 * 流程审核条件查询
	 * @return
	 */
	@RequestMapping("serch")
	public String serch(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
						@RequestParam(value = "page", defaultValue = "0") int page,
						@RequestParam(value = "size", defaultValue = "10") int size){
		User user=udao.getOne(userId);

		String val=null;
		if(!TextUtil.isNullOrEmpty(req.getParameter("val"))){
			val=req.getParameter("val");
		}
		Page<AubUser> pagelist=proservice.index(user, page, size,val,model);
		List<Map<String, Object>> prolist=proservice.index2(pagelist,user);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("url", "serch");
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(user.getRyzb())?user.getRyzb():""  );
		return "process/audtable";
	}


	/**
	 * 查看详细
	 * @return
	 */
	@RequestMapping("particular")
	public String particular(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req){
		User user=udao.getOne(userId);//审核人或者申请人
		User audit=null;//最终审核人
		String id=req.getParameter("id");
		Long proid=Long.parseLong(id);
		String typename=req.getParameter("typename");//类型名称
		String name=null;

		Map<String, Object> map=new HashMap<>();
		ProcessList process=prodao.findById(proid).orElse(null);//查看该条申请
		Boolean flag=process.getUserId().getUserId().equals(userId);//判断是申请人还是审核人

		if(!flag){
			name="审核";
		}else{
			name="申请";
		}
		map=proservice.index3(name,user,typename,process);
		if(("费用报销").equals(typename)){
			Bursement bu=budao.findByProId(process);
			User prove=udao.getOne(bu.getUsermoney().getUserId());//证明人
			if(!Objects.isNull(bu.getOperation())){
				audit=udao.getOne(bu.getOperation().getUserId());//最终审核人
			}
			List<DetailsBurse> detaillist=dedao.findByBurs(bu);
			String type=tydao.findname(bu.getTypeId());
			String money=ProcessService.numbertocn(bu.getAllMoney());
			model.addAttribute("prove", prove);
			model.addAttribute("audit", audit);
			model.addAttribute("type", type);
			model.addAttribute("bu", bu);
			model.addAttribute("money", money);
			model.addAttribute("detaillist", detaillist);
			model.addAttribute("map", map);
			return "process/serch";
		}else if(("出差费用").equals(typename)){
			Double	staymoney=0.0;
			Double	tramoney=0.0;
			EvectionMoney emoney=emdao.findByProId(process);

			String money=ProcessService.numbertocn(emoney.getMoney());
			List<Stay> staylist=sadao.findByEvemoney(emoney);
			for (Stay stay : staylist) {
				staymoney += stay.getStayMoney();
			}
			List<Traffic> tralist=tdao.findByEvection(emoney);
			for (Traffic traffic : tralist) {
				tramoney+=traffic.getTrafficMoney();
			}
			model.addAttribute("staymoney", staymoney);
			model.addAttribute("tramoney", tramoney);
			model.addAttribute("allmoney", money);
			model.addAttribute("emoney", emoney);
			model.addAttribute("staylist", staylist);
			model.addAttribute("tralist", tralist);
			model.addAttribute("map", map);
			return "process/evemonserch";
		}else if(("出差申请").equals(typename)){
			Evection eve=edao.findByProId(process);
			model.addAttribute("eve", eve);
			model.addAttribute("map", map);
			return "process/eveserach";
		}else if(("加班申请").equals(typename)){
			Overtime eve=odao.findByProId(process);
			String type=tydao.findname(eve.getTypeId());
			model.addAttribute("eve", eve);
			model.addAttribute("map", map);
			model.addAttribute("type", type);
			return "process/overserch";
		}else if(("请假申请").equals(typename)){
			Holiday eve=hdao.findByProId(process);
			String type=tydao.findname(eve.getTypeId());
			model.addAttribute("eve", eve);
			model.addAttribute("map", map);
			model.addAttribute("type", type);
			return "process/holiserch";
		}else if(("转正申请").equals(typename)){
			Regular eve=rgdao.findByProId(process);
			model.addAttribute("eve", eve);
			model.addAttribute("map", map);
			return "process/reguserch";
		}else if(("离职申请").equals(typename)){
			Resign eve=rsdao.findByProId(process);
			model.addAttribute("eve", eve);
			model.addAttribute("map", map);
			return "process/resserch";
		}else if(("公务接待").equals(typename)){
			Mealapply eve=mealApplyDao.findByProId(process);
//				List<Mealapply> processLists =prodao.findbymealapplyid(eve.getMealapplyid());
			List<ProcessList> processLists = new ArrayList<ProcessList>();
			for(int i=0;i<eve.getDetailedlistapplys().size();i++) {
				processLists.add(prodao.findbyuseridandtitle( eve.getDetailedlistapplys().get(i).getProId().getProcessId()));
			}

//				List<Reviewed> reviewedLists = new ArrayList<Reviewed>();
//				redao.findByProIdAndUserId(pro, u)
			String type=tydao.findname(eve.getTypeId());
			if(StringUtil.stringIsNotNull(eve.getKskzAdvice())) {
				model.addAttribute("kskz", eve.getKskzAdvice().toString().split(",")[1]);
			}else {
				
				model.addAttribute("kskz", process.getUserId().getRealName());
			}
			if(StringUtil.stringIsNotNull(eve.getBgszrAdvice())) {
				model.addAttribute("bgszr", eve.getBgszrAdvice().toString().split(",")[1]);
			}else {
				model.addAttribute("bgszr", "");
			}
			if(StringUtil.stringIsNotNull(eve.getZgldAdvice())) {
				model.addAttribute("zgyz", eve.getZgldAdvice().toString().split(",")[1]);
			}else {
				model.addAttribute("zgyz", "");
			}
			if(StringUtil.stringIsNotNull(eve.getYzdwsjAdvice())) {
				model.addAttribute("yz", eve.getYzdwsjAdvice().toString().split(",")[1]);
			}else {
				model.addAttribute("yz", "");
			}
			model.addAttribute("eve", eve);
//				model.addAttribute("Detailedlistapplys",eve.getDetailedlistapplys());
			model.addAttribute("prolist",processLists);
			model.addAttribute("map", map);
			model.addAttribute("type", type);
			model.addAttribute("attachmentList",map.get("attachmentList"));
			return "process/mealapplyserch";
		}else if(("接待清单").equals(typename)){
			Detailedlistapply bu=detailedlistapplyDao.findByProId(process);

			if(StringUtil.stringIsNotNull(bu.getKskzAdvice())) {
				model.addAttribute("kskz", bu.getKskzAdvice().toString().split(",")[1]);
			}else {
				Mealapply eve=mealApplyDao.findBymealapplyId(bu.getMealapply().getMealapplyId());
				model.addAttribute("kskz", eve.getProId().getUserId().getRealName());
			}
			if(StringUtil.stringIsNotNull(bu.getBgszrAdvice())) {
				model.addAttribute("bgszr", bu.getBgszrAdvice().toString().split(",")[1]);
			}else {
				model.addAttribute("bgszr", "");
			}
			if(StringUtil.stringIsNotNull(bu.getZgldAdvice())) {
				model.addAttribute("zgyz", bu.getZgldAdvice().toString().split(",")[1]);
			}else {
				model.addAttribute("zgyz", "");
			}
			if(StringUtil.stringIsNotNull(bu.getYzdwsjAdvice())) {
				model.addAttribute("yz", bu.getYzdwsjAdvice().toString().split(",")[1]);
			}else {
				model.addAttribute("yz", "");
			}
			
			
			
			
			Mealapply mealapply=mealApplyDao.findById(bu.getMealapply().getMealapplyId()).orElse(null);
			List<ProcessList> processLists = new ArrayList<ProcessList>();
			processLists.add(prodao.findbyuseridandtitle(mealapply.getProId().getProcessId() ));
//				for(int i=0;i<mealapply.getDetailedlistapplys().size();i++) {
//					processLists.add(prodao.findbyuseridandtitle( mealapply.getDetailedlistapplys().get(i).getProId().getProcessId()));
//				}
			List<DetailsList> detaillist=detailsListDao.findBydetailedlists(bu);
			String money=ProcessService.numbertocn(bu.getAllMoney());
			String allinvoices=bu.getAllinvoices()+"";
			model.addAttribute("eve", bu);
			model.addAttribute("money", money);
			model.addAttribute("allinvoices", allinvoices);
			model.addAttribute("count", bu.getDetails().size()+2);
			model.addAttribute("detaillist", detaillist);
			model.addAttribute("map", map);
			model.addAttribute("prolist",processLists);
			model.addAttribute("attachmentList",map.get("attachmentList"));
			return "process/detailedlistserch";
		}



		return "process/serch";
	}
	/**
	 * 进入审核页面
	 * @return
	 */
	@RequestMapping("auditing")
	public String auditing(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size){
		User u=udao.getOne(userId);

		//流程id
		Long id=Long.parseLong(req.getParameter("id"));
		ProcessList process=prodao.findById(id).orElse(null);

		Reviewed re=redao.findByProIdAndUserId(process.getProcessId(), u);//查找审核表
		String typename=process.getTypeNmae().trim();
		int ishighmoney=0;
		if(("费用报销").equals(typename)){
			Bursement bu=budao.findByProId(process);
			model.addAttribute("bu", bu);
			//proservice.user(page, size, model);
			proservice.indexGWJD(model, userId, page, size,0L);
		}else if(("出差费用").equals(typename)){
			EvectionMoney emoney=emdao.findByProId(process);
			model.addAttribute("bu", emoney);
			proservice.user(page, size, model);
		}else if(("转正申请").equals(typename)||("离职申请").equals(typename)){
			User zhuan=udao.getOne(process.getUserId().getUserId());
			model.addAttribute("position", zhuan);
			proservice.user(page, size, model);
		}else if(("公务接待").equals(typename)){
			Mealapply mealapply=mealApplyDao.findByProId(process);
			model.addAttribute("mealapply", mealapply);
			proservice.indexGWJD(model, userId, page, size,mealapply.getMealapplyId());
			Double AllMoney=mealapply.getGzcje()+mealapply.getZdcje()+mealapply.getZsje();
			if(AllMoney>2000) {
				ishighmoney=1;
			}

		}else if(("接待清单").equals(typename)){
			Detailedlistapply detailedlistapply=detailedlistapplyDao.findByProId(process);
			model.addAttribute("detailedlistapply", detailedlistapply);
			proservice.indexJDQD(model, userId, page, size,detailedlistapply.getMealapply().getMealapplyId());
			Double AllMoney=detailedlistapply.getAllMoney();
			if(AllMoney>2000) {
				ishighmoney=1;
			}
		}else if(("更换程序").equals(typename)){
			ProgramApply programApply=programApplyDao.findByProId(process);
			model.addAttribute("programApply", programApply);
			String risk = tydao.findname(programApply.getRisk());
			model.addAttribute("risk", risk);
			proservice.indexCXGH(model, userId, page, size);

		}else if(("数据查询").equals(typename)){
			DataQueryapply dataQueryApply=dataQueryApplyDao.findByProId(process);
			model.addAttribute("dataQueryApply", dataQueryApply);
			proservice.indexSJCX(model, userId, page, size);
			model.addAttribute("f_iswcmsg", StringUtil.stringIsNotNull(dataQueryApply.getF_iswcmsg())?dataQueryApply.getF_iswcmsg():"");
			model.addAttribute("bz", StringUtil.stringIsNotNull(dataQueryApply.getBz())?dataQueryApply.getBz():"");
			model.addAttribute("zxry", StringUtil.stringIsNotNull(dataQueryApply.getZxry())?dataQueryApply.getZxry():"");
			
		}
		List<Map<String, Object>> list=proservice.index4(process);
		model.addAttribute("statusid", process.getStatusId());
		model.addAttribute("process", process);
		model.addAttribute("revie", list);
		model.addAttribute("size", list.size());
		model.addAttribute("statusid", process.getStatusId());
		if(StringUtil.stringIsNotNull(u.getRyzb())&&(u.getRyzb().equals("数据组")||u.getRyzb().equals("监察组"))) {
			model.addAttribute("ustatusid", process.getStatusId());

		}else {
			model.addAttribute("ustatusid", re.getStatusId());
		}
		model.addAttribute("positionid",u.getPosition().getId());
		model.addAttribute("typename", typename);
		model.addAttribute("ishighmoney", ishighmoney);
		model.addAttribute("userName", u.getUserName().toString());
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(u.getRyzb())?u.getRyzb():""  );
		
		return "process/audetail";

	}
	/**
	 * 审核确定的页面
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("aasusave")
	public String save(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,Reviewed reviewed) throws ParseException, IOException{
		User u=udao.getOne(userId);
		String name=null;
		String typename=req.getParameter("type");
		Long proid=Long.parseLong(req.getParameter("proId"));


		ProcessList pro=prodao.findById(proid).orElse(null);//找到该条流程

		User shen=udao.getOne(pro.getUserId().getUserId());//申请人
		if(!TextUtil.isNullOrEmpty(req.getParameter("liuzhuan"))){
			name=req.getParameter("liuzhuan");
		}
		if(!TextUtil.isNullOrEmpty(name)){
			//审核并流转
			String xzry=reviewed.getUsername();
			User u2 = null;
			if(StringUtil.stringIsNotNull(xzry)) {
				//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
				
				String[] xzrys = xzry.split(";");
				for(int i = 0 ;i<xzrys.length;i++) {
					if(xzrys[i].trim().length()>0) {
						u2=udao.findByUserName(xzrys[i].split("/")[0]);
					}
				}
			}

//			User u2=udao.findByRealNameOfLeader(reviewed.getUsername());//udao.findByUserName(reviewed.getUsername());//找到下一个审核人
			if(("离职申请").equals(typename)){
				if(u.getUserId().equals(pro.getUserId().getFatherId())){
					if(u2.getPosition().getId().equals(5L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选财务经理。");
						return "common/proce";
					}
				}else{
					if(u2.getPosition().getId().equals(7L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选人事经理。");
						return "common/proce";
					}
				}

			}else if(("费用报销").equals(typename)||("出差费用").equals(typename)){
				if(u.getPosition().getId().equals(6L)) {//科员
					if(u2.getPosition().getId().equals(5L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择科长。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(5L)) {//科长
					if(u2.getPosition().getId().equals(4L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择办公室负责人。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(4L)) {//办公室负责人
					if(u2.getPosition().getId().equals(3L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择主管院长。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(3L)) {//主管院长
					if(u2.getPosition().getId().equals(2L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择院长、党委书记。");
						return "common/proce";
					}
				}


//				if(u2.getPosition().getId().equals(5L)){
//					proservice.save(proid, u, reviewed, pro, u2);
//				}else{
//					model.addAttribute("error", "请选财务经理。");
//					return "common/proce";
//				}
			}else if(("公务接待").equals(typename)){
				if(u2==null) {
					model.addAttribute("error", "请审核人员");
					return "common/proce";
				}
				if(u.getPosition().getId().equals(7L)) {//科员
					if(u2.getPosition().getId().equals(6L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择科长。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(6L)) {//科长
//					if(u2.getPosition().getId().equals(5L)){
						proservice.save(proid, u, reviewed, pro, u2);
//					}else{
//						model.addAttribute("error", "请选择办公室办事员。");
//						return "common/proce";
//					}
				}else if(u.getPosition().getId().equals(5L)) {//办公室专员
					if(u2.getPosition().getId().equals(4L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择办公室负责人。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(4L)) {//办公室负责人
					if(u2.getPosition().getId().equals(3L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择主管院长。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(3L)) {//主管院长
					if(u2.getPosition().getId().equals(2L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择院长、党委书记。");
						return "common/proce";
					}
				}

				Mealapply mealApply = mealApplyDao.findByProId(pro);
				sendMessageQYWX(pro,mealApply.getLbdw()+","+mealApply.getLfsy(),u2);
//				if(u2.getPosition().getId().equals(5L)){
//					proservice.save(proid, u, reviewed, pro, u2);
//				}else{
//					model.addAttribute("error", "请选财务经理。");
//					return "common/proce";
//				}
			}else if(("接待清单").equals(typename)){
				if(u2==null) {
					model.addAttribute("error", "请审核人员");
					return "common/proce";
				}
				
				
				if(u.getPosition().getId().equals(7L)) {//办公室科员
					if(u2.getPosition().getId().equals(4L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择办公室主任");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(5L)) {//办公室办事员
					if(u2.getPosition().getId().equals(6L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择科长。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(6L)) {//科长
					if(u2.getPosition().getId().equals(4L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择办公室负责人。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(4L)) {//办公室负责人
					if(u2.getPosition().getId().equals(3L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择主管院长。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(3L)) {//主管院长
					if(u2.getPosition().getId().equals(2L)){
						proservice.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择院长、党委书记。");
						return "common/proce";
					}
				}
				Detailedlistapply detailedlistapply=detailedlistapplyDao.findByProId(pro);
				
				Mealapply mealApply = detailedlistapply.getMealapply();
				sendMessageQYWX(pro,mealApply.getLbdw()+","+mealApply.getLfsy(),u2);
			}else if(("数据查询").equals(typename)){
				
				if ( StringUtil.stringIsNotNull(u.getRyzb())&&(u.getRyzb().equals("数据组"))) {
					
					Long statusid=Long.parseLong(req.getParameter("statusId"));
					String f_iswcmsg=req.getParameter("f_iswcmsg");
					String bz=req.getParameter("bz");
					DataQueryapply dataQueryapply = dataQueryApplyDao.findByProId(pro);
					dataQueryapply.setF_iswcmsg(f_iswcmsg);
					dataQueryapply.setBz(bz);
					dataQueryapply.setZxry(u.getRealName());
					dataQueryApplyDao.save(dataQueryapply);
					pro.setStatusId(statusid);//改变主表的状态
					prodao.save(pro);
					if(f_iswcmsg.equalsIgnoreCase("1")) {
						//发送消息通知申请人
						MessageController messageController = new MessageController();
						String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
								+ "<div  class=\"blue\">"+bz+"</div>";
						messageController.sengCardMessage(pro.getUserId().getUserName(), "", "", pro.getTypeNmae()+"通知",content,moblieurl+"pages/lcsp/lcsp-lcsp-list?usercode="+pro.getUserId().getUserName());
					}


				}else {
					if(u2==null) {
						model.addAttribute("error", "请审核人员");
						return "common/proce";
					}
					if(u.getPosition().getId().equals(7L)) {//科员
						if(u2.getPosition().getId().equals(6L)){
							proservice.save(proid, u, reviewed, pro, u2);
						}else{
							model.addAttribute("error", "请选择科长。");
							return "common/proce";
						}
					}else {
						proservice.save(proid, u, reviewed, pro, u2);
					}
					DataQueryapply dataQueryapply = dataQueryApplyDao.findByProId(pro);
					sendMessageQYWX(pro,dataQueryapply.getCxyq(),u2);
				}

//				if(u2.getPosition().getId().equals(5L)){
//					proservice.save(proid, u, reviewed, pro, u2);
//				}else{
//					model.addAttribute("error", "请选财务经理。");
//					return "common/proce";
//				}
			}

			else{
				if(u2.getPosition().getId().equals(7L)){
					proservice.save(proid, u, reviewed, pro, u2);
				}else{
					model.addAttribute("error", "请选择正确人员审核！");
					return "common/proce";
				}
			}

		}else{
			//审核并结案
			Reviewed re=redao.findByProIdAndUserId(proid,u);
			re.setAdvice(reviewed.getAdvice());
			re.setStatusId(reviewed.getStatusId());
			re.setReviewedTime(new Date());
			redao.save(re);
			pro.setStatusId(reviewed.getStatusId());//改变主表的状态
			prodao.save(pro);
			if(("请假申请").equals(typename)||("出差申请").equals(typename)){
				if(reviewed.getStatusId()==25){
					Attends attend=new Attends();
					attend.setHolidayDays(pro.getProcseeDays());
					attend.setHolidayStart(pro.getStartTime());
					attend.setUser(pro.getUserId());
					if(("请假申请").equals(typename)){
						attend.setStatusId(46L);
					}else if(("出差申请").equals(typename)){
						attend.setStatusId(47L);
					}
					adao.save(attend);
				}
			}else if(("数据查询").equals(typename)){
				User user1=udao.findByUserName("185006");
				User user2=udao.findByUserName("5521");
				User user3=udao.findByUserName("140708");
				sendMessageQYWX(pro,pro.getProcessName(),user1);
				sendMessageQYWX(pro,pro.getProcessName(),user2);
				sendMessageQYWX(pro,pro.getProcessName(),user3);
			}


		}


		if(("费用报销").equals(typename)){
			Bursement  bu=budao.findByProId(pro);
			if(shen.getFatherId().equals(u.getUserId())){
				bu.setManagerAdvice(reviewed.getAdvice());
				budao.save(bu);
			}
			if(u.getPosition().getId().equals(2L)){
				bu.setFinancialAdvice(reviewed.getAdvice());
				bu.setBurseTime(new Date());
				bu.setOperation(u);
				budao.save(bu);
			}


		}else if(("出差费用").equals(typename)){
			EvectionMoney emoney=emdao.findByProId(pro);
			if(shen.getFatherId().equals(u.getUserId())){
				emoney.setManagerAdvice(reviewed.getAdvice());
				emdao.save(emoney);
			}
			if(u.getPosition().getId()==5){
				emoney.setFinancialAdvice(reviewed.getAdvice());
				emdao.save(emoney);
			}
		}else if(("出差申请").equals(typename)){
			Evection ev=edao.findByProId(pro);
			if(shen.getFatherId().equals(u.getUserId())){
				ev.setManagerAdvice(reviewed.getAdvice());
				edao.save(ev);
			}
			if(u.getPosition().getId().equals(7L)){
				ev.setPersonnelAdvice(reviewed.getAdvice());
				edao.save(ev);
			}
		}else if(("加班申请").equals(typename)){
			Overtime over=odao.findByProId(pro);
			if(shen.getFatherId().equals(u.getUserId())){
				over.setManagerAdvice(reviewed.getAdvice());
				odao.save(over);
			}
			if(u.getPosition().getId().equals(7L)){
				over.setPersonnelAdvice(reviewed.getAdvice());
				odao.save(over);
			}
		}else if(("公务接待").equals(typename)){
			Mealapply meal=mealApplyDao.findByProId(pro);
//			if(shen.getFatherId().equals(u.getUserId())){
//				meal.setManagerAdvice(reviewed.getAdvice());
//				mealApplyDao.save(meal);
//			}
			if(u.getPosition().getId().equals(2L)){
				meal.setYzdwsjAdvice(reviewed.getAdvice()+","+u.getRealName());
				mealApplyDao.save(meal);
			}else if(u.getPosition().getId().equals(3L)){
				meal.setZgldAdvice(reviewed.getAdvice()+","+u.getRealName());
				mealApplyDao.save(meal);
			}else if(u.getPosition().getId().equals(4L)){
				meal.setBgszrAdvice(reviewed.getAdvice()+","+u.getRealName());
				mealApplyDao.save(meal);
			}else if(u.getPosition().getId().equals(5L)){
				meal.setBgsbsyAdvice(reviewed.getAdvice()+","+u.getRealName());
				mealApplyDao.save(meal);
			}else if(u.getPosition().getId().equals(6L)){
				meal.setKskzAdvice(reviewed.getAdvice()+","+u.getRealName());
				mealApplyDao.save(meal);
			}
		}else if(("接待清单").equals(typename)){
			Detailedlistapply detailedlistapply=detailedlistapplyDao.findByProId(pro);
			if(u.getPosition().getId().equals(2L)){

				detailedlistapply.setYzdwsjAdvice(reviewed.getAdvice()+","+u.getRealName());
				detailedlistapplyDao.save(detailedlistapply);
			}else if(u.getPosition().getId().equals(3L)){
				detailedlistapply.setZgldAdvice(reviewed.getAdvice()+","+u.getRealName());
				detailedlistapplyDao.save(detailedlistapply);
			}else if(u.getPosition().getId().equals(4L)){
				detailedlistapply.setBgszrAdvice(reviewed.getAdvice()+","+u.getRealName());
				detailedlistapplyDao.save(detailedlistapply);
			}else if(u.getPosition().getId().equals(5L)){
				detailedlistapply.setBgsbsyAdvice(reviewed.getAdvice()+","+u.getRealName());
				detailedlistapplyDao.save(detailedlistapply);
			}else if(u.getPosition().getId().equals(6L)){
				detailedlistapply.setKskzAdvice(reviewed.getAdvice()+","+u.getRealName());
				detailedlistapplyDao.save(detailedlistapply);
			}
		}else if(("数据查询").equals(typename)){
			DataQueryapply dataQueryapply=dataQueryApplyDao.findByProId(pro);
			if((u.getPosition().getId().equals(6L))&&(u.getDept().equals(120L))){
				dataQueryapply.setWlxxkkzAdvice(reviewed.getAdvice()+","+u.getRealName());
				
			}else if((u.getPosition().getId().equals(6L))&&(u.getDept().getLcks().equals("是"))) {
				dataQueryapply.setLckzAdvice(reviewed.getAdvice()+","+u.getRealName());
			}else if((u.getPosition().getId().equals(6L))&&(!u.getDept().getLcks().equals("是"))) {
				dataQueryapply.setZnkzAdvice(reviewed.getAdvice()+","+u.getRealName());
			}
			dataQueryApplyDao.save(dataQueryapply);
			
		}
		else if(("请假申请").equals(typename)){
			Holiday over=hdao.findByProId(pro);
			if(shen.getFatherId().equals(u.getUserId())){
				over.setManagerAdvice(reviewed.getAdvice());
				hdao.save(over);
			}
			if(u.getPosition().getId().equals(7L)){
				over.setPersonnelAdvice(reviewed.getAdvice());
				hdao.save(over);
			}
		}else if(("转正申请").equals(typename)){
			Regular over=rgdao.findByProId(pro);
			if(shen.getFatherId().equals(u.getUserId())){
				over.setManagerAdvice(reviewed.getAdvice());
				rgdao.save(over);
			}
			if(u.getPosition().getId().equals(7L)){
				over.setPersonnelAdvice(reviewed.getAdvice());
				rgdao.save(over);
			}
		}else if(("离职申请").equals(typename)){

			Resign over=rsdao.findByProId(pro);
			if(shen.getFatherId().equals(u.getUserId())){

				over.setManagerAdvice(reviewed.getAdvice());
				rsdao.save(over);
			}
			if(u.getPosition().getId()==5){
				over.setPersonnelAdvice(reviewed.getAdvice());
				rsdao.save(over);
			}else if(u.getPosition().getId().equals(7L)){
				over.setFinancialAdvice(reviewed.getAdvice());
				rsdao.save(over);
			}
		}
		return "redirect:/audit";

	}

	//出差费用
	@RequestMapping("evemoney")
	public String evemoney(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest req,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size){
		Long proid=Long.parseLong(req.getParameter("id"));//出差申请的id
		ProcessList prolist=prodao.findbyuseridandtitle(userId, proid);//找这个用户的出差申请
		proservice.index6(model, userId, page, size);
		model.addAttribute("prolist", prolist);
		return "process/evectionmoney";
	}
	/**
	 * 出差费用表单接收
	 * @param model
	 * @return
	 */
	@RequestMapping("moneyeve")
	public String moneyeve(@RequestParam("filePath")MultipartFile filePath,HttpServletRequest req,@Valid EvectionMoney eve,BindingResult br,
						   @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException{
		User lu=udao.getOne(userId);//申请人
		User shen=udao.findByUserName(eve.getShenname());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		Double allmoney=0.0;
		if(roleid>=3L && fatherid==userid){
			List<Traffic> ss=eve.getTraffic();
			for (Traffic traffic : ss) {
				allmoney+=traffic.getTrafficMoney();
				User u=udao.findByUserName(traffic.getUsername());
				traffic.setUser(u);
				traffic.setEvection(eve);

			}
			List<Stay> mm=eve.getStay();
			for (Stay stay : mm) {
				allmoney+=stay.getStayMoney()*stay.getDay();
				User u=udao.findByUserName(stay.getNameuser());
				stay.setUser(u);
				stay.setEvemoney(eve);
			}

			eve.setMoney(allmoney);
			//set主表
			ProcessList pro=eve.getProId();
			System.out.println(pro+"mmmmmm");
//						proservice.index5(pro, val, lu, filePath,shen.getUserName());
			emdao.save(eve);
			//存审核表
			proservice.index7(shen, pro);
		}else{
			return "common/proce";
		}

		return "redirect:/flowmanage";

	}
	//出差申请
	@RequestMapping("evection")
	public String evection(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size){
		//查找类型
		List<SystemTypeList> outtype=tydao.findByTypeModel("aoa_evection");
		proservice.index6(model, userId, page, size);
		model.addAttribute("outtype", outtype);
		return "process/evection";
	}
	/**
	 * 出差申请表单接收
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("evec")
	public String evec(@RequestParam("filePath")MultipartFile filePath,HttpServletRequest req,@Valid Evection eve,BindingResult br,
					   @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException{
		User lu=udao.getOne(userId);//申请人
		User shen=udao.findByUserName(eve.getNameuser());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		if(roleid>=3L && fatherid==userid){
			//set主表
			ProcessList pro=eve.getProId();
//			proservice.index5(pro, val, lu, filePath,shen.getUserName());
			edao.save(eve);
			//存审核表
			proservice.index7(shen, pro);
		}else{
			return "common/proce";
		}

		return "redirect:/xinxeng";
	}
	//加班申请
	@RequestMapping("overtime")
	public String overtime(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size){
		//查找类型
		List<SystemTypeList> overtype=tydao.findByTypeModel("aoa_overtime");
		proservice.index6(model, userId, page, size);
		model.addAttribute("overtype", overtype);
		return "process/overtime";
	}
	/**
	 * 加班申请接收
	 * @return
	 */
	@RequestMapping("over")
	public String over(HttpServletRequest req,@Valid Overtime eve,BindingResult br,
					   @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException{
		User lu=udao.getOne(userId);//申请人
		User shen=udao.findByUserName(eve.getNameuser());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		if(roleid>=3L && fatherid==userid){
			//set主表
			ProcessList pro=eve.getProId();
			proservice.index8(pro, val, lu,shen.getUserName());
			odao.save(eve);
			//存审核表
			proservice.index7(shen, pro);
		}else{
			return "common/proce";
		}

		return "redirect:/xinxeng";

	}
	//请假申请
	@RequestMapping("holiday")
	public String holiday(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
						  @RequestParam(value = "page", defaultValue = "0") int page,
						  @RequestParam(value = "size", defaultValue = "10") int size){
		//查找类型
		List<SystemTypeList> overtype=tydao.findByTypeModel("aoa_holiday");
		proservice.index6(model, userId, page, size);
		model.addAttribute("overtype", overtype);
		return "process/holiday";
	}
	/**
	 * 请假申请接收
	 * @param model
	 * @return
	 */
	@RequestMapping("holi")
	public String holi(@RequestParam("filePath")MultipartFile filePath,HttpServletRequest req,@Valid Holiday eve,BindingResult br,
					   @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException{
		User lu=udao.getOne(userId);//申请人
		User shen=udao.findByUserName(eve.getNameuser());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		if(roleid>=3L && fatherid==userid){
			SystemTypeList  type=tydao.findById(eve.getTypeId()).orElse(null);
			if(eve.getTypeId()==40){
				if(type.getTypeSortValue()<eve.getLeaveDays()){
					model.addAttribute("error", "婚假必须小于10天。");
					return "common/proce";
				}
			}else if(eve.getTypeId()==38){
				if(type.getTypeSortValue()<eve.getLeaveDays()){
					model.addAttribute("error", "单次事假必须小于4天。");
					return "common/proce";
				}
			}else if(eve.getTypeId()==42){
				if(type.getTypeSortValue()<eve.getLeaveDays()){
					model.addAttribute("error", "陪产假必须小于10天。");
					return "common/proce";
				}
			}else{
				//set主表
				ProcessList pro=eve.getProId();
//				proservice.index5(pro, val, lu, filePath,shen.getUserName());
				hdao.save(eve);
				//存审核表
				proservice.index7(shen, pro);
			}
		}else{
			return "common/proce";
		}

		return "redirect:/xinxeng";
	}
	//转正申请
	@RequestMapping("regular")
	public String regular(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
						  @RequestParam(value = "page", defaultValue = "0") int page,
						  @RequestParam(value = "size", defaultValue = "10") int size){
		proservice.index6(model, userId, page, size);
		return "process/regular";
	}
	/**
	 * 转正申请接收
	 * @param model
	 * @return
	 */
	@RequestMapping("regu")
	public String regu(HttpServletRequest req,@Valid Regular eve,BindingResult br,
					   @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException{
		User lu=udao.getOne(userId);//申请人
		User shen=udao.findByUserName(eve.getNameuser());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		if(roleid>=3L && fatherid==userid){
			if(lu.getRole().getRoleId()==6 ||lu.getRole().getRoleId()==7){

				//set主表
				ProcessList pro=eve.getProId();
				proservice.index8(pro, val, lu,shen.getUserName());
				rgdao.save(eve);
				//存审核表
				proservice.index7(shen, pro);
			}else{
				model.addAttribute("error", "你不需要转正。。。");
				return "common/proce";
			}
		}else{
			return "common/proce";
		}

		return "redirect:/xinxeng";

	}
	//离职申请
	@RequestMapping("resign")
	public String resign(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
						 @RequestParam(value = "page", defaultValue = "0") int page,
						 @RequestParam(value = "size", defaultValue = "10") int size){
		proservice.index6(model, userId, page, size);
		return "process/resign";
	}
	/**
	 * 离职申请接收
	 * @param model
	 * @return
	 */
	@RequestMapping("res")
	public String res(HttpServletRequest req,@Valid Resign eve,BindingResult br,
					  @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException{
		User lu=udao.getOne(userId);//申请人
		User shen=udao.findByUserName(eve.getNameuser());//审核人
		Long roleid=lu.getRole().getRoleId();//申请人角色id
		Long fatherid=lu.getFatherId();//申请人父id
		Long userid=shen.getUserId();//审核人userid
		String val=req.getParameter("val");
		if(roleid>=3L && fatherid==userid){
			//set主表
			ProcessList pro=eve.getProId();
			proservice.index8(pro, val, lu,shen.getUserName());
			eve.setHandUser(udao.findByUserName(eve.getHanduser()));
			rsdao.save(eve);
			//存审核表
			proservice.index7(shen, pro);
		}else{
			return "common/proce";
		}

		return "redirect:/xinxeng";

	}


	/**
	 * 删除单据
	 */
	@RequestMapping("processdelete")
	public String processdelete(HttpServletRequest req,@SessionAttribute("userId") Long userId,Model model){
		Long proid=Long.parseLong(req.getParameter("id"));
		ProcessList process=prodao.findById(proid).orElse(null);
		User lu=udao.getOne(userId);//申请人
		if ( process.getTypeNmae().equals("会议室申请")) {
			
			if(!StringUtil.stringIsNotNull(String.valueOf(lu.getIs_hys()))|| (lu.getIs_hys()!=1) ){
				model.addAttribute("error", "您没有相关权限，请联系会议室管理员进行删除！");
				return "common/proce";
			}
		}

		
		
		
		//删除审核记录
		List<Reviewed> rev=redao.findByProId(proid);
		if(!Objects.isNull(rev)){
			redao.deleteAll(rev);
		}
		//删除附件
		List<Attachment>attList=AttDao.findAttachment(proid);
		if(!Objects.isNull(attList)){
			AttDao.deleteInBatch(attList);
		}
		if( process.getTypeNmae().equals("公务接待")) {
			//删除公务接单申请单
			Mealapply mealapply=mealApplyDao.findByProId(process);
			if(!Objects.isNull(mealapply)){
				mealApplyDao.delete(mealapply);
			}

		}else if(process.getTypeNmae().equals("接待清单")) {
			Detailedlistapply deapply=detailedlistapplyDao.findByProId(process);
			//删除接待清单附件
			List<DetailsList> detailsLists=detailsListDao.findBydetailedlists(deapply);
			if(!Objects.isNull(detailsLists)){
				detailsListDao.deleteAll(detailsLists);
			}
			//删除接待清单申请单
			if(!Objects.isNull(deapply)){
				detailedlistapplyDao.delete(deapply);
			}
		}else if( process.getTypeNmae().equals("领导接待")) {
			//删除领导接待短信通知记录
			List<SendMessageLog> sendMessageLog = sendMessageLogDao.findByProId(process);
			if(!Objects.isNull(sendMessageLog)){
				sendMessageLogDao.deleteAll(sendMessageLog);
			}
			//删除领导接待申请单
			LeaderReception leaderReception=leaderReceptionDao.findByProId(process);
			if(!Objects.isNull(leaderReception)){
				leaderReceptionDao.delete(leaderReception);
			}

		}else if ( process.getTypeNmae().equals("会议室申请")) {
			//删除公务接单申请单
			Meeting meeting=meetingDao.findByProId(process);
			if(!Objects.isNull(meeting)){
				meetingDao.delete(meeting);
			}
		}else if ( process.getTypeNmae().equals("更换程序")) {
			//删除公务接单申请单
			ProgramApply programApply=programApplyDao.findByProId(process);
			if(!Objects.isNull(programApply)){
				programApplyDao.delete(programApply);
			}
		}else if ( process.getTypeNmae().equals("数据查询")) {
			//删除公务接单申请单
			DataQueryapply dataQueryapply=dataQueryApplyDao.findByProId(process);
			if(!Objects.isNull(dataQueryapply)){
				dataQueryApplyDao.delete(dataQueryapply);
			}
		}
		prodao.delete(process);
		return "redirect:/flowmanage";

	}

	/**
	 * 删除
	 */
	@RequestMapping("sdelete")
	public String dele(HttpServletRequest req,@SessionAttribute("userId") Long userId,Model model){
		User lu=udao.getOne(userId);//审核人
		Long proid=Long.parseLong(req.getParameter("id"));
		Reviewed rev=redao.findByProIdAndUserId(proid, lu);
		if(!Objects.isNull(rev)){
			rev.setDel(true);
			redao.save(rev);
		}else{
			return "common/proce";
		}
		return "redirect:/audit";

	}

	/**
	 * 下载文件
	 * @param response
	 * @param fileid
	 */
	@RequestMapping("file")
	public void downFile(HttpServletResponse response, @RequestParam("fileid") Long fileid) {
		try {
			Attachment attd = AttDao.getOne(fileid);
			File file = new File(rootpath,attd.getAttachmentPath());
			response.setContentLength(attd.getAttachmentSize().intValue());
			response.setContentType(attd.getAttachmentType());
			response.setHeader("Content-Disposition","attachment;filename=" + new String(attd.getAttachmentName().getBytes("UTF-8"), "ISO8859-1"));
			proservice.writefile(response, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 图片预览
	 * @param response
	 */
	@RequestMapping("show/**")
	public void image(Model model, HttpServletResponse response, @SessionAttribute("userId") Long userId, HttpServletRequest request)
			throws IOException {

		String startpath = new String(URLDecoder.decode(request.getRequestURI(), "utf-8"));

		String path = startpath.replace("/show", "");

		File f = new File(rootpath, path);
		System.out.println(f.getAbsolutePath());
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

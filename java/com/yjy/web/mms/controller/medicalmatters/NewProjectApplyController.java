package com.yjy.web.mms.controller.medicalmatters;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.mms.model.dao.attendcedao.AttendceDao;
import com.yjy.web.mms.model.dao.medicalmattersdao.MedicalMattersListDao;
import com.yjy.web.mms.model.dao.medicalmattersdao.MedicalMattersReviewedDao;
import com.yjy.web.mms.model.dao.medicalmattersdao.NewPrjectOneClassDao;
import com.yjy.web.mms.model.dao.notedao.AttachmentDao;
import com.yjy.web.mms.model.dao.plandao.TrafficDao;
import com.yjy.web.mms.model.dao.processdao.BursementDao;
import com.yjy.web.mms.model.dao.processdao.DataQueryApplyDao;
import com.yjy.web.mms.model.dao.processdao.DetailedlistapplyDao;
import com.yjy.web.mms.model.dao.processdao.DetailsBurseDao;
import com.yjy.web.mms.model.dao.processdao.DetailsListDao;
import com.yjy.web.mms.model.dao.processdao.EvectionDao;
import com.yjy.web.mms.model.dao.processdao.EvectionMoneyDao;
import com.yjy.web.mms.model.dao.processdao.HolidayDao;
import com.yjy.web.mms.model.dao.processdao.LeaderReceptionDao;
import com.yjy.web.mms.model.dao.processdao.MealApplyDao;
import com.yjy.web.mms.model.dao.processdao.MeetingDao;
import com.yjy.web.mms.model.dao.processdao.OvertimeDao;
import com.yjy.web.mms.model.dao.processdao.ProcessListDao;
import com.yjy.web.mms.model.dao.processdao.ProgramApplyDao;
import com.yjy.web.mms.model.dao.processdao.RegularDao;
import com.yjy.web.mms.model.dao.processdao.ResignDao;
import com.yjy.web.mms.model.dao.processdao.ReviewedDao;
import com.yjy.web.mms.model.dao.processdao.SendMessageLogDao;
import com.yjy.web.mms.model.dao.processdao.StayDao;
import com.yjy.web.mms.model.dao.processdao.SubjectDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.SysMeetroomDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.medicalmatters.MedicalMattersList;
import com.yjy.web.mms.model.entity.medicalmatters.MedicalMattersReviewed;
import com.yjy.web.mms.model.entity.medicalmatters.NewPrjectOneClass;
import com.yjy.web.mms.model.entity.process.Meeting;
import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.Reviewed;
import com.yjy.web.mms.model.entity.system.SysMeetroom;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.medicalmattersservices.MedicalMattersServices;
import com.yjy.web.mms.services.process.ProcessService;

@Controller
@RequestMapping("/")
public class NewProjectApplyController {

	@Autowired
	private UserDao udao;
	@Autowired
	private SubjectDao sudao;
	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private MedicalMattersReviewedDao mmredao;
	
	@Autowired
	private SendMessageLogDao sendMessageLogDao;

	@Autowired
	private MedicalMattersServices mmServices;
	
	@Autowired
	private MedicalMattersListDao medicalMattersListDao;
	@Autowired
	private NewPrjectOneClassDao newPrjectOneClassDao;
	
	
	
	
	@Value("${attachment.roopath}")
	private String rootpath;

	@Value("${attachment.httppath}")
	private String httppath;

	@Value("${moblie.url}")
	private String moblieurl;

	@Value("${customer.name}")
	private String customername;
	/**
	 *I类非创伤性新技术新项目申请
	 *
	 **/
	@RequestMapping("one-new-apply")
	public String onenewapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size){
		User lu=udao.getOne(userId);//申请人
//		if(!((lu.getDept().getLcks().equals("职能科室")) )){
//			model.addAttribute("error", "您没有相关权限，请联系网络信息科！");
//			return "common/proce";
//		}
		//设置页面字典等内容
		mmServices.getApplyDictionaries(model, userId, page, size);
		//申请名称
		model.addAttribute("title", "I类非创伤性新技术新项目");
		//判断是否为编辑
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			
			//获取原有单据信息返回到前台
			Long proid=Long.parseLong(request.getParameter("id"));
			MedicalMattersList medicalMattersList=medicalMattersListDao.findbyMedicalmattersId(proid);
			NewPrjectOneClass newPrjectOneClass=newPrjectOneClassDao.findByMedicalmattersId(medicalMattersList);
			
			model.addAttribute("username", medicalMattersList.getUserId().getUserName());
			
			
			//自动生成的日期格式无法指定,程序进行处理
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
			String startTime=dateFormat.format(medicalMattersList.getStartTime());
			String endTime=dateFormat.format(medicalMattersList.getEndTime());
			
			model.addAttribute("newPrjectOneClass", newPrjectOneClass);
			model.addAttribute("medicalMattersList", medicalMattersList);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			//读取全局变量单位名称
			model.addAttribute("customername", customername);
			
			
		}
		return "medicalapply/newprojectoneclass";

	}
	
	/**
	 *会议室申请单提交
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws ParseException
	 */
	@RequestMapping("one-new-submit")
	public String meetingapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid NewPrjectOneClass newPrjectOneClass,BindingResult br,
									 @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException, ParseException{
		//获取申请人
		User lu=udao.getOne(userId);
		System.out.println("lu:"+lu.getRealName());
		//获取流程名称
		String val=req.getParameter("val");
		//获取审核人
		String xzry=req.getParameter("nameuser");
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = null;
		String[] xzrys = xzry.split(";");
		for(int i = 0 ;i<xzrys.length;i++) {
			if(xzrys[i].trim().length()>0) {
				shen=udao.findByUserName(xzrys[i].split("/")[0]);
			}
		}
		
		//判断是否为编辑
		if(StringUtil.objectIsNotNull(newPrjectOneClass.getOneClassId())) {
			//找到原本的接待记录,因为新提交的接待没有流程ID
			NewPrjectOneClass oldNewPrjectOneClass=newPrjectOneClassDao.findById(newPrjectOneClass.getOneClassId()).orElse(null);
			//找到对应的流程ID
			MedicalMattersList oldMMPro=oldNewPrjectOneClass.getMedicalmattersId();

			//更新流程信息表
			oldMMPro.setProcessName(oldNewPrjectOneClass.getProjceName());
			oldMMPro.setStartTime(oldNewPrjectOneClass.getStartTime());
			oldMMPro.setEndTime(oldNewPrjectOneClass.getStartTime());
			User oldUserid=oldMMPro.getUserId();
			//保存流程信息
			mmServices.setMedicalMatters(oldMMPro, val, lu,shen.getUserName());
			medicalMattersListDao.save(oldMMPro);

			//设置流程信息到接待记录
			newPrjectOneClass.setMedicalmattersId(oldMMPro);
			newPrjectOneClassDao.save(newPrjectOneClass);

			//先删除审核记录,再存审核表
			List<MedicalMattersReviewed> mmrev=mmredao.findByMedicalmattersId(oldMMPro.getMedicalmattersId());
			if(!Objects.isNull(mmrev)){
				mmredao.deleteAll(mmrev);
			}
			mmServices.setMedicalMattersReviewed(shen, oldMMPro);

//			sendMessageQYWX(oldPro,meeting.getHymc(),shen);

		}else {
			//保存流程信息
			MedicalMattersList mmpro=newPrjectOneClass.getMedicalmattersId();
			mmpro.setProcessName(newPrjectOneClass.getProjceName());
			mmServices.setMedicalMatters(mmpro, val, lu,shen.getUserName());
			
			//保存接待记录
			newPrjectOneClassDao.save(newPrjectOneClass);
			//存审核表
			mmServices.setMedicalMattersReviewed(shen, mmpro);
			
			//sendMessageQYWX(pro,meeting.getHymc(),shen);
		}
		return "medicalapply/newprojectindex";

	}
	
}

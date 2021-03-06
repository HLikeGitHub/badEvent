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
	 *I???????????????????????????????????????
	 *
	 **/
	@RequestMapping("one-new-apply")
	public String onenewapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size){
		User lu=udao.getOne(userId);//?????????
//		if(!((lu.getDept().getLcks().equals("????????????")) )){
//			model.addAttribute("error", "???????????????????????????????????????????????????");
//			return "common/proce";
//		}
		//???????????????????????????
		mmServices.getApplyDictionaries(model, userId, page, size);
		//????????????
		model.addAttribute("title", "I?????????????????????????????????");
		//?????????????????????
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			
			//???????????????????????????????????????
			Long proid=Long.parseLong(request.getParameter("id"));
			MedicalMattersList medicalMattersList=medicalMattersListDao.findbyMedicalmattersId(proid);
			NewPrjectOneClass newPrjectOneClass=newPrjectOneClassDao.findByMedicalmattersId(medicalMattersList);
			
			model.addAttribute("username", medicalMattersList.getUserId().getUserName());
			
			
			//???????????????????????????????????????,??????????????????
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
			String startTime=dateFormat.format(medicalMattersList.getStartTime());
			String endTime=dateFormat.format(medicalMattersList.getEndTime());
			
			model.addAttribute("newPrjectOneClass", newPrjectOneClass);
			model.addAttribute("medicalMattersList", medicalMattersList);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			//??????????????????????????????
			model.addAttribute("customername", customername);
			
			
		}
		return "medicalapply/newprojectoneclass";

	}
	
	/**
	 *????????????????????????
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws ParseException
	 */
	@RequestMapping("one-new-submit")
	public String meetingapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid NewPrjectOneClass newPrjectOneClass,BindingResult br,
									 @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException, ParseException{
		//???????????????
		User lu=udao.getOne(userId);
		System.out.println("lu:"+lu.getRealName());
		//??????????????????
		String val=req.getParameter("val");
		//???????????????
		String xzry=req.getParameter("nameuser");
		//????????????????????????????????????????????? ??????/???????????????????????? ?????????????????????????????????
		User shen = null;
		String[] xzrys = xzry.split(";");
		for(int i = 0 ;i<xzrys.length;i++) {
			if(xzrys[i].trim().length()>0) {
				shen=udao.findByUserName(xzrys[i].split("/")[0]);
			}
		}
		
		//?????????????????????
		if(StringUtil.objectIsNotNull(newPrjectOneClass.getOneClassId())) {
			//???????????????????????????,????????????????????????????????????ID
			NewPrjectOneClass oldNewPrjectOneClass=newPrjectOneClassDao.findById(newPrjectOneClass.getOneClassId()).orElse(null);
			//?????????????????????ID
			MedicalMattersList oldMMPro=oldNewPrjectOneClass.getMedicalmattersId();

			//?????????????????????
			oldMMPro.setProcessName(oldNewPrjectOneClass.getProjceName());
			oldMMPro.setStartTime(oldNewPrjectOneClass.getStartTime());
			oldMMPro.setEndTime(oldNewPrjectOneClass.getStartTime());
			User oldUserid=oldMMPro.getUserId();
			//??????????????????
			mmServices.setMedicalMatters(oldMMPro, val, lu,shen.getUserName());
			medicalMattersListDao.save(oldMMPro);

			//?????????????????????????????????
			newPrjectOneClass.setMedicalmattersId(oldMMPro);
			newPrjectOneClassDao.save(newPrjectOneClass);

			//?????????????????????,???????????????
			List<MedicalMattersReviewed> mmrev=mmredao.findByMedicalmattersId(oldMMPro.getMedicalmattersId());
			if(!Objects.isNull(mmrev)){
				mmredao.deleteAll(mmrev);
			}
			mmServices.setMedicalMattersReviewed(shen, oldMMPro);

//			sendMessageQYWX(oldPro,meeting.getHymc(),shen);

		}else {
			//??????????????????
			MedicalMattersList mmpro=newPrjectOneClass.getMedicalmattersId();
			mmpro.setProcessName(newPrjectOneClass.getProjceName());
			mmServices.setMedicalMatters(mmpro, val, lu,shen.getUserName());
			
			//??????????????????
			newPrjectOneClassDao.save(newPrjectOneClass);
			//????????????
			mmServices.setMedicalMattersReviewed(shen, mmpro);
			
			//sendMessageQYWX(pro,meeting.getHymc(),shen);
		}
		return "medicalapply/newprojectindex";

	}
	
}

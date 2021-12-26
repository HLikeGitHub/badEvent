package com.yjy.web.mms.controller.medicalmatters;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.mms.model.dao.attendcedao.AttendceDao;
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
import com.yjy.web.mms.model.entity.process.Meeting;
import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.process.ProcessService;

@Controller
@RequestMapping("/")
public class MedicalMattersApplyController {

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
	private SendMessageLogDao sendMessageLogDao;

	
	@Value("${attachment.roopath}")
	private String rootpath;

	@Value("${attachment.httppath}")
	private String httppath;

	@Value("${moblie.url}")
	private String moblieurl;

	@Value("${customer.name}")
	private String customername;
	
	
	//选择申请大类页面
	@RequestMapping("medical-apply")
	public String medicalapply(){
		//return "process/procedure";
		return "medicalapply/medicalindex";
	}

	//选择具体申请页面
	@RequestMapping("new-project-apply")
	public String newprojectapply(){
		//return "process/procedure";
		return "medicalapply/newprojectindex";
	}

}

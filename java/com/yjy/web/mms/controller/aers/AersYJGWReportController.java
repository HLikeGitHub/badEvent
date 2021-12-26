package com.yjy.web.mms.controller.aers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yjy.web.comm.utils.DateUtil;
import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.mms.controller.weixinqyh.MessageController;
import com.yjy.web.mms.model.dao.aersdao.AersDictionariesListDao;
import com.yjy.web.mms.model.dao.aersdao.AersPatientInfoDao;
import com.yjy.web.mms.model.dao.aersdao.AersReportProcessDao;
import com.yjy.web.mms.model.dao.aersdao.AersReportProcessReviewedDao;
import com.yjy.web.mms.model.dao.aersdao.AersYJGWReportProcessDao;
import com.yjy.web.mms.model.dao.aersdao.MedCareUnOpEventDao;
import com.yjy.web.mms.model.dao.aersdao.MedicalSafetyEventDao;
import com.yjy.web.mms.model.dao.processdao.SendMessageLogDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.aers.AersAllStatusCount;
import com.yjy.web.mms.model.entity.aers.AersDictionariesListEntity;
import com.yjy.web.mms.model.entity.aers.AersPatientInfoEntity;
import com.yjy.web.mms.model.entity.aers.AersReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.AersReportReviewedEntity;
import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.MedCareUnOpEventReportEntity;
import com.yjy.web.mms.model.entity.aers.MedicalSafetyEventReportEntity;
import com.yjy.web.mms.model.entity.attendce.Attends;
import com.yjy.web.mms.model.entity.note.Attachment;
import com.yjy.web.mms.model.entity.process.Bursement;
import com.yjy.web.mms.model.entity.process.DataQueryapply;
import com.yjy.web.mms.model.entity.process.Detailedlistapply;
import com.yjy.web.mms.model.entity.process.DetailsList;
import com.yjy.web.mms.model.entity.process.Evection;
import com.yjy.web.mms.model.entity.process.EvectionMoney;
import com.yjy.web.mms.model.entity.process.Holiday;
import com.yjy.web.mms.model.entity.process.LeaderReception;
import com.yjy.web.mms.model.entity.process.Mealapply;
import com.yjy.web.mms.model.entity.process.Meeting;
import com.yjy.web.mms.model.entity.process.Overtime;
import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.ProgramApply;
import com.yjy.web.mms.model.entity.process.Regular;
import com.yjy.web.mms.model.entity.process.Resign;
import com.yjy.web.mms.model.entity.process.Reviewed;
import com.yjy.web.mms.model.entity.process.SendMessageLog;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.aers.AersReportServices;

@Controller
@RequestMapping("/")
public class AersYJGWReportController {

	@Autowired
	private UserDao udao;
	
	@Autowired
	private DeptDao deptdao;
	
	@Autowired
	private AersReportProcessReviewedDao aersredao;
	
	@Autowired
	private SendMessageLogDao sendMessageLogDao;
	
	@Autowired
	private AersPatientInfoDao aersPatientInfoDao;
	
	
	@Autowired
	private AersReportServices aersServices;
	
	@Autowired
	private AersYJGWReportProcessDao aersYJGWReportProcessDao;
//	@Autowired
//	private MedCareUnOpEventDao medCareUnOpEventDao;
	
	@Autowired
	private AersDictionariesListDao aersDicDao;
	
	@Autowired
	private MedicalSafetyEventDao medicalSafetyEventDao;
	
	@Value("${attachment.roopath}")
	private String rootpath;

	@Value("${attachment.httppath}")
	private String httppath;

	@Value("${moblie.url}")
	private String moblieurl;

	@Value("${customer.name}")
	private String customername;
	
	
	//选择上报大类页面
	@RequestMapping("aers-yjgw-report")
	public String aersreport(){
		//return "process/procedure";
		return "aersyjgw/aersyjgwindex";
	}

	/**
	 *医疗安全事件新建、编辑
	 *
	 **/
	@RequestMapping("MedicalSafety")
	public String MedicalSafety(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size){
		User lu=udao.getOne(userId);//申请人
//		if(!((lu.getDept().getLcks().equals("职能科室")) )){
//			model.addAttribute("error", "您没有相关权限，请联系网络信息科！");
//			return "common/proce";
//		}
		//设置页面公用字典等内容
		aersServices.getAersDictionaries(model, userId, page, size);
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
		String applyTime=dateFormat.format(new Date());
		model.addAttribute("applyTime", applyTime);
		model.addAttribute("sqrUser", lu);
		//申请名称
		model.addAttribute("applyType", "医疗安全（不良）事件");
		
//		model.addAttribute("shUser", lu.getUserName()+"/"+lu.getRealName());
		
		String id = request.getParameter("id");
		//判断是否为编辑
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			
			//获取原有单据信息返回到前台
			Long proid=Long.parseLong(request.getParameter("id"));
			AersYJGWReportProcessEntity aersReportProcessEntity=aersYJGWReportProcessDao.findbyAersreportId(proid);
			MedicalSafetyEventReportEntity medicalSafetyEventReportEntity=medicalSafetyEventDao.findByAersyjgwreportId(aersReportProcessEntity);
			
			model.addAttribute("medicalSafetyEventReportEntity", medicalSafetyEventReportEntity);
			model.addAttribute("aersReportProcessEntity", aersReportProcessEntity);
		
			
			//自动生成的日期格式无法指定,程序进行处理
			 dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
			String happenTime=dateFormat.format(aersReportProcessEntity.getHappenTime());
			model.addAttribute("happenTime", happenTime);
			
			//自动生成的日期格式无法指定,程序进行处理
			applyTime=dateFormat.format(aersReportProcessEntity.getApplyTime());
			model.addAttribute("applyTime", applyTime);
			
			//医疗安全事件 日期
//			String OutDate=dateFormat.format(medicalSafetyEventReportEntity.getOutDate());
//			String OpDate=dateFormat.format(medicalSafetyEventReportEntity.getFirstOpDate());
//			model.addAttribute("OutDate", OutDate);
//			model.addAttribute("OpDate", OpDate);
			System.out.println("id:"+String.valueOf(request.getParameter("id")));
			AersReportReviewedEntity aersReportReviewedEntity = aersredao.findByAersreportIdAndStutas(Long.valueOf(request.getParameter("id")),23L  );
			
			
//			model.addAttribute("opDept", medCareUnOpEventReportEntity.getOpDept().getDeptId()+"/"+medCareUnOpEventReportEntity.getOpDept().getDeptName());
			model.addAttribute("shUser", aersReportReviewedEntity.getUserId().getUserName()+"/"+aersReportReviewedEntity.getUserId().getRealName());
			
			
			
			//读取全局变量单位名称
			model.addAttribute("customername", customername);
			
			
		}
		return "aersyjgw/MedicalSafetyEventReport";

	}
	
	/**
	 *医疗安全事件提交
	 */
	@RequestMapping("MedicalSafetyReportSubmit")
	public String MedicalSafetyReportSubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid MedicalSafetyEventReportEntity medicalSafetyEventReportEntity,@Valid AersYJGWReportProcessEntity aersYJGWReportProcessEntity,
			AersPatientInfoEntity aersPatientInfoEntity,BindingResult br,
									 @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException, ParseException{
		//获取申请人
		User lu=udao.getOne(userId);
		//获取流程名称
		String applyType=req.getParameter("applyType");
		applyType = applyType.equals("")?"医疗安全（不良）事件":applyType;
//		String aersreportId=req.getParameter("aersreportId");
//		String medicalSafetyEventId=req.getParameter("medicalSafetyEventId");
//		System.out.println("aersreportId:"+aersreportId+"  medicalSafetyEventId:"+medicalSafetyEventId);
		System.out.println("提交后：" + aersPatientInfoEntity);
		//获取审核人
		String xzry=req.getParameter("nameuser");
		//获取手术科室
//		String namedept=req.getParameter("namedept");
		//获取提交类型
		String submitType=req.getParameter("submitType");
		
		System.out.println("submitType:"+submitType);
		if(!StringUtil.stringIsNotNull(xzry)){
			model.addAttribute("error", "审核人不能为空！");
			return "common/proce";
		}
//		if(!StringUtil.stringIsNotNull(namedept)){
//			model.addAttribute("error", "审核科室不能为空！");
//			return "common/proce";
//		}
		
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = null;
		String[] xzrys = xzry.split(";");
		for(int i = 0 ;i<xzrys.length;i++) {
			if(xzrys[i].trim().length()>0) {
				shen=udao.findByUserName(xzrys[i].split("/")[0]);
			}
		}
		
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
//		Dept opdept = null;
//		String[] namedepts = namedept.split(";");
//		for(int i = 0 ;i<namedepts.length;i++) {
//			if(namedepts[i].trim().length()>0) {
//				opdept=deptdao.findbydeptName(namedepts[i].split("/")[0]);
//			}
//		}
		
		
		
		
		//判断是否为编辑
		if(StringUtil.objectIsNotNull(medicalSafetyEventReportEntity.getAersyjgwreportId())) {
			//找到原本的接待记录,因为新提交的接待没有流程ID
			MedicalSafetyEventReportEntity oldMedicalSafetyEventReportEntity=medicalSafetyEventDao.findById(medicalSafetyEventReportEntity.getMedicalSafetyEventId()).orElse(null);
			//找到对应的流程ID
			AersYJGWReportProcessEntity oldAersYJGWReportProcessEntity=oldMedicalSafetyEventReportEntity.getAersyjgwreportId();

			oldAersYJGWReportProcessEntity.setLevel(aersYJGWReportProcessEntity.getLevel());
			AersPatientInfoEntity aersPatientInfo=aersPatientInfoEntity;
			//更新流程信息表
//			oldAersReportProcessEntity.setProcessName(oldMedCareUnOpEventReportEntity.getProjceName());
//			oldAersReportProcessEntity.setStartTime(oldMedCareUnOpEventReportEntity.getStartTime());
//			oldAersReportProcessEntity.setEndTime(oldMedCareUnOpEventReportEntity.getStartTime());
//			User oldUserid=oldAersReportProcessEntity.getUserId();
			//保存流程信息
			aersServices.setYJGWAersReportProcess(oldAersYJGWReportProcessEntity, applyType, lu,shen.getUserName());
			aersYJGWReportProcessDao.save(oldAersYJGWReportProcessEntity);

			//设置流程信息到接待记录
			medicalSafetyEventReportEntity.setAersyjgwreportId(oldAersYJGWReportProcessEntity);
//			medicalSafetyEventReportEntity.setOpDept(opdept);
			medicalSafetyEventDao.save(medicalSafetyEventReportEntity);

			//先删除审核记录,再存审核表
			List<AersReportReviewedEntity> aersrev=aersredao.findByAersreportId(oldAersYJGWReportProcessEntity.getAersreportId());
			if(!Objects.isNull(aersrev)){
				aersredao.deleteAll(aersrev);
			}
			
			
			//先患者信息,再存审核表
//			if(StringUtil.objectIsNotNull(oldAersYJGWReportProcessEntity.getPatinfoId())) {
//				aersPatientInfoDao.delete(oldAersYJGWReportProcessEntity.getPatinfoId());
//			}
			
			//保存患者信息表
			aersPatientInfoDao.save(aersPatientInfo);
			oldAersYJGWReportProcessEntity.setPatinfoId(aersPatientInfo);
			
			
			
			aersServices.setAersReportReviewed(shen, oldAersYJGWReportProcessEntity);

//			sendMessageQYWX(oldPro,meeting.getHymc(),shen);

		}else {
			AersPatientInfoEntity aersPatientInfo=aersPatientInfoEntity;
			//保存流程信息
			AersYJGWReportProcessEntity aerspro=aersYJGWReportProcessEntity;
//			aerspro.setProcessName(medCareUnOpEventReportEntity.getProjceName());
			aersServices.setYJGWAersReportProcess(aerspro, applyType, lu,shen.getUserName());
			
			
			//保存患者信息表
			aersPatientInfoDao.save(aersPatientInfo);
			aerspro.setPatinfoId(aersPatientInfo);
			
			//保存流程主表
			aersYJGWReportProcessDao.save(aerspro);
			
			
//			medCareUnOpEventReportEntity.setOpDept(opdept);
			//流程主表与医疗安全关联
			medicalSafetyEventReportEntity.setAersyjgwreportId(aerspro);
			//医疗安全申请
			medicalSafetyEventDao.save(medicalSafetyEventReportEntity);
			//存审核表
			aersServices.setAersReportReviewed(shen, aerspro);
			//sendMessageQYWX(pro,meeting.getHymc(),shen);
		}
		return "redirect:/aersmyapply";

	}

	/**
	 * 删除申请单
	 */
	@RequestMapping("aersprocessdelete")
	public String processdelete(HttpServletRequest req,@SessionAttribute("userId") Long userId,Model model){
		Long proid=Long.parseLong(req.getParameter("id"));
		AersYJGWReportProcessEntity process=aersYJGWReportProcessDao.findbyAersreportId(proid);
		User lu=udao.getOne(userId);//申请人
//		if ( process.getApplyType().equals("医疗安全（不良）事件")) {
//			
//			if(!StringUtil.stringIsNotNull(String.valueOf(lu.getIs_hys()))|| (lu.getIs_hys()!=1) ){
//				model.addAttribute("error", "您没有相关权限，请联系会议室管理员进行删除！");
//				return "common/proce";
//			}
//		}
		
		//先患者信息,再存审核表
		if(StringUtil.objectIsNotNull(process.getPatinfoId())) {
			AersPatientInfoEntity patinfoId = process.getPatinfoId();
			process.setPatinfoId(null);
			aersYJGWReportProcessDao.save(process);
			aersPatientInfoDao.delete(patinfoId);
		}
		
		//删除审核记录
		List<AersReportReviewedEntity> rev=aersredao.findByAersreportId(proid);
		if(!Objects.isNull(rev)){
			aersredao.deleteAll(rev);
		}
//		//删除附件
//		List<Attachment>attList=AttDao.findAttachment(proid);
//		if(!Objects.isNull(attList)){
//			AttDao.deleteInBatch(attList);
//		}
		if( process.getApplyType().equals("医疗安全（不良）事件")) {
			//删除公务接单申请单
			MedicalSafetyEventReportEntity medicalSafety=medicalSafetyEventDao.findByAersyjgwreportId(process);
			if(!Objects.isNull(medicalSafety)){
				medicalSafetyEventDao.delete(medicalSafety);
			}

		}
		aersYJGWReportProcessDao.delete(process);
		return "redirect:/aersmyapply";

	}	
	
	/**
	 * 审核确定的页面
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("susave")
	public String save(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,AersReportReviewedEntity reviewed) throws ParseException, IOException{
		User u=udao.getOne(userId);
		String name=null;
		String typename=req.getParameter("type");
		Long proid=Long.parseLong(req.getParameter("proId"));


		AersYJGWReportProcessEntity pro=aersYJGWReportProcessDao.findbyAersreportId(proid);//找到该条流程

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
			if(("医疗安全（不良）事件").equals(typename)){
				if(u2==null) {
					model.addAttribute("error", "请审核人员");
					return "common/proce";
				}
				if(u.getPosition().getId().equals(6L)) {//科员
					if(u2.getPosition().getId().equals(5L)){
						aersServices.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择科长。");
						return "common/proce";
					}
				}else if(u.getPosition().getId().equals(5L)) {//科长
//					if(u2.getPosition().getId().equals(5L)){
					aersServices.save(proid, u, reviewed, pro, u2);
//					}else{
//						model.addAttribute("error", "请选择办公室办事员。");
//						return "common/proce";
//					}
				}else if(u.getPosition().getId().equals(4L)) {//主管科室
					if(u2.getPosition().getId().equals(3L)){
						aersServices.save(proid, u, reviewed, pro, u2);
					}else{
						model.addAttribute("error", "请选择主管科室科长。");
						return "common/proce";
					}
				}

				MedicalSafetyEventReportEntity mealApply = medicalSafetyEventDao.findByAersyjgwreportId(pro);
//				sendMessageQYWX(pro,mealApply.getLbdw()+","+mealApply.getLfsy(),u2);
//				if(u2.getPosition().getId().equals(5L)){
//					proservice.save(proid, u, reviewed, pro, u2);
//				}else{
//					model.addAttribute("error", "请选财务经理。");
//					return "common/proce";
//				}
			}
			else{
//				if(u2.getPosition().getId().equals(7L)){
//					proservice.save(proid, u, reviewed, pro, u2);
//				}else{
//					model.addAttribute("error", "请选择正确人员审核！");
//					return "common/proce";
//				}
			}

		}else{
			//审核并结案
			AersReportReviewedEntity re=aersredao.findByProIdAndUserId(proid,u);
			re.setAdvice(reviewed.getAdvice());
			re.setStatusId(reviewed.getStatusId());
			re.setReviewedTime(new Date());
			aersredao.save(re);
			pro.setStatus("已完成");//改变主表的状态
			aersYJGWReportProcessDao.save(pro);


		}


//		 if(("公务接待").equals(typename)){
//			Mealapply meal=mealApplyDao.findByProId(pro);
////			if(shen.getFatherId().equals(u.getUserId())){
////				meal.setManagerAdvice(reviewed.getAdvice());
////				mealApplyDao.save(meal);
////			}
//			if(u.getPosition().getId().equals(2L)){
//				meal.setYzdwsjAdvice(reviewed.getAdvice()+","+u.getRealName());
//				mealApplyDao.save(meal);
//			}else if(u.getPosition().getId().equals(3L)){
//				meal.setZgldAdvice(reviewed.getAdvice()+","+u.getRealName());
//				mealApplyDao.save(meal);
//			}else if(u.getPosition().getId().equals(4L)){
//				meal.setBgszrAdvice(reviewed.getAdvice()+","+u.getRealName());
//				mealApplyDao.save(meal);
//			}else if(u.getPosition().getId().equals(5L)){
//				meal.setBgsbsyAdvice(reviewed.getAdvice()+","+u.getRealName());
//				mealApplyDao.save(meal);
//			}else if(u.getPosition().getId().equals(6L)){
//				meal.setKskzAdvice(reviewed.getAdvice()+","+u.getRealName());
//				mealApplyDao.save(meal);
//			}
//		}
		return "redirect:/aersmyaudit";

	}
	
	
}

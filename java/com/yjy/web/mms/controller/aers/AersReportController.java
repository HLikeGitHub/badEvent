package com.yjy.web.mms.controller.aers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.yjy.web.mms.model.dao.aersdao.AersDictionariesListDao;
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
import com.yjy.web.mms.model.entity.aers.AersReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.AersReportReviewedEntity;
import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;
import com.yjy.web.mms.model.entity.aers.MedCareUnOpEventReportEntity;
import com.yjy.web.mms.model.entity.aers.MedicalSafetyEventReportEntity;
import com.yjy.web.mms.model.entity.process.AubUser;
import com.yjy.web.mms.model.entity.process.Bursement;
import com.yjy.web.mms.model.entity.process.DataQueryapply;
import com.yjy.web.mms.model.entity.process.Detailedlistapply;
import com.yjy.web.mms.model.entity.process.DetailsBurse;
import com.yjy.web.mms.model.entity.process.DetailsList;
import com.yjy.web.mms.model.entity.process.Evection;
import com.yjy.web.mms.model.entity.process.EvectionMoney;
import com.yjy.web.mms.model.entity.process.Holiday;
import com.yjy.web.mms.model.entity.process.Mealapply;
import com.yjy.web.mms.model.entity.process.Overtime;
import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.ProgramApply;
import com.yjy.web.mms.model.entity.process.Regular;
import com.yjy.web.mms.model.entity.process.Resign;
import com.yjy.web.mms.model.entity.process.Reviewed;
import com.yjy.web.mms.model.entity.process.Stay;
import com.yjy.web.mms.model.entity.process.Traffic;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.aers.AersReportServices;
import com.yjy.web.mms.services.process.ProcessService;

@Controller
@RequestMapping("/")
public class AersReportController {

	@Autowired
	private UserDao udao;
	
	@Autowired
	private DeptDao deptdao;
	
	@Autowired
	private AersReportProcessReviewedDao aersredao;
	
	@Autowired
	private MedicalSafetyEventDao medicalSafetyEventDao;
	
	@Autowired
	private SendMessageLogDao sendMessageLogDao;

	@Autowired
	private AersReportServices aersServices;
	
	@Autowired
	private AersReportProcessDao aersProcessDao;
	@Autowired
	private MedCareUnOpEventDao medCareUnOpEventDao;
	
	@Autowired
	private AersDictionariesListDao aersDicDao;
	
	@Autowired
	private AersYJGWReportProcessDao aersYJGWReportProcessDao;
	
	@Value("${attachment.roopath}")
	private String rootpath;

	@Value("${attachment.httppath}")
	private String httppath;

	@Value("${moblie.url}")
	private String moblieurl;

	@Value("${customer.name}")
	private String customername;
	
	
	//????????????????????????
	@RequestMapping("aers-report")
	public String aersreport(){
		//return "process/procedure";
		return "aers/aersindex";
	}

	/*
	 * ???????????????
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 */
	@RequestMapping("medicalcare-report")
	public String medicalcarereport(){
		//return "process/procedure";
		return "aers/medicalcareindex";
	}
	
	/*
	 * ???????????????
	 * ??????????????????????????????????????????????????????
	 */
		@RequestMapping("medicaltechnology-report")
		public String medicaltechnologyreport(){
			//return "process/procedure";
			return "aers/medicaltechnologyindex";
		}
	/*
	 * ???????????????
	 * ???????????????????????????????????????
	 */
	@RequestMapping("complaint-report")
	public String complaintreport(){
		//return "process/procedure";
		return "aers/complaintindex";
	}

	/**
	 *??????????????????????????????????????????
	 *
	 **/
	@RequestMapping("MedCareUnOpEventReport")
	public String MedCareUnOpEventReport(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size){
//		User lu=udao.getOne(userId);//?????????
////		if(!((lu.getDept().getLcks().equals("????????????")) )){
////			model.addAttribute("error", "???????????????????????????????????????????????????");
////			return "common/proce";
////		}
//		//?????????????????????????????????
//		aersServices.getAersDictionaries(model, userId, page, size);
//		
//		//?????????????????????????????????
//		AersDictionariesListEntity technologyReasonDic=aersDicDao.findByAersdictionarieName("??????????????????(??????),????????????");
//		List<Map<String, Object>> technologyReasonList=JSON.parseObject(technologyReasonDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
//		model.addAttribute("technologyReasonList", technologyReasonList);
//		
//		AersDictionariesListEntity anaesthesiaReasonDic=aersDicDao.findByAersdictionarieName("??????????????????(??????),????????????");
//		List<Map<String, Object>> anaesthesiaReasonList=JSON.parseObject(anaesthesiaReasonDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
//		model.addAttribute("anaesthesiaReasonList", anaesthesiaReasonList);
//		
//		AersDictionariesListEntity nisReasonDic=aersDicDao.findByAersdictionarieName("??????????????????(??????),????????????");
//		List<Map<String, Object>> nisReasonList=JSON.parseObject(nisReasonDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
//		model.addAttribute("nisReasonList", nisReasonList);
//		
//		AersDictionariesListEntity otherReasonDic=aersDicDao.findByAersdictionarieName("??????????????????(??????),????????????");
//		List<Map<String, Object>> otherReasonList=JSON.parseObject(otherReasonDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
//		model.addAttribute("otherReasonList", otherReasonList);
//		
//		
//		
//		
//		//????????????
//		model.addAttribute("title", "???????????????????????????");
//		//?????????????????????
//		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
//			
//			//???????????????????????????????????????
//			Long proid=Long.parseLong(request.getParameter("id"));
//			AersReportProcessEntity aersReportProcessEntity=aersProcessDao.findbyAersreportId(proid);
//			MedCareUnOpEventReportEntity medCareUnOpEventReportEntity=medCareUnOpEventDao.findByAersreportId(aersReportProcessEntity);
//			
//			model.addAttribute("medCareUnOpEventReportEntity", medCareUnOpEventReportEntity);
//			model.addAttribute("aersReportProcessEntity", aersReportProcessEntity);
//			
//			
//			//???????????????????????????????????????,??????????????????
//			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
//			String happenTime=dateFormat.format(aersReportProcessEntity.getHappenTime());
//			model.addAttribute("happenTime", happenTime);
//			
//			//??????????????????????????? ??????
//			String OutDate=dateFormat.format(medCareUnOpEventReportEntity.getOutDate());
//			String OpDate=dateFormat.format(medCareUnOpEventReportEntity.getFirstOpDate());
//			model.addAttribute("OutDate", OutDate);
//			model.addAttribute("OpDate", OpDate);
//			AersReportReviewedEntity aersReportReviewedEntity = aersredao.findByAersreportIdAndStutas(Long.valueOf(request.getParameter("id")),23L  );
//			
//			
//			model.addAttribute("opDept", medCareUnOpEventReportEntity.getOpDept().getDeptId()+"/"+medCareUnOpEventReportEntity.getOpDept().getDeptName());
//			model.addAttribute("shUser", aersReportReviewedEntity.getUserId().getUserName()+"/"+aersReportReviewedEntity.getUserId().getRealName());
//			
//			
//			
//			//??????????????????????????????
//			model.addAttribute("customername", customername);
//			
//			
//		}
		return "aers/MedCareUnOpEventReport";

	}
	
	/**
	 *?????????????????????????????????
	 */
	@RequestMapping("MedCareUnOpEventReportSubmit")
	public String MedCareUnOpEventReportSubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid MedCareUnOpEventReportEntity medCareUnOpEventReportEntity,@Valid AersReportProcessEntity aersReportProcessEntity,BindingResult br,
									 @SessionAttribute("userId") Long userId,Model model) throws IllegalStateException, IOException, ParseException{
//		//???????????????
//		User lu=udao.getOne(userId);
//		//??????????????????
//		String applyType=req.getParameter("applyType");
//		applyType = applyType.equals("")?"???????????????????????????":applyType;
//		
//		//???????????????
//		String xzry=req.getParameter("nameuser");
//		//??????????????????
//		String namedept=req.getParameter("namedept");
//		//??????????????????
//		String submitType=req.getParameter("submitType");
//		
//		System.out.println("submitType:"+submitType);
//		if(!StringUtil.stringIsNotNull(xzry)){
//			model.addAttribute("error", "????????????????????????");
//			return "common/proce";
//		}
//		if(!StringUtil.stringIsNotNull(namedept)){
//			model.addAttribute("error", "???????????????????????????");
//			return "common/proce";
//		}
//		
//		//????????????????????????????????????????????? ??????/???????????????????????? ?????????????????????????????????
//		User shen = null;
//		String[] xzrys = xzry.split(";");
//		for(int i = 0 ;i<xzrys.length;i++) {
//			if(xzrys[i].trim().length()>0) {
//				shen=udao.findByUserName(xzrys[i].split("/")[0]);
//			}
//		}
//		
//		//????????????????????????????????????????????? ??????/???????????????????????? ?????????????????????????????????
//		Dept opdept = null;
//		String[] namedepts = namedept.split(";");
//		for(int i = 0 ;i<namedepts.length;i++) {
//			if(namedepts[i].trim().length()>0) {
//				opdept=deptdao.findbydeptName(namedepts[i].split("/")[0]);
//			}
//		}
//		
//		
//		
//		
//		//?????????????????????
//		if(StringUtil.objectIsNotNull(medCareUnOpEventReportEntity.getMedCareUnOpEventId())) {
//			//???????????????????????????,????????????????????????????????????ID
//			MedCareUnOpEventReportEntity oldMedCareUnOpEventReportEntity=medCareUnOpEventDao.findById(medCareUnOpEventReportEntity.getMedCareUnOpEventId()).orElse(null);
//			//?????????????????????ID
//			AersReportProcessEntity oldAersReportProcessEntity=oldMedCareUnOpEventReportEntity.getAersreportId();
//
//			//?????????????????????
////			oldAersReportProcessEntity.setProcessName(oldMedCareUnOpEventReportEntity.getProjceName());
////			oldAersReportProcessEntity.setStartTime(oldMedCareUnOpEventReportEntity.getStartTime());
////			oldAersReportProcessEntity.setEndTime(oldMedCareUnOpEventReportEntity.getStartTime());
////			User oldUserid=oldAersReportProcessEntity.getUserId();
//			//??????????????????
//			aersServices.setAersReportProcess(oldAersReportProcessEntity, applyType, lu,shen.getUserName());
//			aersProcessDao.save(oldAersReportProcessEntity);
//
//			//?????????????????????????????????
//			medCareUnOpEventReportEntity.setAersreportId(oldAersReportProcessEntity);
//			medCareUnOpEventReportEntity.setOpDept(opdept);
//			medCareUnOpEventDao.save(medCareUnOpEventReportEntity);
//
//			//?????????????????????,???????????????
//			List<AersReportReviewedEntity> aersrev=aersredao.findByAersreportId(oldAersReportProcessEntity.getAersreportId());
//			if(!Objects.isNull(aersrev)){
//				aersredao.deleteAll(aersrev);
//			}
////			aersServices.setAersReportReviewed(shen, oldAersReportProcessEntity);
//
////			sendMessageQYWX(oldPro,meeting.getHymc(),shen);
//
//		}else {
//			//??????????????????
//			AersReportProcessEntity aerspro=aersReportProcessEntity;
////			aerspro.setProcessName(medCareUnOpEventReportEntity.getProjceName());
//			aersServices.setAersReportProcess(aerspro, applyType, lu,shen.getUserName());
//			//??????????????????
//			aersProcessDao.save(aerspro);
//			medCareUnOpEventReportEntity.setOpDept(opdept);
//			medCareUnOpEventReportEntity.setAersreportId(aerspro);
//			//?????????????????????????????????
//			medCareUnOpEventDao.save(medCareUnOpEventReportEntity);
//			//????????????
////			aersServices.setAersReportReviewed(shen, aerspro);
//			//sendMessageQYWX(pro,meeting.getHymc(),shen);
//		}
		return "aers/medicalcareindex";

	}
	
	/*
	 * ??????????????????????????????
	 */
	@RequestMapping("aersmyapply")
	public String flowManage(@SessionAttribute("userId") Long userId,Model model,
			 HttpServletRequest req,
			 @RequestParam(value = "page", defaultValue = "0") int page,
			 @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException{
		/*Pageable pa=PageRequest.of(page, size);
		User user=udao.findByUserId(userId).get(0);
		model.addAttribute("title", "??????");
		//??????????????????
		String status=StringUtil.stringIsNotNull(req.getParameter("status"))? req.getParameter("status") : "";
		status = status.equals("")?"??????":status;
		//????????????
		String startDate=StringUtil.stringIsNotNull(req.getParameter("startDate")) ? req.getParameter("startDate") : "";
		startDate=startDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), -30),"yyyy-MM-dd"):startDate;
		
		//????????????
		String endDate=StringUtil.stringIsNotNull(req.getParameter("endDate"))? req.getParameter("endDate") : "";
		endDate=endDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), 1),"yyyy-MM-dd"):endDate;
		
		
		//????????????
		String eventLevel=StringUtil.stringIsNotNull(req.getParameter("eventLevel"))? req.getParameter("eventLevel") : "";
		eventLevel = eventLevel.equals("")?"??????":eventLevel;
		
		//????????????/??????
		String eventCondition=StringUtil.stringIsNotNull(req.getParameter("eventCondition"))? req.getParameter("eventCondition") : "";
		
		
		//???????????????????????????
//		AersDictionariesListEntity emergencyDic=aersDicDao.findByAersdictionarieName("????????????????????????");
//		List<Map<String, Object>> emergencyList=JSON.parseObject(emergencyDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
//		model.addAttribute("emergencyList", emergencyList);
		//????????????????????????
		AersDictionariesListEntity levelDic=aersDicDao.findByAersdictionarieName("?????????????????????");
		List<Map<String, Object>> levelList=JSON.parseObject(levelDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("levelList", levelList);
		//????????????????????????????????????
		AersDictionariesListEntity reportStatusDic=aersDicDao.findByAersdictionarieName("??????????????????????????????");
		List<Map<String, Object>> reportStatusList=JSON.parseObject(reportStatusDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("reportStatusList", reportStatusList);
				
				
		
		//?????????????????????????????????
		Map<String,Object> allCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"??????",eventLevel, eventCondition);
		Map<String,Object> DTJCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"?????????",eventLevel, eventCondition);
		Map<String,Object> YTJCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"?????????",eventLevel, eventCondition);
		Map<String,Object> YWCCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"?????????",eventLevel, eventCondition);
		Map<String,Object> HTCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"??????",eventLevel, eventCondition);
		Map<String,Object> ZFCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"??????",eventLevel, eventCondition);
		
		
		
		//System.out.println("myReportCount:"+myReportCount.get("allCount").toString());
		model.addAttribute("allCount", allCount);
		model.addAttribute("DTJCount", DTJCount);
		model.addAttribute("YTJCount", YTJCount);
		model.addAttribute("YWCCount", YWCCount);
		model.addAttribute("HTCount", HTCount);
		model.addAttribute("ZFCount", ZFCount);
		//?????????????????????????????????
		Page<Map<String, Object>> pagelist=aersProcessDao.queryApplyPaByUserId(userId
				,DateUtil.getStrtoDate(startDate),DateUtil.getStrtoDate(endDate)
				,status,eventLevel,eventCondition
				,pa);
		List<Map<String, Object>> prolist=pagelist.getContent();
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);


		for(int i = 0 ;i<prolist.size();i++) {
			String shry=prolist.get(i).get("shenuser").toString();

			String [] shrys=shry.split(";");
			String shryZW="";
			for(int j=0;j<shrys.length;j++) {
				shryZW=shryZW+udao.findByUserName(shrys[j]).getRealName()+";";
			}
			//prolist.get(i).setShenuser(shryZW);
		}
		model.addAttribute("url", "shenser");
		
		model.addAttribute("status", status);
		model.addAttribute("eventLevel", eventLevel);
		model.addAttribute("eventCondition", eventCondition);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		*/
		return "aers/aersmyapply";
	}
	/*
	 * ??????????????????????????????
	 */
	@RequestMapping("aersmyaudit")
	public String aersmyaudit(@SessionAttribute("userId") Long userId,Model model,
			 HttpServletRequest req,
			 @RequestParam(value = "page", defaultValue = "0") int page,
			 @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException{
		Pageable pa=PageRequest.of(page, size);
		User user=udao.findByUserId(userId).get(0);	
		model.addAttribute("title", "??????");
		
//		Page<AubUser> pagelist=aersServices.index(user, page, size,null,model);
//		List<Map<String, Object>> prolist=aersServices.index2(pagelist,user);
//		model.addAttribute("page", pagelist);
//		model.addAttribute("prolist", prolist);
//		model.addAttribute("url", "serch");
//		model.addAttribute("ryzb", StringUtil.stringIsNotNull(user.getRyzb())?user.getRyzb():""  );
		
		
		
		
		
		//??????????????????
		String status=StringUtil.stringIsNotNull(req.getParameter("status"))? req.getParameter("status") : "";
		status = status.equals("")?"??????":status;
		String statusL="23";
		if (status.equals("?????????")) {
			statusL="23";
		}
        if (status.equals("?????????")) {
        	statusL="25";
		}
        if (status.equals("??????")) {
        	statusL="0";
		}
        
        
		//????????????
		String startDate=StringUtil.stringIsNotNull(req.getParameter("startDate")) ? req.getParameter("startDate") : "";
		startDate=startDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), -30),"yyyy-MM-dd"):startDate;
		
		//????????????
		String endDate=StringUtil.stringIsNotNull(req.getParameter("endDate"))? req.getParameter("endDate") : "";
		endDate=endDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), 1),"yyyy-MM-dd"):endDate;
		
		
		//????????????
		String eventLevel=StringUtil.stringIsNotNull(req.getParameter("eventLevel"))? req.getParameter("eventLevel") : "";
		eventLevel = eventLevel.equals("")?"??????":eventLevel;
		
		//????????????/??????
		String eventCondition=StringUtil.stringIsNotNull(req.getParameter("eventCondition"))? req.getParameter("eventCondition") : "";
		
		
		//???????????????????????????
//		AersDictionariesListEntity emergencyDic=aersDicDao.findByAersdictionarieName("????????????????????????");
//		List<Map<String, Object>> emergencyList=JSON.parseObject(emergencyDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
//		model.addAttribute("emergencyList", emergencyList);
		//????????????????????????
		//????????????????????????
		AersDictionariesListEntity levelDic=aersDicDao.findByAersdictionarieName("?????????????????????");
		List<Map<String, Object>> levelList=JSON.parseObject(levelDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("levelList", levelList);
		//????????????????????????????????????
		AersDictionariesListEntity reportStatusDic=aersDicDao.findByAersdictionarieName("??????????????????????????????");
		List<Map<String, Object>> reportStatusList=JSON.parseObject(reportStatusDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("reportStatusList", reportStatusList);
				
				
		
		//?????????????????????????????????
		Map<String,Object> allCount=aersProcessDao.queryAuditCountByUserId(user.getUserName().toString(),DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"0",eventLevel, eventCondition);
		Map<String,Object> DSHCount=aersProcessDao.queryAuditCountByUserId(user.getUserName().toString(),DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"23",eventLevel, eventCondition);
		Map<String,Object> YSHCount=aersProcessDao.queryAuditCountByUserId(user.getUserName().toString(),DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
				"25",eventLevel, eventCondition);
//		Map<String,Object> YWCCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
//				"?????????",eventLevel, eventCondition);
//		Map<String,Object> HTCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
//				"??????",eventLevel, eventCondition);
//		Map<String,Object> ZFCount=aersProcessDao.queryCountByUserId(userId,DateUtil.getStrtoDate(startDate), DateUtil.getStrtoDate(endDate), 
//				"??????",eventLevel, eventCondition);
		
		
		
		//System.out.println("myReportCount:"+myReportCount.get("allCount").toString());
		model.addAttribute("allCount", allCount);
		model.addAttribute("DSHCount", DSHCount);
		model.addAttribute("YSHCount", YSHCount);
//		model.addAttribute("YWCCount", YWCCount);
//		model.addAttribute("HTCount", HTCount);
//		model.addAttribute("ZFCount", ZFCount);
		//?????????????????????????????????
//		List<Map<String, Object>> prolist=aersProcessDao.queryAuditByUserId(user.getUserName().toString()
//				,DateUtil.getStrtoDate(startDate),DateUtil.getStrtoDate(endDate)
//				,statusL,eventLevel,eventCondition
//				);
//		List<AersYJGWReportProcessEntity> prolist=pagelist.getContent();
		
//		Page<AubUser> pagelist=aersServices.index(user, page, size,null,model);
//		List<Map<String, Object>> prolist=aersServices.index2(pagelist,user);
		//?????????????????????????????????
		Page<Map<String, Object>> pagelist=aersProcessDao.queryAuditPaByUserId(user.getUserName().toString(),DateUtil.getStrtoDate(startDate),DateUtil.getStrtoDate(endDate)
				,statusL,eventLevel,eventCondition
				,pa);
		List<Map<String, Object>> prolist=pagelist.getContent();
		
		
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		


		for(int i = 0 ;i<prolist.size();i++) {
			String shry=prolist.get(i).get("shenuser").toString();

			String [] shrys=shry.split(";");
			String shryZW="";
			for(int j=0;j<shrys.length;j++) {
				shryZW=shryZW+udao.findByUserName(shrys[j]).getRealName()+";";
			}
//			pagelist.get(i).put("shenuser", shryZW);   //setShenuser(shryZW);
		}
		model.addAttribute("url", "shenser");
		
		model.addAttribute("status", status);
		model.addAttribute("eventLevel", eventLevel);
		model.addAttribute("eventCondition", eventCondition);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		return "aers/aersmyaudit";
	}

	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("aersauditing")
	public String aersauditing(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size){
		User u=udao.getOne(userId);

		//??????id
		Long proid=Long.parseLong(req.getParameter("id"));
		AersYJGWReportProcessEntity process=aersYJGWReportProcessDao.findbyAersreportId(proid);
		AersReportReviewedEntity re=aersredao.findByProIdAndUserId(process.getAersreportId(), u);//???????????????
		String typename=process.getApplyType().trim();
		int ishighmoney=0;
		if(("??????????????????????????????").equals(typename)){
			AersYJGWReportProcessEntity bu=aersProcessDao.findbyAersreportId(process.getAersreportId());
			model.addAttribute("bu", bu);
			//proservice.user(page, size, model);
			aersServices.indexMedicalSafety(model, userId, page, size,0L);
		}
		List<Map<String, Object>> list=aersServices.aersindex4(process);
//		model.addAttribute("statusid", process.getStatusId());
		model.addAttribute("process", process);
		model.addAttribute("revie", list);
		model.addAttribute("size", list.size());
		model.addAttribute("status", process.getStatus());
//		if(StringUtil.stringIsNotNull(u.getRyzb())&&(u.getRyzb().equals("?????????")||u.getRyzb().equals("?????????"))) {
//			model.addAttribute("ustatusid", process.getStatusId());
//
//		}else {
			model.addAttribute("ustatusid", re.getStatusId());
//		}
		model.addAttribute("positionid",u.getPosition().getId());
		model.addAttribute("typename", typename);
		model.addAttribute("ishighmoney", ishighmoney);
		model.addAttribute("userName", u.getUserName().toString());
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(u.getRyzb())?u.getRyzb():""  );
		
		return "aers/audetail";

	}
	
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("aersSerch")
	public String aersSerch(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req){
		User user=udao.getOne(userId);//????????????????????????
		User audit=null;//???????????????
		String id=req.getParameter("id");
		Long proid=Long.parseLong(id);
		
		String name=null;

		Map<String, Object> map=new HashMap<>();
		AersYJGWReportProcessEntity process=aersProcessDao.findById(proid).orElse(null);//??????????????????
		Boolean flag=process.getUserId().getUserId().equals(userId);//?????????????????????????????????
		String typename=process.getApplyType().toString();//????????????
		map=aersServices.index3(name,user,typename,process);
		model.addAttribute("process", process);
		
		if(("??????????????????????????????").equals(typename)){
			MedicalSafetyEventReportEntity medicalSafetyEvent=medicalSafetyEventDao.findByAersyjgwreportId(process);

//			List<ProcessList> processLists = new ArrayList<ProcessList>();
//			for(int i=0;i<medicalSafetyEvent.getDetailedlistapplys().size();i++) {
//				processLists.add(aersProcessDao.findbyuseridandtitle( eve.getDetailedlistapplys().get(i).getProId().getProcessId()));
//			}


//			String type=tydao.findname(eve.getTypeId());
			if(StringUtil.stringIsNotNull(process.getDeptManager())) {
				model.addAttribute("deptManager", process.getDeptManager().toString().split(",")[1]);
			}else {
				
				model.addAttribute("deptManager", process.getUserId().getRealName());
			}
			if(StringUtil.stringIsNotNull(process.getChargeManager())) {
				model.addAttribute("chargeManager", process.getChargeManager().toString().split(",")[1]);
			}else {
				model.addAttribute("chargeManager", "");
			}
			if(StringUtil.stringIsNotNull(process.getQualityManager())) {
				model.addAttribute("qualityManager", process.getQualityManager().toString().split(",")[1]);
			}else {
				model.addAttribute("qualityManager", "");
			}
			model.addAttribute("medicalSafetyEvent", medicalSafetyEvent);
//				model.addAttribute("Detailedlistapplys",eve.getDetailedlistapplys());
//			model.addAttribute("prolist",processLists);
			model.addAttribute("map", map);
//			model.addAttribute("type", type);
			model.addAttribute("attachmentList",map.get("attachmentList"));
			return "aersyjgw/medicalsafetyserch";
		}



		return "aersyjgw/medicalsafetyserch";
	}
	
	
}

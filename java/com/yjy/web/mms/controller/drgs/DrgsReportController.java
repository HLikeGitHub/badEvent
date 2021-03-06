package com.yjy.web.mms.controller.drgs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yjy.web.app.model.entity.print.mr.PrintMrApply;
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
public class DrgsReportController {

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
	
	
	//????????????????????????
	@RequestMapping("drgs-report-index")
	public String drgsreport(){
		//return "process/procedure";
		return "drgs/drgsindex";
	}

	/**
	 *???????????????
	 *
	 **/
	@RequestMapping("drgs-report-edit")
	public String DrgsReportEdit(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
			 HttpServletRequest req,
			@RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size){
		User lu=udao.getOne(userId);//?????????
//		if(!((lu.getDept().getLcks().equals("????????????")) )){
//			model.addAttribute("error", "???????????????????????????????????????????????????");
//			return "common/proce";
//		}
		//?????????????????????????????????
		/*aersServices.getAersDictionaries(model, userId, page, size);
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
		String applyTime=dateFormat.format(new Date());
		model.addAttribute("applyTime", applyTime);
		model.addAttribute("sqrUser", lu);
		//????????????
		model.addAttribute("applyType", "??????????????????????????????");
		
//		model.addAttribute("shUser", lu.getUserName()+"/"+lu.getRealName());
		
		
		//?????????????????????
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {
			
			//???????????????????????????????????????
			Long proid=Long.parseLong(request.getParameter("id"));
			AersYJGWReportProcessEntity aersReportProcessEntity=aersYJGWReportProcessDao.findbyAersreportId(proid);
			MedicalSafetyEventReportEntity medicalSafetyEventReportEntity=medicalSafetyEventDao.findByAersyjgwreportId(aersReportProcessEntity);
			
			model.addAttribute("medicalSafetyEventReportEntity", medicalSafetyEventReportEntity);
			model.addAttribute("aersReportProcessEntity", aersReportProcessEntity);
		
			
			//???????????????????????????????????????,??????????????????
			 dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
			String happenTime=dateFormat.format(aersReportProcessEntity.getHappenTime());
			model.addAttribute("happenTime", happenTime);
			
			//???????????????????????????????????????,??????????????????
			applyTime=dateFormat.format(aersReportProcessEntity.getApplyTime());
			model.addAttribute("applyTime", applyTime);
			
			//?????????????????? ??????
//			String OutDate=dateFormat.format(medicalSafetyEventReportEntity.getOutDate());
//			String OpDate=dateFormat.format(medicalSafetyEventReportEntity.getFirstOpDate());
//			model.addAttribute("OutDate", OutDate);
//			model.addAttribute("OpDate", OpDate);
			System.out.println("id:"+String.valueOf(request.getParameter("id")));
			AersReportReviewedEntity aersReportReviewedEntity = aersredao.findByAersreportIdAndStutas(Long.valueOf(request.getParameter("id")),23L  );
			
			
//			model.addAttribute("opDept", medCareUnOpEventReportEntity.getOpDept().getDeptId()+"/"+medCareUnOpEventReportEntity.getOpDept().getDeptName());
			model.addAttribute("shUser", aersReportReviewedEntity.getUserId().getUserName()+"/"+aersReportReviewedEntity.getUserId().getRealName());
			
			
			
			//??????????????????????????????
			model.addAttribute("customername", customername);
			
			
		}*/
		String diseaseId=StringUtil.stringIsNotNull(req.getParameter("diseaseId"))? req.getParameter("diseaseId") : "";
		String id=StringUtil.stringIsNotNull(req.getParameter("id"))? req.getParameter("id") : "";
		String version=StringUtil.stringIsNotNull(req.getParameter("version"))? req.getParameter("version") : "1";
		String reportStatus=StringUtil.stringIsNotNull(req.getParameter("reportStatus"))? req.getParameter("reportStatus") : "?????????";
		
		String oper=lu.getRealName();
		String operId=lu.getUserName();
		
		
		
		model.addAttribute("diseaseId", diseaseId);
		model.addAttribute("id", id);
		model.addAttribute("version", version);
		model.addAttribute("reportStatus", reportStatus);
		model.addAttribute("oper", oper);
		model.addAttribute("operId", operId);
		
		
		return "drgs/drgs-detail";

	}
	
	/*
	 * ???????????????????????????
	 */
	@RequestMapping("drgsmyapply")
	public String drgsmyapply(@SessionAttribute("userId") Long userId,Model model,
			 HttpServletRequest req,
			 @RequestParam(value = "page", defaultValue = "1") int page,
			 @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException{
		Pageable pa=PageRequest.of(page, size);
		User user=udao.findByUserId(userId).get(0);
		model.addAttribute("title", "??????");
		//??????????????????
		String status=StringUtil.stringIsNotNull(req.getParameter("status"))? req.getParameter("status") : "";
		status = status.equals("")?"??????":status;
		//????????????
		String startDate=StringUtil.stringIsNotNull(req.getParameter("startDate")) ? req.getParameter("startDate") : "";
//		startDate=startDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), -30),"yyyy-MM-dd"):startDate;
		startDate=startDate.equals("")?"2021-01-01":startDate;
		//????????????
		String endDate=StringUtil.stringIsNotNull(req.getParameter("endDate"))? req.getParameter("endDate") : "";
		endDate=endDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), 1),"yyyy-MM-dd"):endDate;
		
		
		//????????????
		String reportStatus=StringUtil.stringIsNotNull(req.getParameter("reportStatus"))? req.getParameter("reportStatus") : "";
		reportStatus = reportStatus.equals("")?"??????":reportStatus;
		
		//????????????
		String diseaseType=StringUtil.stringIsNotNull(req.getParameter("diseaseType"))? req.getParameter("diseaseType") : "";
		diseaseType = diseaseType.equals("")?"??????":diseaseType;
		
		
		//????????????/??????
		String eventCondition=StringUtil.stringIsNotNull(req.getParameter("eventCondition"))? req.getParameter("eventCondition") : "";
		

		//????????????????????????????????????
		AersDictionariesListEntity reportStatusDic=aersDicDao.findByAersdictionarieName("?????????????????????");
		List<Map<String, Object>> reportStatusList=JSON.parseObject(reportStatusDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("reportStatusList", reportStatusList);
		model.addAttribute("reportStatus", reportStatus);
		
		AersDictionariesListEntity diseaseTypeDic=aersDicDao.findByAersdictionarieName("?????????????????????");
		List<Map<String, Object>> diseaseTypeList=JSON.parseObject(diseaseTypeDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("diseaseTypeList", diseaseTypeList);
		model.addAttribute("diseaseType", diseaseType);
		
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("starttime", startDate.replaceAll("-",""));
		inParamMap.put("endtime", endDate.replaceAll("-",""));
		inParamMap.put("status", reportStatus.equals("??????")?"":reportStatus);
		inParamMap.put("condition", eventCondition);
		inParamMap.put("diseaseType", diseaseType.equals("??????")?"":diseaseType );
		inParamMap.put("page", page);
		inParamMap.put("size", size);
		JSONObject inParamJson = new JSONObject(inParamMap);
        
		String outStr = getUpdateAsset("http://127.0.0.1:8086/disease/json/list/query",inParamJson.toJSONString());
		JSONObject outJson = JSONObject.parseObject(outStr);
		JSONObject dataJson = outJson.getJSONObject("data");
		JSONArray jsonArray = dataJson.getJSONArray("items");
		List<Map<String, Object>> dataList=JSON.parseObject(jsonArray.toJSONString(),new TypeReference<List<Map<String,Object>>>(){});
		System.out.println("dataList:"+dataList.toString());
		model.addAttribute("prolist", dataList);
		model.addAttribute("dataJson", dataJson);
		
		//?????????????????????????????????
//		Page<Map<String, Object>> pagelist=aersProcessDao.queryApplyPaByUserId(userId
//				,DateUtil.getStrtoDate(startDate),DateUtil.getStrtoDate(endDate)
//				,status,eventLevel,eventCondition
//				,pa);
//		List<Map<String, Object>> prolist=pagelist.getContent();
//		model.addAttribute("page", pagelist);
//		model.addAttribute("prolist", prolist);
//
//
//		for(int i = 0 ;i<prolist.size();i++) {
//			String shry=prolist.get(i).get("shenuser").toString();
//
//			String [] shrys=shry.split(";");
//			String shryZW="";
//			for(int j=0;j<shrys.length;j++) {
//				shryZW=shryZW+udao.findByUserName(shrys[j]).getRealName()+";";
//			}
//			//prolist.get(i).setShenuser(shryZW);
//		}
		model.addAttribute("url", "shenser");
		
		model.addAttribute("status", status);
		
		model.addAttribute("eventCondition", eventCondition);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		return "drgs/drgsmyapply";
	}
	
	
	/*
	 * ?????????????????????
	 */
	@RequestMapping("drgsmyaudit")
	public String drgsmyaudit(@SessionAttribute("userId") Long userId,Model model,
			 HttpServletRequest req,
			 @RequestParam(value = "page", defaultValue = "1") int page,
			 @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException{
		Pageable pa=PageRequest.of(page, size);
		User user=udao.findByUserId(userId).get(0);
		model.addAttribute("title", "???????????????");
		//??????????????????
		String status=StringUtil.stringIsNotNull(req.getParameter("status"))? req.getParameter("status") : "";
		status = status.equals("")?"??????":status;
		//????????????
		String startDate=StringUtil.stringIsNotNull(req.getParameter("startDate")) ? req.getParameter("startDate") : "";
//		startDate=startDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), -30),"yyyy-MM-dd"):startDate;
		startDate=startDate.equals("")?"2021-01-01":startDate;
		//????????????
		String endDate=StringUtil.stringIsNotNull(req.getParameter("endDate"))? req.getParameter("endDate") : "";
		endDate=endDate.equals("")?DateUtil.getFormatStr(DateUtils.addDays(new Date(), 1),"yyyy-MM-dd"):endDate;
		
		
		//????????????
		String reportStatus=StringUtil.stringIsNotNull(req.getParameter("reportStatus"))? req.getParameter("reportStatus") : "";
		reportStatus = reportStatus.equals("")?"??????":reportStatus;
		
		//????????????
		String diseaseType=StringUtil.stringIsNotNull(req.getParameter("diseaseType"))? req.getParameter("diseaseType") : "";
		diseaseType = diseaseType.equals("")?"??????":diseaseType;
		
		
		//????????????/??????
		String eventCondition=StringUtil.stringIsNotNull(req.getParameter("eventCondition"))? req.getParameter("eventCondition") : "";
		

		//????????????????????????????????????
		AersDictionariesListEntity reportStatusDic=aersDicDao.findByAersdictionarieName("?????????????????????");
		List<Map<String, Object>> reportStatusList=JSON.parseObject(reportStatusDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("reportStatusList", reportStatusList);
		model.addAttribute("reportStatus", reportStatus);
		
		AersDictionariesListEntity diseaseTypeDic=aersDicDao.findByAersdictionarieName("?????????????????????");
		List<Map<String, Object>> diseaseTypeList=JSON.parseObject(diseaseTypeDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
		model.addAttribute("diseaseTypeList", diseaseTypeList);
		model.addAttribute("diseaseType", diseaseType);
		
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("starttime", startDate.replaceAll("-",""));
		inParamMap.put("endtime", endDate.replaceAll("-",""));
		inParamMap.put("status", reportStatus.equals("??????")?"":reportStatus);
		inParamMap.put("condition", eventCondition);
		inParamMap.put("diseaseType", diseaseType.equals("??????")?"":diseaseType );
		inParamMap.put("page", page);
		inParamMap.put("size", size);
		JSONObject inParamJson = new JSONObject(inParamMap);
        
		String outStr = getUpdateAsset("http://127.0.0.1:8086/disease/json/list/query",inParamJson.toJSONString());
		JSONObject outJson = JSONObject.parseObject(outStr);
		JSONObject dataJson = outJson.getJSONObject("data");
		JSONArray jsonArray = dataJson.getJSONArray("items");
		List<Map<String, Object>> dataList=JSON.parseObject(jsonArray.toJSONString(),new TypeReference<List<Map<String,Object>>>(){});
		System.out.println("dataList:"+dataList.toString());
		model.addAttribute("prolist", dataList);
		model.addAttribute("dataJson", dataJson);
		
		//?????????????????????????????????
//		Page<Map<String, Object>> pagelist=aersProcessDao.queryApplyPaByUserId(userId
//				,DateUtil.getStrtoDate(startDate),DateUtil.getStrtoDate(endDate)
//				,status,eventLevel,eventCondition
//				,pa);
//		List<Map<String, Object>> prolist=pagelist.getContent();
//		model.addAttribute("page", pagelist);
//		model.addAttribute("prolist", prolist);
//
//
//		for(int i = 0 ;i<prolist.size();i++) {
//			String shry=prolist.get(i).get("shenuser").toString();
//
//			String [] shrys=shry.split(";");
//			String shryZW="";
//			for(int j=0;j<shrys.length;j++) {
//				shryZW=shryZW+udao.findByUserName(shrys[j]).getRealName()+";";
//			}
//			//prolist.get(i).setShenuser(shryZW);
//		}
		model.addAttribute("url", "shenser");
		
		model.addAttribute("status", status);
		
		model.addAttribute("eventCondition", eventCondition);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		return "drgs/drgsmyaudit";
	}
	
	
    public String getUpdateAsset(String posturl,String inParam) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse;
        System.out.println("???????????????"+inParam);
        try {
            HttpPost httpPost = new HttpPost(posturl);
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity entity = new StringEntity(inParam, ContentType.create("text/json", "UTF-8"));
            httpPost.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            httpPost.setConfig(requestConfig);
            
            closeableHttpResponse = closeableHttpClient.execute(httpPost);

            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                //TODO:????????????200????????????????????????,????????????????????????
            	System.out.print("????????????");
            	return "????????????";
            }

            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String asset_synchronization = EntityUtils.toString(httpEntity, "UTF-8");
            System.out.println("???????????????"+asset_synchronization);
            return asset_synchronization;
//            JSONArray jsonArray = JSONArray.parseArray(asset_synchronization);
//            if (jsonArray.size() > 0) {
//                //TODO:???????????????json?????????????????????,??????????????????????????????
//            	return "";
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "";
    }
    
	@RequestMapping("drgsummerdatatables")
	public String drgsummerdatatables(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException
	{
		String starTime=String.valueOf(request.getParameter("starTime"));
		String endTime=String.valueOf(request.getParameter("endTime"));
		
		
		List<HashMap<String,String>> fieldList=new ArrayList<HashMap<String,String>>();
		HashMap<String,String> fieldMap=new HashMap<String,String>();

		fieldMap=new HashMap<String,String>();
		fieldMap.put("field","????????????");
		fieldList.add(fieldMap);
		
		fieldMap=new HashMap<String,String>();
		fieldMap.put("field","?????????");
		fieldList.add(fieldMap);
		
		fieldMap=new HashMap<String,String>();
		fieldMap.put("field","????????????");
		fieldList.add(fieldMap);
		
		fieldMap=new HashMap<String,String>();
		fieldMap.put("field","????????????");
		fieldList.add(fieldMap);
		
		fieldMap=new HashMap<String,String>();
		fieldMap.put("field","???????????????");
		fieldList.add(fieldMap);
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("starttime", starTime.replaceAll("-",""));
		inParamMap.put("endtime", endTime.replaceAll("-",""));
		JSONObject inParamJson = new JSONObject(inParamMap);
        
		String outStr = getUpdateAsset("http://127.0.0.1:8086/disease/json/list/query",inParamJson.toJSONString());
		JSONObject outJson = JSONObject.parseObject(outStr);
		JSONObject dataJson = outJson.getJSONObject("data");
		JSONArray jsonArray = dataJson.getJSONArray("items");
		List<Map<String, Object>> dataList=JSON.parseObject(jsonArray.toJSONString(),new TypeReference<List<Map<String,Object>>>(){});
		System.out.println("dataList:"+dataList.toString());
		model.addAttribute("prolist", dataList);
		model.addAttribute("dataJson", dataJson);
		
		model.addAttribute("starTime", starTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("fieldList", fieldList);
		
		
		
		
		return "drgs/drgsummerdatatables";
	}
}

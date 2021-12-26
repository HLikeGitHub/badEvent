package com.yjy.web.app.controller.print.mr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjy.web.app.config.comm.AppConfigCfg;
import com.yjy.web.app.config.comm.PrintMrAuthDeptItem;
import com.yjy.web.app.config.comm.PrintMrForItem;
import com.yjy.web.app.config.comm.PrintMrProgressItem;
import com.yjy.web.app.model.dao.print.mr.AppPrintMrApplyDao;
import com.yjy.web.app.model.entity.comm.AppConfig;
import com.yjy.web.app.model.entity.print.mr.PrintMrApply;
import com.yjy.web.app.service.comm.AppConfigService;
import com.yjy.web.comm.StringtoDate;
import com.yjy.web.comm.result.Result;
import com.yjy.web.comm.utils.*;
import com.yjy.web.mms.model.dao.user.UserService;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("oapp/print/mr")
public class PrintMrMangerController {

	@Autowired
	AppPrintMrApplyDao appPrintMrApplyDao;

	@Autowired
	private AppConfigService appConfigService;

	@Autowired
	private UserService userService;

	@Value("${my.dns.his}")
	private String hisdataurl;

    /*
     * 删除申请
     */
	@RequestMapping("medical-record-print-apply-delete")
	public String DSAGec(HttpServletRequest request, HttpSession session) {

		long pid = Long.valueOf(request.getParameter("pid"));
		appPrintMrApplyDao.deleteById(pid);
		return "redirect:/meetroomview";
	}

	/*
	 * 获取申请列表
	 */
	@RequestMapping(value="medical-record-print-apply-view", method = RequestMethod.GET)
	public String meetroomview(Model model, HttpServletRequest request,HttpSession session, 
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "baseKey", required = false) String baseKey,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "icon", required = false) String icon,
			@RequestParam(value = "size", defaultValue = "15") int size) throws ParseException {
		if(!isAuthDept(session.getAttribute("userId"))){
			return "medicalrecordprint/MRPview";
		}

		String starTime=String.valueOf(request.getParameter("starTime"));
		String endTime=String.valueOf(request.getParameter("endTime"));
		String pstatus=String.valueOf(request.getParameter("pstatus"));
		String adNum=String.valueOf(request.getParameter("adNum"));
		String orderId=String.valueOf(request.getParameter("orderId"));

		if(StringUtil.stringIsNotNull(starTime)&&(StringUtil.stringIsNotNull(endTime))&&(StringUtil.stringIsNotNull(pstatus))) {
			//分页
			Pageable pa=PageRequest.of(page, size);
			StringtoDate stringtoDate = new StringtoDate();
//			Page<PrintMrApply> mrPrintApplyPage = StringUtil.stringIsNotNull(adNum) ?  appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime),DateUtil.getStrtoDate(endTime) , adNum, pstatus,pa):
//					appPrintMrApplyDao.findbyTimeAndStatus(DateUtil.getStrtoDate(starTime),DateUtil.getStrtoDate(endTime) , pstatus,pa);//appPrintMrApplyDao.findAll(pa) ;
			Page<PrintMrApply> mrPrintApplyPage = appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
					StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "", pstatus, pa);
			List<PrintMrApply> printMrApplyList =mrPrintApplyPage.getContent();
			typestatus(model);
			model.addAttribute("printMrApplyList", printMrApplyList);
			if(printMrApplyList.size()>0) {
				model.addAttribute("mrPrintApplyItem", printMrApplyList.get(0));
			}else {
				model.addAttribute("mrPrintApplyItem", null);
			}
			model.addAttribute("page", mrPrintApplyPage);
			model.addAttribute("startDate", starTime);
			model.addAttribute("endDate", endTime);
			model.addAttribute("pstatus", pstatus);
			String url="medical-record-print-apply-list?starTime="+starTime+"&endTime="+endTime+"&adNum="+adNum+"&orderId="+orderId+"&pstatus="+pstatus+"";
			url=url.replaceAll(" ", "%20");
			model.addAttribute("url", url);
			
		}else {
			Calendar cld=Calendar.getInstance();
			cld.setTime(new Date());
			cld.add(cld.DATE, -30);
			starTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd hh:mm:ss");
			model.addAttribute("startDate", starTime);
			cld.add(cld.DATE, 31);
			endTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd hh:mm:ss");
			model.addAttribute("endDate", endTime);
			if(pstatus == null || pstatus.isEmpty()){
				pstatus="全部";
			}
			Pageable pa=PageRequest.of(page, size);
//			Page<PrintMrApply> mrPrintApplyPage = StringUtil.stringIsNotNull(adNum) ?  appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime),DateUtil.getStrtoDate(endTime) , adNum, pstatus,pa):
//					appPrintMrApplyDao.findbyTimeAndStatus(DateUtil.getStrtoDate(starTime),DateUtil.getStrtoDate(endTime) , pstatus,pa);//appPrintMrApplyDao.findAll(pa) ;
			Page<PrintMrApply> mrPrintApplyPage = appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
					StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "", pstatus, pa);
			List<PrintMrApply> printMrApplyList =mrPrintApplyPage.getContent();
			typestatus(model);
			model.addAttribute("printMrApplyList", printMrApplyList);
			if(printMrApplyList.size()>0) {
				model.addAttribute("mrPrintApplyItem", printMrApplyList.get(0));
			}	
			else {
				model.addAttribute("mrPrintApplyItem", null);
			}
			model.addAttribute("page", mrPrintApplyPage);

			model.addAttribute("pstatus", "未审核");
			String url="medical-record-print-apply-list?starTime="+starTime+"&endTime="+endTime+"&adNum="+adNum+"&orderId="+orderId+"&pstatus="+pstatus+"";
			url=url.replaceAll(" ", "%20");
			model.addAttribute("url",url );
		}
		feeSatistics(starTime, endTime, model);

		return "medicalrecordprint/MRPview";
	}

	/**
	 * 用户是否有权限查看病案打印申请
	 * @param uid
	 * @return
	 */
	private boolean isAuthDept(Object uid){
		if(uid == null || uid.toString().isEmpty()){
			return false;
		}

		User user = userService.findByUid(uid.toString());
		if(user == null){
			return false;
		}

		Dept dept = user.getDept();
		if(dept == null || dept.getDeptName() == null){
			return false;
		}

		AppConfig printMrAuthDeptCfg = appConfigService.findByName(AppConfigCfg.APP_PRINT_MR_AUTH_DEPT);
		String printMrAuthDeptJson = printMrAuthDeptCfg == null || TextUtil.isNullOrEmpty(printMrAuthDeptCfg.getContent())?
				new PrintMrAuthDeptItem().toString() : printMrAuthDeptCfg.getContent();
		List<Map<String,String>> itemsArray= JSONUtil.toObject(printMrAuthDeptJson, List.class);
		for(Map<String, String> item : itemsArray){
			String deptItem = item.get("item").toString();
			if(dept.getDeptName().equals(deptItem)){
				if(dept.getDeptName().equals(PrintMrAuthDeptItem.ITEM_DEPT_MR)){
					//病案科只有指定工号可见
					boolean isAuthUser = false;
					JSONArray jsonArray = JSONArray.parseArray(item.get("ext").toString());
					for(int i=0;i<jsonArray.size();i++){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String empId = jsonObject.getString("empId");
						if(user.getUserName().equalsIgnoreCase(empId)){
							isAuthUser = true;
							break;
						}
					}
					return isAuthUser;
				}
				//非病案科的授权科室全部可见
				return true;
			}
		}
		//不是授权科室
		return false;
	}

	/**
	 * 费用统计
	 * @param dateStr1 开始日期
	 * @param dateStr2 截止日期
	 * @param model 模型
	 */
	private void feeSatistics(String dateStr1, String dateStr2, Model model){
		if(TextUtil.isNullOrEmpty(dateStr1, dateStr1)){
			return;
		}

		String pattern = "yyyy-MM-dd HH:mm:ss";
		if(dateStr1.length() == pattern.length() && dateStr2.length() == pattern.length() &&
		dateStr1.contains("-") && dateStr1.contains(":") && dateStr2.contains("-") && dateStr2.contains(":")){
			List<PrintMrApply> applyList = null;
			try{
				Date date1 = new SimpleDateFormat(pattern).parse(dateStr1);
				Date date2 = new SimpleDateFormat(pattern).parse(dateStr2);
				applyList = appPrintMrApplyDao.findByApplyDateAndStatus4Sta(date1, date2);
			}catch (Exception e){
				e.printStackTrace();
			}
			if(applyList == null){
				return;
			}

			float totalPrintFee = 0, totalExpFee = 0, totalFee = 0, totalPaidExpFee = 0;
			for(PrintMrApply apply : applyList){
				float printFee = NumUtil.parseFloat(apply.getPrintFee());
				float expFee = NumUtil.parseFloat(apply.getExpressFee());
				totalPrintFee += printFee;
				totalExpFee += expFee;
				totalFee = totalFee + printFee + expFee;

				if(PrintMrProgressItem.ITEM_SENT.equals(apply.getStatus())){
					totalPaidExpFee += expFee;
				}
			}

			model.addAttribute("totalPrintFee", totalPrintFee);
			model.addAttribute("totalExpFee", totalExpFee);
			model.addAttribute("totalFee", totalFee);
			model.addAttribute("totalPaidExpFee", totalPaidExpFee);
		}
	}

    /*
     * 获取申请详情
     */
	@RequestMapping(value="medical-record-print-apply-table", method = RequestMethod.GET)
	public String testdd(Model model, HttpSession session,HttpServletRequest request) {
		long applyid = Long.valueOf(request.getParameter("id"));
		PrintMrApply printMrApplyList = appPrintMrApplyDao.findByApplyIdAndEnabled(applyid, true);
		model.addAttribute("printMrApplyItem", printMrApplyList);
		
		Map<String,String> map=new HashMap<String,String>();
		//调用数据服务获取病人病案已打印测试
	    URI uri = UriComponentsBuilder.fromHttpUrl(hisdataurl+"getmedical-print-times")
	              .queryParam("adId", printMrApplyList.getAdId())
	              .build().encode().toUri();
	    RestTemplate restTemplate=new RestTemplate();
	    int printCount = 1;
	    try{
			String data=restTemplate.getForObject(uri,String.class);
			//获取病案打印申请实体类
			Result result = JSONUtil.toObject(data, Result.class);
			JSONObject resJson=JSONObject.parseObject(result.getData().toString());
			printCount = Integer.parseInt(resJson.getString("printCount"));
		}catch (Exception e){
	    	e.printStackTrace();
		}
	    printMrApplyList.setPrintCount(printCount);
		AppConfig printMrForCfg = appConfigService.findByName(AppConfigCfg.APP_PRINT_MR_EXPFREE);
		String printMrForJson = printMrForCfg == null ? new PrintMrForItem().toString() : printMrForCfg.getContent();
		List<Map<String,Object>> expfreeArray=JSONUtil.toObject(printMrForJson, List.class);
		model.addAttribute("expfreelist", expfreeArray);
	    return "medicalrecordprint/MRPtableview";

	}

    /*
     * 获取申请列表
     */
	@RequestMapping(value="medical-record-print-apply-list", method = RequestMethod.GET)
	public String mrplist(Model model, HttpServletRequest request,HttpSession session,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "baseKey", required = false) String baseKey,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "icon", required = false) String icon,
			@RequestParam(value = "size", defaultValue = "15") int size
			) throws ParseException {
		String starTime=String.valueOf(request.getParameter("starTime"));
		String endTime=String.valueOf(request.getParameter("endTime"));
		String pstatus=String.valueOf(request.getParameter("pstatus"));
		String adNum=String.valueOf(request.getParameter("adNum"));
		String orderId=String.valueOf(request.getParameter("orderId"));
		if(pstatus == null || pstatus.isEmpty()){
			pstatus="全部";
		}
		//String page1=String.valueOf(request.getParameter("page"));
		if(StringUtil.stringIsNotNull(starTime)&&(StringUtil.stringIsNotNull(endTime))&&(StringUtil.stringIsNotNull(pstatus))) {
			if(pstatus.split("\\?").length>0) {
				String tempage=pstatus.split("\\?")[1];
				pstatus=pstatus.split("\\?")[0];
				
				page=Integer.parseInt(tempage.split("\\=")[1]);
			}
			//分页
			Pageable pa=PageRequest.of(page, size);
			StringtoDate stringtoDate = new StringtoDate();
//			Page<PrintMrApply> mrPrintApplyPage = appPrintMrApplyDao.findbyTimeAndStatus(DateUtil.getStrtoDate(starTime),DateUtil.getStrtoDate(endTime) , pstatus, pa);//appPrintMrApplyDao.findAll(pa) ;
			Page<PrintMrApply> mrPrintApplyPage = appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
					StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "", pstatus, pa);
			List<PrintMrApply> printMrApplyList =mrPrintApplyPage.getContent();
			typestatus(model);
			model.addAttribute("printMrApplyList", printMrApplyList);
			if(printMrApplyList.size()>0) {
				model.addAttribute("printMrApplyItem", printMrApplyList.get(0));
			}	
			else {
				model.addAttribute("printMrApplyItem", null);
			}
			model.addAttribute("page", mrPrintApplyPage);
			model.addAttribute("startDate", starTime);
			model.addAttribute("endDate", endTime);
			model.addAttribute("pstatus", pstatus);
			String url="medical-record-print-apply-list?starTime="+starTime+"&endTime="+endTime+"&adNum="+adNum+"&orderId="+orderId+"&pstatus="+pstatus+"";
			url=url.replaceAll(" ", "%20");
			model.addAttribute("url", "medical-record-print-apply-list");
			
		}else {
			Calendar cld=Calendar.getInstance();
			cld.setTime(new Date());
			cld.add(cld.DATE, -30);
			starTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd hh:mm:ss");
			model.addAttribute("startDate", starTime);
			cld.add(cld.DATE, 31);
			endTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd hh:mm:ss");
			model.addAttribute("endDate", endTime);
			pstatus="全部";
			Pageable pa=PageRequest.of(page, size);
//			Page<PrintMrApply> mrPrintApplyPage = appPrintMrApplyDao.findbyTimeAndStatus(DateUtil.getStrtoDate(starTime),DateUtil.getStrtoDate(endTime) , pstatus,pa);//appPrintMrApplyDao.findAll(pa) ;
			Page<PrintMrApply> mrPrintApplyPage = appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
					StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "", pstatus, pa);
			List<PrintMrApply> printMrApplyList =mrPrintApplyPage.getContent();
			typestatus(model);
			model.addAttribute("printMrApplyList", printMrApplyList);
			if(printMrApplyList.size()>0) {
				model.addAttribute("printMrApplyItem", printMrApplyList.get(0));
			}	
			else {
				model.addAttribute("printMrApplyItem", null);
			}
			model.addAttribute("page", mrPrintApplyPage);

			model.addAttribute("pstatus", "全部");
			String url="medical-record-print-apply-list?starTime="+starTime+"&endTime="+endTime+"&adNum="+adNum+"&orderId="+orderId+"&pstatus="+pstatus+"";
			url=url.replaceAll(" ", "%20");
			model.addAttribute("url", url);
		}
		feeSatistics(starTime, endTime, model);

		return "medicalrecordprint/MRPListview";
	}
	

	
	
	/*
	 * 保存申请单
	 */
	@RequestMapping(value = "medical-record-print-apply-save", method = RequestMethod.POST)
	public String mrpSave(HttpServletRequest req, @Valid PrintMrApply printMrApply,Model model,
			BindingResult br) throws IllegalStateException, IOException, ParseException {
		System.out.println("printMrApply:"+ printMrApply.toString());
		String status=String.valueOf(printMrApply.getApplyState());
		if(status.equals("未支付")) {
			PrintMrApply oldPrintApply = appPrintMrApplyDao.findByApplyIdAndEnabled(printMrApply.getApplyId(), true);
			Pageable pa=PageRequest.of(10, 10);
			String adNum=oldPrintApply.getAdNum();
			if(Double.parseDouble(printMrApply.getExpressFee())>0) {
				Calendar cld=Calendar.getInstance();
				cld.setTime(new Date());
				cld.add(cld.DATE, -30);
				String starTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd hh:mm:ss");
				cld.add(cld.DATE, 31);
				String endTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd hh:mm:ss");
				
				List<PrintMrApply> vPrintApplys=appPrintMrApplyDao.findbyAdNum(DateUtil.getStrtoDate(starTime),DateUtil.getStrtoDate(endTime) ,adNum,printMrApply.getApplyId());
				StringBuffer str=new StringBuffer();
				//List<PrintMrApply> pals=vPrintApplys.getContent();
				if(vPrintApplys.size()>0) {
					str.append("(");
					for(int i=0;i<vPrintApplys.size();i++) {
						str.append("申请日期："+vPrintApplys.get(i).getApplyId()+"\r\n");
						str.append("收件人："+vPrintApplys.get(i).getExpAddressee()+"\r\n");
						str.append("收件地址："+vPrintApplys.get(i).getExpAddress()+"\r\n");
						str.append("打印费："+vPrintApplys.get(i).getPrintFee()+"\r\n");
						str.append("快递费："+vPrintApplys.get(i).getExpressFee()+"\r\n");
						
					}
					str.append(")");
					model.addAttribute("error", "该住院号两天内有收取过快递费，请核查是否把快递费置为0！"+str.toString());
					return "common/proce";
					
				}
			}
			

		}
		
		
		
		
		PrintMrApply oldPrintApply = appPrintMrApplyDao.findByApplyIdAndEnabled(printMrApply.getApplyId(), true);
		oldPrintApply.setExpAddress(printMrApply.getExpAddress());
		oldPrintApply.setExpAddressee(printMrApply.getExpAddressee());
		oldPrintApply.setExpPhoneNum(printMrApply.getExpPhoneNum());
		oldPrintApply.setExpNum(printMrApply.getExpNum());
		oldPrintApply.setStatusExt(printMrApply.getStatusExt());
		oldPrintApply.setExpressFee(printMrApply.getExpressFee());
		oldPrintApply.setExpCompany(printMrApply.getExpCompany());
//		oldPrintApply.setPrintCount(printCount);
		oldPrintApply.setPrintFee(printMrApply.getPrintFee());
		
		if(StringUtil.stringIsNotNull(status)) {
			oldPrintApply.setStatus(status);
//			if(status.equals("审核")) {
//				if(printMrApply.getExpNum().equals("")) {
//					oldPrintApply.setStatus("打印中");
//				}else {
//					oldPrintApply.setStatus("已邮寄");
//				}
//			}else if(status.equals("未通过")) {
//				oldPrintApply.setStatus("未通过");
//			}
		}
		appPrintMrApplyDao.save(oldPrintApply);
		return "redirect:/oapp/print/mr/medical-record-print-apply-table?id="+ printMrApply.getApplyId();

	}
	@RequestMapping(value = "medical-record-print-apply-save", method = RequestMethod.GET)
	public void mrpSave() {
	}

    /*
     * 返回状态列表
     */
	private void typestatus(Model model) {
		AppConfig printMrForCfg = appConfigService.findByName(AppConfigCfg.APP_PRINT_MR_PROGRESS);
		String printMrForJson = printMrForCfg == null ? new PrintMrForItem().toString() : printMrForCfg.getContent();
		List<Map<String,Object>> statusArray=JSONUtil.toObject(printMrForJson, List.class);
		List<Map<String,Object>> reArrayList=new ArrayList<Map<String,Object>>();
		Map<String,Object> reMap=new HashMap<String,Object>();
		reMap.put("pos", "0");
		reMap.put("item", "全部");
		reMap.put("color", "label-success");
		reArrayList.add(reMap);
		reArrayList.addAll(statusArray);
		model.addAttribute("statuslist", reArrayList);

	}
	
	
	
	/**
	 * @author: winper001
	 * @throws ParseException 
	 * @create: 2020-01-15 21:25
	 * @description: 出入境申请
	*/
		@RequestMapping("medicalrecorddatatables")
		public String medicalrecorddatatables(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
								   @RequestParam(value = "page", defaultValue = "0") int page,
								   @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException
		{
			String starTime=String.valueOf(request.getParameter("starTime"));
			String endTime=String.valueOf(request.getParameter("endTime"));
			String pstatus=String.valueOf(request.getParameter("pstatus"));
			String adNum=String.valueOf(request.getParameter("adNum"));
			String orderId=String.valueOf(request.getParameter("orderId"));
			
			
			List<HashMap<String,String>> fieldList=new ArrayList<HashMap<String,String>>();
			HashMap<String,String> fieldMap=new HashMap<String,String>();

			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","申请ID");
			fieldList.add(fieldMap);
			
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","患者名字");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","复印用途ID");
			fieldList.add(fieldMap);
			
//			fieldMap=new HashMap<String,String>();
//			fieldMap.put("field","申请复印项目列表");
//			fieldList.add(fieldMap);
//			
//			fieldMap=new HashMap<String,String>();
//			fieldMap.put("field","申请复印项目其他");
//			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","申请时间");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","打印费用");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","快递费用");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","订单ID");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","审核状态");
			fieldList.add(fieldMap);

			if(StringUtil.stringIsNotNull(starTime)&&(StringUtil.stringIsNotNull(endTime))&&(StringUtil.stringIsNotNull(pstatus))) {
				List<PrintMrApply> printMrApplyList=(List<PrintMrApply>) appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
						StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "",pstatus);
				model.addAttribute("starTime", starTime);
				model.addAttribute("endTime", endTime);
				model.addAttribute("pstatus", pstatus);
				model.addAttribute("printMrApplyList", printMrApplyList);
			}else {
				Calendar cld=Calendar.getInstance();
				cld.setTime(new Date());
				cld.add(cld.DATE, -30);
				starTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd");
				model.addAttribute("startDate", starTime);
				cld.add(cld.DATE, 31);
				endTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd");
				model.addAttribute("endDate", endTime);
				if(!StringUtil.stringIsNotNull(pstatus)){
					pstatus="已邮寄";
				}
				List<PrintMrApply> printMrApplyList=(List<PrintMrApply>) appPrintMrApplyDao.findbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
						StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "",pstatus);
				model.addAttribute("starTime", starTime);
				model.addAttribute("endTime", endTime);
				model.addAttribute("pstatus", pstatus);
				model.addAttribute("printMrApplyList", printMrApplyList);

				
			}
			
			
			
			
			
			model.addAttribute("fieldList", fieldList);
			
			typestatus(model);
			
			
			
			return "medicalrecordprint/medicalrecorddatatables";
		}
		
		/**
		 * @author: winper001
		 * @throws ParseException 
		 * @create: 2020-01-15 21:25
		 * @description: 出入境申请
		*/
			@RequestMapping("medicalrecordsummerdatatables")
			public String medicalrecordsummerdatatables(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
									   @RequestParam(value = "page", defaultValue = "0") int page,
									   @RequestParam(value = "size", defaultValue = "10") int size) throws ParseException
			{
				String starTime=String.valueOf(request.getParameter("starTime"));
				String endTime=String.valueOf(request.getParameter("endTime"));
				String pstatus=String.valueOf(request.getParameter("pstatus"));
				String adNum=String.valueOf(request.getParameter("adNum"));
				String orderId=String.valueOf(request.getParameter("orderId"));
				
				
				List<HashMap<String,String>> fieldList=new ArrayList<HashMap<String,String>>();
				HashMap<String,String> fieldMap=new HashMap<String,String>();

				fieldMap=new HashMap<String,String>();
				fieldMap.put("field","总费用");
				fieldList.add(fieldMap);
				
				fieldMap=new HashMap<String,String>();
				fieldMap.put("field","打印费");
				fieldList.add(fieldMap);
				
				fieldMap=new HashMap<String,String>();
				fieldMap.put("field","邮寄费");
				fieldList.add(fieldMap);
				
				fieldMap=new HashMap<String,String>();
				fieldMap.put("field","总数量");
				fieldList.add(fieldMap);

				if(StringUtil.stringIsNotNull(starTime)&&(StringUtil.stringIsNotNull(endTime))&&(StringUtil.stringIsNotNull(pstatus))) {
					List<Map<String,String>> printMrApplyList=(List<Map<String,String>>) appPrintMrApplyDao.findsumbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
							StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "",pstatus);
					model.addAttribute("starTime", starTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("pstatus", pstatus);
					model.addAttribute("printMrApplyList", printMrApplyList);
				}else {
					Calendar cld=Calendar.getInstance();
					cld.setTime(new Date());
					cld.add(cld.DATE, -30);
					starTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd");
					model.addAttribute("startDate", starTime);
					cld.add(cld.DATE, 31);
					endTime=DateUtil.getFormatStr(cld.getTime(),"yyyy-MM-dd");
					model.addAttribute("endDate", endTime);
					if(!StringUtil.stringIsNotNull(pstatus)){
						pstatus="已邮寄";
					}
					List<Map<String,String>> printMrApplyList=(List<Map<String,String>>) appPrintMrApplyDao.findsumbyTimeAndAdNumAndStatus(DateUtil.getStrtoDate(starTime), DateUtil.getStrtoDate(endTime),
							StringUtil.stringIsNotNull(adNum) ? adNum : "", StringUtil.stringIsNotNull(orderId) ? orderId : "",pstatus);
					model.addAttribute("starTime", starTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("pstatus", pstatus);
					model.addAttribute("printMrApplyList", printMrApplyList);

					
				}
				
				
				
				
				
				model.addAttribute("fieldList", fieldList);
				
				typestatus(model);
				
				
				
				return "medicalrecordprint/medicalrecordsummerdatatables";
			}
		
}

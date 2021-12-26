package com.yjy.web.mms.controller.process;

import com.yjy.web.comm.utils.DateUtil;
import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.mms.controller.weixinqyh.MessageController;
import com.yjy.web.mms.model.dao.notedao.AttachmentDao;
import com.yjy.web.mms.model.dao.processdao.PassportApplyDao;
import com.yjy.web.mms.model.dao.processdao.ProcessListDao;
import com.yjy.web.mms.model.dao.processdao.ReviewedDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.process.AubUser;
import com.yjy.web.mms.model.entity.process.PassportApply;
import com.yjy.web.mms.model.entity.process.ProcessList;
import com.yjy.web.mms.model.entity.process.Reviewed;
import com.yjy.web.mms.model.entity.system.SystemStatusList;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.process.PassportService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class PassportController {

	@Autowired
	private UserDao udao;

	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private ReviewedDao redao;

	@Autowired
	private ProcessListDao prodao;

	@Autowired
	private PassportService proservice;

	@Autowired
	private AttachmentDao AttDao;

	@Autowired
	private PassportApplyDao passportApplyDao;

	@Value("${attachment.roopath}")
	private String rootpath;

	@Value("${attachment.httppath}")
	private String httppath;

	@Value("${moblie.url}")
	private String moblieurl;

	@Value("${customer.name}")
	private String customername;

	@Value("1")//是否发送短信("0"/"1")
	private String isSendMessage;



/**
 * @author: winper001
 * @create: 2020-01-15 21:25
 * @description: 出入境申请【领取】
*/
	@RequestMapping("passportapply")
	public String passportapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size)
	{
		proservice.indexPassportApply(model, userId, page, size);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy年MM月dd日");
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {//编辑
			Long proid=Long.parseLong(request.getParameter("id"));
			Long curPage=Long.parseLong(request.getParameter("curPage"));
			//String itemType=request.getParameter("itemType");
			ProcessList processList=prodao.findbyuseridandtitle(proid);
			PassportApply passportApply=passportApplyDao.findByProId(processList);
			String defashen=passportApply.getNameuser();
			model.addAttribute("passportApply", passportApply);
			model.addAttribute("process", processList);
			model.addAttribute("curPage",curPage);
			//model.addAttribute("itemType", itemType);
			model.addAttribute("defaShen", defashen);

		}
		String nowTime=dateFormat.format(new Date());
		model.addAttribute("nowTime", nowTime);
		User applyPer=udao.getOne(userId);
		String personName=applyPer.getUserName()+"/"+applyPer.getRealName();
		int count_hk=passportApplyDao.findByPersonNameAndDestination_finished(personName,"香港").size();
		int count_ao=passportApplyDao.findByPersonNameAndDestination_finished(personName,"澳门").size();
		int count_tw=passportApplyDao.findByPersonNameAndDestination_finished(personName,"台湾").size();
		int count_foreign=passportApplyDao.findByPersonNameAndDestination_foreign_finished(personName).size();
		int count_all=passportApplyDao.findByPersonNameAll_finished(personName).size();
		model.addAttribute("count_hk",count_hk);
		model.addAttribute("count_ao", count_ao);
		model.addAttribute("count_tw",count_tw);
		model.addAttribute("count_foreign",count_foreign);
		model.addAttribute("count_all",count_all);
		if(applyPer.getLimitDate()!=null && new Date().getTime()<=applyPer.getLimitDate().getTime()){//当前日期小于限制日期，则限制
			String limitDate=dateFormat2.format(applyPer.getLimitDate());
			model.addAttribute("error", limitDate);
			return "common/passport_hint";
		}
		return "process/passportapply";
	}

    /**
     * @author: winper001
     * @create: 2020-01-15 21:25
     * @description: 出入境申请【办理】
     */
    @RequestMapping("passportapply_handle")
    public String passportapply_handle(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size)
    {
        proservice.indexPassportApply(model, userId, page, size);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy年MM月dd日");
        if((StringUtil.stringIsNotNull(request.getParameter("id")))) {//编辑
            Long proid=Long.parseLong(request.getParameter("id"));
            Long curPage=Long.parseLong(request.getParameter("curPage"));
            //String itemType=request.getParameter("itemType");
            ProcessList processList=prodao.findbyuseridandtitle(proid);
            PassportApply passportApply=passportApplyDao.findByProId(processList);
            String defashen=passportApply.getNameuser();
            model.addAttribute("passportApply", passportApply);
            model.addAttribute("process", processList);
            model.addAttribute("curPage",curPage);
            //model.addAttribute("itemType", itemType);
            model.addAttribute("defaShen", defashen);
        }
        String nowTime=dateFormat.format(new Date());
        model.addAttribute("nowTime", nowTime);
        return "process/passportapply_handle";
    }
	/**
	 * @author: winper001
	 * @create: 2020-01-20
	 * @description: 出入境申请提交【领取】
	*/

    @RequestMapping("passportapplysubmit")
    public String passportapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid PassportApply passportApply,BindingResult br,
                                       @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, ParseException{

        User lu=udao.getOne(userId);//申请人
 		//String personName=lu.getUserName()+"/"+lu.getRealName();
        String xzry=passportApply.getNameuser();
        //前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = xzry==null? null: (udao.findByUserName(xzry.split("/")[0]));//审核人

        String val=req.getParameter("val");
        //中英文版信息新增
		String passportId_temp=null;

		if(passportApply.getTypeProve().equals("普通护照通行证(中+英版)")){
			passportId_temp=req.getParameter("passportId_temp");

		}

        if(StringUtil.objectIsNull(passportApply.getPassportApplyId())) {
            PassportApply oldPassportApply=passportApplyDao.findById(passportApply.getPassportApplyId()).orElse(null);

            ProcessList oldPro=oldPassportApply.getProId();
			if(passportApply.getTypeProve().equals("普通护照通行证(中+英版)")){
				passportApply.setPassportId(passportId_temp);
			}

			passportApply.setStateCertificate("未领取");
			passportApply.setTakeCertificateTime("未领取");
			passportApply.setReturnCertificateTime("未领取");
            oldPro.setProcessName(passportApply.getDestination()+": "+passportApply.getApplyReason());
			oldPro.setStartTime(passportApply.getStartTime());
			oldPro.setEndTime(passportApply.getEndTime());
			oldPro.setProcessDescribe(passportApply.getTypeProve()+'【'+ passportApply.getIsPrint()+'】');
            proservice.index8(oldPro, val, lu,shen.getUserName());
            passportApply.setProId(oldPro);
            passportApplyDao.save(passportApply);
            //存审核表
            //删除审核记录
            List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
            if(!Objects.isNull(rev)){
                redao.deleteAll(rev);
            }
            proservice.index7(shen, oldPro);

            //接待消息通知到下级审核人
			if(isSendMessage.equals("1")){
				sendMessageQYWX(oldPro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
        }else {
			if(passportApply.getTypeProve().equals("普通护照通行证(中+英版)")){
				passportApply.setPassportId(passportId_temp);
			}
            ProcessList pro=passportApply.getProId();
			passportApply.setStateCertificate("未领取");
			passportApply.setTakeCertificateTime("未领取");
			passportApply.setReturnCertificateTime("未领取");
			passportApply.setHandleType("申领");
            pro.setProcessName(passportApply.getDestination()+": "+passportApply.getApplyReason());
            pro.setStartTime(passportApply.getStartTime());
            pro.setEndTime(passportApply.getEndTime());
            pro.setProcessDescribe(passportApply.getTypeProve()+'【'+ passportApply.getIsPrint()+'】');
            pro.setHandleType("申领");
            //proservice.index8(pro, val, lu,shen.getUserName());//使用以下几行来代替index8函数
			pro.setTypeNmae(val);
			pro.setApplyTime(passportApply.getApplyTime());
			pro.setUserId(lu);
			pro.setStatusId(23L);
			pro.setShenuser(shen.getUserName());
			passportApplyDao.save(passportApply);
			//存审核表
			proservice.index7_new(shen,lu,pro,passportApply);

            //接待消息通知到下级审核人
			if(isSendMessage.equals("1")){
				sendMessageQYWX(pro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
        }
        return "redirect:/flowmanagePPP";

    }


	/**
	 * @author: winper001
	 * @create: 2020-06-11
	 * @description: 出入境申请提交【办理】
	 */

	@RequestMapping("passportapplysubmit_handle")
	public String passportapplysubmit_handle(HttpServletRequest req,@Valid PassportApply passportApply,BindingResult br,
									  @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, ParseException{

		User lu=udao.getOne(userId);//申请人
		//String personName=lu.getUserName()+"/"+lu.getRealName();
		String xzry=passportApply.getNameuser();
		//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
		User shen = xzry==null? null: (udao.findByUserName(xzry.split("/")[0]));//审核人

		String val=req.getParameter("val");

		if(StringUtil.objectIsNull(passportApply.getPassportApplyId())) {
			PassportApply oldPassportApply=passportApplyDao.findById(passportApply.getPassportApplyId()).orElse(null);

			ProcessList oldPro=oldPassportApply.getProId();

			passportApply.setStateCertificate("未领取");
			passportApply.setTakeCertificateTime("未领取");
			passportApply.setReturnCertificateTime("未领取");
			oldPro.setProcessName("办理"+": "+passportApply.getTypeCertificate());
			oldPro.setStartTime(passportApply.getStartTime());
			oldPro.setEndTime(passportApply.getEndTime());
			oldPro.setProcessDescribe(passportApply.getTypeProve()+'【'+ passportApply.getIsPrint()+'】');
			proservice.index8(oldPro, val, lu,shen.getUserName());
			passportApply.setProId(oldPro);
			passportApplyDao.save(passportApply);
			//存审核表
			//删除审核记录
			List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
			if(!Objects.isNull(rev)){
				redao.deleteAll(rev);
			}
			proservice.index7(shen, oldPro);

			//接待消息通知到下级审核人
			if(isSendMessage.equals("1")){
				sendMessageQYWX(oldPro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
		}else {

			ProcessList pro=passportApply.getProId();
			passportApply.setStateCertificate("未领取");
			passportApply.setTakeCertificateTime("未领取");
			passportApply.setReturnCertificateTime("未领取");
			passportApply.setHandleType("申办");
			pro.setProcessName("办理"+": "+passportApply.getTypeCertificate());
			pro.setStartTime(passportApply.getStartTime());
			pro.setEndTime(passportApply.getEndTime());
			pro.setProcessDescribe(passportApply.getTypeProve()+'【'+ passportApply.getIsPrint()+'】');
			pro.setHandleType("申办");
			//proservice.index8(pro, val, lu,shen.getUserName());//使用以下几行来代替index8函数
			pro.setTypeNmae(val);
			pro.setApplyTime(passportApply.getApplyTime());
			pro.setUserId(lu);
			pro.setStatusId(23L);
			pro.setShenuser(shen.getUserName());
			passportApplyDao.save(passportApply);
			//存审核表
			proservice.index7_new(shen,lu,pro,passportApply);

			//接待消息通知到下级审核人
			if(isSendMessage.equals("1")){
				sendMessageQYWX(pro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
		}
		return "redirect:/flowmanagePPP";

	}

    /**
     * @author: winper001
     * @create: 2020-03-03 15:40
     * @description: 出入境证明
     */
    @RequestMapping("certificate")
    public String certificate(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req)
            throws IllegalStateException, IOException, ParseException{
        User user=udao.getOne(userId);

        Long proid=Long.parseLong(req.getParameter("id"));
        String typeProve=req.getParameter("flag");//这里flag是证ming类型
        //String typeProve_temp=req.getParameter("flag").split("【")[1];
		Long curPage=Long.parseLong(req.getParameter("curPage"));
		String itemType=req.getParameter("itemType");
        ProcessList process=prodao.findById(proid).orElse(null);
        PassportApply passportApply=passportApplyDao.findByProId(process);

        model.addAttribute("curPage", curPage);
		model.addAttribute("itemType", itemType);
        model.addAttribute("process", process);
		model.addAttribute("passportApply", passportApply);
        model.addAttribute("applyname",passportApply.getApplyPersonName().split("/")[1]);
        String sex=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getSex();
        String position=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getPosition().getName();
        Date birth=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getBirth();
        String idcard=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getIdCard();
        String deptName=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getDept().getDeptName();

        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String birthYMD="";
        if(birth!=null){
            birthYMD=df.format(birth);
        }
        String chenghu="",chenghu_Eng="",chenghu_Eng2="",chenghu_Eng3="";
		if(sex.equals("男")){
			chenghu="他";
			chenghu_Eng="he";
			chenghu_Eng2="himself";
			chenghu_Eng3="his";
			passportApply.setSexEnglish("Man");
		}else{
			chenghu="她";
			chenghu_Eng="she";
			chenghu_Eng2="herself";
			chenghu_Eng3="her";
			passportApply.setSexEnglish("Woman");
		}
        String salary=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getSalary();
        model.addAttribute("ryzb",StringUtil.stringIsNotNull(user.getRyzb())?user.getRyzb():""  );
        model.addAttribute("sex",sex);
        model.addAttribute("position", position);
        model.addAttribute("birthYMD", birthYMD);//看人事科是否需要是直接从表直接调用（表里有的人是缺失出生年月的），还是自己填写
        model.addAttribute("startTime", df.format(process.getStartTime()));
        model.addAttribute("endTime", df.format(process.getEndTime()));
        model.addAttribute("salary", salary);
        model.addAttribute("idCard", idcard);
        model.addAttribute("department", deptName);
        model.addAttribute("position", position);
        model.addAttribute("proid",proid);
        //model.addAttribute("flag1",typeProve_temp);
        model.addAttribute("curPage",curPage);
        model.addAttribute("itemType", itemType);
        model.addAttribute("chenghu",chenghu);
        model.addAttribute("chenghu_Eng",chenghu_Eng);
        model.addAttribute("chenghu_Eng2",chenghu_Eng2);
        model.addAttribute("chenghu_Eng3", chenghu_Eng3);

        List<Reviewed> reviewedList=redao.findByProId(proid);
		String finalRevTime=null;
        if(reviewedList.get(reviewedList.size()-1).getReviewedTime() !=null)
		{
			finalRevTime=df.format(reviewedList.get(reviewedList.size()-1).getReviewedTime());
		}
        model.addAttribute("finalRevTime",finalRevTime);//院长书记审核时间
        Date hireTime=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getHireTime();
        String hireTime_t=df.format(hireTime).substring(0, 4);
        String nowTime=df.format(new Date()).substring(0, 4);
        Long workTime=Long.parseLong(nowTime)-Long.parseLong(hireTime_t)+1L;
        model.addAttribute("workTime", workTime);
        if(typeProve.equals("港澳/台湾通行证")){
        	return "process/certificate_gat";
		}else if(typeProve.equals("普通护照通行证(中文版)")){
        	return "process/certificate_cn";
		}else{
			return "process/certificate_en";
		}
    }

/**
 * @author: winper001
 * @create 2020-03-16:
 * @description: 回执单
*/

	@RequestMapping("huizhi")
	public String huizhi(Model model,HttpServletRequest req)
						throws IllegalStateException,IOException,ParseException{

		Long proid=Long.parseLong(req.getParameter("id"));
		Long curPage=Long.parseLong(req.getParameter("curPage"));
		String itemType=req.getParameter("itemType");

		ProcessList process=prodao.findById(proid).orElse(null);
		PassportApply passportApply=passportApplyDao.findByProId(process);
		String position=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getPosition().getName();


		model.addAttribute("curPage",curPage);
		model.addAttribute("itemType",itemType);
		model.addAttribute("passportApply", passportApply);
		model.addAttribute("process", process);
		model.addAttribute("position", position);
		model.addAttribute("applyname",passportApply.getApplyPersonName().split("/")[1]);

		return "process/passport_huizhi";
	}

	/**
	 * @author: winper001
	 * @create 2020-03-16:
	 * @description: 回执单提交
	 */
	@RequestMapping("huizhi_submit")
	public String huizhi_submit(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req) //@Valid PassportApply passportApply,BindingResult br
								throws IllegalStateException,IOException,ParseException{
		Long processId=Long.parseLong(req.getParameter("process_id"));
		String certificateValidStartTime=req.getParameter("certificateValidStartTime");
		String certificateValidEndTime=req.getParameter("certificateValidEndTime");
		String actualStartTime=req.getParameter("actualStartTime");
		String actualEndTime=req.getParameter("actualEndTime");
		Long page=Long.parseLong(req.getParameter("page"));
		String itemType=req.getParameter("itemType");
		ProcessList process=prodao.findById(processId).orElse(null);
		PassportApply passportApply=passportApplyDao.findByProId(process);

		passportApply.setCertificateValidStartTime(certificateValidStartTime);
		passportApply.setCertificateValidEndTime(certificateValidEndTime);
		passportApply.setActualStartTime(actualStartTime);
		passportApply.setActualEndTime(actualEndTime);
		passportApplyDao.save(passportApply);

		String position=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getPosition().getName();

		model.addAttribute("passportApply", passportApply);
		model.addAttribute("process", process);
		model.addAttribute("position", position);
		model.addAttribute("applyname",passportApply.getApplyPersonName().split("/")[1]);
		model.addAttribute("curPage",page);
		model.addAttribute("itemType",itemType);

		return "process/passport_huizhi";

	}


	/**
	 * 查找出自己的申请
	 * @return
	 */
	@RequestMapping("flowmanagePPP")
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
				shryZW=shryZW+udao.findByUserName(shrys[j]).getUserName()+'/'+udao.findByUserName(shrys[j]).getRealName()+";";
			}
			prolist.get(i).setShenuser(shryZW);

		}
		Iterable<SystemStatusList>  statusname=sdao.findByStatusModel("aoa_process_list");
		Iterable<SystemTypeList> typename=tydao.findByTypeModel("aoa_process_list");
		model.addAttribute("typename", typename);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("statusname", statusname);
		model.addAttribute("url", "shenserPPP");
		return "process/flowmanagePPP";
	}
	/**
	 * 搜索【我的申请】
	 */
	@RequestMapping("shenserPPP")
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
		for(int i = 0 ;i<prolist.size();i++) {
			String shry=prolist.get(i).getShenuser();

			String [] shrys=shry.split(";");
			String shryZW="";
			for(int j=0;j<shrys.length;j++) {
				shryZW=shryZW+udao.findByUserName(shrys[j]).getUserName()+'/'+udao.findByUserName(shrys[j]).getRealName()+";";
			}
			prolist.get(i).setShenuser(shryZW);

		}
		Iterable<SystemStatusList>  statusname=sdao.findByStatusModel("aoa_process_list");
		Iterable<SystemTypeList> typename=tydao.findByTypeModel("aoa_process_list");
		model.addAttribute("typename", typename);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("statusname", statusname);
		model.addAttribute("url", "shenserPPP");

		return "process/managetablePPP";
	}
	/**
	 * 搜索【流程审核】
	 * @return
	 */
	@RequestMapping("serchPPP")
	public String serch(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
						@RequestParam(value = "page", defaultValue = "0") int page,
						@RequestParam(value = "size", defaultValue = "10") int size){
		User user=udao.getOne(userId);//审核人
		Page<AubUser> pagelist=null;
		String val=null;
		if(!TextUtil.isNullOrEmpty(req.getParameter("val"))){
			val=req.getParameter("val");
		}
		if(StringUtil.stringIsNotNull(user.getRyzb()) && user.getRyzb().equals("人事科审核确认组")){//新增的代码：解决人事科审核确认组人员的流程审核搜索
			pagelist=proservice.index_HrToSearch(user,page,size,val,model);
		}else{
			pagelist=proservice.index(user, page, size,val,model);
		}
		List<Map<String, Object>> prolist=proservice.index2(pagelist,user);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("url", "serchPPP");
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(user.getRyzb())?user.getRyzb():""  );
		return "process/audtablePPP";
	}
	/**
	 * 流程审核
	 * @return
	 */
	@RequestMapping("auditPPP")
	public String auding(@SessionAttribute("userId") Long userId,Model model,
						 @RequestParam(value = "page", defaultValue = "0") int page,
						 @RequestParam(value = "size", defaultValue = "10") int size){
		User user=udao.getOne(userId);
		Page<AubUser> pagelist=null;
		if(StringUtil.stringIsNotNull(user.getRyzb()) && user.getRyzb().equals("人事科审核确认组")){
			pagelist=proservice.index_HrToSearch(user,page,size,null,model);
		}else{
			pagelist=proservice.index(user, page, size,null,model);
		}
		List<Map<String, Object>> prolist=proservice.index2(pagelist,user);
		model.addAttribute("page", pagelist);
		model.addAttribute("prolist", prolist);
		model.addAttribute("url", "serchPPP");
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(user.getRyzb())?user.getRyzb():""  );
		model.addAttribute("applyperson_pos_id", user.getPosition().getId());

		return "process/auditingPPP";
	}

	/**
	 * 查看详细
	 * @return
	 */
	@RequestMapping("particularPPP")
	public String particular(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req){
		User user=udao.getOne(userId);//审核人或者申请人
		User audit=null;//最终审核人
		String id=req.getParameter("id");
		Long proid=Long.parseLong(id);
		String typename=req.getParameter("typename");//类型名称
		Long curPage=Long.parseLong(req.getParameter("curPage"));
		String itemType=req.getParameter("itemType");
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

		if(("出入境申请").equals(typename)) {
			PassportApply eve = passportApplyDao.findByProId(process);
			int count_hk=passportApplyDao.findByPersonNameAndDestination_finished(eve.getApplyPersonName(),"香港").size();
			int count_ao=passportApplyDao.findByPersonNameAndDestination_finished(eve.getApplyPersonName(),"澳门").size();
			int count_tw=passportApplyDao.findByPersonNameAndDestination_finished(eve.getApplyPersonName(),"台湾").size();
			int count_foreign=passportApplyDao.findByPersonNameAndDestination_foreign_finished(eve.getApplyPersonName()).size();
			if(process.getStatusId()==34L || process.getStatusId()==35L){
				String HrStatus=sdao.findname(process.getStatusId());
				model.addAttribute("HrStatus",HrStatus);
			}else{
				model.addAttribute("HrStatus", "未操作");
			}
			//String stateCertificate=eve.getStateCertificate();
			//model.addAttribute("stateCertificate", stateCertificate);
			if (StringUtil.stringIsNotNull(eve.getKskz_advice())) {
				model.addAttribute("kskz", eve.getKskz_advice().toString().split(",")[0]+"【"+eve.getKskz_advice().toString().split(",")[1]+"】");
			} else {

				model.addAttribute("kskz","未审批");
			}
			if (StringUtil.stringIsNotNull(eve.getHr_advice())) {
				model.addAttribute("hr", eve.getHr_advice().toString().split(",")[0]+"【"+eve.getHr_advice().toString().split(",")[1]+"】");
			} else {
				model.addAttribute("hr", "未审批");
			}
			if (StringUtil.stringIsNotNull(eve.getZgld_advice())) {
				model.addAttribute("zgld", eve.getZgld_advice().toString().split(",")[0]+"【"+eve.getZgld_advice().toString().split(",")[1]+"】");
			} else {
				model.addAttribute("zgld", "未审批");
			}
			if (StringUtil.stringIsNotNull(eve.getYzdwsjAdvice())) {
				model.addAttribute("yzdwsj", eve.getYzdwsjAdvice().toString().split(",")[0]+"【"+eve.getYzdwsjAdvice().toString().split(",")[1]+"】");
			} else {
				model.addAttribute("yzdwsj", "未审批");
			}
			if(StringUtil.stringIsNotNull(eve.getHr_zxry_advice())){
				model.addAttribute("hr_zxry_advice",eve.getHr_zxry_advice());
			}
			else {
				model.addAttribute("hr_zxry_advice", "未确认");
			}
			model.addAttribute("eve", eve);
			model.addAttribute("map", map);
			model.addAttribute("curPage", curPage);
			model.addAttribute("itemType", itemType);
			model.addAttribute("count_hk",count_hk);
			model.addAttribute("count_ao", count_ao);
			model.addAttribute("count_tw",count_tw);
			model.addAttribute("count_foreign",count_foreign);

			return "process/passportapplysearch";
		}
		return "process/serchPPP";
	}
	/**
	 * 进入审核页面
	 * @return
	 */
	@RequestMapping("auditingPPP")
	public String auditing(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size){
		User u=udao.getOne(userId);
		//流程id
		Long id=Long.parseLong(req.getParameter("id"));
		Long curPage=Long.parseLong(req.getParameter("curPage"));
		ProcessList process=prodao.findById(id).orElse(null);
		PassportApply passportApply=passportApplyDao.findByProId((process));
		String applyPersonNum=passportApply.getApplyPersonName().split("/")[0];
		String applyPersonPosId=udao.findByUserName(applyPersonNum).getPosition().getId().toString();
		String applyPersonDeptId=udao.findByUserName(applyPersonNum).getDept().getDeptId().toString();
		int count_hk=passportApplyDao.findByPersonNameAndDestination_finished(passportApply.getApplyPersonName(),"香港").size();
		int count_ao=passportApplyDao.findByPersonNameAndDestination_finished(passportApply.getApplyPersonName(),"澳门").size();
		int count_tw=passportApplyDao.findByPersonNameAndDestination_finished(passportApply.getApplyPersonName(),"台湾").size();
		int count_foreign=passportApplyDao.findByPersonNameAndDestination_foreign_finished(passportApply.getApplyPersonName()).size();
		int count_all=passportApplyDao.findByPersonNameAll_finished(passportApply.getApplyPersonName()).size();
		Reviewed re=redao.findByProIdAndUserId(process.getProcessId(), u);//查找审核表
		String typename=process.getTypeNmae().trim();
		if(("出入境申请").equals((typename))){
			proservice.indexPassportApply(model,userId,page,size);
			if((!applyPersonPosId.equals("6")&& !applyPersonDeptId.equals("103")) &&(u.getUserName().equals("920") || u.getUserName().equals("982") || u.getUserName().equals("195008"))){
				String defaShen=passportApply.getFinalChecker();
				model.addAttribute("defaShen", defaShen);
			}
			model.addAttribute("passportApply",passportApply);
			model.addAttribute("f_iswcmsg",StringUtil.stringIsNotNull(passportApply.getF_iswcmsg())? passportApply.getF_iswcmsg(): "");
			model.addAttribute("bz",StringUtil.stringIsNotNull(passportApply.getBz())? passportApply.getBz(): "");
			model.addAttribute("zxry",StringUtil.stringIsNotNull(passportApply.getZxry())? passportApply.getZxry(): "");
			model.addAttribute("count_hk",count_hk);
			model.addAttribute("count_ao", count_ao);
			model.addAttribute("count_tw",count_tw);
			model.addAttribute("count_foreign",count_foreign);
			model.addAttribute("count_all",count_all);
		}
		List<Map<String, Object>> list=proservice.index4(process);
        //Page<AubUser> pagelist=proservice.index(u, page, size,null,model);
        //model.addAttribute("page",pagelist);
        model.addAttribute("curPage", curPage);
		model.addAttribute("statusid", process.getStatusId());
		model.addAttribute("process", process);
		model.addAttribute("revie", list);
		model.addAttribute("size", list.size());
		if(StringUtil.stringIsNotNull(u.getRyzb())&&(u.getRyzb().equals("监察组") || u.getRyzb().equals("人事科审核确认组"))) {//是人事科审核确认组的话
			model.addAttribute("ustatusid", process.getStatusId());

		}else {
			model.addAttribute("ustatusid", re.getStatusId());
		}
		model.addAttribute("positionid",u.getPosition().getId());
		model.addAttribute("typename", typename);
		model.addAttribute("userName", u.getUserName());
		model.addAttribute("ryzb", StringUtil.stringIsNotNull(u.getRyzb())?u.getRyzb():""  );
		model.addAttribute("applyPersonPosId",applyPersonPosId);
		model.addAttribute("deptid", u.getDept().getDeptId());

		return "process/audetailPPP";

	}
	/**
	 * 审核确定的页面
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("susavePPP")
	public String save(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,Reviewed reviewed) throws ParseException, IOException{
		User u=udao.getOne(userId);
		String name=null;
        System.out.println(reviewed);
		String typename=req.getParameter("type");
		Long proid=Long.parseLong(req.getParameter("proId"));


		ProcessList pro=prodao.findById(proid).orElse(null);//找到该条流程
		PassportApply passportApply=passportApplyDao.findByProId(pro);

		User shen=udao.getOne(pro.getUserId().getUserId());//申请人
		if(!TextUtil.isNullOrEmpty(req.getParameter("liuzhuan"))){
			name=req.getParameter("liuzhuan");
		}
		if(!TextUtil.isNullOrEmpty(name)){
			//审核并流转
			String xzry=reviewed.getUsername();//传过来的审核人姓名
			User u2 = null;//u2是下一步的审核人
			if(StringUtil.stringIsNotNull(xzry)) {
				//前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题

				String[] xzrys = xzry.split(";");
				for(int i = 0 ;i<xzrys.length;i++) {
					if(xzrys[i].trim().length()>0) {
						u2=udao.findByUserName(xzrys[i].split("/")[0]);
					}
				}
			}
			if(StringUtil.stringIsNotNull(req.getParameter("final_checker"))){
				passportApply.setFinalChecker(req.getParameter("final_checker"));
			}
			if(("出入境申请").equals(typename)){
				Long statusid=Long.parseLong(StringUtil.stringIsNotNull(req.getParameter("statusId"))? req.getParameter("statusId"):"34");//如果审核确认组的审核状态是【已完成34，则statusId会为null】
			    if(StringUtil.stringIsNotNull(u.getRyzb()) && (u.getRyzb().equals("人事科审核确认组"))){

			    	String f_iswcmsg=req.getParameter("f_iswcmsg");
			    	String bz=req.getParameter("bz");
			    	String stateCertificate=req.getParameter("stateCertificate");
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar calen = Calendar.getInstance();
			    	passportApply.setF_iswcmsg(f_iswcmsg);
			    	passportApply.setBz(bz);
			    	passportApply.setZxry((u.getRealName()));
			    	if(stateCertificate==null){
						passportApply.setStateCertificate("未领取");//默认情况下为"未领取"
					}else if(stateCertificate.equals("领取,未还")){
			    		passportApply.setTakeCertificateTime(dateFormat.format(new Date()));
						passportApply.setStateCertificate(stateCertificate);
					}else if(stateCertificate.equals("领取,已还") || stateCertificate.equals("已还,未出(国)境")){
						passportApply.setReturnCertificateTime(dateFormat.format(new Date()));
						passportApply.setStateCertificate(stateCertificate);
					}
					if(!passportApply.getTakeCertificateTime().equals("未领取") && !passportApply.getReturnCertificateTime().equals("未领取")){
						Date takeTime=dateFormat.parse(passportApply.getTakeCertificateTime());
						Date returnTime=dateFormat.parse(passportApply.getReturnCertificateTime());
						calen.setTime(returnTime);
						int interval_days=(int) ((takeTime.getTime() - returnTime.getTime()) / (24 * 60 * 60 * 1000));//相隔的天数
						//int interval_days=(int) ((returnTime.getTime() - takeTime.getTime()) / (60 * 60* 1000));//相隔的小时数
						//if(interval_days>1){//相隔大于1分钟
						if(interval_days>30 && interval_days<=60){//30天到60天
							calen.add(Calendar.DAY_OF_YEAR,180);
							//calen.add(Calendar.MINUTE,1);//增加一分钟
							Date temp=calen.getTime();
							shen.setLimitDate(temp);//封号6个月
						}else if(interval_days>60){
							calen.add(Calendar.DAY_OF_YEAR,360);
							//calen.add(Calendar.MINUTE,1);//增加1分钟
							Date temp=calen.getTime();
							shen.setLimitDate(temp);//封号12个月
						}
						udao.save(shen);//保存aoa_user表

					}
					pro.setStatusId(statusid);
			    	if(statusid==34L){
			    		passportApply.setHr_zxry_advice("已完成");
					}else if(statusid==35L){
			    		passportApply.setHr_zxry_advice("无法完成");
					}
			    	prodao.save(pro);
			    	if(f_iswcmsg.equalsIgnoreCase("1")){
			    		MessageController messageController=new MessageController();
			    		String content="<div class=\"gray\">"+ DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
								+ "<div  class=\"blue\">"+bz+"</div>";
						messageController.sengCardMessage(pro.getUserId().getUserName(), "", "", pro.getTypeNmae()+"通知",content,moblieurl+"pages/passport/passport-index?usercode="+pro.getUserId().getUserName());
					}

				}else{//如果不是人事科管理员
			    	if(u2==null){
			    		model.addAttribute("error", "请选择审核人员！");
						return "common/proce";
					}
			    	if(u.getPosition().getId().equals(7L)){
			    		if(u2.getPosition().getId().equals(6L)){
			    			proservice.save(proid,u,reviewed,pro,u2);
						}else{
			    			model.addAttribute("error", "请您选择科长！");
			    			return "common/proce";
						}
					}else{
			    		proservice.save(proid,u,reviewed,pro,u2);
					}
					if(statusid==26L){//如果某一个关节审核不通过，则状态即为“未通过【26L】”
						reviewed.setStatusId(26L);
						pro.setStatusId(26L);
					}
					if(isSendMessage.equals("1")) {
						sendMessageQYWX(pro, passportApply, passportApply.getDestination() + ":" + passportApply.getApplyReason(), u2);
					}
				}
            }

			else{
				if(u2.getPosition().getId().equals(7L)){//审核人是科员
					proservice.save(proid, u, reviewed, pro, u2);
				}else{
					model.addAttribute("error", "请选择正确人员审核！");
					return "common/proce";
				}
			}

		}else {
			//审核并结案
			Reviewed re = redao.findByProIdAndUserId(proid, u);
			re.setAdvice(reviewed.getAdvice());
			re.setStatusId(reviewed.getStatusId());
			re.setReviewedTime(new Date());
			pro.setStatusId(reviewed.getStatusId());//改变主表的状态
			redao.save(re);
			prodao.save(pro);
			if (("出入境申请").equals(typename)) {
				User user1 = udao.findByUserName("195005");
				//User user2 = udao.findByUserName("1983");
				if(isSendMessage.equals("1")) {
					sendMessageQYWX(pro, passportApply, pro.getProcessName(), user1);
				}
			}
		}
			if(u.getPosition().getId().equals(6L) &&u.getDept().getDeptId()!=103L){
				passportApply.setKskz_advice(reviewed.getAdvice()+","+u.getRealName());
			}else if(u.getPosition().getId().equals(6L) &&u.getDept().getDeptId()==103L){
				passportApply.setHr_advice(reviewed.getAdvice()+","+u.getRealName());
			}else if(u.getPosition().getId().equals(3L)  && !u.getUserName().equals("195777")){
				passportApply.setZgld_advice(reviewed.getAdvice()+","+u.getRealName());
			}else if(u.getRyzb().equals("院长/纪委书记")){
				passportApply.setYzdwsjAdvice(reviewed.getAdvice()+","+u.getRealName());

			}
			passportApplyDao.save(passportApply);

		return "redirect:/auditPPP";
	}


	/**
	 * 删除记录
	 */
	@RequestMapping("processdeletePPP")
	public String processdelete(HttpServletRequest req,Model model){
		Long proid=Long.parseLong(req.getParameter("id"));
		ProcessList process=prodao.findById(proid).orElse(null);

		String itemType="";
		Long curPage=null;
		if(process.getTypeNmae().equals("出入境申请")){
			itemType=String.valueOf(req.getParameter("itemType"));
			curPage=Long.parseLong(req.getParameter("curPage"));
			curPage=curPage-1L;
		}

		//删除审核记录
		List<Reviewed> rev=redao.findByProId(proid);
		if(!Objects.isNull(rev)){
			redao.deleteAll(rev);
		}
		if(process.getTypeNmae().equals("出入境申请")){
			PassportApply passportApply=passportApplyDao.findByProId(process);
			if(!Objects.isNull(passportApply)){
				passportApplyDao.delete(passportApply);
			}
		}
		prodao.delete(process);
		return "redirect:/"+itemType+"?page="+curPage.toString();
/*		if(itemType.equals("audit")){
			return "redirect:/auditPPP";
		}else {
			return "redirect:/flowmanagePPP";
		}*/

	}

	private boolean sendMessageQYWX(ProcessList process,PassportApply passportApply,String lfsyAndRy,User shenry) throws ParseException, IOException {
		//接待消息通知到下级审核人
		MessageController messageController = new MessageController();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(process.getTypeNmae().equals("出入境申请")){
			String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy年MM月dd日")+"</div> "
					+ "<div  class=\"blue\">"+"申请人："+passportApply.getApplyPersonName()+"</div>"
					+ "<div  class=\"blue\">"+"业务类型："+passportApply.getHandleType()+"</div>"
					+ "<div  class=\"blue\">"+"申请日期："+df2.format(passportApply.getApplyTime())+"</div>";
					
					if(process.getHandleType().equalsIgnoreCase("申领")) {
						content+="<div  class=\"blue\">"+"出境事由："+lfsyAndRy+"</div>";
						content+="<div  class=\"blue\">"+"出境时间："+df.format(passportApply.getStartTime())+" 至 "+df.format(passportApply.getEndTime())+"</div>";
					}

			messageController.sengCardMessage(shenry.getUserName(), "", "", "出国(境)申请[电脑]",content,
					moblieurl+"pages/passport/passport-detail?"+
							"process_id=" + process.getProcessId()+"&usercode_passport="+shenry.getUserName()+"&type_name="+"出入境申请"+"&jllx="+"正常"+"&detailtype=lcsp" );
			
			
			
			
					
					
					
		}
		return true;
	}
	
	
	/**
	 * @author: winper001
	 * @create: 2020-01-15 21:25
	 * @description: 出入境申请
	*/
		@RequestMapping("passportdatatables")
		public String passportdatatables(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
								   @RequestParam(value = "page", defaultValue = "0") int page,
								   @RequestParam(value = "size", defaultValue = "10") int size)
		{
			
			List<HashMap<String,String>> fieldList=new ArrayList<HashMap<String,String>>();
			HashMap<String,String> fieldMap=new HashMap<String,String>();

			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","申请人");
			fieldList.add(fieldMap);
			
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","申请时间");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","科室");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","职务");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","证件类型");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","是否打印");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","目的地");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","出境事由");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","开始时间");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","结束时间");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","领取时间");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","归还时间");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","科室科长");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","人事科科长");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","主管院领导");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","院长/党委书记");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","执行人员");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","证件状态");
			fieldList.add(fieldMap);
			
//			proservice.indexPassportApply(model, userId, page, size);
//			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			if((StringUtil.stringIsNotNull(request.getParameter("id")))) {//编辑
//				Long proid=Long.parseLong(request.getParameter("id"));
//				Long curPage=Long.parseLong(request.getParameter("curPage"));
//				//String itemType=request.getParameter("itemType");
//				ProcessList processList=prodao.findbyuseridandtitle(proid);
//				PassportApply passportApply=passportApplyDao.findByProId(processList);
//
//				model.addAttribute("passportApply", passportApply);
//				model.addAttribute("process", processList);
//				model.addAttribute("curPage",curPage);
//				//model.addAttribute("itemType", itemType);
//
//			}
//			String nowTime=dateFormat.format(new Date());
			List<PassportApply> passportApplyList=(List<PassportApply>) passportApplyDao.findAll();
			
			
			model.addAttribute("fieldList", fieldList);
			model.addAttribute("passportApplyList", passportApplyList);
			return "process/passportdatatables";
		}
	
}


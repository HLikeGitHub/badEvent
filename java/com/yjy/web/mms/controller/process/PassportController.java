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

	@Value("1")//??????????????????("0"/"1")
	private String isSendMessage;



/**
 * @author: winper001
 * @create: 2020-01-15 21:25
 * @description: ???????????????????????????
*/
	@RequestMapping("passportapply")
	public String passportapply(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
							   @RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "10") int size)
	{
		proservice.indexPassportApply(model, userId, page, size);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy???MM???dd???");
		if((StringUtil.stringIsNotNull(request.getParameter("id")))) {//??????
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
		int count_hk=passportApplyDao.findByPersonNameAndDestination_finished(personName,"??????").size();
		int count_ao=passportApplyDao.findByPersonNameAndDestination_finished(personName,"??????").size();
		int count_tw=passportApplyDao.findByPersonNameAndDestination_finished(personName,"??????").size();
		int count_foreign=passportApplyDao.findByPersonNameAndDestination_foreign_finished(personName).size();
		int count_all=passportApplyDao.findByPersonNameAll_finished(personName).size();
		model.addAttribute("count_hk",count_hk);
		model.addAttribute("count_ao", count_ao);
		model.addAttribute("count_tw",count_tw);
		model.addAttribute("count_foreign",count_foreign);
		model.addAttribute("count_all",count_all);
		if(applyPer.getLimitDate()!=null && new Date().getTime()<=applyPer.getLimitDate().getTime()){//??????????????????????????????????????????
			String limitDate=dateFormat2.format(applyPer.getLimitDate());
			model.addAttribute("error", limitDate);
			return "common/passport_hint";
		}
		return "process/passportapply";
	}

    /**
     * @author: winper001
     * @create: 2020-01-15 21:25
     * @description: ???????????????????????????
     */
    @RequestMapping("passportapply_handle")
    public String passportapply_handle(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size)
    {
        proservice.indexPassportApply(model, userId, page, size);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy???MM???dd???");
        if((StringUtil.stringIsNotNull(request.getParameter("id")))) {//??????
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
	 * @description: ?????????????????????????????????
	*/

    @RequestMapping("passportapplysubmit")
    public String passportapplysubmit(@RequestParam("filePath")MultipartFile[] filePath,HttpServletRequest req,@Valid PassportApply passportApply,BindingResult br,
                                       @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, ParseException{

        User lu=udao.getOne(userId);//?????????
 		//String personName=lu.getUserName()+"/"+lu.getRealName();
        String xzry=passportApply.getNameuser();
        //????????????????????????????????????????????? ??????/???????????????????????? ?????????????????????????????????
		User shen = xzry==null? null: (udao.findByUserName(xzry.split("/")[0]));//?????????

        String val=req.getParameter("val");
        //????????????????????????
		String passportId_temp=null;

		if(passportApply.getTypeProve().equals("?????????????????????(???+??????)")){
			passportId_temp=req.getParameter("passportId_temp");

		}

        if(StringUtil.objectIsNull(passportApply.getPassportApplyId())) {
            PassportApply oldPassportApply=passportApplyDao.findById(passportApply.getPassportApplyId()).orElse(null);

            ProcessList oldPro=oldPassportApply.getProId();
			if(passportApply.getTypeProve().equals("?????????????????????(???+??????)")){
				passportApply.setPassportId(passportId_temp);
			}

			passportApply.setStateCertificate("?????????");
			passportApply.setTakeCertificateTime("?????????");
			passportApply.setReturnCertificateTime("?????????");
            oldPro.setProcessName(passportApply.getDestination()+": "+passportApply.getApplyReason());
			oldPro.setStartTime(passportApply.getStartTime());
			oldPro.setEndTime(passportApply.getEndTime());
			oldPro.setProcessDescribe(passportApply.getTypeProve()+'???'+ passportApply.getIsPrint()+'???');
            proservice.index8(oldPro, val, lu,shen.getUserName());
            passportApply.setProId(oldPro);
            passportApplyDao.save(passportApply);
            //????????????
            //??????????????????
            List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
            if(!Objects.isNull(rev)){
                redao.deleteAll(rev);
            }
            proservice.index7(shen, oldPro);

            //????????????????????????????????????
			if(isSendMessage.equals("1")){
				sendMessageQYWX(oldPro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
        }else {
			if(passportApply.getTypeProve().equals("?????????????????????(???+??????)")){
				passportApply.setPassportId(passportId_temp);
			}
            ProcessList pro=passportApply.getProId();
			passportApply.setStateCertificate("?????????");
			passportApply.setTakeCertificateTime("?????????");
			passportApply.setReturnCertificateTime("?????????");
			passportApply.setHandleType("??????");
            pro.setProcessName(passportApply.getDestination()+": "+passportApply.getApplyReason());
            pro.setStartTime(passportApply.getStartTime());
            pro.setEndTime(passportApply.getEndTime());
            pro.setProcessDescribe(passportApply.getTypeProve()+'???'+ passportApply.getIsPrint()+'???');
            pro.setHandleType("??????");
            //proservice.index8(pro, val, lu,shen.getUserName());//???????????????????????????index8??????
			pro.setTypeNmae(val);
			pro.setApplyTime(passportApply.getApplyTime());
			pro.setUserId(lu);
			pro.setStatusId(23L);
			pro.setShenuser(shen.getUserName());
			passportApplyDao.save(passportApply);
			//????????????
			proservice.index7_new(shen,lu,pro,passportApply);

            //????????????????????????????????????
			if(isSendMessage.equals("1")){
				sendMessageQYWX(pro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
        }
        return "redirect:/flowmanagePPP";

    }


	/**
	 * @author: winper001
	 * @create: 2020-06-11
	 * @description: ?????????????????????????????????
	 */

	@RequestMapping("passportapplysubmit_handle")
	public String passportapplysubmit_handle(HttpServletRequest req,@Valid PassportApply passportApply,BindingResult br,
									  @SessionAttribute("userId") Long userId) throws IllegalStateException, IOException, ParseException{

		User lu=udao.getOne(userId);//?????????
		//String personName=lu.getUserName()+"/"+lu.getRealName();
		String xzry=passportApply.getNameuser();
		//????????????????????????????????????????????? ??????/???????????????????????? ?????????????????????????????????
		User shen = xzry==null? null: (udao.findByUserName(xzry.split("/")[0]));//?????????

		String val=req.getParameter("val");

		if(StringUtil.objectIsNull(passportApply.getPassportApplyId())) {
			PassportApply oldPassportApply=passportApplyDao.findById(passportApply.getPassportApplyId()).orElse(null);

			ProcessList oldPro=oldPassportApply.getProId();

			passportApply.setStateCertificate("?????????");
			passportApply.setTakeCertificateTime("?????????");
			passportApply.setReturnCertificateTime("?????????");
			oldPro.setProcessName("??????"+": "+passportApply.getTypeCertificate());
			oldPro.setStartTime(passportApply.getStartTime());
			oldPro.setEndTime(passportApply.getEndTime());
			oldPro.setProcessDescribe(passportApply.getTypeProve()+'???'+ passportApply.getIsPrint()+'???');
			proservice.index8(oldPro, val, lu,shen.getUserName());
			passportApply.setProId(oldPro);
			passportApplyDao.save(passportApply);
			//????????????
			//??????????????????
			List<Reviewed> rev=redao.findByProId(oldPro.getProcessId());
			if(!Objects.isNull(rev)){
				redao.deleteAll(rev);
			}
			proservice.index7(shen, oldPro);

			//????????????????????????????????????
			if(isSendMessage.equals("1")){
				sendMessageQYWX(oldPro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
		}else {

			ProcessList pro=passportApply.getProId();
			passportApply.setStateCertificate("?????????");
			passportApply.setTakeCertificateTime("?????????");
			passportApply.setReturnCertificateTime("?????????");
			passportApply.setHandleType("??????");
			pro.setProcessName("??????"+": "+passportApply.getTypeCertificate());
			pro.setStartTime(passportApply.getStartTime());
			pro.setEndTime(passportApply.getEndTime());
			pro.setProcessDescribe(passportApply.getTypeProve()+'???'+ passportApply.getIsPrint()+'???');
			pro.setHandleType("??????");
			//proservice.index8(pro, val, lu,shen.getUserName());//???????????????????????????index8??????
			pro.setTypeNmae(val);
			pro.setApplyTime(passportApply.getApplyTime());
			pro.setUserId(lu);
			pro.setStatusId(23L);
			pro.setShenuser(shen.getUserName());
			passportApplyDao.save(passportApply);
			//????????????
			proservice.index7_new(shen,lu,pro,passportApply);

			//????????????????????????????????????
			if(isSendMessage.equals("1")){
				sendMessageQYWX(pro,passportApply,passportApply.getDestination()+":"+passportApply.getApplyReason(),shen);
			}
		}
		return "redirect:/flowmanagePPP";

	}

    /**
     * @author: winper001
     * @create: 2020-03-03 15:40
     * @description: ???????????????
     */
    @RequestMapping("certificate")
    public String certificate(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req)
            throws IllegalStateException, IOException, ParseException{
        User user=udao.getOne(userId);

        Long proid=Long.parseLong(req.getParameter("id"));
        String typeProve=req.getParameter("flag");//??????flag??????ming??????
        //String typeProve_temp=req.getParameter("flag").split("???")[1];
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
		if(sex.equals("???")){
			chenghu="???";
			chenghu_Eng="he";
			chenghu_Eng2="himself";
			chenghu_Eng3="his";
			passportApply.setSexEnglish("Man");
		}else{
			chenghu="???";
			chenghu_Eng="she";
			chenghu_Eng2="herself";
			chenghu_Eng3="her";
			passportApply.setSexEnglish("Woman");
		}
        String salary=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getSalary();
        model.addAttribute("ryzb",StringUtil.stringIsNotNull(user.getRyzb())?user.getRyzb():""  );
        model.addAttribute("sex",sex);
        model.addAttribute("position", position);
        model.addAttribute("birthYMD", birthYMD);//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
        model.addAttribute("finalRevTime",finalRevTime);//????????????????????????
        Date hireTime=udao.findByUserName(passportApply.getApplyPersonName().split("/")[0]).getHireTime();
        String hireTime_t=df.format(hireTime).substring(0, 4);
        String nowTime=df.format(new Date()).substring(0, 4);
        Long workTime=Long.parseLong(nowTime)-Long.parseLong(hireTime_t)+1L;
        model.addAttribute("workTime", workTime);
        if(typeProve.equals("??????/???????????????")){
        	return "process/certificate_gat";
		}else if(typeProve.equals("?????????????????????(?????????)")){
        	return "process/certificate_cn";
		}else{
			return "process/certificate_en";
		}
    }

/**
 * @author: winper001
 * @create 2020-03-16:
 * @description: ?????????
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
	 * @description: ???????????????
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
	 * ????????????????????????
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
	 * ????????????????????????
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
			//?????????
			pagelist=prodao.findByuserId(userId,1L,pa);
		}else if(!Objects.isNull(status)){
			//????????????????????????????????????
			pagelist=prodao.findByuserIdandstatus(userId,status.getStatusId(),pa);
			model.addAttribute("sort", "&val="+val);
		}else{
			//?????????????????????????????????????????????
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
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping("serchPPP")
	public String serch(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
						@RequestParam(value = "page", defaultValue = "0") int page,
						@RequestParam(value = "size", defaultValue = "10") int size){
		User user=udao.getOne(userId);//?????????
		Page<AubUser> pagelist=null;
		String val=null;
		if(!TextUtil.isNullOrEmpty(req.getParameter("val"))){
			val=req.getParameter("val");
		}
		if(StringUtil.stringIsNotNull(user.getRyzb()) && user.getRyzb().equals("????????????????????????")){//???????????????????????????????????????????????????????????????????????????
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
	 * ????????????
	 * @return
	 */
	@RequestMapping("auditPPP")
	public String auding(@SessionAttribute("userId") Long userId,Model model,
						 @RequestParam(value = "page", defaultValue = "0") int page,
						 @RequestParam(value = "size", defaultValue = "10") int size){
		User user=udao.getOne(userId);
		Page<AubUser> pagelist=null;
		if(StringUtil.stringIsNotNull(user.getRyzb()) && user.getRyzb().equals("????????????????????????")){
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
	 * ????????????
	 * @return
	 */
	@RequestMapping("particularPPP")
	public String particular(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req){
		User user=udao.getOne(userId);//????????????????????????
		User audit=null;//???????????????
		String id=req.getParameter("id");
		Long proid=Long.parseLong(id);
		String typename=req.getParameter("typename");//????????????
		Long curPage=Long.parseLong(req.getParameter("curPage"));
		String itemType=req.getParameter("itemType");
		String name=null;

		Map<String, Object> map=new HashMap<>();
		ProcessList process=prodao.findById(proid).orElse(null);//??????????????????
		Boolean flag=process.getUserId().getUserId().equals(userId);//?????????????????????????????????

		if(!flag){
			name="??????";
		}else{
			name="??????";
		}
		map=proservice.index3(name,user,typename,process);

		if(("???????????????").equals(typename)) {
			PassportApply eve = passportApplyDao.findByProId(process);
			int count_hk=passportApplyDao.findByPersonNameAndDestination_finished(eve.getApplyPersonName(),"??????").size();
			int count_ao=passportApplyDao.findByPersonNameAndDestination_finished(eve.getApplyPersonName(),"??????").size();
			int count_tw=passportApplyDao.findByPersonNameAndDestination_finished(eve.getApplyPersonName(),"??????").size();
			int count_foreign=passportApplyDao.findByPersonNameAndDestination_foreign_finished(eve.getApplyPersonName()).size();
			if(process.getStatusId()==34L || process.getStatusId()==35L){
				String HrStatus=sdao.findname(process.getStatusId());
				model.addAttribute("HrStatus",HrStatus);
			}else{
				model.addAttribute("HrStatus", "?????????");
			}
			//String stateCertificate=eve.getStateCertificate();
			//model.addAttribute("stateCertificate", stateCertificate);
			if (StringUtil.stringIsNotNull(eve.getKskz_advice())) {
				model.addAttribute("kskz", eve.getKskz_advice().toString().split(",")[0]+"???"+eve.getKskz_advice().toString().split(",")[1]+"???");
			} else {

				model.addAttribute("kskz","?????????");
			}
			if (StringUtil.stringIsNotNull(eve.getHr_advice())) {
				model.addAttribute("hr", eve.getHr_advice().toString().split(",")[0]+"???"+eve.getHr_advice().toString().split(",")[1]+"???");
			} else {
				model.addAttribute("hr", "?????????");
			}
			if (StringUtil.stringIsNotNull(eve.getZgld_advice())) {
				model.addAttribute("zgld", eve.getZgld_advice().toString().split(",")[0]+"???"+eve.getZgld_advice().toString().split(",")[1]+"???");
			} else {
				model.addAttribute("zgld", "?????????");
			}
			if (StringUtil.stringIsNotNull(eve.getYzdwsjAdvice())) {
				model.addAttribute("yzdwsj", eve.getYzdwsjAdvice().toString().split(",")[0]+"???"+eve.getYzdwsjAdvice().toString().split(",")[1]+"???");
			} else {
				model.addAttribute("yzdwsj", "?????????");
			}
			if(StringUtil.stringIsNotNull(eve.getHr_zxry_advice())){
				model.addAttribute("hr_zxry_advice",eve.getHr_zxry_advice());
			}
			else {
				model.addAttribute("hr_zxry_advice", "?????????");
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
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("auditingPPP")
	public String auditing(@SessionAttribute("userId") Long userId,Model model,HttpServletRequest req,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size){
		User u=udao.getOne(userId);
		//??????id
		Long id=Long.parseLong(req.getParameter("id"));
		Long curPage=Long.parseLong(req.getParameter("curPage"));
		ProcessList process=prodao.findById(id).orElse(null);
		PassportApply passportApply=passportApplyDao.findByProId((process));
		String applyPersonNum=passportApply.getApplyPersonName().split("/")[0];
		String applyPersonPosId=udao.findByUserName(applyPersonNum).getPosition().getId().toString();
		String applyPersonDeptId=udao.findByUserName(applyPersonNum).getDept().getDeptId().toString();
		int count_hk=passportApplyDao.findByPersonNameAndDestination_finished(passportApply.getApplyPersonName(),"??????").size();
		int count_ao=passportApplyDao.findByPersonNameAndDestination_finished(passportApply.getApplyPersonName(),"??????").size();
		int count_tw=passportApplyDao.findByPersonNameAndDestination_finished(passportApply.getApplyPersonName(),"??????").size();
		int count_foreign=passportApplyDao.findByPersonNameAndDestination_foreign_finished(passportApply.getApplyPersonName()).size();
		int count_all=passportApplyDao.findByPersonNameAll_finished(passportApply.getApplyPersonName()).size();
		Reviewed re=redao.findByProIdAndUserId(process.getProcessId(), u);//???????????????
		String typename=process.getTypeNmae().trim();
		if(("???????????????").equals((typename))){
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
		if(StringUtil.stringIsNotNull(u.getRyzb())&&(u.getRyzb().equals("?????????") || u.getRyzb().equals("????????????????????????"))) {//?????????????????????????????????
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
	 * ?????????????????????
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


		ProcessList pro=prodao.findById(proid).orElse(null);//??????????????????
		PassportApply passportApply=passportApplyDao.findByProId(pro);

		User shen=udao.getOne(pro.getUserId().getUserId());//?????????
		if(!TextUtil.isNullOrEmpty(req.getParameter("liuzhuan"))){
			name=req.getParameter("liuzhuan");
		}
		if(!TextUtil.isNullOrEmpty(name)){
			//???????????????
			String xzry=reviewed.getUsername();//???????????????????????????
			User u2 = null;//u2????????????????????????
			if(StringUtil.stringIsNotNull(xzry)) {
				//????????????????????????????????????????????? ??????/???????????????????????? ?????????????????????????????????

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
			if(("???????????????").equals(typename)){
				Long statusid=Long.parseLong(StringUtil.stringIsNotNull(req.getParameter("statusId"))? req.getParameter("statusId"):"34");//???????????????????????????????????????????????????34??????statusId??????null???
			    if(StringUtil.stringIsNotNull(u.getRyzb()) && (u.getRyzb().equals("????????????????????????"))){

			    	String f_iswcmsg=req.getParameter("f_iswcmsg");
			    	String bz=req.getParameter("bz");
			    	String stateCertificate=req.getParameter("stateCertificate");
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar calen = Calendar.getInstance();
			    	passportApply.setF_iswcmsg(f_iswcmsg);
			    	passportApply.setBz(bz);
			    	passportApply.setZxry((u.getRealName()));
			    	if(stateCertificate==null){
						passportApply.setStateCertificate("?????????");//??????????????????"?????????"
					}else if(stateCertificate.equals("??????,??????")){
			    		passportApply.setTakeCertificateTime(dateFormat.format(new Date()));
						passportApply.setStateCertificate(stateCertificate);
					}else if(stateCertificate.equals("??????,??????") || stateCertificate.equals("??????,??????(???)???")){
						passportApply.setReturnCertificateTime(dateFormat.format(new Date()));
						passportApply.setStateCertificate(stateCertificate);
					}
					if(!passportApply.getTakeCertificateTime().equals("?????????") && !passportApply.getReturnCertificateTime().equals("?????????")){
						Date takeTime=dateFormat.parse(passportApply.getTakeCertificateTime());
						Date returnTime=dateFormat.parse(passportApply.getReturnCertificateTime());
						calen.setTime(returnTime);
						int interval_days=(int) ((takeTime.getTime() - returnTime.getTime()) / (24 * 60 * 60 * 1000));//???????????????
						//int interval_days=(int) ((returnTime.getTime() - takeTime.getTime()) / (60 * 60* 1000));//??????????????????
						//if(interval_days>1){//????????????1??????
						if(interval_days>30 && interval_days<=60){//30??????60???
							calen.add(Calendar.DAY_OF_YEAR,180);
							//calen.add(Calendar.MINUTE,1);//???????????????
							Date temp=calen.getTime();
							shen.setLimitDate(temp);//??????6??????
						}else if(interval_days>60){
							calen.add(Calendar.DAY_OF_YEAR,360);
							//calen.add(Calendar.MINUTE,1);//??????1??????
							Date temp=calen.getTime();
							shen.setLimitDate(temp);//??????12??????
						}
						udao.save(shen);//??????aoa_user???

					}
					pro.setStatusId(statusid);
			    	if(statusid==34L){
			    		passportApply.setHr_zxry_advice("?????????");
					}else if(statusid==35L){
			    		passportApply.setHr_zxry_advice("????????????");
					}
			    	prodao.save(pro);
			    	if(f_iswcmsg.equalsIgnoreCase("1")){
			    		MessageController messageController=new MessageController();
			    		String content="<div class=\"gray\">"+ DateUtil.getFormatStr(new Date(), "yyyy???MM???dd???")+"</div> "
								+ "<div  class=\"blue\">"+bz+"</div>";
						messageController.sengCardMessage(pro.getUserId().getUserName(), "", "", pro.getTypeNmae()+"??????",content,moblieurl+"pages/passport/passport-index?usercode="+pro.getUserId().getUserName());
					}

				}else{//??????????????????????????????
			    	if(u2==null){
			    		model.addAttribute("error", "????????????????????????");
						return "common/proce";
					}
			    	if(u.getPosition().getId().equals(7L)){
			    		if(u2.getPosition().getId().equals(6L)){
			    			proservice.save(proid,u,reviewed,pro,u2);
						}else{
			    			model.addAttribute("error", "?????????????????????");
			    			return "common/proce";
						}
					}else{
			    		proservice.save(proid,u,reviewed,pro,u2);
					}
					if(statusid==26L){//?????????????????????????????????????????????????????????????????????26L??????
						reviewed.setStatusId(26L);
						pro.setStatusId(26L);
					}
					if(isSendMessage.equals("1")) {
						sendMessageQYWX(pro, passportApply, passportApply.getDestination() + ":" + passportApply.getApplyReason(), u2);
					}
				}
            }

			else{
				if(u2.getPosition().getId().equals(7L)){//??????????????????
					proservice.save(proid, u, reviewed, pro, u2);
				}else{
					model.addAttribute("error", "??????????????????????????????");
					return "common/proce";
				}
			}

		}else {
			//???????????????
			Reviewed re = redao.findByProIdAndUserId(proid, u);
			re.setAdvice(reviewed.getAdvice());
			re.setStatusId(reviewed.getStatusId());
			re.setReviewedTime(new Date());
			pro.setStatusId(reviewed.getStatusId());//?????????????????????
			redao.save(re);
			prodao.save(pro);
			if (("???????????????").equals(typename)) {
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
			}else if(u.getRyzb().equals("??????/????????????")){
				passportApply.setYzdwsjAdvice(reviewed.getAdvice()+","+u.getRealName());

			}
			passportApplyDao.save(passportApply);

		return "redirect:/auditPPP";
	}


	/**
	 * ????????????
	 */
	@RequestMapping("processdeletePPP")
	public String processdelete(HttpServletRequest req,Model model){
		Long proid=Long.parseLong(req.getParameter("id"));
		ProcessList process=prodao.findById(proid).orElse(null);

		String itemType="";
		Long curPage=null;
		if(process.getTypeNmae().equals("???????????????")){
			itemType=String.valueOf(req.getParameter("itemType"));
			curPage=Long.parseLong(req.getParameter("curPage"));
			curPage=curPage-1L;
		}

		//??????????????????
		List<Reviewed> rev=redao.findByProId(proid);
		if(!Objects.isNull(rev)){
			redao.deleteAll(rev);
		}
		if(process.getTypeNmae().equals("???????????????")){
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
		//????????????????????????????????????
		MessageController messageController = new MessageController();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(process.getTypeNmae().equals("???????????????")){
			String content="<div class=\"gray\">"+DateUtil.getFormatStr(new Date(), "yyyy???MM???dd???")+"</div> "
					+ "<div  class=\"blue\">"+"????????????"+passportApply.getApplyPersonName()+"</div>"
					+ "<div  class=\"blue\">"+"???????????????"+passportApply.getHandleType()+"</div>"
					+ "<div  class=\"blue\">"+"???????????????"+df2.format(passportApply.getApplyTime())+"</div>";
					
					if(process.getHandleType().equalsIgnoreCase("??????")) {
						content+="<div  class=\"blue\">"+"???????????????"+lfsyAndRy+"</div>";
						content+="<div  class=\"blue\">"+"???????????????"+df.format(passportApply.getStartTime())+" ??? "+df.format(passportApply.getEndTime())+"</div>";
					}

			messageController.sengCardMessage(shenry.getUserName(), "", "", "??????(???)??????[??????]",content,
					moblieurl+"pages/passport/passport-detail?"+
							"process_id=" + process.getProcessId()+"&usercode_passport="+shenry.getUserName()+"&type_name="+"???????????????"+"&jllx="+"??????"+"&detailtype=lcsp" );
			
			
			
			
					
					
					
		}
		return true;
	}
	
	
	/**
	 * @author: winper001
	 * @create: 2020-01-15 21:25
	 * @description: ???????????????
	*/
		@RequestMapping("passportdatatables")
		public String passportdatatables(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
								   @RequestParam(value = "page", defaultValue = "0") int page,
								   @RequestParam(value = "size", defaultValue = "10") int size)
		{
			
			List<HashMap<String,String>> fieldList=new ArrayList<HashMap<String,String>>();
			HashMap<String,String> fieldMap=new HashMap<String,String>();

			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","?????????");
			fieldList.add(fieldMap);
			
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","????????????");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","??????");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","??????");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","????????????");
			fieldList.add(fieldMap);
			
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
			fieldMap.put("field","????????????");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","????????????");
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
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","???????????????");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","??????/????????????");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","????????????");
			fieldList.add(fieldMap);
			
			fieldMap=new HashMap<String,String>();
			fieldMap.put("field","????????????");
			fieldList.add(fieldMap);
			
//			proservice.indexPassportApply(model, userId, page, size);
//			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			if((StringUtil.stringIsNotNull(request.getParameter("id")))) {//??????
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


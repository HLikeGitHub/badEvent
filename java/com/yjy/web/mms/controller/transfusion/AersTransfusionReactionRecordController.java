package com.yjy.web.mms.controller.transfusion;

import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.mms.model.dao.aersdao.AersPatientInfoDao;
import com.yjy.web.mms.model.dao.aersdao.AersReportProcessReviewedDao;
import com.yjy.web.mms.model.dao.aersdao.AersYJGWReportProcessDao;
import com.yjy.web.mms.model.dao.transfusiondao.AersTransfusionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.aers.AersPatientInfoEntity;
import com.yjy.web.mms.model.entity.aers.AersReportReviewedEntity;
import com.yjy.web.mms.model.entity.aers.AersYJGWReportProcessEntity;
import com.yjy.web.mms.model.entity.transfusion.AersTransfusionEntity;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.aers.AersReportServices;
import com.yjy.web.mms.services.transfusion.AersTransfusionServcies;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
public class AersTransfusionReactionRecordController {

    @Autowired
    private UserDao udao;

    @Autowired
    private AersTransfusionServcies aersServices;

    @Autowired
    private AersReportProcessReviewedDao aersredao;

    @Autowired
    private AersReportServices aersReportServices;

    @Autowired
    private AersTransfusionDao  aersTransfusionDao;

    @Autowired
    private AersPatientInfoDao patientInfoDao;

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

    /**
     *输血不良反应记录
     *
     **/
    @RequestMapping("transfusionReaction")
    public String transfusionReaction(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size){
         User lu=udao.getOne(userId);//申请人
//		if(!((lu.getDept().getLcks().equals("职能科室")) )){
//			model.addAttribute("error", "您没有相关权限，请联系网络信息科！");
//			return "common/proce";
//		}
        //设置页面公用字典等内容
        aersServices.getAersDictionaries(model, userId, page, size);

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // model.addAttribute("sqrUser", lu);
        //申请名称
        model.addAttribute("applyType", "输血不良反应相关情况");
        model.addAttribute("noteTaker",lu.getUserName()+"/"+lu.getUserId());

//		model.addAttribute("shUser", lu.getUserName()+"/"+lu.getRealName());


        //判断是否为编辑
        if((StringUtil.stringIsNotNull(request.getParameter("id")))) {

            //获取原有单据信息返回到前台
            Long proid=Long.parseLong(request.getParameter("id"));
            AersYJGWReportProcessEntity aersReportProcessEntity=aersYJGWReportProcessDao.findbyAersreportId(proid);
            AersTransfusionEntity aersTransfusionEntity = aersTransfusionDao.findByAersyjgwreportId(aersReportProcessEntity);
          //  MedicalSafetyEventReportEntity medicalSafetyEventReportEntity=medicalSafetyEventDao.findByAersyjgwreportId(aersReportProcessEntity);

             model.addAttribute("aersTransfusionEntity",aersTransfusionEntity);
             model.addAttribute("aersReportProcessEntity",aersReportProcessEntity);
           // model.addAttribute("medicalSafetyEventReportEntity", medicalSafetyEventReportEntity);
          //  model.addAttribute("aersReportProcessEntity", aersReportProcessEntity);


            //自动生成的日期格式无法指定,程序进行处理
            dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
            String happenTime=dateFormat.format(aersTransfusionEntity.getHappenTime());
            model.addAttribute("happenTime", happenTime);
            String recordDate =  dateFormat.format(aersTransfusionEntity.getRecordDate());
            model.addAttribute("recordDate",recordDate);

            //自动生成的日期格式无法指定,程序进行处理
          //  applyTime=dateFormat.format(aersReportProcessEntity.getApplyTime());

            //医疗安全事件 日期
//			String OutDate=dateFormat.format(medicalSafetyEventReportEntity.getOutDate());
//			String OpDate=dateFormat.format(medicalSafetyEventReportEntity.getFirstOpDate());
//			model.addAttribute("OutDate", OutDate);
//			model.addAttribute("OpDate", OpDate);
            AersReportReviewedEntity aersReportReviewedEntity = aersredao.findByAersreportIdAndStutas(Long.valueOf(request.getParameter("id")),23L  );


//			model.addAttribute("opDept", medCareUnOpEventReportEntity.getOpDept().getDeptId()+"/"+medCareUnOpEventReportEntity.getOpDept().getDeptName());
            model.addAttribute("shUser", aersReportReviewedEntity.getUserId().getUserName()+"/"+aersReportReviewedEntity.getUserId().getRealName());



            //读取全局变量单位名称
            model.addAttribute("customername", customername);

        }
        return "aersyjgw/transfusionBadRecord";

    }

    @RequestMapping("transfusionReactionSubmit")
    public String transfusionReactionSubmit(HttpServletRequest request, @Valid AersTransfusionEntity aersTransfusionEntity,
                                            @Valid AersYJGWReportProcessEntity aersYJGWReportProcessEntity,
                                            AersPatientInfoEntity aersPatientInfoEntity,
                                            @SessionAttribute("userId") Long userId,Model model){
        // 获取申请人
        User lu=udao.getOne(userId);
        // 获取流程名称
        String applyType=request.getParameter("applyType");
        applyType = StringUtils.isEmpty(applyType) == true ? "输血不良反应相关情况": applyType;
        System.out.println("提交后" + aersPatientInfoEntity);
        System.out.println("提交后" + aersTransfusionEntity);
        //获取审核人
        String xzry=request.getParameter("noteTaker");
        System.out.println(xzry);
        //获取手术科室
        //		String namedept=req.getParameter("namedept");
        //获取提交类型
        String submitType=request.getParameter("submitType");

        System.out.println("submitType:"+submitType);
        if(!StringUtil.stringIsNotNull(xzry)){
            model.addAttribute("error", "审核人不能为空！");
            return "common/proce";
        }

        //前台提交到后端的人员名称格式为 工号/姓名，后端要进行 处理，解决同名同姓问题
        User shen = null;
        String[] xzrys = xzry.split(";");
        for(int i = 0 ;i<xzrys.length;i++) {
            if(xzrys[i].trim().length()>0) {
                shen=udao.findByUserName(xzrys[i].split("/")[0]);
            }
        }

        //判断是否为编辑
        if(StringUtil.objectIsNotNull(aersTransfusionEntity.getAersyjgwreportId())) {
            AersTransfusionEntity aersEntity = aersTransfusionDao.findById(aersTransfusionEntity.getAersTransfusionId()).orElse(null);
            AersYJGWReportProcessEntity oldAersYJGWReportProcessEntity = aersEntity.getAersyjgwreportId();
            oldAersYJGWReportProcessEntity.setLevel(aersYJGWReportProcessEntity.getLevel());
            AersPatientInfoEntity patientInfoEntity = aersPatientInfoEntity;
            //保存流程信息
            aersReportServices.setYJGWAersReportProcess(oldAersYJGWReportProcessEntity,applyType,lu,shen.getUserName());
            aersYJGWReportProcessDao.save(oldAersYJGWReportProcessEntity);

            //设置流程信息到输血记录
            aersTransfusionEntity.setAersyjgwreportId(oldAersYJGWReportProcessEntity);
            aersTransfusionDao.save(aersTransfusionEntity);

            //先删除审核记录,再存审核表
            List<AersReportReviewedEntity> aersrev=aersredao.findByAersreportId(oldAersYJGWReportProcessEntity.getAersreportId());
            if(!Objects.isNull(aersrev)){
                aersredao.deleteAll(aersrev);
            }
            //保存患者信息表
            patientInfoDao.save(patientInfoEntity);
            oldAersYJGWReportProcessEntity.setPatinfoId(patientInfoEntity);

            aersReportServices.setAersReportReviewed(shen,oldAersYJGWReportProcessEntity);
        }else{
            AersPatientInfoEntity patientInfoEntity = aersPatientInfoEntity;
            // 保存流程信息
            AersYJGWReportProcessEntity aerspro = aersYJGWReportProcessEntity;
            aersReportServices.setYJGWAersReportProcess(aerspro,applyType,lu,shen.getUserName());

            //保存患者信息表
            patientInfoDao.save(patientInfoEntity);
            aerspro.setPatinfoId(patientInfoEntity);
            // 保存流程主表
            aersYJGWReportProcessDao.save(aerspro);

            // 保存输血记录表
            aersTransfusionEntity.setAersyjgwreportId(aerspro);
            aersTransfusionEntity.setStatus("已提交");
            aersTransfusionDao.save(aersTransfusionEntity);
            aersReportServices.setAersReportReviewed(shen,aerspro);
        }
        return "redirect:/aers-yjgw-report";
    }
}

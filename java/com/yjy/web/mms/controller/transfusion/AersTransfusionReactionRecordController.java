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
     *????????????????????????
     *
     **/
    @RequestMapping("transfusionReaction")
    public String transfusionReaction(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size){
         User lu=udao.getOne(userId);//?????????
//		if(!((lu.getDept().getLcks().equals("????????????")) )){
//			model.addAttribute("error", "???????????????????????????????????????????????????");
//			return "common/proce";
//		}
        //?????????????????????????????????
        aersServices.getAersDictionaries(model, userId, page, size);

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // model.addAttribute("sqrUser", lu);
        //????????????
        model.addAttribute("applyType", "??????????????????????????????");
        model.addAttribute("noteTaker",lu.getUserName()+"/"+lu.getUserId());

//		model.addAttribute("shUser", lu.getUserName()+"/"+lu.getRealName());


        //?????????????????????
        if((StringUtil.stringIsNotNull(request.getParameter("id")))) {

            //???????????????????????????????????????
            Long proid=Long.parseLong(request.getParameter("id"));
            AersYJGWReportProcessEntity aersReportProcessEntity=aersYJGWReportProcessDao.findbyAersreportId(proid);
            AersTransfusionEntity aersTransfusionEntity = aersTransfusionDao.findByAersyjgwreportId(aersReportProcessEntity);
          //  MedicalSafetyEventReportEntity medicalSafetyEventReportEntity=medicalSafetyEventDao.findByAersyjgwreportId(aersReportProcessEntity);

             model.addAttribute("aersTransfusionEntity",aersTransfusionEntity);
             model.addAttribute("aersReportProcessEntity",aersReportProcessEntity);
           // model.addAttribute("medicalSafetyEventReportEntity", medicalSafetyEventReportEntity);
          //  model.addAttribute("aersReportProcessEntity", aersReportProcessEntity);


            //???????????????????????????????????????,??????????????????
            dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
            String happenTime=dateFormat.format(aersTransfusionEntity.getHappenTime());
            model.addAttribute("happenTime", happenTime);
            String recordDate =  dateFormat.format(aersTransfusionEntity.getRecordDate());
            model.addAttribute("recordDate",recordDate);

            //???????????????????????????????????????,??????????????????
          //  applyTime=dateFormat.format(aersReportProcessEntity.getApplyTime());

            //?????????????????? ??????
//			String OutDate=dateFormat.format(medicalSafetyEventReportEntity.getOutDate());
//			String OpDate=dateFormat.format(medicalSafetyEventReportEntity.getFirstOpDate());
//			model.addAttribute("OutDate", OutDate);
//			model.addAttribute("OpDate", OpDate);
            AersReportReviewedEntity aersReportReviewedEntity = aersredao.findByAersreportIdAndStutas(Long.valueOf(request.getParameter("id")),23L  );


//			model.addAttribute("opDept", medCareUnOpEventReportEntity.getOpDept().getDeptId()+"/"+medCareUnOpEventReportEntity.getOpDept().getDeptName());
            model.addAttribute("shUser", aersReportReviewedEntity.getUserId().getUserName()+"/"+aersReportReviewedEntity.getUserId().getRealName());



            //??????????????????????????????
            model.addAttribute("customername", customername);

        }
        return "aersyjgw/transfusionBadRecord";

    }

    @RequestMapping("transfusionReactionSubmit")
    public String transfusionReactionSubmit(HttpServletRequest request, @Valid AersTransfusionEntity aersTransfusionEntity,
                                            @Valid AersYJGWReportProcessEntity aersYJGWReportProcessEntity,
                                            AersPatientInfoEntity aersPatientInfoEntity,
                                            @SessionAttribute("userId") Long userId,Model model){
        // ???????????????
        User lu=udao.getOne(userId);
        // ??????????????????
        String applyType=request.getParameter("applyType");
        applyType = StringUtils.isEmpty(applyType) == true ? "??????????????????????????????": applyType;
        System.out.println("?????????" + aersPatientInfoEntity);
        System.out.println("?????????" + aersTransfusionEntity);
        //???????????????
        String xzry=request.getParameter("noteTaker");
        System.out.println(xzry);
        //??????????????????
        //		String namedept=req.getParameter("namedept");
        //??????????????????
        String submitType=request.getParameter("submitType");

        System.out.println("submitType:"+submitType);
        if(!StringUtil.stringIsNotNull(xzry)){
            model.addAttribute("error", "????????????????????????");
            return "common/proce";
        }

        //????????????????????????????????????????????? ??????/???????????????????????? ?????????????????????????????????
        User shen = null;
        String[] xzrys = xzry.split(";");
        for(int i = 0 ;i<xzrys.length;i++) {
            if(xzrys[i].trim().length()>0) {
                shen=udao.findByUserName(xzrys[i].split("/")[0]);
            }
        }

        //?????????????????????
        if(StringUtil.objectIsNotNull(aersTransfusionEntity.getAersyjgwreportId())) {
            AersTransfusionEntity aersEntity = aersTransfusionDao.findById(aersTransfusionEntity.getAersTransfusionId()).orElse(null);
            AersYJGWReportProcessEntity oldAersYJGWReportProcessEntity = aersEntity.getAersyjgwreportId();
            oldAersYJGWReportProcessEntity.setLevel(aersYJGWReportProcessEntity.getLevel());
            AersPatientInfoEntity patientInfoEntity = aersPatientInfoEntity;
            //??????????????????
            aersReportServices.setYJGWAersReportProcess(oldAersYJGWReportProcessEntity,applyType,lu,shen.getUserName());
            aersYJGWReportProcessDao.save(oldAersYJGWReportProcessEntity);

            //?????????????????????????????????
            aersTransfusionEntity.setAersyjgwreportId(oldAersYJGWReportProcessEntity);
            aersTransfusionDao.save(aersTransfusionEntity);

            //?????????????????????,???????????????
            List<AersReportReviewedEntity> aersrev=aersredao.findByAersreportId(oldAersYJGWReportProcessEntity.getAersreportId());
            if(!Objects.isNull(aersrev)){
                aersredao.deleteAll(aersrev);
            }
            //?????????????????????
            patientInfoDao.save(patientInfoEntity);
            oldAersYJGWReportProcessEntity.setPatinfoId(patientInfoEntity);

            aersReportServices.setAersReportReviewed(shen,oldAersYJGWReportProcessEntity);
        }else{
            AersPatientInfoEntity patientInfoEntity = aersPatientInfoEntity;
            // ??????????????????
            AersYJGWReportProcessEntity aerspro = aersYJGWReportProcessEntity;
            aersReportServices.setYJGWAersReportProcess(aerspro,applyType,lu,shen.getUserName());

            //?????????????????????
            patientInfoDao.save(patientInfoEntity);
            aerspro.setPatinfoId(patientInfoEntity);
            // ??????????????????
            aersYJGWReportProcessDao.save(aerspro);

            // ?????????????????????
            aersTransfusionEntity.setAersyjgwreportId(aerspro);
            aersTransfusionEntity.setStatus("?????????");
            aersTransfusionDao.save(aersTransfusionEntity);
            aersReportServices.setAersReportReviewed(shen,aerspro);
        }
        return "redirect:/aers-yjgw-report";
    }
}

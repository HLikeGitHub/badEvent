package com.yjy.web.mms.controller.medicalequipment;

import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.user.User;
import com.yjy.web.mms.services.aers.AersReportServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/")
public class DrugsBadReactionController {

    @Autowired
    private UserDao udao;

    @Autowired
    private AersReportServices aersServices;

    @RequestMapping("equipment")
    public String equipment(Model model, @SessionAttribute("userId") Long userId, HttpServletRequest request,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size){
        User lu=udao.getOne(userId);// 申请人
        //设置页面公用字典等内容
        aersServices.getAersDictionaries(model,userId,page,size);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String applyTime = format.format(new Date());
        model.addAttribute("applyTime", applyTime);
        model.addAttribute("sqrUser", lu);
        model.addAttribute("applyType","药品不良反应 / 事件");
        if(!StringUtil.stringIsNotNull(request.getParameter("id"))){

        }
        return "aersyjgw/drugsBadReaction";
    }
}

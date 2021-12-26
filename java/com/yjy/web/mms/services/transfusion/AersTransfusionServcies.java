package com.yjy.web.mms.services.transfusion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yjy.web.mms.model.dao.aersdao.AersDictionariesListDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.PositionDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.aers.AersDictionariesListEntity;
import com.yjy.web.mms.model.entity.system.SystemTypeList;
import com.yjy.web.mms.model.entity.user.Dept;
import com.yjy.web.mms.model.entity.user.Position;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AersTransfusionServcies {

    @Autowired
    private DeptDao ddao;

    @Autowired
    private PositionDao pdao;

    @Autowired
    private TypeDao tydao;

    @Autowired
    private UserDao udao;

    @Autowired
    private AersDictionariesListDao aersDicDao;

    public void getAersDictionaries(Model model, Long id, int page, int size){

        // 查询部门表
        Iterable<Dept> deptlist = ddao.findAll();
        // 查职位表
        Iterable<Position> poslist = pdao.findAll();
        //加载紧急程度
        List<SystemTypeList> harrylist=tydao.findByTypeModel("aoa_process_list");
        //获取申请人信息
        User lu=udao.getOne(id);
        //分页查询全员所有人员
        Pageable pa1= PageRequest.of(page, size);
        Page<User> pageuser1=null;

        if (lu.getPosition().getId()==6L ) {//如果普通科员就找科长
            pageuser1=udao.findByUserDeptFather(lu.getFatherId(),lu.getDept().getDeptId(),pa1);
        }else if (lu.getPosition().getId()==5L ) {//如果科长就找主管部门科长
            pageuser1=udao.findByUserDeptFather(4L,15L,pa1);
        }else if (lu.getPosition().getId()==4L ) {//如果主管部门科长就找质控科科长
            pageuser1=udao.findByUserDeptFather(3L,1514L,pa1);
        }else {
            pageuser1=udao.findByUserDeptFather(lu.getFatherId(),lu.getDept().getDeptId(),pa1);
        }
        //返回审批人员列表
        List<User> userlist1=pageuser1.getContent();
        //设置默认审核人
        if (userlist1.size()>0) {
            model.addAttribute("shUser", userlist1.get(0).getUserName()+"/"+userlist1.get(0).getRealName());
        }else {
            model.addAttribute("shUser", "");
        }



        model.addAttribute("page", pageuser1);
        model.addAttribute("emplist", userlist1);
        model.addAttribute("poslist", poslist);
        model.addAttribute("deptlist", deptlist);
        model.addAttribute("url", "namesfather");
        model.addAttribute("username", lu.getUserName());
        model.addAttribute("harrylist", harrylist);


        AersDictionariesListEntity bloodDic=aersDicDao.findByAersdictionarieName("输血时是否处于全麻状态");
        List<Map<String, Object>> bloodDicList= JSON.parseObject(bloodDic.getAersdictionarieJson(),new TypeReference<List<Map<String,Object>>>(){});
        model.addAttribute("bloodDicList", bloodDicList);

        AersDictionariesListEntity happenTimeStatus = aersDicDao.findByAersdictionarieName("发生时间状态");
        List<Map<String,Object>> happenStatusList = JSON.parseObject(happenTimeStatus.getAersdictionarieJson(), new TypeReference<List<Map<String,Object>>>(){});
        model.addAttribute("happenStatusList",happenStatusList);

        AersDictionariesListEntity turnOverDic = aersDicDao.findByAersdictionarieName("转归");
        List<Map<String,Object>> turnOverDicList = JSON.parseObject(turnOverDic.getAersdictionarieJson(), new TypeReference<List<Map<String,Object>>>(){});
        model.addAttribute("turnOverDicList",turnOverDicList);

        AersDictionariesListEntity sexDic = aersDicDao.findByAersdictionarieName("性别");
        List<Map<String,Object>> sexDicList = JSON.parseObject(sexDic.getAersdictionarieJson(), new TypeReference<List<Map<String,Object>>>(){});
        model.addAttribute("sexDicList",sexDicList);
    }
}

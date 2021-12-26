package com.yjy.web.mms.services.deanrounds;

import com.yjy.web.comm.utils.StringUtil;
import com.yjy.web.mms.model.dao.deanroundsdao.DeanroundsDao;
import com.yjy.web.mms.model.dao.system.StatusDao;
import com.yjy.web.mms.model.dao.system.TypeDao;
import com.yjy.web.mms.model.dao.user.DeptDao;
import com.yjy.web.mms.model.dao.user.UserDao;
import com.yjy.web.mms.model.entity.deanrounds.Deanroundslist;
import com.yjy.web.mms.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class DeanroundsService {
	
	@Autowired
	private DeanroundsDao tdao;
//	@Autowired
//	private DeanroundsuserDao tudao;
//	@Autowired
//	private DeanroundsloggerDao tldao;
	@Autowired
	private UserDao udao;
	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private DeptDao ddao;
	/*
	public Deanroundslist save(Deanroundslist task){
		return tdao.save(task);
	}
	
	//修改任务表里面的状态
	public int updateStatusid(Long taskid,Integer statusid){
	int s=	tdao.update(taskid, statusid);
	return s;
	
	}
	//修改任务表中间表的任务状态
	public int updateUStatusid(Long taskid,Integer statusid){
		int s=tudao.updatestatus(taskid, statusid);
		return s;
		
		}
	
	//删除任务中间表
	public int delete(Long pkid){
		int i=0;
		if (!Objects.isNull(pkid)){
			tudao.deleteById(pkid);
			i=1;
		}
		return i;
		
	}
	
	//删除任务
	public void deteletask(Tasklist task){
		tdao.delete(task);
	}
	//删除日志表
	public int detelelogger(Long taskid){
		int i=0;
		 List<Tasklogger> taskLogger = tldao.findByTaskId(taskid);
		 if(taskLogger.size()!=0){
			 for (Tasklogger tasklogger2 : taskLogger) {
				tldao.delete(tasklogger2);
			}
			 i=1;
		 }
		return i;
	}*/
	
	public void setJurisdiction(Model model,Long userId,String cffzr){
		//返回人员登录信息
		User tu = udao.getOne(userId);

		model.addAttribute("userdept", tu.getDept().getDeptName());
        if(tu.getDept().getDeptName().equals("办公室")) {
        	model.addAttribute("isbgs", true);
        	model.addAttribute("isfzr", true);
        }else {
        	model.addAttribute("isbgs", false);
    		String xzry=cffzr;
    		String[] xzrys = xzry.split(";");
    		model.addAttribute("isfzr", false);
    		for(int i = 0 ;i<xzrys.length;i++) {
    			if(xzrys[i].trim().length()>0) {
    				if(xzrys[i].split("/")[0].equals(tu.getUserName())) {
    					model.addAttribute("isfzr", true);
    				}
    			}
    		}
        }
        
        
	}
	
	
	public Page<Deanroundslist> index(int page,int size,String val,User tu){
		Page<Deanroundslist> deanroundslist=null;
		List<Order> orders = new ArrayList<>();
		
		orders.addAll(Arrays.asList(new Order(Direction.DESC, "publishDate")));
		Sort sort = Sort.by(orders);
		Pageable pa=PageRequest.of(page, size, sort);
		deanroundslist = tdao.findAll(pa);
		return deanroundslist;
		
	}
	
	
	
	public List<Map<String, Object>>  index2(Page<Deanroundslist> deanroundslist,User user){

		List<Map<String, Object>> list = new ArrayList<>();
		List<Deanroundslist> deanrounds= deanroundslist.getContent();
		for (int i = 0; i < deanrounds.size(); i++) {
			Map<String, Object> result = new HashMap<>();
			result.put("deanroundsId", deanrounds.get(i).getDeanroundsId());
//			result.put("title", deanrounds.get(i).getTitle());
			result.put("publishDate", deanrounds.get(i).getPublishDate());
			result.put("cfks", StringUtil.stringToFormat(deanrounds.get(i).getCfks()));
			result.put("cfap", StringUtil.stringToFormat(deanrounds.get(i).getCfap()));
			result.put("ksgk", StringUtil.stringToFormat(deanrounds.get(i).getKsgk()));
			result.put("cjfx", StringUtil.stringToFormat(deanrounds.get(i).getCjfx()));
			result.put("fzgh", StringUtil.stringToFormat(deanrounds.get(i).getFzgh()));
//			result.put("cflx",deanrounds.get(i).getCflx());
//			result.put("hydd", deanrounds.get(i).getHydd());
			list.add(result);
		}
		return list;
		
	}
	
	
	
	
	/*
	public Page<Tasklist> index3(Long userid,String title,int page,int size){
		Pageable pa=PageRequest.of(page, size);
		List<Order> orders = new ArrayList<>();
		Page<Tasklist> tasklist=null; 
		// 根据接收人id查询任务id
		List<Long> taskid = tudao.findByUserId(userid);
		// 类型
		SystemTypeList type = tydao.findByTypeModelAndTypeName("aoa_task_list", title);
		// 状态
		SystemStatusList status = sdao.findByStatusModelAndStatusName("aoa_task_list", title);
		// 找用户
		User user = udao.findByUserName(title);

		if (StringUtil.isEmpty(title)) {
			orders.addAll(Arrays.asList(new Order(Direction.ASC, "cancel"), new Order(Direction.ASC, "statusId")));
			Sort sort = Sort.by(orders);
			pa=PageRequest.of(page, size, sort);
			if(taskid.size()>0){
				
				tasklist=tdao.findTaskByTaskIds(taskid,pa);
			}
		} else if (!Objects.isNull(type)) {

			tasklist = tdao.findtaskTypeIdAndTaskId(type.getTypeId(), taskid,pa);
				
		} else if (!Objects.isNull(status)) {
			// Long转换成Integer
			Integer statusid = Integer.parseInt(status.getStatusId().toString());
			// 根据找出的taskid和状态id查找任务
			tasklist = tdao.findtaskStatusIdAndCancelAndTaskId(statusid, taskid,pa);
				
		} else if (("已取消").equals(title)) {
			tasklist = tdao.findtaskCancelAndTaskId(true,  taskid,pa);
			
		} else if (!Objects.isNull(user)) {
			
			tasklist = tdao.findtaskUsersIdAndTaskId(user, taskid,pa);
			
		} else {
			// 根据title和taskid进行模糊查询
			tasklist = tdao.findtaskByTitleLikeAndTaskId(taskid, title,pa);

			
		}
	
		return tasklist;
	}
	
	public List<Map<String, Object>> index4(Page<Tasklist> tasklist,Long userid){
		List<Map<String, Object>> list = new ArrayList<>();
		if(tasklist!=null){
			
			List<Tasklist> task= tasklist.getContent();
			
				for (int i = 0; i < task.size(); i++) {
					Map<String, Object> result = new HashMap<>();
					// 查询任务id
					Long tid = task.get(i).getTaskId();
					
					// 查询接收人的任务状态id
					Long statusid = tudao.findByuserIdAndTaskId(userid, tid);
					
					// 查询发布人
					User ptu = udao.getOne(task.get(i).getUsersId().getUserId());
					String username = ptu.getUserName();
					String deptname = ddao.findname(ptu.getDept().getDeptId());
					
					result.put("taskid", tid);
					result.put("typename", tydao.findname(task.get(i).getTypeId()));
					result.put("statusname", sdao.findname(statusid));
					result.put("statuscolor", sdao.findcolor(statusid));
					result.put("title", task.get(i).getTitle());
					result.put("publishtime", task.get(i).getPublishTime());
					result.put("zhiding", task.get(i).getTop());
					result.put("cancel", task.get(i).getCancel());
					result.put("username", username);
					result.put("deptname", deptname);
					
					list.add(result);
				}
			}
		
		return list;
	}
*/
}

package com.supermap.wisdombusiness.workflow.service.wfi;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_Abnormal;
import com.supermap.wisdombusiness.workflow.model.Wfi_PassWork;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;


@Component("smAbnormal")
public class SmAbnormal {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private UserService userService;
	@Autowired
	private SmStaff smStaff;
	/*
	 * 记录委托信息
	 * @param staffid String 操作人ID
	 * @param tostaffid String 目标人ID
	 * @param actinstid String  活动ID
	 * @param proinstid String 实例ID
	 * @param msg  String 消息
	 * @param  type int  类型
	 * */
    public boolean AddAbnormInfo(String staffid,String Staff_name,String tostaffid,String actinstid,String proinstid,String msg,int type){
    	Wfi_Abnormal abnormal=new Wfi_Abnormal();
    	abnormal.setActinst_Id(actinstid);
    	abnormal.setProinst_Id(proinstid);
    	abnormal.setOpeartion_Date(new Date());
    	abnormal.setOperation_Msg(msg);
    	abnormal.setOperation_Type(type);
    	abnormal.setTostaff_Id(tostaffid);
    	abnormal.setStaff_Name(Staff_name);
    	commonDao.save(abnormal);
    	commonDao.flush();
    	return false;
    }
    /**
     * 兼容其接口调用
     * 重载方法AddAbnormInfo;增加:删除原因
     * */
    public boolean AddAbnormInfo(String staffid,String Staff_name,String tostaffid,String actinstid,String proinstid,String msg,int type,String deleteReasion){
    	Wfi_Abnormal abnormal=new Wfi_Abnormal();
    	abnormal.setActinst_Id(actinstid);
    	abnormal.setProinst_Id(proinstid);
    	abnormal.setOpeartion_Date(new Date());
    	abnormal.setOperation_Msg(msg);
    	abnormal.setOperation_Type(type);
    	abnormal.setTostaff_Id(tostaffid);
    	abnormal.setStaff_Name(Staff_name);
    	abnormal.setFJ(deleteReasion);
    	commonDao.save(abnormal);
    	commonDao.flush();
    	return false;
    }
    
    public void AddPassWorkInfo(Wfd_Actdef def,SmObjInfo smObjInfo){
    	//通过当前活动判断下一步活动定义是否委托给他人
    String staffid=smObjInfo.getID();
    String isPass=userService.isPassWork(staffid,def);
    if(isPass!=null&&!isPass.equals("")){
    	smObjInfo.setID(isPass);
    	smObjInfo.setName(smStaff.GetStaffName(isPass));
    }
    }
}

package com.supermap.wisdombusiness.workflow.service.wfm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSendProject;

@Service("sendProjectService")
public class SendProjectService {
	@Autowired
	private SmSendProject smSendProject;
	/**
	 * 平均派件
	 * @param actinstid
	 * @return
	 * 2015年10月16日
	 */
	public SmObjInfo AverageSendProject(String actinstid){
		SmObjInfo info=null;
		String staffid=smSendProject.RandomGetStaff(actinstid);
		if(staffid!=null&&!staffid.equals("")){
			info=smSendProject.SendProjectToStaff(actinstid, staffid,WFConst.Send_Type.Average.value);
		}
		return info;
	}
	/**
	 * 办件较少者优先
	 *
	 * @param actinstid
	 * @return
	 * 2015年10月16日
	 */
	public SmObjInfo LessSendProject(String actinstid){
		SmObjInfo info=null;
		String staffid=smSendProject.GetLessDoStaffID(actinstid);
		if(staffid!=null&&!staffid.equals("")){
			info=smSendProject.SendProjectToStaff(actinstid, staffid,WFConst.Send_Type.LessWorkFirst.value);
		}
		return info;
	}
	/**
	 * 高效原则
	 * @param actinstid
	 * @return
	 * 2015年10月16日
	 */
	public SmObjInfo EfficientSendProject(String actinstid){
		SmObjInfo info=null;
		String staffid=smSendProject.GetLessDoingStaffID(actinstid);
		if(staffid!=null&&!staffid.equals("")){
			info=smSendProject.SendProjectToStaff(actinstid, staffid,WFConst.Send_Type.Efficient.value);
		}
		return info;
	}
	/**
	 * 任务较少者优先
	 * @param actinstid
	 * @return
	 * 2015年10月16日
	 */
	public SmObjInfo LessWorkSendProject(String actinstid){
		SmObjInfo info=null;
		String staffid=smSendProject.GetLessDoindWorkBoxStaff(actinstid);
		if(staffid!=null&&!staffid.equals("")){
			info=smSendProject.SendProjectToStaff(actinstid, staffid,WFConst.Send_Type.LessFirst.value);
		}
		return info;
	}
	
}

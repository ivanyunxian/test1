package com.supermap.wisdombusiness.workflow.service.wfi;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
@Component("smSendProject")
public class SmSendProject {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmActInst smActinst;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmProInst smProinst;
	/**
	 * 派件给固定的个人
	 * 
	 * @param actinst_id
	 * @param staffid
	 *            2015年10月16日
	 */
	public SmObjInfo SendProjectToStaff(String actinst_id, String staffid,int type) {
		SmObjInfo smObjInfo=new SmObjInfo();
		if (actinst_id != null && !actinst_id.equals("")) {
			Wfi_ActInst actinst = smActinst.GetActInst(actinst_id);
			Wfi_ProInst proinst = smProinst.GetProInstByActInstId(actinst_id);
			if (actinst != null) {
				String staffname=smStaff.GetStaffName(staffid);
				actinst.setStaff_Id(staffid);
				actinst.setDept_Code(smStaff.GetStaffDeptID(staffid));
				actinst.setStaff_Name(staffname);
				actinst.setActinst_Start(new Date());
				actinst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
				//TODO:proisnt表中冗余存储当前环节的信息jhx2019-11-03：
				if(proinst!=null){
					proinst.setStaff_Id_Nact(staffid);
					proinst.setStaff_Name_Nact(staffname);
					proinst.setACTINST_START(actinst.getActinst_Start());
					proinst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
				}
				switch(type){
				case 1:
					actinst.setMsg("平均自动派件");
					break;
				case 2:
					actinst.setMsg("办件箱数量少自动派件");
					break;
				case 3:
					actinst.setMsg("办件少自动派件");
					break;
				case 4:
					actinst.setMsg("高效自动派件");
					break;
				}
				commonDao.update(proinst);
				commonDao.update(actinst);
				commonDao.flush();
				smObjInfo.setDesc("已成功派件"+staffname);
				smObjInfo.setID(staffid);
			}
		}
		return smObjInfo;
	}

	/**
	 * 检测用户是否为转出时默认添加的用户
	 *
	 * @param actinst_id
	 * @param staffid
	 *            2015年10月16日
	 */
	public boolean CheckedActStaff(String actinst_id, String staffid) {
		boolean Result = false;
		StringBuilder sb = new StringBuilder();
		sb.append(" actinst_id='");
		sb.append(actinst_id);
		sb.append("' and staff_id='");
		sb.append(staffid);
		sb.append("'");
		List<Wfi_ActInstStaff> actstaffs = commonDao.getDataList(
				Wfi_ActInstStaff.class, Common.WORKFLOWDB + "Wfi_ActInstStaff",
				sb.toString());
		if (actstaffs != null && actstaffs.size() > 0) {
			Result = true;
		}
		return Result;
	}

	/**
	 * 设置活动和用户的信息
	 *
	 * @param actinst_id
	 * @param staffid
	 * @return 2015年10月16日
	 */
	public Wfi_ActInstStaff SetActStaffInfo(String actinst_id, String staffid) {
		Wfi_ActInstStaff actStaff = new Wfi_ActInstStaff();
		actStaff.setActinst_Id(actinst_id);
		actStaff.setStaff_Id(staffid);
		commonDao.save(actStaff);
		commonDao.flush();
		return actStaff;
	}

	/**
	 * 获取可以办理改活动的员工数据
	 *
	 * @param actinstid
	 * @return 2015年10月16日
	 */
	private List<Wfi_ActInstStaff> getActstaffs(String actinstid) {
		
		List<Wfi_ActInstStaff> actdefStaffs = commonDao.getDataList(
				Wfi_ActInstStaff.class, Common.WORKFLOWDB + "Wfi_ActInstStaff",
				"actinst_id='" + actinstid + "'");
		return actdefStaffs;
	}

	/**
	 * 检测本活动下某种状态下办件最少的员工
	 *
	 * @param actinstid
	 * @param Status
	 * @return 返回该员工的ID 2015年10月16日
	 */
	public String CheckLessWorkByActdefid(String actinstid, String Status) {
		Wfd_Actdef actdef = smActinst.GetActDef(actinstid);
		long countInteger = 0;
		String Staff_id = "";
		if (actdef != null) {
			List<Wfi_ActInstStaff> actdefStaffs = getActstaffs(actinstid);
			if (actdefStaffs != null && actdefStaffs.size() > 0) {
				for (Wfi_ActInstStaff wfi_ActInstStaff : actdefStaffs) {
					StringBuilder sBuilder = new StringBuilder();
					sBuilder.append(" from ");
					sBuilder.append(Common.WORKFLOWDB + "wfi_actinst where ");
					sBuilder.append(" actdef_id='");
					sBuilder.append(actdef.getActdef_Id());
					sBuilder.append("' and staff_id='");
					sBuilder.append(wfi_ActInstStaff.getStaff_Id());
					sBuilder.append("' and actinst_status in (");
					sBuilder.append(Status);
					sBuilder.append(")");
					long count = commonDao.getCountByFullSql(sBuilder
							.toString());
					if (countInteger == 0 || count < countInteger) {
						countInteger = count;
						Staff_id = wfi_ActInstStaff.getStaff_Id();
					}

				}
			}
		}
		return Staff_id;
	}

	/**
	 * 获取活动中可以办理人员的随机人员
	 *
	 * @param actinstid
	 * @return 2015年10月16日
	 */
	public String RandomGetStaff(String actinstid) {
		String staff_id = "";
		List<Wfi_ActInstStaff> actdefStaffs = getActstaffs(actinstid);
		if (actdefStaffs != null && actdefStaffs.size() > 0) {
			Random rand = new Random();
			int num = rand.nextInt(actdefStaffs.size());
			staff_id = actdefStaffs.get(num).getStaff_Id();
		}
		return staff_id;
	}

	/**
	 * 获取办件箱中该类活动数量最少的员工
	 * 
	 * @param actinstid
	 * @return 2015年10月16日
	 */
	public String GetLessDoingStaffID(String actinstid) {
		return CheckLessWorkByActdefid(actinstid,
				WFConst.Instance_Status.Instance_doing.value + "");
	}

	/**
	 * 办理此类活动最少的人
	 * 
	 * @param actinstid
	 * @return 2015年10月16日
	 */
	public String GetLessDoStaffID(String actinstid) {
		String statusString = WFConst.Instance_Status.Instance_doing.value
				+ "," + WFConst.Instance_Status.Instance_Passing.value;
		return CheckLessWorkByActdefid(actinstid, statusString);
	}

	/**
	 * 获取在办箱中项目最少的人员
	 *
	 * @param actinstid
	 * @param status
	 * @return
	 * 2015年10月16日
	 */
	public String GetLessWorkBoxStaff(String actinstid,String status) {
		long countInteger = 0;
		String Staff_id = "";
		List<Wfi_ActInstStaff> actdefStaffs = getActstaffs(actinstid);
		if (actdefStaffs != null && actdefStaffs.size() > 0) {
			for (Wfi_ActInstStaff wfi_ActInstStaff : actdefStaffs) {
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append(" from ");
				sBuilder.append(Common.WORKFLOWDB + "wfi_actinst where ");
				sBuilder.append(" staff_id='");
				sBuilder.append(wfi_ActInstStaff.getStaff_Id());
				sBuilder.append("' and actinst_status in (");
				sBuilder.append(status);
				sBuilder.append(")");
				long count = commonDao.getCountByFullSql(sBuilder.toString());
				if (countInteger == 0 || count < countInteger) {
					countInteger = count;
					Staff_id = wfi_ActInstStaff.getStaff_Id();
				}

			}
		}
		return Staff_id;
	}
	/**
	 * 获取在办箱中项目最少的人
	 *
	 * @param actinstid
	 * @return
	 * 2015年10月16日
	 */
	public String GetLessDoindWorkBoxStaff(String actinstid){
		return GetLessWorkBoxStaff(actinstid,WFConst.Instance_Status.Instance_doing.value+"");
	}
}

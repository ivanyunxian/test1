package com.supermap.wisdombusiness.workflow.service.wfi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.service.DXSendService;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.util.DButil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * 更改海口市电子政务系统业务受理的状态
 * 
 * */
@Component("proinstStateModify")
public class proinstStateModify {
	
	@Autowired
	private SmActDef smActDef;
	@Autowired
	private SmActInst smActInst;
	@Autowired
	private SmProInst smProInst;
	@Autowired
	private SmProDef smProDef;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private DXSendService dxSendService;

	/**
	 * @param actinstid 给房产推送网签信息
	 * @author JOE
	 */
	public void sendInfoToHouse(final String actinstid, final String requesturl) {
		try {
			_sendInfoToHouse(actinstid, requesturl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void _sendInfoToHouse(String actinstid, String requesturl) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<Map> houses = new ArrayList<Map>();
		List<String> houseworkflowname = new ArrayList<String>();
		//是否启动推送信息接口
		String pushtrue = ConfigHelper.getNameByValue("HOUSENETSET-PUSHTRUE");
		String pushurl = ConfigHelper.getNameByValue("HOUSENETSET-PUSHURL");
		//需发送的流程编号
		String hcodes = ConfigHelper.getNameByValue("HOUSENETSET-WORKFLOWCODE");
		if (hcodes != null && hcodes.trim() != "") {
			for (String h : hcodes.split(",")) {
				houseworkflowname.add(h);
			}
		}
		if (pushtrue != null && !pushtrue.equals("") && pushtrue.equals("1")) {
			Wfd_Actdef actdef = smActInst.GetActDef(actinstid);
			Wfd_Prodef prodef = smProDef.GetProdefById(actdef.getProdef_Id());
			//只对智能审批的件处理
			if (!prodef.getHouse_Status().equals("1"))
				return;
			//只在申请受理环节转出后才推送
			if (!actdef.getActdef_Type().equalsIgnoreCase("1010"))
				return;
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
			String file_number = proinst == null ? "" : proinst.getFile_Number();

			List<BDCS_XMXX> xmxxs = commonDao.getDataList(BDCS_XMXX.class, "project_id='" + file_number + "'");
			BDCS_XMXX xmxx = null;
			if (xmxxs != null && xmxxs.size() > 0) {
				xmxx = xmxxs.get(0);
				//根据本地化网签配置，税务配置 发送短信通知网签部门 和税务部门 WUZHU
				dxSendService.dxsendForHouseAndTax(file_number, xmxx.getYWLSH());
				result.put("ywh", file_number);
				result.put("zlxz", requesturl.substring(0, requesturl.indexOf("operation/passoversuccess/")) + "gx/zl/" + file_number);
				List<BDCS_DJDY_GZ> djdys = commonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='" + xmxx.getId() + "'");
				for (BDCS_DJDY_GZ djdy : djdys) {
					Map house = new HashMap();
					house.put("bdcdyh", djdy.getBDCDYH());
					house.put("jyjg", "");
					house.put("fwzl", "");
					house.put("cqzh", "");
					ConstValue.BDCDYLX bdcdylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
					ConstValue.DJDYLY djdylx = ConstValue.DJDYLY.initFrom(djdy.getLY());
					//获取房屋信息
					RealUnit unit = UnitTools.loadUnit(bdcdylx, djdylx, djdy.getBDCDYID());
					if (unit != null) {
						house.put("fwzl", unit.getZL());
					}
					List<BDCS_QL_GZ> qls = commonDao.getDataList(BDCS_QL_GZ.class, "xmbh='" + xmxx.getId() + "' and djdyid='" + djdy.getDJDYID() + "' and qllx in('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')");
					List<Map> sqrlist = new ArrayList<Map>();
					if (qls != null && qls.size() > 0) {
						BDCS_QL_GZ after_ql = qls.get(0);
						BDCS_QL_XZ before_ql = commonDao.get(BDCS_QL_XZ.class, after_ql.getLYQLID());
						List<BDCS_QLR_GZ> after_qlrs = commonDao.getDataList(BDCS_QLR_GZ.class, "xmbh='" + xmxx.getId() + "' and qlid='" + after_ql.getId() + "' ");
						List<BDCS_QLR_XZ> before_qlrs = commonDao.getDataList(BDCS_QLR_XZ.class, " qlid='" + before_ql.getId() + "' ");
						for (BDCS_QLR_XZ qlr : before_qlrs)//卖方信息，即义务人
						{
							Map oldqlr = new HashMap();
							oldqlr.put("sqrxm", qlr.getQLRMC());
							oldqlr.put("zjlx", ConstHelper.getNameByValue("ZJLX", qlr.getZJZL()));
							oldqlr.put("zjhm", qlr.getZJZL());
							oldqlr.put("lxdh", qlr.getDH());
							oldqlr.put("txdz", qlr.getDZ());
							oldqlr.put("sqrlx", qlr.getQLRLXName());
							oldqlr.put("sqrlb", "义务人");
							oldqlr.put("gyfs", ConstHelper.getNameByValue("GYFS", qlr.getGYFS()));
							oldqlr.put("qlbl", qlr.getQLBL());
							sqrlist.add(oldqlr);
						}
						for (BDCS_QLR_GZ qlr : after_qlrs)//买方信息，即权利人
						{
							Map newqlr = new HashMap();
							newqlr.put("sqrxm", qlr.getQLRMC());
							newqlr.put("zjlx", ConstHelper.getNameByValue("ZJLX", qlr.getZJZL()));
							newqlr.put("zjhm", qlr.getZJZL());
							newqlr.put("lxdh", qlr.getDH());
							newqlr.put("txdz", qlr.getDZ());
							newqlr.put("sqrlx", qlr.getQLRLXName());
							newqlr.put("sqrlb", "权利人");
							newqlr.put("gyfs", ConstHelper.getNameByValue("GYFS", qlr.getGYFS()));
							newqlr.put("qlbl", qlr.getQLBL());
							sqrlist.add(newqlr);
						}
						house.put("sqrlist", sqrlist);
						house.put("jyjg", after_ql.getQDJG());
						house.put("cqzh", before_ql.getBDCQZH());
					}
					houses.add(house);
				}
				result.put("houses", houses);
			}
			String housesjson = JSON.toJSONString(result);
			String error = "";
			Map param = new HashMap();
			param.put("houses", housesjson);
			boolean canpush = false;
			//验证是否为配置得流程类型。
			List<Map> resMap = commonDao.getDataListByFullSql("select WORKFLOWNAME from bdc_workflow.WFD_MAPPING maping where  instr('" + file_number + "','-'||maping.workflowcode||'-')>0");
			if (resMap != null && resMap.size() > 0) {
				String wn = resMap.get(0).get("WORKFLOWNAME").toString();
				if (houseworkflowname.contains(wn) && StringHelper.formatObject(pushtrue).equals("1"))
					canpush = true;
			}
			if (canpush) {
				String r = HttpRequest.sendPost(pushurl, param);
				if (r.equals("")) {
					error = "发送失败";
				} else {
					JSONObject o = JSON.parseObject(r);
					if (o != null && o.getString("success") != null && o.getString("success").equalsIgnoreCase("true")) {

					} else
						error = o.getString("msg");
				}

				LOG_API logapi = new LOG_API();
				logapi.setAPIURL(pushurl);
				logapi.setAPIPARAM(JSON.toJSONString(param));
				logapi.setAPITYPE("POST");
				if (error.equals("")) {
					logapi.setSUCCESS("true");
				} else {
					logapi.setSUCCESS("false");
					logapi.setERROR(error);
				}
				logapi.setOPTIME(new Date());
				logapi.setBZ(xmxx != null ? xmxx.getPROJECT_ID() : "");
				logapi.setSYTYPE("登记系统推送数据至网签");
				commonDao.saveOrUpdate(logapi);
				commonDao.flush();
			}
		}
	}

	/**
	 * @author JHX
	 * @param actinstid status(0 不批准，1批准，3退档)
	 * 
	 * */
	public void modifyProinstState(String actinstid,String status){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("受理", "审批中");
		map.put("审核", "审批中");
		map.put("初审", "审批中");
		map.put("收费", "审批中");
		map.put("缮证", "已制证");
		map.put("归档", "证到窗口");
		map.put("登簿", "已审结");
		map.put("发证", "已制证");
		map.put("复审", "审批中");
		map.put("登簿缮证","已审结");
		map.put("申请受理","审批中");
		map.put("登簿", "已审结");
		map.put("处长派件", "审批中");
		String isinsert = GetProperties.getConstValueByKey("ISINSERT");
		if(isinsert!=null&&!isinsert.equals("")){
				Wfd_Actdef actdef = smActInst.GetActDef(actinstid);
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
				String slbh = proinst==null?"":proinst.getProlsh();
				Wfi_ActInst actinst = smActInst.GetActInst(actinstid);
				String operator="";
				String operatorDept="";
				String actdefName="";
				String reason="";
				if(actinst!=null){
					   operator  = actinst.getStaff_Name();
					   reason = actinst.getPassoverdesc();
				}
				if(actdef!=null){
					actdefName  =actdef.getActdef_Name();
					operatorDept=actdef.getActdef_Dept();
				}
				//如果是退件环节，状态更新为退档
				if(actdefName.equals("退件")){
					status="3";
				}
				//判断流水号的长度，11位是电子政务产生的数据，10位是登记系统产生的数据
				if(slbh.trim().length()==11){
					String state =map.get(actdefName);
					//状态映射
					Connection conn = DButil.getConnection();
					PreparedStatement ps;
					try {
						ps = conn.prepareStatement("insert into 审批流转表   (受理编号,办件状态值,时间,经办人,经办部门,审批结果,不批准理由)"
								+ " values ('"+slbh+"','"+state+"',convert (varchar,getdate(),120),'"+operator+"','"+operatorDept+"','"+status+"','"+reason+"')");
						//ps = conn.prepareStatement("insert into 审批流转表   (受理编号,办件状态值,时间) values ('1234','111',getdate())");
						ps.execute();
						DButil.close(null, ps, conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
				}
		    }
		 }
	}
	/***
	 * @author JHX
	 * @DATE:2016-08-06
	 * 海口项目，执行挂起操作，要操作电子政务的数据库
	 * 操作的表：停止及时表
	 * @param type(1补证材料,2缴税停止计时,3其他,需要走审批)
	 * */
	public boolean hangUp(String actinstid,String type, String fgbh, String fgyj,String fgqx,String msg){
		boolean result=true;
		String isinsert = GetProperties.getConstValueByKey("ISINSERT");
		if(isinsert!=null&&!isinsert.equals("")){
				Wfd_Actdef actdef = smActInst.GetActDef(actinstid);
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
				String slbh = proinst==null?"":proinst.getProlsh();
				Wfi_ActInst actinst = smActInst.GetActInst(actinstid);
				String operator="";
				String operatorid="";
				String operatorDept="";
				String operatorDeptid="";
				String actdefName="";
				String reason="";
				String loginName="";
				if(actinst!=null){
					   operator  = actinst.getStaff_Name();
					   operatorid = actinst.getStaff_Id();
					   reason = actinst.getPassoverdesc();
					   User user = commonDao.get(User.class, operatorid);
					   if(user!=null){
						   loginName = user.getLoginName();
					   } 
				}
				if(actdef!=null){
					actdefName  =actdef.getActdef_Name();
					operatorDept=actdef.getActdef_Dept();
					operatorDeptid = actdef.getActdef_Dept_Id();
				}
				//状态映射
				Connection conn = DButil.getConnection();
				PreparedStatement ps=null;
				try {
					Connection con = DButil.getConnection();
					PreparedStatement psp;
					psp = con.prepareStatement("select MAX(编号) as 编号 from 停止计时表");
					ResultSet rs = psp.executeQuery();
					int bh = 0;
					if(rs!=null){
						String maxbh="";
						while(rs.next()){
						maxbh=rs.getString("编号");
						}
						bh=Integer.parseInt(maxbh);
						bh++;
					}
						
					if(type!=null&&type.equals("1")){
						ps = conn.prepareStatement("insert into 补正材料数据表   (受理编号,单位编号,经办人,经办部门,申请时间,录入员,说明,操作,经办人ID,环节ID)"
								+ " values ('"+slbh+"','"+operatorDeptid+"',"
										+ "'"+operator+"','"+operatorDept+"',convert (varchar,getdate(),120),'"+operator+"','"+msg+"','待发送','"+operatorid+"','"+actinstid+"')");
					}else if(type!=null&&type.equals("2")){
						ps = conn.prepareStatement("insert into 缴税停止计时   (受理编号,单位编号,经办人,经办部门,申请时间,录入员,经办人ID,环节ID)"
								+ " values ('"+slbh+"','"+operatorDeptid+"',"
										+ "'"+operator+"',"
												+ "'"+operatorDept+"',convert (varchar,getdate(),120),'"+operator+"','"+operatorid+"','"+actinstid+"')");
					}else if(type!=null&&type.equals("3")){
						ps = conn.prepareStatement("insert into 停止计时表   (编号,loginname,受理编号,经办人代码,经办人,申请时间,经办处室,法规编号,法规依据,法规期限,具体事由,单位编号,状态,操作,经办人ID,环节ID)"
								+ " values ('"+bh+"','"+loginName+"','"+slbh+"','"+operatorid+"','"+operator+"',convert (varchar,getdate(),120),'"+operatorDept+"','"+fgbh+"','"+fgyj+"',"
										+ "'"+fgqx+"','"+msg+"',"
												+ "'"+operatorDeptid+"','新增','待发送','"+operatorid+"','"+actinstid+"')");
					}

					ps.execute();
					DButil.close(null, ps, conn);
				} catch (SQLException e) {
					result=false;
					e.printStackTrace();
				}finally{
			    }
		}else{result=false;}
		return result;
	}
	/***
	 * @author JHX
	 * @DATE:2016-08-06
	 * 海口项目，执行解挂操作，要操作电子政务的数据库
	 * 操作的表有停止及时表
	 * 
	 * */
	public void hangDown(String actinstid){
		String isinsert = GetProperties.getConstValueByKey("ISINSERT");
		if(isinsert!=null&&!isinsert.equals("")){
			Wfd_Actdef actdef = smActInst.GetActDef(actinstid);
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
			String slbh = proinst==null?"":proinst.getProlsh();
			Wfi_ActInst actinst = smActInst.GetActInst(actinstid);
			String operator="";
			String operatorid="";
			String operatorDept="";
			String operatorDeptid="";
			String actdefName="";
			String reason="";
			String loginName ="";
			if(actinst!=null){
				   operator  = actinst.getStaff_Name();
				   operatorid = actinst.getStaff_Id();
				   User user = commonDao.get(User.class, operatorid);
				   if(user!=null){
					   loginName = user.getLoginName();
				   } 
				   reason = actinst.getPassoverdesc();
			}
			if(actdef!=null){
				actdefName  =actdef.getActdef_Name();
				operatorDept=actdef.getActdef_Dept();
				operatorDeptid = actdef.getActdef_Dept_Id();
			}
			Connection conn = DButil.getConnection();
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement("insert into 恢复停止计时表   (受理编号,经办人代码,经办人,申请时间,经办部门,经办人ID,环节ID)"
						+ " values ('"+slbh+"','"+loginName+"','"+operator+"',convert (varchar,getdate(),120),'"+operatorDept+"','"+operatorid+"','"+actinstid+"')");
				ps.execute();
				DButil.close(null, ps, conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
		    }
		}
	}
	/***
	 * @author JHX
	 * @DATE:2016-08-06
	 * 海口项目，查询法规表
	 * 
	 * 
	 * */
	public List<Map<String,String>> getRule(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//状态映射
		Connection conn = DButil.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("select * from 法规依据表");
			ResultSet rs = ps.executeQuery();
			Map<String,String> map = null;
			if(rs!=null){
				while(rs.next()){
					map=new HashMap<String,String>();  
					map.put("fgbh", rs.getString("法规编号"));
					map.put("fgyj", rs.getString("法规依据"));
					map.put("fgqx", rs.getInt("法规期限")+"");
					list.add(map);
				}

			}
			DButil.close(null, ps, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
	    }
		return list;
	}
}

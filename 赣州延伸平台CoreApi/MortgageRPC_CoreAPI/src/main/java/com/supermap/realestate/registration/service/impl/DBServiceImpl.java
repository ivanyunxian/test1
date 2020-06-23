package com.supermap.realestate.registration.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.dataExchange.JHK.Imp.DataSwapImpEx;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.PUSHFAIL;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.impl.share.ShareTool;
import com.supermap.realestate.registration.tools.SafeHttpSms;
import com.supermap.realestate.registration.tools.ShareMsgTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.service.impl.AccumufundServiceImpl;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

@Service("dbService")
public class DBServiceImpl implements DBService {

	
	@Autowired
	private  CommonDao baseCommonDao;
	@Autowired
	private ShareMsgTools shareMsgTools;
	@Autowired
	private SafeHttpSms sms;
	public Logger logger = Logger.getLogger(DBServiceImpl.class);
	
	@Autowired
	private AccumufundServiceImpl   accumufundServiceImpl;

	
	@Autowired
	private ShareTool sharetool;
	/**
	 * 根据项目编号进行进行登簿
	 * 
	 * @作者：俞学斌
	 * @return
	 */
	@Override
	@Transactional(rollbackFor={Exception.class})
	public ResultMessage BoardBook(String xmbh) throws Exception {
		return boardBookInternal(xmbh);
	}

	private ResultMessage boardBookInternal(String xmbh) throws Exception{
		ResultMessage msg = new ResultMessage();
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		// 在登簿前获取单元，
		List<BDCS_DJDY_GZ> djdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, " XMBH='" + xmbh + "'");
		if (xmxx.getSFDB().equals(SFDB.YES.Value)) {
			msg.setSuccess("false");
			msg.setMsg("项目已经登簿，不能重复登簿！");
			YwLogUtil.addYwLog("项目已经登簿，不能重复登簿！项目名称：" + xmxx.getXMMC(), ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			return msg;
		}else if (xmxx.getGJJYWLSH()!=null) {
			accumufundServiceImpl.GjjZJK(xmbh);
			msg.setSuccess("true");
			msg.setMsg("登簿成功！");
		} else {
			DJHandler dj = HandlerFactory.createDJHandler(xmbh);
			boolean bsuccess = dj.writeDJB();
			if (!bsuccess) {
				msg.setSuccess("false");
				msg.setMsg(dj.getError());
				YwLogUtil.addYwLog("项目登薄失败！项目名称：" + xmxx.getXMMC(), ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			} else {
				String result = "";
				try {
					// 推送到中间库,0不推送，1中间库模式推送（受理和登薄都推）,2MQ模式推送,3接口模式推送（受理和登薄都推）,4中间库模式（仅登薄推）,5接口模式（仅登薄推）,6中间库模式（受理+登薄+缮证）,7接口模式（受理+登薄+缮证）,8中间库模式推送（受理和归档都推）,9接口模式推送（受理和归档都推）
					int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETOZJK"));
					if (zjk == 1||zjk == 3||zjk == 4||zjk == 5||zjk == 6||zjk == 7||zjk == 21||zjk == 24) {
						logger.info("登薄按钮推送");
						// 其中，删掉受理时推送的，重新推送一遍，因为审批过程中属性可能有修改
//						zjkpushType(xmxx, "2");
						SendMsg(xmbh,"2");
					}
				} catch (Exception ex) {
				}

				msg.setSuccess("true");
				msg.setMsg("登簿成功！" + result);
				YwLogUtil.addYwLog("项目登簿成功！项目名称：" + xmxx.getXMMC(), ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
				// 登簿成功之后，发送消息
				// dj.SendMsg("3");

				// 登簿后 依据workflowcode判断是否启用了限制移除功能
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
				String sql = " WORKFLOWCODE='" + workflowcode + "'";
				CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
				List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, sql);

				if (mappings != null && mappings.size() > 0) {
					WFD_MAPPING maping = mappings.get(0);
					if (("1").equals(maping.getSFYCXZ())) {
						String xzlxs = maping.getYCXZLX().toString().replaceAll("&", ",");
						if (xzlxs != null && xzlxs.length() > 0) {
							// 登簿前获取
							for (BDCS_DJDY_GZ dy : djdy) {
								String fromsql = " XZLX IN (" + xzlxs + ") AND BDCDYID='" + dy.getBDCDYID() + "' AND YXBZ='1'";
								List<BDCS_DYXZ> count = baseCommonDao.getDataList(BDCS_DYXZ.class, fromsql);
								if (count != null && count.size() > 0) {
									BDCS_DYXZ XZDY = count.get(0);
									String dbr = Global.getCurrentUserName();
									Date djsj = new Date();
									XZDY.setZXDBR(dbr);
									XZDY.setZXDJSJ(djsj);
									XZDY.setZXYWH(xmxx.getPROJECT_ID());
									XZDY.setLSXZ(XZDY.getXZLX());
//									XZDY.setXZLX("");
									XZDY.setYXBZ("2");
									baseCommonDao.update(XZDY);
								}
							}
						}
					}
				}
			}
		}
		return msg;
	}


	/**
	 * 发送消息
	 * 
	 * @Title: SendMsg
	 * @author:俞学斌
	 * @date：2015年11月25日 下午9:55:33
	 * @param xmbh
	 * @param bljc
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultMessage SendMsg(String xmbh, String bljc) {
		ResultMessage msg = new ResultMessage();
		// 中间库方式
		// 推送到中间库,0不推送，1中间库模式推送（受理和登薄都推），2MQ模式推送，3接口模式推送（受理和登薄都推），4中间库模式（仅登薄推），5接口模式（仅登薄推）
		int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETOZJK"));
		logger.info("WRITETOZJK的值："+zjk);
		//0不推送，1中间库模式推送（受理和登薄都推）,2MQ模式推送,3接口模式推送（受理和登薄都推）,4中间库模式（仅登薄推）,5接口模式（仅登薄推）,6中间库模式（受理+登薄+缮证）,
		//7接口模式（受理+登薄+缮证）,8中间库模式推送（受理和归档都推）,9接口模式推送（受理和归档都推）,21房产和土地中间库模式推送（受理和登薄），24房产和土地中间库模式(仅登薄)
//		if ((zjk == 1||zjk==3) && iBLJD != 2) {
		if ((zjk >0 && zjk!=2)) {
//			 logger.info("进入推送");
			// 中间库方式
			List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class, "xmbh='"+xmbh+"'");
			if (xmxxList != null && xmxxList.size() > 0) {
				if(bljc.equals("100")){
					String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxxList.get(0).getPROJECT_ID());
					List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class,"WORKFLOWCODE='" + workflowcode + "'");
					// 泸州——选择单元后立即推送 判断业务流程是否推送，PUSHTOZJK为2才推送
					if (mappings == null || mappings.size() == 0 || mappings.get(0).getPUSHTOZJK() == null
							|| !mappings.get(0).getPUSHTOZJK().equals("2")) {
						msg.setSuccess("false");
						msg.setMsg("当前流程未配置单元选择后立即推送！");
						return msg;
					}{
						bljc="1";
					}
				}
				//组合流程自动推送会出现重复推送
				BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
				HandlerMapping _mapping = HandlerFactory.getMapping();
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
				String _handleClassName = _mapping.getHandlerClassName(workflowcode);
				if (_handleClassName.contains("ZY_YDYTODY_DJHandler") ||_handleClassName.contains("ZY_YGDYTODY_DJHandler")) {
					sharetool.zjkpushType3(xmxxList.get(0), bljc);
				}else {
					sharetool.zjkpushType(xmxxList.get(0), bljc);
				}
				//吉林 登簿时调用协同接口推送数据到地籍库
				String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
				msg.setSuccess("true");
				msg.setMsg("数据推送成功！");
				YwLogUtil.addYwLog("发送共享信息成功！", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
			}
		} else if (zjk == 2) {
			 logger.info("MQ方式推送");
			// mq方式
			DJHandler dj = HandlerFactory.createDJHandler(xmbh);
			dj.SendMsg(bljc);
		} else if (zjk == 0) {
			msg.setSuccess("false");
			msg.setMsg("已禁止发送共享信息，不发送信息！");
			YwLogUtil.addYwLog("已禁止发送共享信息，不发送信息！", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
		}
		// } else {
		// msg.setSuccess("false");
		// msg.setMsg("已禁止发送共享信息，不发送信息！");
		// }
		return msg;
	}
	
	/**
	 * 推送土地信息
	 * 
	 * @Title: BDCKPushToDJK
	 * @author:卜晓波
	 * @date：2016年12月18日 16:34:05
	 * @param xmbh
	 * @param bljc
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultMessage BDCKPushToDJK(String projectid, String bljc) {
		ResultMessage msg = new ResultMessage();
		// 地籍是否推送
		// 推送到中间库, 1登簿推送地籍共享，0不推送
		int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETODJZJK"));
		if ((zjk==1)) {
			if(projectid.indexOf(",")>0 || projectid.indexOf("，")>0){
				projectid=projectid.replaceAll("，", ",");
				String[] projectidarray=projectid.split(",");
				for(int n=0;n<projectidarray.length;n++){
					String curprojectid=projectidarray[n];
					logger.info("登簿进入地籍共享推送，流程编号:"+curprojectid+"");
					// 中间库方式
					 List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class, "PROJECT_ID='" + curprojectid + "' or xmbh='"+curprojectid+"'");
					 if (xmxxList != null && xmxxList.size() > 0) {
							BDCS_XMXX xmxx = xmxxList.get(0);
							//登薄
							if(xmxx.getSFDB().equals("1")){
								sharetool.BDCKPushToDJK(xmxxList.get(0));								
							}
						}	 
				}
				msg.setSuccess("成功");
				msg.setMsg("数据推送成功！");
				YwLogUtil.addYwLog("发送共享信息成功！", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
			}else{
				logger.info("登簿进入地籍共享推送，流程编号:"+projectid+"");
				if (projectid.length() >0) {
					 logger.info("登簿进入地籍共享推送，流程编号:"+projectid+"");
						// 中间库方式
						 List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class, "PROJECT_ID='" + projectid + "' or xmbh='"+projectid+"'");
							if (xmxxList != null && xmxxList.size() > 0) {
								BDCS_XMXX xmxx = xmxxList.get(0);
								//登薄
								if(xmxx.getSFDB().equals("1")){
									//吉林 登簿时调用协同接口推送数据到地籍库
									String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
									if(XZQHDM.contains("2202")&&bljc.equals("2")){
										sharetool.BDCKPushToDJK(xmxxList.get(0));
									}
									msg.setSuccess("成功");
									msg.setMsg("数据推送成功！");
									YwLogUtil.addYwLog("发送共享信息成功！", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
								}
							}
				}else{
					msg.setSuccess("失败！");
					msg.setMsg("请输入正确Project_id！");
				}
			}
			
		}
		return msg;
	}
	
	/**
	 * 孝感市 关联未关联的共享登记库的项目
	 * 
	 * @Title: Relationdata
	 * @author:卜晓波
	 * @date：2017年5月8日 18:45:49
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultMessage Relationdata() {
		ResultMessage resultMessage=sharetool.Relationdata();
		return resultMessage;
		
	}
	
	@Override	
	public ResultMessage PrintZSSendMsg(String xmbh, String zsid)
			throws Exception {
		// TODO Auto-generated method stub
		ResultMessage msg = new ResultMessage();
//		ShareMsgTools shareMsgTools = new ShareMsgTools();
		try{
			int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETOZJK"));
			if(xmbh != null && !xmbh.equals("") && zsid != null && !zsid.equals("") && zjk==2){
				 shareMsgTools.PrintZSSendMsg(xmbh, zsid);
				 msg.setMsg("消息发送成功！");
				 msg.setSuccess("true");
			}
		}
		catch(Exception ex){
			msg.setMsg("消息发送失败！原因："+ex.getMessage());
			msg.setSuccess("false");
		}
		return msg;
	}
	/**
	 * TODO:@liushufeng:请描述这个方法的作用
	 * 
	 * @Title: boardBookIgnorWarning
	 * @author:liushufeng
	 * @date：2016年1月12日 上午12:29:09
	 * @param xmbh
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor={Exception.class})
	public ResultMessage boardBookIgnorWarning(String xmbh) throws Exception {
		return boardBookInternal(xmbh);
	}

	
	@Override
	public String getXMBHFromActinst_Id(String actinst_id) {
		if (StringHelper.isEmpty(actinst_id)) {
			return "";
		}
		Wfi_ActInst actinst = baseCommonDao.get(Wfi_ActInst.class, actinst_id);
		if (actinst == null) {
			return "";
		}
		String proinst_id = actinst.getProinst_Id();
		if (StringHelper.isEmpty(proinst_id)) {
			return "";
		}
		Wfi_ProInst proinst = baseCommonDao.get(Wfi_ProInst.class, proinst_id);
		if (proinst == null) {
			return "";
		}
		String ywlsh = proinst.getProlsh();
		if (!StringHelper.isEmpty(ywlsh)) {
			List<BDCS_XMXX> list_xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, "YWLSH='" + ywlsh + "'");
			if (list_xmxx != null && list_xmxx.size() > 0) {
				return list_xmxx.get(0).getId();
			}
		}
		String project_id = proinst.getFile_Number();
		if (!StringHelper.isEmpty(project_id)) {
			List<BDCS_XMXX> list_xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, "PROJECT_ID='" + project_id + "'");
			if (list_xmxx != null && list_xmxx.size() > 0) {
				return list_xmxx.get(0).getId();
			}
		}
		return "";
	}

	

	@Override
	public void pushdata(String projectids, String bljd) {
		sharetool.Pushsingledata(projectids,bljd);
		
	}

	/**
	 * 检查表是否存在
	 * @作者 think
	 * @创建时间 2016年9月6日下午9:40:48
	 * @param owner
	 * @param tablename
	 * @return
	 */
	boolean tableExist(String owner,String tablename) {
    	 boolean b=false;
    	 String sql="SELECT COUNT(1) as count FROM DBA_TABLES WHERE TABLE_NAME=UPPER('"+tablename+"') AND OWNER='"+owner+"'";
  		try {
  			List<Map> li = baseCommonDao.getDataListByFullSql(sql);
  			if (li.size() > 0) {
  				int k =Integer.parseInt( li.get(0).get("COUNT").toString());
  				if(k>0){
  					b=true;
  				}
  			}
 		} catch (Exception ex) {
 		}
  		return b;
	}

    @Override
   public ResultMessage SendSmsMsg(String project_id, String smsIp)
   		throws Exception {
   	// TODO Auto-generated method stub
   	ResultMessage rm = new ResultMessage();
   	String msg = sms.SendSMSMsg(project_id, smsIp);
   	rm.setMsg(msg);
   	rm.setSuccess("true");
   	return rm;
   }

    /**
	 * 批量推送数据到中间库
	 * 
	 * @作者 likun
	 * @创建时间 2016年1月26日下午4:50:53
	 * @param bljc
	 *            办理阶段1受理，2登薄
	 */
	public void PushBatchData() {
//		DataSwapImpEx dataSwapImpEx2=new DataSwapImpEx();
//		dataSwapImpEx2.checkAndPushXZDJ(baseCommonDao);
		logger.info("批量推送被调用");
		//分批推送
		getGXDJKPrjSQL();
//		String sql="select XMBH from bdck.BDCS_XMXX  where "+subsql+" order by slsj asc";
		String sql="select XMBH from bdck.BDCS_XMXX x where  not exists(select t.project_id from bdck.gxdj_xm t where x.project_id=t.project_id ) order by slsj asc";
//		List<Map> countList= baseCommonDao.getDataListByFullSql(sql);
//		if(countList==null &&countList.size()==0){
//			return;
//		}
//		String hql1 = " 1 > 0 order by slsj";
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
//		String tt=countList.get(0).get("TOTAL").toString();
//		int total=Integer.parseInt(tt);
//		int pagesize=200;  //每次取几个
//		int turnCount=total/pagesize +1; //循环次数
//		for(int k=0;k<turnCount;k++){
		if (xzqdm != null && xzqdm.contains("1301")) {
			// 石家庄
			sql = " select XMBH from bdck.BDCS_XMXX x where not exists(select t.project_id from bdck.gxdj_xm t where x.project_id=t.project_id ) and x.project_id not in (SELECT PROJECT_ID FROM bdck.BDCS_XMXX WHERE (DJLX='200' AND QLLX='4') OR (DJLX='100' AND QLLX='23') OR (DJLX='400' AND QLLX='23'))  ";// x.project_id not in(select t.project_id from gxdjk.gxjhxm t where 1>0)";
		}
		List<Map> xmxxList=baseCommonDao.getDataListByFullSql(sql);//   getPageDataByHql(BDCS_XMXX.class, hql1, k+ 1, pagesize);
		
//		List<BDCS_XMXX> xmxxList=(List<BDCS_XMXX>) page.getResult();
		// 先将所有数据都推过去，都设置成登薄阶段，不用多线程推送，连接数会超出
//		List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class, hql1);
		if (xmxxList != null && xmxxList.size() > 0) {
			int i=0;
			for (Map map : xmxxList) {
				BDCS_XMXX xmxx=baseCommonDao.get(BDCS_XMXX.class, map.get("XMBH").toString());
				sharetool.zjkpushType2(xmxx, "2");
				i++;
				if(i==200){
					i=0;
					try {
					    Thread.sleep(5000);                 //1000 毫秒，也就是1秒.
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
				}
			}
		}
		// 再将未登簿的办理阶段值改过来
		sql="select XMBH from bdck.BDCS_XMXX x where not exists(select t.project_id from bdck.gxdj_xm t where x.project_id=t.project_id ) and x.SFDB='0'  order by slsj asc"; 
		if (xzqdm != null && xzqdm.contains("1301")) {
			// 石家庄
			sql = " select XMBH from bdck.BDCS_XMXX x where not exists(select t.project_id from bdck.gxdj_xm t where x.project_id=t.project_id ) and x.SFDB='0' and x.project_id not in (SELECT PROJECT_ID FROM bdck.BDCS_XMXX WHERE (DJLX='200' AND QLLX='4') OR (DJLX='100' AND QLLX='23') OR (DJLX='400' AND QLLX='23'))  ";
		}
//		 page=baseCommonDao.getPageDataByHql(BDCS_XMXX.class, hql2, k+ 1, pagesize);
//		List<BDCS_XMXX> xmxxWdbList=(List<BDCS_XMXX>) page.getResult();
//		List<BDCS_XMXX> xmxxWdbList = baseCommonDao.getDataList(BDCS_XMXX.class, hql2);
		List<Map> xmxxWdbList=baseCommonDao.getDataListByFullSql(sql);
		if (xmxxWdbList != null && xmxxWdbList.size() > 0) {
			int i=0;
			for (Map map : xmxxWdbList) {
				BDCS_XMXX xmxx=baseCommonDao.get(BDCS_XMXX.class, map.get("XMBH").toString());
				DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
				dataSwapImpEx.updateBLJD(xmxx.getPROJECT_ID(), "1");
				try {
					dataSwapImpEx.getJyConnection().close();
				} catch (SQLException e) {
				}
				i++;
				if(i==200){
					i=0;
					try {
					    Thread.sleep(5000);                 //1000 毫秒，也就是1秒.
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
				}
			}
		}
//		}
		//检查补推单元限制信息
		DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
		dataSwapImpEx.checkAndPushXZDJ(baseCommonDao);
	}

	String getGXDJKPrjSQL(){
		StringBuilder sBuilder=new StringBuilder();
		com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ	CommonDaoDJ=(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ) SuperSpringContext.getContext().getBean("baseCommonDaoDJ");
		String sql="select distinct(PROJECT_ID) from gxdjk.gxjhxm  where PROJECT_ID is not null";
		List<Map> xmList =CommonDaoDJ.getDataListByFullSql(sql);
		sql="truncate table bdck.gxdj_xm";
		if(xmList!=null&&xmList.size()>0){
			int count=xmList.size();
			int k=0;
			StringBuilder sbBuilder=new StringBuilder();
			//清空原有数据
			try {
				baseCommonDao.excuteQueryNoResult(sql);
			} catch (SQLException e1) {
			}
			for(int i=0;i<count;i++){
				String bdcdyid="0";
				if(xmList.get(i)!=null&&xmList.get(i).get("PROJECT_ID")!=null){
					bdcdyid=xmList.get(i).get("PROJECT_ID").toString();
					sql="insert into bdck.gxdj_xm (PROJECT_ID) values('"+bdcdyid+"')";
					try {
						baseCommonDao.excuteQueryNoResult(sql);
						
					} catch (SQLException e) {
					}
				}
//				sbBuilder.append("'");
//				sbBuilder.append(bdcdyid);
//				sbBuilder.append("',");
//				if(k==count-1||k==980){//
//					//每980个执行一次，一次不能超过1000
//					k=0;
//					String sb=sbBuilder.toString();
//			    	 sql +=" project_id not in("+sb.substring(0,sb.length()-1)+") AND ";
//			    	 
//			    	 //清空一下
//			    	 sbBuilder.setLength(0);
//				}
//				k++;
			}
			baseCommonDao.flush();
			
		}
		return "";
	}
	
	/**
	 * 单个或多个以逗号隔开的projectid推送数据到中间库
	 * 
	 * @作者 likun
	 * @创建时间 2016年1月26日下午4:50:53
	 * @param project_id
	 *            登记系统中项目编号
	 */
	public void Pushsingledata(String projectid) {
		logger.info("单条页面推送被调用");
		if(projectid.indexOf(",")>0 || projectid.indexOf("，")>0){
			projectid=projectid.replaceAll("，", ",");
			String[] projectidarray=projectid.split(",");
			for(int n=0;n<projectidarray.length;n++){
				String curprojectid=projectidarray[n];
				List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class, "project_id='" + curprojectid + "'");
				if (xmxxList.size() > 0) {
					int k=0;
					for (int i = 0; i < xmxxList.size(); i++) {
						String bljd = "1";
						String sfdb=xmxxList.get(i).getSFDB();
						if (sfdb!=null &&sfdb.equals("1")) {
							bljd = "2";
						}
						sharetool.zjkpushType(xmxxList.get(i), bljd);
						//连续大量操作会死，休息5秒吧
						k++;
						if(k==200){
							k=0;
							try {
							    Thread.sleep(5000);                 //1000 毫秒，也就是1秒.
							} catch(InterruptedException ex) {
							    Thread.currentThread().interrupt();
							}
						}
					}
				}
			}
		}else{
			List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class, "project_id='" + projectid + "'");
			if (xmxxList.size() > 0) {
				int k=0;
				for (int i = 0; i < xmxxList.size(); i++) {
					String bljd = "1";
					if (xmxxList.get(i).getSFDB().equals("1")) {
						bljd = "2";
					}
					sharetool.zjkpushType(xmxxList.get(i), bljd);
					
					//连续大量操作会死，休息5秒吧
					k++;
					if(k==200){
						k=0;
						try {
						    Thread.sleep(5000);                 //1000 毫秒，也就是1秒.
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
					}
				}
			}
		}
	}

	public void PushArchivesState(String projectid) {
		DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
		dataSwapImpEx.updateBLJD(projectid, "5");
	};

	/**
	 * 更新推送失败数据 李堃
	 */
	public void refreshfail() {
		try {
			DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
			 //保存到bdck的fail表
			if(tableExist("BDCK", "PUSHFAIL")){
				dataSwapImpEx.reFreshFailTable2(baseCommonDao);
			}
			else {
				dataSwapImpEx.reFreshFailTable(baseCommonDao);
			}
			
		} catch (Exception ex) {
		}
	}

	/**
	 * 供齐齐哈尔房产系统调用回写更新新Relationid
	 * 
	 * @作者：卜晓波  
	 * @param casenum
	 *            项目编号
	 * @param fwzt
	 *            房屋状态
	 * @param relationid
	 *            旧relationid
	 * @param nrelationid
	 *            新relationid
	 * @return
	 * @throws Exception
	 */
 	public Map<String, String> UPDATERELATIONID(String fwzt,String relationid, String nrelationid){
 		Map<String, String> resultmap = new HashMap<String, String>();
 		try{
 			//对房产调用接口时间、修改参数内容做出日志文件记录
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fctime=sdf1.format(new Date());
 			logger.info("齐齐哈尔市房产系统调用Relationid回写接口日志记录：调用时间：" + fctime + ",房屋状态：" + fwzt + ",原房屋ID:"+relationid + ",新房屋ID:"+nrelationid);
 			String sql="RELATIONID = '"+relationid+"'";
 			if(fwzt!=null && fwzt.equals("2")){
 				List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, sql);
 				if (h_xzs != null && h_xzs.size() > 0) {
 					for(BDCS_H_XZ h_xz:h_xzs){
 						h_xz.setRELATIONID(nrelationid);
 						baseCommonDao.update(h_xz);
 						resultmap.put("1", "操作成功");
 					}
 				}else{
 					resultmap.put("400", "参数错误");
 				}
 				return resultmap;
 			}else if(fwzt!=null && fwzt.equals("1")){
 				List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(BDCS_H_XZY.class, sql);
 				if (h_xzys != null || h_xzys.size() > 0) {
 					for(BDCS_H_XZY h_xzy:h_xzys){
 						h_xzy.setRELATIONID(nrelationid);
 						baseCommonDao.update(h_xzy);
 					}
 				}
 				resultmap.put("1", "操作成功");
 				return resultmap;
 			}else{
 				resultmap.put("400", "参数错误");
 				return resultmap;
 			}
 		}catch(Exception e){
 			resultmap.put("401", "程序错误");
 			return resultmap;
 		}
 	}

 	/**
	 * 批量推送pushfail表中的所有数据
	 * 
	 * @作者 think
	 * @创建时间 2016年10月11日下午2:39:04
	 */
	 public void PushBatchDataInFail(String nowhere) {
		  List<PUSHFAIL> failList=baseCommonDao.getDataList(PUSHFAIL.class, nowhere);
		  if(failList!=null&&failList.size()>0){
			  List<String> list=new ArrayList<String>();
			  for(int i=0;i<failList.size();i++){
				  String projectid=failList.get(i).getPROJECT_ID();
				  if(projectid!=null&&!list.contains(projectid)){
					  list.add(projectid);
				  }
			  }
			  String prjString=list.toString().replace("[", "");
			  prjString=prjString.replace("]", "");
			  prjString=prjString.replace(" ", "");
			  //批量推送
			  Pushsingledata(prjString);
			  //检查推送单元限制表数据
			  DataSwapImpEx dSwapImpEx=new DataSwapImpEx();
			  dSwapImpEx.checkAndPushXZDJ(baseCommonDao);
		  }
	  }

	/**
	 * 只更新限制类推送失败数据 李堃
	 */
	public void refreshXZfail() {
		try {
			DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
			// 保存到bdck的fail表
			if (tableExist("BDCK", "PUSHFAIL")) {
				dataSwapImpEx.reFreshXZFailTable2(baseCommonDao);
				
			} else {
				dataSwapImpEx.reFreshXZFailTable(baseCommonDao);
			}
			//检查补推权籍的限制数据
			dataSwapImpEx.checkAndPushXZDJ(baseCommonDao);

		} catch (Exception ex) {
		}
	}

	/**
	 * 供权籍系统限制登记时调用，按照共享逻辑，推送gxjhxm、h、xzdj表
	 * 
	 * @作者：卜晓波 2017年5月24日 11:08:57
	 * @return
	 * @throws Exception
	 */
	public ResultMessage qjSendMessage(String inputLine,String xzly) {
		ResultMessage msg=new ResultMessage();
		try{
			if (inputLine != null) {
				sharetool.qjSendMessage(inputLine,xzly);
				msg.setSuccess("成功");
				msg.setMsg("权籍限制调用协同接口推送成功");
			}
		}catch(Exception e){
			msg.setSuccess("失败");
			msg.setMsg(e.toString());
		}
		return msg;
	}

	@Override
	public ResultMessage isTheLastDY(String xmbh) {
		ResultMessage msg = new ResultMessage();
		ProjectInfo projectInfo=ProjectHelper.GetPrjInfoByXMBH(xmbh);
		String lcbh = projectInfo.getBaseworkflowcode();
		if(lcbh.equals("ZX016")){
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
			// 在登簿前获取单元，
			List<BDCS_DJDY_GZ> djdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, " XMBH='" + xmbh + "'");

			if (xmxx.getSFDB().equals(SFDB.YES.Value)) {
				msg.setSuccess("false");
				msg.setMsg("项目已登簿，不需要再处理！");
			} else {
				if(djdy.size() == 1){
					String bdcdyid = djdy.get(0).getBDCDYID();
					BDCS_H_XZ house = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
					String zdbdcdyid = house.getZDBDCDYID();
					//BDCS_SHYQZD_XZ zd = baseCommonDao.get(BDCS_SHYQZD_XZ.class, zdbdcdyid);
					
					List<BDCS_H_XZ> houses = baseCommonDao.getDataList(BDCS_H_XZ.class, "zdbdcdyid = '"+zdbdcdyid+"'");
					if(houses.size() == 1){
						msg.setMsg(zdbdcdyid);
						msg.setSuccess("true");
					}
					else{
						msg.setSuccess("false");
					}
				}
			}
		}
		else{
			msg.setSuccess("false");
		}
		
		return msg;
	}

	@Override
	public Map<String, String> read() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

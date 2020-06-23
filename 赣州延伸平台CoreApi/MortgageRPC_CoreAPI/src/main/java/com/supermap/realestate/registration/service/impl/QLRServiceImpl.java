package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.QLRService;
import com.supermap.realestate.registration.tools.GeoOperateTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.util.Message;

@Service("qlrService")
public class QLRServiceImpl implements QLRService {

	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 获取大宗地上权利人信息
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message getZDQLRInfo(String zdbdcdyid, int pageIndex, int pageSize,Map<String, Object> mapCondition) {
		 Message msg=new Message();
		 String basesql="select QLR.QLRID,QLR.QLRMC,QLR.QLID,QLR.ZJZL,QLR.ZJH,QLR.BDCQZH,QLR.GYFS,QLR.GYQK,DJDY.BDCDYLX ";
		 StringBuilder sql=new StringBuilder();
		 sql.append("  FROM BDCK.BDCS_DJDY_XZ DJDY  ");
		 sql.append("  LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID  ");
		 sql.append(" LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID WHERE 1=1 ");
		 //可以添加其它条件判断
			for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Object value = mapCondition.get(key);
				if (StringUtils.hasLength(value.toString())) {
					sql.append(" and " + key + " like '%" + value + "%' ");
				}
			}
			sql.append("AND  QLR.QLRID IS NOT NULL AND DJDY.BDCDYID='");
			sql.append(zdbdcdyid).append("'");
			Long count=baseCommonDao.getCountByFullSql(sql.toString());
			List<Map> lst=baseCommonDao.getPageDataByFullSql(basesql+sql.toString(), pageIndex, pageSize);
		    msg.setTotal(count);
		    msg.setRows(lst);
		    return msg;
	}

	/**
	 * 注销权利人信息
	 */
	@Override
	public ResultMessage writeHolderBook(String zdbdcdyid, String qlrids,String ywhs) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(qlrids)) {
			String[] arrqlrid1 = qlrids.split(",");
			String[] arrywh = ywhs.split(",");
			/****************************通过zxywh分组*************************************/
			List<String> lstqlrid = java.util.Arrays.asList(arrqlrid1);
			Map<String, String> lstmap = new HashMap<String, String>();
			List<String> temp = new ArrayList<String>();
			for (int j = 0; j < arrywh.length; j++) {
				if (!temp.contains(arrywh[j])) {
					temp.add(arrywh[j]);
				}
			}
			for (int t = 0; t < temp.size(); t++) {
				String strt = temp.get(t);
				String qlrid = "";
				for (int j = 0; j < arrywh.length; j++) {
					if (arrywh[j].equals(strt)) {
						if (qlrid.equals("")) {
							qlrid = arrqlrid1[j];
						} else {
							qlrid = qlrid + "," + arrqlrid1[j];
						}
					}
				}
				if (!qlrid.equals("")) {
					lstmap.put(strt, qlrid);
				}
			}
			/*****************************************************************/
			for (String key : lstmap.keySet()) {
				String value = lstmap.get(key);
				String[] arrqlrid = value.split(",");
				Rights newrights=null;
				SubRights newsubrights=null;
				String bdcqzhs="";
				String bdcqzhxhs="";
				String newqlid=(String) SuperHelper.GeneratePrimaryKey();
				String newfsqlid=(String) SuperHelper.GeneratePrimaryKey();
				for(int i=0;i<arrqlrid.length;i++){
					String qlrid=arrqlrid[i];
					String ywh=key;
					if(!StringHelper.isEmpty(qlrid)){
						RightsHolder oldholder=RightsHolderTools.loadRightsHolder(DJDYLY.XZ, qlrid);
						if(!StringHelper.isEmpty(oldholder)){
							String qlid=oldholder.getQLID();
							String bdcqzh=oldholder.getBDCQZH();
							BDCS_QLR_XZ qlr=(BDCS_QLR_XZ) oldholder;
							String bdcqzhxh=qlr.getBDCQZHXH();
							if(!StringHelper.isEmpty(bdcqzh))
							if(bdcqzhs.equals("")){
								bdcqzhs=bdcqzh;
							}else{
								bdcqzhs=bdcqzhs+","+bdcqzh;
							}
							if(!StringHelper.isEmpty(bdcqzhxh))
								if(bdcqzhxhs.equals("")){
									bdcqzhxhs=bdcqzhxh;
								}else{
									bdcqzhxhs=bdcqzhxhs+","+bdcqzhxh;
								}
							//权利信息
							if(newrights ==null){
								Rights oldrights=RightsTools.loadRights(DJDYLY.XZ, qlid);
								 newrights=new BDCS_QL_GZ();
								if(oldrights !=null){
									ObjectHelper.copyObject(oldrights, newrights);
								}
								newrights.setFSQLID(newfsqlid);
							    newrights.setId(newqlid);
							    List<BDCS_XMXX> xmxxs=baseCommonDao.getDataList(BDCS_XMXX.class, "YWLSH='"+ywh+"'");
							    Date djsj=new Date();
							    if(xmxxs !=null && xmxxs.size()>0){
							    	if(!StringHelper.isEmpty(xmxxs.get(0).getDJSJ())){
							    		djsj=xmxxs.get(0).getDJSJ();
							    	}
							    }
							    newrights.setDJSJ(djsj);
							    newrights.setDBR(Global.getCurrentUserName());	
							    if(newsubrights ==null){
									//附属权利信息
								    SubRights oldsubrights=RightsTools.loadSubRights(DJDYLY.XZ, oldrights.getFSQLID());
								     newsubrights=new BDCS_FSQL_GZ();
								    if(oldsubrights !=null){
								    	 ObjectHelper.copyObject(oldsubrights,newsubrights);
								    }
								    newsubrights.setId(newfsqlid);
								    newsubrights.setQLID(newqlid);
								    newsubrights.setZXDYYWH(ywh);
								}
							}
						    
						    //删除权利人、权利及附属权利
//						    baseCommonDao.deleteEntity(oldsubrights);
//						    baseCommonDao.deleteEntity(oldrights);
						    baseCommonDao.deleteEntity(oldholder);						 
						    //修改权利人
						    RightsHolder lsholder=RightsHolderTools.loadRightsHolder(DJDYLY.LS, qlrid);
						    lsholder.setQLID(newqlid);
						    baseCommonDao.update(lsholder);
						    //权地证人信息
						    List<BDCS_QDZR_XZ> lstqdzr=new ArrayList<BDCS_QDZR_XZ>();
						    List<BDCS_QDZR_XZ> lstoldqdzr=baseCommonDao.getDataList(BDCS_QDZR_XZ.class, "QLRID='"+qlrid+"'");
						    List<String> zsids=new ArrayList<String>();
						    if(lstoldqdzr !=null && lstoldqdzr.size()>0){
						    	BDCS_QDZR_XZ qdzr=lstoldqdzr.get(0);
						    	if(qdzr !=null){
						    		String zsid=qdzr.getZSID();
						    		if(!zsids.contains(zsid) && !StringHelper.isEmpty(zsid)){
						    			zsids.add(qdzr.getZSID());
						    		}
						    		lstqdzr.add(qdzr);
						    	}
						    }
						    //从证书ID判断是否还有其它人共用一个证，若存在，不删除证书
						    for(String zsid:zsids){
						    	boolean isremovezsflag=false;
						    	List<BDCS_QDZR_XZ> lst=baseCommonDao.getDataList(BDCS_QDZR_XZ.class, "ZSID='"+zsid+"'");
						    	if(lst !=null && lst.size()>0){
						    		for(BDCS_QDZR_XZ qdzr:lst){
						    			String oldqlrid=qdzr.getQLRID();
						    			if(!lstqlrid.contains(oldqlrid)){
						    				isremovezsflag=true;
						    				break;
						    			}
						    		}
						    	}
						    	if(!isremovezsflag){
						    		baseCommonDao.delete(BDCS_ZS_XZ.class, zsid);
						    	}
						    	
						    }
						    for(BDCS_QDZR_XZ qdzr:lstqdzr){
						    	if(!StringHelper.isEmpty(qdzr)){
						    		baseCommonDao.deleteEntity(qdzr);
						    		BDCS_QDZR_LS lsqdzr=baseCommonDao.get(BDCS_QDZR_LS.class, qdzr.getId());
						    		if(lsqdzr !=null){
						    			lsqdzr.setQLID(newqlid);
						    			lsqdzr.setFSQLID(newfsqlid);
						    			baseCommonDao.update(lsqdzr);
						    		}
						    	}
						    }
						}
					}
				}
				   //拷贝工作到历史
			    BDCS_QL_LS lsql=new BDCS_QL_LS();
			    BDCS_FSQL_LS lsfsql=new BDCS_FSQL_LS();
			    newrights.setBDCQZH(bdcqzhs);
			    BDCS_QL_GZ ql= (BDCS_QL_GZ) newrights; 
			    ql.setBDCQZHXH(bdcqzhxhs);
			    ObjectHelper.copyObject(ql, lsql);
			    ObjectHelper.copyObject(newsubrights, lsfsql);
			    baseCommonDao.save(lsql);
			    baseCommonDao.save(lsfsql);
			}
		}
		baseCommonDao.flush();
		 StringBuilder sql=new StringBuilder();
		 sql.append("  FROM BDCK.BDCS_DJDY_XZ DJDY  ");
		 sql.append("  LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID  ");
		 sql.append(" LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID WHERE 1=1 ");
		 sql.append("AND  QLR.QLRID IS NOT NULL AND DJDY.BDCDYID='");
		 sql.append(zdbdcdyid).append("'");
		 Long count=baseCommonDao.getCountByFullSql(sql.toString());
		if(count>0){
			msg.setSuccess("1");
			msg.setMsg("登簿成功");
		}else{
			msg.setSuccess("0");
			msg.setMsg("是否单元灭失");
		}
		return msg;
		
	}

	/**
	 * 单元灭失
	 */
	@Override
	public ResultMessage writeUnitBook(String zdbdcdyid) {
		ResultMessage msg=new ResultMessage();
		msg.setMsg("登簿失败");
		List<BDCS_DJDY_XZ> lstdjdy=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+zdbdcdyid+"'");
	    if(lstdjdy !=null && lstdjdy.size()>0){
	    	BDCS_DJDY_XZ djdy=lstdjdy.get(0);
	    	String bdcdylx=djdy.getBDCDYLX();
	    	BDCDYLX lx=BDCDYLX.initFrom(bdcdylx);
	    	RightsTools.deleteRightsAllByCondition(DJDYLY.XZ, "DJDYID='"+lstdjdy.get(0).getDJDYID()+"'");
	    	baseCommonDao.deleteEntity(djdy);
	    	UnitTools.deleteUnit(lx, DJDYLY.XZ, zdbdcdyid);
	    	if (BDCDYLX.SYQZD.equals(lx) || BDCDYLX.SHYQZD.equals(lx)) {
				GeoOperateTools.DeleteFeatures("BDCK", lx.XZTableName, "BDCDYID='" + zdbdcdyid + "'");
		    	DeleteBdck_("BDCK", lx.XZTableName, "BDCDYID='" + zdbdcdyid + "'");
			}
	    	baseCommonDao.flush();
	    	msg.setMsg("单元已灭失");
	    }
	    return msg;
	}
	
	/**
	 * 删除空间数据
	 * @Title: DeleteFeatures
	 * @author:heks
	 * @date：2017年09月18日 下午17:14:45
	 * @param _desDsName
	 *            目标数据源别名
	 * @param _desDtName 目标数据集名
	 * @param _conditon 查询条件
	 */
	public void DeleteBdck_(String _desDsName,String _desDtName,String _conditon) {
		_desDtName=_desDtName.replaceAll("BDCS_", "BDCK_");
		_desDtName=_desDtName.replaceAll("DCS_", "BDCK_");
		String deleteFulSql = "delete from "+_desDsName+"."+_desDtName+" where "+_conditon;
		baseCommonDao.updateBySql(deleteFulSql);
		String deletebdcdck_fulsql = "delete from BDCDCK."+_desDtName+" where "+_conditon;
		baseCommonDao.updateBySql(deletebdcdck_fulsql);
		baseCommonDao.flush();
	}
	

}

package com.supermap.realestate.registration.handlerImpl;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 1、只能处理强制转移登记（赣州专用）
 */
/**
 * 
* 强制转移登记处理类（赣州专用）
* @ClassName: QZZYDJHandler 
* @author liushufeng 
* @date 2015年9月8日 下午10:36:43
 */
public class QZZYDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public QZZYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
				e.printStackTrace();
			}

			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			fsql.setQLID(ql.getId());
			ql.setFSQLID(fsql.getId());
			// 如果是使用权宗地，把使用权面积加上
			if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ xzshyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				if (xzshyqzd != null) {
					fsql.setSYQMJ(xzshyqzd.getZDMJ());
					ql.setQDJG(xzshyqzd.getJG());// 取得价格
				}
			}

			//做转移的时候加上来源权利ID
			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hql = " DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray;
			String lyqlid = "";
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if (list != null && list.size() > 0) {
				lyqlid = list.get(0).getId();
				ql.setLYQLID(lyqlid);
			}
			// 保存权利和附属权利
			dao.save(ql);
			dao.save(fsql);
			dao.save(djdy);
			// 拷贝转移前权利人到申请人
			super.CopySQRFromXZQLR(djdy.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.YF.Value);
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.removeQLFromXZByQLLX(djdyid);
					super.removeQLRFromXZByQLLX(djdyid);
					super.removeQDZRFromXZByQLLX(djdyid);
					super.removeZSFromXZByQLLX(djdyid);

					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					
					String bdcdyid=bdcs_djdy_gz.getBDCDYID();
					//把现状层里边的查封信息删除掉，
					String sql=" QLID IN (SELECT DISTINCT id FROM BDCS_QL_XZ WHERE  DJDYID=''{0}'' AND DJLX=''{1}'' AND QLLX=''{2}'')";
					String sql2=" DJDYID=''{0}'' AND DJLX=''{1}'' AND QLLX=''{2}''";
					sql=MessageFormat.format(sql, djdyid,DJLX.CFDJ.Value,"99");
					sql2=MessageFormat.format(sql2, djdyid,DJLX.CFDJ.Value,"99");
					getCommonDao().deleteEntitysByHql(BDCS_QL_XZ.class, sql2);
					getCommonDao().deleteEntitysByHql(BDCS_FSQL_XZ.class, sql);
					//根据本地化配置来确定时候注销期房查封(1为是，2为否)
					String is_cancleqfcf_xfqzgh=ConfigHelper.getNameByValue("IS_CANCLEQFCF_XFQZGH");
					if ("1".equals(is_cancleqfcf_xfqzgh)) {
						List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
						REA = getCommonDao().getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+bdcdyid+"'");
						if(REA.size() > 0 ){
							List<BDCS_DJDY_XZ> YCH = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getYCBDCDYID()+"'");
							if(YCH != null && YCH.size() > 0){
								List<BDCS_QL_XZ> cfqls = getCommonDao().getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+YCH.get(0).getDJDYID()+"'");
								for (int i = 0; i < cfqls.size(); i++) {
									getCommonDao().deleteEntity(cfqls.get(i));
									if (!StringHelper.isEmpty(cfqls.get(i).getFSQLID())){
										getCommonDao().delete(BDCS_FSQL_XZ.class, cfqls.get(i).getFSQLID());
										//把历史层里边的查封记录填写上注销登簿人和注销原因等
										BDCS_FSQL_LS fsql_LS = getCommonDao().get(BDCS_FSQL_LS.class, cfqls.get(i).getFSQLID());
										fsql_LS.setZXFJ("现房强制过户注销期房查封");
										fsql_LS.setZXDBR(Global.getCurrentUserName());
										fsql_LS.setZXSJ(new Date());
										fsql_LS.setZXDYYWH(super.getProject_id());
										fsql_LS.setZXDYYY("现房强制过户注销期房查封");
										getCommonDao().update(fsql_LS);
									}								
								}
							}
						}
					}
				
					//把历史层里边的查封记录填写上注销登簿人和注销原因等
					String sql3=" QLID IN (SELECT DISTINCT id FROM BDCS_QL_LS WHERE  DJDYID=''{0}'' AND DJLX=''{1}'' AND QLLX=''{2}'')";
					sql3=MessageFormat.format(sql3, djdyid,DJLX.CFDJ.Value,"99");
					List<BDCS_FSQL_LS> lsqls=getCommonDao().getDataList(BDCS_FSQL_LS.class, sql3);
					if(lsqls!=null && lsqls.size()>0)
					{
						for(BDCS_FSQL_LS fsql:lsqls)
						{
							fsql.setZXFJ("强制过户");
							//这个地方get到的是当前强制转移后的数据，存值错误，登记簿册获取到的信息就不正确。
							//fsql.setZXDBR(Global.getCurrentUserName());
							//fsql.setZXSJ(new Date());
							//fsql.setZXDYYWH(super.getProject_id());
							fsql.setZXDYYY("强制过户");
							getCommonDao().update(fsql);
						}
					}
					
					BDCDYLX dylx=BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
					DJDYLY dyly=DJDYLY.initFrom(bdcs_djdy_gz.getLY());
					
					//把房屋的限制状态改为无
					House house=(House)UnitTools.loadUnit(dylx,dyly, bdcdyid);
					if(house!=null)
					{
						house.setXZZT(null);
						getCommonDao().update(house);
					}
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);// list.get(0);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除权利关联申请人
			super.RemoveSQRByQLID(djdyid, getXMBH());

			//删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
			
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	/**
	 * 根据申请人ID添加权利人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

	/**
	 * 获取错误信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日下午8:11:43
	 * @return
	 */
	@Override
	public String getError() {
		return super.getErrMessage();
	}

	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return null;
	}

	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
	@Override
	public void SendMsg(String bljc){
		BDCS_XMXX xmxx=getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter=ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql=RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql=RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg= super.getShareMsgTools().GetMsg(bdcdy,bdcql,bdcfsql,bdcqlrs,bljc,xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy+1,djdy.getBDCDYLX(),xmxx);
			}
		}
	}
}

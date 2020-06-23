package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、国有建设用地使用权抵押预告登记
 2、宅基地使用权抵押预告登记
 3、集体建设用地抵押预告登记
 4、国有建设用地使用权/房屋所有权抵押预告登记
 5、宅基地使用权/房屋所有权抵押预告登记
 6、集体建设用地使用权/房屋所有权抵押预告登记
 */
/**
 * 
* 实测绘单元抵押预告登记
* @ClassName: DYYGDJHandler 
* @author 俞学斌
* @date 2015年12月27日 下午10:27:40
 */
public class DYYGDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * @param inf_o
	 */
	public DYYGDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = true;
		if (!StringHelper.isEmpty(bdcdyid)) {
			String[] bdcdyids = bdcdyid.split(",");
			if (bdcdyids != null && bdcdyids.length > 0) {
				for (String id : bdcdyids) {
					if (bsuccess) {
						bsuccess = addbdcdy(id);
					}
				}
			}
		}
		return bsuccess;
	}

	private boolean addbdcdy(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		ResultMessage msg = new ResultMessage();
		RealUnit _srcUnit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
		if (_srcUnit == null) {
			msg.setSuccess("false");
			msg.setMsg("找不到单元");
			return false;
		}
		if (_srcUnit != null) {
			BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
			if (djdy != null) {
				// 抵押权
				BDCS_QL_GZ dyql = super.createQL(djdy, _srcUnit);
				
				dyql.setCZFS(CZFS.GTCZ.Value);
				// 抵押权附属权利
				BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());
                
				dyql.setFSQLID(dyfsql.getId());
				dyql.setCZFS(CZFS.GTCZ.Value);//设置持证方式为共同持证。
				dyfsql.setQLID(dyql.getId());
				// 设置抵押物类型
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				// 抵押不动产类型
				dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setYGDJZL(YGDJLX.ZQTBDCDYQYGDJ.Value.toString());
                // 建筑物坐落  --wlb
				dyfsql.setZJJZWZL(_srcUnit.getZL());
				int dysw=RightsTools.getMaxMortgageSWS(djdy.getDJDYID());
				dyfsql.setDYSW(dysw+1);
				// 设置附属权利里边的抵押人和义务人，同时拷贝主体权利中的权利人到申请人中
				String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
				String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray + ")";

				List<BDCS_QLR_XZ> list = dao.getDataList(BDCS_QLR_XZ.class, hql);
				if (list != null && list.size() > 0) {
					String qlrnames = "";
					for (int i = 0; i < list.size(); i++) {
						qlrnames += list.get(i).getQLRMC() + ",";
						//过滤重复，刘树峰2015年12月24日晚23点
						BDCS_QLR_XZ qlr = list.get(i);
						String zjhm = qlr.getZJH();
						boolean bexists = false;
						if (!StringHelper.isEmpty(qlr.getQLRMC())) {
							String Sql = "";
							if (!StringHelper.isEmpty(zjhm)) {
								Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(), qlr.getQLRMC(), zjhm);
							} else {
								Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(), qlr.getQLRMC());
							}
							List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, Sql);
							if (sqrlist != null && sqrlist.size() > 0) {
								bexists = true;
							}
						}
						
						// 判断申请人是否已经添加过，如果添加过，就不再添加
						if (!bexists) {
							String SQRID = getPrimaryKey();
							BDCS_SQR sqr = new BDCS_SQR();
							sqr.setGYFS(qlr.getGYFS());
							sqr.setFZJG(qlr.getFZJG());
							sqr.setGJDQ(qlr.getGJ());
							sqr.setGZDW(qlr.getGZDW());
							sqr.setXB(qlr.getXB());
							sqr.setHJSZSS(qlr.getHJSZSS());
							sqr.setSSHY(qlr.getSSHY());
							sqr.setYXBZ(qlr.getYXBZ());
							sqr.setQLBL(qlr.getQLBL());
							sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
							sqr.setSQRXM(qlr.getQLRMC());
							sqr.setSQRLB("2");
							sqr.setSQRLX(qlr.getQLRLX());
							sqr.setDZYJ(qlr.getDZYJ());
							sqr.setLXDH(qlr.getDH());
							sqr.setZJH(qlr.getZJH());
							sqr.setZJLX(qlr.getZJZL());
							sqr.setTXDZ(qlr.getDZ());
							sqr.setYZBM(qlr.getYB());
							sqr.setXMBH(getXMBH());
							sqr.setId(SQRID);
							sqr.setGLQLID(dyql.getId());
							dao.save(sqr);
						}
					}
					if (!StringUtils.isEmpty(qlrnames)) {
						qlrnames = qlrnames.substring(0, qlrnames.length() - 1);
						dyfsql.setDYR(qlrnames);
						dyfsql.setYWR(qlrnames);
					}
				}

				// 保存
				dao.save(djdy);
				dao.save(dyql);
				dao.save(dyfsql);
			}
			dao.flush();
			bsuccess = true;
		}
		return bsuccess;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		if (super.isCForCFING()) {
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
					// 更新单元抵押状态
					SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);// list.get(0);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			//删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
			// 删除权利关联申请人
			super.RemoveSQRByQLID(djdyid, getXMBH());
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
	 * 根据申请人ID数组添加申请人
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
	 * 获得抵押物类型
	 * @作者 diaoliwei
	 * @创建时间 2015年7月25日下午10:18:25
	 * @param bdcdylx
	 * @return
	 */
	private String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
		String dybdclx = "";
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.SYQZD)) {
			dybdclx = "1";
		} else if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "2";
		} else if (bdcdylx.equals(BDCDYLX.LD)) {
			dybdclx = "3";
		} else if (bdcdylx.equals(BDCDYLX.GZW) || bdcdylx.equals(BDCDYLX.YCH) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "4";
		} else if (bdcdylx.equals(BDCDYLX.HY)) {
			dybdclx = "5";
		} else {
			dybdclx = "7";
		}
		return dybdclx;
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

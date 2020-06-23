package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YSBZGG;
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
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
1、国有建设用地使用权换证登记
2、集体建设用地使用权换证登记（未配置）
3、宅基地使用权换证登记（未配置）
4、国有建设用地使用权/房屋所有权换证登记
5、集体建设用地使用权/房屋所有权换证登记（未配置）
6、宅基地使用权/房屋所有权换证登记（未配置）
*/
/**
 * 遗失补证公告
 * @author 海豹
 *
 */
public class YSBZGGDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public YSBZGGDJHandler(ProjectInfo info) {
		super(info);
	}
	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		
		if (super.isCForCFING()) {
			return false;
		}
		
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if(djdy==null)
		{
			djdy=new BDCS_DJDY_GZ();
			djdy.setDJDYID((String)SuperHelper.GeneratePrimaryKey());
			djdy.setBDCDYLX(getBdcdylx().Value);
			djdy.setBDCDYID(bdcdyid);
			djdy.setLY(DJDYLY.XZ.Value);
			djdy.setId((String)SuperHelper.GeneratePrimaryKey());
			djdy.setXMBH(getXMBH());
		}
		if (djdy != null) {
			RealUnit unit = null;
			try {
				unit=UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
				if(unit!=null)
				{
					djdy.setBDCDYH(unit.getBDCDYH());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			dao.save(djdy);
		}
		this.SetSFDB();
		this.alterCachedXMXX();
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		return true;
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
	 * 移除不动产单元(移除遗失补证公告表中相对应的信息)
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
			baseCommonDao.deleteEntitysByHql(BDCS_YSBZGG.class, "XMBH='"+getXMBH()+"' AND BDCDYID='"+bdcdyid+"'");//删除遗失补正
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
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
				String lyqlid=bdcql.getLYQLID();
				//bdcql.setId(lyqlid);
				SubRights bdcfsql=RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS,lyqlid);
				MessageExport msg= super.getShareMsgTools().GetMsg(bdcdy,bdcql,bdcfsql,bdcqlrs,bljc,xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy+1,djdy.getBDCDYLX(),xmxx);
			}
		}
	}
}

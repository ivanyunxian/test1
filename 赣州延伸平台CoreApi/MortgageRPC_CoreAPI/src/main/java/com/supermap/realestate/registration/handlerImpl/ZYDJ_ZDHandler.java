package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
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
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.Message;

public class ZYDJ_ZDHandler extends DJHandlerBase implements DJHandler {

	public ZYDJ_ZDHandler(ProjectInfo info) {
		super(info);
		// TODO Auto-generated constructor stub
	}

	/* 
	 * 依据权利信息添加单元
	 */
	@Override
	public boolean addBDCDY(String qlStrs) {
		/*
		 * 要做的事情：
		 * 1.添加单元的检查
		 * 2.生成单元
		 * 3.生成权利
		 * 4.拷贝权利人到申请人
		 */
		/*
		 * 1.添加单元的检查
		 */
		Message msg = IsCanAccept(qlStrs);
		if(!"true".equals(msg.getSuccess())){
			return false;
		}
		/*
		 * 2.生成单元
		 * 3.生成权利
		 * 4.拷贝权利人到申请人
		 */
		if(addBDCDYbyQlid(msg.getRows())){
			getCommonDao().flush();
			return true;
		}
		return false;
	}

	private boolean addBDCDYbyQlid(List<?> rows) {
		for (Object object : rows) {
			String lyqlid = object.toString();
			Rights R = RightsTools.loadRights(DJDYLY.XZ,lyqlid);
			List<BDCS_DJDY_XZ> dyxz = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+R.getDJDYID()+"'");
			if(dyxz!=null&&dyxz.size()>0){
				String bdcdyid = dyxz.get(0).getBDCDYID();
				String djdyid = dyxz.get(0).getDJDYID();
				RealUnit unit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
				if (unit!=null) {
					BDCS_DJDY_GZ dy = super.createDJDYfromXZ(bdcdyid);
					BDCS_QL_GZ ql = super.createQL(dy, unit);
					ql.setLYQLID(lyqlid);
					ql.setMAINQLID(lyqlid);
					ql.setQLQSSJ(R.getQLQSSJ());
					ql.setQLJSSJ(R.getQLJSSJ());
					BDCS_FSQL_GZ fsql = super.createFSQL(dy.getDJDYID());
					fsql.setQLID(ql.getId());
					ql.setFSQLID(fsql.getId());
					// 如果是使用权宗地，把使用权面积加上
					if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
						BDCS_SHYQZD_XZ xzshyqzd = getCommonDao().get(BDCS_SHYQZD_XZ.class, bdcdyid);
						if (xzshyqzd != null) {
							fsql.setSYQMJ(xzshyqzd.getZDMJ());
							ql.setQDJG(xzshyqzd.getJG());// 取得价格
						}
					}
					getCommonDao().save(dy);
					getCommonDao().save(ql);
					getCommonDao().save(fsql);
					super.CopySQRFromXZQLR(djdyid, R.getQLLX(), getXMBH(), lyqlid, SQRLB.YF.Value);
					return true;
				}
			}
		}
		return false;
	}

	private Message IsCanAccept(String qlStrs) {
		Message msg = new Message();
		String[] qlStr = qlStrs.split(",");
		HashSet<String> hs = new HashSet<String>();
		for (String qlid : qlStr) {
			if(!StringHelper.isEmpty(qlid))
				hs.add(qlid);
		}
		if(hs.size()>0){
			List<String> list = new ArrayList<String>(hs);
			msg.setRows(list);
			msg.setSuccess("true");
		}else{
			super.setErrMessage("选择权利为空");
		}
		boolean flg = false;
		boolean Notexistsdjdy = false;
		boolean Notexistsql = false;
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ, strSqlXMBH);
		for (String qlid : hs) {
			for (Rights Rights : qls) {
				if(qlid.equals(Rights.getLYQLID())){
					flg = true;
				}
			}
			Rights qlxz = RightsTools.loadRights(DJDYLY.XZ,qlid);
			if(qlxz==null){
				Notexistsql = true;
			}
			String className = getBdcdylx().getTableName(DJDYLY.XZ);
			String fromSql = "FROM BDCK.BDCS_QL_XZ QL INNER JOIN BDCK.BDCS_DJDY_XZ DY ON DY.DJDYID=QL.DJDYID "
					+ " INNER JOIN BDCK." + className +" ZD ON ZD.BDCDYID=DY.BDCDYID WHERE QL.QLID = '"+ qlid +"'";
			long count = getCommonDao().getCountByFullSql(fromSql);
			if(count<1){
				Notexistsdjdy = true;
			}
		}
		if(flg){
			msg.setSuccess("false");
			super.setErrMessage("不能重复选择权利");
		}
		if(Notexistsql){
			msg.setSuccess("false");
			super.setErrMessage("选择权利不存在");
		}
		if(Notexistsdjdy){
			msg.setSuccess("false");
			super.setErrMessage("单元不存在");
		}
		return msg;
	}

	
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
					super.removeQLFromXZByQLLX(djdyid);
					super.removeQLRFromXZByQLLX(djdyid);
					super.removeQDZRFromXZByQLLX(djdyid);
					super.removeZSFromXZByQLLX(djdyid);

					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		BDCS_DJDY_GZ djdy = null;
		String hql = MessageFormat.format("XMBH=''{0}'' AND BDCDYID=''{1}''", getXMBH(), bdcdyid);
		List<BDCS_DJDY_GZ> list = getCommonDao().getDataList(BDCS_DJDY_GZ.class, hql);
		if (list != null && list.size() > 0) {
			djdy = list.get(0);
			getCommonDao().deleteEntity(djdy);
		}
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除权利关联申请人
			super.RemoveSQRByQLID(djdyid, getXMBH());
			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
		}
		getCommonDao().flush();
		bsuccess = true;
		return bsuccess;
	}

	@Override
	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

	@Override
	public String getError() {
		return super.getErrMessage();
	}

	@Override
	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
	
}

package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * @author zhaomengfan
 * 组合登记_注销：转移预告+预抵押登记
 */
public class ZYYG_YDY_ZXDJHandler extends DJHandlerBase implements DJHandler {

	public ZYYG_YDY_ZXDJHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String qlid) {
		if(!StringHelper.isEmpty(qlid)){
			/**
			 * 检查单元是否办理过 转移预告+预抵押登记 的组合登记
			 */
			Rights ql_xz = RightsTools.loadRights(DJDYLY.XZ, qlid);
			if((!"99".equals(ql_xz.getQLLX()))||(!DJLX.YGDJ.Value.toString().equals(ql_xz.getDJLX()))){
				super.setErrMessage("未办理 转移预告+预抵押登记 的组合登记");
				return false;
			}
			StringBuilder dybuild= new StringBuilder();
			dybuild.append(" DJDYID='" + ql_xz.getDJDYID() + "'");
			List<BDCS_DJDY_XZ> dy_xz_list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, dybuild.toString());
			//dybuild.append(" AND YWH='" + ql_xz.getYWH() + "'");
			dybuild.append(" AND DJLX= '700' ");
			dybuild.append(" AND QLLX= '23' ");
			List<Rights> qls = null;
			if(dy_xz_list!=null&&dy_xz_list.size()>0){
				qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, dybuild.toString());
				//if(qls!=null&&qls.size()==2){
				if(qls.size()>0&&qls!=null){
					qls.remove(ql_xz);
					if((!"23".equals(qls.get(0).getQLLX()))||(!DJLX.YGDJ.Value.toString().equals(qls.get(0).getDJLX()))){
						super.setErrMessage("未办理 转移预告+预抵押登记 的组合登记");
						return false;
					}
				}else{
					super.setErrMessage("未办理 转移预告+预抵押登记 的组合登记");
					return false;
				}
			}else{
				super.setErrMessage("单元不存在");
				return false;
			}
			/**
			 * 拷贝权利、附属权利、权利人；
			 * 拷贝单元
			 */
			//转移预告
			BDCS_QL_GZ ql = ObjectHelper.copyQL_XZToGZ((BDCS_QL_XZ) ql_xz);
			ql.setId(getPrimaryKey());
			ql.setFSQLID(getPrimaryKey());
			ql.setXMBH(getXMBH());
			ql.setLYQLID(qlid);
			ql.setDJLX(DJLX.ZXDJ.Value);
			ql.setYWH(getProject_id());
			getCommonDao().save(ql);
			SubRights fsql_xz = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, qlid);
			BDCS_FSQL_GZ fsql = ObjectHelper.copyFSQL_XZToGZ((BDCS_FSQL_XZ) fsql_xz);
			fsql.setId(ql.getFSQLID());
			fsql.setQLID(ql.getId());
			fsql.setXMBH(getXMBH());
			getCommonDao().save(fsql);
			
			if(dy_xz_list!=null&&dy_xz_list.size()>0){
				BDCS_DJDY_GZ dy = ObjectHelper.copyDJDY_XZToGZ(dy_xz_list.get(0));
				dy.setId(getPrimaryKey());
				dy.setXMBH(getXMBH());
				dy.setLY(DJDYLY.XZ.Value);
				getCommonDao().save(dy);
			}
			
			CopySQRFromXZQLR(dy_xz_list.get(0).getDJDYID(), ql.getQLLX(), getXMBH(), qlid, SQRLB.JF.Value);
			List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, " QLID='"+qlid+"'");
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
					if (bdcs_qlr_xz != null) {
						// 拷贝权利人
						String gzqlrid = getPrimaryKey();
						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
						bdcs_qlr_gz.setId(gzqlrid);
						bdcs_qlr_gz.setQLID(ql.getId());
						bdcs_qlr_gz.setXMBH(getXMBH());
						getCommonDao().save(bdcs_qlr_gz);
					}
				}
			}

			//预抵押
			Rights dyq_xz = qls.get(0);
			BDCS_QL_GZ dyq = ObjectHelper.copyQL_XZToGZ((BDCS_QL_XZ) dyq_xz);
			dyq.setId(getPrimaryKey());
			dyq.setFSQLID(getPrimaryKey());
			dyq.setXMBH(getXMBH());
			dyq.setDJLX(DJLX.ZXDJ.Value);
			dyq.setLYQLID(dyq_xz.getId());
			dyq.setYWH(getProject_id());
			getCommonDao().save(dyq);
			
			SubRights dyfsql_xz = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, dyq_xz.getId());
			BDCS_FSQL_GZ dyfsql = ObjectHelper.copyFSQL_XZToGZ((BDCS_FSQL_XZ) dyfsql_xz);
			dyfsql.setId(dyq.getFSQLID());
			dyfsql.setQLID(dyq.getId());
			dyfsql.setXMBH(getXMBH());
			
			getCommonDao().save(dyfsql);
			CopySQRFromXZQLR(dy_xz_list.get(0).getDJDYID(), dyq_xz.getQLLX(), getXMBH(), dyq_xz.getId(), SQRLB.JF.Value);
			CopySQRFromXZQLR(dy_xz_list.get(0).getDJDYID(), ql.getQLLX(), getXMBH(), qlid, SQRLB.YF.Value);
			
			List<BDCS_QLR_XZ> qlrs2 = getCommonDao().getDataList(BDCS_QLR_XZ.class, " QLID='"+dyq_xz.getId()+"'");
			if (qlrs2 != null && qlrs2.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs2.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs2.get(iqlr);
					if (bdcs_qlr_xz != null) {
						// 拷贝权利人
						String gzqlrid = getPrimaryKey();
						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
						bdcs_qlr_gz.setId(gzqlrid);
						bdcs_qlr_gz.setQLID(dyq.getId());
						bdcs_qlr_gz.setXMBH(getXMBH());
						getCommonDao().save(bdcs_qlr_gz);
					}
				}
			}
			
			getCommonDao().flush();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean writeDJB() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, xmbhFilter);
		if(qls!=null&&qls.size()>0){
			for (BDCS_QL_GZ gzql : qls) {
				super.removeQLXXFromXZByQLID(gzql.getLYQLID());
				gzql.setDJSJ(new Date());
				gzql.setDBR(Global.getCurrentUserName());
				gzql.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
				getCommonDao().update(gzql);
				// 登记时间，登簿人，登记机构。
				BDCS_FSQL_GZ gzfsql = getCommonDao().get(BDCS_FSQL_GZ.class, gzql.getFSQLID());
				if (gzfsql != null) {
					gzfsql.setZXSJ(new Date());
					gzfsql.setZXDBR(Global.getCurrentUserName());
					getCommonDao().update(gzfsql);
				}
				SubRights lsfsql = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, gzql.getLYQLID());
				if(lsfsql!=null){
					BDCS_FSQL_LS ls = (BDCS_FSQL_LS) lsfsql;
					ls.setZXDBR(gzfsql.getZXDBR());
					ls.setZXSJ(gzfsql.getZXSJ());
					ls.setZXDYYWH(getProject_id());
					ls.setZXFJ(gzfsql.getZXFJ());
					ls.setZXDYYY(gzfsql.getZXDYYY());
					getCommonDao().update(gzfsql);
				}
			}
			super.SetSFDB();
			super.alterCachedXMXX();
			getCommonDao().flush();
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean removeBDCDY(String qlid) {

		Rights _rights = RightsTools.deleteRightsAndSubRights(DJDYLY.GZ, qlid);
		if(_rights!=null){
			String djdyid = _rights.getDJDYID();
			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(djdyid);
			builderDJDY.append("' AND XMBH='");
			builderDJDY.append(getXMBH());
			builderDJDY.append("'");
			// 获取登记单元集合
			List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, builderDJDY.toString());
			if (djdys != null && djdys.size() > 0) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(0);
				getCommonDao().deleteEntity(bdcs_djdy_gz);
			}
			List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, builderDJDY.toString());
			//权利不止一条
			if (qls != null && qls.size() > 0) {
				for (Iterator<BDCS_QL_GZ> iterator = qls.iterator(); iterator.hasNext();) {
					BDCS_QL_GZ bdcs_QL_GZ = (BDCS_QL_GZ) iterator.next();
					RightsTools.deleteRightsAndSubRights(DJDYLY.GZ, bdcs_QL_GZ.getId());
				}
			}
			String sql=" XMBH='" + getXMBH() + "'";
			getCommonDao().deleteEntitysByHql(BDCS_SQR.class, sql);
			super.RemoveSQRByQLID(djdyid, getXMBH());
			getCommonDao().flush();
			return true;
		}
		return false;
	}

	@Override
	public List<UnitTree> getUnitTree() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = this.getCommonDao();
		List<UnitTree> list = new ArrayList<UnitTree>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				UnitTree tree = new UnitTree();
				StringBuilder BuilderQL = new StringBuilder();
				BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
				List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
				if (qls != null) {
					for (int iql = 0; iql < qls.size(); iql++) {
						BDCS_QL_GZ ql = qls.get(iql);
						if (ql.getQLLX().equals(QLLX.DIYQ.Value)) {
							tree.setDIYQQlid(ql.getId());
							tree.setOlddiyqqlid(ql.getLYQLID());
						} else {
							tree.setQlid(ql.getId());
							tree.setOldqlid(ql.getLYQLID());
							tree.setlyqlid(ql.getLYQLID());
						}
					}
				}
				tree.setId(djdy.getBDCDYID());
				tree.setBdcdyid(djdy.getBDCDYID());
				tree.setBdcdylx(djdy.getBDCDYLX());
				tree.setDjdyid(djdy.getDJDYID());
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);
				String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
				tree.setText(zl);
				// 如果是户的话，把房号也加上
				if (djdy.getBDCDYLX().equals(BDCDYLX.H.Value)) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.H, ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							tree.setMj(house.getMJ());
							tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
						}
					}
				}
				tree.setZl(zl);
				list.add(tree);
			}
		}
		return list;
	}

	private String getZL(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly) {
		String zl = "";
		CommonDao dao = getCommonDao();
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		if (djdyly.equals(DJDYLY.GZ.Name)) {
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_GZ shyqzd = dao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, bdcdyid);
				tree.setCid(h.getCID());
				tree.setZdbdcdyid(h.getZDBDCDYID());
				tree.setZrzbdcdyid(h.getZRZBDCDYID());
				tree.setLjzbdcdyid(h.getLJZID());
				zl = h == null ? "" : h.getZL();
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_GZ zrz = dao.get(BDCS_ZRZ_GZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else if (dylx.equals(BDCDYLX.SYQZD))// sunhb-2015-06-23添加所有权宗地，获取坐落
			{
				BDCS_SYQZD_GZ syqzd = dao.get(BDCS_SYQZD_GZ.class, bdcdyid);
				zl = syqzd == null ? "" : syqzd.getZL();
			} else if (dylx.equals(BDCDYLX.HY))// sunhb-2015-06-23添加宗海，获取坐落
			{
				BDCS_ZH_GZ zh = dao.get(BDCS_ZH_GZ.class, bdcdyid);
				zl = zh == null ? "" : zh.getZL();
			} else if (dylx.equals(BDCDYLX.LD))// sunhb-2015-06-23添加林地，获取坐落
			{
				BDCS_SLLM_GZ ld = dao.get(BDCS_SLLM_GZ.class, bdcdyid);
				tree.setZdbdcdyid(ld.getZDBDCDYID());
				zl = ld == null ? "" : ld.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
		} else {// 来源于现状，把原来的所有权/使用权的权利ID也加上
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ shyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_XZ shyqzd = dao.get(BDCS_H_XZ.class, bdcdyid);
				if (shyqzd != null) {
					zl = shyqzd == null ? "" : shyqzd.getZL();

					tree.setCid(shyqzd.getCID());
					tree.setZdbdcdyid(shyqzd.getZDBDCDYID());
					tree.setZrzbdcdyid(shyqzd.getZRZBDCDYID());
					tree.setLjzbdcdyid(shyqzd.getLJZID());
				}

			} else if (dylx.equals(BDCDYLX.YCH)) {
				BDCS_H_XZY bdcs_h_xzy = dao.get(BDCS_H_XZY.class, bdcdyid);
				if (bdcs_h_xzy != null) {
					zl = bdcs_h_xzy.getZL();
					tree.setId(bdcs_h_xzy.getCID());
					tree.setZdbdcdyid(bdcs_h_xzy.getZDBDCDYID());
					tree.setZrzbdcdyid(bdcs_h_xzy.getZRZBDCDYID());
					tree.setLjzbdcdyid(bdcs_h_xzy.getLJZID());
				}
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_XZ zrz = dao.get(BDCS_ZRZ_XZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
			// 这块的逻辑有点问题，原来的权利ID应该包含两种，一种是所有权/使用权ID，一种是他项权利ID，例如
			// 抵押权的转移，就包含了被抵押单元的所有权权利和转移前的抵押权
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ", bdcdyid, qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setOldqlid(ql.getId());
			}
		}
		return zl;
	}
	
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
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
	}
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return exportXmlUtil.createXMLAndUp(path, actinstID);
	}
}

package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.dataExchange.DJFDJFZ;
import com.supermap.realestate.registration.dataExchange.DJFDJGD;
import com.supermap.realestate.registration.dataExchange.DJFDJSF;
import com.supermap.realestate.registration.dataExchange.DJFDJSH;
import com.supermap.realestate.registration.dataExchange.DJFDJSJ;
import com.supermap.realestate.registration.dataExchange.DJFDJSQR;
import com.supermap.realestate.registration.dataExchange.DJFDJSZ;
import com.supermap.realestate.registration.dataExchange.DJTDJSLSQ;
import com.supermap.realestate.registration.dataExchange.FJF100;
import com.supermap.realestate.registration.dataExchange.Message;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
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
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * @author zhaomengfan
 * 组合登记_注销：预告登记+预抵押登记
 */
public class YGYDYZXDJHandler extends DJHandlerBase implements DJHandler {

	public YGYDYZXDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * @Description: 添加单元
	 * @param qlid
	 */
	@Override
	public boolean addBDCDY(String qlid) {
		if(!StringHelper.isEmpty(qlid)){
			/**
			 * 检查单元是否办理过 预告登记+预抵押登记 的组合登记
			 */
			Rights ql_xz = RightsTools.loadRights(DJDYLY.XZ, qlid);
			if((!"4".equals(ql_xz.getQLLX()))||(!DJLX.YGDJ.Value.equals(ql_xz.getDJLX()))){
				super.setErrMessage("未办理 预告登记+预抵押登记 的组合登记");
				return false;
			}
			StringBuilder dybuild= new StringBuilder();
			dybuild.append(" DJDYID='" + ql_xz.getDJDYID() + "'");
			List<BDCS_DJDY_XZ> dy_xz_list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, dybuild.toString());
//			dybuild.append(" AND YWH='" + ql_xz.getYWH() + "'");
			dybuild.append(" AND QLLX = '23' ");
			dybuild.append(" AND DJLX = '700' ");
			List<Rights> qls = null;
			if(dy_xz_list!=null&&dy_xz_list.size()>0){
				qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, dybuild.toString());
//				if(qls!=null&&qls.size()==2){
				if(qls.size() > 0 && qls != null){
					qls.remove(ql_xz);
					if((!"23".equals(qls.get(0).getQLLX()))||(!DJLX.YGDJ.Value.toString().equals(qls.get(0).getDJLX()))){
						super.setErrMessage("未办理 预告登记+预抵押登记 的组合登记");
						return false;
					}
				}else{
					super.setErrMessage("未办理 预告登记+预抵押登记 的组合登记");
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
			dyq.setLYQLID(dyq_xz.getId());
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
				// 登记时间，登簿人，登记机构。
				BDCS_FSQL_GZ gzfsql = getCommonDao().get(BDCS_FSQL_GZ.class, gzql.getFSQLID());
				if (gzfsql != null) {
					gzfsql.setZXSJ(new Date());
					gzfsql.setZXDBR(Global.getCurrentUserName());
					getCommonDao().update(gzfsql);
				}
				Rights xzql = RightsTools.loadRights(DJDYLY.LS, gzql.getLYQLID());
				
				if(xzql!=null){
					SubRights lsfsql = RightsTools.loadSubRights(DJDYLY.LS, xzql.getFSQLID());
					if(lsfsql!=null){
						BDCS_FSQL_LS ls = (BDCS_FSQL_LS) lsfsql;
						ls.setZXDBR(gzfsql.getZXDBR());
						ls.setZXSJ(gzfsql.getZXSJ());
						ls.setZXFJ(gzfsql.getZXFJ());
						ls.setZXDYYWH(getProject_id());
						ls.setZXDYYY(gzfsql.getZXDYYY());
						getCommonDao().update(ls);
					}
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
				// 预测户也要算上  heks 2017/3/20  13:12:00
				else if (djdy.getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.YCH, ely, djdy.getBDCDYID()));
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
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {}

	@Override
	public void removeQLR(String qlid, String qlrid) {}

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
	
	
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {



		Message msg=null;
		String result = "";
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		CommonDao dao = super.getCommonDao();
		String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
		try {
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				//String cyear = calendar.get(Calendar.YEAR) + "";
				List<BDCS_QL_GZ> qlList= dao.getDataList(BDCS_QL_GZ.class,xmbhHql);
				for (int i = 0; i < qlList.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(0);
					String ywh = packageXml.GetYWLSHByYWH(super.getProject_id());
					BDCS_QL_GZ ql = qlList.get(i);
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					 msg = exchangeFactory.createMessageByZXDJ();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
					House h = null;
//					System.out.println(super.getQllx().Value);
//					System.out.println(super.getBdcdylx().toString());
					if (djdy != null) {
						if (QLLX.GYJSYDSHYQ.Value.equals(super.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(super.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(super.getQllx().Value)
								||"SHYQZD".equals(super.getBdcdylx().toString())||"02".equals(djdy.getBDCDYLX())) { // 国有建设使用权、宅基地、集体建设用地使用权
							try {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
								
								if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
									msg.getHead().setAreaCode(zd.getQXDM());
								}
								super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zd.getQXDM(),subrights);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);
								msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
								msg.getHead().setRecType("4000101");
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if ((QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(super.getQllx().Value)||"H".equals(super.getBdcdylx().toString()))||"YCH".equals(super.getBdcdylx().toString())) { 
							// 房屋所有权
							try {
								
								if ("YCH".equals(super.getBdcdylx().toString())) {
									h=dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
								}else {
									h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
								}
								
								if(h != null && h.getZDBDCDYID() != null){
									BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
									h.setZDDM(zd.getZDDM());
								}
								if(h != null && !StringUtils.isEmpty(h.getQXDM())){
									msg.getHead().setAreaCode(h.getQXDM());
								}
								super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, h.getQXDM(),subrights);
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJGD(gd);

								msg.getData().setDJSQR(djsqrs);
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								
								
								msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
								msg.getHead().setRecType("4000101");
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (QLLX.HYSYQ.Value.equals(super.getQllx().Value) || QLLX.WJMHDSYQ.Value.equals(super.getQllx().Value)) { 
							// 海域(含无居民海岛)使用权注销
							 // 海域(含无居民海岛)使用权
							String zhdm=null;
							String hql=null;
							YHZK yhzk_gz = null;
							Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
							if (zh==null) {
								zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
							}
							if (null != zh) {
							 zhdm=zh.getZHDM();
							 hql = "BDCDYID = '" + zh.getId() + "' ";
								List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
								if (yhzks != null && yhzks.size() > 0) {
									yhzk_gz = yhzks.get(0);
								}else {
									List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
									if (yhzksxz != null && yhzksxz.size() > 0) {
										yhzk_gz = yhzksxz.get(0);
									}
								}
							}
							if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
								//维护数据
								yhzk_gz.setZHDM(zhdm);
							}
							// 这些字段先手动赋值 diaoliwei
							if (zh != null) {
								if (StringUtils.isEmpty(zh.getZHT())) {
									zh.setZHT("无");
								}
							}
								//设置报文头
								super.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
								msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
								//2.非结构化文档
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								msg.getData().setFJF100(fj);

								//7.登记受理信息表
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
								msg.getData().setDJSLSQ(sq);

								//8.登记收件(可选)
								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);
								
								//13.申请人属性
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
						        //注销登记
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zh.getQXDM(),subrights);
								msg.getData().setZXDJ(zxdj);
								msg.getHead().setRecType("4000101");
						}
					}
					//组合流程业务流水号特殊处理
					msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+i);
					String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
					mashaller.marshal(msg, new File(path +msgName));
					result = super.uploadFile(path +msgName, ConstValue.RECCODE.YY_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
					names.put(djdy.getDJDYID(), path +msgName);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null == result){
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
			YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
			//return xmlError;
		}
		if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", result);
			//return xmlError;
		}
		if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
			names.put("reccode", result);
		}
		return names;
	
	
	}


}

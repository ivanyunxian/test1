package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.DCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/** 
 * 数据来源：从权籍系统中权籍调查中来，房屋
 * 变更前，如果房地都是同一个权利人所有
 * 则 办理房+地的转移（单元变更、权利人变更），
 * 变更后的房屋需要满足任意一下情形之一（满足房地一体）：
 * 1.房屋土地都没有登记过；
 * 2.房屋没有登记过，但土地产权归变更后的权利人所有
 *
 * 变更前，如果房地不是同一个权利人所有
 * 则 办理房的转移（单元变更、权利人变更）
 */

/**
 * @author zhaomengfan
 * @date 2017-03-31 09:15:10
 */
public class ZYDJANDBGDJHandler extends DJHandlerBase implements DJHandler {

	public ZYDJANDBGDJHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		String[] ids = bdcdyid.split(",");
		if (ids == null || ids.length <= 0) {
			return false;
		}
		boolean zdflag=false;
		boolean zrzflag=false;
		for (String id : ids) {
			if (StringUtils.isEmpty(id)) {
				continue;
			}
			RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, id);
			if (dy != null) {
				// 变更前
				BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(id);
				if (djdy != null) {
					String sql = "DJDYID=''{0}'' AND QLLX=''{1}''";
					sql = MessageFormat.format(sql, djdy.getDJDYID(), getQllx().Value);
					List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, sql);
					// 拷贝权利人
					if (qls != null && qls.size() > 0) {
						Rights ql = qls.get(0);
						List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql.getId());
						if (qlrs != null && qlrs.size() > 0) {
							for (RightsHolder rightsHolder : qlrs) {
								BDCS_QLR_XZ qlr = (BDCS_QLR_XZ) rightsHolder;
								BDCS_SQR sqr = super.copyXZQLRtoSQR(qlr, SQRLB.YF);
								dao.save(sqr);
							}
						}
					}
					dao.save(djdy);
				}
			} else {
				// 变更后
				RealUnit dy_dc = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, bdcdyid);
				if (!StringHelper.isEmpty(dy_dc)) {
					dy_dc.setDJZT(DJZT.DJZ.Value);
					dao.save(dy_dc);
					dy = UnitTools.copyUnit(dy_dc, this.getBdcdylx(), DJDYLY.GZ);
					if (!StringHelper.isEmpty(dy)) {
						dy.setXMBH(this.getXMBH());
					}
					// 先判断h对应的宗地跟自然幢在XZ层有没有，没有从BDCDCK拷贝，ZRZ跟ZD
					if (dy instanceof House && BDCDYLX.H.equals(this.getBdcdylx())) {
						House h = (House) dy;
						String zrzbdcdyid = h.getZRZBDCDYID();
						RealUnit zrz_xz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
						// 考虑到分割时，可能添加第一个单元时，没有自然幢信息跟宗地信息，再添加单元时，有自然幢信息跟宗地信息，就不添加了
						if (zrz_xz == null) {
							if(!zrzflag){
								RealUnit zrz_dc = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrzbdcdyid);
								if (!StringHelper.isEmpty(zrz_dc)) {
									RealUnit zrz_gz = UnitTools.copyUnit(zrz_dc, BDCDYLX.ZRZ, DJDYLY.GZ);
									super.CopyGeo(h.getId(), this.getBdcdylx(), DJDYLY.DC);
									zrz_dc.setDJZT(DJZT.DJZ.Value);
									zrz_gz.setXMBH(this.getXMBH());
									dao.update(zrz_gz);
									dao.update(zrz_dc);
								}
								zrzflag=true;
							}
						}
						String zdbdcdyid = h.getZDBDCDYID();
						RealUnit zd_xz = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, zdbdcdyid);
						if (zd_xz == null) {
							if(!zdflag){
								RealUnit zd_dc = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, zdbdcdyid);
								if (!StringHelper.isEmpty(zd_dc)) {
									zd_dc.setDJZT(DJZT.DJZ.Value);
									RealUnit zd_gz = UnitTools.copyUnit(zd_dc, BDCDYLX.SHYQZD, DJDYLY.GZ);
									super.CopyGeo(zdbdcdyid, BDCDYLX.SHYQZD, DJDYLY.DC);// 拷贝图形信息
									zd_gz.setXMBH(this.getXMBH());
									dao.save(zd_gz);
									dao.save(zd_dc);
								}
								zdflag=true;
							}
						}
					}
					// 登记单元索引表
					BDCS_DJDY_GZ djdy = super.createDJDYfromGZ(dy);
					if (djdy != null) {
						BDCS_QL_GZ ql = super.createQL(djdy, dy);
						BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
						ql.setFSQLID(fsql.getId());
						fsql.setQLID(ql.getId());
						// 如果是使用权宗地，要填写使用权面积,或者其他的
						if (djdy.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
							BDCS_SHYQZD_GZ shyqzd = (BDCS_SHYQZD_GZ) dy;
							if (shyqzd != null) {
								fsql.setSYQMJ(shyqzd.getZDMJ());
							}
						}
						dao.save(djdy);
						dao.save(ql);
						dao.save(fsql);
					}
				}
			}
			dao.save(dy);
		}
		dao.flush();
		return true;
	}

	@Override
	public boolean writeDJB() {

		if (super.isCForCFING()) {
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH + " ORDER BY LY");
		List<String> listBGQ = new ArrayList<String>();
		List<String> listBGH = new ArrayList<String>();
		boolean flg = false;//标记zrz、zd是否已更新
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					StringBuilder builder = new StringBuilder();
					builder.append(djdyid).append(";").append(bdcdyid);
					String strDYID = builder.toString();
					String ly = bdcs_djdy_gz.getLY();
					BDCDYLX lx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());

					if (DJDYLY.GZ.Value.equals(ly)) {
						if (!listBGH.contains(strDYID)) {
							listBGH.add(strDYID);
						}
						super.CopyGZQLToXZAndLS(djdyid);
						super.CopyGZQLRToXZAndLS(djdyid);
						super.CopyGZQDZRToXZAndLS(djdyid);
						super.CopyGZZSToXZAndLS(djdyid);
						super.CopyGZDYToXZAndLS(bdcs_djdy_gz.getBDCDYID());
						RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, bdcs_djdy_gz.getBDCDYID());
						if (!StringHelper.isEmpty(dy)) {
							dy.setDJZT(DJZT.YDJ.Value);
							getCommonDao().save(dy);
						}
						super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
						super.CopyGeo(bdcdyid, lx, DJDYLY.initFrom(ly));
						// 获取变更后户信息
						RealUnit unit = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcs_djdy_gz.getBDCDYID());
						if (!StringHelper.isEmpty(unit)) {
							House h = (House) unit;
							// 获取变更后户对应的宗地信息
							List<RealUnit> zd = UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ,
									strSqlXMBH + " and bdcdyid ='" + h.getZDBDCDYID() + "'");
							// 若存在，则宗地信息修改，则需要修改宗地信息及对应的权利信息
							if (!StringHelper.isEmpty(zd) && zd.size() > 0) {
								if (!flg&&BDCDYLX.H.equals(lx)) {
									updateZRZ();
									updateZD();
									flg = true;
								}
							}
						}
					} else if (DJDYLY.XZ.Value.equals(ly)) {
						if (!listBGQ.contains(strDYID)) {
							listBGQ.add(strDYID);
						}
						super.removeZSFromXZByALL(djdyid);
						super.removeQLRFromXZByALL(djdyid);
						super.removeQDZRFromXZByALL(djdyid);
						super.removeQLFromXZByALL(djdyid);
						UnitTools.deleteUnit(lx, DJDYLY.XZ, bdcdyid);
						super.removeDJDYFromXZ(djdyid);
						super.DeleteGeo(bdcdyid, lx, DJDYLY.initFrom(ly));
					}
				}
			}
		}
		if (listBGQ != null && listBGQ.size() > 0 && listBGH != null && listBGH.size() > 0) {
			for (int ibgq = 0; ibgq < listBGQ.size(); ibgq++) {
				for (int ibgh = 0; ibgh < listBGH.size(); ibgh++) {
					String BGQDYID = listBGQ.get(ibgq);// 老单元的ID
					String BGHDYID = listBGH.get(ibgh);// 新增单元的ID-在单元变更中放到lbdcdyid中
					Date time = new Date();
					RebuildDYBG(BGQDYID.split(";")[1], BGQDYID.split(";")[0], BGHDYID.split(";")[1],
							BGHDYID.split(";")[0], time, null);
				}
			}
		}

		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}

	public void updateZD() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH + " ORDER BY LY");
		if (djdys != null && djdys.size() > 0) {
			List<RealUnit> zdlist = new ArrayList<RealUnit>();
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					String ly = bdcs_djdy_gz.getLY();
					if (DJDYLY.GZ.Value.equals(ly)) {
						RealUnit h = UnitTools.loadUnit(getBdcdylx(), DJDYLY.GZ, bdcdyid);
						if(h!=null){
							House house = (House)h;
							String zdbdcdyid=house.getZDBDCDYID();
							RealUnit zd_gz=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
							RealUnit lszd=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, zdbdcdyid);
							if(zd_gz!=null&&lszd==null){
								if(zdlist.contains(zd_gz)){
									continue;
								}
								zdlist.add(zd_gz);
								// 拷贝单元信息到现状跟历史
								RealUnit zd_xz = UnitTools.copyUnit(zd_gz, BDCDYLX.SHYQZD, DJDYLY.XZ);
								RealUnit zd_ls = UnitTools.copyUnit(zd_gz, BDCDYLX.SHYQZD, DJDYLY.LS);
								RealUnit zd_dc = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, zd_gz.getId());
								zd_dc.setDJZT(DJZT.YDJ.Value);
								getCommonDao().update(zd_dc);
								getCommonDao().save(zd_xz);
								getCommonDao().save(zd_ls);
								super.CopyGeo(zdbdcdyid, BDCDYLX.SHYQZD,DJDYLY.GZ);
								//宗地权利 --- 拷贝房屋权利
								String djdyid=bdcs_djdy_gz.getDJDYID();
								Rights gzql = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, getXMBH(), djdyid);
								BDCS_DJDY_XZ xzdy = new BDCS_DJDY_XZ();
								xzdy.setXMBH(this.getXMBH());
								xzdy.setDJDYID(getPrimaryKey());
								xzdy.setBDCDYH(zd_gz.getBDCDYH());
								xzdy.setBDCDYID(zdbdcdyid);
								xzdy.setLY(DJDYLY.GZ.Value);
								xzdy.setBDCDYLX(BDCDYLX.SHYQZD.Value);
								getCommonDao().save(xzdy);
								BDCS_DJDY_LS lsdy = ObjectHelper.copyDJDY_XZToLS(xzdy);
								getCommonDao().save(lsdy);
								if(gzql!=null){
									List<BDCS_QDZR_GZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_GZ.class, 
											"XMBH='"+super.getXMBH() + "' and qlid='"+gzql.getId()+"'");
									if (qdzrs != null && qdzrs.size() > 0) {
										List<String> hqlid = new ArrayList<String>();
										List<String> hfsqlid = new ArrayList<String>();
										List<String> hqlrid = new ArrayList<String>();
										List<String> hzsid = new ArrayList<String>();
										String zdqlid = getPrimaryKey();
										String zdfsqlid = getPrimaryKey();
										String zdqlrid = getPrimaryKey();
										String zdzsid = getPrimaryKey();
										for (BDCS_QDZR_GZ bdcs_QDZR_GZ : qdzrs) {
											String qlid = bdcs_QDZR_GZ.getQLID();
											BDCS_QDZR_XZ zd_qdzr_xz = ObjectHelper.copyQDZR_GZToXZ(bdcs_QDZR_GZ);
											zd_qdzr_xz.setId(getPrimaryKey());
											zd_qdzr_xz.setDJDYID(xzdy.getDJDYID());
											zd_qdzr_xz.setBDCDYH(xzdy.getBDCDYH());
											zd_qdzr_xz.setQLID(zdqlid);
											zd_qdzr_xz.setFSQLID(zdfsqlid);
											zd_qdzr_xz.setQLRID(zdqlrid);
											zd_qdzr_xz.setZSID(zdzsid);
											BDCS_QDZR_LS zd_qdzr_ls = ObjectHelper.copyQDZR_XZToLS(zd_qdzr_xz);
											getCommonDao().save(zd_qdzr_xz);
											getCommonDao().save(zd_qdzr_ls);
											if (!hqlid.contains(qlid)) {
												hqlid.add(qlid);
												Rights bdcs_ql_gz = RightsTools.loadRights(DJDYLY.GZ, qlid);
												BDCS_QL_XZ zd_ql_xz = ObjectHelper.copyQL_GZToXZ((BDCS_QL_GZ) bdcs_ql_gz);
												zd_ql_xz.setId(zdqlid);
												zd_ql_xz.setBDCQZH("");
												zd_ql_xz.setBDCQZHXH("");
												zd_ql_xz.setQLLX(((DCS_SHYQZD_GZ)zd_dc).getQLLX());
												zd_ql_xz.setFSQLID(zdfsqlid);
												zd_ql_xz.setBDCDYH(xzdy.getBDCDYH());
												zd_ql_xz.setDJDYID(xzdy.getDJDYID());
												BDCS_QL_LS zd_ql_ls = ObjectHelper.copyQL_XZToLS(zd_ql_xz);
												getCommonDao().save(zd_ql_xz);
												getCommonDao().save(zd_ql_ls);
											}
											String fsqlid = bdcs_QDZR_GZ.getFSQLID();
											if (!hfsqlid.contains(fsqlid)) {
												hfsqlid.add(fsqlid);
												SubRights bdcs_fsql_gz = RightsTools.loadSubRights(DJDYLY.GZ, fsqlid);
												BDCS_FSQL_XZ zd_fsql_xz = ObjectHelper.copyFSQL_GZToXZ((BDCS_FSQL_GZ) bdcs_fsql_gz);
												zd_fsql_xz.setId(zdfsqlid);
												zd_fsql_xz.setQLID(zdqlid);
												zd_fsql_xz.setBDCDYH(xzdy.getBDCDYH());
												zd_fsql_xz.setDJDYID(xzdy.getDJDYID());
												BDCS_FSQL_LS zd_fsql_ls = ObjectHelper.copyFSQL_XZToLS(zd_fsql_xz);
												getCommonDao().save(zd_fsql_xz);
												getCommonDao().save(zd_fsql_ls);
											}
											String qlrid = bdcs_QDZR_GZ.getQLRID();
											if (!hqlrid.contains(qlrid)) {
												hqlrid.add(qlrid);
												RightsHolder bdcs_qlr_gz = RightsHolderTools.loadRightsHolder(DJDYLY.GZ, qlrid);
												BDCS_QLR_XZ zd_qlr_xz = ObjectHelper.copyQLR_GZToXZ((BDCS_QLR_GZ) bdcs_qlr_gz);
												zd_qlr_xz.setId(zdqlrid);
												zd_qlr_xz.setQLID(zdqlid);
												zd_qlr_xz.setBDCQZH("");
												zd_qlr_xz.setBDCQZHXH("");
												BDCS_QLR_LS zd_qlr_ls = ObjectHelper.copyQLR_XZToLS(zd_qlr_xz);
												getCommonDao().save(zd_qlr_xz);
												getCommonDao().save(zd_qlr_ls);
											}
											String zsid = bdcs_QDZR_GZ.getZSID();
											if (!hzsid.contains(zsid)) {
												hzsid.add(zsid);
												BDCS_ZS_GZ bdcs_zs_gz = getCommonDao().get(BDCS_ZS_GZ.class, zsid);
												BDCS_ZS_XZ zd_zs_xz = ObjectHelper.copyZS_GZToXZ(bdcs_zs_gz);
												zd_zs_xz.setId(zdzsid);
												zd_zs_xz.setBDCQZH("");
												zd_zs_xz.setZSBH("");
												BDCS_ZS_LS zd_zs_ls = ObjectHelper.copyZS_XZToLS(zd_zs_xz);
												getCommonDao().save(zd_zs_xz);
												getCommonDao().save(zd_zs_ls);
											}
										}
									}
								}
							}
						}
					} else if (DJDYLY.XZ.Value.equals(ly)) {
						RealUnit h = UnitTools.loadUnit(getBdcdylx(), DJDYLY.XZ, bdcdyid);
						if(h!=null){
							House house = (House)h;
							String zdbdcdyid=house.getZDBDCDYID();
							// 删除变更前单元信息
							String hql = MessageFormat.format("BDCDYID=''{0}''", zdbdcdyid);
							List<BDCS_DJDY_XZ> list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, hql);
							if(list!=null&&list.size()>0){
								String djdyid = list.get(0).getDJDYID();
								super.removeZSFromXZByALL(djdyid);
								super.removeQLRFromXZByALL(djdyid);
								super.removeQDZRFromXZByALL(djdyid);
								super.removeQLFromXZByALL(djdyid);
								UnitTools.deleteUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, zdbdcdyid);
								super.removeDJDYFromXZ(djdyid);
								super.DeleteGeo(zdbdcdyid, BDCDYLX.SHYQZD, DJDYLY.XZ);
							}
						}
					}
				}
			}
		}
	}

	public void updateZRZ(){
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH + " ORDER BY LY");
		if (djdys != null && djdys.size() > 0) {
			List<RealUnit> zrzlist = new ArrayList<RealUnit>();
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					String ly = bdcs_djdy_gz.getLY();
					if (DJDYLY.GZ.Value.equals(ly)) {
						RealUnit h = UnitTools.loadUnit(getBdcdylx(), DJDYLY.GZ, bdcdyid);
						if(h!=null){
							House house = (House)h;
							String zrzbdcdyid=house.getZRZBDCDYID();
							RealUnit zrz_gz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, zrzbdcdyid);
							RealUnit lszrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, zrzbdcdyid);
							if(zrz_gz!=null&&lszrz==null){
								if(zrzlist.contains(zrz_gz)){
									continue;
								}
								zrzlist.add(zrz_gz);
								// 拷贝单元信息到现状跟历史
								RealUnit zrz_xz = UnitTools.copyUnit(zrz_gz, BDCDYLX.ZRZ, DJDYLY.XZ);
								RealUnit zrz_ls = UnitTools.copyUnit(zrz_gz, BDCDYLX.ZRZ, DJDYLY.LS);
								RealUnit zrz_dc = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrz_gz.getId());
								zrz_dc.setDJZT(DJZT.YDJ.Value);
								getCommonDao().update(zrz_dc);
								getCommonDao().save(zrz_xz);
								getCommonDao().save(zrz_ls);
								super.CopyGeo(zrz_gz.getId(), BDCDYLX.ZRZ, DJDYLY.DC);
							}
						}
					} else if (DJDYLY.XZ.Value.equals(ly)) {
						RealUnit h = UnitTools.loadUnit(getBdcdylx(), DJDYLY.XZ, bdcdyid);
						if(h!=null){
							House house = (House)h;
							String zrzbdcdyid=house.getZRZBDCDYID();
							RealUnit zrz_xz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
							if(zrz_xz!=null){
								// 删除变更前自然幢单元信息
								getCommonDao().delete(BDCS_ZRZ_XZ.class, zrzbdcdyid);
								super.CopyGeo(zrz_xz.getId(), BDCDYLX.ZRZ, DJDYLY.XZ);
							}
						}
					}
				}
			}
		}
	}
	
	public boolean removeBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String bdcdylx = djdy.getBDCDYLX();
			String ly = djdy.getLY();
			String djdyid = djdy.getDJDYID();
			if (ly.equals(DJDYLY.GZ.Value)) {
				// 移除具体单元
				RealUnit unit = UnitTools.deleteUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.GZ, bdcdyid);
				if (!StringHelper.isEmpty(unit)) {
					if (unit instanceof House) {
						// 判断是否移除自然幢跟地的信息,先要判断调查户的信息全部移除掉
						String zrzbdcdyid = ((House) unit).getZRZBDCDYID();
						List<RealUnit> zrzs = UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ,
								ProjectHelper.GetXMBHCondition(this.getXMBH()) + " AND BDCDYID ='" + zrzbdcdyid + "'");
						if (!StringHelper.isEmpty(zrzs) && zrzs.size() > 0) {
							UnitTools.deleteUnit(BDCDYLX.ZRZ, DJDYLY.GZ, zrzbdcdyid);
							updateDCDYStatus(BDCDYLX.ZRZ.Value, zrzbdcdyid);
							super.DeleteGeo(zrzbdcdyid, BDCDYLX.ZRZ, DJDYLY.GZ);
						}
						String zdbdcdyid = ((House) unit).getZDBDCDYID();
						List<RealUnit> zdzs = UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ,
								ProjectHelper.GetXMBHCondition(this.getXMBH()) + " AND BDCDYID ='" + zdbdcdyid + "'");
						if (!StringHelper.isEmpty(zdzs) && zdzs.size() > 0) {
							UnitTools.deleteUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
							super.DeleteGeo(zdbdcdyid, BDCDYLX.SHYQZD, DJDYLY.GZ);
							updateDCDYStatus(BDCDYLX.SHYQZD.Value, zdbdcdyid);
						}
					}
				}
				// 删除权利、附属权利、权利人、证书、权地证人关系
				String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
				RightsTools.deleteRightsAllByCondition(DJDYLY.initFrom(ly), _hqlCondition);

				// 更新调查库相应单元状态
				updateDCDYStatus(bdcdylx, bdcdyid);
			} else {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ,
						"DJDYID='" + djdy.getDJDYID() + "' AND QLLX='" + getQllx().Value + "'");
				if (qls != null && qls.size() > 0) {
					List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class,
							"XMBH='" + getXMBH() + "' AND GLQLID='" + qls.get(0).getId() + "'");
					if (sqrs != null && sqrs.size() > 0) {
						for (int isqr = 0; isqr < sqrs.size(); isqr++) {
							dao.deleteEntity(sqrs.get(isqr));
						}
					}
				}
			}
		}
		dao.flush();
		return false;
	}

	private void updateDCDYStatus(String bdcdylx, String bdcdyid) {
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.DC, bdcdyid);
		if (unit != null) {
			unit.setDJZT(DJZT.WDJ.Value);
			getCommonDao().update(unit);
		}
	}

	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> tree = super.getUnitList();
		List<UnitTree> newtree = new ArrayList<UnitTree>();
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(this.getXMBH());
		if (!StringHelper.isEmpty(xmxx) && SF.YES.Value.equals(xmxx.getSFDB())) {
			if (!StringHelper.isEmpty(tree)) {
				for (int i = 0; i < tree.size(); i++) {
					UnitTree t = tree.get(i);
					if (!this.getBdcdylx().Value.equals(t.getBdcdylx())) {
						continue;
					}
					if (!StringHelper.isEmpty(t)) {
						// 获取变更前的权利信息
						if (DJDYLY.LS.Name.toUpperCase().equals(t.getLy().toUpperCase())
								|| DJDYLY.XZ.Name.toUpperCase().equals(t.getLy().toUpperCase())) {
							String fulSql = "select ql.qlid from BDCK.BDCS_QL_LS ql left join BDCK.BDCS_FSQL_LS fsql on "
									+ "fsql.qlid=ql.qlid where fsql.zxdyywh='" + xmxx.getPROJECT_ID() + "' "
									+ "and ql.djdyid='" + t.getDjdyid() + "' and ql.qllx='" + this.getQllx().Value
									+ "'";
							@SuppressWarnings("rawtypes")
							List<Map> lstql = getCommonDao().getDataListByFullSql(fulSql);
							if (!StringHelper.isEmpty(lstql) && lstql.size() > 0) {
								@SuppressWarnings("rawtypes")
								Map m = lstql.get(0);
								String qlid = StringHelper.formatObject(m.get("QLID"));
								tree.get(i).setQlid(qlid);
								tree.get(i).setOldqlid(qlid);
								tree.get(i).setLy("ls");
							}
						}
					}
					newtree.add(t);
				}
			}
			// 重写
		} else if (!StringHelper.isEmpty(xmxx)) {
			if (!StringHelper.isEmpty(tree)) {
				for (int i = 0; i < tree.size(); i++) {
					UnitTree t = tree.get(i);
					if (!this.getBdcdylx().Value.equals(t.getBdcdylx())) {
						continue;
					}
					if (!StringHelper.isEmpty(t)) {
						if (!DJDYLY.GZ.Name.toUpperCase().equals(t.getLy().toUpperCase())) {
							List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.LS,
									" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='" + t.getBdcdyid()
											+ "') AND QLLX='" + super.getQllx().Value + "'");
							if (qls != null && qls.size() > 0) {
								t.setOldqlid(qls.get(0).getId());
							}
						} else {
							// 判断自然幢跟宗地信息来源
							if (BDCDYLX.H.equals(this.getBdcdylx())) {
								String zdbdcdyid = t.getZdbdcdyid();
								List<RealUnit> zds = UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, "BDCDYID ='"
										+ zdbdcdyid + "' and " + ProjectHelper.GetXMBHCondition(this.getXMBH()));
								if (!StringHelper.isEmpty(zds) && zds.size() > 0) {
									t.setZdly("gz");
								} else {
									t.setZdly("ls");
								}
								String zrzbdcdyid = t.getZrzbdcdyid();
								List<RealUnit> zrzs = UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ, "BDCDYID ='"
										+ zrzbdcdyid + "' and " + ProjectHelper.GetXMBHCondition(this.getXMBH()));
								if (!StringHelper.isEmpty(zrzs) && zrzs.size() > 0) {
									t.setZrzly("gz");
								} else {
									t.setZrzly("ls");
								}
							}
						}
					}
					newtree.add(t);
				}
			}
		}
		return newtree;
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

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

	/**
	 * 导出交换文件
	 */
	@SuppressWarnings("unused")
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
					xmbhHql + " and LY = '" + ConstValue.DJDYLY.GZ.Value + "' ");
			List<BDCS_DJDY_GZ> bgqdjdys = dao.getDataList(BDCS_DJDY_GZ.class,
					xmbhHql + " and LY = '" + ConstValue.DJDYLY.XZ.Value + "' ");
			String msgName = "";
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value)
							|| QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)
							|| QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
							BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, djdy.getBDCDYID());
							String preEstateNum = "";
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
							for (int j = 0; j < bgqdjdys.size(); j++) {
								BDCS_DJDY_GZ bgqdjdy = bgqdjdys.get(j);
								BDCS_SHYQZD_XZ zd_XZ = dao.get(BDCS_SHYQZD_XZ.class, bgqdjdy.getBDCDYID());
								if (zd_XZ != null && (j == 0 || j == bgqdjdys.size() - 1)) {
									preEstateNum += zd_XZ.getBDCDYH();
								} else {
									preEstateNum += zd_XZ.getBDCDYH() + ",";
								}
							}
							// 标记单元号是否发生变化了，是否取消空间节点的存在
							boolean flag = false;
							if (!(zd.getBDCDYH().equals(preEstateNum)) && !StringUtils.isEmpty(preEstateNum)) {
								flag = true;
							}
							Message msg = exchangeFactory.createMessage(flag);

							super.fillHead(msg, i, ywh, zd.getBDCDYH(), zd.getQXDM(),ql.getLYQLID());

							BDCS_DYBG dybg = packageXml.getDYBG(zd.getId());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));// 当前的不动产单元号
//							msg.getHead().setPreEstateNum(preEstateNum);// 上一首的不动产单元号
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);

								String zdzhdm = "";
								if (zd != null) {
									zdzhdm = zd.getZDDM();
								}

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
								msg.getData().setZDBHQK(bhqk);

								KTTGYJZX jzx = msg.getData().getKTTGYJZX();
								jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

								KTTGYJZD jzd = msg.getData().getKTTGYJZD();
								jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);

								QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
								syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null,
										null);

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
								msg.getData().setDJGD(gd);	;

								BDCS_SHYQZD_GZ zd2 = null;
								if (null != dybg) {
									zd2 = new BDCS_SHYQZD_GZ();
									zd2.setId(dybg.getLBDCDYID());
									// zd.setId(dybg.getLBDCDYID());
								}
								// 如果当前不动产单元号和上一个不动产单元号不同的话，把空间节点加进去
								if (!(preEstateNum.equals(zd.getBDCDYH()))) {
									List<ZDK103> zdk = msg.getData().getZDK103();
									zdk = packageXml.getZDK103(zdk, zd2, null, null);
									msg.getData().setZDK103(zdk);
								}
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value, actinstID,
									djdy.getDJDYID(), ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_gz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if (zrz_gz != null) {
									zrz_gz.setGHYT(h.getGHYT()); // 自然幢的ghyt取户的ghyt
									zrz_gz.setFWJG(zrz_gz.getFWJG() == null || zrz_gz.getFWJG().equals("") ? h.getFWJG()
											: zrz_gz.getFWJG());
								}
							}
							BDCS_LJZ_XZ ljz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_gz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
								if (zrz_gz != null) {
									zrz_gz.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ();
							super.fillHead(msg, i, ywh, h.getBDCDYH(), h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
								msg.getHead().setAreaCode(h.getQXDM());
							}
							String preEstateNum = "";
							for (BDCS_DJDY_GZ bgqdjdy : bgqdjdys) {
								BDCS_H_LS h_LS = dao.get(BDCS_H_LS.class, bgqdjdy.getBDCDYID());
								if (!StringUtils.isEmpty(h.getId()) && h.getId().equals(h_LS.getId())) {
									preEstateNum = h_LS.getBDCDYH();
									break;
								}
							}
//							msg.getHead().setPreEstateNum(preEstateNum);
							BDCS_C_GZ c = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c = dao.get(BDCS_C_GZ.class, h.getCID());
							}

							if (djdy != null) {
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
								zrz = packageXml.getKTTFWZRZ(zrz, zrz_gz);

								KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_gz,h);

								KTTFWC kttc = msg.getData().getKTTFWC();
								kttc = packageXml.getKTTFWC(kttc, c, zrz);
								msg.getData().setKTTFWC(kttc);

								KTTFWH fwh = msg.getData().getKTTFWH();
								fwh = packageXml.getKTTFWH(fwh, h);

								QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);

								List<ZDK103> fwk = msg.getData().getZDK103();
								fwk = packageXml.getZDK103H(fwk, h, zrz_gz);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null,
										null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								//9.登记收费(可选)
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
								msg.getData().setDJGD(gd);	;

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value, actinstID,
									djdy.getDJDYID(), ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (null == result) {
						Map<String, String> xmlError = new HashMap<String, String>();
						if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value,
									ConstValue.RECCODE.TDSYQ_BGDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value,
									ConstValue.RECCODE.FDCQDZ_BGDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value,
									ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}
						return xmlError;
					}
					if (!"1".equals(result) && result.indexOf("success") == -1) { // xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if (!StringUtils.isEmpty(result) && result.indexOf("success") > -1
							&& !names.containsKey("reccode")) {
						names.put("reccode", result);
					}
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}

	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
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
				Rights bdcql = RightsTools.loadRightsByDJDYID(dyly, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(dyly, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(dyly, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}

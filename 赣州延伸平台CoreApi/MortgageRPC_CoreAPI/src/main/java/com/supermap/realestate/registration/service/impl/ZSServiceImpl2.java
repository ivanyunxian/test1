package com.supermap.realestate.registration.service.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.maintain.Unit;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_XTDZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.ZSService2;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.GYFS;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

@Component
public class ZSServiceImpl2 implements ZSService2 {

	@Autowired
	private CommonDao baseCommonDao;

	public BDCS_ZS_GZ getZS(String zsid) {
		return baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
	}

	/**
	 * 获取多个单元的不动产证书信息
	 * 
	 * @Title: getZSForm
	 * @author:liushufeng
	 * @date：2015年11月14日 下午8:46:13
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	//TODO1.根据配置文件读取配置2.优化部分代码逻辑3.地区化配置4.去掉一些非必要的逻辑5.非THML化
	public ZS getZSForm(String xmbh, String zsid) {
		
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		
		ZS zsform = new ZS();
		// 1.根据zsid获取证书信息
		BDCS_ZS_GZ zs = getZS(zsid);
		// 项目信息
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		// 登记单元
		BDCS_DJDY_GZ djdy_gz = this.getDjdyGzByZs(xmbh, zsid);
		// 不动产单元 类型
		BDCDYLX lx = BDCDYLX.initFrom(djdy_gz.getBDCDYLX());
		// 不动产单元来源
		DJDYLY ly = DJDYLY.initFrom(djdy_gz.getLY());
		// 当前证书关联的权利
		Rights rights = RightsTools.loadRightsByZSID(DJDYLY.GZ, zsid, xmbh);
		// 不动产单元（如果是多个单元合一本证，就是多个单元，如果是每个单元一本证，就是当前证书对应的单元）
		List<RealUnit> units = null;
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value))
			units = UnitTools.loadUnits(lx, ly,
					" id in (select BDCDYID FROM BDCS_DJDY_GZ WHERE "
							+ ProjectHelper.GetXMBHCondition(xmbh)
							+ " AND GROUPID=" + djdy_gz.getGROUPID() + " AND LY='"+djdy_gz.getLY()+"')");
		else
			units = UnitTools.loadUnits(lx, ly,
					"BDCDYID='" + djdy_gz.getBDCDYID() + "'");

		units = SortUnit(units);
		units = ObjectHelper.SortList(units);

		// 权利关联的权利人列表
		List<RightsHolder> qlholders = RightsHolderTools.loadRightsHolders(
				DJDYLY.GZ, rights.getId());
		// 证书关联的权利人列表
		List<RightsHolder> zsholders = RightsHolderTools
				.loadRightsHoldersByZSID(DJDYLY.GZ, xmbh, zsid);
//		// 空白符
//		String blank = "----";
//		// 换行符
//		String hhf = "<br/>";
//		// 空格
//		String space = "&nbsp;&nbsp;&nbsp;";
		// 空白符
		String blank = "";
		// 换行符
		String hhf = ";";
		// 空格
		String space = "";

		// -1,证书编号
		String zshb = "";
		// 0,不动产权证号
		String bdcqzh = "";
		// 1,封面年
		String fmn = "";
		// 2,封面月
		String fmy = "";
		// 3,封面日
		String fmr = "";
		// 4,省份
		String qhjc = "";
		// 5,地区名称
		String qhmc = "";
		// 6,年度
		String nd = "";
		// 7,产权证号
		String cqzh = "";
		// 8,权利人
		String qlrmc = "";
		// 9,共有情况
		String gyqk = "";
		// 10,坐落
		String zl = "";
		// 11,不动产单元号
		String bdcdyh = "";
		// 12,权利类型
		String qllx = "";
		// 13,权利性质
		String qlxz = "";
		// 14,用途
		String yt = "";
		// 15,面积
		String mj = "";
		// 16,使用期限
		String syqx = "";
		// 17,其他状况-土地使用权面积
		String qt_tdsyqmj = "";
		// 18,其他状况-专有建筑面积
		String qt_zyjzmj = "";
		// 19,其他状况-分摊建筑面积
		String qt_ftjzmj = "";
		// 20,其他状况-房屋结构
		String qt_fwjg = "";
		String qt_fwjg1 = "";
		String qt_fwjg2 = "";
		String qt_fwjg3 = "";
		// 21,其他状况-房屋总层数
		String qt_fwzcs = "";
		// 22,其他状况-房屋所在层
		String qt_fwszc = "";
		// 23,其他状况-房屋竣工时间
		String qt_fwjgsj = "";
		
		String qt_pzmj = "";
		// 24,其他状况-持证方式
		String qt_czfs = "";
		// 25,其他状况-持证人
		String qt_czr = "";
		// 26,附记
		String fj = "";

		String qt_fh = "";

		// 其他状况-建筑密度和容积率
		String qt_jzmd_and_rjl = "";
		
		// 其他状况-共有土地面积
		String qt_gytdmj = "";
		
		// 其他状况-权利共有人情况
		String qt_ql_gyrqk = "";
		
		//获取区县代码 liangq
		String qxdm_ = ConfigHelper.getNameByValue("XZQHDM");
		String pfm = "㎡";
		if(qxdm_.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		// 省份、年度、地区、顺序号
		if (null != zs) {
			bdcqzh = zs.getBDCQZH()== null ?rights.getBDCQZH() : zs.getBDCQZH();//多个单元一本证不重新获取证号时，bdcs_zs_gz无权证号，取ql表的权证号
			zshb = zs.getZSBH();
			if (!StringUtils.isEmpty(bdcqzh)) {
				List<String> listqzh = StringHelper.MatchBDCQZH(bdcqzh);
				if (listqzh.size() == 4)// 受理页面想查看证书信息时，出错
				{
					qhjc = listqzh.get(0);
					nd = listqzh.get(1);
					qhmc = listqzh.get(2);
					cqzh = listqzh.get(3);
				}
			}
			// 封面上的年，月，日
			Date date = rights.getDJSJ();
			if (date != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				fmn = cal.get(Calendar.YEAR) + "";
				fmy = (cal.get(Calendar.MONTH) + 1) + "";
				fmr = cal.get(Calendar.DATE) + "";
			}

			// 权利人名称--不区分是否多个单元一本证
			for (RightsHolder holder : zsholders) {
				if (StringHelper.isEmpty(qlrmc)) {
					qlrmc = holder.getQLRMC() + ",";
				} else {
					qlrmc = qlrmc + holder.getQLRMC() + ",";
				}
			}
			if(!StringHelper.isEmpty(qlrmc)){
				// 权利人名称多于本地化配置设置显示个数时，显示等xx个
				qlrmc = qlrmc.substring(0, qlrmc.length() - 1);
				String[] qlrmcs = qlrmc.split("、");
				int qlgs = 0;
				if (qlrmcs != null) {
					qlgs = desCountBySrcCount("ZSPARAM_QLR_COUNT",qlrmcs.length, 1);
				}
				if (qlrmcs != null && qlrmcs.length > qlgs) {
					// 获取前两个不动产权证号
					for (int i = 0; i < qlgs; i++) {
						if (i == 0) {
							qlrmc = qlrmcs[i];
						} else {
							qlrmc = qlrmc + "、" + qlrmcs[i];
						}
					}
					qlrmc = qlrmc + "等" + qlrmcs.length + "人";
				}
			}
			// 共有方式--不区分是否多个单元一本证
			if (zsholders != null && zsholders.size() > 0) {
				for (RightsHolder holder : zsholders) {
					if (holder.getGYFS() != null) {
						gyqk = holder.getGYFSName();
						break;
					}
				}
			}

			// 坐落和不动产单元号--区分是否多个单元一本证（已兼容）
			if (units != null && units.size() > 0) {
				int zlgs = desCountBySrcCount("ZSPARAM_ZL_COUNT", units.size(),
						1);
				int dygs = desCountBySrcCount("ZSPARAM_BDCDYH_COUNT",
						units.size(), 1);
				zl = units.get(0).getZL();

				// if (units.size() > 1) {
				// zl = zl + "等" + units.size() + "处";
				// bdcdyh = bdcdyh + "等" + units.size() + "个";
					//坐落
				if (units.size() > zlgs) {
					for (int i = 0; i < zlgs; i++) {
						if (i == 0) {
							zl = units.get(0).getZL();
						} else {
							zl += "," + units.get(i).getZL();
						}
					}
					zl += "等" + units.size() + "处";
				} 
				else {
					for (int i = 0; i < units.size(); i++) {
						if (i == 0) {
							zl = units.get(0).getZL();
						} else {
							zl += "," + units.get(i).getZL();
						}
					}
				}
				if (units.size() > dygs) {
					for (int i = 0; i < dygs; i++) {
						if (i == 0) {
							bdcdyh = formatBDCDYH(units.get(i).getBDCDYH());
						} else {
							bdcdyh += ","
									+ formatBDCDYH(units.get(i).getBDCDYH());
						}
					}
					bdcdyh += "等" + units.size() + "个";
				} else {
					for (int i = 0; i < units.size(); i++) {
						if (i == 0) {
							bdcdyh = formatBDCDYH(units.get(i).getBDCDYH());
						} else {
							bdcdyh += ","
									+ formatBDCDYH(units.get(i).getBDCDYH());
						}
					}
				}
			}

			// 权利类型--不区分是否多个单元一本证
				String ql_lx = rights.getQLLX();
				if (!StringHelper.isEmpty(ql_lx)) {
					qllx = ConstHelper.getNameByValue_new("QLLX", ql_lx.trim(),StringHelper.formatObject(xmxx.getSLSJ()));
				}else {
					qllx = blank;
				}
			
			//qllx = ConstHelper.getNameByValue("QLLX", rights.getQLLX().trim());			

			// 权利性质--区分是否多个单元一本证（还不兼容）
			if (units != null && units.size() > 0) {
				if (units.get(0).getBDCDYLX().equals(BDCDYLX.H)) {
					House house = (House) units.get(0);
					if (house != null) {
						String tdqlxz = "";
						String fwqlxz = "";
						for (RealUnit unit : units) {
							House h = (House) unit;
							if (h == null)
								continue;
							if (SF.NO.Value.equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
								if (!StringHelper.isEmpty(h.getQLXZ())) {
									String tdqlxzmc = ConstHelper.getNameByValue("QLXZ", h.getQLXZ().trim());
									if (!StringHelper.isEmpty(tdqlxzmc)&& !tdqlxz.contains(tdqlxzmc)) {
										tdqlxz += tdqlxzmc + "、";
									}
								}
							} else if (SF.YES.Value.equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
								UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,house.getZDBDCDYID());
								if (land != null && !StringHelper.isEmpty(land.getQLXZ())) {
									String tdqlxzmc = ConstHelper.getNameByValue("QLXZ", land.getQLXZ().trim());
									if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
										tdqlxz += tdqlxzmc + "、";
									}
								}
							} else if (("2").equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
								if (!StringHelper.isEmpty(h.getQLXZ())) {
									String tdqlxzmc = ConstHelper.getNameByValue("QLXZ", h.getQLXZ().trim());
									if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
										tdqlxz += tdqlxzmc + "、";
									}
								} else if (!StringHelper.isEmpty(h.getZDBDCDYID())) {
									UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, h.getZDBDCDYID());
									if (land != null && !StringHelper.isEmpty(land.getQLXZ())) {
										String tdqlxzmc = ConstHelper.getNameByValue("QLXZ", land.getQLXZ().trim());
										if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
											tdqlxz += tdqlxzmc + "、";
										}
									}
								}
							}
							if (!StringHelper.isEmpty(h.getFWXZ())) {
								String fwxzmc = ConstHelper.getNameByValue("FWXZ", h.getFWXZ().trim());
								if (fwxzmc != null && !fwqlxz.contains(fwxzmc))
									fwqlxz += fwxzmc + "、";
							}
						}
						if (StringHelper.isEmpty(tdqlxz))
							tdqlxz = blank;
						else
							tdqlxz = tdqlxz.substring(0, tdqlxz.length() - 1);
						if (StringHelper.isEmpty(fwqlxz))
							fwqlxz = blank;
						else
							fwqlxz = fwqlxz.substring(0, fwqlxz.length() - 1);
						qlxz = tdqlxz + "/" + fwqlxz;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							qlxz = blank + "/" + fwqlxz;
						}
					}
				} else if (lx.equals(BDCDYLX.SHYQZD)) {
				}
			}

			// 用途
			if (units != null && units.size() > 0) {
				if (units.get(0).getBDCDYLX().equals(BDCDYLX.H)) {
					House house = (House) units.get(0);
					if (house != null) {
						String tdyt = "";
						String fwyt = "";
						List<String> list_tdyt = new ArrayList<String>();
						List<String> list_fwyt = new ArrayList<String>();
						for (RealUnit unit : units) {
							House h = (House) unit;
							if (h == null)
								continue;
							String configZDTDYT = ConfigHelper.getNameByValue("GetZDTDYTFrom");
							if ("0".equals(configZDTDYT)) {// 从房屋土地用途获取
								String tdytmc = "";
								if (!StringHelper.isEmpty(h.getFWTDYT())) {
									tdytmc = ConstHelper.getNameByValue("TDYT", h.getFWTDYT().trim());
								}
								if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(h.getFWTDYT())) {
									tdyt += tdytmc + "、";
									list_tdyt.add(h.getFWTDYT());
								}
							} else if ("1".equals(configZDTDYT)) {// 关联宗地主用途
								List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + h.getZDBDCDYID() + "'");
								String tdyt_tdyt = "";
								if (yts != null && yts.size() > 0) {
									for (BDCS_TDYT_XZ yt_xz : yts) {
										if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
											if (StringHelper.isEmpty(tdyt_tdyt) || ("1").equals(yt_xz.getSFZYT())) {
												tdyt_tdyt = yt_xz.getTDYT();
											}
										}
									}
								}
								if (!StringHelper.isEmpty(tdyt_tdyt)) {
									String tdytmc = ConstHelper.getNameByValue("TDYT", tdyt_tdyt.trim());
									if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdyt_tdyt)) {
										tdyt += tdytmc + "、";
										list_tdyt.add(tdyt_tdyt);
									}
								}
							} else if ("2".equals(configZDTDYT)) {// 关联宗地所有用途（、分隔）
								List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + h.getZDBDCDYID() + "'");
								if (yts != null && yts.size() > 0) {
									for (BDCS_TDYT_XZ yt_xz : yts) {
										if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
											String tdytmc = ConstHelper.getNameByValue("TDYT", yt_xz.getTDYT().trim());
											if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(yt_xz.getTDYT())) {
												tdyt += tdytmc + "、";
												list_tdyt.add(yt_xz.getTDYT());
											}
										}
									}
								}
							} else if ("3".equals(configZDTDYT)) {// 优先从房屋土地用途中获取，再从关联宗地主用途获取
								String tdytmc = "";
								String tdyt_tdyt = "";
								if (!StringHelper.isEmpty(h.getFWTDYT())) {
									tdyt_tdyt = h.getFWTDYT().trim();
									tdytmc = ConstHelper.getNameByValue("TDYT", h.getFWTDYT().trim());
								}
								if (StringHelper.isEmpty(tdytmc)) {
									List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + h.getZDBDCDYID() + "'");
									if (yts != null && yts.size() > 0) {
										for (BDCS_TDYT_XZ yt_xz : yts) {
											if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
												if (StringHelper.isEmpty(tdyt_tdyt) || ("1").equals(yt_xz.getSFZYT())) {
													tdyt_tdyt = yt_xz.getTDYT();
												}
											}
										}
									}
									if (!StringHelper.isEmpty(tdyt_tdyt)) {
										tdytmc = ConstHelper.getNameByValue("TDYT", tdyt_tdyt.trim());
									}
								}
								if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdyt_tdyt)) {
									tdyt += tdytmc + "、";
									list_tdyt.add(tdyt_tdyt);
								}
							} else if ("4".equals(configZDTDYT)) {// 优先从房屋土地用途获取，再从关联宗地所有用途（、分隔）
								String tdytmc = "";
								String tdyt_tdyt = "";
								if (!StringHelper.isEmpty(h.getFWTDYT())) {
									tdytmc = ConstHelper.getNameByValue("TDYT", h.getFWTDYT().trim());
								}

								if (StringHelper.isEmpty(tdytmc)) {
									List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='"+ h.getZDBDCDYID() + "'");
									if (yts != null && yts.size() > 0) {
										for (BDCS_TDYT_XZ yt_xz : yts) {
											if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
												tdytmc = ConstHelper.getNameByValue("TDYT", yt_xz.getTDYT().trim());
												if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(yt_xz.getTDYT().trim())) {
													tdyt += tdytmc + "、";
													list_tdyt.add(yt_xz.getTDYT().trim());
												}
											}
										}
									}
								} else {
									if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdyt_tdyt)) {
										tdyt += tdytmc + "、";
										list_tdyt.add(tdyt_tdyt);
									}
								}
							}
							if (!StringHelper.isEmpty(h.getGHYT())) {
								String fwytmc = ConstHelper.getNameByValue("FWYT", h.getGHYT().trim());
								if (!list_fwyt.contains(h.getGHYT().trim())) {
									fwyt += fwytmc + "、";
									list_fwyt.add(h.getGHYT().trim());
								}
							}
						}
						if (StringHelper.isEmpty(tdyt))
							tdyt = blank;
						else
							tdyt = tdyt.substring(0, tdyt.length() - 1);
						if (StringHelper.isEmpty(fwyt))
							fwyt = blank;
						else
							fwyt = fwyt.substring(0, fwyt.length() - 1);
						yt = tdyt + "/" + fwyt;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							yt = blank + "/" + fwyt;
						}
					}
				} else if (lx.equals(BDCDYLX.SHYQZD)) {
				}
			}

			// 面积--区分是否多个单元一本证（已兼容），还没写除了房屋的其他类型
			if (units != null && units.size() > 0) {
				if (units.get(0).getBDCDYLX().equals(BDCDYLX.H)) {
					House house = (House) units.get(0);
					if (house != null) {
						Double sumtemp = 0.0;
						//改为多个单元时多块宗地时，证书页面，宗地的面积汇总
//						UseLand land = (UseLand) UnitTools
//								.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
//										house.getZDBDCDYID());
//						if (land != null) {
//							temp = land.getZDMJ() == null ? blank
//									: formatDouble(land.getZDMJ());
//						}
//						mj = "共有宗地面积" + temp + "㎡/";
						HashSet<String> has = new HashSet<String>();
						Double sumjzmj = 0.0;
						for (RealUnit unit : units) {
							House h = (House) unit;
							if (h != null) {
								sumjzmj += (h.getSCJZMJ() != null ? h.getSCJZMJ() : 0);
							}
							if(!has.contains(h.getZDBDCDYID())&&!StringHelper.isEmpty(h.getZDBDCDYID())){
								has.add(h.getZDBDCDYID());
								//改为多个单元时多块宗地时，证书页面，宗地的面积汇总
								UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,h.getZDBDCDYID());
								if (land != null) {
									sumtemp += land.getZDMJ() == null ? 0 : land.getZDMJ();
								}else {
									land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ,h.getZDBDCDYID());
									if (land != null) {
										sumtemp += land.getZDMJ() == null ? 0 : land.getZDMJ();
									}
								}
							}
						}
						if(qxdm_.indexOf("45")==0){//广西需求，分摊或独有土地面积>0时，显示“宗地面积”4个字
							if(!StringHelper.isEmpty(house.getFTTDMJ())&&house.getFTTDMJ()>0||!StringHelper.isEmpty(house.getDYTDMJ())&&house.getDYTDMJ()>0){
								mj = "宗地面积：" ;
							}else{
								mj = "共有宗地面积：" ;
							}
						}else{
							mj = "共有宗地面积：" ;
						}
						
						if(qxdm_.contains("451102")){
							mj = "宗地面积：" ;
						}
						
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							mj +=  blank + pfm+"/";
						}else {
							mj +=  formatDouble(sumtemp) +pfm+ "/";
						}
						mj += "房屋建筑面积:" + formatDouble(sumjzmj) + pfm;
						has.clear();
					}
				}
			};

			// 使用期限
			if (units != null && units.size() > 0) {
				String ytqx = "";
				for (RealUnit unit : units) {
					House h = (House) unit;
					if (h != null) {
						if (!StringHelper.isEmpty(h.getFWTDYT())) {
							String tdyt = ConstHelper.getNameByValue("TDYT", h.getFWTDYT().trim());
							if (!StringHelper.isEmpty(tdyt)) {

								String qssj = "";
								if (h.getTDSYQQSRQ() != null) {
									qssj = StringHelper.FormatByDatetime(h.getTDSYQQSRQ());
									if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
										qssj = blank;
									}
								}
								qssj = StringHelper.isEmpty(qssj) ? blank : qssj;
								String jssj = "";
								if (h.getTDSYQZZRQ() != null) {
									jssj = StringHelper.FormatByDatetime(h.getTDSYQZZRQ());
									if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
										jssj = blank;
									}
								}
								jssj = StringHelper.isEmpty(jssj) ? blank : jssj;
								String tempyt = tdyt + ":";
								if (!StringHelper.isEmpty(qssj)) {
									tempyt += qssj + "起";
								}
								if (!StringHelper.isEmpty(jssj)) {
									tempyt += jssj + "止";
								}
								if (!ytqx.contains(tempyt)) {
									ytqx += tempyt + hhf;
								}
							}
						}
					}
				}
				if (StringHelper.isEmpty(ytqx))
					ytqx = blank;
				else
					ytqx = ytqx.substring(0, ytqx.length() - 1);
				syqx = ytqx;
			}
			if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("XZQHDM"))
					&& ("1").equals(ConfigHelper.getNameByValue("GetSYQXFrom"))) {
				syqx = "";
				String qssj = "";
				if (rights.getQLQSSJ() != null) {
					qssj = StringHelper.FormatByDatetime(rights.getQLQSSJ());

				}
				qssj = StringHelper.isEmpty(qssj) ? blank : qssj;
				String jssj = "";
				if (rights.getQLJSSJ() != null) {
					jssj = StringHelper.FormatByDatetime(rights.getQLJSSJ());
				}
				jssj = StringHelper.isEmpty(jssj) ? blank : jssj;
				if (!StringHelper.isEmpty(qssj)) {
					syqx += qssj + "起";
				}
				if (!StringHelper.isEmpty(jssj)) {
					syqx += jssj + "止";
				}
				if ("1".equals(ConfigHelper.getNameByValue("LXAndSYQX"))) {
					if (BDCDYLX.H.equals(units.get(0).getBDCDYLX())) {
						StringBuffer stringBuffer = new StringBuffer(syqx);
						String nr_qllx = blank;
						if ("4".equals(xmxx.getQLLX())) {
							nr_qllx = ConstHelper.getNameByValue("QLLX", "3");
						}
						if ("6".equals(xmxx.getQLLX())) {
							nr_qllx = ConstHelper.getNameByValue("QLLX", "5");
						}
						if ("8".equals(xmxx.getQLLX())) {
							nr_qllx = ConstHelper.getNameByValue("QLLX", "7");
						}
						syqx = stringBuffer.insert(0, nr_qllx + " ").toString();
					}
				}
			}

			// 其他状况-土地使用权面积
			if (units != null && units.size() > 0) {   
				Double _dytdqmj = 0.0;
				Double _fttdmj = 0.0;		
				Double _gytdmj = 0.0;
				//土地使用权面积是否根据用途显示详情		
				String detailft = "",detaildy ="";								
				Map<String, Double> mpdy = new LinkedHashMap();
				Map<String, Double> mpft = new LinkedHashMap();
				
				//EXCHANGE配置后需要的变量
				Double temp = 0.0;
				HashSet<String> has = new HashSet<String>();
				int len = units.size();
				if (units.get(len-1)!=null) {//多个单元一本证时，任一单元共有土地面积有值就显示
					for (int i=0; i <len; i++) {
						House h = (House) units.get(i);
						if (h != null) {	
							if (!StringHelper.isEmpty(h.getGYTDMJ())&& h.getGYTDMJ()!=0){
								_gytdmj =h.getGYTDMJ();
							}
						}
					}
				}
				for (RealUnit unit : units) {
					House h = (House) unit;
					if (h != null) {
						if ("1".equals(ConfigHelper.getNameByValue("SHOWDETAIL_SHYQMJ"))) {
							//独用土地使用权面积
							String _tempyt = h.getGHYTName();
							Double _tempmj = h.getDYTDMJ() != null ? h.getDYTDMJ() : 0;
							//total
							_dytdqmj += _tempmj;
							if (mpdy.containsKey(_tempyt)) {
								mpdy.put(_tempyt, mpdy.get(_tempyt) + _tempmj);
							} else {
								mpdy.put(_tempyt, _tempmj);
							}
			
							//分摊土地使用权面积
							_tempmj = h.getFTTDMJ() != null ? h.getFTTDMJ() : 0;
							//total
							_fttdmj += _tempmj;
							if (mpft.containsKey(_tempyt)) {
								mpft.put(_tempyt, mpft.get(_tempyt) + _tempmj);
							} else {
								mpft.put(_tempyt, _tempmj);
							}
								
						}else {
							if (h.getDYTDMJ() != null) {
								_dytdqmj += h.getDYTDMJ();
							}
							if (h.getFTTDMJ() != null) {
								_fttdmj += h.getFTTDMJ();
							}	
						}
						//共有宗地面积
						if ("1".equals(ConfigHelper.getNameByValue("EXCHANGE")) || "1".equals(ConfigHelper.getNameByValue("ISSHOW_TDSYQMJ"))) {					
							if(!has.contains(h.getZDBDCDYID())){
								has.add(h.getZDBDCDYID());
								UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
										h.getZDBDCDYID());
								if (land != null) {
									temp += land.getZDMJ() == null ? 0: land.getZDMJ();
								}
							}
						}	
						
					}
				}
				
				if ("1".equals(ConfigHelper.getNameByValue("SHOWDETAIL_SHYQMJ"))) {
					if (mpdy.entrySet().size() > 1) {
						detaildy += "(其中";
						for (Entry<String, Double> entry : mpdy.entrySet()) {
							String strmj = blank;
							if (entry.getValue() > 0) {
								strmj = formatDouble(entry.getValue());
							}
							detaildy += entry.getKey() + ":" + strmj + pfm+",";
						}
						detaildy = detaildy.substring(0, detaildy.length() - 1) + ")";
					}		
					
					if (mpft.entrySet().size() > 1) {
						detailft += "(其中";
						for (Entry<String, Double> entry : mpft.entrySet()) {
							String strmj = blank;
							if (entry.getValue() > 0) {
								strmj = formatDouble(entry.getValue());
							}
							detailft += entry.getKey() + ":" + strmj + pfm+",";
						}
						detailft = detailft.substring(0, detailft.length() - 1) + ")";
					}		
				}	
				
				// Double zmj = _dytdqmj + _fttdmj;
				// String strzmj = formatDouble(zmj);
				// qt_tdsyqmj = "土地使用权面积:" + strzmj.toString() + "㎡";
				if (_fttdmj > 0 && _dytdqmj == 0) {
					qt_tdsyqmj = "分摊土地使用权面积：" + formatDouble(_fttdmj) + pfm+ detailft + hhf;
					if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
						qt_tdsyqmj = "分摊土地使用权面积：" + blank+ hhf;
					}
				} else if (_fttdmj == 0 && _dytdqmj > 0) {
					qt_tdsyqmj = "独用土地使用权面积：" + formatDouble(_dytdqmj) + pfm+ detaildy + hhf;
					if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
						qt_tdsyqmj = "独用土地使用权面积：" + blank+ hhf;
					}
				} else if (_fttdmj > 0 && _dytdqmj > 0) {
					qt_tdsyqmj = "分摊土地使用权面积：" + formatDouble(_fttdmj)+ pfm+ detailft+ "，独用土地使用权面积：" + formatDouble(_dytdqmj) + pfm+ detaildy + hhf;
					if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
						qt_tdsyqmj = "分摊土地使用权面积：" + blank+ "，独用土地使用权面积：" + blank + hhf;
					}
				}
				// if (_fttdmj > 0 && _dytdqmj > 0) {
				// qt_tdsyqmj += ",其中分摊面积：" + formatDouble(_fttdmj) + "㎡，独用面积："
				// + formatDouble(_dytdqmj) + "㎡" + hhf;
				// } else {
				// qt_tdsyqmj += hhf;
				// }
				if ("1".equals(ConfigHelper.getNameByValue("ZSPARAM_showGYTDMJ"))) {
					if (_gytdmj >0) {
						qt_gytdmj ="共有土地面积："+_gytdmj+pfm+"，";
					}else {
						if(qxdm_.contains("451102")){
							qt_gytdmj = "" ;
						}else{
							qt_gytdmj ="共有土地面积："+"---- ㎡，";
						}
					}
				}
				
				// 其他状况-土地使用权面积交换面积
				if ("1".equals(ConfigHelper.getNameByValue("EXCHANGE"))) {
					String exchange = "";
					String temp_zdname = "";
					if(qxdm_.indexOf("45") == 0 && !qxdm_.contains("4502")){//广西需求，分摊或独有土地面积>0时，显示“宗地面积”4个字
						if(_dytdqmj > 0|| _fttdmj > 0){
							temp_zdname = "宗地面积：" ;
						}else{
							temp_zdname = "共有宗地面积：" ;
						}
					}else {
						temp_zdname = "共有宗地面积：" ;
					}
					
					exchange = temp_zdname; 
					if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
						exchange = temp_zdname + blank + pfm;   //(共有)宗地面积：----
					}else{
						exchange = temp_zdname + formatDouble(temp) + pfm;  //(共有)宗地面积：tt㎡
					} 
					
					has.clear();
					// 分摊土地面积>0时，才在面 积中显示分摊土地使用权面积
					if (_fttdmj > 0 && _dytdqmj == 0) {						
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							qt_tdsyqmj = "共有宗地面积：" + blank + pfm + hhf;
							exchange = "分摊土地使用权面积：" + blank + pfm+"/";
						}else {
							qt_tdsyqmj = exchange + hhf;
							exchange = "分摊土地使用权面积：" + formatDouble(_fttdmj) + pfm + detailft + "/";
						}
					} else if (_fttdmj == 0 && _dytdqmj > 0) {
						qt_tdsyqmj = "独用土地使用权面积：" + formatDouble(_dytdqmj) + pfm + detaildy + hhf;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							qt_tdsyqmj = "独用土地使用权面积：" + blank + pfm + hhf;
						}						
					} else if (_fttdmj > 0 && _dytdqmj > 0) {						
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							qt_tdsyqmj = exchange + "，独用土地使用权面积：" + blank + pfm + hhf;
							exchange = "分摊土地使用权面积：" + blank + pfm+"/";
						}else {
							qt_tdsyqmj = exchange + "，独用土地使用权面积：" + formatDouble(_dytdqmj) + pfm + detaildy + hhf;
							exchange = "分摊土地使用权面积：" + formatDouble(_fttdmj) + pfm + detailft + "/";
						}
					}
					// 面积--区分是否多个单元一本证（已兼容），还没写除了房屋的其他类型
					if (units.get(0).getBDCDYLX().equals(BDCDYLX.H)) {
						mj = exchange;
						Double sumjzmj = 0.0;
						for (RealUnit unit : units) {
							House h = (House) unit;
							if (h != null) {
								sumjzmj += (h.getSCJZMJ() != null ? h.getSCJZMJ() : 0);
							}
						}
						mj += "房屋建筑面积:" + formatDouble(sumjzmj) + pfm;
					}
				}
				
				//是否将面积中的共用宗地面积改为土地使用权面积，并将共用宗地面积显示在权利其他状况里---赤壁
				if ("1".equals(ConfigHelper.getNameByValue("ISSHOW_TDSYQMJ"))){
					String tdsyqmj = "";
					if (_fttdmj > 0 && _dytdqmj == 0) {
						tdsyqmj = "土地使用权面积:" + formatDouble(_fttdmj) + pfm;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							tdsyqmj = "土地使用权面积:" + blank +  pfm;
						}
					} else if (_fttdmj == 0 && _dytdqmj > 0) {
						tdsyqmj = "土地使用权面积:" + formatDouble(_dytdqmj) + pfm;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							tdsyqmj = "土地使用权面积:" + blank + pfm;
						}
					} else if (_fttdmj > 0 && _dytdqmj > 0) {
						tdsyqmj = "土地使用权面积:" + formatDouble(_fttdmj + _dytdqmj)+ pfm;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							tdsyqmj = "土地使用权面积:" + blank + pfm;
						}
					}
					Double sumjzmj = 0.0;
					for (RealUnit unit : units) {
						House h = (House) unit;
						if (h != null) {
							sumjzmj += (h.getSCJZMJ() != null ? h.getSCJZMJ() : 0);
						}
					}
					if(!StringHelper.isEmpty(tdsyqmj)){
						mj = tdsyqmj + "/房屋建筑面积:" + formatDouble(sumjzmj) + pfm;					
					//共有宗地面积					
						if (!"1".equals(ConfigHelper.getNameByValue("EXCHANGE"))) {											
							if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
								qt_tdsyqmj = "共有宗地面积:"+ blank + hhf + qt_tdsyqmj;	  //(共有)宗地面积：----
							}else {
								qt_tdsyqmj = "共有宗地面积:"+ temp + pfm + hhf + qt_tdsyqmj;	
							}
						}
					}
				}
			}
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
			String sql = " WORKFLOWCODE='" + workflowcode + "'";
			CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
			List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, sql);
			if (mappings != null && mappings.size() > 0) {
				StringBuilder builderPZMJ = new StringBuilder();
				WFD_MAPPING maping = mappings.get(0);
				if (("1").equals(maping.getSFADDPZMJ())){
					String pzmj="";
					double pz=0.0;
					House house = (House) units.get(0);
					String zdbdcdyid=house.getZDBDCDYID();
					if(!StringHelper.isEmpty(zdbdcdyid)){
						BDCS_SYQZD_XZ SYQZD_XZ = baseCommonDao.get(BDCS_SYQZD_XZ.class,zdbdcdyid);
						if(SYQZD_XZ!=null&&!StringHelper.isEmpty(SYQZD_XZ.getPZMJ())){
							pz=SYQZD_XZ.getPZMJ();
							pzmj=ReplaceEmptyByBlankOnDouble(pz);
							builderPZMJ.append("批准面积：").append(pzmj).append(pfm);
						}else{
							BDCS_SHYQZD_XZ SHYQZD_XZ = baseCommonDao.get(BDCS_SHYQZD_XZ.class,zdbdcdyid);
							if(SHYQZD_XZ!=null&&!StringHelper.isEmpty(SHYQZD_XZ.getPZMJ())){
								pz=SHYQZD_XZ.getPZMJ();
								pzmj=ReplaceEmptyByBlankOnDouble(pz);
								builderPZMJ.append("批准面积：").append(pzmj).append(pfm);
							}
						}
					}
				}
				mj += space + space + builderPZMJ.toString();
			}
			// 其他状况-专有建筑面积和分摊建筑面积
			if (units != null && units.size() > 0) {
				Double sumzy = 0.0;
				Double sumft = 0.0;
				// 把HashMap修改成LinkedHashMap,
				Map<String, Double> mpzy = new LinkedHashMap();
				Map<String, Double> mpft = new LinkedHashMap();
				for (RealUnit unit : units) {
					House h = (House) unit;
					if (h != null) {
						String _tempyt = h.getGHYTName();
						Double _tempmj = h.getSCTNJZMJ() != null ? h.getSCTNJZMJ() : 0;
						sumzy += _tempmj;
						if (mpzy.containsKey(_tempyt)) {
							mpzy.put(_tempyt, mpzy.get(_tempyt) + _tempmj);
						} else {
							mpzy.put(_tempyt, _tempmj);
						}

						// _tempmj = h.getSCFTJZMJ() != null ?
						// h.getSCFTJZMJ()+100 : 0;
						_tempmj = h.getSCFTJZMJ() != null ? h.getSCFTJZMJ() : 0;
						sumft += _tempmj;
						if (mpft.containsKey(_tempyt)) {
							mpft.put(_tempyt, mpft.get(_tempyt) + _tempmj);
						} else {
							mpft.put(_tempyt, _tempmj);
						}
					}
				}
				String strsumzy = blank;
				if (sumzy > 0) {
					strsumzy = formatDouble(sumzy);
					//专有建筑面积为0时，不显示
					qt_zyjzmj += "专有建筑面积:" + strsumzy + pfm;
					if (mpzy.entrySet().size() > 1) {
						qt_zyjzmj += "(其中";
						for (Entry<String, Double> entry : mpzy.entrySet()) {
							String strmj = blank;
							if (entry.getValue() > 0) {
								strmj = formatDouble(entry.getValue());
							}
							qt_zyjzmj += entry.getKey()  + ":" +  strmj + pfm+",";
						}
						qt_zyjzmj = qt_zyjzmj.substring(0, qt_zyjzmj.length() - 1) + ")" + hhf;
					} else {
						qt_zyjzmj += hhf;
					}
				}

				String strsumft = blank;
				if (sumft > 0) {
					strsumft = formatDouble(sumft);
					//分摊建筑面积为0时，不显示
					qt_ftjzmj = "分摊建筑面积:" + strsumft + pfm;
					if (mpft.entrySet().size() > 1) {
						qt_ftjzmj += "(其中";
						for (Entry<String, Double> entry : mpft.entrySet()) {
							String strmj = blank;
							if (entry.getValue() > 0) {
								strmj = formatDouble(entry.getValue());
							}
							qt_ftjzmj += entry.getKey() +  ":" + strmj + pfm+",";
						}
						qt_ftjzmj = qt_ftjzmj.substring(0, qt_ftjzmj.length() - 1) + ")";
					}
					qt_ftjzmj += hhf;
				}
			}

			// 其他状况-房屋结构
			if (units != null && units.size() > 0) {
				qt_fwjg = "";
				qt_fwjg1 = "";
				qt_fwjg2 = "";
				qt_fwjg3 = "";
				List<String> list = new ArrayList<String>();
				for (RealUnit unit : units) {
					House h = (House) unit;
					if (null != h) {
						if (!StringHelper.isEmpty(h.getFWJG())) {
							String _fwjg = ConstHelper.getNameByValue("FWJG", h .getFWJG().trim());
							if (!StringHelper.isEmpty(_fwjg) && !list.contains(_fwjg)) {
								qt_fwjg += _fwjg + "、";
								list.add(_fwjg);
							}
						}
						if (!StringHelper.isEmpty(h.getFWJG1())) {
							String _fwjg1 = ConstHelper.getNameByValue("FWJG", h .getFWJG1().trim());
							if (!StringHelper.isEmpty(_fwjg1) && !list.contains(_fwjg1)) {
								qt_fwjg1 += _fwjg1 + "、";
								list.add(_fwjg1);
							}
						}
						if (!StringHelper.isEmpty(h.getFWJG2())) {
							String _fwjg2 = ConstHelper.getNameByValue("FWJG", h .getFWJG2().trim());
							if (!StringHelper.isEmpty(_fwjg2) && !list.contains(_fwjg2)) {
								qt_fwjg2 += _fwjg2 + "、";
								list.add(_fwjg2);
							}
						}
						if (!StringHelper.isEmpty(h.getFWJG3())) {
							String _fwjg3 = ConstHelper.getNameByValue("FWJG", h .getFWJG3().trim());
							if (!StringHelper.isEmpty(_fwjg3) && !list.contains(_fwjg3)) {
								qt_fwjg3 += _fwjg3 + "、";
								list.add(_fwjg3);
							}
						}
					}
				}
				if (!StringHelper.isEmpty(qt_fwjg)) {
					qt_fwjg = "房屋结构：" + qt_fwjg.substring(0, qt_fwjg.length() - 1);
					if (!StringHelper.isEmpty(qt_fwjg1)) {
						qt_fwjg +="、"+ qt_fwjg1.substring(0, qt_fwjg1.length() - 1);
						if (!StringHelper.isEmpty(qt_fwjg2)) {
							qt_fwjg += "、"+ qt_fwjg2.substring(0, qt_fwjg2.length() - 1);
							if (!StringHelper.isEmpty(qt_fwjg3)) {
								qt_fwjg += "、"+ qt_fwjg3.substring(0, qt_fwjg3.length() - 1);
							}
						}
					}
					qt_fwjg += hhf;
				}
			}

			// 其他状况-房屋总层数		
			if (units != null) {
				//黑龙江齐齐哈尔（230200）、龙沙区（230202）、建华区（230203）、铁锋区（230204）不显示房屋总层数		
				String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
				boolean flag_zcs = true;
				if ("230200".equals(xzqhdm) || "230202".equals(xzqhdm) || "230203".equals(xzqhdm) || "230203".equals(xzqhdm)) {
					flag_zcs= false;
				}
				if (flag_zcs) {
					qt_fwzcs = "房屋总层数：";
					House h = (House) units.get(0);
					if ("1".equals(ConfigHelper.getNameByValue("SHOWWAY_FWZCS"))) {
						if (h != null) {						
							if ( StringHelper.isEmpty(h.getZCS()) || "0".equals(h.getZCS().toString())) {
								qt_fwzcs +=  blank;
								if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
									qt_fwzcs += "(地上层数：----,地下层数：----)";
								}
								qt_fwzcs +=  "，";							
							}else {		
								qt_fwzcs +=  h.getZCS().toString();
								if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
									String dscs = "----",dxcs = "----";
									if(!StringHelper.isEmpty(h.getZRZBDCDYID())){
										RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, h.getZRZBDCDYID());
										if(unit_zrz==null){
											unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, h.getZRZBDCDYID());
										}
										if(unit_zrz!=null){
											Building building=(Building)unit_zrz;
											if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDSCS())) && !"0".equals(StringHelper.formatDouble(building.getDSCS()))) {
												dscs = StringHelper.formatDouble(building.getDSCS());
											}
											if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDXCS())) && !"0".equals(StringHelper.formatDouble(building.getDXCS()))) {
												dxcs = StringHelper.formatDouble(building.getDXCS());
											}
										}
									}
									qt_fwzcs += "(地上层数：" + dscs+ ",地下层数：" + dxcs+")";
								}								
								qt_fwzcs +=  "，";
							}
						} else {
							qt_fwzcs +=  blank ;
							if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
								qt_fwzcs += "(地上层数：----,地下层数：----)";
							}
							qt_fwzcs +=  "，";	
						}
					}else {
						if (h != null) {
							if (StringHelper.isEmpty(h.getZCS()) || "0".equals(h.getZCS().toString())) {	
								qt_fwzcs +=  "0" ;
								if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
									qt_fwzcs +=  "(地上层数：0,地下层数：0)";
								}
								qt_fwzcs +=  "，";
							}else {
								//qt_fwzcs +=  h.getZCS().toString() + "，";
								if(units.size() > 0){
									StringBuilder zcss = new StringBuilder();
									for (int i = 0; i < units.size(); i++) {
										House _h = (House)units.get(i);
										zcss = zcss.append(_h.getZCS());
										if (i < units.size()-1) {
											zcss = zcss.append(",");
										}
									}
									if(qxdm_.contains("450300")){
										if(units.size()>3){
											qt_fwzcs =qt_fwzcs+zcss.toString()+ "等"+units.size()+"处";
										}else{
											qt_fwzcs =qt_fwzcs+zcss.toString();
										}
									}else{
										if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)){
											qt_fwzcs =qt_fwzcs+((House)units.get(0)).getZCS();
										}else{
											qt_fwzcs =qt_fwzcs+zcss.toString();
										} 
									}
								}
								if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
									String dscs = "0",dxcs = "0";
									if(!StringHelper.isEmpty(h.getZRZBDCDYID())){
										RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, h.getZRZBDCDYID());
										if(unit_zrz==null){
											unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, h.getZRZBDCDYID());
										}
										if(unit_zrz!=null){
											Building building=(Building)unit_zrz;
											if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDSCS())) && !"0".equals(StringHelper.formatDouble(building.getDSCS()))) {
												dscs = StringHelper.formatDouble(building.getDSCS());
											}
											if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDXCS())) && !"0".equals(StringHelper.formatDouble(building.getDXCS()))) {
												dxcs = StringHelper.formatDouble(building.getDXCS());
											}
										}
									}
									qt_fwzcs += "(地上层数：" + dscs + ",地下层数："+dxcs + ")";
								}
								qt_fwzcs += "，";
							}
						} else {
							qt_fwzcs += "0" + "，";
							if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
								qt_fwzcs += "(地上层数：0,地下层数：0)";
							}
							//qt_fwzcs +="，";
						}
					}
				}
			}
			// 其他状况-房屋所在层
			if (units != null) {
				qt_fh = "房号:";
				String tempfh = "";
				for (RealUnit unit : units) {
					House h = (House) unit;
					if (h != null) {
						tempfh += StringHelper.isEmpty(h.getFH()) ? blank : h.getFH() +"";
						tempfh+= ",";
					}
				}
				tempfh = StringHelper.isEmpty(tempfh) ? blank : tempfh
						.substring(0, tempfh.length() - 1);
				// 如果房屋所在层超过3个时，用等几个显示
				String[] lstfh = tempfh.split(",");
				int szcge = 0;
				if (lstfh != null) {
					szcge = desCountBySrcCount("ZSPARAM_HOUSE_COUNT",
							lstfh.length, 3);
				}
				if (lstfh != null && lstfh.length > szcge) {
					for (int i = 0; i < szcge; i++) {
						if (i == 0) {
							tempfh = lstfh[i];
						} else {
							tempfh = tempfh + "," + lstfh[i];
						}
					}
					tempfh = tempfh + "等" + lstfh.length + "个";
				}
				qt_fh += tempfh + hhf;
				if (!"1".equals(ConfigHelper.getNameByValue("SFXSFH"))) {
					qt_fh="";
				}
			}

			
			// 其他状况-房屋所在层
			if (units != null) {
				qt_fwszc = "房屋所在层：";
				String tempszc = "";
				for (RealUnit unit : units) {
					House h = (House) unit;
					if (h != null) {
						tempszc += StringHelper.isEmpty(h.getSZC()) ? blank : h.getSZC() +"";
						tempszc+= ",";
					}
				}
				tempszc = StringHelper.isEmpty(tempszc) ? blank : tempszc.substring(0, tempszc.length() - 1);
				// 如果房屋所在层超过3个时，用等几个显示
				String[] lstszc = tempszc.split(",");
				int szcge = 0;
				if (lstszc != null) {
					szcge = desCountBySrcCount("ZSPARAM_HOUSE_COUNT", lstszc.length, 3);
				}
				if (lstszc != null && lstszc.length > szcge) {
					for (int i = 0; i < szcge; i++) {
						if (i == 0) {
							tempszc = lstszc[i];
						} else {
							tempszc = tempszc + "," + lstszc[i];
						}
					}
					tempszc = tempszc + "等" + lstszc.length + "个";
				}
				qt_fwszc += tempszc + hhf;
			}

			// 其他状况-房屋竣工时间
			if (units != null && units.size() > 0) {
				qt_fwjgsj = "";
				String tempjgsj = "";
				House h = (House) units.get(0);
				if (h != null) {
					tempjgsj = StringHelper.FormatByDatetime(h.getJGSJ());
				}
				if (!StringHelper.isEmpty(tempjgsj)) {
					qt_fwjgsj = "房屋竣工时间:" + tempjgsj + hhf;
				}
			}
			
			
			

			// 其他状况-持证方式
			if (qlholders.size() > 1) {
				qt_czfs = "持证方式:" + ConstHelper.getNameByValue("CZFS", rights.getCZFS()) + hhf;
			}
			// 其他状况-持证人
			if (rights.getCZFS().equals(CZFS.GTCZ.Value)) {
				qt_czr = "持证人:";
				if (qlholders != null && qlholders.size() > 0) {
					for (RightsHolder holder : qlholders) {
						if (holder.getISCZR() != null && holder.getISCZR().equals(SF.YES.Value)) {
							qt_czr += holder.getQLRMC() + ",";
						}
					}

				}
				if (qt_czr.equals("持证人:")) {
					qt_czr = "";
				} else {
					qt_czr = qt_czr.substring(0, qt_czr.length() - 1) + hhf;
				}
			}

			// 其他状况-建筑密度和容积率liangc
			String configShowJZMDandRJL = ConfigHelper.getNameByValue("ShowJZMDandRJL");
			if ("1".equals(configShowJZMDandRJL)) {
				if (units != null && units.size() > 0) {
					if (units.get(0).getBDCDYLX().equals(BDCDYLX.H)) {
						House h = (House) units.get(0);
						List<BDCS_SHYQZD_XZ> list_shyqzd_xzs = baseCommonDao
								.getDataList(BDCS_SHYQZD_XZ.class, "BDCDYID='"
										+ h.getZDBDCDYID() + "'");
						String jzmd = "";
						String rjl = "";
						if (list_shyqzd_xzs != null
								&& list_shyqzd_xzs.size() > 0) {
							for (BDCS_SHYQZD_XZ list_shyqzd_xz : list_shyqzd_xzs) {
								jzmd = FormatByDatatype(list_shyqzd_xz
												.getJZMD());
								rjl =  FormatByDatatype(list_shyqzd_xz
												.getRJL());
							}
							qt_jzmd_and_rjl += "建筑密度：" + jzmd + "容积率：" + rjl + hhf;
						}
					} else if (units.get(0).getBDCDYLX().equals(BDCDYLX.SHYQZD)) {

					}
				}
			}
			
			//权利共有人情况
			if (rights != null) {
				if (rights.getGYRQK() !=null) {
					qt_ql_gyrqk ="共有人情况:"+ rights.getGYRQK() + hhf;
				}
			}
			
			
			// 附记
			if (rights != null) {
				if (qlholders != null && qlholders.size() > 0) {
					boolean bShowGYQR = false;
					if (CZFS.FBCZ.Value.equals(rights.getCZFS())) {
						for (RightsHolder qlr : qlholders) {
							if (!GYFS.DYSY.Value.equals(qlr.getGYFS())) {
								bShowGYQR = true;
								break;
							}
						}
					} else if (CZFS.GTCZ.Value.equals(rights.getCZFS())) {
						for (RightsHolder qlr : qlholders) {
							if (GYFS.AFGY.Value.equals(qlr.getGYFS())) {
								bShowGYQR = true;
								break;
							}
						}
					}
					
					if (qlholders.size() <= 1) {
						bShowGYQR = false;
					}
					
					if ("2".equals(ConfigHelper.getNameByValue("ISSHOWALLZJH"))) {
						if(qlholders != null && qlholders.size() > 0){
//							fj += "<table style='text-align:center;width:auto;border:none;margin:10px 0px;'>"
//								+ "<tr>"
//								+ "<td style='text-align:left;'>共有权人</td>";
							fj += "共有权人，";
							if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
//								fj += "<td style='padding:0 15px;'>证件号</td>";
								fj += "证件号";
							}
//							fj += "<td style='padding:0 15px;'>不动产权证号</td>"
//								+ "<td style='padding:0 15px;'>共有情况</td>"
//								+ "</tr>";
							fj += "，不动产权证号，共有情况：";
							for (RightsHolder holder : qlholders) {
//								fj += "<tr>";
//								fj += "<td style='text-align:left;'>" + holder.getQLRMC() + "</td>";	
								fj +=holder.getQLRMC()+",";	
								if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
//									fj += "<td style='padding:0 15px;'>" + holder.getZJH() + "</td>";	
									fj += "，" + holder.getZJH()+",";						
								}
								String qzh=holder.getBDCQZH();
								if(holder.getBDCQZH() == null){
									qzh = "-------";
								}
//								fj += "<td style='padding:0 15px;'>" + qzh + "</td>";
								fj +=  qzh+";";
								if (holder.getGYFS() != null && holder.getGYFS().equals(GYFS.AFGY.Value)) {
//									fj += "<td style='padding:0 15px;'>" + "共有份额 " + holder.getQLBL() + "</td>";	
									fj += "共有份额: ";	
								} else if (!StringHelper.isEmpty(holder.getGYFS()) 
											&& !StringHelper.isEmpty(ConstHelper.getNameByValue("GYFS", holder.getGYFS()))) {
//									fj += "<td style='padding:0 15px;'>" + ConstHelper.getNameByValue("GYFS", holder.getGYFS()) + "</td>";	
									fj += ConstHelper.getNameByValue("GYFS", holder.getGYFS())+":";			
								}
//								fj += "</tr>";
							}
//							fj += "</table>";
						}
					}else if ("3".equals(ConfigHelper.getNameByValue("ISSHOWALLZJH"))) {
						
					}else if (bShowGYQR) {
						fj += "共有权人,";
						if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
							fj += "，证件号";
						}
						fj += "不动产权证号，共有情况:";
						for (RightsHolder holder : qlholders) {
//							fj += "<tr>";
							fj +=holder.getQLRMC()+",";		
							if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
								fj +=holder.getZJH()+",";		
							}
							String qzh = holder.getBDCQZH();
							if(holder.getBDCQZH() == null){
								qzh = "-------";
							}
							fj +=qzh+";";
							if (holder.getGYFS() != null && holder.getGYFS().equals(GYFS.AFGY.Value)) {
								fj +="共有份额: " + holder.getQLBL();		
							} else if (!StringHelper.isEmpty(holder.getGYFS()) 
										&& !StringHelper.isEmpty(ConstHelper.getNameByValue("GYFS", holder.getGYFS()))) {
								fj +=ConstHelper.getNameByValue("GYFS", holder.getGYFS())+":";		
							}
//							fj += "</tr>";
						}
//						fj += "</table>";
					}
				}
				//房屋编号
				if (units != null && units.size() > 0) {
					if ((BDCDYLX.H.equals(units.get(0).getBDCDYLX()) || BDCDYLX.YCH.equals(units.get(0).getBDCDYLX()))
							&& "1".equals(ConfigHelper.getNameByValue("ISSHOW_FWBM"))) {
							String tempfmbm = "";
							for (RealUnit unit : units) {
								House h = (House) unit;
								if (h != null) {
									tempfmbm += StringHelper.isEmpty(h.getFWBM()) ? blank : h.getFWBM() +"";
									tempfmbm+= ",";
								}
							}
							tempfmbm = StringHelper.isEmpty(tempfmbm) ? blank : tempfmbm.substring(0, tempfmbm.length() - 1);
							// 如果房屋编码超过3个时，用等几个显示（根据本地化配置中的所在层显示个数进行配置）
							String[] lstfmbm = tempfmbm.split(",");
							int fmbmge = 0;
							if (lstfmbm != null) {
								fmbmge = desCountBySrcCount("ZSPARAM_HOUSE_COUNT", lstfmbm.length, 3);
							}
							if (lstfmbm != null && lstfmbm.length > fmbmge) {
								for (int i = 0; i < fmbmge; i++) {
									if (i == 0) {
										tempfmbm = lstfmbm[i];
									} else {
										tempfmbm = tempfmbm + "," + lstfmbm[i];
									}
								}
								tempfmbm = tempfmbm + "等" + lstfmbm.length + "个";
							}
							fj += hhf + "房屋编码：" + tempfmbm + hhf;

						}
					}
				
				String showfjlclx = ConfigHelper.getNameByValue("showlclx");
				List<Wfi_ProInst> proinst=baseCommonDao.getDataList(Wfi_ProInst.class, " FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
				if(proinst!=null&&proinst.size()>0){
					Wfi_ProInst profj = proinst.get(0);
					if ("1".equals(showfjlclx)){
						String name = profj.getProdef_Name();
						fj +="业务类型：" + name +hhf;
					}
				}
				fj += StringHelper.isEmpty(rights.getFJ()) ? "" : (rights.getFJ().replaceAll("\r\n|\r|\n|\n\r", hhf).replaceAll(" ", "\u00A0") + hhf);
				
				//房屋详情————后台json写法
				String fuj = ConfigHelper.getNameByValue("jsonfuj");
				if(units.size()>1&&!StringHelper.isEmpty(fuj)){
					//java代码转换成json，循环取值
					JSONArray arr = null;
					try {
						arr = JSONArray.fromObject(fuj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					List<JSONObject> list =new ArrayList<JSONObject>();
					if(arr != null){
						for (int i = 0; i < arr.size(); i++) {
							list.add(arr.getJSONObject(i));
						}
					}
					//order排序
					Collections.sort(list, new Comparator<JSONObject>() {
						@Override
						public int compare(JSONObject fieldi, JSONObject fieldj) {
							Integer orderi=StringHelper.getInt(fieldi.get("ORDER"));
							Integer orderj=StringHelper.getInt(fieldj.get("ORDER"));
							if (orderi>orderj) {
								return 1;
							} else if (orderi==orderj) {
								return 0;
							}
							return -1;
						}
					});
					
					StringBuilder builder_h_detail=new StringBuilder();
					int i=0;
					//名称循环遍历获取
					for(JSONObject field:list){
						if (i==0) {
//							builder_h_detail.append("<table style='text-align:center;width:auto;border:none;table-layout:fixed;'>"
//									+ "<tr>");
							builder_h_detail.append(field.get("TITLE"));
						}else {
							builder_h_detail.append(field.get("TITLE"));
						}
						if( i-1 == list.size()){
//							builder_h_detail.append("</tr>");
						}
						i++;
					}
					
					int fwlx_zz =0;//房屋类型为“住宅”的个数
					int fwlx_qt =0;//房屋类型为“非住宅”的个数
					//循环遍历取数据库值
					for (RealUnit ff : units) {
						i=0;
//						builder_h_detail.append("<tr>");
						for(JSONObject field:list){
						
							String name=field.getString("NAME");
							Object obj="";
							if("SZC/ZCS".equals(name)){
								Object obj_szc=getFieldValueByName("SZC",ff);
								Object obj_zcs=getFieldValueByName("ZCS",ff);
								if(StringHelper.isEmpty(obj_szc)){
									obj_szc="--";
								}
								if(StringHelper.isEmpty(obj_zcs)){
									obj_zcs="--";
								}
								obj=obj_szc+"/"+obj_zcs;
							}else{
								obj=getFieldValueByName(name,ff);
							}
							if("FWJG".equals(name)){
								if(!"----".equals(obj)){
									String obj_dicmc=ConstHelper.getNameByValue("FWJG", StringHelper.formatObject(obj));
									if(StringHelper.isEmpty(obj_dicmc)){
										obj="----";
									}else{
										obj=obj_dicmc;
									}
								}
							}else if("FWLX".equals(name)){
								if(!"----".equals(obj)){
									String obj_dicmc=ConstHelper.getNameByValue("FWLX", StringHelper.formatObject(obj));
									if(StringHelper.isEmpty(obj_dicmc)){
										obj="----";
									}else{
										obj=obj_dicmc;
									}
								}
							}else if("FWXZ".equals(name)){
								if(!"----".equals(obj)){
									String obj_dicmc=ConstHelper.getNameByValue("FWXZ", StringHelper.formatObject(obj));
									if(StringHelper.isEmpty(obj_dicmc)){
										obj="----";
									}else{
										obj=obj_dicmc;
									}
								}
							}else if("FWCB".equals(name)){
								if(!"----".equals(obj)){
									String obj_dicmc=ConstHelper.getNameByValue("FWCB", StringHelper.formatObject(obj));
									if(StringHelper.isEmpty(obj_dicmc)){
										obj="----";
									}else{
										obj=obj_dicmc;
									}
								}
							}else if("GHYT".equals(name)||"FWYT1".equals(name)||"FWYT2".equals(name)||"FWYT3".equals(name)){
								if(!"----".equals(obj)){
									String obj_dicmc=ConstHelper.getNameByValue("FWYT", StringHelper.formatObject(obj));
									if(StringHelper.isEmpty(obj_dicmc)){
										obj="----";
									}else{
										obj=obj_dicmc;
									}
								}
							}
							if (i == 0) {
								builder_h_detail.append("<td nowrap='nowrap' style='text-align:left;'>").append(obj).append("</td>");
							}else {
								builder_h_detail.append("<td style='padding:0 3px;word-break: break-all; word-wrap:break-word;'>").append(obj).append("</td>");
							}
							i++;
						}
//						builder_h_detail.append("</tr>");

						if(qxdm_.indexOf("4504") == 0 ){//广西梧州需求，登记单元为多个户时，证书信息-附记页面显示“住宅类XXX套，非住宅类XXX套”，方便收费
							Object fwlx=getFieldValueByName("FWLX",ff);
							if ("1".equals(fwlx)) 
								fwlx_zz++;
							else 
								fwlx_qt++;
						}
					}
					if(!StringHelper.isEmpty(builder_h_detail)){
						builder_h_detail.append("</table>");
						fj += "房屋详情:";
						fj+=builder_h_detail;
						fj+=hhf;
					}
					if(qxdm_.indexOf("4504") == 0 ){//广西梧州需求，登记单元为多个户时，证书信息-附记页面显示“住宅类XXX套，非住宅类XXX套”，方便收费
						if (fwlx_zz>0) {
							fj += "住宅类"+fwlx_zz+"套"+hhf;
						}
						if (fwlx_qt>0) {
							fj += "非住宅类"+fwlx_qt+"套"+hhf;
						}
					}
				}
				
				//产权来源
				if (units != null && units.size() > 0) {
					if ((BDCDYLX.H.equals(units.get(0).getBDCDYLX()) || BDCDYLX.YCH.equals(units.get(0).getBDCDYLX()))
							&& "6501".equals(qxdm_.length()>4?qxdm_.substring(0,4):qxdm_)) {
							String tempcqly = "";
							List<String> cqlys= new ArrayList<String>();
							for (RealUnit unit : units) {
								House h = (House) unit;
								if (h != null) {
									if(!StringHelper.isEmpty(h.getCQLY()) && !cqlys.contains(h.getCQLY())){
										tempcqly += StringHelper.isEmpty(h.getCQLY()) ? blank : ConstHelper.getNameByValue("CQLY", h.getCQLY()) +"";
										cqlys.add(h.getCQLY());
										tempcqly += ",";
									}
									
								}
							}
							if (tempcqly.length() > 0) {
								tempcqly = StringHelper.isEmpty(tempcqly) ? blank : tempcqly.substring(0, tempcqly.length() - 1);
								// 如果房屋编码超过3个时，用等几个显示（根据本地化配置中的所在层显示个数进行配置）
								String[] lstcqly = tempcqly.split(",");
								int cqlyCount = 0;
								if (lstcqly != null) {
									cqlyCount = desCountBySrcCount("ZSPARAM_HOUSE_COUNT", lstcqly.length, 3);
								}
								if (lstcqly != null && lstcqly.length > cqlyCount) {
									for (int i = 0; i < cqlyCount; i++) {
										if (i == 0) {
											tempcqly = lstcqly[i];
										} else {
											tempcqly = tempcqly + "," + lstcqly[i];
										}
									}
									tempcqly = tempcqly + "等" + lstcqly.length + "个";
								}
							}else {
								tempcqly = blank;
							}
							
							fj += hhf + "产权来源：" + tempcqly + hhf;
						}
					}
				
			}
			
			
			String project_Id = "";
			if (!StringHelper.isEmpty(xmxx)) {
				project_Id = xmxx.getPROJECT_ID();
				String[] projects = project_Id.split("-");
				if (!StringHelper.isEmpty(projects)) {
					if (projects.length == 4) {
						project_Id = projects[2] + "-" + projects[3];
					}
				}
			}
			if (!StringHelper.isEmpty(project_Id) && ("1").equals(ConfigHelper.getNameByValue("ShowYWHInFJ"))) {

				if (!StringHelper.isEmpty(xmxx.getYWLSH()))
					project_Id = xmxx.getYWLSH();
				if (!StringHelper.isEmpty(fj)) {
					fj = "业务编号：" + project_Id + "<br/>" + fj;
				} else {
					fj = "业务编号：" + project_Id;
				}
			}
			
			zsform.setFm_year(fmn);
			zsform.setFm_month(fmy);
			zsform.setFm_day(fmr);
			zsform.setQhjc(qhjc);
			zsform.setNd(nd);
			zsform.setQhmc(qhmc);
			zsform.setZsbh(zshb);
			zsform.setCqzh(cqzh);
			zsform.setQlr(qlrmc);
			zsform.setGyqk(gyqk);
			zsform.setZl(zl);
			zsform.setBdcdyh(bdcdyh);
			zsform.setQllx(qllx);
			zsform.setQlxz(qlxz);
			zsform.setYt(yt);
			zsform.setMj(mj);
			zsform.setSyqx(syqx);
			if ((DJLX.CSDJ.Value.equals(xmxx.getDJLX()) && QLLX.GYJSYDSHYQ_FWSYQ.Value
					.equals(xmxx.getQLLX()))) {
				House h = (House) units.get(0);
				if ("460100".equals(h.getQXDM()) || "460100".equals(ConfigHelper.getNameByValue("XZQHDM"))) {//海南海口要求多个单元一本证，显示总层数，不显示所在层
					 qt_fwszc = "";
					 qt_fwzcs =qt_fwzcs.substring(0,qt_fwzcs.length()-1)+hhf;
				}			
			}
			if(!qxdm_.contains("450300")){
				if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)){
					qt_fwzcs = qt_fwzcs.replace("，", "");
					qt_fwszc = hhf;
				}
			}
			if ("0".equals(ConfigHelper.getNameByValue("ZSPARAM_showGYTDMJ"))) {
				qt_gytdmj="";
			}
			if(qxdm_.contains("451102")){
				zsform.setQlqtzk(qt_fh+ qt_gytdmj+qt_tdsyqmj+ qt_zyjzmj+ qt_ftjzmj + qt_fwjg
						+ qt_fwzcs + qt_fwszc + qt_fwjgsj + qt_czfs + qt_pzmj +qt_czr+ qt_jzmd_and_rjl+qt_ql_gyrqk);
			}else{
				zsform.setQlqtzk(qt_fh+qt_tdsyqmj + qt_gytdmj + qt_zyjzmj + qt_ftjzmj + qt_fwjg
						+ qt_fwzcs + qt_fwszc + qt_fwjgsj + qt_czfs + qt_pzmj +qt_czr+ qt_jzmd_and_rjl);
			}
			
			zsform.setFj(fj);
		}
		//添加义务人ywr
		List<BDCS_SQR> listsqr = baseCommonDao.getDataList(BDCS_SQR.class,
				"xmbh='"+xmxx.getId() + "' ORDER BY SXH");
		if (listsqr != null) {
			String yWR = null;
			for (BDCS_SQR s : listsqr) {
				if (!StringHelper.isEmpty(s.getSQRLB())) {
					if ("2".equals(s.getSQRLB())) {
						yWR= StringHelper.isEmpty(yWR) ? s.getSQRXM() : yWR + "," + s.getSQRXM();
						zsform.setYwr(yWR);
					}
				}
			}
		} 
		
		//添加原不动产权证号
		String ybdcqzh = "";
		List<String> ybdcqzhs = new ArrayList<String>();
		for (RealUnit unit : units) {			
			List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ,
					" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_GZ WHERE BDCDYID='"
							+ unit.getId() + "' AND XMBH='" + xmbh
							+ "') AND XMBH='" + xmbh + "' AND QLLX<>'23'");
			if (qls != null && qls.size() > 0) {				
				if(!StringHelper.isEmpty(qls.get(0).getLYQLID())){
					Rights ql_ls=RightsTools.loadRights(DJDYLY.LS, qls.get(0).getLYQLID());
					if(ql_ls != null){							
						List<RightsHolder> list_qlr_ls=RightsHolderTools.loadRightsHolders(DJDYLY.LS, qls.get(0).getLYQLID());
						if(list_qlr_ls != null && list_qlr_ls.size() > 0){
							for(RightsHolder qlr_ls:list_qlr_ls){
								if(!StringHelper.isEmpty(qlr_ls.getBDCQZH()) && !ybdcqzhs.contains(qlr_ls.getBDCQZH())){
									ybdcqzh= StringHelper.isEmpty(ybdcqzh) ? qlr_ls.getBDCQZH() : ybdcqzh + "," + qlr_ls.getBDCQZH();
									ybdcqzhs.add(qlr_ls.getBDCQZH());
								}
							}
						}
					}
				}
						
			}
		}
		zsform.setYbdcqzh(ybdcqzh);
		
		return zsform;
	}
	
	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			String qhdm = ConfigHelper.getNameByValue("XZQHDM");
			/*
			 * 桂林要求证书附记里的分摊土地使用权面积、专有建筑面积不填时，显示“----”
			 * @a
			 */
			if(qhdm.equals("450300")){
				if("FTTDMJ".equals(fieldName) || "ZYJZMJ".equals(fieldName)){
					if(value.toString().indexOf("0.0") != -1){
						value = "----";
					}
				}
			}
 			if (StringHelper.isEmpty(value) || "null".equals(value)) {
				value = "----";
			}
 			if (value instanceof Date) {
 				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 				value = sdf.format(value);
 				//value = StringHelper.FormatByDate(value);
			}
 			if (value instanceof Double) {
 				value=StringHelper.formatDouble(value);
			}
 			if (value instanceof Integer) {
 				value=StringHelper.formatDouble(value);
			}
			return value;
		} catch (Exception e) {
			return "----";
		}
	}
	
	public String ReplaceEmptyByBlankOnDouble(Double obj) {
		String value = "----";
		String areaFormater = "#######.###";
		if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("AREAFORMATER"))) {
			areaFormater = ConfigHelper.getNameByValue("AREAFORMATER");
		}
		String configAreaJudge = ConfigHelper.getNameByValue("AreaJudge");
		if (!StringHelper.isEmpty(obj) && obj >= 0) {
			if ("1".equals(configAreaJudge) && (obj == 0)) {
				return value;
			}
			DecimalFormat df = new DecimalFormat(areaFormater);
			df.setRoundingMode(RoundingMode.HALF_UP);
			value = df.format(obj);
		}
		return value;
	}
	/**
	 * 获取不动产登记证明
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public Map getBDCDJZM(String xmbh, String zsid) {
		Map<String, String> map = new HashMap<String, String>();
		String yybdcqzh="";
		if ("1".equals(ConfigHelper.getNameByValue("yybdcqzh"))){
			yybdcqzh ="已有";
		}
		boolean comboDJFlag=false;
		// 1.根据zsid获取证书信息
		BDCS_ZS_GZ zs = getZS(zsid);
		// 项目信息
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		// 登记单元
		BDCS_DJDY_GZ djdy_gz = this.getDjdyGzByZs(xmbh, zsid);
		// 不动产单元 类型
		BDCDYLX lx = BDCDYLX.initFrom(djdy_gz.getBDCDYLX());
		// 不动产单元来源
		DJDYLY ly = DJDYLY.initFrom(djdy_gz.getLY());
		// 当前证书关联的权利
		Rights rights = RightsTools.loadRightsByZSID(DJDYLY.GZ, zsid, xmbh);
		// 不动产单元（如果是多个单元合一本证，就是多个单元，如果是每个单元一本证，就是当前证书对应的单元）
		List<RealUnit> units = null;
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value))
		{
			 comboDJFlag=Global.SfComboDJ(xmbh);//带抵押的组合登记
			if(comboDJFlag){
				units = UnitTools.loadUnits(lx,ly,
						" id in (select BDCDYID FROM BDCS_DJDY_GZ WHERE "
								+ ProjectHelper.GetXMBHCondition(xmbh)
								+ " AND DJDYID IN (SELECT DJDYID FROM BDCS_QL_GZ WHERE "
								+ ProjectHelper.GetXMBHCondition(xmbh)
								+ " AND GROUPID="
								+ rights.getGROUPID()
								+ " AND QLLX='"
								+ rights.getQLLX()
								+ "' AND (NVL2(ISCANCEL,1,0)=0 OR ISCANCEL='0' OR ISCANCEL='2')))");
			}else{
				units = UnitTools.loadUnits(lx,ly,
						" id in (select BDCDYID FROM BDCS_DJDY_GZ WHERE "
								+ ProjectHelper.GetXMBHCondition(xmbh)
								+ " AND GROUPID="
								+ djdy_gz.getGROUPID()
								+ " AND DJDYID IN (SELECT DJDYID FROM BDCS_QL_GZ WHERE "
								+ ProjectHelper.GetXMBHCondition(xmbh)
								+ " AND (NVL2(ISCANCEL,1,0)=0 OR ISCANCEL='0' OR ISCANCEL='2')))");
			}
		}
			
		else
			units = UnitTools.loadUnits(lx, ly, "BDCDYID='" + djdy_gz.getBDCDYID() + "'");
		units = SortUnit(units);
		DecimalFormat df = new DecimalFormat("######0.00");	
		// 权利关联的权利人列表
		List<RightsHolder> qlholders = RightsHolderTools.loadRightsHolders(
				DJDYLY.GZ, rights.getId());
		// 证书关联的权利人列表
		
		List<RightsHolder> zsholders = RightsHolderTools
				.loadRightsHoldersByZSID(DJDYLY.GZ, xmbh, zsid);

		// -1,证书编号
		String zsbh = "";
		// 0,不动产权证号
		String bdcqzh = "";
		// 1,封面年
		String fmn = "";
		// 2,封面月
		String fmy = "";
		// 3,封面日
		String fmr = "";
		// 4,省份
		String qhjc = "";
		// 5,地区名称
		String qhmc = "";
		// 6,年度
		String nd = "";
		// 7,产权证号
		String cqzh = "";
		// 8、证明权利或事项
		String zmqlhsx = "";
		// 9、权利人
		String qlr = "";
		// 10、义务人
		String ywr = "";
		// 11、坐落
		String zl = "";
		// 12、不动产单元号
		String bdcdyh = "";
		// 13-0、其他_预告登记种类
		String qt_ygdjzl = "";
		// 13、其他_原不动产权证号
		String qt_ybdcqzh = "";
		String ybdcqzhAll = "";
		// 14、其他_抵押方式
		String qt_dyfs = "";
		// 15、其他_担保债权数额
		String qt_dbzqse = "";
		// 16、其他_债权起止时间
		String qt_zqqzsj = "";
		
		//添加抵押物类型
		String qt_dywlx="";
		// 17、附记
		String fj = "";

		// 空白符
		String blank = "----";
		// 换行符
		String hhf = "<br/>";
		// 空格
		String space = "";

		// 总额债务
		double zwses = 0.0;

		// -1、证书编号
		zsbh = zs.getZSBH();

		// -1证书编号,0不动产权证号,1封面年,2封面月, 3封面日,4省份,5地区名称,6年度,7产权证号
		if (null != zs) {
			bdcqzh = zs.getBDCQZH()== null ?rights.getBDCQZH() : zs.getBDCQZH();//多个单元一本证不重新获取证号时，bdcs_zs_gz无权证号，取ql表的权证号
			zsbh = zs.getZSBH();
			if (!StringUtils.isEmpty(bdcqzh)) {
				List<String> listqzh = StringHelper.MatchBDCZMH(bdcqzh);
				if (listqzh.size() == 4)// 受理页面想查看证书信息时，出错
				{
					qhjc = listqzh.get(0);
					nd = listqzh.get(1);
					qhmc = listqzh.get(2);
					cqzh = listqzh.get(3);
				}
			}
			// 封面上的年，月，日
			Date date = rights.getDJSJ();
			if (date != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				fmn = cal.get(Calendar.YEAR) + "";
				fmy = (cal.get(Calendar.MONTH) + 1) + "";
				fmr = cal.get(Calendar.DATE) + "";
			}
		}

		// 8证明权利或事项
		if (DJLX.YGDJ.Value.equals(xmxx.getDJLX()))// 预告登记
		{
			zmqlhsx = "预告登记";			
		} else if (DJLX.YYDJ.Value.equals(xmxx.getDJLX())) {
			zmqlhsx = "异议登记";
		} else if (QLLX.DIYQ.Value.equals(xmxx.getQLLX())||(!StringHelper.isEmpty(rights)&&QLLX.DIYQ.Value.equals(rights.getQLLX()))) {
			zmqlhsx = "抵押权";
		}
		
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
		String code = "";
		if(flow!=null&&!StringHelper.isEmpty(flow.getHandlername())&&!"XFYDYZXFDYDJHandler".equals(flow.getHandlername())
				&&!"YDYZXDYDJHandler".equals(flow.getHandlername())&&!"ZY_YDYTODY_DJHandler".equals(flow.getHandlername())
				&&!"YDYZXDYDJHandler_LuZhou".equals(flow.getHandlername())&&!"ZY_YDYTODY_DJHandler_LuZhou".equals(flow.getHandlername())&&!"ZY_YGDYTODY_DJHandler".equals(flow.getHandlername())){
			String lyqlid = rights.getLYQLID();
			if (!StringHelper.isEmpty(lyqlid)) {
				Rights rightsxz = RightsTools.loadRights(DJDYLY.LS, lyqlid);
				if (rightsxz != null) {
					if (DJLX.YGDJ.Value.equals(rightsxz.getDJLX())) {
						zmqlhsx = "预告登记";
					}
				}
			}
		}
		
		if(flow!=null&&!StringHelper.isEmpty(flow.getName())){
			code = flow.getName();
			if ("CS215".equals(flow.getName())) {
				zmqlhsx = "抵押权";
			}
		}

		// 9权利人
		for (RightsHolder holder : zsholders) {
			qlr = qlr + holder.getQLRMC() + ",";
		}
		if(!StringHelper.isEmpty(qlr)){
			qlr = qlr.substring(0, qlr.length() - 1);
		}
		// 10义务人--抵押权义务人读取附属权利中抵押人（过滤重复）
		if (QLLX.DIYQ.Value.equals(xmxx.getQLLX())
				|| QLLX.DIYQ.Value.equals(rights.getQLLX())) {
			if (StringHelper.isEmpty(ywr)) {
				List<String> ywrmcs = new ArrayList<String>();
				List<Rights> listrights=new ArrayList<Rights>();
				if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)){
//					if(comboDJFlag){//抵押，多个单元一本证，比如20个单元进来，然后设置了分组是5个5个是一组时的情况，即使是所有单元一本证，加个GROUPID的条件也不影响
						listrights = RightsTools.loadRightsByCondition(
								DJDYLY.GZ, "XMBH='" + xmbh + "' AND QLLX='23' AND GROUPID="+rights.getGROUPID());
//					}
//					else{
//						listrights = RightsTools.loadRightsByCondition(
//								DJDYLY.GZ, "XMBH='" + xmbh + "' AND QLLX='23'");
//					}
				}else{
					 listrights = RightsTools.loadRightsByCondition(
								DJDYLY.GZ, "XMBH='" + xmbh + "' AND QLLX='23'");
				}
				
				if (listrights != null && listrights.size() > 0) {
					for (Rights crights : listrights) {
						if (!StringHelper.isEmpty(crights.getFSQLID())) {
							SubRights subrights = RightsTools.loadSubRights(
									DJDYLY.GZ, crights.getFSQLID());
							if (subrights != null) {
								if (!StringHelper.isEmpty(subrights.getDYR())) {
									String dyr = subrights.getDYR();
									String[] strs_dyr = dyr.split(",");
									for (String str_dyr : strs_dyr) {
										String[] strs_dyrEx = str_dyr
												.split("、");
										for (String str_dyrEx : strs_dyrEx) {
											if (!ywrmcs.contains(str_dyrEx)) {
												ywrmcs.add(str_dyrEx);
											}
										}
									}
								}
							}
						}
					}
				}
				ywr = StringHelper.formatList(ywrmcs, ",");
			}
		} else{
			if(!StringHelper.isEmpty(rights.getFSQLID())){
				SubRights subrights = RightsTools.loadSubRights(
						DJDYLY.GZ, rights.getFSQLID());
				if(subrights!=null){
					ywr = subrights.getYWR();
				}
			}
			if(StringHelper.isEmpty(ywr)){
				//ywr=null
				ywr = "";
				// 预告登记，非抵押类，义务人读取申请人里边的义务人
				List<BDCS_SQR> sqrs = baseCommonDao.getDataList(BDCS_SQR.class,
						"SQRLB='" + SQRLB.YF.Value + "' AND XMBH='" + xmbh + "'");
				for (BDCS_SQR mc : sqrs) {
					ywr += mc.getSQRXM() + ",";
				}
				if(!StringHelper.isEmpty(ywr)){
					ywr = ywr.substring(0, ywr.length() - 1);
				}
			}
		} 
		// 11坐落和 12不动产单元号
		if (units != null && units.size() > 0) {
			zl = units.get(0).getZL();
			bdcdyh = formatBDCDYH(units.get(0).getBDCDYH());
			if (units.size() > 1) {
				zl = zl + "等" + units.size() + "处";
				bdcdyh = bdcdyh + "等" + units.size() + "个";
			}
		}
		// 13-0、其他_预告登记种类
		if (DJLX.YGDJ.Value.equals(xmxx.getDJLX())||DJLX.YGDJ.Value.equals(rights.getDJLX())||"BG_YGandDYQBG_DJHandler".equals(flow.getHandlername())||"GZ007".equals(flow.getName())) {
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ,
					rights.getId());
			if (fsql != null) {
				YGDJLX ygdjlx = YGDJLX.initFrom(Integer.parseInt((StringHelper.isEmpty(fsql.getYGDJZL()) ? "0" : fsql.getYGDJZL())));
				if(ygdjlx!=null){
					qt_ygdjzl = "预告登记的种类：" + ygdjlx.Name + hhf;
				}else{
					qt_ygdjzl = "预告登记的种类：" + blank + hhf;
				}
			}
		}
		// 13其他_原不动产权证号或证明号

		// 判断是否是转移预告登记的抵押预告登记
		boolean bzyygdy = false;
		if (DJLX.YGDJ.Value.equals(rights.getDJLX())
				&& QLLX.DIYQ.Value.equals(rights.getQLLX())
				&& BDCDYLX.H.Value.equals(djdy_gz.getBDCDYLX())) {
			for (RealUnit unit : units) {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ,
						" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_GZ WHERE BDCDYID='"
								+ unit.getId() + "' AND XMBH='" + xmbh
								+ "') AND XMBH='" + xmbh + "' AND QLLX='23'");
				if (qls != null && qls.size() > 0) {
					for (Rights ql : qls) {
						if (StringHelper.isEmpty(ql.getLYQLID())) {
							continue;
						}
						Rights ql_ly = RightsTools.loadRights(DJDYLY.LS,
								ql.getLYQLID());
						if (ql_ly != null) {
							if (QLLX.QTQL.Value.equals(ql_ly.getQLLX()) && DJLX.YGDJ.Value.equals(ql_ly.getDJLX())) {
								bzyygdy = true;
								if (!StringHelper.isEmpty(ql_ly.getBDCQZH())) {
									qt_ybdcqzh += ql_ly.getBDCQZH() + "、";
								}
							}
						} else {
							ql_ly = RightsTools.loadRights(DJDYLY.GZ, ql.getLYQLID());
							if (ql_ly != null && xmxx.getId().equals(ql_ly.getXMBH())) {
								if (QLLX.QTQL.Value.equals(ql_ly.getQLLX()) && DJLX.YGDJ.Value.equals(ql_ly.getDJLX())) {
									bzyygdy = true;
									if (!StringHelper.isEmpty(ql_ly.getBDCQZH())) {
										qt_ybdcqzh += ql_ly.getBDCQZH() + "、";
									}
								}
							}
						}
					}
				}
				ybdcqzhAll = qt_ybdcqzh;
			}
			if (bzyygdy) {
				if (!StringHelper.isEmpty(qt_ybdcqzh)) {
					// 多单元一本证时，抵押单元的不动产权证号多余两个时，显示等
					qt_ybdcqzh = qt_ybdcqzh.substring(0,
							qt_ybdcqzh.length() - 1);
					String[] lstybdcqzh = qt_ybdcqzh.split("、");
					int ygs = 0;
					if (lstybdcqzh != null) {
						ygs = desCountBySrcCount("ZSPARAM_YBDCQZSH_COUNT",
								lstybdcqzh.length, 2);
					}
					if (lstybdcqzh != null && lstybdcqzh.length > ygs) {
						// 获取前两个不动产权证号
						for (int i = 0; i < ygs; i++) {
							if (i == 0) {
								qt_ybdcqzh = lstybdcqzh[i];
							} else {
								qt_ybdcqzh = qt_ybdcqzh + "、" + lstybdcqzh[i];
							}
						}
						qt_ybdcqzh = qt_ybdcqzh + "等" + lstybdcqzh.length + "个";
					}
					qt_ybdcqzh = "已有预告不动产证明号：" + qt_ybdcqzh + hhf;
				}
			}
		}
		if (!bzyygdy) {
			if (QLLX.DIYQ.Value.equals(xmxx.getQLLX()) || QLLX.DIYQ.Value.equals(rights.getQLLX())) {
				if (!"CS013".equals(code)) {//在建工程不获取权证号
					for (RealUnit unit : units) {
						String bdcdyid = unit.getId();
						String condition_gz = " DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='"
								+ bdcdyid
								+ "') AND QLLX IN ('3','4','5','6','7','8','24') AND XMBH='"
								+ xmbh
								+ "'";
						List<Rights> rights_syq = RightsTools.loadRightsByCondition(DJDYLY.GZ, condition_gz);
//					String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
						if (rights_syq == null || rights_syq.size() <= 0) {
							String condition = " DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='"
									+ bdcdyid + "') AND QLLX IN ('3','4','5','6','7','8','24') ";
						/*if ("BG027".equals(Baseworkflow_ID)) {
								rights_syq = RightsTools.loadRightsByCondition(DJDYLY.LS, condition +
								" AND (TO_CHAR(DJSJ, 'yyyy-MM-dd hh24:mi:ss') < '"+ new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss").format(xmxx.getSLSJ())+"' OR DJSJ IS NULL)");
						}else {*/
							rights_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
							//}

						}
						if (rights_syq != null && rights_syq.size() > 0) {
							for (Rights rts : rights_syq) {
								if (!StringHelper.isEmpty(rts.getBDCQZH()) && !qt_ybdcqzh.contains(rts.getBDCQZH())) {
									String syqbdcqzh = rts.getBDCQZH();
									String ybdcqzh = "";
									if (!StringHelper.isEmpty(syqbdcqzh)) {
										if (ConfigHelper.getNameByValue("ZSPARAM_YBDCZSH_CONTAIN") != null) {
											if (syqbdcqzh.contains(ConfigHelper .getNameByValue("ZSPARAM_YBDCZSH_CONTAIN"))
													|| QLLX.JTTDSYQ.Value.equals(rts.getQLLX())
													|| QLLX.JTJSYDSYQ.Value.equals(rts.getQLLX())
													|| QLLX.GYJSYDSHYQ.Value.equals(rts.getQLLX())
													|| QLLX.ZJDSYQ.Value.equals(rts.getQLLX())) {
												ybdcqzh = syqbdcqzh;
											} else {
												if (DJLX.YGDJ.Value.equals(xmxx.getDJLX()) && QLLX.DIYQ.Value.equals(xmxx.getQLLX())) {
													ybdcqzh = ConfigHelper.getNameByValue("ZSPARAM_PREYBDCSZSH_PREFIX")
															+ syqbdcqzh
															+ ConfigHelper.getNameByValue("ZSPARAM_PREYBDCSZSH_SUFFIX");
												} else {
													ybdcqzh = ConfigHelper.getNameByValue("ZSPARAM_YBDCZSH_PREFIX")
															+ syqbdcqzh
															+ ConfigHelper.getNameByValue("ZSPARAM_YBDCZSH_SUFFIX");// 原不动产权证书号
												}
											}
										}
									}
									if (!StringHelper.isEmpty(ybdcqzh)) {
										qt_ybdcqzh += ybdcqzh + "、";
									}
								}
							}
						}
						ybdcqzhAll = qt_ybdcqzh;
					}
				}
				if (!StringHelper.isEmpty(qt_ybdcqzh)) {
					// 多单元一本证时，抵押单元的不动产权证号多余两个时，显示等
					qt_ybdcqzh = qt_ybdcqzh.substring(0,
							qt_ybdcqzh.length() - 1);
					qt_ybdcqzh = qt_ybdcqzh.replace(",", "、");
					String[] lstybdcqzh = qt_ybdcqzh.split("、");
					int ygs = 0;
					if (lstybdcqzh != null) {
						ygs = desCountBySrcCount("ZSPARAM_YBDCQZSH_COUNT",
								lstybdcqzh.length, 2);
					}
					if (lstybdcqzh != null && lstybdcqzh.length > ygs) {
						// 获取前两个不动产权证号
						for (int i = 0; i < ygs; i++) {
							if (i == 0) {
								qt_ybdcqzh = lstybdcqzh[i];
							} else {
								qt_ybdcqzh = qt_ybdcqzh + "、" + lstybdcqzh[i];
							}
						}
						qt_ybdcqzh = qt_ybdcqzh + "等" + lstybdcqzh.length + "个";
					}else {
						///桂林需要房屋宗地一体抵押的情况下，证明号显示宗地权证号 
						//在现有的房地一体抵押流程中，在证明页面上“其他”一栏目前只读取了房屋的产权证号，
						//正确的应把房屋、房屋所关联的宗地的土地证号也读取进来，涉及到的流程有CS034,CS037。
						//涉及到的流程有CS034,CS037
						for (RealUnit realUnit : units) {
							String flowName = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID()).getName(); 
							if(ConfigHelper.getNameByValue("XZQHDM").contains("4503")&&("CS034".equals(flowName)||"CS037".equals(flowName))) {
								String zdbdcqzh  = "";
								if(realUnit!=null) {
									House h = (House)realUnit;
									zdbdcqzh =  getZDBDCZQH(h.getZDBDCDYID());
								}
								if ("".equals(zdbdcqzh)||qt_ybdcqzh.contains(zdbdcqzh)) {
									 
								}else {
									qt_ybdcqzh =  qt_ybdcqzh +"、"+zdbdcqzh+ hhf;
								}
							} 
						} 
					}
					qt_ybdcqzh = yybdcqzh + "产权证书号：" + qt_ybdcqzh + hhf;
				}
				if(BDCDYLX.H.equals(lx)&&ConfigHelper.getNameByValue("XZQHDM").contains("6501")){
					List<String> list_xtdz_str=new ArrayList<String>();
					for(RealUnit unit_hhh:units){
						List<BDCS_XTDZ> list_xtdz=baseCommonDao.getDataList(BDCS_XTDZ.class, "FWBDCDYID='"+unit_hhh.getId()+"' AND YXBZ=1 AND IFSHOWTDZH=0 ");
						if(list_xtdz!=null&&list_xtdz.size()>0){
							for(BDCS_XTDZ xtdz:list_xtdz){
								if(!StringHelper.isEmpty(xtdz.getTDZ())&&!list_xtdz_str.contains(xtdz.getTDZ())){
									list_xtdz_str.add(xtdz.getTDZ());
								}
							}
						}
					}
					
					if(list_xtdz_str!=null&&list_xtdz_str.size()>0){
						if(list_xtdz_str.size()>2){
							qt_ybdcqzh+="土地证号："+StringHelper.formatList(list_xtdz_str.subList(0, 2), "、")+"等"+list_xtdz_str.size()+"本"+hhf;
						}else{
							qt_ybdcqzh+="土地证号："+StringHelper.formatList(list_xtdz_str, "、")+hhf;
						}
					}
				}
			}
		}

		// 14其他_抵押方式和15其他_担保债权数额和16其他_债权起止时间
		if (QLLX.DIYQ.Value.equals(xmxx.getQLLX())
				|| QLLX.DIYQ.Value.equals(rights.getQLLX())) {
			SubRights bdcs_fsql_gz = RightsTools.loadSubRights(DJDYLY.GZ, rights.getFSQLID());
			String configJE2DX = ConfigHelper.getNameByValue("JEisDX");
			if (bdcs_fsql_gz != null ) {
				if (bdcs_fsql_gz.getDYFS() != null
						&& ConstHelper.getNameByValue("DYFS", bdcs_fsql_gz.getDYFS()) != null) {
					qt_dyfs = "抵押方式："+ ConstHelper.getNameByValue("DYFS", bdcs_fsql_gz.getDYFS()) + hhf;
				} else {
					qt_dyfs = "抵押方式：" + blank + hhf;
				}
				String qssj = StringHelper.FormatByDatetime(rights.getQLQSSJ());
				String jssj = StringHelper.FormatByDatetime(rights.getQLJSSJ());
				if (StringHelper.isEmpty(qssj)) {
					qssj = "";
				} else {
					qssj = qssj + "起";
				}
				if (StringHelper.isEmpty(jssj)) {
					jssj = "----";
				}
				if (ConstValue.DYFS.ZGEDY.Value.equals(bdcs_fsql_gz.getDYFS())) {
					qt_zqqzsj = "债权确定期间：" + qssj + jssj + "止" + hhf;
				} else {
					String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
					if (xzqhdm.indexOf("441821") == 0) //广东佛冈
						qt_zqqzsj = "债权起止时间：</br>" + qssj + jssj + "止" + hhf;
					else
						qt_zqqzsj = "债权起止时间：" + qssj + jssj + "止" + hhf;
					
				}
				Double zse = 0.0;
				qt_dbzqse = "担保债权数额：";	
				if (ConstValue.DYFS.ZGEDY.Value.equals(bdcs_fsql_gz.getDYFS())) {
					qt_dbzqse = "最高债权数额：";
				}
				
				if (("1").equals(bdcs_fsql_gz.getDYFS())) {
					if (bdcs_fsql_gz.getBDBZZQSE() != null) {
						zse = bdcs_fsql_gz.getBDBZZQSE();
					}
				} else {
					if (bdcs_fsql_gz.getZGZQSE() != null) {
						zse = bdcs_fsql_gz.getZGZQSE();
					}
				}
				String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
				if("420900".equals(xzqhdm)||"420902".equals(xzqhdm)){
					if("4".equals(bdcs_fsql_gz.getDYWLX())){
						qt_dywlx="抵押类型：在建建筑物抵押";
					}
				}
				String strdw = "元";
				if (bdcs_fsql_gz != null
						&& !StringHelper.isEmpty(bdcs_fsql_gz.getZQDW())) {
					strdw = ConstHelper.getNameByValue("JEDW",
							bdcs_fsql_gz.getZQDW());
				}
				if("1".equals(configJE2DX)){
					if (strdw.equals("万元")) {
						zse = zse * 10000;
					}
					qt_dbzqse += StringHelper.number2CNMontrayUnit( new BigDecimal(zse))+ " " + hhf;
					if (qt_dbzqse.contains("元整")) {
						if (!strdw.equals("万元")) {
							qt_dbzqse = qt_dbzqse.replace("元", strdw);
						}
					}else {
						qt_dbzqse = qt_dbzqse.replace(hhf, " ") + "(" + strdw + ")" + hhf;
					}
				}else{
					qt_dbzqse += df.format(zse) + " " + strdw + hhf;
				}
			}
		}
		// 17附记
		fj = rights.getFJ();
		String project_Id = "";
		if (!StringHelper.isEmpty(xmxx)) {
			if (!StringHelper.isEmpty(xmxx.getYWLSH())) {
				project_Id = xmxx.getYWLSH();
			} else {
				project_Id = xmxx.getPROJECT_ID();
				String[] projects = project_Id.split("-");
				if (!StringHelper.isEmpty(projects)) {
					if (projects.length == 4) {
						project_Id = projects[2] + "-" + projects[3];
					}
				}
			}
		}
		//18 权利面积
		String qxdm = ConfigHelper.getNameByValue("QLMJXX");
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		if("1".equals(qxdm)){
			double qlmj = 0;
			for(int u = 0;u<units.size();u++){
				if(units.get(u).getMJ()!=0){
					qlmj = StringHelper.addDouble(qlmj,units.get(u).getMJ());		
				}		
			}
				if(qlmj!=0){ //liang q
					if ("LD".equals(lx.name())) {//林地的面积单位“亩”
						qt_dbzqse += "权利面积："+qlmj+"亩"+hhf;
					}else {
						qt_dbzqse += "权利面积："+qlmj+pfm+hhf;
					}
				}
		}
		if (!StringHelper.isEmpty(project_Id)&& ("1").equals(ConfigHelper.getNameByValue("ShowYWHInFJ"))) {
			if (!StringHelper.isEmpty(xmxx.getYWLSH()))
				project_Id = xmxx.getYWLSH();
			if (!StringHelper.isEmpty(fj)) {
				fj = "业务编号："
						+ project_Id
						+ "<br/>"
						+ fj.replaceAll("\r\n|\r|\n|\n\r", hhf).replaceAll(" ",
								"\u00A0");
			} else {
				fj = "业务编号：" + project_Id;
			}
		}
		
		String qlrzjh = "";
		if ("1".equals(ConfigHelper.getNameByValue("xzqlrandzjh"))&&"YG001".equals(flow.getName())) {
			for (RightsHolder holder : zsholders) {
				qlrzjh += "<br/>" + holder.getQLRMC() + holder.getZJH();
			}
			fj += qlrzjh;
		}
		String hmj = "";
		if ("1".equals(ConfigHelper.getNameByValue("hymj"))) {
			for (RealUnit unit : units) {
				hmj = "<br/>建筑面积：" + ReplaceEmptyByBlankOnDouble(unit.getMJ()) + pfm;
			}
			fj += hmj;
		}
		map.put("zmqlhsx", zmqlhsx);
		map.put("qlr", qlr);
		map.put("ywr", ywr);
		map.put("zl", zl);
		map.put("bdcdyh", bdcdyh);
		map.put("qt", qt_ygdjzl + qt_ybdcqzh + qt_dyfs + qt_dbzqse + qt_zqqzsj);
		map.put("fj", fj);
		map.put("zsbh", zsbh);
		map.put("sjc", qhjc);
		map.put("nd", nd);
		map.put("qhmc", qhmc);
		map.put("sxh", cqzh);
		map.put("year", fmn);
		map.put("month", fmy);
		map.put("day", fmr);
		map.put("bdcqzh", bdcqzh);
		if(!StringHelper.isEmpty(ybdcqzhAll)){
			map.put("ybdcqzhall", ybdcqzhAll.substring(0, ybdcqzhAll.length()-1));
		}else{
			map.put("ybdcqzhall", "");
		}
		
		//添加原不动产权证号
		String ybdcqzmh = "";
		List<String> ybdcqzmhs = new ArrayList<String>();
		//在建工程不获取权证号
		if (!"CS013".equals(code)) {
			for (RealUnit unit : units) {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ,
						" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_GZ WHERE BDCDYID='"
								+ unit.getId() + "' AND XMBH='" + xmbh
								+ "') AND XMBH='" + xmbh + "' AND QLLX = '23'");
				if (qls != null && qls.size() > 0) {
					if(!StringHelper.isEmpty(qls.get(0).getLYQLID())){
						Rights ql_ls=RightsTools.loadRights(DJDYLY.LS, qls.get(0).getLYQLID());
						if(ql_ls != null){
							List<RightsHolder> list_qlr_ls=RightsHolderTools.loadRightsHolders(DJDYLY.LS, qls.get(0).getLYQLID());
							if(list_qlr_ls != null && list_qlr_ls.size() > 0){
								for(RightsHolder qlr_ls:list_qlr_ls){
									if(!StringHelper.isEmpty(qlr_ls.getBDCQZH()) && !ybdcqzmhs.contains(qlr_ls.getBDCQZH())){
										ybdcqzmh= StringHelper.isEmpty(ybdcqzmh) ? qlr_ls.getBDCQZH() : ybdcqzmh + "," + qlr_ls.getBDCQZH();
										ybdcqzmhs.add(qlr_ls.getBDCQZH());
									}
								}
							}
						}
					}

				}
			}
		}
		map.put("ybdcqzmhs", ybdcqzmh);
		return map;
	}

	/**
	 * 根据项目编号和证书id获取BDCS_DJDY_GZ
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月16日下午8:22:38
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	private BDCS_DJDY_GZ getDjdyGzByZs(String xmbh, String zsid) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" XMBH ='").append(xmbh).append("' ");
		hqlBuilder
				.append(" AND DJDYID IN (select distinct DJDYID from BDCS_QDZR_GZ ");
		hqlBuilder.append(" WHERE ZSID = '").append(zsid).append("' ");
		hqlBuilder.append(" AND XMBH = '").append(xmbh).append("' ) ");
		List<BDCS_DJDY_GZ> list = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,
				hqlBuilder.toString());
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 格式化不动产单元号，中间加空格
	 * 
	 * @Title: formatBDCDYH
	 * @author:liushufeng
	 * @date：2015年11月13日 下午11:33:11
	 * @param bdcdyh
	 * @return
	 */
	private String formatBDCDYH(String bdcdyh) {
		String _bdcdyh = "";
		// 字符串不为null并且不为""
		if (!StringHelper.isEmpty(bdcdyh)) {
			int length = bdcdyh.length();
			if (length > 0 && length <= 6) { // 长度是0-6
				_bdcdyh = bdcdyh;
			} else if (length > 6 && length <= 12) { // 长度是7-12
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6);
				_bdcdyh = s1 + "  " + s2;
			} else if (length > 12 && length <= 19) { // 长度是13-19
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6, 12);
				String s3 = bdcdyh.substring(12);
				_bdcdyh = s1 + " " + s2 + " " + s3;
			} else { // 长度大于19
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6, 12);
				String s3 = bdcdyh.substring(12, 19);
				String s4 = bdcdyh.substring(19, length);
				_bdcdyh = s1 + " " + s2 + " " + s3 + " " + s4;
			}
		}
		return _bdcdyh;
	}

	private List<RealUnit> SortUnit(List<RealUnit> units_old) {
		List<RealUnit> units_new = new ArrayList<RealUnit>();
		if (StringHelper.isEmpty(units_old) || units_old.size() <= 0) {
			return units_old;
		}

		for (RealUnit unit : units_old) {
			double mj = StringHelper.getDouble(unit.getMJ());
			int index = 0;
			String zl = unit.getZL();
			String fh = "";
			if (BDCDYLX.H.equals(unit.getBDCDYLX()) || BDCDYLX.YCH.equals(unit.getBDCDYLX())) {
				House h = (House) unit;
				if (!StringHelper.isEmpty(h)) {
					fh = h.getFH();
				}
			}
			if (StringHelper.isEmpty(zl) || zl.contains("地下室") || zl.contains("车库")
					|| (!StringHelper.isEmpty(fh) && (fh.contains("地下室") || fh.contains("车库")))) {
				if (units_new.size() > 0) {
					for (index = units_new.size() - 1; index >= 0; index--) {
						RealUnit unit1 = units_new.get(index);
						double mj1 = StringHelper.getDouble(unit1.getMJ());
						if (mj1 >= mj) {
							units_new.add(unit);
							break;
						} else {
							String zl1 = unit1.getZL();
							String fh1 = "";
							if (BDCDYLX.H.equals(unit1.getBDCDYLX()) || BDCDYLX.YCH.equals(unit1.getBDCDYLX())) {
								House h = (House) unit1;
								if (!StringHelper.isEmpty(h)) {
									fh1 = h.getFH();
								}
							}
							if (StringHelper.isEmpty(zl1) || zl1.contains("地下室") || zl1.contains("车库")
									|| (!StringHelper.isEmpty(fh1) && (fh1.contains("地下室") || fh1.contains("车库")))) {
								if (index == 0) {
									units_new.add(0, unit);
									break;
								}
								continue;
							} else {
								if (index == units_new.size() - 1) {
									units_new.add(unit);
								} else {
									units_new.add(index + 1, unit);
								}
								break;
							}
						}
					}
				} else {
					units_new.add(unit);
				}
			} else {
				if (units_new.size() == 0) {
					units_new.add(unit);
				} else {
					for (RealUnit unit1 : units_new) {
						double mj1 = StringHelper.getDouble(unit1.getMJ());
						if (mj > mj1) {
							units_new.add(index, unit);
							break;
						} else {
							String zl1 = unit1.getZL();
							String fh1 = "";
							if (BDCDYLX.H.equals(unit1.getBDCDYLX()) || BDCDYLX.YCH.equals(unit1.getBDCDYLX())) {
								House h = (House) unit1;
								if (!StringHelper.isEmpty(h)) {
									fh1 = h.getFH();
								}
							}
							if (StringHelper.isEmpty(zl1) || zl1.contains("地下室") || zl1.contains("车库") 
									|| (!StringHelper.isEmpty(fh1) && (fh1.contains("地下室") || fh1.contains("车库")))) {
								units_new.add(index, unit);
								break;
							}
						}
						index++;
						if (index == units_new.size()) {
							units_new.add(unit);
							break;
						}
					}
				}
			}
		}
		return units_new;
	}

	private String formatDouble(Double d) {
		String value = "----";
		String areaFormater = "#######.###";
		if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("AREAFORMATER"))) {
			areaFormater = ConfigHelper.getNameByValue("AREAFORMATER");
		}
		String configAreaJudge = ConfigHelper.getNameByValue("AreaJudge");
		if (!StringHelper.isEmpty(d) && d >= 0) {
			if ("1".equals(configAreaJudge) && (d == 0)) {
				return "----";
			}
			DecimalFormat df = new DecimalFormat(areaFormater);
			df.setRoundingMode(RoundingMode.HALF_UP);
			value = df.format(d);
			if((d < 1 && value.indexOf("0")==-1)||(d < 1 && value.indexOf("0")!=0)){	
				value = "0"+value;
			}
		}
		return value;
	}

	/**
	 * 通过本地化配置文件的信息及源大小获取实际需要的大小
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月18日下午4:04:27
	 * @param configName
	 * @param srcCount
	 *            ，元个数
	 * @param defaultCount
	 *            ,默认个数
	 * @return
	 */
	private int desCountBySrcCount(String configName, int srcCount,
			int defaultCount) {
		String sdesCount = ConfigHelper.getNameByValue(configName);
		int desCount = StringHelper.getInt(sdesCount);
		if (desCount > srcCount) {
			desCount = srcCount;
		} else if (desCount == 0) {
			desCount = 2;
		}
		return desCount;
	}
	/**
	 * @Description: 长度补全
	 * @Title: calculatePlaces
	 * @Author：赵梦帆
	 * @Data：2016年12月7日 下午3:05:12
	 * @param Oldstr
	 * @param len
	 * @return
	 * @return StringBuilder
	 */
	public static String calculatePlaces(String Oldstr , int len)  
	{
		if(StringHelper.isEmpty(Oldstr))
			return "";
		StringBuilder str = new StringBuilder(Oldstr);
		byte[] tempByte = null;
		try {
			tempByte = Oldstr.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		if(tempByte!=null)
			str.setLength((len*3-tempByte.length)>0?len*3:tempByte.length);
		return str.toString();
	} 
	
	public static String FormatByDatatype(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "----";
		}
	}
	private String getZDBDCZQH(String zdbdcdyid) {
		String zdbdcqzh  = "";
		if(!StringUtils.isEmpty(zdbdcdyid)) {
			//获取了宗地的不动产单元ID，那么就要通过期登记单元ID获取到其权利，然后获取到BDCQZH
			String fulSql = "select wm_concat(to_char(BDCQZH)) ZDBDCQZH  from BDCK.BDCS_QL_XZ where DJDYID=(select DISTINCT DJDYID from  BDCK.bdcs_djdy_XZ where BDCDYID='"+zdbdcdyid+"') AND QLLX IN ('3','5','7') AND (DJLX IS NULL OR DJLX<>'700')";
	 		List<Map> list = baseCommonDao.getDataListByFullSql(fulSql);
	 		if(list!=null&&!list.isEmpty()) {
	 			zdbdcqzh = StringHelper.formatObject(list.get(0).get("ZDBDCQZH"));
	 		}
		}  
		return zdbdcqzh;
	}
}
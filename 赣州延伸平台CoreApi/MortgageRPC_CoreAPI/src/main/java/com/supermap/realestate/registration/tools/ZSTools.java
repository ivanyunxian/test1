package com.supermap.realestate.registration.tools;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.*;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description	：	1.ZSServiceImpl2方法抽取，待版本稳定后迁移，该工具类提供给证书（ZSServiceImpl2）、申请审批表
 * 					2.非静态类方法，不可直接使用：
 * 						若子类中有使用过@Component注解，可使用SuperSpringContext.getContext().getBean(ZSTools.class)获取该工具类，
 * 						否则使用时需要new一个出来
 * @ClassName	：ZSTools
 * @author		：zhaomengfan
 * @date		：2017年9月19日 下午3:55:20
 */
public class ZSTools {

	protected String blank = "----";
	protected String hhf = "<br/>";
	protected String space = "&nbsp;&nbsp;&nbsp;";
	
	public ZSTools() {
		super();
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
	protected int desCountBySrcCount(String configName, int srcCount, int defaultCount) {
		String sdesCount = ConfigHelper.getNameByValue(configName);
		int desCount = StringHelper.getInt(sdesCount);
		if (desCount > srcCount) {
			desCount = srcCount;
		} else if (desCount == 0) {
			desCount = 2;
		}
		return desCount;
	}

	public String GetTDYT(List<RealUnit> units, BDCDYLX lx, String xmbh, ProjectInfo info,DJDYLY ly) {
		CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		String blank = "----";
		String yt = "";
		if (units.get(0).getBDCDYLX().equals(BDCDYLX.H) || units.get(0).getBDCDYLX().equals(BDCDYLX.YCH) ){
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
							tdytmc = ConstHelper.getNameByValue_new("TDYT", h.getFWTDYT().trim(),StringHelper.formatObject(info.getSlsj()));
						}
						if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(h.getFWTDYT())) {
							tdyt += tdytmc + "、";
							list_tdyt.add(h.getFWTDYT());
						}
					} else if ("1".equals(configZDTDYT)) {// 关联宗地主用途
						List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class,
								"BDCDYID='" + h.getZDBDCDYID() + "'");
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
							String tdytmc = ConstHelper.getNameByValue_new("TDYT", tdyt_tdyt.trim(),StringHelper.formatObject(info.getSlsj()));
							if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdyt_tdyt)) {
								tdyt += tdytmc + "、";
								list_tdyt.add(tdyt_tdyt);
							}
						}
					} else if ("2".equals(configZDTDYT)) {// 关联宗地所有用途（、分隔）
						List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class,
								"BDCDYID='" + h.getZDBDCDYID() + "'");
						if (yts != null && yts.size() > 0) {
							for (BDCS_TDYT_XZ yt_xz : yts) {
								if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
									String tdytmc = ConstHelper.getNameByValue_new("TDYT", yt_xz.getTDYT().trim(),StringHelper.formatObject(info.getSlsj()));
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
							tdytmc = ConstHelper.getNameByValue_new("TDYT", h.getFWTDYT().trim(),StringHelper.formatObject(info.getSlsj()));
						}
						if (StringHelper.isEmpty(tdytmc)) {
							List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class,
									"BDCDYID='" + h.getZDBDCDYID() + "'");
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
								tdytmc = ConstHelper.getNameByValue_new("TDYT", tdyt_tdyt.trim(),StringHelper.formatObject(info.getSlsj()));
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
							tdytmc = ConstHelper.getNameByValue_new("TDYT", h.getFWTDYT().trim(),StringHelper.formatObject(info.getSlsj()));
						}

						if (StringHelper.isEmpty(tdytmc)) {
							List<BDCS_TDYT_XZ> yts = baseCommonDao.getDataList(BDCS_TDYT_XZ.class,
									"BDCDYID='" + h.getZDBDCDYID() + "'");
							if (yts != null && yts.size() > 0) {
								for (BDCS_TDYT_XZ yt_xz : yts) {
									if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
										tdytmc = ConstHelper.getNameByValue_new("TDYT", yt_xz.getTDYT().trim(),StringHelper.formatObject(info.getSlsj()));
										if (!StringHelper.isEmpty(tdytmc)
												&& !list_tdyt.contains(yt_xz.getTDYT().trim())) {
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
						String fwytmc = ConstHelper.getNameByValue_new("FWYT", h.getGHYT().trim(),StringHelper.formatObject(info.getSlsj()));
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
				if (info != null && !StringHelper.isEmpty(info.getZdbtn()) && info.getZdbtn()) {
					yt = blank + "/" + fwyt;
				}
			}
		} else if (lx.equals(BDCDYLX.SHYQZD)) {
			List<TDYT> tdytlist_all = new ArrayList<TDYT>();
			for (RealUnit unit : units) {
				UseLand land = (UseLand) unit;
				if (land != null) {
					List<TDYT> tdytlist_part = new ArrayList<TDYT>();
					tdytlist_part = land.getTDYTS();
					tdytlist_all.addAll(tdytlist_part);
				}
			}
			if (tdytlist_all != null && tdytlist_all.size() > 0) {				
				List<String> tdytmclist = new ArrayList<String>();
				for (TDYT tdyt : tdytlist_all) {
					String shyqzdyt = tdyt.getTDYT();
					if (!StringHelper.isEmpty(shyqzdyt)) {
						String tdytmc = ConstHelper.getNameByValue_new("TDYT", shyqzdyt.trim(),StringHelper.formatObject(info.getSlsj()));
						if (!StringHelper.isEmpty(tdytmc) && !tdytmclist.contains(tdytmc)) {
							tdytmclist.add(tdytmc);
						}
					}
				}
				if (tdytmclist != null && tdytmclist.size() > 0) {
					yt = StringHelper.formatList(tdytmclist, "、");
				}
			}
		}else if (lx.equals(BDCDYLX.SYQZD)) {
			List<TDYT> tdytlist_all = new ArrayList<TDYT>();
			for (RealUnit unit : units) {
				OwnerLand land = (OwnerLand) unit;
				if (land != null) {
					List<TDYT> tdytlist_part = new ArrayList<TDYT>();
					//获取所有权宗地的土地用途
					String ytClassName = "";
					if (ly != null ){
						ytClassName = EntityTools.getEntityName("BDCS_TDYT", ly);					
						Class<?> ytClass = EntityTools.getEntityClass(ytClassName);//
						@SuppressWarnings("unchecked")
						List<TDYT> listyts = (List<TDYT>) baseCommonDao.getDataList(ytClass, "BDCDYID='" + unit.getId() + "' ORDER BY SFZYT DESC");
						if (listyts != null && listyts.size() > 0) {
							tdytlist_part = listyts;
						}
						tdytlist_all.addAll(tdytlist_part);
					}
				}
			}
			if (tdytlist_all != null && tdytlist_all.size() > 0) {
				List<String> tdytmclist = new ArrayList<String>();
				for (TDYT tdyt : tdytlist_all) {
					String shyqzdyt = tdyt.getTDYT();
					if (!StringHelper.isEmpty(shyqzdyt)) {
						String tdytmc = ConstHelper.getNameByValue_new("TDYT", shyqzdyt.trim(),StringHelper.formatObject(info.getSlsj()));
						if (!StringHelper.isEmpty(tdytmc) && !tdytmclist.contains(tdytmc)) {
							tdytmclist.add(tdytmc);
						}
					}
				}
				if (tdytmclist != null && tdytmclist.size() > 0) {
					yt = StringHelper.formatList(tdytmclist, "、");
				}
			}
		} else if (BDCDYLX.LD.equals(lx)) {
			List<String> tdytmclist = new ArrayList<String>();
			for (RealUnit unit : units) {
				Forest forest = (Forest) unit;
				String tdytmc = "";
				if (forest != null && !StringHelper.isEmpty(forest.getTDYT())) {
					tdytmc = ConstHelper.getNameByValue_new("TDYT", forest.getTDYT().trim(),StringHelper.formatObject(info.getSlsj()));
					if (!StringHelper.isEmpty(tdytmc) && !tdytmclist.contains(tdytmc)) {
						tdytmclist.add(tdytmc);
					}
				}
			}
			if (tdytmclist != null && tdytmclist.size() > 0) {
				yt = StringHelper.formatList(tdytmclist, "、");
			}
		} else if (BDCDYLX.HY.equals(lx)) {
			List<String> tdytmclist = new ArrayList<String>();
			for (RealUnit unit : units) {
				Sea sea = (Sea) unit;
				String hyyt = "";
				String yhlxa = "";
				String yhlxb = "";
				if (sea != null) {
					if (!StringHelper.isEmpty(sea.getYHLXA())) {
						yhlxa = ConstHelper.getNameByValue_new("HYSYLXA", sea.getYHLXA().trim(),StringHelper.formatObject(info.getSlsj()));
						if (!StringHelper.isEmpty(yhlxa)) {
							hyyt = yhlxa;
						}
					}

					if (!StringHelper.isEmpty(sea.getYHLXB())) {
						yhlxb = ConstHelper.getNameByValue_new("HYSYLXB", sea.getYHLXB().trim(),StringHelper.formatObject(info.getSlsj()));
						if (!StringHelper.isEmpty(yhlxb)) {
							if (StringHelper.isEmpty(hyyt)) {
								hyyt = yhlxb;
							} else {
								hyyt = hyyt + "/" + yhlxb;
							}
						}
					}
				}
				if (!StringHelper.isEmpty(hyyt) && !tdytmclist.contains(hyyt)) {
					tdytmclist.add(hyyt);
				}
			}
			if (tdytmclist != null && tdytmclist.size() > 0) {
				yt = StringHelper.formatList(tdytmclist, "、");
			}
		} else if (BDCDYLX.NYD.equals(lx)) {
			List<String> tdytmclist = new ArrayList<String>();
			for (RealUnit unit : units) {
				AgriculturalLand nyd = (AgriculturalLand) unit;
				String tdytmc = "";
				if (nyd != null && !StringHelper.isEmpty(nyd.getYT())) {
					tdytmc = ConstHelper.getNameByValue_new("TDYT", nyd.getYT().trim(),StringHelper.formatObject(info.getSlsj()));
					if (!StringHelper.isEmpty(tdytmc) && !tdytmclist.contains(tdytmc)) {
						tdytmclist.add(tdytmc);
					}
				}
			}
			if (tdytmclist != null && tdytmclist.size() > 0) {
				yt = StringHelper.formatList(tdytmclist, "、");
			}

		} else if (BDCDYLX.GZW.equals(lx)) {
			if (units != null && units.size() > 0) {
				List<String> tdytmclist = new ArrayList<String>();
				List<TDYT> tdytlist = new ArrayList<TDYT>();

				for (RealUnit unit : units) {
					Structure gzw = (Structure) unit;
					RealUnit landunit = null;
					if (gzw != null && !StringHelper.isEmpty(gzw.getZDBDCDYID())) {
						List<RealUnit> landunits = UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ,
								"XMBH='" + xmbh + "' and BDCDYID='" + gzw.getZDBDCDYID() + "'");
						if (!StringHelper.isEmpty(landunits) && landunits.size() > 0) {
							landunit = landunits.get(0);
						} else {
							landunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, gzw.getZDBDCDYID());
						}
						if (landunit == null) {
							landunit = UnitTools.loadUnit(BDCDYLX.SYQZD, DJDYLY.XZ, gzw.getZDBDCDYID());
						}
						if (landunit != null) {
							if (BDCDYLX.SHYQZD.equals(landunit.getBDCDYLX())) {
								UseLand land = (UseLand) landunit;
								if (land != null) {
									tdytlist = land.getTDYTS();
								}
							}
						}
					}
					// 格式化
					String gzwyt_zd = "";
					String gzwyt_gzw = "";
					if (gzw != null) {
						// gzwyt_zd = ConstHelper.getNameByValue("TDYT",gzw.getGJZWGHYT());
						gzwyt_gzw = "构筑物";
						if (landunit != null) {
							if (tdytlist != null && tdytlist.size() > 0) {
								String gzwyt = tdytlist.get(0).getTDYT();
								if (!StringHelper.isEmpty(gzwyt)) {
									gzwyt_zd = ConstHelper.getNameByValue_new("TDYT", yt.trim(),StringHelper.formatObject(info.getSlsj()));
								}
							}
						}
						if (!StringHelper.isEmpty(gzw.getGJZWGHYT()) && !"null".equals(gzw.getGJZWGHYT())) {
							gzwyt_gzw = ConstHelper.getNameByValue_new("FWYT", gzw.getGJZWGHYT().trim(),StringHelper.formatObject(info.getSlsj()));

						}
					}
					String _yt = gzwyt_zd + "/" + gzwyt_gzw;
					if (!StringHelper.isEmpty(_yt) && !tdytmclist.contains(_yt)) {
						tdytmclist.add(_yt);
					}
				}
				if (tdytmclist != null && tdytmclist.size() > 0) {
					yt = StringHelper.formatList(tdytmclist, "、");
				}
			}
		}
		return yt;
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
	protected String formatBDCDYH(String bdcdyh) {
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

	public String GetQLLX(Rights rights,ProjectInfo info) {
		String qllx = "----";
		String ql_lx = rights.getQLLX();
		if (!StringHelper.isEmpty(ql_lx)) {
			qllx = ConstHelper.getNameByValue_new("QLLX", ql_lx.trim(),StringHelper.formatObject(info.getSlsj()));
		}
		return qllx;
	}

	public String GetZL(List<RealUnit> units, String qxdm_) {
		String zl = "";
		if (units == null || units.size() == 0) {
			return zl;
		}
		int zlgs = desCountBySrcCount("ZSPARAM_ZL_COUNT", units.size(), 1);
		if (units.size() > zlgs) {
			if (qxdm_.indexOf("45") == 0) {// 房地一体转移登记，2个单元发一本证，坐落取的值都是第一个单元的坐落。（广西整合之前梁秦3.0.2做的）
				for (int i = 0; i < zlgs; i++) {
					if (i == 0) {
						zl = units.get(0).getZL();
						if (units.get(0).getBDCDYLX().equals(BDCDYLX.H)) {// 广西
							House h = (House) units.get(0);
							if (!"".equals(h.getBZ()) && h.getBZ() != null) {
								zl = h.getBZ();
							}
						}
					} else {
						zl += "," + units.get(0).getZL(); // 都取第一个（get(0)）
					}
				}
				zl += "等" + units.size() + "处";
			} else {
				for (int i = 0; i < zlgs; i++) {
					if (i == 0) {
						zl = units.get(0).getZL();
					} else {
						zl += "," + units.get(i).getZL();
					}
				}
				zl += "等" + units.size() + "处";
			}
		} else {
			for (int i = 0; i < units.size(); i++) {
				if (i == 0) {
					zl = units.get(0).getZL();
				} else {
					zl += "," + units.get(i).getZL();
				}
			}
		}
		return zl;
	}

	public String GetBDCDYH(List<RealUnit> units) {
		String bdcdyh = "";
		if (units == null || units.size() == 0) {
			return bdcdyh;
		}
		int dygs = desCountBySrcCount("ZSPARAM_BDCDYH_COUNT", units.size(), 1);
		if (units.size() > dygs) {
			for (int i = 0; i < dygs; i++) {
				if (i == 0) {
					bdcdyh = formatBDCDYH(units.get(i).getBDCDYH());
				} else {
					bdcdyh += "," + formatBDCDYH(units.get(i).getBDCDYH());
				}
			}
			bdcdyh += "等" + units.size() + "个";
		} else {
			for (int i = 0; i < units.size(); i++) {
				if (i == 0) {
					bdcdyh = formatBDCDYH(units.get(i).getBDCDYH());
				} else {
					bdcdyh += "," + formatBDCDYH(units.get(i).getBDCDYH());
				}
			}
		}
		return bdcdyh;
	}

	public String GetQLXZ(List<RealUnit> units, BDCDYLX lx, BDCS_XMXX xmxx, ProjectInfo info, WFD_MAPPING mapping) {
		String blank = "----", qlxz = "";
		if (units != null && units.size() > 0) {
			if (units.get(0).getBDCDYLX().equals(BDCDYLX.H) || units.get(0).getBDCDYLX().equals(BDCDYLX.YCH)) { // 证书没有YCH，此处添加不会给原证书造成影响
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
								String tdqlxzmc = ConstHelper.getNameByValue_new("QLXZ", h.getQLXZ().trim(),null);
								if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
									tdqlxz += tdqlxzmc + "、";
								}
							}
						} else if (SF.YES.Value.equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
							UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
									house.getZDBDCDYID());
							if (land != null && !StringHelper.isEmpty(land.getQLXZ())) {
								String tdqlxzmc = ConstHelper.getNameByValue_new("QLXZ", land.getQLXZ().trim(),null);
								if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
									tdqlxz += tdqlxzmc + "、";
								}
							}
						} else if (("2").equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
							if (!StringHelper.isEmpty(h.getQLXZ())) {
								String tdqlxzmc = ConstHelper.getNameByValue_new("QLXZ", h.getQLXZ().trim(),null);
								if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
									tdqlxz += tdqlxzmc + "、";
								}
							} else if (!StringHelper.isEmpty(h.getZDBDCDYID())) {
								UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
										h.getZDBDCDYID());
								if (land != null && !StringHelper.isEmpty(land.getQLXZ())) {
									String tdqlxzmc = ConstHelper.getNameByValue_new("QLXZ", land.getQLXZ().trim(),null);
									if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
										tdqlxz += tdqlxzmc + "、";
									}
								}
							}
						}
						if (!StringHelper.isEmpty(h.getFWXZ())) {
							String fwxzmc = ConstHelper.getNameByValue_new("FWXZ", h.getFWXZ().trim(),null);
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
					if (info != null && !StringHelper.isEmpty(info.getZdbtn()) && info.getZdbtn()) {
						qlxz = blank + "/" + fwqlxz;
					}
				}
			} else if (BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.SYQZD.equals(lx)) {
				List<String> list_qlxz = new ArrayList<String>();
				for (RealUnit unit : units) {
					if ("1".equals(mapping.getCERTMODE())) {
						List<TDYT> tdytlist = new ArrayList<TDYT>();
						UseLand land = (UseLand) unit;
						if (land != null) {
							tdytlist = land.getTDYTS();
							for (TDYT tdyt : tdytlist) {
								String zdqlxz = tdyt.getQLXZName();
								if (!StringHelper.isEmpty(zdqlxz) && !list_qlxz.contains(zdqlxz)) {
									list_qlxz.add(0, zdqlxz);
								}
							}
						}
					} else {
						String zdqlxz = "";
						if (BDCDYLX.SHYQZD.equals(lx)) {
							UseLand land = (UseLand) unit;
							if (land != null) {
								zdqlxz = land.getQLXZ();
							}
						} else if (BDCDYLX.SYQZD.equals(lx)) {
							OwnerLand land = (OwnerLand) unit;
							if (land != null) {
								zdqlxz = land.getQLXZ();
							}
						}
						if (!StringHelper.isEmpty(zdqlxz)) {
							zdqlxz = ConstHelper.getNameByValue_new("QLXZ", zdqlxz.trim(),null);
							if (!StringHelper.isEmpty(zdqlxz) && !list_qlxz.contains(zdqlxz)) {
								list_qlxz.add(0, zdqlxz);
							}
						}
					}
				}
				if (list_qlxz != null && list_qlxz.size() > 0) {
					qlxz = StringHelper.formatList(list_qlxz, "、");
				} else {
					qlxz = "----";
				}
			} else if (BDCDYLX.LD.equals(lx)) {
				List<String> list_qlxz = new ArrayList<String>();
				for (RealUnit unit : units) {
					Forest forest = (Forest) unit;
					String ldqlxz = "----";
					String lz = "----";
					if (forest != null) {
						if (!StringHelper.isEmpty(forest.getQLXZ()))
							ldqlxz = ConstHelper.getNameByValue_new("QLXZ", forest.getQLXZ().trim(),null);
						if (!StringHelper.isEmpty(forest.getLZ()))
							lz = ConstHelper.getNameByValue_new("LZ", forest.getLZ().trim(),null);
					}
					String nr_qlxz = ldqlxz + "/" + lz;
					if (!StringHelper.isEmpty(nr_qlxz) && !list_qlxz.contains(nr_qlxz)) {
						list_qlxz.add(nr_qlxz);
					}
				}
				if (list_qlxz != null && list_qlxz.size() > 0) {
					qlxz = StringHelper.formatList(list_qlxz, "、");
				} else {
					qlxz = "----/----";
				}
				// } else if (BDCDYLX.HY.equals(lx)) {
				// List<String> list_qlxz=new ArrayList<String>();
				// for (RealUnit unit : units) {
				// Sea sea = (Sea) unit;
				// String qdfs = "";
				// if (sea != null && !StringHelper.isEmpty(sea.getQDFS())) {
				// qdfs = ConstHelper.getNameByValue("HYQDFS", sea.getQDFS().trim());
				// if(!StringHelper.isEmpty(qdfs) && !list_qlxz.contains(qdfs)){
				// list_qlxz.add(qdfs);
				// }
				// }
				// }
				// if (list_qlxz != null && list_qlxz.size() > 0) {
				// qlxz = StringHelper.formatList(list_qlxz, "、");
				// }else {
				// qlxz = "----";
				// }
				// }else if (BDCDYLX.NYD.equals(lx)) {
				// List<String> list_qlxz=new ArrayList<String>();
				// for (RealUnit unit : units) {
				// AgriculturalLand nyd = (AgriculturalLand) unit;
				// String nydfs = "";
				// if (nyd != null && !StringHelper.isEmpty(nyd.getQLXZ())) {
				// nydfs = ConstHelper.getNameByValue("QLXZ", nyd.getQLXZ());
				// if(!StringHelper.isEmpty(nydfs) && !list_qlxz.contains(nydfs)){
				// list_qlxz.add(nydfs);
				// }
				// }
				// }
				// if (list_qlxz != null && list_qlxz.size() > 0) {
				// qlxz = StringHelper.formatList(list_qlxz, "、");
				// }else {
				// qlxz = "----";
				// }
				// }else if (BDCDYLX.GZW.equals(lx)) {
				// List<String> list_qlxz=new ArrayList<String>();
				// for (RealUnit unit : units) {
				// Structure gzw = (Structure) unit;
				// if (gzw != null && !StringHelper.isEmpty(gzw.getZDBDCDYID())){
				// RealUnit landunit = null;
				// List<RealUnit> landunits = UnitTools.loadUnits(BDCDYLX.SHYQZD,
				// DJDYLY.GZ, "XMBH='" + xmxx.getId() + "' and BDCDYID='"
				// + gzw.getZDBDCDYID() + "'");
				// if (!StringHelper.isEmpty(landunits) && landunits.size() > 0) {
				// landunit = landunits.get(0);
				// } else {
				// landunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
				// gzw.getZDBDCDYID());
				// }
				// if (landunit == null) {
				// landunit = UnitTools.loadUnit(BDCDYLX.SYQZD, DJDYLY.XZ,
				// gzw.getZDBDCDYID());
				// }
				// if (landunit != null) {
				// String zdqlxz = "";
				// if (BDCDYLX.SHYQZD.equals(landunit.getBDCDYLX())) {
				// UseLand land = (UseLand) landunit;
				// if (land != null) {
				// zdqlxz = land.getQLXZ();
				// }
				// } else if (BDCDYLX.SYQZD.equals(landunit
				// .getBDCDYLX())) {
				// OwnerLand land = (OwnerLand) landunit;
				// if (land != null) {
				// zdqlxz = land.getQLXZ();
				// }
				// }
				// if (!StringHelper.isEmpty(zdqlxz)) {
				// String gzwqlxz = ConstHelper.getNameByValue("QLXZ",zdqlxz.trim());
				// if(!StringHelper.isEmpty(gzwqlxz) && !list_qlxz.contains(gzwqlxz + "/构筑物")){
				// list_qlxz.add(gzwqlxz+ "/构筑物");
				// }
				// }
				// }
				// }
				// if (list_qlxz != null && list_qlxz.size() > 0) {
				// qlxz = StringHelper.formatList(list_qlxz, "、");
				// }else {
				// qlxz = "----/构筑物";
				// }
				// }
			}
		}
		return qlxz;
	}

	public String ReplaceEmptyByBlankOnDoubleForZH(Double obj) {
		String value = blank;
		String areaFormater = "#######.###";
		if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("AREAFORMATER_ZH"))) {
			areaFormater = ConfigHelper.getNameByValue("AREAFORMATER_ZH");
		}
		String configAreaJudge = ConfigHelper.getNameByValue("AreaJudge");
		if (!StringHelper.isEmpty(obj) && obj >= 0) {
			if ("1".equals(configAreaJudge) && (obj == 0)) {
				return value;
			}
			DecimalFormat f = new DecimalFormat(areaFormater);
			f.setRoundingMode(RoundingMode.HALF_UP);
			value = f.format(obj);
			// if(obj < 1.0){ //liangq 当数值小于1时,不显示前面的0问题修正
			// value = "0"+value;
			// }
		}
		return value;
	}

	private String GetPZMJ(List<RealUnit> units, BDCDYLX lx, String pfm) {
		StringBuilder builderPZMJ = new StringBuilder();
		String preStrPZMJ = "批准面积：";
		String preStr = ConfigHelper.getNameByValue("PREZDPZMJ");
		if (!StringHelper.isEmpty(preStr)) {
			preStrPZMJ = preStr;
		}		
		String pzmjValue = GetPZMJValue(units,lx,pfm);
		if (!StringHelper.isEmpty(pzmjValue)) {
			builderPZMJ.append(preStrPZMJ).append(pzmjValue).toString();
			return builderPZMJ.toString();
		}else {
			return null;
		}
	}
	
	private String GetPZMJValue(List<RealUnit> units, BDCDYLX lx, String pfm) {
		CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		StringBuilder builderPZMJ = new StringBuilder();
		double pz = 0.0;	

		if (BDCDYLX.SYQZD.equals(lx)) {
			for (RealUnit unit : units) {
				OwnerLand ownerland = (OwnerLand) unit;
				pz += (!StringHelper.isEmpty(ownerland.getPZMJ()) ? ownerland.getPZMJ() : 0);
			}
			builderPZMJ.append(pz).append(pfm);
		} else if (BDCDYLX.SHYQZD.equals(lx)) {
			for (RealUnit unit : units) {
				UseLand useland = (UseLand) unit;
				pz += (!StringHelper.isEmpty(useland.getPZMJ()) ? useland.getPZMJ() : 0);
			}
			builderPZMJ.append(pz).append(pfm);
		} else if (BDCDYLX.H.equals(lx) || BDCDYLX.YCH.equals(lx)) {
			for (RealUnit unit : units) {
				House house = (House) unit;
				String zdbdcdyid = house.getZDBDCDYID();
				if (!StringHelper.isEmpty(zdbdcdyid)) {
					BDCS_SYQZD_XZ SYQZD_XZ = baseCommonDao.get(BDCS_SYQZD_XZ.class, zdbdcdyid);
					if (SYQZD_XZ != null && !StringHelper.isEmpty(SYQZD_XZ.getPZMJ())) {
						pz += SYQZD_XZ.getPZMJ();
					} else {
						BDCS_SHYQZD_XZ SHYQZD_XZ = baseCommonDao.get(BDCS_SHYQZD_XZ.class, zdbdcdyid);
						if (SHYQZD_XZ != null && !StringHelper.isEmpty(SHYQZD_XZ.getPZMJ())) {
							pz += SHYQZD_XZ.getPZMJ();
						}
					}
				}
			}
			builderPZMJ.append(pz).append(pfm);
		} else {
			return null;
		}
		return builderPZMJ.toString();
	}

	protected String formatDouble(Double d) {
		String areaFormater = "#######.###";
		if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("AREAFORMATER"))) {
			areaFormater = ConfigHelper.getNameByValue("AREAFORMATER");
		}
		String configAreaJudge = ConfigHelper.getNameByValue("AreaJudge");
		if (!StringHelper.isEmpty(d) && d >= 0) {
			if ("1".equals(configAreaJudge) && (d == 0)) {
				return "----";
			}
		}
		DecimalFormat df = new DecimalFormat(areaFormater);
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(d);
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

	public String GetMJ(List<RealUnit> units, BDCDYLX lx, ProjectInfo info, String pfm, String qxdm_,
			WFD_MAPPING mapping, Rights rights) {
		String mj = "";
		if (units != null && units.size() > 0) {
			if ((BDCDYLX.H).equals(lx)) {
				House house = (House) units.get(0);
				if (house != null) {
					Double sumtemp = 0.0;
					// 改为多个单元时多块宗地时，证书页面，宗地的面积汇总
					// UseLand land = (UseLand) UnitTools
					// .loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
					// house.getZDBDCDYID());
					// if (land != null) {
					// temp = land.getZDMJ() == null ? blank
					// : formatDouble(land.getZDMJ());
					// }
					// mj = "共有宗地面积" + temp + "㎡/";
					HashSet<String> has = new HashSet<String>();
					Double sumjzmj = 0.0;
					for (RealUnit unit : units) {
						House h = (House) unit;
						if (h != null) {
							sumjzmj += (h.getSCJZMJ() != null ? h.getSCJZMJ() : 0);
						}
						if (!has.contains(h.getZDBDCDYID()) && !StringHelper.isEmpty(h.getZDBDCDYID())) {
							has.add(h.getZDBDCDYID());
							// 改为多个单元时多块宗地时，证书页面，宗地的面积汇总
							UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, h.getZDBDCDYID());
							if (land != null) {
								sumtemp += land.getZDMJ() == null ? 0 : land.getZDMJ();
							} else {
								land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, h.getZDBDCDYID());
								if (land != null) {
									sumtemp += land.getZDMJ() == null ? 0 : land.getZDMJ();
								}
							}
						}
					}
					if (qxdm_.indexOf("45") == 0) {// 广西需求，分摊或独有土地面积>0时，显示“宗地面积”4个字
						if (house.getFTTDMJ() > 0 || house.getDYTDMJ() > 0) {
							mj = "宗地面积：";
						} else {
							mj = "共有宗地面积：";
						}
					} else {
						mj = "共有宗地面积：";
					}

					if (info != null && !StringHelper.isEmpty(info.getZdbtn()) && info.getZdbtn()) {
						mj += blank + pfm + "/";
					} else {
						if ("220381".equals(qxdm_)) { //吉林公主岭要求宗地面积显示批准面积
							mj += GetPZMJValue(units,lx,pfm);
						}else{
							mj += formatDouble(sumtemp) + pfm + "/";
						}
					}
					mj += "房屋建筑面积:" + formatDouble(sumjzmj) + pfm;
					has.clear();
				}
			} else if (BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.SYQZD.equals(lx)) {
				double zdmj = 0.0;
				for (RealUnit unit : units) {
					zdmj += (!StringHelper.isEmpty(unit.getMJ()) ? unit.getMJ() : 0);
				}
				mj = ReplaceEmptyByBlankOnDouble(zdmj) + pfm;
				if (qxdm_.indexOf("5110") == 0) {
					mj = "宗地面积：" + mj;
				}
				if ("220381".equals(qxdm_)) { //吉林公主岭要求宗地面积显示批准面积
					mj = GetPZMJValue(units,lx,pfm);
				}
			} else if (BDCDYLX.LD.equals(lx)) {
				double ldmj = 0.0;
				for (RealUnit unit : units) {
					ldmj += (!StringHelper.isEmpty(unit.getMJ()) ? unit.getMJ() : 0);
				}
				mj = ReplaceEmptyByBlankOnDouble(ldmj) + "亩";
			}		
			// 依据workflowcode
			if (("1").equals(mapping.getSFADDPZMJ())) {
				String pzmj = GetPZMJ(units, lx, pfm);
				if (!StringHelper.isEmpty(pzmj)) {
					mj += space + space + pzmj;
					if(qxdm_.indexOf("450721") == 0 && rights != null && QLLX.ZJDSYQ.Value.equals(rights.getQLLX())){// 广西灵山要求宅基地只显示批准面积
						mj = pzmj;
					}
				}
			}
		}
		return mj;
	}

	public String ReplaceEmptyByBlankOnDate(Date obj) {
		String value = StringHelper.FormatByDatetime(obj);
		if (StringHelper.isEmpty(value)) {
			value = blank;
		}
		return value;
	}

	public String ReplaceEmptyByBlankOnString(String obj) {
		String value = blank;
		if (!StringHelper.isEmpty(obj) && !"null".equals(obj)) {
			value = StringHelper.formatObject(obj);
		}
		return value;
	}

	public String GetSYQX(List<RealUnit> units, Rights rights, BDCDYLX lx, BDCS_XMXX xmxx, ProjectInfo info,
			String qxdm_) {
		String syqx = "";
		if (units != null && units.size() > 0) {
			if (BDCDYLX.H.equals(lx)) {
				if (!StringHelper.isEmpty(qxdm_) && ("1").equals(ConfigHelper.getNameByValue("GetSYQXFrom"))) {
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
								nr_qllx = ConstHelper.getNameByValue_new("QLLX", "3",StringHelper.formatObject(info.getSlsj()));
							}
							if ("6".equals(xmxx.getQLLX())) {
								nr_qllx = ConstHelper.getNameByValue_new("QLLX", "5",StringHelper.formatObject(info.getSlsj()));
							}
							if ("8".equals(xmxx.getQLLX())) {
								nr_qllx = ConstHelper.getNameByValue_new("QLLX", "7",StringHelper.formatObject(info.getSlsj()));
							}
							syqx = stringBuffer.insert(0, nr_qllx + " ").toString();
						}
					}
					
					if ("140200".equals(ConfigHelper.getNameByValue("XZQHDM"))) {//山西大同——权利时间为空的时候使用房屋的土地使用权时间
						if (qssj.equals(blank) && jssj.equals(blank)) 
							syqx = getYTQX(units,info);
					}					
				} else {				
					syqx = getYTQX(units,info);
				}
			} else if (BDCDYLX.SHYQZD.equals(lx)) {
				List<TDYT> tdytlist = new ArrayList<TDYT>();
				List<String> tdytmclist = new ArrayList<String>();
				for (RealUnit unit : units) {
					UseLand land = (UseLand) unit;
					if (land != null) {
						List<TDYT> tdytlist_part = new ArrayList<TDYT>();
						tdytlist_part = land.getTDYTS();
						tdytlist.addAll(tdytlist_part);
					}
				}
				if (tdytlist != null && tdytlist.size() > 0) {
					if (tdytlist.size() == 1) {
						String qssj = ReplaceEmptyByBlankOnDate(tdytlist.get(0).getQSRQ()) + "起";
						String jssj = ReplaceEmptyByBlankOnDate(tdytlist.get(0).getZZRQ()) + "止";
						syqx = qssj + jssj;
					} else {
						int iii = 0;
						for (TDYT tdyt : tdytlist) {
							iii++;
							if (!StringHelper.isEmpty(tdyt.getTDYTName()) && !tdytmclist.contains(tdyt.getTDYTName())) {
								syqx = syqx + tdyt.getTDYTName() + ":";
							} else {
								continue;
							}
							String qssj = ReplaceEmptyByBlankOnDate(tdyt.getQSRQ()) + "起";
							String jssj = ReplaceEmptyByBlankOnDate(tdyt.getZZRQ()) + "止";

							if (iii == tdytlist.size()) {
								syqx += qssj + jssj;
							} else {
								syqx += qssj + jssj + hhf;
							}
						}
					}
				}
			} else if (BDCDYLX.LD.equals(lx)) {
				String qllxname = ReplaceEmptyByBlankOnString(QLLX.initFrom(rights.getQLLX()).Name);
				if (QLLX.TDCBJYQ_SLLMSYQ.Value.equals(rights.getQLLX())) {
					qllxname = "土地承包经营权";
				}
				if (QLLX.LDSYQ_SLLMSYQ.Value.equals(rights.getQLLX())) {
					qllxname = "林地使用权";
				}
				String qssj = ReplaceEmptyByBlankOnDate(rights.getQLQSSJ()) + "起";
				String jssj = ReplaceEmptyByBlankOnDate(rights.getQLJSSJ()) + "止";
				syqx = qllxname + space + qssj + jssj;
			}
			// else if (BDCDYLX.HY.equals(lx)) {
			// String qssj = ReplaceEmptyByBlankOnDate(rights.getQLQSSJ()) + "起";
			// String jssj = ReplaceEmptyByBlankOnDate(rights.getQLJSSJ()) + "止";
			// syqx = qssj + jssj;
			// } else if (BDCDYLX.NYD.equals(lx)) {
			// String qssj = ReplaceEmptyByBlankOnDate(rights.getQLQSSJ()) + "起";
			// String jssj = ReplaceEmptyByBlankOnDate(rights.getQLJSSJ()) + "止";
			// if(qxdm_.indexOf("450") == 0 ){
			// List<Map> cbsjs = baseCommonDao.getDataListByFullSql("SELECT CBQSSJ,CBJSSJ
			// FROM BDCK.BDCS_NYD_GZ WHERE BDCDYH='" + units.get(0).getBDCDYH()+"'");
			// String cbqssj = ReplaceEmptyByBlankOnDate((Date)cbsjs.get(0).get("CBQSSJ")) +
			// "起";
			// String cbjssj = ReplaceEmptyByBlankOnDate((Date)cbsjs.get(0).get("CBJSSJ")) +
			// "止";
			// syqx = cbqssj + cbjssj;
			// }else{
			// syqx = qssj + jssj;
			// }
			// }else if (BDCDYLX.GZW.equals(lx)) {
			// String qssj = ReplaceEmptyByBlankOnDate(rights.getQLQSSJ()) + "起";
			// String jssj = ReplaceEmptyByBlankOnDate(rights.getQLJSSJ()) + "止";
			// syqx = qssj + jssj;
			//// if(info!=null && !StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
			//// nr_syqx = blank +"起"+ blank+"止";
			//// }
			// }
			if (StringHelper.isEmpty(syqx)) {
				syqx = blank + "起" + blank + "止";
			}

		}
		return syqx;
	}

	protected String getYTQX(List<RealUnit> units, ProjectInfo info){
		
		String ytqx = "";
		for (RealUnit unit : units) {
			House h = (House) unit;
			if (h != null) {
				if (!StringHelper.isEmpty(h.getFWTDYT())) {
					String tdyt = ConstHelper.getNameByValue_new("TDYT", h.getFWTDYT().trim(),StringHelper.formatObject(info.getSlsj()));
					if (!StringHelper.isEmpty(tdyt)) {
						String qssj = "";
						if (h.getTDSYQQSRQ() != null) {
							qssj = StringHelper.FormatByDatetime(h.getTDSYQQSRQ());
							if (info != null && !StringHelper.isEmpty(info.getZdbtn()) && info.getZdbtn()) {
								qssj = blank;
							}
						}
						qssj = StringHelper.isEmpty(qssj) ? blank : qssj;
						String jssj = "";
						if (h.getTDSYQZZRQ() != null) {
							jssj = StringHelper.FormatByDatetime(h.getTDSYQZZRQ());
							if (info != null && !StringHelper.isEmpty(info.getZdbtn()) && info.getZdbtn()) {
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
		
		return ytqx;
		
	}
	/*
	 * 获取持证方式
	 */
	protected String GetCZFS(List<RightsHolder> qlrlist, Rights ql, String qhdm) {
		StringBuilder builderCZFS = new StringBuilder();
		if (qlrlist != null && qlrlist.size() > 1 ){
			if ("460100".equals(qhdm)) {//海口持证方式，都进行显示
				builderCZFS.append("持证方式：").append(
				ConstHelper.getNameByValue_new("CZFS", ql.getCZFS(),null));
			}else{
				builderCZFS.append("持证方式：").append(
				ConstHelper.getNameByValue_new("CZFS", ql.getCZFS(),null));
			}			
		}
		return builderCZFS.toString();
	}

	/*
	 * 获取持证人
	 */
	protected String GetCZR(List<RightsHolder> qlrlist, Rights ql, String qhdm) {
		StringBuilder builderCZR = new StringBuilder();
		if (qlrlist != null && qlrlist.size() > 0 ){
			if ("460100".equals(qhdm)  ) {//海口不区分持证方式，都进行显示
				String czr = "";
				for (RightsHolder qlr : qlrlist) {
					if (SF.YES.Value.equals(qlr.getISCZR())) {
						czr += qlr.getQLRMC() + ",";
					}
				}
				if (!StringHelper.isEmpty(czr)) {
					czr = czr.substring(0, czr.length() - 1);
					builderCZR.append("持证人：").append(czr);
				}
			}else  if (qlrlist.size() > 1 && CZFS.GTCZ.Value.equals(ql.getCZFS()) )  {
				String czr = "";
				for (RightsHolder qlr : qlrlist) {
					if (SF.YES.Value.equals(qlr.getISCZR())) {
						czr += qlr.getQLRMC() + ",";
					}
				}
				if (!StringHelper.isEmpty(czr)) {
					czr = czr.substring(0, czr.length() - 1);
					builderCZR.append("持证人：").append(czr);
				}
			}			
		}		
		return builderCZR.toString();
	}

	/**
	 * 云南开远要求总层数显示最低层和最高层
	 * 
	 * @param units
	 * @return
	 */
	protected String getTotalFloorsKaiyuan(List<RealUnit> units) {
		// 对units按照层数排序
		Collections.sort(units, new Comparator<RealUnit>() {
			@Override
			public int compare(RealUnit o1, RealUnit o2) {
				int i = ((House) o1).getZCS() - ((House) o2).getZCS();
				return i;
			}
		});
		String qt_fwzcs = "房屋总层数:";
		if (units != null && units.size() > 0) {
			House l = (House) units.get(0);
			qt_fwzcs += getTotalFloorsByHouse(l);
			House h = (House) units.get(units.size() - 1);
			qt_fwzcs += this.getTotalFloorsByHouse(h);
		}
		return qt_fwzcs.substring(0, qt_fwzcs.length() - 1) + space;
	}

	private String getTotalFloorsByHouse(House h) {
		String qt_fwzcs = "";
		if ("1".equals(ConfigHelper.getNameByValue("SHOWWAY_FWZCS"))) {
			if (h != null) {
				if (StringHelper.isEmpty(h.getZCS()) || "0".equals(h.getZCS().toString())) {
					qt_fwzcs += blank;
					if ("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))) {
						qt_fwzcs += "(地上层数：----,地下层数：----)";
					}
					qt_fwzcs += "，";
				} else {
					qt_fwzcs += h.getZCS().toString();
					if ("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))) {
						String dscs = "----", dxcs = "----";
						if (!StringHelper.isEmpty(h.getZRZBDCDYID())) {
							RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, h.getZRZBDCDYID());
							if (unit_zrz == null) {
								unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, h.getZRZBDCDYID());
							}
							if (unit_zrz != null) {
								Building building = (Building) unit_zrz;
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDSCS()))
										&& !"0".equals(StringHelper.formatDouble(building.getDSCS()))) {
									dscs = StringHelper.formatDouble(building.getDSCS());
								}
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDXCS()))
										&& !"0".equals(StringHelper.formatDouble(building.getDXCS()))) {
									dxcs = StringHelper.formatDouble(building.getDXCS());
								}
							}
						}
						qt_fwzcs += "(地上层数：" + dscs + ",地下层数：" + dxcs + ")";
					}
					qt_fwzcs += "，";
				}
			} else {
				qt_fwzcs += blank;
				if ("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))) {
					qt_fwzcs += "(地上层数：----,地下层数：----)";
				}
				qt_fwzcs += "，";
			}
		} else {
			if (h != null) {
				if (StringHelper.isEmpty(h.getZCS()) || "0".equals(h.getZCS().toString())) {
					qt_fwzcs += "0";
					if ("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))) {
						qt_fwzcs += "(地上层数：0,地下层数：0)";
					}
					qt_fwzcs += "，";
				} else {
					qt_fwzcs += h.getZCS().toString();
					if ("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))) {
						String dscs = "0", dxcs = "0";
						if (!StringHelper.isEmpty(h.getZRZBDCDYID())) {
							RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, h.getZRZBDCDYID());
							if (unit_zrz == null) {
								unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, h.getZRZBDCDYID());
							}
							if (unit_zrz != null) {
								Building building = (Building) unit_zrz;
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDSCS()))
										&& !"0".equals(StringHelper.formatDouble(building.getDSCS()))) {
									dscs = StringHelper.formatDouble(building.getDSCS());
								}
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDXCS()))
										&& !"0".equals(StringHelper.formatDouble(building.getDXCS()))) {
									dxcs = StringHelper.formatDouble(building.getDXCS());
								}
							}
						}
						qt_fwzcs += "(地上层数：" + dscs + ",地下层数：" + dxcs + ")";
					}
					qt_fwzcs += "，";
				}
			} else {
				qt_fwzcs += "0";
				if ("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))) {
					qt_fwzcs += "(地上层数：0,地下层数：0)";
				}
				qt_fwzcs += "，";
			}
		}
		return qt_fwzcs;
	}

	/**
	 * 获取房号，从原来代码中抽取封装
	 * 
	 * @param units
	 * @return
	 *
	 * @author YuGuowei
	 * @date 2017年9月12日 08:56:40
	 */
	protected String getRoomNo(List<RealUnit> units) {
		String qt_fh;
		qt_fh = "房号:";
		String tempfh = "";
		for (RealUnit unit : units) {
			House h = (House) unit;
			if (h != null) {
				tempfh += StringHelper.isEmpty(h.getFH()) ? blank : h.getFH() + "";
				tempfh += ",";
			}
		}
		tempfh = StringHelper.isEmpty(tempfh) ? blank : tempfh.substring(0, tempfh.length() - 1);
		// 如果房屋所在层超过3个时，用等几个显示
		String[] lstfh = tempfh.split(",");
		int szcge = 0;
		if (lstfh != null) {
			szcge = desCountBySrcCount("ZSPARAM_HOUSE_COUNT", lstfh.length, 3);
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
			qt_fh = "";
		}
		return qt_fh;
	}

	/**
	 * 获取房屋所在层，从原来代码中抽取封装
	 * 
	 * @param units
	 * @return
	 *
	 * @author YuGuowei
	 * @date 2017年9月12日 08:55:42
	 */
	protected String getHouseFloors(List<RealUnit> units) {
		String qt_fwszc;
		qt_fwszc = "房屋所在层:";
		String tempszc = "";
		for (RealUnit unit : units) {
			House h = (House) unit;
			if (h != null) {
				tempszc += StringHelper.isEmpty(h.getSZC()) ? blank : h.getSZC() + "";
				tempszc += ",";
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
		return qt_fwszc;
	}

	/*
	 * 获取预告的产权证明号
	 */
	public String getYGCQBDCZMH(String lyqlid) {
		Rights ql_ls = null;
		String bdcqzh = "";
		if (!StringHelper.isEmpty(lyqlid)) {
			ql_ls = RightsTools.loadRights(DJDYLY.LS, lyqlid);
			if (!"700".equals(ql_ls.getDJLX()) && !StringHelper.isEmpty(ql_ls.getLYQLID())) {
				return getYGCQBDCZMH(ql_ls.getLYQLID());
			}
			bdcqzh = ql_ls.getBDCQZH();
		}
		return bdcqzh;
	}

	/**
	 * 获取房屋总层数，从原来代码中抽取封装
	 * 
	 * @param units
	 * @return
	 *
	 * @author YuGuowei
	 * @date 2017年9月12日 08:55:42
	 */
	protected String getTotalFloors(List<RealUnit> units) {
		String qt_fwzcs = "房屋总层数:";
		if (units != null && units.size() > 0) {
			/*
			 * 不能只取一个单元的总层数，要考虑到多单元的情况，所以总层数要拼接
			 * heks
			 */
			for (int i = 0; i < units.size(); i++) {
				if(i <= 3){
					qt_fwzcs +=  getTotalFloorsByHouse((House) units.get(i));
				}
				else if (i > 3){
					
					qt_fwzcs += getTotalFloorsByHouse((House) units.get(i)) + "等"+ units.size() +"处";
				}
			}
			/*House h = (House) units.get(0);
			qt_fwzcs += getTotalFloorsByHouse(h);*/
		}
		return qt_fwzcs + space;
	}

	public String GetYBDCQZH(List<RealUnit> units, String xmbh) {
		String ybdcqzh = "";
		List<String> ybdcqzhs = new ArrayList<String>();
		for (RealUnit unit : units) {
			List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ,
					" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_GZ WHERE BDCDYID='" + unit.getId() + "' AND XMBH='" + xmbh
							+ "') AND XMBH='" + xmbh + "' AND QLLX<>'23'");
			if (qls != null && qls.size() > 0) {
				if (!StringHelper.isEmpty(qls.get(0).getLYQLID())) {
					Rights lyql = RightsTools.loadRights(DJDYLY.XZ, qls.get(0).getLYQLID());
					if (lyql == null) {
						lyql = RightsTools.loadRights(DJDYLY.LS, qls.get(0).getLYQLID());
					}
					if (lyql != null) {
						List<RightsHolder> list_qlr_ls = RightsHolderTools.loadRightsHolders(DJDYLY.LS,
								qls.get(0).getLYQLID());
						if (list_qlr_ls != null && list_qlr_ls.size() > 0) {
							for (RightsHolder qlr_ls : list_qlr_ls) {
								if (!StringHelper.isEmpty(qlr_ls.getBDCQZH())
										&& !ybdcqzhs.contains(qlr_ls.getBDCQZH())) {
									ybdcqzh = StringHelper.isEmpty(ybdcqzh) ? qlr_ls.getBDCQZH()
											: ybdcqzh + "," + qlr_ls.getBDCQZH();
									ybdcqzhs.add(qlr_ls.getBDCQZH());
								}
							}
						}
					}
				}
			}
		}
		return ybdcqzh;
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
	public static String calculatePlaces(String Oldstr, int len) {
		if (StringHelper.isEmpty(Oldstr))
			return "";
		StringBuilder str = new StringBuilder(Oldstr);
		byte[] tempByte = null;
		try {
			tempByte = Oldstr.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (tempByte != null)
			str.setLength((len * 3 - tempByte.length) > 0 ? len * 3 : tempByte.length);
		return str.toString();
	}

	@SuppressWarnings("unchecked")
	public StringBuilder formatListorString(Object o, String name) {
		StringBuilder resultBuilder = new StringBuilder();
		if (o instanceof String) {
			if (!StringHelper.isEmpty((String) o))
				resultBuilder.append(name).append("：").append(o).append(hhf);
			return resultBuilder;
		}
		if (o instanceof List<?>) {
			List<String> list = (List<String>) o;
			if (list != null && list.size() > 0)
				resultBuilder.append(name).append(StringHelper.formatList(list, "、")).append(hhf);
			else
				resultBuilder.append(name).append(blank).append(hhf);
			return resultBuilder;
		}
		return resultBuilder;

	}

	public static String FormatByDatatype(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "----";
		}
	}

	Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			if (StringHelper.isEmpty(value) || "null".equals(value)) {
				value = "----";
			}
			if (value instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				value = sdf.format(value);
				// value = StringHelper.FormatByDate(value);
			}
			if (value instanceof Double) {
				value = StringHelper.formatDouble(value);
			}
			if (value instanceof Integer) {
				value = StringHelper.formatDouble(value);
			}
			return value;
		} catch (Exception e) {
			return "----";
		}
	}

	public StringBuilder GetFWXQ(List<RealUnit> units, String qxdm_) {
		StringBuilder hDetailBuilder = new StringBuilder();
		String fuj = ConfigHelper.getNameByValue("jsonfuj");
		if (units.size() > 1 && !StringHelper.isEmpty(fuj)) {
			// java代码转换成json，循环取值
			JSONArray arr = null;
			try {
				arr = JSONArray.fromObject(fuj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<JSONObject> list = new ArrayList<JSONObject>();
			if (arr != null) {
				for (int i = 0; i < arr.size(); i++) {
					list.add(arr.getJSONObject(i));
				}
			}
			// order排序
			Collections.sort(list, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject fieldi, JSONObject fieldj) {
					Integer orderi = StringHelper.getInt(fieldi.get("ORDER"));
					Integer orderj = StringHelper.getInt(fieldj.get("ORDER"));
					if (orderi > orderj) {
						return 1;
					} else if (orderi == orderj) {
						return 0;
					}
					return -1;
				}
			});

			StringBuilder builder_h_detail = new StringBuilder();
			int i = 0;
			// 名称循环遍历获取
			int fwlx_zz = 0;// 房屋类型为“住宅”的个数
			int fwlx_qt = 0;// 房屋类型为“非住宅”的个数
			if ("1".equals(ConfigHelper.getNameByValue("PRINTPARAM_ZSDY_SFPDF"))) {
				for (JSONObject field : list) {
					i++;
					builder_h_detail.append(field.get("TITLE"));
					if (i == list.size()) {
						builder_h_detail.append(hhf);
					} else {
						builder_h_detail.append(space + space);
					}
				}

				String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
				String fj = "";
				// 循环遍历取数据库值
				for (RealUnit ff : units) {
					i = 0;
					for (JSONObject field : list) {
						i++;
						String name = field.getString("NAME");
						Object obj = "";
						if ("SZC/ZCS".equals(name)) {
							Object obj_szc = getFieldValueByName("SZC", ff);
							Object obj_zcs = getFieldValueByName("ZCS", ff);
							if (StringHelper.isEmpty(obj_szc)) {
								obj_szc = "--";
							}
							if (StringHelper.isEmpty(obj_zcs)) {
								obj_zcs = "--";
							}
							obj = obj_szc + "/" + obj_zcs;
						} else {
							obj = getFieldValueByName(name, ff);
						}
						if ("FWJG".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWJG", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("FWLX".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWLX", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("FWXZ".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWXZ", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("FWCB".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWCB", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("GHYT".equals(name) || "FWYT1".equals(name) || "FWYT2".equals(name)
								|| "FWYT3".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWYT", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						}
						builder_h_detail.append(obj).toString();
						if (i == list.size()) {
							builder_h_detail.append(hhf);
						} else {
							builder_h_detail.append(space + space + space + space + space);
						}
					}
					if (XZQHDM.indexOf("4504") == 0) {// 广西梧州需求，登记单元为多个户时，证书信息-附记页面显示“住宅类XXX套，非住宅类XXX套”，方便收费
						Object fwlx = getFieldValueByName("FWLX", ff);
						if ("1".equals(fwlx))
							fwlx_zz++;
						else
							fwlx_qt++;
					}
				}
				if (!StringHelper.isEmpty(builder_h_detail)) {
					fj += "<p>房屋详情:" + hhf;
					fj += builder_h_detail;
					fj += "</p>" + hhf;
				}
				if (XZQHDM.indexOf("4504") == 0) {// 广西梧州需求，登记单元为多个户时，证书信息-附记页面显示“住宅类XXX套，非住宅类XXX套”，方便收费
					if (fwlx_zz > 0) {
						fj += "住宅类" + fwlx_zz + "套" + hhf;
					}
					if (fwlx_qt > 0) {
						fj += "非住宅类" + fwlx_qt + "套" + hhf;
					}
				}
				builder_h_detail.append(fj);
			} else {
				for (JSONObject field : list) {
					if (i == 0) {
						builder_h_detail
								.append("<table style='text-align:center;width:auto;border:none;table-layout:fixed;'>"
										+ "<tr>");
						builder_h_detail
								.append("<td style='text-align:left;word-break: break-all; word-wrap:break-word;'>")
								.append(field.get("TITLE")).append("</td>");
					} else {
						builder_h_detail
								.append("<td style='padding:0 3px;word-break: break-all; word-wrap:break-word;'>")
								.append(field.get("TITLE")).append("</td>");
					}
					if (i - 1 == list.size()) {
						builder_h_detail.append("</tr>");
					}
					i++;
				}

				// 循环遍历取数据库值
				for (RealUnit ff : units) {
					i = 0;
					builder_h_detail.append("<tr>");
					for (JSONObject field : list) {

						String name = field.getString("NAME");
						Object obj = "";
						if ("SZC/ZCS".equals(name)) {
							Object obj_szc = getFieldValueByName("SZC", ff);
							Object obj_zcs = getFieldValueByName("ZCS", ff);
							if (StringHelper.isEmpty(obj_szc)) {
								obj_szc = "--";
							}
							if (StringHelper.isEmpty(obj_zcs)) {
								obj_zcs = "--";
							}
							obj = obj_szc + "/" + obj_zcs;
						} else {
							obj = getFieldValueByName(name, ff);
						}
						if ("FWJG".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWJG", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("FWLX".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWLX", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("FWXZ".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWXZ", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("FWCB".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWCB", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						} else if ("GHYT".equals(name) || "FWYT1".equals(name) || "FWYT2".equals(name)
								|| "FWYT3".equals(name)) {
							if (!"----".equals(obj)) {
								String obj_dicmc = ConstHelper.getNameByValue_new("FWYT", StringHelper.formatObject(obj),null);
								if (StringHelper.isEmpty(obj_dicmc)) {
									obj = "----";
								} else {
									obj = obj_dicmc;
								}
							}
						}
						if (i == 0) {
							builder_h_detail.append("<td nowrap='nowrap' style='text-align:left;'>").append(obj)
									.append("</td>");
						} else {
							builder_h_detail
									.append("<td style='padding:0 3px;word-break: break-all; word-wrap:break-word;'>")
									.append(obj).append("</td>");
						}
						i++;
					}
					builder_h_detail.append("</tr>");

					if (qxdm_.indexOf("4504") == 0) {// 广西梧州需求，登记单元为多个户时，证书信息-附记页面显示“住宅类XXX套，非住宅类XXX套”，方便收费
						Object fwlx = getFieldValueByName("FWLX", ff);
						if ("1".equals(fwlx))
							fwlx_zz++;
						else
							fwlx_qt++;
					}
				}
				if (!StringHelper.isEmpty(builder_h_detail)) {
					builder_h_detail.append("</table>");
					hDetailBuilder.append("<p style='margin:5px 0;'>房屋详情:");
					hDetailBuilder.append(builder_h_detail);
					hDetailBuilder.append("</p>").append(hhf);
				}
			}
			if (qxdm_.indexOf("4504") == 0) {// 广西梧州需求，登记单元为多个户时，证书信息-附记页面显示“住宅类XXX套，非住宅类XXX套”，方便收费
				if (fwlx_zz > 0) {
					hDetailBuilder.append("住宅类").append(fwlx_zz).append("套").append(hhf);
				}
				if (fwlx_qt > 0) {
					hDetailBuilder.append("非住宅类").append(fwlx_qt).append("套").append(hhf);
				}
			}
		}
		return hDetailBuilder;
	}

}
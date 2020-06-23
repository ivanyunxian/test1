package com.supermap.realestate.registration.tools;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Floor;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LogicBuilding;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.interfaces.YHYDZB;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * @Description:单元操作类
 * @author 刘树峰
 * @date 2015年6月12日 上午11:53:14
 * @Copyright SuperMap
 */
public class UnitTools {

	/**
	 * 从数据库加载不动产单元
	 * @Title: loadUnit
	 * @author:liushufeng
	 * @date：2015年7月11日 下午11:38:45
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param ly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return RealUnit 不动产单元
	 */
	public static RealUnit loadUnit(BDCDYLX bdcdylx, DJDYLY dyly, String bdcdyid) {
		RealUnit unit = null;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (bdcdylx == null)
			return null;
		if (dyly == null)
			return null;
		Class<?> unitClass = getUnitClass(bdcdylx, dyly);
		if (unitClass == null)
			return null;
		unit = (RealUnit) dao.get(unitClass, bdcdyid);
		if (unit == null) {
			unitClass = getUnitClass(bdcdylx, DJDYLY.initFromByEnumName("GZ"));
			if (unitClass == null)
				return null;
			unit = (RealUnit) dao.get(unitClass, bdcdyid);
		}
		if (unit == null) {
			unitClass = getUnitClass(bdcdylx, DJDYLY.initFromByEnumName("LS"));
			if (unitClass == null)
				return null;
			unit = (RealUnit) dao.get(unitClass, bdcdyid);
		}
		
		// 如果是自然幢，还要取建筑物基本用途
		if (unit instanceof Building) {
			Building building = (Building)unit;
			String ytClassName = getZRZEntityName(dyly);
			Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
			@SuppressWarnings("unchecked")
			List<Building> list = (List<Building>) dao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "'");
			if (list != null && list.size() > 0) {
				String jzwjbyt = StringHelper.formatObject(list.get(0).getJZWJBYT());
				//权籍补录数据有些本来就是用途了，判断是用途代码（数字）再进行取值
				Pattern pattern = Pattern.compile("[0-9]*");
				boolean isNumeric = pattern.matcher(jzwjbyt).matches();
				if (isNumeric) {
					jzwjbyt = ConstHelper.getNameByValue("FWYT", jzwjbyt);
				}
				building.setJZWJBYT(jzwjbyt);
			}
		}
		
		if(unit instanceof Forest) {
			Forest forest = (Forest) unit;
			String ytClassName = getTDYTEntityName(dyly);
			Class<?> ytClass = EntityTools.getEntityClass(ytClassName);//
			@SuppressWarnings("unchecked")
			List<TDYT> listyts = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "' ORDER BY SFZYT DESC");
			if (listyts != null) {
				forest.setTDYTS(listyts);
			}
		}
		
		// 如果是使用权宗地，还要取土地用途
		if (unit instanceof UseLand) {
			UseLand land = (UseLand) unit;
			String ytClassName = getTDYTEntityName(dyly);
			Class<?> ytClass = EntityTools.getEntityClass(ytClassName);//
			@SuppressWarnings("unchecked")
			List<TDYT> listyts = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "' ORDER BY SFZYT DESC");
			if (listyts != null) {
				land.setTDYTS(listyts);
			}
			StringBuilder bdcqzh=new StringBuilder();
			
			List<BDCS_DJDY_XZ> djdys=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid+"'");
			if(djdys!=null&&djdys.size()>0){
				String djdyid=djdys.get(0).getDJDYID();
				if(!StringHelper.isEmpty(djdyid)){
					List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7') AND (DJLX IS NULL OR DJLX<>'700')");
					if(qls!=null&&qls.size()>0){
						 int i=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
						for(Rights ql:qls){
							i++;
							bdcqzh.append(ql.getBDCQZH());
							if(i<qls.size())
								bdcqzh.append(",");
						//bdcqzh=qls.get(0).getBDCQZH();
							String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
							if("420900".equals(xzqdm)||"420902".equals(xzqdm)){
								
							
							List<BDCS_DJDY_GZ> DY=dao.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='"+bdcdyid+"'");
							if(DY.size()>1){
							List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class, "xmbh='" + DY.get(1).getXMBH() +"'");
							//String xmbh=djdy.getXMBH();
							String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.get(0).getPROJECT_ID());
							String sqls = " WORKFLOWCODE='" + workflowcode + "'";
							List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sqls);
							WFD_MAPPING wm=mappings.get(0);
							String lcbm=wm.getWORKFLOWNAME();
								if(lcbm.contains("ZY001")){
									List<BDCS_DJDY_LS> djdyss=dao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='"+bdcdyid+"'");
									if(djdys!=null&&djdyss.size()>0){
										String djdyids=djdyss.get(0).getDJDYID();
										if(!StringHelper.isEmpty(djdyids)){
											List<Rights> qlss=RightsTools.loadRightsByCondition(DJDYLY.LS, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7') AND (DJLX IS NULL OR DJLX<>'700')");
											if(qlss!=null&&qlss.size()>0){
												 int ii=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
												for(Rights qll:qls){
													if("100".equals(qll.getDJLX())){
															ii++;
															bdcqzh.append(qll.getBDCQZH()==null?"":qll.getBDCQZH());
															if(i<qls.size()&&bdcqzh.length()>0)
																bdcqzh.append(",");
														//bdcqzh=qls.get(0).getBDCQZH();
													}
												}
											}
										}
									}
									if(!StringHelper.isEmpty(bdcqzh.toString())){
										land.setZDBDCQZH(bdcqzh.toString());
									}
								}
							}else {
									List<BDCS_DJDY_XZ> djdysi=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid+"'");
									if(djdysi!=null&&djdysi.size()>0){
										String djdyidi=djdysi.get(0).getDJDYID();
										if(!StringHelper.isEmpty(djdyidi)){
											List<Rights> qlsi=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7') AND (DJLX IS NULL OR DJLX<>'700')");
											if(qlsi!=null&&qlsi.size()>0){
												 int is=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
												for(Rights qli:qlsi){
													is++;
													bdcqzh.append(qli.getBDCQZH());
													if(i<qlsi.size())
														bdcqzh.append(",");
												//bdcqzh=qls.get(0).getBDCQZH();
												}
											}
										}
									}
									if(!StringHelper.isEmpty(bdcqzh.toString())){
										land.setZDBDCQZH(bdcqzh.toString());
									}
							}
							}
							if(!StringHelper.isEmpty(bdcqzh.toString())){
								land.setZDBDCQZH(bdcqzh.toString());
							}
						}
					}
				}
			}
		}
		
		
		// 如果是农用地，还要取土地用途
		if (unit instanceof AgriculturalLand) {
			AgriculturalLand land = (AgriculturalLand) unit;
			String ytClassName = getTDYTEntityName(dyly);
			Class<?> ytClass = EntityTools.getEntityClass(ytClassName);//
			@SuppressWarnings("unchecked")
			List<TDYT> listyts = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "' ORDER BY SFZYT DESC");
			if (listyts.size() > 0) {
				land.setTDYTS(listyts);
			}
			StringBuilder bdcqzh=new StringBuilder();

			List<BDCS_DJDY_XZ> djdys=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid+"'");
			if(!StringHelper.isEmpty(djdys)&&djdys.size()>0){
				String djdyid=djdys.get(0).getDJDYID();
				if(!StringHelper.isEmpty(djdyid)){
					List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7','24') AND (DJLX IS NULL OR DJLX<>'700')");
					if(qls!=null&&qls.size()>0){
						int i=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
						for(Rights ql:qls){
							i++;
							bdcqzh.append(ql.getBDCQZH());
							if(i<qls.size())
								bdcqzh.append(",");
							//bdcqzh=qls.get(0).getBDCQZH();
							String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
							if("420900".equals(xzqdm)||"420902".equals(xzqdm)){


								List<BDCS_DJDY_GZ> DY=dao.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='"+bdcdyid+"'");
								if(DY.size()>1){
									List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class, "xmbh='" + DY.get(1).getXMBH() +"'");
									//String xmbh=djdy.getXMBH();
									String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.get(0).getPROJECT_ID());
									String sqls = " WORKFLOWCODE='" + workflowcode + "'";
									List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sqls);
									WFD_MAPPING wm=mappings.get(0);
									String lcbm=wm.getWORKFLOWNAME();
									if(lcbm.contains("ZY001")){
										List<BDCS_DJDY_LS> djdyss=dao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='"+bdcdyid+"'");
										if(djdys!=null&&djdyss.size()>0){
											String djdyids=djdyss.get(0).getDJDYID();
											if(!StringHelper.isEmpty(djdyids)){
												List<Rights> qlss=RightsTools.loadRightsByCondition(DJDYLY.LS, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7','24') AND (DJLX IS NULL OR DJLX<>'700')");
												if(qlss!=null&&qlss.size()>0){
													int ii=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
													for(Rights qll:qls){
														if("100".equals(qll.getDJLX())){
															ii++;
															bdcqzh.append(qll.getBDCQZH()==null?"":qll.getBDCQZH());
															if(i<qls.size()&&bdcqzh.length()>0)
																bdcqzh.append(",");
															//bdcqzh=qls.get(0).getBDCQZH();
														}
													}
												}
											}
										}
										if(!StringHelper.isEmpty(bdcqzh.toString())){
											land.setZDBDCQZH(bdcqzh.toString());
										}
									}
								}else {
									List<BDCS_DJDY_XZ> djdysi=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid+"'");
									if(djdysi!=null&&djdysi.size()>0){
										String djdyidi=djdysi.get(0).getDJDYID();
										if(!StringHelper.isEmpty(djdyidi)){
											List<Rights> qlsi=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7','24') AND (DJLX IS NULL OR DJLX<>'700')");
											if(qlsi!=null&&qlsi.size()>0){
												int is=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
												for(Rights qli:qlsi){
													is++;
													bdcqzh.append(qli.getBDCQZH());
													if(i<qlsi.size())
														bdcqzh.append(",");
													//bdcqzh=qls.get(0).getBDCQZH();
												}
											}
										}
									}
									if(!StringHelper.isEmpty(bdcqzh.toString())){
										land.setZDBDCQZH(bdcqzh.toString());
									}
								}
							}
							if(!StringHelper.isEmpty(bdcqzh.toString())){
								land.setZDBDCQZH(bdcqzh.toString());
							}
						}
					}
				}
			}
		}
						
		// 如果是海域，还要取用海状况、用海用地坐标
		if (unit instanceof Sea) {
			Sea sea = (Sea) unit;
			String yhzkClassName = getYHZKEntityName(dyly);
			Class<?> yhzkClass = EntityTools.getEntityClass(yhzkClassName);//
			@SuppressWarnings("unchecked")
			List<YHZK> listyhzks = (List<YHZK>) dao.getDataList(yhzkClass, "BDCDYID='" + bdcdyid + "'");
			if (listyhzks != null) {
				sea.setYHZKS(listyhzks);
			}
			
			String yhydzbClassName = getYHYDZBEntityName(dyly);
			Class<?> yhydzbClass = EntityTools.getEntityClass(yhydzbClassName);//
			@SuppressWarnings("unchecked")
			List<YHYDZB> listyhydzbs = (List<YHYDZB>) dao.getDataList(yhydzbClass, "BDCDYID='" + bdcdyid + "' ORDER BY XH ");
			if (listyhydzbs != null) {
				sea.setYHYDZBS(listyhydzbs);
			}
		}
		return unit;
	}

	/**
	 * 根据条件加载多个不动产单元
	 * @Title: loadUnits
	 * @author:liushufeng
	 * @date：2015年11月14日 下午8:00:22
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param dyly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param condition
	 *            条件
	 * @return 不动产单元列表
	 */
	public static List<RealUnit> loadUnits(BDCDYLX bdcdylx, DJDYLY dyly, String condition) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (bdcdylx == null)
			return null;
		if (dyly == null)
			return null;
		Class<?> unitClass = getUnitClass(bdcdylx, dyly);
		if (unitClass == null)
			return null;

		@SuppressWarnings("unchecked")
		List<RealUnit> list = (List<RealUnit>) dao.getDataList(unitClass, condition);
		if (bdcdylx.equals(BDCDYLX.SHYQZD)) {
			for (RealUnit unit : list) {
				UseLand land = (UseLand) unit;
				String ytClassName = getTDYTEntityName(dyly);
				Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
				@SuppressWarnings("unchecked")
				List<TDYT> listyts = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + land.getId() + "'");
				if (listyts != null) {
					land.setTDYTS(listyts);
				}
			}
		} else if (bdcdylx.equals(BDCDYLX.LD)) {
			for (RealUnit unit : list) {
				Forest forest = (Forest) unit;
				String ytClassName = getTDYTEntityName(dyly);
				Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
				@SuppressWarnings("unchecked")
				List<TDYT> listyts = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + forest.getId() + "'");
				if (listyts != null) {
					forest.setTDYTS(listyts);
				}
			}
		}

		return list;
	}

	/**
	 * 根据条件加载多个不动产单元
	 * @Title: loadUnits
	 * @author:liushufeng
	 * @date：2016年1月14日 上午12:14:44
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param dyly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param condition
	 *            条件
	 * @param paramMap
	 *            参数
	 * @return List<RealUnit>
	 */
	public static List<RealUnit> loadUnits(BDCDYLX bdcdylx, DJDYLY dyly, String condition, Map<String, String> paramMap) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (bdcdylx == null)
			return null;
		if (dyly == null)
			return null;
		Class<?> unitClass = getUnitClass(bdcdylx, dyly);
		if (unitClass == null)
			return null;

		@SuppressWarnings("unchecked")
		List<RealUnit> list = (List<RealUnit>) dao.getDataList(unitClass, condition, paramMap);
		if (bdcdylx.equals(BDCDYLX.SHYQZD)) {
			for (RealUnit unit : list) {
				UseLand land = (UseLand) unit;
				String ytClassName = getTDYTEntityName(dyly);
				Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
				@SuppressWarnings("unchecked")
				List<TDYT> listyts = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + land.getId() + "'");
				if (listyts != null) {
					land.setTDYTS(listyts);
				}
			}
		} else if (bdcdylx.equals(BDCDYLX.LD)) {
			for (RealUnit unit : list) {
				Forest forest = (Forest) unit;
				String ytClassName = getTDYTEntityName(dyly);
				Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
				@SuppressWarnings("unchecked")
				List<TDYT> listyts = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + forest.getId() + "'");
				if (listyts != null) {
					forest.setTDYTS(listyts);
				}
			}
		}

		return list;
	}

	/**
	 * 删除不动产单元
	 * @Title: deleteUnit
	 * @author:liushufeng
	 * @date：2015年7月12日 上午2:25:08
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param ly
	 *            来源层（GZ,XZ,LS,DC）
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return 返回被删除的不动产单元
	 */
	public static RealUnit deleteUnit(BDCDYLX bdcdylx, DJDYLY ly, String bdcdyid) {
		RealUnit unit = loadUnit(bdcdylx, ly, bdcdyid);
		if (unit != null) {
			CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			dao.deleteEntity(unit);

			// 如果是使用权宗地，还要删除关联的土地用途
			if (unit instanceof UseLand) {
				String ytClassName = getTDYTEntityName(ly);
				Class<?> ytclass = EntityTools.getEntityClass(ytClassName);
				dao.deleteEntitysByHql(ytclass, "BDCDYID='" + bdcdyid + "'");
			}
			// 如果是森林林木，还要删除关联的土地用途
			if (unit instanceof Forest) {
				String ytClassName = getTDYTEntityName(ly);
				Class<?> ytclass = EntityTools.getEntityClass(ytClassName);
				dao.deleteEntitysByHql(ytclass, "BDCDYID='" + bdcdyid + "'");
			}
			// 如果是农用地，还要删除关联的土地用途
			if (unit instanceof AgriculturalLand) {
				String ytClassName = getTDYTEntityName(ly);
				Class<?> ytclass = EntityTools.getEntityClass(ytClassName);
				dao.deleteEntitysByHql(ytclass, "BDCDYID='" + bdcdyid + "'");
			}
			// 如果是海域，还要删除关联的用海状况和用海用地坐标
			if (unit instanceof Sea) {
				// 拷贝用海状况
				if (true) {
					String yhzkClassName = getYHZKEntityName(ly);
					Class<?> yhzkclass = EntityTools.getEntityClass(yhzkClassName);
					dao.deleteEntitysByHql(yhzkclass, "BDCDYID='" + bdcdyid + "'");
				}

				// 拷贝用海坐标
				if (true) {
					String yhydzbClassName = getYHYDZBEntityName(ly);
					Class<?> yhydzbclass = EntityTools.getEntityClass(yhydzbClassName);
					dao.deleteEntitysByHql(yhydzbclass, "BDCDYID='" + bdcdyid + "'");
				}
			}
		}
		return unit;
	}

	/**
	 * 拷贝不动产单元
	 * @Title: copyUnit
	 * @author:liushufeng
	 * @date：2015年7月11日 下午11:53:13
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param from
	 *            来源层（GZ,XZ,LS,DC）
	 * @param to
	 *            目标层（GZ,XZ,LS,DC）
	 * @param fromID
	 *            来源不动产单元ID
	 * @return RealUnit 拷贝出来的新单元
	 */
	public static RealUnit copyUnit(BDCDYLX bdcdylx, DJDYLY from, DJDYLY to, String fromID) {
		RealUnit desUnit = null;
		RealUnit srcUnit = loadUnit(bdcdylx, from, fromID);
		if (srcUnit == null)
			return null;
		desUnit = copyUnit(srcUnit, bdcdylx, to);
		return desUnit;
	}

	/**
	 * 拷贝不动产单元
	 * @Title: copyUnit
	 * @author:liushufeng
	 * @date：2015年7月12日 上午12:15:39
	 * @param srcUnit
	 *            源不动产单元
	 * @param desUnit
	 *            目标不动产单元
	 * @return boolean 成功或失败
	 */
	public static boolean copyUnit(RealUnit srcUnit, RealUnit desUnit) {
		return ObjectHelper.copyObject(srcUnit, desUnit);
	}

	/**
	 * 拷贝不动产单元
	 * @Title: copyUnit
	 * @author:liushufeng
	 * @date：2015年7月12日 上午12:24:46
	 * @param srcUnit
	 *            源不动产单元
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param desLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @return
	 */
	public static RealUnit copyUnit(RealUnit srcUnit, BDCDYLX bdcdylx, DJDYLY desLy) {
		RealUnit desUnit = null;
		// 使用权宗地、森林林木、农用地，使用共用的拷贝单元的方法，连同土地用途也一起拷贝了。
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.LD) || bdcdylx.equals(BDCDYLX.NYD)) {
			desUnit = copyLand(srcUnit, desLy);
			return desUnit;
		}
		//本地化配置：所有权宗地是否启用土地多用途管理 ISOPENTDYT_SYQZD
		if ("1".equals(ConfigHelper.getNameByValue("ISOPENTDYT_SYQZD")) && bdcdylx.equals(BDCDYLX.SYQZD)) {
			desUnit = copyLand(srcUnit, desLy);
			return desUnit;
		}		

		// 使用房屋，使用单独的拷贝房屋的方法，连同层、逻辑幢、自然幢也一起拷贝了。
		if (bdcdylx.equals(BDCDYLX.H)||bdcdylx.equals(BDCDYLX.YCH)) {
			desUnit = copyHouse((House) srcUnit, bdcdylx, srcUnit.getLY(), desLy, true, true, true);
			return desUnit;
		}
		
		// 海域，使用单独的拷贝海域的方法，连同用海状况和用海用地坐标也一起拷贝了。
		if (bdcdylx.equals(BDCDYLX.HY)) {
			Sea _srcSea = (Sea) srcUnit;
			Sea _desSea = copySea(_srcSea, _srcSea.getLY(), desLy, true, true);
			return _desSea;
		}
		
		desUnit = newRealUnit(bdcdylx, desLy);
		if (desUnit == null)
			return null;
		copyUnit(srcUnit, desUnit);
		return desUnit;
	}

	/**
	 * 拷贝房屋（同时判断层、逻辑幢、自然幢）
	 * @Title: copyHouse
	 * @author:liushufeng
	 * @date：2015年7月12日 上午12:55:00
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLY
	 *            目标层（GZ,XZ,LS,DC）
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param copyFloor
	 * @param copyLogicBuilding
	 * @param copyBuilding
	 * @return 新拷贝出来的房屋单元
	 */
	public static House copyHouse(BDCDYLX bdcdylx, DJDYLY fromLy, DJDYLY toLy, String bdcdyid, boolean copyFloor, boolean copyLogicBuilding, boolean copyBuilding) {
		House srcHouse = (House) loadUnit(bdcdylx, fromLy, bdcdyid);
		if (srcHouse == null)
			return null;
		House desHouse = copyHouse(srcHouse, bdcdylx, fromLy, toLy, copyFloor, copyLogicBuilding, copyBuilding);
		return desHouse;
	}

	/**
	 * 拷贝房屋（同时判断层、逻辑幢、自然幢）
	 * @Title: copyHouse
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:59:58
	 * @param srcHouse
	 *            源房屋对象
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLY
	 *            目标层（GZ,XZ,LS,DC）
	 * @param copyFloor
	 *            是否拷贝对应的层
	 * @param copyLogicBuilding
	 *            是否拷贝对应的逻辑幢
	 * @param copyBuilding
	 *            是否拷贝对应的自然幢
	 * @return 新拷贝出来的房屋单元
	 */
	public static House copyHouse(House srcHouse, BDCDYLX bdcdylx, DJDYLY fromLy, DJDYLY toLY, boolean copyFloor, boolean copyLogicBuilding, boolean copyBuilding) {
		return copyHouseInner(srcHouse, bdcdylx, fromLy, toLY, copyFloor, copyLogicBuilding, copyBuilding);
	}

	/**
	 * 拷贝宗海单元（同时判断用海状况、用海坐标）
	 * @Title: copySea
	 * @author:liushufeng
	 * @date：2015年7月12日 下午3:31:54
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param bdcdyid
	 *            来源不动产单元ID
	 * @param copyUseInfo
	 *            是否拷贝用海状况
	 * @param copyCoordinate
	 *            是否拷贝用海坐标
	 * @return 新拷贝出来的宗海单元
	 */
	public static Sea copySea(DJDYLY fromLy, DJDYLY toLy, String bdcdyid, boolean copyUseInfo, boolean copyCoordinate) {
		Sea _srcSea = (Sea) loadUnit(BDCDYLX.HY, fromLy, bdcdyid);
		if (_srcSea == null)
			return null;
		Sea _desSea = copySea(_srcSea, fromLy, toLy, copyUseInfo, copyCoordinate);
		return _desSea;
	}

	/**
	 * 拷贝宗海单元（同时判断用海状况、用海坐标）
	 * @Title: copySea
	 * @author:liushufeng
	 * @date：2015年7月12日 下午3:29:27
	 * @param srcSea
	 *            源宗海单元
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param copyUseInfo
	 *            是否拷贝用海状况
	 * @param copyCoordinate
	 *            是否拷贝用海坐标
	 * @return 新拷贝出来的宗海单元
	 */
	public static Sea copySea(Sea srcSea, DJDYLY fromLy, DJDYLY toLy, boolean copyUseInfo, boolean copyCoordinate) {

		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);

		// 拷贝宗海单元
		BDCDYLX bdcdylx = BDCDYLX.HY;// 海域
		Sea desSea = (Sea) newRealUnit(bdcdylx, toLy);
		if (desSea == null)
			return null;
		copyUnit(srcSea, desSea);

		// 拷贝用海状况
		if (copyUseInfo) {
			String srcYhzkClassName = getYHZKEntityName(fromLy);
			String desYhzkClassName = getYHZKEntityName(toLy);

			String hqlCondition = "BDCDYID='" + srcSea.getId() + "'";
			// 先删除
			EntityTools.deleteEntities(desYhzkClassName, hqlCondition);
			// 再拷贝
			List<YHZK> desListYHZK = EntityTools.copyObjects(srcYhzkClassName, desYhzkClassName, "BDCDYID='" + srcSea.getId() + "'");
			if (desListYHZK != null && desListYHZK.size() > 0) {
				for (YHZK yhzk : desListYHZK) {
					yhzk.setId((String) SuperHelper.GeneratePrimaryKey());
					dao.save(yhzk);
				}
			}
		}

		// 拷贝用海坐标
		if (copyCoordinate) {
			String srcYhydzbClassName = getYHYDZBEntityName(fromLy);
			String desYhydzbClassName = getYHYDZBEntityName(toLy);

			String hqlCondition = "BDCDYID='" + srcSea.getId() + "'";
			// 先删除
			EntityTools.deleteEntities(desYhydzbClassName, hqlCondition);
			// 再拷贝
			List<YHYDZB> desListYHYDZB = EntityTools.copyObjects(srcYhydzbClassName, desYhydzbClassName, "BDCDYID='" + srcSea.getId() + "'");
			if (desListYHYDZB != null && desListYHYDZB.size() > 0) {
				for (YHYDZB yhydzb : desListYHYDZB) {
					yhydzb.setId((String) SuperHelper.GeneratePrimaryKey());
					dao.save(yhydzb);
				}
			}
		}

		return desSea;
	}

	/**
	 * 拷贝使用权宗地，连同土地用途也一起拷贝
	 * @Title: copyLand
	 * @author:liushufeng
	 * @date：2015年10月27日 上午11:03:17
	 * @param srcUnit
	 * @param desLY
	 * @return
	 */
	private static RealUnit copyLand(RealUnit srcUnit, DJDYLY desLY) {		
		RealUnit desUnit = newRealUnit(srcUnit.getBDCDYLX(), desLY);
		if (desUnit == null)
			return null;
		copyUnit(srcUnit, desUnit);

		// 拷贝土地用途
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		DJDYLY srcly = srcUnit.getLY();
		String srcYtClassName = getTDYTEntityName(srcly);
		String desYtClassName = getTDYTEntityName(desLY);

		String hqlCondition = "BDCDYID='" + srcUnit.getId() + "'";
		// 先删除
		EntityTools.deleteEntities(desYtClassName, hqlCondition);
		// 再拷贝
		List<TDYT> desListTDYT = EntityTools.copyObjects(srcYtClassName, desYtClassName, "BDCDYID='" + srcUnit.getId() + "'");
		if (desListTDYT != null && desListTDYT.size() > 0) {
			for (TDYT tdyt : desListTDYT) {
				//tdyt.setId((String) SuperHelper.GeneratePrimaryKey());
				tdyt.setXMBH(srcUnit.getXMBH());
				dao.save(tdyt);
			}
		}
		return desUnit;
	}

	/**
	 * 拷贝房屋内部方法
	 * @Title: copyHouseInner
	 * @author:liushufeng
	 * @date：2015年9月10日 下午8:40:09
	 * @param srcHouse
	 * @param bdcdylx
	 * @param fromLy
	 * @param toLY
	 * @param copyFloor
	 * @param copyLogicBuilding
	 * @param copyBuilding
	 * @return
	 */
	private static House copyHouseInner(House srcHouse, BDCDYLX bdcdylx, DJDYLY fromLy, DJDYLY toLY, boolean copyFloor, boolean copyLogicBuilding, boolean copyBuilding) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		// 拷贝房屋
		House desHouse = (House) newRealUnit(bdcdylx, toLY);
		if (desHouse == null)
			return null;
		copyUnit(srcHouse, desHouse);

		// 拷贝预测层
		if (copyFloor && !StringHelper.isEmpty(srcHouse.getCID())) {
			String _desFloorEntityName = getFloorEntityName(bdcdylx, toLY);
			String _srcFloorEntityName = getFloorEntityName(bdcdylx, fromLy);
			Object _desFloor = EntityTools.copyObjectIfNotExists(_srcFloorEntityName, _desFloorEntityName, srcHouse.getCID());
			if (_desFloor != null) {
				dao.save(_desFloor);
			}
		}

		// 逻辑预测幢
		if (copyLogicBuilding && !StringHelper.isEmpty(srcHouse.getLJZID())) {
			String _desLogicBuildingEntityName = getLogicBuildingEntityName(bdcdylx, toLY);
			String _srcLogicBuildingEntityName = getLogicBuildingEntityName(bdcdylx, fromLy);
			Object _desLogicBuilding = EntityTools.copyObjectIfNotExists(_srcLogicBuildingEntityName, _desLogicBuildingEntityName, srcHouse.getLJZID());
			if (_desLogicBuilding != null) {
				dao.save(_desLogicBuilding);
			}
		}

		// 自然幢
		if (copyBuilding && !StringHelper.isEmpty(srcHouse.getZRZBDCDYID())) {
			String _desBuildingEntityName = getBuildingEntityName(bdcdylx, toLY);
			String _srcBuildingEntityName = getBuildingEntityName(bdcdylx, fromLy);
			Object _desBuilding = EntityTools.copyObjectIfNotExists(_srcBuildingEntityName, _desBuildingEntityName, srcHouse.getZRZBDCDYID());
			if (_desBuilding != null) {
				dao.save(_desBuilding);
			}
		}
		return desHouse;
	}

	/**
	 * 获取层的实体名
	 * @Title: getFloorEntityName
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:29:05
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param ly
	 *            来源
	 * @return
	 */
	private static String getFloorEntityName(BDCDYLX bdcdylx, DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_C", ly);
		else
			_entityName = "DCS_C_GZ";
		if (bdcdylx.equals(BDCDYLX.YCH) || bdcdylx.equals(BDCDYLX.YCZRZ)) {
			_entityName += "Y";
		}
		return _entityName;
	}

	/**
	 * 获取逻辑幢实体名
	 * @Title: getLogicBuildingEntityName
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:37:43
	 * @param bdcdylx
	 * @param ly
	 * @return
	 */
	private static String getLogicBuildingEntityName(BDCDYLX bdcdylx, DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_LJZ", ly);
		else
			_entityName = "DCS_LJZ_GZ";
		if (bdcdylx.equals(BDCDYLX.YCH) || bdcdylx.equals(BDCDYLX.YCZRZ)) {
			_entityName += "Y";
		}
		return _entityName;
	}

	/**
	 * 获取自然幢实体名
	 * @Title: getBuildingEntityName
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:37:58
	 * @param bdcdylx
	 * @param ly
	 * @return
	 */
	private static String getBuildingEntityName(BDCDYLX bdcdylx, DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_ZRZ", ly);
		else
			_entityName = "DCS_ZRZ_GZ";
		if (bdcdylx.equals(BDCDYLX.YCH) || bdcdylx.equals(BDCDYLX.YCZRZ)) {
			_entityName += "Y";
		}
		return _entityName;
	}

	/**
	 * new一个新的不动产单元的实例
	 * @Title: newRealUnit
	 * @author:liushufeng
	 * @date：2015年7月12日 上午12:21:48
	 * @param bdcdylx
	 *            单元类型
	 * @param ly
	 *            单元来源
	 * @return
	 */
	public static RealUnit newRealUnit(BDCDYLX bdcdylx, DJDYLY ly) {
		RealUnit unit = null;
		String _unitEntityName = bdcdylx.getTableName(ly);
		unit = (RealUnit) EntityTools.newEntity(_unitEntityName);
		return unit;
	}
	
	/**
	 * new一个新的土地用途的实例
	 * @Title: newRealUnit
	 * @author:liushufeng
	 * @date：2015年7月12日 上午12:21:48
	 * @param bdcdylx
	 *            单元类型
	 * @param ly
	 *            单元来源
	 * @return
	 */
	public static TDYT newTDYTUnit(DJDYLY ly) {
		TDYT tdyt = null;
		String _tdytEntityName = getTDYTEntityName(ly);
		tdyt = (TDYT) EntityTools.newEntity(_tdytEntityName);
		return tdyt;
	}
	
	/**
	 * new一个新的用海状况的实例
	 * @Title: newYHZKUnit
	 * @author:yuxuebin
	 * @date：2016年7月28日 01:23:48
	 * @param ly
	 *            单元来源
	 * @return
	 */
	public static YHZK newYHZKUnit(DJDYLY ly) {
		YHZK yhzk = null;
		String _yhzkEntityName = getYHZKEntityName(ly);
		yhzk = (YHZK) EntityTools.newEntity(_yhzkEntityName);
		return yhzk;
	}
	
	/**
	 * new一个新的用海用地坐标的实例
	 * @Title: newYHYDZBUnit
	 * @author:yuxuebin
	 * @date：2016年7月28日 01:24:48
	 * @param ly
	 *            单元来源
	 * @return
	 */
	public static YHYDZB newYHYDZBUnit(DJDYLY ly) {
		YHYDZB yhydzb = null;
		String _yhydzbEntityName = getYHYDZBEntityName(ly);
		yhydzb = (YHYDZB) EntityTools.newEntity(_yhydzbEntityName);
		return yhydzb;
	}

	/**
	 * 获取不动产单元的类
	 * @Title: getUnitClass
	 * @author:liushufeng
	 * @date：2015年7月12日 上午12:21:23
	 * @param bdcdylx
	 *            单元类型
	 * @param ly
	 *            单元来源
	 * @return
	 */
	private static Class<?> getUnitClass(BDCDYLX bdcdylx, DJDYLY ly) {
		Class<?> unitClass = null;
		String className = bdcdylx.getTableName(ly);
		if (StringUtils.isEmpty(className))
			return null;
		unitClass = EntityTools.getEntityClass(className);//
		return unitClass;
	}
	
	/**
	 * 从数据库加载土地用途
	 * @Title: loadTDYT
	 * @author:yuxuebin
	 * @date：2016年7月27日 11:26:45
	 * @param ly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return TDYT 土地用途
	 */
	public static TDYT loadTDYT(DJDYLY dyly, String id) {
		TDYT tdyt = null;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (dyly == null)
			return null;
		if (StringHelper.isEmpty(id))
			return null;
		Class<?> tdytClass = null;
		String _tdytEntityName = getTDYTEntityName(dyly);
		if (StringUtils.isEmpty(_tdytEntityName))
			return null;
		tdytClass = EntityTools.getEntityClass(_tdytEntityName);//
		if (tdytClass == null)
			return null;
		tdyt = (TDYT) dao.get(tdytClass, id);
		return tdyt;
	}
	
	/**
	 * 从数据库加载用海状况
	 * @Title: loadYHZK
	 * @author:yuxuebin
	 * @date：2016年7月28日 01:26:45
	 * @param ly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return YHZK 用海状况
	 */
	public static YHZK loadYHZK(DJDYLY dyly, String id) {
		YHZK yhzk = null;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (dyly == null)
			return null;
		if (StringHelper.isEmpty(id))
			return null;
		Class<?> yhzkClass = null;
		String _yhzkEntityName = getYHZKEntityName(dyly);
		if (StringUtils.isEmpty(_yhzkEntityName))
			return null;
		yhzkClass = EntityTools.getEntityClass(_yhzkEntityName);//
		if (yhzkClass == null)
			return null;
		yhzk = (YHZK) dao.get(yhzkClass, id);
		return yhzk;
	}
	
	/**
	 * 从数据库加载用海用地坐标
	 * @Title: loadYHYDZB
	 * @author:yuxuebin
	 * @date：2016年7月28日 01:28:45
	 * @param ly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return YHYDZB 用海用地坐标
	 */
	public static YHYDZB loadYHYDZB(DJDYLY dyly, String id) {
		YHYDZB yhydzb = null;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (dyly == null)
			return null;
		if (StringHelper.isEmpty(id))
			return null;
		Class<?> yhydzbClass = null;
		String _yhydzbEntityName = getYHYDZBEntityName(dyly);
		if (StringUtils.isEmpty(_yhydzbEntityName))
			return null;
		yhydzbClass = EntityTools.getEntityClass(_yhydzbEntityName);//
		if (yhydzbClass == null)
			return null;
		yhydzb = (YHYDZB) dao.get(yhydzbClass, id);
		return yhydzb;
	}
	
	/**
	 * 从数据库加载逻辑幢
	 * @Title: loadLogicBuilding
	 * @author:yuxuebin
	 * @date：2017年03月14日 11:52:45
	 * @param bdcdylx
	 *            单元类型
	 * @param ly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return YHYDZB 用海用地坐标
	 */
	public static LogicBuilding loadLogicBuilding(BDCDYLX bdcdylx,DJDYLY dyly, String id) {
		LogicBuilding logicbuilding = null;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (dyly == null)
			return null;
		if (StringHelper.isEmpty(id))
			return null;
		Class<?> logicbuildingClass = null;
		String _logicbuildingEntityName = getLogicBuildingEntityName(bdcdylx,dyly);
		if (StringUtils.isEmpty(_logicbuildingEntityName))
			return null;
		logicbuildingClass = EntityTools.getEntityClass(_logicbuildingEntityName);//
		if (logicbuildingClass == null)
			return null;
		logicbuilding = (LogicBuilding) dao.get(logicbuildingClass, id);
		return logicbuilding;
	}
	
	/**
	 * 从数据库加载层
	 * @Title: loadFloor
	 * @author:yuxuebin
	 * @date：2017年03月14日 11:52:45
	 * @param bdcdylx
	 *            单元类型
	 * @param ly
	 *            不动产单元来源(GZ,XZ,LS,DC)
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return YHYDZB 用海用地坐标
	 */
	public static Floor loadFloor(BDCDYLX bdcdylx,DJDYLY dyly, String id) {
		Floor floor = null;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (dyly == null)
			return null;
		if (StringHelper.isEmpty(id))
			return null;
		Class<?> floorClass = null;
		String _floorEntityName = getFloorEntityName(bdcdylx,dyly);
		if (StringUtils.isEmpty(_floorEntityName))
			return null;
		floorClass = EntityTools.getEntityClass(_floorEntityName);//
		if (floorClass == null)
			return null;
		floor = (Floor) dao.get(floorClass, id);
		return floor;
	}

	/**
	 * 获取土地用途实体类名称。
	 * @Title: getTDYTEntityName
	 * @author:liushufeng
	 * @date：2016年07月27日 11:54:14
	 * @param ly
	 * @return
	 */
	private static String getTDYTEntityName(DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_TDYT", ly);
		else
			_entityName = "DCS_TDYT_GZ";
		return _entityName;
	}

	/**
	 * 获取用海用地坐标实体类名称。
	 * @Title: getYHYDZBEntityName
	 * @author:yuxuebin
	 * @date：2016年07月05日 11:37:14
	 * @param ly
	 * @return
	 */
	private static String getYHYDZBEntityName(DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_YHYDZB", ly);
		else
			_entityName = "DCS_YHYDZB_GZ";
		return _entityName;
	}
	
	/**
	 * 获取用海状况实体类名称。
	 * @Title: getYHZKEntityName
	 * @author:yuxuebin
	 * @date：2016年07月05日 11:36:14
	 * @param ly
	 * @return
	 */
	private static String getYHZKEntityName(DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_YHZK", ly);
		else
			_entityName = "DCS_YHZK_GZ";
		return _entityName;
	}
	
	/**
	 * 获取自然幢实体类名称。
	 * @Title: getZRZEntityName
	 * @author:taochunda
	 * @date：2017年12月05日 14:11:00
	 * @param ly
	 * @return
	 */
	private static String getZRZEntityName(DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_ZRZ", ly);
		else
			_entityName = "DCS_ZRZ_GZ";
		return _entityName;
	}
	
	/**
	 * 根据单元号生成依赖值，单元类型生成不动产单元号 单元类型：宗地：01;宗海：02;自然幢：03;户04；森林、林木：05
	 * @作者 俞学斌
	 * @创建时间 2015年9月18日上午11:04:53
	 * @param RelyOnValue
	 *            单元号生成依赖值
	 * @param DYLX
	 *            单元类型
	 * @return
	 */
	public static String CreatBDCDYH(String RelyOnValue, String DYLX) {
		final String m_producename = "GETDYHEX";
		final String m_relyonvalue = RelyOnValue;
		final String m_dylx = DYLX;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		Session session = dao.getCurrentSession();
		String filenumber = session.doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append("BDCDCK.");
				str.append(m_producename);
				str.append("(");
				str.append("?,?,?");
				str.append(") }");
				String filrnumberString = "";
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				statement.setString(1, m_relyonvalue);
				statement.setString(2, m_dylx);
				statement.registerOutParameter(3, Types.NVARCHAR);
				statement.execute();
				filrnumberString = statement.getString(3);
				statement.close();
				return filrnumberString;
			}
		});
		if (StringHelper.isEmpty(filenumber)) {
			return "";
		}
		String BDCDYH = "";
		if ("01".equals(m_dylx)) {// 宗地
			//BDCDYH=14(ZD的宗地代码的前14位)+5(最大序号位)+9(W00000000)
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0') + "W00000000";
		}
		if ("011".equals(m_dylx)) {// 宗地
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0');
		}
		if ("02".equals(m_dylx)) {// 宗海
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0') + "H00000000";
		}
		if ("03".equals(m_dylx)) {// 自然幢
			//BDCDYH=19(宗地代码)+1(F)+4(最大序号位)+4(0000)
			BDCDYH = RelyOnValue + "F" + StringHelper.PadLeft(filenumber, 4, '0') + "0000";
		}
		if ("04".equals(m_dylx)) {// 户
			//BDCDYH=24(ZRZ不动产单元号的前24位)+4(最大序号位)
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 4, '0');
		}
		if ("05".equals(m_dylx)) {// 森林林木
			BDCDYH = RelyOnValue + "L" + StringHelper.PadLeft(filenumber, 8, '0');
		}
		return BDCDYH;
	}
}

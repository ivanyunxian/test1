package com.supermap.realestate.registration.service.impl.share;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.stream.FileImageOutputStream;

import oracle.net.aso.q;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.DCS_H_GZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.InsertDataService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.wisdombusiness.framework.model.gxjyk.Gxjhxm;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYJYBGQH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYQLR;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.Http;

@Service("insertDataService")
public class InsertDataServiceImpl implements InsertDataService {
	// private static final Log logger =
	// LogFactory.getLog(ExtractDataForSJZServiceImpl.class);
	@Autowired
	private com.supermap.wisdombusiness.workflow.dao.CommonDao commonDao;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDaoJY CommonDaoJY;
	@Autowired
	private DYService dyservice;

	private com.supermap.realestate.registration.handlerImpl.DYBGDJHandler DYBGDJHandler;
	private com.supermap.realestate.registration.handlerImpl.BGDJHandler BGDJHandler;
	private com.supermap.realestate.registration.handlerImpl.GZDJHandler GZDJHandler;
	private com.supermap.realestate.registration.handlerImpl.CSDJHandler CSDJHandler;
	String djyy="";
	// 石家庄抵押、转移业务抽取权利、权利人、户信息存入不动产库
	@Override
	public String InsertSXFromZJK(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh, String casenum,
			Map<String, List<BDCQLR>> dyQlrList, Map<String, List<BDCQLR>> zyQlrList, List<String> bdcdyhList,
			boolean bool) throws Exception {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		if (xmxx.size() > 0) {
			// logger.info("获取到项目信息");
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			if (qllx.equals(QLLX.DIYQ.Value)  && ql != null&& (djlx.equals(DJLX.ZXDJ.Value)||djlx.equals(DJLX.BGDJ.Value)||djlx.equals(DJLX.GZDJ.Value))) {
				flag = InsertDYZX(ql, xmbh, casenum);
				// 用房产的权利人名称刷新登记库中的
				updateSQRMC(xmbh, getDYR(casenum));
			} else {
				Map<String, List<BDCQLR>> qlrList = new HashMap<String, List<BDCQLR>>();
				if (qllx.equals(QLLX.DIYQ.Value)) {
					qlrList = dyQlrList;
				} else if (qllx.equals(QLLX.GYJSYDSHYQ_FWSYQ.Value) || qllx.equals(QLLX.JTJSYDSYQ_FWSYQ.Value)) {
					qlrList = zyQlrList;
				}
				// 这对单元的处理要根据业务类型区别开来【除变更、更正、初始登记外】
				if (bool) {
					String bdcdyidArr = "";
					for (String bdcdyh : bdcdyhList) {
						String hql = " BDCDYH = '" + bdcdyh + "' ";
						String bdcdyid1 ="";
						List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
						if (h_xzs.size() < 1) {
//							//考虑在建工程抵押，如果现状户中没找到，再到现状预中找
							List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
							if (h_xzys.size() < 1) {
								flag = "warning";
								return flag;
							}
							else {
								 bdcdyid1 = h_xzys.get(0).getId();
							}
						}
						else {
							// 变更、更正、初始登记直接传入单元信息作为现状层户信息
							 bdcdyid1 = h_xzs.get(0).getId();
						}
						
						BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
						if (_srcUnit == null) {
							continue;
						}
						String bdcdyid = _srcUnit.getId();
						if (bdcdyidArr.equals("")) {
							bdcdyidArr += bdcdyid;
						} else {
							bdcdyidArr += "," + bdcdyid;
						}
					}
					// 增加审核环节
					ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
					if (rltMessage.getSuccess().equals("false")) {
						return rltMessage.getMsg();
					} else if (rltMessage.getSuccess().equals("warning")) {
						return "警告：" + rltMessage.getMsg();
					}
				}
				// 将权利人保存到申请人表中
				Object[] sqrids = SaveBDCS_SQR(qlrList.get("0"), xmbh);
				for (String bdcdyh : bdcdyhList) {
					String hql = " BDCDYH = '" + bdcdyh + "' ";
					String bdcdyid1 ="";
					List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
					if (h_xzs.size() < 1) {
//						//考虑在建工程抵押，如果现状户中没找到，再到现状预中找
						List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
						if (h_xzys.size() < 1) {
							flag = "warning";
							return flag;
						}
						else {
							 bdcdyid1 = h_xzys.get(0).getId();
						}
					}
					else {
						// 变更、更正、初始登记直接传入单元信息作为现状层户信息
						 bdcdyid1 = h_xzs.get(0).getId();
					}
					BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
					if (_srcUnit == null) {
						continue;
					}
					DJHandler handler = HandlerFactory.createDJHandler(xmbh);
					String bdcdyid = _srcUnit.getId();
					dyservice.removeBDCDY(xmbh, bdcdyid);
					dyservice.addBDCDYNoCheck(xmbh, bdcdyid);

					// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】
					
					String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh, bdcdyid);
					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					
					Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
					if (_rights == null) {
						continue;
					}
					handler.addQLRbySQRArray(_rights.getId(), sqrids);
					SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
					if (_subRights == null) {
						continue;
					}
					SaveQL(ql, fsql, casenum, _rights, _subRights);
					SaveYWRDLR(xmbh, qlrList);
					flag = "true";
				}
				baseCommonDao.flush();
			}
		}
		return flag;
	}

	// 统一GXJYK抽取权利、权利人、户信息存入不动产库
	@Override
	public String InsertSXFromZJK(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<BDCQLR> qlrList,
			List<JYQLR> jyqlrList, List<String> bdcdyhList, List<JYH> HList, boolean bool, String fwzt)
			throws Exception {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		// 业务类型判断
		String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
		String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 注销类业务、异议登记处理
			// if
			// ((djlx.equals(DJLX.ZXDJ.Value)&&qllx.equals(QLLX.DIYQ.Value))||djlx.equals(DJLX.YYDJ.Value)&&
			// ql != null) {
			if (_handleClassName.contains("DYYGZXDJHandler") || _handleClassName.contains("DYZXDJHandler")
					|| _handleClassName.contains("DYZYDJHandler") || _handleClassName.contains("YYDJHandler")
					|| _handleClassName.contains("YYZXDJHandler") || _handleClassName.contains("ZYYG_YDY_DJHandler") ||
					// _handleClassName.contains("ZYYGDJHandler")
					_handleClassName.contains("ZYYGDYYGDJHandler") || _handleClassName.contains("ZYYGZXDJHandler")
					|| _handleClassName.contains("DYBGDJHandler")) {
				flag = InsertDYZX(ql, xmbh, casenum);
				// SaveBDCS_SQR()
			} else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")) {// djlx.equals(DJLX.CFDJ.Value)&&qllx.equals("98")
				// 解封
				flag = InsertJF(fsql, xmbh, casenum, bdcdyhList, fwzt);
			} else if (_handleClassName.contains("CSDJHandler")) {
				// else
				// if(djlx.equals(DJLX.CSDJ.Value)&&(qllx.equals("1")||qllx.equals("2")||qllx.equals("3")||qllx.equals("4")||
				// qllx.equals("5")||qllx.equals("6")||qllx.equals("7")||qllx.equals("8")))
				// {
				// 初始登记
				flag = InsertCSDJ(xmbh, casenum);
				// 保存申请人到登记库
				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			} else {
				// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】

				if (bool) {
					String bdcdyidArr = "";
					for (String bdcdyh : bdcdyhList) {
						String bdcdyid1 = "";
						String hql = " relationid = '" + bdcdyh + "' ";
						if (fwzt.equals("2")) {
							List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning:房产推送数据中单元号为：" + bdcdyh + " 的房屋状态与当前业务类型不符，请检测！";
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						} else if (fwzt.equals("1")) {
							List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning" + bdcdyh;
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						}

						BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
						if (_srcUnit == null) {
							continue;
						}
						String bdcdyid = _srcUnit.getId();
						if (bdcdyidArr.equals("")) {
							bdcdyidArr += bdcdyid;
						} else {
							bdcdyidArr += "," + bdcdyid;
						}
					}
					// 增加审核环节
					ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
					if (rltMessage.getSuccess().equals("false")) {
						return rltMessage.getMsg();
					} else if (rltMessage.getSuccess().equals("warning")) {
						return "警告：" + rltMessage.getMsg();
					}
				}
				// 将权利人保存到申请人表中
				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
				for (String bdcdyh : bdcdyhList) {
					// if (_handleClassName.contains("DYBGDJHandler")
					// || _handleClassName.contains("BGDJHandler")
					// || _handleClassName.contains("GZDJHandler")
					// || _handleClassName.contains("CSDJHandler")) {
					/************** 此处为除变更、更正、初始登记业务的单元添加方法 **************/
					// String bdcdyid="";
					// for(JYH H:HList){
					// if (_handleClassName.contains("DYBGDJHandler")){
					// DYBGDJHandler.addBDCDY(H.getBDCDYID());
					// }else if(_handleClassName.contains("BGDJHandler")){
					// BGDJHandler.addBDCDY(H.getBDCDYID());
					// }else if(_handleClassName.contains("GZDJHandler")){
					// GZDJHandler.addBDCDY(H.getBDCDYID());
					// }else if(_handleClassName.contains("CSDJHandler")){
					// CSDJHandler.addBDCDY(H.getBDCDYID());
					// }else{}
					// }
					// /********************************************************************/
					// DJHandler handler = HandlerFactory
					// .createDJHandler(xmbh);
					// String str = MessageFormat.format(
					// "XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh,
					// bdcdyid);
					// List<BDCS_DJDY_GZ> listdjdy = baseCommonDao
					// .getDataList(BDCS_DJDY_GZ.class, str);
					// Rights _rights = RightsTools.loadRightsByDJDYID(
					// DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
					// if (_rights == null) {
					// continue;
					// }
					// handler.addQLRbySQRArray(_rights.getId(), sqrids);
					// SubRights _subRights = RightsTools
					// .loadSubRightsByRightsID(DJDYLY.GZ,
					// _rights.getId());
					// if (_subRights == null) {
					// continue;
					// }
					// SaveQL(ql, fsql, casenum, _rights, _subRights);
					// SaveJYYWRDLR(xmbh, jyqlrList);
					// flag = "true";
					// } else {
					String hql = " relationid = '" + bdcdyh + "' ";
					String bdcdyid1 = "";
					// List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(
					// BDCS_H_XZ.class, hql);
					// if (h_xzs.size() < 1) {
					// flag = "warning";
					// return flag;
					// }
					// String bdcdyid1 = h_xzs.get(0).getId();
					// 验证是否落宗
					if (fwzt.equals("2")) {
						List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning";
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					} else if (fwzt.equals("1")) {
						List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning";
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					}

					BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
					if (_srcUnit == null) {
						continue;
					}
					String bdcdyid = _srcUnit.getId();
					String bdcdyh2 = _srcUnit.getBDCDYH();
					dyservice.removeBDCDY(xmbh, bdcdyid);
					dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
					DJHandler handler = HandlerFactory.createDJHandler(xmbh);
					String str = MessageFormat.format("XMBH=''{0}'' AND (BDCDYID=''{1}'' OR BDCDYH=''{2}'')", xmbh,
							bdcdyid, bdcdyh2);
					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					boolean bLoadQLR = true;// 是否加载权利人，换补证addBDCDYNoCheck时自动加载权利人了，不能重复添加
					// 如果用bdcdyid加载不到，要用qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						List<BDCS_DJDY_XZ> djdy_XZs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
								"BDCDYH='" + _srcUnit.getBDCDYH() + "'");
						if (djdy_XZs != null && djdy_XZs.size() > 0) {
							List<BDCS_QL_XZ> ql_XZs = baseCommonDao.getDataList(BDCS_QL_XZ.class,
									"BDCQZH='" + ql.getBDCQZH() + "'");
							if (ql_XZs != null && ql_XZs.size() > 0) {
								dyservice.addBDCDYNoCheck(xmbh, ql_XZs.get(0).getId());
								listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
								bLoadQLR = false;
							}
						}
					}
					// 如果还为空，再用另一种方式qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						String condition = "select qlid from bdck.bdcs_ql_xz where djdyid in (select djdyid from bdck.bdcs_djdy_xz where bdcdyid = '"
								+ bdcdyid + "') ";
						List<Map> ql_XZs = baseCommonDao.getDataListByFullSql(condition);
						if (ql_XZs != null && ql_XZs.size() > 0) {
							String qlid = ql_XZs.get(0).get("QLID").toString();
							dyservice.removeBDCDY(xmbh, qlid);
							dyservice.addBDCDYNoCheck(xmbh, qlid);
							listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
							bLoadQLR = false;
						}
					}
					Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
					if (_rights == null) {
						continue;
					}
					// 保存义务人到申请人表
					SaveJYYWRDLR(xmbh, jyqlrList);
					if (bLoadQLR) {
						// 加载权利人
						handler.addQLRbySQRArray(_rights.getId(), sqrids);
					}

					SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
					if (_subRights == null) {
						continue;
					}
					SaveQL(ql, fsql, casenum, _rights, _subRights);
					
					flag = "true";
				}
				baseCommonDao.flush();
			}
			// }
		}
		return flag;
	}

	@Override
	public String InsertSXFromZJK(List<BDCS_QL_GZ> qlList, List<BDCS_FSQL_GZ> fsqlList, String xmbh, String casenum,
			List<BDCQLR> qlrList, List<JYQLR> jyqlrList, List<String> bdcdyhList, boolean bool, String fwzt)
			throws Exception {
		String flag = "false";
		//大宗件时获取前台传来的登记原因
		String[] ywh_djyy= xmbh.split("&&");
		if(ywh_djyy!=null&&ywh_djyy.length>0){
			xmbh=ywh_djyy[0];
		}
		djyy="";
		if(ywh_djyy!=null&&ywh_djyy.length>1){
			djyy=ywh_djyy[1];
		}
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		// 业务类型判断
		String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
		String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 注销类业务、异议登记处理
			if (_handleClassName.contains("DYYGZXDJHandler") || _handleClassName.contains("DYZXDJHandler")
					|| _handleClassName.contains("YYDJHandler") || _handleClassName.contains("YYZXDJHandler") || // _handleClassName.contains("ZYYG_YDY_DJHandler")||
					// _handleClassName.contains("ZYYGDYYGDJHandler")||
					_handleClassName.contains("ZYYGZXDJHandler") ) {
				flag = InsertDYZX(qlList.get(0), xmbh, casenum);
				// SaveBDCS_SQR()
			} else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")) {// djlx.equals(DJLX.CFDJ.Value)&&qllx.equals("98")
				// 解封
				flag = InsertJF(fsqlList.get(0), xmbh, casenum, bdcdyhList, fwzt);
			}  else if (_handleClassName.contains("CSDJHandler")) {
				// 初始登记
				flag = InsertSCDJ(xmbh, casenum, bdcdyhList, bool, qlrList, qlList, bdcdyhList, fsqlList, fwzt);
				// 保存申请人到登记库
//				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			}
			// else if(_handleClassName.contains("DYZYDJHandler"))
			// {//_handleClassName.contains("ZYYG_YDY_DJHandler")||
			// //预告预抵-->转移抵押登记
			// InsertSXFromZJKZYDY(qlList, fsqlList, xmbh, casenum, qlrList,
			// jyqlrList, bdcdyhList, HList, bool, fwzt);
			// }
			else {
				// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】
				if (bool) {
					String bdcdyidArr = "";
					for (String bdcdyh : bdcdyhList) {
						String bdcdyid1 = "";
						String hql = " relationid = '" + bdcdyh + "' ";
						if (fwzt.equals("2")) {
							List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning:房产推送数据中单元号为：" + bdcdyh + " 的房屋状态与当前业务类型不符，请检测！";
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						} else if (fwzt.equals("1")) {
							// 此处原因:1、房产房屋状态推送错误【本该是现房2的推送成1】2、该预测户未落宗
							List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning" + bdcdyh;
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						}

						BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
						if (_srcUnit == null) {
							continue;
						}
						String bdcdyid = _srcUnit.getId();
						if (bdcdyidArr.equals("")) {
							bdcdyidArr += bdcdyid;
						} else {
							bdcdyidArr += "," + bdcdyid;
						}
					}
					// 增加审核环节
					ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
					if (rltMessage.getSuccess().equals("false")) {
						return rltMessage.getMsg();
					} else if (rltMessage.getSuccess().equals("warning")) {
						return "警告：" + rltMessage.getMsg();
					}
				}
				// 不是转现类的才将权利人保存到申请人表中，否则会重复
				Object[] sqrids = null;
				if (_handleClassName.contains("ZY_YDYTODY_DJHandler")
						|| _handleClassName.contains("ZY_YGDYTODY_DJHandler")) {
				} else {
					sqrids = SaveBDCS_SQR(qlrList, xmbh);
				}

				BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				for (String relationid : bdcdyhList) {
					String hql = " relationid = '" + relationid + "' or BDCDYH='"+relationid+"'";
					String bdcdyid_xz = "";
					// 验证是否落宗
					if (fwzt.equals("2")) {
						List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning";
							return flag;
						}
						bdcdyid_xz = h_xzs.get(0).getId();
					} else if (fwzt.equals("1")) {
						List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning";
							return flag;
						}
						bdcdyid_xz = h_xzs.get(0).getId();
					}

					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid_xz);
					if (_srcUnit == null) {
						continue;
					}
					String bdcdyid = _srcUnit.getId();
					String bdcdyh2 = _srcUnit.getBDCDYH();
					dyservice.removeBDCDY(xmbh, bdcdyid);
					ResultMessage rs= dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
					// DJHandler handler = HandlerFactory
					// .createDJHandler(xmbh);
					String str = MessageFormat.format("XMBH=''{0}'' AND (BDCDYID=''{1}'' OR BDCDYH=''{2}'')", xmbh,
							bdcdyid, bdcdyh2);
					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					boolean bLoadQLR = false;// true;//是否加载权利人，换补证addBDCDYNoCheck时自动加载权利人了，不能重复添加
					// 如果用bdcdyid加载不到，要用qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						List<BDCS_DJDY_XZ> djdy_XZs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
								"BDCDYH='" + _srcUnit.getBDCDYH() + "'");
						if (djdy_XZs != null && djdy_XZs.size() > 0) {
							List<BDCS_QL_XZ> ql_XZs = baseCommonDao.getDataList(BDCS_QL_XZ.class,
									"BDCQZH='" + qlList.get(0).getBDCQZH() + "'");
							if (ql_XZs != null && ql_XZs.size() > 0) {
								rs=dyservice.addBDCDYNoCheck(xmbh, ql_XZs.get(0).getId());
								listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
								bLoadQLR = false;
							}
						}
					}
					// 如果还为空，再用另一种方式qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						String condition = "select qlid from bdck.bdcs_ql_xz where djdyid in (select djdyid from bdck.bdcs_djdy_xz where bdcdyid = '"
								+ bdcdyid + "') ";
						List<Map> ql_XZs = baseCommonDao.getDataListByFullSql(condition);
						if (ql_XZs != null && ql_XZs.size() > 0) {
							String qlid = ql_XZs.get(0).get("QLID").toString();
							dyservice.removeBDCDY(xmbh, qlid);
							rs=dyservice.addBDCDYNoCheck(xmbh, qlid);
							listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
							bLoadQLR = false;
						}
					}

					if (listdjdy != null && listdjdy.size() > 0) {
						for (int i = 0; i < listdjdy.size(); i++) {
							// 考虑到并案流程，一次可能有多条数据
							List<Rights> bdcRights = RightsTools.loadRightsByCondition(DJDYLY.GZ,
									"XMBH='" + xmbh + "'");
							if (bdcRights != null && bdcRights.size() > 0) {
								for (int k = 0; k < bdcRights.size(); k++) {
									Rights _rights = bdcRights.get(k);// RightsTools.loadRightsByDJDYID(DJDYLY.GZ,
																		// xmbh,
																		// listdjdy.get(i).getDJDYID());
									if (_rights == null) {
										continue;
									}
									// 保存义务人到申请人表
									SaveJYYWRDLR(xmbh, jyqlrList);
									if (bLoadQLR) {
										// 加载权利人
										handler.addQLRbySQRArray(_rights.getId(), sqrids);
									}

									SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ,
											_rights.getId());
									if (_subRights == null) {
										continue;
									}
									// 考虑并案流程，根据登记类型和权利类型去对应的权利和附属权利
									// 克拉玛依多次抽取时避免后一次抽取覆盖掉前面抽取的casenum，利用BDCS_QL_GZ中的archives_bookno记录relationid判断此次循环relationid相同则保存
									// 2016年10月24日 15:19:59 卜晓波
									String rights_bookno = "";
									if (_rights.getARCHIVES_BOOKNO() != null) {
										rights_bookno = _rights.getARCHIVES_BOOKNO();
									}
									List<Object> ql_fsqlList = getMatchQLFSQL(qlList, fsqlList, _rights.getDJLX(),
											_rights.getQLLX(), relationid, fwzt, rights_bookno);
									if (ql_fsqlList.size() < 1) {
										continue;
									}
									BDCS_QL_GZ ql = (BDCS_QL_GZ) ql_fsqlList.get(0);
									BDCS_FSQL_GZ fsql_GZ = (BDCS_FSQL_GZ) ql_fsqlList.get(1);
									SaveQL(ql, fsql_GZ, casenum, _rights, _subRights);
									// SaveQL(qlList.get(0), fsqlList.get(0),
									// casenum, _rights, _subRights);
									
									flag = "true";
									/**
									 * 因齐齐哈尔房产公司relationid需要更新
									 * 故在利用原Relationid抽取完毕后取出共享交易库H表【此处未更新BGQH】中NRelarionid，并更新不动产库工作层户的Relarionid
									 * 特殊情况：退件重新读取时无法识别原Relationid，直接以NRelationid作为抽取结果
									 * 2016年8月31日 20:53:19 卜晓波
									 */
									String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
									if (xzqdm.equals("230200")) {
										Connection jyConnection = null;
										try {
											if (jyConnection == null || jyConnection.isClosed()) {
												jyConnection = JH_DBHelper.getConnect_jy();
											}
										} catch (SQLException e1) {
										}
										// 从casenum发起去找当前业务新旧relationid的关系
										String gxjhxmsql = "select GXXMBH from gxjyk.gxjhxm where CASENUM= '" + casenum
												+ "'";
										PreparedStatement pstmt = jyConnection.prepareStatement(gxjhxmsql);
										ResultSet GXJHXMrst = pstmt.executeQuery();
										// ResultSet
										// GXJHXMrst=JH_DBHelper.excuteQuery(jyConnection,
										// gxjhxmsql);
										if (GXJHXMrst != null) {
											while (GXJHXMrst.next()) {
												String gxxmbh = GXJHXMrst.getString("GXXMBH");
												// 取NRELATIONID不能用model取，因为此处专门为齐齐哈尔设计
												String NRELATIONID = "";
												String sql = "select NRELATIONID from gxjyk.h where GXXMBH= '" + gxxmbh
														+ "'";
												PreparedStatement pstmt2 = jyConnection.prepareStatement(sql);
												ResultSet NRELATIONIDrst = pstmt2.executeQuery();
												// ResultSet
												// NRELATIONIDrst=JH_DBHelper.excuteQuery(jyConnection,
												// sql);
												if (NRELATIONIDrst != null) {
													if (NRELATIONIDrst.next()) {
														NRELATIONID = NRELATIONIDrst.getString("NRELATIONID");
														// 更新现状层户RELATIONID
														if (fwzt.equals("2")) {
															List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(
																	BDCS_H_XZ.class,
																	"RELATIONID= '" + relationid + "'");
															// 非退件情况才做更新
															if (h_xzs != null && h_xzs.size() > 0) {
																BDCS_H_XZ h_xz = h_xzs.get(0);
																h_xz.setRELATIONID(NRELATIONID);
																baseCommonDao.update(h_xz);
															}
														} else {
															List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(
																	BDCS_H_XZY.class,
																	"RELATIONID= '" + relationid + "'");
															// 非退件情况才做更新
															if (h_xzys != null && h_xzys.size() > 0) {
																BDCS_H_XZY h_xzy = h_xzys.get(0);
																h_xzy.setRELATIONID(NRELATIONID);
																baseCommonDao.update(h_xzy);
															}
														}
													}
													pstmt2.close();
													NRELATIONIDrst.close();
												}
											}
											pstmt.close();
											GXJHXMrst.close();
										}
									}
								}
							}
						}
					}
				}
				baseCommonDao.flush();
			}
			// }
		}
		return flag;
	}

	// 变更业务GXJYK抽取权利、权利人、户信息存入不动产库，只支持一套房屋
	@Override
	public String InsertSXFromZJKBG(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<BDCQLR> qlrList,
			List<JYQLR> jyqlrList, List<JYJYBGQH> bgqhList, List<JYH> hList, boolean bool, String fwzt)
			throws Exception {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】
			// 业务类型判断
			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";

			if (bool) {
				String bdcdyidArr = "";
				// 检查是否落宗
				for (JYJYBGQH bgqh : bgqhList) {
					String bdcdyh = bgqh.getRELATIONID();
					String bdcdyid1 = "";
					String hql = " relationid = '" + bdcdyh + "' ";
					if (fwzt.equals("2")) {
						List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning" + bdcdyh;
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					} else if (fwzt.equals("1")) {
						List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning" + bdcdyh;
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					}

					BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
					if (_srcUnit == null) {
						continue;
					}
					String bdcdyid = _srcUnit.getId();
					if (bdcdyidArr.equals("")) {
						bdcdyidArr += bdcdyid;
					} else {
						bdcdyidArr += "," + bdcdyid;
					}
				}
				// 增加审核环节，检查是否有抵押、查封等
				ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
				if (rltMessage.getSuccess().equals("false")) {
					return rltMessage.getMsg();
				} else if (rltMessage.getSuccess().equals("warning")) {
					return "警告：" + rltMessage.getMsg();
				}
			}
			// 将权利人保存到申请人表中
			Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			// for (JYJYBGQH bgqh : bgqhList) {
			if (_handleClassName.contains("BGDJHandler")) {
				// || _handleClassName.contains("BGDJHandler")
				// || _handleClassName.contains("GZDJHandler")) {
				/************** 此处为除变更、更正、初始登记业务的单元添加方法 **************/
				String bdcdyid = "", bdcdyid2 = "";
				for (JYH H : hList) {
					// 先从共享交易库中查出所有的relationID，然后去调查库关联查出所有的bdcdyid
					List<DCS_H_GZ> dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class,
							"relationid='" + H.getRELATIONID() + "'");
					String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
					if (xzqdm.equals("230200")) {
						Connection jyConnection = null;
						try {
							if (jyConnection == null || jyConnection.isClosed()) {
								jyConnection = JH_DBHelper.getConnect_jy();
							}
						} catch (SQLException e1) {
						}
						// 取NRELATIONID不能用model取，因为此处专门为齐齐哈尔设计
						String NRELATIONID = "";
						String sql = "select NRELATIONID from gxjyk.h where RELATIONID= '" + H.getRELATIONID() + "'";
						PreparedStatement pstmt = jyConnection.prepareStatement(sql);
						ResultSet NRELATIONIDrst = pstmt.executeQuery();
						// ResultSet
						// NRELATIONIDrst=JH_DBHelper.excuteQuery(jyConnection,
						// sql);
						if (NRELATIONIDrst != null) {
							while (NRELATIONIDrst.next()) {
								NRELATIONID = NRELATIONIDrst.getString("NRELATIONID");
								if (fwzt.equals("2")) {
									// 变更的户现状层户和工作层户都维护，变更前户这块别更新（后面会取变更前单元，这块更新了后面取不到）
									// List<BDCS_H_XZ> h_xzs =
									// baseCommonDao.getDataList(BDCS_H_XZ.class,"RELATIONID=
									// '"+H.getRELATIONID()+"'");
									// //非退件情况才做更新
									// if(h_xzs!=null && h_xzs.size()>0){
									// BDCS_H_XZ h_xz=h_xzs.get(0);
									// h_xz.setRELATIONID(NRELATIONID);
									// baseCommonDao.update(h_xz);
									// }
									List<BDCS_H_GZ> h_gzs = baseCommonDao.getDataList(BDCS_H_GZ.class,
											"RELATIONID= '" + H.getRELATIONID() + "' and XMBH = '" + xmbh + "'");
									// 非退件情况才做更新
									if (h_gzs != null && h_gzs.size() > 0) {
										BDCS_H_GZ h_gz = h_gzs.get(0);
										h_gz.setRELATIONID(NRELATIONID);
										baseCommonDao.update(h_gz);
									}
								} else {
									List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(BDCS_H_XZY.class,
											"RELATIONID= '" + H.getRELATIONID() + "'");
									// 非退件情况才做更新
									if (h_xzys != null && h_xzys.size() > 0) {
										BDCS_H_XZY h_xzy = h_xzys.get(0);
										h_xzy.setRELATIONID(NRELATIONID);
										baseCommonDao.update(h_xzy);
									}
								}
							}
							pstmt.close();
							NRELATIONIDrst.close();
							dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class, "relationid='" + NRELATIONID + "'");
						}

					}
					bdcdyid += dcs_H_GZs.get(0).getId() + ",";
					bdcdyid2 += "'" + dcs_H_GZs.get(0).getId() + "',";
				}
				bdcdyid = bdcdyid.substring(0, bdcdyid.length() - 1);
				bdcdyid2 = bdcdyid2.substring(0, bdcdyid2.length() - 1);
				if (_handleClassName.contains("DYBGDJHandler")) {
					// DYBGDJHandler.addBDCDY(bdcdyid);
				} else if (_handleClassName.contains("BGDJHandler")) {
					// 变更后
					dyservice.addBDCDYNoCheck(xmbh, bdcdyid);

					// 变更前
					String bdcdyid3 = "";
					for (JYJYBGQH _bgqh : bgqhList) {
						// 不动产库关联所有bdcdyid
						List<BDCS_H_XZ> bdcs_H_XZs = baseCommonDao.getDataList(BDCS_H_XZ.class,
								"relationid='" + _bgqh.getRELATIONID() + "'");
						bdcdyid3 += bdcs_H_XZs.get(0).getId() + ",";
					}
					bdcdyid3 = bdcdyid3.substring(0, bdcdyid3.length() - 1);
					dyservice.addBDCDYNoCheck(xmbh, bdcdyid3);
					// BGDJHandler.addBDCDY(bdcdyid);
				} else if (_handleClassName.contains("GZDJHandler")) {
					// GZDJHandler.addBDCDY(bdcdyid);
					// //考虑到南宁房产的更正登记后relationid会变化，拷贝单元后，用更正后的relationid更新一下工作层的户relationid

				} else {
				}

				/********************************************************************/
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID in ({1})", xmbh, bdcdyid2);
				List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
				Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
				if (_rights == null) {
					// continue;
				}
				//添加义务人
				SaveJYYWRDLR(xmbh, jyqlrList);
				handler.addQLRbySQRArray(_rights.getId(), sqrids);
				SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
				if (_subRights == null) {
					// continue;
				}
				SaveQL(ql, fsql, casenum, _rights, _subRights);
				
				baseCommonDao.flush();
				flag = "true";
				/**
				 * 因齐齐哈尔房产公司relationid需要更新
				 * 故在利用原Relationid抽取完毕后取出共享交易库H表【此处未更新BGQH】中NRelarionidlist，并更新不动产库工作层户的Relarionid
				 * 特殊情况：退件重新读取时无法识别原Relationid，直接以NRelationid作为抽取结果 2016年8月31日
				 * 20:53:19 卜晓波
				 */
				String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
				if (xzqdm.equals("230200")) {
					Connection jyConnection = null;
					try {
						if (jyConnection == null || jyConnection.isClosed()) {
							jyConnection = JH_DBHelper.getConnect_jy();
						}
					} catch (SQLException e1) {
					}
					// 取NRELATIONID不能用model取，因为此处专门为齐齐哈尔设计
					String NRELATIONID = "";
					for (JYH H : hList) {
						String sql = "select NRELATIONID from gxjyk.h where RELATIONID= '" + H.getRELATIONID() + "'";
						PreparedStatement pstmt = jyConnection.prepareStatement(sql);
						ResultSet NRELATIONIDrst = pstmt.executeQuery();
						// ResultSet
						// NRELATIONIDrst=JH_DBHelper.excuteQuery(jyConnection,
						// sql);
						if (NRELATIONIDrst != null) {
							while (NRELATIONIDrst.next()) {
								NRELATIONID = NRELATIONIDrst.getString("NRELATIONID");
								if (fwzt.equals("2")) {
									// 变更的户现状层户和工作层户都维护
									List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class,
											"RELATIONID= '" + H.getRELATIONID() + "'");
									// 非退件情况才做更新
									if (h_xzs != null && h_xzs.size() > 0) {
										BDCS_H_XZ h_xz = h_xzs.get(0);
										h_xz.setRELATIONID(NRELATIONID);
										baseCommonDao.update(h_xz);
									}
									List<BDCS_H_GZ> h_gzs = baseCommonDao.getDataList(BDCS_H_GZ.class,
											"RELATIONID= '" + H.getRELATIONID() + "' and XMBH = '" + xmbh + "'");
									// 非退件情况才做更新
									if (h_gzs != null && h_gzs.size() > 0) {
										BDCS_H_GZ h_gz = h_gzs.get(0);
										h_gz.setRELATIONID(NRELATIONID);
										baseCommonDao.update(h_gz);
									}
								} else {
									List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(BDCS_H_XZY.class,
											"RELATIONID= '" + H.getRELATIONID() + "'");
									// 非退件情况才做更新
									if (h_xzys != null && h_xzys.size() > 0) {
										BDCS_H_XZY h_xzy = h_xzys.get(0);
										h_xzy.setRELATIONID(NRELATIONID);
										baseCommonDao.update(h_xzy);
									}
								}
							}
							pstmt.close();
							NRELATIONIDrst.close();
						}
					}
				}
			}
			// }
		}
		return flag;
	}

	// 更正业务GXJYK抽取权利、权利人、户信息存入不动产库，支持一笔业务多套房
	@Override
	public String InsertSXFromZJKGZ(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<BDCQLR> qlrList,
			List<JYQLR> jyqlrList, List<JYJYBGQH> bgqhList, List<JYH> hList, boolean bool, String fwzt)
			throws Exception {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】
			// 业务类型判断
			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";

			if (bool) {
				String bdcdyidArr = "";
				// 检查是否落宗
				for (JYJYBGQH bgqh : bgqhList) {
					String bdcdyh = bgqh.getRELATIONID();
					String bdcdyid1 = "";
					String hql = " relationid = '" + bdcdyh + "' ";
					if (fwzt.equals("2")) {
						List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning" + bdcdyh;
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					} else if (fwzt.equals("1")) {
						List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning" + bdcdyh;
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					}

					BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
					if (_srcUnit == null) {
						continue;
					}
					String bdcdyid = _srcUnit.getId();
					if (bdcdyidArr.equals("")) {
						bdcdyidArr += bdcdyid;
					} else {
						bdcdyidArr += "," + bdcdyid;
					}
				}
				// 增加审核环节，检查是否有抵押、查封等
				ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
				if (rltMessage.getSuccess().equals("false")) {
					return rltMessage.getMsg();
				} else if (rltMessage.getSuccess().equals("warning")) {
					return "警告：" + rltMessage.getMsg();
				}
			}
			// 将权利人保存到申请人表中
			Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			for (JYH bghh : hList) {
				if (_handleClassName.contains("GZDJHandler")) {
					/************** 此处为除变更、更正、初始登记业务的单元添加方法 **************/
					String bdcdyid = "", bdcdyid2 = "";
					for (JYH H : hList) {
						// 先从共享交易库中查出所有的relationID，然后去调查库关联查出所有的bdcdyid
						List<BDCS_H_GZ> bdcs_H_GZs = baseCommonDao.getDataList(BDCS_H_GZ.class,
								"relationid='" + H.getRELATIONID() + "'");
						String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
						// 如果是齐齐哈尔的，用nrelationid去查
						if (xzqdm.equals("230200") && (bdcs_H_GZs == null || bdcs_H_GZs.size() == 0)) {
							Connection jyConnection = null;
							try {
								if (jyConnection == null || jyConnection.isClosed()) {
									jyConnection = JH_DBHelper.getConnect_jy();
								}
							} catch (SQLException e1) {
							}
							String sql = "select NRELATIONID from gxjyk.h where RELATIONID= '" + H.getRELATIONID()
									+ "'";
							String NRELATIONID = "";
							PreparedStatement pstmt = jyConnection.prepareStatement(sql);
							ResultSet NRELATIONIDrst = pstmt.executeQuery();
							// ResultSet
							// NRELATIONIDrst=JH_DBHelper.excuteQuery(jyConnection,
							// sql);
							if (NRELATIONIDrst != null) {
								while (NRELATIONIDrst.next()) {
									NRELATIONID = NRELATIONIDrst.getString("NRELATIONID");
								}
							}
							pstmt.close();
							NRELATIONIDrst.close();
							bdcs_H_GZs = baseCommonDao.getDataList(BDCS_H_GZ.class, "relationid='" + NRELATIONID + "'");
						}
						bdcdyid += bdcs_H_GZs.get(0).getId() + ",";
						bdcdyid2 += "'" + bdcs_H_GZs.get(0).getId() + "',";
					}
					bdcdyid = bdcdyid.substring(0, bdcdyid.length() - 1);
					bdcdyid2 = bdcdyid2.substring(0, bdcdyid2.length() - 1);
					if (_handleClassName.contains("GZDJHandler")) {
						DJHandler handler = HandlerFactory.createDJHandler(xmbh);
						handler.addBDCDY(bdcdyid);
						// 考虑到南宁房产的更正登记后relationid会变化，拷贝单元后，用更正后的relationid更新一下工作层的户relationid
						String bgqRelationid = getBGQRelationid(bghh.getBDCDYID(), bgqhList);
						String sql = "update bdck.BDCS_H_GZ set relationid='" + bgqRelationid + "' where relationid='"
								+ bghh.getRELATIONID() + "'";
						baseCommonDao.updateBySql(sql);
						baseCommonDao.flush();
					} else {
					}

					/********************************************************************/
					DJHandler handler = HandlerFactory.createDJHandler(xmbh);
					String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID in ({1}) )", xmbh, bdcdyid2);
					if (_handleClassName.contains("GZDJHandler")) {
						// 因为更正登记的bdcdyid变化了，所以去除bdcdyid
						str = MessageFormat.format("XMBH=''{0}'')", xmbh);
					}
					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
					if (_rights == null) {
						continue;
					}
					//保存义务人
					SaveJYYWRDLR(xmbh, jyqlrList);
					handler.addQLRbySQRArray(_rights.getId(), sqrids);
					SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
					if (_subRights == null) {
						continue;
					}
					SaveQL(ql, fsql, casenum, _rights, _subRights);
					
					flag = "true";
				} else if (_handleClassName.contains("DYBGDJHandler") || _handleClassName.contains("BGDJHandler")) {
					/************** 此处为除变更、更正、初始登记业务的单元添加方法 **************/
					String bdcdyid = "", bdcdyid2 = "";
					for (JYH H : hList) {
						// 先从共享交易库中查出所有的relationID，然后去调查库关联查出所有的bdcdyid
						List<DCS_H_GZ> dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class,
								"relationid='" + H.getRELATIONID() + "'");
						String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
						// 如果是齐齐哈尔的，用nrelationid去查
						if (xzqdm.equals("230200") && (dcs_H_GZs == null || dcs_H_GZs.size() == 0)) {
							Connection jyConnection = null;
							try {
								if (jyConnection == null || jyConnection.isClosed()) {
									jyConnection = JH_DBHelper.getConnect_jy();
								}
							} catch (SQLException e1) {
							}
							String sql = "select NRELATIONID from gxjyk.h where RELATIONID= '" + H.getRELATIONID()
									+ "'";
							String NRELATIONID = "";
							PreparedStatement pstmt = jyConnection.prepareStatement(sql);
							ResultSet NRELATIONIDrst = pstmt.executeQuery();
							// ResultSet
							// NRELATIONIDrst=JH_DBHelper.excuteQuery(jyConnection,
							// sql);
							if (NRELATIONIDrst != null) {
								while (NRELATIONIDrst.next()) {
									NRELATIONID = NRELATIONIDrst.getString("NRELATIONID");
								}
							}
							pstmt.close();
							NRELATIONIDrst.close();
							dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class, "relationid='" + NRELATIONID + "'");
						}
						bdcdyid += dcs_H_GZs.get(0).getId() + ",";
						bdcdyid2 += "'" + dcs_H_GZs.get(0).getId() + "',";
					}
					bdcdyid = bdcdyid.substring(0, bdcdyid.length() - 1);
					bdcdyid2 = bdcdyid2.substring(0, bdcdyid2.length() - 1);
					if (_handleClassName.contains("DYBGDJHandler")) {
						DYBGDJHandler.addBDCDY(bdcdyid);
					} else if (_handleClassName.contains("BGDJHandler")) {
						// //变更后
						// dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
						// //变更前
						// String bdcdyid3 = "";
						// for (JYJYBGQH _bgqh : bgqhList) {
						// //不动产库关联所有bdcdyid
						// List<BDCS_H_XZ>
						// bdcs_H_XZs=baseCommonDao.getDataList(BDCS_H_XZ.class,
						// "relationid='"+_bgqh.getRELATIONID()+"'");
						// bdcdyid3+=bdcs_H_XZs.get(0).getId()+",";
						// }
						// bdcdyid3=bdcdyid3.substring(0,bdcdyid3.length()-1);
						// dyservice.addBDCDYNoCheck(xmbh, bdcdyid3);
					} else {
					}

					/********************************************************************/
					DJHandler handler = HandlerFactory.createDJHandler(xmbh);
					String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID in ({1}) )", xmbh, bdcdyid2);
					if (_handleClassName.contains("GZDJHandler")) {
						// 因为更正登记的bdcdyid变化了，所以去除bdcdyid
						str = MessageFormat.format("XMBH=''{0}'')", xmbh);
					}
					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
					if (_rights == null) {
						continue;
					}
					//保存义务人
					SaveJYYWRDLR(xmbh, jyqlrList);
					handler.addQLRbySQRArray(_rights.getId(), sqrids);
					SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
					if (_subRights == null) {
						continue;
					}
					SaveQL(ql, fsql, casenum, _rights, _subRights);
					
					flag = "true";
				}
			}
			baseCommonDao.flush();
		}
		return flag;
	}

	// 转移抵押登记
	public String InsertSXFromZJKZYDY(List<BDCS_QL_GZ> qlList, List<BDCS_FSQL_GZ> fsqlList, String xmbh, String casenum,
			List<BDCQLR> qlrList, List<JYQLR> jyqlrList, List<String> bdcdyhList, List<JYH> hList, boolean bool,
			String fwzt) throws Exception {
		String flag = "";
		// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】
		if (bool) {
			String bdcdyidArr = "";
			for (String bdcdyh : bdcdyhList) {
				String bdcdyid1 = "";
				String hql = " relationid = '" + bdcdyh + "' ";
				if (fwzt.equals("2")) {
					List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
					if (h_xzs.size() < 1) {
						flag = "warning:房产推送数据中单元号为：" + bdcdyh + " 的房屋状态与当前业务类型不符，请检测！";
						return flag;
					}
					bdcdyid1 = h_xzs.get(0).getId();
				} else if (fwzt.equals("1")) {
					// 此处原因:1、房产房屋状态推送错误【本该是现房2的推送成1】2、该预测户未落宗
					List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
					if (h_xzs.size() < 1) {
						flag = "warning" + bdcdyh;
						return flag;
					}
					bdcdyid1 = h_xzs.get(0).getId();
				}

				BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
				if (_srcUnit == null) {
					continue;
				}
				String bdcdyid = _srcUnit.getId();
				if (bdcdyidArr.equals("")) {
					bdcdyidArr += bdcdyid;
				} else {
					bdcdyidArr += "," + bdcdyid;
				}
			}
			// 增加审核环节
			ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
			if (rltMessage.getSuccess().equals("false")) {
				return rltMessage.getMsg();
			} else if (rltMessage.getSuccess().equals("warning")) {
				return "警告：" + rltMessage.getMsg();
			}
		}
		// 将权利人保存到申请人表中
		Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
		BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		// for (int m=0;m<qlList.size();m++) { //String relationid : bdcdyhList
		String hql = " relationid = '" + bdcdyhList.get(0) + "' ";// relationid
		String bdcdyid_xz = "";
		// 验证是否落宗
		if (fwzt.equals("2")) {
			List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
			if (h_xzs.size() < 1) {
				flag = "warning";
				return flag;
			}
			bdcdyid_xz = h_xzs.get(0).getId();
		} else if (fwzt.equals("1")) {
			List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
			if (h_xzs.size() < 1) {
				flag = "warning";
				return flag;
			}
			bdcdyid_xz = h_xzs.get(0).getId();
		}

		RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid_xz);
		if (_srcUnit == null) {
			return flag;
		}
		String bdcdyid = _srcUnit.getId();
		String bdcdyh2 = _srcUnit.getBDCDYH();
		dyservice.removeBDCDY(xmbh, bdcdyid);
		dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
		String str = MessageFormat.format("XMBH=''{0}'' AND (BDCDYID=''{1}'' OR BDCDYH=''{2}'')", xmbh, bdcdyid,
				bdcdyh2);
		List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
		boolean bLoadQLR = true;// 是否加载权利人，换补证addBDCDYNoCheck时自动加载权利人了，不能重复添加
		// 如果用bdcdyid加载不到，要用qlid
		// if(listdjdy==null||listdjdy.size()==0){
		// List<BDCS_DJDY_XZ>
		// djdy_XZs=baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
		// "BDCDYH='"+_srcUnit.getBDCDYH()+"'");
		// if(djdy_XZs!=null&&djdy_XZs.size()>0){
		// List<BDCS_QL_XZ> ql_XZs=baseCommonDao.getDataList(BDCS_QL_XZ.class,
		// "BDCQZH='"+qlList.get(0).getBDCQZH()+"'");
		// if(ql_XZs!=null&&ql_XZs.size()>0){
		// dyservice.addBDCDYNoCheck(xmbh, ql_XZs.get(0).getId());
		// listdjdy = baseCommonDao
		// .getDataList(BDCS_DJDY_GZ.class, str);
		// bLoadQLR=false;
		// }
		// }
		// }
		// 如果还为空，再用另一种方式qlid
		if (listdjdy == null || listdjdy.size() == 0) {
			String condition = "select qlid from bdck.bdcs_ql_xz where ((djlx='700' and qllx='99') or (djlx='700' and qllx='23')) and djdyid in (select djdyid from bdck.bdcs_djdy_xz where bdcdyid = '"
					+ bdcdyid + "') ";
			// "select qlid from bdck.bdcs_ql_xz where ((djlx='700' and
			// qllx='99') or (djlx='700' and qllx='23')) and djdyid in (select
			// djdyid from bdck.bdcs_djdy_xz where bdcdyid = '"+bdcdyid+"') ";
			List<Map> ql_XZs = baseCommonDao.getDataListByFullSql(condition);
			if (ql_XZs != null && ql_XZs.size() > 0) {
				String qlidString = "";
				for (int n = 0; n < ql_XZs.size(); n++) {
					String qlid = ql_XZs.get(n).get("QLID").toString();
					qlidString += qlid + ",";
				}
				qlidString = qlidString.substring(0, qlidString.length() - 1);
				dyservice.removeBDCDY(xmbh, qlidString);
				dyservice.addBDCDYNoCheck(xmbh, qlidString);
				listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
				bLoadQLR = false;
				// }
			}
		}

		if (listdjdy != null && listdjdy.size() > 0) {
			for (int i = 0; i < listdjdy.size(); i++) {
				// 预告或预抵
				String sql = "";
				List<Rights> bdcRights = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
				if (bdcRights != null && bdcRights.size() > 0) {
					for (int k = 0; k < bdcRights.size(); k++) {
						Rights _rights = bdcRights.get(k);// RightsTools.loadRightsByDJDYID(DJDYLY.GZ,
															// xmbh,
															// listdjdy.get(i).getDJDYID());
						if (_rights == null) {
							continue;
						}
						// 保存义务人到申请人表
						SaveJYYWRDLR(xmbh, jyqlrList);
						if (bLoadQLR) {
							// 加载权利人
							handler.addQLRbySQRArray(_rights.getId(), sqrids);
						}

						SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
						if (_subRights == null) {
							continue;
						}
						String rights_bookno = "";
						if (_rights.getARCHIVES_BOOKNO() != null) {
							rights_bookno = _rights.getARCHIVES_BOOKNO();
						}
						// 考虑并案流程，根据登记类型和权利类型去对应的权利和附属权利
						List<Object> ql_fsqlList = getMatchQLFSQL(qlList, fsqlList, _rights.getDJLX(),
								_rights.getQLLX(), bdcdyhList.get(0), fwzt, rights_bookno);
						if (ql_fsqlList.size() < 1) {
							continue;
						}
						BDCS_QL_GZ ql = (BDCS_QL_GZ) ql_fsqlList.get(0);
						BDCS_FSQL_GZ fsql_GZ = (BDCS_FSQL_GZ) ql_fsqlList.get(1);
						SaveQL(ql, fsql_GZ, casenum, _rights, _subRights);
						
						flag = "true";
						/**
						 * 因齐齐哈尔房产公司relationid需要更新
						 * 故在利用原Relationid抽取完毕后取出共享交易库H表【此处未更新BGQH】中NRelarionid，并更新不动产库工作层户的Relarionid
						 * 特殊情况：退件重新读取时无法识别原Relationid，直接以NRelationid作为抽取结果
						 * 2016年8月31日 20:53:19 卜晓波
						 */
						String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
						if (xzqdm.equals("230200") && getFieldExist("gxjyk", "h", "NRELATIONID")) {
							Connection jyConnection = null;
							try {
								if (jyConnection == null || jyConnection.isClosed()) {
									jyConnection = JH_DBHelper.getConnect_jy();
								}
							} catch (SQLException e1) {
							}
							// 从casenum发起去找当前业务新旧relationid的关系
							String gxjhxmsql = "select GXXMBH from gxjyk.gxjhxm where CASENUM= '" + casenum + "'";
							PreparedStatement pstmt = jyConnection.prepareStatement(gxjhxmsql);
							ResultSet GXJHXMrst = pstmt.executeQuery();
							// ResultSet
							// GXJHXMrst=JH_DBHelper.excuteQuery(jyConnection,
							// gxjhxmsql);
							if (GXJHXMrst != null) {
								if (GXJHXMrst.next()) {
									String gxxmbh = GXJHXMrst.getString("GXXMBH");
									// 取NRELATIONID不能用model取，因为此处专门为齐齐哈尔设计
									String NRELATIONID = "";
									sql = "select NRELATIONID from gxjyk.h where GXXMBH= '" + gxxmbh + "'";
									PreparedStatement pstmt2 = jyConnection.prepareStatement(sql);
									ResultSet NRELATIONIDrst = pstmt2.executeQuery();
									// ResultSet
									// NRELATIONIDrst=JH_DBHelper.excuteQuery(jyConnection,
									// sql);
									if (NRELATIONIDrst != null) {
										if (NRELATIONIDrst.next()) {
											NRELATIONID = NRELATIONIDrst.getString("NRELATIONID");
											// 更新现状层户RELATIONID
											if (fwzt.equals("2")) {
												List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class,
														"RELATIONID= '" + bdcdyhList.get(0) + "'");
												// 非退件情况才做更新
												if (h_xzs != null && h_xzs.size() > 0) {
													BDCS_H_XZ h_xz = h_xzs.get(0);
													h_xz.setRELATIONID(NRELATIONID);
													baseCommonDao.update(h_xz);
												}
											} else {
												List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(BDCS_H_XZY.class,
														"RELATIONID= '" + bdcdyhList.get(0) + "'");
												// 非退件情况才做更新
												if (h_xzys != null && h_xzys.size() > 0) {
													BDCS_H_XZY h_xzy = h_xzys.get(0);
													h_xzy.setRELATIONID(NRELATIONID);
													baseCommonDao.update(h_xzy);
												}
											}
										}
										pstmt2.close();
										NRELATIONIDrst.close();
									}
								}
								pstmt.close();
								GXJHXMrst.close();
							}
						}
					}
				}
				// }
			}
		}
		baseCommonDao.flush();
		return flag;
	}

	private void SaveYWRDLR(String xmbh, Map<String, List<BDCQLR>> qlrList) {
		if (qlrList.get("1") != null && qlrList.get("1").size() > 0) {
			String sqrHql = MessageFormat.format("XMBH=''{0}'' AND SQRLB=''{1}''", xmbh, "2");
			List<BDCS_SQR> sqr = baseCommonDao.getDataList(BDCS_SQR.class, sqrHql);
			sqr.get(0).setDLRXM(qlrList.get("1").get(0).getDLRXM());
			sqr.get(0).setDLRLXDH(qlrList.get("1").get(0).getDLRLXDH());
			sqr.get(0).setDLRZJHM(qlrList.get("1").get(0).getDLRZJHM());
			sqr.get(0).setDLRZJLX(qlrList.get("1").get(0).getDLRZJLX());
			baseCommonDao.update(sqr.get(0));
			baseCommonDao.flush();
		}
	}

	// 交易统一存义务人
	private void SaveJYYWRDLR(String xmbh, List<JYQLR> jyqlrList) {
		if (jyqlrList != null && jyqlrList.size() > 0) {
			for (JYQLR jyqlr : jyqlrList) {
				if (jyqlr.getSQRLB().trim().equals("2")) {
					String sqrHql = MessageFormat.format("XMBH=''{0}'' AND SQRXM=''{1}'' and sqrlb='2'", xmbh,
							jyqlr.getQLRMC());
					List<BDCS_SQR> sqr = baseCommonDao.getDataList(BDCS_SQR.class, sqrHql);
					// 登记库如果查到了，用中间库更新登记，如果没有，新增进登记库
					String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");//延吉市不要把义务人本人抽取到代理人中
					if (sqr != null && sqr.size() > 0 &&!xzqhdm.equals("222401")) {
						sqr.get(0).setDLRXM(jyqlr.getQLRMC());
						sqr.get(0).setDLRLXDH(jyqlr.getDH());
						sqr.get(0).setDLRZJHM(jyqlr.getZJH());
						sqr.get(0).setDLRZJLX(jyqlr.getZJZL());
						baseCommonDao.saveOrUpdate(sqr.get(0));
						// baseCommonDao.flush(); //统一放外面提交
					} else {
						BDCS_SQR sqr2 = new BDCS_SQR();
						sqr2.setSQRLB("2");
						sqr2.setXMBH(xmbh);
						sqr2.setSQRXM(jyqlr.getQLRMC());
						sqr2.setZJH(jyqlr.getZJH());
						sqr2.setXB(jyqlr.getXB());
						sqr2.setZJLX(jyqlr.getZJZL());
						sqr2.setGJDQ(jyqlr.getGJ());
						sqr2.setHJSZSS(jyqlr.getHJSZSS());
						sqr2.setGZDW(jyqlr.getGZDW());
						sqr2.setLXDH(jyqlr.getDH());
						sqr2.setTXDZ(jyqlr.getDZ());
						sqr2.setYZBM(jyqlr.getYB());
						sqr2.setDZYJ(jyqlr.getDZYJ());
						sqr2.setSQRLX(jyqlr.getQLRLX());
						sqr2.setQLBL(jyqlr.getQLBL());
						sqr2.setGYFS(jyqlr.getGYFS());
						baseCommonDao.save(sqr2);
						// baseCommonDao.flush(); //统一放外面提交
					}
				}
			}
			baseCommonDao.flush();
		}
	}

	private void SaveQL(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String casenum, Rights _rights, SubRights _subRights) {
		_rights.setCASENUM(casenum);
		_subRights.setCASENUM(casenum);
		if (ql != null) {
			_rights.setQLQSSJ(ql.getQLQSSJ());
			_rights.setQLJSSJ(ql.getQLJSSJ());
			_rights.setQDJG(ql.getQDJG());
			_rights.setFJ(ql.getFJ());
			_rights.setARCHIVES_BOOKNO(ql.getARCHIVES_BOOKNO());
			if(djyy!=null&&!"".equals(djyy)&&!djyy.equalsIgnoreCase("null")){
				_rights.setDJYY(djyy);
			}
		}
		if (fsql != null) {
			_subRights.setDYR(fsql.getDYR());
			_subRights.setZJJZWDYFW(fsql.getZJJZWDYFW());
			_subRights.setDYFS(fsql.getDYFS());
			_subRights.setZXSJ(fsql.getZXSJ());
			_subRights.setFDCJYJG(fsql.getFDCJYJG());
			_subRights.setCFWH(fsql.getCFWH());
			_subRights.setCFJG(fsql.getCFJG());
			if (fsql.getZGZQSE() != null && fsql.getZGZQSE() > 0) {
				_subRights.setZGZQSE(fsql.getZGZQSE());
			}
			if (fsql.getBDBZZQSE() != null && fsql.getBDBZZQSE() > 0) {
				_subRights.setBDBZZQSE(fsql.getBDBZZQSE());
			}
		}
		baseCommonDao.save(_rights);
		baseCommonDao.save(_subRights);
//		 baseCommonDao.flush(); //统一放外面提交
	}

	// 解封
	private String InsertJF(BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<String> relatioinidList, String fwzt) {
		String flag = "false";
		// 获取行政区名称和简称
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String xzqmc = ConfigHelper.getNameByValue("XZQHMC");
		String xzqjc = getXZQJC(xzqhdm);
		String condition = " CFWH = '" + fsql.getCFWH() + "'";
		List<SubRights> subRights = RightsTools.loadSubRightsByCondition(DJDYLY.XZ, condition);

		if (subRights != null && subRights.size() > 0) {
			for (SubRights subright : subRights) {
				Rights rights = RightsTools.loadRights(DJDYLY.XZ, subright.getQLID());
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				handler.addBDCDY(rights.getId());
				baseCommonDao.flush();
				List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
				if (list != null && list.size() > 0) {
					for (Rights tmepQl : list) {
						tmepQl.setCASENUM(casenum);
						baseCommonDao.update(tmepQl);
					}
				}
				flag = "true";
			}

			// 如果解封的户数和查封户数不一致，去掉多余的
			if (relatioinidList.size() != subRights.size()) {
				String relationids = "", bdcdyhs = "", djdyids = "";
				for (String id : relatioinidList) {
					relationids += "'" + id + "',";
				}
				// 查出要解封的户
				String sql = "relationid  in (" + relationids.substring(0, relationids.length() - 1) + ")";
				if (fwzt.equals("1")) {
					// 预测户
					// List<BDCS_H_GZY>
					// h_GZs=baseCommonDao.getDataList(BDCS_H_GZY.class, sql);
					// if(h_GZs!=null ){
					// for(BDCS_H_GZ h_GZ:h_GZs){
					// bdcdyhs+="'"+h_GZ.getBDCDYH()+"',";
					// }
					// }
				} else {
					// 实测户
					List<BDCS_H_XZ> h_GZs = baseCommonDao.getDataList(BDCS_H_XZ.class, sql);
					if (h_GZs != null) {
						for (BDCS_H_XZ h_GZ : h_GZs) {
							bdcdyhs += "'" + h_GZ.getBDCDYH() + "',";
						}
						// 删除户
						// sql="relationid NOT in ("+relationids.substring(0,
						// relationids.length()-1)+") and xmbh='"+xmbh+"'";
						// baseCommonDao.deleteEntitysByHql(BDCS_H_GZ.class,
						// sql);
					}
				}
				sql = "BDCDYH  in (" + bdcdyhs.substring(0, bdcdyhs.length() - 1) + ") and xmbh='" + xmbh + "'";
				List<BDCS_DJDY_GZ> djdy_GZs = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
				if (djdy_GZs != null) {
					for (BDCS_DJDY_GZ h_GZ : djdy_GZs) {
						djdyids += "'" + h_GZ.getDJDYID() + "',";
					}
					// 删除登记单元
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, sql);
					// 删除权利
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_QL_GZ.class, sql);
					// 删除附属权利
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_FSQL_GZ.class, sql);
				}
			}

		}
		return flag;
	}

	// 初始登记
	private String InsertCSDJ(String xmbh, String casenum) {
		String flag = "false";
		List<Gxjhxm> gxjhxms = CommonDaoJY.getDataList(Gxjhxm.class,
				"casenum = '" + casenum + "' and (gxlx!='99' or gxlx is not null) ");
		if (gxjhxms != null && gxjhxms.size() > 0) {
			// 先从GXJYK中查出该casenum所有的relationID
			List<String> relationids = new ArrayList<String>();
			for (int i = 0; i < gxjhxms.size(); i++) {
				List<JYH> jyhs = CommonDaoJY.getDataList(JYH.class, "GXXMBH='" + gxjhxms.get(i).getGxxmbh() + "'");
				if (!relationids.contains(jyhs.get(0).getRELATIONID())) {
					relationids.add(jyhs.get(0).getRELATIONID());
				}
			}
			// 从调查库中查出所有的bdcdyid
			String bdcdyidString = "";
			for (int i = 0; i < relationids.size(); i++) {
				List<DCS_H_GZ> dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class,
						"relationid='" + relationids.get(i) + "'");
				bdcdyidString += dcs_H_GZs.get(0).getId() + ",";
			}
			if (bdcdyidString.length() > 1) {
				bdcdyidString = bdcdyidString.substring(0, bdcdyidString.length() - 1);
				// 添加单元到登记库
				dyservice.addBDCDYNoCheck(xmbh, bdcdyidString);
				flag = "true";
			}
		}
		return flag;
	}

	// 抵押注销
	private String InsertDYZX(BDCS_QL_GZ ql, String xmbh, String casenum) {
		String flag = "false";
		// 获取行政区名称和简称
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String xzqmc = ConfigHelper.getNameByValue("XZQHMC");
		String xzqjc = getXZQJC(xzqhdm);
		String qzhFull = ql.getBDCQZH();
		if (qzhFull != null) {
			String[] qzhs = qzhFull.split(",");
			if (qzhs != null && qzhs.length > 0) {
				String condition = "1=1 ";
				for (int i = 0; i < qzhs.length; i++) {
					condition += "and BDCQZH like '%" + qzhs[i] + "%' ";
				}
				List<Rights> _rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
				if (_rightsList == null || _rightsList.size() < 1) {
					if(xzqhdm.equals("130100")){
						condition = " BDCQZH like '%不动产权第%" + ql.getBDCQZH().substring(4) +"号%' or BDCQZH like '冀("+ql.getBDCQZH().substring(0, 4)+")石家庄市不动产证明第%" + ql.getBDCQZH().substring(4)+ "号%'";
					}else{
						condition = " BDCQZH like '%不动产权第%" + ql.getBDCQZH().substring(4) + "号%'  or BDCQZH like '%不动产证明第%" + ql.getBDCQZH().substring(4) + "号%'";
					}					
					_rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
				}

				if (_rightsList != null && _rightsList.size() > 0) {
					for (Rights rights : _rightsList) {
						String qlid = rights.getId();
//						DJHandler handler = HandlerFactory.createDJHandler(xmbh);
//						handler.addBDCDY(qlid);
						dyservice.removeBDCDY(xmbh, qlid);
						ResultMessage rs= dyservice.addBDCDYNoCheck(xmbh, qlid);
						baseCommonDao.flush();
						List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
						if (list != null && list.size() > 0) {
							for (Rights tmepQl : list) {
								tmepQl.setCASENUM(casenum);
								baseCommonDao.update(tmepQl);
							}
						}
						flag = "true";
					}
				}
			}
		}
		return flag;
	}

	// 抵押注销，支持部分注销
	private String InsertDYZX(BDCS_QL_GZ ql, String xmbh, String casenum, List<String> fwidList, String fwzt) {
		String flag = "false";
		// 获取行政区名称和简称
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String xzqmc = ConfigHelper.getNameByValue("XZQHMC");
		String xzqjc = getXZQJC(xzqhdm);
		String qzhFull = ql.getBDCQZH();
		if (qzhFull != null) {
			String[] qzhs = qzhFull.split(",");
			if (qzhs != null && qzhs.length > 0) {
				String condition = "1=1 ";
				for (int i = 0; i < qzhs.length; i++) {
					condition += "and BDCQZH like '%" + qzhs[i] + "%' ";
				}
				// 根据fwid获取DJDYID
				String djdyidsString = getDJDYIDsByFWIDs(fwidList, fwzt);
				condition += " and djdyid in " + djdyidsString;
				List<Rights> _rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);

				if (_rightsList == null || _rightsList.size() < 1) {
					condition = " BDCQZH like '%不动产权第00" + ql.getBDCQZH().substring(4) + "号%'";
					condition += " and djdyid in " + djdyidsString;
					_rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
				}

				if (_rightsList != null && _rightsList.size() > 0) {
					for (Rights rights : _rightsList) {
						String qlid = rights.getId();
						DJHandler handler = HandlerFactory.createDJHandler(xmbh);
						handler.addBDCDY(qlid);
						baseCommonDao.flush();
						List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
						if (list != null && list.size() > 0) {
							for (Rights tmepQl : list) {
								tmepQl.setCASENUM(casenum);
								baseCommonDao.update(tmepQl);
							}
						}
						flag = "true";
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 乌鲁木齐首次登记第一版
	 * 
	 * @Title: 首次登记
	 * @author:Kongdebo
	 * @date：2017年3月5日 @return flag
	 */
	private String InsertSCDJ(String xmbh, String casenum, List<String> bdcdyhList, boolean bool, List<BDCQLR> qlrList,
			List<BDCS_QL_GZ> qlList, List<String> casenumList, List<BDCS_FSQL_GZ> fsqlList, String fwzt) {
		String flag = "false";
		String bdcdyid1 = "";
		String bdcdyidString = "";
		// 先从GXJYK中查出该casenum所有的relationID
		for (String bdcdyh : bdcdyhList) {
			String hql = " relationid = '" + bdcdyh + "' ";
			// 从调查库中查出所有的bdcdyid
			List<DCS_H_GZ> dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class, hql);
			bdcdyidString += dcs_H_GZs.get(0).getId() + ",";
		}
		if (bdcdyidString.length() > 1) {
			bdcdyidString = bdcdyidString.substring(0, bdcdyidString.length() - 1);
			// 添加单元到登记库
			dyservice.addBDCDYNoCheck(xmbh, bdcdyidString);
		}

		if (bool) {
			String bdcdyidArr = "";
			Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			// 保存申请人到登记库
			for (String bdcdyh : bdcdyhList) {
				String hql = " relationid = '" + bdcdyh + "' ";

				if (fwzt.equals("2")) {
					List<BDCS_H_GZ> h_gz = baseCommonDao.getDataList(BDCS_H_GZ.class, hql);
					if (h_gz.size() < 1) {
						flag = "warning:房产推送数据中单元号为：" + bdcdyh + " 的房屋状态与当前业务类型不符，请检测！";
						return flag;
					}
					bdcdyid1 = h_gz.get(0).getId();
				} else if (fwzt.equals("1")) {
					// 此处原因:1、房产房屋状态推送错误【本该是现房2的推送成1】2、该预测户未落宗
					List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
					if (h_xzs.size() < 1) {
						flag = "warning" + bdcdyh;
						return flag;
					}
					bdcdyid1 = h_xzs.get(0).getId();
				}

				BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcdyid1);
				if (_srcUnit == null) {
					continue;
				}

				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				String bdcdyid = _srcUnit.getId();
				String bdcdyh2 = _srcUnit.getBDCDYH();//
				dyservice.removeBDCDY(xmbh, bdcdyid);
				dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
				String str = MessageFormat.format("XMBH=''{0}'' AND (BDCDYID=''{1}'' OR BDCDYH=''{2}'')", xmbh, bdcdyid,
						bdcdyh2);
				List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);//
				boolean bLoadQLR = true;// true;//是否加载权利人，换补证addBDCDYNoCheck时自动加载权利人了，不能重复添加

				if (listdjdy == null || listdjdy.size() == 0) {
					String condition = "select qlid from bdck.bdcs_ql_xz where djdyid in (select djdyid from bdck.bdcs_djdy_xz where bdcdyid = '"
							+ bdcdyid + "') ";
					List<Map> ql_XZs = baseCommonDao.getDataListByFullSql(condition);
					if (ql_XZs != null && ql_XZs.size() > 0) {
						String qlid = ql_XZs.get(0).get("QLID").toString();
						dyservice.removeBDCDY(xmbh, qlid);
						dyservice.addBDCDYNoCheck(xmbh, qlid);
						listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
						// bLoadQLR = false;
					}
				}
				// 考虑到并案流程，一次可能有多条数据
				List<Rights> bdcRights = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");

				if (bdcRights != null && bdcRights.size() > 0) {
					for (int k = 0; k < bdcRights.size(); k++) {
						Rights _rights = bdcRights.get(k);
						if (_rights == null) {
							continue;
						}
						_rights.setCASENUM(casenum);
						baseCommonDao.update(_rights);
						if (bLoadQLR) {
							// 加载权利人
							handler.addQLRbySQRArray(_rights.getId(), sqrids);
						}
						flag = "true";
					}
				}

				if (bdcdyidArr.equals("")) {
					bdcdyidArr += bdcdyid;
				} else {
					bdcdyidArr += "," + bdcdyid;
				}
			}
			// 增加审核环节
			ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
			if (rltMessage.getSuccess().equals("false")) {
				return rltMessage.getMsg();
			} else if (rltMessage.getSuccess().equals("warning")) {
				return "警告：" + rltMessage.getMsg();
			}
		}

		baseCommonDao.flush();
		return flag;
	}
	// 根据fwid获取对应的DJDYID，格式为:('1','2',...)
	protected String getDJDYIDsByFWIDs(List<String> fwidList, String fwzt) {
		String djdyids = "'0',";
		if (fwidList != null && fwidList.size() > 0) {
			// String[] fwStrings=(String[])fwidList.toArray();
			String[] fwStrings = fwidList.toArray(new String[fwidList.size()]);
			// String fwids=Arrays.toString(fwStrings).replaceAll("[", "");
			String fwids = Arrays.toString(fwStrings);
			fwids = fwids.replace("[","");
			fwids = fwids.replace("]", "");// a,b,c,d
			fwids = fwids.replace(",", "','");
			fwids = "('" + fwids + "')";
			fwids=fwids.replace(" ", "");
			String sql = "";
			if ("1".equals(fwzt)) {
				// 期房
				sql = "select djdyid from bdck.bdcs_djdy_xz where bdcdyid in (select bdcdyid from bdck.bdcs_h_xzy where relationid in "
						+ fwids + ")";
			} else {
				// 现房
				sql = "select djdyid from bdck.bdcs_djdy_xz where bdcdyid in (select bdcdyid from bdck.bdcs_h_xz where relationid in "
						+ fwids + ")";
			}
			List<Map> djdyList = baseCommonDao.getDataListByFullSql(sql);//{DJDYID=YCH51529014-9985}
			if (djdyList != null && djdyList.size() > 0) {
				for (Map djdy : djdyList) {
					djdyids += "'" + djdy.get("DJDYID") + "',";
				}
			}

		}
		return "(" + djdyids.substring(0, djdyids.length() - 1) + ")";
	}

	/**
	 * 根据DB中的权利人信息保存申请人信息
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年8月8日下午4:46:17
	 * @param qlrs
	 * @param xmbh
	 */
	private Object[] SaveBDCS_SQR(List<BDCQLR> qlrs, String xmbh) {
		List<String> sqrids = new ArrayList<String>();

		// String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		// baseCommonDao.deleteEntitysByHql(BDCS_SQR.class, xmbhcond);
		// baseCommonDao.flush();
		String gyfString="0";//单独所有
		if(qlrs!=null &&qlrs.size()>1){
			gyfString="1";//共同所有
		}
		for (BDCQLR _qlr : qlrs) {
			BDCS_SQR sqr = new BDCS_SQR();
			sqr.setSQRXM(_qlr.getQLRMC());
			sqr.setTXDZ(_qlr.getDZ());
			sqr.setFDDBR(_qlr.getFDDBR());
			sqr.setXB(_qlr.getXB());
			sqr.setSQRLB("1");
			sqr.setSQRLX(_qlr.getQLRLX());
			sqr.setISCZR(_qlr.getISCZR());
			sqr.setZJH(_qlr.getZJH());// 证件号码
			sqr.setZJLX(_qlr.getZJZL());
			sqr.setGYFS(_qlr.getGYFS());
			sqr.setLXDH(_qlr.getDH());
			sqr.setXMBH(xmbh);
			sqr.setDLRXM(_qlr.getDLRXM());
			sqr.setDLRLXDH(_qlr.getDLRLXDH());
			sqr.setDLRZJHM(_qlr.getDLRZJHM());
			sqr.setDLRZJLX(_qlr.getDLRZJLX());
			if(sqr.getGYFS()==null||"".equals(sqr.getGYFS())){
				sqr.setGYFS(gyfString);
			}
			baseCommonDao.save(sqr);
			baseCommonDao.flush();
			sqrids.add(sqr.getId());
		}
		return sqrids.toArray();
	}

	@Override
	public void InsertFJFromZJK(String proInstId, String fileName, String configFilePath, byte[] buf, int i)
			throws Exception {
		Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proInstId);
		SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");// 设置日期格式
		String first = fir.format(inst.getCreat_Date());
		SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
		String second = sec.format(inst.getCreat_Date());
		// 从配置文件中取出待生成图像文件夹地址
		String fileSavePath = ConfigHelper.getNameByValue("material") + first + "\\" + second + "\\" + proInstId;
		// 服务存储路径配置
		String fileServerSavePath = first + "\\" + second + "\\" + proInstId;
		// 房产文件类型，不动产文件类型
		String defaultFileType = "其他证明材料";
		String fileType = FindFileType(proInstId, fileName, configFilePath, defaultFileType);
		String materilinstId = GetMaterilinstId(proInstId, fileType, defaultFileType);
		SaveImage(fileName, buf, i, fileSavePath, fileServerSavePath, materilinstId);
	}

	private String FindFileType(String proInstId, String fileName, String configFilePath, String defaultFileType)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String proInstsql = " select FILE_NUMBER from BDC_WORKFLOW.WFI_PROINST t where t.proinst_id = '" + proInstId
				+ "' ";
		List<Map> proInstList = commonDao.getDataListByFullSql(proInstsql);
		if (proInstList != null && proInstList.size() > 0) {
			Map proInst = proInstList.get(0);
			String fileNumber = proInst.get("FILE_NUMBER").toString();
			String xmxxsql = " select QLLX from BDCK.BDCS_XMXX t where t.PROJECT_ID = '" + fileNumber + "' ";
			List<Map> xmxxList = commonDao.getDataListByFullSql(xmxxsql);
			if (xmxxList != null && xmxxList.size() > 0) {
				Map xmxx = xmxxList.get(0);
				String qllx = xmxx.get("QLLX").toString();
				String s = "";
				if (qllx.equals(QLLX.DIYQ.Value)) {
					s = StringHelper.readFile(configFilePath + "WEB-INF\\classes\\抵押收件资料匹配.txt");
				} else {
					s = StringHelper.readFile(configFilePath + "WEB-INF\\classes\\转移收件资料匹配.txt");
				}
				map = StringHelper.JsonToMap(s);
			}
		}
		String fileType = "";
		if (map != null && map.get(fileName) != null) {
			fileType = map.get(fileName);
		} else {
			fileType = defaultFileType;
		}
		return fileType;
	}

	private void SaveImage(String fileName, byte[] buf, int i, String fileSavePath, String fileServerSavePath,
			String materilinstId) throws Exception {
		String filePath = "";
		String serverPath = "";
		// 根据文件存储配置选择对应方法存储图片：1、本地存储；2、服务存储
		String materialtype = ConfigHelper.getNameByValue("materialtype");
		if (materialtype == null || materialtype.equals("") || materialtype.trim().equals("1")) {
			// 存放在materdata表中的路径
			String strMaterdataPathString = fileSavePath + "\\" + materilinstId;
			File file = new File(strMaterdataPathString);
			if (!file.exists()) {
				file.mkdirs();
			}
			filePath = strMaterdataPathString + "\\" + i + fileName + ".jpg";
			serverPath = fileServerSavePath + "\\" + materilinstId;
			ByteToImage(buf, filePath);
			filePath = i + fileName + ".jpg";
		} else if (materialtype.trim().equals("3")) {
			serverPath = fileServerSavePath + "\\" + materilinstId;
			String fileName1 = i + fileName + ".jpg";
			ByteToImage(buf, "C:\\temp.jpg");
			File tempfile = new File("C:\\temp.jpg");
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
			fileName1 = df.format(new Date()) + "_" + java.net.URLEncoder.encode(fileName1, "UTF-8");
			Http.postFile(tempfile, fileName1, serverPath);
			filePath = fileName1;
			DeleteFile("C:\\temp.jpg");
		}
		fileName = fileName + ".jpg";
		Wfi_MaterData materdata = new Wfi_MaterData();
		materdata.setMaterialdata_Id(Common.CreatUUID());
		materdata.setMaterilinst_Id(materilinstId);
		materdata.setFile_Name(fileName);
		materdata.setFile_Path(filePath);
		materdata.setFile_Index((short) i);
		materdata.setUpload_Date(new Date());
		materdata.setPath(serverPath);
		commonDao.save(materdata);
		Wfi_ProMater promater = commonDao.get(Wfi_ProMater.class, materilinstId);
		promater.setImg_Path(materdata.getMaterialdata_Id());
		promater.setMaterial_Status(3);
		commonDao.update(promater);
		commonDao.flush();
	}

	private String GetMaterilinstId(String proinstid, String file_Name, String fileType) {
		String materilinstid = "";
		String sql1 = "select MATERILINST_ID from bdc_workflow.wfi_promater where MATERIAL_NAME='" + file_Name
				+ "' and PROINST_ID ='" + proinstid + "'";
		List<Map> lists = commonDao.getDataListByFullSql(sql1);
		if (lists != null && lists.size() > 0) {
			Map mater = lists.get(0);
			materilinstid = mater.get("MATERILINST_ID").toString();
		} else {
			String sql2 = "select MATERILINST_ID from bdc_workflow.wfi_promater where MATERIAL_NAME='" + fileType
					+ "' and PROINST_ID ='" + proinstid + "'";
			List<Map> promaters = commonDao.getDataListByFullSql(sql2);
			if (promaters != null && promaters.size() > 0) {
				Map promater = promaters.get(0);
				materilinstid = promater.get("MATERILINST_ID").toString();
			}
		}
		return materilinstid;
	}

	private void ByteToImage(byte[] data, String path) throws Exception {
		if (data.length < 3 || path.equals("")) {
			return;
		}
		FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
		imageOutput.write(data, 0, data.length);
		imageOutput.close();
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	private boolean DeleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 从共享交易库抽取到workflow库
	 * 
	 * @作者 likun
	 * @创建时间 2016年4月9日下午5:48:07
	 * @param proInstId
	 * @param materilinstId
	 * @param fileName
	 * @param buf
	 * @param i
	 * @throws Exception
	 */
	public void InsertFJFromZJKEx(String proInstId, String fileName, String materilinstId, String imgURL, int i,Blob imgTHUMB)
			throws Exception {
		Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proInstId);
		SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");// 设置日期格式
		String first = fir.format(inst.getCreat_Date());
		SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
		String second = sec.format(inst.getCreat_Date());
		// 从配置文件中取出待生成图像文件夹地址
		String fileSavePath = ConfigHelper.getNameByValue("material") + first + "\\" + second + "\\" + proInstId;
		// 服务存储路径配置
		String fileServerSavePath = first + "\\" + second + "\\" + proInstId;
		// 房产文件类型，不动产文件类型
		// String defaultFileType = "其他证明材料";
		// String fileType = FindFileType(proInstId, fileName, configFilePath,
		// defaultFileType);
		// String materilinstId = GetMaterilinstId(proInstId,
		// fileType,defaultFileType);
		SaveImage(fileName, imgURL, i, fileSavePath, fileServerSavePath, materilinstId,imgTHUMB);
	}

	/**
	 * 根据图片URL生成图片
	 * 
	 * @作者 think
	 * @创建时间 2016年4月17日下午3:54:10
	 * @param fileName
	 * @param imgURL
	 * @param i
	 * @param fileSavePath
	 * @param fileServerSavePath
	 * @param materilinstId
	 * @throws Exception
	 */
	private void SaveImage(String fileName, String imgURL, int i, String fileSavePath, String fileServerSavePath,
			String materilinstId,Blob imgTHUMB) throws Exception {
		String filePath = "";
		String serverPath = "";
		// 根据文件存储配置选择对应方法存储图片：1、本地存储；2、服务存储
		String materialtype = ConfigHelper.getNameByValue("materialtype");
		if (materialtype == null || materialtype.equals("") || materialtype.trim().equals("1")) {
			// 存放在materdata表中的路径
			String strMaterdataPathString = fileSavePath + "\\" + materilinstId;
			File file = new File(strMaterdataPathString);
			if (!file.exists()) {
				file.mkdirs();
			}
			filePath = strMaterdataPathString + "\\" + i + fileName + ".jpg";
			serverPath = fileServerSavePath + "\\" + materilinstId;
			// ByteToImage(buf, filePath);
			imgURLToImg(imgURL, filePath,imgTHUMB);
			filePath = i + fileName + ".jpg";
		} else if (materialtype.trim().equals("3")) {
			serverPath = fileServerSavePath + "\\" + materilinstId;
			String fileName1 = i + fileName + ".jpg";
			// ByteToImage(buf, "C:\\temp.jpg");
			imgURLToImg(imgURL, "C:\\temp.jpg",imgTHUMB);
			File tempfile = new File("C:\\temp.jpg");
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
			fileName1 = df.format(new Date()) + "_" + java.net.URLEncoder.encode(fileName1, "UTF-8");

			Http.postFile(tempfile, fileName1, serverPath);
			filePath = fileName1;
			DeleteFile("C:\\temp.jpg");
		}
		fileName = fileName + ".jpg";
		Wfi_MaterData materdata = new Wfi_MaterData();
		materdata.setMaterialdata_Id(Common.CreatUUID());
		materdata.setMaterilinst_Id(materilinstId);
		materdata.setFile_Name(fileName);
		materdata.setFile_Path(filePath);
		materdata.setFile_Index((short) i);
		materdata.setUpload_Date(new Date());
		materdata.setPath(serverPath);
		commonDao.save(materdata);
		Wfi_ProMater promater = commonDao.get(Wfi_ProMater.class, materilinstId);
		promater.setImg_Path(materdata.getMaterialdata_Id());
		promater.setMaterial_Status(3);
		commonDao.update(promater);
		commonDao.flush();
	}

	/*
	 * 根据图片的URL获取图片字节数组
	 */
	@SuppressWarnings("unused")
	public Boolean imgURLToImg(String srcimgUrl, String imgFilePath,Blob imgTHUMB) { // ,String
		Boolean boolean1 = false;
		byte[] buf = null;
		BufferedInputStream in = null;
		try {
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			if(xzqhdm.equals("222401")){
				//延吉 房产直接推到数据库里图片BLOB
				in = new BufferedInputStream(imgTHUMB.getBinaryStream());
			}else if(xzqhdm.equals("230200")){
				// 齐齐哈尔RELATIVEURL字段存的是前置机图片相对路径故走下面方法
				// 获取齐齐哈尔前置机IP地址 2016年9月7日 01:33:33 卜晓波 暂时写死
				String shareURL = ConfigHelper.getNameByValue("URL_SHARE");
				String IP = shareURL.substring(7, shareURL.length() - 16);
				// y="http://'"+IP+"':8080/share/resources/sharefj/'"+srcimgUrl+"'.jpg";
				String imgurl = "\\" + "\\" + IP + "\\" + "d$" + "\\photo" + "\\" + srcimgUrl + ".jpg";
				File file = new File(imgurl);
				if (file.exists()) {
					file = new File(imgurl);
					in = new BufferedInputStream(new FileInputStream(file));
				}
			}else{
				//服务
				URL url = new URL(srcimgUrl);
				InputStream stream = url.openStream();
				in = new BufferedInputStream(stream);
				//路径
				if(in==null){
					File file = new File(srcimgUrl);
					in = new BufferedInputStream(new FileInputStream(file));
				}
				//blob字段
				if(in==null){
					in = new BufferedInputStream(imgTHUMB.getBinaryStream());
				}
			}
			// 生成图片名
//			int index = srcimgUrl.lastIndexOf("/");
			// String sName = srcimgUrl.substring(index+1, srcimgUrl.length());
			// // 存放地址
			File img = new File(imgFilePath);
			// // 生成图片
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(img));
			buf = new byte[2048];
			// int len=readInt(in);
			// buf = new byte[len];
			int length = in.read(buf);
			while (length != -1) {
				out.write(buf, 0, length);
				length = in.read(buf);
			}
			in.close();
			out.close();
			boolean1 = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boolean1;
	}

	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年2月24日 下午3:01:13 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	 * @throws Exception 
	*/
	private InputStream BLOBtoInputstm(Blob blob) throws Exception {
		InputStream inStream = blob.getBinaryStream();
		byte[] buf = InputStreamToByte(inStream);
		InputStream inputstm=new ByteArrayInputStream(buf);
		return inputstm;
	} 
	protected byte[] InputStreamToByte(InputStream is) throws Exception {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			byteStream.write(ch);
		}
		byte imgData[] = byteStream.toByteArray();
		byteStream.close();
		return imgData;
	}
	/**
	 * 获取行政区简称
	 * 
	 * @作者 李堃
	 * @创建时间 2016年5月16日下午3:49:18
	 * @param xzqhdm
	 * @return
	 */
	protected String getXZQJC(String xzqhdm) {
		String jc = "";
		if (xzqhdm != null && !xzqhdm.equals("")) {
			String hql = MessageFormat.format(
					" CONSTSLSID IN (SELECT CONSTSLSID FROM BDCS_CONSTCLS WHERE CONSTCLSTYPE=''{0}'') AND CONSTVALUE=''{1}''",
					"SS", xzqhdm.substring(0, 2) + "0000");
			List<BDCS_CONST> lst1 = baseCommonDao.getDataList(BDCS_CONST.class, hql);

			if (lst1 != null && lst1.size() > 0) {
				jc = lst1.get(0).getBZ();
			}
		}
		return jc;
	}

	// 抵押通过casenum，从房产系统中获取抵押人
	private List<BDCQLR> getDYR(String casenum) throws SQLException {
		Connection connection = JH_DBHelper.getConnect_jy();
		List<BDCQLR> dyqlrlist = new ArrayList<BDCQLR>();
		// 抵押权利人信息OPR_LIENERINFO
		String DYQRsql = "select * from HOUSE.OPR_LIENERINFO where casenum ='" + casenum + "'";
		if (connection != null) {
			PreparedStatement pstmt = connection.prepareStatement(DYQRsql);
			ResultSet DYQRRSet = pstmt.executeQuery();
			// ResultSet DYQRRSet = JH_DBHelper.excuteQuery(connection,
			// DYQRsql);
			if (DYQRRSet != null) {
				if (DYQRRSet.next()) {
					BDCQLR qlr = new BDCQLR();
					qlr.setQLRMC(DYQRRSet.getString("LIENER"));// 权利人名称
					qlr.setFDDBR(DYQRRSet.getString("MANAGERNAME"));// 法定代表人
					qlr.setZJH(DYQRRSet.getString("LIENERIDNUM"));// 证件号码
					dyqlrlist.add(qlr);
				}
				pstmt.close();
				DYQRRSet.close();
			}
			connection.close();
		}

		return dyqlrlist;
	}

	// 用房产系统中权利人名称更新登记系统中申请人名称
	protected void updateSQRMC(String xmbh, List<BDCQLR> fcQLR) {
		if (fcQLR != null && fcQLR.size() > 0) {
			for (int i = 0; i < fcQLR.size(); i++) {
				if (fcQLR.get(i).getZJH() != null && !fcQLR.get(i).getZJH().equals("")) {
					String sqrHql = MessageFormat.format("XMBH=''{0}''  AND ZJHM=''{1}''", xmbh, fcQLR.get(i).getZJH());
					// 修改申请人表
					List<BDCS_SQR> sqr = baseCommonDao.getDataList(BDCS_SQR.class, sqrHql);
					if (sqr != null && sqr.size() > 0) {
						sqr.get(0).setDLRXM(fcQLR.get(i).getQLRMC());
						baseCommonDao.update(sqr.get(0));
						baseCommonDao.flush();
					}

					// 修改权利人工作表
					List<BDCS_QLR_GZ> qlr = baseCommonDao.getDataList(BDCS_QLR_GZ.class, sqrHql);
					if (qlr != null && qlr.size() > 0) {
						qlr.get(0).setDLRXM(fcQLR.get(i).getQLRMC());
						baseCommonDao.update(qlr.get(0));
						baseCommonDao.flush();
					}

				}

			}

		}
	}

	// 根据不动产单元号获取变更前户的relationid
	protected String getBGQRelationid(String bdcdy, List<JYJYBGQH> bgqhList) {
		String relationidString = "";
		if (bgqhList != null && bgqhList.size() > 0) {
			for (JYJYBGQH _bgqh : bgqhList) {
				if (_bgqh.getBDCDYID() != null && _bgqh.getBDCDYID().equals(bdcdy)) {
					relationidString = _bgqh.getRELATIONID();
					break;
				}
			}
		}
		return relationidString;
	}

	// 抵押注销
	private String checkDYZX(BDCS_QL_GZ ql, String xmbh, String casenum) {
		String flag = "false";
		// 获取行政区名称和简称
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String xzqmc = ConfigHelper.getNameByValue("XZQHMC");
		String xzqjc = getXZQJC(xzqhdm);
		String qzhFull = ql.getBDCQZH();
		if (qzhFull != null) {
			String[] qzhs = qzhFull.split(",");
			if (qzhs != null && qzhs.length > 0) {
				String condition = "1=1 ";
				for (int i = 0; i < qzhs.length; i++) {
					condition += "and BDCQZH like '%" + qzhs[i] + "%' ";
				}
				List<Rights> _rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
				if (_rightsList == null || _rightsList.size() < 1) {
					condition = " BDCQZH =" + xzqjc + "+ '(" + ql.getBDCQZH().substring(0, 4) + ")" + xzqmc + "不动产证明第"
							+ ql.getBDCQZH().substring(4) + "号'";
					_rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
				}

				if (_rightsList != null && _rightsList.size() > 0) {
					for (Rights rights : _rightsList) {
						String qlid = rights.getId();
						List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
						if (list != null && list.size() > 0) {
							flag = "true";
						}

					}
				}
			}
		}
		return flag;
	}

	// 解封
	private String checkJF(BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<String> relatioinidList, String fwzt) {
		String flag = "false";
		// 获取行政区名称和简称
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String xzqmc = ConfigHelper.getNameByValue("XZQHMC");
		String xzqjc = getXZQJC(xzqhdm);
		String condition = " CFWH = '" + fsql.getCFWH() + "'";
		List<SubRights> subRights = RightsTools.loadSubRightsByCondition(DJDYLY.XZ, condition);

		if (subRights != null && subRights.size() > 0) {
			for (SubRights subright : subRights) {
				Rights rights = RightsTools.loadRights(DJDYLY.XZ, subright.getQLID());
				// DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				// handler.addBDCDY(rights.getId());
				// baseCommonDao.flush();
				List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
				if (list != null && list.size() > 0) {
					for (Rights tmepQl : list) {
						tmepQl.setCASENUM(casenum);
						baseCommonDao.update(tmepQl);
					}
					flag = "true";
				}
			}

		}
		return flag;
	}

	// 初始登记
	private String checkCSDJ(String xmbh, String casenum) {
		String flag = "false";
		List<Gxjhxm> gxjhxms = CommonDaoJY.getDataList(Gxjhxm.class,
				"casenum = '" + casenum + "' and (gxlx!='99' or gxlx is not null) ");
		if (gxjhxms != null && gxjhxms.size() > 0) {
			// 先从GXJYK中查出该casenum所有的relationID
			List<String> relationids = new ArrayList<String>();
			for (int i = 0; i < gxjhxms.size(); i++) {
				List<JYH> jyhs = CommonDaoJY.getDataList(JYH.class, "GXXMBH='" + gxjhxms.get(i).getGxxmbh() + "'");
				if (!relationids.contains(jyhs.get(0).getRELATIONID())) {
					relationids.add(jyhs.get(0).getRELATIONID());
				}
			}
			// 从调查库中查出所有的bdcdyid
			String bdcdyidString = "";
			for (int i = 0; i < relationids.size(); i++) {
				List<DCS_H_GZ> dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class,
						"relationid='" + relationids.get(i) + "'");
				bdcdyidString += dcs_H_GZs.get(0).getId() + ",";
			}
			if (bdcdyidString.length() > 1) {
				bdcdyidString = bdcdyidString.substring(0, bdcdyidString.length() - 1);
				// //添加单元到登记库
				// dyservice.addBDCDYNoCheck(xmbh, bdcdyidString);
				flag = "true";
			}
		}
		return flag;
	}

	/**
	 * 判断字段是否存在
	 * 
	 * @作者 likun
	 * @创建时间 2016年8月30日下午2:54:31
	 * @param user
	 *            用户名
	 * @param tablename
	 *            表名
	 * @param fldName
	 *            字段名
	 * @return
	 */
	private boolean getFieldExist(String user, String tablename, String fldName) {
		boolean bExist = false;
		String sql = "SELECT COUNT(1)  FROM DBA_TAB_COLUMNS WHERE OWNER='" + user + "' AND TABLE_NAME='" + tablename
				+ "' AND COLUMN_NAME='" + fldName + "'";
		try {
			ResultSet rsResultSet = baseCommonDao.excuteQuery(sql);
			if (rsResultSet != null) {
				while (rsResultSet.next()) {
					int i = rsResultSet.getInt(1);
					if (i == 1) {
						bExist = true;
					}
					break;
				}
			}
			PreparedStatement PS = rsResultSet.unwrap(PreparedStatement.class);
			rsResultSet.close();
			if(PS!=null) {
				PS.close();
			}
		} catch (SQLException e) {
		}
		return bExist;
	}

	/**
	 * 获取djlx和qllx匹配的权利和附属权利
	 * 
	 * @作者 think
	 * @创建时间 2016年9月7日下午9:32:47
	 * @param ql_GZs
	 * @param fsql_GZs
	 * @param djlx
	 * @param qllx
	 * @return
	 */
	protected List<Object> getMatchQLFSQL(List<BDCS_QL_GZ> ql_GZs, List<BDCS_FSQL_GZ> fsql_GZs, String djlx,
			String qllx, String realtionid, String fwzt, String rights_bookno) {
		List<Object> ql_fsqlMap = new ArrayList();
		if (ql_GZs != null && fsql_GZs != null && ql_GZs.size() > 0 && fsql_GZs.size() > 0 && djlx != null
				&& qllx != null && !djlx.equals("") && !qllx.equals("")) {
			for (int i = 0; i < ql_GZs.size(); i++) {
				try {
					BDCS_QL_GZ ql = ql_GZs.get(i);
					if ("700".equals(djlx) && "2".equals(fwzt) && !"23".equals(ql.getQLLX())) {
						// 如果是现房预告，权利类型改成99
						ql.setQLLX("99");
					}
					if (qllx.equals(ql.getQLLX()) && realtionid.equals(ql.getARCHIVES_BOOKNO())
							&& (rights_bookno.equals("")
									|| (!rights_bookno.equals("") && rights_bookno.equals(ql.getARCHIVES_BOOKNO())))) {
						BDCS_FSQL_GZ fsql_GZ = getMatchFSQL(fsql_GZs, ql.getId());
						ql_fsqlMap.add(ql);
						ql_fsqlMap.add(fsql_GZ);
						break;
					}
				} catch (Exception e) {
				}
			}
		}
		return ql_fsqlMap;
	}

	/**
	 * 找出与权利对应的附属权利
	 * 
	 * @作者 think
	 * @创建时间 2016年9月7日下午9:27:25
	 * @param fsql_GZs
	 * @param qlid
	 * @return
	 */
	protected BDCS_FSQL_GZ getMatchFSQL(List<BDCS_FSQL_GZ> fsql_GZs, String qlid) {
		BDCS_FSQL_GZ fsql_GZ = null;
		if (fsql_GZs != null && fsql_GZs.size() > 0 && qlid != null && !qlid.equals("")) {
			for (int i = 0; i < fsql_GZs.size(); i++) {
				if (qlid.equals(fsql_GZs.get(i).getQLID())) {
					fsql_GZ = fsql_GZs.get(i);
					break;
				}
			}
		}
		return fsql_GZ;
	}

	/**
	 * 添加宗地单元
	 * 
	 * @author buxiaobo
	 * @date 2016年11月7日 20:51:22
	 * @param xmbh
	 *            项目编号 bdcdyidArr 宗地不动产单元ID数组
	 */
	public String DJAddZDDY(String xmbh, String[] bdcdyidArr) {
		return xmbh;
	}

	/**
	 * 从地籍接口抽取数据到不动产库
	 * 
	 * @author buxiaobo
	 * @date 2016年11月11日 08:40:45
	 */
	@Override
	public String InsertSXFromTD(List<BDCS_QL_GZ> qlList, List<BDCS_FSQL_GZ> fsqlList, String xmbh, String tDCasenum,
			List<BDCQLR> qlrList, List<JYQLR> jYQlrList, List<String> bdcdyhList) {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		// 业务类型判断
		String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
		String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 注销类业务、异议登记处理
			if (_handleClassName.contains("DYYGZXDJHandler") || _handleClassName.contains("DYZXDJHandler")
					|| _handleClassName.contains("YYDJHandler") || _handleClassName.contains("YYZXDJHandler")
					|| _handleClassName.contains("ZYYGZXDJHandler") || _handleClassName.contains("DYBGDJHandler")) {
				flag = InsertDYZX(qlList.get(0), xmbh, tDCasenum);
			} else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")) {// djlx.equals(DJLX.CFDJ.Value)&&qllx.equals("98")
				// 解封
				flag = InsertTDJF(fsqlList.get(0), xmbh, tDCasenum, bdcdyhList);
			} else if (_handleClassName.contains("CSDJHandler")) {
				// 初始登记
				flag = TDInsertCSDJ(xmbh, bdcdyhList);
				// 保存申请人到登记库
				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			} else {
				// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新
				if (true) {
					String bdcdyidArr = "";
					for (String bdcdyh : bdcdyhList) {
						String bdcdyid1 = "";
						List<Map> shyqzd_gz = baseCommonDao.getDataListByFullSql(
								"SELECT * FROM BDCDCK.BDCS_SHYQZD_GZ WHERE relationid='" + bdcdyh + "'");
						if (shyqzd_gz != null && shyqzd_gz.size() > 0) {
							if (shyqzd_gz.get(0).get("BDCDYID") != null
									&& !shyqzd_gz.get(0).get("BDCDYID").equals("")) {
								bdcdyid1 = shyqzd_gz.get(0).get("BDCDYID").toString();
							}
						} else {
							flag = "warning:地籍生成的新宗地ID为：" + bdcdyh + " 的宗地经权籍系统处理，请检测！";
							return flag;
						}
						BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
						if (_srcUnit == null) {
							continue;
						}
						String bdcdyid = _srcUnit.getId();
						if (bdcdyidArr.equals("")) {
							bdcdyidArr += bdcdyid;
						} else {
							bdcdyidArr += "," + bdcdyid;
						}
					}
					// 增加审核环节
					ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
					if (rltMessage.getSuccess().equals("false")) {
						return rltMessage.getMsg();
					} else if (rltMessage.getSuccess().equals("warning")) {
						return "警告：" + rltMessage.getMsg();
					}
				}
				// 不是转现类的才将权利人保存到申请人表中，否则会重复
				Object[] sqrids = null;
				if (_handleClassName.contains("ZY_YDYTODY_DJHandler")
						|| _handleClassName.contains("ZY_YGDYTODY_DJHandler")) {
				} else {
					sqrids = SaveBDCS_SQR(qlrList, xmbh);
				}
				BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				for (String relationid : bdcdyhList) {
					String bdcdyid1 = "";
					List<Map> shyqzd_gz = baseCommonDao.getDataListByFullSql(
							"SELECT * FROM BDCDCK.BDCS_SHYQZD_GZ WHERE relationid='" + relationid + "'");
					if (shyqzd_gz != null && shyqzd_gz.size() > 0) {
						if (shyqzd_gz.get(0).get("BDCDYID") != null && !shyqzd_gz.get(0).get("BDCDYID").equals("")) {
							bdcdyid1 = shyqzd_gz.get(0).get("BDCDYID").toString();
						}
					} else {
						flag = "warning:地籍生成的新宗地ID为：" + relationid + " 的宗地经权籍系统处理，请检测！";
						return flag;
					}
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
					if (_srcUnit == null) {
						continue;
					}
					String bdcdyid = _srcUnit.getId();
					String bdcdyh2 = _srcUnit.getBDCDYH();
					dyservice.removeBDCDY(xmbh, bdcdyid);
					dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
					String str = MessageFormat.format("XMBH=''{0}'' AND (BDCDYID=''{1}'' OR BDCDYH=''{2}'')", xmbh,
							bdcdyid, bdcdyh2);
					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					boolean bLoadQLR = false;// true;//是否加载权利人，换补证addBDCDYNoCheck时自动加载权利人了，不能重复添加
					// 如果用bdcdyid加载不到，要用qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						List<BDCS_DJDY_XZ> djdy_XZs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
								"BDCDYH='" + _srcUnit.getBDCDYH() + "'");
						if (djdy_XZs != null && djdy_XZs.size() > 0) {
							List<BDCS_QL_XZ> ql_XZs = baseCommonDao.getDataList(BDCS_QL_XZ.class,
									"BDCQZH='" + qlList.get(0).getBDCQZH() + "'");
							if (ql_XZs != null && ql_XZs.size() > 0) {
								dyservice.addBDCDYNoCheck(xmbh, ql_XZs.get(0).getId());
								listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
								bLoadQLR = false;
							}
						}
					}
					// 如果还为空，再用另一种方式qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						String condition = "select qlid from bdck.bdcs_ql_xz where djdyid in (select djdyid from bdck.bdcs_djdy_xz where bdcdyid = '"
								+ bdcdyid + "') ";
						List<Map> ql_XZs = baseCommonDao.getDataListByFullSql(condition);
						if (ql_XZs != null && ql_XZs.size() > 0) {
							String qlid = ql_XZs.get(0).get("QLID").toString();
							dyservice.removeBDCDY(xmbh, qlid);
							dyservice.addBDCDYNoCheck(xmbh, qlid);
							listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
							bLoadQLR = false;
						}
					}
					if (listdjdy != null && listdjdy.size() > 0) {
						for (int i = 0; i < listdjdy.size(); i++) {
							// 考虑到并案流程，一次可能有多条数据
							List<Rights> bdcRights = RightsTools.loadRightsByCondition(DJDYLY.GZ,
									"XMBH='" + xmbh + "'");
							if (bdcRights != null && bdcRights.size() > 0) {
								for (int k = 0; k < bdcRights.size(); k++) {
									Rights _rights = bdcRights.get(k);// RightsTools.loadRightsByDJDYID(DJDYLY.GZ,
																		// xmbh,
																		// listdjdy.get(i).getDJDYID());
									if (_rights == null) {
										continue;
									}
									SaveJYYWRDLR(xmbh, jYQlrList);
									if (bLoadQLR) {
										// 加载权利人
										handler.addQLRbySQRArray(_rights.getId(), sqrids);
									}

									SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ,
											_rights.getId());
									if (_subRights == null) {
										continue;
									}
									List<Object> ql_fsqlList = TDgetMatchQLFSQL(qlList, fsqlList, _rights.getDJLX(),
											_rights.getQLLX(), relationid);
									if (ql_fsqlList.size() < 1) {
										continue;
									}
									BDCS_QL_GZ ql = (BDCS_QL_GZ) ql_fsqlList.get(0);
									BDCS_FSQL_GZ fsql_GZ = (BDCS_FSQL_GZ) ql_fsqlList.get(1);
									SaveQL(ql, fsql_GZ, tDCasenum, _rights, _subRights);
									
									flag = "true";
								}
							}
						}
					}
				}
				baseCommonDao.flush();
			}
		}
		return null;
	}

	@Override
	public String InsertSXFromTDBG(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh, String tDCasenum,
			List<BDCQLR> qlrList, List<JYQLR> jyqlrList, List<String> bdcdyhList) {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新
			// 业务类型判断
			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";

			if (true) {
				String bdcdyidArr = "";
				// 检查新宗地是否经过权籍处理
				for (String relationid : bdcdyhList) {
					String bdcdyid1 = "";
					List<Map> shyqzd_gz = baseCommonDao.getDataListByFullSql(
							"SELECT * FROM BDCDCK.BDCS_SHYQZD_GZ WHERE relationid='" + relationid + "'");
					if (shyqzd_gz != null && shyqzd_gz.size() > 0) {
						if (shyqzd_gz.get(0).get("BDCDYID") != null && !shyqzd_gz.get(0).get("BDCDYID").equals("")) {
							bdcdyid1 = shyqzd_gz.get(0).get("BDCDYID").toString();
						}
					} else {
						flag = "warning:地籍生成的新宗地ID为：" + relationid + " 的宗地未经权籍系统处理，请检测！";
						return flag;
					}
					BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
					if (_srcUnit == null) {
						continue;
					}
					String bdcdyid = _srcUnit.getId();
					if (bdcdyidArr.equals("")) {
						bdcdyidArr += bdcdyid;
					} else {
						bdcdyidArr += "," + bdcdyid;
					}
				}
				// 增加审核环节，检查是否有抵押、查封等
				ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
				if (rltMessage.getSuccess().equals("false")) {
					return rltMessage.getMsg();
				} else if (rltMessage.getSuccess().equals("warning")) {
					return "警告：" + rltMessage.getMsg();
				}
			}
			// 将权利人保存到申请人表中
			Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			if (_handleClassName.contains("DYBGDJHandler") || _handleClassName.contains("BGDJHandler")
					|| _handleClassName.contains("GZDJHandler")) {
				/************** 此处为除变更、更正、初始登记业务的单元添加方法 **************/
				String bdcdyid = "", bdcdyid2 = "";
				for (String relationid : bdcdyhList) {
					List<Map> shyqzd_gz = baseCommonDao.getDataListByFullSql(
							"SELECT * FROM BDCDCK.BDCS_SHYQZD_GZ WHERE relationid='" + relationid + "'");
					if (shyqzd_gz != null && shyqzd_gz.size() > 0) {
						if (shyqzd_gz.get(0).get("BDCDYID") != null && !shyqzd_gz.get(0).get("BDCDYID").equals("")) {
							bdcdyid += shyqzd_gz.get(0).get("BDCDYID").toString() + ",";
							bdcdyid2 += "'" + shyqzd_gz.get(0).get("BDCDYID").toString() + ",";
						}
					}
				}
				bdcdyid = bdcdyid.substring(0, bdcdyid.length() - 1);
				bdcdyid2 = bdcdyid2.substring(0, bdcdyid2.length() - 1);
				// 备注：此块王哥说以后地籍不再走变更和更正 都走登记系统！！！！！！！！代码先保留
				// if (_handleClassName.contains("DYBGDJHandler")){
				//// DYBGDJHandler.addBDCDY(bdcdyid);
				// }else if(_handleClassName.contains("BGDJHandler")){
				// //变更后
				// dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
				//
				// //变更前
				// String bdcdyid3 = "";
				// for (JYJYBGQH _bgqh : bgqhList) {
				// //不动产库关联所有bdcdyid
				// List<BDCS_H_XZ>
				// bdcs_H_XZs=baseCommonDao.getDataList(BDCS_H_XZ.class,
				// "relationid='"+_bgqh.getRELATIONID()+"'");
				// bdcdyid3+=bdcs_H_XZs.get(0).getId()+",";
				// }
				// bdcdyid3=bdcdyid3.substring(0,bdcdyid3.length()-1);
				// dyservice.addBDCDYNoCheck(xmbh, bdcdyid3);
				// //BGDJHandler.addBDCDY(bdcdyid);
				// }else if(_handleClassName.contains("GZDJHandler")){
				//// GZDJHandler.addBDCDY(bdcdyid);
				//// //考虑到南宁房产的更正登记后relationid会变化，拷贝单元后，用更正后的relationid更新一下工作层的户relationid
				//
				// }else{}
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID in ({1}) )", xmbh, bdcdyid2);
				List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
				Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
				if (_rights == null) {
					// continue;
				}
				//保存义务人
				SaveJYYWRDLR(xmbh, jyqlrList);
				handler.addQLRbySQRArray(_rights.getId(), sqrids);
				SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
				if (_subRights == null) {
					// continue;
				}
				SaveQL(ql, fsql, tDCasenum, _rights, _subRights);
				
				baseCommonDao.flush();
				flag = "true";
			}
		}
		return flag;
	}

	// 土地初始登记
	private String TDInsertCSDJ(String xmbh, List<String> bdcdyhList) {
		String flag = "false";
		// 从调查库中查出所有的bdcdyid
		String bdcdyidString = "";
		for (int i = 0; i < bdcdyhList.size(); i++) {
			List<Map> shyqzd_gz = baseCommonDao.getDataListByFullSql(
					"SELECT * FROM BDCDCK.BDCS_SHYQZD_GZ WHERE relationid='" + bdcdyhList.get(i) + "'");
			if (shyqzd_gz != null && shyqzd_gz.size() > 0) {
				bdcdyidString += shyqzd_gz.get(0).get("BDCDYID").toString() + ",";
			}
		}
		if (bdcdyidString.length() > 1) {
			bdcdyidString = bdcdyidString.substring(0, bdcdyidString.length() - 1);
			// 添加单元到登记库
			dyservice.addBDCDYNoCheck(xmbh, bdcdyidString);
			flag = "true";
		}
		return flag;
	}

	// 土地解封
	private String InsertTDJF(BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<String> relatioinidList) {
		String flag = "false";
		String condition = " CFWH = '" + fsql.getCFWH() + "'";
		List<SubRights> subRights = RightsTools.loadSubRightsByCondition(DJDYLY.XZ, condition);
		if (subRights != null && subRights.size() > 0) {
			for (SubRights subright : subRights) {
				Rights rights = RightsTools.loadRights(DJDYLY.XZ, subright.getQLID());
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				handler.addBDCDY(rights.getId());
				baseCommonDao.flush();
				List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
				if (list != null && list.size() > 0) {
					for (Rights tmepQl : list) {
						tmepQl.setCASENUM(casenum);
						baseCommonDao.update(tmepQl);
					}
				}
				flag = "true";
			}

			// 如果解封的户数和查封户数不一致，去掉多余的
			if (relatioinidList.size() != subRights.size()) {
				String relationids = "", bdcdyhs = "", djdyids = "";
				for (String id : relatioinidList) {
					relationids += "'" + id + "',";
				}
				// 查出要解封的户
				String sql = "relationid  in (" + relationids.substring(0, relationids.length() - 1) + ")";
				List<Map> shyqzd_gz = baseCommonDao
						.getDataListByFullSql("SELECT * FROM BDCDCK.BDCS_SHYQZD_GZ WHERE " + sql + "");
				if (shyqzd_gz != null && shyqzd_gz.size() > 0) {
					bdcdyhs += shyqzd_gz.get(0).get("BDCDYH").toString() + ",";
				}

				sql = "BDCDYH  in (" + bdcdyhs.substring(0, bdcdyhs.length() - 1) + ") and xmbh='" + xmbh + "'";
				List<BDCS_DJDY_GZ> djdy_GZs = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
				if (djdy_GZs != null) {
					for (BDCS_DJDY_GZ h_GZ : djdy_GZs) {
						djdyids += "'" + h_GZ.getDJDYID() + "',";
					}
					// 删除登记单元
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, sql);
					// 删除权利
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_QL_GZ.class, sql);
					// 删除附属权利
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_FSQL_GZ.class, sql);
				}
			}

		}
		return flag;
	}

	/**
	 * 获取djlx和qllx匹配的权利和附属权利
	 * 
	 * @作者 think
	 * @创建时间 2016年9月7日下午9:32:47
	 * @param ql_GZs
	 * @param fsql_GZs
	 * @param djlx
	 * @param qllx
	 * @return
	 */
	protected List<Object> TDgetMatchQLFSQL(List<BDCS_QL_GZ> ql_GZs, List<BDCS_FSQL_GZ> fsql_GZs, String djlx,
			String qllx, String realtionid) {
		List<Object> ql_fsqlMap = new ArrayList();
		if (ql_GZs != null && fsql_GZs != null && ql_GZs.size() > 0 && fsql_GZs.size() > 0 && djlx != null
				&& qllx != null && !djlx.equals("") && !qllx.equals("")) {
			for (int i = 0; i < ql_GZs.size(); i++) {
				try {
					BDCS_QL_GZ ql = ql_GZs.get(i);
					if (qllx.equals(ql.getQLLX())) {
						BDCS_FSQL_GZ fsql_GZ = getMatchFSQL(fsql_GZs, ql.getId());
						ql_fsqlMap.add(ql);
						ql_fsqlMap.add(fsql_GZ);
						break;
					}
				} catch (Exception e) {
				}
			}
		}
		return ql_fsqlMap;
	}

	protected List<Object> getMatchQLFSQL(List<BDCS_QL_GZ> ql_GZs, List<BDCS_FSQL_GZ> fsql_GZs, String djlx,
			String qllx, List<String> listcasenum) {
		List<Object> ql_fsqlMap = new ArrayList();
		if (ql_GZs != null && fsql_GZs != null && fsql_GZs.size() > 0 && djlx != null && qllx != null
				&& !djlx.equals("") && !qllx.equals("")) {
			for (int i = 0; i < fsql_GZs.size(); i++) {
				BDCS_QL_GZ ql = ql_GZs.get(i);
				if (qllx.equals(ql.getQLLX())) {
					BDCS_FSQL_GZ fsql_GZ = getMatchFSQL(fsql_GZs, ql.getId());
					ql_fsqlMap.add(ql);
					ql_fsqlMap.add(fsql_GZ);
					ql_fsqlMap.add(listcasenum.get(i));
					break;
				}
			}
		}
		return ql_fsqlMap;
	}

	@Override
	public String InsertSXFromZJK(List<BDCS_QL_GZ> qlList, List<BDCS_FSQL_GZ> fsqlList, String xmbh,
			List<String> casenumList, List<BDCQLR> qlrList, List<JYQLR> jyqlrList, List<String> bdcdyhList,
			List<JYH> HList, boolean bool, String fwzt) throws Exception {
		String flag = "false";
		//大宗件时获取前台传来的登记原因
		String[] ywh_djyy= xmbh.split("&&");
		if(ywh_djyy!=null&&ywh_djyy.length>0){
			xmbh=ywh_djyy[0];
		}
		djyy="";
		if(ywh_djyy!=null&&ywh_djyy.length>1){
			djyy=ywh_djyy[1];
		}
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		// 业务类型判断
		String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
		String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 注销类业务、异议登记处理
			if (_handleClassName.contains("DYYGZXDJHandler") || _handleClassName.contains("DYZXDJHandler")
					|| _handleClassName.contains("YYDJHandler") || _handleClassName.contains("YYZXDJHandler") || // _handleClassName.contains("ZYYG_YDY_DJHandler")||
					// _handleClassName.contains("ZYYGDYYGDJHandler")||
					_handleClassName.contains("ZYYGZXDJHandler") || _handleClassName.contains("DYBGDJHandler")) {
				// 支持部分注销
				flag = InsertDYZX(qlList.get(0), xmbh, casenumList.get(0), bdcdyhList, fwzt);
				// 在建工程抵押注销等，允许注销其中部分单元
				// if(_handleClassName.contains("DYBGDJHandler2")){
				// setBFZX(bdcdyhList,xmbh);
				// }
				// SaveBDCS_SQR()
			} else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")) {// djlx.equals(DJLX.CFDJ.Value)&&qllx.equals("98")
				// 解封
				flag = InsertJF(fsqlList.get(0), xmbh, casenumList.get(0), bdcdyhList, fwzt);
			} else if (_handleClassName.contains("CSDJHandler")) {
				// 初始登记
				flag = InsertCSDJ(xmbh, casenumList.get(0));
				// 保存申请人到登记库
				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			}
			// else if(_handleClassName.contains("DYZYDJHandler"))
			// {//_handleClassName.contains("ZYYG_YDY_DJHandler")||
			// //预告预抵-->转移抵押登记
			// InsertSXFromZJKZYDY(qlList, fsqlList, xmbh, casenum, qlrList,
			// jyqlrList, bdcdyhList, HList, bool, fwzt);
			// }
			else {
				// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】
				if (bool) {
					String bdcdyidArr = "";
					for (String bdcdyh : bdcdyhList) {
						String bdcdyid1 = "";
						String hql = " relationid = '" + bdcdyh + "' ";
						if (fwzt.equals("2")) {
							List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning:房产推送数据中单元号为：" + bdcdyh + " 的房屋状态与当前业务类型不符，请检测！";
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						} else if (fwzt.equals("1")) {
							// 此处原因:1、房产房屋状态推送错误【本该是现房2的推送成1】2、该预测户未落宗
							List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning" + bdcdyh;
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						}

						BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
						if (_srcUnit == null) {
							continue;
						}
						String bdcdyid = _srcUnit.getId();
						if (bdcdyidArr.equals("")) {
							bdcdyidArr += bdcdyid;
						} else {
							bdcdyidArr += "," + bdcdyid;
						}
					}
					// 增加审核环节
					ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
					if (rltMessage.getSuccess().equals("false")) {
						return rltMessage.getMsg();
					} else if (rltMessage.getSuccess().equals("warning")) {
						return "警告：" + rltMessage.getMsg();
					}
				}
				// 将权利人保存到申请人表中
				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
				BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				for (String relationid : bdcdyhList) {
					String hql = " relationid = '" + relationid + "' ";
					String bdcdyid_xz = "";
					// 验证是否落宗
					if (fwzt.equals("2")) {
						List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning";
							return flag;
						}
						bdcdyid_xz = h_xzs.get(0).getId();
					} else if (fwzt.equals("1")) {
						List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning";
							return flag;
						}
						bdcdyid_xz = h_xzs.get(0).getId();
					}
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid_xz);

					if (_srcUnit == null) {
						continue;
					}
					// flag = "true";

					String bdcdyid = _srcUnit.getId();
					String bdcdyh2 = _srcUnit.getBDCDYH();
					dyservice.removeBDCDY(xmbh, bdcdyid);
					dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
					String str = MessageFormat.format("XMBH=''{0}'' AND (BDCDYID=''{1}'' OR BDCDYH=''{2}'')", xmbh,
							bdcdyid, bdcdyh2);
					// 更正登記工作戶有新數據，新的bdcdyid
					if (_handleClassName.contains("GZDJHandler")) {
						str = MessageFormat.format(
								"XMBH=''{0}'' AND (DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{1}'' AND BDCDYLX=''{2}'') OR BDCDYH=''{3}'')",
								xmbh, bdcdyid, bdcdylx.Value, bdcdyh2);
					}
					// DJHandler handler = HandlerFactory
					// .createDJHandler(xmbh);

					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					// 更正登記修改戶工作的面積，坐落、規劃用途、房屋性質
					if (_handleClassName.contains("GZDJHandler") && listdjdy != null && listdjdy.size() > 0) {
						str = "bdcdyid='" + listdjdy.get(0).getBDCDYID() + "'";
						List<BDCS_H_GZ> gzhList = baseCommonDao.getDataList(BDCS_H_GZ.class, str);
						if (gzhList != null && gzhList.size() > 0) {
							BDCS_H_GZ gzh = gzhList.get(0);
							gzh.setSCJZMJ(HList.get(0).getSCJZMJ());
							gzh.setZL(HList.get(0).getZL());
							gzh.setFWXZ(HList.get(0).getFWXZ());
							gzh.setGHYT(HList.get(0).getGHYT());
							baseCommonDao.update(gzh);
						}
					}
					boolean bLoadQLR =  true;//false 是否加载权利人，换补证addBDCDYNoCheck时自动加载权利人了，不能重复添加
					// 如果用bdcdyid加载不到，要用qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						List<BDCS_DJDY_XZ> djdy_XZs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
								"BDCDYH='" + _srcUnit.getBDCDYH() + "'");
						if (djdy_XZs != null && djdy_XZs.size() > 0) {
							List<BDCS_QL_XZ> ql_XZs = baseCommonDao.getDataList(BDCS_QL_XZ.class,
									"BDCQZH='" + qlList.get(0).getBDCQZH() + "'");
							if (ql_XZs != null && ql_XZs.size() > 0) {
								dyservice.addBDCDYNoCheck(xmbh, ql_XZs.get(0).getId());
								listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
//								bLoadQLR = false;
							}
						}
					}
					// 如果还为空，再用另一种方式qlid
					if (listdjdy == null || listdjdy.size() == 0) {
						String condition = "select qlid from bdck.bdcs_ql_xz where djdyid in (select djdyid from bdck.bdcs_djdy_xz where bdcdyid = '"
								+ bdcdyid + "') ";
						List<Map> ql_XZs = baseCommonDao.getDataListByFullSql(condition);
						if (ql_XZs != null && ql_XZs.size() > 0) {
							String qlid = ql_XZs.get(0).get("QLID").toString();
							dyservice.removeBDCDY(xmbh, qlid);
							dyservice.addBDCDYNoCheck(xmbh, qlid);
							listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
//							bLoadQLR = false;
						}
					}

					if (listdjdy != null && listdjdy.size() > 0) {
						for (int i = 0; i < listdjdy.size(); i++) {
							// 考虑到并案流程，一次可能有多条数据
							List<Rights> bdcRights = RightsTools.loadRightsByCondition(DJDYLY.GZ,
									"XMBH='" + xmbh + "'");
							if (bdcRights != null && bdcRights.size() > 0) {
								for (int k = 0; k < bdcRights.size(); k++) {
									Rights _rights = bdcRights.get(k);// RightsTools.loadRightsByDJDYID(DJDYLY.GZ,
																		// xmbh,
																		// listdjdy.get(i).getDJDYID());
									if (_rights == null) {
										continue;
									}
									// 保存义务人到申请人表
									SaveJYYWRDLR(xmbh, jyqlrList);
									if (bLoadQLR) {
										// 加载权利人
										handler.addQLRbySQRArray(_rights.getId(), sqrids);
									}

									SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ,
											_rights.getId());
									if (_subRights == null) {
										continue;
									}
									// 考虑并案流程，根据登记类型和权利类型去对应的权利和附属权利

									List<Object> ql_fsqlList = getMatchQLFSQL(qlList, fsqlList, _rights.getDJLX(),
											_rights.getQLLX(), casenumList);
									if (ql_fsqlList != null && ql_fsqlList.size() > 1) {
										BDCS_QL_GZ ql = (BDCS_QL_GZ) ql_fsqlList.get(0);
										BDCS_FSQL_GZ fsql_GZ = (BDCS_FSQL_GZ) ql_fsqlList.get(1);
										SaveQL(ql, fsql_GZ, String.valueOf(ql_fsqlList.get(2)), _rights, _subRights);
										// SaveQL(qlList.get(0),
										// fsqlList.get(0), casenum, _rights,
										// _subRights);
									}
									
									flag = "true";

								}
							}
						}
					}
				}
				baseCommonDao.flush();
			}
			// }
		}
		return flag;
	}

	// 在建工程抵押注销时，有些只是部分注销
	protected void setBFZX(List<String> fwidList, String xmbh) {
		if (fwidList != null && fwidList.size() > 0) {
			String sql = "";
			for (String fwid : fwidList) {
				List<BDCS_H_XZ> hList = baseCommonDao.getDataList(BDCS_H_XZ.class, "relationid='" + fwid + "'");
				if (hList != null && hList.size() > 0) {
					String bdcdyid = hList.get(0).getId();
					List<BDCS_DJDY_XZ> djdy_XZs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
							"BDCDYID='" + bdcdyid + "'");
					if (djdy_XZs != null && djdy_XZs.size() > 0) {
						sql = "djdyid='" + djdy_XZs.get(0).getDJDYID() + "' and xmbh='" + xmbh + "'";
						List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(BDCS_QL_GZ.class, sql);
						if (qlList != null && qlList.size() > 0) {
							BDCS_QL_GZ ql = qlList.get(0);
							// 抵押注销
							ql.setISCANCEL("1");
							baseCommonDao.update(ql);
						}
					}

				}
			}
			baseCommonDao.flush();
		}
	}

}

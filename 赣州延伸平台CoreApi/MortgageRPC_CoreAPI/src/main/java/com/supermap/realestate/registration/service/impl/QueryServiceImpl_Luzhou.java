package com.supermap.realestate.registration.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.ViewClass.HistoryUnitInfo;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitInfo;
import com.supermap.realestate.registration.ViewClass.UnitRelation;
import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_BLACKLIST;
import com.supermap.realestate.registration.model.BDCS_CX_LOG;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_GEO;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_QLR_EX;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_XTDZ;
import com.supermap.realestate.registration.model.BDCS_XZCF;
import com.supermap.realestate.registration.model.BDCS_XZDY;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.tools.ProcedureParam;
import com.supermap.realestate.registration.tools.ProcedureTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.BarcodeUtil;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DYFS;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.YXBZ;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.service.common.Common;
/**
 * @author Administrator
 *
 */
@Service("queryServiceluzhou")
public class QueryServiceImpl_Luzhou extends QueryServiceImpl {

	@Override
	public JSONObject queryhousestate(
			String querystr,String ly) {
		JSONObject resultJsonObject=new JSONObject();

		if(querystr!=null && !"".equals(querystr)){
			JSONObject jsonObject= JSONObject.parseObject(querystr);
			JSONObject hjson=jsonObject.getJSONObject("h");
			JSONObject ysyqrjson=jsonObject.getJSONObject("ysyqr");
			JSONObject ydyqrjson=jsonObject.getJSONObject("ydyqr");
			
			String RelationID=hjson.getString("fwdm");
			String bdcdylx=hjson.getString("bdcdylx");
	//		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			List<RealUnit> list_unit = null;
			if (!StringHelper.isEmpty(RelationID)) {
				list_unit = UnitTools.loadUnits(BDCDYLX.initFrom(bdcdylx),
						DJDYLY.XZ, "RelationID='" + RelationID + "' or BDCDYH='"+RelationID + "'",
						new HashMap<String, String>());
			}
			if (list_unit == null || list_unit.size() <= 0) {
				HashMap<String, String> map = new HashMap<String, String>();
				// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
	//			map.put("MortgageStatus", "2");// 抵押状态
	//			map.put("NoticeStatus", "2");// 期房预告状态
	//			map.put("PreMortgageStatus", "2");// 期房抵押状态
	//			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
	//			map.put("Handleing", "2");
	//			map.put("RelationID", RelationID);
	//			list.add(map);
	//			return list;
				resultJsonObject.put("h",hjson);
				resultJsonObject.put("ysyqrsyqzt", "2");
				resultJsonObject.put("ydyqrdyqzt", "2");
				resultJsonObject.put("dyqzt", "2");
				resultJsonObject.put("xzqzt", "2");
				resultJsonObject.put("ygqzt", "2");
				resultJsonObject.put("sfzblyw", "2");
				return resultJsonObject;
			}
			if (BDCDYLX.H.Value.equals(bdcdylx)) {
	//			for (RealUnit unit : list_unit) {
				resultJsonObject = getStatus_SCH_Mapping_Luzhou(list_unit.get(0),hjson,ysyqrjson,ydyqrjson);
	//				list.add(map);
	//			}
			} else {
	//			for (RealUnit unit : list_unit) {
				resultJsonObject = getStatus_YCH_Mapping_Luzhou(list_unit.get(0),hjson,ysyqrjson,ydyqrjson);
	//				list.add(map);
	//			}
			}
			}
			return resultJsonObject;
	}
	private JSONObject getStatus_SCH_Mapping_Luzhou(RealUnit unit,JSONObject hjson,JSONObject ysyqrjson,JSONObject ydyqrjson) {
		JSONObject resultJson=new JSONObject();
		HashMap<String, String> map = new HashMap<String, String>();
		String MortgageStatus = "0";
		String NoticeStatus = "0";
		String PreMortgageStatus = "0";
		String LimitStatus = "0";
		String bdcdyid = unit.getId();
		String RelationID = unit.getRELATIONID();
		String Handleing = "0";
		if (true) {
			HashMap<String, String> Status_map = GetStatus_SCH(bdcdyid);
			if (Status_map != null) {
				if (Status_map.containsKey("CFZT")) {
					String CFZT = Status_map.get("CFZT").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						LimitStatus = CFZT;
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains(CFZT)) {
							LimitStatus = CFZT + "、" + LimitStatus;
						}
					}
				}
				if (Status_map.containsKey("DYZT")) {
					String DYZT = Status_map.get("DYZT").toString();
					if (MortgageStatus.equals("0")
							|| MortgageStatus.equals("2")) {
						MortgageStatus = DYZT;
					} else if (DYZT.equals("1")) {
						if (!MortgageStatus.contains(DYZT)) {
							MortgageStatus = DYZT + "、" + MortgageStatus;
						}
					}
				}
				if (Status_map.containsKey("CFZT_InProcess")) {
					String CFZT = Status_map.get("CFZT_InProcess").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						if (LimitStatus.equals("1")) {
							LimitStatus = "-1";
						} else {
							LimitStatus = CFZT;
						}
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains("-1")) {
							LimitStatus = LimitStatus + "、" + "-1";
						}
					}
				}
				if (Status_map.containsKey("DYZT_InProcess")) {
					String DYZT = Status_map.get("DYZT_InProcess").toString();
					if (MortgageStatus.equals("0")
							|| MortgageStatus.equals("2")) {
						if (DYZT.equals("1")) {
							MortgageStatus = "-1";
						} else {
							MortgageStatus = DYZT;
						}
					} else if (DYZT.equals("1")) {
						if (!MortgageStatus.contains("-1")) {
							MortgageStatus = MortgageStatus + "、" + "-1";
						}
					}
				}
			}
			HashMap<String, String> Handleing_map = GetHandleing(bdcdyid);
			if (Handleing_map.containsKey("HANDLEING")) {
				String HANDLEING = Handleing_map.get("HANDLEING").toString();
				if (HANDLEING.equals("1")) {
					String DJLXandQLLX = Handleing_map.get("DJLXandQLLX").toString();
					Handleing=DJLXandQLLX;
				} 
			}
		}

		List<YC_SC_H_XZ> list_gx = baseCommonDao.getDataList(YC_SC_H_XZ.class,
				"SCBDCDYID='" + bdcdyid + "'");
		if (list_gx != null && list_gx.size() > 0) {
			for (YC_SC_H_XZ gx : list_gx) {
				String ycbdcdyid = gx.getYCBDCDYID();
				if (StringHelper.isEmpty(ycbdcdyid)) {
					continue;
				}
				HashMap<String, String> Status_map = GetStatus_YCH(ycbdcdyid);
				if (Status_map != null) {
					if (Status_map.containsKey("CFZT")) {
						String CFZT = Status_map.get("CFZT").toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							LimitStatus = CFZT;
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains(CFZT)) {
								LimitStatus = CFZT + "、" + LimitStatus;
							}
						}
					}
					if (Status_map.containsKey("DYZT")) {
						String DYZT = Status_map.get("DYZT").toString();
						if (PreMortgageStatus.equals("0")
								|| PreMortgageStatus.equals("2")) {
							PreMortgageStatus = DYZT;
						} else if (DYZT.equals("1")) {
							if (!PreMortgageStatus.contains(DYZT)) {
								PreMortgageStatus = DYZT + "、"
										+ PreMortgageStatus;
							}
						}
					}
					if (Status_map.containsKey("YGZT")) {
						String YGZT = Status_map.get("YGZT").toString();
						if (NoticeStatus.equals("0")
								|| NoticeStatus.equals("2")) {
							NoticeStatus = YGZT;
						} else if (YGZT.equals("1")) {
							if (!NoticeStatus.contains(YGZT)) {
								NoticeStatus = YGZT + "、" + NoticeStatus;
							}
						}
					}
					if (Status_map.containsKey("CFZT_InProcess")) {
						String CFZT = Status_map.get("CFZT_InProcess")
								.toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							if (CFZT.equals("1")) {
								LimitStatus = "-1";
							} else {
								LimitStatus = CFZT;
							}
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains("-1")) {
								LimitStatus = LimitStatus + "、" + "-1";
							}
						}
					}
					if (Status_map.containsKey("DYZT_InProcess")) {
						String DYZT = Status_map.get("DYZT_InProcess")
								.toString();
						if (PreMortgageStatus.equals("0")
								|| PreMortgageStatus.equals("2")) {
							if (DYZT.equals("1")) {
								PreMortgageStatus = "-1";
							} else {
								PreMortgageStatus = DYZT;
							}
						} else if (DYZT.equals("1")) {
							if (!PreMortgageStatus.contains("-1")) {
								PreMortgageStatus = PreMortgageStatus + "、"
										+ "-1";
							}
						}
					}

					if (Status_map.containsKey("YGZT_InProcess")) {
						String YGZT = Status_map.get("YGZT_InProcess")
								.toString();
						if (NoticeStatus.equals("0")
								|| NoticeStatus.equals("2")) {
							if (YGZT.equals("1")) {
								NoticeStatus = "-1";
							} else {
								NoticeStatus = YGZT;
							}
						} else if (YGZT.equals("1")) {
							if (!NoticeStatus.contains("-1")) {
								NoticeStatus = NoticeStatus + "、" + "-1";
							}
						}
					}
				}
			}
		}
//		map.put("MortgageStatus", MortgageStatus);
//		map.put("NoticeStatus", NoticeStatus);
//		map.put("PreMortgageStatus", PreMortgageStatus);
//		map.put("LimitStatus", LimitStatus);
//		map.put("RelationID", RelationID);
//		map.put("Handleing", Handleing);
		
		String dyzt="0";
		if("1".equals(MortgageStatus)||"1".equals(PreMortgageStatus)){
			dyzt="1";
		}
		resultJson.put("time", DateUtil.getDateTime());
		resultJson.put("dyqzt", dyzt);
		resultJson.put("xzqzt", LimitStatus);
		resultJson.put("ygqzt", NoticeStatus);
		resultJson.put("sfzblyw", Handleing);
		String ysyqr=getStatus_syqr(bdcdyid,ysyqrjson);
		String ydyqr=getStatus_syqr(bdcdyid,ydyqrjson);
		resultJson.put("ysyqrzt", ysyqr);
		resultJson.put("ydyqrzt", ydyqr);
		resultJson.put("h", hjson);
		return resultJson;
	}

	private JSONObject getStatus_YCH_Mapping_Luzhou(RealUnit unit,JSONObject hjson,JSONObject ysyqrjson,JSONObject ydyqrjson) {
		JSONObject resultJson=new JSONObject();
		HashMap<String, String> map = new HashMap<String, String>();
		String MortgageStatus = "0";
		String NoticeStatus = "0";
		String PreMortgageStatus = "0";
		String LimitStatus = "0";
		String bdcdyid = unit.getId();
		String RelationID = unit.getRELATIONID();
		String Handleing = "0";
		if (true) {
			HashMap<String, String> Status_map = GetStatus_YCH(bdcdyid);
			if (Status_map != null) {
				if (Status_map.containsKey("CFZT")) {
					String CFZT = Status_map.get("CFZT").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						LimitStatus = CFZT;
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains(CFZT)) {
							LimitStatus = CFZT + "、" + LimitStatus;
						}
					}
				}
				if (Status_map.containsKey("DYZT")) {
					String DYZT = Status_map.get("DYZT").toString();
					if (PreMortgageStatus.equals("0")
							|| PreMortgageStatus.equals("2")) {
						PreMortgageStatus = DYZT;
					} else if (DYZT.equals("1")) {
						if (!PreMortgageStatus.contains(DYZT)) {
							PreMortgageStatus = DYZT + "、" + PreMortgageStatus;
						}
					}
				}
				if (Status_map.containsKey("YGZT")) {
					String YGZT = Status_map.get("YGZT").toString();
					if (NoticeStatus.equals("0") || NoticeStatus.equals("2")) {
						NoticeStatus = YGZT;
					} else if (YGZT.equals("1")) {
						if (!NoticeStatus.contains(YGZT)) {
							NoticeStatus = YGZT + "、" + NoticeStatus;
						}
					}
				}
				if (Status_map.containsKey("CFZT_InProcess")) {
					String CFZT = Status_map.get("CFZT_InProcess").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						if (CFZT.equals("1")) {
							LimitStatus = "-1";
						} else {
							LimitStatus = CFZT;
						}
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains("-1")) {
							LimitStatus = LimitStatus + "、" + "-1";
						}
					}
				}
				if (Status_map.containsKey("DYZT_InProcess")) {
					String DYZT = Status_map.get("DYZT_InProcess").toString();
					if (PreMortgageStatus.equals("0")
							|| PreMortgageStatus.equals("2")) {
						if (DYZT.equals("1")) {
							PreMortgageStatus = "-1";
						} else {
							PreMortgageStatus = DYZT;
						}
					} else if (DYZT.equals("1")) {
						if (!PreMortgageStatus.contains("-1")) {
							PreMortgageStatus = PreMortgageStatus + "、" + "-1";
						}
					}
				}

				if (Status_map.containsKey("YGZT_InProcess")) {
					String YGZT = Status_map.get("YGZT_InProcess").toString();
					if (NoticeStatus.equals("0") || NoticeStatus.equals("2")) {
						if (YGZT.equals("1")) {
							NoticeStatus = "-1";
						} else {
							NoticeStatus = YGZT;
						}
					} else if (YGZT.equals("1")) {
						if (!NoticeStatus.contains("-1")) {
							NoticeStatus = NoticeStatus + "、" + "-1";
						}
					}
				}
			}
			HashMap<String, String> Handleing_map = GetHandleing(bdcdyid);
			if (Handleing_map.containsKey("HANDLEING")) {
				String HANDLEING = Handleing_map.get("HANDLEING").toString();
				if (HANDLEING.equals("1")) {
					String DJLXandQLLX = Handleing_map.get("DJLXandQLLX").toString();
					Handleing=DJLXandQLLX;
				} 
			}
		}

		List<YC_SC_H_XZ> list_gx = baseCommonDao.getDataList(YC_SC_H_XZ.class,
				"YCBDCDYID='" + bdcdyid + "'");
		if (list_gx != null && list_gx.size() > 0) {
			for (YC_SC_H_XZ gx : list_gx) {
				String scbdcdyid = gx.getSCBDCDYID();
				if (StringHelper.isEmpty(scbdcdyid)) {
					continue;
				}
				HashMap<String, String> Status_map = GetStatus_SCH(scbdcdyid);
				if (Status_map != null) {
					if (Status_map.containsKey("CFZT")) {
						String CFZT = Status_map.get("CFZT").toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							LimitStatus = CFZT;
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains(CFZT)) {
								LimitStatus = CFZT + "、" + LimitStatus;
							}
						}
					}
					if (Status_map.containsKey("DYZT")) {
						String DYZT = Status_map.get("DYZT").toString();
						if (MortgageStatus.equals("0")
								|| MortgageStatus.equals("2")) {
							MortgageStatus = DYZT;
						} else if (DYZT.equals("1")) {
							if (!MortgageStatus.contains(DYZT)) {
								MortgageStatus = DYZT + "、" + MortgageStatus;
							}
						}
					}
					if (Status_map.containsKey("CFZT_InProcess")) {
						String CFZT = Status_map.get("CFZT_InProcess")
								.toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							if (LimitStatus.equals("1")) {
								LimitStatus = "-1";
							} else {
								LimitStatus = CFZT;
							}
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains("-1")) {
								LimitStatus = LimitStatus + "、" + "-1";
							}
						}
					}
					if (Status_map.containsKey("DYZT_InProcess")) {
						String DYZT = Status_map.get("DYZT_InProcess")
								.toString();
						if (MortgageStatus.equals("0")
								|| MortgageStatus.equals("2")) {
							if (DYZT.equals("1")) {
								MortgageStatus = "-1";
							} else {
								MortgageStatus = DYZT;
							}
						} else if (DYZT.equals("1")) {
							if (!MortgageStatus.contains("-1")) {
								MortgageStatus = MortgageStatus + "、" + "-1";
							}
						}
					}
				}
			}
		}
//		map.put("MortgageStatus", MortgageStatus);
//		map.put("NoticeStatus", NoticeStatus);
//		map.put("PreMortgageStatus", PreMortgageStatus);
//		map.put("LimitStatus", LimitStatus);
//		map.put("RelationID", RelationID);
//		map.put("Handleing", Handleing);
		resultJson.put("h", hjson);
		String dyzt="0";
		if("1".equals(MortgageStatus)||"1".equals(PreMortgageStatus)){
			dyzt="1";
		}
		resultJson.put("dyqzt", dyzt);
		resultJson.put("xzqzt", LimitStatus);
		resultJson.put("ygqzt", NoticeStatus);
		resultJson.put("sfzblyw", Handleing);
		return resultJson;
	}


	/**
	 * 获取权利与人的匹配状态
	 * @作者 lenovo
	 * @创建时间 2017年7月10日下午2:09:09
	 * @param bdcdyid
	 * @param syqrjson
	 * @return
	 */
	String getStatus_syqr(String bdcdyid,JSONObject syqrjson){
		String zt="0";
		String qlrmc=syqrjson.getString("mc");
		String zjh=syqrjson.getString("zjhm");
		String bdcqzh=syqrjson.getString("bdcqzh");
		String sql="select h.bdcdyid  from bdck.bdcs_h_xz h inner join bdck.bdcs_djdy_xz djdy on h.bdcdyid=djdy.bdcdyid inner join bdck.bdcs_ql_xz ql on djdy.djdyid=ql.djdyid inner join bdck.bdcs_qlr_xz qlr on ql.qlid=qlr.qlid where h.bdcdyid='"+bdcdyid+"' and qlr.qlrmc='"+qlrmc+"' and qlr.zjh='"+zjh
				+"' and qlr.BDCQZH='"+bdcqzh+"'";
		List<Map> list=baseCommonDao.getDataListByFullSql(sql);
		if(list.size()>0){
			zt="1";
		}
		return zt;
	}

}
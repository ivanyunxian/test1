package com.supermap.realestate.registration.ViewClass;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class BuildingTalbe {

	private CommonDao baseCommonDao;

	public BuildingTalbe() {
		this.baseCommonDao = SuperSpringContext.getContext().getBean(
				CommonDao.class);
	}

	/**
	 * 自然幢对应的户的所有信息
	 */
	private List<HouseInfoDY> buildingTalbes = new ArrayList<BuildingTalbe.HouseInfoDY>();

	public List<HouseInfoDY> getBuildingTalbes() {
		return buildingTalbes;
	}

	public void setBuildingTalbes(List<HouseInfoDY> buildingTalbes) {
		this.buildingTalbes = buildingTalbes;
	}

	public BuildingInfo getBuildinfo() {
		return buildinfo;
	}

	public void setBuildinfo(BuildingInfo buildinfo) {
		this.buildinfo = buildinfo;
	}

	/**
	 * 自然幢的基本信息
	 */
	private BuildingInfo buildinfo;

	/**
	 * 房屋单元信息+具体的状态
	 * 
	 * @author 海豹
	 *
	 */
	public class HouseInfoDY {

		/**
		 * 房屋信息
		 */
		private HouseInfo houseinfo;

		public HouseInfo getHouseinfo() {
			return houseinfo;
		}

		public void setHouseinfo(HouseInfo houseinfo) {
			this.houseinfo = houseinfo;
		}

		/**
		 * 现房产权状态定义
		 */
		private XFCQZTDY xfcqztdy;

		public XFCQZTDY getXfcqztdy() {
			return xfcqztdy;
		}

		public void setXfcqztdy(XFCQZTDY xfcqztdy) {
			this.xfcqztdy = xfcqztdy;
		}

		/**
		 * 期房产权状态定义
		 */
		private QFCQZTDY qfcqztdy;

		public QFCQZTDY getQfcqztdy() {
			return qfcqztdy;
		}

		public void setQfcqztdy(QFCQZTDY qfcqztdy) {
			this.qfcqztdy = qfcqztdy;
		}
		
		/**
		 * 期现关联状态定义
		 */
		private String sfqxgl;

		public String getSfqxgl() {
			return sfqxgl;
		}

		public void setSfqxgl(String sfqxgl) {
			this.sfqxgl = sfqxgl;
		}
	}

	/**
	 * 自然幢基本信息
	 * 
	 * @author 海豹
	 *
	 */
	public class BuildingInfo {

		/**
		 * 总层数
		 */
		private String ZCS;

		public String getZCS() {
			return ZCS;
		}

		public void setZCS(String zCS) {
			ZCS = zCS;
		}

		/**
		 * 地上层数
		 */
		private String DSCS;

		public String getDSCS() {
			return DSCS;
		}

		public void setDSCS(String dSCS) {
			DSCS = dSCS;
		}

		/**
		 * 地下层数
		 */
		private String DXCS;

		public String getDXCS() {
			return DXCS;
		}

		public void setDXCS(String dXCS) {
			DXCS = dXCS;
		}

		/**
		 * 总套数
		 */
		private String ZTS;

		public String getZTS() {
			return ZTS;
		}

		public void setZTS(String zTS) {
			ZTS = zTS;
		}

		/**
		 * 建筑面积
		 */
		private String JZMJ;

		public String getJZMJ() {
			return JZMJ;
		}

		public void setJZMJ(String jZMJ) {
			JZMJ = jZMJ;
		}

		/**
		 * 车库个数
		 */
		private int CKGS;

		public int getCKGS() {
			return CKGS;
		}

		public void setCKGS(Integer cKGS) {
			CKGS = cKGS;
		}

		/**
		 * 住宅个数
		 */
		private Map YTandGS;

		public Map getYTandGS() {
			return YTandGS;
		}

		public void setYTandGS(Map yTandGS) {
			YTandGS = yTandGS;
		}

		/**
		 * 楼盘表标记，0表示现房楼盘表，1表示期房楼盘表
		 */
		private String flagBuilding;

		public String getFlagBuilding() {
			return flagBuilding;
		}

		public void setFlagBuilding(String flagBuilding) {
			this.flagBuilding = flagBuilding;
		}
		
		/**
		 * 楼盘表标记，0表示现房楼盘表，1表示期房楼盘表
		 */
		private UnitStatus ZDInfo;
		public String ZDbdcdyid;
		public String Zdzl;
		public String Zddytdmj;
		public String Zdfttdmj;
		public int hszc;
		public Map build;
		
		public  UnitStatus getZDInfo() {
			return ZDInfo;
		}

		public void setZDInfo(UnitStatus ZDInfo) {
			this.ZDInfo = ZDInfo;
		}
	
		List<String> dYHGroup = new ArrayList<String>();

		public List<String> getdYHGroup() {
			return dYHGroup;
		}

		public void setdYHGroup(List<String> dYHGroup) {
			this.dYHGroup = dYHGroup;
		}
		
	}

	/**
	 * 房屋信息
	 * 
	 * @author 海豹
	 *
	 */
	public static class HouseInfo {

		
		/**
		 * 分摊建筑面积
		 */
		private String FTJZMJ;

		public String getFTJZMJ() {
			return FTJZMJ;
		}
		
		public void setFTJZMJ(String fTJZMJ) {
			FTJZMJ = fTJZMJ;
		}
		
		/**
		 * 分摊土地面积
		 */
		private String FTTDMJ;

		public String getFTTDMJ() {
			return FTTDMJ;
		}
		
		public void setFTTDMJ(String fTTDMJ) {
			FTTDMJ = fTTDMJ;
		}
		
		/**
		 * 房屋性质
		 */
		private String FWXZ;

		public String getFWXZ() {
			return FWXZ;
		}

		public void setFWXZ(String fWXZ) {
			FWXZ = fWXZ;
		}

		/**
		 * 套内建筑面积
		 */
		private String TNJZMJ;

		public String getTNJZMJ() {
			return TNJZMJ;
		}

		public void setTNJZMJ(String tNJZMJ) {
			TNJZMJ = tNJZMJ;
		}

		/**
		 * 房屋结构
		 */
		private String FWJG;

		public String getFWJG() {
			return FWJG;
		}

		public void setFWJG(String fWJG) {
			FWJG = fWJG;
		}

		/**
		 * 房屋土地用途
		 */
		private String FWTDYT;

		public String getFWTDYT() {
			return FWTDYT;
		}

		public void setFWTDYT(String fWTDXZ) {
			FWTDYT = fWTDXZ;
		}

		/**
		 * 单元号
		 */
		private String DYH;

		public String getDYH() {
			return DYH;
		}

		public void setDYH(String dYH) {
			DYH = dYH;
		}

		/**
		 * 建筑面积
		 */
		private String JZMJ;

		public String getJZMJ() {
			return JZMJ;
		}

		public void setJZMJ(String jZMJ) {
			JZMJ = jZMJ;
		}

		/**
		 * 规划用途
		 */
		private String GHYT;

		public String getGHYT() {
			return GHYT;
		}

		public void setGHYT(String gHYT) {
			GHYT = gHYT;
		}

		/**
		 * 房屋编码
		 */
		private String FWBM;

		public String getFWBM() {
			return FWBM;
		}

		public void setFWBM(String fWBM) {
			FWBM = fWBM;
		}

		/**
		 * 房屋状态
		 */
		private String FWZT;

		public String getFWZT() {
			return FWZT;
		}

		public void setFWZT(String fWZT) {
			FWZT = fWZT;
		}

		/**
		 * 不动产权单元号
		 */
		private String BDCDYH;

		public String getBDCDYH() {
			return BDCDYH;
		}

		public void setBDCDYH(String bDCDYH) {
			BDCDYH = bDCDYH;
		}

		/**
		 * 起始层
		 */
		private String QSC;

		public String getQSC() {
			return QSC;
		}

		public void setQSC(String qSC) {
			this.QSC = qSC;
		}

		/**
		 * 终止层
		 */
		private String ZZC;

		public String getZZC() {
			return ZZC;
		}

		public void setZZC(String zZC) {
			this.ZZC = zZC;
		}

		/**
		 * 所在层
		 */
		private String SZC;

		public String getSZC() {
			return SZC;
		}

		public void setSZC(String sZC) {
			this.SZC = sZC;
		}

		/**
		 * 不动产单元ID
		 */
		private String BDCDYID;

		public String getBDCDYID() {
			return BDCDYID;
		}

		public void setBdcdyid(String bDCDYID) {
			this.BDCDYID = bDCDYID;
		}

		/**
		 * 房号
		 */
		private String FH;

		public String getFH() {
			return FH;
		}

		public void setFH(String fH) {
			FH = fH;
		}

		private String BDCDYLX;

		public String getBDCDYLX() {
			return BDCDYLX;
		}

		public void setBDCDYLX(String bDCDYLX) {
			BDCDYLX = bDCDYLX;
		}

		private String ZL;

		public String getZL() {
			return ZL;
		}

		public void setZL(String zL) {
			ZL = zL;
		}
		
		/**
		 * 产权人姓名
		 */
		private String qlrxm;

		public String getQlrxm() {
			return qlrxm;
		}

		public void setQlrxm(String qlrxm) {
			this.qlrxm = qlrxm;
		}
	
		private String Realszc ;

		public String getRealszc() {
			return Realszc;
		}

		public void setRealszc(String realszc) {
			Realszc = realszc;
		}
		
		//************start********************************用于期现关联时显示*******************************//
		/**
		 * 预测分摊建筑面积
		 */
		private String YCFTJZMJ;

		public String getYCFTJZMJ() {
			return YCFTJZMJ;
		}
		
		public void setYCFTJZMJ(String yCFTJZMJ) {
			YCFTJZMJ = yCFTJZMJ;
		}
		
		/**
		 * 预测建筑面积
		 */
		private String YCJZMJ;

		public String getYCJZMJ() {
			return YCJZMJ;
		}
		
		public void setYCJZMJ(String yCJZMJ) {
			YCJZMJ = yCJZMJ;
		}
		
		/**
		 * 预测套内建筑面积
		 */
		private String YCTNJZMJ;

		public String getYCTNJZMJ() {
			return YCTNJZMJ;
		}
		
		public void setYCTNJZMJ(String yCTNJZMJ) {
			YCTNJZMJ = yCTNJZMJ;
		}
		
		/**
		 * 实测分摊建筑面积
		 */
		private String SCFTJZMJ;

		public String getSCFTJZMJ() {
			return SCFTJZMJ;
		}
		
		public void setSCFTJZMJ(String sCFTJZMJ) {
			SCFTJZMJ = sCFTJZMJ;
		}
		
		/**
		 * 实测建筑面积
		 */
		private String SCJZMJ;

		public String getSCJZMJ() {
			return SCJZMJ;
		}
		
		public void setSCJZMJ(String sCJZMJ) {
			SCJZMJ = sCJZMJ;
		}
		
		/**
		 * 实测套内建筑面积
		 */
		private String SCTNJZMJ;

		public String getSCTNJZMJ() {
			return SCTNJZMJ;
		}
		
		public void setSCTNJZMJ(String sCTNJZMJ) {
			SCTNJZMJ = sCTNJZMJ;
		}
		//************end********************************用于期现关联时显示*******************************//
	
	}

	/**
	 * 现房产权状态定义,默认初始化为0
	 * 
	 * @author 海豹
	 *
	 */
	public static class XFCQZTDY {
		//0、1、2：未办理、已办理、正在办理
		//现房抵押
		private String XFDIYDJ = "0";
		//现房抵押预告
		private String XFDIYYGDJ = "0";
		//现房转移预告
		private String XFZYYGDJ = "0";
		//现房查封
		private String XFCFDJ = "0";
		//现房首次
		private String XFSCDJ = "0";
		//现房转移
		private String XFZYDJ = "0";
		//现房预售
		private String YXFYS = "0";
		//现房限制
		private String XFXZDJ = "0";
		//现房异议
		private String XFYYDJ = "0";
		
		public String getXFDIYDJ() {
			return XFDIYDJ;
		}
		public void setXFDIYDJ(String xFDIYDJ) {
			XFDIYDJ = xFDIYDJ;
		}
		public String getXFDIYYGDJ() {
			return XFDIYYGDJ;
		}
		public void setXFDIYYGDJ(String xFDIYYGDJ) {
			XFDIYYGDJ = xFDIYYGDJ;
		}
		public String getXFZYYGDJ() {
			return XFZYYGDJ;
		}
		public void setXFZYYGDJ(String xFZYYGDJ) {
			XFZYYGDJ = xFZYYGDJ;
		}
		public String getXFCFDJ() {
			return XFCFDJ;
		}
		public void setXFCFDJ(String xFCFDJ) {
			XFCFDJ = xFCFDJ;
		}
		public String getXFSCDJ() {
			return XFSCDJ;
		}
		public void setXFSCDJ(String xFSCDJ) {
			XFSCDJ = xFSCDJ;
		}
		public String getXFZYDJ() {
			return XFZYDJ;
		}
		public void setXFZYDJ(String xFZYDJ) {
			XFZYDJ = xFZYDJ;
		}
		public String getYXFYS() {
			return YXFYS;
		}
		public void setYXFYS(String yXFYS) {
			YXFYS = yXFYS;
		}
		public String getXFXZDJ() {
			return XFXZDJ;
		}
		public void setXFXZDJ(String xFXZDJ) {
			XFXZDJ = xFXZDJ;
		}
		public String getXFYYDJ() {
			return XFYYDJ;
		}
		public void setXFYYDJ(String xFYYDJ) {
			XFYYDJ = xFYYDJ;
		}
	}

	/**
	 * 期房产权状态定义,默认初始化为0
	 * 
	 * @author 海豹
	 *
	 */
	public static class QFCQZTDY {
		//（0：表示未办理，1：表示已办理,2:在办）
		// 已在建工程抵押登记
		private String QFZJGCDIYDJ = "0";
		// 已在抵押登记 
		private String QFDIYDJ = "0";
		// 已预告商品房预告登记
		private String QFYGDJ = "0";
		// 已预告商品房抵押登记
		private String QFDIYYGDJ = "0";
		// 已期房查封登记
		private String QFCFDJ = "0";
		//已期房限制登记
		private String QFXZDJ = "0";
		// 已期房预售
		private String YQFYS = "0";
		//期房异议
		private String QFYYDJ = "0";
		
		public String getQFZJGCDIYDJ() {
			return QFZJGCDIYDJ;
		}
		public void setQFZJGCDIYDJ(String qFZJGCDIYDJ) {
			QFZJGCDIYDJ = qFZJGCDIYDJ;
		}
		public String getQFDIYDJ() {
			return QFDIYDJ;
		}
		public void setQFDIYDJ(String qFDIYDJ) {
			QFDIYDJ = qFDIYDJ;
		}
		public String getQFYGDJ() {
			return QFYGDJ;
		}
		public void setQFYGDJ(String qFYGDJ) {
			QFYGDJ = qFYGDJ;
		}
		public String getQFDIYYGDJ() {
			return QFDIYYGDJ;
		}
		public void setQFDIYYGDJ(String qFDIYYGDJ) {
			QFDIYYGDJ = qFDIYYGDJ;
		}
		public String getQFCFDJ() {
			return QFCFDJ;
		}
		public void setQFCFDJ(String qFCFDJ) {
			QFCFDJ = qFCFDJ;
		}
		public String getQFXZDJ() {
			return QFXZDJ;
		}
		public void setQFXZDJ(String qFXZDJ) {
			QFXZDJ = qFXZDJ;
		}
		public String getYQFYS() {
			return YQFYS;
		}
		public void setYQFYS(String yQFYS) {
			YQFYS = yQFYS;
		}
		public String getQFYYDJ() {
			return QFYYDJ;
		}
		public void setQFYYDJ(String qFYYDJ) {
			QFYYDJ = qFYYDJ;
		}
		
	}
	
	/**
	 * 根据户（预测户）条件创建楼盘表信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年2月29日上午10:44:26
	 * @param bdcdyid
	 *            ,户不动产单元ID
	 * @param bdcdylx
	 *            ,不动产单元类型(H：031，YCH:032)
	 * @param ly
	 *            ,来源(调查库：01，现状层：02)
	 */
	@SuppressWarnings("rawtypes")
	public BuildingTalbe queryBuildingTableByHouseCond(String bdcdyid,
			String bdcdylx, String ly) {
		double starttime = System.currentTimeMillis();
		BuildingTalbe createbt = new BuildingTalbe();
		BDCDYLX lx = BDCDYLX.initFrom(bdcdylx);
		DJDYLY djdyly = DJDYLY.initFrom(ly);
		RealUnit unit = UnitTools.loadUnit(lx, djdyly, bdcdyid);
		if (!StringHelper.isEmpty(unit)) {
			House h = (House) unit;
			String zrzbdcdyid = h.getZRZBDCDYID();
			if (BDCDYLX.H.equals(lx)) {

				createbt = getXFBuildingInfo(zrzbdcdyid, djdyly);
			} else if (BDCDYLX.YCH.equals(lx)) {
				createbt = getQFBuildingInfo(zrzbdcdyid, djdyly);
			}
		}
		System.out.println(System.currentTimeMillis() - starttime);
		return createbt;
	}

	/**
	 * 根据自然幢（预测自然幢）条件创建楼盘表信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月2日下午2:33:07
	 * @param zrzbdcdyid
	 * @param bdcdylx
	 * @param ly
	 * @return
	 */
	public BuildingTalbe queryBuildingTableByBuildingCond(String zrzbdcdyid,
			String bdcdylx, String ly) {
		BuildingTalbe createbt = new BuildingTalbe();
		double starttime = System.currentTimeMillis();
		BDCDYLX lx = BDCDYLX.initFrom(bdcdylx);
		DJDYLY djdyly = DJDYLY.initFrom(ly);
		if (BDCDYLX.ZRZ.equals(lx) || BDCDYLX.H.equals(lx)) {
			createbt = getXFBuildingInfo(zrzbdcdyid, djdyly);
		} else if (BDCDYLX.YCZRZ.equals(lx) || BDCDYLX.YCH.equals(lx)) {
			createbt = getQFBuildingInfo(zrzbdcdyid, djdyly);
		}
		System.out.println(System.currentTimeMillis() - starttime);
		return createbt;
	}

	/**
	 * 获取现房的产权状态信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月1日下午3:20:52
	 * @param bdcdyid
	 *            ，不动产单元ID
	 * @param ly
	 *            (数据来源，若是调查库则所有产权状态都设置为0，若是不动产库则通过具体信息获取)
	 * @return
	 */
	private XFCQZTDY getXFCQZTDYEx(String bdcdyid, String ly) {
		XFCQZTDY xfzt = new XFCQZTDY();
		if ("1".equals(ly)) {/*
			String sql = "SELECT DY.BDCDYID,DY.ZRZBDCDYID, "
					+ "CASE WHEN EXISTS("
					+ "  SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID"
					+ "  LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
					+ "  WHERE QL.DJLX='100' AND QL.QLLX='23' AND FSQL.DYFS='1' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ "  THEN '1' ELSE '0' END AS YBDY,"
					+ "   CASE WHEN EXISTS("
					+ "  SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
					+ "  LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
					+ "  WHERE QL.DJLX='100' AND QL.QLLX='23' AND FSQL.DYFS='2' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ "  THEN '1' ELSE '0' END AS ZGEDY,"
					+ "    CASE WHEN EXISTS("
					+ "  SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
					+ "  LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
					+ "  WHERE QL.DJLX='700' AND QL.QLLX='23' AND FSQL.DYFS='1' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ "  THEN '1' ELSE '0' END AS YBDYYG,"
					+ "   CASE WHEN EXISTS("
					+ "  SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
					+ "  LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
					+ "  WHERE QL.DJLX='700' AND QL.QLLX='23' AND FSQL.DYFS='2' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ "  THEN '1' ELSE '0' END AS ZGEDYYG,"
					+ "   CASE WHEN EXISTS("
					+ "  SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
					+ "  WHERE QL.DJLX='700' AND QL.QLLX='98' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ "  THEN '1' ELSE '0' END AS ZYYG,"
					+ "    CASE WHEN EXISTS("
					+ "  SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
					+ "  WHERE QL.DJLX='800' AND QL.QLLX='99' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ "  THEN '1' ELSE '0' END AS CFDJ,"
					+ "  CASE WHEN EXISTS("
					+ " SELECT 1 FROM BDCK.BDCS_DJDY_LS DJDY LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.DJDYID=DJDY.DJDYID"
					+ "  WHERE QL.DJLX='200' AND QL.QLLX='4' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ "  THEN '1' ELSE '0' END AS ZYDJ,"
					+ "      CASE WHEN EXISTS("
					+ "  SELECT 1 FROM BDCK.BDCS_DJDY_LS DJDY LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.DJDYID=DJDY.DJDYID"
					+ "  WHERE QL.DJLX='100' AND QL.QLLX='4' AND DJDY.BDCDYID=DY.BDCDYID)"
					+ " THEN '1' ELSE '0' END AS SCDJ,"
					+ "CASE WHEN EXISTS("
					+ " SELECT 1 FROM BDCK.BDCS_DYXZ DYXZ WHERE DY.BDCDYID=DYXZ.BDCDYID)"
					+ " THEN '1' ELSE '0' END AS DYXZ"
					+ " FROM BDCK.BDCS_H_XZ DY WHERE DY.BDCDYID='" + bdcdyid
					+ "'";

			List<Map> lstcq = baseCommonDao.getDataListByFullSql(sql);
			if (!StringHelper.isEmpty(lstcq) && lstcq.size() > 0) {
				Map m = lstcq.get(0);
				xfzt.setYSCDJ(m.get("SCDJ").toString());
				xfzt.setYZYDJ(m.get("ZYDJ").toString());
				xfzt.setYXFCFDJ(m.get("CFDJ").toString());
				xfzt.setYXFXZDJ(m.get("DYXZ").toString());
				xfzt.setYXFYBDYDJ(m.get("YBDY").toString());
				xfzt.setYXFZGEDYDJ(m.get("ZGEDY").toString());
				xfzt.setYXFYBDYQYGDJ(m.get("YBDYYG").toString());
				xfzt.setYXFZGEDYQYGDJ(m.get("ZGEDYYG").toString());
				xfzt.setYXFZYYGDJ(m.get("ZYYG").toString());
			}
		*/}
		return xfzt;
	}

	private XFCQZTDY getXFCQZTDY(String bdcdyid, String ly) {
		XFCQZTDY xfzt = new XFCQZTDY();
		if ("1".equals(ly)) {/*
			String sql = "SELECT QL.DJLX,QL.QLLX,QL.FSQLID,QL.MSR,QL.HTH FROM BDCK.BDCS_DJDY_XZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_XZ QL   ON DJDY.DJDYID = QL.DJDYID "
					+ " WHERE   DJDY.BDCDYID='" + bdcdyid + "'";
			List<Map> lst = baseCommonDao.getDataListByFullSql(sql);		
			if (!StringHelper.isEmpty(lst) && lst.size() > 0) {
				for (Map m : lst) {
					String cS = StringHelper.FormatByDatatype(m.get("FSQLID"));
					String djlx = StringHelper.FormatByDatatype(m.get("DJLX"));
					String qllx = StringHelper.FormatByDatatype(m.get("QLLX"));
					String fsqlid = StringHelper.FormatByDatatype(m.get("FSQLID"));
					String dyfs = "";
					if (!"".equals(fsqlid)) {
						BDCS_FSQL_XZ fsql = baseCommonDao.get(BDCS_FSQL_XZ.class, fsqlid);
						if (!StringHelper.isEmpty(fsql)) {
							dyfs = fsql.getDYFS();
						}
					}
					// 现房的一般抵押
					if (DJLX.CSDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.YBDY.Value.equals(dyfs)) {
						xfzt.setYXFYBDYDJ("1");
						xfzt.setXFZCORQTDJ("1");
					}
					// 现房的最高额抵押抵押
					if (DJLX.CSDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.ZGEDY.Value.equals(dyfs)) {
						xfzt.setYXFZGEDYDJ("1");
						xfzt.setXFZCORQTDJ("1");
					}
					// 现房的预告登记一般抵押
					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.YBDY.Value.equals(dyfs)) {
						xfzt.setYXFYBDYQYGDJ("1");
						xfzt.setXFZCORQTDJ("1");
					}
					// 现房的预告登记最高额抵押抵押
					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.ZGEDY.Value.equals(dyfs)) {
						xfzt.setYXFZGEDYQYGDJ("1");
						xfzt.setXFZCORQTDJ("1");
					}
					// 现房的转移预告登记
					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.CFZX.Value.equals(qllx)) {
						xfzt.setYXFZYYGDJ("1");
					}
					// 现房的查封登记
					if (DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
						xfzt.setYXFCFDJ("1");
						xfzt.setXFZCORQTDJ("1");
					}
					// 判断权利中的合同号和买受人存在，存在即已预售
					if (("4".equals(qllx) || "6".equals(qllx) || "8" .equals(qllx))) {
						if (!StringHelper.isEmpty(m.get("MSR")) && !StringHelper.isEmpty(m.get("HTH"))) {
							xfzt.setYXFYS("1");
						}

					}
				}
			}
			String djsql = "SELECT QL.DJLX,QL.QLLX FROM BDCK.BDCS_DJDY_LS DJDY "
					+ " LEFT JOIN BDCK.BDCS_QL_LS QL  ON QL.DJDYID = DJDY.DJDYID "
					+ " WHERE   DJDY.BDCDYID ='" + bdcdyid + "'";
			List<Map> djlst = baseCommonDao.getDataListByFullSql(djsql);
			if (!StringHelper.isEmpty(djlst) && djlst.size() > 0) {
				for (Map m : djlst) {
					String djlx = StringHelper.FormatByDatatype(m.get("DJLX"));
					String qllx = StringHelper.FormatByDatatype(m.get("QLLX"));
					// 现房的首次登记
					if (DJLX.CSDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
						xfzt.setYSCDJ("1");
					}
					// 现房的转移登记
					if (DJLX.ZYDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
						xfzt.setYZYDJ("1");
					}
				}
			}
			String xzsql = "SELECT DYXZ.ID FROM BDCK.BDCS_DYXZ DYXZ  WHERE DYXZ.BDCDYID ='"
					+ bdcdyid + "'";
			List<Map> xzlst = baseCommonDao.getDataListByFullSql(xzsql);
			if (!StringHelper.isEmpty(xzlst) && xzlst.size() > 0) {
				xfzt.setYXFXZDJ("1");
			}

			// 设置正在办理中的状态
			
			String sqlforgz = "SELECT QL.QLID,QL.DJLX,QL.QLLX,QL.FSQLID,QL.MSR,QL.HTH FROM BDCK.BDCS_DJDY_GZ DJDY "
					+ "  LEFT JOIN BDCK.BDCS_QL_GZ QL   ON DJDY.DJDYID = QL.DJDYID "
					+ " WHERE   DJDY.BDCDYID='" + bdcdyid + "'";
			List<Map> lstforgz = baseCommonDao.getDataListByFullSql(sqlforgz);
			if (!StringHelper.isEmpty(lstforgz) && lstforgz.size() > 0) {
				for (Map m : lstforgz) {
					String cS = StringHelper.FormatByDatatype(m.get("FSQLID"));
					String djlx = StringHelper.FormatByDatatype(m.get("DJLX"));
					String qllx = StringHelper.FormatByDatatype(m.get("QLLX"));
					String fsqlid = StringHelper.FormatByDatatype(m.get("FSQLID"));
					String qlid = StringHelper.FormatByDatatype(m.get("QLID"));
					String sqlforsfdb = "SELECT XMXX.SFDB FROM BDCK.BDCS_XMXX XMXX "
							+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.XMBH = XMXX.XMBH "
							+ " WHERE QL.QLID ='" + qlid + "'";
					String dyfs = "";
					List<Map> lstforsfdb = baseCommonDao.getDataListByFullSql(sqlforsfdb);	
					String sfdb = null;
					if (!StringHelper.isEmpty(lstforsfdb) && lstforsfdb.size() > 0) {
						 sfdb = lstforsfdb.get(0).get("SFDB").toString();
					}
					if (!"".equals(fsqlid)) {
						BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, fsqlid);
						if (!StringHelper.isEmpty(fsql)) {
							dyfs = fsql.getDYFS();
						}
					}
					if ("0".equals(sfdb)) {//未登簿
						
						if (!QLLX.DIYQ.Value.equals(qllx) || !DJLX.CFDJ.Value.equals(djlx) || !DJLX.YYDJ.Value.equals(djlx)) {
							xfzt.setXFZCORQTDJ("2");
						}
						// 现房的一般抵押
						if (DJLX.CSDJ.Value.equals(djlx)&& QLLX.DIYQ.Value.equals(qllx)&& DYFS.YBDY.Value.equals(dyfs)) {
							xfzt.setYXFYBDYDJ("2");
							xfzt.setXFZCORQTDJ("1");
						}
						// 现房的最高额抵押抵押
						if (DJLX.CSDJ.Value.equals(djlx)&& QLLX.DIYQ.Value.equals(qllx)&& DYFS.ZGEDY.Value.equals(dyfs)) {
							xfzt.setYXFZGEDYDJ("2");
							xfzt.setXFZCORQTDJ("1");
						}
						// 现房的预告登记一般抵押
						if (DJLX.YGDJ.Value.equals(djlx)&& QLLX.DIYQ.Value.equals(qllx)&& DYFS.YBDY.Value.equals(dyfs)) {
							xfzt.setYXFYBDYQYGDJ("2");
							xfzt.setXFZCORQTDJ("1");
						}
						// 现房的预告登记最高额抵押抵押
						if (DJLX.YGDJ.Value.equals(djlx)&& QLLX.DIYQ.Value.equals(qllx)&& DYFS.ZGEDY.Value.equals(dyfs)) {
							xfzt.setYXFZGEDYQYGDJ("2");
							xfzt.setXFZCORQTDJ("1");
						}
						// 现房的转移预告登记
						if (DJLX.YGDJ.Value.equals(djlx)&& QLLX.CFZX.Value.equals(qllx)) {
							xfzt.setYXFZYYGDJ("2");
						}
						// 现房的查封登记
						if (DJLX.CFDJ.Value.equals(djlx)&& QLLX.QTQL.Value.equals(qllx)) {
							xfzt.setYXFCFDJ("2");
							xfzt.setXFZCORQTDJ("1");
						}
						// 判断权利中的合同号和买受人存在，存在即已预售
						if (("4".equals(qllx) || "6".equals(qllx) || "8".equals(qllx))) {
							if (!StringHelper.isEmpty(m.get("MSR"))&& !StringHelper.isEmpty(m.get("HTH"))) {
								xfzt.setYXFYS("1");
							}
						}
						// 现房的首次登记
						if (DJLX.CSDJ.Value.equals(djlx)&& QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
							xfzt.setYSCDJ("2");
						}
						// 现房的转移登记
						if (DJLX.ZYDJ.Value.equals(djlx)&& QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
							xfzt.setYZYDJ("2");
							
						}
					}
				}					
			}
		*/}
		
		return xfzt;
	}

	/**
	 * 获取期房的产权状态信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月2日上午11:24:45
	 * @param bdcdyid
	 *            ，期房的不动产单员ID
	 * @return
	 */
	private QFCQZTDY getQFCQZTDY(String bdcdyid) {
		QFCQZTDY qfzt = new QFCQZTDY();
//		String qlsql = " SELECT QL.DJLX,QL.QLLX,QL.FSQLID FROM BDCK.BDCS_DJDY_XZ DJDY "
//				+ "LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ " WHERE DJDY.BDCDYID='" + bdcdyid + "'";
//		List<Map> qllst = baseCommonDao.getDataListByFullSql(qlsql);		
//		boolean lsflag = false;// LS有值
//		if (!StringHelper.isEmpty(qllst) && qllst.size() > 0) {
//			for (Map m : qllst) {
//				String djlx = StringHelper.FormatByDatatype(m.get("DJLX"));
//				String qllx = StringHelper.FormatByDatatype(m.get("QLLX"));
//				String fsqlid = StringHelper.FormatByDatatype(m.get("FSQLID"));
//				if (!"".equals(fsqlid)) {
//					BDCS_FSQL_XZ fsql = baseCommonDao.get(BDCS_FSQL_XZ.class,
//							fsqlid);
//					String dyfs = "";
//					if (!StringHelper.isEmpty(fsql)) {
//						dyfs = fsql.getDYFS();
//					}
//					// 期房在建工程一般抵押
//					if (DJLX.CSDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.YBDY.Value.equals(dyfs)) {
//						qfzt.setYZJGCYBDY("1");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房在建工程最高额抵押
//					if (DJLX.CSDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.ZGEDY.Value.equals(dyfs)) {
//						qfzt.setYZJGCZGEDY("1");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房预告登记一般抵押
//					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.YBDY.Value.equals(dyfs)) {
//						qfzt.setYYGSPFYBDY("1");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房的预告登记最高额抵押抵押
//					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.ZGEDY.Value.equals(dyfs)) {
//						qfzt.setYYGSPFZGEDY("1");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房预告登记
//					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
//						qfzt.setYYGSPFYGDJ("1");
//					}
//					// 期房查封登记
//					if (DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
//						qfzt.setYQFCF("1");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 判断权利中的合同号和买受人存在，存在即已预售
//					if (("4".equals(qllx) || "6".equals(qllx) || "8"
//							.equals(qllx))) {
//						if (!StringHelper.isEmpty(m.get("MSR"))
//								&& !StringHelper.isEmpty(m.get("HTH"))) {
//							qfzt.setYQFYS("1");
//						}
//
//					}
//				}
//			}
//		}
//		//正在办理的业务
//		String qlsqlforgz = " SELECT QL.QLID,QL.DJLX,QL.QLLX,QL.FSQLID FROM BDCK.BDCS_DJDY_GZ DJDY "
//				+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ " WHERE DJDY.BDCDYID='" + bdcdyid + "'";
//		List<Map> qllstforgz = baseCommonDao.getDataListByFullSql(qlsqlforgz);					
//		if (!StringHelper.isEmpty(qllstforgz) && qllstforgz.size() > 0) {
//			for (Map m : qllstforgz) {
//				String djlx = StringHelper.FormatByDatatype(m.get("DJLX"));
//				String qllx = StringHelper.FormatByDatatype(m.get("QLLX"));
//				String fsqlid = StringHelper.FormatByDatatype(m.get("FSQLID"));
//				String qlid = StringHelper.FormatByDatatype(m.get("QLID"));
//				String sqlforsfdb = "SELECT XMXX.SFDB FROM BDCK.BDCS_XMXX XMXX "
//						+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.XMBH = XMXX.XMBH "
//						+ " WHERE QL.QLID ='" + qlid + "'";				
//				List<Map> lstforsfdb = baseCommonDao.getDataListByFullSql(sqlforsfdb);	
//				String sfdb = null;
//				if (!StringHelper.isEmpty(lstforsfdb) && lstforsfdb.size() > 0) {
//					 sfdb = lstforsfdb.get(0).get("SFDB").toString();
//				}
//				if (!"".equals(fsqlid) && "0".equals(sfdb)) {//未登簿
//					BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class,
//							fsqlid);
//					String dyfs = "";
//					if (!StringHelper.isEmpty(fsql)) {
//						dyfs = fsql.getDYFS();
//					}
//					
//					if (!QLLX.DIYQ.Value.equals(qllx) || !DJLX.CFDJ.Value.equals(djlx) || !DJLX.YYDJ.Value.equals(djlx)) {
//						qfzt.setQFZCORQTDJ("2");
//					}
//					// 期房在建工程一般抵押
//					if (DJLX.CSDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.YBDY.Value.equals(dyfs)) {
//						qfzt.setYZJGCYBDY("2");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房在建工程最高额抵押
//					if (DJLX.CSDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.ZGEDY.Value.equals(dyfs)) {
//						qfzt.setYZJGCZGEDY("2");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房预告登记一般抵押
//					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.YBDY.Value.equals(dyfs)) {
//						qfzt.setYYGSPFYBDY("2");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房的预告登记最高额抵押抵押
//					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx) && DYFS.ZGEDY.Value.equals(dyfs)) {
//						qfzt.setYYGSPFZGEDY("2");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 期房预告登记
//					if (DJLX.YGDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
//						qfzt.setYYGSPFYGDJ("2");
//					}
//					// 期房查封登记
//					if (DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
//						qfzt.setYQFCF("2");
//						qfzt.setQFZCORQTDJ("1");
//					}
//					// 判断权利中的合同号和买受人存在，存在即已预售
//					if (("4".equals(qllx) || "6".equals(qllx) || "8".equals(qllx))) {
//						if (!StringHelper.isEmpty(m.get("MSR")) && !StringHelper.isEmpty(m.get("HTH"))) {
//							qfzt.setYQFYS("1");
//						}
//
//					}
//				}
//			}
//		}
//		
//		String xzsql = "SELECT DYXZ.ID FROM BDCK.BDCS_DYXZ DYXZ  WHERE DYXZ.BDCDYID ='"
//				+ bdcdyid + "'";
//		List<Map> xzlst = baseCommonDao.getDataListByFullSql(xzsql);
//		if (!StringHelper.isEmpty(xzlst) && xzlst.size() > 0) {
//			qfzt.setYQFXZ("1");
//		}
		return qfzt;
	}

	private QFCQZTDY getQFCQZTDYEx(String bdcdyid) {
		QFCQZTDY qfzt = new QFCQZTDY();
//		String sql = "SELECT DY.BDCDYID,DY.ZRZBDCDYID,"
//				+ "CASE WHEN EXISTS("
//				+ " SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
//				+ "LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ " LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
//				+ " WHERE QL.DJLX='100' AND QL.QLLX='23' AND FSQL.DYFS='1' AND DJDY.BDCDYID=DY.BDCDYID)"
//				+ " THEN '1' ELSE '0' END AS YBDY,"
//				+ " CASE WHEN EXISTS("
//				+ "SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
//				+ "LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ "LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
//				+ " WHERE QL.DJLX='100' AND QL.QLLX='23' AND FSQL.DYFS='2' AND DJDY.BDCDYID=DY.BDCDYID)"
//				+ " THEN '1' ELSE '0' END AS ZGEDY,"
//				+ "  CASE WHEN EXISTS("
//				+ " SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
//				+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ " LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
//				+ " WHERE QL.DJLX='700' AND QL.QLLX='23' AND FSQL.DYFS='1' AND DJDY.BDCDYID=DY.BDCDYID)"
//				+ " THEN '1' ELSE '0' END AS YBDYYG,"
//				+ "  CASE WHEN EXISTS("
//				+ " SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
//				+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ " LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
//				+ " WHERE QL.DJLX='700' AND QL.QLLX='23' AND FSQL.DYFS='2' AND DJDY.BDCDYID=DY.BDCDYID)"
//				+ " THEN '1' ELSE '0' END AS ZGEDYYG,"
//				+ "  CASE WHEN EXISTS("
//				+ " SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
//				+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ " WHERE QL.DJLX='700' AND QL.QLLX='4' AND DJDY.BDCDYID=DY.BDCDYID)"
//				+ " THEN '1' ELSE '0' END AS YGDJ,"
//				+ "   CASE WHEN EXISTS("
//				+ " SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY "
//				+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID "
//				+ " WHERE QL.DJLX='800' AND QL.QLLX='99' AND DJDY.BDCDYID=DY.BDCDYID)"
//				+ " THEN '1' ELSE '0' END AS CFDJ,"
//				+ "  CASE WHEN EXISTS("
//				+ "    SELECT 1 FROM BDCK.BDCS_DYXZ DYXZ WHERE DY.BDCDYID=DYXZ.BDCDYID)"
//				+ "    THEN '1' ELSE '0' END AS DYXZ"
//				+ " FROM BDCK.BDCS_H_XZY DY WHERE DY.BDCDYID='" + bdcdyid + "'";
//		List<Map> lstcq = baseCommonDao.getDataListByFullSql(sql);
//		if (!StringHelper.isEmpty(lstcq) && lstcq.size() > 0) {
//			Map m = lstcq.get(0);
//			qfzt.setYZJGCYBDY(m.get("YBDY").toString());
//			qfzt.setYZJGCZGEDY(m.get("ZGEDY").toString());
//			qfzt.setYQFCF(m.get("CFDJ").toString());
//			qfzt.setYQFXZ(m.get("DYXZ").toString());
//			qfzt.setYYGSPFYBDY(m.get("YBDYYG").toString());
//			qfzt.setYYGSPFZGEDY(m.get("ZGEDYYG").toString());
//			qfzt.setYYGSPFYGDJ(m.get("YGDJ").toString());
//		}
		return qfzt;
	}

	/**
	 * 获取自然幢基本信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月1日下午3:56:11
	 * @param zrzbdcdyid
	 * @param dyly
	 * @param bdcdylx
	 * @return
	 */
	private BuildingInfo getBuildingInfo(String zrzbdcdyid, DJDYLY dyly,
			BDCDYLX bdcdylx) {
		BuildingInfo buildinginfo = new BuildingInfo();
		RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly, zrzbdcdyid);
		if (!StringHelper.isEmpty(unit)) {
			Building building = (Building) unit;
			buildinginfo.DSCS = StringHelper.formatDouble(building.getDSCS());
			buildinginfo.DXCS = StringHelper.formatDouble(building.getDXCS());
			buildinginfo.JZMJ = StringHelper.formatDouble(building.getMJ());
			buildinginfo.ZCS = StringHelper.formatDouble(building.getZCS());
			buildinginfo.ZTS = StringHelper.FormatByDatatype(building.getZTS());
			buildinginfo.Zddytdmj = StringHelper.formatDouble(building.getDYTDMJ());
			buildinginfo.Zdfttdmj = StringHelper.formatDouble(building.getFTTDMJ());
			ObjectMapper mapper = new ObjectMapper();
			String Json = "";
			try {
				Json = mapper.writeValueAsString(building);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			Map map = null; 
			try {
				map = mapper.readValue(Json, Map.class);
				if(map.containsKey("jzwjbyt")){
					Object v = map.get("jzwjbyt");
					String value = null;
					if(!StringHelper.isEmpty(v)){
						value = (String) v;
					}
					Object valuename = ConstHelper.getNameByValue("FWYT", value);
					map.put("jzwjbytname", valuename);
				}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buildinginfo.build = map;
			if(DJDYLY.DC.equals(dyly)){
				dyly = DJDYLY.XZ;
			}
			RealUnit ZDunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, dyly, building.getZDBDCDYID());
			if(ZDunit!=null){
				List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID='"+ ZDunit.getId() +"'");
				if(dy!=null&&dy.size()>0){
					buildinginfo.ZDInfo = getStatus(dy.get(0).getDJDYID(), ZDunit.getId(), ZDunit.getBDCDYLX().Value);
				}
				buildinginfo.ZDbdcdyid = ZDunit.getId();
				buildinginfo.Zdzl = ZDunit.getZL();
			}
		}
		return buildinginfo;
	}

	/**
	 * 通过实测不动产单元ID值，获取预测不动产单元ID值
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月2日上午11:35:26
	 * @param bdcdyid
	 *            ，实测不动产单元ID
	 * @return
	 */
	private QFCQZTDY getQFBDCDYID(String bdcdyid, BDCDYLX lx) {
		QFCQZTDY qfcqztdy = new QFCQZTDY();
		if (BDCDYLX.YCH.equals(lx)) {
			qfcqztdy = getQFCQZTDY(bdcdyid);
		} else if (BDCDYLX.H.equals(lx)) {
			List<Map> gxs = baseCommonDao
					.getDataListByFullSql("SELECT GX.YCBDCDYID FROM BDCK.YC_SC_H_XZ GX WHERE SCBDCDYID='"
							+ bdcdyid + "'");
			if (!StringHelper.isEmpty(gxs) && gxs.size() > 0) {
				Map gx = gxs.get(0);
				String ycbdcdyid = StringHelper.FormatByDatatype(gx
						.get("YCBDCDYID"));
				qfcqztdy = getQFCQZTDY(ycbdcdyid);
			}
		}
		return qfcqztdy;
	}

	/**
	 * 通过自然幢不动产单元ID,登记单元来源获取现房楼盘表信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月2日下午2:34:12
	 * @param zrzbdcdyid
	 *            ，自然幢不动产单元ID
	 * @param dyly
	 *            ,登记单元来源(现在，调查)
	 * @return
	 */
	private BuildingTalbe getXFBuildingInfo(String zrzbdcdyid, DJDYLY dyly) {
		BuildingTalbe createbt = new BuildingTalbe();
		// String hsql=
		// " SELECT
		// FWBM,SCJZMJ,BDCDYH,SCTNJZMJ,QSC,ZZC,SZC,FH,GHYT,FWXZ,FWJG,FWTDYT,DJZT,BDCDYID,
		// 0 ZT,DYH FROM BDCDCK.BDCS_H_GZ DY WHERE DY.DJZT !='03' AND NOT
		// EXISTS("+
		// "SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_XMXX XMXX
		// ON XMXX.XMBH=DJDY.XMBH "+
		// " WHERE XMXX.DJLX='300' AND XMXX.QLLX='4'AND DJDY.BDCDYID=DY.BDCDYID)
		// AND DY.ZRZBDCDYID='"+zrzbdcdyid+"'"+
		// " union select
		// FWBM,SCJZMJ,BDCDYH,SCTNJZMJ,QSC,ZZC,SZC,FH,GHYT,FWXZ,FWJG,FWTDYT,DJZT,BDCDYID,1
		// ZT,DYH FROM BDCK.BDCS_H_XZ XZDY WHERE
		// XZDY.ZRZBDCDYID='"+zrzbdcdyid+"'"+
		// " ORDER BY QSC,FH";
		// List<Map> llll=baseCommonDao.getDataListByFullSql(hsql);
		// 调查库户信息
		String bdcdcksql = "  SELECT SCFTJZMJ,FTTDMJ,FWBM,SCJZMJ,BDCDYH,SCTNJZMJ,QSC,ZZC,SZC,FH,GHYT,FWXZ,FWJG,FWTDYT,DJZT,BDCDYID, 0 ZT,DYH ,ZL FROM BDCDCK.BDCS_H_GZ DY WHERE (DY.DJZT !='03'OR DY.DJZT IS NULL) AND NOT EXISTS("
				+ "SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=DJDY.XMBH "
				+ " WHERE XMXX.DJLX='300' AND XMXX.QLLX='4'AND DJDY.BDCDYID=DY.BDCDYID) AND DY.ZRZBDCDYID='"
				+ zrzbdcdyid + "'";
		// 登记库户信息
		String bdcksql = " select SCFTJZMJ,FTTDMJ,FWBM,SCJZMJ,BDCDYH,SCTNJZMJ,QSC,ZZC,SZC,FH,GHYT,FWXZ,FWJG,FWTDYT,DJZT,BDCDYID,1 ZT,DYH,ZL FROM BDCK.BDCS_H_XZ XZDY WHERE  XZDY.ZRZBDCDYID='"
				+ zrzbdcdyid + "'";
		int ckgs = 0;// 车库个数
		int zzgs = 0;// 住宅个数
		Map Dicmap = ConstHelper.getDictionary("FWYT");
		Map<Object, Integer> ytmap = new HashMap<Object, Integer>();
		for (Object value : Dicmap.values()) {
			ytmap.put(value, 0);
		}
		List<Map> bdcdcklst = baseCommonDao.getDataListByFullSql(bdcdcksql);
		List<Map> bdcklst = baseCommonDao.getDataListByFullSql(bdcksql);
		List<Map> lst = new ArrayList<Map>();
		List<String> bdcdyids = new ArrayList<String>();
		// 不动产调查库的户数据
		if (!StringHelper.isEmpty(bdcdcklst) && bdcdcklst.size() > 0) {
			for (Map m : bdcdcklst) {
				lst.add(m);
				if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
					bdcdyids.add(m.get("BDCDYID").toString());
				}
			}
		}
		// 不动产库户的数据
		if (!StringHelper.isEmpty(bdcklst) && bdcklst.size() > 0) {
			for (Map m : bdcklst) {
				if (bdcdyids.size() > 0) {
					if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
						if (!bdcdyids.contains(m.get("BDCDYID").toString())) {
							lst.add(m);
						}
					}
				} else {
					lst.add(m);
				}
			}
		}
		// 地下层数，地上层数，总层数，默认为0
		Integer dxcs = 0, dscs = 0, zcs = 0;
		if (!StringHelper.isEmpty(lst) && lst.size() > 0) {
			List<HouseInfoDY> lstdy = new ArrayList<BuildingTalbe.HouseInfoDY>();
			for (Map m : lst) {
				HouseInfoDY houseInfody = new HouseInfoDY();// 房屋单元信息+具体的状态
				HouseInfo houseinfo = new HouseInfo();
				String id = StringHelper.FormatByDatatype(m.get("BDCDYID"));
				String zt = StringHelper.FormatByDatatype(m.get("ZT"));
				houseinfo.setFH(StringHelper.FormatByDatatype(m.get("FH")));
				houseinfo.setBDCDYH(StringHelper.FormatByDatatype(m
						.get("BDCDYH")));
				houseinfo.setFWBM(StringHelper.FormatByDatatype(m.get("FWBM")));
				double scjzmj = StringHelper.getDouble(m.get("SCJZMJ"));
				houseinfo.setJZMJ(StringHelper.formatDouble(scjzmj));
				double sctnjzmj = StringHelper.getDouble(m.get("SCTNJZMJ"));
				houseinfo.setTNJZMJ(StringHelper.formatDouble(sctnjzmj));
				houseinfo.setDYH(StringHelper.FormatByDatatype(m.get("DYH")));
				String ghyt = StringHelper.FormatByDatatype((m.get("GHYT")));
				if ("185".equals(ghyt)) {
					ckgs++;
				}
				ghyt = ConstHelper.getNameByValue("FWYT", ghyt);
				if(ytmap.containsKey(ghyt)){
					Object value = ytmap.get(ghyt);
					if(!StringHelper.isEmpty(value)){
						ytmap.put(ghyt, Integer.parseInt(String.valueOf(value))+1);
					}
				}
				houseinfo.setGHYT(ghyt);
				houseinfo.setQlrxm(getqlrmc(id));
				houseinfo.setJZMJ(StringHelper.formatDouble(scjzmj));
				houseinfo.setTNJZMJ(StringHelper.formatDouble(sctnjzmj));
				String fwxz = StringHelper.FormatByDatatype((m.get("FWXZ")));
				fwxz = ConstHelper.getNameByValue("FWXZ", fwxz);
				houseinfo.setFWXZ(fwxz);
				String fwjg = StringHelper.FormatByDatatype((m.get("FWJG")));
				fwjg = ConstHelper.getNameByValue("FWJG", fwjg);
				houseinfo.setFWJG(fwjg);
				String fwtdyt = StringHelper.FormatByDatatype(m.get("FWTDYT"));
				fwtdyt = ConstHelper.getNameByValue("TDYT", fwtdyt);
				houseinfo.setFWTDYT(fwtdyt);
				houseinfo.setFTJZMJ(StringHelper.FormatByDatatype(m.get("SCFTJZMJ")));
				houseinfo.setFTTDMJ(StringHelper.FormatByDatatype(m.get("FTTDMJ")));
				// 房屋基本信息
				houseInfody.setHouseinfo(houseinfo);
				// 现房产权信息
				houseInfody.setXfcqztdy(getXFCQZTDY(id, zt));
				// 期房产权信息
				houseInfody.setQfcqztdy(getQFBDCDYID(id, BDCDYLX.H));
				Integer qsc = StringHelper.getInt(m.get("QSC"));
				if (qsc > dscs)// 获取地上层数的最大值
				{
					dscs = qsc;
				}
				if (qsc < dxcs)// 获取地下层数的最小值
				{
					dxcs = qsc;
				}
				houseinfo.setQSC(qsc.toString());
				houseinfo.setZZC(StringHelper.FormatByDatatype(m.get("ZZC")));
				houseinfo.setSZC(StringHelper.FormatByDatatype(m.get("SZC")));
				houseinfo.setBdcdyid(StringHelper.FormatByDatatype(m
						.get("BDCDYID")));
				houseinfo.setZL(StringHelper.FormatByDatatype(m.get("ZL")));
				houseinfo.setBDCDYLX(BDCDYLX.H.Value);
				lstdy.add(houseInfody);
			}
			if (dxcs < 0)// 求总层数
			{
				zcs = -dxcs + dscs;
			} else {
				zcs = dscs;
			}
			Collections.sort(lstdy,
					new Comparator<BuildingTalbe.HouseInfoDY>() {
						@Override
						public int compare(BuildingTalbe.HouseInfoDY treei,
								BuildingTalbe.HouseInfoDY treej) {
							String fhi = null;
							String fhj = null;
							try {
								if (!StringHelper.isEmpty(treei.getHouseinfo()
										.getFH())) {
									fhi = treei.getHouseinfo().getFH()
											.replaceAll("-", "");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								if (!StringHelper.isEmpty(treei.getHouseinfo()
										.getFH())) {
									fhj = treej.getHouseinfo().getFH()
											.replaceAll("-", "");
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
							if (fhi == null || fhj != null
									&& fhi.compareTo(fhj) > 0) {
								return 1;
							} else if (fhi.equals(fhj)) {
								return 0;
							}
							return -1;
						}
					});
			createbt.setBuildingTalbes(lstdy);
			// 自然幢基本信息
			BuildingInfo binfo = getBuildingInfo(zrzbdcdyid, dyly, BDCDYLX.ZRZ);
			int bzcs = StringHelper.getInt(binfo.ZCS);
			if (bzcs == 0) // 若自然幢中的地上层数或总层数为0，则从房屋中获取
			{
				binfo.setDSCS(dscs.toString());
				Integer cs = dxcs;
				if (dxcs < 0) {
					cs = -dxcs;
				}
				binfo.setDXCS(cs.toString());
				binfo.setZCS(zcs.toString());
			}
			binfo.setCKGS(ckgs);
			Collection<Integer> col = ytmap.values();
			while (col.contains(0)) {
				col.remove(0);
			}
			binfo.setYTandGS(ytmap);
			binfo.setFlagBuilding("0");
			createbt.setBuildinfo(binfo);

		}
		return createbt;
	}
	
	/********************START**************************/

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BuildingTalbe getXFBuildingInfo_new(String zrzbdcdyid, DJDYLY dyly, String szc, String hbdcdyid,
			String ljzid) {
		BuildingTalbe createbt = new BuildingTalbe();
		BuildingInfo binfo = new BuildingInfo();
		binfo = getBuildingInfo(zrzbdcdyid, dyly, BDCDYLX.ZRZ);
		binfo.flagBuilding = "0";
		// 调查库户信息
		String bdcdcksql = "SELECT SCFTJZMJ AS FTJZMJ,FTTDMJ,FWBM,SCJZMJ AS JZMJ,BDCDYH,SCTNJZMJ AS TNJZMJ,QSC,ZZC,SZC,FH,GHYT AS FWYT,FWXZ,FWJG,FWTDYT AS TDYT,DJZT,BDCDYID, 0 AS　ZT,DYH ,ZL FROM BDCDCK.BDCS_H_GZ DY WHERE (DY.DJZT !='03'OR DY.DJZT IS NULL) AND NOT EXISTS("
				+ "SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=DJDY.XMBH "
				+ " WHERE XMXX.DJLX='300' AND XMXX.QLLX='4'AND DJDY.BDCDYID=DY.BDCDYID) AND ";
		if (StringHelper.isEmpty(ljzid)) {
			bdcdcksql += " DY.ZRZBDCDYID='" + zrzbdcdyid + "'";
		} else {
			bdcdcksql += " DY.LJZID='" + ljzid + "'";
		}
		// 登记库户信息
		String bdcksql = "SELECT SCFTJZMJ AS FTJZMJ,FTTDMJ,FWBM,SCJZMJ AS JZMJ,BDCDYH,SCTNJZMJ AS TNJZMJ,QSC,ZZC,SZC,FH,GHYT AS FWYT,FWXZ,FWJG,FWTDYT AS TDYT,DJZT,BDCDYID,1　AS ZT,DYH,ZL FROM BDCK.BDCS_H_XZ XZDY WHERE ";
		if (StringHelper.isEmpty(ljzid)) {
			bdcksql += " XZDY.ZRZBDCDYID='" + zrzbdcdyid + "'";
		} else {
			bdcksql += " XZDY.LJZID='" + ljzid + "'";
		}
		
		
		
		List<Map> bdcdcklst = baseCommonDao.getDataListByFullSql(bdcdcksql);
		List<Map> bdcklst = baseCommonDao.getDataListByFullSql(bdcksql);
		List<Map> lst = new ArrayList<Map>();
		List<String> bdcdyids = new ArrayList<String>();
		// 不动产调查库的户数据
		
		if (!StringHelper.isEmpty(bdcdcklst) && bdcdcklst.size() > 0) {
			for (Map m : bdcdcklst) {
				lst.add(m);
				if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
					bdcdyids.add(m.get("BDCDYID").toString());
				}
			}
			
			
		}
		// 不动产库户的数据
		if (!StringHelper.isEmpty(bdcklst) && bdcklst.size() > 0) {
			for (Map m : bdcklst) {
				if (bdcdyids.size() > 0) {
					if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
						if (!bdcdyids.contains(m.get("BDCDYID").toString())) {
							lst.add(m);
						}
					}
				} else {
					lst.add(m);
				}
			}
		}
		final boolean Group = "1".equals(ConfigHelper.getNameByValue("BuildingTableGroup"));
		List<String> DYHGroup = new ArrayList<String>();
		// 权利查询
		Map<String, XFCQZTDY> xfqlxx = GetXFQlInfoByZRZ(dyly, zrzbdcdyid, BDCDYLX.H, ljzid);
		Map<String, QFCQZTDY> qfqlxx = GetQFQlInfoByZRZ(dyly, zrzbdcdyid, BDCDYLX.H, ljzid);
		Map Dicmap = ConstHelper.getDictionary("FWYT");
		Map<Object, Integer> ytmap = new HashMap<Object, Integer>();
		for (Object value : Dicmap.values()) {
			ytmap.put(value, 0);
		}
		Integer dxcs = StringHelper.isEmpty(binfo.DXCS) ? StringHelper.getInt(binfo.DXCS) : 0,
				dscs = StringHelper.isEmpty(binfo.DSCS) ? StringHelper.getInt(binfo.DSCS) : 0,
				zcs = StringHelper.isEmpty(binfo.ZCS) ? StringHelper.getInt(binfo.ZCS) : 0;
		// 需要转换的字典类型
		List<String> ConstTypes = new ArrayList<String>();
		ConstTypes.add("FWYT");
		ConstTypes.add("FWXZ");
		ConstTypes.add("FWJG");
		ConstTypes.add("TDYT");
		List<String> GroupYT = new ArrayList<String>();
		GroupYT.add("30");
		GroupYT.add("31");
		GroupYT.add("32");
		GroupYT.add("33");
		GroupYT.add("34");
		GroupYT.add("35");
		int dywdjs=0;
		int dyydjs=0;
		int count=0;
		BigDecimal zjzmj=new BigDecimal(new String ("0"));
		BigDecimal dywdjmj=new BigDecimal(new String ("0"));
		BigDecimal dyydjmj=new BigDecimal(new String ("0"));
		for (Map m : lst) {
			// 计算地上层数、底下层数
			Integer qsc = StringHelper.getInt(m.get("QSC"));
			dxcs = qsc < dxcs ? qsc : dxcs;
			dscs = qsc > dscs ? qsc : dscs;
			// 将房屋信息按照设定的字典类型转换
			for (String type : ConstTypes) {
				String TypeOfKey = StringHelper.FormatByDatatype(m.get(type));
				String TypeOfName = ConstHelper.getNameByValue(type, TypeOfKey);
				if ("FWYT".equals(type)) {
					m.put("GHYT", TypeOfName);
					// 将房屋按照GHYT分类统计
					if (ytmap.containsKey(TypeOfName)) {
						// 分组时，记录分组的bdcdyid--------依据用途分组，商业用途类
						if (Group) {
							if (GroupYT.contains(TypeOfKey)) {
								String key = StringHelper.formatObject(m.get("BDCDYID"));
								if (!StringHelper.isEmpty(key))
									DYHGroup.add(key);
							}
						}
						Object value = ytmap.get(TypeOfName);
						if (!StringHelper.isEmpty(value)) {
							ytmap.put(TypeOfName, Integer.parseInt(String.valueOf(value)) + 1);
						}
					}
				} else if ("TDYT".equals(type)) {
					m.put("FWTDYT", TypeOfName);
				} else {
					m.put(type, TypeOfName);
				}
			}
			XFCQZTDY xf = new XFCQZTDY();
			xf = xfqlxx.get(m.get("BDCDYID"));
			QFCQZTDY qf = new QFCQZTDY();
			qf = qfqlxx.get(m.get("BDCDYID"));
			if (qf == null) {
				qf = new QFCQZTDY();
			}
			if (xf == null) {
				xf = new XFCQZTDY();
			}
			//计算房屋中单元未登记数，单元正登记数，已登记数
			String xfjs = JSONObject.toJSONString(xf);
			Map<String,String> xfmap = JSONObject.parseObject(xfjs, Map.class);
			Collection<String> xfztvalue = xfmap.values();
			for (String s : xfztvalue) {
				if(!"0".equals(s)) {
						dyydjs+=1;
						break;
				
				}
			}
			// 房屋单元信息+具体的状态
			HouseInfo houseinfo = new HouseInfo();
			HouseInfoDY houseInfody = new HouseInfoDY();
			String jsonStr = JSONObject.toJSONString(m);
			houseinfo = JSONObject.parseObject(jsonStr, HouseInfo.class);
			houseinfo.setBDCDYLX(BDCDYLX.H.Value);
			houseinfo.setRealszc(houseinfo.getSZC());
			if (houseinfo.getBDCDYID().equals(hbdcdyid)) {
				binfo.hszc = StringHelper.getInt(houseinfo.getSZC());
				List<Map> qxgls = getqxglqk_xf(hbdcdyid);
				if(qxgls != null && qxgls.size()>0){
					houseInfody.setSfqxgl("1");
					houseinfo.setYCFTJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCFTJZMJ")));
					houseinfo.setYCJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCJZMJ")));
					houseinfo.setYCTNJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCTNJZMJ")));
				}else{
					houseInfody.setSfqxgl("0");
				}
			}else{
				List<Map> qxgls = getqxglqk_xf(houseinfo.getBDCDYID());
				if(qxgls != null && qxgls.size()>0){
					houseInfody.setSfqxgl("1");
					houseinfo.setYCFTJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCFTJZMJ")));
					houseinfo.setYCJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCJZMJ")));
					houseinfo.setYCTNJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCTNJZMJ")));
				}else{
					houseInfody.setSfqxgl("0");
				}
			}
			houseinfo.Realszc = houseinfo.QSC;
			if (!StringHelper.isEmpty(houseinfo.ZZC) && !StringHelper.isEmpty(houseinfo.QSC)) {
				if (!houseinfo.ZZC.equals(houseinfo.QSC)) {
					int loftqsc = StringHelper.getInt(houseinfo.QSC);
					int loftzzc = StringHelper.getInt(houseinfo.ZZC);
					while (loftqsc < loftzzc) {
						++loftqsc;
						HouseInfo info = new HouseInfo();
						info = JSONObject.parseObject(jsonStr, HouseInfo.class);
						info.setBDCDYLX(BDCDYLX.H.Value);
						info.setRealszc(info.getSZC());

						info.Realszc = loftqsc + "";

						HouseInfoDY dy = new HouseInfoDY();
						dy.houseinfo = info;
						List<Map> qxgls = getqxglqk_xf(info.BDCDYID);
						if(qxgls != null && qxgls.size()>0){
							dy.setSfqxgl("1");
							info.setYCFTJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCFTJZMJ")));
							info.setYCJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCJZMJ")));
							info.setYCTNJZMJ(StringHelper.formatDouble(qxgls.get(0).get("YCTNJZMJ")));
						}else{
							dy.setSfqxgl("0");
						}
						dy.qfcqztdy = qf;
						dy.xfcqztdy = xf;
						buildingTalbes.add(dy);
					}
				}
			}

			houseInfody.setHouseinfo(houseinfo);
			houseInfody.setQfcqztdy(qf);
			houseInfody.setXfcqztdy(xf);
			buildingTalbes.add(houseInfody);
			if(dyydjs!=count){
				dyydjmj=dyydjmj.add(new BigDecimal(houseinfo.JZMJ));
				count =dyydjs;
			}else {
				dywdjmj=dywdjmj.add(new BigDecimal(houseinfo.JZMJ));
			}
			zjzmj=zjzmj.add(new BigDecimal(houseinfo.JZMJ));
		}
		dywdjs=lst.size()-dyydjs;
		//添加建筑总面积数
		binfo.build.put("zjzmj", zjzmj.toString()+"㎡");
		//添加未登记单元总面积数
		binfo.build.put("dywdjmj", dywdjmj.toString()+"㎡");
		//添加已登记单元总面积数
		binfo.build.put("dyydjmj", dyydjmj.toString()+"㎡");
		//添加单元未登记数
		binfo.build.put("dywdjs", dywdjs);
		//添加单元已登记数
		binfo.build.put("dyydjs", dyydjs);
		// 求总层数z
		if (dxcs < 0) {
			zcs = -dxcs + dscs;
		} else {
			zcs = dscs;
		}
		// 房屋用途统计后过滤
		Collection<Integer> col = ytmap.values();
		while (col.contains(0)) {
			col.remove(0);
		}
		// 自然幢基本信息
		binfo.setZCS(StringHelper.FormatByDatatype(zcs));
		binfo.setDXCS(StringHelper.FormatByDatatype(dxcs));
		binfo.setDSCS(StringHelper.FormatByDatatype(dscs));
		binfo.setYTandGS(ytmap);
		if (Group) {
			binfo.dYHGroup = DYHGroup;
		} else {
			binfo.dYHGroup = null;
		}
		createbt.setBuildinfo(binfo);
		Collections.sort(buildingTalbes, new Comparator<BuildingTalbe.HouseInfoDY>() {
			@Override
			public int compare(BuildingTalbe.HouseInfoDY treei, BuildingTalbe.HouseInfoDY treej) {
				String fhi = "";
				String fhj = "";
				String dyhi = "";
				String dyhj = "";
				try {
					if (treei != null && treei.getHouseinfo() != null) {
						if (!StringHelper.isEmpty(treei.getHouseinfo().getFH())) {
							fhi = treei.getHouseinfo().getFH();
						}
						if (!StringHelper.isEmpty(treej.getHouseinfo().getFH())) {
							fhj = treej.getHouseinfo().getFH();
						}
						if (!StringHelper.isEmpty(treei.getHouseinfo().getFH())) {
							dyhi = treei.getHouseinfo().getDYH();
						}
						if (!StringHelper.isEmpty(treej.getHouseinfo().getFH())) {
							dyhj = treej.getHouseinfo().getDYH();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (StringHelper.isEmpty(dyhi) || !StringHelper.isEmpty(dyhj) && ObjectHelper.cat(dyhi, dyhj) < 0)
					return -1;
				if (StringHelper.isEmpty(dyhj) || !StringHelper.isEmpty(dyhi) && ObjectHelper.cat(dyhj, dyhi) < 0)
					return 1;
				if (StringHelper.isEmpty(fhi) || !StringHelper.isEmpty(fhj) && ObjectHelper.cat(fhi, fhj) < 0)
					return -1;
				if (StringHelper.isEmpty(fhj) || !StringHelper.isEmpty(fhi) && ObjectHelper.cat(fhj, fhi) < 0)
					return 1;
				return 0;
			}
		});
		createbt.setBuildingTalbes(buildingTalbes);
		return createbt;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, XFCQZTDY> GetXFQlInfoByZRZ(DJDYLY dyly, String zrzbdcdyid,BDCDYLX bdcdylx, String ljzid) {
		if(!StringHelper.isEmpty(ljzid)){
			zrzbdcdyid=ljzid;
		}
		String sql = " SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR, QL.HTH,NULL AS SFDB,NVL2(QLXZ.QLID,'XZ','LS') AS LY "
				+ " FROM BDCK.BDCS_H_XZ H "
				+ " LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.BDCDYID = H.BDCDYID "
				+ " LEFT JOIN BDCK.BDCS_QL_XZ QLXZ ON QL.QLID=QLXZ.QLID "
				+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'"
				+ " UNION ALL SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,XM.SFDB,'GZ' AS LY "
				+ " FROM BDCK.BDCS_H_XZ H "
				+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.BDCDYID = H.BDCDYID "
				+ " LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH = QL.XMBH "
				+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'";
		String xzsql = "SELECT H.BDCDYID FROM BDCK.BDCS_H_XZ H "
				+ " INNER JOIN BDCK.BDCS_DYXZ XZ ON XZ.BDCDYID=H.BDCDYID WHERE XZ.YXBZ='1' AND H.ZRZBDCDYID='"
				+ zrzbdcdyid + "'";
		if(DJDYLY.DC.equals(dyly)){
			sql = " SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR, QL.HTH,NULL AS SFDB,NVL2(QLXZ.QLID,'XZ','LS') AS LY "
					+ " FROM BDCK.BDCS_H_XZ H "
					+ " LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.BDCDYID = H.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_XZ QLXZ ON QL.QLID=QLXZ.QLID "
					+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'"
					+ " UNION ALL SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,XM.SFDB,'GZ' AS LY "
					+ " FROM BDCK.BDCS_H_GZ H "
					+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.BDCDYID = H.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH = QL.XMBH "
					+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'";
		}
		if(!StringHelper.isEmpty(ljzid)){
			sql = sql.replaceAll("H.ZRZBDCDYID", "H.LJZID");
			xzsql = xzsql.replaceAll("H.ZRZBDCDYID", "H.LJZID");
		}
		if(BDCDYLX.YCH.equals(bdcdylx)){
			sql = " SELECT YCH.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR, QL.HTH,NULL AS SFDB,NVL2(QLXZ.QLID,'XZ','LS') AS LY "
					+ " FROM BDCK.BDCS_H_XZ H "
					+ " INNER JOIN BDCK.YC_SC_H_XZ YS ON YS.SCBDCDYID=H.BDCDYID "
					+ " INNER JOIN BDCK.BDCS_H_XZY YCH ON YS.YCBDCDYID=YCH.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.BDCDYID = H.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_XZ QLXZ ON QL.QLID=QLXZ.QLID "
					+ " WHERE YCH.ZRZBDCDYID ='"+zrzbdcdyid+"'"
					+ " UNION ALL SELECT YCH.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,XM.SFDB,'GZ' AS LY "
					+ " FROM BDCK.BDCS_H_XZ H "
					+ " INNER JOIN BDCK.YC_SC_H_XZ YS ON YS.SCBDCDYID=H.BDCDYID "
					+ " INNER JOIN BDCK.BDCS_H_XZY YCH ON YS.YCBDCDYID=YCH.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.BDCDYID = H.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH = QL.XMBH "
					+ " WHERE YCH.ZRZBDCDYID ='"+zrzbdcdyid+"'";
			xzsql = " SELECT YCH.BDCDYID FROM BDCK.BDCS_H_XZ H "
					+ " INNER JOIN BDCK.YC_SC_H_XZ YS ON YS.SCBDCDYID=H.BDCDYID "
					+ " INNER JOIN BDCK.BDCS_H_XZY YCH ON YS.YCBDCDYID=YCH.BDCDYID "
					+ " INNER JOIN BDCK.BDCS_DYXZ XZ ON XZ.BDCDYID=H.BDCDYID "
					+ " WHERE XZ.YXBZ='1' AND YCH.ZRZBDCDYID='"
					+ zrzbdcdyid + "'";
			if(!StringHelper.isEmpty(ljzid)){
				sql = sql.replaceAll("YCH.ZRZBDCDYID", "YCH.LJZID");
				xzsql = xzsql.replaceAll("YCH.ZRZBDCDYID", "YCH.LJZID");
			}
		}
		List<Map> lst = baseCommonDao.getDataListByFullSql(sql);	
		List<Map> xzlst = baseCommonDao.getDataListByFullSql(xzsql);
		List<String> xzlist = new ArrayList<String>();
		for (Map map : xzlst) {
			xzlist.add(StringHelper.formatObject(map.get("BDCDYID")));
		}
		Map<String, XFCQZTDY> GX = new HashMap<String, XFCQZTDY>();
		if (!StringHelper.isEmpty(lst) && lst.size() > 0) {
			for (Map m : lst) {
				String bdcdyid = StringHelper.formatObject(m.get("BDCDYID"));
				String ly = StringHelper.formatObject(m.get("LY"));
				
				XFCQZTDY xfzt = new XFCQZTDY();
				if(GX.containsKey(bdcdyid)){
					xfzt = GX.get(bdcdyid);
					xfzt = Updatexfzt(m,xfzt,ly);
					if(xzlist.contains(bdcdyid)){
						xfzt.setXFXZDJ("1");
					}
				}else{
					xfzt = Updatexfzt(m,xfzt,ly);
					if(xzlist.contains(bdcdyid)){
						xfzt.setXFXZDJ("1");
					}
					GX.put(bdcdyid, xfzt);
				}
				
			}
		}
		return GX;
	}

	@SuppressWarnings("rawtypes")
	private XFCQZTDY Updatexfzt(Map m, XFCQZTDY xfzt, String ly) {
		String djlx = StringHelper.formatObject(m.get("DJLX"));
		String qllx = StringHelper.formatObject(m.get("QLLX"));
		String sfdb = StringHelper.formatObject(m.get("SFDB"));
		if("XZ".equals(ly)){
			// 现房的抵押
			if (QLLX.DIYQ.Value.equals(qllx)) {
				xfzt.setXFDIYDJ("1");
			}
			// 现房的预告登记抵押
			if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx)) {
				xfzt.setXFDIYYGDJ("1");
			}
			// 现房的转移预告登记
			if (DJLX.YGDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
				xfzt.setXFZYYGDJ("1");
			}
			// 现房的查封登记
			if (DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
				xfzt.setXFCFDJ("1");
			}
			// 现房的首次登记
			if (DJLX.CSDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
				xfzt.setXFSCDJ("1");
			}
			// 现房的转移登记
			if (DJLX.ZYDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
				xfzt.setXFZYDJ("1");
			}
			// 判断权利中的合同号和买受人存在，存在即已预售
			if (("4".equals(qllx) || "6".equals(qllx) || "8" .equals(qllx))) {
				if (!StringHelper.isEmpty(m.get("MSR")) && !StringHelper.isEmpty(m.get("HTH"))) {
					xfzt.setYXFYS("1");
				}
			}
			// 现房异议
			if (DJLX.YYDJ.Value.equals(djlx)) {
				xfzt.setXFYYDJ("1");
			}
		}else if("LS".equals(ly)){
			// 现房的首次登记
			if (DJLX.CSDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
				xfzt.setXFSCDJ("1");
			}
			// 现房的转移登记
			if (DJLX.ZYDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
				xfzt.setXFZYDJ("1");
			}
		} else if ("GZ".equals(ly)) {
			if ("0".equals(sfdb)) {// 未登簿
				// 现房的抵押
				if (QLLX.DIYQ.Value.equals(qllx)) {
					xfzt.setXFDIYDJ("2");
				}
				// 现房的预告登记抵押
				if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx)) {
					xfzt.setXFDIYYGDJ("2");
				}
				// 现房的转移预告登记
				if (DJLX.YGDJ.Value.equals(djlx) && QLLX.CFZX.Value.equals(qllx)) {
					xfzt.setXFZYYGDJ("2");
				}
				// 现房的查封登记
				if (DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
					xfzt.setXFCFDJ("2");
				}
				// 判断权利中的合同号和买受人存在，存在即已预售
				if (("4".equals(qllx) || "6".equals(qllx) || "8".equals(qllx))) {
					if (!StringHelper.isEmpty(m.get("MSR")) && !StringHelper.isEmpty(m.get("HTH"))) {
						xfzt.setYXFYS("2");
					}
				}
				// 现房的首次登记
				if (DJLX.CSDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
					xfzt.setXFSCDJ("2");
				}
				// 现房的转移登记
				if (DJLX.ZYDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
					xfzt.setXFZYDJ("2");
				}
				// 现房异议
				if (DJLX.YYDJ.Value.equals(djlx)) {
					xfzt.setXFYYDJ("2");
				}
			}
		}
		return xfzt;
	}

	/**********************END************************/
	
	/**
	 * 通过自然幢不动产单元ID,登记单元来源获取期房楼盘表信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月2日下午3:29:36
	 * @param zrzbdcdyid
	 * @param dyly
	 * @return
	 */
	private BuildingTalbe getQFBuildingInfo(String zrzbdcdyid, DJDYLY dyly) {
		BuildingTalbe createbt = new BuildingTalbe();
		String sql = "select YCFTJZMJ,FTTDMJ,FWBM,YCJZMJ,BDCDYH,YCTNJZMJ,QSC,ZZC,SZC,FH,GHYT,FWXZ,FWJG,FWTDYT,DJZT,BDCDYID,DYH,ZL FROM BDCK.BDCS_H_XZY XZDY WHERE  XZDY.ZRZBDCDYID='"
				+ zrzbdcdyid + "'";
		List<Map> lst = baseCommonDao.getDataListByFullSql(sql);
		int ckgs = 0;// 车库个数
		int zzgs = 0;// 住宅个数
		//FWYT
		Map Dicmap = ConstHelper.getDictionary("FWYT");
		Map<Object, Integer> ytmap = new HashMap<Object, Integer>();
		for (Object value : Dicmap.values()) {
			ytmap.put(value, 0);
		}
		// 地下层数，地上层数，总层数，默认为0
		Integer dxcs = 0, dscs = 0, zcs = 0;
		if (!StringHelper.isEmpty(lst) && lst.size() > 0) {
			List<HouseInfoDY> lstdy = new ArrayList<BuildingTalbe.HouseInfoDY>();
			int i = 0;
			for (Map m : lst) {
				i++;
				HouseInfoDY houseInfody = new HouseInfoDY();// 房屋单元信息+具体的状态
				HouseInfo houseinfo = new HouseInfo();
				String id = StringHelper.FormatByDatatype(m.get("BDCDYID"));
				houseinfo.setFH(StringHelper.FormatByDatatype(m.get("FH")));
				houseinfo.setBDCDYH(StringHelper.FormatByDatatype(m
						.get("BDCDYH")));
				houseinfo.setFWBM(StringHelper.FormatByDatatype(m.get("FWBM")));
				houseinfo.setDYH(StringHelper.FormatByDatatype(m.get("DYH")));
				double scjzmj = StringHelper.getDouble(m.get("YCJZMJ"));
				houseinfo.setJZMJ(StringHelper.formatDouble(scjzmj));
				double sctnjzmj = StringHelper.getDouble(m.get("YCTNJZMJ"));
				houseinfo.setTNJZMJ(StringHelper.formatDouble(sctnjzmj));
				String ghyt = StringHelper.FormatByDatatype((m.get("GHYT")));
				if ("185".equals(ghyt)) {
					ckgs++;
				}
				ghyt = ConstHelper.getNameByValue("FWYT", ghyt);
				if(ytmap.containsKey(ghyt)){
					Object value = ytmap.get(ghyt);
					if(!StringHelper.isEmpty(value)){
						ytmap.put(ghyt, Integer.parseInt(String.valueOf(value))+1);
					}
				}
				houseinfo.setGHYT(ghyt);
				houseinfo.setJZMJ(StringHelper.formatDouble(scjzmj));
				houseinfo.setTNJZMJ(StringHelper.formatDouble(sctnjzmj));
				String fwxz = StringHelper.FormatByDatatype((m.get("FWXZ")));
				fwxz = ConstHelper.getNameByValue("FWXZ", fwxz);
				houseinfo.setFWXZ(fwxz);
				String fwjg = StringHelper.FormatByDatatype((m.get("FWJG")));
				fwjg = ConstHelper.getNameByValue("FWJG", fwjg);
				houseinfo.setFWJG(fwjg);
				String fwtdyt = StringHelper.FormatByDatatype(m.get("FWTDYT"));
				fwtdyt = ConstHelper.getNameByValue("TDYT", fwtdyt);
				houseinfo.setFWTDYT(fwtdyt);
				houseinfo.setQlrxm(getqlrmc(id));
				houseinfo.setFTJZMJ(StringHelper.FormatByDatatype(m.get("YCFTJZMJ")));
				houseinfo.setFTTDMJ(StringHelper.FormatByDatatype(m.get("FTTDMJ")));
				// 房屋基本信息
				houseInfody.setHouseinfo(houseinfo);
				// 现房产权信息
				houseInfody.setXfcqztdy(getXFCQZTDY(id, "0"));
				// 期房产权信息
				houseInfody.setQfcqztdy(getQFBDCDYID(id, BDCDYLX.YCH));
				Integer qsc = StringHelper.getInt(m.get("QSC"));
				if (qsc > dscs)// 获取地上层数的最大值
				{
					dscs = qsc;
				}
				if (qsc < dxcs)// 获取地下层数的最小值
				{
					dxcs = qsc;
				}
				houseinfo.setQSC(qsc.toString());
				houseinfo.setZZC(StringHelper.FormatByDatatype(m.get("ZZC")));
				houseinfo.setSZC(StringHelper.FormatByDatatype(m.get("SZC")));
				houseinfo.setBdcdyid(StringHelper.FormatByDatatype(m
						.get("BDCDYID")));
				houseinfo.setZL(StringHelper.FormatByDatatype(m.get("ZL")));
				houseinfo.setBDCDYLX(BDCDYLX.YCH.Value);
				lstdy.add(houseInfody);
			}
			createbt.setBuildingTalbes(lstdy);
			// 自然幢基本信息
			BuildingInfo binfo = getBuildingInfo(zrzbdcdyid, dyly,
					BDCDYLX.YCZRZ);
			int bzcs = StringHelper.getInt(binfo.ZCS);
			if (bzcs == 0) // 若自然幢中的地上层数或总层数为0，则从房屋中获取
			{
				binfo.setDSCS(dscs.toString());
				Integer cs = dxcs;
				if (dxcs < 0) {
					cs = -dxcs;
				}
				binfo.setDXCS(cs.toString());
				binfo.setZCS(zcs.toString());
			}
			binfo.setCKGS(ckgs);
			binfo.setYTandGS(ytmap);
			binfo.setFlagBuilding("1");
			createbt.setBuildinfo(binfo);
		}
		return createbt;
	}
	
	/*********************START*************************/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BuildingTalbe getQFBuildingInfo_new (String zrzbdcdyid, DJDYLY dyly , String szc, String hbdcdyid, String ljzid) {
		BuildingTalbe createbt = new BuildingTalbe();
		BuildingInfo binfo = new BuildingInfo();
		binfo = getBuildingInfo(zrzbdcdyid, dyly, BDCDYLX.YCZRZ);
		binfo.flagBuilding="1";
		String bdcksql = "SELECT YCFTJZMJ AS FTJZMJ,FTTDMJ,FWBM,YCJZMJ AS JZMJ,BDCDYH,YCTNJZMJ AS TNJZMJ,QSC,ZZC,SZC,FH,GHYT AS FWYT,FWXZ,FWJG,FWTDYT AS TDYT,DJZT,BDCDYID,1　AS ZT,DYH,ZL FROM BDCK.BDCS_H_XZY XZDY WHERE ";
		if(StringHelper.isEmpty(ljzid)){
			bdcksql+= " XZDY.ZRZBDCDYID='" + zrzbdcdyid + "'";
		}else{
			bdcksql+= " XZDY.LJZID='" + ljzid + "'";
		}
		List<Map> lst = baseCommonDao.getDataListByFullSql(bdcksql);
		final boolean Group = "1".equals(ConfigHelper.getNameByValue("BuildingTableGroup"));
		List<String> DYHGroup = new ArrayList<String>();
		Map<String,XFCQZTDY> xfqlxx = GetXFQlInfoByZRZ(dyly,zrzbdcdyid,BDCDYLX.YCH,ljzid);
		Map<String,QFCQZTDY> qfqlxx = GetQFQlInfoByZRZ(dyly,zrzbdcdyid,BDCDYLX.YCH,ljzid);
		Map Dicmap = ConstHelper.getDictionary("FWYT");
		Map<Object, Integer> ytmap = new HashMap<Object, Integer>();
		for (Object value : Dicmap.values()) {
			ytmap.put(value, 0);
		}
		Integer dxcs = StringHelper.isEmpty(binfo.DXCS)?StringHelper.getInt(binfo.DXCS):0, 
				dscs = StringHelper.isEmpty(binfo.DSCS)?StringHelper.getInt(binfo.DSCS):0, 
				zcs = StringHelper.isEmpty(binfo.ZCS)?StringHelper.getInt(binfo.ZCS):0;
		//需要转换的字典类型
		List<String> ConstTypes = new ArrayList<String>();
		ConstTypes.add("FWYT");
		ConstTypes.add("FWXZ");
		ConstTypes.add("FWJG");
		ConstTypes.add("TDYT");
		List<String> GroupYT = new ArrayList<String>();
		GroupYT.add("30");
		GroupYT.add("31");
		GroupYT.add("32");
		GroupYT.add("33");
		GroupYT.add("34");
		GroupYT.add("35");
		//单元未登记数
		int dywdjs=0;
		//单元已登记数
		int dyydjs=0;
		int count=0;
		BigDecimal zjzmj=new BigDecimal(new String("0"));
		BigDecimal dywdjmj=new BigDecimal(new String("0"));
		BigDecimal dyydjmj=new BigDecimal(new String("0"));
		for (Map m : lst) {
			//计算地上层数、底下层数
			Integer qsc = StringHelper.getInt(m.get("QSC"));
			dxcs = qsc<dxcs?qsc:dxcs;
			dscs = qsc>dscs?qsc:dscs;
			//将房屋信息按照设定的字典类型转换
			for (String type : ConstTypes) {
				String TypeOfKey = StringHelper.FormatByDatatype(m.get(type));
				String TypeOfName = ConstHelper.getNameByValue(type, TypeOfKey);
				if("FWYT".equals(type)){
					m.put("GHYT", TypeOfName);
					//将房屋按照GHYT分类统计
					if(ytmap.containsKey(TypeOfName)){
						if(Group){
							if(GroupYT.contains(TypeOfKey)){
								String key = StringHelper.formatObject(m.get("BDCDYID"));
								if(!StringHelper.isEmpty(key))
									DYHGroup.add(key);
							}
						}
						Object value = ytmap.get(TypeOfName);
						if(!StringHelper.isEmpty(value)){
							ytmap.put(TypeOfName, Integer.parseInt(String.valueOf(value))+1);
						}
					}
				}else if("TDYT".equals(type)){
					m.put("FWTDYT", TypeOfName);
				}else{
					m.put(type,TypeOfName);
				}
			}
			XFCQZTDY xf = new XFCQZTDY();
			xf = xfqlxx.get(m.get("BDCDYID"));
			QFCQZTDY qf = new QFCQZTDY();
			qf = qfqlxx.get(m.get("BDCDYID"));
			if(qf==null){
				qf = new QFCQZTDY();
			}
			if(xf==null){
				xf = new XFCQZTDY();
				
			}
			//计算单元正登记数，未登记数，已登记数
			String qfjs = JSONObject.toJSONString(qf);
			Map<String,String> qfmap = JSONObject.parseObject(qfjs, Map.class);
			Collection<String> qfztvalue = qfmap.values();
			for (String s : qfztvalue) {
				if(!"0".equals(s)) {
						dyydjs+=1;
						break;
				}
			}
			
			//房屋单元信息+具体的状态
			HouseInfo houseinfo = new HouseInfo();
			HouseInfoDY houseInfody = new HouseInfoDY();
			String jsonStr = JSONObject.toJSONString(m);
			houseinfo = JSONObject.parseObject(jsonStr, HouseInfo.class);
			houseinfo.setBDCDYLX(BDCDYLX.YCH.Value);
			houseinfo.setRealszc(houseinfo.getSZC());
			if(houseinfo.getBDCDYID().equals(hbdcdyid)){
				binfo.hszc = StringHelper.getInt(houseinfo.getSZC());
				List<Map> qxgls = getqxglqk_qf(hbdcdyid);
				if(qxgls != null && qxgls.size()>0){
					houseInfody.setSfqxgl("1");
					houseinfo.setSCFTJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCFTJZMJ")));
					houseinfo.setSCJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCJZMJ")));
					houseinfo.setSCTNJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCTNJZMJ")));
				}else{
					houseInfody.setSfqxgl("0");
				}
			}else{
				List<Map> qxgls = getqxglqk_qf(houseinfo.getBDCDYID());
				if(qxgls != null && qxgls.size()>0){
					houseInfody.setSfqxgl("1");
					houseinfo.setSCFTJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCFTJZMJ")));
					houseinfo.setSCJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCJZMJ")));
					houseinfo.setSCTNJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCTNJZMJ")));
				}else{
					houseInfody.setSfqxgl("0");
				}
			}
			houseinfo.Realszc=houseinfo.QSC;

			if(!StringHelper.isEmpty(houseinfo.ZZC) && !StringHelper.isEmpty(houseinfo.QSC)){
				if(!houseinfo.ZZC.equals(houseinfo.QSC)){
					int loftqsc = StringHelper.getInt(houseinfo.QSC);
					int loftzzc = StringHelper.getInt(houseinfo.ZZC);
					while (loftqsc<loftzzc) {
						++loftqsc;
						HouseInfo info = new HouseInfo();
						info = JSONObject.parseObject(jsonStr, HouseInfo.class);
						info.setBDCDYLX(BDCDYLX.YCH.Value);
						info.setRealszc(info.getSZC());

						info.Realszc=loftqsc+"";

						HouseInfoDY dy = new HouseInfoDY();
						dy.houseinfo=info;
						List<Map> qxgls = getqxglqk_qf(info.BDCDYID);
						if(qxgls != null && qxgls.size()>0){
							dy.setSfqxgl("1");
							info.setSCFTJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCFTJZMJ")));
							info.setSCJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCJZMJ")));
							info.setSCTNJZMJ(StringHelper.formatDouble(qxgls.get(0).get("SCTNJZMJ")));
						}else{
							dy.setSfqxgl("0");
						}
						dy.qfcqztdy = qf;
						dy.xfcqztdy = xf;
						buildingTalbes.add(dy);
					}
				}
			}

			houseInfody.setHouseinfo(houseinfo);
			houseInfody.setQfcqztdy(qf);
			houseInfody.setXfcqztdy(xf);
			buildingTalbes.add(houseInfody);
			if(dyydjs!=count){
				dyydjmj=dyydjmj.add(new BigDecimal(houseinfo.JZMJ));
				count =dyydjs;
			}else {
				dywdjmj=dywdjmj.add(new BigDecimal(houseinfo.JZMJ));
			}
			zjzmj=zjzmj.add(new BigDecimal(houseinfo.JZMJ));
			
		}
		dywdjs=lst.size()-dyydjs;
		//添加建筑总面积数
		binfo.build.put("zjzmj", zjzmj.toString()+"㎡");
		//添加未登记单元总面积数
		binfo.build.put("dywdjmj", dywdjmj.toString()+"㎡");
		//添加已登记单元总面积数
		binfo.build.put("dyydjmj", dyydjmj.toString()+"㎡");
		//添加单元未登记数
		binfo.build.put("dywdjs", dywdjs);
		//添加单元已登记数
		binfo.build.put("dyydjs", dyydjs);
		// 求总层数
		if (dxcs < 0)
		{
			zcs = -dxcs + dscs;
		} else {
			zcs = dscs;
		}
		//房屋用途统计后过滤
		Collection<Integer> col = ytmap.values();
		while (col.contains(0)) {
			col.remove(0);
		}
		// 自然幢基本信息
		binfo.setZCS(StringHelper.FormatByDatatype(zcs));
		binfo.setDXCS(StringHelper.FormatByDatatype(dxcs));
		binfo.setDSCS(StringHelper.FormatByDatatype(dscs));
		binfo.setYTandGS(ytmap);
		if(Group){
			binfo.dYHGroup=DYHGroup;
		}else{
			binfo.dYHGroup=null;
		}
		createbt.setBuildinfo(binfo);
		Collections.sort(buildingTalbes, new Comparator<BuildingTalbe.HouseInfoDY>() {
			@Override
			public int compare(BuildingTalbe.HouseInfoDY treei, BuildingTalbe.HouseInfoDY treej) {
				String fhi = "";
				String fhj = "";
				String dyhi = "";
				String dyhj = "";
				try {
					if (!StringHelper.isEmpty(treei.getHouseinfo().getFH())) {
						fhi = treei.getHouseinfo().getFH();
					}
					if (!StringHelper.isEmpty(treej.getHouseinfo().getFH())) {
						fhj = treej.getHouseinfo().getFH();
					}
					if (!StringHelper.isEmpty(treei.getHouseinfo().getFH())) {
						dyhi = treei.getHouseinfo().getDYH();
					}
					if (!StringHelper.isEmpty(treej.getHouseinfo().getFH())) {
						dyhj = treej.getHouseinfo().getDYH();
					}
				} catch (Exception e) {
				}
				if(StringHelper.isEmpty(dyhi)||!StringHelper.isEmpty(dyhj)&&ObjectHelper.cat(dyhi,dyhj)<0)
					return -1;
				if(StringHelper.isEmpty(dyhj)||!StringHelper.isEmpty(dyhi)&&ObjectHelper.cat(dyhj,dyhi)<0)
					return 1;
				if(StringHelper.isEmpty(fhi)||!StringHelper.isEmpty(fhj)&&ObjectHelper.cat(fhi,fhj)<0)
					return -1;
				if(StringHelper.isEmpty(fhj)||!StringHelper.isEmpty(fhi)&&ObjectHelper.cat(fhj,fhi)<0)
					return 1;
				return 0;
			}
		});
		createbt.setBuildingTalbes(buildingTalbes);
		return createbt;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<String, QFCQZTDY> GetQFQlInfoByZRZ(DJDYLY dyly, String zrzbdcdyid,BDCDYLX bdcdylx, String ljzid) {
		if(!StringHelper.isEmpty(ljzid)){
			zrzbdcdyid=ljzid;
		}
		String sql = " SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,NULL AS SFDB,'XZ' AS LY "
				+ " FROM BDCK.BDCS_H_XZY H "
				+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.BDCDYID = H.BDCDYID "
				+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'"
				+ " UNION ALL SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,XM.SFDB,'GZ' AS LY "
				+ " FROM BDCK.BDCS_H_XZY H "
				+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.BDCDYID = H.BDCDYID "
				+ " LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH = QL.XMBH "
				+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'";
		String xzsql = "SELECT H.BDCDYID FROM BDCK.BDCS_H_XZY H "
				+ " INNER JOIN BDCK.BDCS_DYXZ XZ ON XZ.BDCDYID=H.BDCDYID WHERE XZ.YXBZ='1' AND H.ZRZBDCDYID='"
				+ zrzbdcdyid + "'";
		if(DJDYLY.DC.equals(dyly)){
			sql = " SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,NULL AS SFDB,'XZ' AS LY "
					+ " FROM BDCK.BDCS_H_XZY H "
					+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.BDCDYID = H.BDCDYID "
					+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'"
					+ " UNION ALL SELECT H.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,XM.SFDB,'GZ' AS LY "
					+ " FROM BDCK.BDCS_H_GZY H "
					+ " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.BDCDYID = H.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH = QL.XMBH "
					+ " WHERE H.ZRZBDCDYID ='"+zrzbdcdyid+"'";
		}
		if(!StringHelper.isEmpty(ljzid)){
			sql = sql.replaceAll("H.ZRZBDCDYID", "H.LJZID");
			xzsql = xzsql.replaceAll("H.ZRZBDCDYID", "H.LJZID");
		}
		if(BDCDYLX.H.equals(bdcdylx)){
			if(DJDYLY.DC.equals(dyly)){
				sql = "SELECT SCH.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,NULL AS SFDB,'XZ' AS LY "
						+ " FROM BDCK.BDCS_H_XZY H "
						+ " INNER JOIN BDCK.YC_SC_H_XZ YS ON YS.YCBDCDYID=H.BDCDYID "
						+ " INNER JOIN BDCK.BDCS_H_GZ SCH ON YS.SCBDCDYID=SCH.BDCDYID "
						+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.BDCDYID = H.BDCDYID "
						+ " WHERE SCH.ZRZBDCDYID ='"+zrzbdcdyid+"'";
				xzsql = "SELECT SCH.BDCDYID FROM "
						+ " BDCK.BDCS_H_XZY H "
						+ " INNER JOIN BDCK.YC_SC_H_XZ YS ON YS.YCBDCDYID=H.BDCDYID "
						+ " INNER JOIN BDCK.BDCS_H_GZ SCH ON YS.SCBDCDYID=SCH.BDCDYID "
						+ " INNER JOIN BDCK.BDCS_DYXZ XZ ON XZ.BDCDYID=H.BDCDYID "
						+ " WHERE XZ.YXBZ='1' AND SCH.ZRZBDCDYID='"
						+ zrzbdcdyid + "'";
			}else{
				sql = " SELECT SCH.BDCDYID,QL.QLID,QL.DJLX,QL.QLLX,QL.MSR,QL.HTH,NULL AS SFDB,'XZ' AS LY "
						+ " FROM BDCK.BDCS_H_XZY H "
						+ " INNER JOIN BDCK.YC_SC_H_XZ YS ON YS.YCBDCDYID=H.BDCDYID "
						+ " INNER JOIN BDCK.BDCS_H_XZ SCH ON YS.SCBDCDYID=SCH.BDCDYID "
						+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.BDCDYID = H.BDCDYID "
						+ " WHERE SCH.ZRZBDCDYID ='"+zrzbdcdyid+"'";
				xzsql = "SELECT SCH.BDCDYID FROM "
						+ " BDCK.BDCS_H_XZY H "
						+ " INNER JOIN BDCK.YC_SC_H_XZ YS ON YS.YCBDCDYID=H.BDCDYID "
						+ " INNER JOIN BDCK.BDCS_H_XZ SCH ON YS.SCBDCDYID=SCH.BDCDYID "
						+ " INNER JOIN BDCK.BDCS_DYXZ XZ ON XZ.BDCDYID=H.BDCDYID "
						+ " WHERE XZ.YXBZ='1' AND SCH.ZRZBDCDYID='"
						+ zrzbdcdyid + "'";
			}
			if(!StringHelper.isEmpty(ljzid)){
				sql = sql.replaceAll("YCH.ZRZBDCDYID", "SCH.LJZID");
				xzsql = xzsql.replaceAll("YCH.ZRZBDCDYID", "SCH.LJZID");
			}
		}
		List<Map> lst = baseCommonDao.getDataListByFullSql(sql);	
		List<Map> xzlst = baseCommonDao.getDataListByFullSql(xzsql);
		List<String> xzlist = new ArrayList<String>();
		for (Map map : xzlst) {
			xzlist.add(StringHelper.formatObject(map.get("BDCDYID")));
		}
		Map<String, QFCQZTDY> GX = new HashMap<String, QFCQZTDY>();
		if (!StringHelper.isEmpty(lst) && lst.size() > 0) {
			for (Map m : lst) {
				String bdcdyid = StringHelper.formatObject(m.get("BDCDYID"));
				String ly = StringHelper.formatObject(m.get("LY"));
				
				QFCQZTDY qfzt = new QFCQZTDY();
				if(GX.containsKey(bdcdyid)){
					qfzt = GX.get(bdcdyid);
					qfzt = Updateqfzt(m,qfzt,ly);
					if (xzlist.contains(bdcdyid)) {
						qfzt.setQFXZDJ("1");
					}
				}else{
					qfzt = Updateqfzt(m,qfzt,ly);
					if(xzlist.contains(bdcdyid)){
						qfzt.setQFXZDJ("1");
					}
					GX.put(bdcdyid, qfzt);
				}
			}
		}
		return GX;
	}

	@SuppressWarnings("rawtypes")
	private QFCQZTDY Updateqfzt(Map m, QFCQZTDY qfzt, String ly) {
		String djlx = StringHelper.formatObject(m.get("DJLX"));
		String qllx = StringHelper.formatObject(m.get("QLLX"));
		String sfdb = StringHelper.formatObject(m.get("SFDB"));
		if ("XZ".equals(ly)) {
			// 期房抵押
			if (QLLX.DIYQ.Value.equals(qllx)) {
				qfzt.setQFDIYDJ("1");
			}
			// 期房预告登记抵押
			if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx)) {
				qfzt.setQFDIYYGDJ("1");
			}
			// 期房预告登记
			if (DJLX.YGDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
				qfzt.setQFYGDJ("1");
			}
			// 期房查封登记
			if (DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
				qfzt.setQFCFDJ("1");
			}
			// 判断权利中的合同号和买受人存在，存在即已预售
			if (("4".equals(qllx) || "6".equals(qllx) || "8".equals(qllx))) {
				if (!StringHelper.isEmpty(m.get("MSR")) && !StringHelper.isEmpty(m.get("HTH"))) {
					qfzt.setYQFYS("1");
				}
			}
			// 异议
			if (DJLX.YYDJ.Value.equals(djlx)) {
				qfzt.setQFYYDJ("1");
			}
		} else if ("GZ".equals(ly)) {
			if ("0".equals(sfdb)) {
				// 期房抵押
				if (QLLX.DIYQ.Value.equals(qllx)) {
					qfzt.setQFDIYDJ("2");
				}
				// 期房预告登记抵押
				if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx)) {
					qfzt.setQFDIYYGDJ("2");
				}
				// 期房预告登记
				if (DJLX.YGDJ.Value.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
					qfzt.setQFYGDJ("2");
				}
				// 期房查封登记
				if (DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)) {
					qfzt.setQFCFDJ("2");
				}
				// 判断权利中的合同号和买受人存在，存在即已预售
				if (("4".equals(qllx) || "6".equals(qllx) || "8".equals(qllx))) {
					if (!StringHelper.isEmpty(m.get("MSR")) && !StringHelper.isEmpty(m.get("HTH"))) {
						qfzt.setYQFYS("2");
					}
				}
				// 异议
				if (DJLX.YYDJ.Value.equals(djlx)) {
					qfzt.setQFYYDJ("2");
				}
			}
		}
		return qfzt;
	}
	
	/*********************END*************************/
	
	private UnitStatus getStatus(String djdyid, String bdcdyid, String bdcdylx) {
		UnitStatus status = new UnitStatus();
		// 在办状态
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.YWLSH AS YWLSH,XMXX.DJLX AS XMDJLX,XMXX.QLLX AS XMQLLX, ");
		builder.append("QL.DJLX AS QLDJLX,QL.QLLX AS QLQLLX ");
		builder.append("FROM BDCK.BDCS_QL_GZ QL ");
		builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ");
		builder.append("ON QL.XMBH=XMXX.XMBH ");
		builder.append("WHERE XMXX.DJLX<>'400' AND NVL2(XMXX.SFDB,'1','0')<>'1' ");
		builder.append("AND QL.DJDYID='" + djdyid + "' ");
		List<Map> qls_gz = baseCommonDao.getDataListByFullSql(builder.toString());
		for (Map ql : qls_gz) {
			String xmdjlx = StringHelper.formatDouble(ql.get("XMDJLX"));
			String xmqllx = StringHelper.formatDouble(ql.get("XMQLLX"));
			if (DJLX.CFDJ.Value.equals(xmdjlx)) {
				if ("98".equals(xmqllx)) {
					status.setSeizureState("正在办理查封");
				}
			}
			if (DJLX.YYDJ.Value.equals(xmdjlx)) {
				status.setObjectionState("正在办理异议");
			} else if (DJLX.YGDJ.Value.equals(xmdjlx)) {
				if (QLLX.QTQL.Value.equals(xmqllx)) {
					status.setTransferNoticeState("正在办理转移预告");
				} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("正在办理抵押");
					} else {
						status.setMortgageNoticeState("正在办理抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
				status.setMortgageState("正在办理抵押");
			}
		}
		// 已办状态
		List<Rights> qls_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdyid + "'");
		for (Rights ql : qls_xz) {
			String djlx = ql.getDJLX();
			String qllx = ql.getQLLX();
			if (DJLX.CFDJ.Value.equals(djlx)) {
				status.setSeizureState("已查封");
			}
			if (DJLX.YYDJ.Value.equals(djlx)) {
				status.setObjectionState("已异议");
			} else if (DJLX.YGDJ.Value.equals(djlx)) {
				if (QLLX.QTQL.Value.equals(qllx)) {
					status.setTransferNoticeState("已转移预告");
				} else if (QLLX.DIYQ.Value.equals(qllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("已抵押");
					} else {
						status.setMortgageNoticeState("已抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(qllx)) {
				status.setMortgageState("已抵押");
			}
		}

		List<BDCS_DYXZ> list_limit = baseCommonDao.getDataList(BDCS_DYXZ.class,
				"YXBZ<>'2' AND BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "' ORDER BY YXBZ ");
		if (list_limit != null && list_limit.size() > 0) {
			for (BDCS_DYXZ limit : list_limit) {
				if ("1".equals(limit.getYXBZ())) {
					status.setLimitState("已限制");
				} else {
					status.setLimitState("正在办理限制");
				}
			}
		}
		return status;
	}

	private String getqlrmc(String bdcdyid) {
		String qlrmc = "";
		List<BDCS_DJDY_XZ> dys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID='"+bdcdyid+"'");
		if(dys!=null&&dys.size()>0){
			BDCS_DJDY_XZ dy = null;
			dy=dys.get(0);
			String condition=" QLLX IN ('4','6','8') AND DJDYID='"+dy.getDJDYID()+"'";
			List<Rights> rights = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
			if(rights!=null&&!rights.isEmpty()){
				List<RightsHolder> Holders = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, rights.get(0).getId());
				if(Holders!=null&&Holders.size()>0){
					for (RightsHolder rightsHolder : Holders) {
						qlrmc+=rightsHolder.getQLRMC()+",";
					}
				}
			}
			if(!StringHelper.isEmpty(qlrmc)){
				qlrmc=qlrmc.substring(0, qlrmc.length()-1);
			}
			return qlrmc;
		}
		return "";
	}
	
	public BuildingTalbe queryBuildingTableByBuildingCond_new(
			String zrzbdcdyid, String bdcdylx, String ly, String szc, String hbdcdyid) {
		BuildingTalbe createbt = new BuildingTalbe();
		double starttime = System.currentTimeMillis();
		BDCDYLX lx = BDCDYLX.initFrom(bdcdylx);
		DJDYLY djdyly = DJDYLY.initFrom(ly);
		String ljzid = null;;
		if (BDCDYLX.H.equals(lx)) {
			RealUnit unit = UnitTools.loadUnit(lx, djdyly, zrzbdcdyid);
			House h = (House) unit;
			if(h!=null){
				ljzid = h.getLJZID();
			}
			createbt = getXFBuildingInfo_new(zrzbdcdyid, djdyly,szc,hbdcdyid, ljzid);
		} else if (BDCDYLX.YCH.equals(lx)) {
			RealUnit unit = UnitTools.loadUnit(lx, djdyly, zrzbdcdyid);
			House h = (House) unit;
			if(h!=null){
				ljzid = h.getLJZID();
			}
			createbt = getQFBuildingInfo_new(zrzbdcdyid, djdyly,szc,hbdcdyid, ljzid);
		}else if(BDCDYLX.ZRZ.equals(lx)){
			RealUnit unit = UnitTools.loadUnit(BDCDYLX.H, djdyly, hbdcdyid);
			House h = (House) unit;
			if(h!=null){
				ljzid = h.getLJZID();
			}
			createbt = getXFBuildingInfo_new(zrzbdcdyid, djdyly,szc,hbdcdyid, ljzid);
		}else if(BDCDYLX.YCZRZ.equals(lx)){
			RealUnit unit = UnitTools.loadUnit(BDCDYLX.YCH, djdyly, hbdcdyid);
			House h = (House) unit;
			if(h!=null){
				ljzid = h.getLJZID();
			}
			createbt = getQFBuildingInfo_new(zrzbdcdyid, djdyly,szc,hbdcdyid, ljzid);
		}
		System.out.println(System.currentTimeMillis() - starttime);
		return createbt;
	}

	public BuildingTalbe queryBuildingTableByHouseCond_new(String bdcdyid,
			String bdcdylx, String ly, String szc, String hbdcdyid, boolean ljz) {
		double starttime = System.currentTimeMillis();
		BuildingTalbe createbt = new BuildingTalbe();
		BDCDYLX lx = BDCDYLX.initFrom(bdcdylx);
		DJDYLY djdyly = DJDYLY.initFrom(ly);
		RealUnit unit = UnitTools.loadUnit(lx, djdyly, bdcdyid);
		if (!StringHelper.isEmpty(unit)) {
			House h = (House) unit;
			String zrzbdcdyid = h.getZRZBDCDYID();
			String ljzid = h.getLJZID();
			if(!ljz){
				ljzid=null;
			}
			if (BDCDYLX.H.equals(lx)) {
				createbt = getXFBuildingInfo_new(zrzbdcdyid, djdyly,szc,hbdcdyid, ljzid);
			} else if (BDCDYLX.YCH.equals(lx)) {
				createbt = getQFBuildingInfo_new(zrzbdcdyid, djdyly,szc,hbdcdyid, ljzid);
			}
		}
		System.out.println(System.currentTimeMillis() - starttime);
		return createbt;
	}
	
	public static class node{
		public Object groupname;
		public Object groupcolor;
		public List<Object> flag;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map> getqxglqk_xf(String bdcdyid) {
		String glsql = "SELECT YCH.YCFTJZMJ,YCH.YCJZMJ,YCH.YCTNJZMJ FROM BDCK.BDCS_H_XZY YCH LEFT JOIN BDCK.YC_SC_H_XZ GL ON GL.YCBDCDYID=YCH.BDCDYID WHERE GL.SCBDCDYID='" + bdcdyid + "'";
		List<Map> ychs = baseCommonDao.getDataListByFullSql(glsql);
		return ychs;
	}
	@SuppressWarnings({ "rawtypes", "unused" })
	private List<Map> getqxglqk_qf(String bdcdyid) {
		String glsql = "SELECT H.SCFTJZMJ,H.SCJZMJ,H.SCTNJZMJ FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.YC_SC_H_XZ GL ON GL.SCBDCDYID=H.BDCDYID WHERE GL.YCBDCDYID='" + bdcdyid + "'";
		List<Map> schs = baseCommonDao.getDataListByFullSql(glsql);
		return schs;
	}
	
}

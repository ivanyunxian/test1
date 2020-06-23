package com.supermap.realestate.registration.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.gis.common.Point2D;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.LogicBuilding;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.OperateFeature;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * @author zhaomengfan
 *
 */
@Controller
@RequestMapping("/share_wlmq")
public class Share_wlmqController {

	@Autowired
	private CommonDao CommonDao;

	/**
	 * @param xmbh
	 * @param bdcdylx
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/unitinfo/{xmbh}/{bdcdylx}", method = RequestMethod.GET)
	public @ResponseBody ShareUnitInfo GetUnitInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response) {
		ShareUnitInfo T = new ShareUnitInfo();

		BDCDYLX bdcdylxV = BDCDYLX.initFrom(bdcdylx);

		if (BDCDYLX.H.equals(bdcdylxV) || BDCDYLX.YCH.equals(bdcdylxV)) {
			T = HouseInfo(bdcdylx, xmbh);
		} else if (BDCDYLX.ZRZ.equals(bdcdylxV) || BDCDYLX.YCZRZ.equals(bdcdylxV)) {
			T = BuildingInfo(bdcdylx, xmbh);
		} else if (BDCDYLX.SHYQZD.equals(bdcdylxV) || BDCDYLX.SYQZD.equals(bdcdylxV)) {
			T = LandInfo(bdcdylx, xmbh);
		}

		return T;
	}

	/**
	 * @param bdcdylx
	 * @param xmbh
	 * @return
	 */
	private ShareUnitInfo HouseInfo(String bdcdylx, String xmbh) {

		String hqlCondition = " XMBH=:XMBH AND BDCDYLX=:BDCDYLX ";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("XMBH", xmbh);
		paramMap.put("BDCDYLX", bdcdylx);

		List<BDCS_DJDY_GZ> dys = CommonDao.getDataList(BDCS_DJDY_GZ.class, hqlCondition, paramMap);

		List<UNITINFOS> UNITINFOS = new ArrayList<UNITINFOS>();

		if (dys != null && !dys.isEmpty()) {
			for (BDCS_DJDY_GZ dy : dys) {
				String bdcdyid = dy.getBDCDYID();
				String djdyid=dy.getDJDYID();
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
				HOUSE house = getHOUSE(unit);
				BUILDING building = getBUILDING(unit);
				LOGICBUILDING logicbuilding = getLOGICBUILDING(unit);
				LAND land = getLAND(unit);
				RIGHTS rights = getRIGHTS(bdcdylx, xmbh, djdyid);
				SUBRIGHTS subrights = getSUBRIGHTS(bdcdylx, xmbh, djdyid);
				List<RIGHTHOLDERS> rightholders = getRIGHTHOLDERS(bdcdylx, xmbh, djdyid);
				UNITINFOS.add(new UNITINFOS(house, building, logicbuilding, land, rights, subrights, rightholders));
			}
		}
		ShareUnitInfo info = new ShareUnitInfo(xmbh, bdcdylx, UNITINFOS);
		return info;
	}

	/**
	 * @param bdcdylx
	 * @param xmbh
	 * @return
	 */
	private ShareUnitInfo BuildingInfo(String bdcdylx, String xmbh) {

		String hqlCondition = " XMBH=:XMBH AND BDCDYLX=:BDCDYLX ";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("XMBH", xmbh);
		paramMap.put("BDCDYLX", bdcdylx);

		List<BDCS_DJDY_GZ> dys = CommonDao.getDataList(BDCS_DJDY_GZ.class, hqlCondition, paramMap);

		List<UNITINFOS> UNITINFOS = new ArrayList<UNITINFOS>();

		if (dys != null && !dys.isEmpty()) {
			for (BDCS_DJDY_GZ dy : dys) {
				String bdcdyid = dy.getBDCDYID();
				String djdyid=dy.getDJDYID();
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
				BUILDING building = getBUILDING(unit);
				LOGICBUILDING logicbuilding = getLOGICBUILDING(unit);
				LAND land = getLAND(unit);
				RIGHTS rights = getRIGHTS(bdcdylx, xmbh, djdyid);
				SUBRIGHTS subrights = getSUBRIGHTS(bdcdylx, xmbh, djdyid);
				List<RIGHTHOLDERS> rightholders = getRIGHTHOLDERS(bdcdylx, xmbh, djdyid);
				UNITINFOS.add(new UNITINFOS(building, logicbuilding, land, rights, subrights, rightholders));
			}
		}

		ShareUnitInfo info = new ShareUnitInfo(xmbh, bdcdylx, UNITINFOS);

		return info;
	}

	/**
	 * @param bdcdylx
	 * @param xmbh
	 * @return
	 */
	private ShareUnitInfo LandInfo(String bdcdylx, String xmbh) {

		String hqlCondition = " XMBH=:XMBH AND BDCDYLX=:BDCDYLX ";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("XMBH", xmbh);
		paramMap.put("BDCDYLX", bdcdylx);

		List<BDCS_DJDY_GZ> dys = CommonDao.getDataList(BDCS_DJDY_GZ.class, hqlCondition, paramMap);

		List<UNITINFOS> UNITINFOS = new ArrayList<UNITINFOS>();

		if (dys != null && !dys.isEmpty()) {
			for (BDCS_DJDY_GZ dy : dys) {
				String bdcdyid = dy.getBDCDYID();
				String djdyid=dy.getDJDYID();
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
				LAND land = getLAND(unit);
				RIGHTS rights = getRIGHTS(bdcdylx, xmbh, djdyid);
				SUBRIGHTS subrights = getSUBRIGHTS(bdcdylx, xmbh, djdyid);
				List<RIGHTHOLDERS> rightholders = getRIGHTHOLDERS(bdcdylx, xmbh, djdyid);
				UNITINFOS.add(new UNITINFOS(land, rights, subrights, rightholders));
			}
		}

		ShareUnitInfo info = new ShareUnitInfo(xmbh, bdcdylx, UNITINFOS);

		return info;
	}

	private HOUSE getHOUSE(RealUnit unit) {
		House house = (House)unit;
		Map<String, Object> map = StringHelper.beanToMap(house);
		map.put("BDCDYID", unit.getId());
		HOUSE h = JSONObject.parseObject(JSONObject.toJSONString(map), HOUSE.class);
		return h;
	}

	private BUILDING getBUILDING(RealUnit unit) {
		BUILDING zrz = new BUILDING();
		if(unit instanceof House){
			if(BDCDYLX.H.equals(unit.getBDCDYLX())){
				unit = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, ((House)unit).getZRZBDCDYID());
			}else if(BDCDYLX.YCH.equals(unit.getBDCDYLX())){
				unit = UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.LS, ((House)unit).getZRZBDCDYID());
			}
		}
		if(unit!=null){
			Building building = (Building)unit;
			Map<String, Object> map = StringHelper.beanToMap(building);
			map.put("BDCDYID", unit.getId());
			zrz.attr = JSONObject.parseObject(JSONObject.toJSONString(map), BUILDING.ATTR.class);
			zrz.geo = new BUILDING.GEO();
			zrz.geo.parts=getFeatureInfo(unit.getBDCDYLX(), unit.getId());
		}
		return zrz;
	}

	private LOGICBUILDING getLOGICBUILDING(RealUnit unit) {
		LogicBuilding logicbuilding = null;
		LOGICBUILDING logic = new LOGICBUILDING();
		if(unit instanceof House){
			logicbuilding = UnitTools.loadLogicBuilding(BDCDYLX.SHYQZD, DJDYLY.LS, ((House)unit).getLJZID());
		}
		if(logicbuilding!=null){
			Map<String, Object> map = StringHelper.beanToMap(logicbuilding);
			map.put("BDCDYID", logicbuilding.getId());
			logic.attr =JSONObject.parseObject(JSONObject.toJSONString(map), LOGICBUILDING.ATTR.class);
		}
		return logic;
	}

	private LAND getLAND(RealUnit unit) {
		LAND land = new LAND();
		if(unit instanceof LandAttach){
			unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, ((LandAttach)unit).getZDBDCDYID());
		}
		if(unit!=null){
			UseLand useland = (UseLand) unit;
			Map<String, Object> map = StringHelper.beanToMap(useland);
			map.put("BDCDYID", useland.getId());
			land.attr = JSONObject.parseObject(JSONObject.toJSONString(map), LAND.ATTR.class);
			land.geo = new LAND.GEO();
			land.geo.parts=getFeatureInfo(unit.getBDCDYLX(), unit.getId());
		}
		return land;
	}

	private RIGHTS getRIGHTS(String bdcdylx, String xmbh, String djdyid) {
		Rights rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdyid);
		RIGHTS right = new RIGHTS();
		if(rights!=null){
			Map<String, Object> map = StringHelper.beanToMap(rights);
			map.put("QLID", rights.getId());
			right = JSONObject.parseObject(JSONObject.toJSONString(map), RIGHTS.class);
		}
		return right;
	}

	private SUBRIGHTS getSUBRIGHTS(String bdcdylx, String xmbh, String djdyid) {
		List<SubRights> subrights = RightsTools.loadSubRightsByCondition(DJDYLY.GZ, " XMBH='"+xmbh+"' and "+" DJDYID='"+djdyid+"'");
		SUBRIGHTS subright = new SUBRIGHTS();
		if(subrights!=null&&!subrights.isEmpty()){
			Map<String, Object> map = StringHelper.beanToMap(subrights.get(0));
			map.put("FSQLID", subrights.get(0).getId());
			subright = JSONObject.parseObject(JSONObject.toJSONString(map), SUBRIGHTS.class);
		}
		return subright;
	}

	private List<RIGHTHOLDERS> getRIGHTHOLDERS(String bdcdylx, String xmbh, String djdyid) {
		List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, djdyid, xmbh);
		List<RIGHTHOLDERS> rightholder = new ArrayList<RIGHTHOLDERS>();
		for (RightsHolder rightsHolder : holders) {
			Map<String, Object> map = StringHelper.beanToMap(rightsHolder);
			map.put("QLRID", rightsHolder.getId());
			rightholder.add(JSONObject.parseObject(JSONArray.toJSONString(map), RIGHTHOLDERS.class));
		}
		return rightholder;
	}

	private List<POINT2D> getFeatureInfo(BDCDYLX bdcdylx, String bdcdyid) {
		String url_iserver_data = ConfigHelper.getNameByValue("URL_ISERVER_DATA");
		List<POINT2D> Ponits = new ArrayList<POINT2D>();
		String filterWhere = "BDCDYID='" + bdcdyid + "' ";
		OperateFeature operateFeature = new OperateFeature(url_iserver_data);
		if ("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
			com.supermap.realestate.gis.common.Feature[] features = operateFeature.queryFeatures_GIS("BDCK",
					bdcdylx.LSTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if (features == null || features.length < 0) {
				features = operateFeature.queryFeatures_GIS("BDCK", bdcdylx.LSTableName.replaceAll("BDCS_", "BDCK_"),
						filterWhere);
			}
			if (features != null && features.length > 0) {
				com.supermap.realestate.gis.common.Feature feature = features[0];
				com.supermap.realestate.gis.common.Geometry geometry = feature.geometry;
				for (int i = 0; i < geometry.points.length; i++) {
					POINT2D p = new POINT2D(geometry.points[i]);
					Ponits.add(p);
				}
			}
		} else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
			List<Feature> list_feature = operateFeature.queryFeatures_iServer("BDCK",
					bdcdylx.LSTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if (list_feature == null || list_feature.size() <= 0) {
				list_feature = operateFeature.queryFeatures_iServer("BDCK",
						bdcdylx.LSTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			}
			if (list_feature != null && list_feature.size() > 0) {
				Feature feature = list_feature.get(0);
				Geometry ObjGeometry = feature.geometry;
				for (int ni = 0; ni < ObjGeometry.points.length; ni++) {
					POINT2D p = new POINT2D(ObjGeometry.points[ni]);
					Ponits.add(p);
				}
			}
		}
		return Ponits;
	}

	public static class ShareUnitInfo {
		private String XMBH;
		private String BDCDYLX;
		List<UNITINFOS> UNITINFOS;

		public ShareUnitInfo() {

		}

		public ShareUnitInfo(String XMBH, String BDCDYLX, List<UNITINFOS> UNITINFOS) {
			this.XMBH = XMBH;
			this.BDCDYLX = BDCDYLX;
			this.UNITINFOS = UNITINFOS;
		}

		public String getXMBH() {
			return XMBH;
		}

		public void setXMBH(String xMBH) {
			XMBH = xMBH;
		}

		public String getBDCDYLX() {
			return BDCDYLX;
		}

		public void setBDCDYLX(String bDCDYLX) {
			BDCDYLX = bDCDYLX;
		}

		public List<UNITINFOS> getUNITINFOS() {
			return UNITINFOS;
		}

		public void setUNITINFOS(List<UNITINFOS> uNITINFOS) {
			UNITINFOS = uNITINFOS;
		}
	}

	public static class UNITINFOS {
		private HOUSE HOUSE;
		private BUILDING BUILDING;
		private LOGICBUILDING LOGICBUILDING;
		private LAND LAND;
		private RIGHTS RIGHTS;
		private SUBRIGHTS SUBRIGHTS;
		private List<RIGHTHOLDERS>  RIGHTHOLDERS;

		public UNITINFOS() {

		}

		public UNITINFOS(HOUSE HOUSE, BUILDING BUILDING, LOGICBUILDING LOGICBUILDING, LAND LAND, RIGHTS RIGHTS,
				SUBRIGHTS SUBRIGHTS, List<RIGHTHOLDERS> RIGHTHOLDERS) {
			this.HOUSE = HOUSE;
			this.BUILDING = BUILDING;
			this.LOGICBUILDING = LOGICBUILDING;
			this.LAND = LAND;
			this.RIGHTS = RIGHTS;
			this.SUBRIGHTS = SUBRIGHTS;
			this.RIGHTHOLDERS = RIGHTHOLDERS;
		}

		public UNITINFOS(BUILDING BUILDING, LOGICBUILDING LOGICBUILDING, LAND LAND, RIGHTS RIGHTS, SUBRIGHTS SUBRIGHTS,
				List<RIGHTHOLDERS> RIGHTHOLDERS) {
			this.BUILDING = BUILDING;
			this.LOGICBUILDING = LOGICBUILDING;
			this.LAND = LAND;
			this.RIGHTS = RIGHTS;
			this.SUBRIGHTS = SUBRIGHTS;
			this.RIGHTHOLDERS = RIGHTHOLDERS;
		}

		public UNITINFOS(LAND LAND, RIGHTS RIGHTS, SUBRIGHTS SUBRIGHTS, List<RIGHTHOLDERS> RIGHTHOLDERS) {
			this.LAND = LAND;
			this.RIGHTS = RIGHTS;
			this.SUBRIGHTS = SUBRIGHTS;
			this.RIGHTHOLDERS = RIGHTHOLDERS;
		}

		public HOUSE getHOUSE() {
			return HOUSE;
		}

		public void setHOUSE(HOUSE hOUSE) {
			HOUSE = hOUSE;
		}

		public BUILDING getBUILDING() {
			return BUILDING;
		}

		public void setBUILDING(BUILDING bUILDING) {
			BUILDING = bUILDING;
		}

		public LOGICBUILDING getLOGICBUILDING() {
			return LOGICBUILDING;
		}

		public void setLOGICBUILDING(LOGICBUILDING lOGICBUILDING) {
			LOGICBUILDING = lOGICBUILDING;
		}

		public LAND getLAND() {
			return LAND;
		}

		public void setLAND(LAND lAND) {
			LAND = lAND;
		}

		public RIGHTS getRIGHTS() {
			return RIGHTS;
		}

		public void setRIGHTS(RIGHTS rIGHTS) {
			RIGHTS = rIGHTS;
		}

		public SUBRIGHTS getSUBRIGHTS() {
			return SUBRIGHTS;
		}

		public void setSUBRIGHTS(SUBRIGHTS sUBRIGHTS) {
			SUBRIGHTS = sUBRIGHTS;
		}

		public List<com.supermap.realestate.registration.web.Share_wlmqController.RIGHTHOLDERS> getRIGHTHOLDERS() {
			return RIGHTHOLDERS;
		}

		public void setRIGHTHOLDERS(List<RIGHTHOLDERS> rIGHTHOLDERS) {
			RIGHTHOLDERS = rIGHTHOLDERS;
		}
	}

	public static class HOUSE {
		private String BDCDYID;
		private String BDCDYH;
		private String ZRZBDCDYID;
		private String ZDDM;
		private String ZDBDCDYID;
		private String ZL;
		private String ZCS;

		public String getBDCDYID() {
			return BDCDYID;
		}

		public void setBDCDYID(String bDCDYID) {
			BDCDYID = bDCDYID;
		}

		public String getBDCDYH() {
			return BDCDYH;
		}

		public void setBDCDYH(String bDCDYH) {
			BDCDYH = bDCDYH;
		}

		public String getZRZBDCDYID() {
			return ZRZBDCDYID;
		}

		public void setZRZBDCDYID(String zRZBDCDYID) {
			ZRZBDCDYID = zRZBDCDYID;
		}

		public String getZDDM() {
			return ZDDM;
		}

		public void setZDDM(String zDDM) {
			ZDDM = zDDM;
		}

		public String getZDBDCDYID() {
			return ZDBDCDYID;
		}

		public void setZDBDCDYID(String zDBDCDYID) {
			ZDBDCDYID = zDBDCDYID;
		}

		public String getZL() {
			return ZL;
		}

		public void setZL(String zL) {
			ZL = zL;
		}

		public String getZCS() {
			return ZCS;
		}

		public void setZCS(String zCS) {
			ZCS = zCS;
		}

	}

	public static class BUILDING {
		private ATTR attr;
		private GEO geo;

		public ATTR getAttr() {
			return attr;
		}

		public void setAttr(ATTR attr) {
			this.attr = attr;
		}

		public GEO getGeo() {
			return geo;
		}

		public void setGeo(GEO geo) {
			this.geo = geo;
		}

		public static class ATTR {
			private String BDCDYID;
			private String BDCDYH;
			private String ZDDM;
			private String ZDBDCDYID;
			private String ZL;
			private String ZCS;
			private String DSCS;
			private String DXCS;
			private String GHYT;

			public String getBDCDYID() {
				return BDCDYID;
			}

			public void setBDCDYID(String bDCDYID) {
				BDCDYID = bDCDYID;
			}

			public String getBDCDYH() {
				return BDCDYH;
			}

			public void setBDCDYH(String bDCDYH) {
				BDCDYH = bDCDYH;
			}

			public String getZDDM() {
				return ZDDM;
			}

			public void setZDDM(String zDDM) {
				ZDDM = zDDM;
			}

			public String getZDBDCDYID() {
				return ZDBDCDYID;
			}

			public void setZDBDCDYID(String zDBDCDYID) {
				ZDBDCDYID = zDBDCDYID;
			}

			public String getZL() {
				return ZL;
			}

			public void setZL(String zL) {
				ZL = zL;
			}

			public String getZCS() {
				return ZCS;
			}

			public void setZCS(String zCS) {
				ZCS = zCS;
			}

			public String getDSCS() {
				return DSCS;
			}

			public void setDSCS(String dSCS) {
				DSCS = dSCS;
			}

			public String getDXCS() {
				return DXCS;
			}

			public void setDXCS(String dXCS) {
				DXCS = dXCS;
			}

			public String getGHYT() {
				return GHYT;
			}

			public void setGHYT(String gHYT) {
				GHYT = gHYT;
			}
		}

		public static class GEO {
			private List<POINT2D> parts;

			public List<POINT2D> getParts() {
				return parts;
			}

			public void setParts(List<POINT2D> parts) {
				this.parts = parts;
			}
		}
	}

	public static class LOGICBUILDING {
		private ATTR attr;
		
		public ATTR getAttr() {
			return attr;
		}

		public void setAttr(ATTR attr) {
			this.attr = attr;
		}

		public static class ATTR{
			private String LJZID;
			private String ZRZBDCDYID;
			private String ZRZH;
			private String LJZH;
			private String ZCS;
			private String DSCS;
			private String DXCS;
			private String GHYT;

			public String getLJZID() {
				return LJZID;
			}

			public void setLJZID(String lJZID) {
				LJZID = lJZID;
			}

			public String getZRZBDCDYID() {
				return ZRZBDCDYID;
			}

			public void setZRZBDCDYID(String zRZBDCDYID) {
				ZRZBDCDYID = zRZBDCDYID;
			}

			public String getZRZH() {
				return ZRZH;
			}

			public void setZRZH(String zRZH) {
				ZRZH = zRZH;
			}

			public String getLJZH() {
				return LJZH;
			}

			public void setLJZH(String lJZH) {
				LJZH = lJZH;
			}

			public String getZCS() {
				return ZCS;
			}

			public void setZCS(String zCS) {
				ZCS = zCS;
			}

			public String getDSCS() {
				return DSCS;
			}

			public void setDSCS(String dSCS) {
				DSCS = dSCS;
			}

			public String getDXCS() {
				return DXCS;
			}

			public void setDXCS(String dXCS) {
				DXCS = dXCS;
			}

			public String getGHYT() {
				return GHYT;
			}

			public void setGHYT(String gHYT) {
				GHYT = gHYT;
			}
		}
	}

	public static class LAND {
		private ATTR attr;
		private GEO geo;

		public ATTR getAttr() {
			return attr;
		}

		public void setAttr(ATTR attr) {
			this.attr = attr;
		}

		public GEO getGeo() {
			return geo;
		}

		public void setGeo(GEO geo) {
			this.geo = geo;
		}

		public static class ATTR {
			private String BDCDYID;
			private String BDCDYH;
			private String ZDDM;
			private String ZDTZM;
			private String ZL;
			private String ZDMJ;

			public String getBDCDYID() {
				return BDCDYID;
			}

			public void setBDCDYID(String bDCDYID) {
				BDCDYID = bDCDYID;
			}

			public String getBDCDYH() {
				return BDCDYH;
			}

			public void setBDCDYH(String bDCDYH) {
				BDCDYH = bDCDYH;
			}

			public String getZDDM() {
				return ZDDM;
			}

			public void setZDDM(String zDDM) {
				ZDDM = zDDM;
			}

			public String getZDTZM() {
				return ZDTZM;
			}

			public void setZDTZM(String zDTZM) {
				ZDTZM = zDTZM;
			}

			public String getZL() {
				return ZL;
			}

			public void setZL(String zL) {
				ZL = zL;
			}

			public String getZDMJ() {
				return ZDMJ;
			}

			public void setZDMJ(String zDMJ) {
				ZDMJ = zDMJ;
			}
		}

		public static class GEO {
			private List<POINT2D> parts;

			public List<POINT2D> getParts() {
				return parts;
			}

			public void setParts(List<POINT2D> parts) {
				this.parts = parts;
			}
		}
	}

	public static class RIGHTS {
		private String QLID;
		private String BDCDYID;
		private String FSQLID;
		private String QLLX;
		private String DJLX;
		private String FJ;

		public String getQLID() {
			return QLID;
		}

		public void setQLID(String qLID) {
			QLID = qLID;
		}

		public String getBDCDYID() {
			return BDCDYID;
		}

		public void setBDCDYID(String bDCDYID) {
			BDCDYID = bDCDYID;
		}

		public String getFSQLID() {
			return FSQLID;
		}

		public void setFSQLID(String fSQLID) {
			FSQLID = fSQLID;
		}

		public String getQLLX() {
			return QLLX;
		}

		public void setQLLX(String qLLX) {
			QLLX = qLLX;
		}

		public String getDJLX() {
			return DJLX;
		}

		public void setDJLX(String dJLX) {
			DJLX = dJLX;
		}

		public String getFJ() {
			return FJ;
		}

		public void setFJ(String fJ) {
			FJ = fJ;
		}
	}

	public static class SUBRIGHTS {
		private String FSQLID;
		private String QLID;
		private String BDCDYH;
		private String SYLX;
		private String ZXDBR;
		private String DYR;

		public String getFSQLID() {
			return FSQLID;
		}

		public void setFSQLID(String fSQLID) {
			FSQLID = fSQLID;
		}

		public String getQLID() {
			return QLID;
		}

		public void setQLID(String qLID) {
			QLID = qLID;
		}

		public String getBDCDYH() {
			return BDCDYH;
		}

		public void setBDCDYH(String bDCDYH) {
			BDCDYH = bDCDYH;
		}

		public String getSYLX() {
			return SYLX;
		}

		public void setSYLX(String sYLX) {
			SYLX = sYLX;
		}

		public String getZXDBR() {
			return ZXDBR;
		}

		public void setZXDBR(String zXDBR) {
			ZXDBR = zXDBR;
		}

		public String getDYR() {
			return DYR;
		}

		public void setDYR(String dYR) {
			DYR = dYR;
		}
	}

	public static class RIGHTHOLDERS {
		private String QLRID;
		private String QLID;
		private String QLRMC;
		private String ZJZL;
		private String ZJH;
		private String XB;

		public String getQLRID() {
			return QLRID;
		}

		public void setQLRID(String qLRID) {
			QLRID = qLRID;
		}

		public String getQLID() {
			return QLID;
		}

		public void setQLID(String qLID) {
			QLID = qLID;
		}

		public String getQLRMC() {
			return QLRMC;
		}

		public void setQLRMC(String qLRMC) {
			QLRMC = qLRMC;
		}

		public String getZJZL() {
			return ZJZL;
		}

		public void setZJZL(String zJZL) {
			ZJZL = zJZL;
		}

		public String getZJH() {
			return ZJH;
		}

		public void setZJH(String zJH) {
			ZJH = zJH;
		}

		public String getXB() {
			return XB;
		}

		public void setXB(String xB) {
			XB = xB;
		}
	}

	public static class POINT2D {
		private String x;
		private String y;

		public POINT2D() {

		}

		public POINT2D(Point2D point2D) {
			if (point2D == null) {
				throw new IllegalArgumentException("参数为空");
			}
			this.x = StringHelper.cut(point2D.x, 4).toString();
			this.y = StringHelper.cut(point2D.y, 4).toString();
		}

		public POINT2D(com.supermap.services.components.commontypes.Point2D point2D) {
			if (point2D == null) {
				throw new IllegalArgumentException("参数为空");
			}
			this.x = StringHelper.cut(point2D.x, 4).toString();
			this.y = StringHelper.cut(point2D.y, 4).toString();
		}

		public String getX() {
			return x;
		}

		public void setX(String x) {
			this.x = x;
		}

		public String getY() {
			return y;
		}

		public void setY(String y) {
			this.y = y;
		}
	}

}

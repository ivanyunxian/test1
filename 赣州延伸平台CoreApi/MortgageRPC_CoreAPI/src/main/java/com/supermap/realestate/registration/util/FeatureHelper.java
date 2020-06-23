package com.supermap.realestate.registration.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.supermap.realestate.registration.tools.OperateFeature;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.FeatureHelper.POINT2D;

/**
 * 
 * @author zhaomengfan
 * @date 2017-07-25 16:28:25
 *
 */
public class FeatureHelper {
	
	/**
	 * @param bdcdylx 自然幢、宗地
	 * @param djdyly 
	 * @param bdcdyid
	 * @return
	 */
	public static List<List<POINT2D>> getFeatureInfo(BDCDYLX bdcdylx, DJDYLY djdyly, String bdcdyid) {
		if(!"1".equals(ConfigHelper.getNameByValue("GeoOperate"))){
			return null;
		}
		String url_iserver_data = ConfigHelper.getNameByValue("URL_ISERVER_DATA");
		List<List<POINT2D>> Ponits = new ArrayList<List<POINT2D>>();
		try {
			String filterWhere = "BDCDYID='" + bdcdyid + "' ";
			OperateFeature operateFeature = new OperateFeature(url_iserver_data);
			if ("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
				com.supermap.realestate.gis.common.Feature[] features = operateFeature.queryFeatures_GIS("BDCK",
						bdcdylx.getTableName(djdyly).replaceAll("BDCS_", "BDCK_"), filterWhere);
				if (features == null || features.length < 0) {
					features = operateFeature.queryFeatures_GIS("BDCK", bdcdylx.getTableName(djdyly).replaceAll("BDCS_", "BDCK_"),
							filterWhere);
				}
				if (features != null && features.length > 0) {
					com.supermap.realestate.gis.common.Feature feature = features[0];
					com.supermap.realestate.gis.common.Geometry geometry = feature.geometry;
					int[] parts = geometry.parts;
					int len=0;
					int prelen=0;
					if(parts!=null&&parts.length>0) {
						for (int i : parts) {
							i+=prelen;
							List<POINT2D> part = new ArrayList<POINT2D>();
							while(len<i) {
								POINT2D p = new POINT2D(geometry.points[len].x,geometry.points[len].y);
								part.add(p);
								len++;
							}
							Ponits.add(part);
							prelen=i;
						}
					}
				}
			} else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
				List<com.supermap.services.components.commontypes.Feature> list_feature = operateFeature.queryFeatures_iServer("BDCK",
						bdcdylx.getTableName(djdyly).replaceAll("BDCS_", "BDCK_"), filterWhere);
				if (list_feature == null || list_feature.size() <= 0) {
					list_feature = operateFeature.queryFeatures_iServer("BDCK",
							bdcdylx.getTableName(djdyly).replaceAll("BDCS_", "BDCK_"), filterWhere);
				}
				if (list_feature != null && list_feature.size() > 0) {
					com.supermap.services.components.commontypes.Feature feature = list_feature.get(0);
					com.supermap.services.components.commontypes.Geometry ObjGeometry = feature.geometry;
					int[] parts = ObjGeometry.parts;
					int len=0;
					if(parts!=null&&parts.length>0) {
						for (int i : parts) {
							List<POINT2D> part = new ArrayList<POINT2D>();
							while(len<i) {
								POINT2D p = new POINT2D(ObjGeometry.points[len].x,ObjGeometry.points[len].y);
								part.add(p);
								len++;
							}
							Ponits.add(part);
						}
					}
				}
			}
		}catch (Exception e) {
			System.out.println("请检查是否启用图形服务！！！！！！！！！！！！！！！！！！！！！");
		}
		return Ponits;
	}
	
	public static class POINT2D {
		private String x;
		private String y;

		public POINT2D() {

		}
		
		public POINT2D(double x,double y) {
			this.x = StringHelper.formatObject(new BigDecimal(x));
			this.y = StringHelper.formatObject(new BigDecimal(y));
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
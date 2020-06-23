package com.supermap.realestate.registration.tools;

import java.util.List;

import org.apache.log4j.Logger;

import com.supermap.realestate.registration.maintain.realestateWebHelper;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.services.components.commontypes.Feature;

/**
 * 
 * @Description:图形数据处理操作类
 * @author 俞学斌
 * @date 2015年10月21日 上午08:32:14
 * @Copyright SuperMap
 */
public class GeoOperateTools {
	
	private static Logger logger = Logger.getLogger(GeoOperateTools.class);  
	/**
	 * 拷贝矢量数据
	 * @Title: CopyFeatures
	 * @author:俞学斌
	 * @date：2015年10月21日 上午08:39:45
	 * @param _srcDsName
	 *            源数据源别名
	 * @param _srcDtName
	 *            源数据集名
	 * @param _desDsName
	 *            目标数据源别名
	 * @param _desDtName 目标数据集名
	 * @param _conditon 查询条件
	 */
	public static boolean CopyFeatures(String _srcDsName,String _srcDtName,String _desDsName,String _desDtName,String _conditon,CopyOperateMode mode) {
		_desDtName=_desDtName.replaceAll("BDCS_", "BDCK_");
		_srcDtName=_srcDtName.replaceAll("BDCS_", "BDCK_");
		_desDtName=_desDtName.replaceAll("DCS_", "BDCK_");
		_srcDtName=_srcDtName.replaceAll("DCS_", "BDCK_");
		logger.info("源数据库："+_srcDsName+"；源数据集："+_srcDtName+"目标数据库："+_desDsName+"目标数据集："+_desDtName);
		if(!"1".equals(ConfigHelper.getNameByValue("GeoOperate"))){
			logger.info("矢量数据服务开关未打开!");
			return false;
		}
		boolean b=false;
		try {
			String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
			logger.info("ISERVER数据服务地址："+url_iserver_data);
			OperateFeature hf=new OperateFeature(url_iserver_data);
			if(CopyOperateMode.Append.equals(mode)){
				logger.info("不判断直接拷贝!");
				b=hf.CopyFeatures(_srcDsName, _srcDtName, _desDsName, _desDtName, _conditon);
			}else if(CopyOperateMode.Override.equals(mode)){
				logger.info("覆盖拷贝!");
				hf.deleteFeatures(_desDsName, _desDtName, _conditon);
				b= hf.CopyFeatures(_srcDsName, _srcDtName, _desDsName, _desDtName, _conditon);
			}else if(CopyOperateMode.AppendCaseNotExisted.equals(mode)){
				logger.info("如果存在则不拷贝!");
				if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
					com.supermap.realestate.gis.common.Feature[] features = hf.queryFeatures_GIS(_desDsName, _desDtName, _conditon);
					if (features == null || features.length < 0) {
						logger.info("不存在，开始拷贝!");
						b= hf.CopyFeatures(_srcDsName, _srcDtName, _desDsName, _desDtName, _conditon);
					}
				}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
					List<Feature> list_f=hf.queryFeatures_iServer(_desDsName, _desDtName, _conditon);
					if(list_f==null||list_f.size()<=0){
						logger.info("不存在，开始拷贝!");
						b= hf.CopyFeatures(_srcDsName, _srcDtName, _desDsName, _desDtName, _conditon);
					}
				}
			}
		} catch (Exception e) {
			logger.info("拷贝失败！");
			logger.info(e.getMessage());
		}
		logger.info(b);
		return b;
	}

	/**
	 * 删除矢量数据
	 * @Title: DeleteFeatures
	 * @author:俞学斌
	 * @date：2015年10月21日 上午08:39:45
	 * @param _desDsName
	 *            目标数据源别名
	 * @param _desDtName 目标数据集名
	 * @param _conditon 查询条件
	 */
	public static boolean DeleteFeatures(String _desDsName,String _desDtName,String _conditon) {
		_desDtName=_desDtName.replaceAll("BDCS_", "BDCK_");
		_desDtName=_desDtName.replaceAll("DCS_", "BDCK_");
		if(!"1".equals(ConfigHelper.getNameByValue("GeoOperate"))){
			return false;
		}
		boolean b=false;
		try {
			String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
			OperateFeature hf=new OperateFeature(url_iserver_data);
			//参数：数据源别名，数据集名称，查询条件，地图地址
			b= hf.deleteFeatures(_desDsName, _desDtName, _conditon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * 更新矢量数据属性
	 * @Title: UpdateFeatures
	 * @author:俞学斌
	 * @date：2015年10月21日 上午08:39:45
	 * @param _desDsName
	 *            目标数据源别名
	 * @param _desDtName 目标数据集名
	 * @param _conditon 查询条件
	 */
	public static boolean UpdateFeatures(String _desDsName,String _desDtName,String _conditon,List<String> fieldnames,List<String> fieldvalues) {
		_desDtName=_desDtName.replaceAll("BDCS_", "BDCK_");
		_desDtName=_desDtName.replaceAll("DCS_", "BDCK_");
		if(!"1".equals(ConfigHelper.getNameByValue("GeoOperate"))){
			return false;
		}
		boolean b=false;
		try {
			String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
			OperateFeature hf=new OperateFeature(url_iserver_data);
			b= hf.UpdateFeaturesAttr(_desDsName, _desDtName, fieldnames.toArray(new String[fieldnames.size()]), fieldvalues.toArray(new String[fieldvalues.size()]), _conditon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public static int[] QueryFeatures(String _desDsName,String _desDtName,String _conditon) {
		_desDtName=_desDtName.replaceAll("BDCS_", "BDCK_");
		_desDtName=_desDtName.replaceAll("DCS_", "BDCK_");
		if(!"1".equals(ConfigHelper.getNameByValue("GeoOperate"))){
			return null;
		}
		int[] smids=null;
		try {
			String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
			OperateFeature hf=new OperateFeature(url_iserver_data);
			if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
				com.supermap.realestate.gis.common.Feature[] features = hf.queryFeatures_GIS(_desDsName, _desDtName, _conditon);
				if (features != null && features.length > 0) {
					smids = new int[features.length];
					int kkk = 0;
					for (int i = 0; i < features.length; i++) {
						smids[kkk] = features[i].getID();
						kkk++;
					}
				}
			}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
				List<Feature> list_f=hf.queryFeatures_iServer(_desDsName, _desDtName, _conditon);
				if(list_f!=null&&list_f.size()>0){
					smids=new int[list_f.size()];
					int kkk=0;
					for(Feature f:list_f){
						smids[kkk]=f.getID();
						kkk++;
					}
				}
			}
			
		} catch (Exception e) {
		}
		return smids;
	}
	
	public enum CopyOperateMode {

		/** 按照指定条件，覆盖已有记录(100) */
		Override("100", "按照指定条件，覆盖已有记录"),

		/** 按照指定条件，若记录，则不拷贝(200) */
		AppendCaseNotExisted("200", "按照指定条件，若记录，则不拷贝"),

		/** 追加数据(300) */
		Append("300", "追加数据");

		public String Value;

		public String Name;

		private CopyOperateMode(String value) {
			this.Value = value;
		}

		private CopyOperateMode(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static CopyOperateMode initFrom(String value) {
			for (CopyOperateMode examType : CopyOperateMode.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}
}

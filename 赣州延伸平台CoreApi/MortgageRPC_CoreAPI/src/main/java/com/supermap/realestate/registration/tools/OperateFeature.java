package com.supermap.realestate.registration.tools;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.supermap.realestate.gis.common.DataProvider;
import com.supermap.realestate.gis.common.QueryResult;
import com.supermap.realestate.registration.maintain.realestateWebHelper;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.services.components.MapException;
import com.supermap.services.components.commontypes.EditResult;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.QueryParameter;
import com.supermap.services.providers.RestDataProvider;
import com.supermap.services.providers.RestDataProviderSetting;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;

/**
 * 矢量数据处理服务操作类
 * @ClassName: OperateFeature
 * @author yuxuebin
 * @date 2016年7月13日 21:06:34
 */
public class OperateFeature {

	/*
	 * 数据服务地址
	 */
	private String dataurl = "";
	
	@Autowired
	private CommonDao baseCommonDao;
	/*
	 * RestDataProvider对象
	 */
	private RestDataProvider restDataProvider = null;
	private DataProvider provider = null;
	private String url_gis = ConfigHelper.getNameByValue("URL_NEW_SYSTEM");

	/*
	 * 构造函数 _dataurl 数据服务地址
	 */
	public OperateFeature(String _dataurl) {
		System.out.println("数据服务的地址:"+_dataurl);
		dataurl = _dataurl;
		// 初始化RestDataProvider对象
		RestDataProviderSetting restDataProviderSetting = new RestDataProviderSetting();
		restDataProviderSetting.restServiceRootURL = dataurl;
		restDataProvider = new RestDataProvider(restDataProviderSetting);
		if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
			provider = new DataProvider();
		}
	}

	/**
	 * 原图形系统查询对象
	 * 
	 * @author yuxuebin
	 * @param Alias
	 *            数据源
	 * @param dataSetName
	 *            数据集
	 * @param Filter
	 *            获取数据条件
	 * @return Feature对象集合
	 * @throws MapException
	 */
	public List<Feature> queryFeatures_iServer(String Alias, String DtName,String Filter) {
		List<Feature> Features = null;
		QueryParameter queryParam = new QueryParameter();
		queryParam.name = DtName;
		queryParam.attributeFilter = Filter;
		try {
			
			Features = restDataProvider.getFeature(Alias, queryParam);
		} catch (NullPointerException ex) {
			System.out.print("");
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Features;
	}

	/** 新版图形系统查询图形
	 * @param Alias ： 数据源名
	 * @param DtName ： 数据集名
	 * @param Filter ： 获取数据条件
	 * @return Feature对象集合
	 */
	public com.supermap.realestate.gis.common.Feature[] queryFeatures_GIS(String Alias, String DtName,String Filter){
		QueryResult queryResult = null;
		com.supermap.realestate.gis.common.Feature[] features = null;
		try {
			queryResult = provider.queryFeatures(url_gis, Alias, DtName, Filter);
		} catch (IOException e) {
			System.out.print("查询图形出错："+e.getMessage());
		}
		if (queryResult.recordsets != null && queryResult.recordsets.length > 0) {
			features = queryResult.recordsets[0].features;
			return features;
		}
		return features;
	}
	
	/** 是否启用新版图形系统更新矢量数据的属性信息
	 * @param Alias
	 * @param DtName
	 * @param fldNames
	 * @param fldValues
	 * @param Filter
	 * @return
	 */
	public boolean UpdateFeaturesAttr(String Alias, String DtName,String[] fldNames, String[] fldValues, String Filter){
		boolean b = false;
		if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
			try {
				b =  UpdateFeaturesAttr_GIS(Alias, DtName, fldNames, fldValues, Filter);
			} catch (MapException e) {
				System.out.print("更新矢量图形的属性信息出错：" + e.getMessage());
			}
		}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
			try {
				b = UpdateFeaturesAttr_iServer(Alias, DtName, fldNames, fldValues, Filter);
			} catch (MapException e) {
				System.out.print("更新矢量图形的属性信息出错：" + e.getMessage());
			}
		}
		return b;
	}
	
	/**
	 * 原图形系统更新矢量数据属性信息
	 * 
	 * @author yuxuebin
	 * @param Alias
	 *            数据源名
	 * @param DtName
	 *            数据集名
	 * @param fldNames
	 *            更新数组数组
	 * @param fldValues
	 *            更新属性值数组
	 * @param Filter
	 *            更新数据条件
	 * @return 是否更新成功
	 * @throws MapException
	 */
	public Boolean UpdateFeaturesAttr_iServer(String Alias, String DtName,String[] fldNames, String[] fldValues, String Filter)throws MapException {
		Boolean bo = false;
		List<Feature> featureList = queryFeatures_iServer(Alias, DtName, Filter);
		if (featureList != null && featureList.size() > 0) {
			int[] smids = new int[featureList.size()];
			for (int j = 0; j < featureList.size(); j++) {
				featureList.get(j).fieldNames = fldNames;
				featureList.get(j).fieldValues = fldValues;
			}
			if (smids.length > 0) {
				EditResult editresult = new EditResult();
				editresult = restDataProvider.updateFeatures(Alias, DtName,
						featureList);
				if (editresult.succeed) {
					bo = true;
				}
			}
		}
		return bo;
	}

	/**
	 * 新版图形系统更新矢量数据属性信息
	 * 
	 * @param Alias ： 数据源名
	 * @param DtName ：数据集名
	 * @param fldNames ： 更新数组数组
	 * @param fldValues ：更新属性值数组
	 * @param Filter ：更新数据条件
	 * @return 是否更新成功
	 * @throws MapException
	 */
	public Boolean UpdateFeaturesAttr_GIS(String Alias, String DtName,String[] fldNames, String[] fldValues, String Filter)throws MapException {
		com.supermap.realestate.gis.common.EditResult result = new com.supermap.realestate.gis.common.EditResult();
		try {
			result = provider.updateGeo(url_gis, Alias, DtName, fldNames, fldValues, Filter);
		} catch (IOException e) {
			System.out.print("更新矢量图形的属性信息出错：" + e.getMessage());
		}
		return result.succeed;
	}
	
	/**是否启用新版图形系统删除矢量数据
	 * @param Alias ： 数据源名
	 * @param DtName ： 数据集名
	 * @param Filter ： 删除数据条件
	 * @return ： 是否删除成功
	 * @throws MapException
	 */
	public boolean deleteFeatures(String Alias, String DtName, String Filter) throws MapException{
		if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
			return deleteFeatures_GIS(Alias, DtName, Filter);
		}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
			return deleteFeatures_iServer(Alias, DtName, Filter);
		}else {
			return false;
		}
	}
	
	/**
	 * 原图形系统删除矢量数据
	 * 
	 * @author yuxuebin
	 * @param Alias
	 *            数据源名
	 * @param DtName
	 *            数据集名
	 * @param Filter
	 *            删除数据条件
	 * @return 是否删除成功
	 * @throws MapException
	 */
	public boolean deleteFeatures_iServer(String Alias, String DtName, String Filter)throws MapException {
		int[] smids = null;
		Boolean bo = false;
		List<Feature> featureList = queryFeatures_iServer(Alias, DtName, Filter);
		EditResult editresult = new EditResult();
		if (featureList != null && featureList.size() > 0) {
			smids = new int[featureList.size()];
			for (int j = 0; j < featureList.size(); j++) {
				int smid = Integer.parseInt(getfieldValueByName("SMID",
						featureList.get(j)));
				smids[j] = smid;
			}
			
			editresult = restDataProvider.deleteFeatures(Alias, DtName, smids);
			if (editresult.succeed) {
				bo = true;
				System.out.print("图形删除成功");
			}
			else{
				System.out.print("图形删除出错："+editresult.message);
			}
		} else {
			bo = true;
		}
		
		String zdSql = "select * fro bdck.bdck_shyqzd_xz where "+Filter;
		List<Map> zds = baseCommonDao.getDataListByFullSql(zdSql);
		if(zds != null){
			int j = 0;
			for (Map map : zds) {
				int _smid = Integer.parseInt((String)map.get("SMID"));
				smids[j] = _smid;
				j++;
			}
			editresult = restDataProvider.deleteFeatures(Alias, DtName, smids);
			if (editresult.succeed) {
				bo = true;
				System.out.print("图形删除成功");
			}
			else{
				System.out.print("图形删除出错："+editresult.message);
			}
		}
		return bo;
	}

	/**新版图形系统删除矢量数据
	 * @param Alias ： 数据源名
	 * @param DtName ： 数据集名
	 * @param Filter ： 删除数据条件
	 * @return ： 是否删除成功
	 */
	public boolean deleteFeatures_GIS(String Alias, String DtName, String Filter){
		boolean b = false;
		
		com.supermap.realestate.gis.common.EditResult eResult;
		try {
			eResult = provider.deleteFeatures(url_gis, Alias, DtName, Filter);
			if(eResult.ids != null && eResult.ids.length > 0){
				b=true;
			}
		} catch (JsonParseException e) {
			System.out.print("图形删除出错："+e.getMessage());
		} catch (JsonMappingException e) {
			System.out.print("图形删除出错："+e.getMessage());
		} catch (IOException e) {
			System.out.print("图形删除出错："+e.getMessage());
		}
		return b;
	}
	
	/** 是否启用新版图形系统拷贝矢量数据
	 * @param srcAlias ： 源数据源
	 * @param srcDtName ： 源数据集
	 * @param destAlias ： 目标数据源
	 * @param destDtName ： 目标数据集
	 * @param Filter ： 拷贝条件
	 * @return 是否拷贝成功
	 * @throws MapException
	 */
	public boolean CopyFeatures(String srcAlias, String srcDtName,String destAlias, String destDtName, String Filter) throws MapException{
		if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
			return CopyFeatures_GIS(srcAlias, srcDtName, destAlias, destDtName, Filter);
		}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
			return CopyFeatures_iServer(srcAlias, srcDtName, destAlias, destDtName, Filter);
		}else {
			return false;
		}
	}
	
	/**
	 * 拷贝矢量数据
	 * 
	 * @author yuxuebin
	 * @param srcAlias
	 *            源数据源
	 * @param srcDtName
	 *            源数据集
	 * @param destAlias
	 *            目标数据源
	 * @param destDtName
	 *            目标数据集
	 * @param Filter
	 *            拷贝数据条件
	 * @return 是否拷贝成功
	 * @throws MapException
	 */
	public boolean CopyFeatures_iServer(String srcAlias, String srcDtName,String destAlias, String destDtName, String Filter)throws MapException {
		Boolean bo = false;
		List<Feature> featureList = queryFeatures_iServer(srcAlias, srcDtName, Filter);
		if (featureList.size() > 0) {
			EditResult editresult = new EditResult();
			editresult = restDataProvider.addFeatures(destAlias, destDtName,
					featureList);
			if (editresult.succeed) {
				bo = true;
			}
		}
		return bo;
	}

	/** 新版图形系统拷贝矢量数据
	 * @param srcAlias ： 源数据源
	 * @param srcDtName ： 源数据集
	 * @param destAlias ： 目标数据源
	 * @param destDtName ： 目标数据集
	 * @param Filter ： 拷贝条件
	 * @return ： 是否拷贝成功
	 */
	public boolean CopyFeatures_GIS(String srcAlias, String srcDtName,String destAlias, String destDtName, String Filter){
		Boolean b = false;
		try {
			com.supermap.realestate.gis.common.EditResult eResult =  provider.copyFeatures(url_gis, srcAlias, srcDtName, destAlias, destDtName, Filter, null, null);
			if(eResult.ids != null && eResult.ids.length > 0){
				b=true;
			}
		} catch (IOException e) {
			System.out.print("拷贝图形出错："+e.getMessage());
		}
		return b;
	}
	
	/**
	 * 获取Feature对象指定属性值
	 * 
	 * @author yuxuebin
	 * @param fieldName
	 *            属性
	 * @param feature
	 *            Feature对象
	 * @return String 属性值
	 */
	private String getfieldValueByName(String fieldName, Feature feature) {
		for (int i = 0; i < feature.fieldNames.length; i++) {
			if (feature.fieldNames[i].equalsIgnoreCase(fieldName)) {
				return feature.fieldValues[i];
			}
		}
		return "";
	}
}

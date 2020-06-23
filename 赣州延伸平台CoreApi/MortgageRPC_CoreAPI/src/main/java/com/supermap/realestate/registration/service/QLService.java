package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.Message;

/**
 * 
 * @Description:权利service
 * @author 刘树峰
 * @date 2015年6月12日 下午3:10:12
 * @Copyright SuperMap
 */
public interface QLService {

	/**
	 * 获取权利信息
	 * 
	 * @param qlid
	 * @return
	 */
	public BDCS_QL_GZ GetQL(String qlid);

	/**
	 * 更新权利信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月8日上午8:03:31
	 * @param ql
	 */
	public void UpdateQLandRebuildRelation(BDCS_QL_GZ ql);

	/**
	 * 根据附属权利ID获取附属权利信息
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午11:56:45
	 * @param fsqlid
	 * @return
	 */
	public BDCS_FSQL_GZ GetFSQL(String fsqlid);

	/**
	 * 更新附属权利信息
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午11:57:10
	 * @param fsql
	 */
	public void UpdateFSQL(BDCS_FSQL_GZ fsql);

	/**
	 * 获取林地所对应的权利及权利附属信息
	 * @作者 海豹
	 * @创建时间 2015年6月28日上午12:27:43
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map GetQlandFsqlInfo(String qlid);

	/**
	 * 组合业务：初始登记+在建工程抵押转现房抵押
	 * @作者 海豹
	 * @创建时间 2015年7月24日下午11:34:48
	 * @param djdyid
	 *            ：通过登记单元ID查找出所有的权利，并从中找出抵押权为23
	 * @return
	 */
	public Map<String, String> GetCombDyqInfo(String djdyid, String xmbh);

	/**
	 * 获取权利人信息
	 * 
	 * @param qlrid
	 * @return
	 */
	public BDCS_QLR_GZ GetQLRInfo(String qlrid);

	/**
	 * 获取（现状层）权利人信息
	 * @author diaoliwei
	 * @date 2015-8-3
	 * @param qlrid
	 * @return
	 */
	public BDCS_QLR_XZ GetXZQLRInfo(String qlrid);

	/**
	 * 根据权利ID获取现状权利人列表
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午3:36:42
	 * @param qlid
	 * @return
	 */
	public List<BDCS_QLR_XZ> getXZQLRList(String qlid);

	/**
	 * 根据抵押权权利id获取到所有权的权利人列表
	 * @author diaoliwei
	 * @date 2015-8-3
	 * @param dyqQlid
	 * @param qllx
	 * @return
	 */
	public List<BDCS_QLR_XZ> getSYQXZQLRList(String dyqQlid, String qllx);

	/**
	 * 分页获取登记单元对应的权利人列表
	 * 
	 * @param xmbh
	 * @param qlid
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page GetPagedQLR(String xmbh, String qlid, Integer page, Integer rows);

	/**
	 * 更新权利人
	 * 
	 * @param qlr
	 */
	public void UpdateQLR(BDCS_QLR_GZ qlr);

	/**
	 * 根据申请人ID数组添加权利人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午3:12:27
	 * @param xmbh
	 * @param qlid
	 * @param sqrids
	 */
	public ResultMessage addQLRfromSQR(String xmbh, String qlid, Object[] sqrids);

	/**
	 * 移除权利人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午4:15:51
	 * @param xmbh
	 * @param qlid
	 * @param qlrid
	 */
	public ResultMessage removeQLR(String xmbh, String qlid, String qlrid);

	/**
	 * 更新初始登记-国有土地使用权抵押中权利信息，需要做3件事； 1、保存权利人信息；2、更新权利信息；3、更新附属权利信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月12日下午9:15:55
	 * @修改时间
	 * @修改人 刘树峰
	 * @param map
	 */
	public void SaveQlInfo(String xmbh, String qlid, JSONObject object);

	/**
	 * 批量更新抵押信息
	 * @作者 海豹
	 * @创建时间 2015年8月6日上午12:23:14
	 * @param xmbh
	 * @param object
	 */
	public void plSaveQlInfo(String xmbh, JSONObject object);
	
	/**
	 * 批量更新林权信息
	 * @作者 rq
	 * @创建时间 2017年4月26日
	 * @param xmbh
	 * @param object
	 */
	public void plSaveQlInfo_lq(String xmbh, JSONObject object);
	/**
	 * 根据项目编号和权利ID获取抵押权信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月14日下午11:38:57
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public Map<String, Object> getDYQInfo(String xmbh, String qlid);

	/**
	 * 根据权利ID获取抵押权信息（转移登记+一般抵押权（上一手的抵押信息））
	 * @作者 海豹
	 * @创建时间 2015年9月1日上午1:13:27
	 * @param qlid
	 * @return
	 */
	public Map<String, Object> getzydj_DYQInfo(String qlid,String ly);

	/**
	 * 获取转移登记的抵押权信息
	 * @作者 diaoliwei
	 * @创建时间 2015年7月14日下午4:03:06
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public Map<String, Object> getDyqZyInfo(String xmbh, String qlid);

	/**
	 * 通过qlid获取查封的基本信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:09:28
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getCfxxInfo(String qlid);

	/**
	 * 更新查封中的；通过qlid更新ql_gz中的fj，及更新fsql_gz中的内容
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:08:35
	 * @param qlid
	 * @param object
	 */
	public void updateCfxxInfo(String qlid, JSONObject object);

	/**
	 * 通过项目编号批量更新查封信息
	 * @作者 海豹
	 * @创建时间 2015年8月5日下午10:14:30
	 * @param xmbh
	 * @param object
	 */

	public void plUpdateCfxxInfo(String xmbh, JSONObject object);

	/**
	 * 更新解封中的；通过qlid更新ql_gz中的fj，及更新fsql_gz中的内容
	 * 
	 * @作者 WUZ
	 * @创建时间 2015年6月18日上午12:08:35
	 * @param qlid
	 * @param object
	 */
	public void updateJfxxInfo(String qlid, JSONObject object);

	/**
	 * 通过项目编号批量更新解封信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月5日下午11:48:02
	 * @param xmbh
	 * @param object
	 */
	public void plUpdateJfxxInfo(String xmbh, JSONObject object);

	/**
	 * 通过xmbh获取页面初始的预告登记权利信息
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月18日下午19:38:28
	 * @param
	 */
	public Map<String, String> getYgxxInfoInit(String xmbh);

	/**
	 * 通过xmbh获取预告登记权利信息
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月18日下午19:38:28
	 * @param
	 */
	public Map<String, String> getYgxxInfo(String xmbh);

	/**
	 * 通过qlid获取注销的基本信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:09:28
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getZxxxInfo(String qlid);

	/**
	 * 更新注销中的；通过qlid更新ql_gz中的fj，及更新fsql_gz中的内容
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:08:35
	 * @param qlid
	 * @param object
	 */
	public void updateZxxxInfo(String qlid, JSONObject object);

	/**
	 * 批量更新注销信息：通过项目编号更新
	 * @作者 海豹
	 * @创建时间 2015年8月4日下午10:29:17
	 * @param xmbh
	 * @param object
	 */
	public void plUpdateZxxxInfo(String xmbh, JSONObject object);

	/**
	 * 获取异议登记权利信息，通过来源权利ID
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 上午2:09:01
	 * @param lyqlid
	 * @return
	 */
	public Map<String, String> GetYYDJQL(String qlid);

	/**
	 * 更新异议登记权利信息，通过来源权利ID
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 上午2:09:01
	 * @param lyqlid
	 * @return
	 */
	public boolean updateYYDJQL(String qlid, HttpServletRequest request);

	/**
	 * 通过权利_GZ及申请人ID数组添加权利人
	 * @作者 海豹
	 * @创建时间 2015年7月2日下午4:27:03
	 * @param xmbh
	 * @param ql
	 * @param sqrids
	 */
	public void plAddQlRfromSQR(String xmbh, BDCS_QL_GZ ql, Object[] sqrids);

	/**
	 * 首次登记-一般抵押权-在建工程抵押权登记转现房抵押登记时，抵押权人不能被修改,只保存权利及附属权利信息
	 * @作者 海豹
	 * @创建时间 2015年7月16日上午12:03:29
	 * @param xmbh
	 * @param qlid
	 * @param object
	 */
	public void SaveQlandFsqlInfo(String xmbh, String qlid, JSONObject object);

	/**
	 * 批量更新一般抵押权-在建工程抵押权登记转现房抵押登记时，抵押权人不能被修改,只保存权利及附属权利信息
	 * @作者 海豹
	 * @创建时间 2015年8月6日上午12:48:40
	 * @param xmbh
	 * @param object
	 */
	public void plSaveQlandFsqlInfo(String xmbh, JSONObject object);

	/**
	 * 2015年8月7日刘树峰新加，只保存权利，不重新构建关系
	 * @Title: UpdateQL
	 * @author:liushufeng
	 * @date：2015年8月7日 下午11:52:10
	 * @param ql
	 */
	public void UpdateQL(BDCS_QL_GZ ql);

	@SuppressWarnings("rawtypes")
	public Map getRightsAndSubRights(DJDYLY ly, String qlid) throws ClassNotFoundException;

	/**
	 * 获取现状林权信息
	 * @Title: getXZForestRightsInfo
	 * @author liushufeng
	 * @date：2015年10月10日 下午2:56:20
	 * @param qlid
	 * @return
	 */
	public Map<String, String> getXZForestRightsInfo(String qlid);

	/**
	 * 注销信息显示： 通过权利id从BDCS_FSQL_GZZ中获取数据
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年12月05日 18:32:16
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getLoadZxxxInfo(String qlid, String xmbh);

	/**
	 * 根据项目编号和土地用途更新户中土地用途
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年12月05日 18:06:16
	 * @param djdyid
	 * @param tdyt
	 * @param xmbh
	 * @return
	 */
	public void Updatetdyt(String djdyid, String tdyt, String xmbh);

	/**
	 * 获取单元限制详细信息
	 * 
	 * @Title: getXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	public BDCS_DYXZ getXZInfo(String xzdyid);

	/**
	 * 更新单元限制详细信息（URL:"/{xzdyid}/xzinfo/",Method：POST）
	 * 
	 * @Title: updateXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean updateXZInfo(String xzdyid, HttpServletRequest request);

	/**
	 * 获取单元限制列表
	 * 
	 * @Title: getXZInfoList
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getXZInfoList(String bdcdyid, String xmbh);

	/**
	 * 解除单元限制列表
	 * 
	 * @Title: DYXZLifted
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param xzdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	public ResultMessage DYXZLifted(String xzdyid, String xmbh);

	/**
	 * 获取解除单元限制详细信息
	 * 
	 * @Title: getZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	public BDCS_XM_DYXZ getZXXZInfo(String xzdyid, String xmbh);

	/**
	 * 批量更新解除单元限制详细信息
	 * 
	 * @Title: updateZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean updatePlZXXZInfo(String xmbh, HttpServletRequest request);

	/**
	 * 更新解除单元限制详细信息
	 * 
	 * @Title: updateZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean updateZXXZInfo(String xzdyid,String xmbh, HttpServletRequest request);
	
	/**
	 * 获取抵押权变更权利信息
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> getBGdyqInfo(String xmbh, String type);

	/**
	 * 获取抵押权变更抵押权人权利信息
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	public List<RightsHolder> getBGdyqrInfo(String xmbh, String type);
	
	/**
	 * 获取（当前一条）抵押权变更 抵押权人信息（URL:"/qlrgz/{qlrid}",Method：GET）
	 * @作者 WLB
	 * @创建时间 2017年05月25日下午22:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	public BDCS_QLR_GZ getCurrentDyqrInfo(String qlrid);
	
	/**
	 * 修改抵押权变更权利信息（URL:"/modifybgqdyqlrs/qlrinfo/{qlrid}",Method：POST）
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	public void modifyBgqDyqlrs(Map map,String xmbh);
	
	
	/**
	 * 保存抵押权变更权利信息
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	public void plSaveQlInfo2(String xmbh, String bdcdyid, HttpServletRequest request);
	
	/**
	 * 保存抵押权变更权利信息
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	public void plSaveQlInfo2(String xmbh, HttpServletRequest request);

	/**
	 * 解除单元抵押
	 * 
	 * @Title: dybgCancel
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param qlid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	public ResultMessage dybgCancel(String qlid, String xmbh);

	/**
	 * 移除单元限制详细信息
	 * 
	 * @Title: RemoveXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean RemoveXZInfo(String xzdyid, String xmbh);

	/**
	 * 添加单元限制详细信息
	 * 
	 * @Title: AddXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月15日 01:10:18
	 * @param bdcdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	public ResultMessage AddXZInfo(String bdcdyid, String xmbh, String bdcdylx, HttpServletRequest request);

	/**
	 * 获取单元限制列表
	 * 
	 * @Title: getXZInfoList
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getXZInfoListFromGZ(String bdcdyid, String xmbh);

	/**
	 * 批量更新权利人信息
	 * @Title: batchUpdateQLR
	 * @author:liushufeng
	 * @date：2016年3月16日 上午11:42:43
	 * @param holder
	 *            权利人信息
	 * @param option
	 *            更新选项，1：按照权利人名称批量更新，2：按照权利人证件号批量更新，3：按照名称和证件号更新
	 * @return
	 */
	public ResultMessage batchUpdateQLR(RightsHolder holder, String option);

	/*
	 * 批量更新单元限制详细信息
	 */
	public boolean updatePlXZInfo(String xmbh, HttpServletRequest request);

	public Map<String, Object> GetQLEx(String qlid) throws Exception;

	public void UpdateZYQLRList(String[] zyqlrids,BDCS_QL_GZ ql);

	public void updateQzh(String qlrid, String dataString, String xmbh);

	public void updataQLqzh(String qlrid);
	
	/**
	 * 登簿后更新权证号和登记时间（组件功能）
	 * @author weilb
	 * @param String qlrid ,djsj
	 * gz,xz,ls 持久化操作
	 */
	public void updateDbhQzh(String qlrid,String bdcdqzh,String djsj);
	
	@SuppressWarnings("rawtypes")
	public Map getCfxxInfoEX(String qlid);

	public Map<String, Object> getls_DYQInfo(String qlid);
	
	public Map<String, Object> getBGdyqInfo_dy(String bdcdyid,String xmbh, String type);
	public abstract Message getAllMortgage(String paramString);
	public List<BDCS_WSXX> getWsxxInfo(String xmbh, String qlid);
}

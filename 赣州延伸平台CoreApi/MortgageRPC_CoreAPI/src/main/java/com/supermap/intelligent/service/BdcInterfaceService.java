package com.supermap.intelligent.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



public interface BdcInterfaceService {
	/**对外接口，token认证
	 * @param request
	 * @return
	 */
	public String finalResultModule(HttpServletRequest request) throws Exception;
	public JSONObject coreQueryAlias(HttpServletRequest request) throws Exception;
	/**未加密处理，内部使用
	 * @param request
	 * @return
	 */
	public JSONObject resultsDecryptJson(HttpServletRequest request);
	
	/**查询证书数据
	 * @param cxsql
	 * @return
	 */
	public JSONArray ZS_JSONArray(@SuppressWarnings("rawtypes") List<Map> zsidMaps) ;
	
	
	/**查询证明数据
	 * @param cxsql
	 * @return
	 */
	public JSONArray ZM_JSONArray(@SuppressWarnings("rawtypes") List<Map> zsidMaps) ;
	
	
	/**获取token 
	 * @param request
	 * @return
	 */
	public String applicationToken(HttpServletRequest request);
}
package com.supermap.realestate.registration.service;

import java.util.Map;

/**
 * 交易后台接口
 * @author weilb
 */
public interface DealService {
	
	/**
	 * 获取交易详细信息
	 * @return
	 */
   public Map<String,Object> getDealDetails(String BDCDYH,String BDCDYID);
   
    /**
     * 通过权证号精确获取信息
     * @param QZH
     * @return Map<String,List<Map>> 
     */
   public Map<String,Object> getZDInfoByQZH(String QZH);
   
}

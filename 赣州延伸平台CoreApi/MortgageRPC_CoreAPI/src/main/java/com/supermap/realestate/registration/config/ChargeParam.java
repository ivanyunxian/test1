/**   
 * 费用计算参数类
 * @Title: ChargeParam.java 
 * @Package com.supermap.realestate.registration.config 
 * @author liushufeng 
 * @date 2015年7月26日 上午2:16:08 
 * @version V1.0   
 */

package com.supermap.realestate.registration.config;

import java.util.HashMap;
import java.util.Map;


/**
 * 费用计算参数类
 * @ClassName: ChargeParam
 * @author liushufeng
 * @date 2015年7月26日 上午2:16:08
 */
public class ChargeParam {

	private Map<String, Double> map = new HashMap<String, Double>();

	public Double get(String paramname) {
		Double d = (double) 0;
		if (map.containsKey(paramname))
			d = map.get(paramname);
		return d;
	}

	public void put(String name, double value) {
		if (map.containsKey(name)) {
			map.remove(name);
		}
		map.put(name, value);
	}
	
	public  Map<String, Double> getMap()
	{
		return map;
	}
}

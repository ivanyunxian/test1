package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 交易登记一体化数据交互，房产交易系统和登记系统数据交互    
 * @author wanghongchao
 *
 */
public interface JYDJYTHDataSwapService {
	/**
	 * 通过合同编号读取
	 * <pre>
	 * 1.首先要判断登记类型是期房还是现房，如果是现房，则要读取现房合同，如果通过期房，读取期房合同，从而获取房屋数据，根据
	 * 	SMRIGHT_STO.T_HOUSE a,SMPRESALE_STO.T_COMPACT_HOUSE b  a.HOUSE_ID=b.HOUSE_ID
	 * 2.得到houseID，通过一下关系获取到登记系统里面的房屋信息，
	 * 	通过登记系统里面的房屋信息创建单元，申请人中的权利人和义务人从合同数据中获取。根据：
	 * 	SMRIGHT_STO.T_HOUSE a,BDCK.BDCS_H_XZY b  a.HOUSE_CHID=b.BDCDYID
	  </pre>
	 * @param casenum
	 * @param xmbh
	 * @param project_id
	 * @param params 
	 * @return
	 */
	ResultMessage readContract(String casenum, String xmbh,String project_id,Map params);
	
	/**
	 * 从视图V_JY_COMPACTHOUSEALL查询数据 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Message queryHouseCompact(Map params);

	/**
	 *  不动产登簿操作，则将房屋及权利状态发送给房产交易，调用房产交易系统的接口，推送房屋的登记状态信息，
	 *  这里因为是推送信息，登记系统不应该关注是否推送成功，
	 *  而处于等待状态，而是触发推送事件，自身业务继续往下执行，无需等待交易接口反馈。
	 *  这种情况下就得使用异步请求来实现，即另起线程
	 *  建议使用 AsyncRestTemplate
	 * @param xmbh
	 */
	void writeBackHouseStateToFCJY(String xmbh);
	
	/**
	   *   获取不动产权利，通过视图BDCK.V_HOUSE_STATE
	 * @param bdcdyids
	 * @return
	 */
	Message getHouseState(List<String>  bdcdyids);
	
	
	
}

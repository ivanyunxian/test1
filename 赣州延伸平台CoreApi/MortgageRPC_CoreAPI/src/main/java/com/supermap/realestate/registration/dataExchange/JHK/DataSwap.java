/**
 * 
 */
package com.supermap.realestate.registration.dataExchange.JHK;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * @author likun
 *
 */
public interface DataSwap {
	/**
	 * 
	 * @作者 likun
	 * @创建时间 2015年12月23日下午1:58:58
	 * @param baseCommonDao 
	 * @param xmxx BDCS_XMXX model
	 * @param bljd 办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
	 */
    void pushToGXDJK(CommonDao baseCommonDao,BDCS_XMXX xmxx,String bljd);
  
    /**
	 * 更新办理进度值
	 * xmbh 项目编号
	 * bljd 办理进度
	 */
    void updateBLJD(String xmbh,String bljd);
    
    /**
     * 退件时删除
     * @作者 think
     * @创建时间 2016年1月13日下午4:12:12
     * @param gxxmbh
     */
    void delete(String gxxmbh);
}

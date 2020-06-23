package com.supermap.realestate.registration.service;

import java.util.Map;

import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.util.Message;


/**
 * 
 * @Description:权利service
 * @author 刘树峰
 * @date 2015年6月12日 下午3:10:12
 * @Copyright SuperMap
 */
public interface QLRService {

/**
 * 获取大宗地上权利人信息
 * @param zdbdcdyid 宗地不动产单元ID
 * @param pageIndex 当前页的索引号
 * @param pageSize 每页的大小
 * @param mapCondition 过滤条件集合
 * @return
 */
public Message getZDQLRInfo(String zdbdcdyid, int pageIndex, int pageSize,Map<String, Object> mapCondition);	

/**
 * 注销权利人
 * @param zdbdcdyid ：宗地不动产单元id
 * @param qlrids：权利人ID集合
 * @param ywhs：权利人对应的注销业务号
 * @return 返回1表示只注销权利人,返回0表示大宗地上的权利人都注销完了，是否需要灭失单元
 */
public ResultMessage writeHolderBook(String zdbdcdyid,String qlrids,String ywhs);
public ResultMessage writeUnitBook(String zdbdcdyid);
}

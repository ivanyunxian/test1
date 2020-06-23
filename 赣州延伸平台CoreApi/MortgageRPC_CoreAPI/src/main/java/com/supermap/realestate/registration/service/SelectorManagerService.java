package com.supermap.realestate.registration.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;


/**
 * 选择器配置
 * @ClassName: SelectorManagerController
 * @author 俞学斌
 * @date 2016年08月25日 10:42:28
 */
public interface SelectorManagerService {
	
	/**
	 * 获取选择器信息 
	 * @param: GetSelectorInfo
	 * @author:俞学斌
	 * @date：2016年08月25日 16:36:28
	 * @return
	 */
	HashMap<String, String> GetSelectorInfo(String selectorid);

	/**
	 * 更新选择器信息 
	 * @Title: UpdateSelectorInfo
	 * @author:俞学斌
	 * @date：2016年08月25日 16:36:28
	 * @return
	 */
	ResultMessage UpdateSelectorInfo(String selectorid,
			HttpServletRequest request);

	/**
	 * 获取选择器查询条件列表
	 * @Title: GetQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:10:28
	 * @return
	 */
	Message GetQueryConfig(String selectorid, HttpServletRequest request);

	/**
	 * 新增或保存选择器查询条件 
	 * @Title: AddOrUpdateQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:14:28
	 * @return
	 */
	ResultMessage AddOrUpdateQueryConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 删除选择器查询条件
	 * @Title: RemoveQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:13:28
	 * @return
	 */
	ResultMessage RemoveQueryConfig(String id, HttpServletRequest request);

	/**
	 * 重置选择器查询条件顺序 
	 * @Title: ResetSXHOnQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:12:28
	 * @return
	 */
	ResultMessage ResetSXHOnQueryConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 获取选择器查询排序列表
	 * @Title: GetSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	Message GetSortConfig(String selectorid, HttpServletRequest request);

	/**
	 * 新增或保存选择器查询排序 
	 * @Title: AddOrUpdateSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage AddOrUpdateSortConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 删除选择器查询排序
	 * @Title: RemoveSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage RemoveSortConfig(String id, HttpServletRequest request);

	/**
	 * 重置选择器查询排序顺序 
	 * @Title: ResetSXHOnSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage ResetSXHOnSortConfig(String selectorid,
			HttpServletRequest request);
	
	/**
	 * 获取选择器查询结果列表
	 * @Title: GetGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	Message GetGridConfig(String selectorid, HttpServletRequest request);

	/**
	 * 新增或保存选择器查询结果 
	 * @Title: AddOrUpdateGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage AddOrUpdateGridConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 删除选择器结果排序
	 * @Title: RemoveGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage RemoveGridConfig(String id, HttpServletRequest request);

	/**
	 * 重置选择器查询结果顺序 
	 * @Title: ResetSXHOnGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage ResetSXHOnGridConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 获取选择器结果常量列表
	 * @Title: GetResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	Message GetResultConfig(String selectorid, HttpServletRequest request);

	/**
	 * 新增或保存选择器结果常量
	 * @Title: AddOrUpdateResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage AddOrUpdateResultConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 删除选择器结果常量
	 * @Title: RemoveResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage RemoveResultConfig(String id, HttpServletRequest request);

	/**
	 * 获取选择器查询结果详情
	 * @Title: GetDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	Message GetDetailConfig(String selectorid, HttpServletRequest request);

	/**
	 * 新增或保存选择器结果详情 
	 * @Title: AddOrUpdateDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage AddOrUpdateDetailConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 删除选择器结果详情
	 * @Title: RemoveDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage RemoveDetailConfig(String id, HttpServletRequest request);

	/**
	 * 重置选择器查询结果详情
	 * @Title: ResetSXHOnDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage ResetSXHOnDetailConfig(String selectorid,
			HttpServletRequest request);

	/**
	 * 复制选择器 
	 * @Title: CopySelector
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	ResultMessage CopySelector(String selectorid, HttpServletRequest request);
}

package com.supermap.realestate.registration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.supermap.realestate.registration.ViewClass.DJDY_LS_EX;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

public interface TJCXFXService {
	
	/**
	 * 获取行政区信息
	 * @return
	 */
	public Message GetQueryxzq_info();
	
	/**
	 * 通过行政区，房屋类型，统计方式，统计单位，起始时间，终止时间对房屋交易分析统计
	 * @param strxzq 行政区
	 * @param fwlx 房屋类型     商品房买卖、存量房买卖
	 * @param tjfs 统计方式    按面积统计、按成交价格、按成交套数
	 * @param tjdw 统计单位    按日统计、按月统计、按年统计
	 * @param kssj 起始时间
	 * @param jssj 终止时间
	 * @return
	 */
	public Message Getxz_fwdj_info(String strxzq,String fwlx,String tjfs,String tjdw,String kssj,String jssj);
	
	/**
	 * 通过行政区，土地类型，统计方式，统计单位，起始时间，终止时间对土地交易分析统计
	 * @param strxzq 行政区
	 * @param fwlx 土地类型     商品房买卖、存量房买卖
	 * @param tjfs 统计方式    按面积统计、按成交价格
	 * @param tjdw 统计单位    按日统计、按月统计、按年统计
	 * @param kssj 起始时间
	 * @param jssj 终止时间
	 * @return
	 */
	public Message Getxz_tdfxdj_info(String strxzq,String fwlx,String tjfs,String tjdw,String kssj,String jssj);
	
	/**
	 * 通过行政区，登陆类型，统计单位，起始时间，终止时间对登记 业务统计
	 * @param strxzq 行政区
	 * @param fwlx 土地类型     商品房买卖、存量房买卖
	 * @param tjdw 统计单位    按日统计、按月统计、按年统计
	 * @param kssj 起始时间
	 * @param jssj 终止时间
	 * @return
	 */
	public Message Getxz_djywtj_info(String strxzq,String fwlx,String tjdw,String kssj,String jssj);
	
	/**
	 * 通过行政区，起始时间，终止时间对房屋交易报表
	 * @param strxzq 行政区
	 * @param kssj 起始时间
	 * @param jssj 终止时间
	 * @return
	 */
	public Message Getxz_fwjybb_info(String strxzq,String kssj,String jssj);
	
	/**
	 * 获取权利信息
	 * @param qlid
	 * @return
	 */
	public Message Getxz_QLXXXZ(String qlid);
	
	/**
	 * 通过不动产单元ID,单元类型获取权利信息列表
	 * 
	 * @param BDCDYID
	 * @param BDCDYTYPE
	 * @return
	 */
	public Message Get_BDCDYID_ALLLC(String BDCDYID,String BDCDYTYPE);
	
	/**
	 * 通过不同产单元号查询项目信息 历史
	 * @param strBDCDYH
	 * @return
	 */
	public Message GetProject_History(String strBDCDYH);
	
	/**
	 * 不动产单元查询 现状
	 * @param page
	 * @param rows
	 * @param StrZL
	 * @param Strbdcqzh
	 * @param StrBDCDYH
	 * @param StrType
	 * @return
	 */
	public Message Getxzzs_Info(int page, int rows, String StrZL,
			String Strbdcqzh, String StrBDCDYH, String StrType,String StrXm,String StrZjh);
	
	/**
	 * 通过不同产单元号查询项目信息 现状
	 * @param strBDCDYH
	 * @return
	 */
	public Message GetProject_xz(String strBDCDYH);
	
	/**
	 * 通过不动产单元号获取不动产权证号，权利人名称
	 * @param strbdcdyh
	 * @return
	 */
	public Message GetQLR_CQZInfo(String strbdcdyh);
	
	/**
	 * 历史追溯查询
	 * @param page
	 * @param rows
	 * @param StrZL
	 * @param Strbdcqzh
	 * @param StrBDCDYH
	 * @param StrType
	 * @return
	 */
	public Message Getlszs_Info(int page, int rows, String StrZL,
			String Strbdcqzh, String StrBDCDYH, String StrType);
	
	/**
	 * 获取不动产土地登记业务WUZHU
	 * @return
	 */
	public Message GetWorkBookLand(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception;
	/**
	 * 获取不动产土地登记抵押业务
	 * @return
	 */
	public Message GetDiyInfo(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception;
	/**
	 * 获取不动产土地登记抵押业务面积、抵押金额汇总
	 * @return
	 */
	public HashMap<String, String> GetDiyInfo_HZ(Map<String, String> conditionParameter) throws Exception;
	/**
	 * 查封业务导出
	 * @作者 胡加红
	 * @创建时间 2016年3月20日下午4:40:07
	 * @param conditionParameter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> DiyInfoDownload(Map<String, String> conditionParameter) throws Exception;
	
	/**
	 * 获取不动产土地登记抵押业务
	 * @return
	 */
	public Message GetCFInfo(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception;
	/**
	 * 查封业务导出
	 * @作者 胡加红
	 * @创建时间 2016年3月20日下午4:40:07
	 * @param conditionParameter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> CFInfoDownload(Map<String, String> conditionParameter) throws Exception;
	/**
	 * 获取不动产房屋登记业务WUZHU
	 * @return
	 */
	public Message GetHouseQueryList(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception;
	
	/**
	 * 获取不动产房屋登记业务WUZHU
	 * @return
	 */
	public List<DJDY_LS_EX> WorkBookDownload(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception;

	/** 
	 * 不动产登记台账
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public Message GetStandingBook(Map<String, String> mapCondition,
			Integer page, Integer rows);

	/** 
	 * 到处不动产登记台账
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> GetStandingBook(Map<String, String> mapCondition);

	/** 
	 * 自定义查询统计获取数据服务
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	
	public HashMap<String, String> GetStandingBook_HZ(Map<String, String> mapCondition);
	
	
	public Message GetUserDefineBook(String id,HttpServletRequest request);

	/** 
	 * 获取自定义查询统计配置信息
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public HashMap<String, Object> GetUserDefineBookConfig(String id);

	/** 
	 * 自定义查询统计定义管理页面
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> UserDefineBookALL(String id, HttpServletRequest request);

	/** 
	 * 自定义查询统计定义列表
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public Message GetBookDefineList(HttpServletRequest request);

	/** 
	 * 添加或更新自定义查询统计定义
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public ResultMessage AddOrUpdateBookDefine(HttpServletRequest request);

	/** 
	 * 删除自定义查询统计定义
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public ResultMessage RemoveBookDefine(String id);

	/** 
	 * 自定义查询统计查询条件列表
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public Message GetQueryManager(String bookid);

	/** 
	 * 添加或更新自定义查询统计查询条件
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public ResultMessage AddOrUpdateQueryManager(String bookid,HttpServletRequest request);

	/** 
	 * 删除自定义查询统计查询条件
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public ResultMessage RemoveQueryManager(String id);
	
	/** 
	 * 自定义查询统计结果字段列表
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public Message GetResultManager(String bookid);

	/** 
	 * 添加或更新自定义查询统计结果字段
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public ResultMessage AddOrUpdateResultManager(String bookid,HttpServletRequest request);

	/** 
	 * 删除自定义查询统计结果字段
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public ResultMessage RemoveResultManager(String id);

	/**
	 * 不动产登记台账(云南玉溪)
	 * @param mapCondition
	 * @param page
	 * @param rows
	 * @return
	 */
	Message GetStandingBookYuxi(Map<String, String> mapCondition, Integer page, Integer rows);

	/**
	 * 导出不动产登记台账(云南玉溪)
	 * @param mapCondition
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> GetStandingBookYuxi(Map<String, String> mapCondition);

	public Message getSFTJ(String qsj, String zsj, String sfry, String sfbmmc);

	@SuppressWarnings("rawtypes")
	public List<Map> getSFRYList();

	@SuppressWarnings("rawtypes")
	public List<Map> getSLRYList();

	public List<HashMap<String,Object>> getSFDYList(String sfbmmc);

	public Message getSFMXTJ(String qsj, String zsj, String slry,
			String sfbmmc, String sflx, String sfry,String sfdl);

	@SuppressWarnings("rawtypes")
	public List<Map> getSFBMMCList();

	@SuppressWarnings("rawtypes")
	public List<Map> getDepartmentlistList();
	
	@SuppressWarnings("rawtypes")
	public List<Map> getRoleList();

	@SuppressWarnings("rawtypes")
	public List<Map> getUserList(HttpServletRequest request);

	public Message getBJMXTJ(String qsj, String zsj, String departmentid,
			String userid, String isdyinfo);
	
	public Message getCertificateStatistics(String SZSJ_Q, String SZSJ_Z,String SZRY,String SearchStates);
	public Message getIssueStatistics(String FZSJ_Q, String FZSJ_Z,String SearchStates);
	
	/**
	 * 不动产登记台账导出liangc
	 * @return
	 */
	/*public  Message GetDJTZInfo(Map<String, String> mapCondition,
			int currentpage, int pageSize) throws Exception;*/

}

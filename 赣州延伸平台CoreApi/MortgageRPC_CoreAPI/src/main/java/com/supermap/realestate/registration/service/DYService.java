package com.supermap.realestate.registration.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.DCS_DCXM;
import com.supermap.realestate.registration.model.DCS_H_GZ;
import com.supermap.realestate.registration.model.DCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_SLLM_GZ;
import com.supermap.realestate.registration.model.DCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_ZH_GZ;
import com.supermap.realestate.registration.model.DCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:单元service 不动产单元相关操作都放到里边
 * @author diaoliwei
 * @date 2015年6月12日 下午12:13:29
 * @Copyright SuperMap
 */
public interface DYService {

	/**
	 * 分页查询调查库林地
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午4:47:40
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @param ld
	 * @return
	 */
	public Message QueryDCKld(String xmbh, int page, int rows, DCS_SLLM_GZ ld);

	/**
	 * 根据不动产单元ID获取森林林木信息
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午5:00:56
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_SLLM_GZ GetSHYQLDInfo(String bdcdyid);

	/**
	 * 查询调查库使用权宗地
	 * 
	 * @param page
	 *            当前页数
	 * @param rows
	 *            每页行数
	 * @param xmbh
	 *            项目编号
	 * @param syqzd
	 *            使用权宗地
	 * @return
	 */
	public Message QueryDCKSHYQzd(String xmbh, int page, int rows, DCS_SHYQZD_GZ shyqzd);

	/**
	 * 查询调查库所有权宗地
	 * 
	 * @param page
	 *            当前页数
	 * @param rows
	 *            每页行数
	 * @param xmbh
	 *            项目编号
	 * @param syqzd
	 *            所有权宗地
	 * @return
	 */
	public Message QueryDCKSYQzd(String xmbh, int page, int rows, DCS_SYQZD_GZ syqzd);

	/**
	 * 查询调查库宗海
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午4:35:02
	 * @param xmbh
	 *            项目编号
	 * @param page
	 *            当前页数
	 * @param rows
	 *            每行行数
	 * @param zh
	 *            宗海
	 * @return
	 */
	public Message QueryDCKZH(String xmbh, int page, int rows, DCS_ZH_GZ zh);

	/**
	 * 通过不动产单元ID获取用海坐标
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午6:20:31
	 * @param bdcdyid
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page GetPagedYHYDZB(String bdcdyid, Integer page, Integer rows);

	/**
	 * 通过不动产单元ID获取用海状况
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午6:19:35
	 * @param bdcdyid
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page GetPagedYHZK(String bdcdyid, Integer page, Integer rows);

	/**
	 * 查询调查库房屋
	 * 
	 * @param xmbh
	 *            项目编号
	 * @param page
	 *            当前页数
	 * @param rows
	 *            每页行数
	 * @param h
	 *            房屋对象
	 * @return
	 */
	public Message QueryDCKfws(String xmbh, int page, int rows, DCS_H_GZ h);

	/**
	 * 查询调查库自然幢
	 * 
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @param zrz
	 * @return
	 */
	public Message QueryDCKzrzs(String xmbh, int page, int rows, DCS_ZRZ_GZ zrz);

	/**
	 * 从调查库选中不动产单元
	 * 
	 * @param xmbh
	 *            项目编号
	 * @param bdcdyid
	 *            不动产单元ID
	 * @author diaoliwei
	 * @return
	 */
	public ResultMessage addBDCDY(String xmbh, String bdcdyid);
	
	public ResultMessage addBDCDYNoCheck(String xmbh, String bdcdyid);

	/**
	 * 获取项目不动产单元列表
	 * 
	 * @param list
	 *            登记单元list
	 * @param xmbh
	 *            项目编号
	 * @author diaoliwei
	 * @return
	 */
	public List<UnitTree> getDJDYS(String xmbh);

	/**
	 * 移除不动产单元
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月12日下午8:21:34
	 * @param xmbh
	 * @param bdcdyid
	 */
	public boolean removeBDCDY(String xmbh, String bdcdyid);

	/**
	 * 根据项目编号和不动产单元ID获取房屋信息
	 * 
	 * @param xmbh
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_H_GZ GetFwInfo(String bdcdyid);

	/**
	 * 获取调查项目
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public List<DCS_DCXM> getPagedDcxm(int pageIndex, int pageSize, Map<String, Object> mapCondition, String bdcdylx);

	/**
	 * 获取匹配调查项目的个数
	 * 
	 * @param dcxmid
	 * @return
	 */
	public int getXMXXCountById(String dcxmid);

	/**
	 * 更新调查项目
	 * 
	 * @param dcxmid
	 * @return
	 */
	public ResultMessage updateXmxx(String xmbh, String dcxmid);

	/**
	 * 根据不动产单元ID获取自然幢信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午4:37:45
	 * @return
	 */
	public BDCS_ZRZ_GZ GetZRZInfo(String bdcdyid);

	/**
	 * 根据层ID获取层信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午4:40:38
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_C_GZ GetCInfo(String cid);

	/**
	 * 根据不动产单元ID获取使用权宗地信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午4:42:11
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_SHYQZD_GZ GetSHYQZDInfo(String bdcdyid);

	/**
	 * 根据不动产单元ID获取所有权宗地信息
	 * @作者 海豹
	 * @创建时间 2015年6月23日下午6:27:12
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_SYQZD_GZ GetSYQZDInfo(String bdcdyid);

	/**
	 * 根据不动产单元ID获取宗海信息
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午3:09:38
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_ZH_GZ GetZHInfo(String bdcdyid);

	/**
	 * 获取(现状)使用权宗地信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午5:52:47
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	public BDCS_SHYQZD_XZ GetXZSHYQZDInfo(String bdcdyid);

	/**
	 * 获取(现状)自然幢信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午6:00:52
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	public BDCS_ZRZ_XZ GetXZZRZInfo(String bdcdyid);

	/**
	 * 获取(现状)层信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午6:05:45
	 * @param cid
	 *            层id
	 * @return
	 */
	public BDCS_C_XZ GetXZCInfo(String cid);

	/**
	 * 获取(现状)房屋信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午8:10:45
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	public BDCS_H_XZ GetXZFwInfo(String bdcdyid);

	/**
	 * 获取（现状）预测房屋信息
	 * @作者 海豹
	 * @创建时间 2015年7月10日下午8:17:28
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_H_XZY GetXzyFwInfo(String bdcdyid);

	/**
	 * 获取转移房屋信息
	 * @作者 rongxianfeng
	 * @param page
	 * @param rows
	 * @param StrZL
	 *            坐落
	 * @param StrQLR
	 *            权利人
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param StrBDCDYH
	 *            房屋编号
	 * @return
	 */
	public Message GetXZZY_fw_Info(int page, int rows, String StrZL, String StrQLR, String Strbdcqzh, String Strbdcdyh, String Strbdcfwbh);

	/**
	 * 初始登记中一般抵押权（最高额抵押权）的在建工程抵押信息的获取按户登记信息
	 * @作者 海豹
	 * @创建时间 2015年7月9日下午10:20:33
	 * @param page
	 *            当前页码
	 * @param rows
	 *            当前页的行数
	 * @param StrZL
	 *            坐落
	 * @param StrQLR
	 *            权利人
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param Strbdcdyh
	 *            不动产权单员号
	 * @param Strbdcfwbh
	 *            不动产房屋编码
	 * @return
	 */
	public Message GetXZ_Hy_Info(int page, int rows, String StrZL, String StrQLR, String Strbdcqzh, String Strbdcdyh, String Strbdcfwbh);

	/**
	 * 获取转移宗地信息
	 * @作者 rongxianfeng
	 * @param page
	 * @param rows
	 * @param StrZL
	 *            坐落
	 * @param StrQLR
	 *            权利人
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param StrBDCDYH
	 *            不动产单元号
	 * @param Strbdczddm
	 *            不动产宗地代码
	 * @return
	 */
	public Message GetXZZY_zd_Info(int page, int rows, String StrZL, String StrQLR, String Strbdcqzh, String StrBDCDYH, String Strbdczddm);

	/**
	 * 分页查询抵押权信息 wuzhu
	 * @param strbdcdyh
	 * @return
	 * @throws Exception
	 */
	public Message GetDYQInfo(String bdcdylx, Map<String, String> conditionParameter, int page, int rows) throws Exception;

	/**
	 * 分页查询注销登记信息 wuzhu
	 * @param strbdcdyh
	 * @return
	 * @throws Exception
	 */
	public Message GetZXDJInfo(String bdcdylx, Map<String, String> conditionParameter, int page, int rows) throws Exception;

	/**
	 * 分页查询期房单元
	 * @作者 李桐
	 * @创建时间 2015年6月15日下午4:45:36
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedPreUnit(int pageIndex, int pageSize, Map<String, String> mapCondition, String hqlCondition);

	/**
	 * 分页查询现状自然幢信息
	 * @作者 diaoliwei
	 * @创建时间 2015年6月23日上午11:35:59
	 * @param page
	 * @param rows
	 * @param zrz
	 * @return
	 */
	public Message QueryZrzs(int page, int rows, BDCS_ZRZ_XZ zrz);

	/**
	 * 检测期房单元状态
	 * @作者 李想
	 * @创建时间 2015年6月27日上午19:35:59
	 * @param page
	 * @param rows
	 * @param zrz
	 * @return
	 */

	/**
	 * 根据项目编号，不动产单元ID更新不动产单元信息
	 * @作者：俞学斌
	 * @创建时间 2015年6月26日 上午11:34:24
	 * @param xmbh
	 * @param bdcdyid
	 * @param object
	 * @return
	 */
	public ResultMessage GetPreUnitState(String xmbh, String bdcdyid);

	/**
	 * 根据项目编号，不动产单元ID更新不动产单元信息
	 * @作者 俞学斌
	 * @创建时间 2015年6月26日 上午11:34:24
	 * @param xmbh
	 * @param bdcdyid
	 * @param object
	 * @return
	 */
	public ResultMessage UpdateDYInfo(String xmbh, String bdcdyid, JSONObject object);

	/**
	 * 读取合同
	 * @作者 diaoliwei
	 * @创建时间 2015年7月20日上午12:03:08
	 * @param htbh
	 *            合同编号
	 * @param bmbm
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public ResultMessage readContract(String htbh, String bmbm, String xmbh) throws IOException, ParseException;

	/**
	 * 保存户信息
	 * @author diaoliwei
	 * @date 2015-8-6
	 * @param bdcdylx
	 * @param ly
	 * @param dataString
	 * @return
	 */
	public ResultMessage updateHouse(BDCDYLX bdcdylx, String ly, String dataString, String bdcdyid);

	
	public ResultMessage addQZGHXZ(String bdcdyid,String xmbh);
	
	
	public ResultMessage qxQZGHXZ(String bdcdyid,String xmbh);
	
	public boolean BackBDCDYInfo(String xmbh,BDCDYLX dylx,DJDYLY dyly,String bdcdyid);

	@SuppressWarnings("rawtypes")
	public ResultMessage UpdateBDCDYInfo(Map map,BDCDYLX dylx,DJDYLY dyly,String bdcdyid);
	
	public ResultMessage getHouseBDCDYH(String zrzbdcdyid,String bdcdylx);
	
	@SuppressWarnings("rawtypes")
	public ResultMessage AddBDCDYInfo(Map map);
	/**
	 * 批量更新户信息
	 * @作者 海豹
	 * @创建时间 2015年9月21日上午1:47:52
	 * @param bdcdyids
	 * @param houseinfo
	 * @return
	 */
	public ResultMessage plupdatehouseinfo(Object[] bdcdyids,
			JSONObject houseinfo);
	
	/**
	 * 检查是否可受理
	 * @Title: checkAcceptable 
	 * @author:liushufeng
	 * @date：2016年1月5日 下午8:48:05
	 * @param xmbh
	 * @param bdcdyid
	 * @return
	 */
	public ResultMessage checkAcceptable(String xmbh, String bdcdyid);

	
	/**
	 * 获取抵押变更项目不动产单元列表
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 */
	public Message GetDYBGDJDYS(String xmbh,String type,Integer page,Integer rows);
	
	/**
	 * BG027权利页面单元过滤查询
	 * @author weilb
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return json
	 */
	public Message dyFilterQuery(String xmbh, String bdcqzh,String  zl);
	
	/**
	 * 抵押变更添加一个登记单元（URL:"/{xmbh}/djdys/{bdcdyid}",Method：POST）
	 * 
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 */
	public ResultMessage addBDCDY_DYBG(String xmbh, String bdcdyid);
	
	/**
	 * 获取发证单元列表信息（URL:"/{xmbh}/fzdys/",Method：GET）
	 * 
	 * @param xmbh
	 * @author yuxuebin
	 * @return
	 */
	public Message GetFzDys(String xmbh,String qllx);

	/**
	 * 更正发证单元分组标识（URL:"/{xmbh}/fzdys/",Method：POST）
	 * 
	 * @param xmbh
	 * @param m
	 * @author yuxuebin
	 * @return
	 */
	public ResultMessage UpdateFzDys(String xmbh, HashMap<String, Integer> m,String qllx);
	
	/**
	 * 获取对应自然幢（实测\预测）中的地上层数及地下层数
	 * @作者 海豹
	 * @创建时间 2016年3月28日下午3:18:10
	 * @param xmbh，为了获取想对应的流程
	 * @param djdyly,为了确定单元来源（GZ,XZ）
	 * @param bdcdylx，为了确定单元对应的自然幢信息
	 * @param unit，设置地下层数及地上层数
	 * @return
	 */
	public RealUnit  getZrzUnit(String xmbh,String djdyly, String  bdcdylx, RealUnit unit);
	
	public void UpdateZrzUnit(Map map,String xmbh,DJDYLY ly, BDCDYLX  lx,String bdcdyid);

	/**
	 * 更新户的不动产单元号信息
	 * @param xmbh
	 */
	void updateHouse(String xmbh);
}

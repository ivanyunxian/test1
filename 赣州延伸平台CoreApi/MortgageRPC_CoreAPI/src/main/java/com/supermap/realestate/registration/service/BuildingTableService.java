package com.supermap.realestate.registration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.realestate.registration.ViewClass.BuildingTalbe;
import com.supermap.realestate.registration.ViewClass.BuildingTalbe.BuildingInfo;
import com.supermap.wisdombusiness.web.Message;

/**
 * 楼盘表服务接口
 * @author 海豹
 *
 */
public interface BuildingTableService {

	/**
	 * 根据户条件查询楼盘表信息
	 * @作者 海豹
	 * @创建时间 2016年2月29日上午10:44:26
	 * @param bdcdyid,户不动产单元ID
	 * @param bdcdylx,不动产单元类型(H：031，YCH:032)
	 * @param ly,来源(调查库：01，现状层：02)
	 */
	public BuildingTalbe queryBuildingTableByHouseCond(String bdcdyid,String bdcdylx,String ly);
	/**
	 * 根据自然幢条件查询楼盘表信息
	 * @作者 海豹
	 * @创建时间 2016年2月29日上午10:48:01
	 * @param bdcdyid,自然幢不动产单元ID
	 * @param bdcdylx,不动产单元类型(ZRZ：03，YCZRZ:08)
	 * @param ly,来源(调查库：01，现状层：02)
	 */
    public BuildingTalbe queryBuildingTableByBuildingCond(String bdcdyid,String bdcdylx,String ly);
    /**
     * 根据户条件查询幢基本信息
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:04:42
     * @param bdcdyid,户不动产单元ID
     * @param bdcdylx,不动产单元类型(H：031，YCH:032)
     * @param ly,来源(调查库：01，现状层：02)
     */
    public BuildingInfo  queryBuildingByHouseCond(String bdcdyid,String bdcdylx,String ly);
    /**
     * 根据幢查询幢基本信息
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:06:57
     * @param bdcdyid,自然幢不动产单元ID
     * @param bdcdylx,不动产单元类型(ZRZ：03，YCZRZ:08)
     * @param ly,来源(调查库：01，现状层：02)
     */
    public BuildingInfo queryBuildingByBuildingCond(String bdcdyid,String bdcdylx,String ly);
    /**
     * 根据户条件查询期现房关系表
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:09:52
     * @param bdcdyid,户不动产单元ID
     * @param bdcdylx,不动产单元类型(H：031，YCH:032)
     * @param ly,来源(调查库：01，现状层：02)
     */
    public void queryYsgxByHouseCond(String bdcdyid,String bdcdylx,String ly);
    /**
     * 根据自然幢条件查询期现房关系表
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:12:30
     * @param bdcdyid,自然幢不动产单元ID
     * @param bdcdylx,不动产单元类型(ZRZ：03，YCZRZ:08)
     * @param ly,来源(调查库：01，现状层：02)
     */
    public void queryYsgxByBuidingCond(String bdcdyid,String bdcdylx,String ly);
	public BuildingTalbe queryBuildingTableByBuildingCond_new(
			String zrzbdcdyid, String bdcdylx, String ly, String szc, String hbdcdyid);
	public BuildingTalbe queryBuildingTableByHouseCond_new(String bdcdyid,
			String bdcdylx, String ly, String szc, String hbdcdyid, boolean ljz);
	
	/** 获取勾选单元的权利
	 * @param prodef_id
	 * @param ids
	 * @return
	 */
	public Message getRightsInfo(String prodef_id, String ids);
	
	/** 楼盘表受理
	 * @param bdcdyids
	 * @param qlids
	 * @param prodef_id
	 * @param request
	 * @param response
	 * @return
	 */
	public HashMap<String,Object> buildingAccept(String bdcdyids, String qlids, String prodef_id, HttpServletRequest request, HttpServletResponse response);
	
	/** 警告状态下继续受理
	 * @param prodef_id
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	public HashMap<String, Object> AcceptProjectByBaseWorkflowName(String prodef_id, String id, HttpServletRequest request, HttpServletResponse response);
}

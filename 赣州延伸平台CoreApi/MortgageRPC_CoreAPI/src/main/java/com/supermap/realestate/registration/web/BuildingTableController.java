package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.supermap.realestate.registration.ViewClass.BuildingTalbe;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.model.BDCS_TABLE_COLOR;
import com.supermap.realestate.registration.model.BDCS_TABLE_DEFINE;
import com.supermap.realestate.registration.service.BuildingTableService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 楼盘表
 * @author 海豹
 *
 */
@Controller
@RequestMapping("/buildingtable")
public class BuildingTableController {
	@Autowired
	private BuildingTableService buildingtableservice;
	@Autowired
    private CommonDao dao;
	/**
	 * 通过选择流程及房屋信息获取楼盘表
	 * @作者 海豹
	 * @创建时间 2016年3月3日上午9:12:56
	 * @param project_id，流程ID
	 * @param bdcdyid,户的不动产单元ID
	 * @param request
	 * @param response
	 * @return,楼盘表
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "{project_id}/{bdcdyid}/buildingtablebyhc", method = RequestMethod.GET)

	public @ResponseBody BuildingTalbe buildingtablebyhc(@PathVariable("project_id") String project_id,@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		            // 选择器配置
					SelectorConfig config = HandlerFactory.getSelectorByProjectID(project_id);
					// 不动产单元类型
					String bdcdylx=config.getBdcdylx();
					// 来源
					String ly=config.getLy();
					BuildingTalbe bt=buildingtableservice.queryBuildingTableByHouseCond(bdcdyid, bdcdylx, ly);
				    return bt;
	}
	/**
	 *  通过选择流程及自然幢信息获取楼盘表
	 * @作者 海豹
	 * @创建时间 2016年3月3日上午9:16:39
	 * @param project_id,流程编码
	 * @param zrzbdcdyid,自然幢不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "{project_id}/{zrzbdcdyid}/buildingtablebybc", method = RequestMethod.GET)
	public @ResponseBody BuildingTalbe buildingtablebybc(@PathVariable("project_id") String project_id,@PathVariable("zrzbdcdyid") String zrzbdcdyid,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		            // 选择器配置
					SelectorConfig config = HandlerFactory.getSelectorByProjectID(project_id);
					if(!StringHelper.isEmpty(config))
					{
						// 不动产单元类型
						String bdcdylx=config.getBdcdylx();
						// 来源
						String ly=config.getLy();
						BuildingTalbe bt=	buildingtableservice.queryBuildingTableByBuildingCond(zrzbdcdyid, bdcdylx, ly);
					    return bt;
					}
					else {
						return new BuildingTalbe();
					}
					
	}
	/**
	 * 通过自然幢信息获取楼盘表
	 * @作者 海豹
	 * @创建时间 2016年3月4日下午3:43:17
	 * @param zrzbdcdyid
	 * @param bdcdylx
	 * @param ly
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="{zrzbdcdyid}/{bdcdylx}/{ly}/buildingtablebyzrz",method=RequestMethod.GET)
	public @ResponseBody BuildingTalbe buildingtablebyzrz(@PathVariable("zrzbdcdyid") String zrzbdcdyid,@PathVariable("bdcdylx") String bdcdylx,@PathVariable("ly") String ly ,HttpServletRequest request,HttpServletResponse response)
	{
		return buildingtableservice.queryBuildingTableByBuildingCond(zrzbdcdyid, bdcdylx, ly);
	}
	
	@RequestMapping(value="{zrzbdcdyid}/{bdcdylx}/{ly}/buildingtablebyzrz/{szc}/{hbdcdyid}",method=RequestMethod.GET)
	public @ResponseBody BuildingTalbe buildingtablebyzrz_new(@PathVariable("zrzbdcdyid") String zrzbdcdyid,@PathVariable("bdcdylx") String bdcdylx,@PathVariable("ly") String ly ,@PathVariable("szc") String szc ,@PathVariable("hbdcdyid") String hbdcdyid ,HttpServletRequest request,HttpServletResponse response)
	{
		return buildingtableservice.queryBuildingTableByBuildingCond_new(zrzbdcdyid, bdcdylx, ly,szc,hbdcdyid);
	}
	/**
	 * 通过房屋信息获取楼盘表
	 * @作者 海豹
	 * @创建时间 2016年3月4日下午3:43:22
	 * @param bdcdyid
	 * @param bdcdylx
	 * @param ly
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="{bdcdyid}/{bdcdylx}/{ly}/buildingtablebyh",method=RequestMethod.GET)
	public @ResponseBody BuildingTalbe buildingtablebyh(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("bdcdylx") String bdcdylx,@PathVariable("ly") String ly ,HttpServletRequest request,HttpServletResponse response)
	{
		return buildingtableservice.queryBuildingTableByHouseCond(bdcdyid, bdcdylx, ly);
	}
	@RequestMapping(value="{bdcdyid}/{bdcdylx}/{ly}/buildingtablebyh/{szc}/{hbdcdyid}/{ljz}",method=RequestMethod.GET)
	public @ResponseBody BuildingTalbe buildingtablebyh_new(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("bdcdylx") String bdcdylx,@PathVariable("ly") String ly ,@PathVariable("szc") String szc ,@PathVariable("hbdcdyid") String hbdcdyid ,@PathVariable("ljz") boolean ljz, HttpServletRequest request,HttpServletResponse response)
	{
		return buildingtableservice.queryBuildingTableByHouseCond_new(bdcdyid, bdcdylx, ly,szc,hbdcdyid,ljz);
	}

	private final String prefix = "/realestate/registration/";
	
	@RequestMapping(value = "/TableBuilding/")
	public String getTableBuildingConfig() {
		return prefix + "config/TableBuildingConfig";
	}
	
	@RequestMapping(value = "/ShowZSInfo/")
	public String ShowZSInfo() {
		return prefix + "common/ShowZSInfo";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/ZSlist/{bdcdyid}/{bdcdylx}",method=RequestMethod.GET)
	public @ResponseBody List<Map> zslist(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("bdcdylx") String bdcdylx,HttpServletRequest request,HttpServletResponse response)
	{
		String fulSql = "SELECT XM.XMBH, QDZR.ZSID, XM.PROJECT_ID, QL.DJLX, QL.QLLX FROM BDCK.BDCS_QL_GZ QL " + 
				" INNER JOIN BDCK.BDCS_XMXX XM ON QL.XMBH = XM.XMBH AND XM.SFDB<>'1' " + 
				" INNER JOIN BDCK.BDCS_QDZR_GZ QDZR ON QDZR.DJDYID = QL.DJDYID AND QDZR.QLID = QL.QLID AND QDZR.XMBH = XM.XMBH " + 
				" INNER JOIN BDCK.BDCS_DJDY_GZ DY ON DY.XMBH = XM.XMBH AND QL.DJDYID = DY.DJDYID " + 
				" WHERE QL.BDCDYID = '"+bdcdyid+"' AND DY.BDCDYLX = '"+bdcdylx+"' ";
		List<Map> list = dao.getDataListByFullSql(fulSql);
		return list;
	}
	
	@RequestMapping(value = "/loadColorConfig/", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_TABLE_COLOR> loadColorConfig(HttpServletRequest request, HttpServletResponse response) {
		List<BDCS_TABLE_COLOR> colors = dao.getDataList(BDCS_TABLE_COLOR.class, " 1>0 ORDER BY TO_NUMBER(GROUPINDEX)");
		BuildingTableConfig.clear();
		return colors;
	}
	@RequestMapping(value = "/loadColorConfig/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateColor(HttpServletRequest request, HttpServletResponse response) {
		String row = "";
		ResultMessage msg = new ResultMessage();
		msg.setMsg("更新失败！");
		msg.setSuccess("false");
		try {
			row = RequestHelper.getParam(request, "row");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return msg;
		}
		if(StringHelper.isEmpty(row))
			return msg;
		List<BDCS_TABLE_COLOR> arr = new ArrayList<BDCS_TABLE_COLOR>();
		arr = JSONArray.parseArray(row, BDCS_TABLE_COLOR.class);
		for (BDCS_TABLE_COLOR bdcs_TABLE_COLOR : arr) {
			dao.update(bdcs_TABLE_COLOR);
		}
		msg.setMsg("更新成功！");
		msg.setSuccess("true");
		return msg;
	}
	@RequestMapping(value = "/loadColorConfig/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage DelColor(HttpServletRequest request, HttpServletResponse response) {
		String row = "";
		ResultMessage msg = new ResultMessage();
		msg.setMsg("删除失败！");
		msg.setSuccess("false");
		try {
			row = RequestHelper.getParam(request, "row");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return msg;
		}
		if(StringHelper.isEmpty(row))
			return msg;
		List<BDCS_TABLE_COLOR> arr = new ArrayList<BDCS_TABLE_COLOR>();
		arr = JSONArray.parseArray(row, BDCS_TABLE_COLOR.class);
		for (BDCS_TABLE_COLOR bdcs_TABLE_COLOR : arr) {
			dao.deleteEntity(bdcs_TABLE_COLOR);
		}
		msg.setMsg("删除成功！");
		msg.setSuccess("true");
		return msg;
	}
	@RequestMapping(value = "/loadColorConfig/", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage AddColor(HttpServletRequest request, HttpServletResponse response) {
		String row = "";
		ResultMessage msg = new ResultMessage();
		msg.setMsg("添加失败！");
		msg.setSuccess("false");
		try {
			row = RequestHelper.getParam(request, "row");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return msg;
		}
		if(StringHelper.isEmpty(row))
			return msg;
		List<BDCS_TABLE_COLOR> arr = new ArrayList<BDCS_TABLE_COLOR>();
		arr = JSONArray.parseArray(row, BDCS_TABLE_COLOR.class);
		for (BDCS_TABLE_COLOR bdcs_TABLE_COLOR : arr) {
			dao.save(bdcs_TABLE_COLOR);
		}
		msg.setMsg("添加成功！");
		msg.setSuccess("true");
		return msg;
	}
	
	@RequestMapping(value = "/loadStatusConfig/", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_TABLE_DEFINE> loadStatusConfig(HttpServletRequest request, HttpServletResponse response) {
		List<BDCS_TABLE_DEFINE> status = dao.getDataList(BDCS_TABLE_DEFINE.class, " 1>0 ORDER BY STATUS");
		return status;
	}
	@RequestMapping(value = "/loadStatusConfig/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateStatusGroup(HttpServletRequest request, HttpServletResponse response) {
		String row = "";
		ResultMessage msg = new ResultMessage();
		msg.setMsg("更新失败！");
		msg.setSuccess("false");
		try {
			row = RequestHelper.getParam(request, "row");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return msg;
		}
		if(StringHelper.isEmpty(row))
			return msg;
		BDCS_TABLE_DEFINE obj = JSONArray.parseObject(row, BDCS_TABLE_DEFINE.class);
		dao.update(obj);
		msg.setMsg("更新成功！");
		msg.setSuccess("true");
		return msg;
	}
	
	private static final List<node> BuildingTableConfig = new ArrayList<BuildingTableController.node>();
	
	public static class node{
		Object GROUPNAME;
		Object GROUPCOLOR;
		List<Object> FLAG = new ArrayList<Object>();
		public Object getGROUPNAME() {
			return GROUPNAME;
		}
		public void setGROUPNAME(Object gROUPNAME) {
			GROUPNAME = gROUPNAME;
		}
		public Object getGROUPCOLOR() {
			return GROUPCOLOR;
		}
		public void setGROUPCOLOR(Object gROUPCOLOR) {
			GROUPCOLOR = gROUPCOLOR;
		}
		public List<Object> getFLAG() {
			return FLAG;
		}
		public void setFLAG(List<Object> fLAG) {
			FLAG = fLAG;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/BuildingTableConfig",method=RequestMethod.GET)
	public @ResponseBody List<node> GetBuildingTableConfig(HttpServletRequest request,HttpServletResponse response)
	{
		List<node> config = new ArrayList<BuildingTableController.node>();
		if(BuildingTableConfig.size()<1){
			String fulSql = "SELECT COLOR.GROUPNAME,DEFINE.STATUS||'-'||DEFINE.STATUSVALUE AS STATUS,COLOR.GROUPCOLOR FROM BDCK.BDCS_TABLE_COLOR COLOR LEFT JOIN BDCK.BDCS_TABLE_DEFINE DEFINE ON DEFINE.GROUPID=COLOR.ID ORDER BY COLOR.GROUPINDEX";
			List<Map> colors = dao.getDataListByFullSql(fulSql);
			String jsonStrArr = JSONArray.toJSONString(colors);
			List<node> nodes = JSONArray.parseArray(jsonStrArr, node.class);
			for (int i = 0; i < nodes.size(); i++)  //外循环是循环的次数
            {
                for (int j = nodes.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
                {
                    if (nodes.get(i).GROUPNAME.equals(nodes.get(j).GROUPNAME))
                    {
                    	nodes.remove(j);
                    }
                }
            }
			for (node node : nodes) {
				for (Map map : colors) {
					Object name = map.get("GROUPNAME");
					Object flag = map.get("STATUS");
					if(node.GROUPNAME.equals(name)){
						node.FLAG.add(flag);
					}
				}
			}
			BuildingTableConfig.addAll(nodes);
		}
		config = BuildingTableConfig;
		return config;
	}
	
	/** 根据BDCDYID获取单元信息和权利信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getqlinfo/{prodef_id}", method = RequestMethod.GET)
	public @ResponseBody Message getQlInfo(@PathVariable("prodef_id") String prodef_id,HttpServletRequest request, HttpServletResponse response) {
		String bdcdyids = request.getParameter("ids");
		Message msg = buildingtableservice.getRightsInfo(prodef_id, bdcdyids);
		return msg;
	}
	
	/** 楼盘表受理
	 * @param prodef_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/buildingaccept/{prodef_id}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> buildingAccept(@PathVariable("prodef_id") String prodef_id,HttpServletRequest request, HttpServletResponse response) {
		String bdcdyids = request.getParameter("bdcdyids");
		String qlids = request.getParameter("qlids");
		HashMap<String,Object> result = buildingtableservice.buildingAccept(bdcdyids, qlids, prodef_id, request, response);
		return result;
	}
	
	/** 警告状态下继续受理
	 * @param prodef_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/continueaccept/{prodef_id}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> continueAccept(@PathVariable("prodef_id") String prodef_id,HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids");
		HashMap<String,Object> result = buildingtableservice.AcceptProjectByBaseWorkflowName(prodef_id, ids, request, response);
		return result;
	}
}
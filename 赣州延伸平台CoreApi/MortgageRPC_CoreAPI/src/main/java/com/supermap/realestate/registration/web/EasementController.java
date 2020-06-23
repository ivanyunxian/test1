package com.supermap.realestate.registration.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.web.UserController;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:单元控制器 跟不动产单元操作相关的都放在这里边
 * @author 刘树峰
 * @date 2015年6月12日 上午11:45:12
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/easement")
public class EasementController {

	/**
	 * DYService
	 */
	@Autowired
	private DYService dyService;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao;
	private static final Log logger = LogFactory.getLog(UserController.class);

	/**
	 * 获取地役权信息
	 * @param xmbh：项目编号
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/easementinfo/{xmbh}")
	public @ResponseBody Map easementinfo(@PathVariable String xmbh){
		Map map=new HashMap<String, RealUnit>();
		map.put("xydinfo", UnitTools.newRealUnit(BDCDYLX.SHYQZD,DJDYLY.GZ));
		map.put("gydinfo", UnitTools.newRealUnit(BDCDYLX.SHYQZD, DJDYLY.GZ));
		List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, ProjectHelper.GetXMBHCondition(xmbh));
		if(djdys !=null){
			for(BDCS_DJDY_GZ djdy :djdys){
				String djdyid=djdy.getDJDYID();
				String bdcdyid=djdy.getBDCDYID();
				String bdcdylx=djdy.getBDCDYLX();
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
			if(unit == null){
				unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, bdcdyid);				
			}
			Rights rights=RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdyid);
			if(rights !=null){
				map.remove("xydinfo");
				map.put("xydinfo", unit);
			}else{
				map.remove("gydinfo");
				map.put("gydinfo", unit);
			}
			}
		}
		logger.info("获取单元信息");
		return map;
	}
	
	/**
	 * 检查该单元是否在需役地或供役地中存在
	 * @param xmbh ：项目编号
	 * @param type：类型
	 * @param bdcdyid：不动产单元id
	 * @return
	 */
	@RequestMapping(value="/checkeasementinfo/{xmbh}/{type}/{bdcdyid}")
	public @ResponseBody ResultMessage checkEasementInfo(@PathVariable String xmbh,@PathVariable String type,@PathVariable("bdcdyid") String bdcdyid){
		ResultMessage msg=new ResultMessage();
		String sql=ProjectHelper.GetXMBHCondition(xmbh);
		if(type.equals("gydtype")){
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
			if(djdys !=null && djdys.size()>0){
				if(djdys.size()==2){
					msg.setSuccess("false");
					msg.setMsg("供役地存在单元信息，请先删除供役地单元信息，再添加");
				}else{		
						Rights rights=	RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdys.get(0).getDJDYID());
						if(rights !=null){
							if(bdcdyid.equals(djdys.get(0).getBDCDYID())){
								msg.setSuccess("false");
								msg.setMsg("需役地已添加该单元，请从新选择供役地单元信息");
							}else{
								if(bdcdyid.equals(djdys.get(0).getBDCDYID())){
									msg.setSuccess("false");
									msg.setMsg("供役地存在单元信息，请先删除供役地单元信息，再添加");
								}else{
									msg.setSuccess("true");
								}
							}
						}
				}
			}else{
				msg.setSuccess("true");
			}
		}else if(type.equals("xydtype")){
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
			if(djdys !=null && djdys.size()>0){
				if(djdys.size()==2){
					msg.setSuccess("false");
					msg.setMsg("供役地存在单元信息，请先删除供役地单元信息，再添加");
				}else{
					Rights rights=	RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdys.get(0).getDJDYID());
					if(rights !=null){
						msg.setSuccess("false");
						msg.setMsg("需役地存在单元信息，请先删除需役地单元信息，再添加");
					}else{
						if(bdcdyid.equals(djdys.get(0).getBDCDYID())){
							msg.setSuccess("false");
							msg.setMsg("供役地已添加该单元，请从新选择需役地单元信息");
						}else{
							msg.setSuccess("true");
						}
					}
				}
			}else{
				msg.setSuccess("true");
			}
		}else{
			msg.setMsg("不存在这个供役地类型");
			msg.setSuccess("false");
		}
		return msg;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/easementqlinfo/{xmbh}/{qlid}")
	public @ResponseBody Map easementQLInfo(@PathVariable String xmbh,@PathVariable("qlid") String qlid){
		Map map=new HashMap<String, Object>();
		Rights rights=RightsTools.loadRights(DJDYLY.GZ, qlid);
		if(rights ==null){
			rights=new BDCS_QL_GZ();
		}
		map.put("ql", rights);
		SubRights subrights=RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, qlid);
		if(subrights ==null){
			subrights=new BDCS_FSQL_GZ();
		}
		map.put("fsql", subrights);
		return map;
	}
}
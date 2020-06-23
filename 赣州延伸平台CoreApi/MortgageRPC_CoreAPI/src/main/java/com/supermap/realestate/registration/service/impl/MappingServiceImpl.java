package com.supermap.realestate.registration.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.service.MappingService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.service.common.Common;

@Service("mappingService")
public class MappingServiceImpl implements MappingService {

	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 获取流程信息以及处理流程信息及工作流信息
	 * 
	 * @作者 mss
	 * @创建时间
	 * @param
	 * @param id
	 * @return
	 * @return
	 */
	@Override
	public Map<String, Object> GetLcxx(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Wfd_Prodef processData = null;
		processData = baseCommonDao.get(Wfd_Prodef.class, id);
		map.put("Prodef_Name", processData.getProdef_Name());
		map.put("Prodef_Code", processData.getProdef_Code());
		map.put("Prodef_Id",processData.getProdef_Id());
		if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
			List<WFD_MAPPING> listMap=baseCommonDao.getDataList(WFD_MAPPING.class, "workflowcode='"+processData.getProdef_Code()+"'");
			if(listMap!=null&&listMap.size()>0){
				if(!StringHelper.isEmpty(listMap.get(0).getWORKFLOWNAME())){
					map.put("workflowname", listMap.get(0).getWORKFLOWNAME());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getWORKFLOWCAPTION())){
					map.put("workflowcaption", listMap.get(0).getWORKFLOWCAPTION());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getHOUSEEDIT())){
					map.put("houseedit", listMap.get(0).getHOUSEEDIT());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getLANDEDIT())){
					map.put("landedit", listMap.get(0).getLANDEDIT());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getNEWQZH())){
					map.put("newqzh", listMap.get(0).getNEWQZH());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getSHOWDATAREPORTBTN())){
					map.put("showdatareportbtn", listMap.get(0).getSHOWDATAREPORTBTN());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getSHOWBUILDINGTABLE()))
				{
					map.put("showbuildingtable", listMap.get(0).getSHOWBUILDINGTABLE());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getDYFS()))
				{
					map.put("dyfs", listMap.get(0).getDYFS());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getCZFS()))
				{
					map.put("czfs", listMap.get(0).getCZFS());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getSFHBZS()))
				{
					map.put("sfhbzs", listMap.get(0).getSFHBZS());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getUNITPAGEID()))
				{
					map.put("unitpageid", listMap.get(0).getUNITPAGEID());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getRIGHTPAGEID()))
				{
					map.put("rightpageid", listMap.get(0).getRIGHTPAGEID());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getSFYCXZ()))
				{
					map.put("sfycxz", listMap.get(0).getSFYCXZ());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getYCXZLX()))
				{
					map.put("ycxzlx", listMap.get(0).getYCXZLX());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getSFADDPZMJ()))
				{
					map.put("sfaddpzmj", listMap.get(0).getSFADDPZMJ());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getDATASTYLE()))
				{
					map.put("datastyle", listMap.get(0).getDATASTYLE());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getSFJCCQDQLR()))
				{
					map.put("sfjccqdqlr", listMap.get(0).getSFJCCQDQLR());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getISREMOVESEAL()))
				{
					map.put("isremoveseal", listMap.get(0).getISREMOVESEAL());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getISUNLOCKYCHDY()))
				{
					map.put("isunlockychdy", listMap.get(0).getISUNLOCKYCHDY());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getDELTDQL()))
				{
					map.put("deltdql", listMap.get(0).getDELTDQL());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getISINITATATUS()))
				{
					map.put("isinitatatus", listMap.get(0).getISINITATATUS());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getCERTMODE()))
				{
					map.put("certmode", listMap.get(0).getCERTMODE());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getSPBSJR()))
				{
					map.put("spbsjr", listMap.get(0).getSPBSJR());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getISEXTENDMORTGAGEINFO()))
				{
					map.put("isextendmortgageinfo", listMap.get(0).getISEXTENDMORTGAGEINFO());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getDELYCDY()))
				{
					map.put("delycdy", listMap.get(0).getDELYCDY());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getREADCONTRACTBTN()))
				{
					map.put("readcontractbtn", listMap.get(0).getREADCONTRACTBTN());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getENCRYPTION()))
				{
					map.put("encryption", listMap.get(0).getENCRYPTION());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getPUSHTOZJK()))
				{
					map.put("pushtozjk", listMap.get(0).getPUSHTOZJK());
				}

				if(!StringHelper.isEmpty(listMap.get(0).getDEFAULTFJ()))
				{
					map.put("defaultfj", listMap.get(0).getDEFAULTFJ());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getDEFAULTDJYY()))
				{
					map.put("defaultdjyy", listMap.get(0).getDEFAULTDJYY());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getUSESPBDHTM()))
				{
					map.put("bdcdjsqb", listMap.get(0).getUSESPBDHTM());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getUSEAPPROVALHTM()))
				{
					map.put("bdcdjspb", listMap.get(0).getUSEAPPROVALHTM());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getUSEFJHTM()))
				{
					map.put("bdcdjsqbfb", listMap.get(0).getUSEFJHTM());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getUSESQRHTM()))
				{
					map.put("bdcdjsqr", listMap.get(0).getUSESQRHTM());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getCFCONFIG()))
				{
					map.put("cfconfig", listMap.get(0).getCFCONFIG());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getISALLOWADDSQR()))
				{
					map.put("isallowaddsqr", listMap.get(0).getISALLOWADDSQR());
				}
				if(!StringHelper.isEmpty(listMap.get(0).getISCOPYYCF2CF()))
				{
					map.put("iscopyycf2cf", listMap.get(0).getISCOPYYCF2CF());
				}
				
			}
		}
		if(!map.containsKey("workflowname")){
			map.put("workflowname", "");
		}
		if(!map.containsKey("workflowcaption")){
			map.put("workflowcaption", "");
		}
		if(!map.containsKey("houseedit")){
			map.put("houseedit", "0");
		}
		if(!map.containsKey("landedit")){
			map.put("landedit", "0");
		}
		if(!map.containsKey("newqzh")){
			map.put("newqzh", "1");
		}
		if(!map.containsKey("showdatareportbtn")){
			map.put("showdatareportbtn", "0");
		}
		if(!map.containsKey("dyfs")){
			map.put("dyfs", "0");
		}
		if(!map.containsKey("czfs")){
			map.put("czfs", "0");
		}
		if(!map.containsKey("sfhbzs")){
			map.put("sfhbzs", "0");
		}
		if(!map.containsKey("sfycxz")){
			map.put("sfycxz", "0");
		}
		if(!map.containsKey("ycxzlx")){
			map.put("ycxzlx", "");
		}
		if(!map.containsKey("sfaddpzmj")){
			map.put("sfaddpzmj", "0");
		}
		if(!map.containsKey("datastyle")){
			map.put("datastyle", "0");
		}
		if(!map.containsKey("sfjccqdqlr")){
			map.put("sfjccqdqlr", "0");
		}
		if(!map.containsKey("isremoveseal")){
			map.put("isremoveseal", "0");
		}
		if(!map.containsKey("isunlockychdy")){
			map.put("isunlockychdy", "0");
		}
		if(!map.containsKey("deltdql")){
			map.put("deltdql", "0");
		}
		if(!map.containsKey("isinitatatus")){
			map.put("isinitatatus", "0");
		}
		if(!map.containsKey("certmode")){
			map.put("certmode", "0");
		}
		if(!map.containsKey("spbsjr")){
			map.put("spbsjr", "");
		}
		if(!map.containsKey("isextendmortgageinfo")){
			map.put("isextendmortgageinfo", "");
		}
		if(!map.containsKey("delycdy")){
			map.put("delycdy", "0");
		}
		if(!map.containsKey("readcontractbtn")){
			map.put("readcontractbtn", "");
		}
		if(!map.containsKey("pushtozjk")){
			map.put("pushtozjk", "");
		}
		if(!map.containsKey("encryption")){
			map.put("encryption", "");
		}
		if(!map.containsKey("defaultfj")){
			map.put("defaultfj", "");
		}
		if(!map.containsKey("defaultdjyy")){
			map.put("defaultdjyy", "");
		}
		if(!map.containsKey("bdcdjsqb")){
			map.put("bdcdjsqb", "");
		}
		if(!map.containsKey("bdcdjspb")){
			map.put("bdcdjspb", "");
		}
		if(!map.containsKey("bdcdjsqbfb")){
			map.put("bdcdjsqbfb", "");
		}
		if(!map.containsKey("bdcdjsqr")){
			map.put("bdcdjsqr", "");
		}
		if(!map.containsKey("cfconfig")){
			map.put("cfconfig", "");
		}
		if(!map.containsKey("isallowaddsqr")){
			map.put("isallowaddsqr", "");
		}
		if(!map.containsKey("iscopyycf2cf")){
			map.put("iscopyycf2cf", "");
		}
		
		return map;
	}

	/**
	 * 把form中的数据提交到数据库
	 */
	@Override
	public ResultMessage SaveOrUpdate(HttpServletRequest request) {
		ResultMessage ms = new ResultMessage();
		String workflowname = request.getParameter("workflowname");
		String workflowcaption = request.getParameter("workflowcaption");
		String houseedit = request.getParameter("houseedit");
		String landedit = request.getParameter("landedit");
		String newqzh = request.getParameter("newqzh");
		String Prodef_Code = request.getParameter("Prodef_Code");
		String showdatareportbtn = request.getParameter("showdatareportbtn");
		//添加是否通过楼盘表添加单元
		String showbuildingtable=request.getParameter("showbuildingtable");
		String dyfs = request.getParameter("dyfs");
		String czfs = request.getParameter("czfs");
		String sfhbzs = request.getParameter("sfhbzs");
		String unitpageid = request.getParameter("unitpageid");
		String rightpageid = request.getParameter("rightpageid");
		String sfycxz=request.getParameter("sfycxz");
		String ycxzlx=request.getParameter("ycxzlx");
		String sfaddpzmj=request.getParameter("sfaddpzmj");
		String datastyle=request.getParameter("datastyle");
		String sfjccqdqlr=request.getParameter("sfjccqdqlr");
		String isremoveseal=request.getParameter("isremoveseal");		
		String isunlockychdy=request.getParameter("isunlockychdy");	
		String isinitatatus=request.getParameter("isinitatatus");
		String certmode=request.getParameter("certmode");
		String delycdy=request.getParameter("delycdy");
		String deltdql=request.getParameter("deltdql");
		String spbsjr=request.getParameter("spbsjr");
		String isextendmortgageinfo=request.getParameter("isextendmortgageinfo");
		String readcontractbtn=request.getParameter("readcontractbtn");
		String pushtozjk=request.getParameter("pushtozjk");
		String encryption=request.getParameter("encryption");
		String defaultfj=request.getParameter("defaultfj");
		String defaultdjyy=request.getParameter("defaultdjyy");
		String bdcdjsqb=request.getParameter("bdcdjsqb");
		String bdcdjspb=request.getParameter("bdcdjspb");
		String bdcdjsqbfb=request.getParameter("bdcdjsqbfb");
		String bdcdjsqr=request.getParameter("bdcdjsqr");
		String cfconfig=request.getParameter("cfconfig");
		String isallowaddsqr = request.getParameter("isallowaddsqr");
		String iscopyycf2cf = request.getParameter("iscopyycf2cf");
		if(StringHelper.isEmpty(Prodef_Code)){
			ms.setMsg("请先选择流程！");
			ms.setSuccess("false");
			return ms;
		}
		List<WFD_MAPPING> list = baseCommonDao.getDataList(WFD_MAPPING.class,
				"WORKFLOWCODE='" + Prodef_Code + "'");
		/**
		 * 判断如果存在相同的workflowcode则更新
		 */
		if (list != null && list.size() > 0) {
			WFD_MAPPING oldMapping= list.get(0);
			oldMapping.setHOUSEEDIT(houseedit);
			oldMapping.setLANDEDIT(landedit);
			oldMapping.setWORKFLOWCAPTION(workflowcaption);
			oldMapping.setWORKFLOWNAME(workflowname); 
			oldMapping.setNEWQZH(newqzh);
			oldMapping.setSHOWDATAREPORTBTN(showdatareportbtn);
			oldMapping.setSHOWBUILDINGTABLE(showbuildingtable);
			oldMapping.setDYFS(dyfs);
			oldMapping.setCZFS(czfs);
			oldMapping.setSFHBZS(sfhbzs);
			oldMapping.setUNITPAGEID(unitpageid);
			oldMapping.setRIGHTPAGEID(rightpageid);
			oldMapping.setSFYCXZ(sfycxz);
			oldMapping.setYCXZLX(ycxzlx);
			oldMapping.setSFADDPZMJ(sfaddpzmj);
			oldMapping.setDATASTYLE(datastyle);
			oldMapping.setSFJCCQDQLR(sfjccqdqlr);
			oldMapping.setISREMOVESEAL(isremoveseal);
			oldMapping.setISUNLOCKYCHDY(isunlockychdy);
			oldMapping.setISINITATATUS(isinitatatus);
			oldMapping.setCERTMODE(certmode);
			oldMapping.setDELYCDY(delycdy);
			oldMapping.setDELTDQL(deltdql);
			oldMapping.setSPBSJR(spbsjr);
			oldMapping.setISEXTENDMORTGAGEINFO(isextendmortgageinfo);
			oldMapping.setREADCONTRACTBTN(readcontractbtn);
			oldMapping.setPUSHTOZJK(pushtozjk);
			oldMapping.setENCRYPTION(encryption);
			oldMapping.setDEFAULTFJ(defaultfj);
			oldMapping.setDEFAULTDJYY(defaultdjyy);
			oldMapping.setUSESPBDHTM(bdcdjsqb);
			oldMapping.setUSEAPPROVALHTM(bdcdjspb);
			oldMapping.setUSEFJHTM(bdcdjsqbfb);
			oldMapping.setUSESQRHTM(bdcdjsqr);
			oldMapping.setCFCONFIG(cfconfig);
			oldMapping.setISALLOWADDSQR(isallowaddsqr);
			oldMapping.setISCOPYYCF2CF(iscopyycf2cf);
			baseCommonDao.update(oldMapping);
			baseCommonDao.flush();
			ms.setMsg("更新成功");
			ms.setSuccess("true");
			YwLogUtil.addYwLog("本地流程配置维护-更新成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			HandlerFactory.reloadMappingConfig();
			 
		} else {
			/**
			 * 如果不存在相同的workflowcode则保存
			 */
			WFD_MAPPING mapping = new WFD_MAPPING();
			if (mapping.getId().equals("")) {
				mapping.setId(Common.CreatUUID());
			}

			mapping.setHOUSEEDIT(houseedit);
			mapping.setLANDEDIT(landedit);
			mapping.setWORKFLOWCAPTION(workflowcaption);
			mapping.setWORKFLOWCODE(Prodef_Code);
			mapping.setWORKFLOWNAME(workflowname);
			mapping.setNEWQZH(newqzh);
			mapping.setSHOWDATAREPORTBTN(showdatareportbtn);
			mapping.setSHOWBUILDINGTABLE(showbuildingtable);
			mapping.setDYFS(dyfs);
			mapping.setCZFS(czfs);
			mapping.setSFHBZS(sfhbzs);
			mapping.setUNITPAGEID(unitpageid);
			mapping.setRIGHTPAGEID(rightpageid);
			mapping.setSFYCXZ(sfycxz);
			mapping.setYCXZLX(ycxzlx);
			mapping.setSFADDPZMJ(sfaddpzmj);
			mapping.setDATASTYLE(datastyle);
			mapping.setSFJCCQDQLR(sfjccqdqlr);
			mapping.setISREMOVESEAL(isremoveseal);
			mapping.setISUNLOCKYCHDY(isunlockychdy);
			mapping.setISINITATATUS(isinitatatus);
			mapping.setCERTMODE(certmode);
			mapping.setDELYCDY(delycdy);
			mapping.setDELTDQL(deltdql);
			mapping.setSPBSJR(spbsjr);
			mapping.setISEXTENDMORTGAGEINFO(isextendmortgageinfo);
			mapping.setREADCONTRACTBTN(readcontractbtn);
			mapping.setPUSHTOZJK(pushtozjk);
			mapping.setENCRYPTION(encryption);
			mapping.setUSESPBDHTM(bdcdjsqb);
			mapping.setUSEAPPROVALHTM(bdcdjspb);
			mapping.setUSEFJHTM(bdcdjsqbfb);
			mapping.setUSESQRHTM(bdcdjsqr);
			mapping.setCFCONFIG(cfconfig);
			mapping.setISALLOWADDSQR(isallowaddsqr);
			mapping.setISCOPYYCF2CF(iscopyycf2cf);
			baseCommonDao.save(mapping);
			baseCommonDao.flush();
			ms.setMsg("添加成功");
			ms.setSuccess("true");
			YwLogUtil.addYwLog("本地流程配置维护-添加成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			HandlerFactory.reloadMappingConfig();
		}
		return ms;
	}

	@Override
	public List<RegisterWorkFlow> getWorkflows() {
		List<RegisterWorkFlow> workflowdata = HandlerFactory.getWorkflows();
		return workflowdata;
	}
	
	@Override
	public int getReadcontractbtn(String xmbh) {
		if(xmbh!=null){
			List<BDCS_XMXX> list = baseCommonDao.getDataList(BDCS_XMXX.class,"xmbh='" + xmbh + "'");
			if(list!=null&& list.size()>0){
				List<WFD_MAPPING> maplist = baseCommonDao.getDataList(WFD_MAPPING.class,"WORKFLOWCODE='" + list.get(0).getPROJECT_ID().split("-")[2] + "'");
				if(maplist!=null&&maplist.size()>0){
					int readcontractbtn=maplist.get(0).getREADCONTRACTBTN()!=null?Integer.valueOf(maplist.get(0).getREADCONTRACTBTN()):0;
					return readcontractbtn;
				}
			}
		}
		return 0;
	}
	/**
	 *  获取业务流程配置的“是否允许使用申请人新增按钮”字段的值
	 */
	public int getIsAllowAddSqr(String xmbh){
		if(xmbh!=null){
			List<BDCS_XMXX> list = baseCommonDao.getDataList(BDCS_XMXX.class,"xmbh='" + xmbh + "'");
			if(list!=null&& list.size()>0){
				List<WFD_MAPPING> maplist = baseCommonDao.getDataList(WFD_MAPPING.class,"WORKFLOWCODE='" + list.get(0).getPROJECT_ID().split("-")[2] + "'");
				if(maplist!=null&&maplist.size()>0){
					int isallowaddsqr=maplist.get(0).getISALLOWADDSQR()!=null?Integer.valueOf(maplist.get(0).getISALLOWADDSQR()):0;
					return isallowaddsqr;
				}
			}
		}
		return 0;
	}
	
	
	
	
	
	
	
}
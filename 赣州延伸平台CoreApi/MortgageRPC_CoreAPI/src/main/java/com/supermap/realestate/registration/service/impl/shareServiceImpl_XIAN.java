package com.supermap.realestate.registration.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJSZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.RegisterUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.shareService_XIAN;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

/**
 * 
 * @Description:西安交易共享
 * @author yuxuebin
 * @date 2016年6月25日 15:27:22
 * @Copyright SuperMap
 */
@Service("shareService_XIAN")
public class shareServiceImpl_XIAN implements shareService_XIAN {

	@Autowired
	private CommonDao baseCommonDao;
	public Logger logger = Logger.getLogger(DBServiceImpl.class);
	
	/**
	 * 项目受理
	 * @Title: accept 
	 * @author:yuxuebin
	 * @date：2016年6月29日 00:55:22
	 * @param request
	 * @param strXml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String accept(HttpServletRequest request, String strXml) {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("0000");
		ms.setMsg("成功");
		Document document = this.getDocumentFromStrXML(strXml);
		if (document == null) {
			ms.setSuccess("4006");
			ms.setMsg("报文解析错误");
		}else{
			HashMap<String, Object> map=AnalysisXML(document);
			if(!map.containsKey("InfoHEAD")||map.get("InfoHEAD")==null){
				ms.setSuccess("4005");
				ms.setMsg("头部信息错误【头部信息缺失】");
			}else{
				try {
					ms=AcceptProject(map, request);
				} catch (Exception e) {
				}
			}
		}
		
		Date currenttime=new Date();
		Element root=document.getRootElement();
		List<Element> root_elements=root.elements();
		if (root_elements != null && root_elements.size() > 0) {
			for (Element root_element : root_elements) {
				String root_name = root_element.getName();
				if ("Head".endsWith(root_name)) {
					Element head_element = root_element;
					List<Element> head_elements=head_element.elements();
					if (head_elements != null && head_elements.size() > 0) {
						for (Element headproperty_element : head_elements) {
							String headproperty_name = headproperty_element.getName();
							if ("RetCode".endsWith(headproperty_name)) {
								headproperty_element.setText("0000");
							}else if ("RetMsg".endsWith(headproperty_name)) {
								headproperty_element.setText("成功");
							}else if ("OpDate".endsWith(headproperty_name)) {
								headproperty_element.setText(StringHelper.FormatDateOnType(currenttime, "yyyyMMdd"));
							}else if ("OpTime".endsWith(headproperty_name)) {
								headproperty_element.setText(StringHelper.FormatDateOnType(currenttime, "hhmmss"));
							}
						}
					}
				}
			}
		}
		return document.asXML();
	}
	
	/**
	 * 根据xml字符串获取Document
	 * @Title: getOpcodes 
	 * @author:yuxuebin
	 * @date：2016年6月29日 01:00:23
	 * @return
	 */
	private Document getDocumentFromStrXML(String strxml){
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new ByteArrayInputStream(strxml
				     .getBytes("GBK")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}
	
	/**
	 * 根据xml信息受理项目
	 * @Title: AcceptProject 
	 * @author:yuxuebin
	 * @date：2016年6月29日 01:36:23
	 * @param projectinfo
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	private ResultMessage AcceptProject(HashMap<String, Object> projectinfo,
			HttpServletRequest request) throws Exception {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("0000");
		ms.setMsg("成功");
		HashMap<String,String> map_head=null;
		if(projectinfo.containsKey("InfoHEAD")){
			map_head=(HashMap<String,String>)projectinfo.get("InfoHEAD");
		}
		String opcode="";
		String fcywh="";
		if(map_head.containsKey("OpCode")){
			opcode=map_head.get("OpCode");
		}
		if(map_head.containsKey("CaseNum")){
			fcywh=map_head.get("CaseNum");
		}
		String prodef_id=getProdefIdFromOpCode(opcode);

		BDCS_XMXX xmxx=GetXMXX(request,fcywh,prodef_id);
		if(xmxx==null){
			ms.setSuccess("4444");
			ms.setMsg("系统错误");
			return ms;
		}
		if("10004".equals(opcode)){
			accept10004(projectinfo,xmxx);
		}else if("20004".equals(opcode)){
			accept20004(projectinfo,xmxx);
		}else if("30004".equals(opcode)){
			accept30004(projectinfo,xmxx);
		}else if("40004".equals(opcode)){
			accept40004(projectinfo,xmxx);
		}else if("10023".equals(opcode)){
			accept10023(projectinfo,xmxx);
		}else if("20023".equals(opcode)){
			accept20023(projectinfo,xmxx);
		}else if("30023".equals(opcode)){
			accept30023(projectinfo,xmxx);
		}else if("40023".equals(opcode)){
			accept40023(projectinfo,xmxx);
		}else if("70001".equals(opcode)){
			accept70001(projectinfo,xmxx);
		}else if("70002".equals(opcode)){
			accept70023(projectinfo,xmxx);
		}else if("70023".equals(opcode)){
			accept70002(projectinfo,xmxx);
		}else if("80001".equals(opcode)){
			accept80001(projectinfo,xmxx);
		}else if("80002".equals(opcode)){
			accept80002(projectinfo,xmxx);
		}
		else if("60001".equals(opcode)){
			accept60001(projectinfo,xmxx);
		}else if("60002".equals(opcode)){
			accept60002(projectinfo,xmxx);
		}
		return ms;
	}
	
	/**
	 * 根据指令码回去流程定义ID
	 * @Title: getProdefIdFromOpCode 
	 * @author:yuxuebin
	 * @date：2016年6月29日 01:49:23
	 * @param Opcode
	 * @return
	 */
	private String getProdefIdFromOpCode(String Opcode){
		String prodef_id="";
		if("10004".equals(Opcode)){
			prodef_id="64f88375406648c79c8274786ee9af04";
		}else if("20004".equals(Opcode)){
			prodef_id="89ac7fd6e0d74e538382badfd8fcb353";
		}else if("30004".equals(Opcode)){
			prodef_id="65726d51351b45fc97d845b5879ca060";
		}else if("40004".equals(Opcode)){
			prodef_id="2b2d6e68ec8c46dea55afec4f0bf54b8";
		}else if("10023".equals(Opcode)){
			prodef_id="d84a2ce3afa44155bf0a080ed481cfff";
		}else if("20023".equals(Opcode)){
			prodef_id="c51a708f6efd48969b8ef8382db6b29b";
		}else if("30023".equals(Opcode)){
			prodef_id="ea31bb6195774f50ac4c050e6dab11fc";
		}else if("40023".equals(Opcode)){
			prodef_id="69317168b8664e588ec876e27e3d4f4e";
		}else if("70001".equals(Opcode)){
			prodef_id="71f7ccc4395040ce94563c585b9772ac";
		}else if("70002".equals(Opcode)){
			prodef_id="a36dfee05c1d47e6ab4b4da5bcfade19";
		}else if("80001".equals(Opcode)){
			prodef_id="46aa91ed25d046809c910a315b78281b";
		}else if("80002".equals(Opcode)){
			prodef_id="c9c6f01245354750b7c520d03925f019";
		}
		return prodef_id;
	}
	
	/**
	 * 获取指令列表
	 * @Title: getOpcodes 
	 * @author:yuxuebin
	 * @date：2016年6月29日 01:00:22
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<String> getOpcodes(){
		List<String> opcodes=new ArrayList<String>();
		opcodes.add("10004");
		opcodes.add("20004");
		opcodes.add("30004");
		opcodes.add("40004");
		opcodes.add("10023");
		opcodes.add("20023");
		opcodes.add("30023");
		opcodes.add("40023");
		opcodes.add("60001");
		opcodes.add("60002");
		opcodes.add("70001");
		opcodes.add("70002");
		opcodes.add("80001");
		opcodes.add("80002");
		return opcodes;
	}
	
	/**
	 * 根据共享业务编号获取项目信息
	 * @Title: GetXMXX 
	 * @author:yuxuebin
	 * @date：2016年6月29日 01:51:22
	 * @param request
	 * @param casenum
	 * @param prodef_id
	 * @return
	 */
	private BDCS_XMXX GetXMXX(HttpServletRequest request,String casenum,String prodef_id) throws IOException{
		if(StringHelper.isEmpty(casenum)){
			casenum=SuperHelper.GeneratePrimaryKey();
		}
		String staffid="8599241fface464ca4e64aae4bf7f4ef";//受理用户ID
		String servername = StringHelper.formatObject(InetAddress
				.getLocalHost().getHostAddress());
		String basePath = request.getScheme() + "://" + servername + ":"
				+ request.getLocalPort() + request.getContextPath() + "/";
		String url = basePath + "app/operation/batch/acceptproject/" + prodef_id
				+ "/" + casenum + "/" + staffid;
		Map<String, String> map = new HashMap<String, String>();
		// 调用易大师创建项目方法（工作流）
		// 1、URL : /app/operation/batch/acceptproject/{prodefid}/{batch}
		// 2、请求方式 POST
		// 3、参数 prodefid 流程定义id batch 批次号
		String jsonresult = ProjectHelper.httpGet(url, map);
		JSONObject object = JSON.parseObject(jsonresult);
		// 返回值
		// id :产生流程实例iD
		String id = object.containsKey("id") ? StringHelper.formatObject(object
				.get("id")) : "";
		// desc:消息 如果受理成功是 “受理成功”
		String desc = object.containsKey("desc") ? StringHelper
				.formatObject(object.get("desc")) : "";
		if (!desc.equals("受理成功")) {
			return null;
		}
		// 刘树峰 2016.3.17 创建项目 获取xmbh
		String project_id = null;
		Wfi_ProInst proinst = baseCommonDao.get(Wfi_ProInst.class, id);
		if (proinst != null) {
			project_id = proinst.getFile_Number();
		}
		//创建项目
		if (!StringHelper.isEmpty(project_id)) {
			ProjectInfo info = ProjectHelper.GetProjectFromRest(
					project_id, request);
			if(info==null){
				return null;
			}
		}
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		return xmxx;
	}
	
	@SuppressWarnings("unchecked")
	private boolean accept10004(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listqlr = (List<HashMap<String, String>>) projectinfo.get("InfoQLR");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			House house = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);
			List<HashMap<String, String>> qlrmaplist =listqlr;
			house = createNewHouse_GZ(hmap,xmxx);
			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			
			createOwnerRights_GZ(house, gzdjdy, qlmap, qlrmaplist, xmxx, "", "4","100",head.get("CaseNum"));
		}
		baseCommonDao.flush();
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean accept20004(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listqlr = (List<HashMap<String, String>>) projectinfo.get("InfoQLR");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			House house = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);
			List<HashMap<String, String>> qlrmaplist =listqlr;
			house = createNewHouse_GZ(hmap,xmxx);
			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			
			createOwnerRights_GZ(house, gzdjdy, qlmap, qlrmaplist, xmxx, "", "4","200",head.get("CaseNum"));
		}
		baseCommonDao.flush();
		return true;
	}
	@SuppressWarnings("unchecked")
	private boolean accept30004(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listqlr = (List<HashMap<String, String>>) projectinfo.get("InfoQLR");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			House house = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);
			List<HashMap<String, String>> qlrmaplist =listqlr;
			house = createNewHouse_GZ(hmap,xmxx);
			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			
			createOwnerRights_GZ(house, gzdjdy, qlmap, qlrmaplist, xmxx, "", "4","300",head.get("CaseNum"));
		}
		baseCommonDao.flush();
		return true;
	}
	@SuppressWarnings("unchecked")
	private boolean accept40004(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		if(!projectinfo.containsKey("InfoH")||projectinfo.get("InfoH")==null){
			return false;
		}
		List<HashMap<String, String>> list_h= (List<HashMap<String, String>>)projectinfo.get("InfoH");
		if(list_h==null||list_h.size()<=0){
			return false;
		}
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		for(HashMap<String, String> map_h:list_h){
			BDCS_H_XZ h=new BDCS_H_XZ();
			StringHelper.setValue(map_h, h);
			String bdcdyh=h.getBDCDYH();
			if(!StringHelper.isEmpty(bdcdyh)&&bdcdyh.length()>19){
				List<RealUnit> list_zdunit=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.XZ, "BDCDYH LIKE '"+bdcdyh.substring(0, 19)+"%'");
				if(list_zdunit!=null&&list_zdunit.size()>0){
					h.setZDBDCDYID(list_zdunit.get(0).getId());
				}
			}
			String bdcdyid=map_h.get("BDCDYID");
//			String bdcdyid=SuperHelper.GeneratePrimaryKey();
			h.setId(bdcdyid);
			h.setXMBH(xmxx.getId());
			
			BDCS_DJDY_XZ djdy_gz=new BDCS_DJDY_XZ();
			djdy_gz.setXMBH(xmxx.getId());
			djdy_gz.setBDCDYH(map_h.get("BDCDYH"));
			djdy_gz.setBDCDYID(bdcdyid);
			djdy_gz.setBDCDYLX(BDCDYLX.H.Value);
			djdy_gz.setDJDYID(bdcdyid);
			djdy_gz.setGROUPID(1);
			djdy_gz.setLY(DJDYLY.XZ.Value);
			String id_djdy_gz=SuperHelper.GeneratePrimaryKey();
			djdy_gz.setId(id_djdy_gz);
			
			BDCS_DJDY_GZ djdy=new BDCS_DJDY_GZ();
			djdy.setXMBH(xmxx.getId());
			djdy.setBDCDYH(map_h.get("BDCDYH"));
			djdy.setBDCDYID(bdcdyid);
			djdy.setBDCDYLX(BDCDYLX.H.Value);
			djdy.setDJDYID(bdcdyid);
			djdy.setGROUPID(1);
			djdy.setXMBH(xmxx.getId());
			djdy.setLY(DJDYLY.XZ.Value);
			String id_djdy=SuperHelper.GeneratePrimaryKey();
			djdy.setId(id_djdy);
			BDCS_H_LS h_ls=new BDCS_H_LS();
			try {
				PropertyUtils.copyProperties(h_ls, h);
			} catch (Exception e) {
				e.printStackTrace();
			}
			baseCommonDao.save(h);
			baseCommonDao.save(h_ls);
			baseCommonDao.save(djdy_gz);
			baseCommonDao.save(djdy);
			baseCommonDao.flush();
		}
		HashMap<String,String> ql_gzql=new HashMap<String, String>();
		List<HashMap<String, String>> list_fdcq= (List<HashMap<String, String>>)projectinfo.get("InfoFDCQ");
		if(list_fdcq==null||list_fdcq.size()<=0){
			return false;
		}
		for(HashMap<String, String> map_fdcq:list_fdcq){
			String djdyid=map_fdcq.get("BDCDYID");
			String qlid=map_fdcq.get("QLID");
			String fsqlid=SuperHelper.GeneratePrimaryKey();
			BDCS_QL_XZ ql=new BDCS_QL_XZ();
			StringHelper.setValue(map_fdcq, ql);
			ql.setId(qlid);
			ql.setCZFS(CZFS.FBCZ.Value);
			ql.setZSBS(ZSBS.DYB.Value);
			ql.setFSQLID(fsqlid);
			ql.setDJDYID(djdyid);
			ql.setXMBH(xmxx.getId());
			ql.setDBR("");
			ql.setCASENUM(head.get("CaseNum"));
			if(StringHelper.isEmpty(ql.getQLLX())){
				ql.setQLLX(QLLX.GYJSYDSHYQ_FWSYQ.Value);
			}
			BDCS_FSQL_XZ fsql=new BDCS_FSQL_XZ();
			StringHelper.setValue(map_fdcq, fsql);
			fsql.setId(fsqlid);
			fsql.setDJDYID(djdyid);
			fsql.setXMBH(xmxx.getId());
			baseCommonDao.save(ql);
			baseCommonDao.save(fsql);
			BDCS_QL_LS ql_ls=new BDCS_QL_LS();
			try {
				PropertyUtils.copyProperties(ql_ls, ql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			BDCS_FSQL_LS fsql_ls=new BDCS_FSQL_LS();
			try {
				PropertyUtils.copyProperties(fsql_ls, fsql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			baseCommonDao.save(ql_ls);
			baseCommonDao.save(fsql_ls);
			baseCommonDao.flush();
			
			String qlid_gz=SuperHelper.GeneratePrimaryKey();
			String fsqlid_gz=SuperHelper.GeneratePrimaryKey();
			BDCS_QL_GZ ql_gz=new BDCS_QL_GZ();
			StringHelper.setValue(map_fdcq, ql_gz);
			ql_gz.setId(qlid_gz);
			ql_gz.setYWH(xmxx.getPROJECT_ID());
			ql_gz.setLYQLID(qlid);
			ql_gz.setCZFS(CZFS.FBCZ.Value);
			ql_gz.setZSBS(ZSBS.DYB.Value);
			ql_gz.setFSQLID(fsqlid_gz);
			ql_gz.setDJDYID(djdyid);
			ql_gz.setXMBH(xmxx.getId());
			ql_gz.setDBR("");
			ql_gz.setDJLX(DJLX.ZXDJ.Value);
			if(StringHelper.isEmpty(ql_gz.getQLLX())){
				ql_gz.setQLLX(QLLX.GYJSYDSHYQ_FWSYQ.Value);
			}
			BDCS_FSQL_GZ fsql_gz=new BDCS_FSQL_GZ();
			StringHelper.setValue(map_fdcq, fsql_gz);
			fsql_gz.setId(fsqlid_gz);
			fsql_gz.setQLID(qlid_gz);
			fsql_gz.setDJDYID(djdyid);
			fsql_gz.setXMBH(xmxx.getId());
			baseCommonDao.save(ql_gz);
			baseCommonDao.save(fsql_gz);
			baseCommonDao.flush();
			if(!ql_gzql.containsKey(qlid)){
				ql_gzql.put(qlid, qlid_gz);
			}
		}
		
		List<HashMap<String, String>> list_qlr= (List<HashMap<String, String>>)projectinfo.get("InfoQLR");
		if(list_qlr==null||list_qlr.size()<=0){
			return false;
		}
		for(HashMap<String, String> map_qlr:list_qlr){
			String qlrid=map_qlr.get("QLRID");
			BDCS_QLR_XZ qlr=new BDCS_QLR_XZ();
			StringHelper.setValue(map_qlr, qlr);
			qlr.setXMBH(xmxx.getId());
			qlr.setId(qlrid);
			
			String qlrid_gz=SuperHelper.GeneratePrimaryKey();
			BDCS_QLR_GZ qlr_gz=new BDCS_QLR_GZ();
			StringHelper.setValue(map_qlr, qlr_gz);
			qlr_gz.setXMBH(xmxx.getId());
			qlr_gz.setId(qlrid_gz);
			qlr_gz.setQLID(ql_gzql.get(qlr_gz.getQLID()));
			
			BDCS_QDZR_XZ qdzr=new BDCS_QDZR_XZ();
			String id_qdzr=SuperHelper.GeneratePrimaryKey();
			String id_zs=SuperHelper.GeneratePrimaryKey();
			SubRights fsql=RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, qlr.getQLID());
			qdzr.setBDCDYH(fsql.getBDCDYH());
			qdzr.setDJDYID(fsql.getDJDYID());
			qdzr.setFSQLID(fsql.getId());
			qdzr.setId(id_qdzr);
			qdzr.setQLID(qlr.getQLID());
			qdzr.setQLRID(qlrid);
			qdzr.setXMBH(xmxx.getId());
			qdzr.setZSID(id_zs);
			
			BDCS_ZS_XZ zs=new BDCS_ZS_XZ();
			zs.setBDCQZH(qlr.getBDCQZH());
			zs.setId(id_zs);
			zs.setXMBH(xmxx.getId());
			
			BDCS_ZS_LS zs_ls=new BDCS_ZS_LS();
			try {
				PropertyUtils.copyProperties(zs_ls, zs);
			} catch (Exception e) {
				e.printStackTrace();
			}
			BDCS_QLR_LS qlr_ls=new BDCS_QLR_LS();
			try {
				PropertyUtils.copyProperties(qlr_ls, qlr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			BDCS_QDZR_LS qdzr_ls=new BDCS_QDZR_LS();
			try {
				PropertyUtils.copyProperties(qdzr_ls, qdzr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			baseCommonDao.save(zs);
			baseCommonDao.save(qdzr);
			baseCommonDao.save(qlr);
			baseCommonDao.save(zs_ls);
			baseCommonDao.save(qdzr_ls);
			baseCommonDao.save(qlr_ls);
			baseCommonDao.save(qlr_gz);
			baseCommonDao.flush();
		}
		return true;
	}
	
	/**
	 * 抵押权初始登记
	 * @Title: accept10023 
	 * @author:liushufeng
	 * @date：2016年6月28日 下午11:34:22
	 * @param projectinfo
	 * @param xmxx
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean accept10023(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listdyq = (List<HashMap<String, String>>) projectinfo.get("InfoDYAQ");
		List<HashMap<String, String>> listdyqr = (List<HashMap<String, String>>) projectinfo.get("InfoQLR");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			String relationid = hmap.get("BDCDYID");
			House house = null;
			Rights rights = null;
			Rights dyrights = null;
			RegisterUnit xzdjdy = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);// 房地产权 getRightsMap(listql, hmap);//
			HashMap<String, String> dyqmap = listdyq.get(0);// 抵押权
			List<HashMap<String, String>> qlrmaplist =listdyqr;// 抵押权人列表  getRightsHolder((List<HashMap<String, String>>) projectinfo.get("QLR"), qlmap);// 房地产权利人
			List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID='" + relationid + "'");
			house = (House) getFirstFromList(houses);
			if (house == null)
				house = createNewHouse_XZ(hmap,xmxx);
			String fromsql = "  FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=QL.DJDYID LEFT JOIN BDCK.BDCS_H_XZ H ON H.BDCDYID=DJDY.BDCDYID WHERE H.RELATIONID='"
					+ relationid + "' AND QL.QLLX='4'";
			long rightscount = baseCommonDao.getCountByFullSql(fromsql);
			if (rightscount <= 0) {
				// 还没有所有权，要创建权利关系
				xzdjdy = (RegisterUnit) getFirstFromList(baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + house.getId() + "'"));
				if (xzdjdy == null) {
					xzdjdy = createDJDY(house, new BDCS_DJDY_XZ(),xmxx);
				}
				baseCommonDao.save(xzdjdy);
				rights = createOwnerRights(house, xzdjdy, qlmap, dyqmap.get("DYR"));
				baseCommonDao.save(rights);
			}

			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			dyrights = createMortgageRights(house, xmxx.getPROJECT_ID(), rights, gzdjdy, dyqmap,head.get("CaseNum"));
			baseCommonDao.save(dyrights);
			createHolders(dyrights, qlrmaplist);
		}
		baseCommonDao.flush();
		return true;
	}

	/**
	 * 创建现状户，再拷贝到历史户
	 * @Title: createNewHouse_XZ 
	 * @author:liushufeng
	 * @date：2016年6月28日 下午11:15:02
	 * @param map
	 * @param xmxx
	 * @return
	 */
	private House createNewHouse_GZ(HashMap<String, String> map,BDCS_XMXX xmxx) {
		BDCS_H_GZ h = new BDCS_H_GZ();
		StringHelper.setValue(map, h);
		String bdcdyh=h.getBDCDYH();
		if(!StringHelper.isEmpty(bdcdyh)&&bdcdyh.length()>19){
			List<RealUnit> list_zdunit=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.XZ, "BDCDYH LIKE '"+bdcdyh.substring(0, 19)+"%'");
			if(list_zdunit!=null&&list_zdunit.size()>0){
				h.setZDBDCDYID(list_zdunit.get(0).getId());
			}
		}
		String bdcdyid=SuperHelper.GeneratePrimaryKey();
		h.setId(bdcdyid);
		h.setXMBH(xmxx.getId());
		baseCommonDao.save(h);
		return h;
	}
	private House createNewHouse_XZ(HashMap<String, String> map,BDCS_XMXX xmxx) {
		String relationid=map.get("RELATIONID");
		List<RealUnit> list_unit=UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID='"+relationid+"'");
		if(list_unit!=null&&list_unit.size()>0){
			return (House)list_unit.get(0);
		}else{
			BDCS_H_XZ h = new BDCS_H_XZ();
			StringHelper.setValue(map, h);
			String bdcdyh=h.getBDCDYH();
			if(!StringHelper.isEmpty(bdcdyh)&&bdcdyh.length()>19){
				List<RealUnit> list_zdunit=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.XZ, "BDCDYH LIKE '"+bdcdyh.substring(0, 19)+"%'");
				if(list_zdunit!=null&&list_zdunit.size()>0){
					h.setZDBDCDYID(list_zdunit.get(0).getId());
				}
			}
			String bdcdyid=SuperHelper.GeneratePrimaryKey();
			h.setId(bdcdyid);
			h.setXMBH(xmxx.getId());
			
			baseCommonDao.save(h);
			baseCommonDao.save(ObjectHelper.copyH_XZToLS(h));
			return h;
		}
	}
	
	/**
	 * 创建现状和历史登记单元
	 * @Title: createDJDY
	 * @author:liushufeng
	 * @date：2016年6月28日 下午11:10:16
	 * @param house
	 * @param djdy
	 * @param xmxx
	 * @return
	 */
	private RegisterUnit createDJDY(House house, RegisterUnit djdy,BDCS_XMXX xmxx) {
		djdy.setBDCDYH(house.getBDCDYH());
		djdy.setBDCDYID(house.getId());
		djdy.setBDCDYLX(BDCDYLX.H.Value);
		djdy.setDJDYID((String) SuperHelper.GeneratePrimaryKey());
		if(DJDYLY.GZ.Value.equals(house.getLY())){
			List<BDCS_DJDY_XZ> list=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+house.getId()+"'");
			if(list!=null&&list.size()>0){
				djdy.setDJDYID(list.get(0).getDJDYID());
			}
		}
		djdy.setXMBH(xmxx.getId());
		djdy.setLY(house.getLY() == null ? "" : house.getLY().Value);
		if(DJDYLY.XZ.Value.equals(house.getLY())){
			baseCommonDao.save(ObjectHelper.copyDJDY_XZToLS((BDCS_DJDY_XZ) djdy));
		}
		return djdy;
	}
	
	/**
	 * 创建现状和历史的权利、附属权利、权利人、证书、权地证人。
	 * @Title: createOwnerRights 
	 * @author:liushufeng
	 * @date：2016年6月28日 下午11:10:46
	 * @param house
	 * @param xzdjdy
	 * @param qlmap
	 * @param qlrmc
	 * @return
	 */
	private Rights createOwnerRights(House house, RegisterUnit xzdjdy, HashMap<String, String> qlmap, String qlrmc) {
		Rights rights = new BDCS_QL_XZ();
		SubRights subrights = new BDCS_FSQL_XZ();
		RightsHolder holder = new BDCS_QLR_XZ();
		BDCS_ZS_XZ zs = new BDCS_ZS_XZ();
		BDCS_QDZR_XZ qdzr = new BDCS_QDZR_XZ();
		
		rights.setBDCDYH(house.getBDCDYH());
		rights.setDJDYID(xzdjdy.getDJDYID());
		rights.setQLLX(QLLX.GYJSYDSHYQ_FWSYQ.Value);
		rights.setDJLX(DJLX.CSDJ.Value);
		rights.setZSBS(ZSBS.DYB.Value);
		rights.setDJJG(qlmap.get("DJJG"));
		rights.setQXDM(qlmap.get("QXDM"));
		rights.setDBR(qlmap.get("DBR"));
		rights.setBDCQZH(qlmap.get("BDCQZH"));
		try {
			rights.setDJSJ(StringHelper.FormatByDate(qlmap.get("DJSJ"),"yyyyMMddHHmmss"));
		} catch (Exception ee) {
			logger.info("生成现状时转换登记时间出错");
		}
		try {
			rights.setQLQSSJ(StringHelper.FormatByDate(qlmap.get("QLQSSJ"),"yyyyMMdd"));
		} catch (Exception ee) {
			logger.info("生成现状时转换权利起始时间出错");
		}
		try {
			rights.setQLJSSJ(StringHelper.FormatByDate(qlmap.get("QLJSSJ"),"yyyyMMdd"));
		} catch (Exception ee) {
			logger.info("生成现状时转换权利结束时间出错");
		}
		rights.setDJYY(qlmap.get("DJYY"));
		rights.setFJ(qlmap.get("FJ"));
		rights.setYWH(qlmap.get("YWH"));
		
		subrights.setQLID(rights.getId());
		
		holder.setQLID(rights.getId());
		holder.setBDCQZH(rights.getBDCQZH());
		holder.setQLRMC(qlrmc);
		
		zs.setBDCQZH(rights.getBDCQZH());
		
		qdzr.setQLID(rights.getId());
		qdzr.setZSID(zs.getId());
		qdzr.setQLRID(holder.getId());
		qdzr.setDJDYID(rights.getDJDYID());
		baseCommonDao.save(zs);
		baseCommonDao.save(holder);
		baseCommonDao.save(qdzr);
		baseCommonDao.save(rights);
		baseCommonDao.save(subrights);
		baseCommonDao.save(ObjectHelper.copyQL_XZToLS((BDCS_QL_XZ) rights));
		baseCommonDao.save(ObjectHelper.copyQLR_XZToLS((BDCS_QLR_XZ) holder));
		baseCommonDao.save(ObjectHelper.copyQDZR_XZToLS(qdzr));
		baseCommonDao.save(ObjectHelper.copyZS_XZToLS(zs));
		baseCommonDao.save(ObjectHelper.copyFSQL_XZToLS((BDCS_FSQL_XZ) subrights));
		return rights;
	}
	/**
	 * 主体权利、权利人、证书及权地证人信息
	 * @param house
	 * @param xzdjdy
	 * @param qlmap
	 * @param projectinfo
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	private Rights createOwnerRightsEx(House house, RegisterUnit xzdjdy, HashMap<String, String> qlmap, HashMap<String, Object> projectinfo) {
		
		String djdyid=xzdjdy.getDJDYID();
		String qlid=SuperHelper.GeneratePrimaryKey();
		String fsqlid=SuperHelper.GeneratePrimaryKey();
		
		Rights rights = new BDCS_QL_XZ();
		StringHelper.setValue(qlmap, rights);
		rights.setId(qlid);
		rights.setCZFS(CZFS.FBCZ.Value);
		rights.setZSBS(ZSBS.DYB.Value);
		rights.setFSQLID(fsqlid);
		rights.setDJDYID(djdyid);
		rights.setQLLX(QLLX.GYJSYDSHYQ_FWSYQ.Value);
		rights.setFJ(fsqlid);
		
		SubRights subrights = new BDCS_FSQL_XZ();
		StringHelper.setValue(qlmap, subrights);
		subrights.setId(fsqlid);
		subrights.setDJDYID(djdyid);
		subrights.setQLID(qlid);
		
		List<HashMap<String, String>> list_qlr= (List<HashMap<String, String>>)projectinfo.get("InfoQLR");
		if(list_qlr==null||list_qlr.size()<=0){
			return rights;
		}
		for(HashMap<String, String> map_qlr:list_qlr){
			String qlrid=map_qlr.get("QLRID");
			RightsHolder qlr=new BDCS_QLR_XZ();
			StringHelper.setValue(map_qlr, qlr);
			qlr.setId(qlrid);
			qlr.setQLID(qlid);
			BDCS_QDZR_XZ qdzr=new BDCS_QDZR_XZ();
			String id_zs=SuperHelper.GeneratePrimaryKey();
			qdzr.setBDCDYH(rights.getBDCDYH());
			qdzr.setDJDYID(rights.getDJDYID());
			qdzr.setFSQLID(subrights.getId());
			qdzr.setQLID(qlr.getQLID());
			qdzr.setQLRID(qlrid);
			qdzr.setZSID(id_zs);			
			BDCS_ZS_XZ zs=new BDCS_ZS_XZ();
			zs.setId(id_zs);			
			BDCS_ZS_LS bdcs_zs_ls=ObjectHelper.copyZS_XZToLS(zs);
			baseCommonDao.save(bdcs_zs_ls);
			BDCS_QDZR_LS bdcs_qdzr_ls=ObjectHelper.copyQDZR_XZToLS(qdzr);
			baseCommonDao.save(bdcs_qdzr_ls);
			BDCS_QLR_LS bdcs_qlr_ls=ObjectHelper.copyQLR_XZToLS((BDCS_QLR_XZ)qlr);
			baseCommonDao.save(zs);
			baseCommonDao.save(qdzr);
			baseCommonDao.save(qlr);
			baseCommonDao.save(bdcs_zs_ls);
			baseCommonDao.save(bdcs_qdzr_ls);
			baseCommonDao.save(bdcs_qlr_ls);		
		}	
		baseCommonDao.save(rights);
		baseCommonDao.save(subrights);
		baseCommonDao.save(ObjectHelper.copyQL_XZToLS((BDCS_QL_XZ) rights));
		baseCommonDao.save(ObjectHelper.copyFSQL_XZToLS((BDCS_FSQL_XZ) subrights));			
		return rights;
	}
	
	/**
	 * 现状查封权利
	 * @param house
	 * @param xzdjdy
	 * @param qlmap
	 * @param projectinfo
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	private Rights createLimitRights(House house, RegisterUnit xzdjdy, HashMap<String, String> qlmap) {
		
		String djdyid=xzdjdy.getDJDYID();
		String qlid=SuperHelper.GeneratePrimaryKey();
		String fsqlid=SuperHelper.GeneratePrimaryKey();
		
		Rights rights = new BDCS_QL_XZ();
		StringHelper.setValue(qlmap, rights);
		rights.setId(qlid);
		rights.setCZFS(CZFS.FBCZ.Value);
		rights.setZSBS(ZSBS.DYB.Value);
		rights.setFSQLID(fsqlid);
		rights.setDJDYID(djdyid);
		rights.setQLLX("99");
		rights.setDJLX("800");
		rights.setFJ(fsqlid);
		
		SubRights subrights = new BDCS_FSQL_XZ();
		StringHelper.setValue(qlmap, subrights);
		subrights.setId(fsqlid);
		subrights.setDJDYID(djdyid);
		subrights.setQLID(qlid);
		baseCommonDao.save(rights);
		baseCommonDao.save(subrights);
		baseCommonDao.save(ObjectHelper.copyQL_XZToLS((BDCS_QL_XZ) rights));
		baseCommonDao.save(ObjectHelper.copyFSQL_XZToLS((BDCS_FSQL_XZ) subrights));			
		return rights;
	}
	
	/**
	 * 主体权利、权利人、证书及权地证人信息
	 * @param house
	 * @param xzdjdy
	 * @param qlmap
	 * @param projectinfo
	 * @return
	 */
	private Rights createOwnerRights_GZ(House house, RegisterUnit gzdjdy, HashMap<String, String> qlmap, List<HashMap<String, String>> list_qlr,BDCS_XMXX xmxx,String lyqlid,String qllx,String djlx,String casenum) {
		
		String djdyid=gzdjdy.getDJDYID();
		String qlid=SuperHelper.GeneratePrimaryKey();
		String fsqlid=SuperHelper.GeneratePrimaryKey();
		
		Rights rights = new BDCS_QL_GZ();
		StringHelper.setValue(qlmap, rights);
		rights.setId(qlid);
		rights.setXMBH(xmxx.getId());
		rights.setCZFS(CZFS.FBCZ.Value);
		rights.setZSBS(ZSBS.DYB.Value);
		rights.setFSQLID(fsqlid);
		rights.setDJDYID(djdyid);
		rights.setQLLX(qllx);
		rights.setDJLX(djlx);
		rights.setFJ(fsqlid);
		rights.setLYQLID(lyqlid);
		rights.setCASENUM(casenum);
		
		SubRights subrights = new BDCS_FSQL_GZ();
		StringHelper.setValue(qlmap, subrights);
		subrights.setId(fsqlid);
		subrights.setXMBH(xmxx.getId());
		subrights.setDJDYID(djdyid);
		subrights.setQLID(qlid);
		baseCommonDao.save(rights);
		baseCommonDao.save(subrights);
		if(list_qlr==null||list_qlr.size()<=0){
			return rights;
		}
		for(HashMap<String, String> map_qlr:list_qlr){
			String qlrid=map_qlr.get("QLRID");
			RightsHolder qlr=new BDCS_QLR_GZ();
			StringHelper.setValue(map_qlr, qlr);
			qlr.setId(qlrid);
			qlr.setQLID(qlid);
			qlr.setXMBH(xmxx.getId());
			BDCS_QDZR_GZ qdzr=new BDCS_QDZR_GZ();
			String id_zs=SuperHelper.GeneratePrimaryKey();
			qdzr.setBDCDYH(rights.getBDCDYH());
			qdzr.setDJDYID(rights.getDJDYID());
			qdzr.setFSQLID(subrights.getId());
			qdzr.setQLID(qlr.getQLID());
			qdzr.setQLRID(qlrid);
			qdzr.setXMBH(xmxx.getId());
			qdzr.setZSID(id_zs);			
			BDCS_ZS_GZ zs=new BDCS_ZS_GZ();
			zs.setId(id_zs);
			zs.setXMBH(xmxx.getId());
			baseCommonDao.save(zs);
			baseCommonDao.save(qdzr);
			baseCommonDao.save(qlr);
		}	
		return rights;
	}
	
	/**
	 * 创建工作层抵押权
	 * @Title: createMortgageRights
	 * @author:liushufeng
	 * @date：2016年6月28日 下午10:31:57
	 * @param house
	 * @param gzdjdy
	 * @param hashMap
	 * @return
	 */
	private Rights createMortgageRights(House house, String ywh, Rights ownerRights, RegisterUnit gzdjdy, HashMap<String, String> dyqmap,String casenum) {
		Rights rights = new BDCS_QL_GZ();
		rights.setBDCDYH(house.getBDCDYH());
		rights.setBDCQZH(dyqmap.get("BDCDJZMH"));
		rights.setCZFS(CZFS.GTCZ.Value);
		rights.setZSBS(ZSBS.DYB.Value);
		rights.setQLLX(QLLX.DIYQ.Value);
		rights.setDJLX(DJLX.CSDJ.Value);
		rights.setDJDYID(gzdjdy.getDJDYID());
		rights.setDJYY(dyqmap.get("DJYY"));
		rights.setLYQLID(ownerRights.getId());
		rights.setFJ(dyqmap.get("FJ"));
		rights.setYWH(ywh);
		rights.setXMBH(gzdjdy.getXMBH());
		rights.setCASENUM(casenum);
		try {
			rights.setQLQSSJ(StringHelper.FormatByDate(dyqmap.get("ZWLXQSSJ"),"yyyyMMdd"));
			rights.setQLJSSJ(StringHelper.FormatByDate(dyqmap.get("ZWLXJSSJ"),"yyyyMMdd"));
		} catch (ParseException e) {
			logger.info("创建工作层抵押权的时候，权利起始时间和终止时间转换出错");
		}

		SubRights subrights = new BDCS_FSQL_GZ();
		subrights.setDYR(dyqmap.get("DYR"));
		subrights.setDYFS(dyqmap.get("DYFS"));
		subrights.setZJJZWZL(dyqmap.get("ZJJZWZL"));
		subrights.setQLID(rights.getId());
		subrights.setZJJZWDYFW(dyqmap.get("ZJJZWDYFW"));
		subrights.setZGZQQDSS(dyqmap.get("ZGZQQDSS"));
		subrights.setXMBH(gzdjdy.getXMBH());
		try {
			subrights.setZGZQSE(Double.valueOf(dyqmap.get("ZGZQSE")));
		} catch (Exception ee) {
			logger.info("转换最高债权数额出错");
		}
		try {
			subrights.setBDBZZQSE(Double.valueOf(dyqmap.get("BDBZZQSE")));
		} catch (Exception ee) {
			logger.info("转换担保债权数额出错");
		}
		rights.setFSQLID(subrights.getId());
		subrights.setQLID(rights.getId());
		baseCommonDao.save(rights);
		baseCommonDao.save(subrights);
		return rights;
	}

	/**
	 * 根据权利，权利人map创建工作层抵押权人，证书，权地证人
	 * @Title: createHolders
	 * @author:liushufeng
	 * @date：2016年6月28日 下午10:59:19
	 * @param rights
	 * @param qlrmaplist
	 * @return
	 */
	private List<RightsHolder> createHolders(Rights rights, List<HashMap<String, String>> qlrmaplist) {
		List<RightsHolder> holders = new ArrayList<RightsHolder>();
		BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
		zs.setXMBH(rights.getXMBH());
		baseCommonDao.save(zs);
		for (HashMap<String, String> qlrmap : qlrmaplist) {
			RightsHolder holder = null;
			if (rights instanceof BDCS_QL_GZ) {
				holder = new BDCS_QLR_GZ();
			} else if (rights instanceof BDCS_QL_XZ) {
				holder = new BDCS_QLR_XZ();
			} else {
				holder = new BDCS_QLR_LS();
			}
			holder = createNewHolder(qlrmap, holder);
			holder.setXMBH(rights.getXMBH());
			holder.setQLID(rights.getId());
			BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
			qdzr.setQLID(rights.getId());
			qdzr.setFSQLID(rights.getFSQLID());
			qdzr.setDJDYID(rights.getDJDYID());
			qdzr.setQLRID(holder.getId());
			qdzr.setBDCDYH(rights.getBDCDYH());
			qdzr.setXMBH(rights.getXMBH());
			qdzr.setZSID(zs.getId());
			baseCommonDao.save(qdzr);
			baseCommonDao.save(holder);
			holders.add(holder);
		}
		return holders;
	}

	/**
	 * 根据权利人Map创建权利人对象
	 * @Title: createNewHolder 
	 * @author:liushufeng
	 * @date：2016年6月28日 下午11:16:23
	 * @param qlrmap
	 * @param holder
	 * @return
	 */
	private RightsHolder createNewHolder(HashMap<String, String> qlrmap, RightsHolder holder) {
		StringHelper.setValue(qlrmap, holder);
		return holder;
	}

	/**
	 * 如果列表不为空，取第一个元素
	 * @Title: getFirstFromList 
	 * @author:liushufeng
	 * @date：2016年6月28日 下午11:15:34
	 * @param list
	 * @return
	 */
	private <T> T getFirstFromList(List<T> list) {
		T o = null;
		if (list != null && list.size() > 0) {
			o = list.get(0);
		}
		return o;
	}
	
	@SuppressWarnings("unchecked")
	private boolean accept20023(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listdyq = (List<HashMap<String, String>>) projectinfo.get("InfoDYAQ");
		List<HashMap<String, String>> listdyqr = (List<HashMap<String, String>>) projectinfo.get("InfoQLR");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			String relationid = hmap.get("BDCDYID");
			House house = null;
			Rights rights = null;
			Rights dyrights = null;
			RegisterUnit xzdjdy = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);// 房地产权 getRightsMap(listql, hmap);//
			HashMap<String, String> dyqmap = listdyq.get(0);// 抵押权
			List<HashMap<String, String>> qlrmaplist =listdyqr;// 抵押权人列表  getRightsHolder((List<HashMap<String, String>>) projectinfo.get("QLR"), qlmap);// 房地产权利人
			List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID='" + relationid + "'");
			house = (House) getFirstFromList(houses);
			if (house == null)
				house = createNewHouse_XZ(hmap,xmxx);
			String fromsql = "  FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=QL.DJDYID LEFT JOIN BDCK.BDCS_H_XZ H ON H.BDCDYID=DJDY.BDCDYID WHERE H.RELATIONID='"
					+ relationid + "' AND QL.QLLX='4'";
			long rightscount = baseCommonDao.getCountByFullSql(fromsql);
			if (rightscount <= 0) {
				// 还没有所有权，要创建权利关系
				xzdjdy = (RegisterUnit) getFirstFromList(baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + house.getId() + "'"));
				if (xzdjdy == null) {
					xzdjdy = createDJDY(house, new BDCS_DJDY_XZ(),xmxx);
				}
				baseCommonDao.save(xzdjdy);
				rights = createOwnerRights(house, xzdjdy, qlmap, dyqmap.get("DYR"));
				baseCommonDao.save(rights);
			}

			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			dyrights = createMortgageRights(house, xmxx.getPROJECT_ID(), rights, gzdjdy, dyqmap,head.get("CaseNum"));
			baseCommonDao.save(dyrights);
			createHolders(dyrights, qlrmaplist);
		}
		baseCommonDao.flush();
		return true;
	}
	@SuppressWarnings("unchecked")
	private boolean accept30023(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listdyq = (List<HashMap<String, String>>) projectinfo.get("InfoDYAQ");
		List<HashMap<String, String>> listdyqr = (List<HashMap<String, String>>) projectinfo.get("InfoQLR");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			String relationid = hmap.get("BDCDYID");
			House house = null;
			Rights rights = null;
			Rights dyrights = null;
			RegisterUnit xzdjdy = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);// 房地产权 getRightsMap(listql, hmap);//
			HashMap<String, String> dyqmap = listdyq.get(0);// 抵押权
			List<HashMap<String, String>> qlrmaplist =listdyqr;// 抵押权人列表  getRightsHolder((List<HashMap<String, String>>) projectinfo.get("QLR"), qlmap);// 房地产权利人
			List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID='" + relationid + "'");
			house = (House) getFirstFromList(houses);
			if (house == null)
				house = createNewHouse_XZ(hmap,xmxx);
			String fromsql = "  FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=QL.DJDYID LEFT JOIN BDCK.BDCS_H_XZ H ON H.BDCDYID=DJDY.BDCDYID WHERE H.RELATIONID='"
					+ relationid + "' AND QL.QLLX='4'";
			long rightscount = baseCommonDao.getCountByFullSql(fromsql);
			if (rightscount <= 0) {
				// 还没有所有权，要创建权利关系
				xzdjdy = (RegisterUnit) getFirstFromList(baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + house.getId() + "'"));
				if (xzdjdy == null) {
					xzdjdy = createDJDY(house, new BDCS_DJDY_XZ(),xmxx);
				}
				baseCommonDao.save(xzdjdy);
				rights = createOwnerRights(house, xzdjdy, qlmap, dyqmap.get("DYR"));
				baseCommonDao.save(rights);
			}

			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			dyrights = createMortgageRights(house, xmxx.getPROJECT_ID(), rights, gzdjdy, dyqmap,head.get("CaseNum"));
			baseCommonDao.save(dyrights);
			createHolders(dyrights, qlrmaplist);
		}
		baseCommonDao.flush();
		return true;
	}
	@SuppressWarnings("unchecked")
	private boolean accept40023(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listdyq = (List<HashMap<String, String>>) projectinfo.get("InfoDYAQ");
		List<HashMap<String, String>> listdyqr = (List<HashMap<String, String>>) projectinfo.get("InfoQLR");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			String relationid = hmap.get("BDCDYID");
			House house = null;
			Rights rights = null;
			Rights dyrights = null;
			RegisterUnit xzdjdy = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);// 房地产权 getRightsMap(listql, hmap);//
			HashMap<String, String> dyqmap = listdyq.get(0);// 抵押权
			List<HashMap<String, String>> qlrmaplist =listdyqr;// 抵押权人列表  getRightsHolder((List<HashMap<String, String>>) projectinfo.get("QLR"), qlmap);// 房地产权利人
			List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID='" + relationid + "'");
			house = (House) getFirstFromList(houses);
			if (house == null)
				house = createNewHouse_XZ(hmap,xmxx);
			String fromsql = "  FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=QL.DJDYID LEFT JOIN BDCK.BDCS_H_XZ H ON H.BDCDYID=DJDY.BDCDYID WHERE H.RELATIONID='"
					+ relationid + "' AND QL.QLLX='4'";
			long rightscount = baseCommonDao.getCountByFullSql(fromsql);
			if (rightscount <= 0) {
				// 还没有所有权，要创建权利关系
				xzdjdy = (RegisterUnit) getFirstFromList(baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + house.getId() + "'"));
				if (xzdjdy == null) {
					xzdjdy = createDJDY(house, new BDCS_DJDY_XZ(),xmxx);
				}
				baseCommonDao.save(xzdjdy);
				rights = createOwnerRights(house, xzdjdy, qlmap, dyqmap.get("DYR"));
				baseCommonDao.save(rights);
			}

			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			dyrights = createMortgageRights(house, xmxx.getPROJECT_ID(), rights, gzdjdy, dyqmap,head.get("CaseNum"));
			baseCommonDao.save(dyrights);
			createHolders(dyrights, qlrmaplist);
		}
		baseCommonDao.flush();
		return true;
	}
	private boolean accept70001(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		return true;
	}
	private boolean accept70002(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		return true;
	}
	@SuppressWarnings("unchecked")
	private boolean accept80001(HashMap<String, Object> projectinfo, BDCS_XMXX xmxx) {
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listcfdj = (List<HashMap<String, String>>) projectinfo.get("InfoCFDJ");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			String relationid = hmap.get("BDCDYID");
			House house = null;
			Rights cfdjights = null;
			RegisterUnit xzdjdy = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);// 房地产权 getRightsMap(listql, hmap);//
			HashMap<String, String> cfdj = listcfdj.get(0);// 抵押权
			List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID='" + relationid + "'");
			house = (House) getFirstFromList(houses);
			if (house == null)
				house = createNewHouse_XZ(hmap,xmxx);
			String fromsql = "  FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=QL.DJDYID LEFT JOIN BDCK.BDCS_H_XZ H ON H.BDCDYID=DJDY.BDCDYID WHERE H.RELATIONID='"
					+ relationid + "' AND QL.QLLX='4'";
			long rightscount = baseCommonDao.getCountByFullSql(fromsql);
			if (rightscount <= 0) {
				// 还没有所有权，要创建权利关系
				xzdjdy = (RegisterUnit) getFirstFromList(baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + house.getId() + "'"));
				if (xzdjdy == null) {
					xzdjdy = createDJDY(house, new BDCS_DJDY_XZ(),xmxx);
				}
				baseCommonDao.save(xzdjdy);
				this.createOwnerRightsEx(house, xzdjdy, qlmap, projectinfo);
			}

			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			cfdjights = this.createOwnerRights_GZ(house, gzdjdy, cfdj, null, xmxx, "", "99","800",head.get("CaseNum"));
			baseCommonDao.save(cfdjights);
		}
		baseCommonDao.flush();
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private boolean accept70023(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		String xmbh = xmxx.getId();
		//FDCQ、H、QLR、YGDJ
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			boolean flag=true;//默认预告登记不存在，需要创建
			String relationid = hmap.get("RELATIONID");
			List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.YCH, DJDYLY.XZ, "RELATIONID='" + relationid + "'");
			if(!StringHelper.isEmpty(houses) && houses.size()>0) //执行预告抵押登记
			{
				flag=false;
			}
			if(flag)
			{
				House ych = new BDCS_H_XZY();
				StringHelper.setValue(hmap, ych);
			   String bdcdyh=	ych.getBDCDYH();
				if(!StringHelper.isEmpty(bdcdyh) && bdcdyh.length()>19){
					List<RealUnit> list_zdunit=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.XZ, "BDCDYH LIKE '"+bdcdyh.substring(0, 19)+"%'");
					if(list_zdunit!=null&&list_zdunit.size()>0){
						ych.setZDBDCDYID(list_zdunit.get(0).getId());
					}
				}
				baseCommonDao.save(UnitTools.copyUnit(ych, BDCDYLX.YCH, DJDYLY.LS));
				baseCommonDao.save(ych);
				BDCS_DJDY_XZ djdy =new BDCS_DJDY_XZ();
				djdy.setBDCDYH(ych.getBDCDYH());
				djdy.setBDCDYID(ych.getId());
				djdy.setBDCDYLX(BDCDYLX.YCH.Value);
				djdy.setDJDYID((String) SuperHelper.GeneratePrimaryKey());		
				djdy.setLY(ych.getLY() == null ? "" : ych.getLY().Value);
				baseCommonDao.save(djdy);
				baseCommonDao.save(ObjectHelper.copyDJDY_XZToLS((BDCS_DJDY_XZ) djdy));
				
				List<HashMap<String, String>> list_fdcq= (List<HashMap<String, String>>)projectinfo.get("InfoFDCQ");
				if(list_fdcq==null||list_fdcq.size()<=0){
					return false;
				}
				HashMap<String, String> qlmap=list_fdcq.get(0);
				String djdyid=djdy.getDJDYID();
				String qlid=qlmap.get("QLID");
				String fsqlid=SuperHelper.GeneratePrimaryKey();
				
				Rights rights = new BDCS_QL_XZ();
				StringHelper.setValue(qlmap, rights);
				rights.setId(qlid);
				rights.setCZFS(CZFS.FBCZ.Value);
				rights.setZSBS(ZSBS.DYB.Value);
				rights.setFSQLID(fsqlid);
				rights.setDJDYID(djdyid);
				rights.setQLLX(QLLX.GYJSYDSHYQ_FWSYQ.Value);
				
				SubRights subrights = new BDCS_FSQL_XZ();
				StringHelper.setValue(qlmap, subrights);
				subrights.setId(fsqlid);
				subrights.setDJDYID(djdyid);
				subrights.setQLID(qlid);
				
				List<HashMap<String, String>> listygdj = (List<HashMap<String, String>>) projectinfo.get("InfoYGDJ");
				if(!StringHelper.isEmpty(listygdj) && listygdj.size()>0)
				{
					HashMap<String,String> ygdj=listygdj.get(0);
					subrights.setYGDJZL(ygdj.get("YGDJZL"));
					rights.setFJ(ygdj.get("FJ"));
					rights.setDBR(ygdj.get("DBR"));
					Date dt;
					try {
						dt = StringHelper.FormatByDate(ygdj.get("DJSJ"));
						rights.setDJSJ(dt);
					} catch (ParseException e) {						
					}				
				}
				
				List<HashMap<String, String>> list_qlr= (List<HashMap<String, String>>)projectinfo.get("InfoQLR");
				if(list_qlr==null||list_qlr.size()<=0){
					return false;
				}
				for(HashMap<String, String> map_qlr:list_qlr){
					String qlrid=map_qlr.get("QLRID");
					RightsHolder qlr=new BDCS_QLR_XZ();
					StringHelper.setValue(map_qlr, qlr);
					qlr.setId(qlrid);
					qlr.setQLID(qlid);
					BDCS_QDZR_XZ qdzr=new BDCS_QDZR_XZ();
					String id_zs=SuperHelper.GeneratePrimaryKey();
					qdzr.setBDCDYH(rights.getBDCDYH());
					qdzr.setDJDYID(rights.getDJDYID());
					qdzr.setFSQLID(subrights.getId());
					qdzr.setQLID(qlr.getQLID());
					qdzr.setQLRID(qlrid);
					qdzr.setZSID(id_zs);			
					BDCS_ZS_XZ zs=new BDCS_ZS_XZ();
					zs.setId(id_zs);			
					BDCS_ZS_LS bdcs_zs_ls=ObjectHelper.copyZS_XZToLS(zs);
					baseCommonDao.save(bdcs_zs_ls);
					BDCS_QDZR_LS bdcs_qdzr_ls=ObjectHelper.copyQDZR_XZToLS(qdzr);
					baseCommonDao.save(bdcs_qdzr_ls);
					BDCS_QLR_LS bdcs_qlr_ls=ObjectHelper.copyQLR_XZToLS((BDCS_QLR_XZ)qlr);
					baseCommonDao.save(zs);
					baseCommonDao.save(qdzr);
					baseCommonDao.save(qlr);
					baseCommonDao.save(bdcs_zs_ls);
					baseCommonDao.save(bdcs_qdzr_ls);
					baseCommonDao.save(bdcs_qlr_ls);		
				}	
				baseCommonDao.save(rights);
				baseCommonDao.save(subrights);
				baseCommonDao.save(ObjectHelper.copyQL_XZToLS((BDCS_QL_XZ) rights));
				baseCommonDao.save(ObjectHelper.copyFSQL_XZToLS((BDCS_FSQL_XZ) subrights));	
				baseCommonDao.flush();	
			}
			else
			{
				List<RealUnit> units=UnitTools. loadUnits(BDCDYLX.YCH, DJDYLY.XZ, "RELATIONID ='"+relationid+"'");
				String bdcdyid="";
				if(!StringHelper.isEmpty(units) && units.size()>0)
				{
					bdcdyid=units.get(0).getId();
				}
				if(!StringHelper.isEmpty(bdcdyid))
				{
                List<BDCS_DJDY_XZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid+"'");
                if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
                {
                	BDCS_DJDY_GZ bdcs_djdy_gz =ObjectHelper.copyDJDY_XZToGZ(djdys.get(0));
                	bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
					String  djdyid=SuperHelper.GeneratePrimaryKey();
					bdcs_djdy_gz.setId(djdyid);
					bdcs_djdy_gz.setXMBH(xmxx.getId());
                	if(!StringHelper.isEmpty(bdcs_djdy_gz))
                	{
                		List<BDCS_QL_XZ> qls= baseCommonDao.getDataList(BDCS_QL_XZ.class, "DJDYID ='"+bdcs_djdy_gz.getDJDYID()+"' and QLLX ='4'");//获取主体权利
                		if(!StringHelper.isEmpty(qls) && qls.size()>0 )
                		{
                		   BDCS_QL_XZ bdcs_ql_xz =qls.get(0);	
                		   BDCS_QL_GZ bdcs_ql_gz = new BDCS_QL_GZ();
                		   bdcs_ql_gz.setBDCDYH(bdcs_djdy_gz.getBDCDYH());
                		   bdcs_ql_gz.setCZFS(CZFS.FBCZ.Value);
                		   bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);
                		   bdcs_ql_gz.setDJDYID(bdcs_djdy_gz.getDJDYID());
                		   bdcs_ql_gz.setQLLX(xmxx.getQLLX());
                		   bdcs_ql_gz.setDJLX(DJLX.YGDJ.Value);
                		   bdcs_ql_gz.setXMBH(xmxx.getId());
                		   bdcs_ql_gz.setYWH(xmxx.getPROJECT_ID());
                		   bdcs_ql_gz.setLYQLID(bdcs_ql_xz.getId());
	                   		BDCS_FSQL_GZ bdcs_fsql_gz = new BDCS_FSQL_GZ();
	                   		bdcs_fsql_gz.setDJDYID(bdcs_djdy_gz.getDJDYID());
	                   		bdcs_fsql_gz.setXMBH(xmxx.getId());
	                   		bdcs_ql_gz.setFSQLID(bdcs_fsql_gz.getId());
	                		bdcs_fsql_gz.setQLID(bdcs_ql_gz.getId());
	                		
	                		List<HashMap<String, String>> list_yydj = (List<HashMap<String, String>>) projectinfo.get("InfoYYDJ");
	        				if(list_yydj!=null&&list_yydj.size()>0){
	        					bdcs_fsql_gz.setYYSX(list_yydj.get(0).get("YYSX"));
	        					bdcs_ql_gz.setFJ(list_yydj.get(0).get("FJ"));
	        					bdcs_fsql_gz.setYWR(list_yydj.get(0).get("YWR"));
	        					bdcs_fsql_gz.setYWRZJZL(list_yydj.get(0).get("YGDJZL"));
	        					bdcs_fsql_gz.setYWRZJH(list_yydj.get(0).get("YWRZJH"));
	        					bdcs_fsql_gz.setQDJG(StringHelper.getDouble(list_yydj.get(0).get("QDJG")));
	        					//TODO 缺少抵押登记信息
	        				}
	        				List<HashMap<String, String>> list_qlr= (List<HashMap<String, String>>)projectinfo.get("InfoQLR");
	        				if(list_qlr==null||list_qlr.size()<=0){
	        					return false;
	        				}
	        				for(HashMap<String, String> map_qlr:list_qlr){
	        					String qlrid=map_qlr.get("QLRID");
	        					RightsHolder qlr=new BDCS_QLR_XZ();
	        					StringHelper.setValue(map_qlr, qlr);
	        					qlr.setId(qlrid);
	        					qlr.setQLID(bdcs_ql_gz.getId());
	        					qlr.setXMBH(xmxx.getId());
	                		BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
	                		
							qdzr.setXMBH(xmxx.getId());
							qdzr.setQLID(bdcs_ql_gz.getId());
                            qdzr.setDJDYID(bdcs_djdy_gz.getDJDYID());
                            
							BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
							zs.setXMBH(xmxx.getId());
							qdzr.setZSID(zs.getId());

							qdzr.setBDCDYH(bdcs_ql_gz.getBDCDYH());
							qdzr.setDJDYID(bdcs_ql_gz.getDJDYID());
							qdzr.setFSQLID(bdcs_fsql_gz.getId());

							// 设置为共同持证
							bdcs_ql_gz.setCZFS(CZFS.GTCZ.Value);
                            baseCommonDao.save(qlr);
							baseCommonDao.save(zs);
							baseCommonDao.save(qdzr);
	        				}
							// 保存权利和附属权利
							
							baseCommonDao.save(bdcs_ql_gz);
							baseCommonDao.save(bdcs_fsql_gz);
							baseCommonDao.save(bdcs_djdy_gz);
                		}                		
                	}
                }
			
                baseCommonDao.flush();				
			}
			
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean accept80002(HashMap<String, Object> projectinfo, BDCS_XMXX xmxx) {
		HashMap<String, String> head = (HashMap<String, String>) projectinfo.get("InfoHEAD");
		List<HashMap<String, String>> listh = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		List<HashMap<String, String>> listql = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
		List<HashMap<String, String>> listcfdj = (List<HashMap<String, String>>) projectinfo.get("InfoCFDJ");
		// 循环多个户
		for (HashMap<String, String> hmap : listh) {
			String relationid = hmap.get("BDCDYID");
			House house = null;
			Rights cfdjights = null;
			RegisterUnit xzdjdy = null;
			RegisterUnit gzdjdy = null;
			HashMap<String, String> qlmap = listql.get(0);// 房地产权 getRightsMap(listql, hmap);//
			HashMap<String, String> cfdj = listcfdj.get(0);// 抵押权
			List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID='" + relationid + "'");
			house = (House) getFirstFromList(houses);
			if (house == null)
				house = createNewHouse_XZ(hmap,xmxx);
			String fromsql = "  FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=QL.DJDYID LEFT JOIN BDCK.BDCS_H_XZ H ON H.BDCDYID=DJDY.BDCDYID WHERE H.RELATIONID='"
					+ relationid + "' AND QL.QLLX='4'";
			long rightscount = baseCommonDao.getCountByFullSql(fromsql);
			if (rightscount <= 0) {
				// 还没有所有权，要创建权利关系
				xzdjdy = (RegisterUnit) getFirstFromList(baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + house.getId() + "'"));
				if (xzdjdy == null) {
					xzdjdy = createDJDY(house, new BDCS_DJDY_XZ(),xmxx);
				}
				baseCommonDao.save(xzdjdy);
				this.createOwnerRightsEx(house, xzdjdy, qlmap, projectinfo);
			}

			gzdjdy = createDJDY(house, new BDCS_DJDY_GZ(),xmxx);
			baseCommonDao.save(gzdjdy);
			cfdjights = this.createOwnerRights_GZ(house, gzdjdy, cfdj, null, xmxx, "", "99","800",head.get("CaseNum"));
			baseCommonDao.save(cfdjights);
		}
		baseCommonDao.flush();
		return true;
	}
	@SuppressWarnings("unchecked")
	private boolean accept60001(HashMap<String, Object> projectinfo,BDCS_XMXX xmxx){
		if(!projectinfo.containsKey("InfoH")||projectinfo.get("InfoH")==null){
			return false;
		}
		List<HashMap<String, String>> list_h= (List<HashMap<String, String>>)projectinfo.get("InfoH");
		if(list_h==null||list_h.size()<=0){
			return false;
		}
		for(HashMap<String, String> map_h:list_h){
			boolean flag=true;//默认不存在户信息，则创建
			String relationid=StringHelper.formatObject(map_h.get("RELATIONID"));
			if(!StringHelper.isEmpty(relationid))
			{
				List<RealUnit> units=UnitTools. loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID ='"+relationid+"'");
				if(!StringHelper.isEmpty(units) && units.size()>0)
				{
					flag=false;
				}
			}
			if(flag)
			{
				House h=createNewHouse_XZ(map_h,xmxx);
				baseCommonDao.save(h);
				RegisterUnit djdy=createDJDY(h,new BDCS_DJDY_XZ(),xmxx);
				baseCommonDao.save(djdy);
				List<HashMap<String, String>> list_fdcq= (List<HashMap<String, String>>)projectinfo.get("InfoFDCQ");
				if(list_fdcq==null||list_fdcq.size()<=0){
					return false;
				}
				Rights rights =createOwnerRightsEx(h,djdy,list_fdcq.get(0),projectinfo);
				baseCommonDao.save(rights);
				baseCommonDao.flush();	
			}
			else
			{
				List<RealUnit> units=UnitTools. loadUnits(BDCDYLX.H, DJDYLY.XZ, "RELATIONID ='"+relationid+"'");
				String bdcdyid="";
				if(!StringHelper.isEmpty(units) && units.size()>0)
				{
					bdcdyid=units.get(0).getId();
				}
				if(!StringHelper.isEmpty(bdcdyid))
				{
                List<BDCS_DJDY_XZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid+"'");
                if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
                {
                	BDCS_DJDY_GZ bdcs_djdy_gz =ObjectHelper.copyDJDY_XZToGZ(djdys.get(0));
                	if(!StringHelper.isEmpty(bdcs_djdy_gz))
                	{
                		List<BDCS_QL_XZ> qls= baseCommonDao.getDataList(BDCS_QL_XZ.class, "DJDYID ='"+bdcs_djdy_gz.getDJDYID()+"' and QLLX ='4'");//获取主体权利
                		if(!StringHelper.isEmpty(qls) && qls.size()>0 )
                		{
                		   BDCS_QL_XZ bdcs_ql_xz =qls.get(0);	
                		   BDCS_QL_GZ bdcs_ql_gz = new BDCS_QL_GZ();
                		   bdcs_ql_gz.setBDCDYH(bdcs_djdy_gz.getBDCDYH());
                		   bdcs_ql_gz.setCZFS(CZFS.FBCZ.Value);
                		   bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);
                		   bdcs_ql_gz.setDJDYID(bdcs_djdy_gz.getDJDYID());
                		   bdcs_ql_gz.setQLLX(xmxx.getQLLX());
                		   bdcs_ql_gz.setDJLX(DJLX.YYDJ.Value);
                		   bdcs_ql_gz.setXMBH(xmxx.getId());
                		   bdcs_ql_gz.setYWH(xmxx.getPROJECT_ID());
                		   bdcs_ql_gz.setLYQLID(bdcs_ql_xz.getId());
	                   		BDCS_FSQL_GZ bdcs_fsql_gz = new BDCS_FSQL_GZ();
	                   		bdcs_fsql_gz.setDJDYID(bdcs_djdy_gz.getDJDYID());
	                   		bdcs_fsql_gz.setXMBH(xmxx.getId());
	                   		bdcs_ql_gz.setFSQLID(bdcs_fsql_gz.getId());
	                		bdcs_fsql_gz.setQLID(bdcs_ql_gz.getId());
	                		
	                		List<HashMap<String, String>> list_yydj = (List<HashMap<String, String>>) projectinfo.get("InfoYYDJ");
	        				if(list_yydj!=null&&list_yydj.size()>0){
	        					bdcs_fsql_gz.setYYSX(list_yydj.get(0).get("YYSX"));
	        					bdcs_ql_gz.setFJ(list_yydj.get(0).get("FJ"));
	        				}
	                		BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
							qdzr.setXMBH(xmxx.getId());
							qdzr.setQLID(bdcs_ql_gz.getId());

							BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
							zs.setXMBH(xmxx.getId());
							qdzr.setZSID(zs.getId());

							qdzr.setBDCDYH(bdcs_ql_gz.getBDCDYH());
							qdzr.setDJDYID(bdcs_ql_gz.getDJDYID());
							qdzr.setFSQLID(bdcs_fsql_gz.getId());

							// 设置为共同持证
							bdcs_ql_gz.setCZFS(CZFS.GTCZ.Value);

							baseCommonDao.save(zs);
							baseCommonDao.save(qdzr);
							// 保存权利和附属权利
							bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
							String  djdyid=SuperHelper.GeneratePrimaryKey();
							bdcs_djdy_gz.setId(djdyid);
							bdcs_djdy_gz.setXMBH(xmxx.getId());
							baseCommonDao.save(bdcs_ql_gz);
							baseCommonDao.save(bdcs_fsql_gz);
							baseCommonDao.save(bdcs_djdy_gz);
                		}                		
                	}
                }
			
                baseCommonDao.flush();				
			}
			}

		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean accept60002(HashMap<String, Object> projectinfo,
			BDCS_XMXX xmxx) {
		if (!projectinfo.containsKey("InfoH")
				|| projectinfo.get("InfoH") == null) {
			return false;
		}
		List<HashMap<String, String>> list_h = (List<HashMap<String, String>>) projectinfo.get("InfoH");
		if (list_h == null || list_h.size() <= 0) {
			return false;
		}
		for (HashMap<String, String> map_h : list_h) {
			boolean fwflag = true;// 默认不存在户信息，则创建
			boolean yyflag = true;// 默认异议登记不存在
			String relationid = StringHelper.formatObject(map_h
					.get("RELATIONID"));
			if (!StringHelper.isEmpty(relationid)) {
				List<RealUnit> units = UnitTools.loadUnits(BDCDYLX.H,DJDYLY.XZ, "RELATIONID ='" + relationid + "'");
				if (!StringHelper.isEmpty(units) && units.size() > 0) {
					fwflag = false;
				}
			}
			if (fwflag) {
				House h = createNewHouse_XZ(map_h,xmxx);
				baseCommonDao.save(h);
				RegisterUnit djdy = createDJDY(h, new BDCS_DJDY_XZ(),xmxx);
				baseCommonDao.save(djdy);
				List<HashMap<String, String>> list_fdcq = (List<HashMap<String, String>>) projectinfo.get("InfoFDCQ");
				if (list_fdcq == null || list_fdcq.size() <= 0) {
					return false;
				}
				Rights rights = createOwnerRightsEx(h, djdy, list_fdcq.get(0),projectinfo);
				baseCommonDao.save(rights);
				baseCommonDao.flush();
			} else {
				List<RealUnit> units = UnitTools.loadUnits(BDCDYLX.H,DJDYLY.XZ, "RELATIONID ='" + relationid + "'");
				String bdcdyid = "";
				if (!StringHelper.isEmpty(units) && units.size() > 0) {
					bdcdyid = units.get(0).getId();
				}
				if (!StringHelper.isEmpty(bdcdyid)) {
					List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
					if (!StringHelper.isEmpty(djdys) && djdys.size() > 0) {
						BDCS_DJDY_GZ bdcs_djdy_gz = ObjectHelper.copyDJDY_XZToGZ(djdys.get(0));
						BDCS_DJDY_XZ bdcs_djdy_xz = ObjectHelper.copyDJDY_GZToXZ(bdcs_djdy_gz);
						if (!StringHelper.isEmpty(bdcs_djdy_xz)) {
							List<BDCS_QL_XZ> qls = baseCommonDao.getDataList(
									BDCS_QL_XZ.class, "DJDYID ='"+ bdcs_djdy_xz.getDJDYID()+ "' and QLLX ='4' AND DJLX='"+ DJLX.YYDJ.Value + "'");// 获取异议登记
							if (!StringHelper.isEmpty(qls) && qls.size() > 0) {
								yyflag = false;
							}
							if (yyflag)// 补录异议登记信息
							{
								List<BDCS_QL_XZ> ztqls = baseCommonDao.getDataList(BDCS_QL_XZ.class,"DJDYID ='"+ bdcs_djdy_gz.getDJDYID()+ "' and QLLX ='4'");// 获取主体权利
								if (!StringHelper.isEmpty(ztqls)&& ztqls.size() > 0) {
									BDCS_QL_XZ bdcs_ql_xz = qls.get(0);
									BDCS_QL_XZ bdcs_ql_xz_yy = new BDCS_QL_XZ();
									bdcs_ql_xz_yy.setBDCDYH(bdcs_djdy_xz.getBDCDYH());
									bdcs_ql_xz_yy.setCZFS(CZFS.FBCZ.Value);
									bdcs_ql_xz_yy.setZSBS(ZSBS.DYB.Value);
									bdcs_ql_xz_yy.setDJDYID(bdcs_djdy_xz.getDJDYID());
									bdcs_ql_xz_yy.setDJLX(DJLX.YYDJ.Value);
									bdcs_ql_xz_yy.setQLLX(QLLX.GYJSYDSHYQ_FWSYQ.Value);
									bdcs_ql_xz_yy.setLYQLID(bdcs_ql_xz.getId());

									BDCS_FSQL_XZ bdcs_fsql_xz_yy = new BDCS_FSQL_XZ();
									bdcs_fsql_xz_yy.setDJDYID(bdcs_djdy_xz.getDJDYID());
									bdcs_ql_xz_yy.setFSQLID(bdcs_fsql_xz_yy.getId());
									bdcs_fsql_xz_yy.setQLID(bdcs_ql_xz_yy.getId());
									List<HashMap<String, String>> list_yydj = (List<HashMap<String, String>>) projectinfo.get("InfoYYDJ");
									if (list_yydj != null&& list_yydj.size() > 0) {
										bdcs_fsql_xz_yy.setYYSX(list_yydj.get(0).get("YYSX"));
										bdcs_ql_xz_yy.setFJ(list_yydj.get(0).get("FJ"));
									}

									BDCS_QDZR_XZ qdzr = new BDCS_QDZR_XZ();
									qdzr.setQLID(bdcs_ql_xz_yy.getId());
									BDCS_ZS_XZ zs = new BDCS_ZS_XZ();

									qdzr.setZSID(zs.getId());
									qdzr.setBDCDYH(bdcs_ql_xz_yy.getBDCDYH());
									qdzr.setDJDYID(bdcs_ql_xz_yy.getDJDYID());
									qdzr.setFSQLID(bdcs_ql_xz_yy.getId());
									// 设置为共同持证
									bdcs_ql_xz_yy.setCZFS(CZFS.GTCZ.Value);

									// 保存权利和附属权利
									bdcs_djdy_xz.setLY(DJDYLY.XZ.Value);
									String djdyid = SuperHelper.GeneratePrimaryKey();
									bdcs_djdy_xz.setId(djdyid);
									baseCommonDao.save(zs);
									baseCommonDao.save(qdzr);
									baseCommonDao.save(bdcs_ql_xz_yy);
									baseCommonDao.save(bdcs_fsql_xz_yy);
									baseCommonDao.save(bdcs_djdy_xz);

									baseCommonDao.save(ObjectHelper.copyZS_XZToLS(zs));
									baseCommonDao.save(ObjectHelper.copyQDZR_XZToLS(qdzr));
									baseCommonDao.save(ObjectHelper.copyQL_XZToLS(bdcs_ql_xz_yy));
									baseCommonDao.save(ObjectHelper.copyFSQL_XZToLS(bdcs_fsql_xz_yy));
									baseCommonDao.save(ObjectHelper.copyDJDY_XZToLS(bdcs_djdy_xz));
								}
							} else {
								List<BDCS_QL_XZ> qls_yy = baseCommonDao .getDataList( BDCS_QL_XZ.class,
												"DJDYID ='" + bdcs_djdy_xz .getDJDYID() + "' and QLLX ='4' AND DJLX='" + DJLX.YYDJ.Value + "'");// 获取异议登记
								if (!StringHelper.isEmpty(qls_yy)
										&& qls_yy.size() > 0) {
									BDCS_QL_XZ bdcs_ql_xz = qls.get(0);
									if (bdcs_ql_xz != null) {
										String gzqlid = SuperHelper .GeneratePrimaryKey();
										String gzfsqlid = SuperHelper .GeneratePrimaryKey();
										StringBuilder builer = new StringBuilder();
										builer.append(" QLID='").append(gzqlid).append("'");
										String strQuery = builer.toString();
										// 拷贝权利
										BDCS_QL_GZ bdcs_ql_gz = ObjectHelper .copyQL_XZToGZ(bdcs_ql_xz);
										bdcs_ql_gz.setId(gzqlid);
										bdcs_ql_gz.setFSQLID(gzfsqlid);
										bdcs_ql_gz.setXMBH(xmxx.getId());
										bdcs_ql_gz .setLYQLID(bdcs_ql_xz.getId());
										bdcs_ql_gz.setDJLX(xmxx.getDJLX());
										bdcs_ql_gz.setDJYY("");
										bdcs_ql_gz.setFJ("");
										baseCommonDao.save(bdcs_ql_gz);
										BDCS_FSQL_XZ bdcs_fsql_xz = baseCommonDao .get(BDCS_FSQL_XZ.class,bdcs_ql_xz.getFSQLID());
										if (bdcs_fsql_xz != null) {
											// 拷贝附属权利
											BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
											bdcs_fsql_gz.setQLID(gzqlid);
											bdcs_fsql_gz.setId(gzfsqlid);
											bdcs_fsql_gz.setXMBH(xmxx.getId());
											bdcs_fsql_gz.setZXDYYWH(xmxx.getPROJECT_ID());
											baseCommonDao.save(bdcs_fsql_gz);
											List<HashMap<String, String>> list_yydj = (List<HashMap<String, String>>) projectinfo.get("InfoYYDJ");
											if (list_yydj != null&& list_yydj.size() > 0) {
												bdcs_fsql_gz.setYYSX(list_yydj.get(0).get("YYSX"));
												bdcs_ql_gz.setFJ(list_yydj.get(0).get("FJ"));
												bdcs_fsql_gz.setZXYYYY(list_yydj.get(0).get("ZXYYYY"));
												bdcs_fsql_gz.setZXFJ(list_yydj.get(0).get("FJ"));
											}
										}
										
										StringBuilder builderDJDY = new StringBuilder();
										builderDJDY.append(" DJDYID='");
										builderDJDY.append(bdcs_ql_xz.getDJDYID());
										builderDJDY.append("'");
										// 获取登记单元集合
										List<BDCS_DJDY_XZ> djdys_yy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,builderDJDY.toString());
										if (djdys_yy != null
												&& djdys_yy.size() > 0) {
											BDCS_DJDY_XZ bdcs_djdy_xz_yy = djdys_yy.get(0);
											// 拷贝登记单元
											BDCS_DJDY_GZ bdcs_djdy_gz_yy = ObjectHelper.copyDJDY_XZToGZ(bdcs_djdy_xz_yy);
											bdcs_djdy_gz_yy.setId((String) SuperHelper.GeneratePrimaryKey());
											bdcs_djdy_gz_yy.setLY(DJDYLY.XZ.Value);
											bdcs_djdy_gz_yy.setXMBH(xmxx.getId());
											baseCommonDao.save(bdcs_djdy_gz);
										}
										// 获取权利人集合
										List<BDCS_QLR_XZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_XZ.class,strQuery);
										if (qlrs != null && qlrs.size() > 0) {
											for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
												BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
												if (bdcs_qlr_xz != null) {
													// 拷贝权利人
													String gzqlrid = SuperHelper.GeneratePrimaryKey();
													BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
													bdcs_qlr_gz.setId(gzqlrid);
													bdcs_qlr_gz.setQLID(gzqlid);
													bdcs_qlr_gz.setXMBH(xmxx.getId());
													baseCommonDao.save(bdcs_qlr_gz);
													//
													// BDCS_SQR
													// sqr=copyXZQLRtoSQR(bdcs_qlr_xz,SQRLB.JF);
													// if(sqr!=null){
													// sqr.setXMBH(xmxx.getId());
													// sqr.setGLQLID(bdcs_ql_gz.getId());
													// sqr.setId((String)SuperHelper.GeneratePrimaryKey());
													// baseCommonDao.save(sqr);
													// }

													// 获取证书集合
													StringBuilder builder = new StringBuilder();
													builder.append(" id IN (");
													builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
													builder.append(gzqlid).append("'").append(" AND QLRID='");
													builder.append(bdcs_qlr_xz.getId()).append("')");
													String strQueryZS = builder.toString();
													List<BDCS_ZS_XZ> zss = baseCommonDao.getDataList(BDCS_ZS_XZ.class,strQueryZS);
													if (zss != null&& zss.size() > 0) {
														for (int izs = 0; izs < zss.size(); izs++) {
															BDCS_ZS_XZ bdcs_zs_xz = zss.get(izs);
															if (bdcs_zs_xz != null) {
																String gzzsid = SuperHelper.GeneratePrimaryKey();
																BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
																bdcs_zs_gz.setId(gzzsid);
																bdcs_zs_gz.setXMBH(xmxx.getId());
																baseCommonDao.save(bdcs_zs_gz);
																// 获取权地证人集合
																StringBuilder builderQDZR = new StringBuilder();
																builderQDZR.append(strQuery);
																builderQDZR.append(" AND ZSID='");
																builderQDZR.append(bdcs_zs_xz.getId());
																builderQDZR.append("' AND QLID='");
																builderQDZR.append(bdcs_ql_xz.getId());
																builderQDZR.append("' AND QLRID='");
																builderQDZR.append(bdcs_qlr_xz.getId());
																builderQDZR.append("')");
																List<BDCS_QDZR_XZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_XZ.class,
																				builderQDZR.toString());
																if (qdzrs != null&& qdzrs.size() > 0) {
																	for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
																		BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
																		if (bdcs_qdzr_xz != null) {
																			// 拷贝权地证人
																			BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
																			bdcs_qdzr_gz.setId((String) SuperHelper.GeneratePrimaryKey());
																			bdcs_qdzr_gz.setZSID(gzzsid);
																			bdcs_qdzr_gz.setQLID(gzqlid);
																			bdcs_qdzr_gz.setFSQLID(gzfsqlid);
																			bdcs_qdzr_gz.setQLRID(gzqlrid);
																			bdcs_qdzr_gz.setXMBH(xmxx.getId());
																			baseCommonDao.save(bdcs_qdzr_gz);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
										bdcs_ql_xz.setDJZT("02");
										baseCommonDao.update(bdcs_ql_xz);
									}
								}
							}
						}
					}
				}

				baseCommonDao.flush();
			}
		}

		return true;
	}
	
	

	@SuppressWarnings("unchecked")
	private HashMap<String, Object> AnalysisXML(Document document) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取根元素
		Element root = document.getRootElement();
		List<Element> root_elements = root.elements();
		Element head_element =null;
		List<Element> h_elements = new ArrayList<Element>();
		List<Element> fdcq_elements = new ArrayList<Element>();
		List<Element> dyaq_elements = new ArrayList<Element>();
		List<Element> ygdj_elements = new ArrayList<Element>();
		List<Element> yydj_elements = new ArrayList<Element>();
		List<Element> cfdj_elements = new ArrayList<Element>();
		List<Element> qlr_elements = new ArrayList<Element>();

		if (root_elements != null && root_elements.size() > 0) {
			for (Element root_element : root_elements) {
				String property_name = root_element.getName();
				if ("Head".endsWith(property_name)) {
					head_element = root_element;
				} else if ("RecordSet".endsWith(property_name)) {
					String bdcdyid=SuperHelper.GeneratePrimaryKey();
					String qlid=SuperHelper.GeneratePrimaryKey();
					HashMap<String, String> para=new HashMap<String, String>();
					para.put("BDCDYID", bdcdyid);
					para.put("QLID", qlid);
					List<Element> recordset_elements = root_element.elements();
					if (recordset_elements != null && recordset_elements.size() > 0) {
						for (Element recordset_element : recordset_elements) {
							String recordset_name = recordset_element.getName();
							if ("Row".endsWith(recordset_name)) {
								List<Element> row_elements = recordset_element.elements();
								if (row_elements != null && row_elements.size() > 0) {
									for (Element entity_element : row_elements) {
										String row_name = entity_element.getName();
										if("FDCQ".endsWith(row_name)){
											fdcq_elements.addAll(getElementsFromRootElement(entity_element,para));
										}else if("H".endsWith(row_name)){
											h_elements.addAll(getElementsFromRootElement(entity_element,para));
										}else if("DYQ".endsWith(row_name)){
											dyaq_elements.addAll(getElementsFromRootElement(entity_element,para));
										}else if("YGDJ".endsWith(row_name)){
											ygdj_elements.addAll(getElementsFromRootElement(entity_element,para));
										}else if("YYDJ".endsWith(row_name)){
											yydj_elements.addAll(getElementsFromRootElement(entity_element,para));
										}else if("CFDJ".endsWith(row_name)){
											cfdj_elements.addAll(getElementsFromRootElement(entity_element,para));
										}else if("QLR".endsWith(row_name)){
											String qlrid=SuperHelper.GeneratePrimaryKey();
											para.put("QLRID", qlrid);
											qlr_elements.addAll(getElementsFromRootElement(entity_element,para));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// 头部信息
		HashMap<String, String> head_map=getMapFromElement(head_element);
		// 获取户信息集合
		List<HashMap<String, String>> list_h= getListMapFromListElement(h_elements);
		// 获取房地产权信息集合
		List<HashMap<String, String>> list_fdcq= getListMapFromListElement(fdcq_elements);
		// 获取抵押权信息集合
		List<HashMap<String, String>> list_dyaq= getListMapFromListElement(dyaq_elements);
		// 获取预告登记信息集合
		List<HashMap<String, String>> list_ygdj= getListMapFromListElement(ygdj_elements);
		// 获取异议登记信息集合
		List<HashMap<String, String>> list_yydj= getListMapFromListElement(yydj_elements);
		// 获取查封登记信息集合
		List<HashMap<String, String>> list_cfdj= getListMapFromListElement(cfdj_elements);
		// 获取权利人信息集合
		List<HashMap<String, String>> list_qlr= getListMapFromListElement(qlr_elements);
		String opcode=head_map.get("OpCode");
		String casenum="";
		if("10004".equals(opcode)){
			if(list_fdcq!=null&&list_fdcq.size()>0){
				casenum=list_fdcq.get(0).get("YWH");
			}
		}else if("20004".equals(opcode)){
			if(list_fdcq!=null&&list_fdcq.size()>0){
				casenum=list_fdcq.get(0).get("YWH");
			}
		}else if("30004".equals(opcode)){
			if(list_fdcq!=null&&list_fdcq.size()>0){
				casenum=list_fdcq.get(0).get("YWH");
			}
		}else if("40004".equals(opcode)){
			if(list_fdcq!=null&&list_fdcq.size()>0){
				casenum=list_fdcq.get(0).get("ZXYWH");
			}
		}else if("10023".equals(opcode)){
			if(list_dyaq!=null&&list_dyaq.size()>0){
				casenum=list_dyaq.get(0).get("YWH");
			}
		}else if("20023".equals(opcode)){
			if(list_dyaq!=null&&list_dyaq.size()>0){
				casenum=list_dyaq.get(0).get("YWH");
			}
		}else if("30023".equals(opcode)){
			if(list_dyaq!=null&&list_dyaq.size()>0){
				casenum=list_dyaq.get(0).get("YWH");
			}
		}else if("40023".equals(opcode)){
			if(list_dyaq!=null&&list_dyaq.size()>0){
				casenum=list_dyaq.get(0).get("ZXDYYWH");
			}
		}else if("60001".equals(opcode)){
			if(list_yydj!=null&&list_yydj.size()>0){
				casenum=list_yydj.get(0).get("YWH");
			}
		}
		else if("60002".equals(opcode)){
			if(list_yydj!=null&&list_yydj.size()>0){
				casenum=list_yydj.get(0).get("ZXDYYWH");
			}
		}else if("70001".equals(opcode)){
			if(list_ygdj!=null&&list_ygdj.size()>0){
				casenum=list_ygdj.get(0).get("YWH");
			}
		}else if("70002".equals(opcode)){
			if(list_ygdj!=null&&list_ygdj.size()>0){
				casenum=list_ygdj.get(0).get("ZXYWH");
			}
		}else if("80001".equals(opcode)){
			if(list_cfdj!=null&&list_cfdj.size()>0){
				casenum=list_cfdj.get(0).get("YWH");
			}
		}else if("80002".equals(opcode)){
			if(list_cfdj!=null&&list_cfdj.size()>0){
				casenum=list_cfdj.get(0).get("JFYWH");
			}
		}
		head_map.put("CaseNum", casenum);
		map.put("InfoHEAD", head_map);
		map.put("InfoH", list_h);
		map.put("InfoFDCQ", list_fdcq);
		map.put("InfoDYAQ", list_dyaq);
		map.put("InfoYGDJ", list_ygdj);
		map.put("InfoYYDJ", list_yydj);
		map.put("InfoCFDJ", list_cfdj);
		map.put("InfoQLR", list_qlr);		
		return map;
	}
	
	@SuppressWarnings({"unused" })
	private Document GetXMLDocument(String strXml) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new ByteArrayInputStream(strXml
				     .getBytes("GBK")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (document == null) {
			return null;
		}
//		document.getRootElement().element("HEAD").element("RetCode").addText("0000");
//		document.getRootElement().element("HEAD").element("RetMsg").addText("成功！");
		return document;
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String, String> getMapFromElement(Element element){
		HashMap<String, String> map = new HashMap<String, String>();
		List<Element> property_elements = element.elements();
		for (Element property_element : property_elements) {
			String property_name = property_element.getName();
			String property_value = property_element.getText();
			if (!map.containsKey(property_name)) {
				map.put(property_name, property_value);
			}
		}
		return map;
	}
	
	private List<HashMap<String, String>> getListMapFromListElement(List<Element> list_element){
		List<HashMap<String, String>> list_map= new ArrayList<HashMap<String, String>>();
		if (list_element != null && list_element.size() > 0) {
			for (Element element : list_element) {
				HashMap<String, String> map = getMapFromElement(element);
				list_map.add(map);
			}
		}
		return list_map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Element> getElementsFromRootElement(Element element,HashMap<String,String> para){
		String relationid="";
		List<Element> list_element= new ArrayList<Element>();
		List<Element> root_elements_entity = element.elements();
		if (root_elements_entity != null && root_elements_entity.size() > 0) {
			for (Element root_element_entity : root_elements_entity) {
				String root_name_entity = root_element_entity.getName();
				if ("RecordSet".endsWith(root_name_entity)) {
					List<Element> recordset_elements_entity = root_element_entity.elements();
					if (recordset_elements_entity != null && recordset_elements_entity.size() > 0) {
						for (Element recordset_element_entity : recordset_elements_entity) {
							String recordset_name_entity = recordset_element_entity.getName();
							if ("Row".endsWith(recordset_name_entity)) {
								List<Element> row_elements_entity = recordset_element_entity.elements();
								if (row_elements_entity != null && row_elements_entity.size() > 0) {
									for (Element row_element_entity : row_elements_entity) {
										String row_name_entity = row_element_entity.getName();
										if("BDCDYID".equals(row_name_entity)){
											relationid=row_element_entity.getText();
										}
									}
								}
							}
						}
					}
				}
			}
		}
		para.put("RELATIONID", relationid);
		if (root_elements_entity != null && root_elements_entity.size() > 0) {
			for (Element root_element_entity : root_elements_entity) {
				String root_name_entity = root_element_entity.getName();
				if ("RecordSet".endsWith(root_name_entity)) {
					List<Element> recordset_elements_entity = root_element_entity.elements();
					if (recordset_elements_entity != null && recordset_elements_entity.size() > 0) {
						for (Element recordset_element_entity : recordset_elements_entity) {
							String recordset_name_entity = recordset_element_entity.getName();
							if ("Row".endsWith(recordset_name_entity)) {
								List<Element> row_elements_entity = recordset_element_entity.elements();
								if (row_elements_entity != null && row_elements_entity.size() > 0) {
									for (Element row_element_entity : row_elements_entity) {
										String row_name_entity = row_element_entity.getName();
										Set set = para.keySet();
										Iterator iterator = set.iterator();
										while (iterator.hasNext()) {
											String name = StringHelper.formatObject(iterator.next());
											String value = para.get(name);
											if (name.equals(row_name_entity)) {
												row_element_entity.setText(value);
											}
										}
										
									}
								}
								list_element.add(recordset_element_entity);
							}
						}
					}
				}
			}
		}
		return list_element;
	}
	
	/*****************************************生成xml*****************************************/
	@Override
	public String createXMLtest(String xmbh) {
        
		String bdcdbxml="";
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(DJLX.CSDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-首次登记
		{
			bdcdbxml=createCommonBDCDBByDJDY(xmbh,info);
		}		
		else if(DJLX.CSDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))//抵押权登记-首次登记
		{
			bdcdbxml=createCommonBDCDBByDJDY(xmbh,info);
		}		
		else if(DJLX.ZYDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-转移登记
		{
			bdcdbxml=createCommonBDCDBByDJDY(xmbh,info);
		}	
		else if(DJLX.BGDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-变更登记
		{
		
		}	
		else if(DJLX.BGDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))//抵押权登记-变更登记
		{
			
		}
		else if(DJLX.ZXDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-注销登记和预告登记注销-无
		{
			bdcdbxml=createSYQZXBDCDB(xmbh,info);
		}
		else if(DJLX.ZXDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))//抵押权登记-注销登记
		{
			bdcdbxml=createCommonBDCDBByDJDY(xmbh,info);
		}
		else if(DJLX.YGDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//预告登记
		{
			bdcdbxml=createCommonBDCDBByDJDY(xmbh,info);
		}		
		else if(DJLX.CFDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))	//查封登记
		{
			bdcdbxml=CreateCFBDCDB(xmbh,info);
		}		
		else if(DJLX.CFDJ.Value.equals(info.getDjlx()) && QLLX.CFZX.Value.equals(info.getQllx()))//查封登记-注销
		{
			bdcdbxml=createCommonBDCDBByDJDY(xmbh,info);
		}
		else if(DJLX.YYDJ.Value.equals(info.getQllx()) &&(QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//异义登记
		{
			bdcdbxml=createCommonBDCDBByDJDY(xmbh,info);
		}
		//异议登记注销-无
		
		return bdcdbxml;
	}
	
	@Override
	public List<String> createXMLList(String xmbh) {
        
	    List<String> lst=new ArrayList<String>();
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(DJLX.CSDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-首次登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}		
		else if(DJLX.CSDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))//抵押权登记-首次登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}		
		else if(DJLX.ZYDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-转移登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}	
		else if(DJLX.BGDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-变更登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}	
		else if(DJLX.BGDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))//抵押权登记-变更登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}
		else if(DJLX.ZXDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//所有权登记-注销登记和预告登记注销-无
		{
			lst=createSYQZXBDCDBEx(xmbh,info);
		}
		else if(DJLX.ZXDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))//抵押权登记-注销登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}
		else if(DJLX.YGDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//预告登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}		
		else if(DJLX.CFDJ.Value.equals(info.getDjlx()) && (QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))	//查封登记
		{
			lst=CreateCFBDCDBEx(xmbh,info);
		}		
		else if(DJLX.CFDJ.Value.equals(info.getDjlx()) && QLLX.CFZX.Value.equals(info.getQllx()))//查封登记-注销
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}
		else if(DJLX.YYDJ.Value.equals(info.getQllx()) &&(QLLX.ZJDSYQ_FWSYQ.Value.equals(info.getQllx())|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(info.getQllx()) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))//异义登记
		{
			lst=createCommonBDCDBbyDJDYEx(xmbh,info);
		}
		//异议登记注销-无
		
		return lst;
	}
	/**
	 * 通用不动产登簿信息--从证书入口
	 * @param xmbh
	 * @param info
	 * @return
	 */
	protected String createCommonBDCDB(String xmbh,ProjectInfo info)
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("Root");
		Element head = root.addElement("Head");
		Element recordsets = root.addElement("RecordSet");
		Element rows = recordsets.addElement("Row");
		Element bdcdb = rows.addElement("BDCDB");		
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
		int count = 0;	
	
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class, xmbhSql);
		if (!StringHelper.isEmpty(zss) && zss.size() > 0) {
			for (BDCS_ZS_GZ zs : zss) {
				// 获取项目信息
				count++;
				Element recordset = bdcdb.addElement("RecordSet");
				Element row = recordset.addElement("Row");
				// 获取证书节点信息
				getZS(row, zs, info);
				Rights right = RightsTools.loadRightsByZSID(DJDYLY.GZ,
						zs.getId(), xmbh);
				if (!StringHelper.isEmpty(right)) {
					// 获取权利节点信息
					getRight(row, right);
					DJDYLY dyly = DJDYLY.XZ;
					if (DJLX.CSDJ.Value.equals(info.getDjlx())
							|| DJLX.GZDJ.Value.equals(info.getDjlx()))// 没有考虑分割与合并
					{
						dyly = DJDYLY.GZ;
					}
					if(QLLX.DIYQ.Value.equals(info.getQllx()))
					{
						dyly=DJDYLY.XZ;
					}
					List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
							BDCS_DJDY_GZ.class, "DJDYID='" + right.getDJDYID()
									+ "'");
					if (!StringHelper.isEmpty(djdys) && djdys.size() > 0) {
						BDCS_DJDY_GZ djdy = djdys.get(0);
						if(BDCDYLX.H.equals(bdcdylx)|| BDCDYLX.YCH.equals(bdcdylx))
						{
						RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly,
								djdy.getBDCDYID());
						if (!StringHelper.isEmpty(unit)) {
							House house = (House) unit;
							// 获取单元节点信息
							getUnit(row, house, bdcdylx);
						} else {
							clearHouse(row);
							clearUseLand(row);
						}
						}
					}
				} else {
					clearRight(row);
					clearHouse(row);
					clearUseLand(row);
				}
				// 获取登记发证节点信息
				getDJSZ(row, xmbhSql);
				// 获取登记缮证节点信息
				getDJFZ(row, xmbhSql);
			}
		} else {
			Element recordset = bdcdb.addElement("RecordSet");
			Element row = recordset.addElement("Row");
			clearZS(row);
			clearRight(row);
			clearHouse(row);
			clearUseLand(row);
			clearDJSZ(row);
			clearDJFZ(row);
		}
		getHead(head,count,info);
		return document.asXML();
	}
    /**
     * 通用不动产登簿信息-从登记单元入口
     * @param xmbh
     * @param info
     * @return
     */
	@SuppressWarnings("rawtypes")
	protected String createCommonBDCDBByDJDY(String xmbh,ProjectInfo info)
	{
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("Root");
		Element head = root.addElement("Head");
		Element recordsets = root.addElement("RecordSet");
		Element rows = recordsets.addElement("Row");
		Element bdcdb = rows.addElement("BDCDB");		
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
		int count = 0;	
	    List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhSql);
	    if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
	    {
			for (BDCS_DJDY_GZ djdy : djdys) {
				// 获取项目信息
				count++;
				Element recordset = bdcdb.addElement("RecordSet");
				Element row = recordset.addElement("Row");
				DJDYLY dyly = DJDYLY.initFrom(djdy.getLY());
				if (BDCDYLX.H.equals(bdcdylx) || BDCDYLX.YCH.equals(bdcdylx)) {
					RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly,
							djdy.getBDCDYID());
					if (!StringHelper.isEmpty(unit)) {
						House house = (House) unit;
						// 获取单元节点信息
						getUnit(row, house, bdcdylx);
					} else {
						clearHouse(row);
						clearUseLand(row);
					}
				}
				else
				{
					clearHouse(row);
					clearUseLand(row);
				}
				Rights rights =RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getId());
				if (!StringHelper.isEmpty(rights)) {
					// 获取权利节点信息
					getRight(row, rights);
				}
				else
				{
					clearRight(row);
				}
				List<Map> zss= baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_ZS_GZ WHERE ZSID IN (SELECT ZSID FROM BDCK.BDCS_QDZR_GZ WHERE DJDYID ='"+djdy.getDJDYID()+"')");
				getZSEx(row,zss,info);
				// 获取登记发证节点信息
				getDJSZ(row, xmbhSql);
				// 获取登记缮证节点信息
				getDJFZ(row, xmbhSql);
			}	    	
	    }
	    else
	    {
	    	Element recordset = bdcdb.addElement("RecordSet");
			Element row = recordset.addElement("Row");
			clearZS(row);
			clearRight(row);
			clearHouse(row);
			clearUseLand(row);
			clearDJSZ(row);
			clearDJFZ(row);
	    }	
		getHead(head,count,info);
		return document.asXML();
	}
  
	/**
     * 通用不动产登记信息-从登记单元入口(每个登记单元发送)	
     * @param xmbh
     * @param info
     * @return
     */
	@SuppressWarnings("rawtypes")
	protected List<String> createCommonBDCDBbyDJDYEx(String xmbh,ProjectInfo info)
	{
		List<String> lst=new ArrayList<String>();	
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
	    List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhSql);
	    if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
	    {
			for (BDCS_DJDY_GZ djdy : djdys) {
				Document document = DocumentHelper.createDocument();
				document.setXMLEncoding("GBK");
				Element root = document.addElement("Root");
				Element head = root.addElement("Head");
				Element recordsets = root.addElement("RecordSet");
				Element rows = recordsets.addElement("Row");
				Element bdcdb = rows.addElement("BDCDB");						
				int count = 0;	
				// 获取项目信息
				count++;
				Element recordset = bdcdb.addElement("RecordSet");
				Element row = recordset.addElement("Row");
				DJDYLY dyly=DJDYLY.initFrom(djdy.getLY());
				if (BDCDYLX.H.equals(bdcdylx) || BDCDYLX.YCH.equals(bdcdylx)) {
					RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly,
							djdy.getBDCDYID());
					if (!StringHelper.isEmpty(unit)) {
						House house = (House) unit;
						// 获取单元节点信息
						getUnit(row, house, bdcdylx);
					} else {
						clearHouse(row);
						clearUseLand(row);
					}
				}
				else
				{
					clearHouse(row);
					clearUseLand(row);
				}
				Rights rights =RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
				if (!StringHelper.isEmpty(rights)) {
					// 获取权利节点信息
					getRight(row, rights);
				}
				else
				{
					clearRight(row);
				}
				List<Map> zss= baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_ZS_GZ WHERE ZSID IN (SELECT ZSID FROM BDCK.BDCS_QDZR_GZ WHERE DJDYID ='"+djdy.getDJDYID()+"')");
				getZSEx(row,zss,info);
				// 获取登记发证节点信息
				getDJSZ(row, xmbhSql);
				// 获取登记缮证节点信息
				getDJFZ(row, xmbhSql);
				getHead(head,count,info);
				lst.add(document.asXML());
			}	    	
	    }
	    else
	    {
	    	Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("GBK");
			Element root = document.addElement("Root");
			Element head = root.addElement("Head");
			Element recordsets = root.addElement("RecordSet");
			Element rows = recordsets.addElement("Row");
			Element bdcdb = rows.addElement("BDCDB");						
			int count = 0;	
	    	Element recordset = bdcdb.addElement("RecordSet");
			Element row = recordset.addElement("Row");
			clearZS(row);
			clearRight(row);
			clearHouse(row);
			clearUseLand(row);
			clearDJSZ(row);
			clearDJFZ(row);
			getHead(head,count,info);
			lst.add(document.asXML());
	    }					
		return  lst;
	}
	
	/**
	 * 主体权利的注销-从登记单元入口-主体单元从历史层获取
	 * @param xmbh
	 * @param info
	 * @return
	 */
	protected String createSYQZXBDCDB(String xmbh,ProjectInfo info)
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("Root");
		Element head = root.addElement("Head");
		Element recordsets = root.addElement("RecordSet");
		Element rows = recordsets.addElement("Row");
		Element bdcdb = rows.addElement("BDCDB");		
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
		int count = 0;	
	   List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhSql);
	   if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
	   {
			for (BDCS_DJDY_GZ djdy : djdys) {
				// 获取项目信息
				count++;
				Element recordset = bdcdb.addElement("RecordSet");
				Element row = recordset.addElement("Row");
				if (!StringHelper.isEmpty(djdy.getBDCDYID())) {
					RealUnit unit = UnitTools.loadUnit(bdcdylx, DJDYLY.LS,
							djdy.getBDCDYID());// 获取节点单元信息
					if (!StringHelper.isEmpty(unit)) {
						House house = (House) unit;
						// 获取单元节点信息
						getUnit(row, house, bdcdylx);
					} else {
						clearHouse(row);
						clearUseLand(row);
					}
				} else {
					clearHouse(row);
					clearUseLand(row);
				}
				if(!StringHelper.isEmpty(djdy.getDJDYID()))
				{
					Rights right=RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
					if(!StringHelper.isEmpty(right))
					{
						// 获取权利节点信息
						getRight(row, right);
					}
					else
					{
						clearRight(row);
					}
				}
				else
				{
					clearRight(row);
				}
				// 获取登记发证节点信息
				getDJSZ(row, xmbhSql);
				// 获取登记缮证节点信息
				getDJFZ(row, xmbhSql);
			}		
	   }
	   else
	   {
		   Element recordset = bdcdb.addElement("RecordSet");
			Element row = recordset.addElement("Row");
			clearZS(row);
			clearRight(row);
			clearHouse(row);
			clearUseLand(row);
			clearDJSZ(row);
			clearDJFZ(row);
	   }
		getHead(head,count,info);
		return document.asXML();
	}
	
	
	/**
	 * 主体权利的注销-从登记单元入口-主体单元从历史层获取
	 * @param xmbh
	 * @param info
	 * @return
	 */
	protected List<String> createSYQZXBDCDBEx(String xmbh,ProjectInfo info)
	{
		List<String> lst=new ArrayList<String>();
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
	   List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhSql);
	   if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
	   {
			for (BDCS_DJDY_GZ djdy : djdys) {
				Document document = DocumentHelper.createDocument();
				document.setXMLEncoding("GBK");
				Element root = document.addElement("Root");
				Element head = root.addElement("Head");
				Element recordsets = root.addElement("RecordSet");
				Element rows = recordsets.addElement("Row");
				Element bdcdb = rows.addElement("BDCDB");						
				int count = 0;	
				// 获取项目信息
				count++;
				Element recordset = bdcdb.addElement("RecordSet");
				Element row = recordset.addElement("Row");
				if (!StringHelper.isEmpty(djdy.getBDCDYID())) {
					RealUnit unit = UnitTools.loadUnit(bdcdylx, DJDYLY.LS,
							djdy.getBDCDYID());// 获取节点单元信息
					if (!StringHelper.isEmpty(unit)) {
						House house = (House) unit;
						// 获取单元节点信息
						getUnit(row, house, bdcdylx);
					} else {
						clearHouse(row);
						clearUseLand(row);
					}
				} else {
					clearHouse(row);
					clearUseLand(row);
				}
				if(!StringHelper.isEmpty(djdy.getDJDYID()))
				{
					Rights right=RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
					if(!StringHelper.isEmpty(right))
					{
						// 获取权利节点信息
						getRight(row, right);
					}
					else
					{
						clearRight(row);
					}
				}
				else
				{
					clearRight(row);
				}
				// 获取登记发证节点信息
				getDJSZ(row, xmbhSql);
				// 获取登记缮证节点信息
				getDJFZ(row, xmbhSql);
				getHead(head,count,info);
				lst.add(document.asXML());
			}		
	   }
	   else
	   {
		   Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("GBK");
			Element root = document.addElement("Root");
			Element head = root.addElement("Head");
			Element recordsets = root.addElement("RecordSet");
			Element rows = recordsets.addElement("Row");
			Element bdcdb = rows.addElement("BDCDB");						
			int count = 0;	
		   Element recordset = bdcdb.addElement("RecordSet");
			Element row = recordset.addElement("Row");
			clearZS(row);
			clearRight(row);
			clearHouse(row);
			clearUseLand(row);
			clearDJSZ(row);
			clearDJFZ(row);
			getHead(head,count,info);
			lst.add(document.asXML());
	   }	
		return lst;
	}
	
	/**
	 * 查封登记-从登记单元入口-主体单元从现状层获取
	 * @param xmbh
	 * @param info
	 * @return
	 */
	protected String CreateCFBDCDB(String xmbh,ProjectInfo info)
	{
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("Root");
		Element head = root.addElement("Head");
		Element recordsets = root.addElement("RecordSet");
		Element rows = recordsets.addElement("Row");
		Element bdcdb = rows.addElement("BDCDB");		
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
		int count = 0;	
	   List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhSql);
	   if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
	   {
			for (BDCS_DJDY_GZ djdy : djdys) {
				// 获取项目信息
				count++;
				Element recordset = bdcdb.addElement("RecordSet");
				Element row = recordset.addElement("Row");
				if (!StringHelper.isEmpty(djdy.getBDCDYID())) {
					RealUnit unit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ,
							djdy.getBDCDYID());// 获取节点单元信息
					if (!StringHelper.isEmpty(unit)) {
						House house = (House) unit;
						// 获取单元节点信息
						getUnit(row, house, bdcdylx);
					} else {
						clearHouse(row);
						clearUseLand(row);
					}
				} else {
					clearHouse(row);
					clearUseLand(row);
				}
				if(!StringHelper.isEmpty(djdy.getDJDYID()))
				{
					Rights right=RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
					if(!StringHelper.isEmpty(right))
					{
						// 获取权利节点信息
						getRight(row, right);
					}
					else
					{
						clearRight(row);
					}
				}
				else
				{
					clearRight(row);
				}
				// 获取登记发证节点信息
				getDJSZ(row, xmbhSql);
				// 获取登记缮证节点信息
				getDJFZ(row, xmbhSql);
			}		
	   }
	   else
	   {
		   Element recordset = bdcdb.addElement("RecordSet");
			Element row = recordset.addElement("Row");
			clearZS(row);
			clearRight(row);
			clearHouse(row);
			clearUseLand(row);
			clearDJSZ(row);
			clearDJFZ(row);
	   }
		getHead(head,count,info);
		return document.asXML();
	}

	
	/**
	 * 查封登记-从登记单元入口-主体单元从现状层获取
	 * @param xmbh
	 * @param info
	 * @return
	 */
	protected List<String> CreateCFBDCDBEx(String xmbh,ProjectInfo info)
	{
		List<String> lst=new ArrayList<String>();
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
	   List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhSql);
	   if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
	   {
			for (BDCS_DJDY_GZ djdy : djdys) {
				Document document = DocumentHelper.createDocument();
				document.setXMLEncoding("GBK");
				Element root = document.addElement("Root");
				Element head = root.addElement("Head");
				Element recordsets = root.addElement("RecordSet");
				Element rows = recordsets.addElement("Row");
				Element bdcdb = rows.addElement("BDCDB");					
				int count = 0;	
				// 获取项目信息
				count++;
				Element recordset = bdcdb.addElement("RecordSet");
				Element row = recordset.addElement("Row");
					
				if (!StringHelper.isEmpty(djdy.getBDCDYID())) {
					RealUnit unit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ,
							djdy.getBDCDYID());// 获取节点单元信息
					if (!StringHelper.isEmpty(unit)) {
						House house = (House) unit;
						// 获取单元节点信息
						getUnit(row, house, bdcdylx);
					} else {
						clearHouse(row);
						clearUseLand(row);
					}
				} else {
					clearHouse(row);
					clearUseLand(row);
				}
				if(!StringHelper.isEmpty(djdy.getDJDYID()))
				{
					Rights right=RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
					if(!StringHelper.isEmpty(right))
					{
						// 获取权利节点信息
						getRight(row, right);
					}
					else
					{
						clearRight(row);
					}
				}
				else
				{
					clearRight(row);
				}
				// 获取登记发证节点信息
				getDJSZ(row, xmbhSql);
				// 获取登记缮证节点信息
				getDJFZ(row, xmbhSql);
				getHead(head,count,info);
				lst.add(document.asXML());
			}		
	   }
	   else
	   {
		   Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("GBK");
			Element root = document.addElement("Root");
			Element head = root.addElement("Head");
			Element recordsets = root.addElement("RecordSet");
			Element rows = recordsets.addElement("Row");
			Element bdcdb = rows.addElement("BDCDB");					
			int count = 0;	
		   Element recordset = bdcdb.addElement("RecordSet");
			Element row = recordset.addElement("Row");
			clearZS(row);
			clearRight(row);
			clearHouse(row);
			clearUseLand(row);
			clearDJSZ(row);
			clearDJFZ(row);
			getHead(head,count,info);
			lst.add(document.asXML());
	   }
	
		return lst;
	}
	
	protected Element getHead(Element head,int count,ProjectInfo info)
	{
		// 创建头部节点
		
		Element opcode = head.addElement("OpCode");	
		opcode.addText("90001");
//		if(DJLX.CSDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))
//		opcode.addText("10023");
//		else if(DJLX.CSDJ.Value.equals(info.getDjlx()) && !QLLX.DIYQ.Value.equals(info.getQllx()))
//		{
//			opcode.addText("10004");
//		}
//		else if(DJLX.ZYDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))
//		{
//			opcode.addText("20023");
//		}
//		else if(DJLX.ZYDJ.Value.equals(info.getDjlx()) && !QLLX.DIYQ.Value.equals(info.getQllx()))
//		{
//			opcode.addText("20004");
//		}
//		else if(DJLX.ZXDJ.Value.equals(info.getDjlx()) && QLLX.DIYQ.Value.equals(info.getQllx()))
//		{
//			opcode.addText("30023");
//		}
//		else if(DJLX.ZXDJ.Value.equals(info.getDjlx()) && !QLLX.DIYQ.Value.equals(info.getQllx()))
//		{
//			opcode.addText("30004");
//		}
			Date dt = new Date();
		Element opdate = head.addElement("OpDate");
		opdate.addText(StringHelper.FormatDateOnType(dt, "yyyyMMdd"));
		Element optime = head.addElement("OpTime");
		optime.addText(StringHelper.FormatDateOnType(dt, "hhmmss"));
		Element infocount = head.addElement("InfoCount");
		infocount.addText(StringHelper.formatDouble(count));
		head.addElement("RetCode");
		head.addElement("RetMsg");
		return head;
	}
	/**
	 * 获取证书节点信息
	 * @param row
	 * @param zs
	 * @param info
	 * @return
	 */
	protected Element getZS(Element row,BDCS_ZS_GZ zs,ProjectInfo info)
	{
		if (!StringHelper.isEmpty(info)) {
			if (DJLX.YGDJ.Value.equals(info.getDjlx())
					|| QLLX.DIYQ.Value.equals(info.getQllx())) {
				Element bdczmh = row.addElement("BDCZMH");
				bdczmh.addText(StringHelper.FormatByDatatype(zs.getBDCQZH()));
				Element bdcqzh = row.addElement("BDCQZH");
				bdcqzh.addText("");
			} else {
				Element bdczmh = row.addElement("BDCZMH");
				bdczmh.addText("");
				Element bdcqzh = row.addElement("BDCQZH");
				bdcqzh.addText(StringHelper.FormatByDatatype(zs.getBDCQZH()));
			}
		}
		Element ysbh = row.addElement("YSBH");
		ysbh.addText(StringHelper.FormatByDatatype(zs.getZSBH()));
		
		Element ewm=row.addElement("RWM");
		ewm.addText("");
		return row;
	}
	/**
	 * 通过登记单元获取证书信息
	 * @param row
	 * @param zss
	 * @param info
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Element getZSEx(Element row,List<Map> zss,ProjectInfo info)
	{
		boolean flag=true;
		String bdczh="";
		String zsbh="";
		if(!StringHelper.isEmpty(zss) && zss.size()>0)
		{
			for(Map zs : zss)
			{
				if(flag)
				{
					flag=false;
					bdczh=StringHelper.FormatByDatatype(zs.get("BDCQZH"));
					zsbh=StringHelper.FormatByDatatype(zs.get("ZSBH"));
				}
				else
				{
					bdczh=bdczh+","+StringHelper.FormatByDatatype(zs.get("BDCQZH"));
					zsbh=zsbh+","+StringHelper.FormatByDatatype(zs.get("ZSBH"));
				}
			}
		if (!StringHelper.isEmpty(info)) {
			if (DJLX.YGDJ.Value.equals(info.getDjlx())
					|| QLLX.DIYQ.Value.equals(info.getQllx())) {
				Element bdczmh = row.addElement("BDCZMH");
				bdczmh.addText(bdczh);
				Element bdcqzh = row.addElement("BDCQZH");
				bdcqzh.addText("");
			} else {
				Element bdczmh = row.addElement("BDCZMH");
				bdczmh.addText("");
				Element bdcqzh = row.addElement("BDCQZH");
				bdcqzh.addText(bdczh);
			}
		}
		Element ysbh = row.addElement("YSBH");
		ysbh.addText(zsbh);
		Element ewm=row.addElement("RWM");
		ewm.addText("");
		}
		else
		{
			clearZS(row);
		}
		return row;
	}
	/**
	 * 在证书信息为空时，添加默认节点
	 * @param row
	 * @return
	 */
	protected Element clearZS(Element row)
	{
		Element bdczmh = row.addElement("BDCZMH");
		bdczmh.addText("");
		Element bdcqzh = row.addElement("BDCQZH");
		bdcqzh.addText("");
		Element ysbh = row.addElement("YSBH");
		ysbh.addText("");
		Element ewm=row.addElement("RWM");
		ewm.addText("");
		return row;
	}
	/**
	 * 获取权利信息
	 * @param row
	 * @param right
	 * @return
	 */
	protected Element getRight(Element row,Rights right)
	{
		Element tdsyqr = row.addElement("TDSYQR");
		tdsyqr.addText(StringHelper.FormatByDatatype(right.getTDSHYQR()));
		
		Element tdsyqqsrq = row.addElement("TDSYQSRQ");
		tdsyqqsrq.addText(StringHelper.FormatDateOnType(right.getQLQSSJ(), "yyyyMMdd"));
		
		Element tdsyqjsrq = row.addElement("TDSYJSRQ");
		tdsyqjsrq.addText(StringHelper.FormatDateOnType(right.getQLJSSJ(), "yyyyMMdd"));
		
		Element djjg = row.addElement("DJJG");
		djjg.addText(StringHelper.FormatByDatatype(right.getDJJG()));
		
		Element djsj = row.addElement("DJSJ");
		
		djsj.addText(StringHelper.FormatDateOnType(right.getDJSJ(), "yyyyMMddhhmmss"));
		
		Element bdcdyh=row.addElement("BDCDYH");
		bdcdyh.addText(StringHelper.FormatByDatatype(right.getBDCDYH()));
		
		Element ywh=row.addElement("YWH");
		ywh.addText(StringHelper.FormatByDatatype(right.getCASENUM()));
		
		Element djlx=row.addElement("DJLX");		
		djlx.addText(StringHelper.FormatByDatatype(right.getDJLX()));
		Element thyy =row.addElement("THYY");
		thyy.addText("");
		Element bz=row.addElement("BZ");
		BDCS_QL_GZ bdcs_ql_gz =(BDCS_QL_GZ) right;
		bz.addText(StringHelper.FormatByDatatype(bdcs_ql_gz.getBZ()));
		
		Element tqlrgx =row.addElement("TQLRGX");
		tqlrgx.addText("");
		Element zt=row.addElement("DJZT");
		zt.addText(StringHelper.FormatByDatatype(right.getZT()));
		
		return row;
	}
	
	/**
	 * 在不存在权利信息时要添加默认节点
	 * @param row
	 * @return
	 */
	protected Element clearRight(Element row)
	{
		Element tdsyqr = row.addElement("TDSYQR");
		tdsyqr.addText("");
		
		Element tdsyqqsrq = row.addElement("TDSYQSRQ");
		tdsyqqsrq.addText("");
		
		Element tdsyqjsrq = row.addElement("TDSYJSRQ");
		tdsyqjsrq.addText("");
		
		Element djjg = row.addElement("DJJG");
		djjg.addText("");
		
		Element djsj = row.addElement("DJSJ");
		djsj.addText("");
		
		Element bdcdyh=row.addElement("BDCDYH");
		bdcdyh.addText("");
		
		Element ywh=row.addElement("YWH");
		ywh.addText("");
		
		Element djlx=row.addElement("DJLX");		
		djlx.addText("");
		
		Element thyy =row.addElement("THYY");
		thyy.addText("");
		Element bz=row.addElement("BZ");
		bz.addText("");
		
		Element tqlrgx =row.addElement("TQLRGX");
		tqlrgx.addText("");
		Element zt=row.addElement("DJZT");
		zt.addText("");
		return row;
	}
	/**
	 * 获取单元节点信息
	 * @param row 父节点
	 * @param djdyid 登记单元id
	 * @param bdcdylx 不动产单元类型
	 * @param dyly 登记单元来源
	 * @return
	 */
	protected Element getUnit(Element row,House house,BDCDYLX bdcdylx)
	{
				Element dytdmj = row.addElement("DYTDMJ");
				dytdmj.addText(StringHelper.formatDouble(house.getDYTDMJ()));
				
				Element fttdmj = row.addElement("FTTDMJ");
				fttdmj.addText(StringHelper.formatDouble(house.getFTTDMJ()));
		
				if (BDCDYLX.H.equals(bdcdylx)) {
					
					Element zyjzmj = row.addElement("ZYJZMJ");
					zyjzmj.addText(StringHelper.formatDouble(house.getSCJZMJ()));
					
					Element ftjzmj = row.addElement("FTJZMJ");
					ftjzmj.addText(StringHelper.formatDouble(house.getSCFTJZMJ()));
					
				} else {
					
					Element zyjzmj = row.addElement("ZYJZMJ");
					zyjzmj.addText(StringHelper.formatDouble(house.getYCJZMJ()));	
					Element ftjzmj = row.addElement("FTJZMJ");
					ftjzmj.addText(StringHelper.formatDouble(house.getYCFTJZMJ()));
				}
				
				Element qx = row.addElement("QX");
				qx.addText(StringHelper.FormatByDatatype(house.getQXDM()));	
				if(!StringHelper.isEmpty(house.getZDBDCDYID())){
					RealUnit zdunit = UnitTools.loadUnit(
							BDCDYLX.SHYQZD, DJDYLY.XZ,
							house.getZDBDCDYID());
					if (!StringHelper.isEmpty(zdunit)) {
						UseLand useland = (UseLand) zdunit;
						
						Element zddm = row.addElement("ZDDM");
						zddm.addText(StringHelper.FormatByDatatype(useland.getZDDM()));	
						
						Element tdqllx = row.addElement("TDQLLX");
						tdqllx.addText(StringHelper.FormatByDatatype(useland.getQLLX()));	
						
						Element tdqlxz = row.addElement("TDQLXZ");
						tdqlxz.addText(StringHelper.FormatByDatatype(useland.getQLXZ()));							
					}
					else
					{
						clearHouse(row);							
					}
				}
				
		return row;
	}
/**
 * 在不存在房屋信息时要添加默认节点
 * @param row
 * @return
 */
	protected Element clearHouse(Element row)
	{
		Element dytdmj = row.addElement("DYTDMJ");
		dytdmj.addText("");
		
		Element fttdmj = row.addElement("FTTDMJ");
		fttdmj.addText("");
		
		Element zyjzmj = row.addElement("ZYJZMJ");
		zyjzmj.addText("");
		
		Element ftjzmj = row.addElement("FTJZMJ");
		ftjzmj.addText("");
		return row;
	}
	/**
	 * 在不存在土地信息时要添加默认节点
	 * @param row
	 * @return
	 */
	protected Element clearUseLand(Element row)
	{
		Element zddm = row.addElement("ZDDM");
		zddm.addText("");	
		
		Element tdqllx = row.addElement("TDQLLX");
		tdqllx.addText("");	
		
		Element tdqlxz = row.addElement("TDQLXZ");
		tdqlxz.addText("");		
		return row;
	}
	/**
	 * 从BDCS_DJSZ中获取节点需要的信息
	 * @param row，父节点
	 * @param xmbhSql 项目编号sql
	 * @return
	 */
	protected Element getDJSZ(Element row,String xmbhSql)
	{
			List<BDCS_DJSZ> djszs = baseCommonDao.getDataList(
					BDCS_DJSZ.class, xmbhSql);
			if (!StringHelper.isEmpty(djszs)
					&& djszs.size() > 0) {
				BDCS_DJSZ djsz = djszs.get(0);
				
				Element szrmc = row.addElement("SZRMC");
				szrmc.addText(StringHelper.FormatByDatatype(djsz.getSZMC()));	
				
				Element szsj = row.addElement("SZSJ");
				szsj.addText(StringHelper.FormatDateOnType(djsz.getSZSJ(), "yyyyMMddhhmmss"));		
			}
			else
			{
				clearDJSZ(row);
			}		
		return row;
	}
	/**
	 * 在不存在BDCS_DJSZ时，需要添加默认节点
	 * @param row
	 * @return
	 */
	protected Element clearDJSZ(Element row)
	{

				Element szrmc = row.addElement("SZRMC");
				szrmc.addText("");	
				
				Element szsj = row.addElement("SZSJ");
				szsj.addText("");		
		return row;
	}
	
	/**
	 * 从BDCS_DJFZ中获取节点需要的信息
	 * @param row，父节点
	 * @param xmbhSql 项目编号sql
	 * @return
	 */
	protected Element getDJFZ(Element row,String xmbhSql)
	{
		List<BDCS_DJFZ> djfzs = baseCommonDao.getDataList(
				BDCS_DJFZ.class, xmbhSql);
		if (!StringHelper.isEmpty(djfzs)
				&& djfzs.size() > 0) {
			BDCS_DJFZ djfz = djfzs.get(0);
			
			Element fzrmc = row.addElement("FZRMC");
			fzrmc.addText(StringHelper.FormatByDatatype(djfz.getFZMC()));	
			
			Element lzrmc = row.addElement("LZRMC");
			lzrmc.addText(StringHelper.FormatByDatatype(djfz.getLZRXM()));	
			
			Element lzrq = row.addElement("LZRQ");
			lzrq.addText(StringHelper.FormatDateOnType(djfz.getFZSJ(), "yyyyMMddhhmmss"));	
			Element lzrzjhm = row.addElement("LZRZJHM");
			lzrzjhm.addText("");	
			
			Element fzsj = row.addElement("FZSJ");
			fzsj.addText("");	
		}
		else
		{
			clearDJFZ(row);
		}
		return row;
	}
	/**
	 * 在不存在BDCS_DJFZ时，需要添加默认节点
	 * @param row
	 * @return
	 */
	protected Element clearDJFZ(Element row)
	{

		Element fzrmc = row.addElement("FZRMC");
		fzrmc.addText("");	
		
		Element lzrmc = row.addElement("LZRMC");
		lzrmc.addText("");	
		
		Element lzrq = row.addElement("LZRQ");
		lzrq.addText("");		
		
		Element lzrzjhm = row.addElement("LZRZJHM");
		lzrzjhm.addText("");	
		
		Element fzsj = row.addElement("FZSJ");
		fzsj.addText("");	
		return row;
	}
	/*****************************************生成xml*****************************************/
	
	
	
	/**
     * 通用不动产登簿信息-从登记单元入口
     * @param xmbh
     * @param info
     * @return
     */
	@SuppressWarnings("rawtypes")
	protected String createCommonBDCDBByDJDYEX1111(String xmbh,ProjectInfo info)
	{
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("Root");
		Element head = root.addElement("Head");
		Element recordsets = root.addElement("RecordSet");
		Element rows = recordsets.addElement("Row");
		Element bdcdb = rows.addElement("BDCDB");		
		BDCDYLX bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
		
		BDCDBInfo dbinfo=new BDCDBInfo();
		
		int count = 0;	
	    List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhSql);
	    if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
	    {
			for (BDCS_DJDY_GZ djdy : djdys) {
				// 获取项目信息
				count++;
				House h=(House)UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
				if(h!=null){
					dbinfo.setBDCDYH(h.getBDCDYH());
					dbinfo.setDYTDMJ(StringHelper.formatDouble(h.getDYTDMJ()));//1
					dbinfo.setFTJZMJ(StringHelper.formatDouble(h.getSCJZMJ()));//2
					dbinfo.setFTTDMJ(StringHelper.formatDouble(h.getFTTDMJ()));//3
					dbinfo.setQX(h.getQXDM());//4
					dbinfo.setZYJZMJ(StringHelper.formatDouble(h.getDYTDMJ()));//5
					if(!StringHelper.isEmpty(h.getZDBDCDYID())){
						RealUnit zd_unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, h.getZDBDCDYID());
						if(zd_unit!=null){
							UseLand land=(UseLand)zd_unit;
							dbinfo.setTDQLLX(land.getQLLX());//6
							dbinfo.setTDQLXZ(land.getQLXZ());//7
							dbinfo.setZDDM(land.getZDDM());//8
						}
					}
				}
			}	    	
	    }	
		getHead(head,count,info);
		return document.asXML();
	}
	
	protected class BDCDBInfo{
		private String BDCDYH="";
		public String getBDCDYH() {
			return BDCDYH;
		}
		public void setBDCDYH(String bDCDYH) {
			BDCDYH = bDCDYH;
		}
		public String getBDCZMH() {
			return BDCZMH;
		}
		public void setBDCZMH(String bDCZMH) {
			BDCZMH = bDCZMH;
		}
		public String getBDCQZH() {
			return BDCQZH;
		}
		public void setBDCQZH(String bDCQZH) {
			BDCQZH = bDCQZH;
		}
		public String getYWH() {
			return YWH;
		}
		public void setYWH(String yWH) {
			YWH = yWH;
		}
		public String getDJLX() {
			return DJLX;
		}
		public void setDJLX(String dJLX) {
			DJLX = dJLX;
		}
		public String getZDDM() {
			return ZDDM;
		}
		public void setZDDM(String zDDM) {
			ZDDM = zDDM;
		}
		public String getTDSYQR() {
			return TDSYQR;
		}
		public void setTDSYQR(String tDSYQR) {
			TDSYQR = tDSYQR;
		}
		public String getDYTDMJ() {
			return DYTDMJ;
		}
		public void setDYTDMJ(String dYTDMJ) {
			DYTDMJ = dYTDMJ;
		}
		public String getFTTDMJ() {
			return FTTDMJ;
		}
		public void setFTTDMJ(String fTTDMJ) {
			FTTDMJ = fTTDMJ;
		}
		public String getTDSYQSRQ() {
			return TDSYQSRQ;
		}
		public void setTDSYQSRQ(String tDSYQSRQ) {
			TDSYQSRQ = tDSYQSRQ;
		}
		public String getTDSYJSRQ() {
			return TDSYJSRQ;
		}
		public void setTDSYJSRQ(String tDSYJSRQ) {
			TDSYJSRQ = tDSYJSRQ;
		}
		public String getZYJZMJ() {
			return ZYJZMJ;
		}
		public void setZYJZMJ(String zYJZMJ) {
			ZYJZMJ = zYJZMJ;
		}
		public String getFTJZMJ() {
			return FTJZMJ;
		}
		public void setFTJZMJ(String fTJZMJ) {
			FTJZMJ = fTJZMJ;
		}
		public String getTDQLXZ() {
			return TDQLXZ;
		}
		public void setTDQLXZ(String tDQLXZ) {
			TDQLXZ = tDQLXZ;
		}
		public String getTDQLLX() {
			return TDQLLX;
		}
		public void setTDQLLX(String tDQLLX) {
			TDQLLX = tDQLLX;
		}
		public String getQX() {
			return QX;
		}
		public void setQX(String qX) {
			QX = qX;
		}
		public String getRWM() {
			return RWM;
		}
		public void setRWM(String rWM) {
			RWM = rWM;
		}
		public String getYSBH() {
			return YSBH;
		}
		public void setYSBH(String ySBH) {
			YSBH = ySBH;
		}
		public String getSZRMC() {
			return SZRMC;
		}
		public void setSZRMC(String sZRMC) {
			SZRMC = sZRMC;
		}
		public String getSZSJ() {
			return SZSJ;
		}
		public void setSZSJ(String sZSJ) {
			SZSJ = sZSJ;
		}
		public String getLZRMC() {
			return LZRMC;
		}
		public void setLZRMC(String lZRMC) {
			LZRMC = lZRMC;
		}
		public String getLZRZJHM() {
			return LZRZJHM;
		}
		public void setLZRZJHM(String lZRZJHM) {
			LZRZJHM = lZRZJHM;
		}
		public String getTQLRGX() {
			return TQLRGX;
		}
		public void setTQLRGX(String tQLRGX) {
			TQLRGX = tQLRGX;
		}
		public String getLZRQ() {
			return LZRQ;
		}
		public void setLZRQ(String lZRQ) {
			LZRQ = lZRQ;
		}
		public String getFZRMC() {
			return FZRMC;
		}
		public void setFZRMC(String fZRMC) {
			FZRMC = fZRMC;
		}
		public String getFZSJ() {
			return FZSJ;
		}
		public void setFZSJ(String fZSJ) {
			FZSJ = fZSJ;
		}
		public String getDJJG() {
			return DJJG;
		}
		public void setDJJG(String dJJG) {
			DJJG = dJJG;
		}
		public String getDJSJ() {
			return DJSJ;
		}
		public void setDJSJ(String dJSJ) {
			DJSJ = dJSJ;
		}
		public String getDJZT() {
			return DJZT;
		}
		public void setDJZT(String dJZT) {
			DJZT = dJZT;
		}
		public String getTHYY() {
			return THYY;
		}
		public void setTHYY(String tHYY) {
			THYY = tHYY;
		}
		public String getBZ() {
			return BZ;
		}
		public void setBZ(String bZ) {
			BZ = bZ;
		}
		private String BDCZMH="";
		private String BDCQZH="";
		private String YWH="";
		private String DJLX="";
		private String ZDDM="";
		private String TDSYQR="";
		private String DYTDMJ="";
		private String FTTDMJ="";
		private String TDSYQSRQ="";
		private String TDSYJSRQ="";
		private String ZYJZMJ="";
		private String FTJZMJ="";
		private String TDQLXZ="";
		private String TDQLLX="";
		private String QX="";
		private String RWM="";
		private String YSBH="";
		private String SZRMC="";
		private String SZSJ="";
		private String LZRMC="";
		private String LZRZJHM="";
		private String TQLRGX="";
		private String LZRQ="";
		private String FZRMC="";
		private String FZSJ="";
		private String DJJG="";
		private String DJSJ="";
		private String DJZT="";
		private String THYY="";
		private String BZ="";
	}
}

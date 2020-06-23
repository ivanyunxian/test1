package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcifs.dcerpc.msrpc.netdfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_GEO;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

@Controller
@RequestMapping("/djdy")
public class GISController {
	
	@Autowired
	private CommonDao commonDao;
	
	@RequestMapping(value = "/identifypicture", method = RequestMethod.GET)
	public String buildingtable(Model model) {
		return "/workflow/buildingtable/identifypicture";
	}
	
	/**
	 * 设置户打点坐标信息(豹哥-根据类型来源更新H信息  陈博改为更新GZ、XZ、LS所有层的H信息)
	 * @param bdcdylx
	 * @param dyly
	 * @param bdcdyid
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	@RequestMapping(value="/gispegmarker/{bdcdylx}/{dyly}/{bdcdyid}",method=RequestMethod.GET)
	public @ResponseBody ResultMessage gispegmarker(@PathVariable("bdcdylx") String bdcdylx,@PathVariable("dyly") String dyly,
			@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request) throws UnsupportedEncodingException, ParseException{
		ResultMessage msg=new ResultMessage();
		DJDYLY[] djdyly = new DJDYLY[]{DJDYLY.GZ,DJDYLY.XZ,DJDYLY.LS};
//		if(dyly.equals("gz")){
//			djdyly=DJDYLY.GZ;
//		}else if(dyly.equals("ls")){
//			djdyly=DJDYLY.LS;
//		}
		for (int i = 0; i < djdyly.length; i++) {
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), djdyly[i], bdcdyid);
			if(!StringHelper.isEmpty(unit)){
				if(unit instanceof House){
					House h=(House) unit;
					String xzb=request.getParameter("xzb");
					h.setXZB(StringHelper.getDouble(xzb));
					String yzb=request.getParameter("yzb");
					h.setYZB(StringHelper.getDouble(yzb));
					String sj=request.getParameter("sj");
					Date dtsj=StringHelper.FormatByDate(sj,"yyyy-MM-dd hh:mm:ss");
					String bz=RequestHelper.getParam(request, "bz");
					String zrzbdcdyid=request.getParameter("zrzbdcdyid");
					String bdcdyh = request.getParameter("bdcdyh");
					String zddm = request.getParameter("zddm");
					String zdbdcdyid = request.getParameter("zdbdcdyid");
					h.setZRZBDCDYID(zrzbdcdyid);
					h.setBDCDYH(bdcdyh);
					h.setZDDM(zddm);
					h.setZDBDCDYID(zdbdcdyid);
					h.setMARKERSM(bz);
					h.setMARKERTIME(dtsj);
					h.setMARKERZT("0");//默认为0
					commonDao.update(h);
					commonDao.flush();
				}
			}
		}
		msg.setMsg("成功");
		msg.setSuccess("true");
		return msg;
	}
	
	/** 保存每一次的打点信息
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@RequestMapping(value="/savegispegmarker",method=RequestMethod.GET)
	public @ResponseBody ResultMessage savegispegmarker(HttpServletRequest request) throws UnsupportedEncodingException, ParseException{
		ResultMessage msg=new ResultMessage();
		BDCS_GEO geo = new BDCS_GEO();
		String id = geo.getId();
		String bdcdyid = RequestHelper.getParam(request, "bdcdyid");
		String bdcdyh = RequestHelper.getParam(request, "bdcdyh");
		String xmbh = RequestHelper.getParam(request, "xmbh");
		String zl = RequestHelper.getParam(request, "zl");
		String fwbm = RequestHelper.getParam(request, "fwbm");
		String zrzbdcdyid = RequestHelper.getParam(request, "zrzbdcdyid");
		String zrzbdcdyh = RequestHelper.getParam(request, "zrzbdcdyh");
		String xzb = RequestHelper.getParam(request, "xzb");
		String yzb = RequestHelper.getParam(request, "yzb");
		String gl = RequestHelper.getParam(request, "gl");
		String zd = RequestHelper.getParam(request, "zd");
		String sj = RequestHelper.getParam(request, "sj");
		Date dtsj = StringHelper.FormatByDate(sj,"yyyy-MM-dd hh:mm:ss");
		String bz = RequestHelper.getParam(request, "bz");
		String user = Global.getCurrentUserName();
		geo.setBDCDYID(bdcdyid);
		geo.setBDCDYH(bdcdyh);
		geo.setXMBH(xmbh);
		geo.setZL(zl);
		geo.setFWBM(fwbm);
		geo.setZRZBDCDYID(zrzbdcdyid);
		geo.setZRZBDCDYH(zrzbdcdyh);
		geo.setXZB(StringHelper.getDouble(xzb));
		geo.setYZB(StringHelper.getDouble(yzb));
		geo.setGL(gl);
		geo.setZD(zd);
		geo.setTIME(dtsj);
		geo.setSM(bz);
		geo.setZRRY(user);
		commonDao.save(geo);
		commonDao.flush();
		msg.setMsg(id);
		msg.setSuccess("true");
		return msg;
	}
	
	/** 保存打点的截图
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@RequestMapping(value="/saveImg",method=RequestMethod.POST)
	public @ResponseBody ResultMessage saveImg(HttpServletRequest request) throws UnsupportedEncodingException, ParseException{
		ResultMessage msg = new ResultMessage();
		String imgUrl = RequestHelper.getParam(request, "imgUrl");
		String id = RequestHelper.getParam(request, "id");
		BDCS_GEO geo = null;
		List<BDCS_GEO> list = commonDao.getDataList(BDCS_GEO.class, "id='"+id+"'");
		if(!list.isEmpty() && list.size()>0){
			geo = list.get(0);
			geo.setPICTURE(imgUrl);
			commonDao.update(geo);
			commonDao.flush();
		}
		msg.setMsg("成功");
		msg.setSuccess("true");
		return msg;
	}
	
	/** 获取某户所有的指认图
	 * @param bdcdyid : H
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getPictures/{bdcdyid}",method=RequestMethod.POST)
	public @ResponseBody Message getPictures(@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request, HttpServletResponse response){
		Message message = new Message();
		message.setSuccess("false");
		message.setMsg("失败");
		String fulSql = "SELECT * FROM BDCK.BDCS_GEO WHERE BDCDYID='" + bdcdyid + "'" + "ORDER BY TIME desc";
		List<Map> list = commonDao.getDataListByFullSql(fulSql);
		if (list != null && list.size() > 0) {
			message.setTotal(list.size());
			message.setRows(list);
			message.setSuccess("true");
			message.setMsg("成功");
		}
		return message;
	}
	
	/** 根据自然幢的BDCDYID获取BDCDYH
	 * @param bdcdyid : ZRZ
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getZInfo/{bdcdyid}",method=RequestMethod.POST)
	public @ResponseBody List<Map> getZrzBdcdyh(@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request, HttpServletResponse response){
		String fulSql = "SELECT ZRZ.BDCDYH, ZRZ.ZDDM, ZRZ.ZDBDCDYID FROM BDCK.BDCS_ZRZ_XZ ZRZ WHERE BDCDYID='" + bdcdyid + "'";
		List<Map> list = commonDao.getDataListByFullSql(fulSql);
		return list;
	}
	
	/** 判断单元是否存在登簿项目（根据BDCQZH）
	 * @param bdcdyid : H
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getdbInfo/{bdcdyid}",method=RequestMethod.POST)
	public @ResponseBody Message SFDB(@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request, HttpServletResponse response){
		Message message = new Message();
		message.setSuccess("false");
		message.setMsg("不存在登簿的项目");
		String fulSql = "SELECT QLR.BDCQZH,QLR.QLID,QLR.QLRID FROM BDCK.BDCS_QLR_XZ QLR " + 
		                "LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID = QLR.QLID " +
		                "LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID = QL.DJDYID " +
		                "WHERE DJDY.BDCDYID = '" + bdcdyid + "'";
		List<Map> list = commonDao.getDataListByFullSql(fulSql);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String bdcqzh = StringHelper.formatObject(list.get(i).get("BDCDQZH"));
				if (bdcqzh.indexOf("不动产") != -1) {
					message.setSuccess("true");
					message.setMsg("存在登簿的项目");
					return message;
				}
			}
		}
		return message;
	}
	
	/**
	 * 根据自然幢不动产单元id获取户不动产单元号
	 * @param zrzbdcdyid
	 * @param zrzbdcdylx
	 * @return
	 */
	@RequestMapping(value="/NewHouseBDCDYH/{zrzbdcdyid}",method=RequestMethod.POST)
	public @ResponseBody Message NewHouseBDCDYH(@PathVariable("zrzbdcdyid")String zrzbdcdyid, HttpServletRequest request, HttpServletResponse response) {
		String m_bdcdylx = "03";
		Message msg = new Message();
		msg.setSuccess("false");
		if (!StringHelper.isEmpty(zrzbdcdyid)) {
			RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(m_bdcdylx), DJDYLY.XZ, zrzbdcdyid);
			if (unit != null) {
				String zrzbdcdyh = unit.getBDCDYH();
				if (!StringHelper.isEmpty(zrzbdcdyh)) {
					if (zrzbdcdyh.length() != 28 || zrzbdcdyh.charAt(19) != 'F') {
						return msg;
					} else {
						String bdcdyh = UnitTools.CreatBDCDYH(zrzbdcdyh.substring(0, 24), "04");
						if (!StringHelper.isEmpty(bdcdyh)) {
							msg.setSuccess("true");
							msg.setMsg(bdcdyh);
						} 
					}
				}
			}
		}
		return msg;
	}
	
	/**
	 * 获取户打点信息
	 * @param bdcdylx
	 * @param dyly
	 * @param bdcdyid
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/getgispegmarker/{bdcdylx}/{dyly}/{bdcdyid}",method=RequestMethod.GET)
	public @ResponseBody RealUnit getgispegmarker(@PathVariable("bdcdylx") String bdcdylx,@PathVariable("dyly") String dyly,
			@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request) throws UnsupportedEncodingException{
		DJDYLY djdyly=DJDYLY.XZ;
		if(dyly.equals("gz")){
			djdyly=DJDYLY.GZ;
		}else if(dyly.equals("ls")){
			djdyly=DJDYLY.LS;
		}
		House h=null;
		RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), djdyly, bdcdyid);
		if(!StringHelper.isEmpty(unit)){
			if(unit instanceof House){
				 h=(House) unit;
			}
		}
		return h;
	}
	
	/**
	 * 通过图形操作获取对应的单元信息
	 * @param relbdcdyids，图形关联的主键
	 * @param bdcdylx，不动产单元类型
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getbdcdyidsbygis/{relbdcdyids}/{bdcdylx}",method=RequestMethod.GET)
	public @ResponseBody ResultMessage getBdcdyidsByGis(@PathVariable("relbdcdyids") String relbdcdyids,@PathVariable("bdcdylx") String bdcdylx,HttpServletRequest request){
		ResultMessage msg=new ResultMessage();
		String bdcdyids="";
		String relbdcdyid="";
		String qrysql="";
		String tablename="";
		String fieldname="";
		String tiptitle="";
		List<Map> lstbdcdyid=null;
		msg.setSuccess("false");		
		if(BDCDYLX.H.Value.equals(bdcdylx)){
			tablename="BDCDCK.BDCS_H_GZ";
			fieldname=" ZRZBDCDYID ";
			tiptitle="没有查找到自然幢对应的户信息";

		}else if(BDCDYLX.YCH.Value.equals(bdcdylx)){
			tablename="BDCK.BDCS_H_XZY";
			fieldname=" ZRZBDCDYID ";
			tiptitle="没有查找到预测自然幢对应的预测户信息";
			
		}else if(BDCDYLX.SHYQZD.Value.equals(bdcdylx)){
			tablename="BDCDCK.BDCS_SHYQZD_GZ";
			fieldname=" BDCDYID ";
			tiptitle="没有查找到使用权宗地信息";
			
		}else if(BDCDYLX.SYQZD.Value.equals(bdcdylx)){
			tablename="BDCDCK.BDCS_SYQZD_GZ";
			fieldname=" BDCDYID ";
			tiptitle="没有查找到所有权信息";
		}else {
			msg.setSuccess("false");
			msg.setSuccess("不在批量受理范围");
			return msg;
		}
		if(!StringHelper.isEmpty(relbdcdyids)){
			String[] ids = relbdcdyids.split(",");
			if(ids !=null && ids.length>0){
				for(String id :ids){
					if(!StringHelper.isEmpty(id)){
						if(relbdcdyid==""){
							relbdcdyid="'"+id+"'";	
						}else{
							relbdcdyid=relbdcdyid+",'"+id+"'";
						}
					}					
				}
			}
		}
		if(!StringHelper.isEmpty(relbdcdyid)){
			if(relbdcdyid.contains(",")){
				  qrysql=" and "+fieldname+" IN("+relbdcdyid+"')";					
			}else{
				  qrysql=" and ZRZBDCDYID ="+relbdcdyid;
			}
		}
		String dc_gz_sql="select  BDCDYID  from "+tablename+" WHERE 1=1 ";
		dc_gz_sql +=qrysql;
		lstbdcdyid=commonDao.getDataListByFullSql(dc_gz_sql);
		if(!lstbdcdyid.isEmpty() && lstbdcdyid.size()>0){
			for(Map m :lstbdcdyid){
				String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
				if(!StringHelper.isEmpty(bdcdyid)){
					if(bdcdyids !=""){
						bdcdyids+=","+bdcdyid;
					}else{
						bdcdyids=bdcdyid;
					}
				}
			}
		}
		if(bdcdyids !=""){
			msg.setSuccess("true");
			msg.setMsg(bdcdyids);
		}else{
			msg.setSuccess("false");
			msg.setMsg(tiptitle);
		}
		return msg;
	}
}

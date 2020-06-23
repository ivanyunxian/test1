package com.supermap.realestate.registration.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.realestate.registration.maintain.HisInfoMaintenanceTools;
import com.supermap.realestate.registration.maintain.Unit;
import com.supermap.realestate.registration.maintain.Unit.Holder;
import com.supermap.realestate.registration.maintain.Unit.Right;
import com.supermap.realestate.registration.maintain.Unit.RightClass;
import com.supermap.realestate.registration.maintain.Unit.ZDValue;
import com.supermap.realestate.registration.maintain.Units;
import com.supermap.realestate.registration.maintain.realestateWebHelper;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.LOG_DATAMAINTENACE;
import com.supermap.realestate.registration.service.LogInfoService;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 数据维护Controller
 * @ClassName: MaintainController
 * @author liushufeng
 * @date 2016年7月13日 下午1:51:37
 */
@Controller
@RequestMapping(value="/maintain")
public class MaintainController {

	@Autowired
	CommonDao dao;
	
	@Autowired
	private LogInfoService logInfoService;

	/**
	 * 宗地不动产单元补录维护(/maintain)
	 * @Title: landMaintainPage 
	 * @author:liushufeng
	 * @date：2016年7月13日 下午1:58:39
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/maintain", method = RequestMethod.GET)
	public String landMaintainPage(HttpServletRequest request){
		return "/maintain/maintain";
	}
	

	@RequestMapping("/testnode/{xmbh}")
	public @ResponseBody Units getNodes(@PathVariable String xmbh) throws IOException {
		Units uslast = new Units();
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		if (xmxx != null) {
			String str = xmxx.getALTERDATA();
			ObjectMapper mapper3 = new ObjectMapper();
			if (!StringHelper.isEmpty(str))
				uslast = mapper3.readValue(str, Units.class);
			else
				uslast = new Units();
		}
		return uslast;
	}
	
	@RequestMapping("/getdata/{id}")
	public @ResponseBody Units getUnitsData(@PathVariable String id) throws IOException {
		Units uslast = new Units();
		int id_int = StringHelper.getInt(id);
		LOG_DATAMAINTENACE dmt = dao.get(LOG_DATAMAINTENACE.class, id_int);
		if (dmt != null) {
			String str = dmt.getLGCONTENT();
			ObjectMapper mapper3 = new ObjectMapper();
			if (!StringHelper.isEmpty(str))
				uslast = mapper3.readValue(str, Units.class);
			else
				uslast = new Units();
		}
		return uslast;
	}
	

	@RequestMapping(value = "/savedata/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveData(@PathVariable String xmbh, HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		String data = request.getParameter("data");
		msg.setSuccess("false");
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		if (xmxx != null) {
			xmxx.setALTERDATA(data);
			dao.update(xmxx);
			dao.flush();
			msg.setSuccess("true");
			msg.setMsg("保存成功");
		}
		return msg;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/datatype/{tablename}", method = RequestMethod.GET)
	public @ResponseBody Object getDatatype(@PathVariable String tablename, HttpServletRequest request) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		@SuppressWarnings("rawtypes")
		List<Map> list = dao.getDataListByFullSql("select * from bdck." + tablename + " where ROWnum<2");
		Object o = null;
		if (list.size() > 0) {
			Map<String, Object> m = list.get(0);
			for (@SuppressWarnings("rawtypes")
			Entry en : m.entrySet()) {
				en.setValue(null);
			}
			o = m;
		}
		return o;
	}

	@RequestMapping(value = "/savetodb/{xmbh}")
	public @ResponseBody ResultMessage savetodb(@PathVariable String xmbh) throws JsonParseException, JsonMappingException, IOException {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		Units uslast = new Units();
		String content = "";
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		if (xmxx != null) {
			content = xmxx.getALTERDATA();
			ObjectMapper mapper3 = new ObjectMapper();
			uslast = mapper3.readValue(content, Units.class);
			xmxx.setSFDB("1");
			xmxx.setDJSJ(new Date());
			
			dao.update(xmxx);
		}
		for (Unit u : uslast.units) {
			if(u.rights!=null && u.rights.size()>0){
				Object syqmj=u.baseinfo.newvalue.get("ZDMJ");
				u.rights.get(0).newvalue.getFsql().put("SYQMJ", syqmj);
			}
			
			HisInfoMaintenanceTools.MaintenanceInfo(u,xmxx);
		}
		dao.flush();
		BDCS_XMXX cachedXMXX = Global.getXMXX(xmxx.getPROJECT_ID());
		if (cachedXMXX != null) {
			cachedXMXX.setSFDB(SFDB.YES.Value);
		}
		logInfoService.dataMaintenaceLog(content);
		msg.setMsg("成功写入登记簿");
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/selectland/{xmbh}/{bdcdyid}")
	public @ResponseBody Unit selectland(@PathVariable String xmbh, @PathVariable String bdcdyid) {
		Unit u = new Unit();
		List<Map> zds = dao.getDataListByFullSql("select * from bdck.bdcs_shyqzd_xz where bdcdyid='" + bdcdyid + "'");
		zds=StringHelper.ConvertDateOnListMap(zds);
		List<Map> tdyts = dao.getDataListByFullSql("select * from bdck.bdcs_tdyt_xz where bdcdyid='" + bdcdyid + "'");
		tdyts=StringHelper.ConvertDateOnListMap(tdyts);
		ZDValue v = new ZDValue(zds.get(0));
		v.put("OPERATE", "0");
		for (int i = 0; i < tdyts.size(); i++) {
			v.getTdyts().add(tdyts.get(i));
		}
		v.put("BDCDYLX", "02");
		u.baseinfo.newvalue = u.baseinfo.oldvalue = v;

		List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			List<Map> syqs = dao.getDataListByFullSql("select * from bdck.bdcs_ql_xz where djdyid='" + djdyid + "' and qllx='3'");
			syqs=StringHelper.ConvertDateOnListMap(syqs);
			List<Map> morts = dao.getDataListByFullSql("select * from bdck.bdcs_ql_xz where djdyid='" + djdyid + "' and qllx='23'");
			morts=StringHelper.ConvertDateOnListMap(morts);
			List<Map> seals = dao.getDataListByFullSql("select * from bdck.bdcs_ql_xz where djdyid='" + djdyid + "' and qllx='99' and djlx='800'");
			seals=StringHelper.ConvertDateOnListMap(seals);
			
			List<Map> buildingids = dao.getDataListByFullSql("select bdcdyid from  bdck.bdcs_zrz_xz where zdbdcdyid='" + bdcdyid + "'");
			if (buildingids != null && buildingids.size() > 0) {
				for (Map m : buildingids) {
					u.buildingids.add((String) m.get("BDCDYID"));
				}
			}

			// 所有权/使用权
			Right syq = new Right(syqs.get(0));
			List<Map> syqrs = dao.getDataListByFullSql("select * from bdck.bdcs_qlr_xz where qlid='" + syqs.get(0).get("QLID") + "'");
			List<Map> syqfsqls = dao.getDataListByFullSql("select * from bdck.bdcs_fsql_xz where qlid='" + syqs.get(0).get("QLID") + "'");
			syqfsqls=StringHelper.ConvertDateOnListMap(syqfsqls);
			if (syqfsqls.size() > 0) {
				syq.setFsql(syqfsqls.get(0));
			} else {
				syq.setFsql(new HashMap<String, Object>());
				syq.getFsql().put("FSQLID", SuperHelper.GeneratePrimaryKey());
				syq.getFsql().put("QLID", syq.get("QLID"));
				syq.getFsql().put("DJDYID", syq.get("DJDYID"));

			}

			syq.put("OPERATE", "0");
			String syqqlid = (String) syq.get("QLID");
			syq.getFsql().put("OPERATE", "0");
			for (int i = 0; i < syqrs.size(); i++) {
				Holder h = new Holder(syqrs.get(i));
				String qlrid = (String) h.get("QLRID");
				String zsid = "";
				List<Map> qdzrs = dao.getDataListByFullSql("select * from bdck.bdcs_qdzr_xz where qlid='" + syqqlid + "' and qlrid='" + qlrid + "'");
				if (qdzrs != null && qdzrs.size() > 0) {
					zsid = (String) qdzrs.get(0).get("ZSID");
				}
				h.put("OPERATE", "0");
				h.put("ZSID", zsid);
				syq.getHolders().add(h);
			}
			RightClass syqclass = new RightClass();
			syqclass.newvalue = syqclass.oldvalue = syq;
			u.rights.add(syqclass);

			// 抵押权
			for (int i = 0; i < morts.size(); i++) {
				RightClass rs = new RightClass();
				Right rt = new Right(morts.get(i));
				String dyqqlid = (String) rt.get("QLID");
				List<Map> dyqrs = dao.getDataListByFullSql("select * from bdck.bdcs_qlr_xz where qlid='" + morts.get(i).get("QLID") + "'");
				for (int j = 0; j < dyqrs.size(); j++) {
					Holder h = new Holder(dyqrs.get(j));
					h.put("OPERATE", "0");

					String qlrid = (String) h.get("QLRID");
					String zsid = "";
					List<Map> qdzrs = dao.getDataListByFullSql("select * from bdck.bdcs_qdzr_xz where qlid='" + dyqqlid + "' and qlrid='" + qlrid + "'");
					if (qdzrs != null && qdzrs.size() > 0) {
						zsid = (String) qdzrs.get(0).get("ZSID");
					}
					h.put("OPERATE", "0");
					h.put("ZSID", zsid);
					rt.getHolders().add(h);
				}
				String fsqlid = (String) morts.get(i).get("FSQLID");
				List<Map> fsqls = dao.getDataListByFullSql("select * from bdck.bdcs_fsql_xz where fsqlid='" + fsqlid + "'");
				fsqls=StringHelper.ConvertDateOnListMap(fsqls);
				if (fsqls != null && fsqls.size() > 0) {
					rt.setFsql(fsqls.get(0));
				}
				rt.put("OPERATE", "0");
				rt.getFsql().put("OPERATE", "0");
				rs.newvalue = rs.oldvalue = rt;
				u.mortgages.add(rs);
			}

			// 查封
			for (int i = 0; i < seals.size(); i++) {
				RightClass rs = new RightClass();
				rs.newvalue = rs.oldvalue = new Right(seals.get(i));
				String fsqlid = (String) seals.get(i).get("FSQLID");
				List<Map> sealfsql = dao.getDataListByFullSql("select * from bdck.bdcs_fsql_xz where fsqlid='" + fsqlid + "'");
				sealfsql=StringHelper.ConvertDateOnListMap(sealfsql);
				if (sealfsql != null && sealfsql.size() > 0) {
					// rs.newvalue.fsql=rs.oldvalue.fsql=sealfsql.get(0);
					rs.newvalue.setFsql(sealfsql.get(0));
					rs.oldvalue.setFsql(sealfsql.get(0));
					rs.newvalue.put("OPERATE", "0");
					rs.newvalue.getFsql().put("OPERATE", "0");
				}
				u.seals.add(rs);
			}
		}
		Units us = new Units();
		us.getUnits().add(u);
		return u;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/selecthouse/{xmbh}/{bdcdyid}")
	public @ResponseBody Unit selecthouse(@PathVariable String xmbh, @PathVariable String bdcdyid) {
		Unit u = new Unit();
		List<Map> zds = dao.getDataListByFullSql("select * from bdck.bdcs_h_xz where bdcdyid='" + bdcdyid + "'");
		zds=StringHelper.ConvertDateOnListMap(zds);
		ZDValue v = new ZDValue(zds.get(0));
		v.put("OPERATE", "0");
		v.put("BDCDYLX", "031");
		u.baseinfo.newvalue = u.baseinfo.oldvalue = v;

		List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			List<Map> syqs = dao.getDataListByFullSql("select * from bdck.bdcs_ql_xz where djdyid='" + djdyid + "' and qllx='4'");
			syqs=StringHelper.ConvertDateOnListMap(syqs);
			List<Map> morts = dao.getDataListByFullSql("select * from bdck.bdcs_ql_xz where djdyid='" + djdyid + "' and qllx='23'");
			morts=StringHelper.ConvertDateOnListMap(morts);
			List<Map> seals = dao.getDataListByFullSql("select * from bdck.bdcs_ql_xz where djdyid='" + djdyid + "' and qllx='99' and djlx='800'");
			seals=StringHelper.ConvertDateOnListMap(seals);
			
			// 所有权/使用权
			Right syq = new Right(syqs.get(0));
			List<Map> syqrs = dao.getDataListByFullSql("select * from bdck.bdcs_qlr_xz where qlid='" + syqs.get(0).get("QLID") + "'");
			syqrs=StringHelper.ConvertDateOnListMap(syqrs);
			List<Map> syqfsqls = dao.getDataListByFullSql("select * from bdck.bdcs_fsql_xz where qlid='" + syqs.get(0).get("QLID") + "'");
			syqfsqls=StringHelper.ConvertDateOnListMap(syqfsqls);
			if (syqfsqls.size() > 0) {
				syq.setFsql(syqfsqls.get(0));
			} else {
				syq.setFsql(new HashMap<String, Object>());
				syq.getFsql().put("FSQLID", SuperHelper.GeneratePrimaryKey());
				syq.getFsql().put("QLID", syq.get("QLID"));
				syq.getFsql().put("DJDYID", syq.get("DJDYID"));

			}

			syq.put("OPERATE", "0");
			String syqqlid = (String) syq.get("QLID");
			syq.getFsql().put("OPERATE", "0");
			for (int i = 0; i < syqrs.size(); i++) {
				Holder h = new Holder(syqrs.get(i));
				String qlrid = (String) h.get("QLRID");
				String zsid = "";
				List<Map> qdzrs = dao.getDataListByFullSql("select * from bdck.bdcs_qdzr_xz where qlid='" + syqqlid + "' and qlrid='" + qlrid + "'");
				if (qdzrs != null && qdzrs.size() > 0) {
					zsid = (String) qdzrs.get(0).get("ZSID");
				}
				h.put("OPERATE", "0");
				h.put("ZSID", zsid);
				syq.getHolders().add(h);
			}
			RightClass syqclass = new RightClass();
			syqclass.newvalue = syqclass.oldvalue = syq;
			u.rights.add(syqclass);

			// 抵押权
			for (int i = 0; i < morts.size(); i++) {
				RightClass rs = new RightClass();
				Right rt = new Right(morts.get(i));
				String dyqqlid = (String) rt.get("QLID");
				List<Map> dyqrs = dao.getDataListByFullSql("select * from bdck.bdcs_qlr_xz where qlid='" + morts.get(i).get("QLID") + "'");
				for (int j = 0; j < dyqrs.size(); j++) {
					Holder h = new Holder(dyqrs.get(j));
					h.put("OPERATE", "0");

					String qlrid = (String) h.get("QLRID");
					String zsid = "";
					List<Map> qdzrs = dao.getDataListByFullSql("select * from bdck.bdcs_qdzr_xz where qlid='" + dyqqlid + "' and qlrid='" + qlrid + "'");
					if (qdzrs != null && qdzrs.size() > 0) {
						zsid = (String) qdzrs.get(0).get("ZSID");
					}
					h.put("OPERATE", "0");
					h.put("ZSID", zsid);
					rt.getHolders().add(h);
				}
				String fsqlid = (String) morts.get(i).get("FSQLID");
				List<Map> fsqls = dao.getDataListByFullSql("select * from bdck.bdcs_fsql_xz where fsqlid='" + fsqlid + "'");
				fsqls=StringHelper.ConvertDateOnListMap(fsqls);
				if (fsqls != null && fsqls.size() > 0) {
					rt.setFsql(fsqls.get(0));
				}
				rt.put("OPERATE", "0");
				rt.getFsql().put("OPERATE", "0");
				rs.newvalue = rs.oldvalue = rt;
				u.mortgages.add(rs);
			}

			// 查封
			for (int i = 0; i < seals.size(); i++) {
				RightClass rs = new RightClass();
				rs.newvalue = rs.oldvalue = new Right(seals.get(i));
				String fsqlid = (String) seals.get(i).get("FSQLID");
				List<Map> sealfsql = dao.getDataListByFullSql("select * from bdck.bdcs_fsql_xz where fsqlid='" + fsqlid + "'");
				sealfsql=StringHelper.ConvertDateOnListMap(sealfsql);
				if (sealfsql != null && sealfsql.size() > 0) {
					// rs.newvalue.fsql=rs.oldvalue.fsql=sealfsql.get(0);
					rs.newvalue.setFsql(sealfsql.get(0));
					rs.oldvalue.setFsql(sealfsql.get(0));
					rs.newvalue.put("OPERATE", "0");
					rs.newvalue.getFsql().put("OPERATE", "0");
				}
				u.seals.add(rs);
			}
		}
		return u;
	}
	
	/**
	 * 选择自然幢
	 * @Title: selectbuilding 
	 * @author:liushufeng
	 * @date：2016年7月19日 下午4:55:11
	 * @param xmbh
	 * @param bdcdyid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/selectbuilding/{xmbh}/{bdcdyid}")
	public @ResponseBody Unit selectbuilding(@PathVariable String xmbh, @PathVariable String bdcdyid) {
		Unit u = new Unit();
		List<Map> zds = dao.getDataListByFullSql("select * from bdck.bdcs_zrz_xz where bdcdyid='" + bdcdyid + "'");
		zds=StringHelper.ConvertDateOnListMap(zds);
		ZDValue v = new ZDValue(zds.get(0));
		v.put("OPERATE", "0");
		v.put("BDCDYLX", "03");
		u.baseinfo.newvalue = u.baseinfo.oldvalue = v;
		return u;
	}
	

	@RequestMapping(value = "/buildings")
	public @ResponseBody List<BDCS_ZRZ_XZ> getbuildings(HttpServletRequest request) {
		String data = request.getParameter("data");
		String[] strs = data.split(",");
		String sql = " id IN (";
		for (int i = 0; i < strs.length; i++) {

			sql += "'" + strs[i] + "',";
		}
		sql = sql.substring(0, sql.length() - 1) + ")";
		List<BDCS_ZRZ_XZ> list = dao.getDataList(BDCS_ZRZ_XZ.class, sql);
		return list;
	}
	
	
    /**
     * 通过选择器类型获取对应的选择器ID
     * @param selectortype
     * @return
     * @throws IOException 
     */
	@RequestMapping(value="/loadselector/{selectortype}",method=RequestMethod.GET)
    public @ResponseBody String loadselector(@PathVariable String selectortype,HttpServletRequest request) throws IOException{
    	String id="";
    	id=realestateWebHelper.loadselector(selectortype,request);
    	return id;
    }
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getxzq")
	public @ResponseBody List<Map> getXZQ() throws IOException {
		List<Map> list=dao.getDataListByFullSql("SELECT XZQDM,XZQMC FROM BDCK.BDCK_XZQ");
		return list;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getdjq/{qxdm}")
	public @ResponseBody List<Map> getDJQ(@PathVariable String qxdm) throws IOException {
		List<Map> list=dao.getDataListByFullSql("SELECT XZQDM,XZQMC FROM BDCK.BDCK_DJQ WHERE XZQDM LIKE '"+qxdm+"%'");
		return list;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getdjzq/{djqdm}")
	public @ResponseBody List<Map> getDJZQ(@PathVariable String djqdm) throws IOException {
		List<Map> list=dao.getDataListByFullSql("SELECT XZQDM,XZQMC FROM BDCK.BDCK_DJZQ WHERE XZQDM LIKE '"+djqdm+"%'");
		return list;
	}

	/**
	 * 通过不动产单元ID及对应的表名获取不动产库数据
	 * @param tablename
	 * @param bdcdyid
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	@RequestMapping(value="getbdckmap/{tablename}/{bdcdyid}")
	public @ResponseBody Map getbdckmap(@PathVariable String tablename,@PathVariable String bdcdyid)
	{
		Map map=null;
		String fulSql="SELECT * FROM BDCK."+tablename+" WHERE BDCDYID= '"+bdcdyid+"'";
		List<Map> lst=dao.getDataListByFullSql(fulSql);
		if(!StringHelper.isEmpty(lst) && lst.size()>0)
		{
			map=lst.get(0);
		}
		return map;
	}
	
	/**
	 * 通过LBDCDYH、LZL、LBDCQZH、LCQR获取MAINTAINUNIT库数据
	 * @param LBDCDYH
	 * @param LZL
	 * @param LBDCQZH
	 * @param LCQR
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	@RequestMapping(value="/mainTainlandQuery")
	public @ResponseBody Message getmainTainlandList(HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException
	{
		Message msg = new Message();
		List<Map> listresult = null;
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		String lbdcdyh=RequestHelper.getParam(request, "LBDCDYH");
		String lzl=RequestHelper.getParam(request,"LZL");
		String lbdcqzh=RequestHelper.getParam(request, "LBDCQZH");
		String lcqr=RequestHelper.getParam(request, "LCQR");

		String unitentityName = "SELECT ID,XMBH,LBDCDYH,LZL,LBDCQZH,LCQR,XBDCDYH,XZL,XBDCQZH,XCQR,XGRQ,XGR FROM BDCK.BDCS_MAINTAINUNIT WHERE 1=1";
		StringBuilder builder = new StringBuilder();
		if(!StringHelper.isEmpty(lbdcdyh)){
			builder.append(" AND LBDCDYH LIKE"+"'%"+lbdcdyh+"%'");
		}
		if(!StringHelper.isEmpty(lzl)){
			builder.append(" AND LZL LIKE"+"'%"+lzl+"%'");
		}
		if(!StringHelper.isEmpty(lbdcqzh)){
			builder.append(" AND LBDCQZH LIKE"+"'%"+lbdcqzh+"%'");
		}
		if(!StringHelper.isEmpty(lcqr)){
			builder.append(" AND LCQR LIKE'"+"%"+lcqr+"%"+"' OR XCQR LIKE'"+"%"+lcqr+"%"+"'");
		}
		String fullSql = unitentityName + builder ;
		String countSql = " FROM BDCK.BDCS_MAINTAINUNIT";
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		long count = dao.getCountByFullSql(countSql);
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
	@RequestMapping(value = "/createbdcdyh/{relyonvalue}/{bdcdylx}")
	public @ResponseBody String createbdcdyh(@PathVariable String relyonvalue, @PathVariable String bdcdylx,HttpServletRequest request) {
		return ProjectHelper.CreatBDCDYH(relyonvalue, bdcdylx);
	}
	
	/**
	 * 通过数据字段常量类型及数据字段值获取对应的常量名称
	 * @param consttype 常量类型
	 * @param constvalue 常量值
	 * @param request
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value="{consttype}/{constvalue}/getnamebyvalue",method=RequestMethod.GET)
	public @ ResponseBody String getNameByValue(@PathVariable String consttype, @PathVariable String constvalue,HttpServletRequest request,HttpServletResponse response){		
		return ConstHelper.getNameByValue(consttype, constvalue);
	}
}

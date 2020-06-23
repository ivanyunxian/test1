package com.supermap.realestate.registration.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.realestate.registration.maintain.Unit;
import com.supermap.realestate.registration.maintain.Unit.Holder;
import com.supermap.realestate.registration.maintain.Unit.RightClass;
import com.supermap.realestate.registration.maintain.Units;
import com.supermap.realestate.registration.model.LOG_DATAMAINTENACE;
import com.supermap.realestate.registration.model.LOG_LOGIN;
import com.supermap.realestate.registration.model.LOG_QUERY;
import com.supermap.realestate.registration.service.LogInfoService;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.LogConstValue.DLMSG;
import com.supermap.realestate.registration.util.LogConstValue.LOGINTYPE;
import com.supermap.realestate.registration.util.LogConstValue.MTOPRTYPE;
import com.supermap.realestate.registration.util.LogConstValue.MTTYPE;
import com.supermap.realestate.registration.util.LogConstValue.QUERYTYPE;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;

@Service("LogInfoService")
public class LogInfoServiceImpl implements LogInfoService{

	@Autowired
	private CommonDao dao;
	
	/* 
	 * 数据维护日志
	 */
	@Override
	public void dataMaintenaceLog(String content) {
		LOG_DATAMAINTENACE datamaintenace = new LOG_DATAMAINTENACE();
		Boolean ifsave = false;
		String description = "";
		Units uslast = new Units();
		ObjectMapper mapper = new ObjectMapper();
		MTOPRTYPE mttype = null;
		if (!StringHelper.isEmpty(content)) {
			try {
				uslast = mapper.readValue(content, Units.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String dy_bdcdyh = "";
			String dy_zl = "";
			String ql_qllx = "";
			String ql_djlx = "";
			String ql_bdcqzh = "";
			String qlr_qlrmc = "";
			String qlr_bdcqzh = "";
			int i = 1;
			for (Unit u : uslast.units) {
				if (u != null && u.baseinfo != null && u.baseinfo.newvalue != null) {
					dy_bdcdyh = StringHelper.formatObject(u.baseinfo.newvalue.get("BDCDYH"));
					dy_zl = StringHelper.formatObject(u.baseinfo.newvalue.get("ZL"));
					String bdcdylx = StringHelper.formatObject(u.baseinfo.newvalue.get("BDCDYLX"));
					if (bdcdylx.equals("031") || bdcdylx.equals("032")) {
						mttype = MTOPRTYPE.FWWH;
					}else if (bdcdylx.equals("03") || bdcdylx.equals("08")) {
						mttype = MTOPRTYPE.ZRZWH;
					}else if (bdcdylx.equals("01") || bdcdylx.equals("02")) {
						mttype = MTOPRTYPE.ZDWH;
					}
					if (!StringHelper.formatObject(u.baseinfo.newvalue.get("OPERATE")).equals("0")) {
						ifsave = true;
					}
				}
				if (i < 11) {
					description = description + "单元" + i + ":" + dy_zl + "," + dy_bdcdyh + ",";
					if (u != null && u.rights != null && u.rights.size() > 0) {
						for (int j = 0; j < u.rights.size(); j++) {
							int size = j + 1;
							RightClass right = u.rights.get(j);
							if (right.newvalue != null) {
								ql_qllx = StringHelper.formatObject(right.newvalue.get("QLLX"));
								ql_djlx = StringHelper.formatObject(right.newvalue.get("DJLX"));
								ql_bdcqzh = StringHelper.formatObject(right.newvalue.get("BDCDQH"));
								if (!StringHelper.formatObject(right.newvalue.get("OPERATE")).equals("0")) {
									ifsave = true;
								}
								description = description + "权利" + size + ":" + ql_qllx + "," + ql_djlx + "," + ql_bdcqzh + ",";
								if (right.newvalue.getHolders() != null && right.newvalue.getHolders().size() > 0) {
									List<Holder> lstHolders = right.newvalue.getHolders();
									for (int a = 0; a < lstHolders.size(); a++) {
										int qlrSize = a + 1;
										Holder holder = new Holder(lstHolders.get(a));
										qlr_qlrmc = StringHelper.formatObject(holder.get("QLRMC"));
										qlr_bdcqzh = StringHelper.formatObject(holder.get("BDCQZH"));
										if (!StringHelper.formatObject(holder.get("OPERATE")).equals("0")) {
											ifsave = true;
										}
										description = description + "权利人" + qlrSize + ":" + qlr_qlrmc + "," + qlr_bdcqzh + ",";
									}
								}
							}
						}
					}
					if (u != null && u.mortgages != null && u.mortgages.size() > 0) {
						for (int k = 0; k < u.mortgages.size(); k++) {
							int size = u.rights.size() + 1 + k;
							RightClass right = u.mortgages.get(k);
							if (right.newvalue != null) {
								ql_qllx = StringHelper.formatObject(right.newvalue.get("QLLX"));
								ql_djlx = StringHelper.formatObject(right.newvalue.get("DJLX"));
								ql_bdcqzh = StringHelper.formatObject(right.newvalue.get("BDCDQH"));
								if (!StringHelper.formatObject(right.newvalue.get("OPERATE")).equals("0")) {
									ifsave = true;
								}
								description = description + "权利" + size + ":" + ql_qllx + "," + ql_djlx + "," + ql_bdcqzh + ",";
								if (right.newvalue.getHolders() != null && right.newvalue.getHolders().size() > 0) {
									List<Holder> lstHolders = right.newvalue.getHolders();
									for (int b = 0; b < lstHolders.size(); b++) {
										int qlrSize = b + 1;
										Holder holder = new Holder(lstHolders.get(b));
										qlr_qlrmc = StringHelper.formatObject(holder.get("QLRMC"));
										qlr_bdcqzh = StringHelper.formatObject(holder.get("BDCQZH"));
										if (!StringHelper.formatObject(holder.get("OPERATE")).equals("0")) {
											ifsave = true;
										}
										description = description + "权利人" + qlrSize + ":" + qlr_qlrmc + "," + qlr_bdcqzh + ",";
									}
								}
							}
						}
					}
					if (u != null && u.seals != null && u.seals.size() > 0) {
						for (int v = 0; v < u.seals.size(); v++) {
							int size = u.rights.size() + u.mortgages.size() + 1 + v;
							RightClass right = u.rights.get(v);
							if (right.newvalue != null) {
								ql_qllx = StringHelper.formatObject(right.newvalue.get("QLLX"));
								ql_djlx = StringHelper.formatObject(right.newvalue.get("DJLX"));
								ql_bdcqzh = StringHelper.formatObject(right.newvalue.get("BDCDQH"));
								if (!StringHelper.formatObject(right.newvalue.get("OPERATE")).equals("0")) {
									ifsave = true;
								}
								description = description + "权利" + size + ":" + ql_qllx + "," + ql_djlx + "," + ql_bdcqzh + ",";
								if (right.newvalue.getHolders() != null && right.newvalue.getHolders().size() > 0) {
									List<Holder> lstHolders = right.newvalue.getHolders();
									for (int c = 0; c < lstHolders.size(); c++) {
										int qlrSize = c + 1;
										Holder holder = new Holder(lstHolders.get(c));
										qlr_qlrmc = StringHelper.formatObject(holder.get("QLRMC"));
										qlr_bdcqzh = StringHelper.formatObject(holder.get("BDCQZH"));
										if (!StringHelper.formatObject(holder.get("OPERATE")).equals("0")) {
											ifsave = true;
										}
										description = "权利人" + qlrSize + ":" + qlr_qlrmc + "," + qlr_bdcqzh + ",";
									}
								}
							}
						}
					}
				}else if (i == 11 ) {
					description = description + "等" + uslast.getUnits().size() + "单元";
				}
				i++;
			}
		}
		datamaintenace.setLGDESCRIPTION(description);
		datamaintenace.setLGTYPE(StringHelper.getInt(MTTYPE.DJSJ.Value));
		datamaintenace.setLGTNAME(MTTYPE.DJSJ.Name);
		datamaintenace.setMTLX(StringHelper.getInt(mttype.Value));
		datamaintenace.setMTLXNAME(mttype.Name);
		User user = Global.getCurrentUserInfo();
		if(user != null){
			datamaintenace.setLGRYMC(user.getUserName());
			datamaintenace.setLGRYID(user.getId());
		}
		String osName = System.getProperty("os.name");
		try {
			if(osName.toLowerCase().indexOf("windows")>-1){
				if (!StringHelper.isEmpty(InetAddress.getLocalHost().getHostName())) {
					datamaintenace.setLGMACHINE(InetAddress.getLocalHost().getHostAddress() + "(" + InetAddress.getLocalHost().getHostName() + ")");
				}else {
					datamaintenace.setLGMACHINE(InetAddress.getLocalHost().getHostAddress());
				}
			}else{
				Enumeration<NetworkInterface> nif = NetworkInterface.getNetworkInterfaces();
		        while(nif.hasMoreElements()) {
		            NetworkInterface nekif = (NetworkInterface) nif.nextElement();
		            Enumeration<InetAddress> iparray = nekif.getInetAddresses();
		            while (iparray.hasMoreElements()) {
		                InetAddress ip = (InetAddress) iparray.nextElement();
		                if( ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
		                	if (!StringHelper.isEmpty(ip.getHostName())) {
		                		datamaintenace.setLGMACHINE(ip.getHostAddress() + "(" + ip.getHostName() + ")");
							}else {
								datamaintenace.setLGMACHINE(ip.getHostAddress());
							}
		                }
		            }
		        }
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		datamaintenace.setLGTIME(new Date());
		datamaintenace.setLGCONTENT(content);
		if (ifsave) {
			dao.save(datamaintenace);
			dao.flush();
		}
	}
	
	/* 
	 * 系统查询日志
	 */
	@Override
	public void queryLog(Boolean iflike, Map<String, String> queryvalues, QUERYTYPE querytype) {
		LOG_QUERY logQuery = new LOG_QUERY();
		logQuery.setLGTYPE(StringHelper.getInt(querytype.Value));
		logQuery.setLGTNAME(querytype.Name);
		User user = Global.getCurrentUserInfo();
		if(user != null){
			logQuery.setLGRYMC(user.getUserName());
			logQuery.setLGRYID(user.getId());
		}
		String osName = System.getProperty("os.name");
		try {
			if(osName.toLowerCase().indexOf("windows")>-1){
				if (!StringHelper.isEmpty(InetAddress.getLocalHost().getHostName())) {
					logQuery.setLGMACHINE(InetAddress.getLocalHost().getHostAddress() + "(" + InetAddress.getLocalHost().getHostName() + ")");
				}else {
					logQuery.setLGMACHINE(InetAddress.getLocalHost().getHostAddress());
				}
			}else{
				Enumeration<NetworkInterface> nif = NetworkInterface.getNetworkInterfaces();
		        while(nif.hasMoreElements()) {
		            NetworkInterface nekif = (NetworkInterface) nif.nextElement();
		            Enumeration<InetAddress> iparray = nekif.getInetAddresses();
		            while (iparray.hasMoreElements()) {
		                InetAddress ip = (InetAddress) iparray.nextElement();
		                if( ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
		                	if (!StringHelper.isEmpty(ip.getHostName())) {
		                		logQuery.setLGMACHINE(ip.getHostAddress() + "(" + ip.getHostName() + ")");
							}else {
								logQuery.setLGMACHINE(ip.getHostAddress());
							}
		                }
		            }
		        }
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		logQuery.setLGTIME(new Date());
		String description = getDscription(iflike, queryvalues, querytype);
		logQuery.setLGDESCRIPTION(description);
		dao.save(logQuery);
		dao.flush();
	}
	
	/** 描述信息
	 * @param iflike 是否模糊查询
	 * @param queryvalues 查询条件
	 * @param querytype 查询类型
	 * @return
	 */
	public String getDscription(Boolean iflike, Map<String, String> queryvalues, QUERYTYPE querytype){
		String description = "";
		if (querytype.Value.equals("7") || querytype.Value.equals("8") || querytype.Value.equals("9") || querytype.Value.equals("10") || querytype.Value.equals("11")) {// 宗地、自然幢、房屋、林地、农用地
			String qlrmc = queryvalues.get("QLR.QLRMC") == null ? "" : queryvalues.get("QLR.QLRMC"); // 权利人名称
			String qlrzjh = queryvalues.get("QLR.ZJH") == null ? "" : queryvalues.get("QLR.ZJH");  // 权利人证件号
			String zl = queryvalues.get("DY.ZL") == null ? "" : queryvalues.get("DY.ZL");  //坐落
			String bdcqzh = queryvalues.get("QL.BDCQZH") == null ? "" : queryvalues.get("QL.BDCQZH");  // 不动产权证号
			String ywbh = queryvalues.get("QL.YWH") == null ? "" : queryvalues.get("QL.YWH");  // 业务编号
			String dyzt = queryvalues.get("DYZT") == null ? "" : queryvalues.get("DYZT");  // 抵押状态
			String cfzt = queryvalues.get("CFZT") == null ? "" : queryvalues.get("CFZT");  // 查封状态
			String cxzt = queryvalues.get("CXZT") == null ? "" : queryvalues.get("CXZT");  // 查询状态
			String bdcdyh = queryvalues.get("DY.BDCDYH") == null ? "" : queryvalues.get("DY.BDCDYH");  // 不动产单元号
			String zddm = queryvalues.get("DY.ZDDM") == null ? "" : queryvalues.get("DY.ZDDM");  // 宗地代码
			String cfwh = queryvalues.get("FSQL.CFWH") == null ? "" : queryvalues.get("FSQL.CFWH");  // 查封文号
			String tdzt = queryvalues.get("TDZT") == null ? "" : queryvalues.get("TDZT");  // 土地状态
			String lpzt = queryvalues.get("LPZT") == null ? "" : queryvalues.get("LPZT");  // 楼盘状态
			String xmmc = queryvalues.get("XMMC") == null ? "" : queryvalues.get("XMMC");  // 项目名称
			String dyr = queryvalues.get("DYR.DYR") == null ? "" : queryvalues.get("DYR.DYR");  //抵押人
			String fwbm = queryvalues.get("DY.FWBM") == null ? "" : queryvalues.get("DY.FWBM");  //房屋编码
			String fh = queryvalues.get("DY.FH") == null ? "" : queryvalues.get("DY.FH");  // 房号
			String bdcqzhxh = queryvalues.get("QLR.BDCQZHXH") == null ? "" : queryvalues.get("QLR.BDCQZHXH"); 
			String fwzt = queryvalues.get("FWZT") == null ? "" : queryvalues.get("FWZT");  // 房屋状态
			String xdm = queryvalues.get("DY.XDM") == null ? "" : queryvalues.get("DY.XDM");  // 小地名
			description = getlogmsg(querytype,qlrmc,dyr,qlrzjh,zl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,iflike,zddm,tdzt,lpzt,xmmc,xdm);
		}else if(querytype.Value.equals("16")) { //小土地证
/*			String xm = queryvalues.get("XTDZ.XM") == null ? "" : queryvalues.get("XTDZ.XM");  // 姓名
			String fczh = queryvalues.get("XTDZ.FCZH") == null ? "" : queryvalues.get("XTDZ.FCZH");  // 房产证号
			String tdzh = queryvalues.get("XTDZ.TDZH") == null ? "" : queryvalues.get("XTDZ.TDZH");  // 土地证号
			String zl = queryvalues.get("XTDZ.ZL") == null ? "" : queryvalues.get("XTDZ.ZL");  // 坐落
			String txzh = queryvalues.get("XTDZ.TXZH") == null ? "" : queryvalues.get("XTDZ.TXZH");  // 他项证号
			String tdz = queryvalues.get("XTDZ.TDZ") == null ? "" : queryvalues.get("XTDZ.TDZ");  // 土地证号
			String dyr = queryvalues.get("XTDZ.DYR") == null ? "" : queryvalues.get("XTDZ.DYR");  // 抵押人
			String qlr = queryvalues.get("XTDZ.QLR") == null ? "" : queryvalues.get("XTDZ.QLR");  // 权利人
			String cfwh = queryvalues.get("XTDZ.CFWH") == null ? "" : queryvalues.get("XTDZ.CFWH");  // 查封文号
*/		}
		return description;
	}
	
	/** 
	 * 查询条件
	 * querytype,qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,iflike,zddm,tdzt,lpzt,xmmc,xdm
	 * @return
	 */
	public String getlogmsg(QUERYTYPE querytype,String qlrmc,String dyr,String qlrzjh,String fwzl,String bdcqzh,String fwbm,String fh,String ywbh,String dyzt,String cfzt,String cxzt,String fwzt,String bdcdyh,String bdcqzhxh,String cfwh,Boolean iflike,String zddm,String tdzt,String lpzt,String xmmc,String xdm){
		String sql="";
		sql = sql + "查询类型:" + querytype.Name + "; 查询条件:";
		//查询内容
		sql=sql+((qlrmc+"").length()==0?"":"权利人名称:"+qlrmc+"/");
		//活动类型
		sql=sql+((qlrzjh+"").length()==0?"":"身份证号:"+qlrzjh+"/");
		sql=sql+((bdcqzh+"").length()==0?"":"不动产权证号:"+bdcqzh+"/");
		sql=sql+((fwzl+"").length()>0?"坐落:"+fwzl+"/":"");
		sql=sql+((dyr+"").length()>0?"抵押人:"+dyr+"/":"");
		sql=sql+((ywbh+"").length()>0?"业务编号:"+ywbh+"/":"");
		sql=sql+((fwbm+"").length()>0?"房屋编码:"+fwbm+"/":"");
		sql=sql+((fh+"").length()>0?"房号:"+fh+"/":"");
		sql=sql+((bdcdyh+"").length()>0?"单元号:"+bdcdyh+"/":"");
		sql=sql+((cfwh+"").length()>0?"查封文号:"+cfwh+"/":"");
		if("0".equals(dyzt)){
			dyzt="全部";
		}else if("1".equals(dyzt)){
			dyzt="未抵押";
		}else if("2".equals(dyzt)){
			dyzt="抵押中";
		}
		sql=sql+((dyzt+"").length()>0?"抵押状态:"+dyzt+"/":"");
		if("0".equals(cfzt)){
			cfzt="全部";
		}else if("1".equals(cfzt)){
			cfzt="未被查封";
		}else if("2".equals(cfzt)){
			cfzt="已被查封";
		}
		sql=sql+((cfzt+"").length()>0?"查封状态:"+cfzt+"/":"");
		
		if("0".equals(fwzt)){
			fwzt="全部";
		}else if("1".equals(fwzt)){
			fwzt="现房";
		}else if("2".equals(fwzt)) {
			fwzt="期房";
		}
		sql=sql+((fwzt+"").length()>0?"房屋状态:"+fwzt+"/":"");
		
		if("1".equals(cxzt)){
			cxzt="全部";
		}else if("2".equals(cxzt)){
			cxzt="现房";
		}
		sql=sql+((cxzt+"").length()>0?"查询状态:"+cxzt+"/":"");
		if (iflike != null && iflike == true) {
			sql=sql+("模糊查询/");
		}
		sql=sql+((zddm+"").length()>0?"宗地代码:"+zddm+"/":"");
		if("3".equals(tdzt)){
			tdzt="全部";
		}else if("1".equals(tdzt)){
			tdzt="使用权宗地";
		}else if("2".equals(tdzt)){
			tdzt="所有权宗地";
		}
		sql=sql+((tdzt+"").length()>0?"宗地状态:"+tdzt+"/":"");
		if("0".equals(lpzt)){
			lpzt="全部";
		}else if("1".equals(lpzt)){
			lpzt="期楼幢";
		}else if("2".equals(lpzt)){
			lpzt="现楼幢";
		}
		sql=sql+((lpzt+"").length()>0?"楼盘状态:"+lpzt+"/":"");
		sql=sql+((xmmc+"").length()>0?"项目名称:"+xmmc+"/":"");
		sql=sql+((xdm+"").length()>0?"小地名:"+xdm+"/":"");
		return sql;
	}
	
	/* 
	 * 系统登录日志
	 */
	@Override
	public void loginLog(LOGINTYPE logintype, DLMSG msg) {
		LOG_LOGIN logLogin = new LOG_LOGIN();
		logLogin.setLGTYPE(StringHelper.getInt(logintype.Value));
		logLogin.setLGTNAME(logintype.Name);
		User user = Global.getCurrentUserInfo();
		if(user != null){
			logLogin.setLGRYMC(user.getUserName());
			logLogin.setLGRYID(user.getId());
		}
		String osName = System.getProperty("os.name");
		try {
			if(osName.toLowerCase().indexOf("windows")>-1){
				if (!StringHelper.isEmpty(InetAddress.getLocalHost().getHostName())) {
					logLogin.setLGMACHINE(InetAddress.getLocalHost().getHostAddress() + "(" + InetAddress.getLocalHost().getHostName() + ")");
				}else {
					logLogin.setLGMACHINE(InetAddress.getLocalHost().getHostAddress());
				}
			}else{
				Enumeration<NetworkInterface> nif = NetworkInterface.getNetworkInterfaces();
		        while(nif.hasMoreElements()) {
		            NetworkInterface nekif = (NetworkInterface) nif.nextElement();
		            Enumeration<InetAddress> iparray = nekif.getInetAddresses();
		            while (iparray.hasMoreElements()) {
		                InetAddress ip = (InetAddress) iparray.nextElement();
		                if( ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
		                	if (!StringHelper.isEmpty(ip.getHostName())) {
		                		logLogin.setLGMACHINE(ip.getHostAddress() + "(" + ip.getHostName() + ")");
							}else {
								logLogin.setLGMACHINE(ip.getHostAddress());
							}
		                }
		            }
		        }
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		Date date = new Date();
		logLogin.setLGTIME(date);
		logLogin.setLGDESCRIPTION("日志类型：" + logintype.Name + ",登录状态：" + msg.Name +  ",操作人员：" + user.getUserName() + ",操作时间：" + StringHelper.FormatYmdhmsByDate(date));
		dao.save(logLogin);
		dao.flush();
	}
}

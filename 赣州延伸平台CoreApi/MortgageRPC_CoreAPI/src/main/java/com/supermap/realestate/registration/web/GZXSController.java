package com.supermap.realestate.registration.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.BDCS_GZXS;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

import net.sf.json.JSONArray;





/**
 * 
 * @Description:贛州-契税功能
 * @author 刘树峰
 * @date 2015年6月12日 上午11:49:27
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/gzxs")
public class GZXSController {

	@Autowired
	private CommonDao baseCommonDao;
	
	/** 页面跳转路径 */
	private final String prefix = "/realestate/registration/";
	/**
	 * 保存或更新BDCS_GZXS(赣州协税)信息
	 * @作者 海豹
	 * @创建时间 2015年9月10日下午17:39
	 * @param BDCS_GZXS
	 */
	@RequestMapping(value = "/{xmbh}/savegzxsinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage savegzxsinfo(@PathVariable String xmbh, BDCS_GZXS bdcs_gzxs) {
		ResultMessage ms=new ResultMessage();
		try {
			String xmbhsql=ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_GZXS> gzxss=baseCommonDao.getDataList(BDCS_GZXS.class, xmbhsql);
			if(gzxss!=null && gzxss.size()>0){
				bdcs_gzxs.setXMBH(xmbh);
				bdcs_gzxs.setId(gzxss.get(0).getId());
				baseCommonDao.update(bdcs_gzxs);
				ms.setMsg("更新成功！");
				ms.setSuccess("true");
			}
			else
			{
				bdcs_gzxs.setXMBH(xmbh);
				baseCommonDao.save(bdcs_gzxs);
				ms.setMsg("保存成功！");
				ms.setSuccess("true");
			}
			baseCommonDao.flush();
		} catch (Exception e) {
		}
		return ms;
	}
	/**
	 * 获取BDCS_GZXS(赣州协税)信息
	 * @作者 海豹
	 * @创建时间 2015年9月10日下午17:39
	 * @param xmbh
	 */
	@RequestMapping(value = "/{xmbh}/getgzxsInfo", method = RequestMethod.GET)
	public @ResponseBody BDCS_GZXS getgzxsInfo2(Model model, @PathVariable String xmbh) {
		BDCS_GZXS bdcs_gzxs = null;
		 String xmbhsql=ProjectHelper.GetXMBHCondition(xmbh);
		 try {
			 List<BDCS_GZXS> gzxss = baseCommonDao.getDataList(BDCS_GZXS.class,
						xmbhsql);
				if (gzxss != null && gzxss.size() > 0) {
					bdcs_gzxs = gzxss.get(0);
				} else {
					bdcs_gzxs=new BDCS_GZXS();
				}
		} catch (Exception e) {
			
		}		
		model.addAttribute("gzxsAttribute", bdcs_gzxs);
		return bdcs_gzxs;
	}
	/*
	  * 加载赣州协税
	  */
		@RequestMapping(value ="/xs",method=RequestMethod.GET)
		public String ShowXS(Model model)
		{	
			String qhdm = ConfigHelper.getNameByValue("XZQHDM");
			model.addAttribute("xsAttribute",new BDCS_GZXS());
			if("360700".equals(qhdm))
				return prefix+"csdj/taxPage_gz";
			return prefix+"csdj/taxPage";
		}
		/**
		 * 赣州区域加载税务信息
		 * @param model
		 * @param xmbh
		 * @param request
		 * @param response
		 * @return
		 * @throws SQLException
		 * @throws IOException
		 */
		@SuppressWarnings("unused")
		@RequestMapping(value = "/{xmbh}/getgzxsInfo_gz", method = RequestMethod.GET)
		public @ResponseBody Message getgzxsInfo1(Model model, @PathVariable String xmbh,HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
			Message mes = new Message();
			Integer page = 1;
	    	if(request.getParameter("page")!=null){
	    		page = Integer.parseInt(request.getParameter("page"));
	    	}
	    	Integer rows = 10;
	    	if(request.getParameter("rows")!=null){
	    		rows = Integer.parseInt(request.getParameter("rows"));
	    	}
			BDCS_GZXS bdcs_gzxs = null;
			HttpURLConnection conn = null;
			ResultSet rs = null;
			Connection con = null;
			BufferedReader in = null;
			StringBuilder strb = new StringBuilder();
			StringBuilder count = new StringBuilder();
			String sqrmc="",relationid="",fwbm="",result= "",bdcdyh="";
			String sql = "select relationid,h.bdcdyh from bdck.bdcs_xmxx xx left join bdck.bdcs_djdy_gz djdy on xx.xmbh"
					+ "=djdy.xmbh left join bdck.bdcs_h_xz h on djdy.bdcdyid=h.bdcdyid where xx.xmbh='"+xmbh+"'";
			String check = "select * from bdck.bdcs_gzxs a where a.xmbh='"+xmbh+"'";
			List<Map> checkmap = baseCommonDao.getDataListByFullSql(check);
			
			List<BDCS_SQR> list = baseCommonDao.getDataList(BDCS_SQR.class, "xmbh='"+xmbh+"' and sqrlb='1'");
			if(list!=null&&list.size()>0){
				sqrmc=list.get(0).getSQRXM().trim();
				
			}
			sqrmc=URLEncoder.encode(URLEncoder.encode((sqrmc).toString(), "utf-8"));
			List<Map> h = baseCommonDao.getDataListByFullSql(sql);
			if(h!=null&&h.size()>0){
				for(Map m : h){
					relationid =m.get("RELATIONID")==null?"":m.get("RELATIONID").toString();
					bdcdyh = m.get("BDCDYH")==null?"":m.get("BDCDYH").toString();
					con = JH_DBHelper.getConnect_gxjyk();
					String str = "select a.fwbm from gxjyk.h a where a.relationid='"+relationid+"'";
					rs = JH_DBHelper.excuteQuery(con, str);
					while(rs.next()){
						fwbm = rs.getString("FWBM");
					}
					URL url = new URL("http://17.128.25.181:8080/sharesearch/app/swxx?qlrmc="+sqrmc+"&fwbm="+fwbm);
					try {
						conn = (HttpURLConnection) url.openConnection();
						conn.setRequestProperty("Accept-Charset", "utf-8");
						conn.setRequestProperty("contentType", "utf-8");
						conn.setDoOutput(false);
						conn.setDoInput(true);
						conn.setRequestMethod("GET");
						conn.setReadTimeout(100000);
						conn.setConnectTimeout(65000);
				        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
					    conn.connect();
					    int code = conn.getResponseCode();
						in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
						StringBuilder sb = new StringBuilder();
						while((result=in.readLine())!=null){
							sb.append(result);
						}
							
							//JSONObject jsonobj = JSONObject.parseObject(sb.toString());
						Object jsonobj = JSONObject.parse(sb.toString());
						String strs = jsonobj.toString();
						String strarray = "";
						String strsfws = strs.substring(strs.length()-3,strs.length()-2);
						bdcs_gzxs = new BDCS_GZXS();
						if(strs.length()>30){
							strarray = strs.substring(8,strs.length()-12);
							com.alibaba.fastjson.JSONArray ob = JSONObject.parseArray(strarray);
							Object[] os = ob.toArray();
					        String szmc="";Date JSRQ = null;
					        if(checkmap==null||checkmap.size()<=0){
						        for(int i =0 ;i<os.length;i++){
						        	bdcs_gzxs = new BDCS_GZXS();
						        	JSONObject js = (JSONObject) JSONObject.parse(os[i].toString());
						        	bdcs_gzxs.setSZMC(js.getString("SZMC"));
						        	bdcs_gzxs.setFWBM(bdcdyh);
						        	bdcs_gzxs.setHTBH(js.getString("HTBH"));
						        	bdcs_gzxs.setId(UUID.randomUUID().toString());
						        	bdcs_gzxs.setJSYJ(js.getString("JSYJ"));
						        	bdcs_gzxs.setNSRMC(js.getString("NSRMC"));
						        	bdcs_gzxs.setSFWS(strsfws);
						        	bdcs_gzxs.setSL(js.getDouble("SL"));
						        	bdcs_gzxs.setSMMC(js.getString("SMMC"));
						        	bdcs_gzxs.setZJH(js.getString("SFZJHM"));
						        	bdcs_gzxs.setSE(js.getDouble("SE"));
						        	String ts = js.getString("JSRQ");
						        	long time = JSON.parseObject(ts).getLong("time"); 
						        	JSRQ = new Date(time);
						        	bdcs_gzxs.setJSRQ(JSRQ);
						        	bdcs_gzxs.setXMBH(xmbh);
						        	baseCommonDao.save(bdcs_gzxs);
						        	baseCommonDao.flush();
					        	}
						    }
							else{
								if(checkmap==null||checkmap.size()<=0){
									bdcs_gzxs.setXMBH(xmbh);
									bdcs_gzxs.setSFWS(strsfws);
									baseCommonDao.save(bdcs_gzxs);
						        	baseCommonDao.flush();
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						if(in!=null)
							in.close();
						if(conn!=null)
							conn.disconnect();
						if(rs!=null)
							rs.close();
						if(con!=null)
							con.close();
					}
				}
			}
			strb.append("select xs.nsrmc,xs.zjh,xs.fwbm,xs.szmc,xs.smmc,xs.se,xs.htbh,xs.sfws,to_char(xs.jsrq,'yyyy-mm-dd') as jsrq")
			.append(" from bdck.bdcs_gzxs xs where xs.xmbh='").append(xmbh).append("'");
			count.append(" from(").append(strb).append(")");
			long total = baseCommonDao.getCountByFullSql(count.toString());
			List<Map> map = baseCommonDao.getPageDataByFullSql(strb.toString(), page, rows);
			if(total==0)
				return new Message();
			mes.setTotal(total);
			mes.setRows(map);
			return mes;
		}
}

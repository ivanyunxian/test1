/**   
 * TODO:@liushufeng:请描述这个文件
 * @Title: GeoPictureController.java 
 * @Package com.supermap.realestate.registration.web 
 * @author liushufeng 
 * @date 2016年10月14日 下午6:09:44 
 * @version V1.0   
 */

package com.supermap.realestate.registration.web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.supermap.realestate.registration.maintain.WebHelper;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.zip.tool.CompresszZipFile;

import net.sf.json.JSONObject;

/**
 * TODO:@liushufeng:请描述这个类或接口"GeoPictureController"
 * @ClassName: GeoPictureController
 * @author liushufeng
 * @date 2016年10月14日 下午6:09:44
 */
@Controller
@RequestMapping(value = "geo")
public class GeoPictureController {

	@Autowired
	CommonDao dao;

	/**
	 * 测试方法
	 * @Title: test 
	 * @author:liushufeng
	 * @date：2016年10月14日 下午6:59:10
	 * @return
	 */
	@RequestMapping(value = "/test")
	public @ResponseBody String test() {
		//String url = "http://localhost:8081/convertcad/cadtojson/";
		String url = "http://localhost:8081/convertcad/geo/test1?path=c:\00225920.dwg";
		String filename = "c:\\00225920.dwg";
		Map<String, String> textmap = new HashMap<String, String>();
		Map<String, String> filemap = new HashMap<String, String>();
		filemap.put("file", filename);
		String strjson = simulateFormUpload(url, textmap, filemap);
		return strjson;
	}

	/**
	 * 分层分户图页面
	 * @Title: geopicture 
	 * @author:liushufeng
	 * @date：2016年10月14日 下午7:02:23
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/geopicture", method = RequestMethod.GET)
	public String geopicture(Model model) {
		return "/workflow/buildingtable/geopicture";
	}
	
	/**
	 * 接口方法：传入一个户的BDCDYID,CAD文件的路径，将CAD文件转换成JSON存储到户表的HJSON字段内
	 * @Title: writeJson 
	 * @author:liushufeng
	 * @date：2016年10月14日 下午6:58:16
	 * @param housebdcdyid 房屋不动产单元ID
	 * @param cadfilepath CAD文件路径
	 * @return
	 * @throws SQLException 
	 * @throws SerialException 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public boolean writeJson(String housebdcdyid,String cadfilepath) throws SerialException, SQLException, UnsupportedEncodingException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
//		String url = "http://17.128.26.245:8081/convertcad/cadtojson/";
		String url = ConfigHelper.getNameByValue("CADCONVERTPATH");
		Map<String, String> textmap = new HashMap<String, String>();
		Map<String, String> filemap = new HashMap<String, String>();
		filemap.put("file", cadfilepath);
		String strjson = simulateFormUpload(url, textmap, filemap);
		BDCS_H_XZ h=dao.get(BDCS_H_XZ.class, housebdcdyid);
		if (!StringHelper.isEmpty(strjson)) {
			if(h != null && !StringHelper.isEmpty(strjson.trim())){
				byte[] b = strjson.getBytes("gbk");
				h.setHJSON(b);
				h.setYYZT("1");
				h.setFCFHTSLTX(housebdcdyid+".zip");
				dao.update(h);
				dao.flush();
//				String sql = "update bdck.bdcs_h_xz a set a.hjson="+b+",a.yyzt='1' where a.bdcdyid='"+housebdcdyid+"'";
//				Connection conn =JH_DBHelper.getConnect_bdck();
//				JH_DBHelper.excuteUpdate(conn, sql);
//				conn.close();
				String user = Global.getCurrentUserName();
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				YwLogUtil.addYwLog(user+"在"+sdf.format(date)+"上传了分户图", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * 前端调用的方法
	 * @Title: getJson 
	 * @author:liushufeng
	 * @date：2016年10月14日 下午7:09:13
	 * @param bdcdyid
	 * @param bdcdylx
	 * @param djdyly
	 * @return
	 */
	@RequestMapping(value="/unitjson/{bdcdylx}/{djdyly}/{bdcdyid}")
	public @ResponseBody String getJson(@PathVariable String bdcdyid, @PathVariable String bdcdylx, @PathVariable String djdyly) throws Exception{
		BDCS_H_XZ hxz=null;
		List<BDCS_H_XZ> hs=dao.getDataList(BDCS_H_XZ.class, " bdcdyh='"+bdcdyid.replaceAll(" ", "")+"'");
		if(hs!=null&&hs.size()>0)
		hxz=hs.get(0);
		JSONObject obj=null;
		byte[] b = hxz.getHJSON();
		if (b != null) {
			String str = new String(b,"gbk");
			if(!StringHelper.isEmpty(hxz)){
				if(StringHelper.isEmpty(str)){
					obj=JSONObject.fromObject("{}");
				}
				else{
					obj=JSONObject.fromObject(str);
				}
			}
		}
		return obj==null?"":obj.toString();
	}
	
	
	@RequestMapping(value="/dyjson/{bdcdylx}/{djdyly}/{bdcdyid}")
	public @ResponseBody String uploadFCFHT(@PathVariable String bdcdyid, @PathVariable String bdcdylx, @PathVariable String djdyly,@PathVariable MultipartFile file) throws Exception {
		String addr = ConfigHelper.getNameByValue("FHTPATH");
		String filename = addr+"\\"+file.getOriginalFilename();
		File f = new File(filename);
		if(!f.exists()){
			f.mkdirs();
		}
		file.transferTo(f);
		boolean b = writeJson(bdcdyid,filename);
		JSONObject obj = null;
		if (b) {
			RealUnit unit = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, bdcdyid);
			BDCS_H_XZ hxz = (BDCS_H_XZ)unit;
			byte[] bytes = hxz.getHJSON();
			String str = new String(bytes,"gbk");
			if(!StringHelper.isEmpty(hxz)){
				if(StringHelper.isEmpty(hxz.getHJSON())){
					obj=JSONObject.fromObject("{}");
				}
				else{
					obj = JSONObject.fromObject(str);
				}
			}
		}else {
			f.delete();
		}
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * 内部方法：模拟表单提交上传文件到rest服务。
	 * @Title: simulateFormUpload
	 * @author:liushufeng
	 * @date：2016年7月6日 上午10:35:32
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String simulateFormUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
		String res = "";
		String contentType;
		HttpURLConnection conn = null;
		// boundary就是request头和上传文件内容的分隔符
		String BOUNDARY = "---------------------------123821742118716";
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(500000);
			conn.setReadTimeout(100000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			String sessionid = WebHelper.getSessionID("realestate");
			conn.addRequestProperty("Cookie", "JSESSIONID=" + sessionid);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();

					// 没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
					contentType = new MimetypesFileTypeMap().getContentType(file);
					// contentType非空采用filename匹配默认的图片类型
					if (!"".equals(contentType)) {
						if (filename.endsWith(".png")) {
							contentType = "image/png";
						} else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
							contentType = "image/jpeg";
						} else if (filename.endsWith(".gif")) {
							contentType = "image/gif";
						} else if (filename.endsWith(".ico")) {
							contentType = "image/image/x-icon";
						}
					}
					if (contentType == null || "".equals(contentType)) {
						contentType = "application/octet-stream";
					}
					System.out.println("contentType为："+contentType);
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			conn.getResponseCode();
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res=new String(strBuf.toString());//.getBytes(),"UTf-8"
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("成果包内附件上传到项目收件资料时出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
	/**
	 * 赣州区域解析存量cad文件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/plscfht",method=RequestMethod.GET)
	public @ResponseBody Message getallFiles(HttpServletRequest request,HttpServletResponse response)throws Exception{
		StringBuilder str = new StringBuilder();
		str.append("select ql.bdcqzh,h.bdcdyid from bdck.bdcs_djdy_ls djdy left join bdck.bdcs_h_xz h on djdy.bdcdyid=h.bdcdyid ")
		.append("left join bdck.bdcs_ql_ls ql on djdy.djdyid=ql.djdyid where hjson is null and h.fcfhtsltx is null");
   		List<Map> maps = dao.getDataListByFullSql(str.toString());
   		//String path = "F:\\房产测绘数据";
		String path= ConfigHelper.getNameByValue("CLSJDZ");
		Message m = new Message();
		File file = new File(path);
		String qzh = "",bdcdyid="";
		if(file.exists()){
			File[] files = file.listFiles();
			for (File file2 : files){
				if(file2.isFile()){
					getMessgae(file2, maps, qzh, bdcdyid);
				}
				else if(file2.isDirectory()){
					isDir(file2,maps);
				}
			}
		}
		return m;
	}
	/**
	 * 赣州区域解析存量cad文件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/jy",method=RequestMethod.GET)
	public @ResponseBody Message JYFHT(HttpServletRequest request,HttpServletResponse response)throws Exception{
		Message m = new Message();
		int page = Integer.parseInt(request.getParameter("page"));
		int rows = Integer.parseInt(request.getParameter("rows")); 
		StringBuilder str = new StringBuilder();
		str.append("select h.bdcdyid,h.fcfhtsltx from bdck.bdcs_h_xz h where h.fcfhtsltx is not null");
		List<Map> maps = dao.getPageDataByFullSql(str.toString(), page, rows);
		String path = ConfigHelper.getNameByValue("SLTXPATH");
		CompresszZipFile compress = new CompresszZipFile();
		File file = new File(path);
		String sltx = "";
		String bdcdyid="";
		if(maps!=null&&maps.size()>0){
			for(Map map : maps){
				sltx = map.get("FCFHTSLTX")==null?"":map.get("FCFHTSLTX").toString();
				bdcdyid = map.get("BDCDYID")==null?"":map.get("BDCDYID").toString();
				File f = new File(file,sltx);
				if(f.exists()){
					String zippath = f.getAbsolutePath().toString();
					String destPath = trimExtension(zippath);
					compress.ReadZip(zippath, path);
					File newFile = new File(destPath);
					File[] files = newFile.listFiles();
					for(File fi : files){
						if(fi.isFile()){
							String file2 = fi.getAbsolutePath();
							boolean b = writeJson(bdcdyid,file2);
							fi.delete();
						}
					}
				}
			}
		}
		return m;
	}
	
	private  String trimExtension(String filename)
	{         
		if ((filename != null) && (filename.length() > 0)) 
		{ 
			int i = filename.lastIndexOf('.');  
			if ((i >-1) && (i < (filename.length()))) 
			{
				return filename.substring(0, i); 
			} 
		}  
		return filename; 
	} 
	
	private void isDir(File file,List<Map> maps) throws SerialException, UnsupportedEncodingException, SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		File f2 = new File(file.getAbsolutePath());
		Message m = new Message();
		String qzh="",bdcdyid="";
		if(f2.exists()){
			File[] fs2 = f2.listFiles();
			for(File f3 :fs2){
				if(f3.isFile()){
					String filename = f3.getAbsolutePath();
					String name = filename.substring(filename.lastIndexOf('\\')+1);
					if(name.indexOf(".dwg")!=-1){
						name = name.replaceAll(".dwg", "");
							for(Map map :maps){
								qzh = map.get("BDCQZH")==null?"":map.get("BDCQZH").toString();
								bdcdyid = map.get("BDCDYID")==null?"":map.get("BDCDYID").toString();
								if(name.equals(qzh)){
									boolean b = writeJson(bdcdyid, filename);
									file.delete();
									m.setMsg("分户图上传成功");
									m.setSuccess("true");
									YwLogUtil.addYwLog("上传分户图，成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
								}
								else{
									continue;
								}
							}
						}
					}
				else if(f3.isDirectory()){
					 isDir(f3,maps);
				}
			}
		}
	}
	
	private void getMessgae(File file,List<Map> maps,String qzh,String bdcdyid) throws SerialException, UnsupportedEncodingException, SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Message m = new Message();
		String filename = file.getAbsolutePath();
		String name = filename.substring(filename.lastIndexOf('\\')+1);
		if(name.indexOf(".dwg")!=-1){
			name = name.replaceAll(".dwg", "");
			if(maps!=null&&maps.size()>0){
				for(Map map :maps){
					qzh = map.get("BDCQZH")==null?"":map.get("BDCQZH").toString();
					bdcdyid = map.get("BDCDYID")==null?"":map.get("BDCDYID").toString();
					if(name.equals(qzh)){
						boolean b = writeJson(bdcdyid, filename);
						file.delete();
						m.setMsg("分户图上传成功");
						m.setSuccess("true");
						YwLogUtil.addYwLog("上传分户图，成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
					}
				}
			}
		}
	}
}

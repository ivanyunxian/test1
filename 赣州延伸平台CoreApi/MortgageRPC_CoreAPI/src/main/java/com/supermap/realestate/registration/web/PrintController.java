package com.supermap.realestate.registration.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.tools.EncodeTools;
import com.supermap.realestate.registration.tools.ImageMD5Tool;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.PDFHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ZipUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.zip.tool.CompresszZipFile;


@Controller
@RequestMapping("/print")
public class PrintController {

	@Autowired
	private CommonDao dao;
	
	@Autowired
	private GeoPictureController geo;

	private final String prefix = "/realestate/registration/manage";

	@RequestMapping(value = "/fht", method = RequestMethod.GET)
	public String ShowFHT(Model model) {
		return prefix + "/fht";
	}
	@RequestMapping(value = "/zdt", method = RequestMethod.GET)
	public String ShowZDT(Model model) {
		return prefix + "/zdt";
	}
	
	@RequestMapping(value = "/zrzzdt", method = RequestMethod.GET)
	public String ShowZRZZDT(Model model) {
		return prefix + "/zrzzdt";
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
	private List<File> GetFileList(File node,List<File> files)
	{
		if(node.isFile()){
			files.add(node); 
		}
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				GetFileList(new File(node, filename),files);
			}
		}
		return files;
	}
	/**
	 * 根据不动产单元Id检索文件夹获取以不动产单元Id命名的压缩文件路径
	 * node 检索的文件夹 
	 * bdcdyid 不动产单元Id
	 */
	private String findZipFileNameByBDCDYID(File node,String bdcdyid){
		List<File> files=new ArrayList<File>();
		GetFileList(node,files);
		for(File file:files)
		{
			if(trimExtension(file.getName()).equals(bdcdyid))
				return file.getAbsoluteFile().toString();
		}
		return "";
	}
	
	/**
	 * @param 获取幢宗地图名字
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 * @author hmh
	 */
	@RequestMapping(value = "/zsid/encryption", method = RequestMethod.GET)
	public  @ResponseBody  String  getzsidjm(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		try {
			String zsid=request.getParameter("zsid");
			if(!StringHelper.isEmpty(zsid)) {
				return EncodeTools.encoderByDES(zsid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return "0";
		}
	/**
	 * @param 获取幢宗地图名字
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 * @author hmh
	 */
	@RequestMapping(value = "/zrzzdtname", method = RequestMethod.POST)
	public  @ResponseBody  String  getZRZZDT(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {	
		String bdcdyid="",sql="",zrzbdcdyh = ""; 
		String bdcdyh=request.getParameter("bdcdyh");
		String bdcdylx=request.getParameter("bdcdylx");
		bdcdyh=bdcdyh.replaceAll(" ", "");
		String bdcs_zrz_gz_sql="select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_gz  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_gz gz where gz.bdcdyh = '"+bdcdyh+"')";
		String bdcs_zrz_gzy_sql="select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_gzy  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_gzy gzy where gzy.bdcdyh = '"+bdcdyh+"')";
		
		//假如是补录或者数据整合的数据，只查gz层可能会没有数据，所以判断一下是否有数据
		if(bdcdylx.equals("031")) {
			List<Map> list = dao.getDataListByFullSql(bdcs_zrz_gz_sql);
			if(list.size()>0) {
				sql=bdcs_zrz_gz_sql;//现房
			}else {
				sql = "select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_xz  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_xz gz where gz.bdcdyh = '"+bdcdyh+"')";
			}
		}
		if(bdcdylx.equals("032")) {
			List<Map> list = dao.getDataListByFullSql(bdcs_zrz_gzy_sql);
			if(list.size()>0) {
				sql=bdcs_zrz_gzy_sql;//期房
			}else {
				sql = "select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_xzy  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_xzy gzy where gzy.bdcdyh = '"+bdcdyh+"')";
			}
			
		}
		 	

		String zdtPath=ConfigHelper.getNameByValue("ZRZZDTPATH");
		String imgfrom=ConfigHelper.getNameByValue("ZDTFROM");
		ResultSet sqlDataRSet=dao.excuteQuery(sql);
		if (sqlDataRSet.next()){
			//获取不动产单元Id
			bdcdyid=sqlDataRSet.getString("bdcdyid");
			zrzbdcdyh = sqlDataRSet.getString("bdcdyh");
		}
		
		System.out.println("获取幢宗地图SQL语句："+sql);
		File uploadfolder=new File(zdtPath);
	        if(imgfrom.equals("3"))//以不动产单元Id找附件
	        {
	        	if(uploadfolder.exists()){	
	        		User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
					File zdt_file=new File(uploadfolder, bdcdyid+".zip");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								 zdt_file=new File(uploadfolder, bdcdyh+".zip");	
								 break;
							}
						}
					}
					 
//					}
					if(zdt_file.exists())
					{
						String zipPath=zdt_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//ziputil.unzip(zipPath,destPath);
						//解压文件（支持中文）
						 CompresszZipFile compress = new CompresszZipFile();
						 compress.ReadZip(zipPath, zdtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								return subNote[0] ;
								
							}
						}
					}
	        	}
	        }else if(imgfrom.equals("2")){//以不动产单元号找附件
	        	if(uploadfolder.exists()){	
	        		User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
					File zdt_file=new File(uploadfolder, zrzbdcdyh+".zip");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								 zdt_file=new File(uploadfolder, zrzbdcdyh+".zip");	
								 break;
							}
						}
					}
					 
					if(zdt_file.exists()){
						String zipPath=zdt_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//ziputil.unzip(zipPath,destPath);
						//解压文件（支持中文）
						 CompresszZipFile compress = new CompresszZipFile();
						 compress.ReadZip(zipPath, zdtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								return subNote[0] ;
								
							}
						}
					}
	        	}
	        }
			return null;
	        
	        
	}
	
	
	/**
	 * @param 获取幢宗地图文件
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 * @author hmh
	 */
	@RequestMapping(value = "/{bdcdyh}/zrz/{bdcdylx}", method = RequestMethod.GET)
	public void getZRZZDTWJ(@PathVariable("bdcdyh") String bdcdyh,@PathVariable("bdcdylx") String bdcdylx,HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String errorPath = String.format("%s\\resources\\images\\404.bmp",request.getRealPath("/"));
		//bdcdyh="450330100002GB01875F00010001";
		String zdt="",bdcdyid="",sql="",zrzbdcdyh = ""; 
		File bmp = null;
		InputStream inStream=null;
		byte[] f=null;
		String filename="幢宗地图";
		Blob blob=null;
		BufferedImage bim=null;
		bdcdyh=bdcdyh.replaceAll(" ", "");
		String bdcs_zrz_gz_sql="select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_gz  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_gz gz where gz.bdcdyh = '"+bdcdyh+"')";
		String bdcs_zrz_gzy_sql="select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_gzy  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_gzy gzy where gzy.bdcdyh = '"+bdcdyh+"')";
		
		//假如是补录或者数据整合的数据，只查gz层可能会没有数据，所以判断一下是否有数据
		if(bdcdylx.equals("031")) {
			List<Map> list = dao.getDataListByFullSql(bdcs_zrz_gz_sql);
			if(list.size()>0) {
				sql=bdcs_zrz_gz_sql;//现房
			}else {
				sql = "select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_xz  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_xz gz where gz.bdcdyh = '"+bdcdyh+"')";
			}
		}
		 	
		if(bdcdylx.equals("032")) {
			List<Map> list = dao.getDataListByFullSql(bdcs_zrz_gzy_sql);
			if(list.size()>0) {
				sql=bdcs_zrz_gzy_sql;//期房
			}else {
				sql = "select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_xzy  where bdcdyid = (select max(zrzbdcdyid) from bdck.bdcs_h_xzy gzy where gzy.bdcdyh = '"+bdcdyh+"')";
			}
			
		}
		String zdtPath=ConfigHelper.getNameByValue("ZRZZDTPATH");
		String imgfrom=ConfigHelper.getNameByValue("ZDTFROM");
		ResultSet sqlDataRSet=dao.excuteQuery(sql);
		System.out.println("获取宗地图SQL语句："+sql);
		if (sqlDataRSet.next())
		{
			//获取分层分户图文件流
			blob = sqlDataRSet.getBlob("zdtwj");
			//获取分户图文件名
//			zdt=sqlDataRSet.getString("zdt");
			//获取不动产单元Id
			bdcdyid=sqlDataRSet.getString("bdcdyid");
			zrzbdcdyh = sqlDataRSet.getString("bdcdyh");
		  if(blob!=null)
			inStream = blob.getBinaryStream();
		}
//		PreparedStatement PS = sqlDataRSet.unwrap(PreparedStatement.class);
//		sqlDataRSet.close();
//		if(PS!=null) {
//			PS.close();
//		}
		File uploadfolder=new File(zdtPath);
		   if(imgfrom.equals("1"))//从二进制方式读取
		   {
			   System.out.println("从二进制读取图片(bdck.bdcs_zrz_gz.zdtwj)");
				if(inStream!=null)
				{
					bim= ImageIO.read(inStream);
				}//第二优先读数据库记录文件路径
			
		   }
	        
	        if(imgfrom.equals("2"))//以上传文件名找附件
	        {
	        	System.out.println("根据文件名读附件(bdck.bdcs_zrz_gz.zdt)");
	        	if(uploadfolder.exists()){
//				File zdt_database=new File(uploadfolder, zdt);
				File zdt_database=new File(uploadfolder, zrzbdcdyh+".zip");
				if(zdt_database.exists())
				{
					String zipPath=zdt_database.getAbsoluteFile().toString();
					String destPath=trimExtension(zipPath);
					//解压文件(不支持中文)
					//ZipUtil ziputil=new ZipUtil();
					//ziputil.unzip(zipPath,destPath);
					//解压文件（支持中文）
					 CompresszZipFile compress = new CompresszZipFile();
					 System.out.println("解压文件（支持中文）");
					 compress.ReadZip(zipPath, zdtPath);  
					File destFile =new File(destPath);
					//读取解压文件中的图片流
					if(destFile.exists())
					{
						String[] subNote = destFile.list();
						if(subNote.length>0)
						{
							bmp=new File(destFile,subNote[0]);
							filename=subNote[0];
						}
					}
				}
				else
					System.out.println("未配置宗地图上传路径："+zdtPath);
	        	}
	        }
	        if(imgfrom.equals("3"))//以不动产单元Id找附件
	        {
	        	if(uploadfolder.exists()){	
	        		System.out.println("根据不动产单元Id读取上传文件(bdck.bdcs_zrz_gz.bdcdyid)");
					File zdt_file=new File(uploadfolder, bdcdyid+".zip");
					User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
	        		bdcdyid=sqlDataRSet.getString("bdcdyid");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								 zdt_file=new File(uploadfolder, bdcdyh+".zip");	
								 break;
							}
						}
					}
					if(zdt_file.exists())
					{
						String zipPath=zdt_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//解压文件(不支持中文)
					    //ZipUtil ziputil=new ZipUtil();
						//ziputil.unzip(zipPath,destPath);
						//解压文件（支持中文）
						 CompresszZipFile compress = new CompresszZipFile();
						 System.out.println("解压文件（支持中文）");
						 compress.ReadZip(zipPath, zdtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								//只读取解压文件夹中第一张文件
								bmp=new File(destFile,subNote[0]);
								filename=subNote[0];
//								bim=ImageIO.read(bmp);
							}
						}
					}
	        	}
				else
					System.out.println("未配置宗地图上传路径："+zdtPath);
	        }
	        f=FileUtils.readFileToByteArray(bmp);
	        if (filename != null && filename.toLowerCase().indexOf(".pdf") > 0) {
				response.setHeader("Content-Disposition", "inline; filename=" + filename);
				response.setContentType("application/pdf; charset=UTF-8");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setContentType("image/jpeg; charset=UTF-8");
			}
	        if(f==null) {
	        	File errorbmp=new File(errorPath);
	        	f=FileUtils.readFileToByteArray(errorbmp);
	        } 
	        response.getOutputStream().write(f);
	        
	        
	}
	
	
	/**
	 * @param 获取宗地图名字
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/zdtname", method = RequestMethod.POST)
	public  @ResponseBody  String  getZDT(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {	
		String bdcdyid="",sql="",zdbdcdyh = ""; 
		String bdcdyh=request.getParameter("bdcdyh");
		String bdcdylx=request.getParameter("bdcdylx");
		bdcdyh=bdcdyh.replaceAll(" ", "");
		String bdcs_syqzd_sql="select bdcdyid,zdt,zdtwj ,qxdm,bdcdyh from bdck.bdcs_syqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid,zdt,zdtwj ,qxdm,bdcdyh  from bdck.bdcs_syqzd_xz where bdcdyh='"+bdcdyh+"'";
		String bdcs_shyqzd_sql="select bdcdyid,zdt,zdtwj ,qxdm,bdcdyh from bdck.bdcs_shyqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid,zdt,zdtwj,qxdm,bdcdyh   from bdck.bdcs_shyqzd_xz where bdcdyh='"+bdcdyh+"'";
		//宗地的
		if(bdcdyh.toUpperCase().contains("W"))
		{
			if(bdcdylx.equals("01"))
		 	sql=bdcs_syqzd_sql;
		  if(bdcdylx.equals("02"))
		 	sql=bdcs_shyqzd_sql;
		}
		
		//房屋的  
		if(bdcdyh.toUpperCase().contains("F"))
		   sql="select bdcdyid,zdt,zdtwj,qxdm,bdcdyh from bdck.bdcs_shyqzd_xz  where bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"')";
		 
		//获取宗地图文件夹路径
		//String zdtPath=GetProperties.getConstValueByKey("ZDT");
		String zdtPath=ConfigHelper.getNameByValue("ZDTPATH");
		String imgfrom=ConfigHelper.getNameByValue("ZDTFROM");
		ResultSet sqlDataRSet=dao.excuteQuery(sql);
//		PreparedStatement PS = sqlDataRSet.unwrap(PreparedStatement.class);
//		sqlDataRSet.close();
//		if(PS!=null) {
//			PS.close();
//		}
		if (sqlDataRSet.next())
		{
			//获取不动产单元Id
			bdcdyid=sqlDataRSet.getString("bdcdyid");
			zdbdcdyh = sqlDataRSet.getString("bdcdyh");//宗地不动产单元号
		}
		
		System.out.println("获取宗地图SQL语句："+sql);
		File uploadfolder=new File(zdtPath);
	        if(imgfrom.equals("3"))//以不动产单元Id找附件
	        {
	        	if(uploadfolder.exists()){	
	        		User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
					File zdt_file=new File(uploadfolder, bdcdyid+".zip");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								 zdt_file=new File(uploadfolder, bdcdyh+".zip");	
								 break;
							}
						}
					}
					 
//					}
					if(zdt_file.exists())
					{
						String zipPath=zdt_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//ziputil.unzip(zipPath,destPath);
						//解压文件（支持中文）
						 CompresszZipFile compress = new CompresszZipFile();
						 compress.ReadZip(zipPath, zdtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								return subNote[0] ;
								
							}
						}
					}
	        	}
	        }else if(imgfrom.equals("2")) {
	        	//以不动产单元号找附件
	        	if(uploadfolder.exists()){	
	        		User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
					File zdt_file=new File(uploadfolder, zdbdcdyh+".zip");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								 zdt_file=new File(uploadfolder, zdbdcdyh+".zip");	
								 break;
							}
						}
					}
					 
					if(zdt_file.exists()){
						String zipPath=zdt_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//ziputil.unzip(zipPath,destPath);
						//解压文件（支持中文）
						 CompresszZipFile compress = new CompresszZipFile();
						 compress.ReadZip(zipPath, zdtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								return subNote[0] ;
								
							}
						}
					}
	        	}
	        }
			return null;
	        
	        
	}
	
	
	
	
	
	/**
	 * @param 获取宗地图文件
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/{bdcdyh}/zd/{bdcdylx}", method = RequestMethod.GET)
	public void getZDTWJ(@PathVariable("bdcdyh") String bdcdyh,@PathVariable("bdcdylx") String bdcdylx,HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String errorPath = String.format("%s\\resources\\images\\404.bmp",request.getRealPath("/"));
		//bdcdyh="450330100002GB01875F00010001";
		String zdt="",bdcdyid="",sql="",zdbdcdyh = ""; 
		File bmp = null;
		InputStream inStream=null;
		byte[] f=null;
		String filename="宗地图";
		Blob blob=null;
		BufferedImage bim=null;
		bdcdyh=bdcdyh.replaceAll(" ", "");
		String bdcs_syqzd_sql="select bdcdyid,zdt,zdtwj ,qxdm,bdcdyh from bdck.bdcs_syqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid,zdt,zdtwj ,qxdm,bdcdyh  from bdck.bdcs_syqzd_xz where bdcdyh='"+bdcdyh+"'";
		String bdcs_shyqzd_sql="select bdcdyid,zdt,zdtwj ,qxdm,bdcdyh from bdck.bdcs_shyqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid,zdt,zdtwj,qxdm,bdcdyh   from bdck.bdcs_shyqzd_xz where bdcdyh='"+bdcdyh+"'";
		//宗地的
		if(bdcdyh.toUpperCase().contains("W"))
		{
			if(bdcdylx.equals("01"))
		 	sql=bdcs_syqzd_sql;
		  if(bdcdylx.equals("02"))
		 	sql=bdcs_shyqzd_sql;
			//sql="select bdcdyid,zdt,zdtwj from bdck.bdcs_shyqzd_xz where bdcdyh='"+bdcdyh+"' union all select bdcdyid,zdt,zdtwj from bdck.bdcs_shyqzd_gz where bdcdyh='"+bdcdyh+"'";
		}
		//房屋的  
		if(bdcdyh.toUpperCase().contains("F"))
		   sql="select bdcdyid,zdt,zdtwj,qxdm,bdcdyh from bdck.bdcs_shyqzd_xz  where bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"')";
		 
		//获取宗地图文件夹路径
		//String zdtPath=GetProperties.getConstValueByKey("ZDT");
		String zdtPath=ConfigHelper.getNameByValue("ZDTPATH");
		String imgfrom=ConfigHelper.getNameByValue("ZDTFROM");
		ResultSet sqlDataRSet=dao.excuteQuery(sql);
		System.out.println("获取宗地图SQL语句："+sql);
		if (sqlDataRSet.next())
		{
			//获取分层分户图文件流
			blob = sqlDataRSet.getBlob("zdtwj");
			//获取分户图文件名
			zdt=sqlDataRSet.getString("zdt");
			//获取不动产单元Id
			bdcdyid=sqlDataRSet.getString("bdcdyid");
			//宗地不动产单元
			zdbdcdyh=sqlDataRSet.getString("bdcdyh");
		  if(blob!=null)
			inStream = blob.getBinaryStream();
		}
//		PreparedStatement PS = sqlDataRSet.unwrap(PreparedStatement.class);
//		sqlDataRSet.close();
//		if(PS!=null) {
//			PS.close();
//		}
		File uploadfolder=new File(zdtPath);
		   if(imgfrom.equals("1"))//从二进制方式读取
		   {
			   System.out.println("从二进制读取图片(bdck.bdcs_shyqzd_xz.zdtwj)");
				if(inStream!=null)
				{
					bim= ImageIO.read(inStream);
				}//第二优先读数据库记录文件路径
			
		   }
	        
	        if(imgfrom.equals("2"))//以上传文件名找附件
	        {
	        	System.out.println("根据文件名读附件(bdck.bdcs_shyqzd_xz.zdt)");
	        	if(uploadfolder.exists()){
	        		File zdt_database=new File(uploadfolder, zdbdcdyh+".zip");
				if(zdt_database.exists())
				{
					String zipPath=zdt_database.getAbsoluteFile().toString();
					String destPath=trimExtension(zipPath);
					//解压文件(不支持中文)
					//ZipUtil ziputil=new ZipUtil();
					//ziputil.unzip(zipPath,destPath);
					//解压文件（支持中文）
					 CompresszZipFile compress = new CompresszZipFile();
					 System.out.println("解压文件（支持中文）");
					 compress.ReadZip(zipPath, zdtPath);  
					File destFile =new File(destPath);
					//读取解压文件中的图片流
					if(destFile.exists())
					{
						String[] subNote = destFile.list();
						if(subNote.length>0)
						{
							bmp=new File(destFile,subNote[0]);
							filename = subNote[0];
//							bim=ImageIO.read(bmp);
						}
					}
				}
				else
					System.out.println("未配置宗地图上传路径："+zdtPath);
	        	}
	        }
	        if(imgfrom.equals("3"))//以不动产单元Id找附件
	        {
	        	if(uploadfolder.exists()){	
	        		System.out.println("根据不动产单元Id读取上传文件(bdck.bdcs_shyqzd_xz.bdcdyid)");
					File zdt_file=new File(uploadfolder, bdcdyid+".zip");
					User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
//	        		bdcdyid=sqlDataRSet.getString("bdcdyid");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								 zdt_file=new File(uploadfolder, bdcdyid+".zip");	
								 break;
							}
						}
					}
					if(zdt_file.exists())
					{
						String zipPath=zdt_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//解压文件(不支持中文)
					    //ZipUtil ziputil=new ZipUtil();
						//ziputil.unzip(zipPath,destPath);
						//解压文件（支持中文）
						 CompresszZipFile compress = new CompresszZipFile();
						 System.out.println("解压文件（支持中文）");
						 compress.ReadZip(zipPath, zdtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								//只读取解压文件夹中第一张文件
								bmp=new File(destFile,subNote[0]);
								filename=subNote[0];
//								bim=ImageIO.read(bmp);
							}
						}
					}
	        	}
				else
					System.out.println("未配置宗地图上传路径："+zdtPath);
	        }
	        f=FileUtils.readFileToByteArray(bmp);
	        if (filename != null && filename.toLowerCase().indexOf(".pdf") > 0) {
				response.setHeader("Content-Disposition", "inline; filename=" + filename);
				response.setContentType("application/pdf; charset=UTF-8");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setContentType("image/jpeg; charset=UTF-8");
			}
	        if(f==null) {
	        	File errorbmp=new File(errorPath);
	        	f=FileUtils.readFileToByteArray(errorbmp);
	        } 
	        response.getOutputStream().write(f);
	        
	        
	}
	/**
	 * @param 获取房产分户图名字
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/fhtname", method = RequestMethod.POST)
	public  @ResponseBody  String  getFCFHT(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String bdcdyh=request.getParameter("bdcdyh");
		String bdcdylx=request.getParameter("bdcdylx");
		//bdcdyh="450330100002GB01875F00010001";
		String fcfht="",bdcdyid="",sql=""; 
		InputStream inStream=null;
		Blob blob=null;
		bdcdyh=bdcdyh.replaceAll(" ", "");
		String bdcs_h_sql="select bdcdyid,fcfht,fcfhtwj ,qxdm from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid,fcfht,fcfhtwj ,qxdm from bdck.bdcs_h_xz  where bdcdyh='"+bdcdyh+"'";
		String bdcs_h_y_sql="select bdcdyid,fcfht,fcfhtwj,qxdm from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"' union all select bdcdyid,fcfht,fcfhtwj ,qxdm from bdck.bdcs_h_xzy  where bdcdyh='"+bdcdyh+"'";
		 if(bdcdylx.equals("031"))
		 	sql=bdcs_h_sql;
		  if(bdcdylx.equals("032"))
		 	sql=bdcs_h_y_sql;
		ResultSet sqlDataRSet=dao.excuteQuery(sql);
		System.out.println("获取房屋分层分户图SQL语句："+sql);
		if (sqlDataRSet.next())
		{
			//获取分层分户图文件流
			blob = sqlDataRSet.getBlob("fcfhtwj");
			//获取分户图文件名
			fcfht=sqlDataRSet.getString("fcfht");
			//获取分户图文件名
			bdcdyid=sqlDataRSet.getString("bdcdyid");
			if(blob!=null)
			   inStream = blob.getBinaryStream();
		}
		String fhtPath=ConfigHelper.getNameByValue("FHTPATH");
		String imgfrom=ConfigHelper.getNameByValue("FHTFROM");
		File uploadfolder=new File(fhtPath);
	        if(imgfrom.equals("3"))//以不动产单元Id找附件
	        {
	        	if(uploadfolder.exists()){	
	        		System.out.println("根据不动产单元Id读取上传文件(bdck.bdcs_h_xz.bdcdyid)");
	        		File fcfht_file=new File(uploadfolder, bdcdyid+".zip");
	        		User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
	        		bdcdyid=sqlDataRSet.getString("bdcdyid");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								fcfht_file=new File(uploadfolder, bdcdyh+".zip");	
								 break;
							}
						}
					}
					if(fcfht_file.exists())
					{
						String zipPath=fcfht_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//解压文件（不支持中文）
						//ZipUtil ziputil=new ZipUtil();
						//ziputil.unzip(zipPath,destPath);
						 CompresszZipFile compress = new CompresszZipFile();
							//解压文件（支持中文）
							 System.out.println("解压文件（支持中文）");
							 compress.ReadZip(zipPath, fhtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								return subNote[0];
							}
							
						}
					}
	        	}
	        	else
	        		System.out.println("未配置宗地图上传路径："+fhtPath);
	        }else if(imgfrom.equals("2")){//以上传文件名找附件
	        	if(uploadfolder.exists()){	
	        		System.out.println("根据不动产单元Id读取上传文件(bdck.bdcs_h_xz.bdcdyid)");
	        		File fcfht_file=new File(uploadfolder, bdcdyh+".zip");
	        		User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
//	        		bdcdyid=sqlDataRSet.getString("bdcdyid");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								fcfht_file=new File(uploadfolder, bdcdyh+".zip");	
								 break;
							}
						}
					}
					if(fcfht_file.exists())
					{
						String zipPath=fcfht_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//解压文件（不支持中文）
						//ZipUtil ziputil=new ZipUtil();
						//ziputil.unzip(zipPath,destPath);
						 CompresszZipFile compress = new CompresszZipFile();
							//解压文件（支持中文）
							 System.out.println("解压文件（支持中文）");
							 compress.ReadZip(zipPath, fhtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								return subNote[0];
							}
							
						}
					}
	        	}
	        }
		
	
		
		return null;	
		
	}
	/**
	 * @param 获取房产分户图文件
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/{bdcdyh}/h/{bdcdylx}", method = RequestMethod.GET)
	public void getFCFHTWJ(@PathVariable("bdcdyh") String bdcdyh,@PathVariable("bdcdylx") String bdcdylx,HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		File bmp = null;
		String errorPath = String.format("%s\\resources\\images\\404.bmp",request.getRealPath("/"));
		//bdcdyh="450330100002GB01875F00010001";
		String fcfht="",bdcdyid="",sql=""; 
		byte [] f=null;
		String filename="分户图";
		InputStream inStream=null;
		Blob blob=null;
		BufferedImage bim =null;
		bdcdyh=bdcdyh.replaceAll(" ", "");
		String bdcs_h_sql="select bdcdyid,fcfht,fcfhtwj ,qxdm from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid,fcfht,fcfhtwj ,qxdm from bdck.bdcs_h_xz  where bdcdyh='"+bdcdyh+"'";
		String bdcs_h_y_sql="select bdcdyid,fcfht,fcfhtwj,qxdm from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"' union all select bdcdyid,fcfht,fcfhtwj ,qxdm from bdck.bdcs_h_xzy  where bdcdyh='"+bdcdyh+"'";
		 if(bdcdylx.equals("031"))
		 	sql=bdcs_h_sql;
		  if(bdcdylx.equals("032"))
		 	sql=bdcs_h_y_sql;
		ResultSet sqlDataRSet=dao.excuteQuery(sql);
		System.out.println("获取房屋分层分户图SQL语句："+sql);
		if (sqlDataRSet.next())
		{
			//获取分层分户图文件流
			blob = sqlDataRSet.getBlob("fcfhtwj");
			//获取分户图文件名
			fcfht=sqlDataRSet.getString("fcfht");
			//获取分户图文件名
			bdcdyid=sqlDataRSet.getString("bdcdyid");
			if(blob!=null)
			   inStream = blob.getBinaryStream();
		}
//		PreparedStatement PS = sqlDataRSet.unwrap(PreparedStatement.class);
//		sqlDataRSet.close();
//		if(PS!=null) {
//			PS.close();
//		}
		//String fhtPath=GetProperties.getConstValueByKey("FHT");
		String fhtPath=ConfigHelper.getNameByValue("FHTPATH");
		String imgfrom=ConfigHelper.getNameByValue("FHTFROM");
		File uploadfolder=new File(fhtPath);
		 if(imgfrom.equals("1"))//从二进制方式读取
		   {
			   System.out.println("从二进制读取图片(bdck.bdcs_h_xz.fcfhtwj)");
				if(inStream!=null)
				{
					bim= ImageIO.read(inStream);
				}//第二优先读数据库记录文件路径
		   }
	     
	        if(imgfrom.equals("2"))//以上传文件名找附件
	        {
	        	if(uploadfolder.exists())
	    		{
	        	System.out.println("根据文件名读附件(bdck.bdcs_h_xz.fcfht)");
	        	File fcfht_database=new File(uploadfolder, bdcdyh+".zip");
				if(fcfht_database.exists())
				{
					String zipPath=fcfht_database.getAbsoluteFile().toString();
					String destPath=trimExtension(zipPath);
					//解压文件（不支持中文）
					//ZipUtil ziputil=new ZipUtil();
					//ziputil.unzip(zipPath,destPath);
					 CompresszZipFile compress = new CompresszZipFile();
					//解压文件（支持中文）
					 System.out.println("解压文件（支持中文）");
					 compress.ReadZip(zipPath, fhtPath);  
					File destFile =new File(destPath);
					//读取解压文件中的图片流
					if(destFile.exists())
					{
						String[] subNote = destFile.list();
						if(subNote.length>0)
						{
							bmp=new File(destFile,subNote[0]);
							filename=subNote[0];
//							bim=ImageIO.read(bmp);
						}
					}
				}
	    		}
	        	else
	        		System.out.println("分层分户图上传路径："+fhtPath);
	        }
	        if(imgfrom.equals("3"))//以不动产单元Id找附件
	        {
	        	if(uploadfolder.exists()){	
	        		System.out.println("根据不动产单元Id读取上传文件(bdck.bdcs_h_xz.bdcdyid)");
	        		File fcfht_file=new File(uploadfolder, bdcdyid+".zip");
	        		User user=Global.getCurrentUserInfo();
	        		String  qhdmUser=user.getAreaCode();
	        		String bdcdhydq=ConfigHelper.getNameByValue("BDCDHYDQ");
	        		bdcdyid=sqlDataRSet.getString("bdcdyid");
					if(bdcdhydq!=null&&!bdcdhydq.equals("")) {
						for (String dq:bdcdhydq.split(",")) {
							if(dq.equals(qhdmUser)) {
								fcfht_file=new File(uploadfolder, bdcdyh+".zip");	
								 break;
							}
						}
					}
					if(fcfht_file.exists())
					{
						String zipPath=fcfht_file.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						//解压文件（不支持中文）
						//ZipUtil ziputil=new ZipUtil();
						//ziputil.unzip(zipPath,destPath);
						 CompresszZipFile compress = new CompresszZipFile();
							//解压文件（支持中文）
							 System.out.println("解压文件（支持中文）");
							 compress.ReadZip(zipPath, fhtPath);  
						File destFile =new File(destPath);
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								bmp=new File(destFile,subNote[0]);
								filename=subNote[0];
								f=FileUtils.readFileToByteArray(bmp);
//								bim=ImageIO.read(bmp);
							}
						}
					}
	        	}
	        	else
	        		System.out.println("未配置宗地图上传路径："+fhtPath);
	        }
	        f=FileUtils.readFileToByteArray(bmp);
	        if (filename != null && filename.toLowerCase().indexOf(".pdf") > 0) {
				response.setHeader("Content-Disposition", "inline; filename=" + filename);
				response.setContentType("application/pdf; charset=UTF-8");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setContentType("image/jpeg; charset=UTF-8");
			}
	        if(f==null) {
	        	File errorbmp=new File(errorPath);
	        	f=FileUtils.readFileToByteArray(errorbmp);
	        } 
	        response.getOutputStream().write(f);
	}
	
	/**
	 * @param 获取房产分户图文件,用于二维码
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/pdf/{type}/", method = RequestMethod.GET)
	public void getFCFHTWJ_QR(@PathVariable("type") String type,HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String errorPath = String.format("%s\\resources\\images\\noimage.pdf",request.getRealPath("/"));
        String ftp_new_url= request.getSession().getServletContext().getRealPath("/")+"resources\\PDF_Tem";
		byte [] f=null;
		String filename=UUID.randomUUID().toString();
		try {
			String zsid=request.getParameter("bdcqzh");
			if(!StringHelper.isEmpty(zsid)) {
				zsid=EncodeTools.decoderByDES(zsid);
			}
		    String bdcdyh_fht="";   //分户图不动产单元号
		    String bdcdyh_zdt="";       //自然幢宗地图不动产单引号
			//根据不动产权证号查询不动产单元号
				List<BDCS_QDZR_GZ> bdcs_qdzr_gz=this.dao.getDataList(BDCS_QDZR_GZ.class,"zsid='"+zsid+"'");
				if (bdcs_qdzr_gz.size()>0&&!StringHelper.isEmpty(bdcs_qdzr_gz.get(0).getBDCDYH())) {
					bdcdyh_fht=bdcs_qdzr_gz.get(0).getBDCDYH();
				}else {
					List<BDCS_QDZR_XZ> xz=this.dao.getDataList(BDCS_QDZR_XZ.class,"zsid='"+zsid+"'");
					if (xz.size()>0&&!StringHelper.isEmpty(xz.get(0).getBDCDYH())) {
						bdcdyh_fht=xz.get(0).getBDCDYH();
					}
				} 
			String bdcs_zrz_gz_sql="select bdcdyid,zdtwj,qxdm,bdcdyh from bdck.bdcs_zrz_gz  where bdcdyid in (select max(zrzbdcdyid) from bdck.bdcs_h_gz gz where gz.bdcdyh = '"+bdcdyh_fht+"')";
			String sql="";
				List<Map> list = dao.getDataListByFullSql(bdcs_zrz_gz_sql);
				if(list.size()>0) {
					sql=bdcs_zrz_gz_sql;//现房
				}else {
					sql = "select bdcdyh from bdck.bdcs_zrz_xz  where bdcdyid in (select max(zrzbdcdyid) from bdck.bdcs_h_xz gz where gz.bdcdyh = '"+bdcdyh_fht+"')";
				}
				ResultSet sqlDataRSet=dao.excuteQuery(sql);
				if (sqlDataRSet.next()){
					//获取不动产单元Id
					bdcdyh_zdt = sqlDataRSet.getString("bdcdyh");
				}
			String fhtPath=ConfigHelper.getNameByValue("FHTPATH");//分层分户图路径
			String zdtPath=ConfigHelper.getNameByValue("ZRZZDTPATH");//宗地
			
			File file=new File(fhtPath+"\\"+bdcdyh_fht+".zip");
			boolean  fht_existis=file.exists();
			file=new File(zdtPath+"\\"+bdcdyh_zdt+".zip");
			boolean  zdt_existis=file.exists();
			File bmp = null;
			if("0".equals(type)) {
				filename=UUID.randomUUID().toString().replace("-", "");
				/*
				 * 宗地和分户图整合下载
				 */
				file=new File(ftp_new_url);
				if(!file.exists()) {
					file.mkdir();
				} 
				PDFMergerUtility   mergePdf = new PDFMergerUtility(); 
				//解压
				 CompresszZipFile compress = new CompresszZipFile();//解压文件（支持中文）
				 if(fht_existis) {
					 compress.ReadZip(fhtPath+"\\\\"+bdcdyh_fht+".zip", fhtPath); 
					 file=new File(fhtPath+"\\\\"+bdcdyh_fht);
					 mergePdf.addSource((file.listFiles())[0]);
				 }
				 if(zdt_existis) {
					 compress.ReadZip(zdtPath+"\\\\"+bdcdyh_zdt+".zip", zdtPath); 
					 file=new File(zdtPath+"\\\\"+bdcdyh_zdt);
					 mergePdf.addSource(file.listFiles()[0]);
				 }
				//合并
				 if (fht_existis&&zdt_existis) {
					 mergePdf.setDestinationFileName(ftp_new_url+"\\"+filename+".pdf");
					 mergePdf.mergeDocuments(); 
				 }
		    	//下载
				 if (fht_existis&&zdt_existis) {
					 try {
							ImageMD5Tool.preventTamper(new File(ftp_new_url+"\\"+filename+".pdf"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					 f=FileUtils.readFileToByteArray(new File(ftp_new_url+"\\"+filename+".pdf"));
				 }else if(fht_existis) {
					 type="2";
				 }else if(zdt_existis) {
					 type="1";
				 }else {
					 f=FileUtils.readFileToByteArray(new File(errorPath));
				 }
			}
			
			
			    File fcfht_database=new File(file, bdcdyh_fht+".zip");
				 if("1".equals(type)){
						//宗地图
					    file=new File(zdtPath);
					    fcfht_database=new File(file, bdcdyh_zdt+".zip");
						filename="宗地图";
					}else if("2".equals(type)){
					     file=new File(fhtPath);
					    fcfht_database=new File(file, bdcdyh_fht+".zip");
						filename="分户图";
					}
					
					if(fcfht_database.exists()) {
//						解压
						String zipPath=fcfht_database.getAbsoluteFile().toString();
						String destPath=trimExtension(zipPath);
						 CompresszZipFile compress = new CompresszZipFile();//解压文件（支持中文）
						 if("1".equals(type)){
								//宗地图
							 compress.ReadZip(zipPath, zdtPath); 
							}
						 if("2".equals(type)){
							 compress.ReadZip(zipPath, fhtPath); 
							}
						  
						File destFile =new File(destPath);
						
						//读取解压文件中的图片流
						if(destFile.exists())
						{
							String[] subNote = destFile.list();
							if(subNote.length>0)
							{
								bmp=new File(destFile,subNote[0]);
								 try {
										ImageMD5Tool.preventTamper(bmp);
									} catch (Exception e) {
										e.printStackTrace();
									}
								 destFile=new File(destPath);
								 subNote = destFile.list();
								 bmp=new File(destFile,subNote[0]);
								 if(f==null) {
									 f=FileUtils.readFileToByteArray(bmp);
								 }
							}
						}
					
					}
		} catch (Exception e) {
			e.printStackTrace();
			f=null;
		}
		response.setHeader("Content-Disposition", "inline; filename=" + java.net.URLEncoder.encode(filename+".pdf", "UTF-8"));
		//response.setContentType("application/pdf; charset=UTF-8");
		response.setContentType("application/octet-stream");
		response.setHeader("Access-Control-Allow-Origin", "*");
        if(f==null) {
        	File errorbmp=new File(errorPath);
        	f=FileUtils.readFileToByteArray(errorbmp);
        }else {
        	File file=new File(ftp_new_url+"\\"+filename+".pdf");
        	if(file.exists()) {
        		file.delete();
        	}
        }
        response.getOutputStream().write(f);
		
	}
	/**
	 * 保存打印模板
	 * @Title: disablegeooperate
	 * @author:WUZ
	 * @date：2015年10月21日 上午11:15:48
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{folder}/{tpl}/saveprinttpl", method = RequestMethod.POST)
	public @ResponseBody String saveprinttpl(@PathVariable("folder") String folder,@PathVariable("tpl") String tpl, HttpServletRequest request, HttpServletResponse response) {
		FileOutputStream fop = null;
		File file = null;
		String r = "true";
		String filepath = "";
		String dataString = "";
		StringBuilder tplHtml = new StringBuilder();
		String tplHtmlString = "";
		if (StringHelper.isEmpty(tpl))
			return "false";
		try {
			dataString = request.getParameter("info");
			JSONObject htmlobject = JSON.parseObject(dataString);

			if (StringHelper.isEmpty(dataString))
				return "false";
			if(folder.equals("default"))
				filepath = String.format("%s\\resources\\template\\%s.html", request.getRealPath("/"), tpl);
			else 
				filepath = String.format("%s\\resources\\template\\zsprint\\%s\\%s.html", request.getRealPath("/"),folder, tpl);
			if (tpl.equals("zszm")) {
				tplHtml.append("{{# function _getValue(v){ if(v==undefined){return '';} return v;} }}");
				tplHtml.append("<div id=\"mytplrang\" style='#mytplrang");
				tplHtml.append("{{# if(d.is_td==false) {}} background: url(../../resources/images/zszm.png) 0px 0px / cover no-repeat; {{# } }} '>");
				tplHtml.append("<div id=\"id_zsbh\" class=\"drag noprint\" style='#id_zsbh'>{{_getValue(d?d.id_zsbh:'') }}</div>");
				tplHtml.append("<div id=\"qhjc\" class=\"drag noprint\" style='#qhjc'>{{_getValue(d?d.qhjc:'') }}</div>");
				tplHtml.append("<div id=\"nd\" class=\"drag noprint\" style='#nd'>{{_getValue(d?d.nd:'') }}</div>");
				tplHtml.append("<div id=\"qhmc\" class=\"drag noprint\" style='#qhmc'>{{_getValue(d?d.qhmc:'') }}</div>");
				tplHtml.append("<div id=\"cqzh\" class=\"drag noprint\" style='#cqzh'>{{_getValue(d?d.cqzh:'') }}</div>");
				tplHtml.append("<div id=\"zmqlhsx\" class=\"drag noprint\" style='#zmqlhsx'>{{_getValue(d?d.zmqlhsx:'') }}</div>");
				tplHtml.append("<div id=\"qlr\" class=\"drag noprint\" style='#qlr'>{{_getValue(d?d.qlr:'') }}</div>");
				tplHtml.append("<div id=\"ywr\" class=\"drag noprint\" style='#ywr'>{{_getValue(d?d.ywr:'') }}</div>");
				tplHtml.append("<div id=\"zl\" class=\"drag noprint\" style='#zl'>{{_getValue(d?d.zl:'') }}</div>");
				tplHtml.append("<div id=\"bdcdyh\" class=\"drag noprint\" style='#bdcdyh'>{{_getValue(d?d.bdcdyh:'') }}</div>");
				tplHtml.append("<div id=\"qt\" class=\"drag noprint\" style='#qt'>{{_getValue(d?d.qt:'') }}</div>");
				tplHtml.append("<div id=\"fj\" class=\"drag noprint\" style='#fj'>{{_getValue(d?d.fj:'') }}</div>");
				tplHtml.append("<div id=\"qrcode\" class=\"drag2 noprint\" style='#qrcode'><img  style=\"width:140px;height:140px\" alt=\"{{_getValue(d?d.qrcodename:'') }}\" src=\"{{_getValue(d?d.qrcode:'') }}\"/></div>");
				tplHtml.append("<div id=\"fm_year\" class=\"drag noprint\" style='#fm_year'>{{_getValue(d?d.fm_year:'') }}</div>");
				tplHtml.append("<div id=\"fm_month\" class=\"drag noprint\" style='#fm_month'>{{_getValue(d?d.fm_month:'') }}</div>");
				tplHtml.append("<div id=\"fm_day\" class=\"drag noprint\" style='#fm_day'>{{_getValue(d?d.fm_day:'') }}</div>");
				tplHtml.append("</div>");
			}
			if (tpl.equals("zsxx_nr")) {
				tplHtml.append("{{# function _getValue(v){ if(v==undefined){return '';} return v;} }}");
				tplHtml.append("<div id=\"mytplrang\" style='#mytplrang");
				tplHtml.append("{{# if(d.is_td==false) {}}  background: url(../../resources/images/zsxx_nr.png) 0px 0px / cover no-repeat; {{# } }} '>");
				tplHtml.append("<div id=\"syqx\" class=\"drag noprint\" style='#syqx'>{{_getValue(d?d.syqx:'') }}</div>");
				tplHtml.append("<div id=\"qhjc\" class=\"drag noprint\" style='#qhjc'>{{_getValue(d?d.qhjc:'') }}</div>");
				tplHtml.append("<div id=\"nd\" class=\"drag noprint\" style='#nd'>{{_getValue(d?d.nd:'') }}</div>");
				tplHtml.append("<div id=\"qhmc\" class=\"drag noprint\" style='#qhmc'>{{_getValue(d?d.qhmc:'') }}</div>");
				tplHtml.append("<div id=\"cqzh\" class=\"drag noprint\" style='#cqzh'>{{_getValue(d?d.cqzh:'') }}</div>");
				tplHtml.append("<div id=\"mj\" class=\"drag noprint\" style='#mj'>{{_getValue(d?d.mj:'') }}</div>");
				tplHtml.append("<div id=\"qlr\" class=\"drag noprint\" style='#qlr'>{{_getValue(d?d.qlr:'') }}</div>");
				tplHtml.append("<div id=\"yt\" class=\"drag noprint\" style='#yt'>{{_getValue(d?d.yt:'') }}</div>");
				tplHtml.append("<div id=\"zl\" class=\"drag noprint\" style='#zl'>{{_getValue(d?d.zl:'') }}</div>");
				tplHtml.append("<div id=\"bdcdyh\" class=\"drag noprint\" style='#bdcdyh'>{{_getValue(d?d.bdcdyh:'') }}</div>");
				tplHtml.append("<div id=\"gyqk\" class=\"drag noprint\" style='#gyqk'>{{_getValue(d?d.gyqk:'') }}</div>");
				tplHtml.append("<div id=\"fj\" class=\"drag noprint\" style='#fj'>{{_getValue(d?d.fj:'') }}</div>");
				tplHtml.append("<div id=\"qllx\" class=\"drag noprint\" style='#qllx'>{{_getValue(d?d.qllx:'') }}</div>");
				tplHtml.append("<div id=\"qlxz\" class=\"drag noprint\" style='#qlxz'>{{_getValue(d?d.qlxz:'') }}</div>");
				tplHtml.append("<div id=\"qlqtzk\" class=\"drag noprint\" style='#qlqtzk'>{{_getValue(d?d.qlqtzk:'') }}</div>");
				tplHtml.append("<div id=\"qrcode\" class=\"drag2 noprint\" style='#qrcode'><img  style=\"width:120px;height:120px\" alt=\"{{_getValue(d?d.fj_qr:'') }}\" src=\"{{_getValue(d?d.fj_qr:'') }}\"/></div>");
				tplHtml.append("</div>");
			}
			if (tpl.equals("zsxx_fm")) {
				tplHtml.append("{{# function _getValue(v){ if(v==undefined){return '';} return v;} }}");
				tplHtml.append("<div id=\"mytplrang\" style='#mytplrang");
				tplHtml.append("{{# if(d.is_td==false) {}}   background: url(../../resources/images/zsxx_fm.png) 0px 0px / cover no-repeat; {{# } }} '>");
				tplHtml.append("<div id=\"id_zsbh\" class=\"drag noprint\" style='#id_zsbh'>{{_getValue(d?d.id_zsbh:'') }}</div>");
				tplHtml.append("<div id=\"fm_year\" class=\"drag noprint\" style='#fm_year'>{{_getValue(d?d.fm_year:'') }}</div>");
				tplHtml.append("<div id=\"fm_month\" class=\"drag noprint\" style='#fm_month'>{{_getValue(d?d.fm_month:'') }}</div>");
				tplHtml.append("<div id=\"fm_day\" class=\"drag noprint\" style='#fm_day'>{{_getValue(d?d.fm_day:'') }}</div>");
				tplHtml.append("<div id=\"qrcode\" class=\"drag2 noprint\" style='#qrcode'><img  style=\"width:120px;height:120px\" alt=\"{{_getValue(d?d.qrcodename:'') }}\" src=\"{{_getValue(d?d.qrcode:'') }}\"/></div>");
				tplHtml.append("</div>");
			}
			tplHtmlString = tplHtml.toString();
			for (Entry<String, Object> entry : htmlobject.entrySet()) {
				String _value = String.valueOf(entry.getValue()).replace("'", "");// 除去样式中的‘号
				tplHtmlString = tplHtmlString.replace("#" + entry.getKey(), _value);
			}
			file = new File(filepath);
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = tplHtmlString.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			r = "false";
			e.printStackTrace();
		} finally {
			try {
				if (fop != null)
					fop.close();
			} catch (IOException e) {
				r = "false";
				e.printStackTrace();
			}
		}
		return r;
	}

/**
	 * 上传图片 add by wjz
	 * 
	 * @param file
	 * @param request
	 * @param response
 * @throws Exception 
	 */
	@RequestMapping(value = "/updateimage", method = RequestMethod.POST)
	public void upload(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			Object command) throws Exception {
		//-------拷贝二个文件流对象用于保存工作层和现状层----------
		InputStream picstream=file.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = picstream.read(buffer)) > -1 ) {
		    baos.write(buffer, 0, len);
		}
		baos.flush();
		InputStream is1 = new ByteArrayInputStream(baos.toByteArray()); 
		InputStream is2 = new ByteArrayInputStream(baos.toByteArray()); 
		//-------拷贝二个文件流对象用于保存工作层和现状层----------
		//不动产单元类型
		String bdcdylx = request.getParameter("bdcdylx");
		//不动产单元号
		String bdcdyh=request.getParameter("bdcdyh");
		bdcdyh=bdcdyh.replaceAll(" ", "");
		//分层分户图上传地址
		String fhtPath=ConfigHelper.getNameByValue("FHTPATH");
		//宗地图上传地址
		String zdtPath=ConfigHelper.getNameByValue("ZDTPATH");
		//分层分户图备份地址
		String djxtfhtpath = ConfigHelper.getNameByValue("DJXTFHTPATH");
		//分层分户图图片读取/存储方式；
		String fhtimgfrom=ConfigHelper.getNameByValue("FHTFROM");
		//宗地图图片读取/存储方式；
		String zdtimgfrom=ConfigHelper.getNameByValue("ZDTFROM");
		String basepath="",imgfrom="",sql="",bdcdyid="";
		ZipUtil zip=new ZipUtil();
		//通过不动产单元号找到不动产单元Id语句
		String bdcs_shyqzd_sql="select bdcdyid from bdck.bdcs_shyqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_shyqzd_xz where bdcdyh='"+bdcdyh+"'";
		String bdcs_syqzd_sql="select bdcdyid from bdck.bdcs_syqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_syqzd_xz where bdcdyh='"+bdcdyh+"'";
		String h_y_sql="select bdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"'";
		String h_sql="select bdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"'";
		String shyqzd_by_h_sql="select bdcdyid from bdck.bdcs_shyqzd_xz where bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"')";
		
		//宗地的
		if(bdcdyh.toUpperCase().contains("W"))
		{
		 if(bdcdylx.equals("01"))
		 	sql=bdcs_syqzd_sql;
		  if(bdcdylx.equals("02"))
		 	sql=bdcs_shyqzd_sql;
		}
		//房屋的
		if(bdcdyh.toUpperCase().contains("F"))
		{
			  //房屋上的宗地
			 if(bdcdylx.equals("02"))
			 	sql=shyqzd_by_h_sql;
			   //房屋
			 if(bdcdylx.equals("031"))
			 	sql=h_sql;
			   //预测户
			 if(bdcdylx.equals("032"))
			 	sql=h_y_sql;

		}
		//批量上传分户图
		if("4".equals(fhtimgfrom)){
			String id="",hsql="";
			bdcdyh = bdcdyh.replaceAll(";", ",");
			String[] arry_bdcdyh = bdcdyh.split(",");
			bdcdyh = "'" + StringHelper.formatList(StringHelper.Arrary2List(arry_bdcdyh), "','") + "'";
			hsql=" select bdcdyid from bdck.bdcs_h_xz where bdcdyh in("+bdcdyh+")";
			List<Map> map = dao.getDataListByFullSql(hsql);
			System.out.println("获取房屋分层分户图SQL语句："+hsql);
			if (map!=null&&map.size()>0)
			{	
				for(Map m:map){
					id=m.get("BDCDYID")==null?"":m.get("BDCDYID").toString();
					String jsonStr = geo.uploadFCFHT(id, bdcdylx, "02", file);
					if(bdcdylx.contains("031")||bdcdylx.contains("032"))
					{
						basepath = djxtfhtpath;
						imgfrom = fhtimgfrom;
					}
	
					try {
						File dirFile = new File(basepath);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}
					if (jsonStr != null && jsonStr != "") {
						//压缩前源文件
						String srcPathName = basepath + "\\" +file.getOriginalFilename();
						//压缩后文件
						String zipFile = basepath + "\\" +id+".zip";
						File tempFile = new File(srcPathName);
						//fileName = tempFile.getName();                                                             
						//文件存在，先删除
						if (tempFile.exists()) {
						tempFile.delete();
						}
						tempFile.createNewFile();
						System.out.println(file.getOriginalFilename());
						file.transferTo(tempFile);
			            //压缩文件
						zip.compressExe(srcPathName, zipFile);
					}
					   //上传成功后更新数据库------------------
						//户
						is1.close();
						is2.close();
						
					} catch (Exception ex) {
					   System.out.print(ex.getMessage());
					   picstream.close();
						is1.close();
						is2.close();
					}
				}
				
			}
		picstream.close();
		return;	
		}
		ResultSet sqlDataRSet=dao.excuteQuery(sql);
		System.out.println("获取房屋分层分户图SQL语句："+sql);
		if (sqlDataRSet.next())
		{
			bdcdyid=sqlDataRSet.getString("bdcdyid");
		}
//		PreparedStatement PS = sqlDataRSet.unwrap(PreparedStatement.class);
//		sqlDataRSet.close();
//		if(PS!=null) {
//			PS.close();
//		}
		//判断上传的是房屋还是宗地路径
		if(bdcdylx.contains("031")||bdcdylx.contains("032"))
		{
			basepath=fhtPath;
			imgfrom=fhtimgfrom;
		}
		else
		{
			basepath=zdtPath;
			imgfrom=zdtimgfrom;	
		}

		try {
			File dirFile = new File(basepath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			//压缩前源文件
			String srcPathName = basepath + "\\" +file.getOriginalFilename();
			//压缩后文件
			String zipFile = basepath + "\\" +bdcdyid+".zip";
			File tempFile = new File(srcPathName);
			// fileName = tempFile.getName();
			// 文件存在，先删除
			if (tempFile.exists()) {
				tempFile.delete();
			}
			tempFile.createNewFile();
			file.transferTo(tempFile);
            //压缩文件
			zip.compressExe(srcPathName, zipFile);
		   //上传成功后更新数据库------------------

			//所有权宗地
			if(bdcdylx.equals("01"))
			{
				  if(imgfrom.equals("1"))//二进制方式存入数据库
				  {
				String strSQL="BEGIN UPDATE BDCK.BDCS_SYQZD_XZ SET ZDTWJ=? WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_SYQZD_GZ SET ZDTWJ=? WHERE BDCDYH='"+bdcdyh+"'; END;";
				dao.updateBinarys(strSQL, is1,is2);
				  }
				  if(imgfrom.equals("2"))//文件名存入数据库
				  {
					  String strSQL="BEGIN UPDATE BDCK.BDCS_SYQZD_XZ SET ZDT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_SYQZD_GZ SET ZDT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"'; END;";
						dao.excuteQueryNoResult(strSQL);
				  }
			}
			//使用权宗地
			if(bdcdylx.equals("02"))
			{
				  if(imgfrom.equals("1"))//二进制方式存入数据库
				  {
					  String strSQL="";
					    //房屋上的宗地，传进来的是房屋不动产单元号
						if(bdcdyh.toUpperCase().contains("F"))
				          strSQL="BEGIN UPDATE BDCK.BDCS_SHYQZD_XZ SET ZDTWJ=? WHERE  bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"');UPDATE BDCK.BDCS_SHYQZD_GZ SET ZDTWJ=? WHERE  bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"'); END;";
						else//纯土地的
						 strSQL="BEGIN UPDATE BDCK.BDCS_SHYQZD_XZ SET ZDTWJ=? WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_SHYQZD_GZ SET ZDTWJ=? WHERE BDCDYH='"+bdcdyh+"'; END;";
						dao.updateBinarys(strSQL, is1,is2);
				  }
				  if(imgfrom.equals("2"))//文件名存入数据库
				  {
					  String strSQL="";
					//房屋上的宗地，传进来的是房屋不动产单元号
					  if(bdcdyh.toUpperCase().contains("F"))
					   strSQL="BEGIN UPDATE BDCK.BDCS_SHYQZD_XZ SET ZDT='"+bdcdyid+".zip' WHERE  bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"');UPDATE BDCK.BDCS_SHYQZD_GZ SET ZDT='"+bdcdyid+".zip' WHERE  bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"'); END;";
					  else//纯土地的
					    strSQL="BEGIN UPDATE BDCK.BDCS_SHYQZD_XZ SET ZDT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_SHYQZD_GZ SET ZDT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"'; END;";
					  dao.excuteQueryNoResult(strSQL);
				  }
			}
			
			//户
			if(bdcdylx.equals("031"))
			{
				  if(imgfrom.equals("1"))//二进制方式存入数据库
				  {
				String strSQL="BEGIN UPDATE BDCK.BDCS_H_XZ SET FCFHTWJ=? WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_H_GZ SET FCFHTWJ=? WHERE BDCDYH='"+bdcdyh+"'; END;";
				dao.updateBinarys(strSQL,is1,is2);
				  }
				  if(imgfrom.equals("2"))//文件名存入数据库
				  {
					  String strSQL="BEGIN UPDATE BDCK.BDCS_H_XZ SET FCFHT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_H_GZ SET FCFHT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"'; END;";
						dao.excuteQueryNoResult(strSQL);
				  }
			}
			//预测户
			if(bdcdylx.equals("032"))
			{
				  if(imgfrom.equals("1"))//二进制方式存入数据库
				  {
				String strSQL="BEGIN UPDATE BDCK.BDCS_H_XZY SET FCFHTWJ=? WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_H_GZY SET FCFHTWJ=? WHERE BDCDYH='"+bdcdyh+"'; END;";
				dao.updateBinarys(strSQL, is1,is2);
				  }
				  if(imgfrom.equals("2"))//文件名存入数据库
				  {
					  String strSQL="BEGIN UPDATE BDCK.BDCS_H_XZY SET FCFHT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"';UPDATE BDCK.BDCS_H_GZY SET FCFHT='"+bdcdyid+".zip' WHERE BDCDYH='"+bdcdyh+"'; END;";
						dao.excuteQueryNoResult(strSQL);
				  }
			}
			
			picstream.close();
			is1.close();
			is2.close();
		} catch (Exception ex) {
		   System.out.print(ex.getMessage());
			picstream.close();
			is1.close();
			is2.close();
		}
	}
	public String getBdcdyidByBdcdyh(String bdcdyh) {
		String bdcs_shyqzd_sql="select bdcdyid from bdck.bdcs_shyqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_shyqzd_xz where bdcdyh='"+bdcdyh+"'";
		String bdcs_syqzd_sql="select bdcdyid from bdck.bdcs_syqzd_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_syqzd_xz where bdcdyh='"+bdcdyh+"'";
		String h_y_sql="select bdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"'";
		String h_sql="select bdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union all select bdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"'";
		String shyqzd_by_h_sql="select bdcdyid from bdck.bdcs_shyqzd_xz where bdcdyid in (select zdbdcdyid from bdck.bdcs_h_xz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gz where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh='"+bdcdyh+"' union  select zdbdcdyid from bdck.bdcs_h_gzy where bdcdyh='"+bdcdyh+"')";
		ResultSet sqlDataRSet=null;
		//宗地的
		try {
			if(bdcdyh.toUpperCase().contains("W"))
			{
				sqlDataRSet=dao.excuteQuery(bdcs_syqzd_sql);
				if (sqlDataRSet.next())
				{
					return sqlDataRSet.getString("bdcdyid");
				}
				sqlDataRSet=dao.excuteQuery(bdcs_shyqzd_sql);
				if (sqlDataRSet.next())
				{
					return sqlDataRSet.getString("bdcdyid");	
				}
			 	
			 	
			}
			//房屋的
			if(bdcdyh.toUpperCase().contains("F"))
			{
				sqlDataRSet=dao.excuteQuery(shyqzd_by_h_sql);
				if (sqlDataRSet.next())
				{
					return sqlDataRSet.getString("bdcdyid");	
				}
				sqlDataRSet=dao.excuteQuery(h_sql);
				if (sqlDataRSet.next())
				{
					return sqlDataRSet.getString("bdcdyid");	
				}
				sqlDataRSet=dao.excuteQuery(h_y_sql);
				if (sqlDataRSet.next())
				{
					return sqlDataRSet.getString("bdcdyid");	
				
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

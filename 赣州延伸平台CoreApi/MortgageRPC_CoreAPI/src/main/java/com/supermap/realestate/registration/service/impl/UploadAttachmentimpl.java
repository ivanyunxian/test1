package com.supermap.realestate.registration.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.stream.FileImageOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.UploadAttachment;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.Http;


/**
 * 
 * 作者： 罗雨
 * 时间： 2016年8月23日 上午8:31:12
 * 功能：鹰潭市提取法院库共享附件的方法
 */
@Service("UploadAttachment")
public class UploadAttachmentimpl implements UploadAttachment {
	@Autowired
	private CommonDao baseCommonDao;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDao dao;
	@Autowired
	private CommonDao commonDao;
	// 从配置文件中取出待生成图像文件夹地址
		private  String fileSavePath ="";
		//服务存储路径配置
		private  String fileserverSavePath ="";
		private final String fileType = "查封或预查封法律文书";
		private final String fileType1 = "人民法院或其他有权查封机关工作人员的工作证和执行公务证";
		private String strMaterdataPathString="";
	@Override
	public String abstractFJFromZJK(String djywlsh, String fyywlsh) {
	
		String sql3 = "SELECT PROINST_ID FROM bdc_workflow.wfi_proinst where PROLSH = '"+ djywlsh +"' ";
//		ResultSet fjset3 = null;
		String proinstid = null;
		List<Map> list1 = baseCommonDao.getDataListByFullSql(sql3);
		proinstid=(String) list1.get(0).get("PROINST_ID");

		Connection jyConnection =  JH_DBHelper.getConnect_CXGXK();
		Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proinstid);
		SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");// 设置日期格式
		String first = fir.format(inst.getCreat_Date());
		SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
		String second = sec.format(inst.getCreat_Date());
	    fileSavePath = ConfigHelper.getNameByValue("material")+first+"\\"+second+"\\"+proinstid;
	    //服务存储路径配置
	    fileserverSavePath = first+"\\"+second+"\\"+proinstid;
	//}
	String returnValue = "不存在的证书号";
	
	ResultSet fjset =null;
	ResultSet fjset2 = null;
	ResultSet fjset3 = null;
	// 从库中和硬盘上删除已有附件
	
	try {
		 
         //删除该项目已有的档案资料
		//deleteOldMateral(proinstid);
		// 取得当前证件所有图片属性信息
		String sql = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.ZJFJ z on q.rwlsh = z. bdhm where BDHM = '"+ fyywlsh +"' ";
		String sql2 = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.WSFJ w on  q.wsbh = w. wsbh where WSBH = '"+ fyywlsh +"' ";
		String sql4 = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.ZJFJ z on q.rwlsh = z. bdhm where BDHM = '"+ fyywlsh +"' ";
		fjset = JH_DBHelper.excuteQuery(jyConnection, sql);//BDHM
		fjset2 = JH_DBHelper.excuteQuery(jyConnection, sql2);//WSBH
		fjset3 = JH_DBHelper.excuteQuery(jyConnection, sql4);//BDHM
		int i = 1;
		
		//证件附件的工作证
		while(fjset.next()) {						
			String bdhm=fjset.getString("BDHM");
			 System.out.println("正在生成图片文件...");
			List<String> list = BlobToFile(bdhm, proinstid,  jyConnection,i);
		
			if (list.size() < 1) {
				returnValue = "证书号没有关联附件！";
				continue;
			}
			String fileName = list.get(0);
			String filePath = list.get(1);
			String materilinstId = list.get(2);

			String Path =list.get(3);

			if (!SaveThumbnail(materilinstId, fileName, filePath, i, proinstid,Path))  {
				continue;
			}		

		i++;
		}
		//证件附件的公务证
		while (fjset3.next()) {
			
			String bdhm=fjset3.getString("BDHM");
			
			 System.out.println("正在生成图片文件...");
				List<String> list = BlobToFile2(bdhm, proinstid,  jyConnection,i);
			if (list.size() < 1) {
				returnValue = "证书号没有关联附件！";
				continue;
			}
			String fileName = list.get(0);
			String filePath = list.get(1);
			String materilinstId = list.get(2);
//			String materilinstId1 = list.get(2).substring(64);
			String Path =list.get(3);
		
			if (!SaveThumbnail(materilinstId, fileName, filePath, i, proinstid,Path))  {
				continue;
			}		
			i++;
		}
		//文书附件的文书内容
		while (fjset2.next()) {
			String wsbh=fjset2.getString("WSBH");
			 System.out.println("正在生成图片文件...");
			List<String> list = BlobToFile1(wsbh, proinstid,  jyConnection,i);
			if (list.size() < 1) {
				returnValue = "证书号没有关联附件！";
				continue;
			}
			String fileName = list.get(0);
			String filePath = list.get(1);
			String materilinstId = list.get(2);
			String path2 =list.get(3).substring(0,10);
			String Path=path2+list.get(3).substring(10,42);
			String path3 =list.get(3);
		
			if (!SaveThumbnail(materilinstId, fileName, filePath, i, proinstid,path3))  {
				continue;
			}		
			i++;
		}
		
		if (i != 1) {
			returnValue = "true";
		}
	} catch (Exception ex) {
		returnValue = "false";
		System.out.println(ex.getMessage());
	} finally {
		try {
			if(fjset != null ){
				fjset.close();
			}
			if(fjset2 != null ){
				fjset2.close();
			}
			if(fjset3 != null ){
				fjset3.close();
			}
			jyConnection.close();
		} catch (Exception e) {

		}
	}
	return returnValue;
}
	// BLOB字段生成图像并调用方法获取对应ID、name、filepath、THUMB供插入本地库使用
		private List<String> BlobToFile(String rwlsh,  String proinstid,Connection connection, int i) throws Exception {
			List<String> list = new ArrayList<String>();
//			String sql = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.WSFJ w on  q.rwlsh = w.wsbh where RWLSH='" + rwlsh + "'";
			String sql2 = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.ZJFJ z on  q.rwlsh = z.bdhm where RWLSH='" + rwlsh + "'";
			try {
//				ResultSet fjRSet = JH_DBHelper.excuteQuery(connection, sql);
				ResultSet fjRSet2= JH_DBHelper.excuteQuery(connection, sql2);
				String fileName = rwlsh;
				String file_Name = GetFileName(rwlsh, connection);;
			
				if (fjRSet2.next() && fileName != null ) {
					
					String filenumber = null;
					String proInstsql = " select * from BDC_WORKFLOW.WFI_PROINST t where t.proinst_id = '" + proinstid + "' ";
					List<Map> proInstList = commonDao.getDataListByFullSql(proInstsql);
					if (proInstList != null && proInstList.size() > 0) {
						Map proInst = proInstList.get(0);
						
					}		
					String materilinstId = GetMaterilinstId(proinstid, file_Name);
//					byte[] buf=null;
					byte[] buf2=null;

				Blob blob2 = fjRSet2.getBlob("GZZ");
				if(blob2 !=null){
				   InputStream inStream2 = blob2.getBinaryStream();
				   buf2= InputStreamToByte(inStream2);
				   
				}

					String filepath="";
					//根据文件存储配置选择对应方法存储图片：1、本地存储；2、服务存储
					String materialtype = ConfigHelper.getNameByValue("materialtype");
					String Path="";
					//String UpdatePathsql="";
					if (materialtype == null || materialtype.equals("")
							|| materialtype.trim().equals("1")) {
						strMaterdataPathString=fileSavePath+"\\" + materilinstId;
						
						File file = new File(strMaterdataPathString);
						if (!file.exists()) {
							file.mkdirs();
						}
						filepath = strMaterdataPathString  + "\\" + i
								+ fileName + ".jpg";
						Path=fileserverSavePath+"\\" + materilinstId;
						filepath=i+fileName + ".jpg".toString();
//						ByteToImage(buf, filepath);
						ByteToImage(buf2, filepath);
//						ByteToImage(buf3, filepath);
					} else if (materialtype.trim().equals("3")) {
						filepath = fileserverSavePath+"\\" + materilinstId;
						Path=filepath;
						String fileName1=i+fileName + ".jpg";
						if(buf2 != null){
						ByteToImage(buf2, "D:\\temp.jpg");
						}
//						if(buf3 != null){
//							ByteToImage(buf3, "D:\\temp.jpg");
//						}
						File tempfile = new File("D:\\temp.jpg"); 
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
						fileName1 = df.format(new Date())+ "_"+ java.net.URLEncoder.encode(fileName1, "UTF-8");
						Http.postFile(tempfile, fileName1, filepath);
						filepath=fileName1.toString();
					deleteFile("D:\\temp.jpg");
					}
					
					list = new ArrayList<String>();
					list.add(0, i + fileName);
					list.add(1, filepath);
					list.add(2, materilinstId);
					list.add(3,Path);
		
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return list;
		}
	
		// BLOB字段生成图像并调用方法获取对应ID、name、filepath、THUMB供插入本地库使用
				private List<String> BlobToFile2(String rwlsh,  String proinstid,Connection connection, int i) throws Exception {
					List<String> list = new ArrayList<String>();
//					String sql = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.WSFJ w on  q.rwlsh = w.wsbh where RWLSH='" + rwlsh + "'";
					String sql2 = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.ZJFJ z on  q.rwlsh = z.bdhm where RWLSH='" + rwlsh + "'";
					try {
//						ResultSet fjRSet = JH_DBHelper.excuteQuery(connection, sql);
						ResultSet fjRSet2= JH_DBHelper.excuteQuery(connection, sql2);
						String fileName = rwlsh;
						String file_Name = GetFileName(rwlsh, connection);;
					
						if (fjRSet2.next() && fileName != null ) {
							
							String filenumber = null;
							String proInstsql = " select * from BDC_WORKFLOW.WFI_PROINST t where t.proinst_id = '" + proinstid + "' ";
							List<Map> proInstList = commonDao.getDataListByFullSql(proInstsql);
							if (proInstList != null && proInstList.size() > 0) {
								Map proInst = proInstList.get(0);
								
							}		
							String materilinstId = GetMaterilinstId(proinstid, file_Name);
//							byte[] buf=null;
//							byte[] buf2=null;
							byte[] buf3=null;

						Blob blob3 = fjRSet2.getBlob("GWZ");
						if(blob3 != null){
						InputStream inStream3 = blob3.getBinaryStream();
						 buf3= InputStreamToByte(inStream3);
						}
							String filepath="";
							//根据文件存储配置选择对应方法存储图片：1、本地存储；2、服务存储
							String materialtype = ConfigHelper.getNameByValue("materialtype");
							String Path="";
							//String UpdatePathsql="";
							if (materialtype == null || materialtype.equals("")
									|| materialtype.trim().equals("1")) {
								strMaterdataPathString=fileSavePath+"\\" + materilinstId;
								
								File file = new File(strMaterdataPathString);
								if (!file.exists()) {
									file.mkdirs();
								}
								filepath = strMaterdataPathString  + "\\" + i
										+ fileName + ".jpg";
								Path=fileserverSavePath+"\\" + materilinstId;
								filepath=i+fileName + ".jpg".toString();
//								ByteToImage(buf, filepath);
//								ByteToImage(buf2, filepath);
								ByteToImage(buf3, filepath);
							} else if (materialtype.trim().equals("3")) {
								filepath = fileserverSavePath+"\\" + materilinstId;
								Path=filepath;
								String fileName1=i+fileName + ".jpg";
//								if(buf2 != null){
//								ByteToImage(buf2, "D:\\temp.jpg");
//								}
								if(buf3 != null){
									ByteToImage(buf3, "D:\\temp.jpg");
								}
								File tempfile = new File("D:\\temp.jpg"); 
								SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
								fileName1 = df.format(new Date())+ "_"+ java.net.URLEncoder.encode(fileName1, "UTF-8");
								Http.postFile(tempfile, fileName1, filepath);
								filepath=fileName1.toString();
							deleteFile("D:\\temp.jpg");
							}
							
							list = new ArrayList<String>();
							list.add(0, i + fileName);
							list.add(1, filepath);
							list.add(2, materilinstId);
							list.add(3,Path);
				
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return list;
				}
			
		
		private byte[] InputStreamToByte(InputStream is) throws IOException {

			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();

			return imgdata;
		}
		
		private void ByteToImage(byte[] data, String path) {
			if (data.length < 3 || path.equals(""))
				return;
			try {
				FileImageOutputStream imageOutput = new FileImageOutputStream(
						new File(path));
				imageOutput.write(data, 0, data.length);
				imageOutput.close();
				System.out.println("Make Picture success,Please find image in "
						+ path);
			} catch (Exception ex) {
				System.out.println("Exception: " + ex);
				ex.printStackTrace();
			}
		}

		
		private boolean SaveThumbnail(String materilinstId, String fileName, String filePath, int i, String proinstid, String Path) {
			boolean isTrue;
			
			try {
				fileName = fileName + ".jpg";
				//String imageStr = smproMater.CreatThumb2(fileSavePath,fileName, materilinstId);
				Wfi_MaterData materdata = new Wfi_MaterData();
				materdata.setMaterialdata_Id(Common.CreatUUID());
				materdata.setMaterilinst_Id(materilinstId);
				materdata.setFile_Name(fileName);
				materdata.setFile_Path(filePath);
				materdata.setFile_Index((short) i);
				//materdata.setThumb(imageStr);
				materdata.setUpload_Date(new Date());
				materdata.setPath(Path);
				commonDao.save(materdata);
				Wfi_ProMater promater = commonDao.get(Wfi_ProMater.class,
						materilinstId);
				promater.setImg_Path(materdata.getMaterialdata_Id());
				promater.setMaterial_Status(3);
				commonDao.update(promater);
				commonDao.flush();
				isTrue = true;
			} catch (Exception ex) {
				isTrue = false;
			}finally{
				
			}
			return isTrue;
		}

		private String GetMaterilinstId(String proinstid, String fileName)
				throws Exception {
			String sql = "select MATERILINST_ID from bdc_workflow.wfi_promater where MATERIAL_NAME='"
					+ fileName + "' and PROINST_ID ='" + proinstid + "'";
			String materilinstid = "";
			String materilinstid1 ="";
			
			try {
				List<Map> lists = commonDao.getDataListByFullSql(sql);
				if (lists != null && lists.size() > 0) {
					Map mater = lists.get(0);
					materilinstid = mater.get("MATERILINST_ID").toString();
				} else {
//					String sql2 = "select MATERILINST_ID from bdc_workflow.wfi_promater where MATERIAL_NAME='"
//							+ fileType + "' and PROINST_ID ='" + proinstid + "'";
					String sql3 = "select MATERILINST_ID from bdc_workflow.wfi_promater where MATERIAL_NAME='"
							+ fileType1 + "' and PROINST_ID ='" + proinstid + "'";
//					List<Map> promaters = commonDao.getDataListByFullSql(sql2);
					List<Map> promaters1 = commonDao.getDataListByFullSql(sql3);
//					if (promaters != null && promaters.size() > 0) {
//						Map promater = promaters.get(0);
//						materilinstid = promater.get("MATERILINST_ID").toString();
//					}
					if (promaters1 != null && promaters1.size() > 0) {
						Map promater1 = promaters1.get(0);
						
						
						materilinstid1 = promater1.get("MATERILINST_ID").toString();
					}
				}
			} catch (Exception ex) {

			}
			materilinstid =  materilinstid1;
			return materilinstid;
		}
		
		private boolean flag = false;
		private boolean deleteFile(String sPath) {
			flag = false;
			File file = new File(sPath);
			// 路径为文件且不为空则进行删除
			if (file.isFile() && file.exists()) {
				file.delete();
				flag = true;
			}
			return flag;
		}
		
		private String GetFileName( String rwlsh,Connection connection) throws Exception {
			String fileName = null;
			String sql = "select gzzbm  from sharesearch.ZJFJ where BDHM='"+ rwlsh + "'" ;
			String sql2 = "select wjmc  from sharesearch.WSFJ where WSBH='"+ rwlsh + "'" ;
			try {
				ResultSet fileRSet = JH_DBHelper.excuteQuery(connection, sql);
				ResultSet fileRSet2= JH_DBHelper.excuteQuery(connection, sql2);
				if (fileRSet.next()) {
					fileName = fileRSet.getString("GZZBM");
				}
				if (fileRSet2.next()) {
					fileName = fileRSet.getString("WJMC");
				}
			} catch (Exception ex) {

			}
			return fileName;
		}
		// BLOB字段生成图像并调用方法获取对应ID、name、filepath、THUMB供插入本地库使用
		private List<String> BlobToFile1(String rwlsh,  String proinstid,Connection connection, int i) throws Exception {
			List<String> list = new ArrayList<String>();
			String sql = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.WSFJ w on  q.rwlsh = w.wsbh where RWLSH='" + rwlsh + "'";
//			String sql2 = "select * from sharesearch.QUERYAPPROVE q left join sharesearch.ZJFJ z on  q.rwlsh = z.bdhm where RWLSH='" + rwlsh + "'";
			try {
				ResultSet fjRSet = JH_DBHelper.excuteQuery(connection, sql);
//				ResultSet fjRSet2= JH_DBHelper.excuteQuery(connection, sql2);
				String fileName = rwlsh;
				String file_Name = GetFileName(rwlsh, connection);;
			
				if (fjRSet.next() && fileName != null ) {
					
					String filenumber = null;
					String proInstsql = " select * from BDC_WORKFLOW.WFI_PROINST t where t.proinst_id = '" + proinstid + "' ";
					List<Map> proInstList = commonDao.getDataListByFullSql(proInstsql);
					if (proInstList != null && proInstList.size() > 0) {
						Map proInst = proInstList.get(0);
						
					}		
					String materilinstId = GetMaterilinstId1(proinstid, file_Name);
					byte[] buf=null;
					
			   Blob blob = fjRSet.getBlob("WSNR");
				if( blob != null){
				InputStream inStream = blob.getBinaryStream();
				buf= InputStreamToByte(inStream);
					}

					String filepath="";
					//根据文件存储配置选择对应方法存储图片：1、本地存储；2、服务存储
					String materialtype = ConfigHelper.getNameByValue("materialtype");
					String Path="";
					if (materialtype == null || materialtype.equals("")
							|| materialtype.trim().equals("1")) {
						strMaterdataPathString=fileSavePath+"\\" + materilinstId;
						File file = new File(strMaterdataPathString);
						if (!file.exists()) {
							file.mkdirs();
						}
						filepath = strMaterdataPathString  + "\\" + i
								+ fileName + ".jpg";
						Path=fileserverSavePath+"\\" + materilinstId;
						filepath=i+fileName + ".jpg".toString();
 				       ByteToImage(buf, filepath);
//						ByteToImage(buf2, filepath);
//						ByteToImage(buf3, filepath);
					} else if (materialtype.trim().equals("3")) {
						filepath = fileserverSavePath+"\\" + materilinstId;
						Path=filepath;
						String fileName1=i+fileName + ".jpg";
						if(buf != null){
						ByteToImage(buf, "D:\\temp.jpg");
//						ByteToImage(buf3, "C:\\temp.jpg");
						}
						File tempfile = new File("D:\\temp.jpg"); 
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
						fileName1 = df.format(new Date())+ "_"+ java.net.URLEncoder.encode(fileName1, "UTF-8");
						Http.postFile(tempfile, fileName1, filepath);
						filepath=fileName1.toString();
						deleteFile("D:\\temp.jpg");
					}
					
					list = new ArrayList<String>();
					list.add(0, i + fileName);
					list.add(1, filepath);
					list.add(2, materilinstId);
					list.add(3,Path);
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return list;
		}
		private String GetMaterilinstId1(String proinstid, String fileName)
				throws Exception {
			String sql = "select MATERILINST_ID from bdc_workflow.wfi_promater where MATERIAL_NAME='"
					+ fileName + "' and PROINST_ID ='" + proinstid + "'";
			String materilinstid = "";
			try {
				List<Map> lists = commonDao.getDataListByFullSql(sql);
				if (lists != null && lists.size() > 0) {
					Map mater = lists.get(0);
					materilinstid = mater.get("MATERILINST_ID").toString();
				} else {
					String sql2 = "select MATERILINST_ID from bdc_workflow.wfi_promater where MATERIAL_NAME='"
							+ fileType + "' and PROINST_ID ='" + proinstid + "'";
					List<Map> promaters = commonDao.getDataListByFullSql(sql2);
					if (promaters != null && promaters.size() > 0) {
						Map promater = promaters.get(0);
						materilinstid = promater.get("MATERILINST_ID").toString();
					}
				}
			} catch (Exception ex) {

			}
			return materilinstid;
		}
	
	}
	


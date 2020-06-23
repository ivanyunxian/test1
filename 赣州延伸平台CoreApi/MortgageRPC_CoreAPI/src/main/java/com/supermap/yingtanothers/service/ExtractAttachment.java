package com.supermap.yingtanothers.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.stream.FileImageOutputStream;

import oracle.net.aso.n;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.Http;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年3月8日 下午3:31:12
 * 功能：鹰潭市提取共享房产库共享附件的方法
 */
@Service("ExtractAttachment")
public class ExtractAttachment {

	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDao dao;
	
	// 从配置文件中取出待生成图像文件夹地址
	private  String fileSavePath ="";//ConfigHelper.getNameByValue("material");// 
	//服务存储路径配置
	private  String fileserverSavePath ="";
	private final String fileType = "房管局共享资料";
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmProMater smproMater;
	//存放在materdata表中的路径
    private String strMaterdataPathString="";
	public String abstractFJFromZJK(String proinstid, String casenum, String file_Path) {
			
			Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proinstid);
			SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");// 设置日期格式
			String first = fir.format(inst.getCreat_Date());
			SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
			String second = sec.format(inst.getCreat_Date());
		    fileSavePath = ConfigHelper.getNameByValue("material")+first+"\\"+second+"\\"+proinstid;
		    //服务存储路径配置
		    fileserverSavePath = first+"\\"+second+"\\"+proinstid;
		//}
		String returnValue = "不存在的案卷号";
		Connection jyConnection = null;
		// 从库中和硬盘上删除已有附件
		try {
			jyConnection = JH_DBHelper.getConnect_jy();
             //删除该项目已有的档案资料
			//deleteOldMateral(proinstid);
			// 从“图像用户表”取得当前合同号（案卷号）所有图片属性信息

			String sql = "select * from gxfck.fj where gxxmbh = '"+ casenum +"' ";
			ResultSet imagescanset = JH_DBHelper.excuteQuery(jyConnection, sql);
			int i = 1;
			while (imagescanset.next()) {
				// 根据“图像ID”进行循环												
				String imageid = imagescanset.getString("ID");
				String fileid = imagescanset.getString("HZM");
				if (StringHelper.isEmpty(fileid)) {
					fileid = imagescanset.getString("WJMC");
				}
				 System.out.println("正在生成图片文件...");
				List<String> list = BlobToFile(casenum, imageid, fileid, proinstid, file_Path, i, jyConnection);
				if (list.size() < 1) {
					returnValue = "案卷号没有关联附件！";
					continue;
				}
				String fileName = list.get(0);
				String filePath = list.get(1);
				String materilinstId = list.get(2);
				String Path=list.get(3);
			
				if (!SaveThumbnail(materilinstId, fileName, filePath, i, proinstid,Path)) {
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
				jyConnection.close();
			} catch (Exception e) {

			}
		}
		return returnValue;
	}

	private String GetMaterilinstId(String proinstid, String fileName)
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

	// 从opr_filereceived表取图像文件名fileid
	private String GetFileName(String casenum, String fildid,Connection connection) throws Exception {
		String fileName = null;
		String sql = "select fileid from house.opr_filereceived where CASENUM='"
				+ casenum + "' and FILETYPE = '" + fildid + "'";
		try {
			ResultSet fileRSet = JH_DBHelper.excuteQuery(connection, sql);
			if (fileRSet.next()) {
				fileName = fileRSet.getString("FILEID");
			}
		} catch (Exception ex) {

		}
		return fileName;
	}

	// BLOB字段生成图像并调用方法获取对应ID、name、filepath、THUMB供插入本地库使用
	private List<String> BlobToFile(String casenum, String imageid, String fildid, String proinstid, String file_Path, int i,Connection connection) throws Exception {
		List<String> list = new ArrayList<String>();
		String sql = "select NR from gxfck.fjdata where FJID='" + imageid + "'";
		try {
			ResultSet imageDataRSet = JH_DBHelper.excuteQuery(connection, sql);
//			String fileName = GetFileName(casenum, fildid, connection);
			String fileName = fildid;
			String xmlPath = file_Path + "WEB-INF\\classes\\ShareEnclosureParameter.xml";
			if (imageDataRSet.next() && fileName != null) {
				
				String filenumber = null;
				String proInstsql = " select * from BDC_WORKFLOW.WFI_PROINST t where t.proinst_id = '" + proinstid + "' ";
				List<Map> proInstList = commonDao.getDataListByFullSql(proInstsql);
				if (proInstList != null && proInstList.size() > 0) {
					Map proInst = proInstList.get(0);
					filenumber = proInst.get("FILE_NUMBER").toString();
				}				
				String djlx = QueryDJLXByProjectId(filenumber);
				String file_Name = "";
				// 首次登记--抵押
				if (djlx.equals("100")) {
					String qllx = QueryQLLXByProjectId(filenumber);
					//抵押
					if (qllx.equals("23")) {
						file_Name = ReadXML(xmlPath,"DYDJ",fileName);
					//其他首次登记
					} else if(qllx.equals("4")){
						file_Name = ReadXML(xmlPath,"SCDJ",fileName);
					}					
					if (file_Name == null) {
						file_Name = fileType;
					}
				// 转移登记
				}else if(djlx.equals("200")){
 					file_Name = ReadXML(xmlPath,"ZYDJ",fileName);
					if (file_Name == null) {
						file_Name = fileType;
					}
				//变更登记
				}else if(djlx.equals("300")){
 					file_Name = ReadXML(xmlPath,"BGDJ",fileName);
					if (file_Name == null) {
						file_Name = fileType;
					}
				//预告登记
				}else if(djlx.equals("700")){
					file_Name = ReadXML(xmlPath,"YGDJDY",fileName);
					if (file_Name == null) {
						file_Name = fileType;
					}
			    
				}else if(djlx.equals("900")){
 					file_Name = ReadXML(xmlPath,"QTDJ",fileName);
					if (file_Name == null) {
						file_Name = fileType;
					}
				
				}else {
					file_Name = fileType;
				}
//				String qllx = null;
//				String s = null;
//				String xmxxsql = " select * from BDCK.BDCS_XMXX t where t.PROJECT_ID = '" + filenumber + "' ";
//				List<Map> xmxxList = commonDao.getDataListByFullSql(xmxxsql);
//				if (xmxxList != null && xmxxList.size() > 0) {
//					Map xmxx = xmxxList.get(0);
//					qllx = xmxx.get("QLLX").toString();
//				}
//				if (qllx.equals(QLLX.DIYQ.Value)) {
//					s = StringHelper.readFile(file_Path + "WEB-INF\\classes\\抵押收件资料匹配.txt");
//				} else {
//					s = StringHelper.readFile(file_Path + "WEB-INF\\classes\\转移收件资料匹配.txt");
//				}
//				// 房产文件类型，不动产文件类型
//				Map<String, String> map = new HashMap<String, String>();
//				map = StringHelper.JsonToMap(s);
//				String file_Name = "";
//				if (map != null && map.get(fileName) != null) {
//					file_Name = map.get(fileName);
//				} else {
//					file_Name = fileType;
//				}
								
 				String materilinstId = GetMaterilinstId(proinstid, file_Name);
				Blob blob = imageDataRSet.getBlob("NR");
				InputStream inStream = blob.getBinaryStream();
				byte[] buf = InputStreamToByte(inStream);
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
					ByteToImage(buf, filepath);
					filepath=i+fileName + ".jpg".toString();
				} else if (materialtype.trim().equals("3")) {
					filepath = fileserverSavePath+"\\" + materilinstId;
					Path=filepath;
					String fileName1=i+fileName + ".jpg";
					ByteToImage(buf, "D:\\temp.jpg");
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

	// 私有的查询登记类型的方法
	private String QueryDJLXByProjectId(String file_number) {
		String djlx = null;
		try {
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			conditionBuilder.append(" AND PROJECT_ID = '").append(file_number).append("'");
			String strQuery = conditionBuilder.toString();
			List<BDCS_XMXX> xmxxlist = dao.getDataList(BDCS_XMXX.class, strQuery);
			if (xmxxlist.size() > 0) {
				djlx = xmxxlist.get(0).getDJLX();
			}
		} catch (Exception e) {
			System.out.println("查询登记类型失败");
		}

		return djlx;
	}
		
	// 私有的查询登记类型的方法
	private String QueryQLLXByProjectId(String file_number) {
		String qllx = null;
		try {
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			conditionBuilder.append(" AND PROJECT_ID = '").append(file_number).append("'");
			String strQuery = conditionBuilder.toString();
			List<BDCS_XMXX> xmxxlist = dao.getDataList(BDCS_XMXX.class, strQuery);
			if (xmxxlist.size() > 0) {
				qllx = xmxxlist.get(0).getQLLX();
			}
		} catch (Exception e) {
			System.out.println("查询权利类型失败");
		}

		return qllx;
	}
				
	private String ReadXML(String xmlPath,String djlx,String fileName) {
		FileInputStream fis = null;
		String result = null;
		Boolean sign = false; //标记
		try {
			fis = new FileInputStream(xmlPath);
			Document doc = new SAXReader().read(fis);
			Element root = doc.getRootElement();
			if (root != null) {							
				for(Iterator<Element> i = root.elementIterator(); i.hasNext();){
		            Element element = (Element)i.next();
		            String Name = element.getName();
		            if (Name.equals(djlx)) {
		            	for(Iterator<Element> j = element.elementIterator(); j.hasNext();){
		            		if(sign == true){
		            			break;
		            		}
			            	Element element2 =  (Element)j.next();
			            	String name = element2.getName();
			            	String text = element2.getText();			            	
			            	if (!StringHelper.isEmpty(name) && name.equals("contents")) {
			            		result = text;
							}else if(!StringHelper.isEmpty(name) && name.equals("likeCondition")){
								if (!StringHelper.isEmpty(text)) {
									if (text.contains("、")) {
										String[] s = text.split("、");
										for (int k = 0; k < s.length; k++) {
											String ss = s[k];
											if (fileName.indexOf(ss) >= 0) {
												sign = true;
												break;
											}
										}
									}else {
										if (fileName.indexOf(text) >= 0) {
											sign = true;
											break;
										}
									}
								}
							}
		            	}
					}
				}
			}
		} catch (Exception e) {
			YwLogUtil.addYwLog("读取共享房产库附件xml配置文件-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			return null;
		}
		if (sign == true) {
			return result;
		} else {
			return null;
		}		
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
		}
		return isTrue;
	}

	// 删除原有资料，包括库中的和硬盘上的文件
	private void deleteOldMateral(String proinstid) {
		try {
			List<String> materialList = getMaterialIDList(proinstid);
			deleteImgInDisk(materialList);
		} catch (Exception e) {

		}
	}

	// 取出本流程所有上传过附件的materilinstid
	private List<String> getMaterialIDList(String proinstid) {
		List<String> list = new ArrayList<String>();
		String sql = "select MATERILINST_ID from " + Common.WORKFLOWDB
				+ "wfi_promater where IMG_PATH is not null and PROINST_ID ='"
				+ proinstid + "'";
		String materilinstid = "";
		try {
			List<Map> materlinsts = commonDao.getDataListByFullSql(sql);
			if (materlinsts != null && materlinsts.size() > 0) {
				for (Map materlinst : materlinsts) {
					materilinstid = materlinst.get("MATERILINST_ID").toString();
					list.add(materilinstid);
					commonDao.deleteQuery("delete " + Common.WORKFLOWDB
							+ "wfi_materdata where MATERILINST_ID='"
							+ materilinstid + "'");
				}
				commonDao.flush();
			}
		} catch (Exception ex) {

		}
		return list;
	}

	// 从磁盘中删除图片文件
	private void deleteImgInDisk(List<String> materialList) {
		if (materialList == null || materialList.size() == 0)
			return;
		for (String materialid : materialList) {
			deleteDirectory(fileSavePath + materialid);
		}
	}

	private boolean flag = false;

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
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

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	private boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
}

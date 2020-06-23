package com.supermap.realestate.registration.service.impl.share;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.service.ExtractDataService;
import com.supermap.realestate.registration.service.InsertDataService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.DA_DBHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.gxjyk.Gxjhxm;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYCFDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYDYAQ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYFDCQ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYJYBGQH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYQLR;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYXZDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYYGDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYYYDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.MATERDATA;
import com.supermap.wisdombusiness.framework.model.gxjyk.PROMATER;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;

@Service("extractDataForGXService")
public class ExtractDataForGXServiceImpl extends ExtractDataServiceImpl {

	/**
	 * 李堃 从共享交易库中抽取附件
	 * 
	 * @param proinstId流程实例ID
	 * @param 房产业务号
	 */
	public boolean ExtractFJFromZJK(String proinstId, String caseNum) {
		boolean flag = false;
		try {
//			demo();
			// 按顺序查出共享交易库中所有收件种类
			List<PROMATER> gxjy_promaters = CommonDaoJY.getDataList(
					PROMATER.class, "PROINST_ID = '" + caseNum
							+ "' order by MATERIAL_INDEX");
			if (gxjy_promaters != null && gxjy_promaters.size() > 0) {
				Properties props = new Properties();
				// 协同站点基准URL
				String shareURL = ConfigHelper.getNameByValue("URL_SHARE");
				int m = 1;
				// 循环每一个中间库中的收件种类
				for (int i = 0; i < gxjy_promaters.size(); i++) {
					String condt = "PROINST_ID='" + proinstId
							+ "' and MATERIAL_NAME='"
							+ gxjy_promaters.get(i).getMATERIAL_NAME() + "'";
					List<Wfi_ProMater> wfi_promaters = baseCommonDao
							.getDataList(Wfi_ProMater.class, condt);
					if (wfi_promaters != null && wfi_promaters.size() > 0) {
						// 获取workflow库中对应附件类型的ID
						String materilinstID = wfi_promaters.get(0)
								.getMaterilinst_Id();
						// 插入附件前，先删除该模板下的原有附件
						baseCommonDao.deleteEntitysByHql(Wfi_MaterData.class,
								"MATERILINST_ID='" + materilinstID + "'");

						// 获取中间库每个种类下的所有文件
						List<MATERDATA> gxjy_materdatas = CommonDaoJY
								.getDataList(MATERDATA.class,
										"MATERILINST_ID = '"
												+ gxjy_promaters.get(i)
														.getMATERILINST_ID()
												+ "'");
						if (gxjy_materdatas != null
								&& gxjy_materdatas.size() > 0) {
							for (int k = 0; k < gxjy_materdatas.size(); k++) {
								String imgURL = "";
								String path = gxjy_materdatas.get(k).getPATH();
								String relativeURL = gxjy_materdatas.get(k)
										.getRELATIVEURL();
								String fileName = gxjy_materdatas.get(k)
										.getFILE_PATH();
//								if (shareURL != null && !shareURL.equals("")
//										&& relativeURL != null
//										&& !relativeURL.equals("")) {
									// 从站点外通过封装的服务取图片
									imgURL =  relativeURL;
//								} else {
//									// 从站点下相对路径取图片
//									imgURL = shareURL + path + "\\" + fileName;
//								}

								// 插入到workflow库
								insertDataService.InsertFJFromZJKEx(proinstId,
										fileName, materilinstID, imgURL, m,null);
								m++;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		}

		return flag;
	}

	
	
	String test()
	{
//		return "{\"GXJHXM\":[{\"QLLX\":\"23\",\"GXXMBH\":\"83a63d48-10fa-4b95-8f9b-6f9d79e75772\",\"DJDL\":\"100\",\"BLJD\":\"归档\",\"CASENUM\":\"201205030001\",\"TSSJ\":\"2016-11-14 10:31:10\"}],\"H\":[{\"GXXMBH\":\"83a63d48-10fa-4b95-8f9b-6f9d79e75772\",\"BDCDYH\":\"20120313041600001\",\"QSC\":\"1\",\"ZZC\":\"2\",\"ZCS\":\"2\",\"CH\":\"1-2\",\"SZC\":\"1-2\",\"ZL\":\"陆川县农业局东面\",\"MJDW\":\"1\",\"SHBW\":\"\",\"FWZT\":\"0\",\"RELATIONID\":\"450802001047GB01401F00010024\"}],\"FDCQ\":[],\"DYAQ\":[{\"GXXMBH\":\"83a63d48-10fa-4b95-8f9b-6f9d79e75772\",\"DYBDCLX\":\"2\",\"DYR\":\"王帆\",\"DYFS\":\"1\",\"DJLX\":\"抵押\",\"DJYY\":\"\",\"BDCDJZMH\":\"\",\"BDBZZQSE\":\"50000\",\"ZWLXQSSJ\":\"2003-03-21 08:24:59\",\"ZWLXJSSJ\":\"2023-03-21 08:25:07\",\"DJJG\":\"陆川房管局\",\"DBR\":\"\",\"DJSJ\":\"\",\"LYQLID\":\"\",\"DYMJ\":\"106.32\",\"ZGZQQDSS\":\"\",\"ZGZQSE\":\"96000\"}],\"YGDJ\":[],\"QLR\":[{\"GXXMBH\":\"83a63d48-10fa-4b95-8f9b-6f9d79e75772\",\"SXH\":\"1\",\"QLRMC\":\"王帆\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"\"}]}";
		return "{\"GXJHXM\":[{\"QLLX\":\"4\",\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"DJDL\":\"200\",\"BLJD\":\"归档\",\"CASENUM\":\"201205240026\",\"TSSJ\":\"2016-12-22 17:24:37\"},{\"QLLX\":\"4\",\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"DJDL\":\"200\",\"BLJD\":\"归档\",\"CASENUM\":\"201205240026\",\"TSSJ\":\"2016-12-22 17:24:37\"}],\"H\":[{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"QSC\":\"1\",\"ZZC\":\"7\",\"ZCS\":\"7\",\"CH\":\"1\",\"SZC\":\"1\",\"ZL\":\"温泉镇碰搪三队（兴达五巷南边）车库\",\"MJDW\":\"1\",\"SHBW\":\"\",\"FWZT\":\"2\",\"RELATIONID\":\"20120524002600002\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"QSC\":\"1\",\"ZZC\":\"7\",\"ZCS\":\"7\",\"CH\":\"3\",\"SZC\":\"3\",\"ZL\":\"温泉镇碰搪三队（兴达五巷南边）\",\"MJDW\":\"1\",\"SHBW\":\"\",\"FWZT\":\"2\",\"RELATIONID\":\"20120524002600003\"}],\"FDCQ\":[{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"QLLX\":\"4\",\"BDCQZH\":\"06012794\",\"DJDL\":\"200\",\"DJYY\":\"\",\"FDZL\":\"温泉镇碰搪三队（兴达五巷南边）\",\"TDSYQR\":\"\",\"TDSYQSSJ\":\"\",\"TDSYJSSJ\":\"\",\"GHYT\":\"1\",\"SZC\":\"3\",\"ZCS\":\"7\",\"JZMJ\":\"98.074\",\"DJJG\":\"陆川房管局\",\"DBR\":\"\",\"DJSJ\":\"\",\"CZFS\":\"0\",\"FDCJYJG\":\"0\",\"FJ\":\"234R34\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"QLLX\":\"4\",\"BDCQZH\":\"06012794\",\"DJDL\":\"200\",\"DJYY\":\"\",\"FDZL\":\"温泉镇碰搪三队（兴达五巷南边）\",\"TDSYQR\":\"\",\"TDSYQSSJ\":\"\",\"TDSYJSSJ\":\"\",\"GHYT\":\"1\",\"SZC\":\"3\",\"ZCS\":\"7\",\"JZMJ\":\"98.074\",\"DJJG\":\"陆川房管局\",\"DBR\":\"\",\"DJSJ\":\"\",\"CZFS\":\"0\",\"FDCJYJG\":\"0\",\"FJ\":\"ABC\"}],\"DYAQ\":[],\"YGDJ\":[],\"QLR\":[{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"SXH\":\"1\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"},{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"SXH\":\"2\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"SXH\":\"1\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"SXH\":\"2\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"}]}";	}
	//广西省厅房产推数据到GXJYK测试
	public String demo() {
		String resultString="";
		//待传输的json数据
		String jsonString=test();//"{\"name\":\"张三\"}";
		JSONObject jsondata=JSONObject.fromObject(jsonString);
		//用户名密码
		String uname="zhangsan";
		String pwd="123";
		HttpURLConnection connection;
		try {
			//请求服务地址
			URL postUrl=new URL("http://localhost:8080/share/app/sharezjk/fctobdc");
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			// 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
			String content = "username=" + URLEncoder.encode(uname, "UTF-8");
			content += "&password="+ URLEncoder.encode(pwd, "UTF-8");
			content += "&jsondata="+ URLEncoder.encode(jsondata.toString(), "UTF-8");
			out.writeBytes(content);
			out.flush();
			out.close();
			JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;
			resultString = reader.readLine();
			reader.close();
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultString;
	}
}

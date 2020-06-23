package com.supermap.realestate.registration.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;








import com.supermap.realestate.registration.model.BDCS_SJSB;
import com.supermap.realestate.registration.util.ConstValue.LOG;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.Ywlog;

/**
 * 记录业务日志工具类
 * @author diaoliwei
 * @date 2016-1-14
 *
 */
public class YwLogUtil {
	
	/**
	 * 新增业务日志
	 */
	public static void addYwLog(String operateContent, String isSuccess, LOG log){
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		Ywlog ywLog = new Ywlog();
		String osName = System.getProperty("os.name");
		try {
			if(osName.toLowerCase().indexOf("windows")>-1){
				ywLog.setHostName(InetAddress.getLocalHost().getHostName());
				ywLog.setIp(InetAddress.getLocalHost().getHostAddress());
			}else{
				Enumeration<NetworkInterface> nif = NetworkInterface.getNetworkInterfaces();
		        while(nif.hasMoreElements()) {
		            NetworkInterface nekif = (NetworkInterface) nif.nextElement();
		            Enumeration<InetAddress> iparray = nekif.getInetAddresses();
		            while (iparray.hasMoreElements()) {
		                InetAddress ip = (InetAddress) iparray.nextElement();
		                if( ip.isSiteLocalAddress()  
		                        && !ip.isLoopbackAddress()   
		                        && ip.getHostAddress().indexOf(":")==-1){
		                	ywLog.setHostName(ip.getHostName());
		                	ywLog.setIp(ip.getHostAddress());
		                }
		            }
		        }
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ywLog.setId((String) SuperHelper.GeneratePrimaryKey());
		ywLog.setOperateTime(new Date());
		User user = Global.getCurrentUserInfo();
		if(user != null){
			ywLog.setOperateUser(user.getUserName());
			ywLog.setLoginName(user.getLoginName());
		}
		ywLog.setIsSuccess(isSuccess);//是否成功，0失败，1成功
		ywLog.setOperateContent(operateContent);
		ywLog.setLevelName(log.Name);
		ywLog.setLogLevel(log.Value);
		dao.save(ywLog);
		dao.flush();
	}
	
	/**
	 * 添加数据上报结果
	 * @author diaoliwei
	 * @param bwmc  报文名
	 * @param path  响应报文路径
	 * @param responseinfo  响应报文信息
	 * @param successflag  成功与否标识
	 * @param reccode 接入业务编码
	 */
	public static void addSjsbResult(String bwmc, String path, String responseinfo, String successflag, String reccode,String proisntid){
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		BDCS_SJSB sjsb = new BDCS_SJSB();
		sjsb.setOPERATETIME(new Date());
		User user = Global.getCurrentUserInfo();
		if(user != null){
			sjsb.setOPERATEUSER(user.getUserName());
			sjsb.setLOGINNAME(user.getLoginName());
		}
//		sjsb.setBDCDYH(bdcdyh);
		sjsb.setBWMC(bwmc);
		sjsb.setPATH(path);
		sjsb.setRESPONSEINFO(responseinfo);
		sjsb.setSUCCESSFLAG(successflag);
		sjsb.setRECCODE(reccode);
		sjsb.setPROINSTID(proisntid);
		dao.save(sjsb);
		dao.flush();
	}
	
	/**
	 * 获取当天的流水号（方便命名上报xml的文件名）
	 * @author diaoliwei
	 * @date 2016-1-29
	 * @return
	 */
	public static String getMaxLsh(){
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		String day = DateUtil.getDay();
		String delSql = "DELETE FROM BDCK.MAXYWH T WHERE T.IDAY <> '" + day + "' ";
		dao.updateBySql(delSql);
		String sql = " SELECT IDAY,MAXVALUES FROM BDCK.MAXYWH T WHERE T.IDAY ='" + day + "' ";
		List<Map> list = dao.getDataListByFullSql(sql);
		if(list == null || list.size() == 0){
			String id = SuperHelper.GeneratePrimaryKey();
			String maxValues = "000001";
			String addsql = "INSERT INTO BDCK.MAXYWH (ID,IDAY,MAXVALUES) VALUES ('"+id+"',"+day+",'"+maxValues+"')";
			dao.updateBySql(addsql);
			return maxValues;
		}else{
			for (Map map : list) {
				String maxValue = (String) map.get("MAXVALUES");
				if(maxValue.length() > 0){
					maxValue = StringHelper.getCountByParamter(maxValue, 6, 1);
					String updateSql = " UPDATE BDCK.MAXYWH SET MAXVALUES ='" + maxValue + "' WHERE IDAY='" + day + "' ";
					dao.updateBySql(updateSql);
					return maxValue;
				}
			}
		}
		return null;
	}
	
}

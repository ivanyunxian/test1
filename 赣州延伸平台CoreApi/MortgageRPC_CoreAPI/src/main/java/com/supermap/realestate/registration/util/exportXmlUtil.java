package com.supermap.realestate.registration.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.sf.json.JSONObject;

import org.springframework.util.StringUtils;

import com.jcraft.jsch.ChannelSftp;
import com.supermap.realestate.registration.dataExchange.DJFDJFZ;
import com.supermap.realestate.registration.dataExchange.DJFDJGD;
import com.supermap.realestate.registration.dataExchange.DJFDJSF;
import com.supermap.realestate.registration.dataExchange.DJFDJSH;
import com.supermap.realestate.registration.dataExchange.DJFDJSJ;
import com.supermap.realestate.registration.dataExchange.DJFDJSQR;
import com.supermap.realestate.registration.dataExchange.DJFDJSZ;
import com.supermap.realestate.registration.dataExchange.DJTDJSLSQ;
import com.supermap.realestate.registration.dataExchange.FJF100;
import com.supermap.realestate.registration.dataExchange.Head;
import com.supermap.realestate.registration.dataExchange.Message;
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.utils.ValidataXML;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.ygdj.QLFQLYGDJ;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_REPORTINFO;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.service.Sender.dataReport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.ProcedureParam;
import com.supermap.realestate.registration.tools.ProcedureTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.RECCODE;
import com.supermap.realestate.registration.util.HttpRequest.ParamInfo;
import com.supermap.realestate.registration.util.HttpRequest.WSDLInfo;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

/**
 * 流程通用生成报文类，试用
 * 2019年2月26日16:48:05
 * @author liangq
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class exportXmlUtil {
	
	private static CommonDao dao;
	
	static {
		if (dao == null) {
			dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
	}
	
	/**
	 * 方法入口
	 * @param path
	 * @param actinstID
	 * @return
	 */
	public static Map<String,String> createXMLAndUp(String path,String actinstID){
		Map<String,String> maps = new HashMap<String, String>();
		List<Wfi_ActInst> acts = dao.getDataList(Wfi_ActInst.class, " actinst_id = '"+actinstID+"' order by ACTINST_START desc ");
		if(acts.size() > 0){
			List<Wfi_ProInst> pros = dao.getDataList(Wfi_ProInst.class, " proinst_id = '"+acts.get(0).getProinst_Id()+"' ");
			if(pros.size()>0){
				List<BDCS_XMXX> xmxxs = dao.getDataList(BDCS_XMXX.class, " ywlsh = '"+pros.get(0).getProlsh()+"' "); 
				if(xmxxs.size() > 0){
					List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, " xmbh = '"+xmxxs.get(0).getId()+"' "); 
					if(djdys.size() > 0){
						int djdyidnex = 0;//多单元情况
						int qlindex=0;    //组合流程
						for(BDCS_DJDY_GZ djdy:djdys){
							List<BDCS_QL_GZ> qlgzs = dao.getDataList(BDCS_QL_GZ.class, " djdyid = '"+djdy.getDJDYID()+"' "
									+ " and xmbh = '"+xmxxs.get(0).getId()+"' ");  
							if(qlgzs.size()>0){
								for(BDCS_QL_GZ qlgz : qlgzs){
									qlindex++;
									//开始创建报文
									if("100".equals(qlgz.getDJLX())){
										//首次登记
										if(!"23".equals(qlgz.getQLLX())){
											//执行普遍首次类方法
											
										}else if("23".equals(qlgz.getQLLX())){
											//执行抵押类方法
											maps = exportXmlByDYDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}
									}else if("200".equals(qlgz.getDJLX())){
										//转移登记
										if(!"23".equals(qlgz.getQLLX())){
											//执行普遍转移类方法
											maps = exportXmlByZYDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}else if("23".equals(qlgz.getQLLX())){
											//执行抵押类方法
											maps = exportXmlByDYDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}
									}else if("300".equals(qlgz.getDJLX())){
										//变更登记
										if(!"23".equals(qlgz.getQLLX())){
											//执行普遍变更类方法
											
										}else if("23".equals(qlgz.getQLLX())){
											//执行抵押类方法
											maps = exportXmlByDYDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}
									}else if("400".equals(qlgz.getDJLX())){
										//注销登记
										if(!"23".equals(qlgz.getQLLX())){
											maps = exportXmlByZXDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}else if("23".equals(qlgz.getQLLX())){
											maps = exportXmlByDYZXDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}
									}else if("500".equals(qlgz.getDJLX())){
										//更正登记
									}else if("600".equals(qlgz.getDJLX())){
										//异议登记
									}else if("700".equals(qlgz.getDJLX())){
										//预告登记
										if(!"23".equals(qlgz.getQLLX())){
											//执行普遍预告类方法
											maps = exportXmlByYGDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}else if("23".equals(qlgz.getQLLX())){
											//执行抵押类方法
											maps = exportXmlByDYDJHandler(xmxxs.get(0),djdy,qlgz,actinstID,path,djdyidnex,qlindex);
										}
									}else if("800".equals(qlgz.getDJLX())){
										//查封登记
									}else if("900".equals(qlgz.getDJLX())){
										//其他登记
									}
								}
								djdyidnex++;
							}
						}
					}
				}
			}
		}
		return maps;
	}
	
	/**
	 * 填充数据上报报头
	 * @param mess
	 * @param flag
	 * @param ywh
	 * @param bdcdyh
	 * @param qhdm
	 * @param qlrs 
	 */
	private static void fillHead(Message mess, String ywh, BDCS_XMXX xmxx,String bdcdyh,String qhdm,
			String djlx,String bdcdylx,String qllx,int djdyidnex,String lyqlid,int qlindex) {
		Head head = mess.getHead();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String date = sdf.format(new Date());
		List<ProcedureParam> params=new ArrayList<ProcedureParam>();
		ProcedureParam param=new ProcedureParam();
		param.setFieldtype(Types.INTEGER);
		param.setParamtype("out");
		param.setName("sXH");
		param.setSxh(1);
		params.add(param);
		HashMap<String,Object> info=ProcedureTools.executeProcedure(params, "GETSJSBXH", "BDCK");
		String lsh=StringHelper.formatObject(info.get("sXH"));
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if(!StringHelper.isEmpty(bdcdyh)&&bdcdyh.length()>=6){
			xzqhdm=bdcdyh.substring(0, 6);
		}else if(!StringHelper.isEmpty(qhdm)&&qhdm.length()>=6){
			xzqhdm=qhdm.substring(0, 6);
		}
		String bizMsgId = xzqhdm + date + StringHelper.PadLeft(lsh, 6, '0');
		head.setRecType(getRecType(djlx,bdcdylx,qllx,xmxx.getPROJECT_ID()));
		head.setBizMsgID(bizMsgId);
		head.setASID("AS100");
		head.setAreaCode(xzqhdm);
		head.setRightType(qllx);
		if(QLLX.DIYQ.equals(qllx)){
			head.setRightType(QLLX.QTQL.Value);
		}
		head.setRegType(djlx);
		head.setCreateDate(DateUtil.convertToXMLGregorianCalendar(xmxx.getCREATETIME()));
		if(djdyidnex > 0){// 多个单元情况下，业务号结尾加索引（从1开始）
			head.setRecFlowID(xmxx.getYWLSH()+"-"+djdyidnex);
		}else{
			head.setRecFlowID(xmxx.getYWLSH());
		}
		if(qlindex>0) {
			head.setRecFlowID(xmxx.getYWLSH()+"-"+qlindex);
		}
		
		head.setRegOrgID(ConfigHelper.getNameByValue("DJJGMC"));
		head.setPreCertId("");
		head.setPreEstateNum("");
		List list = countZmAndZs(xmxx.getId());
		head.setProofCount(Integer.parseInt(list.get(0).toString()));
		head.setCertCount(Integer.parseInt(list.get(1).toString()));
		List<BDCS_QL_LS> lsqls = null;
		if(!StringHelper.isEmpty(lyqlid)){
			lsqls = dao.getDataList(BDCS_QL_LS.class, "QLID='"+lyqlid+"'");
			StringBuilder qzhs = new StringBuilder();
			String lyqzh = "";
			String lybdcdyh = "";
			String lybdcdyid = "";
			if(lsqls != null&&lsqls.size()>0){
				for(BDCS_QL_LS lsql:lsqls){
					if(qzhs.indexOf(StringHelper.formatObject(lsql.getBDCQZH())) == -1){
						qzhs.append(StringHelper.formatObject(lsql.getBDCQZH())).append(",");
					}
				}
				lybdcdyh = StringHelper.formatObject(lsqls.get(0).getBDCDYH());
				lybdcdyid = StringHelper.formatObject(lsqls.get(0).getBDCDYID());
			}
			if(qzhs!=null&&!"".equals(qzhs.toString())&&qzhs.length()>0){
				lyqzh = qzhs.substring(0, qzhs.length()-1);
			}else{
				lyqzh = qzhs.toString();
			}
			head.setPreCertId(lyqzh);
			head.setPreEstateNum(lybdcdyh);
			head.setPreBdcdyid(lybdcdyid);
			head.setPreQlid(StringHelper.formatObject(lyqlid));
		}else {
			head.setPreEstateNum("");
			head.setPreBdcdyid("");
			head.setPreQlid("");
			head.setPreCertId("");
			
		}
		
	}
   
	/**
	 * 根据项目编号返回证书数量和证明数量
	 *
	 * @return
	 */
	private static List countZmAndZs(String xmbh) {
		List list = new ArrayList();
		String sql = "select distinct t.bdcqzh,t.djlx,t.qllx from BDCK.BDCS_QL_GZ t,BDCK.bdcs_xmxx t1 "
				+ "where t.xmbh=t1.xmbh and t.xmbh='" + xmbh + "'";
		List<Map> zslist = dao.getDataListByFullSql(sql);
		int zmCount = 0;
		int zsCount = 0;
		if (zslist.size() > 0) {
			String djlx;
			String qllx;
			for (int i = 0; i < zslist.size(); i++) {
				djlx = zslist.get(i).get("DJLX").toString();
				qllx = zslist.get(i).get("QLLX").toString();
				//返回证明数量
				if ((djlx.equals("100") && qllx.equals("23")) || (djlx.equals("200") && qllx.equals("23")) ||
						(djlx.equals("300") && qllx.equals("23")) || (djlx.equals("500") && qllx.equals("23")) ||
						(djlx.equals("700"))) {
					zmCount++;
				}
				//返回证书数量
				if ((djlx.equals("100") && !qllx.equals("23")) || (djlx.equals("200") && !qllx.equals("23")) ||
						(djlx.equals("300") && !qllx.equals("23")) || (djlx.equals("500") && !qllx.equals("23"))) {
					zsCount++;
				}
			}
		}
		list.add(zmCount);
		list.add(zsCount);
		return list;
	}
	
    protected static String getRecType(String djlx,String bdcdylx,String qllx,String project_id){
		String strRecType="";
		if("NYD".equals(bdcdylx)) {
//			农用地
			if (DJLX.CSDJ.Value.equals(djlx)) { // 首次登记
				strRecType=RECCODE.NYD_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(djlx)) { // 转移登记
				strRecType=RECCODE.NYD_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(djlx)) { // 变更登记
				strRecType=RECCODE.NYD_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(djlx)) { // 更正登记
				strRecType=RECCODE.NYD_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(djlx)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(djlx)){  // 补换证登记 或者登记类型为900的其他登记
				//strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
				strRecType=RECCODE.HY_BGDJ.Value;//由于补换证登记没有规定上报的格式,定义成变更
			}
			if (DJLX.YGDJ.Value.equals(djlx)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		
			
		} 
		if (QLLX.GYJSYDSHYQ.Value.equals(qllx)||QLLX.JTJSYDSYQ.Value.equals(qllx)||QLLX.ZJDSYQ.Value.equals(qllx)) { // 建设用地使用权、宅基地使用权
			if (DJLX.CSDJ.Value.equals(djlx)) { // 首次登记
				strRecType=RECCODE.JSYDSHYQ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(djlx)) { // 转移登记
				strRecType=RECCODE.JSYDSHYQ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(djlx)) { // 变更登记
				strRecType=RECCODE.JSYDSHYQ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(djlx)) { // 更正登记
				strRecType=RECCODE.JSYDSHYQ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(djlx)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(djlx)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(djlx)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx) ||
				QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx) ||
				QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx) ) { // 房地产权
			if (DJLX.CSDJ.Value.equals(djlx)) { // 首次登记
				strRecType=RECCODE.FDCQDZ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(djlx)) { // 转移登记
				strRecType=RECCODE.FDCQDZ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(djlx)) { // 变更登记
				strRecType=RECCODE.FDCQDZ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(djlx)) { // 更正登记
				strRecType=RECCODE.FDCQDZ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(djlx)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(djlx)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(djlx)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.JTTDSYQ.Value.equals(qllx)) { // 土地所有权
			if (DJLX.CSDJ.Value.equals(djlx)) { // 首次登记
				strRecType=RECCODE.TDSYQ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(djlx)) { // 转移登记
				strRecType=RECCODE.TDSYQ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(djlx)) { // 变更登记
				strRecType=RECCODE.TDSYQ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(djlx)) { // 更正登记
				strRecType=RECCODE.TDSYQ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(djlx)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(djlx)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(djlx)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.HYSYQ.Value.equals(qllx)) { // 海域(含无居民海岛)使用权
			if (DJLX.CSDJ.Value.equals(djlx)) { // 首次登记
				strRecType=RECCODE.HY_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(djlx)) { // 转移登记
				strRecType=RECCODE.HY_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(djlx)) { // 变更登记
				strRecType=RECCODE.HY_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(djlx)) { // 更正登记
				strRecType=RECCODE.HY_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(djlx)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(djlx)){  // 补换证登记 或者登记类型为900的其他登记
				//strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
				strRecType=RECCODE.HY_BGDJ.Value;//由于补换证登记没有规定上报的格式,定义成变更
			}
			if (DJLX.YGDJ.Value.equals(djlx)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx)) { // 林权
			if (DJLX.CSDJ.Value.equals(djlx)) { // 首次登记
				strRecType=RECCODE.LQ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(djlx)) { // 转移登记
				strRecType=RECCODE.LQ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(djlx)) { // 变更登记
				strRecType=RECCODE.LQ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(djlx)) { // 更正登记
				strRecType=RECCODE.LQ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(djlx)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(djlx)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(djlx)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.DIYQ.Value.equals(qllx)) { // 抵押权
			RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
			boolean flag  = flow.getHandlername().toUpperCase().contains("YGDJHandler".toUpperCase()) || flow.getHandlername().toUpperCase().contains("YDYDJHandler".toUpperCase()) ;
			if(flag){
				strRecType=RECCODE.YG_ZXDJ.Value;
			}else{
				strRecType=RECCODE.DIYQ_ZXDJ.Value;
			}
		}
		if (QLLX.QTQL.Value.equals(qllx)) { // 查封
			strRecType=RECCODE.CF_CFDJ.Value;
		}
		return strRecType;
	}
	
    /**
	 * 上传xml文件到接入系统的sftp上
	 * @author diaoliwei 2016.4.13刘树峰改造
	 * @param localFilePath
	 *  本地xml文件地址
	 */
	public static String uploadFile(String localFilePath, String reccode, String actinstID, String djdykey, String qlid,
			String xmbh) {
		List<BDCS_REPORTINFO> list_report=dao.getDataList(BDCS_REPORTINFO.class, " QLID='"+qlid+"'");
		if(list_report!=null&&list_report.size()>0){
			boolean bHaveReport=false;
			String BizMsgIDHaveReport="";
			for(BDCS_REPORTINFO reportinfo_pre:list_report){
				if("1".equals(reportinfo_pre.getSUCCESSFLAG())||"2".equals(reportinfo_pre.getSUCCESSFLAG())){
					bHaveReport=true;
					BizMsgIDHaveReport=reportinfo_pre.getBIZMSGID();
					break;
				}
			}
			if(bHaveReport){
				return "0|报文"+BizMsgIDHaveReport+"已经上报！不能重复上报！";
			}else{
				for(BDCS_REPORTINFO reportinfo_pre:list_report){
					reportinfo_pre.setYXBZ("0");
					dao.update(reportinfo_pre);
				}
			}
		}
		///数据上报方式
		String UoloadDestination = ConfigHelper.getNameByValue("UoloadDestination");
		boolean transferred=false;
		if("4".equals(UoloadDestination)){
			transferred=true;
		}
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		// xml文件进行加签
		String xmlstr=RsaXmlUtil.rsaXmlFile(localFilePath,transferred);
		// 本地先进行校验
		String CheckXmlInfo = "";
		try {
			//分开捕获异常本地校验不影响上报
			CheckXmlInfo = ValidataXML.validateXMLByXSD(reccode, localFilePath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try{
			///上报报文标识
			String bizmsgid="";
			File reportxml=new File(localFilePath);
			if(reportxml!=null){
				String fileName=reportxml.getName();  
				bizmsgid=fileName.substring(3, fileName.length()-4);
			}
			///本地校验
			boolean bCheckXml=false;
			String reportcheck=ConfigHelper.getNameByValue("ReportCheck");
			if(!"1".equals(reportcheck)||"1".equals(CheckXmlInfo)){
				bCheckXml=true;
			}
			///活动实例信息
			Wfi_ActInst actinst=dao.get(Wfi_ActInst.class, actinstID);
			///用户信息
			String username=Global.getCurrentUserName();
			///数据上报信息
			BDCS_REPORTINFO reportinfo=new BDCS_REPORTINFO();
			reportinfo.setREPORTTIME(new Date());
			reportinfo.setREPORTUSER(username);
			reportinfo.setBIZMSGID(bizmsgid);
			reportinfo.setDJDYID(djdykey);
			reportinfo.setQLID(qlid);
			reportinfo.setPROINSTID(actinst.getProinst_Id());
			reportinfo.setXMBH(xmbh);
			reportinfo.setRECTYPE(reccode);
			reportinfo.setREPORTCONTENT(xmlstr);
			if(bCheckXml){
				reportinfo.setLOCALCHECK("1");
			}else{
				reportinfo.setLOCALCHECK("0");
			}
			reportinfo.setLOCALCHECKINFO(CheckXmlInfo);
			reportinfo.setREPORTTYPE(UoloadDestination);
			reportinfo.setYXBZ("1");
			reportinfo.setRESCOUNT(0);
			reportinfo.setSUCCESSFLAG("2");//0：上报失败；1、上报成功；2、上报成功，待响应;3、上报部里成功，上报省厅失败
			if(!bCheckXml){
				reportinfo.setSUCCESSFLAG("0");
				dao.save(reportinfo);
				return CheckXmlInfo;
			}
			
			///上报数据
			String uploadresult="";
			if("1".equals(UoloadDestination)){
				reportinfo.setREPORTTYPE("1");
				uploadresult = uploadToInspurJointSystem(localFilePath, reccode, actinstID);
				if(!"1".equals(uploadresult)){
					reportinfo.setSUCCESSFLAG("0");
					reportinfo.setRESPONSEINFO(uploadresult);
				}else{
					reportinfo.setSUCCESSFLAG("2");
				}
				dao.save(reportinfo);
				dao.flush();
			}else if ("4".equals(UoloadDestination)) {//WSL服务上报
				reportinfo.setREPORTTYPE("4");
				String result_wsdl = uploadToSupermapJointSystemByWSDL(xmlstr);
				String responsecode="";
				String responseinfo="上报失败！";
				String successflag="0";
				String certid="";
				String qrcode="";
				if("411623".equals(xzqhdm)){
					String resFilePath=localFilePath.replaceAll("Biz"+bizmsgid, "Res"+bizmsgid);
					result_wsdl=RsaXmlUtil.formatXml(resFilePath, result_wsdl);
				}
				if(StringHelper.isEmpty(result_wsdl)||StringHelper.isEmpty(result_wsdl.trim())){
					uploadresult="上报失败！";
				}else{
					Map resultmap=null;
					try {
						resultmap = XmlUtil.xml2map(result_wsdl, false);
					} catch (Exception e) {
					}
					if(resultmap!=null&&resultmap.containsKey("SuccessFlag")){
						String r_successflag=StringHelper.formatObject(resultmap.get("SuccessFlag"));
						if("1".equals(r_successflag)){
							successflag="1";
						}
						if(resultmap.containsKey("ResponseCode")){
							responsecode=StringHelper.formatObject(resultmap.get("ResponseCode"));
						}
						if(resultmap.containsKey("ResponseInfo")){
							responseinfo=StringHelper.formatObject(resultmap.get("ResponseInfo"));
						}
						if(resultmap.containsKey("CertID")){
							certid=StringHelper.formatObject(resultmap.get("CertID"));
						}
						if(resultmap.containsKey("QRCode")){
							qrcode=StringHelper.formatObject(resultmap.get("QRCode"));
						}
					}
					if("1".equals(successflag)){
						uploadresult="1";
					}else{
						if(StringHelper.isEmpty(responseinfo)){
							uploadresult="上报失败";
						}else{
							uploadresult=responseinfo;
						}
					}
				}
				reportinfo.setSUCCESSFLAG(successflag);
				reportinfo.setRESPONSECODE(responsecode);
				reportinfo.setRESPONSEINFO(responseinfo);
				reportinfo.setRESPENSECONTENT(result_wsdl);
				reportinfo.setCERTID(certid);
				reportinfo.setQRCODE(qrcode);
				dao.save(reportinfo);
				dao.flush();
			}else if ("2".equals(UoloadDestination)) {//上报省厅介入系统
				reportinfo.setREPORTTYPE("2");
				String result_super = uploadToSupermapJointSystem(xmlstr);
				JSONObject json_super = null;
				if(!"".equals(result_super)){
					try{
						json_super = JSONObject.fromObject(result_super);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				String status="false";
				String code="-1";
				String message="上报失败！";
				if(json_super!=null){
					if(json_super.containsKey("status")){
						status=StringHelper.formatObject(json_super.get("status"));
					}
					if(json_super.containsKey("code")){
						code=StringHelper.formatObject(json_super.get("code"));
					}
					if(json_super.containsKey("message")){
						message=StringHelper.formatObject(json_super.get("message"));
					}
				}
				reportinfo.setREPORTTYPE(UoloadDestination);//上报超图省厅介入系统
				reportinfo.setRESPENSECONTENT(result_super);
				reportinfo.setRESPONSECODE(code);
				reportinfo.setRESPONSEINFO(message);
				
				if("true".equals(status)&&"2".equals(code)){
					reportinfo.setSUCCESSFLAG("1");
					uploadresult="1";
				}else if("true".equals(status)&&"1".equals(code)){
					reportinfo.setSUCCESSFLAG("2");
					uploadresult="1";
				}else{
					reportinfo.setSUCCESSFLAG("0");
					if(!StringHelper.isEmpty(message)){
						uploadresult=message;
					}else{
						uploadresult="上报失败！";
					}
				}
				dao.save(reportinfo);
				dao.flush();
			}else if ("3".equals(UoloadDestination)) {//先上报部里，上报部里成功后上报省厅
				reportinfo.setREPORTTYPE("31");
				uploadresult = uploadToInspurJointSystem(localFilePath, reccode, actinstID);
				if(!"1".equals(uploadresult)){
					reportinfo.setSUCCESSFLAG("0");
				}else{
					reportinfo.setSUCCESSFLAG("2");
				}
				dao.save(reportinfo);
				dao.flush();
			}
			return uploadresult;
		}catch(Exception e1){
			e1.printStackTrace();
			return "false";
		}
		
	}
    
	/**
	 * sftp连接上报
	 * UoloadDestination code = 1 or 3
	 * @param localFilePath
	 * @param reccode
	 * @param actinstID
	 * @return
	 */
	private static String uploadToInspurJointSystem(String localFilePath, String reccode, String actinstID) {
		try
		{
			dataReport mySFTP = new dataReport();
			ChannelSftp sftp = mySFTP.getSftp();
			if (sftp != null) {
				String path = ConfigHelper.getNameByValue("SFTPBIZMSGPATH");
				sftp.cd(path);
				System.out.println("本地xml地址:" + localFilePath);
				mySFTP.upload(localFilePath, sftp);
				return "1";
			}else{
				return "sftp连接失败！";
			}
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}
	
	/**
	 * WSL服务上报
	 * UoloadDestination code = 4
	 * @param bizmsg
	 * @return
	 */
	private static String uploadToSupermapJointSystemByWSDL(String bizmsg) {
		String sresult = "";
		String username=ConfigHelper.getNameByValue("UserNameReportWSDL");
		String password=ConfigHelper.getNameByValue("PassWordReportWSDL");
		
		String soapaction=ConfigHelper.getNameByValue("SoapActionReportWSDL");
		String targetnamespace=ConfigHelper.getNameByValue("TargetNamespaceReportWSDL");
		String url=ConfigHelper.getNameByValue("UrlReportWSDL");
		String methodname=ConfigHelper.getNameByValue("MethodNameReportWSDL");
		
		List<ParamInfo> params=new ArrayList<ParamInfo>();
		ParamInfo param_user=new ParamInfo();
		param_user.setParamName("username");
		param_user.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_user.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_user.setParamValue(username);
		params.add(param_user);
		
		ParamInfo param_pwd=new ParamInfo();
		param_pwd.setParamName("passwd");
		param_pwd.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_pwd.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_pwd.setParamValue(password);
		params.add(param_pwd);
		
		ParamInfo param_bizmsg=new ParamInfo();
		param_bizmsg.setParamName("BizMsg");
		param_bizmsg.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_bizmsg.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_bizmsg.setParamValue(bizmsg);
		params.add(param_bizmsg);
		
		
		WSDLInfo wsdlinfo=new WSDLInfo();
		wsdlinfo.setMethodName(methodname);
		wsdlinfo.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		wsdlinfo.setSoapAction(soapaction);
		wsdlinfo.setTargetNamespace(targetnamespace);
		wsdlinfo.setUrl(url);
		wsdlinfo.setUser(username);
		wsdlinfo.setPassWord(password);
		Object obj=null;
		try {
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			obj = HttpRequest.sendWSDL(wsdlinfo,params);
		} catch (Exception e) {
			System.out.print("Exception："+e.getMessage());
			e.printStackTrace();
		}
		sresult = StringHelper.formatObject(obj);
		return sresult;
	}
	
	/**
	 * 上报系统客户端上报
	 * UoloadDestination code = 2
	 * @param strxml
	 * @return
	 */
	private static String uploadToSupermapJointSystem(String strxml) {
		String sresult = "";
		String strurl =ConfigHelper.getNameByValue("OurJointClientUrl");// "http://localhost:8085/jointserver/receive/xmlfile";
		HttpURLConnection connet = null;
		try {
			StringBuilder sb=new StringBuilder();
			
			String xml=URLEncoder.encode(URLEncoder.encode(strxml,"utf-8"),"utf-8");
			String content =MessageFormat.format("XMLCONTENT={0}",xml);
			
			URL url = new URL(strurl);
			connet = (HttpURLConnection) url.openConnection();
			connet.setDoInput(true);
			connet.setDoOutput(true);
			connet.setRequestMethod("POST");
			//connet.setConnectTimeout(20 * 1000);// 设置连接超时时间为5秒
			//connet.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
			connet.setRequestProperty("Accept-Charset", "UTF-8");
			connet.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        OutputStream os = connet.getOutputStream();
	        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
	        pw.write(content);
	        pw.close();
			BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream(), "utf-8"));
			String line;
			while ((line = brd.readLine()) != null)
			{
				sb.append(line);
			}
			brd.close();
			sresult=sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sresult;
	}
	
	
	/**
	 * 转移登记的生成报文方法 TODO
	 * 2019年2月26日17:31:56
	 * liangq
	 * @return
	 */
	private static Map<String,String> exportXmlByZYDJHandler(BDCS_XMXX xmxx,BDCS_DJDY_GZ djdy,BDCS_QL_GZ ql,
			String actinstID,String path,int djdyidnex,int qlindex){
		RealUnit unit=null;
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
			//mashaller.setProperty(Marshaller.JAXB_ENCODING, "gbk");
			String ywh = packageXml.GetYWLSHByYWH(xmxx.getPROJECT_ID());
			String result = "";
			String msgName = "";
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, " qlid = '"+ql.getId()+"' ");
			List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
			ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
			sqrs = serviceImpl.getSQRList(xmxx.getId());
			if (QLLX.GYJSYDSHYQ.Value.equals(ql.getQLLX()) || QLLX.ZJDSYQ.Value.equals(ql.getQLLX()) 
					|| QLLX.JTJSYDSYQ.Value.equals(ql.getQLLX())) {
				// 国有建设使用权、宅基地、集体建设用地使用权
				try {
					unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, djdy.getBDCDYID());
					UseLand zd=(UseLand)unit;
					Message msg = exchangeFactory.createMessageBySHYQ();
					fillHead(msg, ywh, xmxx,zd.getBDCDYH(),zd.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
					if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
						msg.getHead().setAreaCode(zd.getQXDM());
					}
					
					if (djdy != null) {
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
						msg.getData().setGYQLR(zttqlr);
						
						KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
						jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);
						msg.getData().setZDBHQK(null);
						
						QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
						syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);
						
						syq.replaceEmpty();
						
//						if(ql!=null){
//							if(!StringHelper.isEmpty(ql.getLYQLID())){
//								BDCS_QL_LS lyql=dao.get(BDCS_QL_LS.class, ql.getLYQLID());
//								if(lyql!=null){
//									msg.getHead().setPreCertId(lyql.getBDCQZH());
//								}
//							}
//						}
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
						
					}
					msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
					mashaller.marshal(msg, new File(path + msgName));
					result = uploadFile(path + msgName,ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,actinstID,
							djdy.getDJDYID(),ql.getId(),xmxx.getId());
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql.getQLLX())||QLLX.JTJSYDSYQ_FWSYQ.Value.equals(ql.getQLLX())
					||QLLX.ZJDSYQ_FWSYQ.Value.equals(ql.getQLLX())) {
				// 房屋所有权
				try {
					BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
					unit=h;
					BDCS_ZRZ_XZ zrz_ = null;
					BDCS_LJZ_XZ ljz_ = null;
					BDCS_SHYQZD_XZ zd = null;
					if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
						zrz_ = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
						if(zrz_ != null){
							zrz_.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
						}
					}
					if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
						ljz_ = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
					}
					BDCS_C_XZ c_ = null;
					if (h != null && !StringUtils.isEmpty(h.getCID())) {
						c_ = dao.get(BDCS_C_XZ.class, h.getCID());
					}
					if(h != null && !StringUtils.isEmpty(h.getZDBDCDYID())){
						zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
						if(zd != null){
							zrz_.setZDDM(zd.getZDDM());
						}
					}
					Message msg = exchangeFactory.createMessageByFWSYQ2();
					fillHead(msg, ywh, xmxx,h.getBDCDYH(),h.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(zrz_.getZDDM());
					msg.getHead().setEstateNum(h.getBDCDYH());
					if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
						msg.getHead().setAreaCode(zd.getQXDM());
					}
					if (djdy != null) {
						
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
						msg.getData().setGYQLR(zttqlr);
						
						KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
						zrz = packageXml.getKTTFWZRZ(zrz, zrz_);
						
						KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
						ljz = packageXml.getKTTFWLJZ(ljz, ljz_,h);
						
						KTTFWC kttc = msg.getData().getKTTFWC();
						kttc = packageXml.getKTTFWC(kttc, c_, zrz);
						msg.getData().setKTTFWC(kttc);
						
						KTTFWH fwh = msg.getData().getKTTFWH();
						fwh = packageXml.getKTTFWH(fwh, h);
						
						QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
						fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
					}
					msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
					mashaller.marshal(msg, new File(path + msgName));
					result = uploadFile(path + msgName,ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,
							actinstID,djdy.getDJDYID(),ql.getId(),xmxx.getId());
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (QLLX.JTTDSYQ.Value.equals(ql.getQLLX())) { // 集体土地所有权
				try {
					BDCS_SYQZD_XZ oland = dao.get(BDCS_SYQZD_XZ.class, djdy.getBDCDYID());
					unit=oland;
					Message msg = exchangeFactory.createMessageByTDSYQ();
					fillHead(msg, ywh, xmxx,oland.getBDCDYH(),oland.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(oland.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
					if(oland != null && !StringUtils.isEmpty(oland.getQXDM())){
						msg.getHead().setAreaCode(oland.getQXDM());
					}
					if (djdy != null) {
						
						KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
						jbxx = packageXml.getZDJBXX(jbxx, null, ql, oland, null);
						
						String zdzhdm = "";
						if (oland != null) {
							zdzhdm = oland.getZDDM();
						}
						KTTGYJZX jzx = msg.getData().getKTTGYJZX();
						jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);
						
						KTTGYJZD jzd = msg.getData().getKTTGYJZD();
						jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);
						
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
						
						KTFZDBHQK bhqk = msg.getData().getZDBHQK();
						bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql, oland, null);
						msg.getData().setZDBHQK(bhqk);
						
						QLFQLTDSYQ tdsyq = msg.getData().getQLFQLTDSYQ();
						tdsyq = packageXml.getQLFQLTDSYQ(tdsyq, oland, ql, ywh);
						
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, oland, null, null);
						msg.getData().setGYQLR(zttqlr);
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), oland, null, null);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(oland, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						List<ZDK103> zdk = msg.getData().getZDK103();
						zdk = packageXml.getZDK103(zdk, null, oland, null);
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, oland.getYSDM(), ywh, oland.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
					}
					msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
					mashaller.marshal(msg, new File(path + msgName));
					result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_CSDJ.Value,actinstID,djdy.getDJDYID(),ql.getId(),xmxx.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(result.equals("")|| result==null){
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
				if(QLLX.JTTDSYQ.Value.equals(ql.getQLLX())){
					YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
				}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql.getQLLX())){
					YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
				}else {
					YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
				}
				return xmlError;
			}
			if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", result);
				return xmlError;
			}
			if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
				names.put("reccode", result);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}
	
	/**
	 * 抵押权上报方法 TODO
	 * @param xmxx
	 * @param djdy
	 * @param ql
	 * @param actinstID
	 * @param path
	 * @return
	 */
	private static Map<String,String> exportXmlByDYDJHandler(BDCS_XMXX xmxx,BDCS_DJDY_GZ djdy,BDCS_QL_GZ ql,
			String actinstID,String path,int djdyidnex,int qlindex){
		RealUnit unit;
		Marshaller mashaller;
		Map<String,String> names = new HashMap<String,String>();
		try {
			mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
			String ywh = packageXml.GetYWLSHByYWH(xmxx.getPROJECT_ID());
			String result = ""; 
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, " qlid = '"+ql.getId()+"'");
			List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, " xmbh = '"+xmxx.getId()+"'");
			BDCS_FSQL_GZ fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			Message msg = exchangeFactory.createMessageByDYQ();
			msg.getHead().setRecType("9000101");
			if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX()) || QLLX.ZJDSYQ.Value.equals(ql.getQLLX())) { //使用权宗地、宅基地使用权
				try {
					BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
					unit=zd;
					fillHead(msg, ywh, xmxx,zd.getBDCDYH(),zd.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
					if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
						msg.getHead().setAreaCode(zd.getQXDM());
					}
					
					if (djdy != null) {
						QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
						dyaq = packageXml.getQLFQLDYAQ(dyaq, zd, ql, fsql, ywh, null);
						
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql,null, null, null, null);
						msg.getData().setGYQLR(zttqlr);
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);		
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			BDCS_H_XZ h = null;
			if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())){ //房屋
				try {
					h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
					unit=h;
					if(h != null){
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
						h.setZDDM(zd.getZDDM());
					}
					fillHead(msg, ywh, xmxx,h.getBDCDYH(),h.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
					
					msg.getHead().setRecType("9000101");
					if(h != null && !StringUtils.isEmpty(h.getQXDM())){
						msg.getHead().setAreaCode(h.getQXDM());
					}
					if (djdy != null) {
						
						QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
						dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, h);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
						msg.getData().setGYQLR(zttqlr);
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			BDCS_H_XZY xzy = null;
			if(BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())){ //在建工程
				try {
					xzy = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
					unit=xzy;
					if(xzy != null){
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, xzy.getZDBDCDYID());
						xzy.setZDDM(zd.getZDDM());
					}
					fillHead(msg, ywh, xmxx,xzy.getBDCDYH(),xzy.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(xzy.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(xzy.getBDCDYH()));
					if(xzy != null && !StringUtils.isEmpty(xzy.getQXDM())){
						msg.getHead().setAreaCode(xzy.getQXDM());
					}
					if (djdy != null) {
						
						QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
						dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, xzy);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(xzy, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, xzy, null, null, null);
						msg.getData().setGYQLR(zttqlr);
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, xzy, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, xzy.getYSDM(), ywh, xzy.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if("HY".equals(djdy.getBDCDYLX())){ //海域
				try {
					String zhdm=null;
					String hql=null;
					YHZK yhzk_gz = null;
					Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
					unit=zh;
					if (zh==null) {
						zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
					}
					if (null != zh) {
						zhdm=zh.getZHDM();
						hql = "BDCDYID = '" + zh.getId() + "' ";
						List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
						if (yhzks != null && yhzks.size() > 0) {
							yhzk_gz = yhzks.get(0);
						}else {
							List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
							if (yhzksxz != null && yhzksxz.size() > 0) {
								yhzk_gz = yhzksxz.get(0);
							}
						}
					}
					if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
						//维护数据
						yhzk_gz.setZHDM(zhdm);
					}
					// 这些字段先手动赋值 diaoliwei
					if (zh != null) {
						if (StringUtils.isEmpty(zh.getZHT())) {
							zh.setZHT("无");
						}
					}
					//设置报文头
					fillHead(msg, ywh, xmxx,zh.getBDCDYH(),zh.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
					if (djdy != null) {
						
						QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
						dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, xzy);
						//获取不动产抵押单元类型
						if((dyaq.getDybdclx()==null||dyaq.getDybdclx().length()<=0)&&fsql!=null&&fsql.getDYWLX()!=null&&fsql.getDYWLX().length()>0) {
							dyaq.setDybdclx(fsql.getDYWLX());
						}
						//维护区县代码
						if((dyaq.getQxdm()==null||dyaq.getQxdm().length()<=0)&&ql!=null&&ql.getQXDM()!=null&&ql.getQXDM().length()>0) {
							dyaq.setQxdm(ql.getQXDM());
						}
						msg.getData().setQLFQLDYAQ(dyaq);
						
						//3.非结构化文档
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
						msg.getData().setFJF100(fj);
						
						//5.宗海变化状况表(可选 )
						KTFZHYHZK yhzk = msg.getData().getKTFZHYHZK();
						if(yhzk!=null) {
							yhzk = packageXml.getKTFZHYHZK(yhzk, yhzk_gz, zh.getBDCDYH());
							msg.getData().setKTFZHYHZK(yhzk);
						}
						//6.权力人表
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, null, null, zh);
						msg.getData().setGYQLR(zttqlr);
						
						//7.登记受理信息表
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
						msg.getData().setDJSLSQ(sq);
						
						//8.登记收件(可选)
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
			result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId(),xmxx.getId());
			names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
			
			if(result.equals("")|| result==null){
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
				YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.DIYQ_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
				return xmlError;
			}
			if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", result);
				return xmlError;
			}
			if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
				names.put("reccode", result);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}
	
	
	
	/**
	 * 预告登记报文上报
	 * TODO
	 */
	private static Map<String,String> exportXmlByYGDJHandler(BDCS_XMXX xmxx,BDCS_DJDY_GZ djdy,BDCS_QL_GZ ql,
			String actinstID,String path,int djdyidnex,int qlindex ){
		Marshaller mashaller;
		RealUnit unit;
		Map<String, String> names = new HashMap<String,String>();
		try {
			mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
			Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
			calendar.setTime(new Date()); // 用当前时间初始化日历时间
			String result = "";
			String ywh = packageXml.GetYWLSHByYWH(xmxx.getPROJECT_ID());
			BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
			if(ql != null && !StringUtils.isEmpty(ql.getFSQLID())){
				fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			}
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, " qlid = '"+ql.getId()+"' ");
			List<BDCS_SQR> sqrs =  dao.getDataList(BDCS_SQR.class, " xmbh = '"+xmxx.getId()+"' ");
			
			Message msg = exchangeFactory.createMessageByYGDJ();
			msg.getHead().setRecType("7000101");
			House h = null;
			if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())||BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())){ //房屋所有权
				try {
					h = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
					unit=h;
					if(h==null) {
						h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
					} 
					if(h != null){
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
						if(zd != null){
							h.setZDDM(zd.getZDDM());
						}
					}
					fillHead(msg, ywh, xmxx,h.getBDCDYH(),h.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
					//兼容转移预告登记,强制设置
					msg.getHead().setRecType("7000101");
					if(h != null && !StringUtils.isEmpty(h.getQXDM())){
						msg.getHead().setAreaCode(h.getQXDM());
					}
					
					if (djdy != null) {
						QLFQLYGDJ ygdj = msg.getData().getQLFQLYGDJ();
						ygdj = packageXml.getQLFQLYGDJ(ygdj, h, ql,fsql, ywh);
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
						
						List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
						zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
						msg.getData().setGYQLR(zttqlr);
						
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
			result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml",
					ConstValue.RECCODE.YG_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId(),xmxx.getId());
			names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
			if(result.equals("")|| result==null){
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
				YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.YG_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
				return xmlError;
			}
			if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", result);
				return xmlError;
			}
			if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
				names.put("reccode", result);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}
	
	

	/**
	 * 抵押注销数据上报
	 * TODO
	 * 2019年2月27日15
	 * liangq
	 * @param xmxx
	 * @param djdy
	 * @param ql
	 * @param actinstID
	 * @param path
	 * @return
	 */
	private static Map<String,String> exportXmlByDYZXDJHandler(BDCS_XMXX xmxx,BDCS_DJDY_GZ djdy,BDCS_QL_GZ ql,
			String actinstID,String path,int djdyidnex,int qlindex ){
		Marshaller mashaller;
		Map<String,String> names = new HashMap<String,String>();
		try {
			String result = "";
			String xmbhHql = ProjectHelper.GetXMBHCondition(xmxx.getId());
			String condition =xmbhHql+" and QLLX='23'";
			List<Rights> rights=RightsTools.loadRightsByCondition(DJDYLY.GZ, condition);
			List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
			ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
			sqrs = serviceImpl.getSQRList(xmxx.getId());
			if(!StringHelper.isEmpty(rights) && rights.size()>0){
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				BDCS_XMXX bdcs_xmxx=Global.getXMXXbyXMBH(xmxx.getId());
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String ywh =packageXml.GetYWLSHByYWH(xmxx.getPROJECT_ID());
				Message msg=exchangeFactory.createMessageByDYQ();//注销登记
				RealUnit unit=null;
				BDCS_DJDY_GZ bdcs_djdy_gz=null;
				for(Rights right :rights){
					SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, right.getFSQLID());
					String djdysql = xmbhHql+ "AND DJDYID='"+ right.getDJDYID()+"'";
					List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, djdysql);
					if(!StringHelper.isEmpty(djdys) && djdys.size()>0){
						bdcs_djdy_gz= djdys.get(0);
						BDCDYLX bdcdylx=BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
						DJDYLY djdylx=DJDYLY.initFrom(bdcs_djdy_gz.getLY());
						String bdcdyid=bdcs_djdy_gz.getBDCDYID();
						if(!StringHelper.isEmpty(bdcdyid) &&!StringHelper.isEmpty(bdcdylx) &&!StringHelper.isEmpty(djdylx)){
							unit=UnitTools.loadUnit(bdcdylx, djdylx, bdcdyid);	
						}
					}					
					//ZTT_GY_QLR 权利人(完成)
					List<ZTTGYQLR> qlrs =msg.getData().getGYQLR();
					List<RightsHolder> holders=RightsHolderTools.loadRightsHolders(DJDYLY.LS , right.getLYQLID());
					qlrs=packageXml.getZTTGYQLRsEx(qlrs, holders, right.getBDCQZH(),right.getBDCDYH(), unit);
					
					//QLF_QL_DYAQ 抵押权
					QLFQLDYAQ dyaq=msg.getData().getQLFQLDYAQ();
					dyaq=packageXml.getQLFQLDYAQEx(dyaq, right, subrights, ywh, unit);
					dyaq.setDyjelx("1");
					msg.getData().setQLFQLDYAQ(dyaq);
					if(!StringHelper.isEmpty(right.getYWH())){
						String scywh=packageXml.GetYWLSHByYWH(right.getYWH());//获取上次业务号
						msg.getData().getQLFQLDYAQ().setScywh(scywh);
					}
					
					//DJT_DJ_SLSQ 登记受理申请信息
					DJTDJSLSQ sq = msg.getData().getDJSLSQ();
					sq = packageXml.getDJTDJSLSQEx(sq, ywh, right, bdcs_xmxx,unit);
					sq.setSQFBCZ(1);
					msg.getData().setDJSLSQ(sq);
					
					//DJF_DJ_SJ 登记收件信息
					List<DJFDJSJ> sj = msg.getData().getDJSJ();
					sj = packageXml.getDJFDJSJEx(new DJFDJSJ(), ywh, actinstID, unit);
					msg.getData().setDJSJ(sj);
					
					//DJF_DJ_SF 登记收费信息
					List<DJFDJSF> sfList = msg.getData().getDJSF();
					sfList = packageXml.getDJSFEx(new DJFDJSF(), ywh, xmxx.getId(), unit);
					msg.getData().setDJSF(sfList);
					

					//10.审核(可选)
					List<DJFDJSH> sh = msg.getData().getDJSH();
					 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
					msg.getData().setDJSH(sh);

					//11.缮证(可选)
					List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
					msg.getData().setDJSZ(sz);

					//11.发证(可选)
					List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
					msg.getData().setDJFZ(fz);
					
					//12.归档(可选)
					List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
					msg.getData().setDJGD(gd);	
					
					//DJF_DJ_SQR	申请人属性信息
					String zddm="";
					String ysdm="";
					if(unit instanceof House){
						House h=(House) unit;
						zddm=h.getZDDM();
						ysdm=h.getYSDM();
					}
					if(unit instanceof Sea){
						Sea h=(Sea) unit;
						zddm=h.getZHDM();
						ysdm="6001020000";
					}
					
					else if( unit instanceof OwnerLand){
						OwnerLand l=(OwnerLand) unit;
						zddm=l.getZDDM();
						ysdm=l.getYSDM();
					}
					else if(unit instanceof UseLand){
						UseLand l=(UseLand) unit;
						zddm=l.getZDDM();
						ysdm=l.getYSDM();
					}					
					List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
					djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,ysdm, ywh, unit.getBDCDYH());
					msg.getData().setDJSQR(djsqrs);
					
					//FJ_F_100	非结构化文档
					FJF100 fj = msg.getData().getFJF100();
					fj = packageXml.getFJF(fj);
					msg.getData().setFJF100(fj);
					fillHead(msg, ywh, xmxx,unit.getBDCDYH(),unit.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(zddm));
					msg.getHead().setEstateNum(StringHelper.formatObject(unit.getBDCDYH()));
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,bdcs_djdy_gz.getDJDYID(),right.getId(),xmxx.getId());
					names.put(bdcs_djdy_gz.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
					if(result.equals("")|| result==null){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						return xmlError;
					}
					if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
						names.put("reccode", result);
					}
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}
	
	
	/**
	 * 注销数据上报
	 * TODO 缺少99qllx的数据获取方式
	 * 2019年2月27日15
	 * liangq
	 * @param xmxx
	 * @param djdy
	 * @param ql
	 * @param actinstID
	 * @param path
	 * @return
	 */
	private static Map<String,String> exportXmlByZXDJHandler(BDCS_XMXX xmxx,BDCS_DJDY_GZ djdy,BDCS_QL_GZ ql,
			String actinstID,String path,int djdyidnex,int qlindex){
		Marshaller mashaller;
		RealUnit unit=null;
		Map<String, String> names = new HashMap<String, String>();
		try {
			mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
			Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
			calendar.setTime(new Date()); // 用当前时间初始化日历时间
			String result = "";
			String ywh = packageXml.GetYWLSHByYWH(xmxx.getPROJECT_ID());
			List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
			Message msg = exchangeFactory.createMessageByZXDJ();
			ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
			sqrs = serviceImpl.getSQRList(xmxx.getId());
			BDCS_H_XZ h = null;
			if (djdy != null) {
				msg.getHead().setRecType("4000101");
				if (QLLX.GYJSYDSHYQ.Value.equals(ql.getQLLX()) || QLLX.ZJDSYQ.Value.equals(ql.getQLLX()) || QLLX.JTJSYDSYQ.Value.equals(ql.getQLLX())) { // 国有建设使用权、宅基地、集体建设用地使用权
					try {
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
						unit =zd;
						if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
							msg.getHead().setAreaCode(zd.getQXDM());
						}
						fillHead(msg, ywh, xmxx,zd.getBDCDYH(),zd.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
						Rights rights = ql;
						QLFZXDJ zxdj = msg.getData().getZXDJ();
						SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
						zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zd.getQXDM(),subrights);
						
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql.getQLLX())) { // 房屋所有权
					try {
						h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
						unit=h;
						if(h != null && h.getZDBDCDYID() != null){
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
							h.setZDDM(zd.getZDDM());
						}
						if(h != null && !StringUtils.isEmpty(h.getQXDM())){
							msg.getHead().setAreaCode(h.getQXDM());
						}
						fillHead(msg, ywh, xmxx,h.getBDCDYH(),h.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
						Rights rights = ql;
						QLFZXDJ zxdj = msg.getData().getZXDJ();
						SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
						zxdj = packageXml.getZXDJ(zxdj, rights, ywh, h.getQXDM(),subrights);
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (QLLX.HYSYQ.Value.equals(ql.getQLLX()) || QLLX.WJMHDSYQ.Value.equals(ql.getQLLX())) { 
					// 海域(含无居民海岛)使用权注销
					// 海域(含无居民海岛)使用权
					String zhdm=null;
					String hql=null;
					YHZK yhzk_gz = null;
					Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
					if (zh==null) {
						zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
					}
					unit=zh;
					if (null != zh) {
						zhdm=zh.getZHDM();
						hql = "BDCDYID = '" + zh.getId() + "' ";
						List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
						if (yhzks != null && yhzks.size() > 0) {
							yhzk_gz = yhzks.get(0);
						}else {
							List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
							if (yhzksxz != null && yhzksxz.size() > 0) {
								yhzk_gz = yhzksxz.get(0);
							}
						}
					}
					if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
						//维护数据
						yhzk_gz.setZHDM(zhdm);
					}
					// 这些字段先手动赋值 diaoliwei
					if (zh != null) {
						if (StringUtils.isEmpty(zh.getZHT())) {
							zh.setZHT("无");
						}
					}
					//设置报文头
					fillHead(msg, ywh, xmxx,zh.getBDCDYH(),zh.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
					msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
					//设置报文信息
//								//1.宗海基本信息系
//								KTTZHJBXX jbxx = msg.getData().getKTTZHJBXX();
//								jbxx = packageXml.getKTTZHJBXX(jbxx, ql, zh);//组装成xml格式
//								msg.getData().setKTTZHJBXX(jbxx);
//								
					//2.非结构化文档
					FJF100 fj = msg.getData().getFJF100();
					fj = packageXml.getFJF(fj);
					msg.getData().setFJF100(fj);
//								//3.宗海变化状况
//								KTFZHBHQK bhqk = msg.getData().getKTFZHBHQK();
//								bhqk = packageXml.getKTFZHBHQK(bhqk, ql, zh);
//								msg.getData().setKTFZHBHQK(bhqk);
//
//								//4.宗海使用权
//								QLFQLHYSYQ syq = msg.getData().getQLFQLHYSYQ();
//								syq = packageXml.getQLFQLHYSYQ(syq, ql, zh, ywh);
//								msg.getData().setQLFQLHYSYQ(syq);
//
//								//5.宗海变化状况表(可选 )
//								KTFZHYHZK yhzk = msg.getData().getKTFZHYHZK();
//								yhzk = packageXml.getKTFZHYHZK(yhzk, yhzk_gz, zh.getQXDM());
//								msg.getData().setKTFZHYHZK(yhzk);
					
//								//6.用海,用岛坐标
//								KTFZHYHYDZB zb = msg.getData().getKTFZHYHYDZB();
//								zb = packageXml.getKTFZHYHYDZB(zb, zh);
//								msg.getData().setKTFZHYHYDZB(zb);
					
					
					//7.登记受理信息表
					DJTDJSLSQ sq = msg.getData().getDJSLSQ();
					sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
					msg.getData().setDJSLSQ(sq);
					
					//8.登记收件(可选)
					List<DJFDJSJ> sj = msg.getData().getDJSJ();
					sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
					msg.getData().setDJSJ(sj);
					
					//9.登记收费(可选)
					List<DJFDJSF> sfList = msg.getData().getDJSF();
					sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
					msg.getData().setDJSF(sfList);

					//10.审核(可选)
					List<DJFDJSH> sh = msg.getData().getDJSH();
					 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
					msg.getData().setDJSH(sh);

					//11.缮证(可选)
					List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
					msg.getData().setDJSZ(sz);

					//11.发证(可选)
					List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
					msg.getData().setDJFZ(fz);
					
					//12.归档(可选)
					List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
					msg.getData().setDJGD(gd);	
					
					//13.申请人属性
					List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
					djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
					msg.getData().setDJSQR(djsqrs);
					//注销登记
					Rights rights = ql;
					QLFZXDJ zxdj = msg.getData().getZXDJ();
					SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
					zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zh.getQXDM(),subrights);
					msg.getData().setZXDJ(zxdj);
				}
				
				if (QLLX.QTQL.Value.equals(ql.getQLLX())) { 
					//其他权利获取方式，一般只有现房才有为 400 - 99
					 // 房屋所有权
					try {
						h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
						unit=h;
						if(h != null && h.getZDBDCDYID() != null){
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
							h.setZDDM(zd.getZDDM());
						}
						if(h != null && !StringUtils.isEmpty(h.getQXDM())){
							msg.getHead().setAreaCode(h.getQXDM());
						}
						fillHead(msg, ywh, xmxx,h.getBDCDYH(),h.getQXDM(),ql.getDJLX(),djdy.getBDCDYLX(),ql.getQLLX(),djdyidnex,ql.getLYQLID(),qlindex);
						Rights rights = ql;
						QLFZXDJ zxdj = msg.getData().getZXDJ();
						SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
						zxdj = packageXml.getZXDJ(zxdj, rights, ywh, h.getQXDM(),subrights);
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
						
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
						msg.getData().setDJSJ(sj);
						
						//9.登记收费(可选)
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(unit, ywh,xmxx.getId());
						msg.getData().setDJSF(sfList);

						//10.审核(可选)
						List<DJFDJSH> sh = msg.getData().getDJSH();
						 sh = packageXml.getDJFDJSH(unit, ywh, xmxx.getId(), actinstID);
						msg.getData().setDJSH(sh);

						//11.缮证(可选)
						List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, xmxx.getId());
						msg.getData().setDJSZ(sz);

						//11.发证(可选)
						List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, xmxx.getId());
						msg.getData().setDJFZ(fz);
						
						//12.归档(可选)
						List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, xmxx.getId());
						msg.getData().setDJGD(gd);	
						msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
						msg.getData().setDJSQR(djsqrs);
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
//			mashaller.marshal(msg, new File(path +msgName));
			mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));			
			result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml", ConstValue.RECCODE.ZX_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql.getId(),xmxx.getId());
			names.put(djdy.getDJDYID(), path + "Biz" + msg.getHead().getBizMsgID() + ".xml");
			if(null == result){
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
				YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
				return xmlError;
			}
			if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
				Map<String, String> xmlError = new HashMap<String, String>();
				xmlError.put("error", result);
				return xmlError;
			}
			if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
				names.put("reccode", result);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}

	
	
	
}

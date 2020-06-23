package com.supermap.realestate_gx.registration.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_NYD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SLLM_LS;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_ZH_LS;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.realestate_gx.registration.service.GX_RealestateSearchService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("realestateSearchService")
public class GX_RealestateSearchServicelmpl implements GX_RealestateSearchService{

	@Autowired
	private CommonDao baseCommonDao;
	
	/**
	 * （不动产信息查档）读取Excle模版中的内容并返回页面显示
	 * 
	 */
	public Map<String,Object>  GetQueryRealestateList(String file_path){
		Map<String,Object> maps = new HashMap<String,Object>();
		File tempFile = new File(file_path);
		try{
			List<Map> bdcInfoLists = new ArrayList<Map>();
			FileInputStream is = new FileInputStream(tempFile); //文件流  
			Workbook wb = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的 
			Sheet sheet = wb.getSheetAt(0);//获取Excle模版的第一个sheet
			int num = wb.getNumberOfSheets();
			int rowCount = sheet.getLastRowNum(); //获取第一个sheet有效行数 
			//查询当前记录的最大查档编号
			for (int r = 1; r < rowCount+1; r++) {
				Map<String,String> m = new HashMap<String, String>();
				Row rw = sheet.getRow(r);
				Cell cl0 = rw.getCell(0); //第1列的值 ---编号
				Cell cl1 = rw.getCell(1);//第2列 ---查档人
				Cell cl2 = rw.getCell(2);//第3列的值 ---权利人
				Cell cl3 = rw.getCell(3);//第4列的值 ---证件号码
				
				if(!StringHelper.isEmpty(cl0)){
					cl0.setCellType(Cell.CELL_TYPE_STRING);
					String bh = cl0.getStringCellValue();
					//去掉序号的小数点
					if (bh.indexOf(".") != -1) {
						bh = bh.substring(0, bh.indexOf("."));
					}
					m.put("BH", bh);
				}
				if(!StringHelper.isEmpty(cl1)){
					String cdr = cl1.getStringCellValue();
					m.put("CDR", cdr);
				}
				if(!StringHelper.isEmpty(cl2)){
					String qlrmc = cl2.getStringCellValue();
					m.put("QLRMC", qlrmc);
				}
				if(!StringHelper.isEmpty(cl3)){
					String zjhm = cl3.getStringCellValue();
					m.put("ZJHM", zjhm);
				}
				m.put("FWXX", "");
				m.put("ZDXX", "");
				m.put("HYXX", "");
				m.put("LDXX", "");
				m.put("NYDXX", "");
				bdcInfoLists.add(m);//返回页面显示Excle表的集合
			}
			maps.put("bdcInfoLists", bdcInfoLists);
		}catch(Exception e){
			e.printStackTrace();
		}
		return maps;
	}
	/**
	 * 
	 * 获取查档编号
	 * huangpeifeng
	 * 20170926	
	 * @param qlrmc 权利人名称
	 * @param zjh 证号号
	 * @param cdmd 查档目的
	 * @return
	 */
	public String getCdbh(String qlrmc,String zjh,String cdmd){
		String cdbh = "";
		String fullsql = "SELECT Max(cdbh) as CDBH FROM SMWB_SUPPORT.GX_CONFIG";
		List<Map> cdbhs = baseCommonDao.getDataListByFullSql(fullsql); 	
		if(cdbhs != null && cdbhs.size() >0){
			String max_cdbh = (String) cdbhs.get(0).get("CDBH");
			int curr_maxcdbh = Integer.parseInt(max_cdbh);
			int new_cdbh = curr_maxcdbh +1;
			if(new_cdbh > 1 && new_cdbh < 10)
				cdbh = "0000" + new_cdbh;
			if(new_cdbh > 9 && new_cdbh < 100)
				cdbh = "000" + new_cdbh;
			if(new_cdbh > 99 && new_cdbh < 1000)
				cdbh = "00" + new_cdbh;
			if(new_cdbh > 999 && new_cdbh < 10000)
				cdbh = "0" + new_cdbh;
			if(new_cdbh > 9999 && new_cdbh < 100000)
				cdbh = new_cdbh + "";
			
			GX_CONFIG  gx_config = new GX_CONFIG();
			Date currdate = new Date();
			SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			gx_config.setCdbh(cdbh);
			gx_config.setCdmd(cdmd);
			gx_config.setCdrmc(qlrmc);
			gx_config.setCdrzjh(zjh);		
			String cd_datestring  = formatter.format(currdate);  
			gx_config.setCdtime(cd_datestring);
			
			baseCommonDao.save(gx_config);
		}
		baseCommonDao.flush();
		return cdbh;
	}
	/**
	 * 将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 */
	public Message  getMatchData(String sfall, String sfhouse, String sfqhouse,String sfshyqzd,String sfsyqzd, String sfsea, String sfld,String sfnyd, List<Map> queryVauleList){
					
		Message message = new Message();
		
		
		message = getRealestateData(sfall, sfhouse, sfqhouse,sfshyqzd,sfsyqzd, sfsea, sfld,sfnyd,queryVauleList);
		
		return message;
		
	}

	/**
	 *  * 
	 *  
	 * @time 2018-4-23 17:08:30 
	 * @author liangc 
	 * @param sfall 查询全部不动产：0是，1不是。
	 * @param sfhouse 查询现房：0是，1不是。
	 * @param sfqhouse 查询期房：0是，1不是。
	 * @param sfshyqzd 查询使用权宗地：0是，1不是。
	 * @param sfsyqzd 查询所有权宗地：0是，1不是。
	 * @param sfsea 查询海域：0是，1不是。
	 * @param sfld 查询林地：0是，1不是。
	 * @param sfnyd 查询农用地：0是，1不是。
	 * @param queryVauleList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Message getRealestateData(String sfall, String sfhouse, String sfqhouse,String sfshyqzd,String sfsyqzd, String sfsea, String sfld,String sfnyd, List<Map> queryVauleList){		
		Message message = new Message();
		List<Map> toExcleLists = new ArrayList<Map>();	
		StringBuilder dyselectsql = new StringBuilder();
		StringBuilder dyfromsql = new StringBuilder();
		StringBuilder dywheresql = new StringBuilder();
		StringBuilder dywherehsql = new StringBuilder();
		String qlrzjh_18 = "";
		String qlrzjh_15 = "";
		int total = 0;
		for(Map<String, String> qv : queryVauleList){
			//for循环中移除上次数据
			dyselectsql.delete(0, dyselectsql.length());
			dyfromsql.delete(0, dyfromsql.length());
			dywheresql.delete(0, dywheresql.length());
			dywherehsql.delete(0, dywherehsql.length());
			List<Map> results = new ArrayList<Map>();
			String qlrmc = "";
			if(StringHelper.isEmpty(qv.get("ZJHM"))){
				Map<String,String> map = new HashMap<String, String>();
				map.put("BH", qv.get("BH"));
				map.put("QLRMC", qv.get("QLRMC"));
				map.put("ZJHM", "");
				map.put("BZ", "信息不完整（缺少身份证号）");
				map.put("FWXX", "无");
				map.put("ZDXX", "无");
				map.put("HYXX", "无");
				map.put("LDXX", "无");
				map.put("NYDXX", "无");
				total += 1;
				toExcleLists.add(map);
				continue;
			}
			String cdr = "";//查档人
			if(StringHelper.isEmpty(qv.get("CDR"))){
				cdr = qv.get("QLRMC");
			}else{
				cdr = qv.get("CDR");
			}
			String zjh = qv.get("ZJHM");
			qlrmc = StringHelper.formatObject(qv.get("QLRMC"));
			boolean SFSFZH = false;
			if(!StringHelper.isEmpty(zjh)){
				String[] zjhs = zjh.split("，");
				if(zjhs.length>0){
					zjh = zjhs[0];
				}
				zjh = zjh.trim().toUpperCase(); 
				if(ConverIdCard.checkIDCard(zjh)){
					if(zjh.length()==18){
						qlrzjh_18 = zjh;
						qlrzjh_15 = ConverIdCard.getOldIDCard(zjh);			
					}
					if(zjh.length()==15){
						qlrzjh_15 = zjh;
						qlrzjh_18 = ConverIdCard.getNewIDCard(zjh);		
					}
				}else{
					qlrzjh_18 = qlrzjh_15 = zjh;
				}
			}
			dyselectsql.append("SELECT DJDY.DJDYID,DJDY.BDCDYID,DJDY.BDCDYLX,QLR.QLRMC,QLR.BDCQZH,QLR.ZJH,QLR.GYFS,QL.DJLX,QL.QLLX,QL.DJSJ,QL.QLID,QLR.QLRID,QL.FJ ");
			dyfromsql.append(" FROM(SELECT TRIM(QLRMC) AS QLRMC,TRIM(ZJH) AS ZJH,BDCQZH,GYFS,QLID,QLRID FROM BDCK.BDCS_QLR_XZ) QLR");
			dyfromsql.append(" LEFT JOIN BDCK.BDCS_QL_XZ QL ON QLR.QLID=QL.QLID LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID");
			dyfromsql.append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID ");
			dywheresql.append(" WHERE QLR.QLRMC='");
			dywheresql.append(qlrmc);
			dywheresql.append("' ");
			//dywheresql.append(" AND DJDY.BDCDYLX IN('01','02','04','05','09','031','032')");
			dywheresql.append(" AND (QLR.ZJH='");
			dywheresql.append(qlrzjh_18);
			dywheresql.append("' OR QLR.ZJH='");
			dywheresql.append(qlrzjh_15);
			dywheresql.append("')");
			
			dywheresql.append(" and ql.qllx not in('23','98')");
			dywheresql.append(" and ql.djlx not in('600','800')");
			dywherehsql.append(" ORDER BY DJDY.BDCDYLX");
			if(sfall.equals("1")){
				dywheresql.append(" AND DJDY.BDCDYLX IN('01','02','04','05','09','031','032')");
			}else{
				if(sfsyqzd.equals("1")){
					sfsyqzd="01";
				}
				if(sfshyqzd.equals("1")){
					sfshyqzd="02";
				}
				if(sfhouse.equals("1")){
					sfhouse="031";
				}
				if(sfqhouse.equals("1")){
					sfqhouse="032";
				}
				if(sfsea.equals("1")){
					sfsea="04";
				}
				if(sfld.equals("1")){
					sfld="05";
				}
				if(sfnyd.equals("1")){
					sfnyd="09";
				}
				dywheresql.append(" AND DJDY.BDCDYLX IN('"+sfhouse+"','"+sfqhouse+"','"+sfshyqzd+"','"+sfsyqzd+"','"+sfsea+"','"+sfld+"','"+sfnyd+"')");	
			}
			String fulsql = dyselectsql.toString()+dyfromsql.toString()+dywheresql.toString()+dywherehsql.toString();
			List<Map> listDJDY_XZ =  baseCommonDao.getDataListByFullSql(fulsql);
			String cdbh = getCdbh(qlrmc, zjh, "有无不动产");// 获取查档编号
			if(listDJDY_XZ != null && listDJDY_XZ.size()>0){
				for(int i=0; i<listDJDY_XZ.size(); i++){
					String strBdcdylx = StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"));
					if(strBdcdylx.length()<=0){
						continue; 
					}								
					Map<String,String> map = new HashMap<String, String>();
					map.put("CDBH", cdbh);// 查档编号
					map.put("BH", qv.get("BH"));// 编号
					map.put("QLRMC", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLRMC")));
					map.put("ZJHM", StringHelper.formatObject(listDJDY_XZ.get(i).get("ZJH")));
					BDCDYLX bdcdylx = BDCDYLX.initFrom(strBdcdylx);
						if(BDCDYLX.H.equals(bdcdylx)){
							House house = (House)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYID")));
							if (house != null) {
								String mj = "";
								String djsj = StringHelper.FormatByDatetime(listDJDY_XZ.get(i).get("DJSJ"));// 登记时间
								String gyfs = StringHelper.formatObject(listDJDY_XZ.get(i).get("GYFS"));//共有方式
								String gyrmc = "";
								if (!"0".equals(gyfs)) {//除单独所有外
									String qlid = StringHelper.formatObject(listDJDY_XZ.get(i).get("QLID"));
									String qlrid = StringHelper.formatObject(listDJDY_XZ.get(i).get("QLRID"));
									gyrmc = getGYRMC(qlid, qlrid);
								}
								long countdyzt = 0;
								long countcfzt = 0;
								long countyyzt = 0;
								long countxzzt = 0;
								countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								countxzzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								if(countdyzt>0){
									map.put("XFDYZT", "该现房已抵押");
								}else{
									map.put("XFDYZT", "该现房未抵押");
								}
								if(countcfzt>0){
									map.put("XFCFZT", "该现房已查封");
								}else{
									map.put("XFCFZT", "该现房未查封");
								}
								if(countyyzt>0){
									map.put("XFYYZT", "该现房有异议");
								}else{
									map.put("XFYYZT", "该现房无异议");
								}
								if(countxzzt>0) {
									map.put("XFXZZT", "该现房未限制");
								}else {
									map.put("XFXZZT", "该现房未限制");
								}
								List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
								REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+house.getId()+"'");
								if(REA.size()<1){
									map.put("QXFGLZT", "未关联期房");
								}else{
									map.put("QXFGLZT", "已关联期房");
									List<BDCS_DJDY_XZ> YCH = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getYCBDCDYID()+"'");
									if(YCH!=null&&YCH.size()>0){
										countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND BDCDYID='"+YCH.get(0).getBDCDYID()+"'");
										countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND BDCDYID='"+YCH.get(0).getBDCDYID()+"'");
										countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND BDCDYID='"+YCH.get(0).getBDCDYID()+"'");
										countxzzt  = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+YCH.get(0).getBDCDYID()+"'");
										if(countdyzt>0){
											map.put("XFDYZT", map.get("XFDYZT")+"、期房已抵押");
										}else{
											map.put("XFDYZT", map.get("XFDYZT")+"、期房未抵押");
										}
										if(countcfzt>0){
											map.put("XFCFZT", map.get("XFCFZT")+"、期房已查封");
										}else{
											map.put("XFCFZT", map.get("XFCFZT")+"、期房未查封");
										}
										if(countyyzt>0){
											map.put("XFYYZT", map.get("XFYYZT")+"、期房有异议");
										}else{
											map.put("XFYYZT", map.get("XFYYZT")+"、期房无异议");
										}
										if(countxzzt>0){
											map.put("XFXZZT", map.get("XFXZZT")+"、期房有限制");
										}else{
											map.put("XFXZZT", map.get("XFXZZT")+"、期房无限制");
										}
									}
								}
								if(!StringHelper.isEmpty(house.getZDBDCDYID())){
									countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND BDCDYID='"+house.getZDBDCDYID()+"'");
									countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND BDCDYID='"+house.getZDBDCDYID()+"'");
									countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND BDCDYID='"+house.getZDBDCDYID()+"'");
									countxzzt  = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+house.getZDBDCDYID()+"'");
									if(countdyzt>0){
										map.put("XFDYZT", map.get("XFDYZT")+"、关联宗地已抵押");
									}else{
										map.put("XFDYZT", map.get("XFDYZT")+"、关联宗地未抵押");
									}
									if(countcfzt>0){
										map.put("XFCFZT", map.get("XFCFZT")+"、关联宗地已查封");
									}else{
										map.put("XFCFZT", map.get("XFCFZT")+"、关联宗地未查封");
									}
									if(countyyzt>0){
										map.put("XFYYZT", map.get("XFYYZT")+"、关联宗地有异议");
									}else{
										map.put("XFYYZT", map.get("XFYYZT")+"、关联宗地无异议");
									}
									if(countxzzt>0){
										map.put("XFXZZT", map.get("XFXZZT")+"、关联宗地有限制");
									}else{
										map.put("XFXZZT", map.get("XFXZZT")+"、关联宗地无限制");
									}
								}
								if (!"0".equals(String.valueOf(house.getMJ())))
									mj = String.valueOf(house.getMJ());
									if (!StringHelper.isEmpty((String) listDJDY_XZ.get(i).get("BDCQZH"))) {
										map.put("HBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
										map.put("HZL", house.getZL());
										map.put("HMJ", mj);
										map.put("HDJSJ", djsj);
										map.put("HSFQCQ", ConstHelper.getNameByValue("GYFS", gyfs)); // 共有方式
										map.put("HGHYTname", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));// 房屋用途
										map.put("HDJLX",ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("DJLX"))));
										map.put("HQLLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLLX")));
										map.put("HFWXZ", ConstHelper.getNameByValue("FWXZ", house.getFWXZ()));//房屋性质
										map.put("HFJ", StringHelper.formatObject(listDJDY_XZ.get(i).get("FJ")));
										map.put("HGYRMC", gyrmc);
										map.put("HBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"))));
										map.put("HBDCDYH", house.getBDCDYH());
									}
									if (DJLX.YGDJ.Value.equals(listDJDY_XZ.get(i).get("DJLX").toString())&&QLLX.QTQL.Value.equals(listDJDY_XZ.get(i).get("QLLX").toString())) {
										map.put("HYGBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
									}
									map.put("FWXX", "现有");
							}
						}else if(BDCDYLX.YCH.equals(bdcdylx)){
							House house = (House)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYID")));
							if (house != null) {
								String mj = "";
								String djsj = StringHelper.FormatByDatetime(listDJDY_XZ.get(i).get("DJSJ"));// 登记时间
								String gyfs = StringHelper.formatObject(listDJDY_XZ.get(i).get("GYFS"));//共有方式
								String gyrmc = "";
								if (!"0".equals(gyfs)) {//除单独所有外
									String qlid = StringHelper.formatObject(listDJDY_XZ.get(i).get("QLID"));
									String qlrid = StringHelper.formatObject(listDJDY_XZ.get(i).get("QLRID"));
									gyrmc = getGYRMC(qlid, qlrid);
								}
								long countdyzt = 0;
								long countcfzt = 0;
								long countyyzt = 0;
								long countxzzt = 0;
								countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countxzzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								if(countdyzt>0){
									map.put("QFDYZT", "该期房已抵押");
								}else{
									map.put("QFDYZT", "该期房未抵押");
								}
								if(countcfzt>0){
									map.put("QFCFZT", "该期房已查封");
								}else{
									map.put("QFCFZT", "该期房未查封");
								}
								if(countyyzt>0){
									map.put("QFYYZT", "该期房有异议");
								}else{
									map.put("QFYYZT", "该期房无异议");
								}
								if(countxzzt>0) {
									map.put("QFXZZT", "该期房未限制");
								}else {
									map.put("QFXZZT", "该期房未限制");
								}
								List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
								REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+house.getId()+"'");
								if(REA.size()<1){
									map.put("QXFGLZT", "未关联现房");
								}else{
									map.put("QXFGLZT", "已关联现房");
									List<BDCS_DJDY_XZ> SCH = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getSCBDCDYID()+"'");
									if(SCH!=null&&SCH.size()>0){
										countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+SCH.get(0).getDJDYID()+"'");
										countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+SCH.get(0).getDJDYID()+"'");
										countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND DJDYID='"+SCH.get(0).getDJDYID()+"'");
										countxzzt  = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+SCH.get(0).getBDCDYID()+"'");
										if(countdyzt>0){
											map.put("QFDYZT", map.get("QFDYZT")+"、现房已抵押");
										}else{
											map.put("QFDYZT", map.get("QFDYZT")+"、现房未抵押");
										}
										if(countcfzt>0){
											map.put("QFCFZT", map.get("QFCFZT")+"、现房已查封");
										}else{
											map.put("QFCFZT", map.get("QFCFZT")+"、现房未查封");
										}
										if(countyyzt>0){
											map.put("QFYYZT", map.get("QFYYZT")+"、现房有异议");
										}else{
											map.put("QFYYZT", map.get("QFYYZT")+"、现房无异议");
										}
										if(countxzzt>0){
											map.put("QFXZZT", map.get("QFXZZT")+"、现房有限制");
										}else{
											map.put("QFXZZT", map.get("QFXZZT")+"、现房无限制");
										}
									}
								}
								if(!StringHelper.isEmpty(house.getZDBDCDYID())){
									countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND BDCDYID='"+house.getZDBDCDYID()+"'");
									countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND BDCDYID='"+house.getZDBDCDYID()+"'");
									countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND BDCDYID='"+house.getZDBDCDYID()+"'");
									countxzzt  = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+house.getZDBDCDYID()+"'");
									if(countdyzt>0){
										map.put("QFDYZT", map.get("QFDYZT")+"、关联宗地已抵押");
									}else{
										map.put("QFDYZT", map.get("QFDYZT")+"、关联宗地未抵押");
									}
									if(countcfzt>0){
										map.put("QFCFZT", map.get("QFCFZT")+"、关联宗地已查封");
									}else{
										map.put("QFCFZT", map.get("QFCFZT")+"、关联宗地未查封");
									}
									if(countyyzt>0){
										map.put("QFYYZT", map.get("QFYYZT")+"、关联宗地有异议");
									}else{
										map.put("QFYYZT", map.get("QFYYZT")+"、关联宗地无异议");
									}
									if(countxzzt>0){
										map.put("QFXZZT", map.get("QFXZZT")+"、关联宗地有限制");
									}else{
										map.put("QFXZZT", map.get("QFXZZT")+"、关联宗地无限制");
									}
								}
								if (!"0".equals(String.valueOf(house.getMJ())))
									mj = String.valueOf(house.getMJ());
								
									if (!StringHelper.isEmpty((String) listDJDY_XZ.get(i).get("BDCQZH"))) {// 
										map.put("YCHBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
										map.put("YCHZL", house.getZL());
										map.put("YCHMJ", mj);
										map.put("YCHDJSJ", djsj);
										map.put("YCHSFQCQ", ConstHelper.getNameByValue("GYFS", gyfs)); // 共有方式
										map.put("YCHGHYTname", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));// 房屋用途
										map.put("YCHDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("DJLX"))));
										map.put("YCHQLLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLLX")));
										map.put("YCHFWXZ", ConstHelper.getNameByValue("FWXZ", house.getFWXZ()));//房屋性质
										map.put("YCHFJ", StringHelper.formatObject(listDJDY_XZ.get(i).get("FJ")));
										map.put("YCHGYRMC", gyrmc);
										map.put("YCHBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"))));
										map.put("YCHBDCDYH", house.getBDCDYH());
									}
									if (DJLX.YGDJ.Value.equals(listDJDY_XZ.get(i).get("DJLX").toString())) {// sfyg为1是查期房产权
											map.put("YCHYGBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
									}
									map.put("FWXX", "现有");
							}
						}else if(BDCDYLX.SYQZD.equals(bdcdylx)){
							
						}else if(BDCDYLX.SHYQZD.equals(bdcdylx)){
							UseLand useland = (UseLand)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYID")));
							if(useland != null){
								long countdyzt = 0;
								long countcfzt = 0;
								long countyyzt = 0;
								long countxzzt = 0;
								countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countxzzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								if(countdyzt>0){
									map.put("SHYQZDDYZT", "该宗地已抵押");
								}else{
									map.put("SHYQZDDYZT", "该宗地未抵押");
								}
								if(countcfzt>0){
									map.put("SHYQZDCFZT", "该宗地已查封");
								}else{
									map.put("SHYQZDCFZT", "该宗地未查封");
								}
								if(countyyzt>0){
									map.put("SHYQZDYYZT", "该宗地有异议");
								}else{
									map.put("SHYQZDYYZT", "该宗地无异议");
								}
								if(countxzzt>0) {
									map.put("SHYQZDXZZT", "该宗地未限制");
								}else {
									map.put("SHYQZDXZZT", "该宗地未限制");
								}
								String mj = "";
								if (!"0".equals(String.valueOf(useland.getMJ())))
									mj = String.valueOf(useland.getMJ());
								map.put("SHYQZDBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
								map.put("SHYQZDZL", useland.getZL());
								map.put("SHYQZDMJ", mj);
								map.put("SHYQZDDJSJ", StringHelper.FormatByDatetime(listDJDY_XZ.get(i).get("DJSJ")));
								map.put("SHYQZDDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("DJLX"))));
								map.put("SHYQZDQLLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLLX")));
								map.put("SHYQZDBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"))));
								map.put("SHYQZDBDCDYH", useland.getBDCDYH());
								map.put("ZDXX", "现有");
							}
						}else if(BDCDYLX.HY.equals(bdcdylx)){
							Sea sea = (Sea)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYID")));
							if(sea != null){
								long countdyzt = 0;
								long countcfzt = 0;
								long countyyzt = 0;
								long countxzzt = 0;
								countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countxzzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								if(countdyzt>0){
									map.put("HYDYZT", "该宗海已抵押");
								}else{
									map.put("HYDYZT", "该宗海未抵押");
								}
								if(countcfzt>0){
									map.put("HYCFZT", "该宗海已查封");
								}else{
									map.put("HYCFZT", "该宗海未查封");
								}
								if(countyyzt>0){
									map.put("HYYYZT", "该宗海有异议");
								}else{
									map.put("HYYYZT", "该宗海无异议");
								}
								if(countxzzt>0) {
									map.put("HYXZZT", "该宗海未限制");
								}else {
									map.put("HYXZZT", "该宗海未限制");
								}
								String mj = "";
								if (!"0".equals(String.valueOf(sea.getMJ())))
									mj = String.valueOf(sea.getMJ());
								map.put("HYBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
								map.put("HYZL", sea.getZL());
								map.put("HYMJ", mj);
								map.put("HYDJSJ", StringHelper.FormatByDatetime(listDJDY_XZ.get(i).get("DJSJ")));
								map.put("HYDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("DJLX"))));
								map.put("HYQLLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLLX")));
								map.put("HYBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"))));
								map.put("HYBDCDYH", sea.getBDCDYH());
								map.put("HYXX", "现有");
							}
						}else if(BDCDYLX.LD.equals(bdcdylx)){
							Forest ld = (Forest)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYID")));
							if(ld != null){
								long countdyzt = 0;
								long countcfzt = 0;
								long countyyzt = 0;
								long countxzzt = 0;
								countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countxzzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								if(countdyzt>0){
									map.put("LDDYZT", "该林地已抵押");
								}else{
									map.put("LDDYZT", "该林地未抵押");
								}
								if(countcfzt>0){
									map.put("LDCFZT", "该林地已查封");
								}else{
									map.put("LDCFZT", "该林地未查封");
								}
								if(countyyzt>0){
									map.put("LDYYZT", "该林地有异议");
								}else{
									map.put("LDYYZT", "该林地无异议");
								}
								if(countxzzt>0) {
									map.put("LDXZZT", "该林地未限制");
								}else {
									map.put("LDXZZT", "该林地未限制");
								}
								String mj = "";
								if (!"0".equals(String.valueOf(ld.getMJ())))
									mj = String.valueOf(ld.getMJ());
								map.put("LDBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
								map.put("LDZL", ld.getZL());
								map.put("LDMJ", mj);
								map.put("LDDJSJ", StringHelper.FormatByDatetime(listDJDY_XZ.get(i).get("DJSJ")));
								map.put("LDDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("DJLX"))));
								map.put("LDQLLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLLX")));
								map.put("LDBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"))));
								map.put("LDBDCDYH", ld.getBDCDYH());
								map.put("LDXX", "现有");
							}
						}else if(BDCDYLX.NYD.equals(bdcdylx)){
							AgriculturalLand nyd = (AgriculturalLand)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYID")));
							if(nyd != null){
								long countdyzt = 0;
								long countcfzt = 0;
								long countyyzt = 0;
								long countxzzt = 0;
								countdyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countcfzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countyyzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='600' AND DJDYID='"+listDJDY_XZ.get(i).get("DJDYID")+"'");
								countxzzt = baseCommonDao.getCountByFullSql(" from BDCK.BDCS_DYXZ WHERE BDCDYID='"+listDJDY_XZ.get(i).get("BDCDYID")+"'");
								if(countdyzt>0){
									map.put("NYDDYZT", "该农用地已抵押");
								}else{
									map.put("NYDDYZT", "该农用地未抵押");
								}
								if(countcfzt>0){
									map.put("NYDCFZT", "该农用地已查封");
								}else{
									map.put("NYDCFZT", "该农用地未查封");
								}
								if(countyyzt>0){
									map.put("NYDYYZT", "该农用地有异议");
								}else{
									map.put("NYDYYZT", "该农用地无异议");
								}
								if(countxzzt>0) {
									map.put("NYDXZZT", "该农用地未限制");
								}else {
									map.put("NYDXZZT", "该农用地未限制");
								}
								String mj = "";
								if (!"0".equals(String.valueOf(nyd.getMJ())))
									mj = String.valueOf(nyd.getMJ());
								map.put("NYDBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
								map.put("NYDZL", nyd.getZL());
								map.put("NYDMJ", mj);
								map.put("NYDDJSJ", StringHelper.FormatByDatetime(listDJDY_XZ.get(i).get("DJSJ")));
								map.put("NYDDJLX",ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("DJLX"))));
								map.put("NYDQLLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLLX")));
								map.put("NYDBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"))));
								map.put("NYDBDCDYH", nyd.getBDCDYH());
								map.put("NYDXX", "现有");
							}
						}
						results.add(map);
					}
				}
					String fulsqlls = fulsql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
					List<Map> listDJDY_LS =  baseCommonDao.getDataListByFullSql(fulsqlls); //获取历史权利的单元信息
					if(listDJDY_LS != null && listDJDY_LS.size()>0){
						for(int i=0; i<listDJDY_LS.size(); i++){
							String strBdcdylx = StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYLX"));
							if(strBdcdylx.length()<=0){
								continue; 
							}
							for(Map<String,String> mp : results){
								Map<String,String> map = new HashMap<String, String>();
								if(!mp.containsKey("CDBH"))
								map.put("CDBH", cdbh);// 查档编号
								if(!mp.containsKey("BH"))
								map.put("BH", qv.get("BH"));// 编号
								if(!mp.containsKey("QLRMC"))
								map.put("QLRMC", StringHelper.formatObject(listDJDY_LS.get(i).get("QLRMC")));
								if(!mp.containsKey("ZJHM"))
								map.put("ZJHM", StringHelper.formatObject(listDJDY_LS.get(i).get("ZJH")));
								if(!map.isEmpty())
								results.add(map);
								BDCDYLX bdcdylx = BDCDYLX.initFrom(strBdcdylx);
								if((bdcdylx.equals("H")||bdcdylx.equals("YCH"))&&!mp.containsKey("FWXX")){
									if(bdcdylx.equals("H")&&!mp.containsKey("FWXX")){
										House house = (House)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYID")));
										if (house != null) {
											String mj = "";
											String djsj = StringHelper.FormatByDatetime(listDJDY_LS.get(i).get("DJSJ"));// 登记时间
											String gyfs = StringHelper.formatObject(listDJDY_LS.get(i).get("GYFS"));//共有方式
											String gyrmc = "";
											if (!"0".equals(gyfs)) {//除单独所有外
												String qlid = StringHelper.formatObject(listDJDY_LS.get(i).get("QLID"));
												String qlrid = StringHelper.formatObject(listDJDY_LS.get(i).get("QLRID"));
												gyrmc = getGYRMC(qlid, qlrid);
											}
											if (!"0".equals(String.valueOf(house.getMJ())))
												mj = String.valueOf(house.getMJ());
											
												if (!StringHelper.isEmpty((String) listDJDY_LS.get(i).get("BDCQZH"))) {
													map.put("HBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
													map.put("HZL", house.getZL());
													map.put("HMJ", mj);
													map.put("HDJSJ", djsj);
													map.put("HSFQCQ", ConstHelper.getNameByValue("GYFS", gyfs)); // 共有方式
													map.put("HGHYTname", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));// 房屋用途
													map.put("HDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_LS.get(i).get("DJLX"))));
													map.put("HQLLX", StringHelper.formatObject(listDJDY_LS.get(i).get("QLLX")));
													map.put("HFWXZ", ConstHelper.getNameByValue("FWXZ", house.getFWXZ()));//房屋性质
													map.put("HFJ", StringHelper.formatObject(listDJDY_LS.get(i).get("FJ")));
													map.put("HGYRMC", gyrmc);
													map.put("HBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYLX"))));
													map.put("HBDCDYH", house.getBDCDYH());
												}
												if (DJLX.YGDJ.Value.equals(listDJDY_LS.get(i).get("DJLX").toString())&&QLLX.QTQL.Value.equals(listDJDY_LS.get(i).get("QLLX").toString())) {
													map.put("HYGBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
												}
												map.put("FWXX", "原有");
										}
									}else if(bdcdylx.equals("YCH")&&!mp.containsKey("FWXX")){
										House house = (House)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYID")));
										if (house != null) {
											String mj = "";
											String djsj = StringHelper.FormatByDatetime(listDJDY_LS.get(i).get("DJSJ"));// 登记时间
											String gyfs = StringHelper.formatObject(listDJDY_LS.get(i).get("GYFS"));//共有方式
											String gyrmc = "";
											if (!"0".equals(gyfs)) {//除单独所有外
												String qlid = StringHelper.formatObject(listDJDY_LS.get(i).get("QLID"));
												String qlrid = StringHelper.formatObject(listDJDY_LS.get(i).get("QLRID"));
												gyrmc = getGYRMC(qlid, qlrid);
											}
											if (!"0".equals(String.valueOf(house.getMJ())))
												mj = String.valueOf(house.getMJ());
											
												if (!StringHelper.isEmpty((String) listDJDY_LS.get(i).get("BDCQZH"))) {// 
													map.put("YCHBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
													map.put("YCHZL", house.getZL());
													map.put("YCHMJ", mj);
													map.put("YCHDJSJ", djsj);
													map.put("YCHSFQCQ", ConstHelper.getNameByValue("GYFS", gyfs)); // 共有方式
													map.put("YCHGHYTname", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));// 房屋用途
													map.put("YCHDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_LS.get(i).get("DJLX"))));
													map.put("YCHQLLX", StringHelper.formatObject(listDJDY_LS.get(i).get("QLLX")));
													map.put("YCHFWXZ", ConstHelper.getNameByValue("FWXZ", house.getFWXZ()));//房屋性质
													map.put("YCHFJ", StringHelper.formatObject(listDJDY_LS.get(i).get("FJ")));
													map.put("YCHGYRMC", gyrmc);
													map.put("YCHBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYLX"))));
													map.put("YCHBDCDYH", house.getBDCDYH());
												}
												if (DJLX.YGDJ.Value.equals(listDJDY_LS.get(i).get("DJLX").toString())) {// sfyg为1是查期房产权
														map.put("YCHYGBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
												}
												map.put("FWXX", "原有");
										}
									}else if((bdcdylx.equals("SYQZD")||bdcdylx.equals("SHYQZD"))&&!mp.containsKey("ZDXX")){
										if(bdcdylx.equals("SYQZD")&&!mp.containsKey("ZDXX")){
											
										}else if(bdcdylx.equals("SHYQZD")&&!mp.containsKey("ZDXX")){
											UseLand useland = (UseLand)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYID")));
											if(useland != null){
												String mj = "";
												if (!"0".equals(String.valueOf(useland.getMJ())))
													mj = String.valueOf(useland.getMJ());
												map.put("SHYQZDBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
												map.put("SHYQZDZL", useland.getZL());
												map.put("SHYQZDMJ", mj);
												map.put("SHYQZDDJSJ", StringHelper.FormatByDatetime(listDJDY_LS.get(i).get("DJSJ")));
												map.put("SHYQZDDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_LS.get(i).get("DJLX"))));
												map.put("SHYQZDQLLX", StringHelper.formatObject(listDJDY_LS.get(i).get("QLLX")));
												map.put("SHYQZDBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYLX"))));
												map.put("SHYQZDBDCDYH", useland.getBDCDYH());
												map.put("ZDXX", "原有");
											}
										}
									}else if(bdcdylx.equals("HY")&&!mp.containsKey("HYXX")){
										Sea sea = (Sea)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYID")));
										if(sea != null){
											String mj = "";
											if (!"0".equals(String.valueOf(sea.getMJ())))
												mj = String.valueOf(sea.getMJ());
											map.put("HYBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
											map.put("HYZL", sea.getZL());
											map.put("HYMJ", mj);
											map.put("HYDJSJ", StringHelper.FormatByDatetime(listDJDY_LS.get(i).get("DJSJ")));
											map.put("HYDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_LS.get(i).get("DJLX"))));
											map.put("HYQLLX", StringHelper.formatObject(listDJDY_LS.get(i).get("QLLX")));
											map.put("HYBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYLX"))));
											map.put("HYBDCDYH", sea.getBDCDYH());
											map.put("HYXX", "原有");
										}
									}else if(bdcdylx.equals("LD")&&!mp.containsKey("LDXX")){
										Forest ld = (Forest)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYID")));
										if(ld != null){
											String mj = "";
											if (!"0".equals(String.valueOf(ld.getMJ())))
												mj = String.valueOf(ld.getMJ());
											map.put("LDBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
											map.put("LDZL", ld.getZL());
											map.put("LDMJ", mj);
											map.put("LDDJSJ", StringHelper.FormatByDatetime(listDJDY_LS.get(i).get("DJSJ")));
											map.put("LDDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_LS.get(i).get("DJLX"))));
											map.put("LDQLLX", StringHelper.formatObject(listDJDY_LS.get(i).get("QLLX")));
											map.put("LDBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYLX"))));
											map.put("LDBDCDYH", ld.getBDCDYH());
											map.put("LDXX", "原有");
										}
									}else if(bdcdylx.equals("NYD")&&!mp.containsKey("NYDXX")){
										AgriculturalLand nyd = (AgriculturalLand)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYID")));
										if(nyd != null){
											String mj = "";
											if (!"0".equals(String.valueOf(nyd.getMJ())))
												mj = String.valueOf(nyd.getMJ());
											map.put("NYDBDCQZH", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCQZH")));
											map.put("NYDZL", nyd.getZL());
											map.put("NYDMJ", mj);
											map.put("NYDDJSJ", StringHelper.FormatByDatetime(listDJDY_LS.get(i).get("DJSJ")));
											map.put("NYDDJLX", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(listDJDY_LS.get(i).get("DJLX"))));
											map.put("NYDQLLX", StringHelper.formatObject(listDJDY_LS.get(i).get("QLLX")));
											map.put("NYDBDCDYLX", ConstHelper.getNameByValue("BDCDYLX", StringHelper.formatObject(listDJDY_LS.get(i).get("BDCDYLX"))));
											map.put("NYDBDCDYH", nyd.getBDCDYH());
											map.put("NYDXX", "原有");
										}
									}
								results.add(map);
							}								
						}
					}
				}
				if(results == null || results.size()<=0){//查不到现状单元，就显示原需查档的人员信息
					Map<String,String> map2 = new HashMap<String, String>();
					//String cdbh = getCdbh(qlrmc,zjh,"有无房产");
					map2.put("CDBH", cdbh);
					map2.put("BH", qv.get("BH"));
					map2.put("QLRMC",qlrmc);
					map2.put("ZJHM",qv.get("ZJH"));
					map2.put("FWXX", "无");
					map2.put("ZDXX", "无");
					map2.put("HYXX", "无");
					map2.put("LDXX", "无");
					map2.put("NYDXX", "无");						
					results.add(map2);
				}												
				
										
			total += results.size();					
			for(Map<String,String> mp : results){
				if(SFSFZH)//判断证件号是否是身份证号，1是，0否。
					mp.put("SFSFZH", "1");
				else
					mp.put("SFSFZH", "0");
				mp.put("CDR", cdr);// 查档人
				if(!mp.containsKey("FWXX")){
					mp.put("FWXX", "无");
				}
				if(!mp.containsKey("ZDXX")){
					mp.put("ZDXX", "无");
				}
				if(!mp.containsKey("HYXX")){
					mp.put("HYXX", "无");
				}
				if(!mp.containsKey("LDXX")){
					mp.put("LDXX", "无");
				}
				if(!mp.containsKey("NYDXX")){
					mp.put("NYDXX", "无");
				}
				toExcleLists.add(mp);
			}
	}		
		baseCommonDao.flush();
		message.setTotal(total);
		message.setRows(toExcleLists);

		return message;
	}
	
	
	
	
	/**
	 * 返回不动产查档证明内容
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Message getBDCPLPrintZM(List<Map> results, String jbr){
		Message msg = new Message();
		List<Object> searchResults = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		if(results != null &&results.size() > 0){
			for(Map map : results){
				
				String qlrmc = StringHelper.formatObject(map.get("QLRMC"));// 权利人名称
				String qlrzjh = StringHelper.formatObject(map.get("ZJHM"));// 权利人证件号
				String bh = StringHelper.formatObject(map.get("BH"));// 编号
				
				//房屋信息
				String fwxx = StringHelper.formatObject(map.get("FWXX"));// 是否有房屋
				//实测户
				String hbdcqzh = StringHelper.formatObject(map.get("HBDCQZH"));// 不动产权证号
				String hzl = StringHelper.formatObject(map.get("HZL"));// 坐落
				String hmj = StringHelper.formatObject(map.get("HMJ"));// 房屋面积
				String hdjsj = StringHelper.formatObject(map.get("HDJSJ"));//登记时间
				String hsfqcq = StringHelper.formatObject(map.get("HSFQCQ"));//共有方式
				String hghytname = StringHelper.formatObject(map.get("HGHYTname"));//房屋用途
				String hdjlx = StringHelper.formatObject(map.get("HDJLX"));// 权利类型
				String hqllx = StringHelper.formatObject(map.get("HQLLX"));// 登记类型
				String hfwxz = StringHelper.formatObject(map.get("HFWXZ"));// 房屋性质
				String hfj = StringHelper.formatObject(map.get("HFJ"));// 附记
				String hgyrmc = StringHelper.formatObject(map.get("HGYRMC"));// 共有人名称
				String hbdcdylx = StringHelper.formatObject(map.get("HBDCDYLX"));// 不动产单元类型
				String hbdcdyh = StringHelper.formatObject(map.get("HBDCDYH"));// 不动产单元号
				String hygbdcqzh = StringHelper.formatObject(map.get("HYGBDCQZH"));// 预告不动产权证号
				String qxfglzt = StringHelper.formatObject(map.get("QXFGLZT"));
				String xfdyzt = StringHelper.formatObject(map.get("XFDYZT"));
				String xfcfzt = StringHelper.formatObject(map.get("XFCFZT"));
				String xfyyzt = StringHelper.formatObject(map.get("XFYYZT"));
				String xfxzzt = StringHelper.formatObject(map.get("XFXZZT"));
				
				//预测户
				String ychbdcqzh = StringHelper.formatObject(map.get("YCHBDCQZH"));// 不动产权证号
				String ychzl = StringHelper.formatObject(map.get("YCHZL"));// 坐落
				String ychmj = StringHelper.formatObject(map.get("YCHMJ"));// 预测房屋面积
				String ychdjsj = StringHelper.formatObject(map.get("YCHDJSJ"));//登记时间
				String ychsfqcq = StringHelper.formatObject(map.get("YCHSFQCQ"));//共有方式
				String ychghytname = StringHelper.formatObject(map.get("YCHGHYTname"));//房屋用途
				String ychdjlx = StringHelper.formatObject(map.get("YCHDJLX"));// 登记类型
				String ychqllx = StringHelper.formatObject(map.get("YCHQLLX"));// 登记类型
				String ychfwxz = StringHelper.formatObject(map.get("YCHFWXZ"));// 房屋性质
				String ychfj = StringHelper.formatObject(map.get("YCHFJ"));// 附记
				String ychgyrmc = StringHelper.formatObject(map.get("YCHGYRMC"));// 共有人名称
				String ychbdcdylx = StringHelper.formatObject(map.get("YCHBDCDYLX"));// 不动产单元类型
				String ychbdcdyh = StringHelper.formatObject(map.get("YCHBDCDYH"));// 不动产单元号
				String ychygbdcqzh = StringHelper.formatObject(map.get("YCHYGBDCQZH"));// 预告不动产权证号
				String qfdyzt = StringHelper.formatObject(map.get("QFDYZT"));
				String qfcfzt = StringHelper.formatObject(map.get("QFCFZT"));
				String qfyyzt = StringHelper.formatObject(map.get("QFYYZT"));
				String qfxzzt = StringHelper.formatObject(map.get("QFXZZT"));
				
				//宗地信息
				String zdxx = StringHelper.formatObject(map.get("ZDXX"));// 是否有宗地
				//使用权宗地
				String shyqzdbdcqzh = StringHelper.formatObject(map.get("SHYQZDBDCQZH"));// 不动产权证号
				String shyqzdzl = StringHelper.formatObject(map.get("SHYQZDZL"));// 坐落
				String shyqzdmj = StringHelper.formatObject(map.get("SHYQZDMJ"));// 宗地的面积
				String shyqzddjsj = StringHelper.formatObject(map.get("SHYQZDDJSJ"));//登记时间
				String shyqzddjlx = StringHelper.formatObject(map.get("SHYQZDDJLX"));// 登记类型
				String shyqzdqllx = StringHelper.formatObject(map.get("SHYQZDQLLX"));// 权利类型
				String shyqzdbdcdylx = StringHelper.formatObject(map.get("SHYQZDBDCDYLX"));// 不动产单元类型
				String shyqzdbdcdyh = StringHelper.formatObject(map.get("SHYQZDBDCDYH"));// 不动产单元号
				String shyqzddyzt = StringHelper.formatObject(map.get("SHYQZDDYZT"));
				String shyqzdcfzt = StringHelper.formatObject(map.get("SHYQZDCFZT"));
				String shyqzdyyzt = StringHelper.formatObject(map.get("SHYQZDYYZT"));
				String shyqzdxzzt = StringHelper.formatObject(map.get("SHYQZDXZZT"));
				
				//海域信息
				String hyxx = StringHelper.formatObject(map.get("ZDXX"));// 是否有海域
				String hybdcqzh = StringHelper.formatObject(map.get("HYBDCQZH"));// 不动产权证号
				String hyzl = StringHelper.formatObject(map.get("HYZL"));// 坐落
				String hymj = StringHelper.formatObject(map.get("HYMJ"));// 海域面积
				String hydjsj = StringHelper.formatObject(map.get("HYDJSJ"));//登记时间
				String hydjlx = StringHelper.formatObject(map.get("HYDJLX"));// 登记类型
				String hyqllx = StringHelper.formatObject(map.get("HYQLLX"));// 权利类型
				String hybdcdylx = StringHelper.formatObject(map.get("HYBDCDYLX"));// 不动产单元类型
				String hybdcdyh = StringHelper.formatObject(map.get("HYBDCDYH"));// 不动产单元号
				String hydyzt = StringHelper.formatObject(map.get("HYDYZT"));
				String hycfzt = StringHelper.formatObject(map.get("HYCFZT"));
				String hyyyzt = StringHelper.formatObject(map.get("HYYYZT"));
				String hyxzzt = StringHelper.formatObject(map.get("HYXZZT"));
				
				//林地信息
				String ldxx = StringHelper.formatObject(map.get("LDXX"));// 是否有林地
				String ldbdcqzh = StringHelper.formatObject(map.get("LDBDCQZH"));// 不动产权证号
				String ldzl = StringHelper.formatObject(map.get("LDZL"));// 坐落
				String ldmj = StringHelper.formatObject(map.get("LDMJ"));// 林地面积
				String lddjsj = StringHelper.formatObject(map.get("LDDJSJ"));//登记时间
				String lddjlx = StringHelper.formatObject(map.get("LDDJLX"));// 登记类型
				String ldqllx = StringHelper.formatObject(map.get("LDQLLX"));// 权利类型
				String ldbdcdylx = StringHelper.formatObject(map.get("LDBDCDYLX"));// 不动产单元类型
				String ldbdcdyh = StringHelper.formatObject(map.get("LDBDCDYH"));// 不动产单元号
				String lddyzt = StringHelper.formatObject(map.get("LDDYZT"));
				String ldcfzt = StringHelper.formatObject(map.get("LDCFZT"));
				String ldyyzt = StringHelper.formatObject(map.get("LDYYZT"));
				String ldxzzt = StringHelper.formatObject(map.get("LDXZZT"));
				
				//农用地信息
				String nydxx = StringHelper.formatObject(map.get("LDXX"));// 是否有农用地
				String nydbdcqzh = StringHelper.formatObject(map.get("LDBDCQZH"));// 不动产权证号
				String nydzl = StringHelper.formatObject(map.get("LDZL"));// 坐落
				String nydmj = StringHelper.formatObject(map.get("LDMJ"));// 农用地面积
				String nyddjsj = StringHelper.formatObject(map.get("LDDJSJ"));//登记时间
				String nyddjlx = StringHelper.formatObject(map.get("LDDJLX"));// 登记类型
				String nydqllx = StringHelper.formatObject(map.get("LDQLLX"));// 权利类型
				String nydbdcdylx = StringHelper.formatObject(map.get("LDBDCDYLX"));// 不动产单元类型
				String nydbdcdyh = StringHelper.formatObject(map.get("LDBDCDYH"));// 不动产单元号
				String nyddyzt = StringHelper.formatObject(map.get("NYDDYZT"));
				String nydcfzt = StringHelper.formatObject(map.get("NYDCFZT"));
				String nydyyzt = StringHelper.formatObject(map.get("NYDYYZT"));
				String nydxzzt = StringHelper.formatObject(map.get("NYDXZZT"));

				List<GX_CONFIG> cdjls = baseCommonDao.getDataList(GX_CONFIG.class, "CDRMC = '"+qlrmc+"' AND CDRZJH = '"+qlrzjh+"' AND CDMD = '有无不动产' ORDER BY CDTIME DESC");

				List<Map> rlist = new ArrayList<Map>();
				Map cmap = new HashMap();

				if(cdjls.size() > 0){
					String cdbh = cdjls.get(0).getCdbh();
					String cdmd = cdjls.get(0).getCdmd();

					cmap.put("CDBH", cdbh);
					cmap.put("CDMD", cdmd);
				}

				String cdr = "";
				if (!StringHelper.isEmpty(map.get("CDR"))) {
					cmap.put("CDR", map.get("CDR"));
				} else {
					cmap.put("CDR", qlrmc);
				}
				cmap.put("BH", bh);
				cmap.put("JBR", jbr);
				cmap.put("QLRMC", qlrmc);
				cmap.put("ZJHM", qlrzjh);
				
				cmap.put("FWXX", fwxx);
				cmap.put("HBDCQZH", hbdcqzh);
				cmap.put("HZL", hzl);
				cmap.put("HMJ", hmj);
				cmap.put("HDJSJ", hdjsj);
				cmap.put("HSFQCQ", hsfqcq);
				cmap.put("HGHYTNAME", hghytname);
				cmap.put("HDJLX", hdjlx);
				cmap.put("HQLLX", hqllx);
				cmap.put("HFWXZ", hfwxz);
				cmap.put("HFJ", hfj);
				cmap.put("HGYRMC", hgyrmc);
				cmap.put("HBDCDYLX", hbdcdylx);
				cmap.put("HBDCDYH", hbdcdyh);
				cmap.put("HYGBDCQZH", hygbdcqzh);
				cmap.put("QXFGLZT", qxfglzt);
				cmap.put("XFDYZT", xfdyzt);
				cmap.put("XFCFZT", xfcfzt);
				cmap.put("XFYYZT", xfyyzt);
				cmap.put("XFXZZT", xfxzzt);
				
				cmap.put("YCHBDCQZH", ychbdcqzh);
				cmap.put("YCHZL", ychzl);
				cmap.put("YCHMJ", ychmj);
				cmap.put("YCHDJSJ", ychdjsj);
				cmap.put("YCHSFQCQ", ychsfqcq);
				cmap.put("YCHGHYTNAME", ychghytname);
				cmap.put("YCHDJLX", ychdjlx);
				cmap.put("YCHQLLX", ychqllx);
				cmap.put("YCHFWXZ", ychfwxz);
				cmap.put("YCHFJ", ychfj);
				cmap.put("YCHGYRMC", ychgyrmc);
				cmap.put("YCHBDCDYLX", ychbdcdylx);
				cmap.put("YCHBDCDYH", ychbdcdyh);
				cmap.put("YCHYGBDCQZH", ychygbdcqzh);
				cmap.put("QFDYZT", qfdyzt);
				cmap.put("QFCFZT", qfcfzt);
				cmap.put("QFYYZT", qfyyzt);
				cmap.put("QFXZZT", qfxzzt);
				
				cmap.put("ZDXX", zdxx);
				cmap.put("SHYQZDBDCQZH", shyqzdbdcqzh);
				cmap.put("SHYQZDZL", shyqzdzl);
				cmap.put("SHYQZDMJ", shyqzdmj);
				cmap.put("SHYQZDDJSJ", shyqzddjsj);
				cmap.put("SHYQZDDJLX", shyqzddjlx);
				cmap.put("SHYQZDQLLX", shyqzdqllx);
				cmap.put("SHYQZDBDCDYLX", shyqzdbdcdylx);
				cmap.put("SHYQZDBDCDYH", shyqzdbdcdyh);
				cmap.put("SHYQZDDYZT", shyqzddyzt);
				cmap.put("SHYQZDCFZT", shyqzdcfzt);
				cmap.put("SHYQZDYYZT", shyqzdyyzt);
				cmap.put("SHYQZDXZZT", shyqzdxzzt);
				
				cmap.put("HYXX", hyxx);
				cmap.put("HYBDCQZH", hybdcqzh);
				cmap.put("HYZL", hyzl);
				cmap.put("HYMJ", hymj);
				cmap.put("HYDJSJ", hydjsj);
				cmap.put("HYDJLX", hydjlx);
				cmap.put("HYQLLX", hyqllx);
				cmap.put("HYBDCDYLX", hybdcdylx);
				cmap.put("HYBDCDYH", hybdcdyh);
				cmap.put("HYDYZT", hydyzt);
				cmap.put("HYCFZT", hycfzt);
				cmap.put("HYYYZT", hyyyzt);
				cmap.put("HYXZZT", hyxzzt);
				
				cmap.put("LDXX", ldxx);
				cmap.put("LDBDCQZH", ldbdcqzh);
				cmap.put("LDZL", ldzl);
				cmap.put("LDMJ", ldmj);
				cmap.put("LDDJSJ", lddjsj);
				cmap.put("LDDJLX", lddjlx);
				cmap.put("LDQLLX", ldqllx);
				cmap.put("LDBDCDYLX",ldbdcdylx);
				cmap.put("LDBDCDYH", ldbdcdyh);
				cmap.put("LDDYZT", lddyzt);
				cmap.put("LDCFZT", ldcfzt);
				cmap.put("LDYYZT", ldyyzt);
				cmap.put("LDXZZT", ldxzzt);
				
				cmap.put("NYDXX", nydxx);
				cmap.put("NYDBDCQZH", nydbdcqzh);
				cmap.put("NYDZL", nydzl);
				cmap.put("NYDMJ", nydmj);
				cmap.put("NYDDJSJ", nyddjsj);
				cmap.put("NYDDJLX", nyddjlx);
				cmap.put("NYDQLLX", nydqllx);
				cmap.put("NYDBDCDYLX", nydbdcdylx);
				cmap.put("NYDBDCDYH", nydbdcdyh);
				cmap.put("NYDDYZT", nyddyzt);
				cmap.put("NYDCFZT", nydcfzt);
				cmap.put("NYDYYZT", nydyyzt);
				cmap.put("NYDXZZT", nydxzzt);
				
				
				//加行政区名称
				String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");
				cmap.put("XZQHMC", xzqhmc);
				
				rlist.add(cmap);
				searchResults.add(rlist);
			}
		}
		Date currdate = new Date();
		String datestring = "";
		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd");
		try {
			datestring = formatter.format(currdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		msg.setMsg(datestring);
		msg.setTotal(searchResults.size());
		msg.setRows(searchResults);
		msg.setSuccess("返回成功！");
		return msg;
	}
	
	
	/**
	  个人查档：根据名称和证件号码获取对应的不动产信息
	 * 
	 * @param xm 姓名
	 * @param zjh 证件号
	 * @param jbr 经办人
	 * @param cdr 查档人
	 * @param sfall 查询全部不动产：0是，1不是。
	 * @param sfhouse 查询现房：0是，1不是。
	 * @param sfqhouse 查询期房：0是，1不是。
	 * @param sfshyqzd 查询使用权宗地：0是，1不是。
	 * @param sfsyqzd 查询所有权宗地：0是，1不是。
	 * @param sfsea 查询海域：0是，1不是。
	 * @param sfld 查询林地：0是，1不是。
	 * @param sfnyd 查询农用地：0是，1不是。
	 */
	public Message getWhthinMatchData(String xm, String zjh, String jbr, String cdr,
			String sfall, String sfhouse, String sfqhouse,String sfshyqzd,String sfsyqzd,String sfsea,String sfld,String sfnyd){
		Message message = new Message();
		List<Map> list = new ArrayList<Map>();//存传进来的个人信息，用于匹配数据
		Map<String,String> map = new HashMap<String,String> ();
		map.put("BH", "1");
		map.put("QLRMC", xm);
		map.put("CDR", cdr);
		map.put("ZJHM", zjh);
		map.put("JBR", jbr);
		list.add(map);

		Message msgbdcData = this.getRealestateData(sfall, sfhouse, sfqhouse, sfshyqzd,sfsyqzd, sfsea, sfld, sfnyd,list);
		if(msgbdcData != null){
			List<Map> listbdc = (List<Map>)msgbdcData.getRows();
		message = getBDCPLPrintZM(listbdc, jbr);
		}else{
			message.setMsg("找不到单元，查询失败");
			return message;
		}
		
		return message;
	}
	
	protected String getGYRMC(String qlid, String qlrid) {
		if (!StringHelper.isEmpty(qlid) && !StringHelper.isEmpty(qlrid)) {
			//除当前权利人外其他共有权利人
			String gyrsql = "SELECT QLRMC FROM BDCK.BDCS_QLR_XZ WHERE QLID='"+qlid+"' AND QLRID <> '"+qlrid+"'";
			List<Map> gyr =  baseCommonDao.getDataListByFullSql(gyrsql);
			String gyrmc = "";
			if (gyr != null && gyr.size() > 0) {
				for (int k=0; k < gyr.size() ; k++) {
					if (k > 2) {
						gyrmc += "等" + gyr.size() + "人";
						break;
					}
					if (StringHelper.isEmpty(gyrmc)) {
						gyrmc = gyr.get(k).get("QLRMC").toString();
					} else {
						gyrmc += "、" + gyr.get(k).get("QLRMC").toString();
					}
				}
			}
			return gyrmc;
		} else {
			return "";
		}
	}
	
	/**
	 * 不动产查询
	 * @author liangc
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Message bdcdaQuery(Map<String, String> queryvalues, Integer page,
			Integer rows,boolean iflike){
		Map<String, String> newpara = new HashMap<String, String>();
		Message msg = new Message();
		long count = 0;
		StringBuilder builderWhereH = new StringBuilder();
		StringBuilder builderFromF = new StringBuilder();
		StringBuilder builderSelectS = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (StringHelper.isEmpty(value)){
				builderWhereH.append("");
			}
			if (!StringHelper.isEmpty(value)){
				if (name.equals("QLRMC")) {
					if (iflike) {
						try {
							newpara.put("QLRMC","%"+ new String(queryvalues.get("QLRMC").getBytes("iso8859-1"), "utf-8") + "%");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						builderWhereH.append(" AND SQR.SQRXM LIKE'" + newpara.get("QLRMC") + "'");
					}else{
						try {
							newpara.put("QLRMC",new String(queryvalues.get("QLRMC").getBytes("iso8859-1"), "utf-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						builderWhereH.append(" AND SQR.SQRXM='" + newpara.get("QLRMC") + "'");
					}
				}
				if (name.equals("ZJH")) {
					if (iflike) {
						try {
							newpara.put("ZJH","%"+ new String(queryvalues.get("ZJH").getBytes("iso8859-1"), "utf-8") + "%");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						builderWhereH.append(" AND SQR.ZJH LIKE'" + newpara.get("ZJH") + "'");
					}else{
						try {
							newpara.put("ZJH",new String(queryvalues.get("ZJH").getBytes("iso8859-1"), "utf-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						builderWhereH.append(" AND SQR.ZJH='" + newpara.get("ZJH") + "'");
					}
				}
				
				
			}
		}
		builderSelectS.append(" SELECT DISTINCT QL.QLID,DJDY.BDCDYID ");
		String fromSql = " FROM BDCK.BDCS_DJDY_LS DJDY LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.DJDYID=DJDY.DJDYID "
				+ " LEFT JOIN BDCK.BDCS_QLR_LS QLR ON QL.QLID = QLR.QLID "
				+ " LEFT JOIN BDCK.BDCS_SQR SQR ON SQR.SQRID = QLR.SQRID "
				+ " WHERE QL.QLLX NOT IN('23','99') "
				+ builderWhereH.toString();

		String fullSql = builderSelectS.toString() + fromSql;
		
		count = baseCommonDao.getCountByFullSql("FROM ("+fullSql+") t");
		List<Map> Result = new ArrayList<Map>();
		Result= baseCommonDao.getPageDataByFullSql(fullSql, page, rows);
		List<Map> result_new=new ArrayList<Map>();
		int i = 1;
		if(Result!=null&&Result.size()>0){
			List<Map> result=new ArrayList<Map>();
			for (Map map : Result) {
				if (!map.containsKey(map.get("BDCDYID"))&&!map.containsKey(map.get("QLID"))) {
					result.add(map);
					map.put(map.get("BDCDYID"), "");
					map.put(map.get("QLID"), "");
				}
			}
			Result.clear();
			for (Map map : result) {
				String bdcdyid = StringHelper.formatObject(StringHelper
						.isEmpty(map.get("BDCDYID")) ? "" : map.get("BDCDYID"));
				String qlid = StringHelper.formatObject(StringHelper
						.isEmpty(map.get("QLID")) ? "" : map.get("QLID"));
				List<BDCS_DJDY_LS> djdy = baseCommonDao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='"+bdcdyid+"'");
				String _bdcdylx = "";
				if(djdy != null && djdy.size()>0 ){
					_bdcdylx = djdy.get(0).getBDCDYLX();
				}
				String bdcdylxcode = StringHelper.formatObject(StringHelper
						.isEmpty(_bdcdylx) ? "" : _bdcdylx);			
				BDCDYLX bdcdylx=BDCDYLX.initFrom(bdcdylxcode);
				Map newMap=new HashMap();
				RealUnit u=null;
				RealUnit u2=null;
				if(bdcdylx!=null&&!StringUtils.isEmpty(bdcdyid))
				    u=UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcdyid);//初始化不动产单元信息
				u2 = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid);//初始化不动产单元信息
				String bdcdyzt="单元未注销";
				if(u!=null)
					if(u2 == null){
						bdcdyzt="单元已注销";
					}
				 map.put("BDCDYZT", bdcdyzt);
				 {
					 map.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
					 if (BDCDYLX.SHYQZD.equals(bdcdylx))// 所有权宗地不动产单元
						{
						 	BDCS_SHYQZD_LS _shyqzd = (BDCS_SHYQZD_LS) u;
						 	map.put("BDCDYH",String.valueOf(_shyqzd.getBDCDYH()));
						 	map.put("ZL",String.valueOf(_shyqzd.getZL()));
						}else if (BDCDYLX.SYQZD.equals(bdcdylx))// 使用权宗地不动产单元
						{
							BDCS_SYQZD_LS _syqzd = (BDCS_SYQZD_LS) u;
							map.put("BDCDYH",String.valueOf(_syqzd.getBDCDYH()));
							map.put("ZL",String.valueOf(_syqzd.getZL()));
						}else if (BDCDYLX.H.equals(bdcdylx))// 实测户不动产单元
						{
							 BDCS_H_LS _h = (BDCS_H_LS) u;
							 map.put("BDCDYH",String.valueOf(_h.getBDCDYH()));
							 map.put("ZL",String.valueOf(_h.getZL()));
						}
						else if (BDCDYLX.YCH.equals(bdcdylx))// 预测户不动产单元
						{
							 BDCS_H_LSY _ych = (BDCS_H_LSY) u;
							 map.put("BDCDYH",String.valueOf(_ych.getBDCDYH()));
							 map.put("ZL",String.valueOf(_ych.getZL()));
						}else if (BDCDYLX.LD.equals(bdcdylx))// 林地不动产单元
						{
							 BDCS_SLLM_LS _ld = (BDCS_SLLM_LS) u;
							 map.put("BDCDYH",String.valueOf(_ld.getBDCDYH()));
							 map.put("ZL",String.valueOf(_ld.getZL()));
						}
						else if (BDCDYLX.NYD.equals(bdcdylx))// 农用地不动产单元
						{
							 BDCS_NYD_LS _ld = (BDCS_NYD_LS) u;
							 map.put("BDCDYH",String.valueOf(_ld.getBDCDYH()));
							 map.put("ZL",String.valueOf(_ld.getZL()));
						}
						else if (BDCDYLX.HY.equals(bdcdylx))// 海域不动产单元
						{
							 BDCS_ZH_LS _hy = (BDCS_ZH_LS) u;
							 map.put("BDCDYH",String.valueOf(_hy.getBDCDYH()));
							 map.put("ZL",String.valueOf(_hy.getZL()));
						}
				 }
				String qlsql = "select ql.bdcdyid,ql.djsj,ql.qlqssj,ql.qljssj,fsql.zxsj,qlr.qlrmc,qlr.zjh "
						+ " from bdck.bdcs_ql_ls ql "
						+ " left join bdck.bdcs_fsql_ls fsql on fsql.qlid = ql.qlid "
						+ " left join (SELECT WM_CONCAT(TO_CHAR(qlrmc)) as qlrmc,WM_CONCAT(TO_CHAR(zjh)) as zjh,qlid from bdck.bdcs_qlr_ls group by qlid)qlr on qlr.qlid = ql.qlid "
						+ " where ql.qllx not in('23','99') and ql.qlid='"+qlid+"'";
				List<Map> qls = baseCommonDao.getDataListByFullSql(qlsql);
				if(qls != null && qls.size()>0){
					map.put("QLRMC",StringHelper.formatObject(qls.get(0).get("QLRMC")));
					map.put("ZJH",StringHelper.formatObject(qls.get(0).get("ZJH")));
					String cqzt = "产权未注销";
					if(qls.get(0).get("ZXSJ") != null){
						cqzt = "产权已注销";
					}
					map.put("CQZT",cqzt);
				}
				String sqlLimit = MessageFormat
						.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
								bdcdyid);
				long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
				String xzzt="无限制";
				if(LimitCount>0){
					xzzt="有限制";
				}
				map.put("XZZT", xzzt);
				
				String dyzt="无抵押";
				String cfzt="无查封";
				String yyzt="无异议";
				String sqlMortgage_xz = MessageFormat.format(
						" from BDCK.BDCS_QL_XZ where bdcdyid=''{0}'' and qllx=''23''",
						bdcdyid);
				String sqlSeal_xz = MessageFormat
						.format(" from BDCK.BDCS_QL_XZ where bdcdyid=''{0}'' and djlx=''800'' and qllx=''99''",
								bdcdyid);
				String sqlObjection_xz = MessageFormat
						.format("  from BDCK.BDCS_QL_XZ where bdcdyid=''{0}'' and djlx=''600'' ",
								bdcdyid);

				long mortgageCount_xz = baseCommonDao.getCountByFullSql(sqlMortgage_xz);
				long SealCount_xz = baseCommonDao.getCountByFullSql(sqlSeal_xz);
				long ObjectionCount_xz = baseCommonDao.getCountByFullSql(sqlObjection_xz);
				
				String sqlMortgage_ls = MessageFormat.format(
						" from BDCK.BDCS_QL_XZ where bdcdyid=''{0}'' and qllx=''23''",
						bdcdyid);
				String sqlSeal_ls = MessageFormat
						.format(" from BDCK.BDCS_QL_XZ where bdcdyid=''{0}'' and djlx=''800'' and qllx=''99''",
								bdcdyid);
				String sqlObjection_ls = MessageFormat
						.format("  from BDCK.BDCS_QL_XZ where bdcdyid=''{0}'' and djlx=''600'' ",
								bdcdyid);

				long mortgageCount_ls = baseCommonDao.getCountByFullSql(sqlMortgage_ls);
				long SealCount_ls = baseCommonDao.getCountByFullSql(sqlSeal_ls);
				long ObjectionCount_ls = baseCommonDao.getCountByFullSql(sqlObjection_ls);
				
				if ((mortgageCount_xz > 0)||(mortgageCount_ls > 0)) {
					if((mortgageCount_xz > 0)&&(mortgageCount_ls > 0)){
						dyzt = "已抵押";
					}else if(!(mortgageCount_xz > 0)&&(mortgageCount_ls > 0)){
						dyzt = "抵押已注销";
					}
				}else if (!(mortgageCount_xz > 0)&&!(mortgageCount_ls > 0)) {
					String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.bdcdyid='"
							+ bdcdyid + "' and a.sfdb='0' ";
					long countdydj = baseCommonDao.getCountByFullSql(sqlmortgageing);
					dyzt = countdydj > 0 ? "抵押办理中" : "无抵押";
				}
				
				if ((SealCount_xz > 0)||(SealCount_ls > 0)) {
					if((SealCount_xz > 0)&&(SealCount_ls > 0)){
						cfzt = "已查封";
					}else if(!(SealCount_xz > 0)&&(SealCount_ls > 0)){
						cfzt = "已解封";
					}
				}else if (!(SealCount_xz > 0)&&!(SealCount_ls > 0)) {
					String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.bdcdyid='"
							+ bdcdyid + "' and a.sfdb='0' ";
					long countcfdj = baseCommonDao.getCountByFullSql(sqlSealing);
					cfzt = countcfdj > 0 ? "查封办理中" : "无查封";
				}
				
				if ((ObjectionCount_xz > 0)||(ObjectionCount_ls > 0)) {
					if((ObjectionCount_xz > 0)&&(ObjectionCount_ls > 0)){
						yyzt = "有异议";
					}else if(!(ObjectionCount_xz > 0)&&(ObjectionCount_ls > 0)){
						yyzt = "异议已注销";
					}
				}else if (!(ObjectionCount_xz > 0)&&!(ObjectionCount_ls > 0)) {
					String sqlyying = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='600'  and c.bdcdyid='"
							+ bdcdyid + "' and a.sfdb='0' ";
					long countyydj = baseCommonDao.getCountByFullSql(sqlyying);
					yyzt = countyydj > 0 ? "异议办理中" : "无异议";
				}
				
				map.put("DYZT", dyzt);
				map.put("CFZT", cfzt);
				map.put("YYZT", yyzt);
				
				result_new.add(map);
				
			}
		}
		msg.setTotal(count);
		msg.setRows(result_new);
		return msg;
		
	}
}

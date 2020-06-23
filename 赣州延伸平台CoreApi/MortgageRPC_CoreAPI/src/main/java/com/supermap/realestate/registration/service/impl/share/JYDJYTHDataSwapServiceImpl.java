package com.supermap.realestate.registration.service.impl.share;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.AsyncRestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.model.BDCS_IDCARD_PIC;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.JYDJYTHDataSwapService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.HttpRequest;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;

/**
 * 交易库和登记库都在一个实例，如是分开的，后期再改成从视图获取
 * @author wanghongchao 
 *
 */
@Service("jydjythDataSwapService")
public class JYDJYTHDataSwapServiceImpl implements JYDJYTHDataSwapService {
	protected Logger logger = Logger.getLogger(JYDJYTHDataSwapServiceImpl.class);
	@Autowired
	private CommonDao baseCommonDao;
	@Autowired
	private DYService dyService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage readContract(String casenum, String xmbh,String project_id,Map paramsMap) {
		
		ResultMessage resultMessage = new ResultMessage(); 
		boolean isNoCheck= false;
		if(StringUtils.isBlank(casenum)) {
			resultMessage.setMsg("请输入合同编号！");
			resultMessage.setSuccess("false");
			return resultMessage;
		}else {
			//如果包含warning说明是看到警告后仍然执行添加单元操作，所以要还原正确的合同编号，去掉warning
			if (casenum.contains("warning")) {
				isNoCheck = true;
				casenum = casenum.replaceFirst("warning", "");
			}
		}
		
  		SelectorConfig selectorConfig = HandlerFactory.getSelectorByProjectID(project_id);
 		String idfieldname = StringHelper.formatObject(selectorConfig.getIdfieldname());
 		if(!"BDCDYID".equals(idfieldname)) {
 			resultMessage.setSuccess("false");
			resultMessage.setMsg(" 该登记单元类型，不支持通过合同提取数据"); 
			return resultMessage;
 		}
 	    // 不动产单元类型  com.supermap.realestate.registration.util.ConstValue.BDCDYLX 
 		BDCDYLX bdcdylx = BDCDYLX.initFrom(selectorConfig.getBdcdylx());
  		String djdylx = bdcdylx.Value;
   		String jy_compact_id = "";
 		String jy_chhouse_id = "";
 		StringBuffer compactHouseSql = new StringBuffer();
 		String fcjylxString = StringHelper.formatObject(paramsMap.get("fcjylx")); 
 		
 		if ("1".equals(fcjylxString)||"032".equals(djdylx)) {//业务是需要实测户但是读取的是期房合同
			//预测户
 			compactHouseSql.append(" select TCH.HOUSE_ID ,TCH.COMPACT_ID,H.CHHOUSE_ID ");
 			compactHouseSql.append(" from smpresale_sto.t_compact_house tch inner join smright_sto.t_house h ");
 			compactHouseSql.append(" on (");//on start
 			compactHouseSql.append(" tch.house_id=h.house_id ");
 			compactHouseSql.append(" and tch.compact_id in( ");
 			compactHouseSql.append(" select tc.compact_id from smpresale_sto.t_compact tc where ");  
 			compactHouseSql.append(" TC.COMPACT_RECORDSTATE='6000'");
 			compactHouseSql.append(" and TC.COMPACT_CODE='").append(casenum).append("' ");
 			compactHouseSql.append(" )");//in end
 			compactHouseSql.append(" and TCH.HOUSE_ID is not null ");
 			compactHouseSql.append(" )");//on end
		}else if ("2".equals(fcjylxString)||"031".equals(djdylx)) {
			//实测户
			compactHouseSql.append(" SELECT tch.house_id, tch.salecompact_id compact_id, th.righthouse_id chhouse_id ");
			compactHouseSql.append(" FROM smsale_sto.t_compacthouse tch INNER JOIN smsale_sto.t_house th ");
			compactHouseSql.append(" on ( ");//on start
			compactHouseSql.append(" tch.house_id = th.house_id  ");
			compactHouseSql.append(" and tch.salecompact_id IN(  ");
			compactHouseSql.append(" SELECT ts.salecompact_id FROM smsale_sto.t_salecompact ts where ");  
			compactHouseSql.append(" TS.COMPACT_STATUS='6000' ");
			compactHouseSql.append(" and ts.COMPACT_CODE='").append(casenum).append("' ");
			compactHouseSql.append(")");//in end
			compactHouseSql.append(" and TCH.HOUSE_ID is not null ");
			compactHouseSql.append(" )");//on end
		}else {
			resultMessage.setSuccess("false");
			resultMessage.setMsg(" 该登记单元类型，不支持通过合同提取数据");
			return resultMessage;
		}
		List<Map> list = baseCommonDao.getDataListByFullSql(compactHouseSql.toString());
		if (list!=null&&list.size()>0) {
 			jy_compact_id  = StringHelper.formatObject(list.get(0).get("COMPACT_ID"));
			jy_chhouse_id  = StringHelper.formatObject(list.get(0).get("CHHOUSE_ID")); 
			//获取登记单元号
			String bdcdyid = jy_chhouse_id;//交易库和登记库房屋数据关联字段
 			StringBuffer sql = new StringBuffer();
 			if("031".equals(djdylx)) {
 				sql.append("select bdcdyid from BDCK.BDCS_H_XZ ");
 				if ("1".equals(fcjylxString)) {//说明读取的是期房合同，故而通过期现房关联获取房屋的现房的BDCDYID
					String queryXFbdcdyidSql = "select ycsc.SCBDCDYID from BDCK.YC_SC_H_XZ ycsc where ycsc.YCBDCDYID='"+bdcdyid+"' ";
					List<Map> resultList = baseCommonDao.getDataListByFullSql(queryXFbdcdyidSql);
					if (!resultList.isEmpty()) {
						bdcdyid = StringHelper.formatObject(resultList.get(0).get("SCBDCDYID"));
					}else {
						resultMessage.setSuccess("false");
						resultMessage.setMsg("通过期房【"+bdcdyid+"】获取不到现房的数据，请检查期现房关联关系是否正确！");
						return resultMessage;
					}
 				}
 			}else {
				sql.append("select bdcdyid from BDCK.BDCS_H_XZY ");
			}
			
			sql.append(" where bdcdyid='").append(bdcdyid).append("' ");
			List<Map> bdcdyidList = baseCommonDao.getDataListByFullSql(sql.toString());
			if(bdcdyidList!=null&&bdcdyidList.size()>0) {
				//获取不动产单元ID
				bdcdyid = StringHelper.formatObject(bdcdyidList.get(0).get("BDCDYID"));
				// 2. 创建登记单元
				if (isNoCheck) {
					resultMessage = dyService.addBDCDYNoCheck(xmbh, bdcdyid);
				}else {
					resultMessage = dyService.addBDCDY(xmbh, bdcdyid);
				} 
				String successFlag = resultMessage.getSuccess();
				if("true".equals(successFlag)) {
					// 3.根据xmbh，生成申请人的权利人、义务人信息 
					boolean addSqrFlag=createSQRbyCompactId(xmbh,jy_compact_id,djdylx,fcjylxString); 
					if (addSqrFlag==false) {
						resultMessage.setSuccess("false");
						resultMessage.setMsg(" 创建单元创建成功，但申请人信息失败，请联系管理员");
					}else {
						resultMessage.setSuccess("true");
						resultMessage.setMsg(" 创建单元成功");
					}
					if ("031".equals(djdylx)) {
						if (!copyMaterbyCompactId(xmbh,jy_compact_id,fcjylxString)) {
							resultMessage.setMsg("合同数据提取成功！但提取图片失败！");
							resultMessage.setSuccess("false");
						}
					}
				}  
			}else {
				resultMessage.setSuccess("false");
				resultMessage.setMsg(" 获取单元信息失败，交易合同对应的不动产单元不存在，请联系管理员");
			} 
			
		}else {
			resultMessage.setSuccess("false");
			resultMessage.setMsg(" 该合同号查不到房屋数据，请联系管理员!");
		} 
		
		return resultMessage;
	}
	
	 
	/**
	 * @param xmbh
	 * @param compact_id
	 * @param qxfzt 期现房状态
	 * @param fcjylx 房产交易类型，1 新建商品房买卖 2 存量房买卖
	 * @return
	 */
	private  boolean createSQRbyCompactId(String xmbh,String compact_id, String qxfzt,String fcjylx) {
		boolean successFlag = true;
		List<String> sqridList = new ArrayList<String>();
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
   		String djlx = StringHelper.formatObject(xmxx.getDJLX());
  		//分析：1.首次登记、预告登记提取合同，买卖双方数据都应该提取，2.转移登记-新建商品房买卖-按户/二手房转移登记，只提取买方信息作为权利人人，义务人由首次登记的权利人提取而来
  		
		if("1".equals(fcjylx)||"032".equals(qxfzt)) {
			//期房
			//买方
			List<Map> buyerList = baseCommonDao.getDataListByFullSql("select * from SMPRESALE_STO.T_BUYER where COMPACT_ID = '"+compact_id+"'");	
			if(buyerList!=null&&buyerList.size()>0) {
				for (Map map : buyerList) {
					String buyerid = StringHelper.formatObject(map.get("BUYER_ID"));
					if (!sqridList.contains(buyerid)) {
						sqridList.add(buyerid);
						String SQRID = SuperHelper.GeneratePrimaryKey();;
						BDCS_SQR sqr = new BDCS_SQR();
						sqr.setGYFS(StringHelper.formatObject(map.get("SHARE_TYPENAME")));
						sqr.setXB(StringHelper.formatObject(map.get("BUYER_SEX")));
 						sqr.setYXBZ("1");
						sqr.setQLBL(StringHelper.formatObject(map.get("SZFE")));
 						sqr.setSQRXM(StringHelper.formatObject(map.get("BUYER_NAME")));
						sqr.setSQRLB("1");
						sqr.setSQRLX(StringHelper.formatObject(map.get("BUYER_TYPE")));
 						sqr.setLXDH(StringHelper.formatObject(map.get("BUYER_PHONE")));
						sqr.setZJH(StringHelper.formatObject(map.get("BUYER_CODE")));
//ZJLX						1	身份证
//						2	港澳台身份证
//						3	护照
//						4	户口簿
//						5	军官证（士兵证）
//						6	组织机构代码
//						7	营业执照 
//						99	其它  
						if("9001".equals("")){//其他证件
							sqr.setZJLX("99");
						}else if("5001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//港澳台证件
							sqr.setZJLX("2");
						}else if("1001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){ //身份证
							sqr.setZJLX("1");
						}else if("3001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//营业执照
							sqr.setZJLX("7");
						}else if("4001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//户口本
							sqr.setZJLX("4");
						}else if("2001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//军官证
							sqr.setZJLX("5");
						} 
						sqr.setTXDZ(StringHelper.formatObject(map.get("BUYER_ADDRESS")));
						sqr.setYZBM(StringHelper.formatObject(map.get("BUYER_ZIPCODE")));
						sqr.setPICTURECODE(StringHelper.formatObject(map.get("BUYER_IDCARDR")));//身份证头像
						sqr.setXMBH(xmbh);
						sqr.setId(SQRID); 
						baseCommonDao.save(sqr);
						addIDCARD_PIC(sqr);//保存头像
					}
				}
			}
			if (!"200".equals(djlx)) {//如果是转移登记就不需要读取卖方作为义务人
				//卖方
				List<Map> sellerList = baseCommonDao.getDataListByFullSql("select * from SMPRESALE_STO.T_SELLER where COMPACT_ID = '"+compact_id+"'");
				if (sellerList!=null&&sellerList.size()>0) {
					for (Map map : sellerList) {
						String sellerid = StringHelper.formatObject(map.get("SELLER_ID"));
						if (!sqridList.contains(sellerid)) {
							sqridList.add(sellerid);
							String SQRID = SuperHelper.GeneratePrimaryKey();;
							BDCS_SQR sqr = new BDCS_SQR();
	  						sqr.setYXBZ("1");
	  						sqr.setSQRXM(StringHelper.formatObject(map.get("SELLER_NAME")));
							sqr.setSQRLB("2");
	 						sqr.setLXDH(StringHelper.formatObject(map.get("SELLER_PHONE")));
							sqr.setZJH(StringHelper.formatObject(map.get("SELLER_BUSINESSCODE")));
							sqr.setZJLX("4");//营业执照
							sqr.setTXDZ(StringHelper.formatObject(map.get("SELLER_ADDRESS")));
							sqr.setYZBM(StringHelper.formatObject(map.get("SELLER_ZIPCODE")));
					
							sqr.setFDDBR(StringHelper.formatObject(map.get("SELLER_LEGALPERSON"))); 
							sqr.setFDDBRDH(StringHelper.formatObject(map.get("SELLER_PHONE")));
							sqr.setFDDBRZJHM(StringHelper.formatObject(map.get("SELLER_BUSINESSCODE"))); 
							sqr.setFDDBRZJLX("4");
							sqr.setXMBH(xmbh);
							sqr.setId(SQRID); 
							baseCommonDao.save(sqr);
						}
					}
				}
			} 
		}else if("2".equals(fcjylx)||"031".equals(qxfzt)) {
			//现房
			//买方
			List<Map> buyerList = baseCommonDao.getDataListByFullSql("select * from smsale_sto.t_compactbuyer where salecompact_id = '"+compact_id+"'");
			 for (Map map : buyerList) {
				 String buyerid = StringHelper.formatObject(map.get("COMPACTBUYER_ID"));
					if (!sqridList.contains(buyerid)) {
						sqridList.add(buyerid);
						String SQRID = SuperHelper.GeneratePrimaryKey();;
						BDCS_SQR sqr = new BDCS_SQR();
						sqr.setGYFS(StringHelper.formatObject(map.get("COMMON_SITUATION")));
						sqr.setXB(StringHelper.formatObject(map.get("COMPACTBUYER_SEX")));
						sqr.setYXBZ("1");
						sqr.setQLBL(StringHelper.formatObject(map.get("COMMON_SHARE")));
						sqr.setSQRXM(StringHelper.formatObject(map.get("COMPACTBUYER_NAME")));
						sqr.setSQRLB("1");
						sqr.setSQRLX(StringHelper.formatObject(map.get("COMPACTBUYER_TYPE")));
						sqr.setLXDH(StringHelper.formatObject(map.get("BUYER_TEL")));
						sqr.setZJH(StringHelper.formatObject(map.get("COMPACTBUYER_CODE")));

						//ZJLX	1	身份证
						//		2	港澳台身份证
						//		3	护照
						//		4	户口簿
						//		5	军官证（士兵证）
						//		6	组织机构代码
						//		7	营业执照
						//		99	其它
																								
						String zjzl = "";
						if("9001".equals("")){//其他证件
							sqr.setZJLX("99");
						}else if("5001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//港澳台证件
							sqr.setZJLX("2");
						}else if("1001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){ //身份证
							sqr.setZJLX("1");
						}else if("3001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//营业执照
							sqr.setZJLX("7");
						}else if("4001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//户口本
							sqr.setZJLX("4");
						}else if("2001".equals(StringHelper.formatObject(map.get("CERTIFICATE_TYPE")))){//军官证
							sqr.setZJLX("5");
						}
						sqr.setTXDZ(StringHelper.formatObject(map.get("BUYER_ADDRESS")));
						sqr.setYZBM(StringHelper.formatObject(map.get("BUYER_ZIPCOOD")));
						sqr.setPICTURECODE(StringHelper.formatObject(map.get("IDENTITYPIC")));//身份证头像
						sqr.setXMBH(xmbh);
						sqr.setId(SQRID); 
						baseCommonDao.save(sqr);
						addIDCARD_PIC(sqr);//保存头像
					}
			} 
 		}else {
			successFlag=false;
		}
		baseCommonDao.flush();
		return successFlag;
	}
	
	/**
	 * 从存量房网签系统提取收件资料到登记业务中，如果收件目录名匹配则上传图片到相应目录，如果不匹配则新增目录
	 * @param xmbh
	 * @param compactid
	 * @return
	 */
 	public boolean copyMaterbyCompactId(String xmbh, String compactid,String fcjylx) {
 		
		boolean success= true;
		try {
			User user = Global.getCurrentUserInfo();

			BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
			List<Wfi_ProInst> wfi_ProInstList = baseCommonDao.getDataList(Wfi_ProInst.class, " file_number='"+xmxx.getPROJECT_ID()+"' ");
			
			if (wfi_ProInstList.isEmpty()) {
				System.out.println("查询不到相关的wif_proinst,通过xmxx表的project_id="+xmxx.getPROJECT_ID());
				return false;
			}
			Wfi_ProInst wfi_ProInst= wfi_ProInstList.get(0);
			String proisntId = wfi_ProInst.getProinst_Id();
			String prodefId =  wfi_ProInst.getProdef_Id();
			//获取图片元数据
			String jyytGetCompactImageDataUrl = ConfigHelper.getNameByValue("jyyt.getCompactImageData.url"); 
			//1.从接口获取图片列表
			Map<String, String> paramCompactIdMap = new HashMap<String, String>();
			paramCompactIdMap.put("compact_id", compactid);
			paramCompactIdMap.put("fcjylx", fcjylx);
			System.out.println("-准备提取资料--compact_id:"+compactid+";jyytGetCompactImageDataUrl="+jyytGetCompactImageDataUrl);
			
			//2. 从交易接口获取资料列表元数据
			String materialListResultStr =  HttpRequest.sendGet(jyytGetCompactImageDataUrl.trim(), paramCompactIdMap);
			
			System.out.println("- 提取资料到的数据--materialListResultStr:"+materialListResultStr);
			//test
			//materialListResultStr="[{\"nodes\":[{\"path\":\"\\\\59b46d73a1e04f7c8eeaac97909c9b65\\\\ec35b57272394769bb390cd585589db6\\\\\",\"id\":\"36379e3c6f7047929dbb0ec40d19c1ca\",\"text\":\"mmexport1515488667608\",\"type\":\"n\",\"tags\":\"jpg\"}],\"id\":\"ec35b57272394769bb390cd585589db6\",\"text\":\"完税证明及不动产销售发票\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[{\"path\":\"\\\\59b46d73a1e04f7c8eeaac97909c9b65\\\\f50e53be815441d18397f4d0b055c800\\\\\",\"id\":\"45c58a35000648d3a30c37ef21c5230a\",\"text\":\"远程4\",\"type\":\"n\",\"tags\":\"png\"}],\"id\":\"f50e53be815441d18397f4d0b055c800\",\"text\":\"不动产登记申请表\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[],\"id\":\"913c922189ae416d9deefdd85427247f\",\"text\":\"申请人（代理人）身份证明\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[],\"id\":\"bf0a1daf70ac41189549984ec605e948\",\"text\":\"房产证和土地证\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[{\"path\":\"\\\\59b46d73a1e04f7c8eeaac97909c9b65\\\\4991a6e9402e49b38989cabb09ca6d7f\\\\\",\"id\":\"a06b8fed883f40c893b19e608eb0e7dd\",\"text\":\"远程6\",\"type\":\"n\",\"tags\":\"png\"}],\"id\":\"4991a6e9402e49b38989cabb09ca6d7f\",\"text\":\"二手房买卖契约\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[{\"path\":\"\\\\59b46d73a1e04f7c8eeaac97909c9b65\\\\7d428a03fff5489f98afe49c3865c1c7\\\\\",\"id\":\"228ba8ed5f884f8f8fd6c149dff38f0d\",\"text\":\"远程3\",\"type\":\"n\",\"tags\":\"png\"}],\"id\":\"7d428a03fff5489f98afe49c3865c1c7\",\"text\":\"委托书\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[],\"id\":\"599d8a6969614693b609f95b4dac2738\",\"text\":\"房屋交易于产权告知书\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[],\"id\":\"12b7b88fcfc14fed9e90aa833cf107f1\",\"text\":\"宗地图、宗地界址点坐标\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"},{\"nodes\":[],\"id\":\"16a11f8e813b43b9a5c38eba0563d468\",\"text\":\"询问笔录\",\"state\":\"\",\"type\":\"g\",\"tags\":\"\"}]";
			if (StringUtils.isNotBlank(materialListResultStr)) {
				//目录对象
				JSONArray materialArray = JSONObject.parseArray(materialListResultStr);
				for (int i = 0; i < materialArray.size(); i++) {
					JSONObject promaterJsonObject = materialArray.getJSONObject(i);
					String MATERIAL_ID = StringHelper.formatObject(promaterJsonObject.get("id")); 
   					int MATERIAL_COUNT = 0;
   					Wfi_ProMater proMater = new Wfi_ProMater();
					String MATERIAL_NAME = StringHelper.formatObject(promaterJsonObject.get("text"));
					//如果不存在目录则生成新目录，如果存在只往目录插入图片
					Wfi_ProMater wfi_ProMater = new Wfi_ProMater();
					if ("".equals(MATERIAL_NAME.trim())) {
						continue;
					}else {
						
						List<Wfi_ProMater> wfi_ProMaterList = baseCommonDao.getDataList(Wfi_ProMater.class, "MATERIAL_NAME='"+MATERIAL_NAME+"' and PROINST_ID='"+proisntId+"' ");
						if (wfi_ProMaterList.isEmpty()) {
 							wfi_ProMater.setMaterilinst_Id(Common.CreatUUID());
							wfi_ProMater.setMaterial_Count(1);
							wfi_ProMater.setMaterial_Name(MATERIAL_NAME);
							wfi_ProMater.setMaterial_Date(new Date());
							wfi_ProMater.setProinst_Id(proisntId);
							wfi_ProMater.setMaterial_Type(1);
							wfi_ProMater.setMaterial_Pagecount(1);
							wfi_ProMater.setMaterial_Need(2);
							List<Map> Result = baseCommonDao.getDataListByFullSql("select nvl(max(Material_Index),1) maxindex from " + Common.WORKFLOWDB + "Wfi_ProMater where proinst_id='" + proisntId + "'");
							Integer maxvalue = 1;
							if (Result != null && Result.size() > 0) {
								Object max = Result.get(0).get("MAXINDEX");
								maxvalue = Integer.parseInt(max.toString());
								maxvalue++;
							}
							wfi_ProMater.setMaterial_Index(maxvalue);
							wfi_ProMater.setMaterial_Status(WFConst.MateralStatus.CeratMateral.value);
							baseCommonDao.save(wfi_ProMater);
							baseCommonDao.flush();
						}else {
							wfi_ProMater = wfi_ProMaterList.get(0);
						}
					}
   					JSONArray promaterDataObjectArray = promaterJsonObject.getJSONArray("nodes");
   					if (promaterDataObjectArray!=null&&promaterDataObjectArray.size()>0) {
   						MATERIAL_COUNT = promaterDataObjectArray.size();
   						for (int j = 0; j < promaterDataObjectArray.size(); j++) {
   							JSONObject materdata = promaterDataObjectArray.getJSONObject(j);
   							String IMAGE_ID =  StringHelper.formatObject(materdata.get("id"));
   							String FILE_NAME =  StringHelper.formatObject(materdata.get("text"));
   							if ("".equals(FILE_NAME)) {
   								System.err.println("收件资料文件名为空！");
								continue;
							}
   							String FILE_POSTFIX =  StringHelper.formatObject(materdata.get("tags"));
   							String FILE_PATH =  StringHelper.formatObject(materdata.get("path"));
   							Wfi_MaterData wfi_MaterData = new Wfi_MaterData();
   							wfi_MaterData.setMaterilinst_Id(wfi_ProMater.getMaterilinst_Id());
   							wfi_MaterData.setFile_Name(FILE_NAME+"."+FILE_POSTFIX);
   							wfi_MaterData.setFile_Postfix(FILE_POSTFIX);
   							wfi_MaterData.setFile_Path(FILE_NAME+"."+FILE_POSTFIX);
   							wfi_MaterData.setPath(FILE_PATH);
   							wfi_MaterData.setFile_Index(j);
   							String staff_id="";
   							String staff_name="";
    						if(user!=null){
   								staff_id=user.getId();
   								staff_name=user.getLoginName();
    						}
    						wfi_MaterData.setUpload_Id(staff_id);
    						wfi_MaterData.setUpload_Name(staff_id);
   							baseCommonDao.getCurrentSession().merge(wfi_MaterData);
   							baseCommonDao.flush();
   							uploadCompactImageByImageId(IMAGE_ID,wfi_ProMater,wfi_MaterData,wfi_ProInst);
						}
					} 
				}  
				success = true;
			}else {
				success = false;
			}  
  		} catch (Exception e) {
			// TODO: handle exception
			success = false;
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return success;
	}
 	
 	public boolean uploadCompactImageByImageId(String image_id,Wfi_ProMater wfi_ProMater, Wfi_MaterData wfi_MaterData, Wfi_ProInst wfi_ProInst) throws UnsupportedEncodingException {
 		boolean success = false;
 		//http://localhost:8084/realestate/app/frame/wfipromater/imagedownload/de613dd2fd0647f1b901517db380bfdc/
		String jyytDownloadCompactImageUrl = ConfigHelper.getNameByValue("jyyt.downloadCompactImage.url")+"/"+image_id;
		//jyytDownloadCompactImageUrl = "http://localhost:8084/realestate/app/frame/wfipromater/imagedownload/de613dd2fd0647f1b901517db380bfdc/";
		System.out.println("jyytDownloadCompactImageUrl="+jyytDownloadCompactImageUrl);
		logger.info("jyytDownloadCompactImageUrl="+jyytDownloadCompactImageUrl);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		String fileName = df.format(new Date()) + "_" + java.net.URLEncoder.encode(wfi_MaterData.getFile_Name(), "UTF-8");
		String materialtype = ConfigHelper.getNameByValue("materialtype");
		String mString = GetProperties.getConstValueByKey("materialtype");
		String filePath = GetNewPath(wfi_ProInst, wfi_MaterData.getMaterilinst_Id());
		if (mString != null && !mString.equals("")) {
			materialtype = mString;
		}
		
		String basicPath = ConfigHelper.getNameByValue("material");
		String disc = basicPath;
		basicPath = basicPath + filePath;
		File dirFile = new File(basicPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String allpath = basicPath + "\\" + fileName;
		
		File file = new File(allpath);
		URL url; 
		try {
			if (file.exists()) {
				file.delete();
			}
			url = new URL(jyytDownloadCompactImageUrl);
 			FileUtils.copyURLToFile(url, file);
			//文件上传成功
			if (file.exists()) {
				wfi_MaterData.setFile_Name(fileName);
				wfi_MaterData.setFile_Path(fileName);
				wfi_MaterData.setPath(filePath);
				wfi_MaterData.setDisc(disc);
				wfi_MaterData.setUpload_Date(new Date());
				wfi_ProMater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
				if(wfi_ProMater.getMaterial_Count()==null){
					wfi_ProMater.setMaterial_Count(1);
				}else {
					wfi_ProMater.setMaterial_Count(wfi_ProMater.getMaterial_Count()+1);
				}
				wfi_ProMater.setImg_Path(wfi_MaterData.getMaterialdata_Id());
				baseCommonDao.update(wfi_ProMater);
				baseCommonDao.update(wfi_MaterData);
				baseCommonDao.flush();
				return true;
			}else {
				logger.error("提取资料失败，提取路径为：jyytDownloadCompactImageUrl="+jyytDownloadCompactImageUrl+"请在浏览器录入该路径，分析下载资料是否成功");
				return false;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("访问资料提取接口失败，访问地址是："+jyytDownloadCompactImageUrl+"请在浏览器验证。");
			return false;
		}
		
 	}
 	public static String GetNewPath(Wfi_ProInst inst, String materilinst_id) {
		SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");// 设置日期格式
		String first = fir.format(inst.getCreat_Date());
		SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
		String second = sec.format(inst.getCreat_Date());
		return first + File.separator + second + File.separator + inst.getProinst_Id() + File.separator + materilinst_id;
	}
 	
 	private void addIDCARD_PIC(BDCS_SQR sqr) { 
 		if (!StringHelper.isEmpty(sqr.getZJH())
				&& !StringHelper.isEmpty(sqr.getPICTURECODE())
				&& sqr.getPICTURECODE().length() > 0) {
			List<BDCS_IDCARD_PIC> pics = baseCommonDao
					.getDataList(BDCS_IDCARD_PIC.class,
							"ZJH='" + sqr.getZJH() + "'");
			if (pics != null && pics.size() > 0) {
				BDCS_IDCARD_PIC pic = pics.get(0);
				String pic_code = sqr.getPICTURECODE();
				String pic_code1 = pic_code;
				String pic_code2 = "";
				if (pic_code.length() > 3999) {
					pic_code1 = pic_code.substring(0, 3999);
					pic_code2 = pic_code.substring(3999);
				}
				pic.setZJH(sqr.getZJH());
				pic.setPIC1(pic_code1);
				pic.setPIC2(pic_code2);
				baseCommonDao.update(pic);
			} else {
				BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
				String _id = SuperHelper.GeneratePrimaryKey();
				pic.setId(_id);
				String pic_code = sqr.getPICTURECODE();
				String pic_code1 = pic_code;
				String pic_code2 = "";
				if (pic_code.length() > 3999) {
					pic_code1 = pic_code.substring(0, 3999);
					pic_code2 = pic_code.substring(3999);
				}
				pic.setZJH(sqr.getZJH());
				pic.setPIC1(pic_code1);
				pic.setPIC2(pic_code2);
				baseCommonDao.save(pic);
			}
		}
		if (!StringHelper.isEmpty(sqr.getDLRXM())) {
			if (!StringHelper.isEmpty(sqr.getDLRZJHM())
					&& !StringHelper.isEmpty(sqr.getPICTURECODE_DLR())
					&& sqr.getPICTURECODE_DLR().length() > 0) {
				List<BDCS_IDCARD_PIC> pics = baseCommonDao.getDataList(
						BDCS_IDCARD_PIC.class, "ZJH='" + sqr.getDLRZJHM()
								+ "'");
				if (pics != null && pics.size() > 0) {
					BDCS_IDCARD_PIC pic = pics.get(0);
					String pic_code = sqr.getPICTURECODE_DLR();
					String pic_code1 = pic_code;
					String pic_code2 = "";
					if (pic_code.length() > 3999) {
						pic_code1 = pic_code.substring(0, 3999);
						pic_code2 = pic_code.substring(3999);
					}
					pic.setZJH(sqr.getDLRZJHM());
					pic.setPIC1(pic_code1);
					pic.setPIC2(pic_code2);
					baseCommonDao.update(pic);
				} else {
					BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
					String _id = SuperHelper.GeneratePrimaryKey();
					pic.setId(_id);
					String pic_code = sqr.getPICTURECODE_DLR();
					String pic_code1 = pic_code;
					String pic_code2 = "";
					if (pic_code.length() > 3999) {
						pic_code1 = pic_code.substring(0, 3999);
						pic_code2 = pic_code.substring(3999);
					}
					pic.setZJH(sqr.getDLRZJHM());
					pic.setPIC1(pic_code1);
					pic.setPIC2(pic_code2);
					baseCommonDao.save(pic);
				}
			}
		}
 		baseCommonDao.flush();
 	}


	@SuppressWarnings("rawtypes")
	@Override
	public Message queryHouseCompact(Map params) {
		Message message = new Message();
		
		Integer pageIndex = 1; 
		if (params.get("page") != null) {
			pageIndex = StringHelper.getInt(params.get("page"));
		}
		if (params.get("pageIndex") != null) {
			pageIndex = StringHelper.getInt(params.get("pageIndex"));
		}
		String fuzzyQuery = StringHelper.formatObject(params.get("fuzzyQuery")).toLowerCase();
		boolean isfuzzy = false;
		if ("true".equals(fuzzyQuery)) {
			isfuzzy = true;
		}
		Integer pageSize = 10;
		if (params.get("rows") != null) {
			pageSize = StringHelper.getInt(params.get("rows"));
		}
		if (params.get("pageSize") != null) {
			pageSize = StringHelper.getInt(params.get("pageSize"));
		}
		
		String house_id = StringHelper.formatObject(params.get("house_id"));//交易系统房屋ID
		String bdcdyid = StringHelper.formatObject(params.get("bdcdyid"));
		String house_address = StringHelper.formatObject(params.get("house_address"));
 		String compact_code = StringHelper.formatObject(params.get("compact_code"));
		String compact_id = StringHelper.formatObject(params.get("compact_id"));
		String buyer_name = StringHelper.formatObject(params.get("buyer_name"));
		String buyer_code = StringHelper.formatObject(params.get("buyer_code"));
		
		StringBuffer sqlBuffer = new StringBuffer();
		StringBuffer sqlSelectBuffer = new StringBuffer();
		StringBuffer sqlWhereBuffer = new StringBuffer();
		StringBuffer sqlSelectCountBuffer = new StringBuffer();
		try {
			String select = " select HOUSE_ID ," + 
					" CHHOUSE_ID ," + 
					" BDCDYID ," + 
					" BUILDING_NUMBER ," + 
					" HOUSE_ADDRESS ," + 
					" ROOM_NUMBER ," + 
					" USE_FACT ," + 
					" BUILD_AREA ," + 
					" BUILD_AREA_INSIDE ," + 
					" BUILD_AREA_SHARE ," + 
					" PRICE_TOTAL ," + 
					" COMPACT_ID ," + 
					" COMPACT_CODE ," + 
					" COMPACTRECORDS_DATE ," + 
					" COMPACTRECORDS_NAME ," + 
					" COMPACT_RECORDSTATE ," + 
					" ONLINEAPPLY_ID ," + 
					" WMSYS.WM_CONCAT(BUYER_NAME) BUYER_NAME," + 
					" WMSYS.WM_CONCAT(BUYER_CODE) BUYER_CODE ";
			sqlSelectBuffer.append(select).append(" from BDCK.V_JY_COMPACTHOUSEALL where 1=1  ");
			sqlSelectCountBuffer.append(" from BDCK.V_JY_COMPACTHOUSEALL where 1=1  ");
			
			if (StringUtils.isNotBlank(house_id)) {
				sqlWhereBuffer.append(" and house_id='").append(house_id).append("' ");
			}
			if (StringUtils.isNotBlank(bdcdyid)) {
				sqlWhereBuffer.append(" and bdcdyid='").append(bdcdyid).append("' ");
			}
			if (StringUtils.isNotBlank(house_address)) {
				if (isfuzzy) {
					sqlWhereBuffer.append(" and instr(house_address,'").append(house_address).append("')>0 ");
				}else {
					sqlWhereBuffer.append(" and house_address='").append(house_address).append("' ");
				} 
			}
			if (StringUtils.isNotBlank(compact_code)) {
				if (isfuzzy) {
					sqlWhereBuffer.append(" and instr(compact_code,'").append(compact_code).append("')>0 ");
				}else {
					sqlWhereBuffer.append(" and compact_code='").append(compact_code).append("' ");
				}
				
			}
			if (StringUtils.isNotBlank(compact_id)) {
				sqlWhereBuffer.append(" and compact_id='").append(compact_id).append("' ");
			}
			if (StringUtils.isNotBlank(buyer_name)) {
				String[] buyerNameArr = buyer_name.split(",");
				StringBuffer nameBuffer = new StringBuffer(" 1=3 ");
				for (String name : buyerNameArr) {
					if (isfuzzy) {
						nameBuffer.append(" or instr(buyer_name,'").append(name).append("')>0 ");
 					}else {
 						nameBuffer.append(" or buyer_name='").append(name).append("' ");
					} 
				}
				sqlWhereBuffer.append(" and  (").append(nameBuffer.toString()).append(") ");
			}
			if (StringUtils.isNotBlank(buyer_code)) {  
				String[] buyerCodeArr = buyer_code.split(",");
				StringBuffer codeBuffer = new StringBuffer(" 1=3 ");
				for (String code : buyerCodeArr) {
					if (isfuzzy) {
						codeBuffer.append(" or instr(buyer_code,'").append(code).append("')>0 ");
 					}else {
 						codeBuffer.append(" or buyer_code='").append(code).append("' ");
					} 
				}
				sqlWhereBuffer.append(" and  (").append(codeBuffer.toString()).append(") ");
			}
			//sqlBuffer.append(" and COMPACT_RECORDSTATE>4000 ");
			String groupby = " group by " + 
					" HOUSE_ID ," + 
					" CHHOUSE_ID ," + 
					" BDCDYID ," + 
					" BUILDING_NUMBER ," + 
					" HOUSE_ADDRESS ," + 
					" ROOM_NUMBER ," + 
					" USE_FACT ," + 
					" BUILD_AREA ," + 
					" BUILD_AREA_INSIDE ," + 
					" BUILD_AREA_SHARE ," + 
					" PRICE_TOTAL ," + 
					" COMPACT_ID ," + 
					" COMPACT_CODE ," + 
					" COMPACTRECORDS_DATE ," + 
					" COMPACTRECORDS_NAME ," + 
					" COMPACT_RECORDSTATE ," + 
					" onlineapply_id ";
			
			
			StringBuffer orderBy = new StringBuffer(" order by ");
			orderBy.append(" COMPACT_RECORDSTATE ");
			
			sqlSelectCountBuffer.append(sqlWhereBuffer.toString());
			long count = baseCommonDao.getCountByFullSql(sqlSelectCountBuffer.toString()); 
			
			sqlBuffer.append(sqlSelectBuffer.toString()).append(sqlWhereBuffer.toString()).append(groupby);
			List<Map> resultList = baseCommonDao.getPageDataByFullSql(sqlBuffer.toString(), pageIndex, pageSize);
			message.setSuccess("true");
			message.setRows(resultList);
			message.setTotal(count);
		} catch (Exception e) {
			e.printStackTrace();
			message.setSuccess("false");
			message.setMsg(e.getMessage());
			message.setTotal(0);
		}
		 
		return message; 
	}


//	@SuppressWarnings("rawtypes")
//	@Override
//	public void writeBackHouseStateToFCJY(String xmbh) { 
//		String fulSql = "select * from bdck.v_house_state where xmbh='"+xmbh+"' ";
//		List<Map> resultList = baseCommonDao.getDataListByFullSql(fulSql);
//		final String url = ConfigHelper.getNameByValue("JYYTUPDATEHOUSESTATEURL");
//		if (StringUtils.isNotBlank(url)) {
//			Map<String, String> param = new HashMap<String, String>();
//			param.put("data", JSONObject.toJSONString(resultList));
//			param.put("xmbh", xmbh);
//			try {
//				HttpRequestAsync.sendPostNoReturn(url, param); 
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(url+",访问异常！"+e.getMessage());
//			} 
//		} 
// 	}
	@Override
	public void writeBackHouseStateToFCJY(String xmbh) { 
		final String JYYTUPDATEHOUSESTATEURL = ConfigHelper.getNameByValue("JYYTUPDATEHOUSESTATEURL");
		if (StringUtils.isNotBlank(JYYTUPDATEHOUSESTATEURL)) {
			Map<String, Object> paramMap = new HashMap<String, Object>(); 
			String queryBDCDYIDString = "select bdcdyid from BDCK.V_DJDY where xmbh='"+xmbh+"'";
			paramMap.put("data", baseCommonDao.getDataListByFullSql(queryBDCDYIDString));
			paramMap.put("xmbh", xmbh);
			try {
				MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
 		 		HttpEntity<Object> hpEntity = new HttpEntity<Object>(paramMap, headers);
		        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
				// 调用完后立即返回（没有阻塞）
		 		//ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.postForEntity(JYYTUPDATEHOUSESTATEURL, hpEntity, String.class);
		 		asyncRestTemplate.postForEntity(JYYTUPDATEHOUSESTATEURL, hpEntity, String.class);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(JYYTUPDATEHOUSESTATEURL+",访问异常！"+e.getMessage());
			} 
		} 
 	}
	@SuppressWarnings("rawtypes")
	@Override
	public Message getHouseState(List<String> bdcdyids) {
		Message message = new Message();
		try {
			if (bdcdyids==null||bdcdyids.isEmpty()) {
				message.setSuccess("false");
				message.setMsg("参数为空！");
				return message;
			} 
			String bdcdyidString = StringUtils.join(bdcdyids, "','");
			String fulSql = "select * from bdck.v_house_state where bdcdyid in('"+bdcdyidString+"')";
			List<Map> resultList = baseCommonDao.getDataListByFullSql(fulSql);
			message.setRows(resultList);
			message.setTotal(resultList.size());
			message.setMsg("生产数据时间："+DateUtil.getDateTime()); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			message.setSuccess("false");
			message.setMsg("查询失败！"+e.getMessage());
		}
		return message;
	}
}

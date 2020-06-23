package com.supermap.realestate.registration.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.service.FCQueryService;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoFC;
import com.supermap.wisdombusiness.web.Message;


@Service("fcqueryService")
public class FCQueryServiceImpl implements FCQueryService{

	@Autowired
	private CommonDaoFC baseCommonDao;
	
	/**
	 * 根据登记类型获取房产信息
	 */
	@Override
	public Message getFcInfo(Map<String, String> params)throws Exception{
		if(null==params || params.isEmpty()){
			return null;
		}
		Message msg = null;
		String djlx = params.get("DJLX");
		if("100".equals(djlx)){
			msg = this.getCSDJXX(params);
		}else if("200".equals(djlx)){
			msg = this.getZYDJXX(params);
		}else if("300".equals(djlx)){
			msg = this.getBGDJXX(params);
		}else if("700".equals(djlx)){
			msg = this.getYGDJXX(params);
		}else if("23".equals(djlx)){
			msg = this.getDYDJXX(params);
		}else if("99".equals(djlx)){
			msg = this.getCFDJXX(params);
		}
		return msg;
	}
	
	/**
	 * 获取初始登记信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	private Message getCSDJXX(Map<String, String> params){
		if(null==params || params.isEmpty()){
			return null;
		}
		Message msg = new Message();
		String zl = params.get("ZL");
		String qlr = params.get("QLR");
		String pageIndex = params.get("pageIndex");
		String pageSize = params.get("pageSize");
		int rowStart=(Integer.parseInt(pageIndex)-1)*(Integer.parseInt(pageSize))+1;
		int rowEnd =(Integer.parseInt(pageIndex))*(Integer.parseInt(pageSize));
		int pagecount = Integer.parseInt(pageIndex)*Integer.parseInt(pageSize);
		String select = "select TOP "+pagecount+" a.coNo ,a.cfNo ,a.cregistertype1,a.cRegisterType2 ,a.cOwner ,a.cOwnerID,a.cOwnerProperty,a.cOwnershipFrom"
					  + ",a.cLocation,a.cHouNewCerNo1 + '(' + a.cHouNewCerNo2 + ')第' + a.cHouNewCerNo3 + '号' as cHouNewCerNo,"
					  + "a.fLanArea,a.cContact"
				      + ",a.cContactPhon,a.dNewCerDate,a.dConfirmDate,b.cComOwnerName,b.cComOwnerID,"
				      + "b.cComCerNo1 + b.cComCerNo2 + b.cComCerNo3 as cComCerNo"
				      + ",c.cHouClassify,c.cBldGroup,c.cBldNo,c.cHouNo,c.cBldStructure,c.cBldFloors,c.cBldFloorNo,c.cHouUse"
				      + ",c.fMLanArea,c.fMHouArea,c.fMHouRoomArea,c.fMHouShareArea,c.cEast,c.cWest,c.cSouth,c.cNorth";
		StringBuilder derfrom = new StringBuilder();
		derfrom.append(" from BHFC.dbo.fInitMain a left join BHFC.dbo.fiCommon b on a.iRealtyId = b.iRealtyId")
			   .append(" left join BHFC.dbo.fiHouse c on a.iRealtyId = c.iRealtyId")
			   .append(" where a.cregistertype1 ='初始登记' ");
		if(!StringHelper.isEmpty(zl)){
			derfrom.append(" and a.cLocation like '%").append(zl).append("%'");
		}
		if(!StringHelper.isEmpty(zl) && !StringHelper.isEmpty(qlr)) {
			derfrom.append(params.get("and_or").toString());
		}
		if(!StringHelper.isEmpty(qlr)&&!StringHelper.isEmpty(zl)){
			derfrom.append("  a.cOwner like '%").append(qlr).append("%'");
		}else if(!StringHelper.isEmpty(qlr)&&StringHelper.isEmpty(zl)){
			derfrom.append(" and  a.cOwner like '%").append(qlr).append("%'");
		}
		long count = baseCommonDao.getCountByFullSql(derfrom.toString());
		List<Map> csdjs = baseCommonDao.getDataListByFullSql("SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY coNo) AS ROWID,* FROM ("+select+derfrom.toString()+" ) aa )AS T WHERE T.ROWID BETWEEN "+rowStart+" AND "+rowEnd+"  ORDER BY coNo");
	//	List<Map> csdjs = baseCommonDao.getDataListByFullSql("select TOP "+pageSize+" * FROM ("+select+derfrom.toString()+"  ORDER BY a.coNo) aa  ORDER BY aa.coNo");
		msg.setTotal(count);
		msg.setRows(csdjs);
		msg.setSuccess("true");
		
		return msg;
	}
	
	/**
	 * 获取转移登记信息
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	private Message getZYDJXX(Map<String, String> params){
		if(null==params || params.isEmpty()){
			return null;
		}
		Message msg = new Message();
		String zl = params.get("ZL");
		String qlr = params.get("QLR");
		String pageIndex = params.get("pageIndex");
		String pageSize = params.get("pageSize");
		int rowStart=(Integer.parseInt(pageIndex)-1)*(Integer.parseInt(pageSize))+1;
		int rowEnd =(Integer.parseInt(pageIndex))*(Integer.parseInt(pageSize));
		int pagecount = Integer.parseInt(pageIndex)*Integer.parseInt(pageSize);
		String select = "select TOP "+pagecount+" a.cono,a.cfno,a.cRegisterType1,a.cregistertype2,a.cOwner,a.cOwnerProperty,a.cBuyer,a.cBuyerProperty"
					+ ",a.cBuyerProv,a.cBuyerCity,a.cOwnershipFrom,a.cLocation,"
					+ "a.cHouorgCerNo1 + a.cHouorgCerNo3 +'号' as cHouorgCerNo"
					+ ",a.cHouNewCerNo1 + '('+a.cHouNewCerNo2 + ')第' +a.cHouNewCerNo3 + '号' as cHouNewCerNo3,"
					+ "a.cHouNewCerNo,a.flannewarea"
					+ ",a.fAssessment,a.fTradePrice,a.fCalcTaxPrice,a.cContact,a.cContactPhon,a.cd1receiveman,a.dD1dateReceive"
					+ ",a.dNewCerDate,b.cComOwnerName,b.cComOwnerID,b.cComCerNo1 + b.cComCerNo2 + b.cComCerNo3 as cComCerNo,c.cHouClassify"
					+ ",c.cBldGroup,c.cBldNo,c.cHouNo ,c.cBldStructure,c.cBldFloors,c.cBldFloorNo,c.cHouUse,c.fHouOrgArea"
					+ ",c.fHouNewArea,c.fHouCerArea,fMHouRoomArea,c.fMHouShareArea,c.cEast,c.cWest,c.cSouth,c.cNorth";
		StringBuilder derfrom = new StringBuilder();
		derfrom.append(" from BHFC.dbo.fTradeMain a left join BHFC.dbo.fiCommon b on a.iRealtyId = b.iRealtyId")
			   .append(" left join BHFC.dbo.ftHouse c on a.iRealtyId = c.iRealtyId")
			   .append(" where a.cregistertype1 ='转移登记' ");
/*		derfrom.append(" from BHFC.dbo.fInitMain a left join BHFC.dbo.fiCommon b on a.iRealtyId = b.iRealtyId")
		.append(" left join BHFC.dbo.fiHouse c on a.iRealtyId = c.iRealtyId")
		.append(" where a.cregistertype1 ='转移登记' ");
*/		if(!StringHelper.isEmpty(zl) || !StringHelper.isEmpty(qlr)) {
			derfrom.append(" and ");
		}
		if(!StringHelper.isEmpty(zl)){
			derfrom.append("  a.cLocation like '%").append(zl).append("%'");
		}
		if(!StringHelper.isEmpty(zl) && !StringHelper.isEmpty(qlr)) {
			derfrom.append(params.get("and_or").toString());
		}
		if(!StringHelper.isEmpty(qlr)){			
			
			if (StringHelper.isEmpty(params.get("cOwner"))&&StringHelper.isEmpty(params.get("cTraOwner"))&&StringHelper.isEmpty(params.get("cAllOwner"))) {
			derfrom.append(" (a.cOwner ='中国人' ");
			}
			if (!StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" ( a.cOwner like '%").append(qlr).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cTraOwner"))&&!StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" or a.cBuyer like '%").append(qlr).append("%'");
			} else if(!StringHelper.isEmpty(params.get("cTraOwner"))&&StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" ( a.cBuyer like '%").append(qlr).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cAllOwner"))&&(!StringHelper.isEmpty(params.get("cTraOwner"))||!StringHelper.isEmpty(params.get("cOwner")))) {
				derfrom.append(" or a.cComOwnerName like '%").append(qlr).append("%'");
			}else if(!StringHelper.isEmpty(params.get("cAllOwner"))&&(StringHelper.isEmpty(params.get("cTraOwner"))&&StringHelper.isEmpty(params.get("cOwner")))) {
				derfrom.append(" ( a.cComOwnerName like '%").append(qlr).append("%'");
			}
			derfrom.append(") ");
/*			if (!StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" or a.cOwner like '%").append(qlr).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cTraOwner"))) {
				derfrom.append(" or a.cBuyer like '%").append(qlr).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cAllOwner"))) {
				derfrom.append(" or a.cComOwnerName like '%").append(qlr).append("%'");
			}
			derfrom.append(") ");
*/		}
//		if(!StringHelper.isEmpty(qlr)){
//			derfrom.append(" and (a.cOwner like '%").append(qlr).append("%'")
//				   .append(" or")
//				   .append(" a.cBuyer like '%").append(qlr).append("%'")
//				   .append(" or")
//				   .append(" b.cComOwnerName like '%").append(qlr).append("%')");
//		}
		long count = baseCommonDao.getCountByFullSql(derfrom.toString());
		List<Map> zxdjs =  baseCommonDao.getDataListByFullSql("SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY coNo) AS ROWID,* FROM ("+select+derfrom.toString()+" ) aa )AS T WHERE T.ROWID BETWEEN "+rowStart+" AND "+rowEnd+"  ORDER BY coNo");
		//List<Map> zxdjs = baseCommonDao.getDataListByFullSql("select TOP "+pageSize+" * FROM ("+select+derfrom.toString()+" ORDER BY a.coNo) aa  ORDER BY aa.coNo");
		msg.setTotal(count);
		msg.setRows(zxdjs);
		msg.setSuccess("true");			  
		
		return msg;
	}
	
	/**
	 * 获取变更登记信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	private Message getBGDJXX(Map<String, String> params){
		if(null==params || params.isEmpty()){
			return null;
		}
		Message msg = new Message();
		String zl = params.get("ZL");
		String qlr = params.get("QLR");
		String pageIndex = params.get("pageIndex");
		String pageSize = params.get("pageSize");
		int rowStart=(Integer.parseInt(pageIndex)-1)*(Integer.parseInt(pageSize))+1;
		int rowEnd =(Integer.parseInt(pageIndex))*(Integer.parseInt(pageSize));
		int pagecount = Integer.parseInt(pageIndex)*Integer.parseInt(pageSize);
		String select = "select TOP "+pagecount+" a.cono,a.cfno,a.cRegisterType1,a.cRegisterType2,a.cnewowner,a.cOwner,a.cnewlocation"
				+ ",a.clocation,a.dworkcompletedate,a.cownershipfrom,a.cGHPermissionNo,"
				+ "a.cHouorgCerNo1 + a.cHouorgCerNo3 +'号' as cHouorgCerNo"
				+ ",a.cHouNewCerNo1 + '('+a.cHouNewCerNo2 + ')第' +a.cHouNewCerNo3 + '号' as cHouNewCerNo"
				+ ",a.flanarea,a.clanCerNo1 + '(' +a.clanCerNo2 +')第' + a.clanCerNo3 +'号' as clanCerNo"
				+ ",a.clanproperty,a.cContact,a.cContactPhon,a.cd1receiveman,a.dD1dateReceive,a.dNewCerDate,"
				+ "a.dConfirmDate,b.cComOwnerName,b.cComOwnerID"
				+ ",b.cComCerNo1 + b.cComCerNo2 + b.cComCerNo3 as cComCerNo,"
				+ "c.cHouClassify,c.cBldGroup,c.cBldNo,c.cHouNo,c.cBldStructure"
				+ ",c.cBldFloors,c.cBldFloorNo,c.cHouUse,c.fMLanArea,c.fMHouArea,c.fMHouRoomArea,c.fMHouShareArea ,c.cEast"
				+ ",c.cWest,c.cSouth,c.cNorth";
				
		StringBuilder derfrom = new StringBuilder();
		derfrom.append(" from BHFC.dbo.fHouChangeMain a left join BHFC.dbo.fiCommon b on a.iRealtyId = b.iRealtyId")
			   .append(" left join BHFC.dbo.fiHouse c on a.iRealtyId = c.iRealtyId")
			   .append(" where a.cregistertype1 ='变更登记' ");
		if(!StringHelper.isEmpty(zl) || !StringHelper.isEmpty(qlr)) {
			derfrom.append(" and ");
		}
		if(!StringHelper.isEmpty(zl)){
			derfrom.append(" (");
			if(StringHelper.isEmpty(params.get("cLocation"))&&StringHelper.isEmpty(params.get("cNewLocation"))) {
			derfrom.append("  a.cNewOwner ='中国' ");
			}
			if (!StringHelper.isEmpty(params.get("cLocation"))) {
				derfrom.append("  a.cLocation like '%").append(zl).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cNewLocation"))&&!StringHelper.isEmpty(params.get("cLocation"))) {
				derfrom.append(" or a.cNewLocation like '%").append(zl).append("%'");
			}else if(!StringHelper.isEmpty(params.get("cNewLocation"))&&StringHelper.isEmpty(params.get("cLocation"))) {
				derfrom.append("  a.cNewLocation like '%").append(zl).append("%'");
			}
/*			if (!StringHelper.isEmpty(params.get("cLocation"))) {
				derfrom.append(" or a.cLocation like '%").append(zl).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cNewLocation"))) {
				derfrom.append(" or a.cNewLocation like '%").append(zl).append("%)'");
			}
*///			derfrom.append("  (a.cLocation like '%").append(zl).append("%'")
//				   .append(" or")
//				   .append(" a.cNewLocation like '%").append(zl).append("%')");
			derfrom.append(" )");
		}
		if(!StringHelper.isEmpty(zl) && !StringHelper.isEmpty(qlr)) {
			derfrom.append(params.get("and_or").toString());
		}
		if(!StringHelper.isEmpty(qlr)){
			
			if (StringHelper.isEmpty(params.get("cOwner"))&&StringHelper.isEmpty(params.get("cTraOwner"))&&StringHelper.isEmpty(params.get("cAllOwner"))) {
				derfrom.append(" (a.cOwner ='中国人' ");
				}
				if (!StringHelper.isEmpty(params.get("cOwner"))) {
					derfrom.append(" ( a.cOwner like '%").append(qlr).append("%'");
				}
				if (!StringHelper.isEmpty(params.get("cNewOwner"))&&!StringHelper.isEmpty(params.get("cOwner"))) {
					derfrom.append(" or a.cNewOwner like '%").append(qlr).append("%'");
				} else if(!StringHelper.isEmpty(params.get("cNewOwner"))&&StringHelper.isEmpty(params.get("cOwner"))) {
					derfrom.append(" ( a.cNewOwner like '%").append(qlr).append("%'");
				}
				if (!StringHelper.isEmpty(params.get("cAllOwner"))&&(!StringHelper.isEmpty(params.get("cTraOwner"))||!StringHelper.isEmpty(params.get("cOwner")))) {
					derfrom.append(" or b.cComOwnerName like '%").append(qlr).append("%'");
				}else if(!StringHelper.isEmpty(params.get("cAllOwner"))&&(StringHelper.isEmpty(params.get("cTraOwner"))&&StringHelper.isEmpty(params.get("cOwner")))) {
					derfrom.append(" ( b.cComOwnerName like '%").append(qlr).append("%'");
				}
/*		if (StringHelper.isEmpty(params.get("cOwner"))&&StringHelper.isEmpty(params.get("cNewOwner"))&&StringHelper.isEmpty(params.get("cAllOwner"))) {
			derfrom.append("  (a.cOwner ='中国人' ");
			}		
 * if (!StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" or a.cOwner like '%").append(qlr).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cNewOwner"))) {
				derfrom.append(" or a.cNewOwner like '%").append(qlr).append("%'");
			}
			if (!StringHelper.isEmpty(params.get("cAllOwner"))) {
				derfrom.append(" or a.cComOwnerName like '%").append(qlr).append("%'");
			}
*/			derfrom.append(") ");
		}
//		if(!StringHelper.isEmpty(qlr)){
//			derfrom.append(" and (a.cOwner like '%").append(qlr).append("%'")
//				   .append(" or")
//				   .append(" a.cNewOwner like '%").append(qlr).append("%'")
//				   .append(" or")
//				   .append(" b.cComOwnerName like '%").append(qlr).append("%')");
//		}
		long count = baseCommonDao.getCountByFullSql(derfrom.toString());
		List<Map> bgdjs = baseCommonDao.getDataListByFullSql("SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY coNo) AS ROWID,* FROM ("+select+derfrom.toString()+" ) aa )AS T WHERE T.ROWID BETWEEN "+rowStart+" AND "+rowEnd+"  ORDER BY coNo");
	//	List<Map> bgdjs = baseCommonDao.getDataListByFullSql("select TOP "+pageSize+" * FROM ("+select+derfrom.toString()+" ORDER BY a.coNo) aa  ORDER BY aa.coNo");
		msg.setTotal(count);
		msg.setRows(bgdjs);
		msg.setSuccess("true");			  
		
		return msg;
	}
	
	/**
	 * 预告登记信息
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	private Message getYGDJXX(Map<String, String> params){
		if(null==params || params.isEmpty()){
			return null;
		}
		Message msg = new Message();
		String zl = params.get("ZL");
		String qlr = params.get("QLR");
		String pageIndex = params.get("pageIndex");
		String pageSize = params.get("pageSize");
		int rowStart=(Integer.parseInt(pageIndex)-1)*(Integer.parseInt(pageSize))+1;
		int rowEnd =(Integer.parseInt(pageIndex))*(Integer.parseInt(pageSize));
		int pagecount = Integer.parseInt(pageIndex)*Integer.parseInt(pageSize);
		String select = "select TOP "+pagecount+" cono,cRegisterType1,cRegisterType2,cBuyer,cBuyerID,cOwner,cOwnerID,clocation"
				+ ",cHouNewCerNo1 + '(' + cHouNewCerNo2 + ')第' + cHouNewCerNo3 + '号' as cHouNewCerNo "
				+ ",cPreOrgCerNo1 + '(' + cPreOrgCerNo2 + ')第' + cPreOrgCerNo3 + ')号' as cPreOrgCerNo "
				+ ",cPreNewCerNo1 + '(' + cPreNewCerNo2 + ')第' + cPreNewCerNo3 + '号' as cPreNewCerNo "
				+ ",dD1dateReceive,dConfirmDate,cContact,cContactPhone,cd1receiveman,cNote";
		StringBuilder derfrom = new StringBuilder();
		derfrom.append(" from BHFC.dbo.fQredictMain")
			   .append(" where 1=1 ");
		if(!StringHelper.isEmpty(zl)){
			derfrom.append(" and cLocation like '%").append(zl).append("%'");
		}
		if(!StringHelper.isEmpty(zl) && !StringHelper.isEmpty(qlr)) {
			derfrom.append(params.get("and_or").toString());
		}
		if(!StringHelper.isEmpty(qlr)){
			if(StringHelper.isEmpty(zl)) {
				derfrom.append(" and ");
			}
			derfrom.append("  cOwner like '%").append(qlr).append("%'");
		}
		long count = baseCommonDao.getCountByFullSql(derfrom.toString());
		List<Map> ygdjs = baseCommonDao.getDataListByFullSql("SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY coNo) AS ROWID,* FROM ("+select+derfrom.toString()+" ) aa )AS T WHERE T.ROWID BETWEEN "+rowStart+" AND "+rowEnd+"  ORDER BY coNo");
//		List<Map> ygdjs = baseCommonDao.getDataListByFullSql("select TOP "+pageSize+" * FROM ("+select+derfrom.toString()+" ORDER BY coNo) aa  ORDER BY aa.coNo");
		msg.setTotal(count);
		msg.setRows(ygdjs);
		msg.setSuccess("true");			  
		
		return msg;
	}
	
	/**
	 * 抵押信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	private Message getDYDJXX(Map<String, String> params){
		if(null==params || params.isEmpty()){
			return null;
		}
		Message msg = new Message();
		String zl = params.get("ZL");
		String qlr = params.get("QLR");
		String pageIndex = params.get("pageIndex");
		String pageSize = params.get("pageSize");
		int rowStart=(Integer.parseInt(pageIndex)-1)*(Integer.parseInt(pageSize))+1;
		int rowEnd =(Integer.parseInt(pageIndex))*(Integer.parseInt(pageSize));
		int pagecount = Integer.parseInt(pageIndex)*Integer.parseInt(pageSize);
		String select = "select TOP "+pagecount+" a.cono,a.cfno,a.cRegisterType1,a.cRegisterType2,a.cOwner,a.cComOwnerName,a.clocation,a.cDebtor"
				+ ",a.cDebtorProperty,a.cLoaner,a.fAssessment,a.fBorrowing,a.cMaturity,a.cLoanContractNo,a.cMorContractNo"
				+ ",a.cLoanUse,a.cMorCerNo1 + '(' + a.cMorCerNo2 + ')第' + a.cMorCerNo3 + '号' as cMorCerNo"
				+ ",a.cContact,a.cContactPhon,a.cd1receiveman,a.dD1dateReceive,a.dCancelDate,a.dNewCerDate,a.dDateOfCalcTax,a.cNote,a.cRemark";
		StringBuilder derfrom = new StringBuilder();
		derfrom.append(" from BHFC.dbo.fMortgageMain a left join fmCommon b on a.iRealtyId = b.iRealtyId")
			   .append(" where 1=1");
		if(!StringHelper.isEmpty(zl) || !StringHelper.isEmpty(qlr)) {
			derfrom.append(" and ");
		}
		if(!StringHelper.isEmpty(zl)){
			derfrom.append("  a.cLocation like '%").append(zl).append("%'");
		}
		if(!StringHelper.isEmpty(zl) && !StringHelper.isEmpty(qlr)) {
			derfrom.append(params.get("and_or").toString());
		}
		if(!StringHelper.isEmpty(qlr)){
			if (StringHelper.isEmpty(params.get("cOwner"))&&StringHelper.isEmpty(params.get("cAllOwner"))) {
			derfrom.append(" (a.cOwner ='中国人' ");
			}
			if (!StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" ( a.cOwner like '%").append(qlr).append("%'");
			}
		/*	if (!StringHelper.isEmpty(params.get("cNewOwner"))) {
				derfrom.append(" or a.cNewOwner like '%").append(qlr).append("%'");
			}*/
			if (!StringHelper.isEmpty(params.get("cAllOwner"))&&!StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" or a.cComOwnerName like '%").append(qlr).append("%'");
			}else if(!StringHelper.isEmpty(params.get("cAllOwner"))&&StringHelper.isEmpty(params.get("cOwner"))) {
				derfrom.append(" ( a.cComOwnerName like '%").append(qlr).append("%'");
			}
			derfrom.append(") ");
		}
//		if(!StringHelper.isEmpty(qlr)){
//			derfrom.append(" and (a.cOwner like '%").append(qlr).append("%'")
//			   .append(" or")
//			   .append(" a.cNewOwner like '%").append(qlr).append("%'")
//			   .append(" or")
//			   .append(" b.cComOwnerName like '%").append(qlr).append("%')");
//		}
		long count = baseCommonDao.getCountByFullSql(derfrom.toString());
		List<Map> dydjs =  baseCommonDao.getDataListByFullSql("SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY coNo) AS ROWID,* FROM ("+select+derfrom.toString()+" ) aa )AS T WHERE T.ROWID BETWEEN "+rowStart+" AND "+rowEnd+"  ORDER BY coNo");
//		List<Map> dydjs = baseCommonDao.getDataListByFullSql("select TOP "+pageSize+" * FROM ("+select+derfrom.toString()+" ORDER BY a.coNo) aa  ORDER BY aa.coNo");
		msg.setTotal(count);
		msg.setRows(dydjs);
		msg.setSuccess("true");			  
		
		return msg;
	}
	
	/**
	 * 查封信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	private Message getCFDJXX(Map<String, String> params){
		if(null==params || params.isEmpty()){
			return null;
		}
		Message msg = new Message();
		String zl = params.get("ZL");
		String qlr = params.get("QLR");
		String pageIndex = params.get("pageIndex");
		String pageSize = params.get("pageSize");
		int rowStart=(Integer.parseInt(pageIndex)-1)*(Integer.parseInt(pageSize))+1;
		int rowEnd =(Integer.parseInt(pageIndex))*(Integer.parseInt(pageSize));
		int pagecount = Integer.parseInt(pageIndex)*Integer.parseInt(pageSize);
		String select = "select TOP "+pagecount+" cono,cRegisterType1,cRegisterType2,cLWDW,dDispatchDate,cWNo,cApplicant,cd1receiveman"
				+ ",dD1dateReceive,cremark,cZXNR,cOwner,clocation";
		StringBuilder derfrom = new StringBuilder();
		derfrom.append(" from fSequesterMain")
			   .append(" where 1=1 ");
		if(!StringHelper.isEmpty(zl) || !StringHelper.isEmpty(qlr)) {
			derfrom.append(" and ");
		}
		if(!StringHelper.isEmpty(zl)){
			derfrom.append("   cLocation like '%").append(zl).append("%'");
		}
		if(!StringHelper.isEmpty(zl) && !StringHelper.isEmpty(qlr)) {
			derfrom.append(params.get("and_or").toString());
		}
		if(!StringHelper.isEmpty(qlr)){

			derfrom.append("  cOwner like '%").append(qlr).append("%' ");
		}
		long count = baseCommonDao.getCountByFullSql(derfrom.toString());
		List<Map> cfdjs =  baseCommonDao.getDataListByFullSql("SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY coNo) AS ROWID,* FROM ("+select+derfrom.toString()+" ) aa )AS T WHERE T.ROWID BETWEEN "+rowStart+" AND "+rowEnd+"  ORDER BY coNo");
//		List<Map> cfdjs = baseCommonDao.getDataListByFullSql("select TOP "+pageSize+" * FROM ("+select+derfrom.toString()+" ORDER BY coNo) aa  ORDER BY aa.coNo");
		msg.setTotal(count);
		msg.setRows(cfdjs);
		msg.setSuccess("true");			  
		
		return msg;
	}
}

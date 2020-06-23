package com.supermap.realestate_gx.registration.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.Array;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.supermap.realestate.registration.ViewClass.DJFZXX;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.realestate_gx.registration.service.GX_Service;
import com.supermap.realestate_gx.registration.util.ConverterUtil;
import com.supermap.realestate_gx.registration.util.ConverterUtil.FC_TABLE;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.tree.State;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

@Component
public class GX_ServiceImpl implements GX_Service
{

	@Autowired
	private CommonDao baseCommonDao;

	private static Logger logger = Logger.getLogger(GX_ServiceImpl.class);//用log4j写日志

	@SuppressWarnings("unchecked")
	public Message GetFZList(String xmbh)
	{
		Message msg = new Message();
		List djfzxxList = new ArrayList();

		Map map = new HashMap();
		map.put("xmbh", xmbh);
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		String hql = " length(bDCQZH)>0 and xMBH=:xmbh";

		List<BDCS_ZS_GZ> zsList = this.baseCommonDao.getDataList(BDCS_ZS_GZ.class, hql, map);
		List bdcqzhlist = new ArrayList();
		if(zsList!=null && zsList.size()>0){
			for (BDCS_ZS_GZ zs : zsList) {
				List<BDCS_SQR> ywrs = this.baseCommonDao.getDataList(BDCS_SQR.class," sqrlb='2' and xmbh='"+xmbh+"'");
				String ywr = "";
				boolean ywrflg = true;
				if(ywrs!=null&&ywrs.size()>0){
					for (BDCS_SQR bdcs_SQR : ywrs) {
						if(ywrflg){
							ywrflg = false;
							ywr+=bdcs_SQR.getSQRXM();
						}else{
							ywr=ywr + "、"+bdcs_SQR.getSQRXM();
						}
					}
				}
				if ((!StringHelper.isEmpty(zs.getBDCQZH())) && (!bdcqzhlist.contains(zs.getBDCQZH()))) {
					bdcqzhlist.add(zs.getBDCQZH());
				
					DJFZXX djfzxxBo = new DJFZXX();
					djfzxxBo.setM_ywr(ywr);
					djfzxxBo.setSlsj(StringHelper.FormatByDatetime(xmxx.getSLSJ()));
					String strBDCQZH = zs.getBDCQZH();
					djfzxxBo.setZSBH(zs.getZSBH());
					djfzxxBo.setBDCQZH(strBDCQZH);
					djfzxxBo.setXMBH(zs.getXMBH());
					djfzxxBo.setCfdagh(StringHelper.isEmpty(xmxx.getDAGH()) ? "" : xmxx.getDAGH().toString());
					String strXMBH = zs.getXMBH();
					String strZSID = zs.getId();
					djfzxxBo.setCsdjtzbh(StringHelper.isEmpty(xmxx.getYWLSH()) ? "" : xmxx.getYWLSH().toString());
	
					StringBuilder builerqdzr = new StringBuilder();
					builerqdzr.append("XMBH='").append(strXMBH).append("' AND ZSID='").append(strZSID).append("'");
					List gxbList = this.baseCommonDao.getDataList(BDCS_QDZR_GZ.class, builerqdzr.toString());
					if (!gxbList.isEmpty()&& gxbList.size()>0) {
						BDCS_QDZR_GZ qdzr = (BDCS_QDZR_GZ) gxbList.get(0);
						String strQLRID = qdzr.getQLRID();
						String strQLID = qdzr.getQLID();
						String strDJDYID = qdzr.getDJDYID();
						StringBuilder builerql = new StringBuilder();
						builerql.append("XMBH='").append(strXMBH).append("' AND id='").append(strQLID).append("'");
						List qlList = this.baseCommonDao.getDataList(BDCS_QL_GZ.class, builerql.toString());
						if (!qlList.isEmpty() && gxbList.size()>0) {
							BDCS_QL_GZ bdcs_ql_gz = (BDCS_QL_GZ) qlList.get(0);
							if (bdcs_ql_gz != null) {
								if (bdcs_ql_gz.getCZFS().equals(ConstValue.CZFS.GTCZ.Value)) {
									StringBuilder strBuilder = new StringBuilder();
									StringBuilder builerqlr = new StringBuilder();
									builerqlr.append("XMBH='").append(strXMBH).append("' AND QLID='").append(strQLID)
											.append("'");
									List<BDCS_QLR_GZ> qlrList = this.baseCommonDao.getDataList(BDCS_QLR_GZ.class,
											builerqlr.toString());
									for (BDCS_QLR_GZ qlr : qlrList) {
										strBuilder.append(qlr.getQLRMC() + ",");
									}
									String strQLRMC = "";
									if (strBuilder.toString().length() > 0) {
										strQLRMC = strBuilder.toString().substring(0, strBuilder.length() - 1);
									}
									djfzxxBo.setQLR(strQLRMC);
								} else {
									StringBuilder strBuilder = new StringBuilder();
									StringBuilder builerqlr = new StringBuilder();
									builerqlr.append("XMBH='").append(strXMBH).append("' AND id='").append(strQLRID)
											.append("'");
									List<BDCS_QLR_GZ> qlrList = this.baseCommonDao.getDataList(BDCS_QLR_GZ.class,
											builerqlr.toString());
									for (BDCS_QLR_GZ qlr : qlrList) {
										strBuilder.append(qlr.getQLRMC() + ",");
									}
									String strQLRMC = "";
									if (strBuilder.toString().length() > 0) {
										strQLRMC = strBuilder.toString().substring(0, strBuilder.length() - 1);
									}
									djfzxxBo.setQLR(strQLRMC);
								}
							}
						}
						StringBuilder builerfz = new StringBuilder();
						builerfz.append("XMBH='").append(strXMBH).append("' AND HFZSH='").append(strBDCQZH).append("'");
						List fzList = this.baseCommonDao.getDataList(BDCS_DJFZ.class, builerfz.toString());
						if (fzList!=null && fzList.size() > 0) {
							String lzr = ((BDCS_DJFZ) fzList.get(0)).getLZRXM();
							Date lzsj = ((BDCS_DJFZ) fzList.get(0)).getFZSJ();
							if ((!lzr.isEmpty()) && (lzr != null) && (lzsj != null))
								lzsj.toString().isEmpty();
							djfzxxBo.setLZR(lzr);
							djfzxxBo.setLZSJ(lzsj);
							djfzxxBo.setSFFZ("是");
							djfzxxBo.setCZLX("撤销");
							djfzxxBo.setZSID(((BDCS_DJFZ) fzList.get(0)).getId());
							djfzxxBo.setM_lzrzjh(((BDCS_DJFZ) fzList.get(0)).getLZRZJHM());
							if(strDJDYID!=null){
								List<BDCS_DJDY_GZ> djdys = this.baseCommonDao.getDataList(BDCS_DJDY_GZ.class, " XMBH = '" + xmbh + "' AND DJDYID='"+strDJDYID+"'");
								if (djdys!=null && djdys.size() > 0) {
									String bdcdylx = djdys.get(0).getBDCDYLX();
									String bdcdyid = djdys.get(0).getBDCDYID();
									BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
									RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcdyid);
									if(unit!=null){
										String bdcdyh = unit.getBDCDYH();
										if(bdcdyh!=null){
											djfzxxBo.setBDCDYH(bdcdyh);
										}
										String zl = unit.getZL();
										if(zl!=null){
											djfzxxBo.setZl(zl);
										}
									}
								}
							}
							djfzxxList.add(djfzxxBo);
						}
					}
				}
			}
		}
		Long totalCount = Long.valueOf(this.baseCommonDao.getCountByFullSql(
				" from BDCK.BDCS_ZS_GZ Where length(bDCQZH)>0 and " + ProjectHelper.GetXMBHCondition(xmbh)));
		msg.setTotal(totalCount.longValue());
		msg.setRows(djfzxxList);
		return msg;
	}

	/**
	 * 根据不动产的自然幢信息匹配Mis库里的在建工程信息
	 * @author Hks
	 * @param zcs
	 * @param zl
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List matchFcMisk(String id,String zl,String type){
		long count = 0;
		String zrzType="zrz";
		String houseType="h";
		StringBuffer builderWhereH = new StringBuffer();
		if(type.equals("zrz")){
			List<Tree> resulttrees=new ArrayList<Tree>();
			Tree treeNode = new Tree();

			if (!StringHelper.isEmpty(zl)){
				builderWhereH.append(" and FWZL like '%" +zl+"%'");
			}
			treeNode.setText(zl);
			treeNode.setId(id);
			treeNode.setState(State.open);
			treeNode.setType(zrzType);
			resulttrees.add(treeNode);

			// 查询房产业务库的某一幢下的所有房屋信息
			String fromSql = " FROM FDCMAIN.RS_SYQFWJBXX WHERE 1=1 "+builderWhereH.toString();
			String fullSql = "SELECT * " + fromSql +builderWhereH +"order by fwzl,dy,fh";
			List<Map> houses = new ArrayList<Map>();
			houses =baseCommonDao.getDataListByFullSql(fullSql);
			if(houses == null || houses.size() == 0)
				return resulttrees;
			List<Tree> children_houses=new ArrayList<Tree>();
			for(Map house:houses)
			{
				Tree housetree=new Tree();
				treeNode.setTag1((String)house.get("CLH"));//将CLH作为幢的标识
				housetree.setId((String)house.get("SJBH"));
				housetree.setState(State.closed);
				housetree.setText((String)house.get("FH"));
				housetree.setType(houseType);
				children_houses.add(housetree);
			}
			treeNode.setChildren(children_houses);
			return resulttrees;
		}
		if(type.equals("h"))
		{
			List<Tree> resulttrees=new ArrayList<Tree>();
			StringBuilder sqlSB=new StringBuilder();
			sqlSB.append(" AND");
			sqlSB.append(" SJBH ='");
			sqlSB.append(id);
			sqlSB.append("'");
			// 查询房产业务库的某一个房屋下的权利信息
			List qls = new ArrayList();
			qls.add("FDCMAIN.RS_ZJTXQLXX");//在建工程他项权利信息
			qls.add("FDCMAIN.RS_YGDJXX");//业务预告登记信息
			qls.add("FDCMAIN.RS_YGTXQLXX");//预购商品房他项权利信息

			qls.add("FDCMAIN.RS_DYTXQLXX");//房产抵押他项权利信息
			qls.add("FDCMAIN.RS_YCFGLXX");//(预)查封关联信息


			List<Map> syqrs = new ArrayList<Map>();
			for (int i = 0; i < qls.size(); i++) {
				String fromSql = " FROM "+qls.get(i).toString()+" WHERE 1=1 ";
				String fullSql = "SELECT * " + fromSql + sqlSB.toString();
				syqrs = baseCommonDao.getDataListByFullSql(fullSql);
				if(syqrs == null || syqrs.size() == 0){
					continue;
				}
				else{
					String ql = qls.get(i).toString();
					//房地产抵押他项权利信息
					/*if(ql.equals("FDCMAIN.RS_DYTXQLXX")){
						for(Map syqr:syqrs){
							Tree qltree=new Tree();
							qltree.setId((String)syqr.get("SJBH"));
							qltree.setState(State.closed);
							qltree.setText("房产抵押");
							qltree.setTag1((String)syqr.get("DYR"));
							qltree.setTag2((String)syqr.get("TXQLR")+"(他项权证号："+syqr.get("TXQZH")+")");
							qltree.setTag3((String)syqr.get("SYQZH"));
							qltree.setType("ql");
							resulttrees.add(qltree);
						}
						break;
					}*/
					//(预)查封关联信息
					if(ql.equals("FDCMAIN.RS_YCFGLXX")){
						for(Map syqr:syqrs){
							Tree qltree=new Tree();
							qltree.setId((String)syqr.get("SJBH"));
							qltree.setState(State.closed);
							qltree.setText("预（查封）");
							qltree.setTag1((String)syqr.get("SQZXR"));
							qltree.setTag2((String)syqr.get("CQR"));
							qltree.setTag3((String)syqr.get("ZXDW"));
							qltree.setBdcqzh((String)syqr.get("QZHM"));//查封抵押权证号
							qltree.setType("ql");
							resulttrees.add(qltree);
						}
						break;
					}

					//在建工程他项(抵押)权利信息
					if(ql.equals("FDCMAIN.RS_ZJTXQLXX")){
						for(Map syqr:syqrs){
							Tree qltree=new Tree();
							qltree.setId((String)syqr.get("SJBH"));
							qltree.setState(State.closed);
							qltree.setText("在建工程权利信息");
							qltree.setTag1((String)syqr.get("DYQR"));
							qltree.setTag2((String)syqr.get("DYR"));
							qltree.setTag3((String)syqr.get("DYBH"));
							qltree.setType("ql");
							resulttrees.add(qltree);
						}
						break;
					}
					//业务预告登记信息
					if(ql.equals("FDCMAIN.RS_YGDJXX")){
						for(Map syqr:syqrs){
							Tree qltree=new Tree();
							qltree.setId((String)syqr.get("SJBH"));
							qltree.setState(State.closed);
							qltree.setText((String)syqr.get("YGDJZL"));
							qltree.setTag1((String)syqr.get("QLR"));
							qltree.setTag2((String)syqr.get("YWR"));
							qltree.setTag3((String)syqr.get("DJZMH"));
							qltree.setType("ql");
							resulttrees.add(qltree);
						}
						break;
					}
					//预购商品房他项权利信息
					if(ql.equals("FDCMAIN.RS_YGTXQLXX")){
						for(Map syqr:syqrs){
							Tree qltree=new Tree();
							qltree.setId((String)syqr.get("SJBH"));
							qltree.setState(State.closed);
							qltree.setText("预购商品房抵押权利信息");
							qltree.setTag1((String)syqr.get("DYQR"));
							qltree.setTag2((String)syqr.get("DYR"));
							qltree.setTag3((String)syqr.get("YJCS"));
							qltree.setBdcqzh((String)syqr.get("DYBH"));//在建抵押证明书编号
							qltree.setType("ql");
							resulttrees.add(qltree);
						}
						break;
					}


				}
			}

			// resulttree.setChildren(children_qls);
			return resulttrees;
		}
		//获取附属权利
		if(type.equals("ql")){
			List<Tree> resulttrees=new ArrayList<Tree>();
			StringBuilder sqlSB=new StringBuilder();
			sqlSB.append(" AND");
			sqlSB.append(" SJBH ='");
			sqlSB.append(id);
			sqlSB.append("'");
			String fromSql = " FROM FDCMAIN.RS_DYTXQLXX WHERE 1=1 ";//获取在建工程的抵押他项信息
			String fullSql = "SELECT * " + fromSql + sqlSB.toString();
			List<Map> fsqls = baseCommonDao.getDataListByFullSql(fullSql);
			if(fsqls == null || fsqls.size() == 0)
				return resulttrees;
			for (Map fsql : fsqls) {
				Tree fsqltree = new Tree();
				fsqltree.setId((String)fsql.get("SJBH"));
				fsqltree.setState(State.closed);
				fsqltree.setText((String)fsql.get("QLZL") + "（");
				fsqltree.setTag1("抵押人："+(String)fsql.get("DYR"));
				fsqltree.setTag2("他项权利人："+(String)fsql.get("TXQLR")+"他项权证号："+fsql.get("TXQZH"));
				fsqltree.setTag3((String)fsql.get("FJ"));//附记
				fsqltree.setBdcqzh((String)fsql.get("SYQZH"));//所有权证号
				fsqltree.setType("qlr");
				resulttrees.add(fsqltree);
			}
			return resulttrees;
		}

		return new ArrayList<Tree>();
	}
	
	/**
	 * 获取房屋层数不明确数量
	 * @author HKS
	 * @date 2016-09-13 16:00
	 * @param zl
	 * @return
	 */
	public long getblurfws(String zl){
		StringBuffer build_where = new StringBuffer();
		build_where.append(" AND FWZL like '"+ zl +"%'" );
		
		build_where.append(" AND DABZ = '1' AND (FWSZCS is null");
		build_where.append(" or FWSZCS = '架空层'" );
		build_where.append(" or instr(fwszcs,'，') > 0" );
		build_where.append(" or fwszcs like '_-_'" );
		build_where.append(" or instr(fwszcs,'、') > 0" );
		build_where.append(" or instr(fwszcs,'.') > 0" );
		build_where.append(" or instr(fwszcs,'\') > 0" );
		build_where.append(" or instr(fwszcs,'～') > 0" );
		build_where.append(" or instr(fwszcs,'至') > 0" );
		
		build_where.append(" )" );
		String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1" + build_where;
		long blurCount = baseCommonDao.getCountByFullSql(fromsql);
		return blurCount;
	}
	/**
	 * 获取档案库的自然幢信息
	 * @author Hks
	 * @param zcs
	 * @param zl
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	public List getDasjZRZ(String id,String zl,String type){
		String zrzType="zrz";
		String houseType="h";
		StringBuffer builderWhereH = new StringBuffer();
		if(type.equals("zrz")){
			List<Tree> resulttrees=new ArrayList<Tree>();
			Tree treeNode = new Tree();

			if (!StringHelper.isEmpty(zl)){
				builderWhereH.append(" and FWZL like '" +zl+"%'");
			}
			builderWhereH.append(" and DABZ = '1'");//当前手
			treeNode.setText(zl);
			treeNode.setId(id);
			treeNode.setState(State.open);
			treeNode.setType(zrzType);
			resulttrees.add(treeNode);

			// 查询房产业务库的某一幢下的所有房屋信息
			String fromSql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
			String fullSql = "SELECT sjbh,fwcs,fwszcs,fwzl,fh,fj" + fromSql +builderWhereH +" order by fwzl";
			List<Map> houses = new ArrayList<Map>();
			houses =baseCommonDao.getDataListByFullSql(fullSql);
			if(houses == null || houses.size() == 0)
				return resulttrees;
			List<Tree> children_cs=new ArrayList<Tree>();
			
			String maxfwcssql = "SELECT max(fwcs) as FWCS" + fromSql +builderWhereH +" and asciistr(fwcs) not like '%\\%'" + "group by fwcs order by count(*) Desc";
			List<Map>  max_fwcs =baseCommonDao.getDataListByFullSql(maxfwcssql);
			String fwcs = (String) max_fwcs.get(0).get("FWCS");
			int fwzcs = Integer.parseInt(fwcs);
			//for(Map house : houses){
				//String szc = (String) house.get("FWSZCS");
				String fjsql = "SELECT sjbh,fwcs,fwszcs,fwzl,fh,fj" + fromSql +builderWhereH +" AND FJ LIKE '%地下%'";

				List<Map> isdxcs =baseCommonDao.getDataListByFullSql(fjsql);
				
				if(isdxcs != null && isdxcs.size() > 0){
					String dxc = (String) isdxcs.get(0).get("FJ");
					if(dxc.indexOf("地下三层") != -1 || dxc.indexOf("地下3层") != -1 ){
						int dxcs = 3;

						int dscs = fwzcs - dxcs;
						for(int i = dscs ;i > 0;i--){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'" );
							if(i < 10){
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ i +"' OR"+" FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + 0 +i+"'");
							}else{
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ i + "'");
							}
							buildWhere.append(" AND DABZ = '1'" );
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+i+"");
							}
							else{
								floortree.setId(i+"");
							}
							floortree.setState(State.closed);
							floortree.setText("第"+i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}

						for(int i = 1 ;i <= dxcs;i++){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'");
							if(i < 10){
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ -i +"' OR"+" FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND  FWSZCS = '负"+i+"层'");
							}else{
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ -i + "'");
							}
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+"-"+i+"");
							}
							else{
								floortree.setId("-"+i);
							}
							floortree.setState(State.closed);
							floortree.setText("第"+-i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						/**
						 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
						 */
						long blurcount = getblurfws(zl);
						Tree BlurFloorTree=new Tree();
						BlurFloorTree.setId("mhc");
						BlurFloorTree.setState(State.closed);
						BlurFloorTree.setText("层数不明专放层"+"（"+blurcount+"套房）");
						BlurFloorTree.setType("mhc");
						children_cs.add(BlurFloorTree);
						treeNode.setChildren(children_cs);
						return resulttrees;

					}
					if( dxc.indexOf("地下三层") == -1 && dxc.indexOf("地下3层") == -1 && dxc.indexOf("地下二层") != -1 || dxc.indexOf("地下2层") != -1 ){
						int dxcs = 2;
						int dscs = fwzcs - dxcs;
						for(int i = dscs ;i > 0;i--){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'");
							if(i < 10){
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ i +"' OR"+" FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + 0 +i+"'");
							}else{
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ i + "'");
							}
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+i+"");
							}
							else{
								floortree.setId(i+"");
							}
							floortree.setState(State.closed);
							floortree.setText("第"+i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}

						for(int i = 1 ;i <= dxcs;i++){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'");
							if(i < 10){
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ -i +"' OR"+" FWZL = '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + -0 +i+ "'");
							}else{
								buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ -i +"'");
							}
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+"-"+i+"");
							}
							else{
								floortree.setId("-"+i);
							}
							floortree.setState(State.closed);
							floortree.setText("第"+-i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						
						/**
						 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
						 */
						long blurcount = getblurfws(zl);
						Tree BlurFloorTree=new Tree();
						BlurFloorTree.setId("mhc");
						BlurFloorTree.setState(State.closed);
						BlurFloorTree.setText("层数不明专放层"+"（"+blurcount+"套房）");
						BlurFloorTree.setType("mhc");
						children_cs.add(BlurFloorTree);
						treeNode.setChildren(children_cs);
						return resulttrees;
					}

					if(dxc.indexOf("地下二层") == -1 && dxc.indexOf("地下2层") == -1 && dxc.indexOf("地下三层") == -1 && dxc.indexOf("地下3层") == -1 && dxc.indexOf("地下一层") != -1 || dxc.indexOf("地下1层") != -1 ){
						int dxcs = 1;
						int dscs = fwzcs - dxcs;
						for(int i = dscs ;i > 0;i--){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'");
							buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ i +"' OR "+"FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + 0 +i+"'");
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+i+"");
							}
							else{
								floortree.setId(i+"");
							}
							floortree.setState(State.closed);
							floortree.setText("第"+i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}


						for(int i = 1 ;i <= dxcs;i++){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'");
							buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ -i +"' OR " +"FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + "-0" +i+"'");
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+"-"+i+"");
							}
							else{
								floortree.setId("-" + i);
							}
							floortree.setState(State.closed);
							floortree.setText("第"+-i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						
						/**
						 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
						 */
						long blurcount = getblurfws(zl);
						Tree BlurFloorTree=new Tree();
						BlurFloorTree.setId("mhc");
						BlurFloorTree.setState(State.closed);
						BlurFloorTree.setText("层数不明专放层"+"（"+blurcount+"套房）");
						BlurFloorTree.setType("mhc");
						children_cs.add(BlurFloorTree);
						
						treeNode.setChildren(children_cs);
						return resulttrees;
					}
					
					/**
					 * 有架空层的
					 */
					if( dxc.indexOf("地下三层") == -1 && dxc.indexOf("地下3层") == -1 && dxc.indexOf("地下一层") != -1 || dxc.indexOf("地下1层") != -1 || dxc.indexOf("架空层") != -1){
						int dxcs = 2;
						int dscs = fwzcs - dxcs;
						for(int i = dscs ;i > 0;i--){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'");
							buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ i +"' OR "+"FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + 0 +i+"'");
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+i+"");
							}
							else{
								floortree.setId(i+"");
							}
							floortree.setState(State.closed);
							floortree.setText("第"+i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}


						for(int i = 1 ;i <= dxcs;i++){
							StringBuffer buildWhere = new StringBuffer();
							buildWhere.append(" AND FWZL like '"+ zl +"%'");
							buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ -i +"' OR " +"FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + "-0" +i+"'");
							String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
							String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
							List<Map> fwlist = new ArrayList<Map>();
							fwlist =baseCommonDao.getDataListByFullSql(fullsql);
							Tree floortree=new Tree();
							if(fwlist.size() > 0){
								floortree.setId(fwlist.get(0).get("SJBH")+","+"-"+i+"");
							}
							else{
								floortree.setId("-" + i);
							}
							floortree.setState(State.closed);
							floortree.setText("第"+-i+"层" +"（"+ fwlist.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						
						/**
						 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
						 */
						long blurcount = getblurfws(zl);
						Tree BlurFloorTree=new Tree();
						BlurFloorTree.setId("mhc");
						BlurFloorTree.setState(State.closed);
						BlurFloorTree.setText("层数不明专放层"+"（"+blurcount+"套房）");
						BlurFloorTree.setType("mhc");
						children_cs.add(BlurFloorTree);
						
						treeNode.setChildren(children_cs);
						return resulttrees;
					}
				}
				else {
					for(int i = fwzcs ;i > 0;i--){
						StringBuffer buildWhere = new StringBuffer();
						buildWhere.append("	AND DABZ = '1' AND FWZL like '"+ zl +"%'");
						if(i < 10){
							buildWhere.append(" AND FWSZCS = '"+ i +"' OR "+"FWZL like '"+ zl +"%'"+" AND DABZ = '1' AND FWSZCS = '" + 0 +i+"'");
						}else{
							buildWhere.append(" AND DABZ = '1' AND FWSZCS = '"+ i +"'");
						}
						buildWhere.append(" AND DABZ = '1'" );//当前手标志
						String fromsql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
						String fullsql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh" + fromsql +buildWhere +" order by fwzl";
						List<Map> fwlist = new ArrayList<Map>();
						fwlist =baseCommonDao.getDataListByFullSql(fullsql);
						Tree floortree=new Tree();
						if(fwlist.size() > 0){
							floortree.setId(fwlist.get(0).get("SJBH")+","+i+"");
						}
						else{
							floortree.setId(i+"");
						}
						floortree.setState(State.closed);
						floortree.setText("第"+i+"层" +"（"+ fwlist.size()+"套房）");
						floortree.setType("c");
						children_cs.add(floortree);
					}

					/**
					 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
					 */
					long blurcount = getblurfws(zl);
					Tree BlurFloorTree=new Tree();
					BlurFloorTree.setId("mhc");
					BlurFloorTree.setState(State.closed);
					BlurFloorTree.setText("层数不明专放层"+"（"+blurcount+"套房）");
					BlurFloorTree.setType("mhc");
					children_cs.add(BlurFloorTree);
					treeNode.setChildren(children_cs);
					return resulttrees;
				}	
		}
		if(type.equals("c"))
		{
			List<Tree> resulttrees=new ArrayList<Tree>();

			if (!StringHelper.isEmpty(zl)){
				builderWhereH.append(" and FWZL like '" +zl+"%'");
			}
			if (!StringHelper.isEmpty(id) && id.indexOf(",") != -1){
				String[] sourceStrArray = id.split(",");
				String sjbh = sourceStrArray[0];
				String szc = sourceStrArray[1];
				builderWhereH.append("and fwszcs = '"+ szc+"'" + " AND DABZ = '1'");
				builderWhereH.append(" or FWZL like '"+zl+"%'" +" and fwszcs = '"+"0"+szc+"'" + " AND DABZ = '1'");
			}else{
				return resulttrees;
			}
			// 查询房产业务库的某一幢下的所有房屋信息
			String fromSql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
			String fullSql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh,sjcqbs,tfsj" + fromSql +builderWhereH +" order by fwzl";
			List<Map> houses = new ArrayList<Map>();
			houses =baseCommonDao.getDataListByFullSql(fullSql);
			if(houses == null || houses.size() == 0)
				return resulttrees;
			List<Tree> children_houses=new ArrayList<Tree>();
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for(Map house:houses)
			{
				Tree housetree=new Tree();
				housetree.setId((String)house.get("SJBH")+","+house.get("SYQZH"));
				housetree.setState(State.closed);
				Date tfsj = (Date) house.get("TFSJ");
				
				String sj = "";
				if(tfsj != null){
					sj = bartDateFormat.format(tfsj);
				}
				housetree.setText(house.get("FWZL") + "（房号："+ house.get("FWFH")+",登记日期："+sj+",分户号："+ house.get("FH") +"）");
				//,登记日期："+house.get("TFSJ")+"
				housetree.setTag1((String)house.get("SJCQBS"));//已抽户的标识码
				housetree.setType(houseType);
				children_houses.add(housetree);
				resulttrees.add(housetree);
			}
			return resulttrees;
		}
		
		/**
		 * 层数不明层，单独做一个房屋查询
		 */
		if(type.equals("mhc"))
		{
			List<Tree> resulttrees=new ArrayList<Tree>();

			if (!StringHelper.isEmpty(zl)){
				builderWhereH.append(" and FWZL like '" +zl+"%'");
			}
			if (!StringHelper.isEmpty(id)){
				builderWhereH.append(" AND DABZ = '1' AND (FWSZCS is null");
				builderWhereH.append(" or FWSZCS = '架空层'" );
				builderWhereH.append(" or instr(fwszcs,'，') > 0" );
				builderWhereH.append(" or fwszcs like '_-_'" );
				builderWhereH.append(" or instr(fwszcs,'、') > 0" );
				builderWhereH.append(" or instr(fwszcs,'.') > 0" );
				builderWhereH.append(" or instr(fwszcs,'\') > 0" );
				builderWhereH.append(" or instr(fwszcs,'～') > 0" );
				builderWhereH.append(" or instr(fwszcs,'至') > 0" );
				
				builderWhereH.append(" )" );
			}
			// 查询房产业务库的某一幢下的所有房屋信息
			String fromSql = " FROM FDC_DAK.EP_FJSYQJBXX WHERE 1=1";
			String fullSql = "SELECT clh,sjbh,syqzh,fwcs,fwszcs,fwzl,fwfh,fh,sjcqbs,tfsj,jzmj" + fromSql +builderWhereH +" order by fwzl";
			List<Map> houses = new ArrayList<Map>();
			houses =baseCommonDao.getDataListByFullSql(fullSql);
			if(houses == null || houses.size() == 0)
				return resulttrees;
			List<Tree> children_houses=new ArrayList<Tree>();
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for(Map house:houses)
			{
				Tree housetree=new Tree();
				housetree.setId((String)house.get("SJBH")+","+house.get("SYQZH"));
				housetree.setState(State.closed);
				Date tfsj = (Date) house.get("TFSJ");
				
				String sj = "";
				if(tfsj != null){
					sj = bartDateFormat.format(tfsj);
				}
				housetree.setText(house.get("FWZL") + "（房号："+ house.get("FWFH")+",所在层："+house.get("FWSZCS")+",登记日期："+sj+",建筑面积："+ house.get("JZMJ") +"㎡）");
				//,登记日期："+house.get("TFSJ")+"
				housetree.setTag1((String)house.get("SJCQBS"));//已抽户的标识码
				housetree.setType(houseType);
				children_houses.add(housetree);
				resulttrees.add(housetree);
			}
			return resulttrees;
		}
		
		//获取权利
		if(type.equals("h")){
			long syqcount = 0;
			long dyqcount = 0;
			long cfcount = 0;
			String[] queryPrams = id.split(",");
			String sjbh = queryPrams[0];
			String syqzh = queryPrams[1];
			String syqsql = " from FDC_DAK.EP_FJSYQJBXX WHERE SJBH= '"+sjbh+ "'";
			String dyqsql = " from FDC_DAK.EP_FJTXQJBXX WHERE ZXRQ IS NULL AND (SJBH='"+sjbh+"' or instr(SYQZH,'"+syqzh+"') > 0 or instr(SYQZH,'"+syqzh+"号') > 0)";
			String cfsql = " from FDC_DAK.EP_FJCFQJBXX WHERE SJBH= '"+sjbh+ "'" + " AND JFRQ IS NULL";
			syqcount = baseCommonDao.getCountByFullSql(syqsql);

			List<Tree> resulttrees = new ArrayList<Tree>();
			if(syqcount>0){
				List<Map> syqs = baseCommonDao.getDataListByFullSql("select *"+syqsql);
				if (syqs == null || syqs.size() == 0)
					return resulttrees;
				List<Tree> children_houses = new ArrayList<Tree>();
				for (Map ql : syqs) {
					Tree qltree = new Tree();
					qltree.setId((String) ql.get("SJBH"));
					qltree.setState(State.closed);
					qltree.setText("房屋所有权" + "（所有权人："
							+ ql.get("SYQR") + ",所有权证号：" + ql.get("SYQZH") + "）");
					qltree.setType("syqgyqk");
					children_houses.add(qltree);
					resulttrees.add(qltree);
				}
				dyqcount = baseCommonDao.getCountByFullSql(dyqsql);

				if(dyqcount > 0)
				{
					List<Map> dyqs = baseCommonDao.getDataListByFullSql("select *"+dyqsql);
					if (dyqs == null || dyqs.size() == 0){
						return resulttrees;
					}else{
						//List<Tree> children_houses = new ArrayList<Tree>();
						for (Map ql : dyqs) {
							String dyr = (String) ql.get("DYR");
							dyr = dyr.replace(" ","");
							if(dyr != null){
								for(Map syq_ql : syqs){
									String syqr = (String) syq_ql.get("SYQR");
									syqr = syqr.replace(" ","");
									if(dyr.equals(syqr) || dyr.indexOf(syqr) != -1){
										Tree qltree = new Tree();
										qltree.setId((String) ql.get("SJBH"));
										qltree.setState(State.closed);
										Date tfrq = (Date) ql.get("TFRQ");
										SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
										String dysj = "";
										if(tfrq != null){
											dysj = bartDateFormat.format(tfrq);
										}
										qltree.setText("抵押权" + "（他项权利人：" + ql.get("TXQLR")+ ",抵押人：" + ql.get("DYR") + ",填发时间："+dysj+"）");
										qltree.setType("dygyqr");
										children_houses.add(qltree);
										resulttrees.add(qltree);
									}
								}
							}
						}
					}
				}
				cfcount = baseCommonDao.getCountByFullSql(cfsql);
				if(cfcount == 0 ){
					return resulttrees;
				}else if(cfcount > 0){

					List<Map> cfqs = baseCommonDao.getDataListByFullSql("select *"+cfsql);
					if (cfqs == null || cfqs.size() == 0)
						return resulttrees;

					//List<Tree> children_houses = new ArrayList<Tree>();
					for (Map cfql : cfqs) {
						Tree qltree = new Tree();
						String jfrq = (String) cfql.get("JFRQ");
						if(jfrq == null){
							qltree.setId((String) cfql.get("SJBH"));
							qltree.setState(State.closed);
							qltree.setText("第"+cfql.get("XH")+"笔查 封" + "（查封机构："
									+ cfql.get("ZXFY") + ",法律文书号：" + cfql.get("FLWSH") + ",查封通知单：" + cfql.get("CFTZD")+"）");
							qltree.setType("cf");
							children_houses.add(qltree);
							resulttrees.add(qltree);
						}
					}

				}

				return resulttrees;
			}

		}

		if(type.equals("syqgyqk")){
			List<Tree> resulttrees = new ArrayList<Tree>();
			String gyqsql = " from FDC_DAK.EP_FJSYQGYXX WHERE SJBH= '"+id+ "'";

			long gyqcount = baseCommonDao.getCountByFullSql(gyqsql);

			if(gyqcount>0){
				List<Map> gyqs = baseCommonDao.getDataListByFullSql("select *"+gyqsql);
				if (gyqs == null || gyqs.size() == 0)
					return resulttrees;

				for (Map ql : gyqs) {
					Tree qyqrtree = new Tree();
					qyqrtree.setId((String) ql.get("SJBH"));
					qyqrtree.setState(State.closed);
					
					qyqrtree.setText("共有权信息"+"（共有权人："
							+ ql.get("GYQR") + ",共有权证号：" + ql.get("GYQZH")+"）");
					qyqrtree.setType("gyql");
					resulttrees.add(qyqrtree);
				}
			}
			return resulttrees;

		}
		if(type.equals("dygyqr")){

			List<Tree> resulttrees = new ArrayList<Tree>();
			String dyqgysql = "from FDC_DAK.EP_DYQRGYXX WHERE SJBH= '"+id+ "'";

			List<Map> dygyqs = baseCommonDao.getDataListByFullSql("select *"+dyqgysql);
			if (dygyqs == null || dygyqs.size() == 0)
				return resulttrees;
			//List<Tree> children_houses = new ArrayList<Tree>();
			for (Map ql : dygyqs) {
				Tree qltree = new Tree();
				qltree.setId((String) ql.get("SJBH"));
				qltree.setState(State.closed);
				Date tfrq = (Date) ql.get("TFRQ");
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dysj = "";
				if(tfrq != null){
					dysj = bartDateFormat.format(tfrq);
				}
				qltree.setText(ql.get("SJBH") + "（抵押权人："
						+ ql.get("DYQR") + ",权证号：" + ql.get("QZH")+ ",填发日期："+dysj+"）");
				qltree.setType("ql");
				//children_houses.add(qltree);
				resulttrees.add(qltree);
			}
		}

		return new ArrayList<Tree>();
	}
	
	public long getGegisterBlurFW(String id){
		StringBuffer querywhere = new StringBuffer();
		querywhere.append("ZRZBDCDYID = '"+id+"'");
		querywhere.append("AND ( szc is null");
		querywhere.append(" or szc = 'null'");
		querywhere.append(" or instr(szc,'、') > 0");
		querywhere.append(" or instr(szc,',') > 0");
		querywhere.append(" or instr(szc,'，') > 0");
		querywhere.append(" or szc like '_-_'");
		querywhere.append(" or instr(szc,'.') > 0" );
		querywhere.append(" or instr(szc,'\') > 0" );
		querywhere.append(" or instr(szc,'～') > 0" );
		querywhere.append(" or instr(szc,'至') > 0" );
		querywhere.append(")");
		List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,querywhere.toString());
		
		return houses.size();
		
	}
	
	
	@Override
	public List GetRegisterZRZ(String id,String type) {

		String zrzType="zrz",houseType="h",qlType="ql",qlrType="qlr";
		if(type.equals("zrz"))
		{
			List<Tree> resulttrees=new ArrayList<Tree>();
			Tree node=new Tree(); 
			List<BDCS_ZRZ_XZ> zrzs = this.baseCommonDao.getDataList(BDCS_ZRZ_XZ.class, "BDCDYID = '"+id + "'");
			if(zrzs==null||zrzs.size()==0)
				return resulttrees;

			node.setId(zrzs.get(0).getId());
			node.setText(zrzs.get(0).getZL()+"("+zrzs.get(0).getBDCDYH()+")");
			node.setState(State.open);
			node.setType(zrzType);

			List<Tree> children_cs=new ArrayList<Tree>();
			int dxcs = zrzs.get(0).getDXCS();
			long gblurcount = 0;
			if(dxcs > 0){
				for(int i = 0; i < zrzs.size(); i++){
					if(i == 3 || i == 03){
						int dscs = zrzs.get(0).getDSCS();
						for(int j = dscs ;j > 0;j--){
							List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+id+"'"+" AND SZC='"+j+"'" + " or ZRZBDCDYID = '"+id+"'"+" AND SZC='"+"0"+j+"'");
							Tree floortree=new Tree();
							floortree.setId(id + "," +j);
							floortree.setState(State.closed);
							floortree.setText("第"+j+"层"+"（"+houses.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						for(int k = 1 ;k <= dxcs;k++){
							List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+id+"'"+" AND SZC='"+-k+"'");
							Tree floortree=new Tree();
							floortree.setId(id+","+-k);
							floortree.setState(State.closed);
							floortree.setText("第"+-k+"层"+"（"+houses.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						
						/**
						 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
						 */
						gblurcount = getGegisterBlurFW(id);
						Tree BlurFloorTree=new Tree();
						BlurFloorTree.setId(id);
						BlurFloorTree.setState(State.closed);
						BlurFloorTree.setText("层数不明专放层"+"（"+gblurcount+"套房）");
						BlurFloorTree.setType("mhc");
						children_cs.add(BlurFloorTree);
						
						node.setChildren(children_cs);
						resulttrees.add(node);
						return resulttrees;
					}

					if(i == 2 || i == 02 && i != 3 || i != 03){
						int dscs = zrzs.get(0).getDSCS();
						String bdcdyid = zrzs.get(0).getId();
						for(int j = dscs ;j > 0;j--){
							List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+j+"'" + " or "+ "ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+"0"+j+"'");
							Tree floortree=new Tree();
							floortree.setId(bdcdyid + "," +j);
							floortree.setState(State.closed);
							floortree.setText("第"+j+"层"+"（"+houses.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						for(int k = 1 ;k <= dxcs;k++){
							List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+-k+"'");
							Tree floortree=new Tree();
							floortree.setId(bdcdyid + "," + -k);
							floortree.setState(State.closed);
							floortree.setText("第"+-k+"层"+"（"+houses.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						
						
						/**
						 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
						 */
						gblurcount = getGegisterBlurFW(id);
						Tree BlurFloorTree=new Tree();
						BlurFloorTree.setId(id);
						BlurFloorTree.setState(State.closed);
						BlurFloorTree.setText("层数不明专放层"+"（"+gblurcount+"套房）");
						BlurFloorTree.setType("mhc");
						children_cs.add(BlurFloorTree);
						node.setChildren(children_cs);
						resulttrees.add(node);
						return resulttrees;
					}

					if(i == 1 || i == 01 && i != 2 || i != 02 && i != 3 || i != 03){
						int dscs = zrzs.get(0).getDSCS();
						String bdcdyid = zrzs.get(0).getId();
						for(int j = dscs ;j > 0;j--){
							List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+j+"'" + " or " + "ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+"0"+j+"'");
							Tree floortree=new Tree();
							floortree.setId(bdcdyid + "," +j);
							floortree.setState(State.closed);
							floortree.setText("第"+j+"层"+"（"+houses.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						for(int k = 1 ;k <= dxcs;k++){
							List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+-k+"'");
							Tree floortree=new Tree();
							floortree.setId(bdcdyid + "," + -k);
							floortree.setState(State.closed);
							floortree.setText("第"+-k+"层"+"（"+houses.size()+"套房）");
							floortree.setType("c");
							children_cs.add(floortree);
						}
						
						
						/**
						 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
						 */
						gblurcount = getGegisterBlurFW(id);
						Tree BlurFloorTree=new Tree();
						BlurFloorTree.setId(id);
						BlurFloorTree.setState(State.closed);
						BlurFloorTree.setText("层数不明专放层"+"（"+gblurcount+"套房）");
						BlurFloorTree.setType("mhc");
						children_cs.add(BlurFloorTree);
						node.setChildren(children_cs);
						resulttrees.add(node);
						return resulttrees;
					}
				}
			}else{
				int fwzcs = zrzs.get(0).getZCS();
				String bdcdyid = zrzs.get(0).getId();
				for(int i = fwzcs ;i > 0;i--){
					List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+i+"'" + " or" + " ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC = '" + "0"+i+"'");
					Tree floortree=new Tree();
					floortree.setId(bdcdyid + "," +i);
					floortree.setState(State.closed);
					floortree.setText("第"+i+"层"+"（"+houses.size()+"套房）");
					floortree.setType("c");
					children_cs.add(floortree);
				}
				
				/**
				 * 外加一层---专门放那些层数不明的，有以下几种情况（1、私房  2、楼中楼  3、房产档案库中没有层数）
				 */
				gblurcount = getGegisterBlurFW(id);
				Tree BlurFloorTree=new Tree();
				BlurFloorTree.setId(id);
				BlurFloorTree.setState(State.closed);
				BlurFloorTree.setText("层数不明专放层"+"（"+gblurcount+"套房）");
				BlurFloorTree.setType("mhc");
				children_cs.add(BlurFloorTree);
				node.setChildren(children_cs);
				resulttrees.add(node);
				return resulttrees;
			}
		}
		if(type.equals("c")){
			List<Tree> resulttrees=new ArrayList<Tree>();
			String[] sourceStrArray = id.split(",");
			String bdcdyid = sourceStrArray[0];
			String szc = sourceStrArray[1];
			List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,"ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+szc+"' or "+"ZRZBDCDYID = '"+bdcdyid+"'"+" AND SZC='"+"0"+szc +"' order by zl,dyh");
			if(houses==null||houses.size()==0)
				return resulttrees;
			List<Tree> children_houses=new ArrayList<Tree>();
			for(BDCS_H_XZ house:houses)
			{
				Tree housetree=new Tree();
				housetree.setId(house.getBDCDYH());
				housetree.setState(State.closed);
				housetree.setText(house.getZL()+"（房号："+house.getFH()+"，户号："+house.getHH()+"）");
				housetree.setType(houseType);
				children_houses.add(housetree);
				resulttrees.add(housetree);

			}
			return resulttrees;
		}
		
		/**
		 * 层数不明的另开一个层数查询
		 */
		if(type.equals("mhc")){
			List<Tree> resulttrees=new ArrayList<Tree>();
			StringBuffer query_where = new StringBuffer();
			query_where.append("ZRZBDCDYID = '"+id+"'");
			query_where.append("AND ( szc is null");
			query_where.append(" or szc = 'null'");
			query_where.append(" or instr(szc,'、') > 0");
			query_where.append(" or instr(szc,',') > 0");
			query_where.append(" or instr(szc,'，') > 0");
			query_where.append(" or szc like '_-_'");
			query_where.append(" or instr(szc,'.') > 0" );
			query_where.append(" or instr(szc,'\') > 0" );
			query_where.append(" or instr(szc,'～') > 0" );
			query_where.append(" or instr(szc,'至') > 0" );
			query_where.append(")");
			query_where.append(" order by zl,dyh");
			List<BDCS_H_XZ> houses = this.baseCommonDao.getDataList(BDCS_H_XZ.class,query_where.toString());
			if(houses==null||houses.size()==0)
				return resulttrees;
			List<Tree> children_houses=new ArrayList<Tree>();
			for(BDCS_H_XZ house:houses)
			{
				Tree housetree=new Tree();
				housetree.setId(house.getBDCDYH());
				housetree.setState(State.closed);
				housetree.setText(house.getZL()+"（房号："+house.getFH()+"，所在层："+house.getSZC()+"，建筑面积："+house.getSCJZMJ()+"㎡）");
				housetree.setType(houseType);
				children_houses.add(housetree);
				resulttrees.add(housetree);
			}
			return resulttrees;
		}

		if(type.equals("h"))
		{
			List<Tree> resulttrees=new ArrayList<Tree>();
			StringBuilder sqlSB=new StringBuilder();
			sqlSB.append("BDCDYH='");
			sqlSB.append(id);
			sqlSB.append("'");
			List<BDCS_QL_XZ> qls = this.baseCommonDao.getDataList(BDCS_QL_XZ.class, sqlSB.toString());
			if(qls==null||qls.size()==0)
				return resulttrees;
			// List children_qls=new ArrayList<Tree>();
			for(BDCS_QL_XZ ql:qls)
			{
				Tree qltree=new Tree();
				qltree.setId(ql.getId());
				qltree.setState(State.closed);
				qltree.setText(ConstHelper.getNameByValue("DJLX", String.valueOf(ql.getDJLX()))+"-"+ConstHelper.getNameByValue("QLLX", String.valueOf(ql.getQLLX())));
				qltree.setType(qlType);
				resulttrees.add(qltree);
			}
			// resulttree.setChildren(children_qls);
			return resulttrees;
		}
		if(type.equals("ql"))
		{
			List<Tree> resulttrees=new ArrayList<Tree>();
			StringBuilder sqlSB=new StringBuilder();
			sqlSB.append("QLID='");
			sqlSB.append(id);
			sqlSB.append("'");
			List<BDCS_QLR_XZ> qlrs = this.baseCommonDao.getDataList(BDCS_QLR_XZ.class, sqlSB.toString());
			if(qlrs==null||qlrs.size()==0)
				return resulttrees;
			//List children_qlrs=new ArrayList<Tree>();
			for(BDCS_QLR_XZ qlr:qlrs)
			{
				Tree qlrtree=new Tree();
				qlrtree.setId(qlr.getId());
				qlrtree.setState(State.open);
				qlrtree.setText(qlr.getQLRMC()+"（不动产权证号："+qlr.getBDCQZH()+"）");
				qlrtree.setType(qlrType);
				resulttrees.add(qlrtree);
			}
			//resulttree.setChildren(children_qlrs);
			return resulttrees;
		}
		return  new ArrayList<Tree>();
	}
	/**
	 * 获取要抽取的自然幢信息
	 */
	@Override
	public Object GetImportZRZ(String id, String type,HttpServletRequest request) {
		String zrzType="zrz",houseType="h",qlType="ql",qlrType="qlr";
		String filepath = String.format("%s\\resources\\FC2DJXT\\%s.txt", request.getRealPath("/"),id);
		List<Tree> resulttrees=new ArrayList<Tree>();
		FileOutputStream fop = null;
		Writer outText = null;
		File file = null;
		try {
			file = new File(filepath);
			Object localObject2 = "";
			if (file.exists()) {
				file.delete();
			}
			fop = new FileOutputStream(file);
			outText = new OutputStreamWriter(fop,"gbk");

			List zrzs = this.baseCommonDao.getDataList(BDCS_ZRZ_XZ.class, "BDCDYID='" + id + "'");
			if ((zrzs == null) || (zrzs.size() == 0)) {
				localObject2 = JSON.toJSON(resulttrees);
				return localObject2;
			}Tree zrztree = new Tree();
			zrztree.setId(((BDCS_ZRZ_XZ)zrzs.get(0)).getId());
			zrztree.setText(((BDCS_ZRZ_XZ)zrzs.get(0)).getZL());
			zrztree.setType(zrzType);
			zrztree.setState(State.open);
			resulttrees.add(zrztree);

			outText.write(JSON.toJSONString(resulttrees));
			/*else
			{
				return  JSON.parse(StringHelper.readFile(filepath));

			}*/} catch (IOException e) {
				System.out.print(e.getMessage());
			} finally {
				try {
					if (fop != null){
						fop.close();
					}

					if (outText != null){
						outText.close();
					}

				} catch (IOException e) {
					System.out.print(e.getMessage());
				}
			}
		return JSON.toJSON(resulttrees);
	}

	@Override
	public Message SaveImportZRZ(String id, String paramString,
			HttpServletRequest request) {

		Message m=new Message();
		String filepath = String.format("%s\\resources\\FC2DJXT\\%s.txt", request.getRealPath("/"),id);
		FileOutputStream fop = null;
		File file = null;
		try {
			file = new File(filepath);
			if (file.exists()) {
				fop = new FileOutputStream(file);
				byte[] contentInBytes = paramString.getBytes();
				//byte[] contentInBytes =  URLEncoder.encode(paramString, "UTF-8").getBytes();
				fop.write(contentInBytes);
			}
			m.setSuccess("抽取记录已保存到文件 ！");
		} catch (IOException e) {
			m.setSuccess("false");
			System.out.print(e.getMessage());
		} finally {
			try {
				if (fop != null){
					fop.close();
				}
			} catch (IOException e) {
				m.setSuccess("false");
				System.out.print(e.getMessage());
			}
		}
		return m;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,String>> readFileData(String id, HttpServletRequest request){
		//读取保存的文件列表，获取SJBH、FH
		String filePath = String.format("%s\\resources\\FC2DJXT\\%s.txt", request.getRealPath("/"),id);
		StringBuffer sb = new StringBuffer();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			InputStream in = new FileInputStream(filePath);
			byte[] bytes = new byte[1024];
			int length = 0;
			while((length = in.read(bytes)) != -1){
				sb.append(new String(bytes,0,length));
			}
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		//转换成json类型
		JSONArray json = JSONArray.fromObject(sb.toString());
		System.out.println(json.get(0));
		JSONArray job = (JSONArray) json.getJSONObject(0).get("children");

		for (int i = 0; i < job.size(); i++) {
			Map map = new HashMap<String, String>();
			String sjbh = (String) job.getJSONObject(i).get("id");
			map.put("SJBH", sjbh);
			String text = job.getJSONObject(i).getString("text");
			map.put("TEXT", text);
			int str = text.indexOf('：');
			System.out.println(str);
			int str1 = text.indexOf(',');
			System.out.println(str1);
			String fh = text.substring(str+1,str1);
			map.put("FH", fh);
			System.out.println("新的房号"+fh);
			
			int next_str = text.indexOf('：',text.indexOf('：')+1);
			System.out.println(next_str);
			int next_str1 = text.indexOf(',',text.indexOf(',')+1);
			System.out.println(next_str1);
			String szc = text.substring(next_str+1,next_str1);
			map.put("SZC", szc);
			System.out.println("您编辑的所在层为："+szc);
			
			list.add(map);
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message insertZRZ(String id,HttpServletRequest request)  {
		Message m= new Message();

		List<Map<String,String>> fileList = readFileData(id,request);
		logger.debug("读取房产库数据");
		for (Map sjbhMap : fileList) {
			String query_params = (String) sjbhMap.get("SJBH");
			String [] params = query_params.split(",");
			String sjbh =params[0];
			String qzh = params[1];
			//映射配置文件
			@SuppressWarnings("deprecation")
			String filepath = String.format("%s\\resources\\FC2DJXT\\config\\房产数据与登记系统数据映射关系.txt", request.getRealPath("/"));
			//获取房产数据 结果是List<Map>
			logger.info("房屋基本信息和s所有权信息同在一张表中");
			List<Map> jbxx = this.baseCommonDao.getDataListByFullSql("SELECT * FROM FDC_DAK.EP_FJSYQJBXX WHERE SJBH='"+sjbh+"'");
			List<Map> dyqxx = new ArrayList<Map>();

			if(qzh != null){
				logger.info("抵押权（他项）基本信息0");
				dyqxx = this.baseCommonDao.getDataListByFullSql("SELECT * FROM FDC_DAK.EP_FJTXQJBXX WHERE ZXRQ IS NULL AND (SJBH='"+sjbh+"' or instr(SYQZH ,'"+qzh+"')>0 or instr(SYQZH ,'"+qzh+"号') > 0)");
			}else{
				logger.info("抵押权（他项）基本信息1");
				dyqxx = this.baseCommonDao.getDataListByFullSql("SELECT * FROM FDC_DAK.EP_FJTXQJBXX WHERE SJBH='"+sjbh+"'" + " AND ZXRQ IS NULL");
			}

			logger.info("查封 基本信息");
			List<Map> cfxx = this.baseCommonDao.getDataListByFullSql("SELECT * FROM FDC_DAK.EP_FJCFQJBXX WHERE SJBH='"+sjbh+"'" + "AND JFRQ IS NULL");
			logger.info("//所有权的共有人信息");
			List<Map> gyrxx = this.baseCommonDao.getDataListByFullSql("SELECT * FROM FDC_DAK.EP_FJSYQGYXX WHERE SJBH='"+sjbh+"'");
			logger.info("//抵押共有权人信息");
			List<Map> dygyrxx = this.baseCommonDao.getDataListByFullSql("SELECT * FROM FDC_DAK.EP_DYQRGYXX WHERE SJBH='"+sjbh+"'" );
			//初始化工具类
			ConverterUtil util=new ConverterUtil(filepath);

			//为房屋基本信息的结果数据加入RS_SYQFWJBXX的前缀名，组合成RS_SYQFWJBXX.SYQRMC这样的数据格式
			util.AddPrefix(jbxx, FC_TABLE.FDC_DAK$EP_FJSYQJBXX);
			//所有权共有信息
			util.AddPrefix(gyrxx, FC_TABLE.FDC_DAK$EP_FJSYQGYXX);

			//为房屋抵押权信息的结果数据加入RS_SYQJBXX的前缀名，组合成RS_SYQJBXX.SYQRMC这样的数据格式
			util.AddPrefix(dyqxx, FC_TABLE.FDC_DAK$EP_FJTXQJBXX);
			util.AddPrefix(dygyrxx, FC_TABLE.FDC_DAK$EP_DYQRGYXX);
			//查封
			util.AddPrefix(cfxx, FC_TABLE.FDC_DAK$EP_FJCFQJBXX);

			//将数据集合并起来
			jbxx.addAll(dyqxx);
			jbxx.addAll(cfxx);
			jbxx.addAll(gyrxx);
			jbxx.addAll(dygyrxx);
			//批量创建登记系统实体类。填充房产数据创建登记系统实体类。会根据List 的集合数生产多少个对象。
			List<BDCS_H_XZ> houses = util.CreateClass(BDCS_H_XZ.class, jbxx);
			if(jbxx.size()>0){
				//single_map.put("FDC_DAK.EP_FJSYQJBXX.FJ", "演示演示");
				//填充房产数据创建单个登记系统实体类。
				Map single_map = new HashMap();
				single_map = jbxx.get(0);
				BDCS_H_XZ house = util.CreateSingleClass(BDCS_H_XZ.class, single_map);
				String clh = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.CLH");
				String fh = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.FWFH");
				house.setRELATIONID(clh + "-" + fh);
				house.setZRZBDCDYID(id);

				BDCS_ZRZ_XZ zrz = baseCommonDao.get(BDCS_ZRZ_XZ.class, id);
				String zdbdcdyid = zrz.getZDBDCDYID();
				String zrzbdcdyh = zrz.getBDCDYH().substring(0,zrz.getBDCDYH().length()-4);
				house.setZDBDCDYID(zdbdcdyid);
				house.setFH((String)sjbhMap.get("FH"));//人工编辑后的房号
				String now_szc = house.getSZC();
				if(now_szc == null || now_szc.equals("")){
					house.setSZC((String)sjbhMap.get("SZC"));//人工编辑的所在层
				}
				house.setDYH(house.getFH().substring(0,1));

				String bdcdyh_fulSql = "select max(substr(bdcdyh,26,4)) as BDCDYHHSW from bdck.bdcs_h_xz where" +  " ZRZBDCDYID = '"+id+"'";
				List<Map> bdcdyhhsw = baseCommonDao.getDataListByFullSql(bdcdyh_fulSql);//查询某自然幢下的所有户中不动产单元号后三位最大的值；
				String h_dcdywh = (String) bdcdyhhsw.get(0).get("BDCDYHHSW");
				if(h_dcdywh != null){
					int hswbdcdyh = Integer.parseInt(h_dcdywh);
					if(hswbdcdyh < 10){
						hswbdcdyh = hswbdcdyh+1;
						String new_bdcdyh = "000"+hswbdcdyh;
						String newh_bdcdyh = zrzbdcdyh + new_bdcdyh;
						house.setBDCDYH(newh_bdcdyh);
					}
					if(hswbdcdyh >= 10 && hswbdcdyh < 100){
						hswbdcdyh = hswbdcdyh + 1;
						String new_bdcdyh = "00"+hswbdcdyh;
						String newh_bdcdyh = zrzbdcdyh + new_bdcdyh;
						house.setBDCDYH(newh_bdcdyh);
					}
					if(hswbdcdyh >= 100){
						hswbdcdyh = hswbdcdyh + 1;
						String new_bdcdyh = "0"+hswbdcdyh;
						String newh_bdcdyh = zrzbdcdyh + new_bdcdyh;
						house.setBDCDYH(newh_bdcdyh);
					}
				}else{
					String h_dyh = zrz.getBDCDYH();
					h_dyh = h_dyh.substring(0, h_dyh.length()-4);
					house.setBDCDYH(h_dyh + "0001");
				}

				if(house.getGHYT() != null && house.getGHYT().equals("住宅")){
					house.setGHYT("10");
					house.setFWYT1("10");
				}
				if(house.getFWLX() != null && house.getFWLX().equals("住宅")){
					house.setFWLX("1");
				}
				if(house.getFWLX() != null && house.getFWLX().equals("商业用房")){
					house.setFWLX("2");
				}
				if(house.getFWLX() != null && house.getFWLX().equals("办公用房")){
					house.setFWLX("3");
				}
				if(house.getFWLX() != null && house.getFWLX().equals("工业用房")){
					house.setFWLX("4");
				}
				if(house.getFWLX() != null && house.getFWLX().equals("仓储用房")){
					house.setFWLX("5");
				}
				if(house.getFWLX() != null && house.getFWLX().equals("车库")){
					house.setFWLX("6");
				}
				if(house.getFWLX() != null && house.getFWLX().equals("其他")){
					house.setFWLX("99");
				}

				String fc_fwcs = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.FWCS");//房产总层数

				String fc_fwszcs = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.FWSZCS");//房产总层数
				if(fc_fwszcs != null){
					if(fc_fwszcs.indexOf("、") == -1 && fc_fwszcs.substring(1,fc_fwszcs.length()).indexOf("-") == -1 && fc_fwszcs.indexOf(",") == -1&& fc_fwszcs.indexOf("，") == -1){
						int fc_szc = Integer.parseInt(fc_fwszcs);
						
						house.setQSC((double)fc_szc);
						house.setZZC((double)fc_szc);
					}
					else if(fc_fwszcs.indexOf("、") != -1){
						String[] fw_szcs = fc_fwszcs.split("、");
						int qsc = Integer.parseInt(fw_szcs[0]);
						int zzc = Integer.parseInt(fw_szcs[fw_szcs.length-1]);
						house.setQSC((double)qsc);
						house.setZZC((double)zzc);
					}
					else if(fc_fwszcs.indexOf(",") != -1){
						String[] fw_szcs = fc_fwszcs.split(",");
						int qsc1 = Integer.parseInt(fw_szcs[0]);
						int zzc1 = Integer.parseInt(fw_szcs[fw_szcs.length-1]);
						house.setQSC((double)qsc1);
						house.setZZC((double)zzc1);
					}
					else if(fc_fwszcs.indexOf("，") != -1){
						String[] fw_szcs = fc_fwszcs.split("，");
						int qsc2 = Integer.parseInt(fw_szcs[0]);
						int zzc2 = Integer.parseInt(fw_szcs[fw_szcs.length-1]);
						house.setQSC((double)qsc2);
						house.setZZC((double)zzc2);
					}
					else if(fc_fwszcs.substring(1,fc_fwszcs.length()).indexOf("-") != -1){
						String[] fw_szcs = fc_fwszcs.split("-");
						int qsc = Integer.parseInt(fw_szcs[0]);
						int zzc = Integer.parseInt(fw_szcs[fw_szcs.length-1]);
						house.setQSC((double)qsc);
						house.setZZC((double)zzc);
					}
					else if(fc_fwszcs.indexOf(",") != -1){
						String[] fw_szcs = fc_fwszcs.split(",");
						int qsc = Integer.parseInt(fw_szcs[0]);
						int zzc = Integer.parseInt(fw_szcs[fw_szcs.length-1]);
						house.setQSC((double)qsc);
						house.setZZC((double)zzc);
					}
					else{
						int fc_szc = Integer.parseInt(fc_fwszcs);
						
						house.setQSC((double)fc_szc);
						house.setZZC((double)fc_szc);
					}
				}
				
				baseCommonDao.save(house);

				//baseCommonDao.flush();
				logger.info(String.format("插入一条房屋数据,SJBH:%s,FH:%s",sjbh,fh));				

				String updateSql = "update FDC_DAK.EP_FJSYQJBXX set SJCQBS = 1 where sjbh = '"+ sjbh+"'";
				try {

					BDCS_H_LS ls_house = new BDCS_H_LS(); 
					PropertyUtils.copyProperties(ls_house,house);

					baseCommonDao.save(ls_house);

					logger.info(String.format("插入一条房屋历史层的数据,SJBH:%s,FH:%s",sjbh,fh));		

					baseCommonDao.excuteQueryNoResult(updateSql);

					logger.info(String.format("更新一条房产档案库房屋的数据,SJBH:%s,FH:%s",sjbh,fh));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					logger.error(e1.getMessage());
				}
				/*生成BDCS_DJDY_XZ 对象并填充数据*/ 
				BDCS_DJDY_XZ djdy = new BDCS_DJDY_XZ();

				String djdyid = java.util.UUID.randomUUID().toString();

				djdy.setDJDYID(djdyid);

				djdy.setBDCDYH(house.getBDCDYH().substring(0,house.getBDCDYH().length()-1));
				djdy.setBDCDYID(house.getId());
				djdy.setBDCDYLX("031");

				baseCommonDao.save(djdy);

				logger.info(String.format("插入一条登记单元数据,SJBH:%s,djdyid:%s",sjbh,djdyid));
				/*
				 * 存放关联表，方便记录
				 */
				Subject user = SecurityUtils.getSubject();
				User u = Global.getCurrentUserInfo();
				String curr_user = u.getLoginName();
				String glid = java.util.UUID.randomUUID().toString();
				String insert_clh = clh;
				if(insert_clh == null){
					insert_clh = "";
				}
				String insert_sjbh = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.SJBH");
				String insert_bdcdyh = house.getBDCDYH();
				String insert_djdyid = djdy.getDJDYID();
				Date curr_time = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String insert_time = formatter.format(curr_time);
				String insert_to_mb = "insert into FDC_DAK.EP_FDC_TO_BDC (ID,CLH,SJBH,BDCDYH,DJDYID,LOGINNAME,CREATETIME)"
				+ "values('"+glid+"'"+",'"
						+insert_clh+"',"+"'"
						+insert_sjbh+"'"+",'"
						+insert_bdcdyh+"'"+",'"
						+insert_djdyid+"'"+",'"
						+curr_user+"'"+","
						+"to_date('"+insert_time+"','yyyy-mm-dd hh24:mi:ss')"+")";
				try {
					baseCommonDao.excuteQueryNoResult(insert_to_mb);
					logger.info(String.format("插入一条关联记录,SJBH:%s,BDCDYH:%s,CREATETIME:%s",insert_sjbh,insert_bdcdyh,insert_time));	
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					BDCS_DJDY_LS ls_djdy = new BDCS_DJDY_LS();
					PropertyUtils.copyProperties(ls_djdy,djdy);
					baseCommonDao.save(ls_djdy);

					logger.info(String.format("插入一条历史登记单元数据,SJBH:%s,djdyid:%s",sjbh,djdyid));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					logger.error(e1.getMessage());
				} 

				/*先生成所有权 BDCS_QL_XZ 对象 并填充数据*/
				single_map = jbxx.get(0);
				BDCS_QL_XZ ql = util.CreateSingleClass(BDCS_QL_XZ.class, single_map);
				ql.setBDCDYH(house.getBDCDYH());
				ql.setDJDYID(djdy.getDJDYID());
				ql.setQLLX("4");
				ql.setDJLX("100");
				ql.setQSZT(1);

				/*生成附属权利 对象，并填充数据*/
				BDCS_FSQL_XZ fsql = new BDCS_FSQL_XZ();
				fsql.setDJDYID(djdy.getDJDYID());
				fsql.setBDCDYH(house.getBDCDYH());

				fsql.setQLID(ql.getId());

				ql.setFSQLID(fsql.getId());
				/*Date djsj = null;
				try {
					Date dydjsj  = ql.getDJSJ();
					if(dydjsj != null){
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
						djsj= sdf.parse(sdf.format(dydjsj));
						ql.setDJSJ(null);
					}
				} catch (ParseException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}*/

				baseCommonDao.save(ql);

				logger.info(String.format("BDCS_QL_XZ,SJBH:%s,qlid:%s",sjbh,ql.getId()));

				baseCommonDao.save(fsql);

				logger.info(String.format("BDCS_FSQL_XZ,SJBH:%s,qlid:%s",sjbh,ql.getId()));

				BDCS_QL_LS ls_ql = new BDCS_QL_LS();
				try {
					PropertyUtils.copyProperties(ls_ql,ql);

					BDCS_FSQL_LS ls_fsql = new BDCS_FSQL_LS();
					PropertyUtils.copyProperties(ls_fsql,fsql);

					baseCommonDao.save(ls_ql);

					logger.info(String.format("BDCS_QL_LS,SJBH:%s,qlid:%s",sjbh,ls_ql.getId()));

					baseCommonDao.save(ls_fsql);

					logger.info(String.format("BDCS_FSQL_LS,SJBH:%s,qlid:%s",sjbh,ls_fsql.getId()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					logger.error(e1.getMessage());
				} 

				/* 接下来关联到BDCS_QLR_XZ，并填充数据*/
				single_map = jbxx.get(0);
				BDCS_QLR_XZ qlr = util.CreateSingleClass(BDCS_QLR_XZ.class, single_map);
				qlr.setQLID(ql.getId());
				String qlrmc = qlr.getQLRMC();
				if(qlrmc != null){
					if(qlrmc.indexOf("银行") != -1 || qlrmc.indexOf("柳州") != -1 || qlrmc.indexOf("信用") != -1){
						qlr.setQLRLX("2");
					}else{
						qlr.setQLRLX("1");
						qlr.setZJZL("1");//身份证
						qlr.setISCZR("1");//持证人标志
					}
				}
				
				

				baseCommonDao.save(qlr);

				logger.info(String.format("BDCS_QLR_XZ,SJBH:%s,qlrid:%s",sjbh,qlr.getId()));

				BDCS_QLR_LS ls_qlr = new BDCS_QLR_LS();
				try {
					PropertyUtils.copyProperties(ls_qlr,qlr);
					baseCommonDao.save(ls_qlr);

					logger.info(String.format("BDCS_QLR_LS,SJBH:%s,qlrid:%s",sjbh,ls_qlr.getId()));
				} catch (IllegalAccessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (InvocationTargetException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (NoSuchMethodException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				/* 接下来关联到BDCS_ZS_XZ，并填充数据*/
				single_map = jbxx.get(0);
				BDCS_ZS_XZ zs = util.CreateSingleClass(BDCS_ZS_XZ.class, single_map);
				baseCommonDao.save(zs);

				logger.info(String.format("BDCS_ZS_XZ,SJBH:%s,zsid:%s",sjbh,zs.getId()));

				BDCS_ZS_LS ls_zs = new BDCS_ZS_LS();
				try {
					PropertyUtils.copyProperties(ls_zs,zs);

					baseCommonDao.save(ls_zs);

					logger.info(String.format("BDCS_ZS_LS,SJBH:%s,zsid:%s",sjbh,ls_zs.getId()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 


				/* 接下来关联到BDCS_QDZR_XZ，并填充数据*/
				BDCS_QDZR_XZ qdzr = new BDCS_QDZR_XZ();
				qdzr.setZSID(zs.getId());
				qdzr.setDJDYID(djdy.getDJDYID());
				qdzr.setQLID(ql.getId());
				qdzr.setQLRID(qlr.getId());
				qdzr.setFSQLID(fsql.getId());
				qdzr.setBDCDYH(house.getBDCDYH());
				baseCommonDao.save(qdzr);

				logger.info(String.format("BDCS_QDZR_XZ,SJBH:%s,qdzrid:%s",sjbh,qdzr.getId()));

				try {
					BDCS_QDZR_LS ls_qdzr = new BDCS_QDZR_LS();
					PropertyUtils.copyProperties(ls_qdzr,qdzr);

					baseCommonDao.save(ls_qdzr);

					logger.info(String.format("BDCS_QDZR_LS,SJBH:%s,qdzrid:%s",sjbh,ls_qdzr.getId()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				} 

				if(gyrxx.size() > 0){
					for (Map gyr_map : gyrxx) {
						try {
							/*以下为判断权证号的类型（柳房权证字第XX号、桂房共字第XX号、桂房证字第XX号）*/
							String ysbh = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.YSBH");//印刷编号
							String syqzh = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.SYQZH");//房产的所有权人的所有权证号
							String yexl = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.YWXL");//业务细类，用于判断商品房初始登记

							String gyr_qzh =  (String) gyr_map.get("FDC_DAK.EP_FJSYQGYXX.GYQZH");

							if(yexl != null && yexl.equals("商品房初始登记")){

								if(gyr_qzh != null){
									ql.setBDCQZH("商初字第"+syqzh+"号,"+"商字初第"+gyr_qzh+"号");
								}
								else{
									ql.setBDCQZH("商初字第"+syqzh+"号");
								}
							}

							if(ysbh != null ){
								if(gyr_qzh != null){
									ql.setBDCQZH("柳房权证字第"+syqzh+"号,"+"柳房权证字第"+gyr_qzh+"号");
								}
								else{
									ql.setBDCQZH("柳房权证字第"+syqzh+"号");
								}

							}
							if(ysbh == null && syqzh != null && syqzh.length() == 7){
								if(gyr_qzh != null){
									ql.setBDCQZH("桂房证字第"+syqzh+"号,"+"桂房证字第"+gyr_qzh+"号");
								}
								else{
									ql.setBDCQZH("桂房证字第"+syqzh+"号");
								}

							}
							if(ysbh == null && syqzh != null && syqzh.length() == 6){
								if(gyr_qzh != null){
									ql.setBDCQZH("桂房共字第"+syqzh+"号,"+"桂房共字第"+gyr_qzh+"号");
								}
								else{
									ql.setBDCQZH("桂房共字第"+syqzh+"号");
								}

							}

							BDCS_QLR_XZ gyqr = util.CreateSingleClass(BDCS_QLR_XZ.class, gyr_map);
							gyqr.setQLID(ql.getId());
							gyqr.setZJZL("1");//身份证
							gyqr.setQLRLX("1");

							String szfe = (String) gyr_map.get("FDC_DAK.EP_FJSYQGYXX.SZFE");
							NumberFormat nf = NumberFormat.getPercentInstance();//NumberFormat是一个工厂，可以直接getXXX创建，而getPercentInstance() 是返回当前默认语言环境的百分比格式。
							if(szfe != null && !szfe.trim().isEmpty()){
								String gyfs = qlr.getGYFS();
								if(gyfs == null || !gyfs.equals("2")){
									qlr.setGYFS("2");
									qlr.setSXH(1);
									if(yexl != null && yexl.equals("商品房初始登记")){
										qlr.setBDCQZH("商初字第"+syqzh+"号");
									}
									if(ysbh != null ){
										qlr.setBDCQZH("柳房权证字第"+syqzh+"号");

									}
									if(ysbh == null && syqzh != null && syqzh.length() == 7){
										qlr.setBDCQZH("桂房证字第"+syqzh+"号");

									}
									if(ysbh == null && syqzh != null && syqzh.length() == 6){
										qlr.setBDCQZH("桂房共字第"+syqzh+"号");

									}
									

									baseCommonDao.update(qlr);

									logger.info(String.format("BDCS_QLR_XZ,SJBH:%s,qlrid:%s",sjbh,qlr.getId()));

									PropertyUtils.copyProperties(ls_qlr,qlr);
									baseCommonDao.update(ls_qlr);

									logger.info(String.format("BDCS_QLR_LS,SJBH:%s,qlrid:%s",sjbh,ls_qlr.getId()));

									/*更新权利信息*/
									ql.setCZFS("02");//持证方式-----房产的都是分别持证
									ql.setBDCQZH(ql.getBDCQZH()+","+qlr.getBDCQZH());
									baseCommonDao.update(ql);

									logger.info(String.format("BDCS_QL_XZ,SJBH:%s,qlid:%s",sjbh,ql.getId()));

									PropertyUtils.copyProperties(ls_ql,ql);
									baseCommonDao.update(ls_ql);

									logger.info(String.format("BDCS_QL_LS,SJBH:%s,ls_ql:%s",sjbh,ls_ql.getId()));
								}

								if(szfe.indexOf("%") != -1){
									Number qlrqlbl = nf.parse(szfe);//提供了带有 ParsePosition 和 FieldPosition 的 
									double qlrbl =  qlrqlbl.floatValue();

									String gyrszfe  = nf.format(qlrbl).toString();//自动转换成百分比显示.
									gyqr.setQLBL(gyrszfe);
								}
								if(szfe.indexOf("/") != -1){
									Number qlrbl = nf.parse(szfe);
									double d = qlrbl.doubleValue(); 
									String gyqrfe = nf.format(d).toString();//自动转换成百分比显示.
									gyqr.setQLBL(gyqrfe);
								}
								gyqr.setGYFS("2");
								gyqr.setSXH(gyqr.getSXH()+1);
								/*-------------保存共有权人-------------------*/
								/*以下为判断权证号的类型（柳房权证字第XX号、桂房共字第XX号、桂房证字第XX号）*/
								String gy_ysbh = (String) gyr_map.get("FDC_DAK.EP_FJSYQGYXX.YSBH");//印刷编号
								String gy_gyqzh = (String) gyr_map.get("FDC_DAK.EP_FJSYQGYXX.GYQZH");//房产的所有权人的所有权证号
								if(yexl != null && yexl.equals("商品房初始登记")){
									gyqr.setBDCQZH("商初字第"+gy_gyqzh+"号");
								}

								if(gy_ysbh != null ){
									gyqr.setBDCQZH("柳房权证字第"+gy_gyqzh+"号");

								}
								if(gy_ysbh == null && gy_gyqzh != null && gy_gyqzh.length() == 7){
									gyqr.setBDCQZH("桂房证字第"+gy_gyqzh+"号");

								}
								if(gy_ysbh == null && gy_gyqzh != null &&  gy_gyqzh.length() == 6){
									gyqr.setBDCQZH("桂房共字第"+gy_gyqzh+"号");
								}

								baseCommonDao.save(gyqr);

								logger.info(String.format("BDCS_QLR_XZ,SJBH:%s,gyqr:%s",sjbh,gyqr.getId()));


								BDCS_QLR_LS ls_gyqlr = new BDCS_QLR_LS();
								PropertyUtils.copyProperties(ls_gyqlr,gyqr);

								baseCommonDao.save(ls_gyqlr);

								logger.info(String.format("BDCS_QLR_LS,SJBH:%s,ls_gyqlr:%s",sjbh,ls_gyqlr.getId()));


							}else{
								qlr.setGYFS("1");
								qlr.setSXH(1);

								if(ysbh != null ){
									qlr.setBDCQZH("柳房权证字第"+syqzh+"号");

								}
								if(ysbh == null && syqzh != null && syqzh.length() == 7){
									qlr.setBDCQZH("桂房证字第"+syqzh+"号");

								}
								if(ysbh == null && syqzh != null && syqzh.length() == 6){
									qlr.setBDCQZH("桂房共字第"+syqzh+"号");
								}

								baseCommonDao.update(qlr);

								logger.info(String.format("BDCS_QLR_XZ,SJBH:%s,qlr:%s",sjbh,qlr.getId()));
								PropertyUtils.copyProperties(ls_qlr,qlr);
								baseCommonDao.update(ls_qlr);
								logger.info(String.format("BDCS_QLR_LS,SJBH:%s,ls_qlr:%s",sjbh,ls_qlr.getId()));

								/*-------------保存共有权人-------------------*/
								/*以下为判断权证号的类型（柳房权证字第XX号、桂房共字第XX号、桂房证字第XX号）*/
								String gy_ysbh = (String) gyr_map.get("FDC_DAK.EP_FJSYQGYXX.YSBH");//印刷编号
								String gy_gyqzh = (String) gyr_map.get("FDC_DAK.EP_FJSYQGYXX.GYQZH");//房产的所有权人的所有权证号

								if(gy_ysbh != null ){
									gyqr.setBDCQZH("柳房权证字第"+gy_gyqzh+"号");

								}
								if(gy_ysbh == null && gy_gyqzh != null && gy_gyqzh.length() == 7){
									gyqr.setBDCQZH("桂房证字第"+gy_gyqzh+"号");

								}
								if(gy_ysbh == null && gy_gyqzh != null && gy_gyqzh.length() == 6){
									gyqr.setBDCQZH("桂房共字第"+gy_gyqzh+"号");
								}
								gyqr.setGYFS("1");
								gyqr.setSXH(gyqr.getSXH()+1);
								baseCommonDao.save(gyqr);

								logger.info(String.format("BDCS_QLR_XZ,SJBH:%s,gyqr:%s",sjbh,gyqr.getId()));

								BDCS_QLR_LS ls_gyqlr = new BDCS_QLR_LS();
								PropertyUtils.copyProperties(ls_gyqlr,gyqr);

								baseCommonDao.save(ls_gyqlr);

								logger.info(String.format("BDCS_QLR_LS,SJBH:%s,ls_gyqlr:%s",sjbh,ls_gyqlr.getId()));

							}

							String updategYSql = "update FDC_DAK.EP_FJSYQGYXX set SJCQBS = 1 where sjbh = '"+ sjbh+"'";
							baseCommonDao.excuteQueryNoResult(updategYSql);

							logger.info(String.format("FDC_DAK.EP_FJSYQGYXX,SJBH:%s",sjbh));

							/* 接下来关联到BDCS_ZS_XZ，并填充数据*/
							single_map = jbxx.get(1);
							BDCS_ZS_XZ gyrzs = util.CreateSingleClass(BDCS_ZS_XZ.class, single_map);
							gyrzs.setBDCQZH(gyqr.getBDCQZH());
							baseCommonDao.save(gyrzs);


							BDCS_ZS_LS ls_gyqrzs = new BDCS_ZS_LS();
							PropertyUtils.copyProperties(ls_gyqrzs,gyrzs);

							baseCommonDao.save(ls_gyqrzs);						

							/* 接下来关联到BDCS_QDZR_XZ，并填充数据*/
							BDCS_QDZR_XZ gyrqdzr = new BDCS_QDZR_XZ();
							gyrqdzr.setZSID(gyrzs.getId());
							gyrqdzr.setDJDYID(djdy.getDJDYID());
							gyrqdzr.setQLID(ql.getId());
							gyrqdzr.setQLRID(gyqr.getId());
							gyrqdzr.setFSQLID(fsql.getId());
							gyrqdzr.setBDCDYH(house.getBDCDYH());
							baseCommonDao.save(gyrqdzr);						

							BDCS_QDZR_LS ls_gyqdzr = new BDCS_QDZR_LS();
							PropertyUtils.copyProperties(ls_gyqdzr,gyrqdzr);

							baseCommonDao.save(ls_gyqdzr);

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							System.out.println("更新共有权有误");
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}else{

					/*以下为判断权证号的类型（柳房权证字第XX号、桂房共字第XX号、桂房证字第XX号）*/
					String ysbh = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.YSBH");//印刷编号
					String syqzh = (String) single_map.get("FDC_DAK.EP_FJSYQJBXX.SYQZH");//房产的所有权人的所有权证号
					String yexl = (String) single_map.get("");//业务细类，用于判断商品房初始登记
					if(yexl != null && yexl.equals("商品房初始登记")){
						ql.setBDCQZH("商初字第"+syqzh+"号");
						qlr.setBDCQZH("商初字第"+syqzh+"号");
						zs.setBDCQZH("商初字第"+syqzh+"号");
					}

					if(ysbh != null ){
						ql.setBDCQZH("柳房权证字第"+syqzh+"号");
						qlr.setBDCQZH("柳房权证字第"+syqzh+"号");
						zs.setBDCQZH("柳房权证字第"+syqzh+"号");
					}
					if(ysbh == null && syqzh != null && syqzh.length() == 7){
						ql.setBDCQZH("桂房证字第"+syqzh+"号");
						qlr.setBDCQZH("桂房证字第"+syqzh+"号");
						zs.setBDCQZH("桂房证字第"+syqzh+"号");
					}
					if(ysbh == null && syqzh != null && syqzh.length() == 6){
						ql.setBDCQZH("桂房共字第"+syqzh+"号");
						qlr.setBDCQZH("桂房共字第"+syqzh+"号");
						zs.setBDCQZH("桂房共字第"+syqzh+"号");
					}

					baseCommonDao.update(ql);

					qlr.setGYFS("0");

					baseCommonDao.update(qlr);

					try {

						PropertyUtils.copyProperties(ls_ql,ql);

						baseCommonDao.update(ls_ql);

						PropertyUtils.copyProperties(ls_qlr,qlr);

						baseCommonDao.update(ls_qlr);

						baseCommonDao.update(zs);

						PropertyUtils.copyProperties(ls_zs,zs);
						baseCommonDao.update(zs);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
				}

				/*-----------生成所有权的一系列表和转换数据后，接下来判断该房屋是否存在抵押权，有则再生成新的权利信息-------------*/
				if(dyqxx.size() > 0 ){
					/*先生成抵押权 BDCS_QL_XZ 对象 并填充数据*/
					for(Map dyq_map : dyqxx){
						String dyr1 = (String) dyq_map.get("FDC_DAK.EP_FJTXQJBXX.DYR");
						String syqr1 = (String) jbxx.get(0).get("FDC_DAK.EP_FJSYQJBXX.SYQR");
						if(dyr1 != null && syqr1 != null){
							dyr1 = dyr1.replace(" ", ""); 
							syqr1 = syqr1.replace(" ", "");
							if(dyr1.equals(syqr1) || dyr1.indexOf(syqr1) != -1){
								//single_map = jbxx.get(1);
								BDCS_QL_XZ dyql = util.CreateSingleClass(BDCS_QL_XZ.class, dyq_map);
								dyql.setBDCDYH(house.getBDCDYH());
								dyql.setDJDYID(djdy.getDJDYID());
								dyql.setQLLX("23");
								dyql.setDJLX("100");

								/*生成附属权利 对象，并填充数据*/
								//single_map = jbxx.get(1);
								BDCS_FSQL_XZ dyfsql =util.CreateSingleClass(BDCS_FSQL_XZ.class, dyq_map);
								dyfsql.setDJDYID(djdy.getDJDYID());
								dyfsql.setBDCDYH(house.getBDCDYH());
								dyfsql.setQLID(dyql.getId());

								String dyql_fj = ql.getFJ();
								if(dyql_fj.indexOf("最高") != -1){
									dyfsql.setDYFS("2");//最高额抵押
								}else{
									dyfsql.setDYFS("1");//默认都是一般抵押
								}

								dyfsql.setDYWLX("2");//抵押物类型（土地和房屋）

								dyql.setFSQLID(dyfsql.getId());
								String dyrqzh = (String) dyq_map.get("FDC_DAK.EP_FJTXQJBXX.TXQZH");

								dyql.setBDCQZH("柳房他证字第"+dyrqzh+"号");

								//dyql.setDJSJ(djsj);
								baseCommonDao.save(dyql);

								baseCommonDao.save(dyfsql);

								try {
									BDCS_QL_LS ls_dyql = new BDCS_QL_LS();
									PropertyUtils.copyProperties(ls_dyql,dyql);

									BDCS_FSQL_LS ls_dyfsql = new BDCS_FSQL_LS();
									PropertyUtils.copyProperties(ls_dyfsql,dyfsql);

									baseCommonDao.save(ls_dyql);

									baseCommonDao.save(ls_dyfsql);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} 

								String updateTxSql = "update FDC_DAK.EP_FJTXQJBXX set SJCQBS = 1 where sjbh = '"+ sjbh+"'";
								try {
									baseCommonDao.excuteQueryNoResult(updateTxSql);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									//					e.printStackTrace();
									System.out.println("更新TXQL表有误");
								}

								if(dygyrxx.size() > 0){
									for (Map dygyr_map : dygyrxx) {
										try {
											String dygy_qzh = (String) dygyr_map.get("FDC_DAK.EP_DYQRGYXX.QZH");
											dyql.setCZFS("02");
											dyql.setBDCQZH(dyrqzh+","+"柳房他证字第"+dygy_qzh+"号");
											baseCommonDao.update(dyql);
											BDCS_QLR_XZ dygyqr = util.CreateSingleClass(BDCS_QLR_XZ.class, dygyr_map);
											dygyqr.setQLID(dyql.getId());
											String gyrmc = dygyqr.getQLRMC();
											if(gyrmc != null){
												if(gyrmc.indexOf("银行") != -1 || gyrmc.indexOf("柳州") != -1 || gyrmc.indexOf("信用") != -1){
													dygyqr.setQLRLX("2");
												}else{
													dygyqr.setQLRLX("1");
													dygyqr.setZJZL("1");//身份证
												}
											}  


											dygyqr.setBDCQZH("柳房他证字第"+dygy_qzh+"号");
											baseCommonDao.save(dygyqr);

											try {
												BDCS_QLR_LS ls_dygyqlr = new BDCS_QLR_LS();
												PropertyUtils.copyProperties(ls_dygyqlr,dygyqr);

												baseCommonDao.save(ls_dygyqlr);
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											} 

											/* 接下来关联到BDCS_ZS_XZ，并填充数据*/
											//single_map = jbxx.get(1);
											BDCS_ZS_XZ dygyrzs = util.CreateSingleClass(BDCS_ZS_XZ.class, dyq_map);
											dygyrzs.setBDCQZH(dygyqr.getBDCQZH());
											baseCommonDao.save(dygyrzs);

											BDCS_ZS_LS ls_dygyqrzs = new BDCS_ZS_LS();
											PropertyUtils.copyProperties(ls_dygyqrzs,dygyrzs);

											baseCommonDao.save(ls_dygyqrzs);

											String updateDygyqSql = "update FDC_DAK.EP_DYQRGYXX set SJCQBS = 1 where sjbh = '"+ sjbh+"'";

											baseCommonDao.excuteQueryNoResult(updateDygyqSql);

										/* 接下来关联到BDCS_QDZR_XZ，并填充数据*/
										BDCS_QDZR_XZ dygyr_qdzr = new BDCS_QDZR_XZ();
										dygyr_qdzr.setZSID(dygyrzs.getId());
										dygyr_qdzr.setDJDYID(djdy.getDJDYID());
										dygyr_qdzr.setQLID(dyql.getId());
										dygyr_qdzr.setQLRID(dygyqr.getId());
										dygyr_qdzr.setFSQLID(dyfsql.getId());
										dygyr_qdzr.setBDCDYH(house.getBDCDYH());
										baseCommonDao.save(dygyr_qdzr);
										logger.info(String.format("BDCS_QDZR_XZ,SJBH:%s,dygyr_qdzr:%s",sjbh,dygyr_qdzr.getId()));

										
										BDCS_QDZR_LS ls_dygyqdzr = new BDCS_QDZR_LS();
										PropertyUtils.copyProperties(ls_dygyqdzr,dygyr_qdzr);

										baseCommonDao.save(ls_dygyqdzr);
										logger.info(String.format("BDCS_QDZR_LS,SJBH:%s,ls_dygyqdzr:%s",sjbh,ls_dygyqdzr.getId()));
										String update_DygyqSql = "update FDC_DAK.EP_DYQRGYXX set SJCQBS = 1 where sjbh = '"+ sjbh+"'";

										baseCommonDao.excuteQueryNoResult(update_DygyqSql);
										logger.info("更新了一条房产抵押共有权人信息");
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								}else{
									/* 接下来关联到BDCS_QLR_XZ，并填充数据*/
									//single_map = jbxx.get(1);
									BDCS_QLR_XZ dyqlr = util.CreateSingleClass(BDCS_QLR_XZ.class, dyq_map);
									dyqlr.setQLID(dyql.getId());
									//dyqlr.setZJZL("1");//身份证
									
									dyqlr.setBDCQZH("柳房他证字第"+dyrqzh+"号");
									String dyqlrmc = dyqlr.getQLRMC();
									if(dyqlrmc != null){
										if(dyqlrmc.indexOf("银行") != -1 || dyqlrmc.indexOf("柳州") != -1 || dyqlrmc.indexOf("信用") != -1){
											dyqlr.setQLRLX("2");
										}else{
											dyqlr.setQLRLX("1");
											dyqlr.setZJZL("1");//身份证
											dyqlr.setISCZR("1");//持证人标志
										}
									}
									baseCommonDao.save(dyqlr);

									try {
										BDCS_QLR_LS ls_dyqlr = new BDCS_QLR_LS();
										PropertyUtils.copyProperties(ls_dyqlr,dyqlr);

										baseCommonDao.save(ls_dyqlr);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} 

									/* 接下来关联到BDCS_ZS_XZ，并填充数据*/
									//single_map = jbxx.get(1);
									BDCS_ZS_XZ dyzm = util.CreateSingleClass(BDCS_ZS_XZ.class, dyq_map);

									dyzm.setBDCQZH("柳房他证字第"+dyrqzh+"号");
									baseCommonDao.save(dyzm);

									try {
										BDCS_ZS_LS ls_dyzs = new BDCS_ZS_LS();
										PropertyUtils.copyProperties(ls_dyzs,dyzm);

										baseCommonDao.save(ls_dyzs);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} 

									/* 接下来关联到BDCS_QDZR_XZ，并填充数据*/
									BDCS_QDZR_XZ dyqdzr = new BDCS_QDZR_XZ();
									dyqdzr.setZSID(dyzm.getId());
									dyqdzr.setDJDYID(djdy.getDJDYID());
									dyqdzr.setQLID(dyql.getId());
									dyqdzr.setQLRID(dyqlr.getId());
									dyqdzr.setFSQLID(dyfsql.getId());
									dyqdzr.setBDCDYH(house.getBDCDYH());
									baseCommonDao.save(dyqdzr);
									logger.info(String.format("BDCS_QDZR_XZ,SJBH:%s,dyqdzr:%s",sjbh,dyqdzr.getId()));

									try {
										BDCS_QDZR_LS ls_dyqdzr = new BDCS_QDZR_LS();
										PropertyUtils.copyProperties(ls_dyqdzr,dyqdzr);

										baseCommonDao.save(ls_dyqdzr);
										logger.info(String.format("BDCS_QDZR_LS,SJBH:%s,ls_dyqdzr:%s",sjbh,ls_dyqdzr.getId()));
										String updateDyqSql = "update FDC_DAK.EP_FJTXQJBXX set SJCQBS = 1 where sjbh = '"+ sjbh+"'";

										baseCommonDao.excuteQueryNoResult(updateDyqSql);
										logger.info("更新了一条房产抵押共有权人信息");
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}
					}
				}

				/*-----------生成所有权的一系列表和转换数据后，接下来判断该房屋是否存在抵押权，有则再生成新的权利信息-------------*/
				if(cfxx.size() > 0 && cfxx != null){
					/*先生成查封  BDCS_QL_XZ 对象 并填充数据*/
					for (Map cf_map : cfxx) {
						BDCS_QL_XZ cfql = util.CreateSingleClass(BDCS_QL_XZ.class, cf_map);
						cfql.setBDCDYH(house.getBDCDYH());
						cfql.setDJDYID(djdy.getDJDYID());
						cfql.setQLLX("99");
						cfql.setDJLX("800");

						/*生成附属权利 对象，并填充数据*/
						//single_map = jbxx.get(2);
						BDCS_FSQL_XZ cffsql =util.CreateSingleClass(BDCS_FSQL_XZ.class, cf_map);
						cffsql.setDJDYID(djdy.getDJDYID());
						cffsql.setBDCDYH(house.getBDCDYH());
						cffsql.setQLID(cfql.getId());
						String cflx = cffsql.getCFLX();
						if(cflx != null){
							if(cflx.equals("轮候查封") || cflx.equals("轮后查封")){
								cffsql.setCFLX("2");
							}else{
								cffsql.setCFLX("1");
							}
						}else{
							cffsql.setCFLX("1");
						}

						cfql.setFSQLID(cffsql.getId());

						baseCommonDao.save(cfql);
						logger.info(String.format("BDCS_QL_XZ,SJBH:%s,cfql:%s",sjbh,cfql.getId()));

						baseCommonDao.save(cffsql);
						logger.info(String.format("BDCS_FSQL_XZ,SJBH:%s,cffsql:%s",sjbh,cffsql.getId()));

						try {
							BDCS_QL_LS ls_cfql = new BDCS_QL_LS();
							PropertyUtils.copyProperties(ls_cfql,cfql);

							BDCS_FSQL_LS ls_cffsql = new BDCS_FSQL_LS();
							PropertyUtils.copyProperties(ls_cffsql,cffsql);

							baseCommonDao.save(ls_cfql);
							logger.info(String.format("BDCS_QL_XZ,SJBH:%s,ls_cfql:%s",sjbh,ls_cfql.getId()));
							baseCommonDao.save(ls_cffsql);
							logger.info(String.format("BDCS_QL_LS,SJBH:%s,ls_cffsql:%s",sjbh,ls_cffsql.getId()));

							String updatecFSql = "update FDC_DAK.EP_FJCFQJBXX set SJCQBS = 1 where sjbh = '"+ sjbh+"'";
							baseCommonDao.excuteQueryNoResult(updatecFSql);
							logger.info("更新了一条房产档案库得查封信息");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							System.out.println("插入历史层有误 或者 更新查封表有误");
						}
					}
				}
				String sjbs = zrz.getFDCSJCQBS();
				if(sjbs == null){
					zrz.setFDCSJCQBS("抽取中");
					baseCommonDao.update(zrz);
					System.out.println("自然幢id："+zrz.getId());
				}
				String zrz_bdcdyh = zrz.getBDCDYH();
				String up_bdck_zrzsql = "update bdck.bdck_zrz_xz set FDCSJCQBS = '正在抽取中，但未完成' where BDCDYH = '"+ zrz_bdcdyh+"'";
				try {
					baseCommonDao.excuteQueryNoResult(up_bdck_zrzsql);
					logger.info("更新了一条空间自然幢信息");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			baseCommonDao.flush();
		}
		insertOrUpdateMaxDyh(id);
		m.setTotal(fileList.size());
		m.setRows(fileList);
		return m;
	}
	/**
	 * 判断更新bdcdck.bdcs_maxdyh表里的值
	 * @param zrzbdcdyid
	 */
	public void insertOrUpdateMaxDyh(String zrzbdcdyid){
		BDCS_ZRZ_XZ zrz_xz = baseCommonDao.get(BDCS_ZRZ_XZ.class, zrzbdcdyid);
		String zrzid = zrz_xz.getId();
		System.out.println(zrzid);
		if(zrzid != null){
			String dyh_fullsql = "select max(substr(bdcdyh,25,5)) as BDCDYHHSW from bdck.bdcs_h_xz where" +  " ZRZBDCDYID = '"+zrzbdcdyid+"'";
			List<Map> h_dyhs = baseCommonDao.getDataListByFullSql(dyh_fullsql);//查询某自然幢下的所有户中不动产单元号后三位最大的值；
			String currentmaxdyh = (String) h_dyhs.get(0).get("BDCDYHHSW");

			String zrz_dyh = zrz_xz.getBDCDYH();
			if(zrz_dyh != null){
				zrz_dyh = zrz_dyh.substring(0, zrz_dyh.length() -4);
				String fromSql = "from bdcdck.bdcs_maxdyh where relyonvalue = '"+zrz_dyh+"'";
				long bdcs_maxdyh = baseCommonDao.getCountByFullSql(fromSql);

				String dyh = "";
				if(currentmaxdyh != null){
					dyh = currentmaxdyh.substring(currentmaxdyh.length()-4,currentmaxdyh.length());
					for(int i = 0; i<dyh.length();i++){
						if(dyh.indexOf("000") != -1){
							dyh = dyh.substring(dyh.length()-1,dyh.length());
						} 
						if(dyh.indexOf("00") != -1){
							dyh = dyh.substring(dyh.length()-2,dyh.length());
						}
						if(dyh.indexOf("0") != -1){
							dyh = dyh.substring(dyh.length()-3,dyh.length());
						}
					}
				}
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = formatter.format(date);
				if(bdcs_maxdyh == 0){
					String insert_sql = "insert into bdcdck.bdcs_maxdyh (relyonvalue,MaxXH,dylx,CREATETIME) "
							+ "values('"+zrz_dyh+"'"+",'"+dyh+"',"+"'04'"+","+"to_date('"+currentTime+"','yyyy-mm-dd hh24:mi:ss')"+")";
					try {
						baseCommonDao.excuteQueryNoResult(insert_sql);
						logger.info(String.format("插入一条最大单元号数据,RELYONVALUE:%s,CREATETIME:%s",zrz_dyh,currentTime));	
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					String updateMaxDyh_sql = "update bdcdck.bdcs_maxdyh set MAXXH = '"+dyh +"', MODIFYTIME = "+"to_date('"+currentTime+"','yyyy-mm-dd hh24:mi:ss')"+" where RELYONVALUE = '"+zrz_dyh+"'";
					try {
						baseCommonDao.excuteQueryNoResult(updateMaxDyh_sql);
						logger.info(String.format("更新一条最大单元号数据,RELYONVALUE:%s,CREATETIME:%s",zrz_dyh,currentTime));	
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public String updateZRZ(String id,String cqbs,HttpServletRequest request){
		String up_bdcszrzsql = "update bdck.bdcs_zrz_xz set FDCSJCQBS = '"+cqbs +"' where bdcdyid = '"+id+"'";
		String up_bdckzrzsql = "update bdck.bdck_zrz_xz set FDCSJCQBS = '"+cqbs +"' where bdcdyid = '"+id+"'";
		
		try {
			baseCommonDao.excuteQueryNoResult(up_bdcszrzsql);
			baseCommonDao.excuteQueryNoResult(up_bdckzrzsql);
			logger.info(String.format("更新两条自然幢数据,BDCDYID:%s,FDCSJCQBS:%s",id,cqbs));	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cqbs;
	}
	/**
	 * 同步空间自然幢
	 * @param bdck_zrz
	 * @param request
	 * @return
	 */
	public String update_bdck_ZRZ(String bdck_zrz,HttpServletRequest request){
		String getBdcs_ybj_zrzSql = "select * from bdck.bdcs_zrz_xz where fdcsjcqbs = '抽取中'";
		List<Map> bdcs_zrz = baseCommonDao.getDataListByFullSql(getBdcs_ybj_zrzSql);
		long tb_count = 0;
		for (Map map : bdcs_zrz) {
			String bdcdyh = (String) map.get("BDCDYH");
			String sjcqbs = (String) map.get("FDCSJCQBS");
			if(bdcdyh != null){
				String updateBdck_zrzSql = "update bdck.bdck_zrz_xz set fdcsjcqbs = '抽取中' where bdcdyh = '"+bdcdyh+"'";
				try {
					baseCommonDao.excuteQueryNoResult(updateBdck_zrzSql);
					logger.info(String.format("更新一条空间库里的自然幢数据抽取标记,BDCDYH:%s,FDCSJCQBS:%s",bdcdyh,sjcqbs));
					tb_count ++;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "抽取出错，bdcdyh="+bdcdyh;
				}
			}
		}
		return "已同步"+tb_count+"条空间自然幢数据抽取标识码";
		
	}
	
	/**
	 * 获取前去抽取有些问题的数据
	 * @author HKS
	 * @date 2016/9/12 11:26
	 * @return
	 */
	/*@SuppressWarnings("rawtypes")
	public Message checkBDCDataFromFDC(){
		Message msg = new Message();
		List<Map> compare_list = new ArrayList<Map>();
		String date_incompleteSql = "select * from fdc_dak.ep_fjsyqjbxx where sjcqbs = '1' and clh is not null";
		List<Map> incompleteSqlList = baseCommonDao.getDataListByFullSql(date_incompleteSql);
		for (Map condition_map : incompleteSqlList) {
			String sjbh = (String) condition_map.get("SJBH");

			String fullSql = "select * from fdc_dak.ep_fdc_to_bdc where 1 = 1 AND SJBH = '"+sjbh+"'";
		
			List<Map> query_result = baseCommonDao.getDataListByFullSql(fullSql);
			if(query_result != null && query_result.size() > 0){
				incompleteSqlList.remove(condition_map);
			}else{
				compare_list.add(condition_map);
			}
		}
		for (Map mapping_bdcMap : compare_list) {
			String fwzl = (String) mapping_bdcMap.get("FWZL");
			String curren_sjbh  = (String) mapping_bdcMap.get("SJBH");
			
			List<BDCS_H_XZ> houses = baseCommonDao.getDataList(BDCS_H_XZ.class, "zl = '"+fwzl+"'");
			if(houses != null && houses.size() > 0){
				String bdcdyh = houses.get(0).getBDCDYH();
			}
		}
		msg.setTotal(compare_list.size());
		msg.setRows(compare_list);
		
		return msg;
	}*/
	
	/**
	 * 根据不动产单元号获取土地证的状态
	 * "1":有土地证,"0":无土地证
	 * @author heks
	 * @param _bdcdyh
	 * @return
	 */
	public String getTdStatu(String _bdcdyh){
		
		String fullsql = "select t.TD_STATUS,xmxx.slsj from smwb_support.gx_config t join bdck.bdcs_xmxx xmxx on t.file_number=xmxx.project_id "
				+ "join bdck.bdcs_djdy_gz djdy on xmxx.xmbh = djdy.xmbh "
				+ "join bdck.bdcs_h_xz h on djdy.bdcdyid = h.bdcdyid where h.bdcdyh = '"+_bdcdyh+"' order by xmxx.slsj desc";
		List<Map> gx_configs = baseCommonDao.getDataListByFullSql(fullsql);
		// = baseCommonDao.getDataList(GX_CONFIG.class, "file_number in (select ywh from bdck.bdcs_ql_xz where bdcdyh = '"+_bdcdyh+"')");
		String td_status = "";
		if(gx_configs.size() > 0){ 
			/*for(int i = 0 ;i<gx_configs.size();i++){
				if(td_status != null && td_status.equals("1")){
					continue;
				}
			}*/
			td_status =  (String) gx_configs.get(0).get("TD_STATUS");
			return td_status;
			
		}else{
			String fullsql_gz = "select t.TD_STATUS,xmxx.slsj from smwb_support.gx_config t join bdck.bdcs_xmxx xmxx on t.file_number=xmxx.project_id "
					+ "join bdck.bdcs_djdy_gz djdy on xmxx.xmbh = djdy.xmbh "
					+ "join bdck.bdcs_h_gz h on djdy.bdcdyid = h.bdcdyid where h.bdcdyh = '"+_bdcdyh+"' order by xmxx.slsj desc";
			List<Map> gx_configs_gz = baseCommonDao.getDataListByFullSql(fullsql_gz);
			if(gx_configs_gz.size() > 0 ){ 
				
				td_status =  (String) gx_configs_gz.get(0).get("TD_STATUS");
				return td_status;
			}else{
				String fullsql_ls = "select t.TD_STATUS,xmxx.slsj from smwb_support.gx_config t join bdck.bdcs_xmxx xmxx on t.file_number=xmxx.project_id "
						+ "join bdck.bdcs_djdy_gz djdy on xmxx.xmbh = djdy.xmbh "
						+ "join bdck.bdcs_h_ls h on djdy.bdcdyid = h.bdcdyid where h.bdcdyh = '"+_bdcdyh+"' order by xmxx.slsj desc";
				List<Map> gx_configs_ls = baseCommonDao.getDataListByFullSql(fullsql_ls);
				if(gx_configs_ls.size() > 0 ){ 
					
					td_status =  (String) gx_configs_ls.get(0).get("TD_STATUS");
					return td_status;
				}else{
					return "";
				}
			}
		}
		
	}
	
	/**
	 * 查封超期监控-- 获取当前用户名下查封业务的时限信息
	 */
	public  Message cfControl(){
		Message msg=new Message();
		User user = Global.getCurrentUserInfo();//收款人员
		String jkjs = GetProperties.getConstValueByKey("ControlUser");//监控角色
		if("".equals(jkjs)){
			msg.setSuccess("false");
			msg.setMsg("未配置查封监控角色，不进行查解封超期提示。");
			return msg;
		}
		//判断当前登录人员是否在监控角色下，在监控角色下时查询名下查封业务的时限信息
		String countjksql="select count(1) from SMWB_FRAMEWORK.RT_USERROLE t where t.roleid="
				+ "(select id from SMWB_FRAMEWORK.T_ROLE t where t.rolename='"+jkjs+"') "
				+ "and t.userid='"+user.getId()+"'";
		long countjk=baseCommonDao.getCountByCFullSql(countjksql);
		if(countjk>0){
			String jksql=getCFJKSql(user.getId());//获取监控查询语句
			List<Map> jklist=baseCommonDao.getDataListByFullSql(jksql);
			msg.setRows(jklist);
			msg.setSuccess("true");
			msg.setMsg("查询完成,当前登录人属于查封监控角色。");
		}else{
			msg.setSuccess("false");
			msg.setMsg("当前登录人不在查封监控角色下。");
		}
		return msg;
	}
	
	/**
	 * 超期监控sql
	 * TODO
	 * @Title: getCFJKSql
	 * @author lgqyk
	 * @date   2018-07-27 11:11
	 * @return String
	 */
	public String getCFJKSql(String staffid){
		StringBuilder stb=new StringBuilder();
		stb.append("select * from (");
		stb.append("select po.prolsh,");
		stb.append("abs(ceil(po.proinst_willfinish-sysdate)) syts,");
		stb.append("case when ((po.proinst_willfinish-sysdate)<7 and (po.proinst_willfinish-sysdate)>0) then '即将超期' ");
		stb.append("when (po.proinst_willfinish-sysdate)<0 then '已超期' ");
		stb.append("else '正常' end cqzt ");
		stb.append("FROM BDC_WORKFLOW.WFI_ACTINSTSTAFF ACTSTAFF ");
		stb.append("INNER JOIN BDC_WORKFLOW.WFI_NOWACTINST AI ON ACTSTAFF.ACTINST_ID=AI.ACTINST_ID ");
		stb.append("INNER JOIN BDC_WORKFLOW.WFI_PROINST PO ON AI.PROINST_ID=PO.PROINST_ID ");
		stb.append("where AI.Passedroute_Count is null  and AI.ACTINST_STATUS in (1,2,14,15) ");
		stb.append("and ACTSTAFF.STAFF_ID='");
		stb.append(staffid);
		stb.append("' and po.prodef_name like '查、解封登记%' ");
		stb.append("and po.proinst_end is null ");
		stb.append("and ( AI.STAFF_ID is null or AI.STAFF_ID='");
		stb.append(staffid);
		stb.append("' or  AI.CODEAL =1)");
		stb.append(") where cqzt <>'正常' order by cqzt desc,syts asc");
		return stb.toString();
	}
}
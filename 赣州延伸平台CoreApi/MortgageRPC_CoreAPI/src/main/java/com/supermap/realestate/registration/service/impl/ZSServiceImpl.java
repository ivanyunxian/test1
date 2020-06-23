package com.supermap.realestate.registration.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.DJFZXX;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJFZEX;
import com.supermap.realestate.registration.model.BDCS_DJSZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_QZGLTJB;
import com.supermap.realestate.registration.model.BDCS_RKQZB;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.service.ZSService2;
import com.supermap.realestate.registration.tools.CertInfoTools;
import com.supermap.realestate.registration.tools.CertfyInfoTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.BarcodeUtil;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.QRCodeHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

import net.sf.json.JSONArray;

public class ZSServiceImpl implements ZSService {

	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private ZSService2 zsservice2;

	@Override
	public BDCS_DJFZ GetFZList(String fzid) {
		return baseCommonDao.get(BDCS_DJFZ.class, fzid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page GetPagedFZList(String xmbh, int pageIndex, int rows) {
		List<DJFZXX> djfzxxList = new ArrayList<DJFZXX>();
		// 证书查询参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("xmbh", xmbh);
		String hql = " length(bDCQZH)>0 and xMBH=:xmbh";
		// 获取证书表中信息
		Page p = baseCommonDao.getPageDataByHql(BDCS_ZS_GZ.class, hql, map,
				pageIndex, rows);
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);

		List<BDCS_ZS_GZ> zsList = (List<BDCS_ZS_GZ>) p.getResult();
		List<String> bdcqzhlist=new ArrayList<String>();
		for (BDCS_ZS_GZ zs : zsList) {
			if(!StringHelper.isEmpty(zs.getBDCQZH()) && !bdcqzhlist.contains(zs.getBDCQZH())){
				bdcqzhlist.add(zs.getBDCQZH());
			}
			else{
				continue;
			}
			DJFZXX djfzxxBo = new DJFZXX();
			String strBDCQZH = zs.getBDCQZH();
			djfzxxBo.setZSBH(zs.getZSBH());
			djfzxxBo.setBDCQZH(strBDCQZH);
			djfzxxBo.setXMBH(zs.getXMBH());
			djfzxxBo.setCfdagh(StringHelper.isEmpty(xmxx.getDAGH()) ? "" : xmxx
					.getDAGH().toString());
			String strXMBH = zs.getXMBH();
			String strZSID = zs.getId();

			// 查询权地证人关系表
			StringBuilder builerqdzr = new StringBuilder();
			builerqdzr.append("XMBH='").append(strXMBH).append("' AND ZSID='")
					.append(strZSID).append("'");
			List<BDCS_QDZR_GZ> gxbList = baseCommonDao.getDataList(
					BDCS_QDZR_GZ.class, builerqdzr.toString());
			if (!gxbList.isEmpty()) {
				BDCS_QDZR_GZ qdzr = gxbList.get(0);
				String strQLRID = qdzr.getQLRID();
				String strBDCDYH = qdzr.getBDCDYH();
				String strQLID = qdzr.getQLID();
				djfzxxBo.setBDCDYH(strBDCDYH);

				// 查询权利数据
				StringBuilder builerql = new StringBuilder();
				builerql.append("XMBH='").append(strXMBH).append("' AND id='")
						.append(strQLID).append("'");
				List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
						BDCS_QL_GZ.class, builerql.toString());
				if (!qlList.isEmpty()) {
					BDCS_QL_GZ bdcs_ql_gz = qlList.get(0);
					if (bdcs_ql_gz != null) {
						djfzxxBo.setBDCDYH(bdcs_ql_gz.getBDCDYH());
						if (bdcs_ql_gz.getCZFS().equals(CZFS.GTCZ.Value)) {
							// 权利人
							StringBuilder strBuilder = new StringBuilder();
							StringBuilder builerqlr = new StringBuilder();
							builerqlr.append("XMBH='").append(strXMBH)
									.append("' AND QLID='").append(strQLID)
									.append("'");
							List<BDCS_QLR_GZ> qlrList = baseCommonDao
									.getDataList(BDCS_QLR_GZ.class,
											builerqlr.toString());
							for (BDCS_QLR_GZ qlr : qlrList) { // 权利人可能有多个
								strBuilder.append(qlr.getQLRMC() + ",");
							}
							String strQLRMC = "";
							if (strBuilder.toString().length() > 0) {
								strQLRMC = strBuilder.toString().substring(0,
										strBuilder.length() - 1);// 去除最后一个,号
							}
							djfzxxBo.setQLR(strQLRMC);
						} else {
							// 权利人
							StringBuilder strBuilder = new StringBuilder();
							StringBuilder builerqlr = new StringBuilder();
							builerqlr.append("XMBH='").append(strXMBH)
									.append("' AND id='").append(strQLRID)
									.append("'");
							List<BDCS_QLR_GZ> qlrList = baseCommonDao
									.getDataList(BDCS_QLR_GZ.class,
											builerqlr.toString());
							for (BDCS_QLR_GZ qlr : qlrList) { // 权利人可能有多个
								strBuilder.append(qlr.getQLRMC() + ",");
							}
							String strQLRMC = "";
							if (strBuilder.toString().length() > 0) {
								strQLRMC = strBuilder.toString().substring(0,
										strBuilder.length() - 1);// 去除最后一个,号
							}
							djfzxxBo.setQLR(strQLRMC);
						}
					}
				}
				StringBuilder str = new StringBuilder();
				StringBuilder sqrBuilder = new StringBuilder();
				str.append("XMBH='").append(xmbh).append("' and SQRLB='2'");
				List<BDCS_SQR> sqrlist = baseCommonDao.getDataList(
						BDCS_SQR.class, str.toString());
				if(!sqrlist.isEmpty()){
					for (BDCS_SQR sqr : sqrlist) { // 权利人可能有多个
						sqrBuilder.append(sqr.getSQRXM() + ",");
					}
					String strYWRXM = "";
					if (sqrBuilder.toString().length() > 0) {
						strYWRXM = sqrBuilder.toString().substring(0,
								sqrBuilder.length() - 1);// 去除最后一个,号
					}
					djfzxxBo.setM_ywr(strYWRXM);
				}
				
			}

			// 查询发证信息
			StringBuilder builerfz = new StringBuilder();
			builerfz.append("XMBH='").append(strXMBH).append("' AND HFZSH='")
					.append(strBDCQZH).append("'");
			List<BDCS_DJFZ> fzList = baseCommonDao.getDataList(BDCS_DJFZ.class,
					builerfz.toString());
			if (fzList.size() > 0) {
				djfzxxBo.setLZR(fzList.get(0).getLZRXM());
				djfzxxBo.setLZSJ(fzList.get(0).getFZSJ());
				djfzxxBo.setSFFZ("是");
				djfzxxBo.setCZLX("撤销");
				djfzxxBo.setZSID(fzList.get(0).getId());
			} else {
				djfzxxBo.setSFFZ("否");
				djfzxxBo.setCZLX("发证");
			}
			djfzxxList.add(djfzxxBo);
		}
		Page resultPage = new Page(Page.getStartOfPage(pageIndex, rows),
				djfzxxList.size(), rows, djfzxxList);
		Long totalCount = baseCommonDao
				.getCountByFullSql(" from BDCK.BDCS_ZS_GZ Where length(bDCQZH)>0 and "
						+ ProjectHelper.GetXMBHCondition(xmbh));
		resultPage.setTotalCount(totalCount);
		return resultPage;
	}

	@Override
	public void AddFZXX(BDCS_DJFZ djfz) {
		baseCommonDao.save(djfz);
		baseCommonDao.flush();
	}

	
	@Override
	public void DeleteFZXX(String djfzid,String xmbh) {
		BDCS_DJFZ djfz = getDJFZ("id='"+ djfzid +"'");
		String hfzsh = djfz.getHFZSH();
		
		//添加发证信息到rkqzb中
		List<BDCS_ZS_GZ> zslst = baseCommonDao.getDataList(BDCS_ZS_GZ.class,
				"xmbh='" + xmbh + "' AND BDCQZH='" + hfzsh + "'");
		if (zslst != null && zslst.size() > 0) {
			if (!StringHelper.isEmpty(zslst.get(0).getZSBH())) {
				int start = 0;
				int end = 1;
			    //如果编号第一个为字母
				String zsbh_all=zslst.get(0).getZSBH(), zsbh=zsbh_all;
				String bdcqzh=zslst.get(0).getBDCQZH();
				String qzlx="0";
				String qzzl="D";
			    if (zsbh_all.substring(start, end).matches("[a-zA-Z]")){
			      zsbh = zsbh_all.substring(end, zsbh_all.length());
			      qzzl = zsbh_all.substring(start,end);
			    }
			  
			    if(bdcqzh.contains("不动产证明第")) {
					qzlx="1";
				}
				BDCS_RKQZB rkqzb = getRKQZB("QZBH='" + zsbh + "' AND QZLX='" + qzlx + "' AND QZZL='" + qzzl + "'");
				if (rkqzb != null) {
					rkqzb.setFZSJ(null);
					rkqzb.setSFFZ(SF.NO.Value);
					rkqzb.setLZRY(null);
					rkqzb.setLZRZJH(null);
					rkqzb.setLZRPIC(null);
					//rkqzb.setBDCQZH(null);
					baseCommonDao.update(rkqzb);
					baseCommonDao.flush();
				}				
			}					
		}
		
		baseCommonDao.delete(BDCS_DJFZ.class, djfzid);
		baseCommonDao.flush();
	}

	/**
	 * 添加发证信息到rkqzb
	 * 
	 * @param djfz
	 */
	@Override
	public void AddFZXXtoRKQZB(String xmbh, String bdcqzh, Date fzsj, String lzrxm,String lzrzjhm, String bdcdylx){
		String qLRMC = null, qLRZJH = null, zl=null;		
		List<BDCS_QLR_GZ> listqlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class,
				"BDCQZH='" + bdcqzh + "' AND XMBH='" + xmbh  +"'");
		List<String> list_qLRMC = new ArrayList<String>();
		List<String> list_qLRZJH = new ArrayList<String>();
		for (int i = 0; i < listqlrs.size(); i++) {
			BDCS_QLR_GZ bdcs_qlr_gz = listqlrs.get(i);
			if (bdcs_qlr_gz.getQLRMC() != null
					&& !StringHelper.isEmpty(bdcs_qlr_gz.getQLRMC())) {
				if (!list_qLRMC.contains(bdcs_qlr_gz.getQLRMC())) {
					list_qLRMC.add(bdcs_qlr_gz.getQLRMC());
					qLRMC = StringHelper.isEmpty(qLRMC) ? bdcs_qlr_gz.getQLRMC() : qLRMC + "<br/>"+ bdcs_qlr_gz.getQLRMC();
				}
			}
			if (bdcs_qlr_gz.getZJH() != null
					&& !StringHelper.isEmpty(bdcs_qlr_gz.getZJH())) {
				if (!list_qLRZJH.contains(bdcs_qlr_gz.getZJH())) {
					list_qLRZJH.add(bdcs_qlr_gz.getZJH());
					qLRZJH = StringHelper.isEmpty(qLRZJH) ? bdcs_qlr_gz.getZJH() : qLRZJH + "<br/>"+ bdcs_qlr_gz.getZJH();
				}
			}
		}
		BDCDYLX bDCDYLX = null;
		if (bdcdylx == null && StringHelper.isEmpty(bdcdylx)) {
			bDCDYLX =ProjectHelper.GetBDCDYLX(xmbh);
		}else {
			bDCDYLX = BDCDYLX.initFrom(bdcdylx);
		}
		
		List<BDCS_QL_GZ> listqls = baseCommonDao.getDataList(BDCS_QL_GZ.class,
				"BDCQZH='" + bdcqzh + "' AND XMBH='" + xmbh  +"'");	
		for (int i = 0; i < listqls.size(); i++) {
			BDCS_QL_GZ ql = listqls.get(i);
			String bdcdyh = ql.getBDCDYH();
			//获取bdcdyid
			List<BDCS_DJDY_GZ> listsjdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,
					"BDCDYH='" + bdcdyh + "' AND XMBH='" + xmbh  +"'");	
			if (listsjdys != null && listsjdys.size() > 0) {
				String bdcdyid = listsjdys.get(0).getBDCDYID();
				DJDYLY djdyly = DJDYLY.initFrom("01");
				// 加载单元信息
				RealUnit unit = UnitTools.loadUnit(bDCDYLX, djdyly, bdcdyid);
				if (unit != null){
					zl = StringHelper.isEmpty(zl) ? unit.getZL() : zl + "<br/>"+ unit.getZL() ;
				}else {
					djdyly = DJDYLY.initFrom("02");
					unit = UnitTools.loadUnit(bDCDYLX, djdyly, bdcdyid);
					if (null != unit){
						zl = StringHelper.isEmpty(zl) ? unit.getZL() : zl + "<br/>"+ unit.getZL() ;
					}
				}
			}
		}		
		
		List<BDCS_ZS_GZ> zslst = baseCommonDao.getDataList(BDCS_ZS_GZ.class,
				"xmbh='" + xmbh + "' AND BDCQZH='" + bdcqzh + "'");
		String qzlx ="0";
		if( bdcqzh.contains("不动产证明第")) {
				qzlx="1";
		}
		if (!StringHelper.isEmpty(zslst.get(0).getZSBH())) {
			int start = 0;
			int end = 1;
		    //如果编号第一个为字母
			String zsbh_all=zslst.get(0).getZSBH(),zsbh=zsbh_all;
			String qzzl="D";
		    if (zsbh_all.substring(start, end).matches("[a-zA-Z]")){
		      zsbh = zsbh_all.substring(end, zsbh_all.length());
		      qzzl = zsbh_all.substring(start,end);
		    }
		    
			BDCS_RKQZB rkqzb = getRKQZB("QZBH='"+zsbh+"' AND QZLX='"+qzlx+"' AND QZZL='"+qzzl+"'");
			BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
			String slbh = "";
			if (xmxx != null ) {
				slbh = xmxx.getYWLSH();
			}
			if (rkqzb != null) {
				rkqzb.setQLRMC(qLRMC);
				rkqzb.setQLRZJH(qLRZJH);				
				rkqzb.setSFFZ(SF.YES.Value);
				rkqzb.setFZSJ(fzsj);
				rkqzb.setLZRY(lzrxm);
				rkqzb.setLZRZJH(lzrzjhm);
				rkqzb.setBDCQZH(bdcqzh);
				rkqzb.setZL(zl);
				rkqzb.setSLBH(slbh);
				String syqk = StringHelper.isEmpty(rkqzb.getSYQK()) ? "" : rkqzb.getSYQK()+"<br/>";
				rkqzb.setSYQK(syqk + StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"进行发证，领证人"+lzrxm+";");
				baseCommonDao.update(rkqzb);
				baseCommonDao.flush();
			}else if(!StringHelper.isEmpty(zsbh)){				
				BDCS_RKQZB rkqz = new BDCS_RKQZB();
				rkqz.setQLRMC(qLRMC);
				rkqz.setQLRZJH(qLRZJH);				
				rkqz.setSFFZ(SF.YES.Value);
				rkqz.setSFSZ(SF.YES.Value);//默认发证时已经缮证了（保存编号即为缮证）
				rkqz.setSFZF(SF.NO.Value);
				rkqz.setFZSJ(fzsj);
				rkqz.setLZRY(lzrxm);
				rkqz.setLZRZJH(lzrzjhm);
				rkqz.setBDCQZH(bdcqzh);
				rkqz.setZL(zl);
				//新增基本信息
				rkqz.setId((String) SuperHelper.GeneratePrimaryKey());					
				rkqz.setCJSJ( new Date());
				rkqz.setQZBH(StringHelper.getLong(zsbh));	
				rkqz.setQZZL(qzzl);	
				rkqz.setQZLX(qzlx);
				rkqz.setSLBH(slbh);
				String syqk = StringHelper.isEmpty(rkqz.getSYQK()) ? "" : rkqz.getSYQK()+"<br/>";
				rkqz.setSYQK(syqk + StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"进行发证，领证人"+lzrxm+";");
				baseCommonDao.save(rkqz);
				baseCommonDao.flush();
			}
		}
	}


	
	
	/**
	 * 在打证环节，通过项目编号查找登记缮证信息，若存在，不做任何处理，若不存在，新增一条信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年9月8日下午2:48:57
	 * @param xmbh
	 */
	@Override
	public void updateDJSZ(String xmbh) {
		String xmbhSql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_DJSZ> djszs = baseCommonDao.getDataList(BDCS_DJSZ.class,
				xmbhSql);

		if (djszs != null && djszs.size() > 0) {

		} else {
			BDCS_DJSZ bdcs_djsz = new BDCS_DJSZ();
			String dbr = Global.getCurrentUserName();
			BDCS_XMXX bdcs_xmxx = Global.getXMXXbyXMBH(xmbh);
			if (bdcs_xmxx != null) {
				bdcs_djsz.setYWH(bdcs_xmxx.getPROJECT_ID());
				bdcs_djsz.setSZRY(dbr);
				Date szsj = new Date();
				bdcs_djsz.setSZSJ(szsj);
				bdcs_djsz.setXMBH(xmbh);
			}
			baseCommonDao.save(bdcs_djsz);
			baseCommonDao.flush();
		}
	}

	@Override
	public List<Tree> getZsTreeEx(String xmbh) {
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)) {
			return getUnionZsTreeEx(xmbh);
		}else{
			return GetCurrencyZsTree(xmbh);
		}
	}
	@Override
	public List<Tree> getZsTreeCombo(String xmbh,String qllx) {
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)) {
			return getUnionZsTreeExCombo(xmbh,qllx);
		}else{
			return GetCurrencyZsTree(xmbh);
		}
	}
	
	/**
	 * 通用证书版信息
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Tree> GetCurrencyZsTree(String xmbh){
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		List<BDCS_ZS_GZ> zslst = baseCommonDao.getDataList(BDCS_ZS_GZ.class,
				"xmbh='" + xmbh + "' order by BDCQZH");
		List<Tree> trs = new ArrayList<Tree>();
		String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
		for (BDCS_ZS_GZ zs : zslst) {
			List<Map> qlrlst = this.getQLREx(xmbh, zs.getId());
			if (qlrlst != null && qlrlst.size() > 0) {
				StringBuilder result = new StringBuilder();
				boolean flag = false;
				String qlid = null;
				if (Baseworkflow_ID.indexOf("YY")==0) {
					List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, " XMBH='" + xmbh + "'");
					for (BDCS_QLR_GZ qlr : qlrs) {
						if (flag) {
							result.append(",");
						} else {
							flag = true;
						}
						if (qlid == null)
							qlid = qlr.getQLID();
						result.append(qlr.getQLRMC());
					}
				}else {
					for (Map qlr : qlrlst) {
						if (flag) {
							result.append(",");
						} else {
							flag = true;
						}
						if (qlid == null)
							qlid = qlr.get("QLID").toString();
						result.append(qlr.get("QLRMC"));
					}
				}
				
				if (zs.getZSBH() != null && !StringHelper.isEmpty(zs.getZSBH())) {
					result.append("(证书编号:" + zs.getZSBH() + ")");
				}

				Tree tr = new Tree();
				tr.setText(result.toString());
				tr.setZsbh(zs.getZSBH());
				tr.setId(zs.getId());
				tr.setBdcqzh(zs.getBDCQZH());
				tr.setTag3(zs.getBDCQZH());
				String bdcqzhxh=StringHelper.getBDCQZHXH(zs.getBDCQZH());
				tr.setQzhxh(bdcqzhxh);
				Rights rights = RightsTools.loadRights(DJDYLY.GZ, qlid);
				if (rights != null) {
					tr.setTag1(rights.getDJLX());
					tr.setTag2(rights.getQLLX());
					String djdyid = rights.getDJDYID();
					List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
							BDCS_DJDY_GZ.class, "DJDYID='" + djdyid + "'");
					if (djdys != null) {
						BDCS_DJDY_GZ djdy = djdys.get(0);
						BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
						DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
						RealUnit unit = UnitTools.loadUnit(lx, ly,
								djdy.getBDCDYID());
						if(unit instanceof House){
							House house = (House) unit;
							String dyh=house.getDYH();
							String fh=house.getFH();
							Double qsc=house.getQSC();
							String zrzh=house.getZRZH();
							if(!StringHelper.isEmpty(dyh)){
								tr.setDyh(dyh);
							}
							if(!StringHelper.isEmpty(fh)){
								tr.setFh(fh);
							}
							if(qsc!=null){
								tr.setQsc(qsc);
							}
							if(!StringHelper.isEmpty(zrzh)){
								tr.setZrzh(zrzh);
							}
						}
						if (unit != null) {
							tr.setTag3(tr.getTag3() + unit.getZL());
							tr.setText(result.toString() +" "+ unit.getZL());
						}
					}
				}
				/*查询打印的次数heks*/
				String project_id = xmxx.getPROJECT_ID();
				if(project_id != null){
					String fromSql = " FROM BDCK.BDCS_PRINTRECORD WHERE PRINTTYPE='SZDJ"+"' AND PROJECT_ID ='" + project_id+"-"+ zs.getId()+"'";
					long count = baseCommonDao.getCountByFullSql(fromSql);
					tr.setFzcs(count);
				}
				trs.add(tr);
			}
		}
		/***排序啦 by 赵梦帆 2017-05-18 08:40:24***/
		trs = ObjectHelper.SortList(trs);
		return trs;
	}

	/**
	 * 当多个单元合发一本证书时调用
	 * 
	 * @Title: getUnionZsTreeEx
	 * @author:liushufeng
	 * @date：2015年11月14日 下午7:30:55
	 * @param xmbh
	 * @return
	 */
	private List<Tree> getUnionZsTreeEx(String xmbh) {
		List<Tree> trs = new ArrayList<Tree>();
		List<Rights> rightss = RightsTools.loadRightsByCondition(DJDYLY.GZ,ProjectHelper.GetXMBHCondition(xmbh));
		HashMap<String,List<Integer>> m_qllx=new HashMap<String,List<Integer>>();
		if (rightss != null && rightss.size() > 0) {
			for(Rights rights:rightss){
				if(!StringUtils.isEmpty(rights.getISCANCEL())&&rights.getISCANCEL().equals("1"))//已经取消的权利不计入证书里面wuzhu20161209
					continue;
				String qlid = rights.getId();
				String qllx=rights.getQLLX();
				String djdyid = rights.getDJDYID();
				List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
						BDCS_DJDY_GZ.class, "DJDYID='" + djdyid + "' AND XMBH='"+xmbh+"'");
				//ws7/16  加个判断 否则报java.lang.IndexOutOfBoundsException异常
				if(djdys==null||djdys.isEmpty()){
					continue;
				}
				Integer groupid=djdys.get(0).getGROUPID();
				if(m_qllx.containsKey(qllx)){
					if(m_qllx.get(qllx).contains(groupid)){
						continue;
					}else{
						List<Integer> list_group=m_qllx.get(qllx);
						list_group.add(groupid);
						m_qllx.remove(qllx);
						m_qllx.put(qllx, list_group);
					}
				}else{
					List<Integer> list_group=new ArrayList<Integer>();
					list_group.add(groupid);
					m_qllx.put(qllx, list_group);
				}
				String hql = "xmbh='" + xmbh+ "' and zsid in (select ZSID from BDCS_QDZR_GZ WHERE QLID='"
						+ qlid + "' AND XMBH='" + xmbh + "') order by BDCQZH";
				List<BDCS_ZS_GZ> zslst = baseCommonDao.getDataList(
						BDCS_ZS_GZ.class, hql);
				for (BDCS_ZS_GZ zs : zslst) {
					List<BDCS_QLR_GZ> qlrlst = this.getQLR(xmbh, zs.getId());
					if (qlrlst != null && qlrlst.size() > 0) {
						StringBuilder result = new StringBuilder();
						boolean flag = false;
						for (BDCS_QLR_GZ qlr : qlrlst) {
							if (flag) {
								result.append(",");
							} else {
								flag = true;
							}
							if (qlid == null)
								qlid = qlr.getQLID();
							result.append(qlr.getQLRMC());
						}
						if (zs.getZSBH() != null
								&& !StringHelper.isEmpty(zs.getZSBH())) {
							result.append("(证书编号:" + zs.getZSBH() + ")");
						}

						Tree tr = new Tree();
						tr.setZsbh(zs.getZSBH());
						tr.setText(result.toString());
						tr.setId(zs.getId());
						tr.setTag3(zs.getBDCQZH());
						if (!StringHelper.isEmpty(zs.getCFDAGH())){
							String bdcqzhxh=StringHelper.getBDCQZHXH(zs.getBDCQZH());
							tr.setQzhxh(bdcqzhxh);
						}
						
						if (rights != null) {
							tr.setTag1(rights.getDJLX());
							tr.setTag2(rights.getQLLX());
							if (djdys != null) {
								BDCS_DJDY_GZ djdy = djdys.get(0);
								BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
								DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
								RealUnit unit = UnitTools.loadUnit(lx, ly,
										djdy.getBDCDYID());
								if (unit != null) {
									tr.setTag3(tr.getTag3() + unit.getZL());
								}
							}
						}
						
						/*查询打印的次数heks*/
						BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
						String project_id = xmxx.getPROJECT_ID();
						if(project_id != null){
							String fromSql = " FROM BDCK.BDCS_PRINTRECORD WHERE PRINTTYPE='SZDJ"+"' AND PROJECT_ID ='" + project_id+"-"+ zs.getId()+"'";
							long count = baseCommonDao.getCountByFullSql(fromSql);
							tr.setFzcs(count);
						}
						trs.add(tr);
					}
				}
			}
		}
		/***排序啦 by 赵梦帆 2017-05-18 08:40:24***/
		trs = ObjectHelper.SortList(trs);
		return trs;
	}

	
	private List<Tree> getUnionZsTreeExCombo(String xmbh,String qllx) {
		List<Tree> trs = new ArrayList<Tree>();
		String sql=ProjectHelper.GetXMBHCondition(xmbh)+" AND QLLX='"+qllx+"'";
		List<Rights> rightss = RightsTools.loadRightsByCondition(DJDYLY.GZ,sql);
		HashMap<String,List<Integer>> m_qllx=new HashMap<String,List<Integer>>();
		if (rightss != null && rightss.size() > 0) {
			for(Rights rights:rightss){
				if(!StringUtils.isEmpty(rights.getISCANCEL())&&rights.getISCANCEL().equals("1"))//已经取消的权利不计入证书里面wuzhu20161209
					continue;
				String qlid = rights.getId();
				List<BDCS_DJDY_GZ> djdys=null;
//				String qllx=rights.getQLLX();
				if(QLLX.DIYQ.Value.equals(qllx)){
					Integer groupid=rights.getGROUPID();
					if(m_qllx.containsKey(qllx)){
						if(m_qllx.get(qllx).contains(groupid)){
							continue;
						}else{
							List<Integer> list_group=m_qllx.get(qllx);
							list_group.add(groupid);
							m_qllx.remove(qllx);
							m_qllx.put(qllx, list_group);
						}
					}else{
						List<Integer> list_group=new ArrayList<Integer>();
						list_group.add(groupid);
						m_qllx.put(qllx, list_group);
					}
					 djdys = baseCommonDao.getDataList(
								BDCS_DJDY_GZ.class, "DJDYID='" + rights.getDJDYID() + "' AND XMBH='"+xmbh+"'");
						//ws7/16  加个判断 否则报java.lang.IndexOutOfBoundsException异常
						if(djdys==null||djdys.isEmpty()){
							continue;
						}
				}else{
					String djdyid = rights.getDJDYID();
					 djdys = baseCommonDao.getDataList(
							BDCS_DJDY_GZ.class, "DJDYID='" + djdyid + "' AND XMBH='"+xmbh+"'");
					//ws7/16  加个判断 否则报java.lang.IndexOutOfBoundsException异常
					if(djdys==null||djdys.isEmpty()){
						continue;
					}
					Integer groupid=djdys.get(0).getGROUPID();
					if(m_qllx.containsKey(qllx)){
						if(m_qllx.get(qllx).contains(groupid)){
							continue;
						}else{
							List<Integer> list_group=m_qllx.get(qllx);
							list_group.add(groupid);
							m_qllx.remove(qllx);
							m_qllx.put(qllx, list_group);
						}
					}else{
						List<Integer> list_group=new ArrayList<Integer>();
						list_group.add(groupid);
						m_qllx.put(qllx, list_group);
					}
				}
				String hql = "xmbh='" + xmbh+ "' and zsid in (select ZSID from BDCS_QDZR_GZ WHERE QLID='"
						+ qlid + "' AND XMBH='" + xmbh + "') order by BDCQZH";
				List<BDCS_ZS_GZ> zslst = baseCommonDao.getDataList(
						BDCS_ZS_GZ.class, hql);
				for (BDCS_ZS_GZ zs : zslst) {
					List<BDCS_QLR_GZ> qlrlst = this.getQLR(xmbh, zs.getId());
					if (qlrlst != null && qlrlst.size() > 0) {
						StringBuilder result = new StringBuilder();
						boolean flag = false;
						for (BDCS_QLR_GZ qlr : qlrlst) {
							if (flag) {
								result.append(",");
							} else {
								flag = true;
							}
							if (qlid == null)
								qlid = qlr.getQLID();
							result.append(qlr.getQLRMC());
						}
						if (zs.getZSBH() != null
								&& !StringHelper.isEmpty(zs.getZSBH())) {
							result.append("(证书编号:" + zs.getZSBH() + ")");
						}

						Tree tr = new Tree();
						tr.setZsbh(zs.getZSBH());
						tr.setText(result.toString());
						tr.setId(zs.getId());
						tr.setTag3(zs.getBDCQZH());
						if (!StringHelper.isEmpty(zs.getCFDAGH())){
							String bdcqzhxh=StringHelper.getBDCQZHXH(zs.getBDCQZH());
							tr.setQzhxh(bdcqzhxh);
						}
						
						if (rights != null) {
							tr.setTag1(rights.getDJLX());
							tr.setTag2(rights.getQLLX());
							if (djdys != null) {
								BDCS_DJDY_GZ djdy = djdys.get(0);
								BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
								DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
								RealUnit unit = UnitTools.loadUnit(lx, ly,
										djdy.getBDCDYID());
								if (unit != null) {
									tr.setTag3(tr.getTag3() + unit.getZL());
								}
							}
						}
						
						/*查询打印的次数heks*/
						BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
						String project_id = xmxx.getPROJECT_ID();
						if(project_id != null){
							String fromSql = " FROM BDCK.BDCS_PRINTRECORD WHERE PRINTTYPE='SZDJ"+"' AND PROJECT_ID ='" + project_id+"-"+ zs.getId()+"'";
							long count = baseCommonDao.getCountByFullSql(fromSql);
							tr.setFzcs(count);
						}
						trs.add(tr);
					}
				}
			}
		}
		/***排序啦 by 赵梦帆 2017-05-18 08:40:24***/
		trs = ObjectHelper.SortList(trs);
		return trs;
	}
	@Override
	public BDCS_ZS_GZ getZS(String zsid) {
		return baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
	}

	@Override
	public void updateZS(BDCS_ZS_GZ zs) {
		baseCommonDao.update(zs);
		baseCommonDao.flush();
		//更新中间库的证书号
		//updatezshToZJK(zs.getBDCQZH(),zs.getZSBH());
	}

	/**
	 * 保存编号后，维护入库权证管理表--缮证
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年10月30日下午17:11:25
	 * @param qzlx
	 * @param zsbh
	 * @return
	 */
	@Override
	public boolean AddSZXXToRKQZB(String qzlx, String zsbh, String qzzl, String zsid, String xmbh, String bdcdylx) {
		boolean flag = false;
		if (!StringHelper.isEmpty(zsbh) && !StringHelper.isEmpty(qzlx)) {
			//------------------证书编号有字母情况下----------------------------
				int start = 0;
				int end = 1;
				if(zsbh.substring(start,end).matches("[a-zA-Z]")){
					zsbh = zsbh.replaceAll("[a-zA-Z]", "").trim();
				}
			//----------------------------------------------------------------
			
			List<BDCS_QDZR_GZ> listqdzr = baseCommonDao.getDataList(BDCS_QDZR_GZ.class,
					"ZSID='" + zsid + "' AND XMBH='" + xmbh  +"'");
			String qLRMC = null, qLRZJH = null, bDCQZH = null, zl = null;;
			if (listqdzr != null && listqdzr.size() > 0) {
				String qlrid = listqdzr.get(0).getQLRID();
				BDCS_QLR_GZ bdcs_qlr_gz = baseCommonDao.get(BDCS_QLR_GZ.class, qlrid);
				bDCQZH = bdcs_qlr_gz.getBDCQZH();
				//qlrmc&&qlrzjh	
				List<BDCS_QLR_GZ> listqlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class,
						"BDCQZH='" + bDCQZH + "' AND XMBH='" + xmbh  +"'");
				List<String> list_qLRMC = new ArrayList<String>();
				List<String> list_qLRZJH = new ArrayList<String>();
				for (int i = 0; i < listqlrs.size(); i++) {
					if (bdcs_qlr_gz.getQLRMC() != null
							&& !StringHelper.isEmpty(bdcs_qlr_gz.getQLRMC())) {
						if (!list_qLRMC.contains(bdcs_qlr_gz.getQLRMC())) {
							list_qLRMC.add(bdcs_qlr_gz.getQLRMC());
							qLRMC = StringHelper.isEmpty(qLRMC) ? bdcs_qlr_gz.getQLRMC() : qLRMC + "<br/>"+ bdcs_qlr_gz.getQLRMC();
						}
					}
					if (bdcs_qlr_gz.getZJH() != null
							&& !StringHelper.isEmpty(bdcs_qlr_gz.getZJH())) {
						if (!list_qLRZJH.contains(bdcs_qlr_gz.getZJH())) {
							list_qLRZJH.add(bdcs_qlr_gz.getZJH());
							qLRZJH = StringHelper.isEmpty(qLRZJH) ? bdcs_qlr_gz.getZJH() : qLRZJH + "<br/>"+ bdcs_qlr_gz.getZJH();
						}
					}
				}				
				//zl---通过bdcdyh(ql)和xmbh来取unit
				List<BDCS_QL_GZ> listqls = baseCommonDao.getDataList(BDCS_QL_GZ.class,
						"BDCQZH='" + bDCQZH + "' AND XMBH='" + xmbh  +"'");
				BDCDYLX bDCDYLX = null;
				if (bdcdylx == null && StringHelper.isEmpty(bdcdylx)) {
					bDCDYLX =ProjectHelper.GetBDCDYLX(xmbh);
				}else {
					bDCDYLX = BDCDYLX.initFrom(bdcdylx);
				}
				for (int i = 0; i < listqls.size(); i++) {
					BDCS_QL_GZ ql = listqls.get(i);
					String bdcdyh = ql.getBDCDYH();
					//获取bdcdyid
					List<BDCS_DJDY_GZ> listsjdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,
							"BDCDYH='" + bdcdyh + "' AND XMBH='" + xmbh  +"'");	
					if (listsjdys != null && listsjdys.size() > 0) {
						String bdcdyid = listsjdys.get(0).getBDCDYID();
						DJDYLY djdyly = DJDYLY.initFrom("01");
						// 加载单元信息
						RealUnit unit = UnitTools.loadUnit(bDCDYLX, djdyly, bdcdyid);
						if (unit != null ){ 
							zl = StringHelper.isEmpty(zl) ? unit.getZL() : zl + "<br/>"+ unit.getZL() ;
						}else {
							djdyly = DJDYLY.initFrom("02");
							unit = UnitTools.loadUnit(bDCDYLX, djdyly, bdcdyid);
							zl = StringHelper.isEmpty(zl) ? unit.getZL() : zl + "<br/>"+ unit.getZL() ;
						}
					}
					
				}			
			}
							
			List<BDCS_RKQZB> list = baseCommonDao.getDataList(BDCS_RKQZB.class,
					"QZLX='" + qzlx + "' AND QZBH='" + zsbh + "'  AND QZZL='" + qzzl +"'");
			BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
			String slbh = ""; 
			if (xmxx != null) {
				slbh = xmxx.getYWLSH();
			}
			String szr = Global.getCurrentUserName();				
			Date dt = new Date();
			if (list != null && list.size() > 0) {
				BDCS_RKQZB rkqzb = list.get(0);				
				rkqzb.setBDCQZH(bDCQZH);
				rkqzb.setQLRMC(qLRMC);
				rkqzb.setQLRZJH(qLRZJH);
				rkqzb.setZL(zl);
				rkqzb.setSFSZ(SF.YES.Value);
				rkqzb.setSZR(szr);
				rkqzb.setSZRY(szr);
				rkqzb.setSZSJ(dt);
				rkqzb.setSLBH(slbh);
				String syqk = StringHelper.isEmpty(rkqzb.getSYQK()) ? "" : rkqzb.getSYQK()+"<br/>";
				rkqzb.setSYQK(syqk + StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"进行缮证；");
				flag = true;
				baseCommonDao.update(rkqzb);
				baseCommonDao.flush();
				flag = true;
			}else if(!StringHelper.isEmpty(zsbh)){
				BDCS_RKQZB rkqzb = new BDCS_RKQZB();
				//新增基本信息
				rkqzb.setId((String) SuperHelper.GeneratePrimaryKey());
				rkqzb.setCJSJ(dt);
				rkqzb.setQZBH(StringHelper.getLong(zsbh));	
				rkqzb.setQZZL(qzzl);	
				rkqzb.setQZLX(qzlx);
				rkqzb.setBDCQZH(bDCQZH);
				rkqzb.setQLRMC(qLRMC);
				rkqzb.setQLRZJH(qLRZJH);
				rkqzb.setZL(zl);
				rkqzb.setSFSZ(SF.YES.Value);
				rkqzb.setSFFZ(SF.NO.Value);
				rkqzb.setSFZF(SF.NO.Value);
				rkqzb.setSZR(szr);
				rkqzb.setSZRY(szr);
				rkqzb.setSZSJ(dt);
				rkqzb.setSLBH(slbh);
				rkqzb.setSYQK(StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"创建改权证编号并进行缮证；");
				baseCommonDao.save(rkqzb);
				baseCommonDao.flush();
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 配置流程时，不知道登簿在前还是登簿在后
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月5日上午11:00:25
	 * @param zsid
	 * @param zsbh
	 * @return
	 */
	@Override
	public boolean updateZs(String zsid, String zsbh) {
		boolean flag = true;
		Date dt = new Date();
		if (!StringHelper.isEmpty(zsid)) {
			BDCS_ZS_GZ bdcs_zs_gz = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
			if (bdcs_zs_gz != null) {
				bdcs_zs_gz.setZSBH(zsbh);
				bdcs_zs_gz.setSZSJ(dt);
				baseCommonDao.update(bdcs_zs_gz);
				// 再加上不动产权证号一样的要设置成一样的编号
				if (!StringHelper.isEmpty(bdcs_zs_gz.getBDCQZH())) {
					List<BDCS_ZS_GZ> listzss = baseCommonDao.getDataList(
							BDCS_ZS_GZ.class,
							"BDCQZH='" + bdcs_zs_gz.getBDCQZH() + "' AND id<>'"
									+ zsid + "'");
					if (listzss != null && listzss.size() > 0) {
						for (BDCS_ZS_GZ zs : listzss) {
							zs.setZSBH(zsbh);
							zs.setSZSJ(dt);
							baseCommonDao.update(zs);
						}
					}
				}

			}
			BDCS_ZS_XZ bdcs_zs_xz = baseCommonDao.get(BDCS_ZS_XZ.class, zsid);
			if (bdcs_zs_xz != null) {
				bdcs_zs_xz.setZSBH(zsbh);
				bdcs_zs_xz.setSZSJ(dt);
				baseCommonDao.update(bdcs_zs_xz);

				// 再加上不动产权证号一样的要设置成一样的编号
				if (!StringHelper.isEmpty(bdcs_zs_xz.getBDCQZH())) {
					List<BDCS_ZS_XZ> listzss = baseCommonDao.getDataList(
							BDCS_ZS_XZ.class,
							"BDCQZH='" + bdcs_zs_xz.getBDCQZH() + "' AND id<>'"
									+ zsid + "'");
					if (listzss != null && listzss.size() > 0) {
						for (BDCS_ZS_XZ zs : listzss) {
							zs.setZSBH(zsbh);
							zs.setSZSJ(dt);
							baseCommonDao.update(zs);
						}
					}
				}
			}
			BDCS_ZS_LS bdcs_zs_ls = baseCommonDao.get(BDCS_ZS_LS.class, zsid);
			if (bdcs_zs_ls != null) {
				bdcs_zs_ls.setZSBH(zsbh);
				bdcs_zs_ls.setSZSJ(dt);
				baseCommonDao.update(bdcs_zs_ls);

				// 再加上不动产权证号一样的要设置成一样的编号
				if (!StringHelper.isEmpty(bdcs_zs_xz)&&!StringHelper.isEmpty(bdcs_zs_xz.getBDCQZH())) {
					List<BDCS_ZS_LS> listzss = baseCommonDao.getDataList(
							BDCS_ZS_LS.class,
							"BDCQZH='" + bdcs_zs_ls.getBDCQZH() + "' AND id<>'"
									+ zsid + "'");
					if (listzss != null && listzss.size() > 0) {
						for (BDCS_ZS_LS zs : listzss) {
							zs.setZSBH(zsbh);
							zs.setSZSJ(dt);
							baseCommonDao.update(zs);
						}
					}
				}
			}
			if (bdcs_zs_gz != null || bdcs_zs_xz != null || bdcs_zs_ls != null) {
				baseCommonDao.flush();
			}
		} else {
			flag = false;
		}

		return flag;
	}

	/************************************ 获取初始登记新建商品房首次登记的证书信息，其中不需要封面信息，不需要二维码信息，附记需要添加预购商品房信息 ************************************/

	/*
	 * 获取初始登记新建商品房首次登记的证书信息，其中不需要封面信息，不需要二维码信息，附记需要添加预购商品房信息
	 */
	@SuppressWarnings("rawtypes")
	public ZS getBuildingZSForm(String xmbh, String zsid) {

		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		String djlx = "";
		String qllx = "";
		if (!StringHelper.isEmpty(xmxx)) {
			if (xmxx.getSFHBZS() != null
					&& xmxx.getSFHBZS().equals(SF.YES.Value))
				return zsservice2.getZSForm(xmbh, zsid);
			djlx = xmxx.getDJLX();
			qllx = xmxx.getQLLX();
		}
		ZS zsform = new ZS();
		BDCS_ZS_GZ zs = getZS(zsid);
		if (null != zs) {
			String bdcqzh = zs.getBDCQZH();
			zsform.setBdcqzh(bdcqzh);
			String qhjc = "";
			String qhmc = "";
			String nd = "";
			String cqzh = "";
			if (!StringUtils.isEmpty(bdcqzh)) {
				List<String> list = StringHelper.MatchBDCQZH(bdcqzh);
				if (list.size() == 4)// 受理页面想查看证书信息时，出错
				{
					qhjc = list.get(0);
					nd = list.get(1);
					qhmc = list.get(2);
					cqzh = list.get(3);
				}
			}
			zsform.setNd(nd);
			zsform.setQhjc(qhjc);
			zsform.setQhmc(qhmc);
			zsform.setCqzh(cqzh);
		}
		// 首次登记的新建商品房买卖前提条件：登记类型=100 ，权利类型=4
		if (DJLX.CSDJ.Value.equals(djlx)
				&& QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)) {
			// 2.获取登记单元
			BDCS_DJDY_GZ djdy_gz = this.getDjdyGzByZs(xmbh, zsid);
			if (!StringHelper.isEmpty(djdy_gz)) {
				String bdcdylx = djdy_gz.getBDCDYLX();
				String djdyly = djdy_gz.getLY();
				// 进一步判断是否登记单元来源是否从调查库中来
				if (BDCDYLX.H.Value.equals(bdcdylx)
						&& DJDYLY.GZ.Value.equals(djdyly)) {
					// 4.组装单元信息
					zsform = this.getUnitInfo(djdy_gz, zsform, xmbh);
					// 3.获取权利相关的（权利、权利人）
					zsform = this.getQlInfo(djdy_gz, zsform, xmbh, zsid);
					String bdcdyid = djdy_gz.getBDCDYID();
					String ygdjxx = "";// 预告登记内容
					String ygdyxx = "";// 预抵登记内容
			        StringBuffer fulSql=new StringBuffer();
			        fulSql.append("SELECT QL.QLLX,QL.QLID,QL.BDCQZH FROM BDCK.BDCS_QL_XZ QL");
			        fulSql.append(" LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID ");
			        fulSql.append(" LEFT JOIN BDCK.YC_SC_H_XZ GX ON DJDY.BDCDYID=GX.YCBDCDYID ");
			        fulSql.append(" WHERE (QL.QLLX = '4' OR QL.QLLX = '23') ");
			        fulSql.append(" AND GX.SCBDCDYID ='").append(bdcdyid).append("'");
					List<Map> lstql = baseCommonDao
							.getDataListByFullSql(fulSql.toString());
					for (Map ql : lstql) {
						if (!StringHelper.isEmpty(ql.get("QLLX"))) {
							if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql
									.get("QLLX"))) {
								if (!StringHelper.isEmpty(ql.get("QLID"))) {
									List<RightsHolder> lstrh = RightsHolderTools
											.loadRightsHolders(DJDYLY.XZ, ql
													.get("QLID").toString());
									ygdjxx = ygdjxx + "权利人：";
									String qlrxx = "";
									boolean flag = true;
									for (RightsHolder rh : lstrh) {
										if (flag) {
											if (!StringHelper.isEmpty(rh
													.getQLRMC())) {
												qlrxx = rh.getQLRMC();
											} else {
												qlrxx = "-----";
											}
											flag = false;
										} else {
											if (!StringHelper.isEmpty(rh
													.getQLRMC())) {
												qlrxx = qlrxx + ","
														+ rh.getQLRMC();
											} else {
												qlrxx = qlrxx + ",-----";
											}
										}
									}
									ygdjxx = ygdjxx + qlrxx + "<br/>";
								}
							} else {
								if (!StringHelper.isEmpty(ql.get("QLID"))) {
									List<RightsHolder> lstrh = RightsHolderTools
											.loadRightsHolders(DJDYLY.XZ, ql
													.get("QLID").toString());
									ygdyxx = ygdyxx + "抵押权人：";
									String qlrxx = "";
									boolean flag = true;
									for (RightsHolder rh : lstrh) {
										if (flag) {
											if (!StringHelper.isEmpty(rh
													.getQLRMC())) {
												qlrxx = rh.getQLRMC();
											} else {
												qlrxx = "-----";
											}
											flag = false;
										} else {
											if (!StringHelper.isEmpty(rh
													.getQLRMC())) {
												qlrxx = qlrxx + ","
														+ rh.getQLRMC();
											} else {
												qlrxx = qlrxx + "," + "-----";
											}
										}
									}
									if (!StringHelper.isEmpty(ql.get("BDCQZH"))) {
										ygdyxx = ygdyxx + qlrxx
												+ "<br/> 不动产登记证明号："
												+ ql.get("BDCQZH") + "<br/>";
									} else {
										ygdyxx = ygdyxx + qlrxx
												+ " <br/>不动产登记证明号：----<br/>";
									}
								}
							}
						}

					}
					if (!StringHelper.isEmpty(ygdyxx)) {
						ygdyxx = "预购商品房抵押权预告登记：<br/>" + ygdyxx;
					} else {
						ygdyxx = "预购商品房抵押权预告登记：无";
					}
					if (!StringHelper.isEmpty(ygdjxx)) {
						ygdjxx = "<br/><br/>以上信息记录首次登记内容<br/><br/>预告登记：<br/>"
								+ ygdjxx + "<br/>";
					} else {
						ygdjxx = "<br/><br/>以上信息记录首次登记内容<br/><br/>预告登记：无<br/>";
					}
					String project_Id = "";
					String ywlsh = "";// 业务流水号
					String fj = "";
					fj = zsform.getFj();
					if (!StringHelper.isEmpty(xmxx)) {
						project_Id = xmxx.getPROJECT_ID();
						ywlsh = xmxx.getYWLSH();
						if (!StringHelper.isEmpty(ywlsh)) {
							if (!StringHelper.isEmpty(fj)) {
								fj = "业务编号：" + ywlsh + "<br/>" + fj;
							} else {
								fj = "业务编号：" + ywlsh;
							}
						} else {
							String[] projects = project_Id.split("-");
							if (!StringHelper.isEmpty(projects)) {
								if (projects.length == 4) {
									project_Id = projects[2] + "-"
											+ projects[3];
								}
							}
							if (!StringHelper.isEmpty(project_Id)) {

								if (!StringHelper.isEmpty(fj)) {
									fj = "业务编号：" + project_Id + "<br/>" + fj;
								} else {
									fj = "业务编号：" + project_Id;
								}
							}
						}
					}
					fj = fj + ygdjxx + ygdyxx;
					zsform.setFj(fj);
				}
			}
		}

		return zsform;
	}

	/**
	 * 组装ZS信息中单元部分信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月17日上午12:49:08
	 * @param djdy
	 * @param zsform
	 * @param xmbh
	 * @return
	 */
	private ZS getUnitInfo(BDCS_DJDY_GZ djdy, ZS zsform, String xmbh) {
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		if (null != djdy) {
			DecimalFormat df = new DecimalFormat("#########.###");
			df.setRoundingMode(RoundingMode.HALF_UP);
			String bdcdylx = djdy.getBDCDYLX();
			BDCDYLX bDCDYLX = BDCDYLX.initFrom(bdcdylx);
			if (!StringUtils.isEmpty(djdy.getLY())) {
				DJDYLY djdyly = DJDYLY.initFrom(djdy.getLY());
				// 加载单元信息
				RealUnit unit = UnitTools.loadUnit(bDCDYLX, djdyly,
						djdy.getBDCDYID());
				if (null != unit)
					zsform.setBdcdyh(formatBDCDYH(unit.getBDCDYH()));
				zsform.setZl(StringHelper.isEmpty(unit.getZL()) ? "----" : unit.getZL());

				if (ConstValue.BDCDYLX.SHYQZD.Value.equals(bdcdylx)) {
					// 使用权宗地：面积，用途，权利性质，用途，起止时间
					UseLand land = (UseLand) unit;
					zsform.setMj(StringHelper.isEmpty(land.getZDMJ())
							|| land.getZDMJ() == 0 ? "----" : StringHelper
							.FormatByDatatype(land.getZDMJ()));
					getLandUsage(land, zsform);
					String qlxz = ConstHelper.getNameByValue("QLXZ",
							land.getQLXZ());
					zsform.setQlxz(StringHelper.isEmpty(qlxz) ? "----" : qlxz);
				} else if (ConstValue.BDCDYLX.H.Value.equals(bdcdylx)) { // 户
					House house = (House) unit;
					StringBuffer strBf = new StringBuffer();
					String fttdmj = "----";
					double ftmj = 0;// 宅基地使用权_房屋所有权中的分摊土地面积
					double dytdmj = 0;// 宅基地使用权_房屋所有权中的独用土地面积
					double gymj = 0;
					if (!StringHelper.isEmpty(house.getFTTDMJ())) {
						ftmj = house.getFTTDMJ();
					}
					if (!StringHelper.isEmpty(house.getDYTDMJ())) {
						dytdmj = house.getDYTDMJ();
					}
					gymj = ftmj + dytdmj;
					if (gymj != 0) {
						fttdmj =df.format(gymj) + pfm;
					}
					String PrintMj = "";
					PrintMj = "土地使用权面积：" + fttdmj;
					if (ftmj > 0 && dytdmj > 0) {
						PrintMj = PrintMj
								+ "，其中分摊面积："
								+ df.format(ftmj)
								+ pfm+"，独用面积："
								+ df.format(dytdmj) + pfm;
					}
					if ("1".equals(ConfigHelper.getNameByValue("ZSPARAM_showGYTDMJ"))) {
						if (house.getGYTDMJ()>0) {
							PrintMj +="，共有土地面积："+house.getGYTDMJ()+ pfm;
						}else {
							PrintMj +="，共有土地面积：--- "+ pfm;
						}
					}
					
					strBf.append(PrintMj + "<br/>");
					String sctnjzmj = "----";
					//专有建筑面积和分摊建筑面积为0时，不显示
					if (!StringHelper.isEmpty(house.getSCTNJZMJ())) {
						if (house.getSCTNJZMJ() != 0) {
							sctnjzmj = df.format(house.getSCTNJZMJ()) + pfm;
							strBf.append("专有建筑面积：" + sctnjzmj + "<br/>");
						}
					}
					String scftjzmj = "----";
					if (!StringHelper.isEmpty(house.getSCFTJZMJ())) {
						if (house.getSCFTJZMJ() != 0) {
							scftjzmj = df.format(house.getSCFTJZMJ()) + pfm;
							strBf.append("分摊建筑面积：" + scftjzmj + "<br/>");
						}
					}
//					strBf.append("专有建筑面积：" + sctnjzmj + "，分摊建筑面积：" + scftjzmj
//							+ "<br/>");

					String fwjg = ConstHelper.getNameByValue("FWJG",
							house.getFWJG());
					if (StringHelper.isEmpty(fwjg)) {
						fwjg = "----";
					}
					strBf.append("房屋结构：" + fwjg + "<br/>");
					String _szc = house.getSZC() == null ? "" : house.getSZC()
							.toString();
					if (StringHelper.isEmpty(_szc)) {
						_szc = "----";
					}
					String zcs = "----";
					if (!StringHelper.isEmpty(house.getZCS())
							&& house.getZCS() != 0) {
						zcs = house.getZCS().toString();
					}
					strBf.append("房屋总层数：" + zcs + "，房屋所在层：" + _szc + "<br/>");
					String jgsj = StringHelper
							.FormatByDatetime(house.getJGSJ());

					if (!StringHelper.isEmpty(jgsj)) {
						strBf.append("房屋竣工时间：" + jgsj);
					}

					zsform.setQlqtzk(strBf.toString());

					// 户相对应宗地的信息
					if (!StringUtils.isEmpty(house.getZDBDCDYID())) {
						BDCS_SHYQZD_XZ shyqzd = (BDCS_SHYQZD_XZ) UnitTools
								.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
										house.getZDBDCDYID());
						// baseCommonDao.get(BDCS_SHYQZD_XZ.class,house.getZDBDCDYID());
						if (null != shyqzd) {
							String shyqyt = ConstHelper.getNameByValue("TDYT",
									house.getFWTDYT());// 从房屋用途里面取
							if (StringHelper.isEmpty(shyqyt)) {
								shyqyt = "----";
							}
							String hyt = ConstHelper.getNameByValue("FWYT",
									house.getGHYT());
							if (StringHelper.isEmpty(hyt)) {
								hyt = "----";
							}
							zsform.setYt(shyqyt + "/" + hyt);
							String shyqxz = ConstHelper.getNameByValue("QLXZ",
									house.getQLXZ());
							if (StringHelper.isEmpty(shyqxz)) {
								shyqxz = "----";
							}
							String hxz = ConstHelper.getNameByValue("FWXZ",
									house.getFWXZ());
							if (StringHelper.isEmpty(hxz)) {
								hxz = "----";
							}
							zsform.setQlxz(shyqxz + "/" + hxz);
							String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
							String shyqmj = "共有宗地面积 ----";
							if (!StringHelper.isEmpty(shyqzd.getZDMJ())&& shyqzd.getZDMJ() != 0) {
								if(XZQHDM.indexOf("45")==0){//广西需求，分摊或独有土地面积>0时，显示“宗地面积”4个字
									if(house.getFTTDMJ()>0||house.getDYTDMJ()>0){
										shyqmj = "宗地面积 "	+ df.format(shyqzd.getZDMJ()) + pfm;
									}else{
										shyqmj = "共有宗地面积 "	+ df.format(shyqzd.getZDMJ()) + pfm;
									}
								}else{
									shyqmj = "共有宗地面积 "	+ df.format(shyqzd.getZDMJ()) + pfm;
								}
							}
							String hmj = "房屋建筑面积 ----";
							if (!StringHelper.isEmpty(house.getSCJZMJ())
									&& house.getSCJZMJ() != 0) {
								hmj = "房屋建筑面积 "
										+ df.format(house.getSCJZMJ())
										+ pfm;
							}
							zsform.setMj(shyqmj + "/" + hmj);
						}
					}
				} else if (ConstValue.BDCDYLX.ZRZ.Value.equals(bdcdylx)) { // 自然幢
					Building building = (Building) unit;
					StringBuffer strBf = new StringBuffer();
					String zzdmj = "----";
					if (!StringHelper.isEmpty(building.getZZDMJ())
							&& building.getZZDMJ() != 0) {
						zzdmj = df.format(building.getZDDM()) + pfm;
					}

					strBf.append("幢占地面积：" + zzdmj + "<br/>");
					String dytdmj = "----";
					if (!StringHelper.isEmpty(building.getDYTDMJ())
							&& building.getDYTDMJ() != 0) {
						if (building.getDYTDMJ() != 0)
							dytdmj = df.format(building.getDYTDMJ()) + pfm;
					}
					String fttdmj = "----";
					if (!StringHelper.isEmpty(building.getFTTDMJ())
							&& building.getFTTDMJ() != 0) {
						if (building.getFTTDMJ() != 0)
							fttdmj = df.format(building.getFTTDMJ()) + pfm;
					}
					strBf.append("独用土地面积：" + dytdmj + "，分摊土地面积：" + fttdmj
							+ "<br/>");
					String fwjg = "----";
					if (!StringHelper.isEmpty(building.getFWJG())) {
						fwjg = ConstHelper.getNameByValue("FWJG",
								building.getFWJG());
					}
					strBf.append("房屋结构：" + fwjg + "<br/>");
					String zts = "----";
					String zcs = "----";
					if (!StringHelper.isEmpty(building.getZTS())
							&& building.getZTS() != 0) {
						zts = building.getZTS().toString();
					}
					if (!StringHelper.isEmpty(building.getZCS())
							&& building.getZCS() != 0) {
						zcs = building.getZCS().toString();
					}
					strBf.append("幢总套数：" + zts + "，幢总层数：" + zcs + "<br/>");
					String jgsj = StringHelper.FormatByDatetime(building
							.getJGRQ());
					if (StringHelper.isEmpty(jgsj)) {
						jgsj = "----";
					}
					strBf.append("房屋竣工时间：" + jgsj);
					zsform.setQlqtzk(strBf.toString());

					// 自然幢对应的宗地信息
					if (!StringUtils.isEmpty(building.getZDBDCDYID())) {
						BDCS_SHYQZD_XZ shyqzd = (BDCS_SHYQZD_XZ) UnitTools
								.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
										building.getZDBDCDYID());
						// baseCommonDao.get(BDCS_SHYQZD_XZ.class,
						// building.getZDBDCDYID());
						if (null != shyqzd) {
							String tdyt = getLandUsage(shyqzd);// ConstHelper.getNameByValue("TDYT",
																// shyqzd.getYT());
																// // 土地用途
							if (StringHelper.isEmpty(tdyt)) {
								tdyt = "----";
							}
							String zrzyt = ConstHelper.getNameByValue("FWYT",
									building.getGHYT());
							if (StringHelper.isEmpty(zrzyt)) {
								zrzyt = "----";
							}
							zsform.setYt(tdyt + "/" + zrzyt);
							String shyqxz = ConstHelper.getNameByValue("QLXZ",
									shyqzd.getQLXZ());
							if (StringHelper.isEmpty(shyqxz)) {
								shyqxz = "----";
							}
							zsform.setQlxz(shyqxz);
							String shyqmj = StringHelper
									.FormatByDatatype(shyqzd.getZDMJ());
							if (StringHelper.isEmpty(shyqmj)) {
								shyqmj = "----";
							} else {
								shyqmj = df.format(shyqmj) + pfm;
							}
							String zrzmj = StringHelper
									.FormatByDatatype(building.getSCJZMJ());
							if (StringHelper.isEmpty(zrzmj)) {
								zrzmj = "----";
							} else {
								zrzmj = df.format(zrzmj) + pfm;
							}
							zsform.setMj(shyqmj + "/" + zrzmj);
						}
					}
				} else if (ConstValue.BDCDYLX.SYQZD.Value.equals(bdcdylx)) { // 所有权宗地
					OwnerLand oLand = (OwnerLand) unit;
					String mj = StringHelper.FormatByDatatype(oLand.getZDMJ());
					if (StringHelper.isEmpty(mj)) {
						mj = "----";
					} else {
						mj = df.format(mj) + pfm;
					}
					zsform.setMj(mj);
					String Yt = ConstHelper.getNameByValue("TDYT",
							oLand.getYT());
					zsform.setYt(Yt);
					String qlxz = ConstHelper.getNameByValue("QLLX",
							oLand.getQLLX());
					zsform.setQlxz(qlxz);
				} else if (ConstValue.BDCDYLX.HY.Value.equals(bdcdylx)) { // 海域
					Sea sea = (Sea) unit;
					// 取项目信息
					BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
					String zhmj = "----";
					if (!StringHelper.isEmpty(sea.getZHMJ())
							&& sea.getZHMJ() != 0) {
						zhmj = df.format(sea
								.getZHMJ()) + pfm;
					}
					zsform.setMj(zhmj);
					String yhlxa = ConstHelper.getNameByValue("HYSYLXA",
							sea.getYHLXA());
					if (StringHelper.isEmpty(yhlxa)) {
						yhlxa = "----";
					}
					String yhlxb = ConstHelper.getNameByValue("HYSYLXB",
							sea.getYHLXB());
					if (StringHelper.isEmpty(yhlxb)) {
						yhlxb = "----";
					}
					String Yt = yhlxa + "/" + yhlxb;
					zsform.setYt(Yt);
					// TODO 权利性质没找到
					if (xmxx.getSLLX1().equals("01")) {
						zsform.setQlxz("出让");
					} else {
						zsform.setQlxz("审批");
					}
					StringBuffer strBf = new StringBuffer();
					strBf.append("项目名称：" + sea.getXMMC() + "<br/>");
					strBf.append("项目性质："
							+ ConstHelper.getNameByValue("XMXZ", sea.getXMXX())
							+ "<br/>");
					// 用海状况
					List<BDCS_YHZK_GZ> lstyhzk = baseCommonDao
							.getDataList(BDCS_YHZK_GZ.class,
									"BDCDYID='" + sea.getId() + "'");
					String yhfs = "";
					String mj = "";
					for (BDCS_YHZK_GZ yhzk : lstyhzk) {
						yhfs += yhzk.getYHFSName() + ",";
						mj += yhzk.getYHMJ().toString() + ",";
					}
					if (!StringUtils.isEmpty(yhfs)) {
						yhfs = yhfs.substring(0, yhfs.length() - 1);
					}
					if (!StringUtils.isEmpty(mj)) {
						mj = mj.substring(0, mj.length() - 1);
					}
					strBf.append("用海方式：" + yhfs + "<br/>");
					strBf.append("面积：" + mj + "<br/>");
					zsform.setQlqtzk(strBf.toString());
				} else if (ConstValue.BDCDYLX.LD.Value.equals(bdcdylx)) { // 林地
					Forest forest = (Forest) unit;
					String lzName = ConstHelper.getNameByValue("LZ",
							forest.getLZ());
					if (StringHelper.isEmpty(lzName)) {
						lzName = "----";
					}
					zsform.setQlxz(lzName);
					zsform.setYt("林地");
					zsform.setMj(forest.getSYQMJ() != null ? forest.getSYQMJ()
							.toString() : "");
					StringBuffer strBf = new StringBuffer();
					String zysz = "----";
					if (!StringHelper.isEmpty(forest.getZYSZ())) {
						zysz = forest.getZYSZ();
					}
					strBf.append("树种：" + zysz + "<br/>");
					String zlnd = "----";
					if (!StringHelper.isEmpty(forest.getZLND())
							&& forest.getZLND() != 0) {
						zlnd = forest.getZLND().toString();
					}
					strBf.append("造林年度：" + zlnd + "<br/>");
					String xdm = "----";
					if (!StringHelper.isEmpty(forest.getXDM())) {
						xdm = forest.getXDM();
					}
					strBf.append("小地名：" + xdm + "<br/>");
					zsform.setQlqtzk(strBf.toString());
				}

			}
		}
		return zsform;
	}

	/**
	 * 格式化不动产单元号，中间加空格
	 * 
	 * @Title: formatBDCDYH
	 * @author:liushufeng
	 * @date：2015年11月13日 下午11:33:11
	 * @param bdcdyh
	 * @return
	 */
	private String formatBDCDYH(String bdcdyh) {
		String _bdcdyh = "";
		// 字符串不为null并且不为""
		if (!StringHelper.isEmpty(bdcdyh)) {
			int length = bdcdyh.length();
			if (length > 0 && length <= 6) { // 长度是0-6
				_bdcdyh = bdcdyh;
			} else if (length > 6 && length <= 12) { // 长度是7-12
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6);
				_bdcdyh = s1 + "  " + s2;
			} else if (length > 12 && length <= 19) { // 长度是13-19
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6, 12);
				String s3 = bdcdyh.substring(12);
				_bdcdyh = s1 + " " + s2 + " " + s3;
			} else { // 长度大于19
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6, 12);
				String s3 = bdcdyh.substring(12, 19);
				String s4 = bdcdyh.substring(19, length);
				_bdcdyh = s1 + " " + s2 + " " + s3 + " " + s4;
			}
		}
		return _bdcdyh;
	}

	/**
	 * 根据证书id、项目编号和登记单元组装ZS信息中权利部分信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月16日下午10:27:18
	 * @param djdy
	 *            登记单元
	 * @param zsform
	 *            ZS
	 * @param xmbh
	 *            项目编号
	 * @param zsid
	 *            证书id
	 * @return
	 */
	private ZS getQlInfo(BDCS_DJDY_GZ djdy, ZS zsform, String xmbh, String zsid) {
		if (null != djdy) {
			String bdcdylx = djdy.getBDCDYLX();

			String fj = "";
			// 1.获取权利
			Rights right = RightsTools.loadRightsByZSID(DJDYLY.GZ, zsid, xmbh);
			if (null != right) {
				String qllx = ConstHelper.getNameByValue("QLLX",
						right.getQLLX());
				zsform.setQllx(qllx);
				String year = "", month = "", day = "";
				Date date = right.getDJSJ();
				if (null != date) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					year = cal.get(Calendar.YEAR) + "";
					month = (cal.get(Calendar.MONTH) + 1) + "";
					day = cal.get(Calendar.DATE) + "";
				}
				zsform.setFm_year(year);
				zsform.setFm_month(month);
				zsform.setFm_day(day);
				String qssj = StringHelper.FormatByDatetime(right.getQLQSSJ());
				String jssj = StringHelper.FormatByDatetime(right.getQLJSSJ());

				boolean bViewQZRQ = true;
				String codes_zdhb =ConfigHelper.getNameByValue("CODES_ZDHB"); 
				if (!StringHelper.isEmpty(codes_zdhb)) {
					BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
					if (xmxx != null) {
						String workflowcode = ProjectHelper
								.getWorkflowCodeByProjectID(xmxx
										.getPROJECT_ID());
						if (codes_zdhb.contains(workflowcode)) {
							bViewQZRQ = false;
						}
					}
				}
				if (ConstValue.BDCDYLX.SYQZD.Value.equals(bdcdylx)
						|| !bViewQZRQ) { // sunhb-2015-06-28土地所有权可以不填写
					zsform.setSyqx("");
				} else if (ConstValue.BDCDYLX.SHYQZD.Value.equals(bdcdylx))// sunhb-2015-11-04土使用权从多用途表获取起始时间
				{

				} else {
					if (StringHelper.isEmpty(qssj)) {
						qssj = "";
					} else {
						qssj = qssj + "起";
					}
					if (StringHelper.isEmpty(jssj)) {
						jssj = "----";
					}
					zsform.setSyqx(qssj + jssj + "止");
				}
				if (!StringHelper.isEmpty(right.getFJ())) {
					fj = right.getFJ().replaceAll("\r\n|\r|\n|\n\r", "<br>");
					zsform.setFj(fj);
				}
			}
			// 2.获取权利人
			List<BDCS_QLR_GZ> qlrlst = getQLR(xmbh, zsid);
			if (qlrlst != null && qlrlst.size() > 0) {
				StringBuilder result = new StringBuilder();
				boolean flag = false;
				for (BDCS_QLR_GZ qlr : qlrlst) {
					if (flag) {
						result.append(",");
					} else {
						flag = true;
					}
					result.append(qlr.getQLRMC());
				}
				zsform.setQlr(result.toString());
				for (int k = 0; k < qlrlst.size(); k++) {
					if (qlrlst.get(k) != null) {
						if (qlrlst.get(k).getGYFS() != null) {
							zsform.setGyqk(qlrlst.get(k).getGYFSName());
							break;
						}
					}
				}
				List<RightsHolder> holders = RightsHolderTools
						.loadRightsHolders(DJDYLY.GZ, right.getId());
				if (holders != null) {
					String czfs = "";
					if (holders.size() > 1) {
						if (!StringHelper.isEmpty(right.getCZFS())) {
							czfs = "持证方式："
									+ ConstHelper.getNameByValue("CZFS",
											right.getCZFS());
						} else {
							czfs = "持证方式：----";
						}
						String czr = "";
						for (int i = 0; i < holders.size(); i++) {
							if (i == 0) {
								czr = holders.get(0).getQLRMC();// 默认持证人
							}
							BDCS_QLR_GZ bdcs_qlr_gz = (BDCS_QLR_GZ) holders
									.get(i);
							if (SF.YES.Value.equals(bdcs_qlr_gz.getISCZR())) {
								czr = holders.get(i).getQLRMC();
								break;
							}
						}
						if (CZFS.GTCZ.Value.equals(right.getCZFS())) {
							czfs = czfs + "<br/>" + "持证人:" + czr;
						}
						String qlrinfo = "共有权人&nbsp&nbsp&nbsp&nbsp&nbsp不动产权证号&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp共有情况<br/>";
						boolean gyfsflag = false;
						for (RightsHolder holder : holders) {
							if (!StringHelper.isEmpty(holder.getGYFS())) {
								if (ConstValue.GYFS.AFGY.Value.equals(holder
										.getGYFS()))// 按份共有
								{
									gyfsflag = true;
									if (!StringHelper
											.isEmpty(holder.getQLRMC()))// 权利人
									{
										qlrinfo = qlrinfo + holder.getQLRMC()
												+ "&nbsp&nbsp&nbsp";
									} else {
										qlrinfo = "----&nbsp&nbsp&nbsp";
									}
									if (!StringHelper.isEmpty(holder
											.getBDCQZH()))// 不动产权证号
									{
										qlrinfo = qlrinfo + holder.getBDCQZH()
												+ "&nbsp&nbsp&nbsp按份共有 ";
									} else {
										qlrinfo = qlrinfo
												+ "----&nbsp&nbsp&nbsp按份共有 ";
									}
									if (!StringHelper.isEmpty(holder.getQLBL())) {
										if (holder.getQLBL().lastIndexOf("%") > 0) {
											qlrinfo = qlrinfo
													+ holder.getQLBL()
													+ "<br/>";
										} else if (holder.getQLBL().contains(
												"/")) {
											qlrinfo = qlrinfo
													+ holder.getQLBL()
													+ "<br/>";
										} else {
											qlrinfo = qlrinfo
													+ holder.getQLBL()
													+ "%<br/>";
										}
									}
								} else if (!(ConstValue.GYFS.DYSY.Value
										.equals(holder.getGYFS())))// 共同共有或其它共有
								{
									gyfsflag = false;
									if (!StringHelper
											.isEmpty(holder.getQLRMC()))// 权利人
									{
										qlrinfo = qlrinfo + holder.getQLRMC()
												+ "&nbsp&nbsp&nbsp";
									} else {
										qlrinfo = qlrinfo
												+ "----&nbsp&nbsp&nbsp";
									}
									if (!StringHelper.isEmpty(holder
											.getBDCQZH()))// 不动产权证号
									{
										qlrinfo = qlrinfo + holder.getBDCQZH()
												+ "&nbsp&nbsp&nbsp共同所有<br/>";
									} else {
										qlrinfo = qlrinfo
												+ "----&nbsp&nbsp&nbsp共同所有<br/>";
									}
								}
							}
						}
						if (!StringHelper.isEmpty(right)) {
							if (!gyfsflag
									&& ConstValue.CZFS.FBCZ.Value.equals(right
											.getCZFS()) && holders.size() > 1)// 共同共有或其它共有并且是分别持证且权利人大于1
							{
								// qlrinfo = qlrinfo;
							} else if (gyfsflag && holders.size() > 1)// 按份共有
							{
								// qlrinfo = qlrinfo;
							} else {
								qlrinfo = "";
							}
						}
						if (holders.size() <= 1) {
							qlrinfo = "";
						}
						if (!StringHelper.isEmpty(fj)) {
							zsform.setFj(fj + "<br/>" + qlrinfo);
						} else {
							zsform.setFj(qlrinfo);
						}
					}
					if (!StringHelper.isEmpty(zsform.getQlqtzk())) {
						zsform.setQlqtzk(zsform.getQlqtzk() + "<br/>" + czfs);
					} else {
						zsform.setQlqtzk(czfs);
					}
				}
			}
		}
		return zsform;
	}

	/**
	 * 根据项目编号和证书id获取权利人
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月16日下午10:15:15
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	private List<BDCS_QLR_GZ> getQLR(String xmbh, String zsid) {
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append(" qlrid IN (SELECT QLRID FROM BDCS_QDZR_GZ ");
		hqlCondition.append(" WHERE ZSID ='").append(zsid).append("' ");
		hqlCondition.append(" AND XMBH ='").append(xmbh).append("') ");
//		hqlCondition.append(" AND XMBH ='").append(xmbh)
				hqlCondition.append(" ORDER BY SXH");
		List<BDCS_QLR_GZ> list = baseCommonDao.getDataList(BDCS_QLR_GZ.class,
				hqlCondition.toString());
		return list;
	}
	/**
	 * 提高商品房首次登记的
	 * @作者 海豹
	 * @创建时间 2016年1月9日下午4:33:50
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	private List<Map> getQLREx(String xmbh, String zsid) {
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append("SELECT QLRMC,QLRID,QLID FROM BDCK.BDCS_QLR_GZ QLR ");
		hqlCondition.append(" WHERE EXISTS( SELECT QLRID FROM BDCK.BDCS_QDZR_GZ QDZR ");
		hqlCondition.append(" WHERE QLR.QLRID=QDZR.QLRID ");
		hqlCondition.append(" AND ZSID ='").append(zsid).append("' ");
		hqlCondition.append(" AND XMBH ='").append(xmbh).append("') ");
		hqlCondition.append(" ORDER BY SXH");
		List<Map> lst=baseCommonDao.getDataListByFullSql(hqlCondition.toString());  
		return lst;
	}

	private String getLandUsage(UseLand land) {

		// 获取主要用途
		String yt = "";
		if (land.getTDYTS() != null && land.getTDYTS().size() > 0) {
			// 只有一个用途的时候，就取这个用途，有多个用途的时候，取主要用途
			if (land.getTDYTS().size() == 1) {
				yt = land.getTDYTS().get(0).getTDYT();
			} else {
				for (TDYT tdyt : land.getTDYTS()) {
					if (SF.YES.Value.equals(tdyt.getSFZYT())) {
						yt = tdyt.getTDYT();
						break;
					}
				}
			}
		}
		if (!StringHelper.isEmpty(yt))
			yt = ConstHelper.getNameByValue("TDYT", yt);
		return yt;
	}

	/**
	 * 使用权宗地从多用途表中获取用途及时间
	 * 
	 * @作者 海豹
	 * @创建时间 2015年11月4日下午9:46:54
	 * @param land
	 * @param zsform
	 */
	private void getLandUsage(UseLand land, ZS zsform) {
		String myt = "";// 多个用途
		String sj = "";
		String yt = "";// 单个用途
		boolean flag = false;
		if (land != null) {
			if (land.getTDYTS() != null && land.getTDYTS().size() > 0) {
				for (TDYT tdyt : land.getTDYTS()) {
					String qssj = "";
					String jssj = "";
					if (!flag) {
						if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
							myt = tdyt.getTDYTMC();
							yt = tdyt.getTDYTMC();
						}
						qssj = StringHelper.FormatByDatetime(tdyt.getQSRQ());
						jssj = StringHelper.FormatByDatetime(tdyt.getZZRQ());
						if (StringHelper.isEmpty(qssj)) {
							qssj = "";
						} else {
							qssj = qssj + "起";
						}
						if (StringHelper.isEmpty(jssj)) {
							jssj = "----";
						}
						if (land.getTDYTS().size() > 1)// 判断是多个用途时，添加用途及对应的时间
						{
							sj = myt + " " + qssj + jssj + "止";
						} else {
							sj = qssj + jssj + "止";
						}
						flag = true;
					} else {
						if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
							myt = myt + "、" + tdyt.getTDYTMC();
							yt = tdyt.getTDYTMC();
						}
						qssj = StringHelper.FormatByDatetime(tdyt.getQSRQ());
						jssj = StringHelper.FormatByDatetime(tdyt.getZZRQ());
						if (StringHelper.isEmpty(qssj)) {
							qssj = "";
						} else {
							qssj = qssj + "起";
						}
						if (StringHelper.isEmpty(jssj)) {
							jssj = "----";
						}
						sj = sj + "<br/>" + yt + " " + qssj + jssj + "止";
					}
				}
			}
		}
		if (StringHelper.isEmpty(myt)) {
			myt = "----";
		}
		zsform.setYt(myt);
		if (StringHelper.isEmpty(sj)) {
			sj = "----";
		}
		zsform.setSyqx(sj);
	}

	/************************************ 获取初始登记新建商品房首次登记的证书信息，其中不需要封面信息，不需要二维码信息，附记需要添加预购商品房信息 ************************************/

	/**
	 * 根据项目编号和证书id获取BDCS_DJDY_GZ
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月16日下午8:22:38
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	private BDCS_DJDY_GZ getDjdyGzByZs(String xmbh, String zsid) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" XMBH ='").append(xmbh).append("' ");
		hqlBuilder.append(" AND DJDYID IN (select  DJDYID from BDCS_QDZR_GZ ");
		hqlBuilder.append(" WHERE XMBH = '").append(xmbh).append("' ");
		hqlBuilder.append(" AND ZSID = '").append(zsid).append("')");
		List<BDCS_DJDY_GZ> list = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,
				hqlBuilder.toString()); 
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: getDJFZ
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param condition
	 * @return
	 * @see com.supermap.realestate.registration.service.ZSService#getDJFZ(java.lang.String)
	 */
	@Override
	public BDCS_DJFZ getDJFZ(String condition) {
		BDCS_DJFZ djfz = null;
		List<BDCS_DJFZ> fzs = baseCommonDao.getDataList(BDCS_DJFZ.class,
				condition);
		if (fzs != null && fzs.size() > 0) {
			djfz = fzs.get(0);
		}
		return djfz;
	}

	/**
	 * 获取证书二维码信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年11月05日12:08:08
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@Override
	public String GetQRCodeInfo(String xmbh, String zsid) {
		String QRCodeInfo = "";
		if (StringHelper.isEmpty(xmbh) || StringHelper.isEmpty(zsid)) {
			return QRCodeInfo;
		}
		String bdcqzh = "";
		// String bdcqlr="";
		String bdcdyh = "";
		String bdcywh = "";
		String bdcdyid = "";
		String zsbh = "";
		String zsType = "1";// 默认证明
		String sfhbzs = "0";// 默认证明
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		if (xmxx != null) {
			bdcywh = xmxx.getPROJECT_ID();
			if (!StringHelper.isEmpty(xmxx.getSFHBZS())) {
				sfhbzs = xmxx.getSFHBZS();
			}
		}
		BDCS_ZS_GZ zs = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
		if (zs != null) {
			bdcqzh = zs.getBDCQZH();
			zsbh = zs.getZSBH();
		}
		List<BDCS_QDZR_GZ> qdzrs = baseCommonDao.getDataList(
				BDCS_QDZR_GZ.class, "ZSID='" + zsid + "'");
		if (qdzrs != null && qdzrs.size() > 0) {
			for (BDCS_QDZR_GZ qdzr : qdzrs) {
				List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
						BDCS_DJDY_GZ.class, "DJDYID='" + qdzr.getDJDYID()
								+ "' AND XMBH='" + xmbh + "'");
				if (djdys != null && djdys.size() > 0) {
					for (BDCS_DJDY_GZ djdy : djdys) {
						RealUnit unit = UnitTools.loadUnit(
								BDCDYLX.initFrom(djdy.getBDCDYLX()),
								DJDYLY.initFrom(djdy.getLY()),
								djdy.getBDCDYID());
						if (unit != null) {
							if (!StringHelper.isEmpty(unit.getBDCDYH())
									&& !bdcdyh.contains(unit.getBDCDYH())) {
								if (StringHelper.isEmpty(bdcdyh)) {
									bdcdyh = bdcdyh + unit.getBDCDYH();
								} else {
									bdcdyh = bdcdyh + "、" + unit.getBDCDYH();
								}
							}
							if (!bdcdyid.contains(unit.getId())) {
								if (StringHelper.isEmpty(bdcdyid)) {
									bdcdyid = bdcdyid + unit.getId();
								} else {
									bdcdyid = bdcdyid + "、" + unit.getId();
								}
							}
						}
					}
				}
				BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class,
						qdzr.getQLID());
				if (ql != null) {
					if (!DJLX.YGDJ.Value.equals(ql.getDJLX())) {
						if (QLLX.GJTDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.JTTDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.GYJSYDSHYQ.Value.equals(ql.getQLLX())
								|| QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql
										.getQLLX())
								|| QLLX.JTJSYDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(ql
										.getQLLX())
								|| QLLX.ZJDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.ZJDSYQ_FWSYQ.Value.equals(ql.getQLLX())) {
							zsType = "0";// 设定类型为权证
						}
					}
				}
			}
			List<String> lst = null;
			if (!StringHelper.isEmpty(bdcqzh)) {
				if (bdcqzh.contains("不动产权第")) {
					lst = StringHelper.MatchBDCQZH(bdcqzh);

				} else if (bdcqzh.contains("不动产证明第")) {
					lst = StringHelper.MatchBDCZMH(bdcqzh);
				}
				if (lst != null && lst.size() == 4) {
					bdcqzh = lst.get(0) + "-" + lst.get(1) + "-" + lst.get(2)
							+ "-" + lst.get(3);
				}
			}
			QRCodeInfo = zsbh + "&" + bdcqzh + "&" + bdcdyh + "&" + bdcywh
					+ "&" + zsid + "&" + zsType + "&" + sfhbzs;
		}
		return QRCodeInfo;
	}
	/**
	 * 桂林二维码存蓄的内容获取
	 * @param xmbh
	 * @param zsid
	 * @return  加密后的不动产权证号
	 */
	public String GetQRCodeInfoGL(String xmbh, String zsid) {
		BDCS_ZS_GZ zs = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
		if(zs!=null&&!StringHelper.isEmpty(zs.getBDCQZH())) {
			return zs.getBDCQZH(); 
		}
		return zsid;
		
	}

	/**
	 * 判断证书是否可以打印
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年11月25日20:59:08
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@Override
	public ResultMessage CanPrintZS(String xmbh, String zsid) {
		ResultMessage ms = new ResultMessage();
		ms.setSuccess("false");
		ms.setMsg("证书编号未保存，不能打印！");
		BDCS_ZS_GZ zs = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
		if (zs != null) {
			if (!StringHelper.isEmpty(zs.getZSBH())) {
				ms.setSuccess("true");
				ms.setMsg("");
			}
		}
		return ms;
	}

	/**
	 * TODO:@liushufeng:请描述这个方法的作用
	 * 
	 * @Title: saveDAGH
	 * @author:liushufeng
	 * @date：2015年12月4日 上午6:10:39
	 * @param xmbh
	 * @param dagh
	 * @return
	 */
	@Override
	public ResultMessage saveDAGH(String xmbh, String dagh) {
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class,
				"XMBH='" + xmbh + "'");
		if (zss != null && zss.size() > 0) {
			for (BDCS_ZS_GZ zs : zss) {
				zs.setCFDAGH(dagh);
				baseCommonDao.update(zs);
			}
			baseCommonDao.flush();
		}
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");

		return msg;
	}

	/**
	 * TODO:@liushufeng:请描述这个方法的作用
	 * 
	 * @Title: getDAGH
	 * @author:liushufeng
	 * @date：2015年12月4日 上午6:18:59
	 * @param xmbh
	 * @return
	 */
	@Override
	public String getDAGH(String xmbh) {
		String dagh = "";
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		if (xmxx != null && !StringHelper.isEmpty(xmxx.getDAGH()))
			dagh = xmxx.getDAGH().toString();
		return dagh;
	}

	/**
	 * 获取证书信息
	 * 
	 * @Title: getZSForm
	 * @author:liushufeng
	 * @date：2015年11月14日 下午8:19:26
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@Override
	public ZS getZSForm(String xmbh, String zsid) {
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)) {
			//如果是国有建设用地使用权的话，不支持多个单元一本证
			if (!(QLLX.GYJSYDSHYQ.Value.equals(xmxx.getQLLX()) ||QLLX.ZJDSYQ.Value.equals(xmxx.getQLLX())||QLLX.LDSHYQ_SLLMSYQ.Value.equals(xmxx.getQLLX())||QLLX.LDSYQ_SLLMSYQ.Value.equals(xmxx.getQLLX())
					||QLLX.JTTDSYQ.Value.equals(xmxx.getQLLX()) || QLLX.GYNYDSHYQ.Value.equals(xmxx.getQLLX()))) {
				return zsservice2.getZSForm(xmbh, zsid);
			}
		}
		CertInfoTools certinfotools=new CertInfoTools();
		return certinfotools.CreatZS(xmxx, zsid);
	}

	/**
	 * 获取不动产登记证明
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getBDCDJZM(String xmbh, String zsid) {
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)){
			return zsservice2.getBDCDJZM(xmbh, zsid);
		}
		CertfyInfoTools certinfotools=new CertfyInfoTools();
		return certinfotools.CreateZM(xmxx, zsid);
	}

	/**
	 * 更新中间库的证书印刷号
	 * @作者 likun
	 * @创建时间 2016年7月1日下午3:11:19
	 * @param qzh 权证号
	 * @param zsh 证书印刷号
	 */
	private void updatezshToZJK(String qzh,String zsh) {
		String sql="update gxdjk.qlr set QZYSXLH='"+zsh+"' where BDCQZH='"+qzh+"'";
		Connection jyConnection = JH_DBHelper.getConnect_gxdjk();
		if(jyConnection!=null){
			try {
				JH_DBHelper.excuteUpdate(jyConnection,sql);
				jyConnection.close();
			} catch (SQLException e) {
			}
		}
	}

	public List<ZS> getZsInfoList(String xmbh) {
		Message msg = new Message();
		List<Tree> tree = getZsTreeEx(xmbh);
		List<ZS> zs = new ArrayList<ZS>();
		for (Tree tr : tree) {
			ZS zsInfo = getZSForm(xmbh, tr.getId());
			zsInfo.setBdcqzh(tr.getBdcqzh());
			zs.add(zsInfo);
		}
		return zs;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String ZsInfoExportExcel(JSONObject ColIndex, JSONObject column, JSONArray rows,
			HttpServletRequest request) throws FileNotFoundException,
			IOException {
		String basePath = request.getSession().getServletContext().getRealPath("/") + "\\resources\\PDF";
		String outpath = basePath + "\\tmp\\housequeryresult.xls";
		String url = request.getContextPath() + "\\resources\\PDF\\tmp\\housequeryresult.xls";
		File file=new File(outpath);    
		if(!file.exists())    
		{    
			file.createNewFile();
		}
		FileOutputStream outstream = new FileOutputStream(outpath);
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("证书信息列表");
		Row row = sheet.createRow(0);
		int colindex = 0;
		Map map = ColIndex;
		//指定排序器  
        TreeMap<String, String> index = new TreeMap<String, String>(new Comparator<String>(){   
            public int compare(String o1, String o2) {  
                return Integer.parseInt(o1)-Integer.parseInt(o2);  
            }     
        });
        index.putAll(map);
		Map Col = column;
		HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //水平布局：居中
        cellStyle.setWrapText(true);
        
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中  
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中  
        style.setWrapText(true);
		Iterator it=index.entrySet().iterator();           
		while(it.hasNext()){    
			Map.Entry entry = (Map.Entry)it.next();
			Cell cell = row.createCell(colindex);
			cell.setCellStyle(cellStyle);
	        Object value = entry.getValue();
	        value = Col.get(value);
	        if(StringHelper.isEmpty(value)){
				cell.setCellValue("");
			}else{
				cell.setCellValue(value.toString());
			}
			colindex++;                   
		}
		int rowindex = 1;
		for (Object r : rows) {
			Row insertRow = sheet.createRow(rowindex);
			colindex = 0;
			net.sf.json.JSONObject R = (net.sf.json.JSONObject) r;
			it=index.entrySet().iterator();
			while(it.hasNext()){    
				Map.Entry entry = (Map.Entry)it.next();
				Cell cell = insertRow.createCell(colindex);
				cell.setCellStyle(style);
		        Object value = entry.getValue();
		        if("fj".equals(value)||"qlqtzk".equals(value)){
		        	value = R.get(value);
		        	if(!StringHelper.isEmpty(value)){
		        		value = value.toString().replaceAll("<br/>", "\n");
		        	}
		        }else{
		        	value = R.get(value);
		        }
		        if(StringHelper.isEmpty(value)){
					cell.setCellValue("");
				}else{
					cell.setCellValue(value.toString());
				}
				colindex++;                   
			}
			Cell cell0 = insertRow.createCell(0);
			cell0.setCellValue(rowindex);
			rowindex++;
		}
		sheet.createFreezePane( 0, 1, 0, 1 );
		for (int i = 0; i < map.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		wb.write(outstream);
		outstream.flush();
		outstream.close();
		outstream = null;
		return url;
	}

	/* 
	 * @Description: 抵押注销时抽取产权证信息
	 * @Author：赵梦帆
	 * @Date：2016年12月21日 下午2:15:41
	 * @param xmbh
	 * @return
	 */
	@Override
	public Message GetDYZSList(String xmbh) {
		String fulSql = "SELECT DISTINCT ZS.BDCQZH FROM BDCK.BDCS_ZS_XZ ZS "
				+ "INNER JOIN BDCK.BDCS_QDZR_XZ QDZR ON ZS.ZSID=QDZR.ZSID "
				+ "INNER JOIN BDCK.BDCS_DJDY_GZ DY ON DY.DJDYID=QDZR.DJDYID "
				+ "INNER JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=QDZR.QLID "
				+ "WHERE QL.QLLX IN ('4','6','8') AND DY.XMBH='"+xmbh+"'";
		@SuppressWarnings("rawtypes")
		List<Map> zss = baseCommonDao.getDataListByFullSql(fulSql);
		if(zss!=null&&zss.size()>0){
			String bdcqzh = "";
			for (@SuppressWarnings("rawtypes") Map map : zss) {
				bdcqzh = map.get("BDCQZH") + "";
				List<BDCS_ZS_XZ> yfz = baseCommonDao.getDataList(BDCS_ZS_XZ.class, " BDCQZH='"+bdcqzh+"'");
				List<BDCS_DJFZEX> ZS = baseCommonDao.getDataList(BDCS_DJFZEX.class, " HFZSH='"+bdcqzh+"' AND XMBH='"+xmbh+"'");
				if(ZS!=null&&ZS.size()<1&&yfz!=null&&yfz.size()>0){
					BDCS_DJFZEX fz = new BDCS_DJFZEX();
					BDCS_ZS_XZ zs = yfz.get(0);
					BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
					fz.setHFZSH(zs.getBDCQZH());
					fz.setXMBH(xmbh);
					fz.setYWH(xmxx.getPROJECT_ID());
					baseCommonDao.save(fz);
				}
			}
		}
		Message Message = new Message();
		Message.setSuccess("true");
		return Message;
	}

	/* 
	 * @Description: 抵押注销证书列表
	 * @Author：赵梦帆
	 * @Date：2016年12月21日 下午3:37:01
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@Override
	public Page GetPagedFZListEX(String xmbh, Integer page, Integer rows) {
		List<DJFZXX> djfzxxList = new ArrayList<DJFZXX>();
		// 证书查询参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("xmbh", xmbh);
		String hql = " length(hFZSH)>0 and xMBH=:xmbh";
		// 获取证书表中信息
		Page p = baseCommonDao.getPageDataByHql(BDCS_DJFZEX.class, hql, map,
				page, rows);
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);

		List<BDCS_DJFZEX> zsList = (List<BDCS_DJFZEX>) p.getResult();
		List<String> bdcqzhlist=new ArrayList<String>();
		BDCS_ZS_XZ zs = null;
		for (BDCS_DJFZEX zsex : zsList) {
			if(!StringHelper.isEmpty(zsex.getHFZSH()) && !bdcqzhlist.contains(zsex.getHFZSH())){
				bdcqzhlist.add(zsex.getHFZSH());
				List<BDCS_ZS_XZ> zss = baseCommonDao.getDataList(BDCS_ZS_XZ.class, " BDCQZH='"+zsex.getHFZSH()+"'");
				zs = zss.get(0);
			}
			else{
				continue;
			}
			
			DJFZXX djfzxxBo = new DJFZXX();
			String strBDCQZH = zs.getBDCQZH();
			djfzxxBo.setZSBH(zs.getZSBH());
			djfzxxBo.setBDCQZH(strBDCQZH);
			djfzxxBo.setXMBH(xmbh);
			djfzxxBo.setCfdagh(StringHelper.isEmpty(xmxx.getDAGH()) ? "" : xmxx
					.getDAGH().toString());
			String strXMBH = xmbh;
			String strZSID = zs.getId();

			// 查询权地证人关系表
			StringBuilder builerqdzr = new StringBuilder();
			builerqdzr.append("ZSID='").append(strZSID).append("'");
			List<BDCS_QDZR_XZ> gxbList = baseCommonDao.getDataList(
					BDCS_QDZR_XZ.class, builerqdzr.toString());
			if (!gxbList.isEmpty()) {
				BDCS_QDZR_XZ qdzr = gxbList.get(0);
				String strQLRID = qdzr.getQLRID();
				String strBDCDYH = qdzr.getBDCDYH();
				String strQLID = qdzr.getQLID();
				djfzxxBo.setBDCDYH(strBDCDYH);

				// 查询权利数据
				StringBuilder builerql = new StringBuilder();
				builerql.append("id='").append(strQLID).append("'");
				List<BDCS_QL_XZ> qlList = baseCommonDao.getDataList(
						BDCS_QL_XZ.class, builerql.toString());
				if (!qlList.isEmpty()) {
					BDCS_QL_XZ bdcs_ql_gz = qlList.get(0);
					if (bdcs_ql_gz != null) {
						djfzxxBo.setBDCDYH(bdcs_ql_gz.getBDCDYH());
						if (bdcs_ql_gz.getCZFS().equals(CZFS.GTCZ.Value)) {
							// 权利人
							StringBuilder strBuilder = new StringBuilder();
							StringBuilder builerqlr = new StringBuilder();
							builerqlr.append("QLID='").append(strQLID).append("'");
							List<BDCS_QLR_XZ> qlrList = baseCommonDao
									.getDataList(BDCS_QLR_XZ.class,
											builerqlr.toString());
							for (BDCS_QLR_XZ qlr : qlrList) { // 权利人可能有多个
								strBuilder.append(qlr.getQLRMC() + ",");
							}
							String strQLRMC = "";
							if (strBuilder.toString().length() > 0) {
								strQLRMC = strBuilder.toString().substring(0,
										strBuilder.length() - 1);// 去除最后一个,号
							}
							djfzxxBo.setQLR(strQLRMC);
						} else {
							// 权利人
							StringBuilder strBuilder = new StringBuilder();
							StringBuilder builerqlr = new StringBuilder();
							builerqlr.append("id='").append(strQLRID).append("'");
							List<BDCS_QLR_XZ> qlrList = baseCommonDao
									.getDataList(BDCS_QLR_XZ.class,
											builerqlr.toString());
							for (BDCS_QLR_XZ qlr : qlrList) { // 权利人可能有多个
								strBuilder.append(qlr.getQLRMC() + ",");
							}
							String strQLRMC = "";
							if (strBuilder.toString().length() > 0) {
								strQLRMC = strBuilder.toString().substring(0,
										strBuilder.length() - 1);// 去除最后一个,号
							}
							djfzxxBo.setQLR(strQLRMC);
						}
					}
				}
			}

			// 查询发证信息
			StringBuilder builerfz = new StringBuilder();
			builerfz.append("XMBH='").append(strXMBH).append("' AND HFZSH='")
					.append(strBDCQZH).append("'");
			List<BDCS_DJFZEX> fzList = baseCommonDao.getDataList(BDCS_DJFZEX.class,
					builerfz.toString());
			if (fzList.size() > 0 && fzList.get(0).getFZSJ()==null) {
				djfzxxBo.setSFFZ("否");
				djfzxxBo.setCZLX("发证");
				djfzxxBo.setZSID(fzList.get(0).getId());
			} else {
				djfzxxBo.setLZR(fzList.get(0).getLZRXM());
				djfzxxBo.setLZSJ(fzList.get(0).getFZSJ());
				djfzxxBo.setSFFZ("是");
				djfzxxBo.setCZLX("撤销");
				djfzxxBo.setZSID(fzList.get(0).getId());
			}
			djfzxxList.add(djfzxxBo);
		}
		Page resultPage = new Page(Page.getStartOfPage(page, rows),
				djfzxxList.size(), rows, djfzxxList);
		Long totalCount = baseCommonDao
				.getCountByFullSql(" from BDCK.BDCS_DJFZEX Where length(hFZSH)>0 and "
						+ ProjectHelper.GetXMBHCondition(xmbh));
		resultPage.setTotalCount(totalCount);
		return resultPage;
	}
	
	@Override
	public BDCS_RKQZB getRKQZB(String condition){
		BDCS_RKQZB rkqzb = null;
		List<BDCS_RKQZB> rkqzbs = baseCommonDao.getDataList(BDCS_RKQZB.class,
				condition);
		if (rkqzbs != null && rkqzbs.size() > 0) {
			rkqzb = rkqzbs.get(0);
		}
		return rkqzb;
	}

	@Override
	public Long validateZSBH(String zsid, String zsbh_all, String qzlx) {
		String qztype="%(____)%不动产权第_______号";
	    if("1".equals(qzlx)){
	    	qztype="%(____)%不动产证明第_______号";
	    }
	    long countzs = baseCommonDao.getCountByFullSql(" FROM (SELECT GZ.BDCQZH,GZ.ZSBH,GZ.ZSID FROM BDCK.BDCS_ZS_GZ GZ "
				+ " UNION SELECT XZ.BDCQZH,XZ.ZSBH,XZ.ZSID FROM BDCK.BDCS_ZS_XZ XZ "
				+ " UNION SELECT LS.BDCQZH,LS.ZSBH,LS.ZSID FROM BDCK.BDCS_ZS_LS LS)"
				+ " WHERE ZSBH='"+ zsbh_all +"' AND BDCQZH LIKE '"+qztype+"' AND ZSID NOT IN ('"+zsid+"')");
		return countzs;
	}

	@Override
	public ResultMessage saveZSBHWithManager(String zsid, String xmbh, String zsbh_all, String qzlx, String qzzl, String bdcdylx) {
		ResultMessage msg = new ResultMessage();
		String szr = Global.getCurrentUserInfo().getLoginName();
		// --通过证书编号，查看入库权证表中是否存在该权证；权证类型：0表示证书，1表示证明
		int start = 0;
	    int end = 1;
       //如果编号第一个为字母
	    String zsbh = zsbh_all;
	    if (zsbh_all.substring(start, end).matches("[a-zA-Z]")){
	      zsbh = zsbh_all.substring(end, zsbh_all.length());
	    }
		List<BDCS_RKQZB> rkqzbs = baseCommonDao.getDataList(BDCS_RKQZB.class, "SFSZ='0' AND SFZF='0' AND QZBH='" + zsbh + "' and qzlx='" + qzlx +  "' and qzzl ='" + qzzl +"'");
		if (!StringHelper.isEmpty(rkqzbs) && rkqzbs.size() > 0) {
			// --通过人员登录名及创建时间
			List<BDCS_QZGLTJB> qzgltjbs = baseCommonDao.getDataList(BDCS_QZGLTJB.class, "rydlm ='" + szr + "' and qzlx ='" + qzlx + "' order by  cjsj desc");
			if (!StringHelper.isEmpty(qzgltjbs) && qzgltjbs.size() > 0) {
				if (SF.YES.Value.equals(ConfigHelper.getNameByValue("ISFilter_zsbh"))) {
					if(!StringHelper.isEmpty(qzgltjbs.get(0).getRYID())){
						if(!qzgltjbs.get(0).getRYID().equals(rkqzbs.get(0).getLQRYID())){
							msg.setMsg("当前用户没有领取该证书编号，请先在证书分发管理中领取该证书编号！");
							msg.setSuccess("false");
							return msg;
						}
					}
				}
				Long sorcezsbh = StringHelper.getLong(zsbh);
				boolean bflag = false;
				for (BDCS_QZGLTJB qzgltjb : qzgltjbs) {
					Long maxzsbh = StringHelper.getLong(qzgltjb.getJSQZBH());
					Long minzsbh = StringHelper.getLong(qzgltjb.getQSQZBH());
					if (sorcezsbh <= maxzsbh && sorcezsbh >= minzsbh) {
						bflag = true;
						Boolean flag = updateZs(zsid, zsbh_all);
						try {
							// sunhb-2015-09-08
							// 套打或打印时，添加登记缮证信息，添加try可以保证永远不影响打印的信息
							updateDJSZ(xmbh);
						} catch (Exception e) {
						}
						if (flag) {
							AddSZXXToRKQZB(qzlx, zsbh , qzzl, zsid, xmbh, bdcdylx);
							msg.setMsg("保存成功");
							msg.setSuccess("true");
							YwLogUtil.addYwLog("保存证书编号，保存成功，证书编号：" + zsbh_all, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
						} else {
							msg.setMsg("保存失败");
							msg.setSuccess("false");
							YwLogUtil.addYwLog("保存证书编号，失败，证书编号：" + zsbh_all, ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
						}
					}
					if (bflag) {
						return msg;
					} else {
						msg.setMsg("当前用户没有该证书编号，请先录入证书编号");
						msg.setSuccess("false");
						return msg;
					}
				}
			} else {
				msg.setMsg("当前用户没有该证书编号，请先录入证书编号");
				msg.setSuccess("false");
				return msg;
			}
		} else {
			msg.setMsg("不存在该证书编号或该证书编号已缮证(已保存编号)/已作废");
			msg.setSuccess("false");
			return msg;
		}
	
		return msg;
	}

	@Override
	public ResultMessage saveZSBH(String zsid, String xmbh, String zsbh_all,String qzlx, String qzzl, HttpServletRequest request,String bdcdylx) {
		ResultMessage msg = new ResultMessage();
		Boolean flag = updateZs(zsid, zsbh_all);
		try {// sunhb-2015-09-08 套打或打印时，添加登记缮证信息，添加try可以保证永远不影响打印的信息
			updateDJSZ(xmbh);
		} catch (Exception e) {
		}
		if (flag) {			
			AddSZXXToRKQZB(qzlx, zsbh_all, qzzl, zsid, xmbh, bdcdylx);
			String imgformat = "png";
			String filepath=String.format("%s\\resources\\qrcode\\%s.%s",request.getRealPath("/"),xmbh,imgformat);
			File file=new File(filepath);
			if(!file.exists()){
				BDCS_XMXX xmxx= baseCommonDao.get(BDCS_XMXX.class, xmbh);
				BufferedImage img = BarcodeUtil.createBarcodeStream(xmxx.getYWLSH(), "Code128", 240, 60, true);
				try {
					ImageIO.write(img,"png",new FileOutputStream(new File(filepath)));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String content = GetQRCodeInfo(xmbh, zsid);
			QRCodeHelper.CreateQRCode(content, zsid, imgformat, 120, 120, request);			
			msg.setMsg("保存成功");
			msg.setSuccess("true");
			YwLogUtil.addYwLog("保存证书编号，保存成功，证书编号：" + zsbh_all, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		} else {
			msg.setMsg("保存失败");
			msg.setSuccess("false");
			YwLogUtil.addYwLog("保存证书编号，失败，证书编号：" + zsbh_all, ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
		}		
		return msg;
	}
//	public String GetQRCodeInfo1(String xmbh, String zsid) {
//		
//	}
	//孝感市获取二维码信息
	public String GetQRCodeInfoXG(String xmbh, String zsid) {
		String QRCodeInfo = "";
		if (StringHelper.isEmpty(xmbh) || StringHelper.isEmpty(zsid)) {
			return QRCodeInfo;
		}
		String bdcqzh = "";
		String djjg = "";
		String qlrmc = "";
		String bdcdyh = "";
		String bdcywh = "";
		String bdcdyid = "";
		String zsbh = "";
		String zsType = "1";// 默认证明
		String sfhbzs = "0";// 默认证明
		List<String> list=  new ArrayList<String>();
		System.out.println("zsid="+zsid);
		BDCS_ZS_GZ zs = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
		if (zs != null) {
			bdcqzh = zs.getBDCQZH();
			zsbh = zs.getZSBH();
		}
	
		List<BDCS_QDZR_GZ> qdzrs = baseCommonDao.getDataList(
				BDCS_QDZR_GZ.class, "ZSID='" + zsid + "'");
		if (qdzrs != null && qdzrs.size() > 0) {
			for (BDCS_QDZR_GZ qdzr : qdzrs) {
				List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
						BDCS_DJDY_GZ.class, "DJDYID='" + qdzr.getDJDYID()
								+ "' AND XMBH='" + xmbh + "'");
				if (djdys != null && djdys.size() > 0) {
					for (BDCS_DJDY_GZ djdy : djdys) {
						RealUnit unit = UnitTools.loadUnit(
								BDCDYLX.initFrom(djdy.getBDCDYLX()),
								DJDYLY.initFrom(djdy.getLY()),
								djdy.getBDCDYID());
						if (unit != null) {
							if (!StringHelper.isEmpty(unit.getBDCDYH())
									&& !bdcdyh.contains(unit.getBDCDYH())) {
								if (StringHelper.isEmpty(bdcdyh)) {
									bdcdyh = bdcdyh + unit.getBDCDYH();
								} else {
									bdcdyh = bdcdyh + "、" + unit.getBDCDYH();
								}
							}
							if (!bdcdyid.contains(unit.getId())) {
								if (StringHelper.isEmpty(bdcdyid)) {
									bdcdyid = bdcdyid + unit.getId();
								} else {
									bdcdyid = bdcdyid + "、" + unit.getId();
								}
							}
						}
					}
				}
				BDCS_QLR_GZ qlr = baseCommonDao.get(BDCS_QLR_GZ.class, qdzr.getQLRID());
				if (qlr!= null) {
					qlrmc = qlr.getQLRMC();
					
					list.add(qlrmc);
					
				}
				BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class,
						qdzr.getQLID());
				if (ql != null) {
					djjg = ql.getDJJG();
					if (!DJLX.YGDJ.Value.equals(ql.getDJLX())) {
						if (QLLX.GJTDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.JTTDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.GYJSYDSHYQ.Value.equals(ql.getQLLX())
								|| QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql
										.getQLLX())
								|| QLLX.JTJSYDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(ql
										.getQLLX())
								|| QLLX.ZJDSYQ.Value.equals(ql.getQLLX())
								|| QLLX.ZJDSYQ_FWSYQ.Value.equals(ql.getQLLX())) {
							zsType = "0";// 设定类型为权证
							
						}
					}
				
				}
				
			}
			List<String> lst = null;
			if (!StringHelper.isEmpty(bdcqzh)) {
				if (bdcqzh.contains("不动产权第")) {
					lst = StringHelper.MatchBDCQZH(bdcqzh);

				} else if (bdcqzh.contains("不动产证明第")) {
					lst = StringHelper.MatchBDCZMH(bdcqzh);
				}
				if (lst != null && lst.size() == 4) {
					bdcqzh = lst.get(0) + "-" + lst.get(1) + "-" + lst.get(2)
							+ "-" + lst.get(3);
				}
			}
			QRCodeInfo ="权利人:";
			if(list != null && list.size()>0){
				if(list.size()>2){
					QRCodeInfo += list.get(0)+","+list.get(1)+","+list.get(2)+"等等"+"&";
				}
				else if(list.size()==2){
					QRCodeInfo += list.get(0)+","+list.get(1)+"&" ;
				}
				else{
					QRCodeInfo += list.get(0)+"&" ;
				}
				QRCodeInfo +="证书编号:"+zsbh + "&" +"权证号:"+ bdcqzh + "&" +"登记机构:孝感市国土资源局" ;//+"&"+zsid;
			}
		}
		return QRCodeInfo;
	}

	@Override
	public boolean updateRKQZB(String qzlx, String zsbh) {
		// TODO Auto-generated method stub
		return false;
	}
}
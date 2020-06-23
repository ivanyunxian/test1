package com.supermap.realestate.registration.service.impl.share;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.wisdombusiness.framework.model.gxjyk.Gxjhxm;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYDYAQ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYFDCQ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYYGDJ;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

@Service("extractDataForJLService")
public class ExtractDataForJLServiceImpl extends ExtractDataServiceImpl {

	@Override
	public String ExtractSXFromZJK(String casenum, String xmbh, boolean bool) {
		String flag = "false";
		try {
			casenum=AutogetCasenumbyrelatonid(xmbh);
			if(casenum!=null&&!casenum.equals("")){
				if(casenum.equals("noh")){
					flag="房产未推送当前选择单元对应的户信息！";
					return flag;
				}else if(casenum.equals("nogxjhxm")){
					flag="房产未推送当前选择单元对应的项目信息！";
					return flag;
				}else if(casenum.equals("selected")){
					flag="当前选择单元对应的房产业务号已经被其他业务读取过，本次无法读取！";
					return flag;
				}
				flag=updateQL(casenum,xmbh);
				return flag;
			}else{
				return flag;
			}
			
		} 
		catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}
	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年4月20日 下午4:29:35 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	*/
	private String AutogetCasenumbyrelatonid(String xmbh) {
		String casenum="",relationid="";
		try{
			List<BDCS_DJDY_GZ> djdy_GZs=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='"+xmbh+"'");
			if(djdy_GZs!=null&&djdy_GZs.size()>0){
				String bdcdyid=djdy_GZs.get(0).getBDCDYID();
				String djdylx=djdy_GZs.get(0).getBDCDYLX();
				if(djdylx.equals("031")){
					List<BDCS_H_XZ> h_XZs=baseCommonDao.getDataList(BDCS_H_XZ.class, "bdcdyid='"+bdcdyid+"'");
					relationid=h_XZs.get(0).getRELATIONID();
				}else if(djdylx.equals("032")){
					List<BDCS_H_XZY> h_XZYs=baseCommonDao.getDataList(BDCS_H_XZY.class, "bdcdyid='"+bdcdyid+"'");
					relationid=h_XZYs.get(0).getRELATIONID();
				}
				if(!relationid.equals("")){
					List<JYH> jyhs=CommonDaoJY.getDataList(JYH.class, "relationid='"+relationid+"'");
					if(jyhs!=null&&jyhs.size()>0){
						List<Map> gxjhxms=CommonDaoJY.getDataListByFullSql("select * from gxjhxm where zt='1' and gxxmbh in (select gxxmbh from h where relationid='"+relationid+"')");
						if(gxjhxms!=null&&gxjhxms.size()>0){
							for(int i=0;i<gxjhxms.size();i++){
								String YWH=gxjhxms.get(i).get("CASENUM").toString();
								List<Wfi_ProInst> wfi_ProInsts=baseCommonDao.getDataList(Wfi_ProInst.class, "YWH='"+YWH+"'");
								if(wfi_ProInsts!=null&&wfi_ProInsts.size()>0){
									continue;
								}else{
									return YWH;
								}
							}
							return "selected";
						}else{
							return "nogxjhxm";
						}
					}
					else{
						return "noh";
					}
				}
			}
		}catch(Exception e){
			
		}
		return casenum;
	}
	
	/**
	 * 根据gxjyk中的权利数据，修改不动产库
	 * @作者 likun
	 * @创建时间 2016年11月2日下午2:59:17
	 * @param xmbh
	 * @param bdcdyh
	 */
	protected String updateQL(String casenum,String xmbh) {
		String flag = "false";
		try{
			if(casenum!=null&&!"".equals(casenum)&&xmbh!=null&&!"".equals(xmbh)){
				//项目信息页面显示房产业务号
				List<BDCS_XMXX> xmxxs=baseCommonDao.getDataList(BDCS_XMXX.class,"XMBH='"+xmbh+"'");
				List<Wfi_ProInst> proinsts=baseCommonDao.getDataList(Wfi_ProInst.class,"File_Number='"+xmxxs.get(0).getPROJECT_ID()+"'");
				if(proinsts!=null&&proinsts.size()>0){
					Wfi_ProInst proinst=proinsts.get(0);
					proinst.setYwh(casenum);
					baseCommonDao.update(proinst);
					baseCommonDao.flush();
				}
				List<BDCS_QL_GZ> qls=baseCommonDao.getDataList(BDCS_QL_GZ.class,"xmbh='"+xmbh+"'");
				if(qls!=null&&qls.size()>0){
					for(int i=0;i<qls.size();i++){
						BDCS_QL_GZ ql=qls.get(i);
						ql.setCASENUM(casenum);
						baseCommonDao.update(ql);
						flag = "true";
					}
					baseCommonDao.flush();
				}
			}
		}
		catch(Exception ex){
		}
		return flag;
		
	}

}

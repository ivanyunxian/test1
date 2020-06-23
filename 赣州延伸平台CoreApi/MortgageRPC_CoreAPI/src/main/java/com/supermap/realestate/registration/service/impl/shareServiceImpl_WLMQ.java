package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.service.shareService_WLMQ;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * @Description:乌鲁木齐共享服务
 * @author yuxuebin
 * @date 2017年6月25日 15:15:22
 * @Copyright SuperMap
 */
@Service("shareServiceImpl_WLMQ")
public class shareServiceImpl_WLMQ implements shareService_WLMQ {

	@Autowired
	private CommonDao baseCommonDao;
	/*
	 * 日志输出
	 */
	public Logger logger = Logger.getLogger(DBServiceImpl.class);
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap<String, Object>> getXmxxInfo(String slrq) {
		List<HashMap<String, Object>> msg=new ArrayList<HashMap<String, Object>>();
		StringBuilder builderXmxx=new StringBuilder();
		builderXmxx.append("SELECT XMXX.XMBH,XMXX.YWLSH,XMXX.XMMC,XMXX.DJLX,CS.PRODEFCLASS_NAME AS LCFL,PROINST.PRODEF_NAME AS LCMC,XMXX.SLSJ ");
		builderXmxx.append("FROM BDCK.BDCS_XMXX XMXX ");
		builderXmxx.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ");
		builderXmxx.append("LEFT JOIN BDC_WORKFLOW.WFD_PRODEF PRODEF ON PROINST.PRODEF_ID=PRODEF.PRODEF_ID ");
		builderXmxx.append("LEFT JOIN BDC_WORKFLOW.WFD_PROCLASS CS ON PRODEF.PRODEFCLASS_ID=CS.PRODEFCLASS_ID ");
		builderXmxx.append("LEFT JOIN BDCK.BDCS_WLMQ_FIGURE FIGURE ON FIGURE.PRODEF_ID=PROINST.PRODEF_ID ");
		builderXmxx.append("WHERE FIGURE.PRODEF_ID IS NOT NULL ");
		builderXmxx.append("AND (XMXX.SLSJ BETWEEN TO_DATE('");
		builderXmxx.append(slrq);
		builderXmxx.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('");
		builderXmxx.append(slrq);
		builderXmxx.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS'))");
		List<Map> list=baseCommonDao.getDataListByFullSql(builderXmxx.toString());
		if(list!=null&&list.size()>0){
			for(Map m:list){
				String XMBH=StringHelper.formatObject(m.get("XMBH"));
				String djlxvalue=StringHelper.formatObject(m.get("DJLX"));
				HashMap<String, Object> xminfo=new HashMap<String, Object>();
				xminfo.put("YWLSH", m.get("YWLSH"));
				xminfo.put("XMMC", m.get("XMMC"));
				xminfo.put("LCFL", m.get("LCFL"));
				xminfo.put("LCMC", m.get("LCMC"));
				xminfo.put("SLSJ", m.get("SLSJ"));
				DJLX djlx_info=DJLX.initFrom(djlxvalue);
				if(djlx_info!=null){
					xminfo.put("DJLXH", djlx_info.Name);
				}else{
					xminfo.put("DJLXH", "");
				}
				
				
				List<HashMap<String,String>> list_dyinfo=new ArrayList<HashMap<String,String>>();
				List<BDCS_DJDY_GZ> list_djdy=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, " XMBH='"+XMBH+"'");
				if(list_djdy!=null&&list_djdy.size()>0){
					for(BDCS_DJDY_GZ djdy:list_djdy){
						RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
						if(unit!=null){
							HashMap<String,String> dyinfo=new HashMap<String, String>();
							dyinfo.put("BDCDYH", unit.getBDCDYH());
							list_dyinfo.add(dyinfo);
						}
					}
				}else{
					continue;
				}
				xminfo.put("UNITINFOS", list_dyinfo);
				List<HashMap<String,String>> list_sqrinfo=new ArrayList<HashMap<String,String>>();
				List<BDCS_SQR> list_sqr=baseCommonDao.getDataList(BDCS_SQR.class, " XMBH='"+XMBH+"'");
				if(list_sqr!=null&&list_sqr.size()>0){
					for(BDCS_SQR sqr:list_sqr){
						HashMap<String,String> sqrinfo=new HashMap<String, String>();
						sqrinfo.put("SQRXM", sqr.getSQRXM());
						sqrinfo.put("SQRLB", sqr.getSQRLB());
						sqrinfo.put("LXDH", sqr.getLXDH());
						sqrinfo.put("TXDZ", sqr.getTXDZ());
						list_sqrinfo.add(sqrinfo);
					}
				}
				msg.add(xminfo);
				xminfo.put("SQRINFOS", list_sqrinfo);
			}
		}
		return msg;
	}
	
}

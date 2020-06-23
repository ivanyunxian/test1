package com.supermap.realestate.registration.ViewClass;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 获取权利信息，附属权利_现状，权利人信息
 * @author rongxf
 *
 */
public class QLXXXZ {

	private static CommonDao commonDao;
	static {
		if (commonDao == null) {
			commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
	}

	//权利ID
	private String qlId;
	//权利信息
	private BDCS_QL_XZ BDCS_QL_XZObj;
	//附属权利_现状
	private BDCS_FSQL_XZ BDCS_FSQL_XZObj;
	//权利人列表信息
	private List<BDCS_QLR_XZ> BDCS_QLR_XZObj;
	
	public String getQlId() {
		return qlId;
	}
	public void setQlId(String qlId) {
		this.qlId = qlId;
	}
	public BDCS_QL_XZ getBDCS_QL_XZObj() {
		return BDCS_QL_XZObj;
	}
	public void setBDCS_QL_XZObj(BDCS_QL_XZ bDCS_QL_XZObj) {
		BDCS_QL_XZObj = bDCS_QL_XZObj;
	}
	public BDCS_FSQL_XZ getBDCS_FSQL_XZObj() {
		return BDCS_FSQL_XZObj;
	}
	public void setBDCS_FSQL_XZObj(BDCS_FSQL_XZ bDCS_FSQL_XZObj) {
		BDCS_FSQL_XZObj = bDCS_FSQL_XZObj;
	}
	
	public List<BDCS_QLR_XZ> getBDCS_QLR_XZObj() {
		return BDCS_QLR_XZObj;
	}
	public void setBDCS_QLR_XZObj(List<BDCS_QLR_XZ> bDCS_QLR_XZObj) {
		BDCS_QLR_XZObj = bDCS_QLR_XZObj;
	}

	public static QLXXXZ Create(String qlId){
		QLXXXZ QLXXXZObj=new QLXXXZ();
		if (!StringUtils.isEmpty(qlId)){
			String hql = MessageFormat.format(" QLID=''{0}''", qlId);
			//获取权利信息+附属权利 
			List<BDCS_QL_XZ> listObj = commonDao.getDataList(BDCS_QL_XZ.class,hql);
			if(listObj.size()>0){
				QLXXXZObj.BDCS_QL_XZObj=listObj.get(0);
				//获取附属权利ID
				String StrFSQLID=QLXXXZObj.BDCS_QL_XZObj.getFSQLID();
				String StrXXBH=QLXXXZObj.BDCS_QL_XZObj.getXMBH();
				//附属权利_现状
				List<BDCS_FSQL_XZ> BDCS_FSQL_XZList=commonDao.getDataList(BDCS_FSQL_XZ.class, "FSQLID='" + StrFSQLID + "'");
				if(BDCS_FSQL_XZList.size()>0) {
					QLXXXZObj.BDCS_FSQL_XZObj=BDCS_FSQL_XZList.get(0);
				}
				QLXXXZObj.BDCS_QLR_XZObj=commonDao.getDataList(BDCS_QLR_XZ.class, "QLID='"+qlId+"' and xmbh='"+StrXXBH+"' ");
			}
		}
		return QLXXXZObj;
	} 
	
	/**
	 * 通过登记单元ID获取现状权——利信息和现状——附属权利
	 * @param DJDYID
	 * @return
	 */
	public static List<QLXXXZ> Create_DJDYID(String DJDYID){
		List<QLXXXZ> QLXXXZList = new ArrayList<QLXXXZ>();
		if (!StringUtils.isEmpty(DJDYID)) {
			String hql = MessageFormat.format(" DJDYID=''{0}''", DJDYID);
			//获取权利信息
			List<BDCS_QL_XZ> listObj = commonDao.getDataList(BDCS_QL_XZ.class,hql);
			if(listObj.size()>0) {	
				for(int i=0;i<listObj.size();i++) {
					QLXXXZ  QLXXXZObj=new QLXXXZ();

					QLXXXZObj.BDCS_QL_XZObj=listObj.get(i);
					//获取附属权利ID
					String StrFSQLID=QLXXXZObj.BDCS_QL_XZObj.getFSQLID();
					String StrXXBH=QLXXXZObj.BDCS_QL_XZObj.getXMBH();
					String StrQLID=QLXXXZObj.BDCS_QL_XZObj.getId();
					//附属权利_现状
					List<BDCS_FSQL_XZ> BDCS_FSQL_XZList=commonDao.getDataList(BDCS_FSQL_XZ.class,
							"FSQLID='" + StrFSQLID + "'");
					if(BDCS_FSQL_XZList.size()>0) {
						QLXXXZObj.BDCS_FSQL_XZObj=BDCS_FSQL_XZList.get(0);
					}
					QLXXXZObj.BDCS_QLR_XZObj=commonDao.getDataList(BDCS_QLR_XZ.class, "QLID='"+StrQLID+"' and xmbh='"+StrXXBH+"' ");
					QLXXXZList.add(QLXXXZObj);
				}
			}
		}
		return QLXXXZList;
	}
		
}

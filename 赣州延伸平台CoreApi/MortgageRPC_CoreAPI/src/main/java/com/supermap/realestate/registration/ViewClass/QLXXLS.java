package com.supermap.realestate.registration.ViewClass;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 获取权利信息，附属权利_现状，权利人信息
 * @author rongxf
 *
 */
public class QLXXLS {

	private static CommonDao commonDao;
	static {
		if (commonDao == null) {
			commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
	}

	//权利ID
	private String qlId;
	//权利信息
	private BDCS_QL_LS BDCS_QL_LSObj;
	//附属权利_现状
	private BDCS_FSQL_LS BDCS_FSQL_LSObj;
	//权利人列表信息
	private List<BDCS_QLR_LS> BDCS_QLR_LSObj;
	
	public BDCS_QL_LS getBDCS_QL_LSObj() {
		return BDCS_QL_LSObj;
	}
	public void setBDCS_QL_LSObj(BDCS_QL_LS bDCS_QL_LSObj) {
		BDCS_QL_LSObj = bDCS_QL_LSObj;
	}
	public BDCS_FSQL_LS getBDCS_FSQL_LSObj() {
		return BDCS_FSQL_LSObj;
	}
	public void setBDCS_FSQL_LSObj(BDCS_FSQL_LS bDCS_FSQL_LSObj) {
		BDCS_FSQL_LSObj = bDCS_FSQL_LSObj;
	}
	public List<BDCS_QLR_LS> getBDCS_QLR_LSObj() {
		return BDCS_QLR_LSObj;
	}
	public void setBDCS_QLR_LSObj(List<BDCS_QLR_LS> bDCS_QLR_LSObj) {
		BDCS_QLR_LSObj = bDCS_QLR_LSObj;
	}
	public String getQlId() {
		return qlId;
	}
	public void setQlId(String qlId) {
		this.qlId = qlId;
	}

	public static QLXXLS Create(String qlId){
		QLXXLS QLXXXZObj = new QLXXLS();
		if (!StringUtils.isEmpty(qlId)){
			String hql = MessageFormat.format(" QLID=''{0}''", qlId);
			//获取权利信息+附属权利 
			List<BDCS_QL_LS> listObj = commonDao.getDataList(BDCS_QL_LS.class,hql);
			if(listObj.size() > 0){
				QLXXXZObj.BDCS_QL_LSObj = listObj.get(0);
				//获取附属权利ID
				String StrFSQLID=QLXXXZObj.BDCS_QL_LSObj.getFSQLID();
				String StrXXBH=QLXXXZObj.BDCS_QL_LSObj.getXMBH();
				//附属权利_现状
				List<BDCS_FSQL_LS> BDCS_FSQL_LSList=commonDao.getDataList(BDCS_FSQL_LS.class, "FSQLID='" + StrFSQLID + "'");
				if(BDCS_FSQL_LSList.size()>0) {
					QLXXXZObj.BDCS_FSQL_LSObj=BDCS_FSQL_LSList.get(0);
				}
				QLXXXZObj.BDCS_QLR_LSObj=commonDao.getDataList(BDCS_QLR_LS.class, "QLID='"+qlId+"' and xmbh='"+StrXXBH+"' ");
			}
		}
		return QLXXXZObj;
	}
	
}

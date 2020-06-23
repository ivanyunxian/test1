package com.supermap.realestate.registration.service;

import java.sql.Blob;
import java.util.List;
import java.util.Map;



import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYJYBGQH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYQLR;

public interface InsertDataService {

	public String InsertSXFromZJK(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql,
			String xmbh, String casenum, Map<String, List<BDCQLR>> dyQlrList,
			Map<String, List<BDCQLR>> zyQlrList, List<String> bdcdyhList,
			boolean bool) throws Exception;

	public String InsertSXFromZJK(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql,
			String xmbh, String casenum,  List<BDCQLR> qlrList,List<JYQLR> jyqlrList, List<String> bdcdyhList,List<JYH> HList,
			boolean bool,String fwzt) throws Exception;
	
	public void InsertFJFromZJK(String proinstid, String fileName,
			String file_Path, byte[] buf, int i) throws Exception;
	
	public void InsertFJFromZJKEx(String proinstid, String fileName,
			String file_Path, String imgURL, int i,Blob imgTHUMB) throws Exception;

	public String InsertSXFromZJKBG(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh,
			String casenum, List<BDCQLR> qlrList, List<JYQLR> jyqlrList,
			List<JYJYBGQH> bgqhList, List<JYH> hList, boolean bool, String fwzt)
			throws Exception;

	public String InsertSXFromZJK(List<BDCS_QL_GZ> qlList, List<BDCS_FSQL_GZ> fsqlList,
			String xmbh, String casenum, List<BDCQLR> qlrList,List<JYQLR> jyqlrList, List<String> bdcdyhList,
			boolean bool,String fwzt)throws Exception;
	public String InsertSXFromZJKGZ(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh,
			String casenum, List<BDCQLR> qlrList, List<JYQLR> jyqlrList,
			List<JYJYBGQH> bgqhList, List<JYH> hList, boolean bool, String fwzt)
			throws Exception;

	public String DJAddZDDY(String xmbh,String[] bdcdyidArr)throws Exception;

	public String InsertSXFromTDBG(BDCS_QL_GZ bdcs_QL_GZ,
			BDCS_FSQL_GZ bdcs_FSQL_GZ, String xmbh, String tDCasenum,
			List<BDCQLR> qlrList, List<JYQLR> jYQlrList, List<String> bdcdyhList);

	public String InsertSXFromTD(List<BDCS_QL_GZ> qlList,
			List<BDCS_FSQL_GZ> fsqlList, String xmbh, String tDCasenum,
			List<BDCQLR> qlrList, List<JYQLR> jYQlrList, List<String> bdcdyhList);
	public String InsertSXFromZJK(List<BDCS_QL_GZ> qlList, List<BDCS_FSQL_GZ> fsqlList,
			String xmbh, List<String> casenumlist, List<BDCQLR> qlrList,List<JYQLR> jyqlrList, List<String> bdcdyhList,List<JYH> HList,
			boolean bool,String fwzt)throws Exception;
	
}

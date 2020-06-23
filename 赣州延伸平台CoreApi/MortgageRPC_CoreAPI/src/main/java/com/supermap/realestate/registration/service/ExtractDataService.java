package com.supermap.realestate.registration.service;

import javax.servlet.http.HttpServletRequest;

public interface ExtractDataService {

	public String ExtractSXFromZJK(String ywh, String xmbh, boolean bool);
	public String ExtractSXFromZJKS(String ywh, String xmbh, boolean bool,String projectid,String djyyString);

	public String AddDYBDCDY(String ywh, String xmbh);

	public boolean ExtractFJFromZJK(String proinstid, String casenum,
			String file_Path);
	public boolean ExtractFJFromZJKS(String proinstid, String casenum,String file_Path);

	public String GetBatchProject(String prodefid, String batchNumber,
			HttpServletRequest request, String number);

	public String GetProject(String prodefid, String xmbh, String batchNumber,
			String casenum, String proinstid, String count, String num,
			HttpServletRequest request);

	public boolean ExtractFJFromZJK(String proinstid, String casenum);

	public String getProinstID(String projectid);
	
	public String SaveDatabyFCInterface(String casenum);

	public String checkData(String casenum, String xmbh) ;
	
	public String saveFcywh(String casenum, String xmbh, String bdcdyid, String ly) ;
	
	public java.lang.String ExtractSXFromDJ(java.lang.String xmbh, String htbh);
	public String getFcStatus(String casenum);
	
}

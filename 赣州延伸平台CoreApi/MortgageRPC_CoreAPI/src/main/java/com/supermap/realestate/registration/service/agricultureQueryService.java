package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.wisdombusiness.web.Message;

public interface agricultureQueryService {

	Message queryAgriculture(Map<String, String> queryvalues, int page, int rows, boolean iflike, String sort, String order);

	@SuppressWarnings("rawtypes")
	List<Map> getAgricultureInfo(String bdcdyid, String bdcdylx);

	QLInfo GetQLInfo_XZ(String bdcdyid);

	@SuppressWarnings("rawtypes")
	List<List<Map>> GetDYDJList_XZ(String bdcdyid);

	QLInfo GetQLInfo(String qlid, String djzt);

	@SuppressWarnings("rawtypes")
	List<List<Map>> GetCFDJList_XZ(String bdcdyid);

	@SuppressWarnings("rawtypes")
	List<List<Map>> GetYYDJList_XZ(String bdcdyid);

	List<BDCS_DYXZ> GetDYXZList_XZ(String bdcdyid);

	@SuppressWarnings("rawtypes")
	List<List<Map>> GetQLList(String bdcdyid);

}

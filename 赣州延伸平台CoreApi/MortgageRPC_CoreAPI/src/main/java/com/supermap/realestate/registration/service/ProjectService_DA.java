package com.supermap.realestate.registration.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

import com.supermap.realestate.registration.ViewClass.DJInfo;
import com.supermap.realestate.registration.ViewClass.JKSInfo;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.SHB;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.SQSPBex;
import com.supermap.realestate.registration.model.BDCS_CRHT;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_DJGDFS;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_IDCARD_PIC;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRHT;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
 
public interface ProjectService_DA {

    /**
     * mass
     * @param project_id
     * @param request
     * @return
     * @throws IOException
     */
	public ProjectInfo getProjectInfo(String project_id, HttpServletRequest request) throws IOException;

    /**
     * mass
     * @param xmbh
     * @param projectid
     * @return
     */
	public BDCS_DJGD getDjgdInfo(String xmbh, String projectid);

	 

	 
    /**
     * mass
     * @param project_id
     * @return
     */
	public HashMap<String, List<HashMap<String, Object>>> GetDBXX2(String project_id);
	
	/**
     * yuxb
     * @param ywlsh
     * @return
     */
	public HashMap<String, List<HashMap<String, Object>>> GetDBXX3(String ywlsh);
   
	 
	/**
	 * mass
	 * @param lsh
	 * @param ajh
	 */
	public void UpdateXMXX(String lsh,String ajh);
    /**
     * mass
     * @param project_id
     * @return
     */
	public SHB GetSHB(String project_id);
	/**
	 * mass
	 * @param project_id
	 * @return
	 */
	public Map<String, Object> GetDBXX(String project_id);
	/**
	 * mass
	 * @param xmbh
	 * @param acinstid
	 * @param request
	 * @return
	 */
	public SQSPBex GetSQSPBex(String xmbh, String acinstid, HttpServletRequest request);
	
}

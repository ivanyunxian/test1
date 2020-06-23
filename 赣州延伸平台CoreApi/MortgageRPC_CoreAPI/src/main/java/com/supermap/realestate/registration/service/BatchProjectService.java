package com.supermap.realestate.registration.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.supermap.wisdombusiness.web.Message;


/**
 * 
 * @Description:远程报件service
 * @author 俞学斌
 * @date 2016年4月24日 21:20:12
 */
public interface BatchProjectService {

	Message AnalysisBatchProject(HttpServletRequest request, String prodef_id);

	Message AcceptBatchProject(HttpServletRequest request, String prodef_id,HttpServletResponse responses);

	Message UpLoadBatchZip(MultipartFile file, HttpServletRequest request);

	HashMap<String,Object> UpLoadBatchExcel(MultipartFile file, String prodef_id, HttpServletRequest request);

	HashMap<String, Object> AcceptBathcProjectExcel(HttpServletRequest request,
			String excelid, String prodef_id,HttpServletResponse response);
	
}

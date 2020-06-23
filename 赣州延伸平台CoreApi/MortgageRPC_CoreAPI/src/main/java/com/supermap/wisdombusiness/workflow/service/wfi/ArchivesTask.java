package com.supermap.wisdombusiness.workflow.service.wfi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.ConfigHelper;
 
import com.supermap.wisdombusiness.synchroinline.util.CommonsHttpInvoke;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;

@Component
public class ArchivesTask {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmMaterialService smMaterialService;
	
	public void swapArchives(){
		
		String worflowBasic = ConfigHelper.getNameByValue("worflow2dossier");
		new CommonsHttpInvoke().commonHttpDoPost(null, null, worflowBasic+"bdc/archives/GD/asyntask", null);
	}
	
	private void swap(){
		
	}
}

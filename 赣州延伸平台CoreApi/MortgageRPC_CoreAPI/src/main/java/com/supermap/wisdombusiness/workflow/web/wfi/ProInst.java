package com.supermap.wisdombusiness.workflow.web.wfi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
@Component("proinst")
public class ProInst {
	
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmProInst _SmProInst;
	@Autowired
	private SmActInst _ActInst;
	
     public ProInst(){
    	 
     }
     
     public boolean CheckAcceptType(int proDefID,int acceptTypeID ){
    	// List<SmObjInfo>=
    	 return false;
     }
     
     /**
      * 获取单个项目的详细信息
      * */
     public SmProInfo GetProjectInfo(String actinst){
    	 Wfi_ActInst ActInst=commonDao.load(Wfi_ActInst.class, "ACTINST_ID='"+actinst+"'");
    	 SmProInfo ProInfo=new SmProInfo();
    	 if(ActInst!=null)
    	 {
    		 Wfi_ProInst ProInst=commonDao.load(Wfi_ProInst.class, "PROINST_ID='"+ActInst.getProinst_Id()+"'");
    		 if(ProInst!=null)
    		 {
    			 _SmProInst.ProInfoFromProInst(ProInfo, ProInst);
    			 _ActInst.ProInfoFromActInst(ProInfo, ActInst);
    		 }
    	 }
    	 YwLogUtil.addYwLog("获取单个项目的详细信息", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
    	 return ProInfo;
     }
}

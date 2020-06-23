package com.supermap.realestate.registration.constraint;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.check.CheckItem;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

public class YDYToXFDYCheck implements CheckItem{
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage check(HashMap<String,String> params) {
		String xmbh=params.get("XMBH");
		String bdcdyid=params.get("BDCDYID");
		//String qlid=params.get("QLID");
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("true");
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		if (dao != null && xmxx != null) {
			String  djlx = xmxx.getDJLX();
			if(djlx.equals("100") && xmxx.getQLLX().equals("23")){
				if (!StringHelper.isEmpty(bdcdyid))	{
					StringBuilder sb = new StringBuilder();
					sb.append("SELECT x.PROJECT_ID,x.slsj,x.xmbh,x.djlx,x.qllx FROM bdck.bdcs_xmxx x LEFT JOIN  bdck.bdcs_djdy_gz d ON x.xmbh=d.xmbh WHERE d.bdcdyh IN ( ")
					  .append("SELECT a.bdcdyh FROM bdck.bdcs_h_xz a  WHERE a.bdcdyid='")
					  .append(bdcdyid)
					  .append("') order by x.slsj desc ");
					List<Map> alldjdy = dao.getDataListByFullSql(sb.toString());
					Date ydysj = null, toxfdysj= null;//预抵押时间、预抵押转现房抵押时间
					
					for (Map djdy : alldjdy) {
						if (djdy != null) {
							String project_id = djdy.get("PROJECT_ID").toString();
							Date slsj = (Date) djdy.get("SLSJ");

							if (!StringHelper.isEmpty(project_id) //预抵押
								&& (project_id.substring(12, 21).equals("700230200") 
								|| project_id.substring(12, 21).equals("700236004"))){
									ydysj=slsj;
							}
							if(!StringHelper.isEmpty(project_id) //换补证的流程编码
								&& (project_id.substring(12, 21).equals("100236015") 
								|| project_id.substring(12, 21).equals("100236016")
								|| project_id.substring(12, 21).equals("100232001")
								|| project_id.substring(12, 21).equals("100235002"))){
									toxfdysj=slsj;
								}
						}
					}						
					if(ydysj != null){	
						if(toxfdysj == null){
							ms.setSuccess("false");
						}
						else{
							if(ydysj.after(toxfdysj)){
								ms.setSuccess("true");
							}else{
								ms.setSuccess("false");
							}
						}
					}else{
						ms.setSuccess("true");
					}
				}
			}
			
		}
		return ms;
	}
}

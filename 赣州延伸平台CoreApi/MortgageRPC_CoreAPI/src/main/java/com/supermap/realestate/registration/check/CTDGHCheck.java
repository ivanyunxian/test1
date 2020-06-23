/**   
 * TODO:@liushufeng:请描述这个文件
 * @Title: BDCDYHCheck.java 
 * @Package com.supermap.realestate.registration.check 
 * @author liushufeng 
 * @date 2015年11月29日 上午4:46:21 
 * @version V1.0   
 */

package com.supermap.realestate.registration.check;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.constraint.ConstraintInterface;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * TODO:@胡加红:如果办理过纯土地过户流程的单元，在做转移、抵押等业务时，必须选走换补证流程
 * @ClassName: CTDGHCheck
 * @author 胡加红
 * @date 2016年03月28日 
 */
public class CTDGHCheck implements ConstraintInterface {

	/**
	 * TODO:@胡加红:请描述这个方法的作用
	 * @Title: check
	 * @author:胡加红
	 * @date：2016年03月28日 
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
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
			if(djlx.equals("100") || djlx.equals("200") || djlx.equals("300") || djlx.equals("400")){
				if (!StringHelper.isEmpty(bdcdyid))	{
					StringBuilder sb = new StringBuilder();
					sb.append("SELECT x.PROJECT_ID,x.slsj,x.xmbh,d.djdyid FROM bdck.bdcs_xmxx x LEFT JOIN  bdck.bdcs_djdy_gz d ON x.xmbh=d.xmbh WHERE d.bdcdyh IN ( ")
					  .append("SELECT a.bdcdyh FROM bdck.bdcs_h_xz a  WHERE a.bdcdyid='")
					  .append(bdcdyid)
					  .append("') order by x.slsj desc ");
					List<Map> alldjdy = dao.getDataListByFullSql(sb.toString());
					Date ctdghsj = null, hzsj= null;//纯土地过户时间、换证时间
					
					for (Map djdy : alldjdy) {
						if (djdy != null) {
							String project_id = djdy.get("PROJECT_ID").toString();
							Date slsj = (Date) djdy.get("SLSJ");

							if (!StringHelper.isEmpty(project_id) //纯土地过户的流程编码
								&& (project_id.substring(12, 21).equals("903040001") 
								|| project_id.substring(12, 21).equals("903046001"))){
									ctdghsj=slsj;
							}
							if(!StringHelper.isEmpty(project_id) //换补证的流程编码
								&& (project_id.substring(12, 21).equals("901040001") 
								|| project_id.substring(12, 21).equals("901046001"))){
									hzsj=slsj;
								}
						}
					}						
					if(ctdghsj != null){	
						if(hzsj == null){
							ms.setSuccess("false");
						}
						else{
							if(hzsj.after(ctdghsj)){
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

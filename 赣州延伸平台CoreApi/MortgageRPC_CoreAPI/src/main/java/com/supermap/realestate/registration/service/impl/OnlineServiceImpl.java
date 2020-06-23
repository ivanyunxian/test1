/**   
 * 计费服务实现类
 * @Title: ChargeServiceImpl.java 
 * @Package com.supermap.realestate.registration.service.impl 
 * @author liushufeng 
 * @date 2015年7月26日 上午3:54:14 
 * @version V1.0   
 */

package com.supermap.realestate.registration.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.service.OnlineService;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 在线服务实现类
 * @ClassName: OnlineServiceImpl
 * @author 俞学斌
 * @date 2016年10月13日 20:08:14
 */
@Service
public class OnlineServiceImpl implements OnlineService {
	@Autowired
	private CommonDao dao;

	@Override
	public ResultMessage GetZSInfo(String certtype, String bdcqzh,
			HashMap<String, String> param) {
		ResultMessage msg=new ResultMessage();
		String zsdata=null;
		List<BDCS_ZS_XZ> list_cert=dao.getDataList(BDCS_ZS_XZ.class, "BDCQZH='"+bdcqzh+"'");
		if(list_cert==null||list_cert.size()<=0){
			msg.setSuccess("false");
			msg.setMsg("002");//不动产权证号或不动产证明号无效！
			return msg;
		}else{
			for(BDCS_ZS_XZ cert : list_cert){
				if(!StringHelper.isEmpty(cert.getZSDATA())){
					zsdata=cert.getZSDATA();
					break;
				}
				
			}
		}
		boolean zsbh_check=false;
		if(param.containsKey("zsbh")&&!StringHelper.isEmpty(param.get("zsbh"))){
			String zsbh=param.get("zsbh");
			for(BDCS_ZS_XZ cert : list_cert){
				if(zsbh.equals(cert.getZSBH())){
					zsbh_check=true;
					break;
				}
			}
		}else{
			zsbh_check=true;
		}
		if(!zsbh_check){
			msg.setSuccess("false");
			msg.setMsg("003");//不动产权证或不动产证明证书编号无效！
			return msg;
		}
		boolean qlrmc_check=false;
		boolean qlrzjh_check=false;
		if(!param.containsKey("qlrzjh")||StringHelper.isEmpty(param.get("qlrzjh"))){
			qlrzjh_check=true;
		}
		if(param.containsKey("qlrmc")&&!StringHelper.isEmpty(param.get("qlrmc"))){
			String qlrmc=param.get("qlrmc");
			for(BDCS_ZS_XZ cert : list_cert){
				String zsid=cert.getId();
				List<BDCS_QLR_XZ> list_rightholer=dao.getDataList(BDCS_QLR_XZ.class, " id IN (SELECT QLRID FROM BDCS_QDZR_XZ WHERE ZSID='"+zsid+"')");
				if(list_rightholer!=null&&list_rightholer.size()>0){
					for(BDCS_QLR_XZ rightholer : list_rightholer){
						if(qlrmc.equals(rightholer.getQLRMC())){
							qlrmc_check=true;
							if(param.containsKey("qlrzjh")&&!StringHelper.isEmpty(param.get("qlrzjh"))){
								String qlrzjh=param.get("qlrzjh");
								if(qlrzjh.equals(rightholer.getZJH())){
									qlrzjh_check=true;
								}
							}
							if(qlrmc_check&&qlrzjh_check){
								break;
							}
						}
					}
				}
				if(qlrmc_check&&qlrzjh_check){
					break;
				}
			}
		}else{
			qlrmc_check=true;
			if(param.containsKey("qlrzjh")&&!StringHelper.isEmpty(param.get("qlrzjh"))){
				String qlrzjh=param.get("qlrzjh");
				for(BDCS_ZS_XZ cert : list_cert){
					String zsid=cert.getId();
					List<BDCS_QLR_XZ> list_rightholer=dao.getDataList(BDCS_QLR_XZ.class, " id IN (SELECT QLRID FROM BDCS_QDZR_XZ WHERE ZSID='"+zsid+"')");
					if(list_rightholer!=null&&list_rightholer.size()>0){
						for(BDCS_QLR_XZ rightholer : list_rightholer){
							if(qlrzjh.equals(rightholer.getZJH())){
								qlrzjh_check=true;
								break;
							}
						}
					}
					if(qlrzjh_check){
						break;
					}
				}
			}
		}
		if(!qlrmc_check){
			msg.setSuccess("false");
			msg.setMsg("004");//不动产权证或不动产证明权利人无效！
			return msg;
		}
		if(!qlrzjh_check){
			msg.setSuccess("false");
			msg.setMsg("005");//不动产权证或不动产证明证件号无效！
			return msg;
		}
		
		boolean qlr_check=false;
		boolean ql_check=false;
		boolean dy_check=false;
		for(BDCS_ZS_XZ cert : list_cert){
			String zsid=cert.getId();
			List<BDCS_QDZR_XZ> list_qdzr=dao.getDataList(BDCS_QDZR_XZ.class, " ZSID='"+zsid+"'");
			if(list_qdzr!=null&&list_qdzr.size()>0){
				for(BDCS_QDZR_XZ qdzr : list_qdzr){
					String qlid=qdzr.getQLID();
					String qlrid=qdzr.getQLRID();
					String djdyid=qdzr.getDJDYID();
					if(!StringHelper.isEmpty(qlid)){
						BDCS_QL_XZ right=dao.get(BDCS_QL_XZ.class, qlid);
						if(right!=null){
							ql_check=true;
							djdyid=right.getDJDYID();
						}
					}
					if(!ql_check){
						continue;
					}
					if(!StringHelper.isEmpty(qlrid)){
						BDCS_QLR_XZ rightholder=dao.get(BDCS_QLR_XZ.class, qlrid);
						if(rightholder!=null){
							qlr_check=true;
						}
					}
					if(!qlr_check){
						ql_check=false;
						continue;
					}
					if(!StringHelper.isEmpty(djdyid)){
						List<BDCS_DJDY_XZ> djdys=dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"'");
						if(djdys!=null&&djdys.size()>0){
							BDCS_DJDY_XZ djdy=djdys.get(0);
							RealUnit unit =UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.XZ, djdy.getBDCDYID());
							if(unit!=null){
								dy_check=true;
							}
						}
					}
					if(!dy_check){
						ql_check=false;
						qlr_check=false;
						continue;
					}
					break;
				}
			}
			if(ql_check&&qlr_check&&dy_check){
				break;
			}
		}
		if(!ql_check||!qlr_check||!dy_check){
			msg.setSuccess("false");
			msg.setMsg("006");//不动产权证或不动产证明关联的单元或权利或权利人不存在！
			return msg;
		}
		msg.setSuccess("true");
		msg.setMsg(zsdata);//不动产权证或不动产证明信息！
		return msg;
	}
}

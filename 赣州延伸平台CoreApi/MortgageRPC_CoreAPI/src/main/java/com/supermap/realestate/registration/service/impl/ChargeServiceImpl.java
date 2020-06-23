/**   
 * 计费服务实现类
 * @Title: ChargeServiceImpl.java 
 * @Package com.supermap.realestate.registration.service.impl 
 * @author liushufeng 
 * @date 2015年7月26日 上午3:54:14 
 * @version V1.0   
 */

package com.supermap.realestate.registration.service.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.config.ChargeParam;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_SFDY;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.service.ChargeService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.web.UserController;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

/**
 * 计费服务实现类
 * @ClassName: ChargeServiceImpl
 * @author liushufeng
 * @date 2015年7月26日 上午3:54:14
 */
@Service
public class ChargeServiceImpl implements ChargeService {

	private static final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	private CommonDao dao;
	
	@Autowired
	private ZSService zsService;

	/**
	 * 计算费用
	 * @Title: calculate
	 * @author:liushufeng
	 * @date：2015年7月26日 上午3:54:15
	 * @param xmbh
	 * @return
	 * @throws ParseException
	 */
	@Override
	public boolean calculate(String xmbh) throws ParseException {
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		if (handler != null) {
			ChargeParam param = handler.getChargeParam();
			if (param != null) {
				String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
				// 获取收费定义
				List<BDCS_DJSF> sfs = dao.getDataList(BDCS_DJSF.class, xmbhcond);
				if (sfs != null && sfs.size() > 0) {
					for (BDCS_DJSF sf : sfs) {
						BDCS_DJSF TMP = new BDCS_DJSF();
						try {
							PropertyUtils.copyProperties(TMP, sf);
						} catch (Exception e) {
						}
						try {
							if("njmode".equals(ConfigHelper.getNameByValue("ChargeMode").toLowerCase())){
								String id=sf.getSFDYID();
								if(!StringHelper.isEmpty(id)){
									BDCS_SFDY SFDY = dao.get(BDCS_SFDY.class, id);
									if(SFDY!=null){
										String sql=SFDY.getCACSQL();
										if(!StringHelper.isEmpty(sql)||"GBF".equals(SFDY.getSYMBOL())){
											int ts = 0;
											//工本费 ts-1 
											if("GBF".equals(SFDY.getSYMBOL())){
												List<Tree> list = zsService.getZsTreeEx(xmbh);
												int countdy = (int)dao.getCountByFullSql(" FROM BDCK.BDCS_DJDY_GZ WHERE XMBH='"+ xmbh +"'");
												if(list!=null&&list.size()>0)
													ts=(list.size()-countdy>0)?(list.size()-countdy):0;
											}else{
												ts = (int) dao.getCountByFullSql(sql + "'" + xmbh +"'");
											}
											TMP.setTS(ts);
											sf.setTS(ts);
										}
									}
								}
							}
							double value = calculate(TMP, param, xmbh);
							String strvalue=formatDouble(value);
							  double dd=Double.valueOf(strvalue).doubleValue();
							if (TMP.getSFSX() > 0) {
								if (dd > TMP.getSFSX()) {
									dd = TMP.getSFSX();
								}
							}
							sf.setYSJE(dd);
							sf.setSSJE(StringHelper.formatObject(dd));
							if("1".equals(sf.getSFJB())){
								sf.setYSJE(dd/2);
								sf.setSSJE(StringHelper.formatObject(dd/2));
							}
							dao.update(sf);
						} catch (Exception ee) {
							logger.error("计算费用出错：" + ee.getMessage());
						}
					}
				}
			}
		}
		dao.flush();
		return false;
	}

	/**
	 * 计算某项收费项的应收金额
	 * @Title: calculate
	 * @author:liushufeng
	 * @date：2015年7月26日 上午4:07:53
	 * @param sfdy
	 * @param param
	 * @return
	 * @throws ParseException
	 */
	private double calculate(BDCS_DJSF djsf, ChargeParam param, String xmbh) throws ParseException {
		double result = 0;
		if (djsf.getCALTYPE() == null || !djsf.getCALTYPE().toUpperCase().equals("SQL")) {

			Scope scope = Scope.create();
			Map<String, Double> map = param.getMap();
			for (Entry<String, Double> entry : map.entrySet()) {
				Variable var = scope.getVariable(entry.getKey());
				var.setValue(entry.getValue());

			}

			Variable SFJS = scope.getVariable("SFJS");
			SFJS.setValue(djsf.getSFJS());

			Variable SFBL = scope.getVariable("SFBL");
			SFBL.setValue(djsf.getSFBL());

			Variable MJZL = scope.getVariable("MJZL");
			MJZL.setValue(djsf.getMJZL());

			Variable MJJS = scope.getVariable("MJJS");
			MJJS.setValue(djsf.getMJJS());

			Variable SFZL = scope.getVariable("SFZL");
			SFZL.setValue(djsf.getSFZL());

			Variable ZLFYSX = scope.getVariable("ZLFYSX");
			ZLFYSX.setValue(djsf.getSFSX());
			if("sjzmode".equals(ConfigHelper.getNameByValue("ChargeMode").toLowerCase())
					||"njmode".equals(ConfigHelper.getNameByValue("ChargeMode").toLowerCase()))
			{
				Variable var = scope.getVariable("TS");
				var.setValue(djsf.getTS());
				// 5按套，6按证
				if (djsf.getSFLX().equals("6")) {
					Variable var2 = scope.getVariable("ZSGS");
					var2.setValue(djsf.getTS());
				}
				//显示公式
				String xsgs = djsf.getJSGS();
				if (!StringHelper.isEmpty(xsgs)) {

					xsgs = xsgs.replaceAll("TS", StringHelper.isEmpty(djsf.getTS()) ? "" : djsf.getTS().toString());
					xsgs = xsgs.replaceAll("SFJS", StringHelper.isEmpty(djsf.getSFJS()) ? "" : djsf.getSFJS().toString());
					xsgs = xsgs.replaceAll("SFBL", StringHelper.isEmpty(djsf.getSFBL()) ? "" : djsf.getSFBL().toString());
					xsgs = xsgs.replaceAll("MJZL", StringHelper.isEmpty(djsf.getMJZL()) ? "" : djsf.getMJZL().toString());
					xsgs = xsgs.replaceAll("MJJS", StringHelper.isEmpty(djsf.getMJJS()) ? "" : djsf.getMJJS().toString());
					xsgs = xsgs.replaceAll("SFZL", StringHelper.isEmpty(djsf.getSFZL()) ? "" : djsf.getSFZL().toString());
					xsgs = xsgs.replaceAll("ZLFYSX", StringHelper.isEmpty(djsf.getSFSX()) ? "" : djsf.getSFSX().toString());
					// 5按套，6按证
				
						xsgs = xsgs.replaceAll("ZSGS", StringHelper.isEmpty(djsf.getTS()) ? "" : djsf.getTS().toString());
						xsgs=xsgs.replaceAll("MJ",StringHelper.isEmpty(param.get("MJ")) ? "":param.get("MJ").toString());
						xsgs=xsgs.replaceAll("JYJG",StringHelper.isEmpty(param.get("JYJG")) ? "":param.get("JYJG").toString());
						
					xsgs = xsgs.replaceAll("[*]", "X");
					xsgs = xsgs.replaceAll("/", "÷");
					djsf.setXSGS(xsgs);
					dao.update(djsf);
					dao.flush();
				}
			}

			String expression = djsf.getJSGS();

			Expression expr = (Expression) Parser.parse(expression, scope);
			result = expr.evaluate();
		} else {
			String sql = djsf.getSQLEXP();
			if (!StringHelper.isEmpty(sql)) {
				Map<String, Double> map = param.getMap();
				Map<String, String> mapstr = new HashMap<String, String>();
				// 替换变量
				for (Entry<String, Double> ent : map.entrySet()) {
					String value = ent.getValue() == null ? "" : ent.getValue().toString();
					if (!StringHelper.isEmpty(value)) {
						String oldchar = "{" + ent.getKey() + "}";
						sql = sql.replace(oldchar, value);
					}
				}
				mapstr.put("XMBH", xmbh);
				List<Map> mapresult = dao.getDataListByFullSql(sql, mapstr);
				if (mapresult != null) {
					if (mapresult.size() > 0) {
						Map mp = mapresult.get(0);
						if (mp.keySet().size() > 0) {
							Set st = mp.keySet();
							if (st.size() > 0) {
								Object[] strs = st.toArray();
								if (strs != null && strs.length > 0) {
									if (strs[0] != null) {
										String key = strs[0].toString();
										Object oresult = mp.get(strs[0]);
										if (oresult != null) {
											String strresult = oresult.toString();
											result = Double.parseDouble(strresult);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 根据收费定义ID添加收费记录
	 * @Title: addSFfromDY
	 * @Author:liushufeng
	 * @Date：2015年7月28日 上午5:14:42
	 * @param xmbh
	 * @param sfdyids
	 * @return
	 */
	@Override
	public boolean addSFfromDY(String xmbh, String sfdyids) {
		String[] ids = sfdyids.split(",");
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		
		/*//收费环节收费项前添加权利人名称
		List<BDCS_QLR_XZ> list = dao.getDataList(BDCS_QLR_XZ.class, ProjectHelper.GetXMBHCondition(xmbh));
		List<String> listqlr=new ArrayList<String>();
		String qlrmc= "";
		if(list.size()>0){
			for (BDCS_QLR_XZ qlr:list) {
					if(!StringHelper.isEmpty(qlr.getQLRMC())&&!listqlr.contains(qlr.getQLRMC())){
						listqlr.add(qlr.getQLRMC());
					}
			}
			qlrmc = StringHelper.formatList(listqlr);
		}*/

		// 收费环节收费项前添加权利人名称
		List<BDCS_SQR> listsqr = dao.getDataList(BDCS_SQR.class,
				ProjectHelper.GetXMBHCondition(xmbh) + " ORDER BY SQRLB,SXH ");
		List<String> listqlr1 = new ArrayList<String>();
		if (listsqr.size() > 0) {
			for (BDCS_SQR sqr : listsqr) {
				if (ConstValue.SQRLB.JF.Value.equals(sqr.getSQRLB())) { // 权利人
					if (!StringHelper.isEmpty(sqr.getSQRXM())
							&& !listqlr1.contains(sqr.getSQRXM())) {
						listqlr1.add(sqr.getSQRXM());
					}
				}
			}
		}
		
		if (!StringHelper.isEmpty(xmbh)) {
			for (int i = 0; i < ids.length; i++) {
				BDCS_SFDY sfdy = dao.get(BDCS_SFDY.class, ids[i]);
				if (sfdy != null) {
					BDCS_DJSF sf = new BDCS_DJSF();
					sf.setYWH(info.getProject_id());
					sf.setSFDW(sfdy.getSFDW());
					sf.setSFJS(sfdy.getSFJS());
					sf.setSFLX(sfdy.getSFLX());
					sf.setSFKMMC(sfdy.getSFXLMC() + "(" + sfdy.getSFKMMC() + ")");
					sf.setXMBH(xmbh);
					sf.setMJJS(sfdy.getMJJS());
					sf.setMJZL(sfdy.getMJZL());
					sf.setSFZL(sfdy.getSFZL());
					sf.setSFSX(sfdy.getZLFYSX());
					sf.setSFBL(sfdy.getSFBL());
					sf.setSFBMMC(sfdy.getSFBMMC());
					sf.setJFDW("元");
					sf.setSFEWSF(SF.NO.Value);
					sf.setSFDYID(sfdy.getId());
					sf.setSFDW(ConfigHelper.getNameByValue("DJJGMC"));
					sf.setJSGS(sfdy.getJSGS());
					sf.setBZ(sfdy.getBZ());
					sf.setCALTYPE(sfdy.getCALTYPE());
					sf.setSQLEXP(sfdy.getSQLEXP());
					sf.setXSGS(sfdy.getJSGS());
					sf.setTS(1);
					
					//收费环节收费项前添加权利人名称
					//sf.setQLRMC(qlrmc);
					if(StringHelper.isEmpty(listqlr1)){
						sf.setQLRMC(StringHelper.formatList(listqlr1));
					}
					
					dao.save(sf);
				}
			}
		}
		dao.flush();
		return false;
	}

	private String formatDouble(Double d) {
		String areaFormater = "#######.###";
		if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("AREAFORMATER"))) {
			areaFormater = ConfigHelper.getNameByValue("AREAFORMATER");
		}
//		String configAreaJudge = ConfigHelper.getNameByValue("AreaJudge");
//		if (!StringHelper.isEmpty(d) && d >= 0) {
//			if("1".equals(configAreaJudge)&&(d==0)){
//				return "----";
//			}
//		}
		DecimalFormat df = new DecimalFormat(areaFormater);
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(d);
	}
	
}

package com.supermap.realestate.registration.service.impl.share;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import com.supermap.luzhouothers.service.SmCompactService;
import com.supermap.realestate.registration.dataExchange.JHK.Imp.DataSwapImpEx;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.PUSHFAIL;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.ExtractDataService;
import com.supermap.realestate.registration.service.JYDJYTHDataSwapService;
import com.supermap.realestate.registration.service.XMLService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.HttpRequest;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.gxdjk.Gxjhxm_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.H_dj;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.yingtanothers.model.GXJHXM;
import com.supermap.yingtanothers.service.ExtractAttachment;
import com.supermap.yingtanothers.service.QueryShareXxService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service("ShareTool")
public class ShareTool  {
	protected Logger logger = Logger.getLogger(ShareTool.class);

	@Autowired
	private ExtractDataService extractDataService;
	@Autowired
	private ExtractDataService extractDataForSJZService;
	@Autowired
	private ExtractDataService extractDataForWLMQService;
	@Autowired
	private ExtractAttachment e_ExtractAttachment;
	@Autowired
	private QueryShareXxService q_QueryShareXxService;
	@Autowired
	private ExtractDataService extractDataForGXService;
	@Autowired
	private ExtractDataService extractDataForJLService;
	@Autowired
	private XMLService xmlService;
	@Autowired
	private JYDJYTHDataSwapService jydjythDataSwapService;//交易登记一体化数据交互类
	@Autowired
	protected DBService dbService;
	/**
	 * 读取合同服务 增加by lxk CreateTime 2015年10月31日22:17:00
	 */
	@Autowired
	private SmCompactService smCompactService;
	@Autowired
	protected com.supermap.wisdombusiness.framework.dao.impl.CommonDaoJY CommonDaoJY;
	@Autowired
	protected com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ;
	@Autowired
	protected CommonDao baseCommonDao;
   
	/**
	 * 
	 * @作者 think
	 * @创建时间 2017年2月6日下午3:02:17
	 * @param xmbh 登记库xmbh
	 * @param casenum 房产业务号
	 * @param projectid 
	 * @param file_Path
	 * @param djyyString
	 * @return
	 */
	@SuppressWarnings("unused")
	public ResultMessage extractData(String xmbh,String casenum,String projectid,String file_Path,String djyyString,String bdcdyh,String jig) {
		ResultMessage msg = new ResultMessage();
		try {
			//读取
			String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
			// 是否通用版抽取数据
			String TYCQFROMZJK = ConfigHelper.getNameByValue("TYCQFROMZJK");
			boolean sftycq = false;
			if (TYCQFROMZJK != null && TYCQFROMZJK.equals("1")) {
				sftycq = true;
			}
			boolean flag = false;
			// 修改 by lxk 2015年10月31日22:05:23
			// 读取合同信息的服务不同，如果是泸州的则读取泸州的服务信息
			// System.out.println("准备抽取...");
			System.out.println(xzqdm + "   是否通用抽取:" + sftycq);
			if (xzqdm.contains("5105")) {
				msg = smCompactService.ReadCompactInfo(xmbh, casenum);
			}else if (xzqdm.contains("450300")) {
				//桂林市 房产 直接用数据库
//				msg = smCompactService_gl.ReadCompactInfos(xmbh, casenum);	
			}else if (xzqdm.contains("1301")) {
				// 石家庄读取数据
				boolean boolFlag = false;
				String proinstid = null;
				proinstid = extractDataForSJZService.getProinstID(projectid);
				if(jig.equals("gxjyk")){
					// 抽取属性信息
					String reFlag = extractDataForSJZService.ExtractSXFromZJK(casenum, xmbh, true);
					//属性抽取成功了才抽附件
					if (proinstid != null && reFlag.equals("true")) {
						// System.out.println("正在抽取附件...");
						//String file_Path = request.getSession().getServletContext().getRealPath("/");
						// 抽取附件
						boolFlag = extractDataForSJZService.ExtractFJFromZJK(proinstid, casenum, file_Path);
					}
					if (reFlag.equals("true")) {
						if (boolFlag) {
							msg.setMsg("全部数据读取成功");
							msg.setSuccess("true");
						} else {
							msg.setMsg("交易数据读取成功");
							msg.setSuccess("true");
						}
					} else if (reFlag.equals("warning")) {
						msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
						msg.setSuccess("false");
					} else if (reFlag.equals("false")) {
						msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
						msg.setSuccess("false");
					} else {
						msg.setMsg(reFlag);
						msg.setSuccess("false");
					}
				}else {
					// 抽取属性信息
					String reFlag = extractDataForSJZService.ExtractSXFromZJKS(casenum, xmbh, true,projectid,djyyString);
					//属性抽取成功了才抽附件
					if (proinstid != null && reFlag.equals("true")) {
						// System.out.println("正在抽取附件...");
//						String file_Path = request.getSession().getServletContext().getRealPath("/");
						// 抽取附件
						boolFlag = extractDataForSJZService.ExtractFJFromZJKS(proinstid, casenum, file_Path);
					}
					if (reFlag.equals("true")) {
						if (boolFlag) {
							msg.setMsg("全部数据读取成功");
							msg.setSuccess("true");
						} else {
							msg.setMsg("交易数据读取成功");
							msg.setSuccess("true");
						}
					}else if(reFlag.equals("eray")) {
						msg.setMsg("权利人或证件号为空");
						msg.setSuccess("false");
					} else if (reFlag.equals("warning")) {
						msg.setMsg("权利人或证件号有误");
						msg.setSuccess("false");
					} else if (reFlag.equals("falses")) {
						msg.setMsg("不动产证书编号有误");
						msg.setSuccess("false");
					}else if (reFlag.equals("sas")) {
						msg.setMsg("不动产证号为空");
						msg.setSuccess("false");
					} else if (reFlag.equals("false")) {
						msg.setMsg("不动产证号有误");
						msg.setSuccess("false");
					} else {
						msg.setMsg(reFlag);
						msg.setSuccess("false");
					}
				}
			}else if (xzqdm.contains("3606")) {
				// 鹰潭市读取数据
				String qlrmc = "";
				String qzh = "";
				String bdcdyid = "";
				String bdcdylx = "";
				Map<String, Object> map = xmlService.getMCAndZH(casenum);
				if (map != null && map.size() > 0) {
					qlrmc = map.get("QLRMC") == null ? "" : map.get("QLRMC").toString();
					qzh = map.get("QZH") == null ? "" : map.get("QZH").toString();
				}
				Map<String, String> m = xmlService.getLXAndID(qlrmc, qzh);
				if (m != null && m.size() > 0) {
					bdcdyid = m.get("BDCDYID") == null ? "" : m.get("BDCDYID").toString();
					bdcdylx = m.get("BDCDYLX") == null ? "" : m.get("BDCDYLX").toString();
				}
				flag = xmlService.CFDYFromShareDB(bdcdyid, bdcdylx, casenum, xmbh);
				if (flag) {
					String fcywh = xmlService.pdAutoCreate(xmbh);
					if (!"&".equals(fcywh)) {
						msg.setMsg("读取成功,自动创建项目");
					} else
						msg.setMsg("读取成功");
					msg.setSuccess("true");
					YwLogUtil.addYwLog("读取合同成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
				} else {
					msg.setMsg("合同号有误!");
					msg.setSuccess("false");
					YwLogUtil.addYwLog("读取合同成功-合同号有误!", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
				}
			}else if (xzqdm.contains("4501")) {
				// 南宁市读取数据450100
				boolean boolFlag = false;
				String proinstid = null;
				proinstid = extractDataService.getProinstID(projectid);
				if (proinstid != null) {
					// System.out.println("正在抽取附件...");
//					String file_Path = request.getSession().getServletContext().getRealPath("/");
					// 抽取附件
					boolFlag = extractDataService.ExtractFJFromZJK(proinstid, casenum);
				}
				// 抽取属性信息
				String reFlag = extractDataService.ExtractSXFromZJK(casenum, xmbh, true);
				if (reFlag.equals("true")) {
					if (boolFlag) {
						msg.setMsg("全部数据读取成功");
						msg.setSuccess("true");
					} else {
						msg.setMsg("交易数据读取成功");
						msg.setSuccess("true");
					}
				} else if (reFlag.equals("warning")) {
					msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
					msg.setSuccess("false");
				} else if (reFlag.equals("false")) {
					msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
					msg.setSuccess("false");
				} else {
					msg.setMsg(reFlag);
					msg.setSuccess("false");
				}
			}else if (xzqdm.contains("4502")) {
				// 柳州市读取数据450200
				//以下为原始
				//联调启用
				// boolean boolFlag = false;
				// 迁移后：抽取前先调用协同共享系统服务通过房产接口将数据存入共享交易库，后返回保存成功或者失败标志
				// 2016年7月20日 11:18:05
				// String share_url = ConfigHelper.getNameByValue("URL_SHARE");
				// String htbh=casenum;
				// URL url = new URL("" + share_url +"sharezjk/savedatabyfcinterface/" + htbh );				 
				// URLConnection urlcon = url.openConnection();
				// BufferedReader bf = new BufferedReader(new
				// InputStreamReader(urlcon.getInputStream(), "UTF-8"));
				// String saveFlag = null;
				// saveFlag = bf.readLine();
				// 本机环境测试时因不动产库为未处理当地存量数据，故接口抽取完毕后再修改单元，★记得更改GXJYK中H表Relationid★
				// if (saveFlag.equals("true")||saveFlag.equals("\"true\"")) {
				// 抽取属性信息
				String reFlag = extractDataService.ExtractSXFromZJK(casenum, xmbh, true);
				if (reFlag.equals("true")) {
					// if (boolFlag) {
					msg.setMsg("全部数据读取成功");
					msg.setSuccess("true");
					// } else {
					// msg.setMsg("交易数据读取成功");
					// msg.setSuccess("true");
					// }
				} else if (reFlag.equals("warning")) {
					msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
					msg.setSuccess("false");
				} else if (reFlag.equals("false")) {
					msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
					msg.setSuccess("false");
				} else {
					msg.setMsg(reFlag);
					msg.setSuccess("false");
				}
				// } else if (saveFlag.substring(0, 5).equals("false")) {
				// msg.setMsg("调用房产接口数据保存失败："
				// + saveFlag.substring(6, saveFlag.length()));
				// msg.setSuccess("false");
				// } else {
				// msg.setMsg(saveFlag);
				// msg.setSuccess("false");
				// }
			}else if (xzqdm.contains("1302")) {
				//唐山130200
				 boolean boolFlag = false;
				 // 迁移后：抽取前先调用协同共享系统服务通过房产接口将数据存入共享交易库，后返回保存成功或者失败标志
				 String share_url = ConfigHelper.getNameByValue("URL_SHARE");
				 String htbh=casenum;
				 URL url = new URL("" + share_url +"sharezjk/savedatabyfcinterface/" + htbh );
				 URLConnection urlcon = url.openConnection();
				 BufferedReader bf = new BufferedReader(new
				 InputStreamReader(urlcon.getInputStream(), "UTF-8"));
				 String saveFlag = null;
				 saveFlag = bf.readLine();
    //				 本机环境测试时因不动产库为未处理当地存量数据，故接口抽取完毕后再修改单元，★记得更改GXJYK中H表Relationid★
				 if (saveFlag.equals("true")||saveFlag.equals("\"true\"")) {
	//				 抽取属性信息
					String reFlag = extractDataService.ExtractSXFromZJK(casenum, xmbh, true);
					if (reFlag.equals("true")) {
						 if (boolFlag) {
						msg.setMsg("全部数据读取成功");
						msg.setSuccess("true");
						 } else {
						 msg.setMsg("交易数据读取成功");
						 msg.setSuccess("true");
						 }
					} else if (reFlag.equals("warning")) {
						msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
						msg.setSuccess("false");
					} else if (reFlag.equals("false")) {
						msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
						msg.setSuccess("false");
					} else {
						msg.setMsg(reFlag);
						msg.setSuccess("false");
					}
			    } else if (saveFlag.substring(0, 5).equals("false")) { 
					 msg.setMsg("调用房产接口数据保存失败："+ saveFlag.substring(6, saveFlag.length()));
					 msg.setSuccess("false");
				} else {
					 msg.setMsg(saveFlag);
					 msg.setSuccess("false");
				}						 
			}else if (xzqdm.contains("4500") || sftycq == true) {
				// 通用版从中间库抽取数据，广西省厅测试
				String proinstid = null;
				proinstid = extractDataService.getProinstID(projectid);
				// 抽取属性
				String reFlag = extractDataForGXService.ExtractSXFromZJK(casenum, xmbh, true);
				// 抽取附件【三种附件抽取方式自动判断切换】
				boolean boolFlag = extractDataService.ExtractFJFromZJK(proinstid, casenum);
//				if(xzqdm.contains("2224")){
//					boolean boolFlag = extractDataService.ExtractFJFromZJK(proinstid, casenum);
//				}else{
//					boolean boolFlag = extractDataForGXService.ExtractFJFromZJK(proinstid, casenum);
//				}
				
				if (reFlag.equals("true")) {
					msg.setMsg("全部数据读取成功");
					msg.setSuccess("true");

				} else if (reFlag.equals("warning")) {
					msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
					msg.setSuccess("false");
				} else if (reFlag.equals("false")) {
					msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
					msg.setSuccess("false");
				} else if (reFlag.equals("nofwztorh")) {
					msg.setMsg("交易系统未推送该房产业务号对应房屋状态！</br>请联系交易信息中心!");
					msg.setSuccess("false");
				} else if (reFlag.substring(0, 7).equals("warning")) {
					// 该处可能原因是: 1、房产推送到共享交易库中的该户的房屋状态应该是现房2结果推送为1
					// 2、该房屋类型为预测户，但该户在户现状预表中未找到需要到权籍系统落宗
					msg.setMsg("房产推送以下房屋编码房屋状态错误</br>或该房屋未落宗：" + reFlag.substring(7, reFlag.length()) + "</br>请检查!");
					msg.setSuccess("false");
				}else if ("norelationid".equals(reFlag)){
			          msg.setMsg("relationid与房管关联不上，请手动受理");
			          msg.setSuccess("false");
		        }else if ("istoolong".equals(reFlag)){
		          msg.setMsg("权利人身份证过长，请核实！");
		          msg.setSuccess("false");
		        }else {
					msg.setMsg(reFlag);
					msg.setSuccess("false");
					}
				}else if (xzqdm.contains("5103")) {
					// 自贡市读取数据510300
					boolean boolFlag = false;
					// String proinstid = null;
					// proinstid=extractDataService.getProinstID(bmbm);
					// if (proinstid != null) {
					// // System.out.println("正在抽取附件...");
					// String file_Path = request.getSession().getServletContext()
					// .getRealPath("/");
					// // 抽取附件
					// boolFlag = extractDataService.ExtractFJFromZJK(
					// proinstid, htbh);
					// }
					// 抽取属性信息
					
					
					
					String reFlag = extractDataService.ExtractSXFromZJK(casenum, xmbh, true);
					if (reFlag.equals("true")) {
						if (boolFlag) {
							msg.setMsg("全部数据读取成功");
							msg.setSuccess("true");
						} else {
							msg.setMsg("交易数据读取成功");
							msg.setSuccess("true");
						}
					} else if (reFlag.equals("warning")) {
						msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
						msg.setSuccess("false");
					} else if (reFlag.equals("false")) {
						msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
						msg.setSuccess("false");
					} else if (reFlag.equals("nofwztorh")) {
						msg.setMsg("交易系统未推送该房产业务号对应房屋状态！</br>请联系交易信息中心!");
						msg.setSuccess("false");
					} else if (reFlag.substring(0, 7).equals("warning")) {
						msg.setMsg("以下房屋编码未落宗：" + reFlag.substring(7, reFlag.length()) + "</br>请落宗!");
						msg.setSuccess("false");
					} else {
						msg.setMsg(reFlag);
						msg.setSuccess("false");
					}
				}else if (xzqdm.contains("3606")) {
					// 鹰潭市读取数据360600
					String boolFlag = null;
					String proinstid = null;
					String gxxmbh = null;
					proinstid = extractDataService.getProinstID(projectid);
					/*
					 * if (proinstid != null) { String file_Path =
					 * request.getSession().getServletContext() .getRealPath("/");
					 * String sql = "casenum like '%" + htbh +
					 * "%'  and (GXLX !='99' or GXLX is null)"; List<GXJHXM> Gxjhxms
					 * = baseCommonDao.getDataList( GXJHXM.class, sql); if (Gxjhxms
					 * != null && Gxjhxms.size() > 0) { gxxmbh =
					 * Gxjhxms.get(0).getGxxmbh(); if (gxxmbh != null) { boolFlag =
					 * e_ExtractAttachment.abstractFJFromZJK( proinstid, gxxmbh,
					 * file_Path); } } }
					 */
					// 抽取属性信息
					String reFlag = q_QueryShareXxService.ExtractSXFromZJK(casenum, xmbh, true);
					if (reFlag.equals("warning")) {
						msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
						msg.setSuccess("false");
					} else if (reFlag.equals("false")) {
						msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
						msg.setSuccess("false");
					} else if (reFlag.equals("true")) {
						if (proinstid != null) {
	//						String file_Path = request.getSession().getServletContext().getRealPath("/");
							String sql = "casenum like '%" + casenum + "%'  and (GXLX !='99' or GXLX is null)";
							List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, sql);
							if (Gxjhxms != null && Gxjhxms.size() > 0) {
								gxxmbh = Gxjhxms.get(0).getGxxmbh();
								if (gxxmbh != null) {
									boolFlag = e_ExtractAttachment.abstractFJFromZJK(proinstid, gxxmbh, file_Path);
								}
							}
						}
						if (boolFlag.equals("true")) {
							msg.setMsg("全部数据读取成功");
							msg.setSuccess("true");
						} else {
							msg.setMsg("交易数据读取成功");
							msg.setSuccess("true");
						}
					} else {
						msg.setMsg(reFlag);
						msg.setSuccess("false");
					}
					/*
					 * if (reFlag.equals("true")) { if (boolFlag.equals("true")) {
					 * msg.setMsg("全部数据读取成功"); msg.setSuccess("true"); } else {
					 * msg.setMsg("交易数据读取成功"); msg.setSuccess("true"); } } else if
					 * (reFlag.equals("warning")) {
					 * msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
					 * msg.setSuccess("false"); } else if (reFlag.equals("false")) {
					 * msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
					 * msg.setSuccess("false"); } else { msg.setMsg(reFlag);
					 * msg.setSuccess("false"); }
					 */
				} else if (xzqdm.contains("6501") || sftycq == true) {
							// 乌鲁木齐
						String proinstid = null;
						proinstid = extractDataService.getProinstID(projectid);
						casenum+="&&"+djyyString;//拼接上登记单元
						// 抽取属性
						String reFlag = extractDataForWLMQService.ExtractSXFromZJK(casenum, xmbh, true);
						if (reFlag.equals("true")) {
								msg.setMsg("全部数据读取成功");
								msg.setSuccess("true");
							} else if (reFlag.equals("warning")) {
								msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
								msg.setSuccess("false");
							} else if (reFlag.equals("false")) {
								msg.setMsg("交易系统未成功推送数据，</br>请联系交易信息中心!");
								msg.setSuccess("false");
							} else if (reFlag.equals("nofwztorh")) {
								msg.setMsg("交易系统未推送该房产业务号对应房屋状态！</br>请联系交易信息中心!");
								msg.setSuccess("false");
							} else if (reFlag.equals("formalerror")) {
								msg.setMsg("房产业务号输入格式有误！</br>请检查后重新输入!");
								msg.setSuccess("false");
							} else if (reFlag.equals("noDCH")) {
								msg.setMsg("温馨提示您~请先做权籍调查~");
								msg.setSuccess("false");
							} else if (reFlag.equals("LCCW")) {
								msg.setMsg("办件流程选择错误");
								msg.setSuccess("false");
							}else if(!msg.equals("true")){
								msg.setMsg(reFlag);
								msg.setSuccess("false");
							} else if (reFlag.substring(0, 7).equals("warning")) {
								// 该处可能原因是: 1、房产推送到共享交易库中的该户的房屋状态应该是现房2结果推送为1
								// 2、该房屋类型为预测户，但该户在户现状预表中未找到需要到权籍系统落宗
								msg.setMsg("房产推送以下房屋编码房屋状态错误</br>或该房屋未落宗：" + reFlag.substring(7, reFlag.length()) + "</br>请检查!");
								msg.setSuccess("false");
							} else if (reFlag.equals("formalerror")) {
									msg.setMsg("房产业务号输入格式有误！</br>请检查后重新输入!");
									msg.setSuccess("false");
							} else {
								msg.setMsg(reFlag);
								msg.setSuccess("false");
							}
					}else if (xzqdm.contains("2202")) {
						// 吉林市读取数据220100
							boolean boolFlag = false;
							// 抽取属性信息
							String reFlag = extractDataForJLService.ExtractSXFromZJK(casenum, xmbh, true);
							if (reFlag.equals("true")) {
								if (boolFlag) {
									msg.setMsg("全部数据读取成功");
									msg.setSuccess("true");
								} else {
									msg.setMsg("交易数据读取成功");
									msg.setSuccess("true");
								}
							} else if (reFlag.equals("warning")) {
								msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
								msg.setSuccess("false");
							} else if (reFlag.equals("false")) {
								msg.setMsg("交易系统未成功推送数据或者你输入的业务号有误，</br>请联系交易信息中心!");
								msg.setSuccess("false");
							} else if (reFlag.equals("nofwztorh")) {
								msg.setMsg("交易系统未推送该房产业务号对应房屋状态！</br>请联系交易信息中心!");
								msg.setSuccess("false");
							} else if (reFlag.substring(0, 7).equals("warning")) {
								msg.setMsg("以下房屋编码未落宗：" + reFlag.substring(7, reFlag.length()) + "</br>请落宗!");
								msg.setSuccess("false");
							}else if (reFlag.substring(0, 1).equals("MD")) {
								msg.setMsg("" + reFlag.substring(1, reFlag.length()) + "</br>请联系管理员!");
								msg.setSuccess("false");
							} else {
								msg.setMsg(reFlag);
								msg.setSuccess("false");
							}
			}else if (xzqdm.contains("4503")||xzqdm.contains("4509")) {//桂林市玉林读取交易系统数据，根据合同号casenum读取
				Map<String, String> paraMap = new HashMap<String, String>();
				paraMap.put("fcjylx", djyyString);//不想扩展外部接口，暂时借用 djyy报错合同交易类型区分是存量房还是新建商品房
				msg = jydjythDataSwapService.readContract(casenum, xmbh,projectid,paraMap);  
			}else {
				flag = xmlService.getXMLFromXTGX(casenum, xmbh);
				if (flag) {
					msg.setMsg("读取成功");
					msg.setSuccess("true");
					YwLogUtil.addYwLog("读取合同成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
				} else {
					msg.setMsg("合同号有误!");
					msg.setSuccess("false");
					YwLogUtil.addYwLog("读取合同成功-合同号有误!", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
				}
			}
		} catch (IOException e) {
			msg.setMsg("号码有误!");
			msg.setSuccess("false");
			YwLogUtil.addYwLog("读取合同成功-号码有误!", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
			e.printStackTrace();
		} catch (Exception e) {
			msg.setMsg("号码有误!");
			msg.setSuccess("false");
			YwLogUtil.addYwLog("读取合同成功-号码有误!", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
			e.printStackTrace();
		}
		return msg;
	}

	

	/**
	 * 根据页面指定bljd推送数据，
	 * @作者 think
	 * @创建时间 2016年9月6日上午10:14:01
	 * @param projectid
	 * @param bljd
	 */
	public void Pushsingledata(String projectid,String bljd) {
		logger.info("推送页面指定推送被调用");
			projectid=projectid.replaceAll("，", ",");
			String[] projectidarray=projectid.split(",");
			for(int n=0;n<projectidarray.length;n++){
				String curprojectid=projectidarray[n];
				List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class, "project_id='" + curprojectid + "'or ywlsh='"+curprojectid+"'");
				if (xmxxList.size() > 0) {
					for (int i = 0; i < xmxxList.size(); i++) {
						zjkpushType(xmxxList.get(i), bljd);
					}
				}else {
			    	  this.logger.info("xmxx表中无值");
				 }
			}
		
	}

	/**
	 * 中间库方式推送
	 * 
	 * @作者 likun
	 * @创建时间 2016年2月18日下午4:28:23
	 * @param xmbh
	 * @param bljc,1受理转出；2登簿；3缮证；4归档；
	 * @throws SQLException
	 */
//	@Async("AsyncExecutor1")
	public synchronized void zjkpushType(  final BDCS_XMXX xmxx, final String bljc) {
		Runnable runnable = new Runnable() {
		@SuppressWarnings("unused")
		public  void run() {
		synchronized(xmxx){
		logger.info("开始推送");
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
		String bljd="";
		DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
		try {
			com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ	CommonDaoDJ2=(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ) SuperSpringContext.getContext().getBean("baseCommonDaoDJ");
			List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class,
					"WORKFLOWCODE='" + workflowcode + "'");
			//判断业务流程推送权利人名称 证件号是否加密
			String eNCRYPTION=mappings.get(0).getENCRYPTION();
			// 判断业务流程是否推送，PUSHTOZJK为1才推送
			if (mappings == null || mappings.size() == 0 || mappings.get(0).getPUSHTOZJK() == null|| !(mappings.get(0).getPUSHTOZJK().equals("1")||mappings.get(0).getPUSHTOZJK().equals("2"))) {
				// xmxx表记录本项目无需推送
				// xmxx.settSCS(-1);
				baseCommonDao.update(xmxx);
				baseCommonDao.flush();
				logger.info("该基准流程配置不推送或没有配置基准流程");
				return;
			}
			logger.info("mapping大小："+mappings.size());
			List<String> qltablenameList = new ArrayList<String>();
			List<String> qlsdfList = new ArrayList<String>();
			List<Integer> iPushBGQList = new ArrayList<Integer>();// 是否推送变更前
			String djxl = "0", djdl = null;
			String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
			String XZQHMC = ConfigHelper.getNameByValue("XZQHMC");
			// String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
			// 如果是泸州的，有些业务不推送
			// Boolean bPush = xzqdm.contains("5105") ? false : true;
			// 配置方式新方法推送
			String qllx = xmxx.getQLLX();
			String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
			if (bljc.equals("0")) {
				// logger.info("退件！删除中间库数据");
				// 删除案件，删除推送数据
				/**齐齐哈尔删件时要先从共享交易库把旧relationid取出来，维护现状层户relationid到原值后再删出中间库
				 * */
				if(XZQHDM.equals("230200")){
					DelMaintanceRelation(xmxx);
				}
				if(dataSwapImpEx.getFieldExist("GXDJK", "GXJHXM", "ISDELETE")){
					//不物理删除，打标记
					dataSwapImpEx.deleteEx(xmxx.getPROJECT_ID(),CommonDaoDJ2);
				}
				else{
					dataSwapImpEx.delete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
				}
				//遂宁市  退件时调用亿联接口告知对方
				if(XZQHDM.equals("510900")){
					SNRerurnFCdata(xmxx,bljc);
				}
			}
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
			
			if (djdys == null || djdys.size() == 0) {
				logger.info("登记单元个数为0");
				System.err.println("BDCS_DJDY_GZ无值"+xmbhFilter);
				return;
			}
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
			logger.info("不动产单元类型:"+djdys.get(0).getBDCDYLX()+",workflowcode"+workflowcode+",handlename:"+_handleClassName);
			// 推送到中间库,0不推送，1中间库模式推送（受理和登薄都推）,2MQ模式推送,3接口模式推送（受理和登薄都推）,4中间库模式（仅登薄推）,5接口模式（仅登薄推）,
			//6中间库模式（受理+登薄+缮证）,7接口模式（受理+登薄+缮证）,8中间库模式推送（受理和归档都推）,9接口模式推送（受理和归档都推）
			int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETOZJK"));
			if (zjk==4) {
				bljd="2";
			}else {
				bljd=bljc;
			}
			// 房产业务
			if (djdys.get(0).getBDCDYLX().equals(BDCDYLX.H.Value)|| djdys.get(0).getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
				// 获取推送参数
				System.out.println("--------------进到推送这里来了");
				String paras = getTSParamets(xmxx, djdys.get(0), _handleClassName, qllx, qltablenameList, qlsdfList,
						iPushBGQList,xmxx.getDJLX());
				System.out.println("看一下推送的数据："+paras.toString());
				String[] pStrings = paras.split(",");
				if (pStrings != null && pStrings.length > 0) {
					djxl = pStrings[0];// 登记小类
					if (pStrings.length > 1) {
						djdl = pStrings[1];// 登记大类
					}
				}
				
				if (djdl != null) {
					dataSwapImpEx.setDjdl(djdl);
				}
				dataSwapImpEx.setXzqdm(XZQHDM);
				dataSwapImpEx.setXzqmc(XZQHMC);
				//南宁受理转出的时候，给权利表的casenum赋值，防止手工选单元无casenum
				if (bljc.equals("1")&&"4501".equals(XZQHDM)) {
					
				}
				if (!bljc.equals("0")) {
					// 开始推送，多线程session
					Session session=CommonDaoDJ2.getCurrentSession();
					Connection conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
					int overlap = Integer.parseInt(ConfigHelper.getNameByValue("pushdataoverlap"));
					/**保证登簿后推送项目共享项目编号与受理转出相同 
					 * 在登簿环节取出relationid与之匹配的受理转出时的共享项目编号map
					 * 2016年8月31日 08:33:22 卜晓波
					 */
					String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
					Map<String,String> relationid2gxxmbh=new HashMap<String, String>();
					if(XZQHDM.equals("230200")){
						relationid2gxxmbh=GetRelationidfromSLZC(bljc,xmxx);
					}
					// logger.info("删除中间库重复数据");
					// 推送前先删除一下本项目数据（考虑有被驳回情况）
					if(XZQHDM.equals("230200") && bljc.equals("2")){
						dataSwapImpEx.QQHEdelete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}
					//吉林登簿时不删除原来的项目 2016年12月5日 21:41:37 卜晓波   泸州也不删,赤壁也不删除
					else if((XZQHDM.contains("2202")||XZQHDM.contains("5105")||XZQHDM.contains("421281"))&& bljc.equals("2")){
						
					}else if(XZQHDM.contains("2202") && bljc.equals("1") && xmxx.getDJLX().equals("800")){
						return;
					}else if(overlap==0){
						//不覆盖之前推送的数据,只打标记，不修改推送时间
						dataSwapImpEx.deleteEx2(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}else{
						dataSwapImpEx.delete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}
					logger.info("开始往中间库推送数据，业务流水号：" + xmxx.getYWLSH()+"。权利数："+qltablenameList.size());
					// 开始推送，多线程session
//					Session session=CommonDaoDJ2.getCurrentSession();
//					session.setFlushMode(FlushMode.MANUAL);
					//开启事务    
					Transaction ts=session.beginTransaction();
					for (int i = 0; i < qltablenameList.size(); i++) {
						if(qltablenameList.get(i).equals("XZDJ")){
							//限制登记权利保存在BDCS_DYXZ中，走单独推送方法  2016年7月22日 09:27:10  卜晓波
							dataSwapImpEx.pushXZDJToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
									iPushBGQList.get(i), djxl,relationid2gxxmbh);
						}
						//房屋分割时单独走推送方法，所有变更前户应该找被分割户
						else if(_handleClassName.equals("BGDJHandler")){
							logger.info("分割业务推送方法准备进入");
							dataSwapImpEx.BGpushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
									iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
						}else{ 
							//齐齐哈尔此处验证由房产发起的业务现在受理转出环节不推送
							if(XZQHDM.equals("230200")){
								boolean IsPush=true;
								if(bljc.equals("1")){
									IsPush=CheckIsPush(xmxx);
								}
								if(IsPush==true){
									dataSwapImpEx.pushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
								}
							}else{
								logger.info("准备进入");
								dataSwapImpEx.pushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljd, qlsdfList.get(i),
										iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
							}
//								dataSwapImpEx.pushToGXDJK(baseCommonDao, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
//										iPushBGQList.get(i), djxl,relationid2gxxmbh);
							}
						}
					String result = "";
					// 是否推送附件，0不推送，1受理转出推送，2登薄推送，3都推送（覆盖）
					String pushString = ConfigHelper.getNameByValue("PUSHFJTOFC");
					if (pushString != null && !pushString.equals("")) {
						int pushfj = Integer.parseInt(pushString);
						if (pushfj ==3||(pushfj ==1&&bljc.equals("1"))||(pushfj ==2&&bljc.equals("2"))) {
							logger.info("开始推送附件");
							System.out.println("推送附件开始");
							dataSwapImpEx.pushFJToGXDJK(baseCommonDao,xmxx,session,CommonDaoDJ2,conn);
							System.out.println("推送附件结束");
							
						}
					}
					double startTime = System.currentTimeMillis();
//					ts.commit();
					CommonDaoDJ2.flush();
					conn.close();
//					session.close();
					System.out.println("本次推送事务提交入库用时："+(System.currentTimeMillis() - startTime)+"（毫秒）");
					System.out.println("连接关闭");
						//西宁    推送完成后调用三维服务告知本次推送业务
						if(XZQHDM.equals("630100")){
							logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！登记系统~房屋业务~登簿开始调用share接口！！！");
							List<BDCS_QL_GZ> qlList=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmxx.getId()+"'");
//							if(qlList!=null&&qlList.size()>0&&qlList.get(0).getCASENUM()!=null&&!qlList.get(0).getCASENUM().equals("")){
								try{
									String share_url = ConfigHelper.getNameByValue("URL_SHARE");
//									URL url = new URL(share_url + "sharezjk/xnsendmsgtofc/" + qlList.get(0).getCASENUM());
									URL url = new URL(share_url + "sharezjk/xnsendmsgtofc/" + xmxx.getPROJECT_ID());
									URLConnection urlcon = url.openConnection();
									BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
									String inputLine = null;
									inputLine = bf.readLine();
									StringBuilder json = new StringBuilder();
									json = json.append(inputLine);
									JSONObject flag = JSONObject.fromObject(json.toString());
									logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！西宁调用三维服务成功了啊！！！！！！(*^__^*) ");
								}catch(Exception e){
									System.out.println("啊啊啊啊啊啊啊啊啊我是错误啊！！！！西宁调用三维服务报错了啊！！！！！！"+e.toString());
									logger.error(e.toString());
								}
							}
					//遂宁市  受理、登簿环节调用亿联接口告知对方
					System.out.println(XZQHDM);
					if(XZQHDM.equals("510900")){
						SNRerurnFCdata(xmxx,bljc);
					}
//					String result = "";
//					// 是否推送附件，0不推送，1受理转出推送，2登薄推送，3都推送（覆盖）
//					if (pushString != null && !pushString.equals("")) {
//						int pushfj = Integer.parseInt(pushString);
//						if (pushfj ==3||(pushfj ==1&&bljc.equals("1"))||(pushfj ==2&&bljc.equals("2"))) {
//							logger.info("开始推送附件");
//							System.out.println("推送附件开始");
//							dataSwapImpEx.pushFJToGXDJK(baseCommonDao,xmxx);
//							System.out.println("推送附件结束");
//						}
//					}
					
					String casenum = dataSwapImpEx.getCasenum();
					if (bljc.equals("2")) {
						if("420900".equals(XZQHDM)||"420902".equals(XZQHDM)){
							dataSwapImpEx.updategxxmbh(xmxx.getPROJECT_ID());
						}
						// 登薄调用回写房产库接口
						/* 南宁市回调房产接口和三维接口 */
						if ("4501".equals(XZQHDM)) {
							result = callfcInter(xmxx.getPROJECT_ID());
							threedfcInter(xmxx.getPROJECT_ID());
						}
						/*
						 * * 2016年6月21日 10:02:39
						 * 柳州此处调用房产回写接口，将组织结构代码、房产业务号、标准jsonobject回写房产
						 * GXDJK数据查询接口：http://localhost:8080/share/app/sharezjk/
						 * returndata/casenum
						 */
						else if (XZQHDM.equals("450200")) {
							RerurnFCdata(casenum, xmxx, bljc);
						}else if (XZQHDM.contains("1302")) {
							RerurnFCdatas(casenum, xmxx, bljc);
						}else if (XZQHDM.contains("4500")) {
							//广西省厅测试
//							result=RerurnFCdata2(casenum, xmxx, bljc);
						}else{
							if(zjk==3||zjk==5||zjk==7){
								result=RerurnFCdata2(casenum, xmxx, bljc);
							}
						}
					}else if (bljc.equals("1")) {
						//受理转出
						if(zjk==3||zjk==7||zjk==9){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}else if (bljc.equals("3")) {
						//缮证
						if(zjk==7){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}else if (bljc.equals("4")) {
						//归档
						if(zjk==9){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}
					// 如果推送失败，记录在bdck的失败表中，如果成功从pushfaile表删除
					if (!dataSwapImpEx.allSuccess || result.contains("失败")) {
						String sblxString="2";//调用接口失败
						if (!dataSwapImpEx.allSuccess){
							sblxString="1";//推送中间库失败
						}
					     //保存到bdck的fail表
						if(tableExist("BDCK", "PUSHFAIL")){
							saveFailToBDCK(xmxx, dataSwapImpEx.getCasenum(),bljc,sblxString,dataSwapImpEx.FailCause);
						}
					}else {
						String condition="project_id='"+xmxx.getPROJECT_ID()+"'";			
						   baseCommonDao.deleteEntitysByHql(PUSHFAIL.class, condition);
					}
					// 判断是否存在fail表
//					if (dataSwapImpEx.existFailTable()) {
//						// 如果推送失败，记录在失败表中，回写房产库失败也记录
//						if (!dataSwapImpEx.allSuccess || !dataSwapImpEx.testSingleExist(xmxx.getPROJECT_ID())
//								|| result.contains("失败")) {
//							boolean b = dataSwapImpEx.saveFailData(xmxx, bljc);
//							// 如果推送fail表成功，从失败表中删掉
//							if (b) {
//								dataSwapImpEx.deleteFailRecord(xmxx.getPROJECT_ID());
//							}
//							// 登薄阶段
//							if (bljc.equals("2")) {
//								// xmxx表记录本项目已推送
//								 xmxx.settSCS(0);
//								baseCommonDao.update(xmxx);
//								baseCommonDao.flush();
//							}
//						} else {
//							// 如果推送成功，从失败记录表删除
//							dataSwapImpEx.deleteFailRecord(xmxx.getPROJECT_ID());
//							// 登薄阶段
//							if (bljc.equals("2")) {
//								// xmxx表记录本项目已推送
//								 xmxx.settSCS(1);
//								baseCommonDao.update(xmxx);
//								baseCommonDao.flush();
//							}
//						}
//
//					}
					// logger.info("推送完成！");
				}
				try {
					if(dataSwapImpEx.getJyConnection()!=null&&dataSwapImpEx.getJyConnection().isClosed()!=true){
						dataSwapImpEx.getJyConnection().close();
						logger.info("GXDJK连接正常关闭");
					}
				} catch (SQLException e) {
					logger.info("GXDJK连接关闭出错");
				}
			}
			// 土地业务
			else if (djdys.get(0).getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)|| djdys.get(0).getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				logger.info("进入土地数据推送");
				// 暂时南宁、西宁推送土地业务
				if ("4501".equals(XZQHDM)||XZQHDM.contains("6301")||zjk == 21||zjk == 24) {
					// 获取推送参数
					String paras = getTSParamets(xmxx, djdys.get(0), _handleClassName, qllx, qltablenameList, qlsdfList,
							iPushBGQList,xmxx.getDJLX());
					String[] pStrings = paras.split(",");
					if (pStrings != null && pStrings.length > 0) {
						djxl = pStrings[0];// 登记小类
						if (pStrings.length > 1) {
							djdl = pStrings[1];// 登记大类
						}
					}
					if (bljc.equals("0")) {
						 logger.info("退件！删除中间库数据");
						dataSwapImpEx.delete_tddj(xmxx.getPROJECT_ID());
					} else {
						 logger.info("删除中间库重复数据");
						// 推送前先删除一下本项目数据（考虑有被驳回情况）
						dataSwapImpEx.delete_tddj(xmxx.getPROJECT_ID());
						for (int i = 0; i < qltablenameList.size(); i++) {
							dataSwapImpEx.pushToTDDJK(baseCommonDao, xmxx, qltablenameList.get(i), bljc,
									qlsdfList.get(i), iPushBGQList.get(i), djxl);
						}
						//西宁    土地业务推送完成后调用三维服务告知本次推送业务
						
						if(XZQHDM.equals("630100")){
							logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！登记系统~土地业务~登簿开始调用share接口！！！");
							List<BDCS_QL_GZ> qlList=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmxx.getId()+"'");
//							if(qlList!=null&&qlList.size()>0&&qlList.get(0).getCASENUM()!=null&&!qlList.get(0).getCASENUM().equals("")){
								try{
									String share_url = ConfigHelper.getNameByValue("URL_SHARE");
//									URL url = new URL(share_url + "sharezjk/xnsendmsgtofc/" + qlList.get(0).getCASENUM());
									URL url = new URL(share_url + "sharezjk/xnTDsendmsgtofc/" + xmxx.getPROJECT_ID());
									URLConnection urlcon = url.openConnection();
									BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
									String inputLine = null;
									inputLine = bf.readLine();
									StringBuilder json = new StringBuilder();
									json = json.append(inputLine);
									JSONObject flag = JSONObject.fromObject(json.toString());
									logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！西宁调用三维服务成功了啊！！！！！！(*^__^*) ");
								}catch(Exception e){
									System.out.println("啊啊啊啊啊啊啊啊啊我是错误啊！！！！西宁调用三维服务报错了啊！！！！！！"+e.toString());
									logger.error(e.toString());
								}
//							}
						}
					}
						try {
								dataSwapImpEx.getTddjConnection().close();
							} catch (SQLException e) {
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage() + " 流程编码：" + workflowcode +",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljc+"");
				//保存到bdck的fail表
				if(tableExist("BDCK", "PUSHFAIL")){
					saveFailToBDCK(xmxx, "",bljc,"1",e.toString());
						}
					}finally {
						
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
		
	}

	//组合流程自动推送会重复推送
	public synchronized void zjkpushType3(  final BDCS_XMXX xmxx, final String bljc) {
		if (bljc.equals("2")) {
		Runnable runnable = new Runnable() {
		@SuppressWarnings("unused")
		public  void run() {
		synchronized(xmxx){
		logger.info("开始推送");
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
		String bljd="";
		DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
		try {
			com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ	CommonDaoDJ2=(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ) SuperSpringContext.getContext().getBean("baseCommonDaoDJ");
			List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class,
					"WORKFLOWCODE='" + workflowcode + "'");
			//判断业务流程推送权利人名称 证件号是否加密
			String eNCRYPTION=mappings.get(0).getENCRYPTION();
			// 判断业务流程是否推送，PUSHTOZJK为1才推送
			if (mappings == null || mappings.size() == 0 || mappings.get(0).getPUSHTOZJK() == null|| !(mappings.get(0).getPUSHTOZJK().equals("1")||mappings.get(0).getPUSHTOZJK().equals("2"))) {
				// xmxx表记录本项目无需推送
				// xmxx.settSCS(-1);
				baseCommonDao.update(xmxx);
				baseCommonDao.flush();
				logger.info("该基准流程配置不推送或没有配置基准流程");
				return;
			}
			logger.info("mapping大小："+mappings.size());
			List<String> qltablenameList = new ArrayList<String>();
			List<String> qlsdfList = new ArrayList<String>();
			List<Integer> iPushBGQList = new ArrayList<Integer>();// 是否推送变更前
			String djxl = "0", djdl = null;
			String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
			String XZQHMC = ConfigHelper.getNameByValue("XZQHMC");
			// String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
			// 如果是泸州的，有些业务不推送
			// Boolean bPush = xzqdm.contains("5105") ? false : true;
			// 配置方式新方法推送
			String qllx = xmxx.getQLLX();
			String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
			if (bljc.equals("0")) {
				// logger.info("退件！删除中间库数据");
				// 删除案件，删除推送数据
				/**齐齐哈尔删件时要先从共享交易库把旧relationid取出来，维护现状层户relationid到原值后再删出中间库
				 * */
				if(XZQHDM.equals("230200")){
					DelMaintanceRelation(xmxx);
				}
				if(dataSwapImpEx.getFieldExist("GXDJK", "GXJHXM", "ISDELETE")){
					//不物理删除，打标记
					dataSwapImpEx.deleteEx(xmxx.getPROJECT_ID(),CommonDaoDJ2);
				}
				else{
					dataSwapImpEx.delete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
				}
				//遂宁市  退件时调用亿联接口告知对方
				if(XZQHDM.equals("510900")){
					SNRerurnFCdata(xmxx,bljc);
				}
			}
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
			
			if (djdys == null || djdys.size() == 0) {
				logger.info("登记单元个数为0");
				System.err.println("BDCS_DJDY_GZ无值"+xmbhFilter);
				return;
			}
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
			logger.info("不动产单元类型:"+djdys.get(0).getBDCDYLX()+",workflowcode"+workflowcode+",handlename:"+_handleClassName);
			// 推送到中间库,0不推送，1中间库模式推送（受理和登薄都推）,2MQ模式推送,3接口模式推送（受理和登薄都推）,4中间库模式（仅登薄推）,5接口模式（仅登薄推）,
			//6中间库模式（受理+登薄+缮证）,7接口模式（受理+登薄+缮证）,8中间库模式推送（受理和归档都推）,9接口模式推送（受理和归档都推）
			int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETOZJK"));
			if (zjk==4) {
				bljd="2";
			}else {
				bljd=bljc;
			}
			// 房产业务
			if (djdys.get(0).getBDCDYLX().equals(BDCDYLX.H.Value)|| djdys.get(0).getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
				// 获取推送参数
				System.out.println("--------------进到推送这里来了");
				String paras = getTSParamets(xmxx, djdys.get(0), _handleClassName, qllx, qltablenameList, qlsdfList,
						iPushBGQList,xmxx.getDJLX());
				System.out.println("看一下推送的数据："+paras.toString());
				String[] pStrings = paras.split(",");
				if (pStrings != null && pStrings.length > 0) {
					djxl = pStrings[0];// 登记小类
					if (pStrings.length > 1) {
						djdl = pStrings[1];// 登记大类
					}
				}
				
				if (djdl != null) {
					dataSwapImpEx.setDjdl(djdl);
				}
				dataSwapImpEx.setXzqdm(XZQHDM);
				dataSwapImpEx.setXzqmc(XZQHMC);
				//南宁受理转出的时候，给权利表的casenum赋值，防止手工选单元无casenum
				if (bljc.equals("1")&&"4501".equals(XZQHDM)) {
					
				}
				if (!bljc.equals("0")) {
					// 开始推送，多线程session
					Session session=CommonDaoDJ2.getCurrentSession();
					Connection conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
					int overlap = Integer.parseInt(ConfigHelper.getNameByValue("pushdataoverlap"));
					/**保证登簿后推送项目共享项目编号与受理转出相同 
					 * 在登簿环节取出relationid与之匹配的受理转出时的共享项目编号map
					 * 2016年8月31日 08:33:22 卜晓波
					 */
					String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
					Map<String,String> relationid2gxxmbh=new HashMap<String, String>();
					if(XZQHDM.equals("230200")){
						relationid2gxxmbh=GetRelationidfromSLZC(bljc,xmxx);
					}
					// logger.info("删除中间库重复数据");
					// 推送前先删除一下本项目数据（考虑有被驳回情况）
					if(XZQHDM.equals("230200") && bljc.equals("2")){
						dataSwapImpEx.QQHEdelete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}
					//吉林登簿时不删除原来的项目 2016年12月5日 21:41:37 卜晓波   泸州也不删,赤壁也不删除
					else if((XZQHDM.contains("2202")||XZQHDM.contains("5105")||XZQHDM.contains("421281"))&& bljc.equals("2")){
						
					}else if(XZQHDM.contains("2202") && bljc.equals("1") && xmxx.getDJLX().equals("800")){
						return;
					}else if(overlap==0){
						//不覆盖之前推送的数据,只打标记，不修改推送时间
						dataSwapImpEx.deleteEx2(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}else{
						dataSwapImpEx.delete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}
					logger.info("开始往中间库推送数据，业务流水号：" + xmxx.getYWLSH()+"。权利数："+qltablenameList.size());
					// 开始推送，多线程session
//					Session session=CommonDaoDJ2.getCurrentSession();
//					session.setFlushMode(FlushMode.MANUAL);
					//开启事务    
					Transaction ts=session.beginTransaction();
					for (int i = 0; i < qltablenameList.size(); i++) {
						if(qltablenameList.get(i).equals("XZDJ")){
							//限制登记权利保存在BDCS_DYXZ中，走单独推送方法  2016年7月22日 09:27:10  卜晓波
							dataSwapImpEx.pushXZDJToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
									iPushBGQList.get(i), djxl,relationid2gxxmbh);
						}
						//房屋分割时单独走推送方法，所有变更前户应该找被分割户
						else if(_handleClassName.equals("BGDJHandler")){
							logger.info("分割业务推送方法准备进入");
							dataSwapImpEx.BGpushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
									iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
						}else{ 
							//齐齐哈尔此处验证由房产发起的业务现在受理转出环节不推送
							if(XZQHDM.equals("230200")){
								boolean IsPush=true;
								if(bljc.equals("1")){
									IsPush=CheckIsPush(xmxx);
								}
								if(IsPush==true){
									dataSwapImpEx.pushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
								}
							}else{
								logger.info("准备进入");
								dataSwapImpEx.pushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljd, qlsdfList.get(i),
										iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
							}
//								dataSwapImpEx.pushToGXDJK(baseCommonDao, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
//										iPushBGQList.get(i), djxl,relationid2gxxmbh);
							}
						}
					String result = "";
					// 是否推送附件，0不推送，1受理转出推送，2登薄推送，3都推送（覆盖）
					String pushString = ConfigHelper.getNameByValue("PUSHFJTOFC");
					if (pushString != null && !pushString.equals("")) {
						int pushfj = Integer.parseInt(pushString);
						if (pushfj ==3||(pushfj ==1&&bljc.equals("1"))||(pushfj ==2&&bljc.equals("2"))) {
							logger.info("开始推送附件");
							System.out.println("推送附件开始");
							dataSwapImpEx.pushFJToGXDJK(baseCommonDao,xmxx,session,CommonDaoDJ2,conn);
							System.out.println("推送附件结束");
							
						}
					}
					double startTime = System.currentTimeMillis();
//					ts.commit();
					CommonDaoDJ2.flush();
					conn.close();
//					session.close();
					System.out.println("本次推送事务提交入库用时："+(System.currentTimeMillis() - startTime)+"（毫秒）");
					System.out.println("连接关闭");
						//西宁    推送完成后调用三维服务告知本次推送业务
						if(XZQHDM.equals("630100")){
							logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！登记系统~房屋业务~登簿开始调用share接口！！！");
							List<BDCS_QL_GZ> qlList=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmxx.getId()+"'");
//							if(qlList!=null&&qlList.size()>0&&qlList.get(0).getCASENUM()!=null&&!qlList.get(0).getCASENUM().equals("")){
								try{
									String share_url = ConfigHelper.getNameByValue("URL_SHARE");
//									URL url = new URL(share_url + "sharezjk/xnsendmsgtofc/" + qlList.get(0).getCASENUM());
									URL url = new URL(share_url + "sharezjk/xnsendmsgtofc/" + xmxx.getPROJECT_ID());
									URLConnection urlcon = url.openConnection();
									BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
									String inputLine = null;
									inputLine = bf.readLine();
									StringBuilder json = new StringBuilder();
									json = json.append(inputLine);
									JSONObject flag = JSONObject.fromObject(json.toString());
									logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！西宁调用三维服务成功了啊！！！！！！(*^__^*) ");
								}catch(Exception e){
									System.out.println("啊啊啊啊啊啊啊啊啊我是错误啊！！！！西宁调用三维服务报错了啊！！！！！！"+e.toString());
									logger.error(e.toString());
								}
							}
					//遂宁市  受理、登簿环节调用亿联接口告知对方
					System.out.println(XZQHDM);
					if(XZQHDM.equals("510900")){
						SNRerurnFCdata(xmxx,bljc);
					}
//					String result = "";
//					// 是否推送附件，0不推送，1受理转出推送，2登薄推送，3都推送（覆盖）
//					if (pushString != null && !pushString.equals("")) {
//						int pushfj = Integer.parseInt(pushString);
//						if (pushfj ==3||(pushfj ==1&&bljc.equals("1"))||(pushfj ==2&&bljc.equals("2"))) {
//							logger.info("开始推送附件");
//							System.out.println("推送附件开始");
//							dataSwapImpEx.pushFJToGXDJK(baseCommonDao,xmxx);
//							System.out.println("推送附件结束");
//						}
//					}
					
					String casenum = dataSwapImpEx.getCasenum();
					if (bljc.equals("2")) {
						if("420900".equals(XZQHDM)||"420902".equals(XZQHDM)){
							dataSwapImpEx.updategxxmbh(xmxx.getPROJECT_ID());
						}
						// 登薄调用回写房产库接口
						/* 南宁市回调房产接口和三维接口 */
						if ("4501".equals(XZQHDM)) {
							result = callfcInter(xmxx.getPROJECT_ID());
							threedfcInter(xmxx.getPROJECT_ID());
						}
						/*
						 * * 2016年6月21日 10:02:39
						 * 柳州此处调用房产回写接口，将组织结构代码、房产业务号、标准jsonobject回写房产
						 * GXDJK数据查询接口：http://localhost:8080/share/app/sharezjk/
						 * returndata/casenum
						 */
						else if (XZQHDM.equals("450200")) {
							RerurnFCdata(casenum, xmxx, bljc);
						}else if (XZQHDM.contains("1302")) {
							RerurnFCdatas(casenum, xmxx, bljc);
						}else if (XZQHDM.contains("4500")) {
							//广西省厅测试
//							result=RerurnFCdata2(casenum, xmxx, bljc);
						}else{
							if(zjk==3||zjk==5||zjk==7){
								result=RerurnFCdata2(casenum, xmxx, bljc);
							}
						}
					}else if (bljc.equals("1")) {
						//受理转出
						if(zjk==3||zjk==7||zjk==9){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}else if (bljc.equals("3")) {
						//缮证
						if(zjk==7){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}else if (bljc.equals("4")) {
						//归档
						if(zjk==9){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}
					// 如果推送失败，记录在bdck的失败表中，如果成功从pushfaile表删除
					if (!dataSwapImpEx.allSuccess || result.contains("失败")) {
						String sblxString="2";//调用接口失败
						if (!dataSwapImpEx.allSuccess){
							sblxString="1";//推送中间库失败
						}
					     //保存到bdck的fail表
						if(tableExist("BDCK", "PUSHFAIL")){
							saveFailToBDCK(xmxx, dataSwapImpEx.getCasenum(),bljc,sblxString,dataSwapImpEx.FailCause);
						}
					}else {
						String condition="project_id='"+xmxx.getPROJECT_ID()+"'";			
						   baseCommonDao.deleteEntitysByHql(PUSHFAIL.class, condition);
					}
					// 判断是否存在fail表
//					if (dataSwapImpEx.existFailTable()) {
//						// 如果推送失败，记录在失败表中，回写房产库失败也记录
//						if (!dataSwapImpEx.allSuccess || !dataSwapImpEx.testSingleExist(xmxx.getPROJECT_ID())
//								|| result.contains("失败")) {
//							boolean b = dataSwapImpEx.saveFailData(xmxx, bljc);
//							// 如果推送fail表成功，从失败表中删掉
//							if (b) {
//								dataSwapImpEx.deleteFailRecord(xmxx.getPROJECT_ID());
//							}
//							// 登薄阶段
//							if (bljc.equals("2")) {
//								// xmxx表记录本项目已推送
//								 xmxx.settSCS(0);
//								baseCommonDao.update(xmxx);
//								baseCommonDao.flush();
//							}
//						} else {
//							// 如果推送成功，从失败记录表删除
//							dataSwapImpEx.deleteFailRecord(xmxx.getPROJECT_ID());
//							// 登薄阶段
//							if (bljc.equals("2")) {
//								// xmxx表记录本项目已推送
//								 xmxx.settSCS(1);
//								baseCommonDao.update(xmxx);
//								baseCommonDao.flush();
//							}
//						}
//
//					}
					// logger.info("推送完成！");
				}
				try {
					if(dataSwapImpEx.getJyConnection()!=null&&dataSwapImpEx.getJyConnection().isClosed()!=true){
						dataSwapImpEx.getJyConnection().close();
						logger.info("GXDJK连接正常关闭");
					}
				} catch (SQLException e) {
					logger.info("GXDJK连接关闭出错");
				}
			}
			// 土地业务
			else if (djdys.get(0).getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)|| djdys.get(0).getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				logger.info("进入土地数据推送");
				// 暂时南宁、西宁推送土地业务
				if ("4501".equals(XZQHDM)||XZQHDM.contains("6301")||zjk == 21||zjk == 24) {
					// 获取推送参数
					String paras = getTSParamets(xmxx, djdys.get(0), _handleClassName, qllx, qltablenameList, qlsdfList,
							iPushBGQList,xmxx.getDJLX());
					String[] pStrings = paras.split(",");
					if (pStrings != null && pStrings.length > 0) {
						djxl = pStrings[0];// 登记小类
						if (pStrings.length > 1) {
							djdl = pStrings[1];// 登记大类
						}
					}
					if (bljc.equals("0")) {
						 logger.info("退件！删除中间库数据");
						dataSwapImpEx.delete_tddj(xmxx.getPROJECT_ID());
					} else {
						 logger.info("删除中间库重复数据");
						// 推送前先删除一下本项目数据（考虑有被驳回情况）
						dataSwapImpEx.delete_tddj(xmxx.getPROJECT_ID());
						for (int i = 0; i < qltablenameList.size(); i++) {
							dataSwapImpEx.pushToTDDJK(baseCommonDao, xmxx, qltablenameList.get(i), bljc,
									qlsdfList.get(i), iPushBGQList.get(i), djxl);
						}
						//西宁    土地业务推送完成后调用三维服务告知本次推送业务
						
						if(XZQHDM.equals("630100")){
							logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！登记系统~土地业务~登簿开始调用share接口！！！");
							List<BDCS_QL_GZ> qlList=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmxx.getId()+"'");
//							if(qlList!=null&&qlList.size()>0&&qlList.get(0).getCASENUM()!=null&&!qlList.get(0).getCASENUM().equals("")){
								try{
									String share_url = ConfigHelper.getNameByValue("URL_SHARE");
//									URL url = new URL(share_url + "sharezjk/xnsendmsgtofc/" + qlList.get(0).getCASENUM());
									URL url = new URL(share_url + "sharezjk/xnTDsendmsgtofc/" + xmxx.getPROJECT_ID());
									URLConnection urlcon = url.openConnection();
									BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
									String inputLine = null;
									inputLine = bf.readLine();
									StringBuilder json = new StringBuilder();
									json = json.append(inputLine);
									JSONObject flag = JSONObject.fromObject(json.toString());
									logger.info("啊啊啊啊啊啊啊啊啊我是提示啊！！！！西宁调用三维服务成功了啊！！！！！！(*^__^*) ");
								}catch(Exception e){
									System.out.println("啊啊啊啊啊啊啊啊啊我是错误啊！！！！西宁调用三维服务报错了啊！！！！！！"+e.toString());
									logger.error(e.toString());
								}
//							}
						}
					}
						try {
								dataSwapImpEx.getTddjConnection().close();
							} catch (SQLException e) {
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage() + " 流程编码：" + workflowcode +",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljc+"");
				//保存到bdck的fail表
				if(tableExist("BDCK", "PUSHFAIL")){
					saveFailToBDCK(xmxx, "",bljc,"1",e.toString());
						}
					}finally {
						
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
		
	}
	}
	/**
	 * 在登簿环节推送时，如果受理转出环节已经推送过一条记录，则通过project_id取出对应的共享项目编号和relationid，供登簿推送共享交换项目保证共享项目编号一致使用
	 * */
	private Map<String, String> GetRelationidfromSLZC(String bljc,BDCS_XMXX xmxx) throws SQLException {
		Map<String,String> relationid2gxxmbh=new HashMap<String,String>();
		if(bljc.equals("2")){
//			Connection jyConnection = null;
//			try {
//				if (jyConnection == null || jyConnection.isClosed()) {
//					jyConnection = JH_DBHelper.getConnect_gxdjk();
//				}
//			} catch (SQLException e1) {
//			}
			
			String prjId = xmxx.getPROJECT_ID();
			List<Gxjhxm_dj> gx = CommonDaoDJ.getDataList(Gxjhxm_dj.class, "PROJECT_ID = '"+prjId+"'");
			if(gx.size()>0){
				for (Gxjhxm_dj gj : gx) {
					String relationid="";
					List<H_dj> gh = CommonDaoDJ.getDataList(H_dj.class, " GXXMBH = '"+gj.getGxxmbh()+"'");
					if(gh.size()>0){
						for (H_dj hj : gh) {
							relationid = hj.getRelationid();
						}
					}
					relationid2gxxmbh.put(relationid, gj.getGxxmbh());
				}
			}
//			String sql="select GXXMBH from gxdjk.gxjhxm where PROJECT_ID = '"+prjId+"'";
//			PreparedStatement pstmt = jyConnection.prepareStatement(sql);
//			ResultSet gxxmbhrst = pstmt.executeQuery();
//			if (gxxmbhrst != null ) {
//				try{
//					while (gxxmbhrst.next()) {
//						String gxxmbh=gxxmbhrst.getString("GXXMBH");
//						String qllx=gxxmbhrst.getString("QLLX");
//						String sql1="select RELATIONID from gxdjk.h where GXXMBH = '"+gxxmbh+"'";
//						PreparedStatement pstmt2 = jyConnection.prepareStatement(sql1);
//						ResultSet relationidrst = pstmt2.executeQuery();
//						String relationid="";
//						if (relationidrst != null) {
//							while (relationidrst.next()) {
//								relationid = relationidrst.getString("RELATIONID");
//							}
//							pstmt2.close();
//							relationidrst.close();
//						}
						//考虑到并案业务，relationid相同，加上权利类型，用逗号隔开
//						relationid2gxxmbh.put(relationid, gxxmbh);
//					}
//				}catch(Exception ex){}
                
//				pstmt.close();
//				gxxmbhrst.close();
//			}
			
			if(relationid2gxxmbh.size()==0){
				relationid2gxxmbh.put("SLZCNoPush", "1");
			}
		}
		return relationid2gxxmbh;
	}
	

	/**
	 * 齐齐哈尔检查由房产发起的业务在受理环节不推送
	 * 2016年9月2日 22:54:22 卜晓波
	 * @throws SQLException 
	 * */
	private boolean CheckIsPush(BDCS_XMXX xmxx) throws SQLException {
		boolean IsPush=true;
		List<BDCS_QL_GZ> ql_gzs = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmxx.getId() + "'");
		String DJDL=xmxx.getDJLX();
		if(DJDL!=null && (DJDL.equals("400")||DJDL.equals("500"))){
			//齐齐哈尔注销受理转出全推
			return IsPush=true;
		}
		if(ql_gzs!=null && ql_gzs.size()>0){
			BDCS_QL_GZ ql_gz=ql_gzs.get(0);
			String casenum=ql_gz.getCASENUM();
			Connection jyConnection = null;
			try {
				if (jyConnection == null || jyConnection.isClosed()) {
					jyConnection = JH_DBHelper.getConnect_jy();
				}
			} catch (SQLException e1) {
			}
			String sql="select CASENUM from gxjyk.gxjhxm where CASENUM = '"+casenum+"'";
			PreparedStatement pstmt = jyConnection.prepareStatement(sql);
			ResultSet gxxmbhrst = pstmt.executeQuery();
//			ResultSet gxxmbhrst=JH_DBHelper.excuteQuery(jyConnection, sql);
			if (gxxmbhrst != null) {
				while (gxxmbhrst.next()) {
						IsPush=false;
						pstmt.close();
						gxxmbhrst.close();
						return IsPush;
				}
			}
		}
		return IsPush;
	}

	/**
	 * 获取权利表名称、权利设定方式等推送参数
	 * 
	 * @作者 李堃
	 * @创建时间 2016年5月18日下午3:07:50
	 * @param _handleClassName
	 * @param qllx
	 * @param qltablenameList
	 * @param qlsdfList
	 * @param iPushBGQList
	 * @param djdl 
	 * @return
	 */
	private String getTSParamets(BDCS_XMXX xmxx,BDCS_DJDY_GZ djdy_GZ, String _handleClassName, String qllx,
			List<String> qltablenameList, List<String> qlsdfList, List<Integer> iPushBGQList, String djlx) {
		String djxl = "0", djdl = null;
		if (_handleClassName.contains("DYBGDJHandler")) {
			if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) && qllx.equals(QLLX.GYJSYDSHYQ_FWSYQ.Value) && djlx.equals(DJLX.YGDJ.Value)) {
				//预告登记
				qltablenameList.add("YGDJ");
			}else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) && qllx.equals(QLLX.QTQL.Value) && djlx.equals(DJLX.YGDJ.Value)){
				//现房转移预告更正登记
				qltablenameList.add("YGDJ");
			}else {
				// 抵押更正登记
				qltablenameList.add("DYAQ");
			}
			qlsdfList.add("3");
			iPushBGQList.add(1);
		} else if (_handleClassName.contains("BGDJHandler") ||_handleClassName.contains("BGDJHandler1") || _handleClassName.contains("GZDJHandler")|| _handleClassName.contains("GZDJHandler_LuZhou")) {
			// 变更、更正
			if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) || djdy_GZ.getBDCDYLX().equals(BDCDYLX.HY.Value)) {
				// 房屋
				qltablenameList.add("FDCQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
				// 使用权宗地
				qltablenameList.add("JSYDSYQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				// 所有权宗地
				qltablenameList.add("TDSYQ");
			}

			qlsdfList.add("3");
			iPushBGQList.add(1);
		}  else if (_handleClassName.contains("BZDJHandler") || _handleClassName.contains("YSBZGGDJHandler")|| _handleClassName.contains("BZDJHandler_YSBZ")) {
			// 补证
			if (qllx.equals(QLLX.DIYQ.Value)) {
				qltablenameList.add("DYAQ");
				 if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.YCH.Value) ) {
						//预抵押更正推送登记大类全部改为700	
						djdl="700";
					}
			} else {
				if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) || djdy_GZ.getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
					// 房屋
					qltablenameList.add("FDCQ");
				}else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.YCH.Value) ) {
					//预告更正推送登记大类全部改为700
					djdl="700";
					// 预告更正
					qltablenameList.add("YGDJ");
				} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
					// 使用权宗地
					qltablenameList.add("JSYDSYQ");
				} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
					// 所有权宗地
					qltablenameList.add("TDSYQ");
				}
			}
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			if(xzqhdm.equals("230200")){
				qlsdfList.add("1");
			}else{
				qlsdfList.add("3");
			}
			iPushBGQList.add(1);
			djxl = "2";
		} else if (_handleClassName.contains("HZDJHandler")||_handleClassName.contains("HZDJHandler_LuZhou")) {
			// 换证、
			if (qllx.equals(QLLX.DIYQ.Value)) {
				qltablenameList.add("DYAQ");
			} else {
				if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) || djdy_GZ.getBDCDYLX().equals(BDCDYLX.HY.Value)) {
					// 房屋
					qltablenameList.add("FDCQ");
				} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
					// 使用权宗地
					qltablenameList.add("JSYDSYQ");
				} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
					// 所有权宗地
					qltablenameList.add("TDSYQ");
				}
			}
			qlsdfList.add("1");
			iPushBGQList.add(1);
			djxl = "1";
		} 
		 else if (_handleClassName.contains("HZ_SYQ_DYQ_DJHandler")) {
				// 换证+抵押
			 qltablenameList.add("FDCQ");
				qlsdfList.add("1");
				iPushBGQList.add(1);
				qltablenameList.add("DYAQ");
				qlsdfList.add("1");
				iPushBGQList.add(0);
				djxl = "1";
			} else if (_handleClassName.contains("YCFDJ_HouseHandler")) {
			// 在建工程查封（预测）
			qltablenameList.add("CFDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("CFDJ_HouseHandler")
				|| _handleClassName.contains("CFDJ_XF_HouseHandler")) {
			// 查封
			qltablenameList.add("CFDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("CFDJ_LandHandler")) {
			// 土地查封
			qltablenameList.add("CFDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("CFDJ_GZ_Handler")) {
			// 查封更正
			qltablenameList.add("CFDJ");
			qlsdfList.add("3");
			iPushBGQList.add(1);
		}else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")||_handleClassName.contains("CFDJ_ZX_LandHandler")) {
			// 解封
			qltablenameList.add("CFDJ");
			qlsdfList.add("4");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("CSDJHandler")||_handleClassName.contains("CSDJHandler_HouseAndLand")||_handleClassName.contains("CSDJHandler_LuZhou")) {
			// 初始登记
			if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) || djdy_GZ.getBDCDYLX().equals(BDCDYLX.HY.Value)) {
				// 房屋
				qltablenameList.add("FDCQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
				// 使用权宗地
				qltablenameList.add("JSYDSYQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				// 所有权宗地
				qltablenameList.add("TDSYQ");
			}
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("ZY_YDYTODY_DJHandler")||_handleClassName.contains("ZY_YGDYTODY_DJHandler")||_handleClassName.contains("ZY_YDYTODY_DJHandler_LuZhou")) {
			// 转移+预抵押
			if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) || djdy_GZ.getBDCDYLX().equals(BDCDYLX.HY.Value)) {
				// 房屋
				qltablenameList.add("FDCQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
				// 使用权宗地
				qltablenameList.add("JSYDSYQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				// 所有权宗地
				qltablenameList.add("TDSYQ");
			}
			qlsdfList.add("2");
			iPushBGQList.add(1);
			qltablenameList.add("DYAQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
			
		} else if (_handleClassName.contains("ZYYG_YDY_DJHandler")) {
			// 转移预告+抵押预告登记
			qltablenameList.add("DYAQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
			qltablenameList.add("YGDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("ZYYGDYYGDJHandler")) {
			// 吸老师说这个就是抵押预告登记
			qltablenameList.add("DYAQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("DYZXDJHandler")) {
			// 抵押注销 
			//2016年9月26日 10:57:53 齐齐哈尔 因根据handleclassname无法判断区分抵押注销和转移预抵押注销业务 只能通过项目登记类型判断 设置推送登记大类
//			if(djlx.equals("700")){
//				djdl="700";
//			}
			qltablenameList.add("DYAQ");
			qlsdfList.add("4");
			iPushBGQList.add(0);
		}else if ( _handleClassName.contains("DYYGZXDJHandler") || _handleClassName.contains("YDYZXDYDJHandler")|| _handleClassName.contains("YDYZXDYDJHandler_LuZhou")) {
			//卜晓波 2016年9月20日 14:14:53 根据齐齐哈尔提出问题修改 
			//抵押预告注销登记，预抵押注销
			qltablenameList.add("DYAQ");
			qlsdfList.add("4");
			iPushBGQList.add(0);
			djdl="700";
		} else if (_handleClassName.contains("YCDYDJHandler")) {
			// 在建工程抵押登记（预测）
			qltablenameList.add("DYAQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("YCSCDYDJHandler")) {
			// 在建工程抵押转现房抵押
			qltablenameList.add("DYAQ");
			qlsdfList.add("9");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("YGYDYDJHandler")||_handleClassName.contains("YGYDYDJHandler_LuZhou")) {
			// 预告预抵押登记
			qltablenameList.add("DYAQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
			qltablenameList.add("YGDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("CSDYHandler") || _handleClassName.contains("DYDJHandler")|| _handleClassName.contains("DYDJHandler_HouseAndLand")
				|| _handleClassName.contains("DYYGDJHandler") || _handleClassName.contains("YDYDJHandler")) {
			// 抵押登记、预抵押
			qltablenameList.add("DYAQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("DYLimitedDJHandler")
				|| _handleClassName.contains("DYLimitLiftedDJHandler") || _handleClassName.contains("XZDJHandler")) {
			// 单元限制登记
			qltablenameList.add("XZDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("ZYYGZXDJHandler")) {
			// 转移预告注销
			qltablenameList.add("YGDJ");
			qlsdfList.add("4");
			iPushBGQList.add(0);
			djdl = "700";
		} else if (_handleClassName.contains("YYZXDJHandler")) {
			// 异议注销登记
			qltablenameList.add("YYDJ");
			qlsdfList.add("4");
			iPushBGQList.add(0);
			djxl = "3";
			djdl = "600";
		} else if (_handleClassName.contains("ZXDJHandler")||_handleClassName.contains("ZXDJHandler2")) {
			// 所有权注销登记
			if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value)){
				qltablenameList.add("FDCQZXDJ");
			}
			else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
				// 使用权宗地
				qltablenameList.add("JSYDSYQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				// 所有权宗地
				qltablenameList.add("TDSYQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
				qltablenameList.add("YGDJ");
			}
			qlsdfList.add("4");
			//因登记系统房屋灭失会删除现状层户，想要实现所有注销登记大类户和权利人的推送就统一处理成推送变更前户 变更前权利人吧
			iPushBGQList.add(1);
		} else if (_handleClassName.contains("DYZYDJHandler")) {
			// 抵押转移登记
			qltablenameList.add("DYAQ");
			qlsdfList.add("2");
			iPushBGQList.add(1);
		} else if (_handleClassName.contains("ZYDJHandler")||_handleClassName.contains("ZYDJHandler_LuZhou")||_handleClassName.contains("QZZYDJHandler")) {
			// 转移登记
			if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) || djdy_GZ.getBDCDYLX().equals(BDCDYLX.HY.Value)) {
				// 房屋
				qltablenameList.add("FDCQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
				// 使用权宗地
				qltablenameList.add("JSYDSYQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				// 所有权宗地
				qltablenameList.add("TDSYQ");
			}
			qlsdfList.add("2");
			iPushBGQList.add(1);
		} else if (_handleClassName.contains("YGDJHandler")||_handleClassName.contains("ZYYGDJHandler")) {
			// 预告登记  转移预告登记
			qltablenameList.add("YGDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("YYDJHandler")) {
			// 异议登记
			qltablenameList.add("YYDJ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		} else if (_handleClassName.contains("ZY_DY_DJHandler")) {
			// 转移+抵押登记
			if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.H.Value) || djdy_GZ.getBDCDYLX().equals(BDCDYLX.HY.Value)) {
				// 房屋
				qltablenameList.add("FDCQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
				// 使用权宗地
				qltablenameList.add("JSYDSYQ");
			} else if (djdy_GZ.getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				// 所有权宗地
				qltablenameList.add("TDSYQ");
			}
			qlsdfList.add("2");
			iPushBGQList.add(1);
			qltablenameList.add("DYAQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		}else if (_handleClassName.contains("CQHFDJHandler")) {
			//初始登记
			qltablenameList.add("FDCQ");
			qlsdfList.add("1");
			iPushBGQList.add(0);
		}else if(_handleClassName.contains("BGDJEXHandler")){
			qltablenameList.add("FDCQ");
			qlsdfList.add("3");
			iPushBGQList.add(0);
		}
		// 异议注销，预告注销登记，传回登记大类值
		if (djdl != null) {
			djxl += "," + djdl;
		}
		return djxl;
	}


	/**
	 * 柳州此处调用房产回写接口，将组织结构代码、casenum、标准jsonobject回写房产 2016年6月21日 10:02:39 卜晓波
	 * GXDJK数据查询接口：http://localhost:8080/share/app/sharezjk/returndata/casenum
	 */
	public void RerurnFCdata(String casenum, BDCS_XMXX xmxx, String bljd) {
		try {
			// 此处调用协同共享系统服务先拼接标准jsonobject,然后调用房产回写接口，后返回房产返回json对象
			String share_url = ConfigHelper.getNameByValue("URL_SHARE");
			URL url = new URL(share_url + "sharezjk/returndata/" + casenum + "/" + bljd +"/" +xmxx.getPROJECT_ID());
			URLConnection urlcon = url.openConnection();
			BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
			String inputLine = null;
			inputLine = bf.readLine();
			StringBuilder json = new StringBuilder();
			json = json.append(inputLine);
			JSONObject flag = JSONObject.fromObject(json.toString());

			if (flag.get("EroCode").equals("0") || flag.get("EroCode").equals("400")
					|| flag.get("EroCode").equals("401") || flag.get("EroCode").equals("402")
					|| flag.get("EroCode").equals("500") || flag.get("EroCode").equals("501")) {
				DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
				// 因房产回写接口自身问题导致数据回写失败，更新BDCS_XMXX表中当前项目TSCS=1,等待ETL工具晚上自动检索TSCS=1补推到GXDJK.FAIL表
				try {
					dataSwapImpEx.updateXMXXTSCS(xmxx, 1, baseCommonDao);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
			// 因网络中断等原因导致调用房产回写接口失败，只能先更新BDCS_XMXX表中当前项目TSCS=1,等待ETL工具晚上自动检索TSCS=1维护到GXDJK.FAIL表
			try {
				dataSwapImpEx.updateXMXXTSCS(xmxx, 1, baseCommonDao);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 唐山此处调用房产回写接口，将组织结构代码、casenum、标准jsonobject回写房产 2018年4月21日 10:02:39 陈国考
	 * GXDJK数据查询接口：http://localhost:8080/share/app/sharezjk/returndata/casenum
	 */
	public void RerurnFCdatas(String casenum, BDCS_XMXX xmxx, String bljd) {
		try {
			// 此处调用协同共享系统服务先拼接标准jsonobject,然后调用房产回写接口，后返回房产返回json对象
			String share_url = ConfigHelper.getNameByValue("URL_SHARE");
			URL url = new URL(share_url + "sharezjk/returndata/" + casenum + "/" + bljd +"/" +xmxx.getPROJECT_ID());
			URLConnection urlcon = url.openConnection();
			BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
			String inputLine = null;
			inputLine = bf.readLine();
			StringBuilder json = new StringBuilder();
			json = json.append(inputLine);
			JSONObject flag = JSONObject.fromObject(json.toString());

			if (flag.get("EroCode").equals("0") || flag.get("EroCode").equals("400")
					|| flag.get("EroCode").equals("401") || flag.get("EroCode").equals("402")
					|| flag.get("EroCode").equals("500") || flag.get("EroCode").equals("501")) {
				DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
				// 因房产回写接口自身问题导致数据回写失败，更新BDCS_XMXX表中当前项目TSCS=1,等待ETL工具晚上自动检索TSCS=1补推到GXDJK.FAIL表
				try {
					dataSwapImpEx.updateXMXXTSCS(xmxx, 1, baseCommonDao);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
			// 因网络中断等原因导致调用房产回写接口失败，只能先更新BDCS_XMXX表中当前项目TSCS=1,等待ETL工具晚上自动检索TSCS=1维护到GXDJK.FAIL表
			try {
				dataSwapImpEx.updateXMXXTSCS(xmxx, 1, baseCommonDao);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
     /**
 	 * 广西省厅 调用房产回写接口，将组织结构代码、casenum、标准jsonobject回写房产 2016年6月21日 10:02:39 卜晓波
 	 * GXDJK数据查询接口：http://localhost:8080/share/app/sharezjk/returndata/casenum
 	 */
 	@SuppressWarnings("unused")
	public String RerurnFCdata2(String casenum, BDCS_XMXX xmxx, String bljd) {
 		String resultString="";
 		try {
 			logger.info("开始调用接口");
 			// 此处调用协同共享系统服务先拼接标准jsonobject,然后调用房产回写接口，后返回房产返回json对象
 			String share_url = ConfigHelper.getNameByValue("URL_SHARE");
 			URL url = new URL(share_url + "sharezjk/sharehouse/" +xmxx.getPROJECT_ID()+"/");
 			URLConnection urlcon = url.openConnection();
 			BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
 			String inputLine = null;
 			inputLine = bf.readLine();
 			StringBuilder json = new StringBuilder();
 			json = json.append(inputLine);
 			JSONObject flag = JSONObject.fromObject(json.toString());
 		
 		} catch (IOException e) {
 			resultString="失败";
 			logger.error("出错:"+e.getMessage());
 		}
 		return resultString;
 	}
 	
 	
 	
 	/**
 	 * 齐齐哈尔特别版 退件后将当前删除项目所有户从共享交易库取出原relationid维护回去 保证relationid不变
 	 * 2016年9月3日20:24:04 卜晓波
 	 * */
 	@SuppressWarnings("unused")
 	public void DelMaintanceRelation(BDCS_XMXX xmxx) throws SQLException{
 		Connection jyConnection = null;
		try {
			if (jyConnection == null || jyConnection.isClosed()) {
				jyConnection = JH_DBHelper.getConnect_jy();
			}
		} catch (SQLException e1) {
		}
		//取NRELATIONID不能用model取，因为此处专门为齐齐哈尔设计   此处是要利用project_id查找原relationid
		String NRELATIONID="";
		String prjId = xmxx.getPROJECT_ID();
		String sql="select GXXMBH from gxjyk.gxjhxm where PROJECT_ID = '"+prjId+"'";
		PreparedStatement pstmt = jyConnection.prepareStatement(sql);
		ResultSet gxxmbhrst = pstmt.executeQuery();
//		ResultSet gxxmbhrst=JH_DBHelper.excuteQuery(jyConnection, sql);
		if (gxxmbhrst != null) {
			while (gxxmbhrst.next()) {
				String gxxmbh=gxxmbhrst.getString("GXXMBH");
				String sql1="select FWZT,RELATIONID,NRELATIONID from gxjyk.h where GXXMBH = '"+gxxmbh+"'";
				PreparedStatement pstmt2 = jyConnection.prepareStatement(sql);
				ResultSet relationidrst = pstmt.executeQuery();
//				ResultSet relationidrst=JH_DBHelper.excuteQuery(jyConnection, sql1);
				String fwzt="",relationid="",nrelationid="";
				if (relationidrst != null) {
					while (relationidrst.next()) {
						fwzt = relationidrst.getString("FWZT");
						relationid = relationidrst.getString("RELATIONID");
						nrelationid =relationidrst.getString("NRELATIONID");
						if(fwzt.equals("2")){
							List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class,"RELATIONID= '"+nrelationid+"'");
							//非退件情况才做更新
							if(h_xzs!=null && h_xzs.size()>0){
								BDCS_H_XZ h_xz=h_xzs.get(0);
								h_xz.setRELATIONID(relationid);
								baseCommonDao.update(h_xz);
							}
						}else{
							List<BDCS_H_XZY> h_xzys = baseCommonDao.getDataList(BDCS_H_XZY.class,"RELATIONID= '"+nrelationid+"'");
							//非退件情况才做更新
							if(h_xzys!=null && h_xzys.size()>0){
								BDCS_H_XZY h_xzy=h_xzys.get(0);
								h_xzy.setRELATIONID(relationid);
								baseCommonDao.update(h_xzy);
							}
						}
					}
					pstmt2.close();
					relationidrst.close();
				}
			}
			pstmt.close();
			gxxmbhrst.close();
		}
 	}
 	
 	/**
	 * 保存bdck的fail表
	 * 
	 * @作者 think
	 * @创建时间 2016年9月7日上午12:11:48
	 * @param xmxx
	 * @param casenum
	 * @param bljd
	 * @param sblx
	 *            失败类型
 	 * @param failCause 
	 * @return
	 */
	boolean saveFailToBDCK(BDCS_XMXX xmxx,String casenum,String bljd,String sblx, String failCause){
		  boolean b=false;
		  try {
			  //失败表中已有的再次补推时候也先删掉再保存最新的失败原因 方便实时定位问题
			  String prjid=xmxx.getPROJECT_ID();
			  List<PUSHFAIL> faiList= baseCommonDao.getDataList(PUSHFAIL.class, "project_id='"+prjid+"'");
			  if(faiList!=null&&faiList.size()>0){
				  for(int i=0;i<faiList.size();i++){
					  baseCommonDao.deleteEntity(faiList.get(i));
				  }
			  }
				  PUSHFAIL fail=new PUSHFAIL();
				  fail.setId(Common.CreatUUID());
				  fail.setBLJD(bljd);
				  fail.setCASENUM(casenum);
				  fail.setDJLX(xmxx.getDJLX());
				  fail.setPROJECT_ID(prjid);
				  fail.setQLLX(xmxx.getQLLX());
				  fail.setSLRY(xmxx.getSLRY());
				  fail.setSLSJ(xmxx.getSLSJ());
				  if(failCause.contains("Could not open connection")){
					  failCause="中间库连接失败！请检查网络配置！";
				  }else if(failCause.contains("SQLGrammarException")){
					  failCause="中间库表中缺失字段，导致无法插入待推送数据，请检查！";
				  }else if(failCause.contains("GenericJDBCException")){
					  failCause="中间库表中字段长度不足，导致无法插入待推送数据，请检查！";
				  }
				  fail.setFAILCAUSE(failCause);
	//			  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//				String fctime=sdf1.format(new Date());
				  fail.setTSSJ(new Date());
				  fail.setXMMC(xmxx.getXMMC());
				  fail.setYWLSH(xmxx.getYWLSH());
				  fail.setSBLX(sblx);
				   baseCommonDao.save(fail);
				   baseCommonDao.flush();
		} catch (Exception e) {
			logger.error("pushfail表保存失败："+e.getMessage());
		}
		  return b;
	  }

		
	/**
	 * 不动产与地籍系统对接：登簿时推送地籍库更新相关数据 
	 * @author buxiaobo
	 * @time 2016年11月8日 10:26:49
	 * */
	@SuppressWarnings("unused")
	public void BDCKPushToDJK(BDCS_XMXX xmxx) {
		try {
			//吉林 登簿时调用协同接口推送数据到地籍库 暂时先放开吉林的
			String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
			if(xmxx.getQLLX()!=null && !xmxx.getQLLX().equals("")&&XZQHDM.contains("2202")){
				try{
					// 此处调用协同共享系统服务先拼接标准jsonobject,然后调用房产回写接口，后返回房产返回json对象
					String share_url = ConfigHelper.getNameByValue("URL_SHARE");
					URL url = new URL(share_url + "BDCKPushToDJK/" + xmxx.getId() );
					URLConnection urlcon = url.openConnection();
					BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
					String inputLine = null;
					inputLine = bf.readLine();
//					StringBuilder json = new StringBuilder();
//					json = json.append(inputLine);
//					JSONObject flag = JSONObject.fromObject(json.toString());
					}
					catch(Exception e){
						logger.error("登簿推送地籍数据失败：项目编号" + xmxx.getId()+"错误："+e.getMessage());
						e.printStackTrace();
					}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 不动产与地籍系统对接：登簿时推送地籍库更新相关数据 
	 * @author buxiaobo
	 * @time 2016年11月8日 10:26:49
	 * */
	public String BDCKPushIsRK(String TDCasenum) {
		String flag = null;
		try {
			//吉林 登簿时调用协同接口推送数据到地籍库 暂时先放开吉林的
			String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
			if(XZQHDM.contains("2202")){
				try{
					// 此处调用协同共享系统服务先拼接标准jsonobject,然后调用房产回写接口，后返回房产返回json对象
					String share_url = ConfigHelper.getNameByValue("URL_SHARE");
					URL url = new URL(share_url + "IsRK/" + TDCasenum );
					URLConnection urlcon = url.openConnection();
					BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
					//String inputLine = null;
					flag = bf.readLine();
//					StringBuilder json = new StringBuilder();
//					json = json.append(inputLine);
//					JSONObject flag = JSONObject.fromObject(json.toString());
					}
					catch(Exception e){
						logger.error("登簿推送地籍数据失败：项目编号" + TDCasenum +"错误："+e.getMessage());
						e.printStackTrace();
					}
				return flag;
			}
		} catch (Exception e) {

		}
		return flag;
	}
	
	public synchronized void zjkpushType2(BDCS_XMXX xmxx, String bljc) {
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
		DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
		try {
			com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ	CommonDaoDJ2=(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ) SuperSpringContext.getContext().getBean("baseCommonDaoDJ");
			double startTime = System.currentTimeMillis();
			List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class,
					"WORKFLOWCODE='" + workflowcode + "'");
			//判断业务流程推送权利人名称 证件号是否加密
			String eNCRYPTION=mappings.get(0).getENCRYPTION();
			// 判断业务流程是否推送，PUSHTOZJK为1才推送
			if (mappings == null || mappings.size() == 0 || mappings.get(0).getPUSHTOZJK() == null
					|| !mappings.get(0).getPUSHTOZJK().equals("1")) {
				// xmxx表记录本项目无需推送
				// xmxx.settSCS(-1);
				baseCommonDao.update(xmxx);
				baseCommonDao.flush();
				logger.info("该基准流程配置不推送或没有配置基准流程");
				return;
			}
			List<String> qltablenameList = new ArrayList<String>();
			List<String> qlsdfList = new ArrayList<String>();
			List<Integer> iPushBGQList = new ArrayList<Integer>();// 是否推送变更前
			String djxl = "0", djdl = null;
			String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
			String XZQHMC = ConfigHelper.getNameByValue("XZQHMC");
			// String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
			// 如果是泸州的，有些业务不推送
			// Boolean bPush = xzqdm.contains("5105") ? false : true;
			// 配置方式新方法推送
			String qllx = xmxx.getQLLX();
			String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
			if (djdys == null || djdys.size() == 0) {
				return;
			}
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
			logger.info("不动产单元类型:"+djdys.get(0).getBDCDYLX()+",workflowcode"+workflowcode+",handlename:"+_handleClassName);
			// 推送到中间库,0不推送，1中间库模式推送（受理和登薄都推）,2MQ模式推送,3接口模式推送（受理和登薄都推）,4中间库模式（仅登薄推）,5接口模式（仅登薄推）,
			//6中间库模式（受理+登薄+缮证）,7接口模式（受理+登薄+缮证）,8中间库模式推送（受理和归档都推）,9接口模式推送（受理和归档都推）
			int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETOZJK"));
			// 房产业务
			if (djdys.get(0).getBDCDYLX().equals(BDCDYLX.H.Value)
					|| djdys.get(0).getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
				// 获取推送参数
				String paras = getTSParamets(xmxx, djdys.get(0), _handleClassName, qllx, qltablenameList, qlsdfList,
						iPushBGQList,xmxx.getDJLX());
				String[] pStrings = paras.split(",");
				if (pStrings != null && pStrings.length > 0) {
					djxl = pStrings[0];// 登记小类
					if (pStrings.length > 1) {
						djdl = pStrings[1];// 登记大类
					}
				}
				if (djdl != null) {
					dataSwapImpEx.setDjdl(djdl);
				}
				dataSwapImpEx.setXzqdm(XZQHDM);
				dataSwapImpEx.setXzqmc(XZQHMC);
				//南宁受理转出的时候，给权利表的casenum赋值，防止手工选单元无casenum
				if (bljc.equals("1")&&"4501".equals(XZQHDM)) {
					
				}
				if (bljc.equals("0")) {
					// logger.info("退件！删除中间库数据");
					// 删除案件，删除推送数据
					/**齐齐哈尔删件时要先从共享交易库把旧relationid取出来，维护现状层户relationid到原值后再删出中间库
					 * */
					String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
					if(xzqdm.equals("230200")){
						DelMaintanceRelation(xmxx);
					}
					if(dataSwapImpEx.getFieldExist("GXDJK", "GXJHXM", "ISDELETE")){
						//不物理删除，打标记
						dataSwapImpEx.deleteEx(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}
					else{
						dataSwapImpEx.delete(xmxx.getPROJECT_ID());
					}
				} else {
					/**保证登簿后推送项目共享项目编号与受理转出相同 
					 * 在登簿环节取出relationid与之匹配的受理转出时的共享项目编号map
					 * 2016年8月31日 08:33:22 卜晓波
					 */
					String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
					Map<String,String> relationid2gxxmbh=new HashMap<String, String>();
					if(XZQHDM.equals("230200")){
						relationid2gxxmbh=GetRelationidfromSLZC(bljc,xmxx);
					}
					// logger.info("删除中间库重复数据");
					// 推送前先删除一下本项目数据（考虑有被驳回情况）
					if(XZQHDM.equals("230200") && bljc.equals("2")){
						dataSwapImpEx.QQHEdelete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}
					//吉林登簿时不删除原来的项目 2016年12月5日 21:41:37 卜晓波
					else if(XZQHDM.contains("2202")&& bljc.equals("2")){
						
					}else if(XZQHDM.contains("2202") && bljc.equals("1") && xmxx.getDJLX().equals("800")){
						return;
					}else{
						dataSwapImpEx.delete(xmxx.getPROJECT_ID(),CommonDaoDJ2);
					}
					logger.info("开始往中间库推送数据，业务流水号：" + xmxx.getYWLSH()+"。权利数："+qltablenameList.size());
					// 开始推送，多线程session
					Session session=CommonDaoDJ2.getCurrentSession();
					Connection conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
//					session.setFlushMode(FlushMode.MANUAL);
					 Map<String, String> read = dbService.read();
		      		  String pushString = read.get("push");
		      		  String tssx = read.get("tssx");
					//开启事务
					Transaction ts=session.beginTransaction();
//					String tuisong="";
//					if(xzqdm.equals("451400")){
//						if(djdys.get(0), _handleClassName, qllx, qltablenameList, qlsdfList,
//								iPushBGQList,xmxx.getDJLX()){
//							tuisong="yes";
//						}
//					}else {
//						tuisong="no";
//					}
					// 开始推送
					if("1".equals(tssx)){
						for (int i = 0; i < qltablenameList.size(); i++) {
							if(qltablenameList.get(i).equals("XZDJ")){
								//限制登记权利保存在BDCS_DYXZ中，走单独推送方法  2016年7月22日 09:27:10  卜晓波
								dataSwapImpEx.pushXZDJToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
										iPushBGQList.get(i), djxl,relationid2gxxmbh);
							}else{ 
								//齐齐哈尔此处验证由房产发起的业务现在受理转出环节不推送
								if(XZQHDM.equals("230200")){
									boolean IsPush=true;
									if(bljc.equals("1")){
										IsPush=CheckIsPush(xmxx);
									}
									if(IsPush==true){
										dataSwapImpEx.pushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
												iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
									}else{
									}
								}else{
									
									dataSwapImpEx.pushToGXDJK(baseCommonDao,CommonDaoDJ2, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
											iPushBGQList.get(i), djxl,relationid2gxxmbh,eNCRYPTION);
								}
//								dataSwapImpEx.pushToGXDJK(baseCommonDao, xmxx, qltablenameList.get(i), bljc, qlsdfList.get(i),
//										iPushBGQList.get(i), djxl,relationid2gxxmbh);
							}
						}
						/*ts.commit();
						CommonDaoDJ2.getSession().close();*/
//						}
					}
					String result = "";
					// 是否推送附件，0不推送，1受理转出推送，2登薄推送，3都推送（覆盖）
//					String pushString = ConfigHelper.getNameByValue("PUSHFJTOFC");
					if (pushString != null && !pushString.equals("")) {
						int pushfj = Integer.parseInt(pushString);
						if (pushfj ==3||(pushfj ==1&&bljc.equals("1"))||(pushfj ==2&&bljc.equals("2"))) {
							logger.info("开始推送附件");
							dataSwapImpEx.pushFJToGXDJK(baseCommonDao);
						}
					}
					ts.commit();
					conn.close();
					session.close();
					System.out.println("连接关闭");
					String casenum = dataSwapImpEx.getCasenum();
					if (bljc.equals("2")) {
						// 登薄调用回写房产库接口
						/* 南宁市回调房产接口和三维接口 */
						if ("4501".equals(XZQHDM)) {
							result = callfcInter(xmxx.getPROJECT_ID());
							threedfcInter(xmxx.getPROJECT_ID());
						}
						/*
						 * * 2016年6月21日 10:02:39
						 * 柳州此处调用房产回写接口，将组织结构代码、房产业务号、标准jsonobject回写房产
						 * GXDJK数据查询接口：http://localhost:8080/share/app/sharezjk/
						 * returndata/casenum
						 */
						else if (XZQHDM.equals("450200")) {
							RerurnFCdata(casenum, xmxx, bljc);
						}
						//广西省厅测试
						else if (XZQHDM.contains("4500")) {
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
						else{
							if(zjk==3||zjk==5||zjk==7){
								result=RerurnFCdata2(casenum, xmxx, bljc);
							}
						}
					}
					else if (bljc.equals("1")) {
						//受理转出
						if(zjk==3||zjk==7||zjk==9){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}
					else if (bljc.equals("3")) {
						//缮证
						if(zjk==7){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}
					else if (bljc.equals("4")) {
						//归档
						if(zjk==9){
							result=RerurnFCdata2(casenum, xmxx, bljc);
						}
					}
					// 如果推送失败，记录在bdck的失败表中，如果成功从pushfaile表删除
					if (!dataSwapImpEx.allSuccess || result.contains("失败")) {
						String sblxString="2";//调用接口失败
						if (!dataSwapImpEx.allSuccess){
							sblxString="1";//推送中间库失败
						}
					     //保存到bdck的fail表
						if(tableExist("BDCK", "PUSHFAIL")){
							saveFailToBDCK(xmxx, dataSwapImpEx.getCasenum(),bljc,sblxString,dataSwapImpEx.FailCause);
						}
					}
					else {
						String condition="project_id='"+xmxx.getPROJECT_ID()+"'";			
						   baseCommonDao.deleteEntitysByHql(PUSHFAIL.class, condition);
					}
					
					// logger.info("推送完成！");
				}
				try {
					dataSwapImpEx.getJyConnection().close();
					logger.info("GXDJK连接正常关闭");
					logger.info("本次推送projrct_id是"+xmxx.getPROJECT_ID()+"推送用时："+(System.currentTimeMillis() - startTime)+"（毫秒）");
					System.out.println("本次推送projrct_id是"+xmxx.getPROJECT_ID()+"推送用时："+(System.currentTimeMillis() - startTime)+"（毫秒）");
				} catch (SQLException e) {
					logger.info("GXDJK连接关闭出错");
				}
			}
			// 土地业务
			else if (djdys.get(0).getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)
					|| djdys.get(0).getBDCDYLX().equals(BDCDYLX.SYQZD.Value)) {
				logger.info("进入土地数据推送");
				// 暂时南宁、西宁推送土地业务
				if ("4501".equals(XZQHDM)||XZQHDM.contains("6301")||zjk == 21||zjk == 24) {
					// 获取推送参数
					String paras = getTSParamets(xmxx, djdys.get(0), _handleClassName, qllx, qltablenameList, qlsdfList,
							iPushBGQList,xmxx.getDJLX());
					String[] pStrings = paras.split(",");
					if (pStrings != null && pStrings.length > 0) {
						djxl = pStrings[0];// 登记小类
						if (pStrings.length > 1) {
							djdl = pStrings[1];// 登记大类
						}
					}
					if (bljc.equals("0")) {
						 logger.info("退件！删除中间库数据");
						dataSwapImpEx.delete_tddj(xmxx.getPROJECT_ID());
					} else {
						 logger.info("删除中间库重复数据");
						// 推送前先删除一下本项目数据（考虑有被驳回情况）
						dataSwapImpEx.delete_tddj(xmxx.getPROJECT_ID());
						for (int i = 0; i < qltablenameList.size(); i++) {
							dataSwapImpEx.pushToTDDJK(baseCommonDao, xmxx, qltablenameList.get(i), bljc,
									qlsdfList.get(i), iPushBGQList.get(i), djxl);
						}
					}
					try {
						dataSwapImpEx.getTddjConnection().close();
					} catch (SQLException e) {
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage() + " 流程编码：" + workflowcode +",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljc+"");
			//保存到bdck的fail表
			if(tableExist("BDCK", "PUSHFAIL")){
				saveFailToBDCK(xmxx, "",bljc,"1",e.toString());
			}
		}
	}
	
	/**
	 * 检查表是否存在
	 * @作者 think
	 * @创建时间 2016年9月6日下午9:40:48
	 * @param owner
	 * @param tablename
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	boolean tableExist(String owner,String tablename) {
    	 boolean b=false;
    	 String sql="SELECT COUNT(1) as count FROM DBA_TABLES WHERE TABLE_NAME=UPPER('"+tablename+"') AND OWNER='"+owner+"'";
  		try {
  			List<Map> li = baseCommonDao.getDataListByFullSql(sql);
  			if (li.size() > 0) {
  				int k =Integer.parseInt( li.get(0).get("COUNT").toString());
  				if(k>0){
  					b=true;
  				}
  			}
 		} catch (Exception ex) {
 		}
  		return b;
	}


	/**
	 * 回调房产接口，读取信息写入中间库
	 * 
	 * @作者 lanjf
	 * @创建时间 2016年5月26日下午11:27:55
	 * @param project_id
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused", "resource" })
	public String callfcInter(String project_id) {
		String url2 = GetProperties.getConstValueByKey("FCInterface");
		String result = "<br/> ";
		// 通知房产调用接口
		String fcywbhSQL = "select * from BDC_WORKFLOW.DJ_FC_RELATION where PROJECT_ID='" + project_id + "'";
		String fcyhwh = "";
		List<Map> li = baseCommonDao.getDataListByFullSql(fcywbhSQL);
		if (li.size() > 0) {
			fcyhwh = StringUtil.valueOf(li.get(0).get("ACTIVEID"));
		}
		/* 为空就不调用 */
		if ("".equals(fcyhwh)) {
			return "";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", fcyhwh);
		map.put("action", "3");
		String remessage = HttpRequest.sendGet(url2, map);
		StringBuffer sb = new StringBuffer(remessage);
		sb.insert(0, "[");
		sb.insert(remessage.length() + 1, "]");
		Connection connection = JH_DBHelper.getConnect_gxdjk();
		String sql = null;
		PreparedStatement ps = null;
		String selecasenum = "select * from gxdjk.CALLBACK_INFO where CASENUM='" + fcyhwh + "'";
		ResultSet rs = null;
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(selecasenum);
			rs = pst.executeQuery();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			JSONArray jsonArray = JSONArray.fromObject(sb.toString());
			List<Map> mapListJson = (List) jsonArray;
			if (connection == null) {
				return result + "房产接口回调成功，但返回结果保存失败。";
			}
			// 写入中间库
			if (mapListJson.size() > 0) {
				String success = String.valueOf(mapListJson.get(0).get("success"));
				String message = String.valueOf(mapListJson.get(0).get("message"));

				String callbackResult = "";
				if (rs.next()) {
					sql = "update gxdjk.CALLBACK_INFO set MESSAGE=?,ZT=? where CASENUM=?";
					ps = connection.prepareStatement(sql);
					if ("true".equals(success)) {
						callbackResult = "回调成功";
						ps.setString(2, callbackResult);
					} else if ("false".equals(success)) {
						callbackResult = "回调失败";
						ps.setString(2, callbackResult);
					}
					ps.setString(1, message);
					ps.setString(3, fcyhwh);
					int s = ps.executeUpdate();
				} else {
					sql = "insert into gxdjk.CALLBACK_INFO(CASENUM,MESSAGE,zt,BSM)values(?,?,?,?)";
					ps = connection.prepareStatement(sql);
					if ("true".equals(success)) {
						callbackResult = "回调成功";
						ps.setString(3, callbackResult);
					} else if ("false".equals(success)) {
						callbackResult = "回调失败";
						ps.setString(3, callbackResult);
					}
					ps.setString(2, message);
					ps.setString(1, fcyhwh);
					ps.setString(4, String.valueOf(UUID.randomUUID()));
					int s = ps.executeUpdate();
				}
				result += "房产接口" + callbackResult;

			}
		} catch (Exception e) {
			try {
				if (rs.next()) {
					sql = "update gxdjk.CALLBACK_INFO set MESSAGE=?,ZT=? where CASENUM=?";
					ps = connection.prepareStatement(sql);
					ps.setString(2, "回调异常");
					ps.setString(1, sb.toString());
					ps.setString(3, fcyhwh);
					int s = ps.executeUpdate();
				} else {
					sql = "insert into gxdjk.CALLBACK_INFO(CASENUM,MESSAGE,zt,BSM)values(?,?,?,?)";
					ps = connection.prepareStatement(sql);
					ps.setString(3, "回调异常");
					ps.setString(2, sb.toString());
					ps.setString(1, fcyhwh);
					ps.setString(4, String.valueOf(UUID.randomUUID()));
					int s = ps.executeUpdate();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 三维接口
	 */
	public void threedfcInter(String project_id) {
		String threed = GetProperties.getConstValueByKey("ThreeD");
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("strCmd", "WriteBack");
			map.put("strPapameter", project_id);
			String message = HttpRequest.sendPost(threed, map);
			logger.info("回调三维接口结果:" + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	
	/** 
	 * 孝感市自动更新共享登记库未关联casenum项目
	* @author  buxiaobo
	* @date 创建时间：2017年5月8日 下午6:56:18 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	*/
	public ResultMessage Relationdata() {
		ResultMessage resultMessage=new ResultMessage();
		String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
		if(!XZQHDM.equals("420900")){
			resultMessage.setMsg("此功能为孝感市自动关联casenum专用！");
			resultMessage.setSuccess("失败");
			return resultMessage;
		}
		try{
			double startTime = System.currentTimeMillis();
			Connection djConnection = JH_DBHelper.getConnect_gxdjk();
			ResultSet res=JH_DBHelper.excuteQuery(djConnection, "select project_id from gxdjk.gxjhxm where casenum is null group by project_id");
			int num=0;
			String proString="";
			while(res.next()){
				if(res.getString("PROJECT_ID")==null){
					continue;
				}
				String project_id=res.getString("PROJECT_ID");
				DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
				int curnum=dataSwapImpEx.updategxxmbh(project_id);
				if(curnum!=0){
					proString+=project_id+",";
					num=num+curnum;
				}
			}
			resultMessage.setMsg("本次更新用时："+(System.currentTimeMillis() - startTime)+",其中gxdjk中gxjhxm的casenum数量:"+num+",其中具体Project_id是："+proString);
			resultMessage.setSuccess("成功");
			logger.info("本次更新用时："+(System.currentTimeMillis() - startTime)+",其中gxdjk中gxjhxm的casenum数量:"+num+",其中具体Project_id是："+proString);
		}catch(Exception e){
			resultMessage.setMsg(e.toString());
			resultMessage.setSuccess("失败");
		}
		return resultMessage;
	}



	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年5月24日 上午11:10:47 
	* @version 1.0 
	 * @param xzly 
	 * @param dyxzs 
	* @parameter  
	* @since  
	* @return  
	*/
	public void qjSendMessage(String inputLine, String xzly) {
		try {
			DataSwapImpEx dataSwapImpEx=new DataSwapImpEx();
			dataSwapImpEx.QJpushXZDJToGXDJK(inputLine,xzly);
		} catch (Exception e) {
			System.out.println("权籍系统限制登记调用协同接口推送异常："+e.toString());
		}
	}
	
	/**
	 * 遂宁受理 登簿调用亿联接口
	 */
	@SuppressWarnings("unused")
	public void SNRerurnFCdata(BDCS_XMXX xmxx, String bljd) {
		try {
			// 此处调用协同共享系统服务先拼接标准jsonobject,然后调用房产回写接口，后返回房产返回json对象
			String share_url = ConfigHelper.getNameByValue("URL_SHARE");
			URL url = new URL(share_url + "sharezjk/snreturndata/" + bljd +"/" +xmxx.getPROJECT_ID());
			URLConnection urlcon = url.openConnection();
			BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
			String inputLine = null;
			inputLine = bf.readLine();
			StringBuilder json = new StringBuilder();
			json = json.append(inputLine);
			JSONObject flag = JSONObject.fromObject(json.toString());
		} catch (IOException e) {
			DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
			// 因网络中断等原因导致调用房产回写接口失败，只能先更新BDCS_XMXX表中当前项目TSCS=1,等待ETL工具晚上自动检索TSCS=1维护到GXDJK.FAIL表
			try {
				dataSwapImpEx.updateXMXXTSCS(xmxx, 1, baseCommonDao);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	public String yanzhen(String htbh){
		  List<BDCS_QL_GZ> qlgz = baseCommonDao.getDataList(BDCS_QL_GZ.class, "casenum ='"+htbh+"'");
		  List<Wfi_ProInst> wfi = baseCommonDao.getDataList(Wfi_ProInst.class, "ywh ='"+htbh+"'");
		  if(wfi.size()>0 && qlgz.size()>0){
			  return "y";
		  }else {
			  return "n";
		}  
	  }
}

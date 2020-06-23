package com.supermap.luzhouothers.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.luzhouothers.model.V_H;
import com.supermap.luzhouothers.model.V_QLR;
import com.supermap.luzhouothers.model.V_QYBAXX;
import com.supermap.luzhouothers.service.SmCompactService;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.impl.share.ExtractDataServiceImpl;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description 本服务主要是完成读取合同、买卖双方、房屋的信息
 * 				读取合同后，将提取的信息存入户、房屋、以及申请人的服务
 * @author lxk
 * @CreateTime 2015年10月29日23:48:50
 * @EditeTime 2016年1月24日22:27:09
 * 
 */
@Service("SmCompactService")
public class SmCompactServiceImpl  implements SmCompactService{
	@Autowired
	private CommonDao baseCommonDao;
	protected Logger logger = Logger.getLogger(SmCompactServiceImpl.class);

	@Autowired
	private DYService dyService;
	/**
	 * @author lxk
	 * @CreateTime 2015年10月29日23:27:23
	 * @param compactNO 合同编号
	 * 
	 */
	private ResultMessage GetCompactInfobyCompactNo(String _xmbh,String _compactNo) {
		ResultMessage reslutMessage=new ResultMessage();
		
		//验证该合同号是否已经抽取过
		if(checkAbstracted(_compactNo)){
				reslutMessage.setMsg("该合同号已使用过!");
				reslutMessage.setSuccess("false");
				return reslutMessage;
		}
		//状态为1时时现状的，2是历史的
		String hqlSql=" HTH = '"+_compactNo+"' and ZT=1 order by slsj desc ";
		List<V_QYBAXX> compacts=baseCommonDao.getDataList(V_QYBAXX.class, hqlSql);
		if(compacts!=null && compacts.size()>0)
		{			
			//加载合同内的需要信息
			//房屋编码
			String fwbm=compacts.get(0).getFWBM();	
			//合同业务号
			String ywh=compacts.get(0).getYWH();
			//交易总价
			Double qdjg=compacts.get(0).getZJ();
			//调用封装的方法，根据项目编号判断不动产单元类型
			BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(_xmbh);		
			boolean flag = false;
			//根据合同中的信息（realationID）得到具体户的不动产单元id
			String bdcdyid=GetCompact_HInfo(fwbm,_compactNo, bdcdylx);
			//李堃添加,如果房产库中房屋编码变了，取新的fwbm并更新到relationid
//			logger.info("不动产单元id："+bdcdyid);
			if(bdcdyid==null||"".equals(bdcdyid)){
				String sql="select ID,BGQID,YWH from BDCK.V_BGQID where  id='"+fwbm+"' ";
//				logger.info(sql);
				List<Map> bgqidTbList=baseCommonDao.getDataListByFullSql(sql);
//				logger.info("获取老id...");
				if(bgqidTbList!=null&&bgqidTbList.size()>0){
					String oldfwbm=bgqidTbList.get(0).get("BGQID").toString();
//					logger.info("老id是："+newfwbm);
					bdcdyid=GetCompact_HInfo(oldfwbm,_compactNo, bdcdylx);
//					logger.info("获取到不动产单元id："+bdcdyid);
//					logger.info(bdcdylx);
//					if("031".equals(bdcdylx)){
						List<BDCS_H_XZ> h_XZs= baseCommonDao.getDataList(BDCS_H_XZ.class, "bdcdyid='"+bdcdyid+"'");
//						logger.info("户现状："+h_XZs.size()+"，新房屋编码："+fwbm);
						if(h_XZs!=null &&h_XZs.size()>0){
//							logger.info("更新开始");
							BDCS_H_XZ h_XZ=h_XZs.get(0);
							h_XZ.setRELATIONID(fwbm);
							baseCommonDao.saveOrUpdate(h_XZ);
							baseCommonDao.flush();
//							logger.info("更新完成");
						}
						else {
							List<BDCS_H_XZY> h_XZys= baseCommonDao.getDataList(BDCS_H_XZY.class, "bdcdyid='"+bdcdyid+"'");
//							logger.info("户现状预："+h_XZs.size());
							if(h_XZys!=null &&h_XZys.size()>0){
								BDCS_H_XZY h_XZ=h_XZys.get(0);
								h_XZ.setRELATIONID(fwbm);
								baseCommonDao.saveOrUpdate(h_XZ);
								baseCommonDao.flush();
							}
						}
						
				}
			}
			//加载不动产单元 ，检查当前的限制条件
			reslutMessage =dyService.addBDCDY(_xmbh, bdcdyid) ;		
			//警告，可以继续加载登记单元
			if (reslutMessage.getSuccess().equals("warning")) {
				//不进行检查
				dyService.addBDCDYNoCheck(_xmbh, bdcdyid) ;	
			}
			if(reslutMessage.getSuccess().equals("true")||reslutMessage.getSuccess().equals("warning"))
			{
				//获取登记单元信息
				String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID=''{1}''", _xmbh, bdcdyid);
				List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
			    //加载权利信息
				Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, _xmbh,listdjdy.get(0).getDJDYID());
				//输出不动产单元 以备查证
				System.out.println(_rights.getId());
				
				//从合同中间库中加载申请人的信息
				Object[] sqrids=UpadateCompact_OwnerInfo(_xmbh,fwbm,ywh,bdcdylx);		
				//有申请人类型是“权利人”的则将这些申请人增加到权利人中
				if(sqrids.length>0)
				{
					DJHandler handler = HandlerFactory.createDJHandler(_xmbh);
					if (_rights != null) {
						handler.addQLRbySQRArray(_rights.getId(), sqrids);
					}
				}
				//获取更新的工作权利信息的取得价格信息
				//判空，循环，更新
				String StrQLSql=MessageFormat.format("XMBH=''{0}''", _xmbh);
				List<BDCS_QL_GZ> ql_GZs =baseCommonDao.getDataList(BDCS_QL_GZ.class, StrQLSql);
				if(ql_GZs!=null&&ql_GZs.size()>0)
				{						
					for (int k=0;k<ql_GZs.size();k++)
					{
						BDCS_QL_GZ qlGz=ql_GZs.get(k);
						qlGz.setQDJG(qdjg);
						//保存房产业务号
						qlGz.setCASENUM(_compactNo);
						baseCommonDao.update(qlGz);
						baseCommonDao.flush();
					}				
				}
				flag = true;
			}				
			if(flag)
			{
				//如果是警告，增加提示信息
				if(reslutMessage.getSuccess().equals("warning"))
				{
					reslutMessage.setSuccess("true");
					reslutMessage.setMsg("合同读取成功,警告！\r\n"+reslutMessage.getMsg());
				}
				else {
					reslutMessage.setSuccess("true");
					reslutMessage.setMsg("合同读取成功");
				}
			}
			else
			{
				if(reslutMessage.getSuccess().equals("true"))
				{
					reslutMessage.setMsg("合同号有误!");
					reslutMessage.setSuccess("false");
				}
			}
		}
		else
		{
			reslutMessage.setMsg("没有读取到合同信息，查证合同号是否正确");
			reslutMessage.setSuccess("false");
		}
		return reslutMessage;
	}
	/**
	 * 根据合同编号中的关联字段找到合同买卖房屋的信息
	 * @author lxk
	 * @CreateTime 2015年10月30日15:50:25
	 * @param _id 关联编号
	 * @return
	 */	
	private String GetCompact_HInfo(String _fwbm,String _compactNo,BDCDYLX _bdcdylx) {
		String BDCDYID="";
		String hqlSql=" FWBM= '"+_fwbm+"'";
		List<V_H> listH=baseCommonDao.getDataList(V_H.class, hqlSql);
		if(listH!=null && listH.size()>0)
		{
			//暂时不靠虑多个不动产单元买卖的情况
			//for(int i =0;i<listH.size();i++)
			//{			
				//组织数据 
				String fwbm=listH.get(0).getFWBM();
				String sql=" FWBM='"+fwbm+"'";		
				if(_compactNo.contains("XS"))
				{
					List<BDCS_H_XZ> listHXZ=baseCommonDao.getDataList(BDCS_H_XZ.class, sql);
					if(listHXZ.size()>0)
					{
						BDCS_H_XZ h_xz=listHXZ.get(0);
						BDCDYID=h_xz.getId();
					}
				}
				else if(_compactNo.contains("YS"))
				{
					List<BDCS_H_XZY> listHXZ=baseCommonDao.getDataList(BDCS_H_XZY.class, sql);
					if(listHXZ.size()>0)
					{
						BDCS_H_XZY h_y=listHXZ.get(0);
						BDCDYID=h_y.getId();
					}
					//如果预告合同，实际办理的现状的首次、转移等业务，则根据YC_SC_XZ关系表进行找到现状的不动产单元的ID
					if(_bdcdylx.Value.equals("031"))
					{
						//判断预测实测关系，如果存在则读取现状的id
						String tempSqlString="ycbdcdyid = '"+BDCDYID+"'";;
						List<YC_SC_H_XZ> listYCSC = baseCommonDao.getDataList(YC_SC_H_XZ.class, tempSqlString);
						if(listYCSC!=null&&listYCSC.size()>0)
						{						
							YC_SC_H_XZ yc_sc =listYCSC.get(0);
							BDCDYID=yc_sc.getSCBDCDYID();
						}
					}
					//判断在历史中是否存在该房屋，如果存在则已经做过登记，不允许进行登记
//					if(!BDCDYID.isEmpty())
//					{
//						String exitSql=" FROM BDCK.BDCS_DJDY_LS DJDY LEFT JOIN BDCK.BDCS_QL_LS QL  ON QL.DJDYID = DJDY.DJDYID    WHERE    QL.DJLX='200' and DJDY.BDCDYID =="+BDCDYID;
//						Long count=baseCommonDao.getCountByFullSql(exitSql);
//						if(count>0)
//						{
//							BDCDYID="2";//记录一个标识，说明已经登记做转移登记
//						}
//					}
//					
					
				}
				
			//}
		}		
		return BDCDYID;
	}
	/**
	 * 根据合同编号的关联字段找到具体合同买卖人员的信息
	 * @author lxk
	 * @CreateTime 2015年10月30日15:50:09
	 * @param _xmbh 项目编号
	 * @param id 关联编号
	 * @param _bdcdylx 不动产单元类型，目前只判断是[期房:YCH][现房:H]
	 * @return
	 */
	public Object[] UpadateCompact_OwnerInfo(String _xmbh,String _fwbm,String _ywh,BDCDYLX _bdcdylx) {
		Object[] sqrids=null;
		//为空判断
		if(_xmbh==null||_fwbm==null)
		{
			return sqrids;
		};
		List<String> listsqrid = null;
		String hqlSql=" FWBM = '"+_fwbm +"' and ywh='"+_ywh+"'";
		
		List<V_QLR> listOwner=baseCommonDao.getDataList(V_QLR.class, hqlSql);
		if(listOwner!=null && listOwner.size()>0)
		{
			for(int i=0;i<listOwner.size();i++)
			{
				//权利人的信息读取并保存到数据库中
				/**
				 * 1、权利人姓名
				 * 2、性别
				 * 3、申请人类别：权利人、义务人
				 * 4、申请人类型:个人、企业、事业单位、国家机关、其他
				 * 5、证件种类：身份证、其他、护照、户口薄、军官证、组织机构代码、营业执照、港澳台身份证
				 * 6、证件号码
				 * 7、电话号码
				 * 8、共有方式
				 * 9、地址
				 */
				V_QLR temp_qlr=listOwner.get(i);			
				BDCS_SQR sqr = new BDCS_SQR();
                //1、权利人姓名
				String qlrName=temp_qlr.getQLRMC();			
				//2、性别--暂时无处可以取得				
				//3、申请人类别：1：权利人:2：义务人 			
				String qlrlx=temp_qlr.getQLRLX();
				//卖方、卖方
				String sqrlb=temp_qlr.getQLRFL();	
				// 5、证件种类：1:身份证、2:港澳台身份证、3:护照、4:户口薄、5:军官证、6:组织机构代码、7:营业执照、99:其他				
				String zjlx=temp_qlr.getZJZL();
			    // 6、证件号码
				String zjh=temp_qlr.getZJH();
				// 7、电话号码  
				String sqrdh=temp_qlr.getDH();
				// 8、共有方式  
				String gyfs= temp_qlr.getGYFS();
				// 9、地址
				String zl=temp_qlr.getDZ();
				//__赋值
				//1、权利人姓名
				if(qlrName!=null&&!qlrName.equals(""))
				{
				sqr.setSQRXM(qlrName);
				}
				//2、性别
				sqr.setXB("3");
				//3、申请人类别  义务人、权利人
				String lb="";
				if(sqrlb!=null&&!sqrlb.equals(""))
				{
					if(sqrlb.contains("买受人"))
					{
						lb="1";
					}
					else if(sqrlb.contains("出卖人"))
					{
						lb="2";
					}
					sqr.setSQRLB(lb);
				}
				
				//4、申请人类型:1:个人、2:企业、3:事业单位、4:国家机关、5:其他
				if(qlrlx!=null&&!qlrlx.equals(""))
				{
					sqr.setSQRLX(qlrlx);
				}
				// 5、证件种类：1:身份证、2:港澳台身份证、3:护照、4:户口薄、5:军官证、6:组织机构代码、7:营业执照、99:其他
				if(zjlx!=null&&!zjlx.equals(""))
				{
					sqr.setZJLX(zjlx);
				}
				// 6、证件号码
				if(zjh!=null&&!zjh.equals(""))
				{
					sqr.setZJH(zjh);
				}
				//7、申请人电话
				if(sqrdh!=null&&!sqrdh.equals(""))
				{
					sqr.setLXDH(sqrdh);
				}
				// 8、共有方式
				if(gyfs!=null&&!gyfs.equals(""))
				{
					sqr.setGYFS(gyfs);
				}
				//9、联系人地址
				if(zl!=null && !zl.equals(""))
				{
					sqr.setTXDZ(zl);
				}
				sqr.setXMBH(_xmbh);
				baseCommonDao.save(sqr);
				baseCommonDao.flush();
				if(listsqrid==null)
				{
					listsqrid= new ArrayList<String>();
				}
//				//现状只读取买房人的信息
//				if(_bdcdylx.Name.equals("户"))//期现房的判断
//				{
					//将申请人类型为买方的增加到数组中，以备后面将买方增加到权利人当中
					if(lb!=null&&lb.equals("1"))
					{
						listsqrid.add(sqr.getId());		
					}
//				}
//				else//其他的将数据全部读取出来
//				{
//					listsqrid.add(sqr.getId());						
//				}
				
			}	
			if(listsqrid!=null&&listsqrid.size()>0)
			{
				sqrids=listsqrid.toArray();
			}
		}		
		return sqrids;
	}
	/**
	 * 根据项目编号和合同编号读取合同信息并且同时更新到不动产库中
	 * @author lxk
	 * @CreateTime 2015年10月30日15:49:52
	 * @param xmbh
	 * @param compactNo
	 * @return
	 */	
	@Override
	public ResultMessage ReadCompactInfo(String _xmbh, String _compactNo) {
		ResultMessage reslut=new ResultMessage();		
		reslut=GetCompactInfobyCompactNo(_xmbh,_compactNo);
		return reslut;
	}
	/**
	 * 验证该合同号是否已经抽取过
	 * @作者 李堃
	 * @创建时间 2017年5月23日下午8:55:38
	 * @param hth
	 * @return
	 */
	boolean checkAbstracted(String hth){
		boolean b=false;
		String sql="CASENUM='"+hth+"'";
		List<BDCS_QL_LS> listHXZ=baseCommonDao.getDataList(BDCS_QL_LS.class, sql);
		if(listHXZ.size()>0)
		{
			b=true;
		}
		return b;
	}
}

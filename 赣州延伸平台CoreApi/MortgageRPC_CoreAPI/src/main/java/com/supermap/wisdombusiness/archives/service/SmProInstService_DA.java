/**
 * 
 * 代码生成器自动生成[WFI_PROINST]
 * 
 */

package com.supermap.wisdombusiness.archives.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import oracle.sql.CLOB;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.mchange.v1.lang.NullUtils;
import com.supermap.realestate.registration.ViewClass.DJFZXX;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.XMLService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.gxjyk.PROMATER;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_Abnormal;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_NowActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_PassWork;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spdy;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;
import com.supermap.wisdombusiness.workflow.model.Wfi_TurnList;
import com.supermap.wisdombusiness.workflow.model.vProInstInfo;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProUserInfo;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.QueryCriteria;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmQueryPara;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmAbnormal;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmRouteInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;
import com.supermap.wisdombusiness.workflow.util.SqlFactory;
import com.supermap.wisdombusiness.workflow.web.wfi.ProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;

@Service("smProInstService_DA")
public class SmProInstService_DA {
	@Autowired
	private SqlFactory sqlFactory;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmProMater _SmProMater;
	@Autowired
	private SmProInst _SmProInst;
	@Autowired
	private SmActInst _smActInst;
	@Autowired
	private SmRouteInst _smRouteInst;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SmAbnormal smAbnormal;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private SmProMaterService ProMaterService;
	@Autowired
	private ProjectServiceImpl projectServiceImpl;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private ZSService zsService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private XMLService xmlService;
	@Autowired
	private SmProDef smProdef;
	@Autowired
	private proinstStateModify proinstStateModify;

	/**
	 * mass 按照项目编号获取项目信息
	 * 
	 */
	public SmProInfo GetProjectInfoByFileNumber(String FileNumber) {
		SmProInfo ProInfo = new SmProInfo();
		Wfi_ProInst proindtInst = _SmProInst.GetProInstByFileNumber(FileNumber);
		if (proindtInst != null) {
			_SmProInst.ProInfoFromProInst(ProInfo, proindtInst);
		}
		return ProInfo;
	}

	/**
	 * mass
	 * 
	 * @return
	 */
	public List<Map> GetAllProjectByActinstName() {
		String str = "select actinst_id,actinst_name,file_number from (select distinct proinst_id, actinst_id,actinst_name from "
				+ Common.WORKFLOWDB + "wfi_actinst where actinst_name='归档') act";
		str += " left join " + Common.WORKFLOWDB
				+ "wfi_proinst pro  on  pro.proinst_id=act.proinst_id where pro.proinst_status=0";
		return commonDao.getDataListByFullSql(str);
	}

	/**
	 * lixin修改  20190110
	 * 验证项目是否经过归档环节,是否登簿
	 * @param lsh
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map VerifyDossier(String lsh, User user) {
//		验证是否登簿环节
		StringBuilder strxmxx = new StringBuilder();
		strxmxx.append("SELECT YWLSH, DJLX,QLLX,PROJECT_ID,XMBH,SFDB FROM ");
		strxmxx.append("BDCK.");
		strxmxx.append("BDCS_XMXX WHERE  YWLSH ='");
		strxmxx.append(lsh);
		strxmxx.append("'");
		strxmxx.append("OR PROJECT_ID='");
		strxmxx.append(lsh);
		strxmxx.append("'");
		List<Map> listXMXX = commonDao.getDataListByFullSql(strxmxx.toString());
		if(listXMXX==null||listXMXX.size()<=0
				||listXMXX.get(0).get("SFDB")==null||StringHelper.isEmpty(listXMXX.get(0).get("SFDB").toString())||listXMXX.get(0).get("SFDB").equals("0")) {
			return new HashMap();
		}
		lsh=listXMXX.get(0).get("YWLSH")+"";
		
//		验证是否经过归档环节,不区分环节状态

		
		StringBuilder sb = new StringBuilder();
		sb.append("select pinst.proinst_status,pinst.project_name,ainst.actinst_id from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfi_proinst pinst left join " + Common.WORKFLOWDB
				+ "wfi_actinst ainst on pinst.proinst_id=ainst.proinst_id  where ");
//		sb.append("ainst.actinst_status in (");
//		sb.append(
//				WFConst.Instance_Status.Instance_NotAccept.value + "," + WFConst.Instance_Status.Instance_doing.value);
//		sb.append(") and ");
		sb.append(" ainst.actinst_name like'%归档%' and pinst.prolsh ='");
		sb.append(lsh);
		sb.append("'");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());
		if (list != null && list.size() > 0) {
			Map item = list.get(0);
			Wfi_ActInst inst = commonDao.get(Wfi_ActInst.class, item.get("ACTINST_ID").toString());
			if (inst != null) {
				inst.setStaff_Id(user.getId());
				inst.setStaff_Name(user.getUserName());
				inst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
				commonDao.update(inst);
				commonDao.flush();
			}
			//数据处理
			if (listXMXX != null && listXMXX.size() > 0) {
				Map mapxmxx = listXMXX.get(0);
				String djlx = mapxmxx.get("DJLX").toString();
				String qllx = mapxmxx.get("QLLX").toString();
				//String filenumber = mapxmxx.get("PROJECT_ID").toString();
				String xmbh = mapxmxx.get("XMBH").toString();
				if ((DJLX.ZXDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx))
						|| (DJLX.CFDJ.Value.equals(djlx) && QLLX.CFZX.Value.equals(qllx))) {
					StringBuilder djdystr = new StringBuilder();
					djdystr.append("select * from ");
					djdystr.append("BDCK.");
					djdystr.append("BDCS_DJDY_GZ where  XMBH ='");
					djdystr.append(xmbh);
					djdystr.append("'");
					List<Map> djdys = commonDao.getDataListByFullSql(djdystr.toString());
					if (djdys.size() > 0 && djdys != null) {
						Map djdy = djdys.get(0);
						String djdyid = djdy.get("DJDYID").toString();
						StringBuilder qlstr = new StringBuilder();
						qlstr.append("select * from ");
						qlstr.append("BDCK.");
						qlstr.append("BDCS_QL_GZ where  DJDYID ='");
						qlstr.append(djdyid);
						qlstr.append("'");
						qlstr.append("and xmbh='");
						qlstr.append(xmbh);
						qlstr.append("'");
						List<Map> qls = commonDao.getDataListByFullSql(qlstr.toString());
						if (qls.size() > 0 && qls != null) {
							Map ql = qls.get(0);
							String lyqlid = ql.get("LYQLID").toString();

							StringBuilder lyqlstr = new StringBuilder();
							lyqlstr.append("select * from ");
							lyqlstr.append("BDCK.");
							lyqlstr.append("BDCS_QL_LS where  QLID ='");
							lyqlstr.append(lyqlid);
							lyqlstr.append("'");
							List<Map> lyqls = commonDao.getDataListByFullSql(lyqlstr.toString());
							if (lyqls.size() > 0 && lyqls != null) {
								String lyxmbh = null;
								Map lyql = lyqls.get(0);
								if (lyql.get("XMBH") != null) {
									lyxmbh = lyql.get("XMBH").toString();
								} else {
									return item;
								}

								StringBuilder bdcxmxx = new StringBuilder();
								bdcxmxx.append("select * from ");
								bdcxmxx.append("BDC_DAK.");
								bdcxmxx.append("DAS_BDC where  XMBH ='");
								bdcxmxx.append(lyxmbh);
								bdcxmxx.append("'");
								List<Map> listBDCXMXX = commonDao.getDataListByFullSql(bdcxmxx.toString());
								if (listBDCXMXX.size() > 0) {
									return item;
								}

								StringBuilder strlyxmxx = new StringBuilder();
								strlyxmxx.append("select * from ");
								strlyxmxx.append("BDCK.");
								strlyxmxx.append("BDCS_XMXX where  XMBH ='");
								strlyxmxx.append(lyxmbh);
								strlyxmxx.append("'");
								List<Map> listLYXMXX = commonDao.getDataListByFullSql(strlyxmxx.toString());
								if (listLYXMXX != null && listLYXMXX.size() > 0) {
									Map lyxmxx = listLYXMXX.get(0);
									String lyywlsh = lyxmxx.get("YWLSH").toString();
									item.put("proinst_status", item.get("PROINST_STATUS").toString());
									item.put("lyywlsh", lyywlsh);
								}

							}

						}
					}
				}
				item.put("djlx", djlx);
			}

			return item;
		} else {
			return new HashMap();
		}

	}

	/**
	 * mass
	 * 
	 * @param staffid
	 * @return
	 */
	public long GetWaitDossierCount(String staffid) {
		long Result = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("v_projectlist");
		sb.append(" where actstaff='" + staffid + "'");
		sb.append(" and(staff_id is null or staff_id='" + staffid + "') ");
		sb.append(" and actinst_name='归档' and actinst_status in (1,2)");
		Result = commonDao.getCountByFullSql(sb.toString());
		return Result;

	}

	/**
	 * mass
	 * 
	 * @param actinstid
	 * @param file_number
	 * @return
	 */
	public Map worlflowDossier(String actinstid, String file_number) {
		Map result = new HashMap();
		List<Tree> materils = ProMaterService.GetMaterDataTree(file_number, "true");
		result.put("project", GetProjectInfo(actinstid));
		result.put("materil", materils);
		return result;

	}

	/**
	 * mass
	 * 
	 * @param actdef_name
	 * @param status
	 * @return
	 */
	public Message getAllProjectByActName(String actdef_name, String status) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append("1>0 ");
		if (actdef_name != null && !actdef_name.equals("")) {
			str.append(" and ACTINST_NAME in (");
			str.append(actdef_name);
			str.append(" )");
		}
		if (status.equals("8")) {
			status = "1,2";
		}
		if (status != null && !"".equals(status)) {
			str.append(" and ACTINST_STATUS in (");
			str.append(status);
			str.append(")");
		}
		long tatalCount = commonDao.getCountByFullSql(
				" from (select  row_number() over(partition by file_number order by actinst_start desc"
						+ ") rn,file_number, project_name, PROINST_START,proinst_status,Staff_Name from "
						+ Common.WORKFLOWDB + "V_PROJECTLIST where " + str.toString() + ") where rn=1  ");
		msg.setTotal(tatalCount);
		if (tatalCount > 0) { //
			str.append(" order by ACTINST_START  desc");
			msg.setRows(commonDao.getDataListByFullSql(
					"select * from (select  row_number() over(partition by file_number order by actinst_start desc"
							+ ") rn,file_number,actinst_start,proinst_id,prodef_name,project_name,proinst_status,actinst_id,PROINST_START,Staff_Name,actinst_name,prolsh,actinst_status,codeal,urgency,actinst_willfinish,proinst_willfinish,proouttime,operation_type   from "
							+ Common.WORKFLOWDB + "V_PROJECTLIST where " + str.toString() + " ) where rn=1 "));
		}
		return msg;
	}

	/**
	 * mass 获取单个项目的详细信息
	 */
	public SmProInfo GetProjectInfo(String actinst) {
		Wfi_ActInst ActInst = commonDao.load(Wfi_ActInst.class, actinst);
		SmProInfo ProInfo = new SmProInfo();
		if (ActInst != null) {
			Wfi_ProInst ProInst = commonDao.load(Wfi_ProInst.class, ActInst.getProinst_Id());
			Wfd_Prodef prodef = smProdef.GetProdefById(ProInst.getProdef_Id());
			if (ProInst != null) {
				Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class, ActInst.getActdef_Id());
				_SmProInst.ProInfoFromProInst(ProInfo, ProInst);
				_smActInst.ProInfoFromActInst(ProInfo, ActInst);
				// ProInfo.setDefinedCodeal(_smActInst.GetActDef(actinst).getCodeal());
				ProInfo.setDefinedCodeal(actdef.getCodeal());
				ProInfo.setIsReturnAct(actdef.getIsReturnAct() == null ? 0 : actdef.getIsReturnAct());
				ProInfo.setActDef_updaloadfile(actdef.getUploadfile() == null ? 0 : actdef.getUploadfile());
				// 按钮权限控制
				ProInfo.setBtnPermission(actdef.getBtnPermission() == null ? "" : actdef.getBtnPermission());
				ProInfo.setProdef_Tpl(prodef.getProdef_Tpl());
			}
		}
		return ProInfo;
	}

	public Wfi_ProInst getInfoByLsh(String lsh) {
		List<Wfi_ProInst> insts = commonDao.getDataList(Wfi_ProInst.class,
				"select * from " + Common.WORKFLOWDB + "Wfi_ProInst where prolsh='" + lsh + "'");
		if (insts != null && insts.size() > 0) {
			return insts.get(0);
		} else {
			return null;
		}
	}

	public Map<String, Object> getArchivesInfos(String file_number) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询项目信息：
		SmProInfo info = GetProjectInfoByFileNumber(file_number);
		if (info != null) {
			result.put("project", GetProjectInfoByFileNumber(file_number));
			List<Wfi_ProMater> folders = commonDao.getDataList(Wfi_ProMater.class, "select * from " + Common.WORKFLOWDB
					+ "wfi_promater where proinst_id='" + info.getProInst_ID() + "' and  material_status>1");
            List<Map<String,Object>> folderlist=new ArrayList<Map<String,Object>>();
			if (folders != null && folders.size() > 0) {

				for (Wfi_ProMater mater : folders) {
					Map<String,Object> folder=new HashMap();
					folder.put("id", mater.getMaterilinst_Id());
					folder.put("sxh", mater.getDossier_Index()!=null?mater.getDossier_Index():mater.getMaterial_Index());
					folder.put("wjmc", mater.getMaterial_Name());
					folder.put("bz", mater.getMaterial_Desc());
					folder.put("lyid", mater.getMaterial_Id());
					folder.put("count", mater.getMaterial_Count());
					List<Wfi_MaterData> datas = commonDao.getDataList(Wfi_MaterData.class,
							"select * from " + Common.WORKFLOWDB + "Wfi_MaterData where materilinst_id='"
									+ mater.getMaterilinst_Id() + "'");
                    if(datas!=null&&datas.size()>0){
                    	List<Map<String,Object>> files=new ArrayList<Map<String,Object>>();
                    	for(Wfi_MaterData d:datas){
                    		Map<String,Object> file=new HashMap();
                    		file.put("pid", d.getMaterilinst_Id());
                    		file.put("fjmc",d.getFile_Name());
                    		file.put("id", d.getMaterialdata_Id());
                    		file.put("path", d.getPath());
                    		file.put("yh", d.getFile_Index());
                    		file.put("disc", d.getDisc());
                    		files.add(file);
                    	}
                    	folder.put("files", files);
                    }
                   
                    folderlist.add(folder);
				}
				result.put("folders", folderlist);
			}

		}

		return result;
	}

}

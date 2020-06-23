package com.supermap.wisdombusiness.workflow.service.wfm;

import com.supemap.mns.client.AsyncCallback;
import com.supemap.mns.client.AsyncResult;
import com.supemap.mns.client.CloudHttp;
import com.supemap.mns.common.HttpMethod;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.FileTool;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.archives.web.ArchivesController;
import com.supermap.wisdombusiness.archives.web.common.Basic;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.*;
import com.supermap.wisdombusiness.workflow.service.common.*;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.util.*;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service("SmMaterialService")
public class SmMaterialService {
	public Logger logger = Logger.getLogger(ArchivesController.class);
	@Autowired
	private SqlFactory sqlFactory;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmStaff smstaff;
	@Autowired
	private SmActInst smActInst;
	@Autowired
	private SmProInst smProinst;
	@Autowired
	private SmProMater smProMater;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private SmProInstService smProInstService;
	@Autowired
	private UserService userService;
	private static CloudHttp http = new CloudHttp();
	// 获取档案信息
	private static final String getajinfo = "pigeonhole";
	/**
	 * 获取归档项目  分页
	 * 
	 * @param Status
	 * @param staff_id
	 * @return
	 */
	public Message GetFileProject(String Status, String staff_id, int page, int size, String key, String actinstname,
			String staffs, String proname, String starttime, String endtime, String deptname, String areas, String hk,
			String lshstart, String lshend, String actinstids,String sorttype) {

		Map<String, String> sqlmap = new HashMap<String, String>();
		Message msg = new Message();
		if(!"".equals(key)){
			List<Map> list = null;
			//通过WFI_PROINST,WFI_ACTINST,BDCS_SQR,不动产单元表等
			Map datas = new HashMap();
			String sqlywh = "";
			if(key.contains("-") && key.length() > 11){
				sqlywh = " FILE_NUMBER = '"+key+"' ";
			}else{
				sqlywh = " PROLSH = '"+key+"' ";
			}
			List<Wfi_ProInst> pros = commonDao.getDataList(Wfi_ProInst.class, 
					" SELECT * FROM BDC_WORKFLOW.WFI_PROINST WHERE  " + sqlywh);
			if(pros.size() > 0){
				list = new ArrayList<Map>();
//				item.PROJECT_NAME
//				item.PROINST_START
//				item.PROINST_ID
//				item.XH ??
//				item.FILE_NUMBER
//				item.PRODEF_NAME
//				item.PROLSH
//				item.ACCEPTOR
//				item.ISTRANSFER
//				item.TRANSFER_TIME
				String proinstid = pros.get(0).getProinst_Id();
				datas.put("PROJECT_NAME", pros.get(0).getProject_Name());
				datas.put("PROINST_START", pros.get(0).getProinst_Start());
				datas.put("PROINST_ID",  pros.get(0).getProinst_Id());
				datas.put("FILE_NUMBER",  pros.get(0).getFile_Number());
				datas.put("PRODEF_NAME",  pros.get(0).getProdef_Name());
				datas.put("PROLSH",  pros.get(0).getProlsh());
				datas.put("TRANSFER_TIME", pros.get(0).getTransfer_Time());
				datas.put("ACCEPTOR",  pros.get(0).getAcceptor());
				datas.put("ISTRANSFER",  pros.get(0).getIstransfer());
				datas.put("XH", "");
				List<Wfi_ActInst> acts = commonDao.getDataList(Wfi_ActInst.class, 
						" SELECT * FROM BDC_WORKFLOW.Wfi_ActInst WHERE  PROINST_ID = '"+proinstid+"' ORDER BY ACTINST_START DESC ");
				if(acts.size() > 0){
//					item.ACTINST_ID
//					item.ACTINST_NAME
					if(acts.get(0).getStaff_Name() == null){
						if(acts.size() > 1){
							datas.put("STAFFNAME", acts.get(1).getStaff_Name());
						}else{
							datas.put("STAFFNAME", "");
						}
					}else{
						datas.put("STAFFNAME", acts.get(0).getStaff_Name());
					}
					datas.put("ACTINST_ID",  acts.get(0).getActinst_Id());
					datas.put("ACTINST_NAME",  acts.get(0).getActinst_Name());
				}else{
					datas.put("STAFFNAME","");
					datas.put("ACTINST_ID", "");
					datas.put("ACTINST_NAME", "");
				}
				List<Map> bdcywxxs = commonDao.getDataListByFullSql(" "
						+ " select dj.bdcdylx,xx.xmbh,dj.bdcdyid,dj.djdyid,dj.ly from bdck.bdcs_xmxx xx inner join bdck.bdcs_djdy_gz dj "
						+ " on xx.xmbh = dj.xmbh  "
						+ " where xx.ywlsh = '"+datas.get("PROLSH")+"'  ");
				if(bdcywxxs.size() > 0){
					String xmbh = StringUtil.valueOf(bdcywxxs.get(0).get("XMBH"));
					String bdcdylx = StringUtil.valueOf(bdcywxxs.get(0).get("BDCDYLX"));
					String bdcdyid = StringUtil.valueOf(bdcywxxs.get(0).get("BDCDYID"));
					String djdyid = StringUtil.valueOf(bdcywxxs.get(0).get("DJDYID"));
					String ly = StringUtil.valueOf(bdcywxxs.get(0).get("LY"));
//				item.ZL
//				item.QLR
//				item.YWR
//				item.BDCQZH
					List<BDCS_SQR> qlrs = commonDao.getDataList(BDCS_SQR.class, ""
							+ " select * from bdck.bdcs_sqr where sqrlb = '1' and xmbh = '"+xmbh+"' " );
					if(qlrs.size() > 0){
						String qlrxms = "";
						for(BDCS_SQR qlr : qlrs){
							qlrxms += qlr.getSQRXM()+",";
						}
						if(qlrxms.contains(",")){
							qlrxms = qlrxms.substring(0, qlrxms.length()-1);
						}
						datas.put("QLR", qlrxms);
					}else{
						datas.put("QLR", "");
					}
					List<BDCS_SQR> ywrs = commonDao.getDataList(BDCS_SQR.class, ""
							+ " select * from bdck.bdcs_sqr where sqrlb = '2'  and xmbh = '"+xmbh+"' " );
					if(ywrs.size() > 0){
						String ywrxms = "";
						for(BDCS_SQR ywr:ywrs){
							ywrxms += ywr.getSQRXM()+",";
						}
						if(ywrxms.contains(",")){
							ywrxms = ywrxms.substring(0, ywrxms.length()-1);
						}
						datas.put("YWR", ywrxms);
					}else{
						datas.put("YWR", "");
					}
					String tabelsqr = StringUtil.getBDCDYLXTable(bdcdylx, ly);
					
					List<Map> bdcdys = commonDao.getDataListByFullSql(" select ZL from bdck."+tabelsqr+""
							+ " where bdcdyid = '"+bdcdyid+"' ");
					if(bdcdys.size() > 0){
						datas.put("ZL", bdcdys.get(0).get("ZL"));
					}else{
						datas.put("ZL", "");
					}
					
					List<BDCS_QL_XZ> qlxzs = commonDao.getDataList(BDCS_QL_XZ.class, ""
							+ " select * from bdck.BDCS_QL_XZ where xmbh = '"+xmbh+"' and djdyid = '"+djdyid+"' " );
					if(qlxzs.size() > 0){
						datas.put("BDCQZH", qlxzs.get(0).getBDCQZH());
					}else{
						datas.put("BDCQZH","");
					}
				}else{
					datas = null;
				}
			}else{
				datas = null;
			}
			
			if(datas != null){
				list.add(datas);
			}else{
				list = null;
			}
			
			msg.setRows(list);
			if(list == null){
				msg.setTotal(0);
			}else{
				msg.setTotal(list.size());
			}
		}else{
			sqlmap = sqlFactory.getFileProjectSQL(Status, staff_id, page, size, key, actinstname, staffs, proname,
					starttime, endtime, deptname, areas, hk, lshstart, lshend, actinstids,sorttype);
			long count = commonDao.getCountByFullSqlCustom(sqlmap.get("COUNT"));
			if (count > 0) {
				msg.setTotal(count);
				List<Map> list = commonDao.getDataListByFullSql(sqlmap.get("CONTENT"), page, size);
				long startTime = System.currentTimeMillis();
				if (hk == null || hk.equals("")) {
					list = ContatData(list, false);
				}
				// 预登记系统连接获取登记信息
				long endTime = System.currentTimeMillis();
				System.out.println(endTime - startTime);
				msg.setRows(list);
			}
		}

		return msg;
	}
	/**
	 * 获取归档项目  不分页
	 * 
	 * @param Status
	 * @param staff_id
	 * @return
	 */
	public Message GetFileProjectNotPage(String Status, String staff_id, int page, int size, String key, String actinstname,
			String staffs, String proname, String starttime, String endtime, String deptname, String areas, String hk,
			String lshstart, String lshend, String actinstids,String sorttype) {

		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap = sqlFactory.getFileProjectSQL(Status, staff_id, page, size, key, actinstname, staffs, proname,
				starttime, endtime, deptname, areas, hk, lshstart, lshend, actinstids,sorttype);
		Message msg = new Message();
		long count = commonDao.getCountByFullSqlCustom(sqlmap.get("COUNT"));
		if (count > 0) {
			msg.setTotal(count);
			List<Map> list = commonDao.getDataListByFullSql(sqlmap.get("CONTENT"));
			long startTime = System.currentTimeMillis();
			if (hk == null || hk.equals("")) {
				list = ContatData(list, false);
			}
			// 预登记系统连接获取登记信息
			long endTime = System.currentTimeMillis();
			System.out.println(endTime - startTime);
			msg.setRows(list);
		}

		return msg;
	}

	public Message GetFileProject(String Status, String staff_id, int page, int size, String key, String actinstname,
			String staffs, String proname, String starttime, String endtime, String deptname, String areas, String hk,
			String lshstart, String lshend) {
		return GetFileProject(Status, staff_id, page, size, key, actinstname, staffs, proname, starttime, endtime,
				deptname, areas, hk, lshstart, lshend, null,"1");
	}

	/**
	 * 和登记系统对接
	 * 
	 * @param list
	 * @param ajh
	 *            是否获取案卷号
	 * @return
	 */
	private List<Map> ContatData(List<Map> list, boolean ajh) {
		String filenumber = "";
		for (Map m : list) {
			String FILE_NUMBER = m.get("FILE_NUMBER").toString();
			String PROLSH = "";
			if (m.get("PROLSH") != null) {
				PROLSH = m.get("PROLSH").toString();
			}

			if (ajh && !PROLSH.equals("")) {
				List<WFI_TRANSFERLIST> dosserlist = commonDao.getDataList(WFI_TRANSFERLIST.class,
						"select * from " + Common.WORKFLOWDB + "WFI_TRANSFERLIST where lsh='" + PROLSH + "'");
				if (dosserlist != null && dosserlist.size() > 0) {
					m.put("DAH", dosserlist.get(0).getDah());
				}
			}
			Map plist = ProjectHelper.GetFileTransferInforDayj(FILE_NUMBER);

			m.putAll(plist);

		}
		return list;
	}

	/**
	 * 获取归档项目
	 * 
	 * @param actinstid
	 * @return
	 */
	public Message GetTransferInfo(String actinstid) {
		StringBuilder sqlfrom = new StringBuilder();
		Message msg = new Message();
		String sql = "select info.* ,pro.acceptor";
		sqlfrom.append(" from " + Common.WORKFLOWDB + "wfi_proinst pro ");
		sqlfrom.append("inner join ");
		sqlfrom.append(Common.WORKFLOWDB + "wfi_actinst act ");
		sqlfrom.append("on act.proinst_id=pro.proinst_id ");
		sqlfrom.append("left join ");
		sqlfrom.append(Common.WORKFLOWDB + "wfi_prouserinfo info ");
		sqlfrom.append("on pro.prolsh=info.file_number ");
		sqlfrom.append("where act.actinst_id in('" + actinstid + "')");
		long count = commonDao.getCountByFullSql(sqlfrom.toString());
		if (count > 0) {
			msg.setTotal(count);
			List<Map> list = commonDao.getDataListByFullSql(sql + sqlfrom.toString());
			msg.setRows(list);
		}
		return msg;
	}

	private List<Map> ServerContatData(List<Map> list, String ddh, boolean ajh) {
		String filenumber = "";
		for (Map m : list) {
			String FILE_NUMBER = m.get("FILE_NUMBER").toString();
			String PROLSH = "";
			if (m.get("PROLSH") != null) {
				PROLSH = m.get("PROLSH").toString();
			}
			Map plist = ProjectHelper.GetFileTransferInfoEx2(FILE_NUMBER);
			m.putAll(plist);
			if (ajh && !PROLSH.equals("")) {
				List<WFI_TRANSFERLIST> dosserlist = commonDao.getDataList(WFI_TRANSFERLIST.class,
						"select * from " + Common.WORKFLOWDB + "WFI_TRANSFERLIST where lsh='" + PROLSH
								+ "' and  transferid in (select transferid from " + Common.WORKFLOWDB
								+ "WFI_DOSSIERTRANSFER where ddh='" + ddh + "')");
				if (dosserlist != null && dosserlist.size() > 0) {
					m.put("DAH", dosserlist.get(0).getDah());

				}
			}
		}
		return list;
	}

	private Map getBDCXX(String file_number) {
		Map plist = ProjectHelper.GetFileTransferInfoEx(file_number);
		if (plist != null) {
			Object sqr = plist.get("SQR");
			String cqr = "";
			if (sqr != null) {
				cqr = sqr.toString();
				String[] sqls = sqr.toString().split(",");
				if (sqls != null && sqls.length > 0) {
					for (String r : sqls) {
						if (cqr.length() > r.length()) {
							cqr = r;
						}
					}
				}
			}
			plist.put("SQR", cqr);
		}
		return plist;
	}

	/**
	 * 根据调档号获取信息
	 * 
	 * @param ddh
	 * @return
	 */

	public List<Message> GetProjectByDHH(String ddh) {
		List<Message> msgs = new ArrayList<Message>();
		if (ddh != null && !ddh.equals("")) {
			ddh.replace('，', ',');
			String[] ddhs = ddh.split(",");
			if (ddhs.length > 0) {
				for (String d : ddhs) {
					Message msg = new Message();
					StringBuilder sb = new StringBuilder();
					sb.append("select * from ");
					sb.append(Common.WORKFLOWDB);
					sb.append("wfi_transferlist where  transferid in (");
					sb.append("select transferid from ");
					sb.append(Common.WORKFLOWDB + "WFI_DOSSIERTRANSFER");
					sb.append(" where ddh='");
					sb.append(d);
					sb.append("') order by create_time ");
					List<WFI_TRANSFERLIST> list = commonDao.getDataList(WFI_TRANSFERLIST.class, sb.toString());
					msg.setTotal(list.size());
					msg.setDesc(d);
					msg.setRows(list);
					msgs.add(msg);
				}
			}
		}
		return msgs;
	}

	/**
	 * 获取移交记录
	 * 
	 * @param staff_id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public Page GetTransferRecord(String staff_id, int pageIndex, int pageSize, int type, String dept, String start,
			String end, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(" fromstaff_id='");
		sb.append(staff_id);
		sb.append("' ");
		if (dept != null && !dept.equals("")) {
			sb.append(" and fromdept_name='" + dept + "'");
		}
		;
		if (start != null && !start.equals("")) {
			sb.append(" and SEND_TIME>to_date('" + start + "', 'yyyy-mm-dd hh24:mi:ss') ");
		}
		;
		if (end != null && !end.equals("")) {
			sb.append(" and SEND_TIME<to_date('" + end + "', 'yyyy-mm-dd hh24:mi:ss') ");
		}
		;
		if (!StringHelper.isEmpty(key)) {
			Wfi_ProInst proinst = smProInstService.GetProInstByLsh(key);
			if (null != proinst) {
				List<WFI_TRANSFERLIST> transferlist = commonDao.findList(WFI_TRANSFERLIST.class,
						" file_number = '" + proinst.getFile_Number() + "' ");
				String transferids = "'0'";
				for (WFI_TRANSFERLIST transfer : transferlist) {
					transferids += ",'" + transfer.getTransferid() + "'";
				}
				sb = new StringBuilder(" transferid in (" + transferids + ") ");
			} else {

				sb = new StringBuilder(" transfercode like('%" + key + "%')");
			}
		}
		sb.append(" and type=");
		sb.append(type);
		sb.append(" order by send_time desc");

		Page page = commonDao.GetPagedData(WFI_DOSSIERTRANSFER.class, pageIndex, pageSize, sb.toString());
		return page;
	}

	/**
	 * 移交资料
	 * 
	 * @param user
	 * @param filenumber
	 * @param projectname
	 * @param actinstname
	 * @param type
	 * @return
	 */
	@Transactional
	public String TransferMaterial(User user, String filenumber, String projectname, String actinstid,
			String actinstname, int type) {
		String Result = "";
		if (filenumber != null && !filenumber.equals("")) {
			String[] filenumbers = filenumber.split(";");
			String[] projectnames = projectname.split(";");
			String[] actinstnames = actinstname.split(";");
			String[] actinstids = actinstid.split(";");
			String id = actinstid.replace(";", "','");
			int count = filenumbers.length;
			List<WFI_TRANSFERLIST> transferlist = commonDao.findList(WFI_TRANSFERLIST.class,
					"actinst_id in('" + id + "')");
			List<String> actinstlist = new ArrayList<String>();
			for (WFI_TRANSFERLIST act : transferlist) {
				actinstlist.add(act.getACTINST_ID());
			}
			if (filenumbers.length > 0) {

				String Transferid = Common.CreatUUID();
				for (int i = 0; i < filenumbers.length; i++) {
					// 移除重复移交记录
					if (actinstlist.contains(actinstids[i])) {
						count--;
						continue;
					}
					WFI_TRANSFERLIST list = new WFI_TRANSFERLIST();
					list.setCreate_Time(new Date());
					list.setTransferlistid(Common.CreatUUID());
					list.setTransferid(Transferid);
					list.setFile_Number(filenumbers[i]);
					list.setProject_Name(projectnames[i]);
					list.setFromactinst_Name(actinstnames[i]);
					list.setACTINST_ID(actinstids[i]);
					list.setReceive_Status(WFConst.MateralStatus.NoReceive.value);
					commonDao.save(list);
					Wfi_ActInst actinst = smActInst.GetActInst(actinstids[i]);
					if (actinst != null) {
						actinst.setIstransfer(1);
						actinst.setTransfer_Time(new Date());
						commonDao.update(actinst);
					}
				}
				if (count > 0) {
					WFI_DOSSIERTRANSFER matertransfer = new WFI_DOSSIERTRANSFER();
					matertransfer.setTransferid(Transferid);
					matertransfer.setFromdept_Id(user.getDepartment().getId());
					matertransfer.setFromdept_Name(user.getDepartment().getDepartmentName());
					matertransfer.setFromstaff_Name(user.getUserName());
					matertransfer.setFromstaff_Id(user.getId());
					matertransfer.setSend_Count(count);
					matertransfer.setSend_Time(new Date());
					matertransfer.setType(type);
					matertransfer.setReceive_Count(0);
					matertransfer.setStatus(WFConst.MateralStatus.NoReceive.value);
					matertransfer.setDdh(CreatMaxID("0", "DDH"));
					String deparnment = user.getDepartment().getId();
					String remark = departmentService.findById(deparnment).getRemark();
					Result = remark + commonDao.CreatMaxID(deparnment, "0", "KH");
					matertransfer.setTransferCode(Result);
					commonDao.save(matertransfer);
					commonDao.flush();
					return Result;
				}
			}

		}
		return "1";
	}

	/**
	 * 接收资料by duff
	 * 
	 * @param filenumber
	 * @param projectname
	 * @param actinstname
	 * @param type
	 * @return
	 */
	@Transactional
	public boolean AcceptMaterial(User staff, String filenumber, String projectname, String actinstid,
			String actinstname, int type) {
		boolean Result = false;
		if (filenumber != null && !filenumber.equals("")) {
			String[] filenumbers = filenumber.split(";");
			String[] projectnames = projectname.split(";");
			String[] actinstnames = actinstname.split(";");
			String[] actinstids = actinstid.split(";");
			List<Map> fromactinstids = GetFromActinstids(actinstids);
			User user = GetFromUser(fromactinstids);
			if (filenumbers.length > 0) {
				WFI_DOSSIERTRANSFER matertransfer = new WFI_DOSSIERTRANSFER();
				matertransfer.setTransferid(Common.CreatUUID());
				matertransfer.setFromdept_Id(user.getDepartment().getId());
				matertransfer.setFromdept_Name(user.getDepartment().getDepartmentName());
				matertransfer.setFromstaff_Name(user.getUserName());
				matertransfer.setFromstaff_Id(user.getId());
				matertransfer.setSend_Count(filenumbers.length);
				matertransfer.setSend_Time(new Date());
				matertransfer.setType(type);
				matertransfer.setReceive_Count(filenumbers.length);
				matertransfer.setStatus(WFConst.MateralStatus.Received.value);
				matertransfer.setDdh(CreatMaxID("0", "DDH"));
				matertransfer.setTostaff_Id(staff.getId());
				matertransfer.setTostaff_Name(staff.getUserName());
				matertransfer.setTodept_Id(staff.getDepartment().getId());
				matertransfer.setTodept_Name(staff.getDepartment().getDepartmentName());
				commonDao.save(matertransfer);
				for (int i = 0; i < filenumbers.length; i++) {
					WFI_TRANSFERLIST list = new WFI_TRANSFERLIST();
					Map map = fromactinstids.get(i);
					Wfi_ActInst fromactinst = smActInst.GetActInst(map.get("FROMACTINST_ID").toString());
					list.setTransferlistid(Common.CreatUUID());
					list.setTransferid(matertransfer.getTransferid());
					list.setFile_Number(filenumbers[i]);
					list.setProject_Name(projectnames[i]);
					list.setFromactinst_Name(fromactinst.getActinst_Name());
					list.setReceive_Status(WFConst.MateralStatus.Received.value);
					list.setReceive_Staffid(staff.getId());
					list.setReceive_Staffname(staff.getUserName());
					list.setReceive_Time(new Date());
					commonDao.save(list);
					Wfi_ActInst actinst = smActInst.GetActInst(actinstids[i]);
					if (actinst != null) {
						actinst.setIstransfer(1);
						actinst.setTransfer_Time(new Date());
						commonDao.update(actinst);
					}
				}
				commonDao.flush();
				Result = true;
			}

		}
		return Result;
	}

	/**
	 * 获取前一个环节
	 * 
	 * @param actinstids
	 * @return
	 */

	public List<Map> GetFromActinstids(String[] actinstids) {
		String header = "FROMACTINST_ID ";
		String table = "BDC_WORKFLOW.WFI_ROUTE";
		String sql = " NEXT_ACTINST_ID IN ('";
		for (int i = 0; i < actinstids.length; i++) {
			sql += actinstids[i];
			sql += "','";
		}
		sql = sql.substring(0, sql.length() - 2);
		sql += ")";
		List<Map> list = commonDao.getDataList(header, table, sql);
		return list;
	}

	/**
	 * 获取前一个环节操作者
	 * 
	 * @param fromactinstids
	 * @return
	 */

	public User GetFromUser(List<Map> fromactinstids) {
		String actinstid = fromactinstids.get(0).get("FROMACTINST_ID").toString();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT STAFF_ID FROM ");
		sql.append(Common.WORKFLOWDB + "WFI_ACTINSTSTAFF WHERE ACTINST_ID='");
		sql.append(actinstid + "'");
		List<Map> userinfo = commonDao.getDataListByFullSql(sql.toString());
		String id = userinfo.get(0).get("STAFF_ID").toString();
		User user = commonDao.get(User.class, id);
		return user;
	}

	public List<Map> GetTransferList(String transferid) {

		if (transferid != null && !transferid.equals("")) {
			StringBuilder sb = new StringBuilder();
			sb.append("select pro.project_name,pro.file_number,pro.prodef_name,pro.prolsh,pro.acceptor ");
			sb.append(" from ");
			sb.append(Common.WORKFLOWDB + "wfi_transferlist list");
			sb.append(" left join " + Common.WORKFLOWDB
					+ "wfi_proinst pro on list.file_number=pro.file_number where list.transferid='");
			sb.append(transferid);
			sb.append("' AND pro.file_number IS NOT NULL ");
			List<Map> list = commonDao.getDataListByFullSql(sb.toString());
			WFI_DOSSIERTRANSFER doss = commonDao.get(WFI_DOSSIERTRANSFER.class, transferid);
			if (null != doss) {
				for (Map map : list) {
					map.put("TRANSFERCODE", doss.getTransferCode());
				}
			}
			// 这人需要继续和登记系统对接
			list = ContatData(list, false);
			return list;
		} else {
			return null;
		}
	}

	// 海口获取移交记录
	public List<Map> GetTransList(String transferid) {

		if (transferid != null && !transferid.equals("")) {

			StringBuilder sb = new StringBuilder();
			sb.append("select pro.project_name,pro.acceptor as STAFF_NAME,info.FILE_NUMBER,info.USERNAME,"
					+ "info.TEL,info.AREA,info.ADDRESS,info.PROTYPE，info.BDCQZH，info.BDCDYZL ");
			sb.append(" from ");
			sb.append(Common.WORKFLOWDB + "wfi_transferlist list");
			sb.append(" left join " + Common.WORKFLOWDB + "wfi_proinst pro on list.file_number=pro.file_number ");
			sb.append("left join " + Common.WORKFLOWDB
					+ "wfi_prouserinfo info on info.file_number=pro.prolsh where list.transferid='");
			sb.append(transferid);
			sb.append("'");
			List<Map> list = commonDao.getDataListByFullSql(sb.toString());
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 获取某人可以收取的所有项目资料
	 * 
	 * @param StaffID
	 * @return
	 */

	public Message GetReceiveDossier(String StaffID, int type, String status, String prolsh, String start, String end,
			int pagesize, int pageindex) {
		Message msg = new Message();
		String header = "select t.transferlistid,t.receive_staffname,t.receive_time, t.file_number,t.lsh,v.prolsh,t.project_name,do.fromstaff_name,do.fromdept_name,do.send_time,t.fromactinst_name,t.actinst_id";

		String sql = " from " + Common.WORKFLOWDB + "wfi_transferlist t left join " + Common.WORKFLOWDB
				+ "v_projectlist v on t.file_number=v.file_number left join " + Common.WORKFLOWDB
				+ "wfi_dossiertransfer do on t.transferid=do.transferid";

		sql += " where do.type=" + type + " and v.actinst_status in ("
				+ WFConst.Instance_Status.Instance_NotAccept.value + "," + WFConst.Instance_Status.Instance_doing.value
				+ "," + WFConst.Instance_Status.BeforAccept.value + ") and v.actstaff='" + StaffID
				+ "' and t.receive_status=" + status;
		if (prolsh != null && !prolsh.equals("")) {
			sql += " and v.prolsh='" + prolsh + "'";
		}
		String time = status.equals(WFConst.MateralStatus.NoReceive.value + "") ? "send_time" : "receive_time";

		if (start != null && !start.equals("")) {
			sql += " and do." + time + ">to_date('" + start + "', 'yyyy-mm-dd hh24:mi:ss') ";
		}
		if (end != null && !end.equals("")) {
			sql += " and do." + time + "<to_date('" + end + "', 'yyyy-mm-dd hh24:mi:ss') ";
		}
		/*
		 * sql += "and  t.transferid in ( select transferid from " + Common.WORKFLOWDB +
		 * "wfi_dossiertransfer where status=" + WFConst.MateralStatus.NoReceive.value +
		 * ") ";
		 */
		long count = commonDao.getCountByFullSql(sql);
		if (count > 0) {
			msg.setTotal(count);
			msg.setRows(commonDao.getDataListByFullSql(header + sql, pageindex, pagesize));
		}
		return msg;

	}

	/**
	 * 接收文件
	 * 
	 * @param staff
	 * @param id
	 * @return
	 */
	public SmObjInfo ReceiveMaterial(User staff, String id) {
		SmObjInfo info = new SmObjInfo();
		WFI_TRANSFERLIST list = commonDao.get(WFI_TRANSFERLIST.class, id);
		if (list != null) {
			list.setReceive_Staffid(staff.getId());
			list.setReceive_Staffname(staff.getUserName());
			list.setReceive_Time(new Date());
			list.setReceive_Status(WFConst.MateralStatus.Received.value);
			String actinstid = list.getACTINST_ID();
			Wfd_Actdef currrentActDef = smActInst.GetActDef(actinstid);
			JSONObject obj = JSONObject.fromObject(currrentActDef.getCustomeParamsSet());
			if (!obj.isNullObject()) {
				if (obj.containsKey("ZCHYJDA")) {
					String zchy = obj.getString("ZCHYJDA");
					if (zchy != null && !zchy.equals("")) {
						Wfi_ActInst inst = smActInst.GetActInst(actinstid);
						Date nowTime = new Date();
						inst.setActinst_End(nowTime);
						inst.setActinst_Status(WFConst.Instance_Status.Instance_Passing.value);
						Wfi_ActInst nextInst = smActInst.GetNextActInst(actinstid, currrentActDef.getActdef_Id());
						nextInst.setActinst_Start(nowTime);
						nextInst.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
						commonDao.update(inst);
						commonDao.update(nextInst);
					}
				}
			}
			commonDao.update(list);
			commonDao.flush();
			setDossierTransfer(list.getTransferid(), staff);
			info.setDesc("成功收件");
			info.setID(id);
		}
		return info;
	}

	/**
	 * 撤销某次接收记录
	 * 
	 * @param id
	 * @return
	 */
	public SmObjInfo revokeTransferList(String id) {
		SmObjInfo info = new SmObjInfo();
		WFI_TRANSFERLIST list = commonDao.get(WFI_TRANSFERLIST.class, id);
		if (list != null) {
			list.setReceive_Status(WFConst.MateralStatus.NoReceive.value);
			list.setReceive_Staffid(null);
			list.setReceive_Staffname(null);
			list.setReceive_Time(null);
			WFI_DOSSIERTRANSFER dossier = commonDao.get(WFI_DOSSIERTRANSFER.class, list.getTransferid());
			WFI_TRANSFERLIST transfer = getTransferlist(list.getTransferid());
			if (dossier != null) {
				int count = dossier.getReceive_Count();
				if (count > 1) {
					if (transfer != null) {
						dossier.setReceive_Count(--count);
						dossier.setReceive_Time(new Date());
						dossier.setStatus(WFConst.MateralStatus.NoReceive.value);
						User staff = userService.findById(transfer.getReceive_Staffid());
						dossier.setTostaff_Id(staff.getId());
						dossier.setTostaff_Name(staff.getUserName());
						dossier.setTodept_Id(staff.getDepartmentId());
						dossier.setTodept_Name(staff.getDepartmentName());
					}
				} else {
					dossier.setReceive_Count(0);
					dossier.setReceive_Time(null);
					dossier.setStatus(WFConst.MateralStatus.NoReceive.value);
					dossier.setTostaff_Id(null);
					dossier.setTostaff_Name(null);
					dossier.setTodept_Id(null);
					dossier.setTodept_Name(null);
				}
			}
			commonDao.update(dossier);
			commonDao.update(list);
			commonDao.flush();
			info.setDesc("成功撤销！");
			info.setID(id);
		}
		return info;
	}

	/**
	 * 通过DOSSIERTRANSFERID获取最后一条接收记录
	 * 
	 * @param id
	 * @return
	 */
	private WFI_TRANSFERLIST getTransferlist(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		sql.append(Common.WORKFLOWDB + "wfi_transferlist ");
		sql.append("where transferid='");
		sql.append(id + "' and ");
		sql.append("receive_status='");
		sql.append(WFConst.MateralStatus.Received.value + "' order by receive_time");
		List<WFI_TRANSFERLIST> list = commonDao.getDataList(WFI_TRANSFERLIST.class, sql.toString());
		if (list.size() > 0) {
			WFI_TRANSFERLIST last = list.get(list.size() - 1);
			return last;
		} else {
			return null;
		}
	}

	/**
	 * 设置主表
	 * 
	 * @param id
	 * @param staff
	 */
	private void setDossierTransfer(String id, User staff) {
		WFI_DOSSIERTRANSFER dossier = commonDao.get(WFI_DOSSIERTRANSFER.class, id);
		if (dossier != null) {
			int count = dossier.getReceive_Count();

			dossier.setReceive_Count(++count);
			dossier.setReceive_Time(new Date());
			if (dossier.getReceive_Count() == dossier.getSend_Count()) {
				dossier.setStatus(WFConst.MateralStatus.Received.value);
			}

			dossier.setTostaff_Id(staff.getId());
			dossier.setTostaff_Name(staff.getUserName());
			dossier.setTodept_Id(staff.getDepartment().getId());
			dossier.setTodept_Name(staff.getDepartment().getDepartmentName());
			commonDao.update(dossier);
			commonDao.flush();

		}
	}

	/**
	 * 获取一个项目的资料流转记录
	 * 
	 * @param file_number
	 * @return
	 */
	public Map GetMaterialProcess(String file_number) {
		Map Result = new HashMap();
		List<Map> list = new ArrayList<Map>();
		// select
		// t.fromactinst_name,t.toactinst_name,t.receive_time,t.receive_staffname
		// from wfi_transferlist t left join wfi_dossiertransfer do on
		// t.transferid=do.transferid where
		// file_number='2015-510500-100040102-000009' and receive_status=5 order
		// by receive_time;
		// 1、查找项目收件人
		// 2、查找转移记录 （分解成两部 一步转移，一步接收）
		// 3、归档
		Wfi_ProInst proinst = smProinst.GetProInstByFileNumber(file_number);

		if (proinst != null) {
			Result.put("project_name", proinst.getProject_Name());
			Result.put("project_status", proinst.getProinst_Status());
			StringBuilder actsb = new StringBuilder();
			actsb.append("select * from ");
			actsb.append(Common.WORKFLOWDB + "Wfi_ActInst");
			actsb.append(" where actdef_type='");
			actsb.append(WFConst.ActDef_Type.ProcessStart.value);
			actsb.append("' and proinst_id='" + proinst.getProinst_Id() + "' order by actinst_start desc");
			List<Wfi_ActInst> actlist = commonDao.getDataList(Wfi_ActInst.class, actsb.toString());
			if (actlist != null && actlist.size() > 0) {
				Wfi_ActInst inst = actlist.get(0);
				Map map = CreatMap(inst.getStaff_Name(), "受理" + inst.getActinst_Name(), inst.getActinst_Start(), "揽件");
				list.add(map);
			}
		}
		StringBuilder materialB = new StringBuilder();
		materialB.append(
				"select t.fromactinst_name,t.toactinst_name,t.receive_time,t.receive_staffname,t.receive_status,do.send_time,do.fromstaff_name,do.TRANSFERCODE,do.FROMDEPT_NAME,do.TODEPT_NAME");
		materialB.append(" from " + Common.WORKFLOWDB + "wfi_transferlist t left join " + Common.WORKFLOWDB
				+ "wfi_dossiertransfer do on t.transferid=do.transferid where ");
		materialB.append(" file_number='" + file_number + "'  order by receive_time,create_time");
		List<Map> matermap = commonDao.getDataListByFullSql(materialB.toString());
		if (matermap != null && matermap.size() > 0) {
			for (Map m : matermap) {
				String status = m.get("RECEIVE_STATUS").toString();
				Map map = new HashMap();
				if (status.equals(WFConst.MateralStatus.Received.value + "")) {
					Object staffname = m.get("FROMSTAFF_NAME");
					Object actname = m.get("FROMACTINST_NAME");
					Object time = m.get("SEND_TIME");
					map = CreatMap(staffname, "办理完毕" + actname, time, "发件");
					map.put("department", m.get("FROMDEPT_NAME"));
					map.put("kh", m.get("TRANSFERCODE"));
					list.add(map);
					staffname = m.get("RECEIVE_STAFFNAME").toString();
					actname = m.get("TOACTINST_NAME");
					time = (Date) m.get("RECEIVE_TIME");
					if (actname == null) {
						actname = "即将办理";
					} else {
						actname = "办理" + actname;
					}
					map = CreatMap(staffname, actname, time, "收件");
					map.put("department", m.get("TODEPT_NAME"));
					map.put("kh", m.get("TRANSFERCODE"));
					list.add(map);
				} else if (status.equals(WFConst.MateralStatus.NoReceive.value + "")) {
					Object staffname = m.get("FROMSTAFF_NAME");
					Object actname = m.get("FROMACTINST_NAME");
					Object time = m.get("SEND_TIME");
					map = CreatMap(staffname, "办理完毕" + actname, time, "发件");
					map.put("department", m.get("FROMDEPT_NAME"));
					map.put("kh", m.get("TRANSFERCODE"));
					list.add(map);
				}
			}
		}
		Result.put("process", list);
		return Result;
	}

	private Map CreatMap(Object staff, Object actdefname, Object time, String desc) {
		Map map = new HashMap();
		map.put("staff_name", staff);
		map.put("actdef_name", actdefname);
		map.put("time", time);
		map.put("desc", desc);
		return map;
	}

	// 设置案卷的办理活动
	public void setToActinst_Name(String staffid, String file_number, String actinstname) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(Common.WORKFLOWDB + "WFI_TRANSFERLIST");
		sb.append(" where receive_staffid='");
		sb.append(staffid);
		sb.append("' and receive_status=");
		sb.append(WFConst.MateralStatus.Received.value);
		sb.append(" and file_number='");
		sb.append(file_number);
		sb.append("' and toactinst_name is null ");
		List<WFI_TRANSFERLIST> list = commonDao.getDataList(WFI_TRANSFERLIST.class, sb.toString());
		if (list != null && list.size() > 0) {
			WFI_TRANSFERLIST trans = list.get(0);
			trans.setToactinst_Name(actinstname);
			commonDao.update(trans);
			commonDao.flush();
		}

	}

	public List<String> ValidateMaterial(String filenumbers, String staffid) {
		List<String> Result = new ArrayList<String>();
		if (filenumbers != null && !filenumbers.equals("")) {
			String[] file_number = filenumbers.split(",");

			if (file_number != null && file_number.length > 0) {
				List lfile = Arrays.asList(file_number);
				StringBuilder sb = new StringBuilder();
				sb.append("select * from ");
				sb.append(Common.WORKFLOWDB + "wfi_transferlist");
				sb.append(" where file_number in (");
				sb.append(filenumbers);
				sb.append(") and receive_staffid='");
				sb.append(staffid + "' and ");
				sb.append(" receive_status=");
				sb.append(WFConst.MateralStatus.Received.value);
				List<Map> list = commonDao.getDataListByFullSql(sb.toString());
				for (Map m : list) {
					String f = m.get("FILE_NUMBER").toString();
					if (lfile.contains("'" + f + "'")) {
						Result.add(f);
					}
				}
			}
		}
		return Result;
	}

	/**
	 * 自动资料
	 * 
	 * @param srcid
	 * @param targetfolder
	 * @return
	 */
	public boolean MoveMaterial(String srcid, String targetfolder) {
		boolean Result = false;
		Wfi_MaterData data = commonDao.get(Wfi_MaterData.class, srcid);
		if (data != null) {
			String old = data.getMaterilinst_Id();
			data.setMaterilinst_Id(targetfolder);
			// 检测目标文件夹下有几个文件
			List<Wfi_MaterData> targetFiles = commonDao.getDataList(Wfi_MaterData.class,
					Common.WORKFLOWDB + "Wfi_MaterData", " materilinst_id='" + targetfolder + "'");
			if (targetFiles != null && targetFiles.size() == 0) {
				Wfi_ProMater promater = commonDao.get(Wfi_ProMater.class, targetfolder);
				promater.setImg_Path(data.getMaterialdata_Id());
				commonDao.update(promater);
			}
			commonDao.update(data);
			commonDao.flush();
			// 移动文件

			FileUpload.MoveFile(old, targetfolder, data.getFile_Path());
			Result = true;
		}

		return Result;

	}

	public String CreatMaxID(String bh, String Type) {
		String distId = smstaff.getDistIdByStaffIDString(smstaff.getCurrentWorkStaffID());
		return commonDao.CreatMaxID(distId, bh, Type);
	}

	/**
	 * 按照条码进行归档，批量转出
	 * 
	 * @param user
	 * @param actinstids
	 * @param lshs
	 * @param type
	 * @return
	 */

	public SmObjInfo TransferDossier(User user, String actinstids, String lshs, String cb, int type) {
		SmObjInfo info = new SmObjInfo();
		String Result = "";
		if (lshs != null && !lshs.equals("")) {
			String[] lsh = lshs.split(",");
			String[] actinst = actinstids.split(",");
			//记录失败的lsh
			List listerr=new ArrayList();
			//成功的actinst
			List listok=new ArrayList();
			if (lsh.length > 0) {
				WFI_DOSSIERTRANSFER matertransfer = new WFI_DOSSIERTRANSFER();
				matertransfer.setTransferid(Common.CreatUUID());
				matertransfer.setFromdept_Id(user.getDepartment().getId());
				matertransfer.setFromdept_Name(user.getDepartment().getDepartmentName());
				matertransfer.setFromstaff_Name(user.getUserName());
				matertransfer.setFromstaff_Id(user.getId());
				matertransfer.setSend_Count(lsh.length);
				matertransfer.setSend_Time(new Date());
				matertransfer.setType(type);

				matertransfer.setReceive_Count(0);
				matertransfer.setStatus(WFConst.MateralStatus.NoReceive.value);
				matertransfer.setDdh(CreatMaxID("0", "DDH"));
				info.setID(matertransfer.getDdh());
				commonDao.save(matertransfer);
				String updateAJIDs = "'0'";
				for (int i = 0; i < lsh.length; i++) {
					List<Wfi_ProInst> proinsts = commonDao.getDataList(Wfi_ProInst.class,
							"select * from " + Common.WORKFLOWDB + "Wfi_ProInst where prolsh='" + lsh[i] + "'");
					if (proinsts != null && proinsts.size() > 0) {
						Wfi_ProInst p = proinsts.get(0);
						WFI_TRANSFERLIST list = new WFI_TRANSFERLIST();
						list.setTransferlistid(Common.CreatUUID());
						list.setTransferid(matertransfer.getTransferid());
						list.setFile_Number(p.getFile_Number());
						list.setLsh(lsh[i]);
						list.setProject_Name(p.getProject_Name());
						list.setFromactinst_Name("归档");
						list.setReceive_Status(WFConst.MateralStatus.NoReceive.value);
						list.setCreate_Time(DateUtil.addDay(new Date(), i));
						list.setSFZYZl(0);
						list.setACTINST_ID(actinst[i]);
						list.setSuccess(0);
						list.setCB(cb);
						/*
						 * 判断是否归档
						 */
						String SFQYXDA =ConfigHelper.getNameByValue("SFQYXDA");//新旧判断依据,本地化配置中设置
						if(SFQYXDA.equals("2")){
							//旧归档(3.0版本以下)
							StringBuilder hsb = new StringBuilder();
							hsb.append("SELECT * FROM BDC_DAK.DAS_AJJBXX WHERE YWDH='");
							hsb.append(lsh[i]);
							hsb.append("'");
							List<Map> ajlist = commonDao.getDataListByFullSql(hsb.toString());
							commonDao.save(list);
							commonDao.flush();
							if (ajlist.size() > 0) {
								// 维护信息到工作流
								String ajid = ajlist.get(0).get("AJID").toString();
								Object ydah = ajlist.get(0).get("YDAH");
								updateAJIDs += ",'" + ajid + "'";
								isGDSuccess(list, "", ajid, actinst[i], ydah == null ? "" : ydah.toString(), "");
							} else {
								Map<String, String> ajjbxxmap = new HashMap<String, String>();
								ajjbxxmap.put("LRR", user.getUserName());
								SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");// 设置日期格式
								ajjbxxmap.put("CDSJ", df.format(new Date()));
								ajjbxxmap.put("BZ", "批量归档");
								ajjbxxmap.put("actinstid", actinst[i]);
								ajjbxxmap.put("id", list.getTransferlistid());
								ajjbxxmap.put("MLBH", cb);
								sendDossier(ajjbxxmap, p.getFile_Number());
							}
						}else{
							//3.0以上归档版本
							Map<String, String> param = new HashMap();
							param.put("cb", cb);
							param.put("lsh", lsh[i]);
							com.supemap.mns.model.Message msg= Basic.DossierRequest(getajinfo, param, HttpMethod.GET);
							JSONObject jobj;
//							String tip = null;
//							String rows;
//							String S=msg.getMessageBodyAsString();
							if (msg != null&&msg.getMessageBodyAsString()!=null&&!msg.getMessageBodyAsString().equals("null")) {
								jobj = JSONObject.fromObject(msg.getMessageBodyAsString());
								Object obj = jobj.get("SYQR");
								if(obj instanceof JSONNull || obj==null){
									//记录失败的lsh
									listerr.add(lsh[i]);
								}else{

									String SYQR = null;
									String QZH = null;
									String ZMH = null;
									String ZL = null;
									String DJLX = null;
									String QLLX = null;
									String ajid = null;
									String AJH = null;
									String YDAH = null;


									if (!StringHelper.isEmpty(jobj.get("SYQR"))) {
										SYQR = jobj.getString("SYQR");
									}
									if (!StringHelper.isEmpty(jobj.get("QZH"))) {
										QZH = jobj.getString("QZH");
									}
									if (!StringHelper.isEmpty(jobj.get("ZMH"))) {
										ZMH = jobj.getString("ZMH");
									}
									if (!StringHelper.isEmpty(jobj.get("ZL"))) {
										ZL = jobj.getString("ZL");
									}
									if (!StringHelper.isEmpty(jobj.get("DJLX"))) {
										DJLX = jobj.getString("DJLX");
									}
									if (!StringHelper.isEmpty(jobj.get("QLLX"))) {
										QLLX = jobj.getString("QLLX");
									}
									if (!StringHelper.isEmpty(jobj.get("AJID"))) {
										ajid = jobj.getString("AJID");
									}
									if (!StringHelper.isEmpty(jobj.get("YDAH"))) {
										YDAH = jobj.getString("YDAH");
									}


									if (list != null) {
										list.setACTINST_ID(actinst[i]);
										list.setAJID(ajid);
										list.setDah(AJH);
										list.setOLDDAH(YDAH);

										list.setSuccess(1);
										logger.info("实际归档已经成功。success置为1了");
										if (QLLX.equals("23") || DJLX.equals("700") || DJLX.equals("600")) {
											if (!StringHelper.isEmpty(ZMH) && ZMH != null) {
												list.setQZH(ZMH);
											} else {
												list.setQZH("");
											}

										} else {
											if (!StringHelper.isEmpty(QZH) && QZH != null) {
												list.setQZH(QZH);
											} else {
												list.setQZH("");
											}

										}
										if (!StringHelper.isEmpty(SYQR) && SYQR != null) {
											list.setSQR(SYQR);
										}
										if (!StringHelper.isEmpty(ZL) && ZL != null) {
											list.setZL(ZL);
										}
									}

									commonDao.saveOrUpdate(list);
									listok.add(actinst[i]);

								}

							}
						}


					}
					commonDao.flush();
				}
				if (updateAJIDs.length() > 4 && cb.equals("G")) {
					int set = commonDao.excuteQuery(
							"update bdc_dak.das_ajjbxx set mlbh='" + cb + "' where ajid in (" + updateAJIDs + ")");
				}
				// BetchPassOver(actinstids);
				Map<String, String> map = new HashMap<String, String>();
				map.put("actinstids", actinstids);
				String worflowBasic = ConfigHelper.getNameByValue("worflow2dossier");
				com.supemap.mns.model.Message msg = http.PostSend(worflowBasic + "operation/batch/", map, "passover");
				info.setDesc(msg.getMessageBodyAsString());
			}
		}
		// System.out.println("结束");
		return info;
	}
	
	/**
	 * 旧页面数据归档到2.0档案库
	 * @param user
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public SmObjInfo dossierForDjgd(User user,int type) {
		SmObjInfo info = new SmObjInfo(); 
		String cb="";//产别
		String lsh="";//流水号
		String sql=""; //执行sql语句
		String actinstid=""; //环节did
		String actinstids="";//所有项目环节,为办结项目做准备
		
		
		//1.获取djgd表数据,然后获取 案卷号 ,归档时间和归档人员
		List<BDCS_DJGD> djgdList = commonDao.findAll(BDCS_DJGD.class);
		//2.遍历数据,根据权利人类型区分私产和公产 ,进行归档
		if(djgdList!=null&&djgdList.size()>0) {
			WFI_DOSSIERTRANSFER matertransfer = new WFI_DOSSIERTRANSFER();
			matertransfer.setTransferid(Common.CreatUUID());
			matertransfer.setFromdept_Id(user.getDepartment().getId());
			matertransfer.setFromdept_Name(user.getDepartment().getDepartmentName());
			matertransfer.setFromstaff_Name(user.getUserName());
			matertransfer.setFromstaff_Id(user.getId());
			matertransfer.setSend_Count(djgdList.size());
			matertransfer.setSend_Time(new Date());
			matertransfer.setType(type);
			matertransfer.setReceive_Count(0);
			matertransfer.setStatus(WFConst.MateralStatus.NoReceive.value);
			matertransfer.setDdh(CreatMaxID("0", "DDH"));
			info.setID(matertransfer.getDdh());
			commonDao.save(matertransfer);
			for(BDCS_DJGD djgd:djgdList) {
				sql="SELECT QLRLX FROM BDCK.BDCS_QLR_GZ  QLR  WHERE QLR.XMBH ='"+djgd.getXMBH()+"'";
				List<Map> maplist =commonDao.getDataListByFullSql(sql);
				if(maplist!=null&&maplist.size()>0) {
					//根据权利人类型判断产别
					String qlrlx=maplist.get(0).get("QLRLX")+"";
					if(qlrlx!=null
							&&!qlrlx.equals("null")
							&&!qlrlx.equals("")
							&&qlrlx.equals("1")
							||qlrlx.equals("2")) {
						cb="S";
					}
					if(qlrlx!=null
							&&!qlrlx.equals("null")
							&&!qlrlx.equals("")
							&&qlrlx.equals("3")
							||qlrlx.equals("4")) {
						cb="G";
					}
				}
				//获取流水号
				sql="SELECT YWLSH FROM BDCK.BDCS_XMXX  WHERE XMBH ='"+djgd.getXMBH()+"'";
				maplist=commonDao.getDataListByFullSql(sql);
				if(maplist!=null&&maplist.size()>0) {
					lsh=maplist.get(0).get("YWLSH")+"";
				}
				//获取环节id
				List<Wfi_ProInst> proinsts = commonDao.getDataList(Wfi_ProInst.class,
						"select * from " + Common.WORKFLOWDB + "Wfi_ProInst where prolsh='" + lsh + "'");
				sql="SELECT   ACTINST_ID  FROM BDC_WORKFLOW.WFI_ACTINST   WHERE PROINST_ID ='"+proinsts.get(0).getProinst_Id()+"' AND   ACTINST_NAME LIKE'%归档%'  AND ACTDEF_TYPE='5010'";
				maplist=commonDao.getDataListByFullSql(sql);
				if(maplist!=null&&maplist.size()>0) {
					actinstid=maplist.get(0).get("ACTINST_ID")+"";
					actinstids+=actinstid;
					actinstids+=",";
				}
				String updateAJIDs = "'0'";
					
					if (proinsts != null && proinsts.size() > 0) {
						Wfi_ProInst p = proinsts.get(0);
						WFI_TRANSFERLIST list = new WFI_TRANSFERLIST();
						list.setTransferlistid(Common.CreatUUID());
						list.setTransferid(matertransfer.getTransferid());
						list.setFile_Number(p.getFile_Number());
						list.setLsh(lsh);
						list.setProject_Name(p.getProject_Name());
						list.setFromactinst_Name("归档");
						list.setReceive_Status(WFConst.MateralStatus.NoReceive.value);
						list.setCreate_Time(DateUtil.addDay(new Date(), 0));
						list.setSFZYZl(0);
						list.setACTINST_ID(actinstid);
						list.setSuccess(0);
						list.setCB(cb);
						/*
						 * 判断是否归档
						 */
						StringBuilder hsb = new StringBuilder();
						hsb.append("SELECT * FROM BDC_DAK.DAS_AJJBXX WHERE YWDH='");
						hsb.append(lsh);
						hsb.append("'");
						List<Map> ajlist = commonDao.getDataListByFullSql(hsb.toString());
						commonDao.save(list);
						commonDao.flush();
						if (ajlist.size() > 0) {
							// 维护信息到工作流
							String ajid = ajlist.get(0).get("AJID").toString();
							Object ydah = ajlist.get(0).get("YDAH");
							updateAJIDs += ",'" + ajid + "'";
							isGDSuccess(list, "", ajid, actinstid, ydah == null ? "" : ydah.toString(), "");
						} else {
							Map<String, String> ajjbxxmap = new HashMap<String, String>();
//							ajjbxxmap.put("LRR", user.getUserName());
							SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");// 设置日期格式
							ajjbxxmap.put("CDSJ", df.format(djgd.getGDSJ()));
							ajjbxxmap.put("BZ", "升级归档");
							ajjbxxmap.put("actinstid", actinstid);
							ajjbxxmap.put("id", list.getTransferlistid());
							ajjbxxmap.put("MLBH", cb);
							ajjbxxmap.put("AJH", djgd.getJZH());
							ajjbxxmap.put("LRR", djgd.getGDZR());
							sendDossier(ajjbxxmap, p.getFile_Number());
						}

					}
					commonDao.flush();

					// 归档
				if (updateAJIDs.length() > 4 && cb.equals("G")) {
					int set = commonDao.excuteQuery(
							"update bdc_dak.das_ajjbxx set mlbh='" + cb + "' where ajid in (" + updateAJIDs + ")");
				}
				// BetchPassOver(actinstids);
				Map<String, String> map = new HashMap<String, String>();
				map.put("actinstids", actinstids);
				String worflowBasic = ConfigHelper.getNameByValue("worflow2dossier");
				com.supemap.mns.model.Message msg = http.PostSend(worflowBasic + "operation/batch/", map, "passover");
				info.setDesc(msg.getMessageBodyAsString());
			    //数据维护
				
			}
			
		}
		
		
		
		// System.out.println("结束");
		return info;
	}
	
	
	

	public void BetchPassOver(String actinstids) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("actinstids", actinstids);
		AsyncCallback dossierCallback = new AsyncCallback() {
			public Map<String, String> receipts = new HashMap<String, String>();

			public void onFail(Exception ex) {
				System.out.println("项目办结失败");
				ex.printStackTrace();
			}

			public void onSuccess(Object paramT) {
				@SuppressWarnings("unchecked")
				com.supemap.mns.model.Message result = (com.supemap.mns.model.Message) paramT;
				if (result != null) {
					System.out.println("项目办结");
				}
			}
		};
		String worflowBasic = ConfigHelper.getNameByValue("worflow2dossier");
		AsyncResult asyncBatchPopMessage = http.asyncPostSend(worflowBasic, map, "operation/batch/passover",
				dossierCallback);
		asyncBatchPopMessage.getResult();
	}

	public void sendDossier(Map<String, String> ajjbxxmap, String filenumber) {
		final String File_numnber = filenumber;
		AsyncCallback dossierCallback = new AsyncCallback() {
			public Map<String, String> receipts = new HashMap<String, String>();

			public void onFail(Exception ex) {
				System.out.println("获取信息失败");
				ex.printStackTrace();

			}

			public void onSuccess(Object paramT) {
				com.supemap.mns.model.Message result = (com.supemap.mns.model.Message) paramT;
				if (result != null) {
					String DAInfo = result.getMessageBodyAsString();
					JSONObject obj = JSONObject.fromObject(DAInfo);
					if (obj != null && !obj.equals("")) {
						// jobj.get("AJID");
						// 完成归档
						String dah = obj.getString("DAH");
						String AJID = obj.getString("AJID");
						String ID = obj.getString("ID");// transferlist 主键
						String ACTINST_ID = obj.getString("ACTINST_ID");
						String OLDDAH = obj.getString("OLDDAH");
						String backwrite = obj.getString("backwrite");
						// String qlr=obj.getString("qlr");
						if (backwrite != null && !backwrite.equals("false") && !AJID.equals("")) {
							isGDSuccess(ID, dah, AJID, ACTINST_ID, OLDDAH, "");
						}

					}

				}
			}
		};
		String worflowBasic = ConfigHelper.getNameByValue("worflow2dossier");
		AsyncResult asyncBatchPopMessage = http.asyncPostSend(worflowBasic + "bdc/archives/GD/asyn/", ajjbxxmap,
				filenumber, dossierCallback);
		asyncBatchPopMessage.getResult();
	}

	public boolean isGDSuccess(WFI_TRANSFERLIST list, String DAH, String AJID, String ACTINST_ID, String OLDDAH,
			String qlr) {
		boolean result = false;
		if (list != null) {
			list.setACTINST_ID(ACTINST_ID);
			list.setAJID(AJID);
			list.setDah(DAH);
			if (OLDDAH != null && !OLDDAH.equals("null")) {
				list.setOLDDAH(OLDDAH);
			}
			list.setSuccess(1);
			logger.info("实际归档已经成功。success置为1了");
			// 这人提取案卷信息
			// Map plist =
			// ProjectHelper.GetFileTransferInfoEx3(list.getFile_Number());
			// -----现在直接换为从bdc库中获取。也就是两个地方都使用querytool的数据
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT SYQR,QZH,ZMH,ZL,DJLX,QLLX FROM BDC_DAK.DAS_BDC ");
			builder.append("WHERE AJID= '" + AJID + "'");
			List<Map> listbdcs = commonDao.getDataListByFullSql(builder.toString());
			if (listbdcs.size() > 0) {
				Map bdcmap = listbdcs.get(0);
				String djlx = bdcmap.get("DJLX").toString();
				String qllx = bdcmap.get("QLLX").toString();
				if (qllx.equals("23") || djlx.equals("700") || djlx.equals("600")) {
					if (!StringHelper.isEmpty(bdcmap.get("ZMH")) && bdcmap.get("ZMH") != null) {
						list.setQZH(bdcmap.get("ZMH").toString());
					} else {
						list.setQZH("");
					}

				} else {
					if (!StringHelper.isEmpty(bdcmap.get("QZH")) && bdcmap.get("QZH") != null) {
						list.setQZH(bdcmap.get("QZH").toString());
					} else {
						list.setQZH("");
					}

				}
				if (!StringHelper.isEmpty(bdcmap.get("SYQR")) && bdcmap.get("SYQR") != null) {
					list.setSQR(bdcmap.get("SYQR").toString());
				}
				if (!StringHelper.isEmpty(bdcmap.get("ZL")) && bdcmap.get("ZL") != null) {
					list.setZL(bdcmap.get("ZL").toString());
				}

			}
			result = true;
		}
		return result;
	}

	// 归档成功
	public boolean isGDSuccess(String id, String DAH, String AJID, String ACTINST_ID, String OLDDAH, String qlr) {
		boolean result = false;

		if (id != null && !id.equals("") && !id.equals("null")) {
			WFI_TRANSFERLIST list = commonDao.get(WFI_TRANSFERLIST.class, id);
			result = isGDSuccess(list, DAH, AJID, ACTINST_ID, OLDDAH, qlr);
			commonDao.update(list);
			commonDao.flush();
		} else {
			List<WFI_TRANSFERLIST> lists = commonDao.getDataList(WFI_TRANSFERLIST.class,
					"select * from " + Common.WORKFLOWDB + "WFI_TRANSFERLIST where ajid='" + AJID + "'");
			if (lists != null && lists.size() > 0) {
				for (WFI_TRANSFERLIST l : lists) {
					if (OLDDAH != null && !OLDDAH.equals("")) {
						l.setOLDDAH(OLDDAH);
					}
					if (qlr != null && !qlr.equals("")) {
						l.setSQR(qlr);
					}
					commonDao.update(l);
				}
				commonDao.flush();
			}
		}

		return result;
	}

	public boolean setTransferFJ(String file_number) {
		boolean result = false;
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("WFI_TRANSFERLIST where file_number='");
		sb.append(file_number);
		sb.append("' and receive_status=4 ");
		List<WFI_TRANSFERLIST> list = commonDao.getDataList(WFI_TRANSFERLIST.class, sb.toString());
		if (list != null && list.size() > 0) {
			WFI_TRANSFERLIST l = list.get(0);
			l.setSFZYZl(1);
			commonDao.update(l);
			commonDao.flush();
			result = true;
		}
		return result;

	}

	// 恢复数据,数据库崩溃引起的归档失败，需要设置员工ID
	public boolean recovery() {
		String staffid = "c462f4aa6dd14303a44b3f88651ad9b7";
		String sql = "select * from bdc_workflow.wfi_transferlist where transferid in (select transferid from bdc_workflow.wfi_dossiertransfer where  ddh='2015000149' )";
		List<WFI_TRANSFERLIST> lists = commonDao.getDataList(WFI_TRANSFERLIST.class, sql);
		if (lists != null && lists.size() > 0) {
			for (WFI_TRANSFERLIST l : lists) {
				List<Wfi_ProInst> insts = commonDao.getDataList(Wfi_ProInst.class,
						"select * from bdc_workflow.Wfi_ProInst where file_number='" + l.getFile_Number() + "'");
				if (insts != null) {
					String proinst_id = insts.get(0).getProinst_Id();
					List<Wfi_ActInst> actinst = commonDao.getDataList(Wfi_ActInst.class,
							"select * from bdc_workflow.Wfi_ActInst where proinst_id='" + proinst_id
									+ "' and actinst_status in (1,2)");
					if (actinst != null && actinst.size() > 0) {
						String actinstid = actinst.get(0).getActinst_Id();
						long a = commonDao.getCountByFullSql(" from bdc_workflow.Wfi_ActInstStaff where staff_id='"
								+ staffid + "' and actinst_id='" + actinstid + "'");
						if (a == 0) {
							Wfi_ActInstStaff actstaff = new Wfi_ActInstStaff();
							actstaff.setActstaffid(Common.CreatUUID());
							actstaff.setActinst_Id(actinstid);
							actstaff.setStaff_Id(staffid);
							commonDao.save(actstaff);
							l.setSFZYZl(3);
							commonDao.update(l);

							commonDao.flush();
						}
						// commonDao.delete(l);

					}
				}
			}

		}
		return true;
	}

	// 获取顺序列表
	public Message getOrderList(String ddh) {
		Message msg = new Message();
		if (ddh != null && !ddh.equals("")) {
			ddh.replace('，', ',');
			String[] ddhs = ddh.split(",");
			if (ddhs.length > 0) {
				String ddhtmp = "'0'";
				for (String d : ddhs) {
					ddhtmp += ",'" + d + "'";
				}
				StringBuilder sb = new StringBuilder();
				sb.append("select * from " + Common.WORKFLOWDB);
				sb.append("wfi_transferlist where transferid in (");
				sb.append(" select transferid from ");
				sb.append(Common.WORKFLOWDB);
				sb.append("wfi_dossiertransfer where ddh in (" + ddhtmp);
				sb.append(")) order by qzh ");
				List<WFI_TRANSFERLIST> lists = commonDao.getDataList(WFI_TRANSFERLIST.class, sb.toString());
				msg.setRows(lists);
				msg.setTotal(lists.size());
			}
		}
		return msg;
	}

	// 查询已经归档的项目
	public List<Map> getHaveDossier() {
		StringBuilder sb = new StringBuilder();
		boolean pass = false;
		sb.append(
				"select * from  (select a.file_number,a.transferid ,a.create_time,a.transferlistid,success,row_number() over(partition by a.file_number order by rowid) r1  from bdc_workflow.wfi_transferlist a ) t where r1 = 1 and file_number is not null and success=0  order by   create_time, transferid asc ");
		// sb.append(" and lsh='2015001453'");
		List<Map> lists = commonDao.getDataListByFullSql(sb.toString());
		/*
		 * List<Map> lists=new ArrayList<Map>(); Map m=new HashMap();
		 * m.put("FILE_NUMBER", "2015-130100-100230201-000849"); lists.add(m); m=new
		 * HashMap(); m.put("FILE_NUMBER", "2015-130100-400230201-001245");
		 * lists.add(m);
		 */
		return lists;
	}

	/**
	 * 查看柜子中文件的信息
	 * 
	 * @param boxDef_Id
	 * @param pageSizeStr
	 * @param pageIndexStr
	 * @return
	 */
	public Page getBoxFiles(String boxDef_Id, String pageSizeStr, String pageIndexStr) {
		int pageSize;
		int pageIndex;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			pageIndex = Integer.parseInt(pageIndexStr);
			StringBuilder stb = new StringBuilder();
			stb.append("BOXDEF_ID='");
			stb.append(boxDef_Id);
			stb.append("' AND ");
			stb.append("STATUS is null");
			stb.append(" ORDER BY RGSJ");
			return commonDao.GetPagedData(WFI_BOXINST.class, pageIndex, pageSize, stb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取出盒子中的某个文件
	 * 
	 * @param fileId
	 * @return
	 */
	public SmObjInfo pickUpFiles(String fileId) {
		String msgstr = "";
		SmObjInfo msg = new SmObjInfo();
		WFI_BOXINST boxinst = null;
		String[] fileIds = fileId.split(",");
		if (fileIds.length > 0) {
			for (String id : fileIds) {
				if (!id.equals("")) {
					boxinst = commonDao.get(WFI_BOXINST.class, id);
					if (boxinst != null) {
						boxinst.setStatus(0);
						commonDao.update(boxinst);
					}
				}
			}
			WFD_BOXDEF boxdef = commonDao.get(WFD_BOXDEF.class, boxinst.getBoxdef_Id());
			if (boxdef != null) {
				Integer count = boxdef.getJs();
				if (count > 0) {
					msgstr = "取件成功";
					boxdef.setJs(count - fileIds.length);
					commonDao.update(boxdef);
				} else {
					msgstr = "已经没有案卷了！！！";
				}

			}
			commonDao.flush();
			msg.setID(fileId);
			msg.setDesc(msgstr);
		}
		return msg;
	}

	/**
	 * 
	 * @Title: getBoxDefList @Description: 获取虚拟柜的信息-分页 @author: 郭浩龙 @date:
	 *         2016年2月26日 下午3:42:19 @param: @param pageSizeStr @param: @param
	 *         pageIndexStr @param: @return @return: Page @throws
	 */
	public Page getBoxDefList(String pageSizeStr, String pageIndexStr) {
		int pageSize;
		int pageIndex;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			pageIndex = Integer.parseInt(pageIndexStr);
			return commonDao.GetPagedData(WFD_BOXDEF.class, pageIndex, pageSize, "BOXDEF_PID IS NULL ORDER BY SORT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: getBoxDefByDefId @Description: 根据DefId获取虚拟柜内所有子元素-分页 @author:
	 *         郭浩龙 @date: 2016年2月26日 下午11:03:13 @param: @param
	 *         boxDef_Id @param: @param pageSizeStr @param: @param
	 *         pageIndexStr @param: @return @return: Page @throws
	 */
	public Page getBoxDefByDefId(String boxDef_Id, String pageSizeStr, String pageIndexStr) {
		int pageSize;
		int pageIndex;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			pageIndex = Integer.parseInt(pageIndexStr);
			StringBuilder stb = new StringBuilder();
			stb.append("BOXDEF_PID='");
			stb.append(boxDef_Id);
			stb.append("' ORDER BY ROW_NUM,COLUMN_NUM");
			return commonDao.GetPagedData(WFD_BOXDEF.class, pageIndex, pageSize, stb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: getBoxDefById @Description: 根据boxDef_Id获取虚拟柜对象 @author: 郭浩龙 @date:
	 *         2016年2月26日 下午8:59:19 @param: @param
	 *         boxDef_Id @param: @return @return: WFD_BOXDEF @throws
	 */
	public WFD_BOXDEF getBoxDefById(String boxDef_Id) {
		return commonDao.get(WFD_BOXDEF.class, boxDef_Id);
	}

	/**
	 * 
	 * @Title: addBoxDef @Description: 虚拟柜信息维护-单条信息添加 @author: 郭浩龙 @date: 2016年2月26日
	 *         下午5:02:21 @param: @param boxDef @param: @return @return:
	 *         SmObjInfo @throws
	 */
	public SmObjInfo addBoxDef(WFD_BOXDEF boxDef) {
		SmObjInfo msg = new SmObjInfo();
		try {
			String boxId = Common.CreatUUID();
			boxDef.setBoxdef_Id(boxId);
			commonDao.save(boxDef);
			commonDao.flush();
			msg.setID(boxId);
			msg.setDesc("添加虚拟柜成功");
		} catch (Exception e) {
			msg.setDesc("添加虚拟柜失败");
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 
	 * @Title: updateBoxDef @Description: 虚拟柜信息维护-单条信息更新 @author: 郭浩龙 @date:
	 *         2016年2月26日 下午10:51:32 @param: @param boxDef @param: @return @return:
	 *         SmObjInfo @throws
	 */
	public SmObjInfo updateBoxDef(WFD_BOXDEF boxDef) {
		SmObjInfo msg = new SmObjInfo();
		try {
			commonDao.update(boxDef);
			commonDao.flush();
			String boxId = boxDef.getBoxdef_Id();
			msg.setID(boxId);
			msg.setDesc("更新虚拟柜成功");
		} catch (Exception e) {
			msg.setDesc("更新虚拟柜失败");
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 
	 * @Title: multipleAddBoxDef @Description: 虚拟柜信息维护-批量添加 @author: 郭浩龙 @date:
	 *         2016年2月26日 下午9:06:09 @param: @param boxName @param: @param
	 *         boxBM @param: @param rowNumStr @param: @param
	 *         columnNumStr @param: @return @return: SmObjInfo @throws
	 */
	public SmObjInfo multipleAddBoxDef(String boxName, String boxBM, String sort, String rowNumStr,
			String columnNumStr) {
		SmObjInfo msg = new SmObjInfo();
		// 先创建柜子本身实例
		WFD_BOXDEF boxDef = new WFD_BOXDEF();
		String boxId = Common.CreatUUID();
		boxDef.setBoxdef_Id(boxId);
		boxDef.setBox_Name(boxName);
		boxDef.setBox_Bm(boxBM);
		if (sort != null && !sort.equals("")) {
			try {
				boxDef.setSort(Integer.parseInt(sort));
			} catch (Exception e) {
				msg.setDesc("柜子排序号格式不正确");
				e.printStackTrace();
			}
		}
		// 再根据行数和列数创建虚拟柜实例
		if (rowNumStr != null && !rowNumStr.equals("") && columnNumStr != null && !columnNumStr.equals("")) {
			try {
				int rowNum = Integer.parseInt(rowNumStr);
				int columnNum = Integer.parseInt(columnNumStr);
				// 将行数和列数存储到虚拟柜信息中
				boxDef.setColumn_Num(columnNum);
				boxDef.setRow_Num(rowNum);
				int sortNum = 1;
				if (rowNum >= 1 && columnNum >= 1) {
					for (int i = 1; i <= rowNum; i++) {
						for (int j = 1; j <= columnNum; j++) {
							WFD_BOXDEF boxDefTemp = new WFD_BOXDEF();
							boxDefTemp.setBoxdef_Pid(boxId);
							String boxBmTemp = i + "-" + j;
							boxDefTemp.setBox_Bm(boxBmTemp);
							boxDefTemp.setBox_Name(boxBmTemp);
							boxDefTemp.setRow_Num(i);
							boxDefTemp.setColumn_Num(j);
							boxDefTemp.setSort(sortNum);
							commonDao.save(boxDefTemp);
							sortNum++;
						}
					}
					commonDao.save(boxDef);
					commonDao.flush();
					msg.setID(boxId);
					msg.setDesc("批量添加虚拟柜成功");
				} else {
					msg.setDesc("提供的层数和个数不符合要求");
				}
			} catch (Exception e) {
				msg.setDesc("添加失败");
				e.printStackTrace();
			}
		}
		return msg;
	}

	/**
	 * 
	 * @Title: deleteBoxDef @Description: 虚拟柜信息维护-删除 @author: 郭浩龙 @date: 2016年2月26日
	 *         下午5:13:41 @param: @param boxDefId @param: @return @return:
	 *         SmObjInfo @throws
	 */
	public SmObjInfo deleteBoxDef(String boxDefId) {
		SmObjInfo msg = new SmObjInfo();
		WFD_BOXDEF boxdef = null;
		boxdef = commonDao.get(WFD_BOXDEF.class, boxDefId);
		if (boxdef == null) {
			msg.setDesc("当前已经不存在该记录");
		} else {
			try {
				List<WFI_BOXINST> boxInstList = null;
				StringBuilder stb = new StringBuilder();
				stb.append("status is null and boxdef_pid='" + boxdef.getBoxdef_Id() + "'");
				boxInstList = commonDao.getDataList(WFI_BOXINST.class, Common.WORKFLOWDB + "WFI_BOXINST",
						stb.toString());
				if (boxInstList != null && boxInstList.size() > 0) {
					msg.setDesc("柜内还有文件，不能删除！！！");
				} else {
					commonDao.delete(WFD_BOXDEF.class, boxDefId);
					commonDao.flush();
					msg.setID(boxDefId);
					msg.setDesc("删除虚拟柜成功");
				}

			} catch (Exception e) {
				msg.setDesc("删除虚拟柜失败");
				e.printStackTrace();
			}
		}
		return msg;
	}

	/**
	 * 
	 * @Title: addBoxInst @Description: 入柜操作-增加虚拟柜实例 @author: 郭浩龙 @date: 2016年2月26日
	 *         下午4:53:32 @param: @param staff @param: @param boxDefId @param: @param
	 *         lsh @param: @return @return: SmObjInfo @throws
	 */
	public SmObjInfo addBoxInst(User staff, String boxDefId, String lsh) {
		SmObjInfo msg = new SmObjInfo();
		List<WFI_BOXINST> insts = searchBoxInst(lsh);
		if (insts != null && insts.size() > 0) {
			WFI_BOXINST inst = insts.get(0);
			msg.setDesc("案卷" + lsh + " 已经存入" + inst.getBox_Name() + ",不能重复存入");
		} else {
			// 获取虚拟柜子定义
			WFD_BOXDEF boxdef = null;
			boxdef = commonDao.get(WFD_BOXDEF.class, boxDefId);
			if (boxdef != null) {
				String boxId = Common.CreatUUID();
				WFI_BOXINST boxInst = new WFI_BOXINST();
				boxInst.setBoxdef_Id(boxId);
				boxInst.setBox_Bm(boxdef.getBox_Bm());
				boxInst.setBox_Name(boxdef.getBox_Name());
				boxInst.setBoxdef_Id(boxdef.getBoxdef_Id());
				boxInst.setBoxdef_Pid(boxdef.getBoxdef_Pid());
				boxInst.setContent(lsh);
				boxInst.setLx(boxdef.getLx());
				boxInst.setRgsj(new Date());
				boxInst.setStaff_Id(staff.getId());
				boxInst.setStaff_Name(staff.getUserName());
				try {
					commonDao.save(boxInst);
					Integer count = boxdef.getJs();
					if (count == null) {
						count = 0;
					}
					boxdef.setJs(++count);
					commonDao.update(boxdef);
					commonDao.flush();
					msg.setName(boxdef.getJs() + "");
					msg.setID(boxdef.getBoxdef_Id());
					msg.setDesc("数据入柜成功");
				} catch (Exception e) {
					msg.setDesc("入柜失败");
					e.printStackTrace();
				}
			} else {
				msg.setDesc("没有该虚拟柜子的定义");
			}
		}

		return msg;
	}

	/**
	 * 
	 * @Title: searchBoxInst @Description: 根据流水号查询虚拟柜信息 @author: 郭浩龙 @date:
	 *         2016年2月26日 下午6:08:16 @param: @param lsh @param: @return @return:
	 *         List<WFI_BOXINST> @throws
	 */
	public List<WFI_BOXINST> searchBoxInst(String lsh) {
		List<WFI_BOXINST> boxInstList = null;
		StringBuilder stb = new StringBuilder();
		stb.append("CONTENT LIKE '%");
		stb.append(lsh);
		stb.append("%' and status is null");
		boxInstList = commonDao.getDataList(WFI_BOXINST.class, Common.WORKFLOWDB + "WFI_BOXINST", stb.toString());
		return boxInstList;
	}

	/**
	 * 通过调档号获取
	 * 
	 * @param ddds
	 *            调档单集合
	 * @param field
	 *            排序字段
	 * @param order
	 *            配需方式
	 * @return
	 */
	public List<Map> getArchivesByDDDs(String ddds, String field, String order) {
		StringBuilder sb = new StringBuilder();
		sb.append("select transferlistid,lsh,ajid,zl,QZH,olddah,ddh from " + Common.WORKFLOWDB
				+ "wfi_transferlist list left join " + Common.WORKFLOWDB
				+ "wfi_dossiertransfer dossier on dossier.transferid=list.transferid where");
		sb.append(" DDH in (");
		sb.append(ddds);
		sb.append(") order by ");
		sb.append(field);
		sb.append(" " + order);

		return commonDao.getDataListByFullSql(sb.toString());

	}

	/**
	 * 获取所有的部门
	 * 
	 * @return
	 */
	public List<Map> getallDepts() {
		Map map = new HashMap();
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct departmentname from ");
		sb.append("SMWB_FRAMEWORK.");
		sb.append("t_department  where departmentname is not null  order by departmentname desc");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());

		return list;
	}

	/**
	 * 维护档案号
	 * 
	 * @param ajid
	 * @param lsh
	 * @param dah
	 * @return
	 */
	public boolean MatainAJH(String ajid, String lsh, String dah) {
		boolean Result = false;
		StringBuilder sb = new StringBuilder();
		sb.append(" AJID='");
		sb.append(ajid);
		sb.append("' and LSH='");
		sb.append(lsh);
		sb.append("'");
		List<WFI_TRANSFERLIST> list = commonDao.getDataList(WFI_TRANSFERLIST.class,
				Common.WORKFLOWDB + "WFI_TRANSFERLIST", sb.toString());
		if (list != null && list.size() > 0) {
			for (WFI_TRANSFERLIST item : list) {
				item.setDah(dah);
				commonDao.update(item);
			}
			commonDao.flush();
			Result = true;
		}
		return Result;
	}

	/**
	 * 收件资料批量上传服务
	 * 
	 * @param map
	 * @param proinstid
	 */
	public void afterUploadMasterZip(List<Map> map, String proinstid) {
		String basic = ConfigHelper.getNameByValue("MATERIAL");
		String materialtype = ConfigHelper.getNameByValue("MATERIALTYPE");// 扫描件存储方式 1 硬盘, 3服务
		boolean isUnZip = false;// 解压文件判断
		if (map != null && map.size() > 0) {
			Map uploadres = map.get(0);
			String disksymbol = uploadres.get("disksymbol") + "";
			if (disksymbol != null && !StringHelper.isEmpty(disksymbol) && !disksymbol.equals("")
					&& !disksymbol.equals("null")) {
				basic = disksymbol;
			}
			String fileName = uploadres.get("filename") + "";// batchFile.zip
			String filePath = uploadres.get("filepath") + "";// 201611\09\34c6615740984b8a85e6f509e7ccfcc7
			// 解压文件到当前文件夹:要解压的文件路径和解压之后的输出路径
			String zipFileName = basic + filePath + File.separator + fileName;
			String extPlace = basic + filePath;
			isUnZip = false;
			try {
				isUnZip = unZipFile.unzip(zipFileName, extPlace);
				if (isUnZip) {
					// TODO:解压成功之后：1.删除压缩包，2更改文件夹的名称和文件名称3.生成资料目录
					File file = new File(zipFileName);
					if (file.exists()) {
						file.delete();
					}
					File fileDir = new File(extPlace);
					File[] files = fileDir.listFiles();
					File fileTemp = null;
					String fileRealDir = "";
					String fileNameTemp = "";
					String fileParent = "";
					String newFileName = "";
					// 不删除头像采集分类
					// commonDao.deleteQuery(" delete from " + Common.WORKFLOWDB + "Wfi_ProMater " +
					// " WHERE PROINST_ID ='"
					// + proinstid + "' and img_path is null and Material_Index<>0");
					List<Wfi_ProMater> list = commonDao.getDataList(Wfi_ProMater.class,
							Common.WORKFLOWDB + "Wfi_ProMater ",
							"PROINST_ID ='" + proinstid + "' and img_path is not null ");
					for (int i = 0, j = files.length; i < j; i++) {
						fileTemp = files[i];
						fileRealDir = fileTemp.getPath();
						fileNameTemp = fileTemp.getName();
						if (fileTemp.isDirectory() && fileNameTemp.length() != 32) {
							fileParent = fileTemp.getParent();
							// TODO:生成资料目录:1删除之前的资料内容
							Wfi_ProMater promaster = new Wfi_ProMater();
							promaster.setMaterial_Id("");
							promaster.setMaterial_Count(1);
							promaster.setMaterial_Desc(fileNameTemp);
							promaster.setMaterial_Index(i + 1);
							promaster.setMaterial_Name(fileNameTemp);
							promaster.setMaterial_Pagecount(1);
							promaster.setMaterial_Type(1);
							promaster.setMaterial_Need(0);
							promaster.setProinst_Id(proinstid);
							// TODO:确认路径
							promaster.setMaterial_Status(2);// 已经上传
							promaster.setMaterial_Date(new Date());// 上传时间
							for (Wfi_ProMater mater : list) {
								if (fileNameTemp.equals(mater.getMaterial_Name())) {
									promaster = mater;
								}
							}
							File[] childrenfilese = fileTemp.listFiles();
//							List<Wfi_MaterData> wfi_materdataList = commonDao.getDataList(Wfi_MaterData.class,
//									Common.WORKFLOWDB + "WFI_MATERDATA ",
//									"materilinst_id in (select t.materilinst_id  from  BDC_WORKFLOW.WFI_PROMATER t where t.proinst_id='"
//											+ proinstid + "') ");
							List<Wfi_MaterData> wfi_materdataList = commonDao.getDataList(Wfi_MaterData.class,
									Common.WORKFLOWDB + "WFI_MATERDATA ",
									"materilinst_id = '"+ promaster.getMaterilinst_Id()+ "' ");
							if (materialtype != null && !materialtype.equals("") && materialtype.equals("1")) {// 硬盘储蓄
								for (int k = 0, l = childrenfilese.length; k < l; k++) {
									File childFile = childrenfilese[k];
									boolean f = false;
									if(wfi_materdataList!=null&&wfi_materdataList.size()>0) {
										for (Wfi_MaterData wfi_materdata : wfi_materdataList) {
											if (wfi_materdata.getFile_Name().equals(childFile.getName()) && false == f) {
												f = true;
												break;
											}
										}
									}

									if (f) {
										continue;
									}
									Wfi_MaterData materData = new Wfi_MaterData();
									materData.setFile_Name(childFile.getName());
									materData.setMaterilinst_Id(promaster.getMaterilinst_Id());
									materData.setFile_Index(new Date().getTime());
									materData.setUpload_Id(smstaff.getCurrentWorkStaffID());
									materData.setUpload_Name(smstaff.getCurrentWorkStaff().getUserName());
									materData.setPath(filePath + File.separator + promaster.getMaterilinst_Id());
									materData.setFile_Path(childFile.getName());
									materData.setUpload_Date(new Date());
									materData.setThumb("");
									materData.setDisc(basic);

									/*
									 * File childFile = childrenfilese[k]; String childParent
									 * =childFile.getParent(); String newChildFileName = new
									 * Date().getTime()+"_"+childFile.getName(); childFile.renameTo(new
									 * File(childParent+File.separator +newChildFileName));
									 */
									// TODO:更改文件名称
									promaster.setImg_Path(materData.getMaterialdata_Id());
									commonDao.saveOrUpdate(materData);
								}
								File existFile = new File(
										fileDir.getPath() + File.separator + promaster.getMaterilinst_Id());
								// 资料文件夹若存在，只转存文件,之后删除当前文件夹
								if (existFile.exists()) {
									File[] cuurFiles = fileTemp.listFiles();
									// 遍历文件验证文件是否重复
									for (int k = 0; k < cuurFiles.length; k++) {
										String kname = cuurFiles[k].getName();
										File[] existFiles = existFile.listFiles();
										for (int l = 0; l < existFiles.length; l++) {
											if (existFiles[l].getName().equals(kname)) {
												existFiles[l].delete();
											}
										}
										cuurFiles[k].renameTo(new File(fileDir.getPath() + File.separator
												+ promaster.getMaterilinst_Id() + File.separator + kname));
									}
									fileTemp.delete();// 删除当前文件夹
								} else {
									fileTemp.renameTo(
											new File(fileParent + File.separator + promaster.getMaterilinst_Id()));
								}
								commonDao.saveOrUpdate(promaster);
							} else if (materialtype != null && !materialtype.equals("") && materialtype.equals("3")) {// 服务上传方法
								// 1.上传文件到服务
								for (File fileNew : childrenfilese) {
									Wfi_MaterData materData = null;
									// 判断重复
									boolean f = false;
									if(wfi_materdataList!=null&&wfi_materdataList.size()>0) {
										for (Wfi_MaterData wfi_materdata : wfi_materdataList) {
											if (wfi_materdata.getFile_Name().equals(fileNew.getName()) && false == f) {
												materData = wfi_materdata;
												f = true;
												break;
											}
										}
									}
									if (f) {
										// 重复
										Http.postFileByWfi_materdata(fileNew, materData);
										continue;
									} else {
										// 新增
										materData = new Wfi_MaterData();
										materData.setFile_Name(fileNew.getName());
										materData.setMaterilinst_Id(promaster.getMaterilinst_Id());
										materData.setFile_Index(new Date().getTime());
										materData.setUpload_Id(smstaff.getCurrentWorkStaffID());
										materData.setUpload_Name(smstaff.getCurrentWorkStaff().getUserName());
										materData.setPath(filePath + File.separator + promaster.getMaterilinst_Id());
										materData.setFile_Path(fileNew.getName());
										materData.setUpload_Date(new Date());
										materData.setThumb("");
										promaster.setImg_Path(materData.getMaterialdata_Id());
										materData.setDisc(ConfigHelper.getNameByValue("MATERIAL"));
										Http.postFileByWfi_materdata(fileNew, materData);
										commonDao.saveOrUpdate(materData);
									}
								}
							}

						}
					}
					commonDao.flush();
				}
				
				 if (materialtype != null && !materialtype.equals("") && materialtype.equals("3")) {// 服务上传方法
					// 删除临时文件
						FileTool.delAllFile(basic);
				 }
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 批量上传资料的服务
	 * 
	 * @date 2016年11月10日 下午12:19:36
	 * @author JHX
	 * @param request
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> uploadMasterZipFiles(HttpServletRequest request, MultipartFile file) {
		List<Map> map = new ArrayList<Map>();
		try {
			String proisntid = request.getParameter("proinstid");
			Wfi_ProInst wfi_proisnt = commonDao.get(Wfi_ProInst.class, proisntid);
			String fileName = UUID.randomUUID().toString().replace("-", "") + ".zip";
			SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");
			String first = fir.format(wfi_proisnt.getCreat_Date());
			SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
			String second = sec.format(wfi_proisnt.getCreat_Date());
			String pathfile = first + File.separator + second + File.separator + proisntid;
			String materialtype = ConfigHelper.getNameByValue("MATERIALTYPE");// 扫描件存储方式 1 硬盘, 3服务
			String material = ConfigHelper.getNameByValue("MATERIAL"); // 当前使用的盘符
			String allpath = "";
			String folderPath = "";
			if (materialtype != null && !materialtype.equals("") && materialtype.equals("1")) {
				// 硬盘存储方式
				folderPath = material + pathfile;
				allpath = folderPath + File.separator + fileName;
			} else if (materialtype != null && !materialtype.equals("") && materialtype.equals("3")) {
				// 服务上传方式
				material = request.getSession().getServletContext().getRealPath("/") + "resources\\tmp\\"; // 获取项目路径
				folderPath = material + pathfile;
				allpath = folderPath + File.separator + fileName;
			}
			File dirFile = new File(folderPath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File tempFile = new File(allpath);
			// fileName = tempFile.getName();
			// 文件存在，先删除
			if (tempFile.exists()) {
				tempFile.delete();
			}
			tempFile.createNewFile();
			file.transferTo(tempFile);
			Map mapmess = new HashMap();
			mapmess.put("filepath", pathfile);
			mapmess.put("filename", fileName);
			mapmess.put("disksymbol", material);
			map.add(mapmess);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 档案移交到处excel
	 * 
	 * @date 2016年12月04日
	 * @author Dff
	 * @throws IOException
	 *             startrow 从第几行开始 addXH 是否自动添加序号
	 */
	public String excelDownload1(String url, String outpath, String tmpFullName, LinkedHashMap<String, String> map,
			List<Map<String, String>> djdys, int startrow, boolean addXH) throws IOException {
		FileOutputStream outstream = new FileOutputStream(outpath);
		InputStream input = new FileInputStream(tmpFullName);
		HSSFWorkbook wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		Map<String, Integer> MapCol = new HashMap<String, Integer>();
		MapCol.put("序号", 0);

		int i = addXH ? 1 : 0;
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			String key = entry.getKey().toString();
			MapCol.put(key, i++);
		}
		int rownum = startrow;
		for (Map<String, String> djdy : djdys) {
			HSSFRow row = (HSSFRow) sheet.createRow(rownum);
			HSSFCell cell = null;
			try {
				if (addXH) {
					HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					Cell0.setCellValue(rownum - startrow + 1);
				}
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey().toString();
					String val = entry.getValue().toString();
					cell = row.createCell(MapCol.get(key));
					if (key.equals("活动结束时间") || key.equals("ACTINST_END")) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
						if (null != djdy.get(val)) {
							String obj = StringHelper.formatObject(djdy.get(val));
							String date = sdf2.format(sdf.parse(obj));
							cell.setCellValue(date);
						}
					} else {
						cell.setCellValue(StringHelper.formatObject(djdy.get(val)));
					}
				}
				rownum++;
			} catch (Exception ex) {

			}
		}
		wb.write(outstream);
		outstream.flush();
		outstream.close();
		return url;
	}

	public String excelDownload(String url, String outpath, String tmpFullName, LinkedHashMap<String, String> map,
			List<Map<String, String>> djdys) throws IOException {
		String url1 = excelDownload1(url, outpath, tmpFullName, map, djdys, 2, true);
		return url1;
	}

	public String getCurrentKhByFilenumber(String file_number) {
		String result = "";
		List<WFI_TRANSFERLIST> list = commonDao.getDataList(WFI_TRANSFERLIST.class,
				Common.WORKFLOWDB + "WFI_TRANSFERLIST", " FILE_NUMBER='" + file_number + "' order by CREATE_TIME DESC");
		if (list.size() > 0) {
			for (WFI_TRANSFERLIST transfer : list) {
				WFI_DOSSIERTRANSFER doss = commonDao.get(WFI_DOSSIERTRANSFER.class, transfer.getTransferid());
				String kh = doss.getTransferCode();
				if (null != doss && !StringHelper.isEmpty(kh)) {
					result = kh;
					break;
				}
			}
		}
		return result;
	}

	public SmObjInfo extractMater(String file_number) {
		Wfi_ProInst proinst = smProinst.GetProInstByFileNumber(file_number);
		String prolsh = proinst.getProlsh();
		SmObjInfo info = new SmObjInfo();
		info.setDesc("没有找到关联的资料！");
		List<Map> result = commonDao.getDataListByFullSql(
				"SELECT QL.YWH FROM BDCK.BDCS_XMXX XMXX " + "LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON XMXX.XMBH=DJDY.XMBH "
						+ "LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.SCBDCDYID= DJDY.BDCDYID "
						+ "LEFT JOIN BDCK.BDCS_DJDY_XZ YCDJDY ON YCDJDY.BDCDYID=GX.YCBDCDYID "
						+ "LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=YCDJDY.DJDYID WHERE QL.QLLX='4' AND XMXX.YWLSH='"
						+ prolsh + "'");
		if (null != result && result.size() > 0) {
			Object oldLsh = result.get(0).get("YWH");
			if (!StringHelper.isEmpty(oldLsh)) {
				Wfi_ProInst oldProinst = smProinst.GetProInstByFileNumber((String) oldLsh);
				if (null != oldProinst) {
					List<Wfi_ProMater> proMaterList = commonDao.findList(Wfi_ProMater.class,
							" proinst_id = '" + oldProinst.getProinst_Id() + "'");
					if (null != proMaterList && proMaterList.size() > 0) {
						User currUser = smstaff.getCurrentWorkStaff();
						for (Wfi_ProMater proMater : proMaterList) {
							if (proMater.getMaterial_Count() != null && proMater.getMaterial_Count() > 0) {
								Wfi_ProMater newMater = new Wfi_ProMater();
								newMater.setDossier_Index(proMater.getDossier_Index());
								newMater.setImg_Path(proMater.getImg_Path());
								newMater.setMaterial_Bm(proMater.getMaterial_Bm());
								newMater.setMaterial_Count(proMater.getMaterial_Count());
								newMater.setMaterial_Date(proMater.getMaterial_Date());
								newMater.setMaterial_Desc(proMater.getMaterial_Desc());
								newMater.setMaterial_Id(proMater.getMaterial_Id());
								newMater.setMaterial_Index(proMater.getMaterial_Index());
								newMater.setMaterial_Isdossier(proMater.getMaterial_Isdossier());
								newMater.setMaterial_Name(proMater.getMaterial_Name());
								newMater.setMaterial_Need(proMater.getMaterial_Need());
								newMater.setMaterial_Pagecount(proMater.getMaterial_Count());
								newMater.setMaterial_Status(proMater.getMaterial_Status());
								newMater.setMaterial_Type(proMater.getMaterial_Type());
								newMater.setMaterialdef_Id(proMater.getMaterialdef_Id());
								newMater.setMaterialtype_Id(proMater.getMaterialtype_Id());
								newMater.setProinst_Id(proinst.getProinst_Id());
								commonDao.save(newMater);
								List<Wfi_MaterData> materDataList = commonDao.findList(Wfi_MaterData.class,
										" materilinst_id = '" + proMater.getMaterilinst_Id() + "'");
								if (null != materDataList && materDataList.size() > 0) {
									for (Wfi_MaterData materData : materDataList) {
										Wfi_MaterData newMaterData = new Wfi_MaterData();
										newMaterData.setFile_Index(materData.getFile_Index());
										newMaterData.setFile_Name(materData.getFile_Name());
										newMaterData.setFile_Number(materData.getFile_Number());
										newMaterData.setFile_Path(materData.getFile_Path());
										newMaterData.setFile_Postfix(materData.getFile_Postfix());
										newMaterData.setFile_Year(materData.getFile_Year());
										newMaterData.setMaterilinst_Id(newMater.getMaterilinst_Id());
										newMaterData.setPath(materData.getPath());
										newMaterData.setStorage_Type(materData.getStorage_Type());
										newMaterData.setThumb(materData.getThumb());
										newMaterData.setUpload_Date(new Date());
										newMaterData.setUpload_Id(currUser.getId());
										newMaterData.setUpload_Name(currUser.getUserName());
										newMaterData.setUpload_Status(materData.getUpload_Status());
										commonDao.save(newMaterData);
									}
								}
							}
						}
						commonDao.flush();
						info.setID(currUser.getId());
						info.setDesc("抽取成功！");
					}
				}
			}
		}
		return info;

	}

	public String addMaterClass(Wfi_MaterClass materclass) {
		String proinst_id = materclass.getProinst_Id();
		if (!StringHelper.isEmpty(proinst_id)) {
			List<Wfi_MaterClass> classList = commonDao.findList(Wfi_MaterClass.class,
					"proinst_id='" + proinst_id + "'");
			commonDao.save(materclass);
			String typeId = materclass.getMaterialType_Id();
			if (null != classList && classList.size() > 0) {
				// 已经有目录，按照资料定义添加一份
				Wfi_ProInst proinst = commonDao.get(Wfi_ProInst.class, proinst_id);
				smProMater.CreateProMaterInst(proinst.getProdef_Id(), proinst_id, typeId);

			} else {
				// 没有目录，把现有资料关联到目录下
				List<Wfi_ProMater> materList = commonDao.findList(Wfi_ProMater.class,
						"proinst_id='" + proinst_id + "'");
				for (Wfi_ProMater promater : materList) {
					promater.setMaterialtype_Id(typeId);
					commonDao.update(promater);
				}
			}
			commonDao.flush();
		}
		return materclass.getMaterialType_Id();
	}

	public String removeFloderClass(String materClassId) {
		if (!StringHelper.isEmpty(materClassId)) {
			List<Wfi_ProMater> MaterList = commonDao.findList(Wfi_ProMater.class,
					"MATERIALTYPE_ID = '" + materClassId + "'");
			for (Wfi_ProMater promater : MaterList) {
				commonDao.deleteQuery("delete " + Common.WORKFLOWDB + "wfi_materdata where MATERILINST_ID='"
						+ promater.getMaterilinst_Id() + "'");
				commonDao.delete(promater);
			}
			commonDao.delete(Wfi_MaterClass.class, materClassId);
			commonDao.flush();
		}
		return materClassId;
	}

	public List<WFI_TRANSFERLIST> getTransferList(String transferid) {
		if (!StringHelper.isEmpty(transferid)) {
			return commonDao.findList(WFI_TRANSFERLIST.class, "TRANSFERID = '" + transferid + "'");
		}
		return null;
	}
	/**
	 * 档案移交
	 * @param Status
	 * @param staff_id
	 * @param page
	 * @param size
	 * @param key
	 * @param actinstname
	 * @param staffs
	 * @param proname
	 * @param starttime
	 * @param endtime
	 * @param deptname
	 * @param areas
	 * @param hk
	 * @param lshstart
	 * @param lshend
	 * @param actinstids
	 * @param sorttype
	 * @return
	 */
	public Message GetProjectDAYG(String Status, String staff_id, int page, int size, String key, String actinstname,
			String staffs, String proname, String starttime, String endtime, String deptname, String areas, String hk,
			String lshstart, String lshend, String actinstids,String sorttype) {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap = sqlFactory.getFileProjectSqlEx(Status, staff_id, page, size, key, actinstname, staffs, proname,
				starttime, endtime, deptname, areas, hk, lshstart, lshend, actinstids,sorttype);
		Message msg = new Message();
		long count = commonDao.getCountByFullSqlCustom(sqlmap.get("COUNT"));
		if (count > 0) {
			msg.setTotal(count);
			List<Map> list = commonDao.getDataListByFullSql(sqlmap.get("CONTENT"), page, size);
			if (hk == null || hk.equals("")) {
				list = ContatData(list, false);
			}
			msg.setRows(list);
		}

		return msg;
	}
}

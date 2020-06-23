package com.supermap.wisdombusiness.synchroinline.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.internetbusiness.util.BeresolvException;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.URLUtil;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proinst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_slxmsh;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class XmslshService {

	@Autowired
	CommonDaoInline baseCommonDaoInline;

	@Autowired
	private SmStaff smStaff;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CommonDao commonDao;
	@Autowired
    private SmProInstService ProInstService;
	/**
	 * 项目受理审核页面数据加载
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public JSONArray xmslshData(HttpServletRequest req) throws Exception {
		Integer page = Integer.parseInt(req.getParameter("pageNumber"));
		Integer rows = Integer.parseInt(req.getParameter("pageSize"));
		Integer startIndex = (page - 1) * rows + 1;
		Integer endIndex = page * rows;
		String sqr = req.getParameter("sqr");
		String djlx = req.getParameter("djlx");
		String qllx = req.getParameter("qllx");
		String sqsj = req.getParameter("sqsj");// 申请时间
		StringBuffer SQL_T = new StringBuffer();
		SQL_T.append(
				" select count(A.Id) n from PRO_PROINST A , PRO_PROPOSERINFO B where A.ID = B.PROINST_ID and B.Sqr_Sxh = '1' and A.shzt = '10'"); // 查询所有记录数
		if (sqr != "") {
			SQL_T.append(" and B.SQR_NAME='");
			SQL_T.append(sqr + "'");
		}
		if (djlx != "") {
			SQL_T.append(" and A.Djlx='");
			SQL_T.append(djlx + "'");
		}
		if (qllx != "") {
			SQL_T.append(" and A.QLLX='");
			SQL_T.append(qllx + "'");
		}
		if (sqsj != "") {
			SQL_T.append(" and  A.Prostart like to_date('");
			SQL_T.append(sqsj);
			SQL_T.append("','yyyy/MM/dd ')");
		}
		StringBuffer SQL = new StringBuffer();
		SQL.append(
				"select * from (select B.SQR_NAME sqr, B.SQR_TEL lxdh,A.DJLX djlx,A.QLLX qllx,A.PROSTART sqsj,A.SHZT shzt,A.id xmid,rownum nu ,trunc((rownum - 1)/10)+1 num ");
		SQL.append("from PRO_PROINST A left join");
		SQL.append("(select sqr_tel,sqr_name ,proinst_id ,sqr_sxh from PRO_PROPOSERINFO ) B");
		SQL.append(" on A.ID =B.PROINST_ID where B.Sqr_Sxh ='1' and A.shzt ='10' ");
		if (sqr != "") {
			SQL.append("and B.SQR_NAME='");
			SQL.append(sqr + "'");
		}
		if (djlx != "") {
			SQL.append("and A.Djlx='");
			SQL.append(djlx + "'");
		}
		if (qllx != "") {
			SQL.append("and A.QLLX='");
			SQL.append(qllx + "'");
		}
		if (sqsj != "") {
			SQL.append("and  A.Prostart like to_date('");
			SQL.append(sqsj);
			SQL.append("','yyyy/MM/dd ')");
		}
		SQL.append(")where nu between ");
		SQL.append(startIndex + " and ");
		SQL.append(endIndex);

		ResultSet shResult = null; // 查询后返回的结果集
		ResultSet shT_Result = null; // 查询后返回的结果集
		JSONArray jsonA = new JSONArray(); // jsonArray数组（json对象）
		String N = "";
		try {
			shResult = baseCommonDaoInline.excuteQuery(SQL.toString());// 执行sql查询
			shT_Result = baseCommonDaoInline.excuteQuery(SQL_T.toString());
			baseCommonDaoInline.flush();
			if (shResult != null) {
				if (shT_Result != null) {
					while (shT_Result.next()) {
						N = shT_Result.getString("n");
					}
				}
				while (shResult.next()) { // 遍历结果集
					JSONObject object = new JSONObject();// json对象（"key","value"）一对一关系
					String SQR = shResult.getString("sqr");
					String LXDH = shResult.getString("lxdh");
					String DJLX = shResult.getString("djlx");
					String QLLX = shResult.getString("qllx");
					String SQSJ = shResult.getString("sqsj");
					String SHZT = shResult.getString("shzt");
					String XMID = shResult.getString("xmid");
					String NUM = shResult.getString("num");
					object.put("sqr", SQR);
					object.put("lxdh", LXDH);
					object.put("djlx", DJLX);
					object.put("qllx", QLLX);
					object.put("sqsj", SQSJ);
					object.put("shzt", SHZT);
					object.put("xmid", XMID);
					object.put("num", NUM); // 页数
					object.put("total", N);
					jsonA.add(object); // 将json对象添加进json数组
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jsonA;
	}

	/**
	 * 添加审核意见与修改审核状态
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@Transactional
	public String xmslsh_up(HttpServletRequest req) throws Exception {
		User user = smStaff.getCurrentWorkStaff();// 获取当前user对象
		String shry = user.getUserName();// 获取当前登录用户名(审核人员)
		Date myDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String shrq = df.format(myDate);
		String xmid = req.getParameter("xmid");// 获取项目id
		String shzt = req.getParameter("shzt");// 获取审核状态
		String shyj = RequestHelper.getParam(req, "shyj");// 获取审核意见
		// StringBuffer SQL_H = new StringBuffer(); // 页面当前行数据
//		StringBuffer SQL_SHZT = new StringBuffer();// 页面当前行审核状态
//		StringBuffer SQL_SHYJ = new StringBuffer();// 页面当前行审核意见
		StringBuffer SQL_SHYJ_CX = new StringBuffer();// 查询审核意见表
		String type = "";
//		SQL_SHZT.append("UPDATE PRO_PROINST T  SET T.TBZT='2',T.SHZT='");
//		SQL_SHZT.append(shzt);
//		SQL_SHZT.append("' ");
//		if ("11".equals(shzt) || "21".equals(shzt)) {
//			SQL_SHZT.append(" , T.SEND_MSG='1' ");
//		}
//		SQL_SHZT.append("WHERE ID= '" + xmid + "'");

//		SQL_SHYJ.append(
//				"INSERT INTO PRO_SLXMSH (SH_ID ,SH_QJDC_RY,SH_QJDC_YJ ,SH_QJDC_RQ,SLXM_ID,SHZT )VALUES(SYS_GUID(),'");
//		SQL_SHYJ.append(shry + "','");
//		SQL_SHYJ.append(shyj + "',");
//		SQL_SHYJ.append("TO_DATE('" + shrq + "','yyyy/MM/dd HH24:mi:ss'),'");
//		SQL_SHYJ.append(xmid + "','");
//		SQL_SHYJ.append(shzt + "')");

		Pro_slxmsh SQL_SHYJ = new Pro_slxmsh();
		SQL_SHYJ.setSh_id(StringUtil.getUUID());
		SQL_SHYJ.setSh_xmsl_ry(shry);
		SQL_SHYJ.setSh_xmsl_yj(shyj);
		SQL_SHYJ.setSh_xmsl_rq(myDate);
		SQL_SHYJ.setSlxm_id(xmid);
		SQL_SHYJ.setShzt(Integer.parseInt(shzt));
		ResultSet shyjCx_Result = null; // 查询SQL_SHYJ2后返回的结果集
		JSONArray jsonE = new JSONArray(); // jsonArray数组（json对象）
		try {

			//驳回后判断，如果是交易系统创建的件，调用交易接口
			Pro_proinst pro = baseCommonDaoInline.get(Pro_proinst.class, xmid);
			pro.setTbzt(2);
			if ("11".equals(shzt) || "21".equals(shzt)) {
				pro.setSend_msg(1);
			}
			pro.setShzt(StringHelper.getInt(shzt));

			if(pro!=null && !StringHelper.isEmpty(pro.getSqbm()) && "11".equals(shzt)) {
				String url = ConfigHelper.getNameByValue("resaleBHurl");
				String sqbh = pro.getProlsh().substring(6,pro.getProlsh().length());
				String param = "SQBH="+ URLEncoder.encode(sqbh,"UTF-8")+"&SQBM="+ URLEncoder.encode(pro.getSqbm(),"UTF-8")
						+"&YWLX="+URLEncoder.encode(pro.getJyywlx(),"UTF-8")+"&DESC="+URLEncoder.encode(shyj,"UTF-8");

				String result = URLUtil.readContentFromPost2(new URL(url), param);
				System.out.print(result);
			}

//			int shzt_num = baseCommonDaoInline.updateBySql(SQL_SHZT.toString());
			baseCommonDaoInline.saveOrUpdate(pro);
			baseCommonDaoInline.saveOrUpdate(SQL_SHYJ);
			type = "1";

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("驳回外网错误，请联系管理员");
		}
		return type;
	}

	/**
	 * 获取当前登录用户名
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public JSONArray xmslshry(HttpServletRequest req) throws Exception {
		JSONObject obj = new JSONObject();
		JSONArray jsonAA = new JSONArray();
		User user = smStaff.getCurrentWorkStaff();// 获取当前user对象
		String SHRY = user.getUserName();// 获取当前登录用户名
		obj.put("shry", SHRY);
		jsonAA.add(obj);
		return jsonAA;
	}

	@Transactional
	public String IntelligentApproval(HttpServletRequest req) throws Exception {
		Pro_proinst pro_proinst = null;
		String prolsh = req.getParameter("prolsh");// 获取外网lsh
		String shzt = req.getParameter("shzt");// 获取审核状态
		String type = "3";//定义3种类型，1：成功  2：数据已登簿 3： 数据问题
		String shyj = "";
		try {
			User user = smStaff.getCurrentWorkStaff();// 获取当前user对象
			String shry = user.getUserName();// 获取当前登录用户名(审核人员)
			Date myDate = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String shrq = df.format(myDate);

			shyj = RequestHelper.getParam(req, "shyj");// 获取审核意见
			String file_number = req.getParameter("file_number");//获取file_number
			// StringBuffer SQL_H = new StringBuffer(); // 页面当前行数据
//		StringBuffer SQL_SHZT = new StringBuffer();// 页面当前行审核状态
//		StringBuffer SQL_SHYJ = new StringBuffer();// 页面当前行审核意见
//		StringBuffer SQL_SHYJ_CX = new StringBuffer();// 查询审核意见表
			List<BDCS_XMXX> xmxxs = commonDao.findList(BDCS_XMXX.class, "project_id ='" + file_number + "' and sfdb='1'");
			if (xmxxs.size() > 0) {
				type = "2";
				return type;
			}
			List<Pro_proinst> list = baseCommonDaoInline.getDataList(Pro_proinst.class, "prolsh = '" + prolsh + "'");
			if (list.size() > 0) {
				pro_proinst = list.get(0);
				pro_proinst.setTbzt(2);
				pro_proinst.setShzt(Integer.parseInt(shzt));
				pro_proinst.setSend_msg(1);
				pro_proinst.setLsh("");
				baseCommonDaoInline.update(pro_proinst);


				Pro_slxmsh slxmsh = new Pro_slxmsh();
				slxmsh.setSh_id(Common.CreatUUID());
				slxmsh.setSh_xmsl_ry(shry);
				slxmsh.setSh_xmsl_yj(shyj);
				slxmsh.setSh_xmsl_rq(StringHelper.FormatByDate3(shrq));
				slxmsh.setSlxm_id(pro_proinst.getId());
				slxmsh.setShzt(Integer.parseInt(shzt));
				baseCommonDaoInline.save(slxmsh);

				List<Wfi_ProInst> wfi_proinst = commonDao.getDataList(Wfi_ProInst.class, "select * from bdc_workflow.Wfi_ProInst t where file_number = '" + file_number + "'");
				if (wfi_proinst.size() > 0) {
					SmObjInfo smObjInfo = ProInstService.deleteProInst(wfi_proinst.get(0).getProinst_Id());
					if("checkfail".equals(smObjInfo.getID())) {
						throw new RuntimeException("驳回外网失败，请检查该业务是否已缴费");
					}

				}
			}
		} catch (RuntimeException e ){
			throw new RuntimeException("驳回外网失败:"+e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("驳回外网失败，请检查数据完整性");
		}
		try {

			if(!StringHelper.isEmpty(pro_proinst)) {
				//驳回后判断，如果是交易系统创建的件，调用交易接口
				Pro_proinst pro = baseCommonDaoInline.get(Pro_proinst.class, pro_proinst.getId());
				if(pro!=null && !StringHelper.isEmpty(pro.getSqbm()) && "11".equals(shzt)) {
					String url = ConfigHelper.getNameByValue("resaleBHurl");
					String sqbh = pro.getProlsh().substring(6,pro.getProlsh().length());
					String param = "SQBH="+ URLEncoder.encode(sqbh,"UTF-8")+"&SQBM="+ URLEncoder.encode(pro.getSqbm(),"UTF-8")
							+"&YWLX="+URLEncoder.encode(pro.getJyywlx(),"UTF-8")+"&DESC="+URLEncoder.encode(shyj,"UTF-8");

					String result = URLUtil.readContentFromPost2(new URL(url), param);
					System.out.print(result);
				}
				type = "1";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return type;
	}

}

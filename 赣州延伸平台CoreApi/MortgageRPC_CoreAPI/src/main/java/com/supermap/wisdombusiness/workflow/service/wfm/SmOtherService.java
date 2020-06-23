package com.supermap.wisdombusiness.workflow.service.wfm;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.ANSWER;
import com.supermap.wisdombusiness.workflow.model.Bank_Trustbook;
import com.supermap.wisdombusiness.workflow.model.Bank_TrustbookPage;
import com.supermap.wisdombusiness.workflow.model.QUESTION;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.util.FileUpload;

@Service("smOtherService")
public class SmOtherService {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmStaff smStaff;

	/**
	 * 获取提问信息
	 * 
	 * @param staffid
	 * @param title
	 * @param startdata
	 * @param enddata
	 * @param status
	 * @return
	 */
	public List<Map> GetQuetionData(String staffid, String title,
			String startdata, String enddata, String status) {

		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("QUESTION where 1>0 ");
		if (staffid != null && !staffid.equals("")) {
			sb.append(" and QUESTION_STAFFID='" + staffid + "' ");
		}
		if (title != null && !title.equals("")) {
			sb.append(" and QUESTION_TITLE like '%" + title + "'% ");
		}
		if (startdata != null && !startdata.equals("")) {
			sb.append(" and QUESTION_TIME> to_date('" + startdata
					+ "','yyyy-MM-dd')");
		}
		if (enddata != null && !enddata.equals("")) {
			sb.append(" and QUESTION_TIME< to_date('" + enddata
					+ "','yyyy-MM-dd')");
		}
		if (status != null && !status.equals("")) {
			sb.append(" and QUESTION_STATUS='" + status + "'");
		}
		// 默认10天
		// sb.append(" and question_time  between sysdate-10 and sysdate order by  question_time desc");
		//
		sb.append(" and (question_status=");
		sb.append(WFConst.Question_Status.Wait.value);
		sb.append(" or (question_status=");
		sb.append(WFConst.Question_Status.solved.value);
		sb.append("  and  question_time  between sysdate-3 and sysdate )) order by  question_time desc");
		return commonDao.getDataListByFullSql(sb.toString());

	}

	public Map GetAnswerData(String questionid) {
		Map map = new HashMap();
		if (questionid != null && !questionid.equals("")) {
			QUESTION question = commonDao.get(QUESTION.class, questionid);
			if (question != null) {
				map.put("question", question);
				List<ANSWER> list = commonDao.getDataList(ANSWER.class,
						"select * from " + Common.WORKFLOWDB
								+ "answer where question_id='" + questionid
								+ "' order by  answer_time desc");
				map.put("answer", list);
			}
		}
		return map;

	}

	/**
	 * 提交问题
	 * 
	 * @param title
	 * @param content
	 * @return
	 */
	public QUESTION SubmitQuestion(String title, String content, String type,
			String typename) {
		User user = smStaff.getCurrentWorkStaff();
		QUESTION question = null;
		if (title != null && !title.equals("") && content != null
				&& !content.equals("")) {
			question = new QUESTION();
			question.setQuestion_Id(Common.CreatUUID());
			question.setQuestion_Title(title);
			question.setQuestion_Content(content);
			question.setQuestion_Time(new Date());
			question.setQuestion_Staffid(user.getId());
			question.setQuestion_Staffname(user.getUserName());
			question.setQuestion_Type(type);
			question.setQuestion_Typename(typename);
			question.setQuestion_Status(WFConst.Question_Status.Wait.value + "");
			question.setQuestion_Deptid(user.getDepartment().getId());
			question.setQuestion_Dept(user.getDepartment().getDepartmentName());
			commonDao.save(question);
			commonDao.flush();

		}
		return question;

	}

	/**
	 * 提交答案
	 * 
	 * @param content
	 * @param questionid
	 * @return
	 */
	public String SubmitAnswer(String content, String questionid) {
		User user = smStaff.getCurrentWorkStaff();
		String Result = "";
		if (content != null && !content.equals("")) {
			QUESTION question = commonDao.get(QUESTION.class, questionid);
			ANSWER answer = new ANSWER();
			answer.setAnswer_Id(Common.CreatUUID());
			answer.setAnswer_Content(content);
			answer.setAnswer_Id(user.getId());
			answer.setAnswer_Staffname(user.getUserName());
			answer.setAnswer_Time(new Date());
			answer.setQuestion_Id(questionid);
			question.setQuestion_Status(WFConst.Question_Status.solved.value
					+ "");
			commonDao.update(question);
			commonDao.save(answer);
			commonDao.flush();
			Result = answer.getAnswer_Id();
		}
		return Result;
	}

	/**
	 * 删除提问
	 * 
	 * @param question
	 * @return
	 */
	public String DelQuestion(String question) {
		commonDao.deleteQuery("delete " + Common.WORKFLOWDB
				+ "Answer where question_id='" + question + "'");
		commonDao.delete(QUESTION.class, question);
		commonDao.flush();
		return question;

	}

	/**
	 * 删除回答
	 * 
	 * @param answerid
	 * @return
	 */
	public String DelAnswer(String answerid) {
		commonDao.delete(ANSWER.class, answerid);
		commonDao.flush();
		return answerid;

	}

	/**
	 * 新增修改保存 根据银行名称保存相应银行委托书信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2015年12月21日 23:29:08
	 * @param yhmc
	 * @return msg
	 * */

	public ResultMessage SavetrustbookInfo(Bank_Trustbook bank_Trustbook) {
		try {
			ResultMessage ms = new ResultMessage();
			String yhyyzzh = bank_Trustbook.getTrustbook_Id();
			String bank_name=bank_Trustbook.getBank_Name();
			List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(
					Bank_Trustbook.class,
					"select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE TRUSTBOOK_ID='"
							+ yhyyzzh + "'");
			if (bank_Trustbooks != null && bank_Trustbooks.size() > 0) {
				Bank_Trustbook bank_Trustbook1 = bank_Trustbooks.get(0);
				bank_Trustbook1.setBank_Name(bank_Trustbook.getBank_Name());
				bank_Trustbook1.setTrustbook_Id(bank_Trustbook
						.getTrustbook_Id());
				bank_Trustbook1.setTrustbook_Desc(bank_Trustbook
						.getTrustbook_Desc());
				bank_Trustbook1.setTrustor_Name(bank_Trustbook
						.getTrustor_Name());
				bank_Trustbook1.setTrustor_Tel(bank_Trustbook.getTrustor_Tel());
				bank_Trustbook1.setTrustor_Adrs(bank_Trustbook
						.getTrustor_Adrs());
				bank_Trustbook1.setTrustor_Desc(bank_Trustbook
						.getTrustor_Desc());
				commonDao.update(bank_Trustbook1);
				commonDao.flush();
				ms.setMsg("更新成功！");
				ms.setSuccess("true");
			} else {
				List<Bank_Trustbook> bank_Trustbooks1 = commonDao.getDataList(
						Bank_Trustbook.class,
						"select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE BANK_NAME='"
								+ bank_name + "'");
				if (bank_Trustbooks1 != null && bank_Trustbooks1.size() > 0) {
					for(Bank_Trustbook bank_Trustbook1:bank_Trustbooks1){
						commonDao.delete(bank_Trustbook1);
					}
				}
				// bank_Trustbook.setBank_Id(Common.CreatUUID());
				commonDao.save(bank_Trustbook);
				commonDao.flush();
				ms.setMsg("保存成功！");
				ms.setSuccess("true");
			}
			return ms;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 精确查询 根据银行名称和银行委托人返回银行委托书信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2015年12月22日 10:28:09
	 * @param yhmc
	 *            &yhwtr
	 * @return Bank_Trustbook
	 */
	public Bank_Trustbook GetBank_Trustbook(String yhmc, String yhyyzzh) {
		Bank_Trustbook bank_Trustbook = new Bank_Trustbook();
		List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(
				Bank_Trustbook.class,
				"select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE BANK_NAME = '"
						+ yhmc + "' or TRUSTBOOK_ID = '" + yhyyzzh + "'");
		if (bank_Trustbooks != null && bank_Trustbooks.size() > 0) {
			bank_Trustbook = bank_Trustbooks.get(0);
			return bank_Trustbook;
		}
		return bank_Trustbook;
	}
	/**
	 * 模糊查询 根据银行名称和银行委托人返回银行委托书信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2016年5月18日 10:27:23
	 * @param yhmc
	 *            &yhwtr
	 * @return Bank_Trustbook
	 */
	public List<Bank_Trustbook> GetBank_TrustbookMH(String yhmc, String yhyyzzh) {
		List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(
				Bank_Trustbook.class,
				"select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE BANK_NAME like '%"
						+ yhmc + "%' or TRUSTBOOK_ID like '%" + yhyyzzh + "%'");
		if (bank_Trustbooks != null && bank_Trustbooks.size() > 0) {
			return bank_Trustbooks;
		}
		return bank_Trustbooks;
	}
	

	/**
	 * 根据项目编号project_id获取该项目下所有银行权利人的更名文件归档备注信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2016年1月20日 15:41:21
	 * @param project_id
	 * @return Map<bankname,gdbz>
	 */
	public Map<String, String> GetBankGdbz(String project_id) {
		Map<String, String> bankggdbz = new HashMap<String, String>();
		List<BDCS_XMXX> bdcs_xmxxs = commonDao.getDataList(BDCS_XMXX.class,
				"select * from BDCK.BDCS_XMXX  WHERE PROJECT_ID = '"
						+ project_id + "'");
		if (bdcs_xmxxs != null && bdcs_xmxxs.size() > 0) {
			BDCS_XMXX bdcs_xmxx = bdcs_xmxxs.get(0);
			String xmbh = bdcs_xmxx.getId();
			List<BDCS_QLR_GZ> qlrs = commonDao.getDataList(BDCS_QLR_GZ.class,
					"select * from BDCK.BDCS_QLR_GZ  WHERE XMBH = '" + xmbh
							+ "'");
			if (qlrs != null && qlrs.size() > 0) {
				for (int i = 0; i < qlrs.size(); i++) {
					List<Bank_Trustbook> bank_Trustbooks = commonDao
							.getDataList(Bank_Trustbook.class,
									"select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE BANK_NAME='"
											+ qlrs.get(i).getQLRMC() + "'");
					if (bank_Trustbooks != null && bank_Trustbooks.size() > 0) {
						String bankdesc = bank_Trustbooks.get(0)
								.getTrustbook_Desc();
						bankggdbz.put(qlrs.get(i).getQLRMC(), bankdesc);
					}
				}
			}
		}
		return bankggdbz;
	}

	public String GetBankchangefile(String yhmc) {
		Connection jyConnection = null;
		String bankchangefile = "";
		try {
			jyConnection = JH_DBHelper.getConnect_jy();
			// 从“HOUSE.SYS_FILECODING”取得当前银行所归档案号信息
			String sql = "select * from HOUSE.SYS_FILECODING where filename like '%"
					+ yhmc + "%'";
			ResultSet imagescanset = JH_DBHelper.excuteQuery(jyConnection, sql);
			while (imagescanset.next()) {
				// 根据“图像ID”进行循环
				bankchangefile = imagescanset.getString("FILENAME");
				return bankchangefile;
			}
			jyConnection.close();
		} catch (Exception e) {

		}
		return bankchangefile;
	}

	/**
	 * 回执单添加根据项目编号project_id获取该项目下所有银行权利人的更名文件归档备注信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2016年2月27日 17:10:14
	 * @param project_id
	 * @return Map<bankname,changefileinfo>
	 * @throws SQLException 
	 */
	public List<String> GetBankgmwjgdhFromZJK(String project_id) throws SQLException {
//		Map<Map<String, String>, Map<String, String>> bankgmwjgdh = new HashMap<Map<String, String>, Map<String, String>>();
		List<String> bankgmwjgdh = new ArrayList<String>();
		List<BDCS_XMXX> bdcs_xmxxs = commonDao.getDataList(BDCS_XMXX.class,
				"select * from BDCK.BDCS_XMXX  WHERE PROJECT_ID = '"
						+ project_id + "'");
		if (bdcs_xmxxs != null && bdcs_xmxxs.size() > 0) {
			BDCS_XMXX bdcs_xmxx = bdcs_xmxxs.get(0);
			String xmbh = bdcs_xmxx.getId();
			List<BDCS_QLR_GZ> qlrs = commonDao.getDataList(BDCS_QLR_GZ.class,
					"select * from BDCK.BDCS_QLR_GZ  WHERE XMBH = '" + xmbh
							+ "'");
			if (qlrs != null && qlrs.size() > 0) {
				for (int i = 0; i < qlrs.size(); i++) {
					Connection jyConnection = null;
					jyConnection = JH_DBHelper.getConnect_jy();
					String sql = "select * from HOUSE.SYS_FILECODING where filename like '%"
							+ qlrs.get(i).getQLRMC() + "%'";
					ResultSet bankchangefileset;
					try {
						bankchangefileset = JH_DBHelper.excuteQuery(
								jyConnection, sql);
						while (bankchangefileset.next()) {
							bankgmwjgdh.add("bankname");
							bankgmwjgdh.add(qlrs.get(i).getQLRMC());
							bankgmwjgdh.add("archive");
							bankgmwjgdh.add(bankchangefileset.getString("FILENAME"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jyConnection.close();
				}
			}
		}
		return bankgmwjgdh;
	}
}

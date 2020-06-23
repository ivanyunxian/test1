package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Bank_Trustbook;
import com.supermap.wisdombusiness.workflow.model.Bank_TrustbookPage;
import com.supermap.wisdombusiness.workflow.model.QUESTION;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmOtherService;
import com.supermap.wisdombusiness.workflow.util.FileUpload;

@Controller
@RequestMapping("/other")
public class SmOtherController {
	@Autowired
	private SmOtherService smOtherService;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private CommonDao commonDao;
	/**
	 * 问答页面映射
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/qanda", method = RequestMethod.GET)
	public String QuestionAndAnswer(Model model) {
		return "/workflow/frame/questionandanswer";
	}
	
	@RequestMapping(value = "/qanda/questiondata", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> questiondata(HttpServletRequest request, HttpServletResponse response) {
		String showtype=request.getParameter("showtype");
		String staffid="";
		User user=smStaff.getCurrentWorkStaff();
		if(showtype!=null&&showtype.equals("staff")){
			staffid=user.getId();
		}
		else{
			
		}
		return smOtherService.GetQuetionData(staffid, "", "", "", "");
	}
	@RequestMapping(value = "/qanda/answerdata", method = RequestMethod.GET)
	@ResponseBody
	public Map answerdata(HttpServletRequest request, HttpServletResponse response) {
		String qusetionid=request.getParameter("questionid");
		return smOtherService.GetAnswerData(qusetionid);
	}
	/**
	 * 提交疑问
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qanda/submitquestion", method = RequestMethod.POST)
	@ResponseBody
	public QUESTION submitQuestion(HttpServletRequest request, HttpServletResponse response) {
		String title=request.getParameter("title");
		String content=request.getParameter("content");
		String type=request.getParameter("type");
		String typename=request.getParameter("typename");
		return smOtherService.SubmitQuestion(title, content,type,typename);
	}
	/**
	 * 提交回答
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qanda/submitanswer", method = RequestMethod.POST)
	@ResponseBody
	public String submitAnswer(HttpServletRequest request, HttpServletResponse response) {
		String content=request.getParameter("content");
		String questionid=request.getParameter("questionid");
		return smOtherService.SubmitAnswer(content, questionid);
	}
	@RequestMapping(value = "/qanda/delqusetion", method = RequestMethod.POST)
	@ResponseBody
	public String delQuestion(HttpServletRequest request, HttpServletResponse response) {
		String questionid=request.getParameter("questionid");
		return smOtherService.DelQuestion(questionid);
	}
	@RequestMapping(value = "/qanda/delanswer", method = RequestMethod.POST)
	@ResponseBody
	public String delAnswer(HttpServletRequest request, HttpServletResponse response) {
		String answerid=request.getParameter("answerid");
		return smOtherService.DelAnswer(answerid);
	}

	/**
	 * 新增修改保存 根据银行名称保存相应银行委托书信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2015年12月21日 23:29:08
	 * @param yhmc
	 * @return msg
	 * */
	@RequestMapping(value = "/savetrustbookinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage SavetrustbookInfo(
			HttpServletRequest request, HttpServletResponse response) {
		try {
			ResultMessage ms = new ResultMessage();
			Bank_Trustbook bank_Trustbook = new Bank_Trustbook();
			String yhmc = request.getParameter("yhmc");
			String yhyyzzh = request.getParameter("yhyyzzh");
			String yhwtsms = request.getParameter("yhwtsms");
			String yhwtrxm = request.getParameter("yhwtrxm");
			String yhwtrdh = request.getParameter("yhwtrdh");
			String yhwtrdz = request.getParameter("yhwtrdz");
			String yhwtrms = request.getParameter("yhwtrms");
			bank_Trustbook.setBank_Name(yhmc);
			bank_Trustbook.setTrustbook_Id(yhyyzzh);
			bank_Trustbook.setTrustbook_Desc(yhwtsms);
			bank_Trustbook.setTrustor_Name(yhwtrxm);
			bank_Trustbook.setTrustor_Tel(yhwtrdh);
			bank_Trustbook.setTrustor_Adrs(yhwtrdz);
			bank_Trustbook.setTrustor_Desc(yhwtrms);
//			bank_Trustbook.setTrustbook_Id(trustbookid);
			ms = smOtherService.SavetrustbookInfo(bank_Trustbook);
			return ms;
		} catch (Exception e) {

		}
		return null;
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
	@RequestMapping(value = "/getbank_trustbook", method = RequestMethod.POST)
	public @ResponseBody Bank_Trustbook GetBank_Trustbook(
			HttpServletRequest request, HttpServletResponse response) {
		String yhmc = request.getParameter("yhmc");
		String yhyyzzh = request.getParameter("yhmc");
		return smOtherService.GetBank_Trustbook(yhmc, yhyyzzh);
	}
	/**
	 * 模糊查询 根据银行名称和银行委托人返回银行委托书信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2016年5月18日 10:29:34
	 * @param yhmc
	 *            &yhwtr
	 * @return Bank_Trustbook
	 */
	@RequestMapping(value = "/getbank_trustbookmh", method = RequestMethod.POST)
	public @ResponseBody List<Bank_Trustbook> GetBank_TrustbookMH(
			HttpServletRequest request, HttpServletResponse response) {
		String yhmc = request.getParameter("yhmcmhcx");
		String yhyyzzh = request.getParameter("yhmcmhcx");
		return smOtherService.GetBank_TrustbookMH(yhmc, yhyyzzh);
	}

	/**
	 * 银行委托书预览 根据银行名称返回银行委托书原图二进制字符串
	 * @return 
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2015年12月30日 02:08:23
	 * @return byte[]
	 */
	@RequestMapping(value = "/previewBank_Trustbookpage/{trustbook_id}", method = RequestMethod.GET)
	public @ResponseBody  String previewBank_Trustbookpage(@PathVariable String trustbook_id,HttpServletRequest request) 
					throws IOException{
//		String trustbook_id = request.getParameter("yhyyzzh");
//		String file_name = request.getParameter("yhmc");
		List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(
				Bank_Trustbook.class, "select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE trustbook_id='"
						+ trustbook_id + "'");
		String trustbookpage_id=bank_Trustbooks.get(0).getTrustbookpage_Id();
		List<Bank_TrustbookPage> bank_TrustbookPages = commonDao.getDataList(
				Bank_TrustbookPage.class, "select * from BDC_WORKFLOW.BANK_TRUSTBOOKPAGE  WHERE trustbookpage_id='"
						+ trustbookpage_id + "'");
		Bank_TrustbookPage data=new Bank_TrustbookPage();
		if(bank_TrustbookPages!=null && bank_TrustbookPages.size()>0){
			data=bank_TrustbookPages.get(0);
		}else{
			return "";
		}
		
		return FileUpload.trustbookpagegetFile(data.getTrustbookpage_Id(), data.getTrustbookpage_Path());
		
//		OutputStream os = response.getOutputStream();
//		PrintWriter writer = response.getWriter();
//		response.setCharacterEncoding("UTF-8");
//		String fileName = request.getParameter("fileName");
//		fileName = "temp";
//		fileName = java.net.URLEncoder.encode(
//				new String(fileName.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");
//		try {
//			response.reset();
////			response.setHeader("Content-Disposition", "attachment; filename="
////					+ file_name);
////			response.setContentType("image/jpeg; charset=UTF-8");
//			response.setContentType("text/html;charset=UTF-8");
//			response.setHeader("Content-type","text/html;charset=UTF-8");
//			String file = FileUpload.trustbookpagegetFile(
//					data.getTrustbookpage_Id(), data.getTrustbookpage_Path());
//			if (file != null) {
//				response.getWriter().write(file);
////			    writer.write(file);
////			    writer.close();
////				os.write(file.getBytes("UTF-8"));
////				os.flush();
//			} else {
//
//			}
//		}finally {
//			if (writer != null) {
//				writer.close();
//			}
//		}
	}

	/**
	 * 申请表页面自动填充：根据银行名称获取银行营业执照号
	 * @作者 buxiaobo
	 * @创建时间2016年1月8日 16:30:57
	 * @param bank_name
	 * @return Bank_Trustbook
	 */
	@RequestMapping(value = "/getbank_trustbook/{bank_name}", method = RequestMethod.GET)
	public @ResponseBody String GetBankYyzzh(@PathVariable String bank_name,HttpServletRequest request) {
		List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(
				Bank_Trustbook.class, "select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE trustbook_id='"
						+ bank_name + "'");
		String yyzzh=bank_Trustbooks.get(0).getTrustbook_Id();
		return yyzzh;
	}
	
	
	
	
	/**
	 * 银行委托书查询页面模糊查询 根据银行名称返回银行变更文件所归档案号信息
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2016年2月26日 14:56:32
	 * @param yhmc
	 */
	@RequestMapping(value = "/getbankchangefile", method = RequestMethod.POST)
	public @ResponseBody String GetBankchangefile(
			HttpServletRequest request, HttpServletResponse response) {
		String yhmc = request.getParameter("yhmcmhcx");
		return smOtherService.GetBankchangefile(yhmc);
	}
	/**
	 * 回执单添加根据项目编号project_id获取该项目下所有银行权利人的银行委托书备注信息
	 * @param project_id
	 * @return Map<bankname,gdbz>
	 * 
	 */
	@RequestMapping(value = "/getbankgdbz/{project_id}", method = RequestMethod.GET)
	public @ResponseBody Map<String,String> GetBankGdbz(@PathVariable String project_id,HttpServletRequest request) {
		Map<String,String> bankgdbz=smOtherService.GetBankGdbz(project_id);
		return bankgdbz;
	}
	/**
	 * 回执单添加根据项目编号project_id获取该项目下所有银行权利人的更名文件所归档号信息
	 * @param project_id
	 * @return Map<bankname,bankgmwjgdh>
	 * @throws SQLException 
	 * 
	 */
	@RequestMapping(value = "/getbankgmwjgdh/{project_id}", method = RequestMethod.GET)
	public @ResponseBody List<String> GetBankgmwjgdhFromZJK(@PathVariable String project_id,HttpServletRequest request) throws SQLException {
		List<String> bankgdbz=smOtherService.GetBankgmwjgdhFromZJK(project_id);
		return bankgdbz;
	}
}

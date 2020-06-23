package com.supermap.realestate.registration.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

import com.supermap.realestate.registration.model.BDCS_QLR_D;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QZFFKSB;
import com.supermap.realestate.registration.model.BDCS_QZFFXMB;
import com.supermap.realestate.registration.model.BDCS_QZGLTJB;
import com.supermap.realestate.registration.model.BDCS_QZGLXMB;
import com.supermap.realestate.registration.model.BDCS_RKQZB;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZSZFPIC;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.T_ROLE;
import com.supermap.wisdombusiness.workflow.service.common.Common;

import net.sf.json.JSONArray;

/**
 * 权证管理Controller，包括权证入库项目管理，权证作废，权证过滤查询等。
 * @ClassName: CertManageController
 * @author liushufeng
 * @date 2015年10月29日 下午5:02:20
 */
@Controller
@RequestMapping("/certmanage")
public class CertManageController {

	@Autowired
	private CommonDao basecommondao;

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	/************************************权证入库管理************************************/
	/**
	 * 权证入库项目管理页面 (URL:"/projectmanage",Method：GET)
	 * @Title: ShowMappingMain
	 * @author:liushufeng
	 * @date：2015年10月29日 下午10:04:33
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "/projectmanage", method = RequestMethod.GET)
	public String ShowMappingMain(Model model) {
		return "/realestate/registration/certmanage/projectmanage";
	}

	/**
	 * 项目信息页面 (URL:"/projectinfoindex",Method：GET)
	 * @Title: ShowMappingMain2
	 * @author:liushufeng
	 * @date：2015年10月29日 下午10:03:25
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/projectinfoindex", method = RequestMethod.GET)
	public String ShowMappingMain2(Model model) {
		return "/realestate/registration/certmanage/certprojectinfo";
	}

	/**
	 * 新增或保存项目信息 (URL:"/projectinfo",Method：POST)
	 * @Title: saveOrUpdateProjectInfo
	 * @author:liushufeng
	 * @date：2015年10月29日 下午10:48:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/projectinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveOrUpdateProjectInfo(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg=new ResultMessage();
		if(Global.getCurrentUserInfo()==null){
			msg.setSuccess("false");
			msg.setMsg("保存失败！请先登录！");
			return msg;
		}
		String projectid = request.getParameter("id");
		String xmmc = request.getParameter("xmmc");
		String xmlx = request.getParameter("xmlx");
		String qzlx = request.getParameter("qzlx");
		String qzzl = request.getParameter("qzzl");
		String qsbh = request.getParameter("qsqzbh");
		String zzbh = request.getParameter("jsqzbh");
		String lqzhry = request.getParameter("lqzhry");
		String qzly = request.getParameter("qzly");
		String zszt=request.getParameter("zszt");
		BDCS_QZGLXMB xm = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(projectid)) {
			xm = basecommondao.get(BDCS_QZGLXMB.class, projectid);
			bnew = false;
		}
		if (xm == null) {
			xm = new BDCS_QZGLXMB();
			xm.setId((String) SuperHelper.GeneratePrimaryKey());
			xm.setCJRY(Global.getCurrentUserInfo().getLoginName());// 创建人员
			xm.setCJSJ(new Date());// 创建时间
		}
		xm.setXMMC(xmmc);
		xm.setXMLX(xmlx);// 项目类型
		xm.setSFRK(SF.NO.Value);// 是否入库
		xm.setSFYX(SF.NO.Value);// 是否有效
		xm.setQZLX(qzlx);// 权证类型
		xm.setQZZL(qzzl);
		xm.setQSQZBH(Long.parseLong(qsbh));// 起始编号
		xm.setJSQZBH(Long.parseLong(zzbh));// 终止编号
		xm.setQZLY(qzly);
		xm.setLQZHRY(lqzhry);// 终止编号
		xm.setZSZT(zszt);
		if (bnew)
			basecommondao.save(xm);
		else
			basecommondao.update(xm);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		YwLogUtil.addYwLog("权证管理-新增项目", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}

	/**
	 * 获取项目信息 (URL:"/projectinfo/{id}",Method：GET)
	 * @Title: getProjectInfo
	 * @author:liushufeng
	 * @date：2015年10月30日 上午12:23:07
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/projectinfo/{id}", method = RequestMethod.GET)
	public @ResponseBody Message getProjectInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		BDCS_QZGLXMB xm = basecommondao.get(BDCS_QZGLXMB.class, id);
		Message msg = new Message();
		List<BDCS_QZGLXMB> list = new ArrayList<BDCS_QZGLXMB>();
		list.add(xm);
		msg.setRows(list);
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-获取项目信息", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}

	/**
	 * 分页获取项目信息数据(URL:"/project",Method：POST)
	 * @Title: querProject
	 * @author:liushufeng
	 * @date：2015年10月29日 下午10:52:50
	 * @return
	 */
	@RequestMapping(value = "/project", method = RequestMethod.GET)
	public @ResponseBody Message querProject(HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		if (request.getParameter("filter") != null) {
			try {
				filter = RequestHelper.getParam(request, "filter");
			} catch (Exception e) {
			}
		}
		
		Page p=basecommondao.getPageDataByHql(BDCS_QZGLXMB.class, "1>0 ORDER BY CJSJ DESC", page, rows);
		if(!StringHelper.isEmpty(filter)){
			p=basecommondao.getPageDataByHql(BDCS_QZGLXMB.class, "XMMC LIKE '%"+filter+"%' ORDER BY CJSJ DESC", page, rows);
		}
		
		Message msg = new Message();
		msg.setTotal(p.getTotalCount());
		msg.setRows(p.getResult());
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;

	}

	/**
	 * 删除项目 (URL:"/projectinfo",Method：DELETE)
	 * @Title: deleteProject
	 * @author:liushufeng
	 * @date：2015年10月30日 上午12:59:53
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/projectinfo/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteProject(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		BDCS_QZGLXMB xm = basecommondao.get(BDCS_QZGLXMB.class, id);
		if (xm != null)
			basecommondao.deleteEntity(xm);
		basecommondao.flush();
		ResultMessage msg = new ResultMessage();
		msg.setMsg("删除成功");
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-删除项目信息", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return msg;
	}

	/**
	 * 提交项目 (URL:"/projectcommit/{id}",Method：POST)
	 * @Title: commitProject
	 * @author:liushufeng
	 * @date：2015年10月30日 上午10:43:29
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/projectcommit/{id}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage commitProject(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		BDCS_QZGLXMB xm = basecommondao.get(BDCS_QZGLXMB.class, id);
		if (xm != null) {
			if (SF.YES.equals(xm.getSFRK())) {
				msg.setMsg("已经提交过了");
				msg.setSuccess("false");
				return msg;
			}
			
			if(Global.getCurrentUserInfo()==null){
				msg.setSuccess("false");
				msg.setMsg("提交失败！请先登录！");
				return msg;
			}
			
			// 先删除证书
			String hql = "XMID='" + id + "'";
			basecommondao.deleteEntitysByHql(BDCS_RKQZB.class, hql);
           
			// 再根据起始编号和结束编号计算证书编号
			Long startnumber = xm.getQSQZBH();
			Long endnumber = xm.getJSQZBH();
			if (startnumber > endnumber) {
				msg.setMsg("起始编号不能大于结束编号");
				msg.setSuccess("false");
				return msg;
			}
			String sum="select COUNT(ID) SUM  from BDCK.BDCS_RKQZB WHERE QZBH BETWEEN "+startnumber +" AND "+endnumber +" AND QZLX="+xm.getQZLX();
			List<Map> lstsum=basecommondao.getDataListByFullSql(sum);
			if(lstsum !=null && lstsum.size()>0)
			{
				long count=StringHelper.getLong(lstsum.get(0).get("SUM"));
				if(count>0)
				{
					String maxQzbh="select MAX(QZBH) MQZBH FROM BDCK.BDCS_RKQZB WHERE QZLX ="+xm.getQZLX();
		            List<Map> lst=basecommondao.getDataListByFullSql(maxQzbh);
		            if(lst !=null && lst.size()>0)
		            {	            	
		            	long maxqzhb=StringHelper.getLong(lst.get(0).get("MQZBH"));
		            	if(maxqzhb>=endnumber)
		            	{
		            		msg.setMsg("该编号已入库，请起始编号和终止编号从"+(maxqzhb+1)+"开始");
		    				msg.setSuccess("false");
		    				return msg;
		            	}
		            	else if(maxqzhb>=startnumber)
		            	{
		            		msg.setMsg("该编号已入库，请起始编号从"+(maxqzhb+1)+"开始");
		    				msg.setSuccess("false");
		    				return msg;
		            	}
		            }
				}
			}
			for (Long i = startnumber; i <= endnumber; i++) {
				BDCS_RKQZB qz = new BDCS_RKQZB();
				qz.setXMID(id);
				qz.setQZBH(i);
				qz.setId((String) SuperHelper.GeneratePrimaryKey());
				qz.setCJSJ(new Date());
				qz.setSFSZ(SF.NO.Value);
				qz.setSFFZ(SF.NO.Value);
				qz.setSFZF(SF.NO.Value);
				qz.setQZLX(xm.getQZLX());
				qz.setQZZL(xm.getQZZL());
				qz.setZSZT(SF.YES.Value);
				qz.setSYQK(StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"进行权证入库；");
				basecommondao.save(qz);
			}
			xm.setRKRY(Global.getCurrentUserInfo().getLoginName());
			xm.setRKSJ(new Date());
			xm.setSFRK(SF.YES.Value);
			basecommondao.update(xm);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("提交成功");
		YwLogUtil.addYwLog("权证管理-提交项目项目", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}
	/**
	 * 提交后编辑项目 (URL:"/projectedit/{id}/{qzlx}",Method：GET)
	 * zmf 2016年7月27日15:24:02
	 * @return
	 */
	@RequestMapping(value = "/projectedit/{id}/{qzlx}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage editProject(@PathVariable("id") String id, @PathVariable("qzlx") String qzlx, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败！");
		Subject user = SecurityUtils.getSubject();
		User uu=Global.getCurrentUserInfo();
		String userName = uu.getLoginName();
		BDCS_QZGLXMB xm = basecommondao.get(BDCS_QZGLXMB.class, id);
		if(xm!=null){
			if(xm.getCJRY().equals(userName)){
				xm.setQZLX(qzlx);
				List<BDCS_RKQZB> QZ_list =basecommondao.getDataList(BDCS_RKQZB.class, " XMID='"+id+"'");
				for (BDCS_RKQZB bdcs_RKQZB : QZ_list) {
					bdcs_RKQZB.setQZLX(xm.getQZLX());
					basecommondao.update(bdcs_RKQZB);
				}
				basecommondao.flush();
				msg.setSuccess("true");
				msg.setMsg("更改成功");
				YwLogUtil.addYwLog("权证管理-更改项目", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			}else{
				msg.setMsg("请联系管理员！");
			}
		}
		
		return msg;
	}
	
	
	/************************************权证入库管理************************************/
	
	/************************************权证分发管理************************************/
	/**
	 * 权证分发项目管理页面 (URL:"/certdeptmanage",Method：GET)
	 * @Title: ShowCertDeptManage
	 * @author:taochunda
	 * @date：2017年12月12日 11:18:00
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "/certdeptmanage", method = RequestMethod.GET)
	public String ShowCertDeptManage(Model model) {
		return "/realestate/registration/certmanage/certdeptmanage";
	}
	
	/**
	 * 权证分发项目管理页面 (URL:"/certusemanage",Method：GET)
	 * @Title: ShowCertUseManage
	 * @author:yuxuebin
	 * @date：2017年03月21日 15:26:33
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "/certusemanage", method = RequestMethod.GET)
	public String ShowCertUseManage(Model model) {
		return "/realestate/registration/certmanage/certusemanage";
	}
	
	/**
	 * 权证分发项目信息页面 (URL:"/certuseinfo",Method：GET)
	 * @Title: ShowCertUseInfo
	 * @author:yuxuebin
	 * @date：2017年03月21日 17:10:25
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/certuseinfo", method = RequestMethod.GET)
	public String ShowCertUseInfo(Model model) {
		return "/realestate/registration/certmanage/certuseinfo";
	}
	
	/**
	 * 权证分发到科室信息页面 (URL:"/certdeptinfo",Method：GET)
	 * @Title: ShowCertDeptInfo
	 * @author:taoachunda
	 * @date：2017年12月12日 10:25:00
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/certdeptinfo", method = RequestMethod.GET)
	public String ShowCertDeptInfo(Model model) {
		return "/realestate/registration/certmanage/certdeptinfo";
	}
	
	/**
	 * 分页获取权证分发项目信息数据(URL:"/qzffxm",Method：POST)
	 * @Title: querQZFFXM
	 * @author:yuxuebin
	 * @date：2017年03月21日 16:43:50
	 * @return
	 */
	@RequestMapping(value = "/qzffxm", method = RequestMethod.GET)
	public @ResponseBody Message querQZFFXM(HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		if (request.getParameter("filter") != null) {
			try {
				filter = RequestHelper.getParam(request, "filter");
			} catch (Exception e) {
			}
		}
		
		User user = userService.getCurrentUserInfo();
		List<Map> map = basecommondao.getDataListByFullSql("SELECT U.DEPARTMENTID FROM SMWB_FRAMEWORK.T_USER U WHERE U.ID='"+user.getId()+"'");
		boolean flag = isContainRole(user.getId(),"科室分发员");//是否有管理员或科室分发员角色权限
		
		StringBuilder wherehql = new StringBuilder("1>0 ");
		//除管理员与科室分发员外，人员只能看见分发给自己的数据
		if (!flag) {
			wherehql.append("and LQRYID ='").append(user.getId()).append("' ");
		}
		if(!StringHelper.isEmpty(filter)){
			wherehql.append("AND XMMC LIKE '%"+filter+"%' ");
		}
		wherehql.append("ORDER BY CJSJ DESC");
		
		Page p=basecommondao.getPageDataByHql(BDCS_QZFFXMB.class, wherehql.toString(), page, rows);
		
		Message msg = new Message();
		msg.setTotal(p.getTotalCount());
		msg.setRows(p.getResult());
		msg.setSuccess("true");
		msg.setMsg(Boolean.toString(flag));
		YwLogUtil.addYwLog("权证分发管理(个人)", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;

	}
	
	/**
	 * 分页获取权证分发科室信息数据(URL:"/qzffks",Method：GET)
	 * @Title: querQZFFKS
	 * @author:taochunda
	 * @date：2017年12月12日 10:17:00
	 * @return
	 */
	@RequestMapping(value = "/qzffks", method = RequestMethod.GET)
	public @ResponseBody Message querQZFFKS(HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		if (request.getParameter("filter") != null) {
			try {
				filter = RequestHelper.getParam(request, "filter");
			} catch (Exception e) {
			}
		}
		User user = userService.getCurrentUserInfo();
		List<Map> map = basecommondao.getDataListByFullSql("SELECT U.DEPARTMENTID FROM SMWB_FRAMEWORK.T_USER U WHERE U.ID='"+user.getId()+"'");
		String departmentId = StringHelper.formatObject(map.get(0).get("DEPARTMENTID"));
		boolean flag = isContainRole(user.getId(),"采购分发员");//是否有管理员或采购分发员角色权限
		
		StringBuilder wherehql = new StringBuilder("1>0 ");
		//除管理员与采购分发员外，科室人员只能看见本科室的数据
		if (!flag) {
			wherehql.append("and LQKSID ='").append(departmentId).append("' ");
		}
		if(!StringHelper.isEmpty(filter)){
			wherehql.append("AND XMMC LIKE '%"+filter+"%' ");
		}
		wherehql.append("ORDER BY CJSJ DESC");
		
		Page p=basecommondao.getPageDataByHql(BDCS_QZFFKSB.class, wherehql.toString(), page, rows);
		
		Message msg = new Message();
		msg.setTotal(p.getTotalCount());
		msg.setRows(p.getResult());
		msg.setSuccess("true");
		msg.setMsg(Boolean.toString(flag));
		YwLogUtil.addYwLog("权证分发管理(科室)", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;

	}
	
	/**
	 * 新增或保存权证分发项目信息 (URL:"/qzffinfo",Method：POST)
	 * @Title: saveOrUpdateQZFFInfo
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:17:31
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/qzffinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveOrUpdateQZFFInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		ResultMessage msg=new ResultMessage();
		if(Global.getCurrentUserInfo()==null){
			msg.setSuccess("false");
			msg.setMsg("保存失败！请先登录！");
			return msg;
		}
		String id = request.getParameter("id");
		String xmid = request.getParameter("xmid");
		String xmmc = RequestHelper.getParam(request, "xmmc");
		String fffs = request.getParameter("fffs");
		String lqryid = request.getParameter("lqryid");
		String lqry = request.getParameter("lqry");
		String lqksid = request.getParameter("lqksid");
		String lqks = RequestHelper.getParam(request, "lqks");
		String ffyy = request.getParameter("ffyy");
		String qzlx = request.getParameter("qzlx");
		String qzzl = request.getParameter("qzzl");
		String qsqzbh = request.getParameter("qsqzbh");
		String jsqzbh = request.getParameter("jsqzbh");
		BDCS_QZFFXMB xm = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			xm = basecommondao.get(BDCS_QZFFXMB.class, id);
			bnew = false;
		}
		if (xm == null) {
			xm = new BDCS_QZFFXMB();
			xm.setId((String) SuperHelper.GeneratePrimaryKey());
			xm.setCJRY(Global.getCurrentUserInfo().getLoginName());// 创建人员
			xm.setCJSJ(new Date());// 创建时间
		}
		xm.setXMID(xmid);
		xm.setXMMC(xmmc);
		xm.setLQKS(lqks);
		xm.setLQKSID(lqksid);
		xm.setFFFS(fffs);
		if("0".equals(fffs)){
			xm.setLQRYID(lqryid);
			if(!StringHelper.isEmpty(lqryid)){
				User user=basecommondao.get(User.class, lqryid);
				if(user!=null){
					xm.setLQRY(user.getUserName());
				}else{
					xm.setLQRY("");
				}
			}
			
		}else{
			xm.setLQRY(lqry);
			xm.setLQRYID("");
		}
		xm.setFFYY(ffyy);
		xm.setQZLX(qzlx);
		xm.setQZZL(qzzl);
		xm.setQSQZBH(Long.parseLong(qsqzbh));// 起始编号
		xm.setJSQZBH(Long.parseLong(jsqzbh));// 终止编号
		if (bnew)
			basecommondao.save(xm);
		else
			basecommondao.update(xm);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		YwLogUtil.addYwLog("权证管理-新增权证分发项目", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}
	
	/**
	 * 新增或保存权证分发到科室信息 (URL:"/qzffksinfo",Method：POST)
	 * @Title: saveOrUpdateQZFFInfo
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:17:31
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/qzffksinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveOrUpdateQZFFKSInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		ResultMessage msg=new ResultMessage();
		if(Global.getCurrentUserInfo()==null){
			msg.setSuccess("false");
			msg.setMsg("保存失败！请先登录！");
			return msg;
		}
		String id = request.getParameter("id");
		String xmid = request.getParameter("xmid");
		String xmmc = RequestHelper.getParam(request, "xmmc");
		String fffs = request.getParameter("fffs");
		String lqksid = request.getParameter("lqksid");
		String lqks = request.getParameter("lqks");
		String ffyy = request.getParameter("ffyy");
		String qzlx = request.getParameter("qzlx");
		String qzzl = request.getParameter("qzzl");
		String qsqzbh = request.getParameter("qsqzbh");
		String jsqzbh = request.getParameter("jsqzbh");
		BDCS_QZFFKSB ks = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			ks = basecommondao.get(BDCS_QZFFKSB.class, id);
			bnew = false;
		}
		if (ks == null) {
			ks = new BDCS_QZFFKSB();
			ks.setId((String) SuperHelper.GeneratePrimaryKey());
			ks.setCJRY(Global.getCurrentUserInfo().getLoginName());// 创建人员
			ks.setCJSJ(new Date());// 创建时间
		}
		ks.setXMID(xmid);
		ks.setXMMC(xmmc);
		ks.setFFFS(fffs);
		if("0".equals(fffs)){
			ks.setLQKSID(lqksid);
			if(!StringHelper.isEmpty(lqksid)){
				StringBuilder builder=new StringBuilder();
				builder.append("SELECT * FROM SMWB_FRAMEWORK.T_DEPARTMENT DEPARTMENT ");
				builder.append("WHERE DEPARTMENT.ID='");
				builder.append(lqksid).append("'");
				
				List<Map> list_dept=basecommondao.getDataListByFullSql(builder.toString());
				if(list_dept != null && list_dept.size() > 0){
					ks.setLQKS(StringHelper.formatObject(list_dept.get(0).get("DEPARTMENTNAME")));
				}else{
					ks.setLQKS("");
				}
			}
			
		}else{
			ks.setLQKS(lqks);
			ks.setLQKSID("");
		}
		ks.setFFYY(ffyy);
		ks.setQZLX(qzlx);
		ks.setQZZL(qzzl);
		ks.setQSQZBH(Long.parseLong(qsqzbh));// 起始编号
		ks.setJSQZBH(Long.parseLong(jsqzbh));// 终止编号
		if (bnew)
			basecommondao.save(ks);
		else
			basecommondao.update(ks);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		YwLogUtil.addYwLog("权证管理-新增权证分发到科室", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}
	
	/**
	 * 获取权证分发项目信息 (URL:"/qzffinfo/{id}",Method：GET)
	 * @Title: getQZFFInfo
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:27:07
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzffinfo/{id}", method = RequestMethod.GET)
	public @ResponseBody Message getQZFFInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		BDCS_QZFFXMB xm = basecommondao.get(BDCS_QZFFXMB.class, id);
		Message msg = new Message();
		List<BDCS_QZFFXMB> list = new ArrayList<BDCS_QZFFXMB>();
		list.add(xm);
		msg.setRows(list);
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-获取权证分发项目信息", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}
	
	/**
	 * 获取权证分发项目信息 (科室)(URL:"/qzffinfo/{id}",Method：GET)
	 * @Title: getQZFFKSInfo
	 * @author:taochunda
	 * @date：2017年12月12日 14:21:00
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzffksinfo/{id}", method = RequestMethod.GET)
	public @ResponseBody Message getQZFFKSInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		BDCS_QZFFKSB xm = basecommondao.get(BDCS_QZFFKSB.class, id);
		Message msg = new Message();
		List<BDCS_QZFFKSB> list = new ArrayList<BDCS_QZFFKSB>();
		list.add(xm);
		msg.setRows(list);
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-获取权证分发项目信息(科室)", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}
	
	/**
	 * 获取用户列表 (URL:"/userlist/{type}",Method：GET)
	 * @Title: getUserList
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:27:07
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/userlist/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getUserList(HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT RY.ID,RY.USERNAME,DEPARTMENT.DEPARTMENTNAME ");
		builder.append("FROM SMWB_FRAMEWORK.T_USER RY ");
		builder.append("LEFT JOIN SMWB_FRAMEWORK.T_DEPARTMENT DEPARTMENT ");
		builder.append("ON DEPARTMENT.ID=RY.DEPARTMENTID ORDER BY DEPARTMENT.DEPARTMENTNAME,RY.USERNAME");
		List<Map> list_user=basecommondao.getDataListByFullSql(builder.toString());
		if(list_user!=null&&list_user.size()>0){
			for(Map m:list_user){
				String id=StringHelper.formatObject(m.get("ID"));
				String username=StringHelper.formatObject(m.get("USERNAME"));
				String departmentname=StringHelper.formatObject(m.get("DEPARTMENTNAME"));
				if(StringHelper.isEmpty(departmentname)){
					departmentname="无部门";
				}
				String text=departmentname+"-"+username;
				HashMap<String,String> userinfo=new HashMap<String, String>();
				userinfo.put("id", id);
				userinfo.put("text", text);
				list.add(userinfo);
			}
		}
		return list;
	}
	
	/**
	 * 获取部门下用户列表 (URL:"/userlist/{deptid}",Method：GET)
	 * @Title: getDeptUserList
	 * @author:taochunda
	 * @date：2017年12月13日 15:42:00
	 * @param deptid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/userlist/{deptid}", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getDeptUserList(@PathVariable("deptid") String deptid, HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT RY.ID,RY.USERNAME,DEPARTMENT.DEPARTMENTNAME ");
		builder.append("FROM SMWB_FRAMEWORK.T_USER RY ");
		builder.append("LEFT JOIN SMWB_FRAMEWORK.T_DEPARTMENT DEPARTMENT ");
		builder.append("ON DEPARTMENT.ID=RY.DEPARTMENTID ");
		builder.append("WHERE DEPARTMENT.ID='");
		builder.append(deptid).append("' ");
		builder.append("ORDER BY DEPARTMENT.DEPARTMENTNAME,RY.USERNAME");
		List<Map> list_user=basecommondao.getDataListByFullSql(builder.toString());
		if(list_user!=null&&list_user.size()>0){
			for(Map m:list_user){
				String id=StringHelper.formatObject(m.get("ID"));
				String username=StringHelper.formatObject(m.get("USERNAME"));
				String departmentname=StringHelper.formatObject(m.get("DEPARTMENTNAME"));
				if(StringHelper.isEmpty(departmentname)){
					departmentname="无部门";
				}
				String text=departmentname+"-"+username;
				HashMap<String,String> userinfo=new HashMap<String, String>();
				userinfo.put("id", id);
				userinfo.put("text", text);
				list.add(userinfo);
			}
		}
		return list;
	}
	
	/**
	 * 获取科室列表 (URL:"/deptlist/",Method：GET)
	 * @Title: getDeptList
	 * @author:taochunda
	 * @date：2017年12月12日 10:33:00
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deptlist/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getDeptList(HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT DEPARTMENT.ID,DEPARTMENT.DEPARTMENTNAME ");
		builder.append("FROM SMWB_FRAMEWORK.T_DEPARTMENT DEPARTMENT ");
		builder.append("ORDER BY DEPARTMENT.DEPARTMENTNAME");
		List<Map> list_dept=basecommondao.getDataListByFullSql(builder.toString());
		if(list_dept!=null&&list_dept.size()>0){
			for(Map m:list_dept){
				String id=StringHelper.formatObject(m.get("ID"));
				String departmentname=StringHelper.formatObject(m.get("DEPARTMENTNAME"));
				if(StringHelper.isEmpty(departmentname)){
					departmentname="无部门";
				}
				String text=departmentname;
				HashMap<String,String> deptinfo=new HashMap<String, String>();
				deptinfo.put("id", id);
				deptinfo.put("text", text);
				list.add(deptinfo);
			}
		}
		return list;
	}
	
	/**
	 * 获取总库存名称列表 (URL:"/xmmclist/",Method：GET)
	 * @Title: getDeptList
	 * @author:taochunda
	 * @date：2017年12月12日 10:33:00
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/xmmclist/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getXMMCList(HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT Q.ID,Q.XMMC FROM BDCK.BDCS_QZGLXMB Q ");
		List<Map> list_dept=basecommondao.getDataListByFullSql(builder.toString());
		if(list_dept!=null&&list_dept.size()>0){
			for(Map m:list_dept){
				String id=StringHelper.formatObject(m.get("ID"));
				String xmmc=StringHelper.formatObject(m.get("XMMC"));
				if(StringHelper.isEmpty(xmmc)){
					xmmc="无库存";
				}
				String text=xmmc;
				HashMap<String,String> xmmcinfo=new HashMap<String, String>();
				xmmcinfo.put("id", id);
				xmmcinfo.put("text", text);
				list.add(xmmcinfo);
			}
		}
		return list;
	}
	
	/**
	 * 获取科室库存名称列表 (URL:"/ksxmmclist/{lqksid}",Method：GET)
	 * @Title: getDeptList
	 * @author:taochunda
	 * @date：2017年12月12日 10:33:00
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ksxmmclist/{lqksid}", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getKSXMMCList(@PathVariable("lqksid") String lqksid, HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT KS.XMID,KS.XMMC FROM BDCK.BDCS_QZFFKSB KS WHERE KS.SFRK='1' ");
		builder.append("AND KS.LQKSID='").append(lqksid).append("' ");
		builder.append("GROUP BY KS.XMID,KS.XMMC");
		List<Map> list_dept=basecommondao.getDataListByFullSql(builder.toString());
		if(list_dept!=null&&list_dept.size()>0){
			for(Map m:list_dept){
				String xmid=StringHelper.formatObject(m.get("XMID"));
				String xmmc=StringHelper.formatObject(m.get("XMMC"));
				String text=xmmc;
				HashMap<String,String> xmmcinfo=new HashMap<String, String>();
				xmmcinfo.put("id", xmid);
				xmmcinfo.put("text", text);
				list.add(xmmcinfo);
			}
		} else {
			HashMap<String,String> xmmcinfo=new HashMap<String, String>();
			xmmcinfo.put("id", "");
			xmmcinfo.put("text", "无库存");
			list.add(xmmcinfo);
		}
		
		return list;
	}
	
	/**
	 * 获取起始权证编号 (URL:"/getqzbh/{xmid}",Method：GET)
	 * @Title: getQSQZBH
	 * @author:taochunda
	 * @date：2017年12月12日 16:39:00
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getqzbh/{xmid}/{lqksid}/{type}", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getQSQZBH(@PathVariable("xmid") String xmid,@PathVariable("lqksid") String lqksid,
			@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		StringBuilder builder=new StringBuilder();
		//取总库存剩余编号最大最小值为默认起始编号
		builder.append("SELECT MIN(R.QZBH) QSQZBH, MAX(R.QZBH) JSQZBH FROM BDCK.BDCS_RKQZB R WHERE 1=1  ");
		builder.append("AND R.XMID='").append(xmid+"' ");
		builder.append("AND R.SFSZ='0' AND R.SFFZ='0' ");
		if ("ks".equals(type)) {
			builder.append("AND (R.LQKSID IS NULL OR R.LQKSID='') ");
		}
		if ("ry".equals(type)) {
			builder.append("AND (R.LQRYID IS NULL OR R.LQRYID='') ");
			builder.append("AND R.LQKSID='").append(lqksid).append("' ");
		}
		List<Map> list_qzbh=basecommondao.getDataListByFullSql(builder.toString());
		if(list_qzbh!=null&&list_qzbh.size()>0){
			String qsqzbh=StringHelper.formatObject(list_qzbh.get(0).get("QSQZBH"));
			String jsqzbh=StringHelper.formatObject(list_qzbh.get(0).get("JSQZBH"));
			HashMap<String,String> map=new HashMap<String, String>();
			map.put("qsqzbh", qsqzbh);
			map.put("jsqzbh", jsqzbh);
			list.add(map);
		}
		return list;
	}

	/**
	 * 删除权证分发项目 (URL:"/qzffinfo",Method：DELETE)
	 * @Title: deleteQZFF
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:29:53
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzffinfo/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteQZFF(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if(Global.getCurrentUserInfo()==null){
			msg.setMsg("删除失败！请先登录！");
			return msg;
		}
		BDCS_QZFFXMB xm = basecommondao.get(BDCS_QZFFXMB.class, id);
		if (xm != null)
			basecommondao.deleteEntity(xm);
		basecommondao.flush();
		
		msg.setMsg("删除成功");
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-删除权证分发项目信息(个人)", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return msg;
	}
	
	/**
	 * 删除权证分发项目(科室) (URL:"/qzffksinfo",Method：DELETE)
	 * @Title: deleteQZFFKS
	 * @author:taochunda
	 * @date：2017年12月12日 14:26:00
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzffksinfo/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteQZFFKS(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if(Global.getCurrentUserInfo()==null){
			msg.setMsg("删除失败！请先登录！");
			return msg;
		}
		BDCS_QZFFKSB xm = basecommondao.get(BDCS_QZFFKSB.class, id);
		if (xm != null)
			basecommondao.deleteEntity(xm);
		basecommondao.flush();
		
		msg.setMsg("删除成功");
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-删除权证分发项目信息(科室)", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return msg;
	}

	/**
	 * 提交权证分发项目 (URL:"/qzffcommit/{id}",Method：POST)
	 * @Title: commitQZFF
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:30:29
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzffcommit/{id}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage commitQZFF(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		BDCS_QZFFXMB xm = basecommondao.get(BDCS_QZFFXMB.class, id);
		if (xm != null) {
			if (SF.YES.equals(xm.getSFRK())) {
				msg.setMsg("已经提交过了");
				msg.setSuccess("false");
				return msg;
			}
			
			if(Global.getCurrentUserInfo()==null){
				msg.setSuccess("false");
				msg.setMsg("提交失败！请先登录！");
				return msg;
			}
			
			// 再根据起始编号和结束编号计算证书编号
			Long startnumber = xm.getQSQZBH();
			Long endnumber = xm.getJSQZBH();
			if (startnumber > endnumber) {
				msg.setMsg("起始编号不能大于结束编号");
				msg.setSuccess("false");
				return msg;
			}
			List<Long> list_qzbh=new ArrayList<Long>();
			for(long number=startnumber;number<=endnumber;number++){
				list_qzbh.add(number);
			}
			List<Long> list_sxqz=new ArrayList<Long>();
			List<Long> list_szqz=new ArrayList<Long>();
			List<Long> list_fzqz=new ArrayList<Long>();
			List<Long> list_syqz=new ArrayList<Long>();
			List<Long> list_sfff=new ArrayList<Long>();//是否已分发到个人
			
			StringBuilder builder=new StringBuilder();
			builder.append("QZBH>="+startnumber);
			builder.append(" AND QZBH<="+endnumber);
			builder.append(" AND QZLX='"+xm.getQZLX()+"'");
			builder.append(" AND QZZL='"+xm.getQZZL()+"'");
			List<BDCS_RKQZB> list_rkqz=basecommondao.getDataList(BDCS_RKQZB.class,builder.toString());
			if(list_rkqz!=null&&list_rkqz.size()>0){
				for(BDCS_RKQZB rkqz:list_rkqz){
					if("0".equals(rkqz.getZSZT())){
						list_sxqz.add(rkqz.getQZBH());
					}
					if("1".equals(rkqz.getSFSZ())){
						list_szqz.add(rkqz.getQZBH());
					}
					if("1".equals(rkqz.getSFFZ())){
						list_fzqz.add(rkqz.getQZBH());
					}
					if("1".equals(rkqz.getSFSZ())||"1".equals(rkqz.getSFFZ())){
						list_syqz.add(rkqz.getQZBH());
					}
					if(!StringHelper.isEmpty(rkqz.getLQRYID())){
						list_sfff.add(rkqz.getQZBH());
					}
					list_qzbh.remove(rkqz.getQZBH());
				}
			}
			if(list_qzbh!=null&&list_qzbh.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_qzbh,"、")+"未入库！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_sxqz!=null&&list_sxqz.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_sxqz,"、")+"已失效！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_syqz!=null&&list_syqz.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_syqz,"、")+"已使用！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_sfff!=null&&list_sfff.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_sfff,"、")+"已分发！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_rkqz!=null&&list_rkqz.size()>0){
				for(BDCS_RKQZB rkqz:list_rkqz){
					if("1".equals(xm.getFFFS())){
						rkqz.setZSZT(SF.NO.Value);
						rkqz.setLQRY(xm.getLQRY());
					}else{
						rkqz.setLQRYID(xm.getLQRYID());
						rkqz.setLQRY(xm.getLQRY());
					}
					rkqz.setSYQK(rkqz.getSYQK()+"<br/>"+StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"进行权证分发给"+xm.getLQRY()+"；");
					basecommondao.update(rkqz);
				}
			}
			
			xm.setRKRY(Global.getCurrentUserInfo().getLoginName());
			xm.setRKSJ(new Date());
			xm.setSFRK(SF.YES.Value);
			basecommondao.update(xm);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("提交成功");
		YwLogUtil.addYwLog("权证管理-提交项目项目", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}
	
	/**
	 * 提交权证分发项目(科室) (URL:"/qzffcommit/{id}",Method：POST)
	 * @Title: commitQZFFKS
	 * @author:taochunda
	 * @date：2017年12月12日 14:39:00
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzffkscommit/{id}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage commitQZFFKS(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		BDCS_QZFFKSB ks = basecommondao.get(BDCS_QZFFKSB.class, id);
		if (ks != null) {
			if (SF.YES.equals(ks.getSFRK())) {
				msg.setMsg("已经提交过了");
				msg.setSuccess("false");
				return msg;
			}
			
			if(Global.getCurrentUserInfo()==null){
				msg.setSuccess("false");
				msg.setMsg("提交失败！请先登录！");
				return msg;
			}
			
			// 再根据起始编号和结束编号计算证书编号
			Long startnumber = ks.getQSQZBH();
			Long endnumber = ks.getJSQZBH();
			if (startnumber > endnumber) {
				msg.setMsg("起始编号不能大于结束编号");
				msg.setSuccess("false");
				return msg;
			}
			List<Long> list_qzbh=new ArrayList<Long>();
			for(long number=startnumber;number<=endnumber;number++){
				list_qzbh.add(number);
			}
			List<Long> list_sxqz=new ArrayList<Long>();
			List<Long> list_szqz=new ArrayList<Long>();
			List<Long> list_fzqz=new ArrayList<Long>();
			List<Long> list_syqz=new ArrayList<Long>();
			List<Long> list_sfff=new ArrayList<Long>();//是否已分发到科室
			
			StringBuilder builder=new StringBuilder();
			builder.append("QZBH>="+startnumber);
			builder.append(" AND QZBH<="+endnumber);
			builder.append(" AND QZLX='"+ks.getQZLX()+"'");
			builder.append(" AND QZZL='"+ks.getQZZL()+"'");
			List<BDCS_RKQZB> list_rkqz=basecommondao.getDataList(BDCS_RKQZB.class,builder.toString());
			if(list_rkqz!=null&&list_rkqz.size()>0){
				for(BDCS_RKQZB rkqz:list_rkqz){
					if("0".equals(rkqz.getZSZT())){
						list_sxqz.add(rkqz.getQZBH());
					}
					if("1".equals(rkqz.getSFSZ())){
						list_szqz.add(rkqz.getQZBH());
					}
					if("1".equals(rkqz.getSFFZ())){
						list_fzqz.add(rkqz.getQZBH());
					}
					if("1".equals(rkqz.getSFSZ())||"1".equals(rkqz.getSFFZ())){
						list_syqz.add(rkqz.getQZBH());
					}
					if(!StringHelper.isEmpty(rkqz.getLQKSID())){
						list_sfff.add(rkqz.getQZBH());
					}
					list_qzbh.remove(rkqz.getQZBH());
				}
			}
			if(list_qzbh!=null&&list_qzbh.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_qzbh,"、")+"未入库！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_sxqz!=null&&list_sxqz.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_sxqz,"、")+"已失效！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_syqz!=null&&list_syqz.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_syqz,"、")+"已使用！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_sfff!=null&&list_sfff.size()>0){
				msg.setMsg("权证编号："+StringHelper.formatListEx(list_sfff,"、")+"已分发！");
				msg.setSuccess("false");
				return msg;
			}
			if(list_rkqz!=null&&list_rkqz.size()>0){
				for(BDCS_RKQZB rkqz:list_rkqz){
					if("1".equals(ks.getFFFS())){
						rkqz.setZSZT(SF.NO.Value);
						rkqz.setLQKS(ks.getLQKS());
					}else{
						rkqz.setLQKSID(ks.getLQKSID());
						rkqz.setLQKS(ks.getLQKS());
					}
					rkqz.setSYQK(rkqz.getSYQK()+"<br/>"+StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"进行权证分发给"+ks.getLQKS()+"；");
					basecommondao.update(rkqz);
				}
			}
			
			ks.setRKRY(Global.getCurrentUserInfo().getLoginName());
			ks.setRKSJ(new Date());
			ks.setSFRK(SF.YES.Value);
			basecommondao.update(ks);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("提交成功");
		YwLogUtil.addYwLog("权证管理-提交项目项目", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}
	
	/************************************权证分发管理************************************/
	
	
	/**
	 * 权证管理页面地址（URL:"/certinfomanage",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/certmanage", method = RequestMethod.GET)
	public String ShowCertManagerMain(Model model) {
		return "/realestate/registration/certmanage/certmanage";
	}
	
	/**
	 * 查询权证列表（URL:"/certlist/",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/certinfolist/", method = RequestMethod.GET)
	public @ResponseBody Message QueryCertInfoList(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		String qzbh = "";
		if (request.getParameter("qzbh") != null) {
			qzbh = request.getParameter("qzbh");
		}
		
		String qzlx = "";
		if (request.getParameter("qzlx") != null) {
			qzlx = request.getParameter("qzlx");
		}
		String bdcqzh = "";
		if (request.getParameter("bdcqzh") != null) {
			bdcqzh = RequestHelper.getParam(request,"bdcqzh");
		}
		String qlrmc = "";
		if (request.getParameter("qlrmc") != null) {
			qlrmc = RequestHelper.getParam(request, "qlrmc");
		}
		String qlrzjh = "";
		if (request.getParameter("qlrzjh") != null) {
			qlrzjh =RequestHelper.getParam(request,"qlrzjh");
		}
		String zszt = "";
		if (request.getParameter("zszt") != null) {
			zszt = request.getParameter("zszt");
		}
		String zl = "";
		if (request.getParameter("zl") != null) {
			zl =RequestHelper.getParam(request,"zl");
		}
		String sfsz = "";
		if (request.getParameter("sfsz") != null) {
			sfsz =RequestHelper.getParam(request,"sfsz");
		}
		String sfzf = "";
		if (request.getParameter("sfzf") != null) {
			sfzf =RequestHelper.getParam(request,"sfzf");
		}
		String szry = "";
		if (request.getParameter("szry") != null) {
			szry = RequestHelper.getParam(request, "szry");
		}
		String start = "";
		if (request.getParameter("start") != null) {
			start = RequestHelper.getParam(request, "start");
		}
		String end = "";
		if (request.getParameter("end") != null) {
			end = RequestHelper.getParam(request, "end");
		}
		String viewRange ="";
		if (request.getParameter("viewRange") != null) {
			viewRange = request.getParameter("viewRange");
		}
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(qzbh)) {
			hqlBuilder.append(" and TO_CHAR(QZBH) LIKE '%").append(qzbh).append("%'");
		}
		if (!StringHelper.isEmpty(qzlx) && !("2").equals(qzlx)) {
			hqlBuilder.append(" and QZLX='").append(qzlx).append("'");
		}
		if (!StringHelper.isEmpty(bdcqzh)) {
			hqlBuilder.append(" and BDCQZH='").append(bdcqzh).append("'");
		}
		if (!StringHelper.isEmpty(qlrmc)) {
			hqlBuilder.append(" and QLRMC LIKE '%").append(qlrmc).append("%'");
		}
		if (!StringHelper.isEmpty(qlrzjh)) {
			hqlBuilder.append(" and QLRZJH LIKE '%").append(qlrzjh).append("%'");
		}
		if (!StringHelper.isEmpty(zszt) && !("2").equals(zszt)) {
			hqlBuilder.append(" and ZSZT='").append(zszt).append("'");
		}
		if (!StringHelper.isEmpty(zl)) {
			hqlBuilder.append(" and ZL LIKE '%").append(zl).append("%'");
		}
		if (!StringHelper.isEmpty(sfsz) && !("2").equals(sfsz)) {
			
			hqlBuilder.append(" and SFSZ='").append(sfsz).append("'");
		}
		if (!StringHelper.isEmpty(sfzf) && !("2").equals(sfzf)) {
			hqlBuilder.append(" and SFZF='").append(sfzf).append("'");
		}
		if (!StringHelper.isEmpty(szry)) {
			hqlBuilder.append(" and SZR LIKE '%").append(szry).append("%'");
		}
		if (!StringHelper.isEmpty(start)) {
			hqlBuilder.append(" and SZSJ > TO_DATE('"+start+"','yyyy-mm-dd HH24:mi:ss')");
		}
		if (!StringHelper.isEmpty(end)) {
			hqlBuilder.append(" and SZSJ < TO_DATE('"+end+"','yyyy-mm-dd HH24:mi:ss')");
		}
		
		if (StringHelper.isEmpty(viewRange) || !"2".equals(viewRange)) {//除了权利为2的，其他值只可以查看当前登录用户所领取的qzbh
			User user=Global.getCurrentUserInfo();			
			hqlBuilder.append(" and LQRY='").append(user.getUserName()).append("'")
			.append(" and LQRYID='").append(user.getId()).append("'");
		}
		hqlBuilder.append(" ORDER BY QZBH");
		Page p=basecommondao.getPageDataByHql(BDCS_RKQZB.class, hqlBuilder.toString(), page, rows);
		
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		YwLogUtil.addYwLog("权证管理-查询权证列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}
	
	/**
	 * 权证管理页面地址（URL:"/certinfomanage",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/certinfomanage", method = RequestMethod.GET)
	public String ShowCertInfoManagerMain(Model model) {
		return "/realestate/registration/certmanage/certinfomanager";
	}
	
	/**
	 * 查询权证列表（URL:"/certlist/",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/certlist/", method = RequestMethod.GET)
	public @ResponseBody Message QueryCertList(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		List<Map> listresult = null;
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		String qzbh = "";
		if (request.getParameter("qzbh") != null) {
			qzbh = request.getParameter("qzbh");
		}
		
		String qzlx = "";
		if (request.getParameter("qzlx") != null) {
			qzlx = request.getParameter("qzlx");
		}
		
		String sfzf = "";
		String sfsz = "";
		String bdcqzh = "";
		if (request.getParameter("bdcqzh") != null) {
			bdcqzh = request.getParameter("bdcqzh");
		}
		String qlrmc = "";
		if (request.getParameter("qlrmc") != null) {
			qlrmc = request.getParameter("qlrmc");
		}
		String qlrzjhm = "";
		if (request.getParameter("qlrzjhm") != null) {
			qlrzjhm = request.getParameter("qlrzjhm");
		}
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
		if(xzqdm != null && xzqdm.equals("420900")||"420902".equals(xzqdm)){
			if (request.getParameter("sfzf") != null) {
				sfzf = request.getParameter("sfzf");
			}
			if (request.getParameter("sfsz") != null) {
				sfsz = request.getParameter("sfsz");
			}
		}
		//获取的字段名
		String fieldsname = "RKQZB.*";		
		String unitentityName = "BDCK.BDCS_RKQZB RKQZB";
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(qzbh)) {
			if(xzqdm != null && xzqdm.equals("420900")||"420902".equals(xzqdm)){
				hqlBuilder.append(" and QZBH LIKE '%").append(qzbh).append("%'");
			}
			hqlBuilder.append(" and TO_CHAR(RKQZB.QZBH) LIKE '%").append(qzbh).append("%'");
		}
		if (!StringHelper.isEmpty(qzlx)&&!("2").equals(qzlx)) {
			if(xzqdm != null && xzqdm.equals("420900")||"420902".equals(xzqdm)){
				hqlBuilder.append(" and QZLX='").append(qzlx).append("'");
			}
			hqlBuilder.append(" and QZLX='").append(qzlx).append("'");
		}
		if(xzqdm != null && xzqdm.equals("420900")||"420902".equals(xzqdm)){
			
		Page p = basecommondao.getPageDataByHql(BDCS_RKQZB.class, hqlBuilder.toString(), page, rows);
		List<?> xx = p.getResult();		
		List<Map> finalList = new ArrayList<Map>();
		for (int i = 0; i < xx.size(); i++) {
			BDCS_RKQZB rkqzb = (BDCS_RKQZB)xx.get(i);
			Long rkqzbh =rkqzb.getQZBH();
			String sqlforywlsh = "SELECT XMXX.XMBH, XMXX.YWLSH FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_ZS_XZ ZS ON XMXX.XMBH=ZS.XMBH WHERE ZS.ZSBH=''{0}''";
			sqlforywlsh = MessageFormat.format(sqlforywlsh, rkqzbh.toString());				
			List<Map> listywlsh = basecommondao.getDataListByFullSql(sqlforywlsh);
			Map tomap = StringHelper.beanToMap(rkqzb);	
			if (!StringHelper.isEmpty(sfzf)&&!("2").equals(sfzf)) {
				hqlBuilder.append(" and RKQZB.SFZF='").append(sfzf).append("'");
			}
			
			if (!StringHelper.isEmpty(sfsz)&&!("2").equals(sfsz)) {
				hqlBuilder.append(" and RKQZB.SFSZ='").append(sfsz).append("'");
			}
		}
		}
		if (!StringHelper.isEmpty(bdcqzh) || !StringHelper.isEmpty(qlrmc) || !StringHelper.isEmpty(qlrzjhm)) {
			 fieldsname = fieldsname + ", ZS.XMBH";	
			unitentityName = unitentityName + " LEFT JOIN BDCK.BDCS_ZS_XZ ZS ON TO_CHAR(RKQZB.QZBH) = ZS.ZSBH LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON ZS.BDCQZH = QLR.BDCQZH";
			if (!StringHelper.isEmpty(bdcqzh)) {
				hqlBuilder.append(" and QLR.BDCQZH LIKE '%").append(bdcqzh).append("%'");
			}
			if (!StringHelper.isEmpty(qlrmc)) {
				hqlBuilder.append(" and QLR.QLRMC LIKE '%").append(qlrmc).append("%'");
			}
			if (!StringHelper.isEmpty(qlrzjhm)) {
				hqlBuilder.append(" and QLR.ZJH LIKE '%").append(qlrzjhm).append("%'");
			}
		}
		String fullSql = "SELECT " + fieldsname + " FROM " + unitentityName + " WHERE " + hqlBuilder + " ORDER BY RKQZB.QZBH";
		String fromSql = "FROM " + unitentityName + " WHERE " + hqlBuilder;
		
		Long count = basecommondao.getCountByFullSql(fromSql);
		listresult = basecommondao.getPageDataByFullSql(fullSql, page, rows);
	
		for (Map map : listresult) {
			String cjsj = StringHelper.formatObject(map.get("CJSJ")).substring(0,10);			
			map.put("CJSJ", cjsj);
			if(map.get("SZSJ")!=null){
				String szsj = StringHelper.formatObject(map.get("SZSJ")).substring(0,10);			
				map.put("SZSJ", szsj);
			}else{
				map.put("SZSJ", "");
			}
			
			String sqlforywlsh = "SELECT XMXX.XMBH, XMXX.YWLSH FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON XMXX.XMBH=ZS.XMBH WHERE ZS.ZSBH=''{0}''";
			sqlforywlsh = MessageFormat.format(sqlforywlsh, map.get("QZBH").toString());				
			List<Map> listywlsh = basecommondao.getDataListByFullSql(sqlforywlsh);			
			if (!listywlsh.isEmpty()) {				
				map.put("SLBH",StringHelper.formatObject(listywlsh.get(0).get("YWLSH")));	
				String xmbh = StringHelper.formatObject(listywlsh.get(0).get("XMBH"));
				String sqlforlzr = "SELECT DJFZ.LZRXM,DJFZ.LZRZJHM,DJFZ.LZRZJLB,DJFZ.HFZSH FROM BDCK.BDCS_DJFZ DJFZ WHERE DJFZ.XMBH=''{0}''";
				sqlforlzr = MessageFormat.format(sqlforlzr,xmbh);				
				List<Map> listlzr = basecommondao.getDataListByFullSql(sqlforlzr);		
				if (!listlzr.isEmpty()) {
					map.put("LZR",StringHelper.formatObject(listlzr.get(0).get("LZRXM")));	
					map.put("LZRZJLB",StringHelper.formatObject(listlzr.get(0).get("LZRZJLB")));	
					map.put("LZRZJHM",StringHelper.formatObject(listlzr.get(0).get("LZRZJHM")));
					map.put("HFZSH",StringHelper.formatObject(listlzr.get(0).get("HFZSH")));
				}					
			}
		//	finalList.add(tomap);
		}
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(count);
		m.setRows(listresult);
		m.setMsg("成功！");
		YwLogUtil.addYwLog("权证管理-查询权证列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}
	
	
	/**
	 * 更新权证信息（URL:"/updatecertinfo/",Method：POST）
	 * 
	 * @作者 俞学斌
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updatecertinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateCertInfo(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		ResultMessage ms=new ResultMessage();
		ms.setMsg("保存失败！");
		ms.setSuccess("false");
		String id = "";
		if (request.getParameter("ID") != null) {
			id = request.getParameter("ID");
		}
		String sfzf = "";
		if (request.getParameter("sfzf") != null) {
			sfzf = request.getParameter("sfzf");
		}
		String sfsz = "";
		String qzlx = "";
		
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
		if(xzqdm != null && xzqdm.equals("420900")||"420902".equals(xzqdm)){
			if (request.getParameter("sfszs") != null) {
				sfsz = request.getParameter("sfszs");
			}
			if (request.getParameter("qzlx") != null) {
				qzlx = request.getParameter("qzlx");
			}
		}
		Date zfsj = null;
		String zfr = "";
		String zfyy = "";
		if(SF.YES.Value.equals(sfzf)){
			zfsj=new Date();
			User user=Global.getCurrentUserInfo();
			if(user!=null){
				zfr=Global.getCurrentUserInfo().getLoginName();
			}else{
				ms.setMsg("请先登录！");
				return ms;
			}
			if (request.getParameter("zfyy") != null) {
				zfyy = request.getParameter("zfyy");
			}
		}
		if(!StringHelper.isEmpty(id)){
			BDCS_RKQZB rkqzb=basecommondao.get(BDCS_RKQZB.class, id);
			if(rkqzb!=null){
				rkqzb.setSFZF(sfzf);
				rkqzb.setZFSJ(zfsj);
				rkqzb.setZFR(zfr);
				rkqzb.setZFYY(zfyy);
				if(xzqdm != null && xzqdm.equals("420900")||"420902".equals(xzqdm)){
					rkqzb.setSFSZ(sfsz);
					if(SF.NO.Value.equals(sfsz)){
						List<BDCS_ZS_GZ> certs_gz=basecommondao.getDataList(BDCS_ZS_GZ.class, "ZSBH='"+rkqzb.getQZBH()+"'"+"and bdcqzh like '%"+("1".equals(qzlx)?"不动产权第":"不动产证明第")+"%'");
						if(certs_gz!=null&&certs_gz.size()>0){
							ms.setMsg("此证号已缮证，不能修改！");
							return ms;
						}
					}
				}
				if(SF.YES.Value.equals(sfzf)){
					List<BDCS_ZS_GZ> certs_gz=basecommondao.getDataList(BDCS_ZS_GZ.class, "ZSBH='"+rkqzb.getQZBH()+"'");
					List<BDCS_ZS_XZ> certs_xz=basecommondao.getDataList(BDCS_ZS_XZ.class, "ZSBH='"+rkqzb.getQZBH()+"'");
					List<BDCS_ZS_LS> certs_ls=basecommondao.getDataList(BDCS_ZS_LS.class, "ZSBH='"+rkqzb.getQZBH()+"'");
					if(certs_gz!=null&&certs_gz.size()>0){
						ms.setMsg("此证号正在被使用，不能作废！");
						return ms;
					}else if(certs_xz!=null&&certs_xz.size()>0){
						ms.setMsg("此证号正在被使用，不能作废！");
						return ms;
					}else if(certs_ls!=null&&certs_ls.size()>0){
						ms.setMsg("此证号正在被使用，不能作废！");
						return ms;
					}else{
						rkqzb.setZSZT("已作废");
					}
				}
				
				basecommondao.update(rkqzb);
				basecommondao.flush();
				ms.setMsg("保存成功！");
				ms.setSuccess("true");
				YwLogUtil.addYwLog("权证管理-更新权证信息", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			}
		}
		return ms;
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@RequestMapping(value="/updatecert/",method=RequestMethod.GET)
	public @ResponseBody String updatecert(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String sql=" SFSZ='0'";
		List<BDCS_RKQZB> rks=basecommondao.getDataList(BDCS_RKQZB.class, sql);
		for(BDCS_RKQZB rk:rks){
			String qzbh=rk.getQZBH().toString();
			String qzlx=rk.getQZLX().toString();
			if("1".equals(qzlx)){//不动产权证明
				qzlx="不动产权第";
			}else if("0".equals(qzlx)){
				qzlx="不动产证明第";//不动产证明第
			}
			String ZS="select zsid from  bdck.bdcs_zs_gz zs where zs.zsbh='"+qzbh+"'  and  zs.bdcqzh like'%"+qzlx+"%'";
			long total=basecommondao.getCountByFullSql("from("+ZS+")");
			if(total>0){
				rk.setSFSZ("1");
				basecommondao.update(rk);
			}
		}
		
		return sql;
	}
	
	
	

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@RequestMapping(value="/certinfodownload/",method=RequestMethod.GET)
	public @ResponseBody String certInfoDownload(HttpServletRequest request,HttpServletResponse response) throws Exception{

		Message m = userService.certinfodownload(request, response);
		List<Map> djdys = null;
		if(m!=null&&m.getRows()!=null){
			djdys=(List<Map>)m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\cert.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\cert.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/cert.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);
			Map<String,Integer> col = new HashMap<String, Integer>();
		    col.put("权证编号", 0);
		    col.put("权证类型", 1);
		    col.put("创建时间", 2);
		    col.put("是否缮证", 3);
		    col.put("缮证人", 4);
		    col.put("缮证时间", 5);
		    col.put("是否作废", 6);
		    col.put("作废时间", 7);
		    col.put("作废原因", 8);
		    col.put("作废人", 9);
		    int rownum = 1;
			for(Map djdy:djdys){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
					HSSFCell cell0 = row.createCell(col.get("权证编号"));
					cell0.setCellValue(StringHelper.formatObject(djdy.get("QZBH")));
					HSSFCell cell1 = row.createCell(col.get("权证类型"));
					cell1.setCellValue(StringHelper.formatObject(djdy.get("QZLX")));
					HSSFCell cell2 = row.createCell(col.get("创建时间"));
					cell2.setCellValue(StringHelper.formatObject(djdy.get("CJSJ")));
					HSSFCell cell3 = row.createCell(col.get("是否缮证"));
					cell3.setCellValue(StringHelper.formatObject(djdy.get("SFSZ")));
					HSSFCell cell4 = row.createCell(col.get("缮证人"));
					cell4.setCellValue(StringHelper.FormatByDatatype(djdy.get("SZR")));
					HSSFCell cell5 = row.createCell(col.get("缮证时间"));
					cell5.setCellValue(StringHelper.formatObject(djdy.get("SZSJ")));
					HSSFCell cell6 = row.createCell(col.get("是否作废"));
					cell6.setCellValue(StringHelper.formatObject(djdy.get("SFZF")));
					HSSFCell cell7 = row.createCell(col.get("作废时间"));
					cell7.setCellValue(StringHelper.formatObject(djdy.get("ZFSJ")));
					HSSFCell cell8 = row.createCell(col.get("作废原因"));
					cell8.setCellValue(StringHelper.formatObject(djdy.get("ZFYY")));
					HSSFCell cell9 = row.createCell(col.get("作废人"));
					cell9.setCellValue(StringHelper.formatObject(djdy.get("ZFR")));
					rownum++;
		  		 }
		  		 catch(Exception ex){
		  			 
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	/**
	 * 查询权证过滤条件（URL:"/getcertfilter/{qzlx}",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @param qzlx 权证类型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getcertfilter/{qzlx}", method = RequestMethod.GET)
	public @ResponseBody BDCS_QZGLTJB GetCertFilter(@PathVariable("qzlx") String qzlx,HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		BDCS_QZGLTJB tjb=null;
		User user=Global.getCurrentUserInfo();
		if(user==null){
			return tjb;
		}
		String rydlm=user.getLoginName();
		List<BDCS_QZGLTJB> listFilter=basecommondao.getDataList(BDCS_QZGLTJB.class, "RYDLM='"+rydlm+"' AND QZLX='"+qzlx+"'");
		if(listFilter!=null&&listFilter.size()>0){
			tjb=listFilter.get(0);
		}
		return tjb;
	}
	
	/**
	 * 保存权证过滤条件（URL:"/savecertfilter/",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/savecertfilter/{qzlx}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage SaveCertFilter(@PathVariable("qzlx") String qzlx,HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		ResultMessage ms=new ResultMessage();
		ms.setMsg("保存失败！");
		ms.setSuccess("false");
		User user=Global.getCurrentUserInfo();
		if(user==null){
			ms.setMsg("请先登录！");
			return ms;
		}
		Long qsqzbh=null;
		if (!StringHelper.isEmpty(RequestHelper.getParam(request,"qsqzbh"))) {
			qsqzbh = Long.parseLong(RequestHelper.getParam(request,"qsqzbh"));
		}
		Long jsqzbh = null;
		if (!StringHelper.isEmpty(RequestHelper.getParam(request,"jsqzbh"))) {
			jsqzbh = Long.parseLong(RequestHelper.getParam(request,"jsqzbh"));
		}
		String sfxsyzf = null;
		if (!StringHelper.isEmpty(RequestHelper.getParam(request,"sfxsyzf"))) {
			sfxsyzf = RequestHelper.getParam(request,"sfxsyzf");
		}
		String sfxsysz = null;
		if (!StringHelper.isEmpty(RequestHelper.getParam(request,"sfxsysz"))) {
			sfxsysz = RequestHelper.getParam(request,"sfxsysz");
		}
		Date cjsj=new Date();
		String rydlm=user.getLoginName();
		String ryid=user.getId();
		BDCS_QZGLTJB tjb=null;
		List<BDCS_QZGLTJB> listFilter=basecommondao.getDataList(BDCS_QZGLTJB.class, "RYDLM='"+rydlm+"' AND QZLX='"+qzlx+"'");
		if(listFilter!=null&&listFilter.size()>0){
			tjb=listFilter.get(0);
			tjb.setCJSJ(cjsj);
			tjb.setRYID(ryid);
			if (!StringHelper.isEmpty(jsqzbh)) {
				tjb.setJSQZBH(jsqzbh);
			}
			if (!StringHelper.isEmpty(qsqzbh)) {
				tjb.setQSQZBH(qsqzbh);
			}
			tjb.setSFXSYSZ(sfxsysz);
			tjb.setSFXSYZF(sfxsyzf);
			basecommondao.update(tjb);
			basecommondao.flush();
			ms.setSuccess("true");
			ms.setMsg("更新成功！");
			YwLogUtil.addYwLog("权证管理-更新权证过滤条件", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		}else{
			tjb=new BDCS_QZGLTJB();
			tjb.setCJSJ(cjsj);
			if (!StringHelper.isEmpty(jsqzbh)) {
				tjb.setJSQZBH(jsqzbh);
			}
			if (!StringHelper.isEmpty(qsqzbh)) {
				tjb.setQSQZBH(qsqzbh);
			}
			tjb.setSFXSYSZ(sfxsysz);
			tjb.setSFXSYZF(sfxsyzf);
			tjb.setRYDLM(rydlm);
			tjb.setRYID(ryid);
			tjb.setQZLX(qzlx);
			basecommondao.save(tjb);
			basecommondao.flush();
			ms.setSuccess("true");
			ms.setMsg("保存成功！");
			YwLogUtil.addYwLog("权证管理-保存权证过滤条件", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		}
		return ms;
	}
	
	/**
	 * 根据过滤条件查询权证编号列表（URL:"/certlist/",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/certlistbyfilter/{qzlx}", method = RequestMethod.GET)
	public @ResponseBody Message QueryCertListByFilter(@PathVariable("qzlx") String qzlx,HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Message m=new Message();
		m.setSuccess("false");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		String qzbh = "";
		if (request.getParameter("qzbh") != null) {
			qzbh = request.getParameter("qzbh");
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" QZLX='").append(qzlx).append("'");
		if (!StringHelper.isEmpty(qzbh)) {
			hqlBuilder.append(" and QZBH LIKE '%").append(qzbh).append("%'");
		}
		
		User user=Global.getCurrentUserInfo();
		if(user==null){
			return m;
		}
		String rydlm=user.getLoginName();
		List<BDCS_QZGLTJB> listFilter=basecommondao.getDataList(BDCS_QZGLTJB.class, "RYDLM='"+rydlm+"' AND QZLX='"+qzlx+"'");
		if(listFilter!=null&&listFilter.size()>0){
			if(listFilter.get(0).getQSQZBH()!=null){
				hqlBuilder.append(" and QZBH >=").append(listFilter.get(0).getQSQZBH());
			}
			if(listFilter.get(0).getJSQZBH()!=null){
				hqlBuilder.append(" and QZBH <=").append(listFilter.get(0).getJSQZBH());
			}
			if(!StringHelper.isEmpty(listFilter.get(0).getSFXSYSZ())){
				if(SF.NO.Value.equals(listFilter.get(0).getSFXSYSZ())){
					hqlBuilder.append(" and SFSZ <>'1'");
				}
			}
			if(!StringHelper.isEmpty(listFilter.get(0).getSFXSYZF())){
				if(SF.NO.Value.equals(listFilter.get(0).getSFXSYZF())){
					hqlBuilder.append(" and SFZF <>'1'");
				}
			}
			if (SF.YES.Value.equals(ConfigHelper.getNameByValue("ISFilter_zsbh"))) {
				if(!StringHelper.isEmpty(listFilter.get(0).getRYID())){
					hqlBuilder.append(" and LQRYID ='").append(listFilter.get(0).getRYID()).append("'");
				}
			}
		}else {
			String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
			if(xzqdm != null && "650100".equals(xzqdm)){
				List<Map> newmap = new ArrayList<Map>();
				m.setSuccess("false");
				m.setTotal(0);
				m.setRows(newmap);
				m.setMsg("请先进行查询条件设置!");
				return m;
			}
		}
		hqlBuilder.append(" ORDER BY QZBH ASC");
		Page p = basecommondao.getPageDataByHql(BDCS_RKQZB.class, hqlBuilder.toString(), page, rows);
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 获取应生成的权证编号（URL:"/generatezsbh/{qzlx}",Method：GET）
	 * 
	 * @作者 冉全
	 * @param qzlx 权证类型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/generatezsbh/{qzlx}", method = RequestMethod.GET)
	public @ResponseBody Long GenerateZSBH(@PathVariable("qzlx") String qzlx,HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		long resultString = 0;
		//获取当前用户设置查询条件
		User user=Global.getCurrentUserInfo();
		if(user==null){
			resultString = 1;//未获取到当前用户信息，亲重新登录!
			return resultString;
		}
		String rydlm=user.getLoginName();
		List<BDCS_QZGLTJB> listFilter = basecommondao.getDataList(BDCS_QZGLTJB.class, "RYDLM='"+rydlm+"' AND QZLX='"+qzlx+"'");
		//获取当前用户未缮证未作废的证书编号，根据qzbh升序排列
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" QZLX='").append(qzlx).append("'");
		if(listFilter != null && listFilter.size() > 0){
			if(listFilter.get(0).getQSQZBH() != null){
				hqlBuilder.append(" AND QZBH >=").append(listFilter.get(0).getQSQZBH());
			}
			if(listFilter.get(0).getJSQZBH()!=null){
				hqlBuilder.append(" AND QZBH <=").append(listFilter.get(0).getJSQZBH());
			}
			if (SF.YES.Value.equals(ConfigHelper.getNameByValue("ISFilter_zsbh"))) {
				if(!StringHelper.isEmpty(listFilter.get(0).getRYID())){
					hqlBuilder.append(" AND LQRYID ='").append(listFilter.get(0).getRYID()).append("'");
				}
			}
		}else {
			String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
			if(xzqdm != null && "650100".equals(xzqdm)){
				resultString = 2;//请先进行查询条件设置!
				return resultString;
			}
		}
		hqlBuilder.append(" AND SFSZ='0' AND SFZF='0' ORDER BY QZBH ASC");			
		List<BDCS_RKQZB> listZSBH =basecommondao.getDataList(BDCS_RKQZB.class, hqlBuilder.toString());
		if(listZSBH != null && listZSBH.size() > 0){
			resultString =listZSBH.get(0).getQZBH();
		}else {
			resultString = 3;//查询范围内的证书编号已用完！
		}
		return resultString;
		
	}
	
	
	/**
	 * 导出发证阶段对应的权利人信息（URL:"/outputqlrinfo/",Method：POST）
	 * 
	 * @param request
	 * @param response
	 * @auther mss
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	@RequestMapping(value = "/outputqlrinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage outputQlrInfo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		String qlrinfopath = "%s\\resources\\qlrinfo\\%s.%s"; 
		ResultMessage ms=new ResultMessage();
		 String startdate = null;
		 String actdefname=request.getParameter("actdefname");
		if (request.getParameter("startdate") != null&&request.getParameter("startdate")!="") {
			startdate = request.getParameter("startdate");
			  
		}else{
			ms.setMsg("起始时间不能为空");
			ms.setSuccess("false");
			return ms;
			
		}
		startdate = startdate+ " 00:00:00";
		 String enddate = null;
			if (request.getParameter("enddate") != null&&request.getParameter("endtdate")!="") {
				enddate = request.getParameter("enddate");
				
			}else{
				ms.setMsg("结束时间不能为空");
				ms.setSuccess("false");
				return ms;
			}
			enddate = enddate+ " 23:59:59";
			StringBuilder str = new StringBuilder();
			str.append("1>0 ");
			if (startdate != null && !"".equals(startdate)) {
				str.append(" and ACTINST_START >= to_date('");
				str.append(startdate);
				str.append("','yyyy-MM-dd hh24:mi:ss')");
			}
			if (enddate != null && !"".equals(enddate)) {
				str.append(" and ACTINST_START < to_date('");
				str.append(enddate);
				str.append("','yyyy-MM-dd hh24:mi:ss')");
			}
			if(!StringHelper.isEmpty(actdefname)){
				str.append(" and ACTINST_NAME = '");
				str.append(actdefname);
				str.append("'");
			}
			
			str.append(" and ACTINST_STATUS IN (1,2)");
			
			
			
			long tatalCount = basecommondao.getCountByFullSql(" from "
					+ Common.WORKFLOWDB + "V_PROJECTLIST where " + str.toString());
			List<Map> listfiles=new ArrayList<Map>();
			if (tatalCount > 0) {
				listfiles=basecommondao.getDataListByFullSql( "select file_number from "
						+ Common.WORKFLOWDB + "V_PROJECTLIST where "
						+ str.toString());
			}
			if(listfiles!=null && listfiles.size()>0){
				List<String> listqlrinfo=new ArrayList<String>();   
				for(Map map : listfiles){
					Set set = map.keySet();
					Iterator it = set.iterator();
					while (it.hasNext()) {
					Object key = it.next();
					Object value = map.get(key);
					
				List listxmbhs=new ArrayList();   
				listxmbhs=basecommondao.getDataList(BDCS_XMXX.class, "PROJECT_ID='"+value+"'");
				if(listxmbhs!=null && listxmbhs.size()>0){
					BDCS_XMXX xmxx=(BDCS_XMXX) listxmbhs.get(0);
					String xmbh=xmxx.getId();
					String ywlsh=xmxx.getYWLSH();
					String sfbj=xmxx.getSFBJ();
					String sfdb = xmxx.getSFDB();
					if(!StringHelper.isEmpty(xmbh)&&StringHelper.isEmpty(sfbj)&& "1".equals(sfdb)){
						listxmbhs=basecommondao.getDataList(BDCS_QLR_XZ.class, "XMBH='"+xmbh+"'");
						if(listxmbhs!=null &&listxmbhs.size()>0){
							for (int i = 0; i < listxmbhs.size(); i++) {
								BDCS_QLR_XZ qlr=(BDCS_QLR_XZ) listxmbhs.get(i);
								String qlrmc=qlr.getQLRMC();
								String dh = qlr.getDLRLXDH()!= null?qlr.getDLRLXDH():qlr.getDH();
								String info = qlrmc+","+dh+","+ywlsh;
								String dlrxm = qlr.getDLRXM();
								String dlrxm2 ="";
								if (listxmbhs.size()>1 && i!= listxmbhs.size()-1) {
									dlrxm2 = ((BDCS_QLR_XZ) listxmbhs.get(i+1)).getDLRXM();
								}
								//多个权利人，代理人为同一个人的时候，只获取其中一条权利人信息
								if(listqlrinfo.contains(info)|| (!StringHelper.isEmpty(dlrxm)&&dlrxm.equals(dlrxm2))){
									continue;
								}else {
									listqlrinfo.add(info);
								}
								
							}
//								String info;
//								BDCS_QLR_XZ qlr=(BDCS_QLR_XZ) listxmbhs.get(0);
//								String qlrmc=qlr.getQLRMC();
//								String dh=qlr.getDLRLXDH();
//								if(dh==null||dh==""){
//									dh=qlr.getDH();
//								}
//								if(qlrmc.contains("、")){
//									String[] qlrmcs=qlrmc.split("、");
//									
//									for(int i=0;i<qlrmcs.length;i++){
//										qlrmc=qlrmcs[i];
//									    info=qlrmc+","+dh+","+ywlsh;
//									    listqlrinfo.add(info);
//									}
//								}else{
//									
//									 if(dh!=null && dh!=""&&dh.length()==11){
//										 info=qlrmc+","+dh+","+ywlsh;
//										 if(listqlrinfo.contains(info)){
//											 continue;
//										 }else{
//											 listqlrinfo.add(info);
//										 }
//										
//									 }else{
//										 
//									 }
//									 
//								}
						}
					}
				}
			
				}
			}
				try {
					@SuppressWarnings("deprecation")
					FileOutputStream fs = new FileOutputStream(new File(String.format(qlrinfopath, request.getRealPath("/"), "处于发证阶段的权利人信息", "txt")));
					fs.write(StringHelper.formatList(listqlrinfo,"\r\n").getBytes());
					ms.setSuccess("true");
					ms.setMsg("导出发证阶段权利人信息成功，请注意查看！");
				} catch (FileNotFoundException e) {
					ms.setMsg("失败");
					e.printStackTrace();
				}
			}
			
		return ms;
	}
	
	/**
	 * 上传作废证书文件（URL:"/outputqlrinfo/",Method：POST）
	 * 
	 * @param request
	 * @param response
	 * @auther mss
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/certinfomanage/uploadzfzspic", method = RequestMethod.POST)
	public @ResponseBody void uploadZfZsPic(@RequestParam("file") MultipartFile[] files,HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		String id = request.getParameter("id");
		if (!StringHelper.isEmpty(id) ) {
			String fulsql = "SELECT RKQZB.ID FROM BDCK.BDCS_RKQZB RKQZB WHERE RKQZB.ID='"+id+"'"; 
			List<Map> sqlforid =basecommondao.getDataListByFullSql(fulsql);
			if(sqlforid !=null && sqlforid.size()>0){
				String fulsql_count = "FROM BDCK.BDCS_ZSZFPIC ZSZF WHERE ZSZF.RKQZBID='"+id+"'";
				long count = basecommondao.getCountByFullSql(fulsql_count);
				for (int i = 0; i < files.length; i++) {
					BDCS_ZSZFPIC zszfpc = new BDCS_ZSZFPIC();
					zszfpc.setRKQZBID(id);
					MultipartFile file = files[i];
					BASE64Encoder encoder = new BASE64Encoder();
					// 通过base64来转化图片
					String data ="data:image/jpeg;base64," + encoder.encode(file.getBytes());
					zszfpc.setId((String) SuperHelper.GeneratePrimaryKey());
					if(count > 0){
						zszfpc.setSXH((int)count+1);
						count++;
					}else {
						zszfpc.setSXH(i+1);
					}					
					zszfpc.setZSPIC(data);
					basecommondao.save(zszfpc);
				}
				
			}
		}
		
	}
 
	//获取证书图片
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/certinfomanage/getzszfpic", method = RequestMethod.GET)
	public @ResponseBody ArrayList<String> getZSZFPIC(HttpServletRequest request, HttpServletResponse response) throws ParseException {
	
		String id = request.getParameter("id");
		ArrayList<String> result =new ArrayList<String>();
		if (!StringHelper.isEmpty(id) ) {
			//int qzbh_num = Integer.parseInt(qzbh);
			//Number qzbh_num = NumberFormat.getInstance().parse(qzbh);  
			String fulsql = "SELECT RKQZB.ID FROM BDCK.BDCS_RKQZB RKQZB WHERE RKQZB.ID='"+id+"'"; 
			List<Map> sqlforid =basecommondao.getDataListByFullSql(fulsql);
			if(sqlforid !=null && sqlforid.size()>0)
			{
				List<BDCS_ZSZFPIC> pics = basecommondao.getDataList(BDCS_ZSZFPIC.class, "RKQZBID ='"+id+"'");
				if(pics!=null&&pics.size()>0){
					for (BDCS_ZSZFPIC pic:pics) {
						result.add(pic.getZSPIC());
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 项目编号管理编辑页面 (URL:"/projectinfoindex",Method：GET)
	 * @Title: ShowMappingMain2
	 * @author:liushufeng
	 * @date：2015年10月29日 下午10:03:25
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/certmanageinfo", method = RequestMethod.GET)
	public String ShowCertManageInfo(Model model) {
		return "/realestate/registration/certmanage/certmanageinfo";
	}

	/**
	 * 新增或保存项目编号管理的信息 (URL:"/qzffinfo",Method：POST)
	 * @Title: saveOrUpdateQZFFInfo
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:17:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzbhinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveOrUpdateQZBHInfo(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg=new ResultMessage();
		if(Global.getCurrentUserInfo()==null){
			msg.setSuccess("false");
			msg.setMsg("保存失败！请先登录！");
			return msg;
		}
		String id = request.getParameter("id");
		String qzbh = request.getParameter("qzbh");
		String sfsz = request.getParameter("sfsz");
		String szry = request.getParameter("szry");
		String szsj = request.getParameter("szsj");
		String sffz = request.getParameter("sffz");
		String fzsj = request.getParameter("fzsj");
		String lzry = request.getParameter("lzry");
		String lzrzjh = request.getParameter("lzrzjh");
		String sfzf = request.getParameter("sfzf");
		String zfsj = request.getParameter("zfsj");
		String zfyy = request.getParameter("zfyy");
		String zfr = request.getParameter("zfr");
		BDCS_RKQZB rkqzb = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			rkqzb = basecommondao.get(BDCS_RKQZB.class, id);
			bnew = false;
		}
		if (rkqzb == null) {
			rkqzb = new BDCS_RKQZB();
			rkqzb.setId((String) SuperHelper.GeneratePrimaryKey());
			rkqzb.setQZBH(Long.parseLong(qzbh));// 创建人员
			
		}
		rkqzb.setSFSZ(sfsz);
		rkqzb.setSZRY(szry);
		if (!StringHelper.isEmpty(szsj)) {
			try {
				rkqzb.setSZSJ(StringHelper.FormatByDate(szsj));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else {
			rkqzb.setSZSJ(null);
		}
		if (!StringHelper.isEmpty(fzsj)) {
			try {
				rkqzb.setFZSJ(StringHelper.FormatByDate(fzsj));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else {
			rkqzb.setSZSJ(null);
		}
		
		rkqzb.setSFFZ(sffz);
		rkqzb.setLZRY(lzry);
		rkqzb.setLZRZJH(lzrzjh);
		if(SF.YES.Value.equals(sfzf)){
			List<BDCS_ZS_GZ> certs_gz=basecommondao.getDataList(BDCS_ZS_GZ.class, "ZSBH='"+rkqzb.getQZBH()+"'");
			List<BDCS_ZS_XZ> certs_xz=basecommondao.getDataList(BDCS_ZS_XZ.class, "ZSBH='"+rkqzb.getQZBH()+"'");
			List<BDCS_ZS_LS> certs_ls=basecommondao.getDataList(BDCS_ZS_LS.class, "ZSBH='"+rkqzb.getQZBH()+"'");
			if(certs_gz!=null&&certs_gz.size()>0){
				msg.setMsg("此证号正在被使用，不能作废！");
				return msg;
			}else if(certs_xz!=null&&certs_xz.size()>0){
				msg.setMsg("此证号正在被使用，不能作废！");
				return msg;
			}else if(certs_ls!=null&&certs_ls.size()>0){
				msg.setMsg("此证号正在被使用，不能作废！");
				return msg;
			}else{				
				rkqzb.setSFZF(sfzf);
				rkqzb.setZSZT("0");
				try {
					rkqzb.setZFSJ(StringHelper.FormatByDate(zfsj));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rkqzb.setZFYY(zfyy);
				rkqzb.setZFR(zfr);
			}
		}else {
			rkqzb.setSFZF(sfzf);
			rkqzb.setZFSJ(null);
			rkqzb.setZFYY(null);
			//将作废图片删除
			String condition = "RKQZBID='"+id+"'";
			basecommondao.deleteEntitysByHql(BDCS_ZSZFPIC.class, condition);
		}
		
		if (bnew)
			basecommondao.save(rkqzb);
		else
			basecommondao.update(rkqzb);
		basecommondao.flush();
		rkqzb.setSYQK(StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"进行了权证信息编辑；");
		msg.setSuccess("true");
		msg.setMsg("保存成功");

		YwLogUtil.addYwLog("权证管理-权证编号管理更新", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}
	
	/**
	 * 获取权证分发项目信息 (URL:"/qzffinfo/{id}",Method：GET)
	 * @Title: getQZFFInfo
	 * @author:yuxuebin
	 * @date：2017年03月27日 14:27:07
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qzbhinfo/{id}", method = RequestMethod.GET)
	public @ResponseBody Message getQZBHInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		BDCS_RKQZB rkqzb = basecommondao.get(BDCS_RKQZB.class, id);
		Message msg = new Message();
		List<BDCS_RKQZB> list = new ArrayList<BDCS_RKQZB>();
		list.add(rkqzb);
		msg.setRows(list);
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-获取权证编号信息", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}
	
	/**
	 * 分页获取选择领证人信息列表
	 * @author luml
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/getlzrlist/", method = RequestMethod.GET)
	public @ResponseBody Message GetLZRInfo(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// 分页查询
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String lzrxm = RequestHelper.getParam(request, "lzrxm");
		StringBuilder conditionBuilder = new StringBuilder("1 = 1");
		if (!StringHelper.isEmpty(lzrxm)) {
			conditionBuilder.append(" AND DLRXM LIKE '%").append(lzrxm)
					.append("%'");
		}
		String strQuery = conditionBuilder.toString();
		Page p = basecommondao.getPageDataByHql(BDCS_QLR_D.class, strQuery, page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}
	
	
	/**
     * 添加领证人模板
     * @作者 luml
     * @创建时间 2017年9月7日下午15:02:14
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
	@RequestMapping(value = "/lzrs/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddDJYYS(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		ResultMessage msg = new ResultMessage();
		String lzrdz=request.getParameter("bz");
		String dlrxm=request.getParameter("dlrxm");
		String dlrzjhm=request.getParameter("dlrzjhm");
		String dlrlxdh=request.getParameter("dlrlxdh");
		
		BDCS_QLR_D bdcs_qlr_d = new BDCS_QLR_D();
		bdcs_qlr_d.setId((String)SuperHelper.GeneratePrimaryKey());
		bdcs_qlr_d.setBZ(lzrdz);
		bdcs_qlr_d.setCREATETIME(new Date());
		bdcs_qlr_d.setDLRXM(dlrxm);
		bdcs_qlr_d.setDLRZJHM(dlrzjhm);
		bdcs_qlr_d.setDLRLXDH(dlrlxdh);
		basecommondao.save(bdcs_qlr_d);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 修改领证人信息
	 * @作者 luml
	 * @创建时间 2017年9月7日下午17:02:18
	 * @param sqrid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/lzrs/{sqrid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ModifyLZXXS(@PathVariable("sqrid") String sqrid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		ResultMessage msg = new ResultMessage();	
		if (StringHelper.isEmpty(sqrid)) {
			msg.setSuccess("false");
			msg.setMsg("待更新记录ID不能为空");
			return msg;
		}			
		String lzrdz=request.getParameter("bz");
		String dlrxm=request.getParameter("dlrxm");
		String dlrzjhm=request.getParameter("dlrzjhm");
		String dlrlxdh=request.getParameter("dlrlxdh");
		if (StringHelper.isEmpty(dlrxm)) {
			msg.setSuccess("false");
			msg.setMsg("领证人姓名不能为空");
			return msg;
		}			
		BDCS_QLR_D bdcs_qlr_d=basecommondao.get(BDCS_QLR_D.class, sqrid);
		if(sqrid==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+sqrid+"的信息，无法更新！");
			return msg;
		}
		bdcs_qlr_d.setBZ(lzrdz);
		bdcs_qlr_d.setMODIFYTIME(new Date());
		bdcs_qlr_d.setDLRXM(dlrxm);
		bdcs_qlr_d.setDLRZJHM(dlrzjhm);
		bdcs_qlr_d.setDLRLXDH(dlrlxdh);
		basecommondao.update(bdcs_qlr_d);
		basecommondao.flush();
		return msg;
	}
	/**
	 * 获取缮证人员列表 (URL:"/qzffinfo/{id}",Method：GET)
	 * @Title: getSZRY
	 * @author:taochunda
	 * @date：2017年11月16日 15:00:07
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/szrys", method = RequestMethod.GET)
	public @ResponseBody Message getSZRY(HttpServletRequest request, HttpServletResponse response) {
		List<T_ROLE> role = basecommondao.getDataList(T_ROLE.class, "ROLENAME='缮证'");
		String roleId = "";
		List<String> users = null;
		List<User> list = new ArrayList<User>();
		if (role != null && role.size() > 0) {
			roleId = role.get(0).getID();
			users = roleService.findUserIdByRoleId(roleId);
			for (int i = 0; i < users.size(); i++) {
				String userId = users.get(i);
				User user = basecommondao.get(User.class, userId);
				list.add(user);
			}
		}
		Message msg = new Message();
		msg.setRows(list);
		msg.setSuccess("true");
		YwLogUtil.addYwLog("权证管理-获取缮证人员列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}
	
	/**
	 * 根据登录用户获取所属角色信息 (URL:"/qzffinfo/{id}",Method：GET)
	 * @Title: getRole
	 * @author:taochunda
	 * @date：2017年12月14日 10:12:00
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getrole/", method = RequestMethod.GET)
	public @ResponseBody Message getRole(HttpServletRequest request, HttpServletResponse response) {
		User user = userService.getCurrentUserInfo();
		StringBuilder fullsql = new StringBuilder();
		fullsql.append("SELECT R.ID,R.ROLENAME FROM SMWB_FRAMEWORK.RT_USERROLE RT ")
		.append("LEFT JOIN SMWB_FRAMEWORK.T_USER U ON RT.USERID = U.ID ")
		.append("LEFT JOIN SMWB_FRAMEWORK.T_ROLE R ON RT.ROLEID = R.ID ")
		.append("WHERE U.ID='").append(user.getId()).append("'");
		
		List<Map> map = basecommondao.getDataListByFullSql(fullsql.toString());
		for (Map m : map) {
			if (m.containsValue("采购分发员") || m.containsValue("科室分发员") || m.containsValue("打证员")) {
				System.out.println("采购分发员or科室分发员or打证员");
				break;
			}
		}
//		List<String> roles = roleService.getRolesByLoginName("'"+user.getLoginName()+"'");
		Message msg = new Message();
		msg.setSuccess("true");
		//YwLogUtil.addYwLog("权证管理-获取缮证人员列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}
	
	/**
	 * 根据角色名判断当前用户是否有该角色权限
	 * @param rolename
	 * @return
	 */
	public boolean isContainRole(String userID, String rolename){
		User user = userService.getCurrentUserInfo();
		StringBuilder fullsql = new StringBuilder();
		fullsql.append("SELECT R.ID,R.ROLENAME,U.ID USERID FROM SMWB_FRAMEWORK.RT_USERROLE RT ")
		.append("LEFT JOIN SMWB_FRAMEWORK.T_USER U ON RT.USERID = U.ID ")
		.append("LEFT JOIN SMWB_FRAMEWORK.T_ROLE R ON RT.ROLEID = R.ID ")
		.append("WHERE U.ID='").append(userID).append("' ")
		.append("OR R.ROLENAME='管理员'");
		
		List<Map> map = basecommondao.getDataListByFullSql(fullsql.toString());
		for (Map m : map) {
			if ("管理员".equals(m.get("ROLENAME"))) {
				if (userID.equals(m.get("USERID"))) {//先看是否有管理员角色权限
					return true;
				}
			} else if (rolename.equals(m.get("ROLENAME"))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 证书编号管理导出
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/downloadzsbh", method = RequestMethod.GET)
	public @ResponseBody String DownloadZSBH(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//********************************
		String qzbh = "";
		if (request.getParameter("qzbh") != null) {
			qzbh = request.getParameter("qzbh");
		}
		
		String qzlx = "";
		if (request.getParameter("qzlx") != null) {
			qzlx = request.getParameter("qzlx");
		}
		String bdcqzh = "";
		if (request.getParameter("bdcqzh") != null) {
			bdcqzh = RequestHelper.getParam(request,"bdcqzh");
		}
		String qlrmc = "";
		if (request.getParameter("qlrmc") != null) {
			qlrmc = RequestHelper.getParam(request, "qlrmc");
		}
		String qlrzjh = "";
		if (request.getParameter("qlrzjh") != null) {
			qlrzjh =RequestHelper.getParam(request,"qlrzjh");
		}
		String zszt = "";
		if (request.getParameter("zszt") != null) {
			zszt = request.getParameter("zszt");
		}
		String zl = "";
		if (request.getParameter("zl") != null) {
			zl =RequestHelper.getParam(request,"zl");
		}
		String sfsz = "";
		if (request.getParameter("sfsz") != null) {
			sfsz =RequestHelper.getParam(request,"sfsz");
		}
		String sfzf = "";
		if (request.getParameter("sfzf") != null) {
			sfzf =RequestHelper.getParam(request,"sfzf");
		}
		String szry = "";
		if (request.getParameter("szry") != null) {
			szry = RequestHelper.getParam(request, "szry");
		}
		String start = "";
		if (request.getParameter("start") != null) {
			start = RequestHelper.getParam(request, "start");
		}
		String end = "";
		if (request.getParameter("end") != null) {
			end = RequestHelper.getParam(request, "end");
		}
		String viewRange ="";
		if (request.getParameter("viewRange") != null) {
			viewRange = request.getParameter("viewRange");
		}
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("SELECT * FROM BDCK.BDCS_RKQZB WHERE 1>0 ");
		if (!StringHelper.isEmpty(qzbh)) {
			hqlBuilder.append(" and TO_CHAR(QZBH) LIKE '%").append(qzbh).append("%'");
		}
		if (!StringHelper.isEmpty(qzlx) && !("2").equals(qzlx)) {
			hqlBuilder.append(" and QZLX='").append(qzlx).append("'");
		}
		if (!StringHelper.isEmpty(bdcqzh)) {
			hqlBuilder.append(" and BDCQZH='").append(bdcqzh).append("'");
		}
		if (!StringHelper.isEmpty(qlrmc)) {
			hqlBuilder.append(" and QLRMC LIKE '%").append(qlrmc).append("%'");
		}
		if (!StringHelper.isEmpty(qlrzjh)) {
			hqlBuilder.append(" and QLRZJH LIKE '%").append(qlrzjh).append("%'");
		}
		if (!StringHelper.isEmpty(zszt) && !("2").equals(zszt)) {
			hqlBuilder.append(" and ZSZT='").append(zszt).append("'");
		}
		if (!StringHelper.isEmpty(zl)) {
			hqlBuilder.append(" and ZL LIKE '%").append(zl).append("%'");
		}
		if (!StringHelper.isEmpty(sfsz) && !("2").equals(sfsz)) {
			
			hqlBuilder.append(" and SFSZ='").append(sfsz).append("'");
		}
		if (!StringHelper.isEmpty(sfzf) && !("2").equals(sfzf)) {
			hqlBuilder.append(" and SFZF='").append(sfzf).append("'");
		}
		if (!StringHelper.isEmpty(szry)) {
			hqlBuilder.append(" and SZR LIKE '%").append(szry).append("%'");
		}
		if (!StringHelper.isEmpty(start)) {
			hqlBuilder.append(" and SZSJ > TO_DATE('"+start+"','yyyy-mm-dd HH24:mi:ss')");
		}
		if (!StringHelper.isEmpty(end)) {
			hqlBuilder.append(" and SZSJ < TO_DATE('"+end+"','yyyy-mm-dd HH24:mi:ss')");
		}
		
		if (StringHelper.isEmpty(viewRange) || !"2".equals(viewRange)) {//除了权利为2的，其他值只可以查看当前登录用户所领取的qzbh
			User user=Global.getCurrentUserInfo();			
			hqlBuilder.append(" and LQRY='").append(user.getUserName()).append("'")
			.append(" and LQRYID='").append(user.getId()).append("'");
		}
		hqlBuilder.append(" ORDER BY QZBH");
		List<Map> list = basecommondao.getDataListByFullSql(hqlBuilder.toString());
		if(list.size() >0){
			for (Map map : list) {
				String _zszt = (String) map.get("ZSZT");
				String _qzlx = (String) map.get("QZLX");
				String _sfsz = (String) map.get("SFSZ");
				String _sffz = (String) map.get("SFFZ");
				String _sfzf = (String) map.get("SFZF");
				if(!StringHelper.isEmpty(_zszt)){
					if(_zszt.equals("0")){
						map.put("ZSZT", "无效");
					}
					if(_zszt.equals("1")){
						map.put("ZSZT", "有效");
					}
				}
				if(!StringHelper.isEmpty(_qzlx)){
					if(_qzlx.equals("0")){
						map.put("QZLX", "不动产权证书");
					}
					if(_qzlx.equals("1")){
						map.put("QZLX", "不动产权证明");
					}
				}
				if(!StringHelper.isEmpty(_sfsz)){
					if(_sfsz.equals("0")){
						map.put("SFSZ", "否");
					}
					if(_sfsz.equals("1")){
						map.put("SFSZ", "是");
					}
				}
				if(!StringHelper.isEmpty(_sffz)){
					if(_sffz.equals("0")){
						map.put("SFFZ", "否");
					}
					if(_sffz.equals("1")){
						map.put("SFFZ", "是");
					}
				}
				if(!StringHelper.isEmpty(_sfzf)){
					if(_sfzf.equals("0")){
						map.put("SFZF", "否");
					}
					if(_sfzf.equals("1")){
						map.put("SFZF", "是");
					}
				}
			}
		}
		//********************************
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\cretmanageinfo.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\cretmanageinfo.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/cretmanageinfo.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行格式
		int lastrow = sheet.getLastRowNum();
		HSSFRow head = sheet.getRow(lastrow - 1);
		HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
		short height = sheet.getRow(lastrow).getHeight();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.createRow(1);
		title.setHeight(height);
		//序号
		HSSFCell Cell = title.createCell(0);
		 Cell.setCellValue("序号") ;
		 Cell.setCellStyle(style);
		 MapCol.put("序号", 0);
		//content
		for (int i = 0; i < mapListJson.size(); i++) {
			 HSSFCell title_Cell = title.createCell(i+1);
			 title_Cell.setCellValue(mapListJson.get(i).get("name").toString()) ;
			 MapCol.put(mapListJson.get(i).get("name").toString(), i+1);
			 title_Cell.setCellStyle(style);
		}
		//添加内容行
		int rownum = 2;
		for(Map m : list){
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
	         Cell0.setCellValue(rownum-1);
	         for (int i = 0; i < mapListJson.size(); i++) {
				 String name = mapListJson.get(i).get("name").toString();
				 String value = mapListJson.get(i).get("value").toString();
	        	 HSSFCell Content_Cell = row.createCell(MapCol.get(name));
	        	 Content_Cell.setCellValue(StringHelper.FormatByDatatype(m.get(value)));
			}
	        rownum += 1;
	  }
	
	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    outstream = null;
	    return url;
	}
}

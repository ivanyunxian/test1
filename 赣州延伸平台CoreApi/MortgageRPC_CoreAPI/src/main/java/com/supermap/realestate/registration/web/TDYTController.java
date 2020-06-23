package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 获取户所对应的土地用途信息BDCS_TDYT
 * @author 海豹
 *
 */
@Controller
@RequestMapping("/tdyt")
public class TDYTController {

	@Autowired
	private CommonDao dao;

/**
 * 通过qlid和xmbh查询土地用途信息
 * @作者 海豹
 * @创建时间 2015年11月3日下午10:31:39
 * @param xmbh
 * @param qlid
 * @param request
 * @param response
 * @return
 * @throws UnsupportedEncodingException
 */
	@RequestMapping(value = "/tdytinfo/", method = RequestMethod.GET)
	public @ResponseBody Message QueryTDYTS( HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String xmbh=request.getParameter("xmbh");
		String qlid="";
		if(request.getParameter("qlid") != null){
			qlid=request.getParameter("qlid");
		}
		
		String sqlxmbh = ProjectHelper.GetXMBHCondition(xmbh);
		Message msg = new Message();
		Rights _rights = null;
		if(StringHelper.isEmpty(qlid)){
			List<Rights> list=RightsTools.loadRightsByCondition(DJDYLY.GZ, sqlxmbh);
			if(list!=null&&list.size()>0){
				_rights=list.get(0);
			}
		}else{
			_rights= RightsTools.loadRights(DJDYLY.GZ, qlid);
		}
		
		if (!StringHelper.isEmpty(_rights)) {
			String djdyid = _rights.getDJDYID();
			List<BDCS_DJDY_GZ> lstdjdy = dao.getDataList(BDCS_DJDY_GZ.class,
					sqlxmbh + " and DJDYID ='" + djdyid + "'");
			if (lstdjdy != null && lstdjdy.size() > 0) {
				BDCS_DJDY_GZ bdcs_djdy_gz = lstdjdy.get(0);
				if (bdcs_djdy_gz != null) {
					if (!StringHelper.isEmpty(bdcs_djdy_gz.getBDCDYLX())
							&& !StringHelper.isEmpty(bdcs_djdy_gz.getLY())) {
						DJDYLY djdyly = DJDYLY.initFrom(bdcs_djdy_gz.getLY());
						BDCDYLX bdcdylx = BDCDYLX.initFrom(bdcs_djdy_gz
								.getBDCDYLX());
						RealUnit _unit = UnitTools.loadUnit(bdcdylx, djdyly,
								bdcs_djdy_gz.getBDCDYID());
						if (_unit != null) {						
							if(_unit instanceof House)
							{
								House house = (House) _unit;
								String zdbdcdyid = house.getZDBDCDYID();
								if (!StringHelper.isEmpty(zdbdcdyid)) {
									msg.setSuccess("true");
									List<RealUnit> zd=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, sqlxmbh+" and BDCDYID='"+zdbdcdyid+"'");
									Page p=null;
									if(!StringHelper.isEmpty(zd) && zd.size()>0)
									{
										p=dao.getPageDataByHql(BDCS_TDYT_GZ.class, "BDCDYID ='"
													+ zdbdcdyid + "'", page, rows);
									}
									else
									{
										p = dao.getPageDataByHql(
												BDCS_TDYT_XZ.class, "BDCDYID ='"
														+ zdbdcdyid + "'", page, rows);
									}
									if (p != null) {
										msg.setTotal(p.getTotalCount());
										msg.setRows(p.getResult());
									}
								}
							}						
						}
					}
				}
			}
		}
		return msg;
	}
}

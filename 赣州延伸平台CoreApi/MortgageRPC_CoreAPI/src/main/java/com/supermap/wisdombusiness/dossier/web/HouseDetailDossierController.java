package com.supermap.wisdombusiness.dossier.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supemap.mns.common.HttpMethod;
import com.supemap.mns.model.Message;
import com.supermap.wisdombusiness.dossier.web.common.Basic;
import com.supermap.wisdombusiness.dossier.web.common.BookMapping;
import com.supermap.wisdombusiness.dossier.web.common.WorkFlow2Dossier;

@Controller
@RequestMapping("/bdc/HouseDetaildossier")
public class HouseDetailDossierController {
	/*
	 * private final String dossierService = GetProperties
	 * .getConstValueByKey("dossierservice");
	 */
	@Autowired
	private WorkFlow2Dossier workFlow2Dossier;
	@Autowired
	private BookMapping bookMapping;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String ShowIndex(Model model) {

		return "";
	}

	@RequestMapping(value = "/SearchAjid/{file_number}", method = RequestMethod.GET)
	@ResponseBody
	public String ee(@PathVariable String file_number,
			HttpServletRequest request, HttpServletResponse response) {
		String resultstr = "";
		if (StringHelper.isNotNull(file_number)) {
			Message creatResult = null;
			creatResult = workFlow2Dossier.SearchAjid(file_number);
			if (creatResult != null) {
				String returnvalue = creatResult.getMessageBodyAsString();
				if (StringHelper.isNotNull(returnvalue)) {
					JSONObject jobj = JSONObject.fromObject(returnvalue);
					String success = jobj.getString("success");
					if (success != "-1" && success != null) {
						String AJID = jobj.getString("msg");
						if (StringHelper.isNotNull(AJID)) {
							if (AJID != "null") {
								String dossierService = ConfigHelper
										.getNameByValue("dossierservice");
								resultstr = dossierService
										+ "Edit/DAManage.jsp?AJID=" + AJID;
							} else {
								resultstr = "-1";
							}
						} else {
							resultstr = "-1";
						}
					} else {
						resultstr = "-1";
					}
				} else {
					resultstr = "-1";
				}
			} else {
				resultstr = "-1";
			}
		}
		return resultstr;
	}

	@RequestMapping(value = "/searchajidbyxmbh/{xmbh}", method = RequestMethod.GET)
	@ResponseBody
	public String SearchAjidByXMBH(@PathVariable String xmbh,
			HttpServletRequest request, HttpServletResponse response) {
		String resultstr = "";
		if (StringHelper.isNotNull(xmbh)) {
			Message creatResult = null;
			creatResult = workFlow2Dossier.SearchAjidByXMBH(xmbh);
			if (creatResult != null) {
				String returnvalue = creatResult.getMessageBodyAsString();
				if (StringHelper.isNotNull(returnvalue)) {
					JSONObject jobj = JSONObject.fromObject(returnvalue);
					String success = jobj.getString("success");
					if (success != "-1" && success != null) {
						String AJID = jobj.getString("msg");
						if (StringHelper.isNotNull(AJID)) {
							if (AJID != "null") {
								String dossierService = ConfigHelper
										.getNameByValue("dossierservice");
								resultstr = dossierService
										+ "Edit/DAManage.jsp?AJID=" + AJID;
							} else {
								resultstr = "-1";
							}
						} else {
							resultstr = "-1";
						}
					} else {
						resultstr = "-1";
					}
				} else {
					resultstr = "-1";
				}
			} else {
				resultstr = "-1";
			}
		}
		return resultstr;
	}

}

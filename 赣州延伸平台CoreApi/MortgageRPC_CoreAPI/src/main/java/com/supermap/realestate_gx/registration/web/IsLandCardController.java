package com.supermap.realestate_gx.registration.web;

import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.realestate_gx.registration.util.GX_Util;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/frame" })
public class IsLandCardController {
	@RequestMapping(value = { "/approval/saveTDYW" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public SmObjInfo saveTDYW(HttpServletRequest request,
			HttpServletResponse response) {
		String FILE_NUMBER = request.getParameter("FILE_NUMBER");
		String td_tsatus = request.getParameter("status");
		GX_CONFIG gxconfig = GX_Util.findByFile_number(FILE_NUMBER);
		if (gxconfig == null) {
			GX_CONFIG gx = new GX_CONFIG();
			gx.setFILE_NUMBER(FILE_NUMBER);
			gx.setTD_STATUS(td_tsatus);
			GX_Util.save(gx);
			GX_Util.gxFlush();
			System.out.println("保存一条新的记录");
		} else if (gxconfig != null) {
			gxconfig.setTD_STATUS(td_tsatus);
			GX_Util.saveOrUpdate(gxconfig);
			System.out.println("更新一条原有记录");
			GX_Util.gxFlush();
		}

		SmObjInfo info = new SmObjInfo();
		info.setText("1");
		return info;
	}

	@RequestMapping(value = { "/approval/tdStatus/{file_number}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public static String GetGx_ConfigEx1(@PathVariable String file_number,
			HttpServletRequest request) {
		String check = null;
		GX_CONFIG gxconfig = GX_Util.findByFile_number(file_number);
		if (gxconfig != null) {
			String status = gxconfig.getTD_STATUS();
			if (status == null)
				check = "有";
			else if (status.equals("1")) {
				check = "有";
			} else if (status.equals("0"))
				check = "无";
		} else {
			check = "有";
		}
		return check;
	}
}

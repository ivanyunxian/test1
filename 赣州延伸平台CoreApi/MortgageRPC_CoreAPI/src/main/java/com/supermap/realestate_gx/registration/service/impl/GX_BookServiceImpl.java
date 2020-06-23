package com.supermap.realestate_gx.registration.service.impl;

import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.realestate_gx.registration.service.GX_BookService;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import java.io.PrintStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service("gx_bookService")
public class GX_BookServiceImpl implements GX_BookService {

	@Autowired
	private CommonDao baseCommonDao;

	public String getTd_statusByBdcdyid(String bdcdyid) {
		String status = "";
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		List gxconfig = cdao.getDataList(GX_CONFIG.class, "BDCDYID='" + bdcdyid
				+ "'");
		if ((gxconfig != null) && (gxconfig.size() > 0)) {
			GX_CONFIG gc = (GX_CONFIG) gxconfig.get(0);
			String td_status = gc.getTD_STATUS();
			if ((td_status != null) && (td_status.equals("0"))) {
				status = td_status;
				System.out.println("土地证状态：" + status);
			} else {
				status = "1";
			}
		}
		return status;
	}
}

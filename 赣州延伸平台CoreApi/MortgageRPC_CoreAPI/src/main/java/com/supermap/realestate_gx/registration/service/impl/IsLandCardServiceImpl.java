package com.supermap.realestate_gx.registration.service.impl;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.realestate_gx.registration.service.IsLandCardService;
import com.supermap.realestate_gx.registration.util.GX_Util;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import java.io.PrintStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service("isLandCardService")
public class IsLandCardServiceImpl implements IsLandCardService {

	@Autowired
	private CommonDao baseCommonDao;

	public String getTdStatus(String xmbh, String djdyid) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		List xmxx = cdao.getDataList(BDCS_XMXX.class, "XMBH='" + xmbh + "'");
		String td_status = "";
		String bdcdyh = "";
		String bdcdyid = "";
		if ((xmxx != null) && (xmxx.size() > 0)) {
			BDCS_XMXX xm = (BDCS_XMXX) xmxx.get(0);
			String file_number = xm.getPROJECT_ID();
			List gxconfig = cdao.getDataList(GX_CONFIG.class, "FILE_NUMBER='"
					+ file_number + "'");
			if ((gxconfig != null) && (gxconfig.size() > 0)) {
				GX_CONFIG gc = (GX_CONFIG) gxconfig.get(0);
				String status = gc.getTD_STATUS();
				System.out.println(gc.toString());
				if ((status != null) && (status.equals("0"))) {
					td_status = status;
					gc.setXmbh(xmbh);
					gc.setDJDYID(djdyid);
					List djdys = cdao.getDataList(BDCS_DJDY_GZ.class,
							"DJDYID='" + djdyid + "'");
					if ((djdys != null) && (djdys.size() > 0)) {
						BDCS_DJDY_GZ djdy = (BDCS_DJDY_GZ) djdys.get(0);
						bdcdyh = djdy.getBDCDYH();
						bdcdyid = djdy.getBDCDYID();
						gc.setBDCDYH(bdcdyh);
						gc.setBDCDYID(bdcdyid);
					}
					GX_Util.saveOrUpdate(gc);
					System.out.println("更新一条原有记录");
					GX_Util.gxFlush();
				} else {
					td_status = "1";
				}

			}

		}

		return td_status;
	}
}

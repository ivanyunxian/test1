package com.supermap.realestate.registration.ViewClass;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

public class DJInfoExtend extends DJInfo {
    /** 代理人姓名 */
    private String DLRXM;
    /** 代理人联系电话 */
    private String DLRLXDH;
    /** 权利人2 */
    private String QLR;
    /** 申请人地址 */
    private String SQRDZ;

    public String getQLR() {
	return QLR;
    }

    public void setQLR(String qLR) {
	QLR = qLR;
    }

    public String getDLRXM() {
	return DLRXM;
    }

    public void setDLRXM(String dLRXM) {
	DLRXM = dLRXM;
    }

    public String getDLRLXDH() {
	return DLRLXDH;
    }

    public void setDLRLXDH(String dLRLXDH) {
	DLRLXDH = dLRLXDH;
    }
   
    public String getSQRDZ() {
	return SQRDZ;
    }

    public void setSQRDZ(String sQRDZ) {
	SQRDZ = sQRDZ;
    }

    /**
     * 预告登记审批表
     * 
     * @author DreamLi
     * @time 2015-6-23 20:17
     * 
     */
    public static DJInfoExtend Create(String project_id, CommonDao dao) {
	DJInfoExtend b = new DJInfoExtend();
	if (!StringUtils.isEmpty(project_id)) {
	    String slry = Global.getCurrentUserName();
	    b.setSLRY(slry);
	    List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class,
		    "PROJECT_ID='" + project_id + "'");
	    if (xmxx != null && xmxx.size() > 0) {
		String xmbh = xmxx.get(0).getId();
		List<BDCS_SQR> list = dao.getDataList(BDCS_SQR.class,
			ProjectHelper.GetXMBHCondition(xmbh));
		BDCS_SQR sqr = new BDCS_SQR();
		StringBuilder sqrXmBuilder = new StringBuilder();// 义务人
		StringBuilder qlrXmBuilder = new StringBuilder();// 权利人
		StringBuilder dlrXmBuilder = new StringBuilder();// 代理人
		StringBuilder dlrDhBuilder = new StringBuilder();// 代理人电话
		StringBuilder sqrDhBuilder = new StringBuilder();
		StringBuilder sqrDZ = new StringBuilder();// 申请人地址
		int length = list.size();
		for (int i = 0; i < length; i++) {
		    sqr = list.get(i);
		    if (length - 1 == i) {
			if (ConstValue.SQRLB.JF.Value.equals(sqr.getSQRLB()))// 权利人
			{
			    qlrXmBuilder.append(sqr.getSQRXM());
			    dlrXmBuilder.append(sqr.getDLRXM() != null ? sqr
				    .getDLRXM() : sqr.getSQRXM());
			    dlrDhBuilder.append(sqr.getDLRLXDH() != null ? sqr
				    .getDLRLXDH() : sqr.getLXDH());

			} else// 义务人
			{
			    sqrXmBuilder.append(sqr.getSQRXM());
			}

			sqrDhBuilder.append(sqr.getLXDH());
			sqrDZ.append(sqr.getTXDZ() != null ? sqr.getTXDZ() : "");
		    } else {
			if (ConstValue.SQRLB.JF.Value.equals(sqr.getSQRLB()))// 权利人
			{
			    qlrXmBuilder.append(sqr.getSQRXM()).append(",");
			    dlrXmBuilder.append(
				    sqr.getDLRXM() != null ? sqr.getDLRXM()
					    : sqr.getSQRXM()).append(",");
			    dlrDhBuilder.append(
				    sqr.getDLRLXDH() != null ? sqr.getDLRLXDH()
					    : sqr.getLXDH()).append(",");

			} else// 义务人
			{
			    sqrXmBuilder.append(sqr.getSQRXM()).append(",");
			}

			sqrDhBuilder.append(sqr.getLXDH()).append(",");
			sqrDZ.append(sqr.getTXDZ() != null ? sqr.getTXDZ() : "")
				.append(",");
		    }
		}
		String xm = "";
		if (!StringHelper.isEmpty(qlrXmBuilder.toString())) {
		    xm = "权利人：" + qlrXmBuilder.toString() + "; ";
		}
		if (!StringHelper.isEmpty(sqrXmBuilder.toString())) {
		    xm = xm + "义务人：" + sqrXmBuilder.toString() + ";";
		}

		// 判断是否是以“，”结尾的
		String qlrxmStr = qlrXmBuilder.toString();
		String dlrXmStr = dlrXmBuilder.toString();
		String dlrDhStr = dlrDhBuilder.toString();
		if (qlrxmStr.endsWith(",")) {
		    qlrxmStr = qlrxmStr.substring(0, qlrxmStr.length() - 1);
		}
		if (dlrXmStr.endsWith(",")) {
		    dlrXmStr = dlrXmStr.substring(0, dlrXmStr.length() - 1);
		}
		if (dlrDhStr.endsWith(",")) {
		    dlrDhStr = dlrDhStr.substring(0, dlrDhStr.length() - 1);
		}
		b.setSQR(xm);
		b.setQLR(qlrxmStr);
		b.setLXDH(sqrDhBuilder.toString());
		if (dlrXmBuilder != null) {
		    dlrXmStr = array_unique(dlrXmStr.split(","), ",");
		    b.setDLRXM(dlrXmStr);
		}
		if (dlrDhBuilder != null) {
		    dlrDhStr = array_unique(dlrDhStr.split(","), ",");
		    b.setDLRLXDH(dlrDhStr);
		}

		//申请人地址
		if(sqrDZ != null){
		    b.setSQRDZ(getStringFormArray(sqrDZ.toString().split(",")));
		}
		// 不动产单元类型、不动产坐落
		BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
		if (bdcdylx != null) {
		    b.setBDCLX(bdcdylx.Name);
		}
		StringBuilder builder = new StringBuilder();
		builder.append(" XMBH='").append(xmbh).append("'");
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
			builder.toString());
		if (djdys != null && djdys.size() > 0) {
		    BDCS_DJDY_GZ djdy = djdys.get(0);
		    RealUnit dy = UnitTools.loadUnit(
			    BDCDYLX.initFrom(djdy.getBDCDYLX()),
			    DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
		    if (dy != null) {
			b.setBDCZL(dy.getZL());
		    }

		} else {
		    b.setBDCZL("");
		}
	    }
	}
	return b;
    }
   
    /**
     * 去除数组的重复字段
     * 
     * @作者 欧展榕
     * @创建时间 2015年12月14日上午10:17:22
     * @param a
     * @return
     */
    public static String array_unique(String[] a, String interval) {
	List<String> list = new ArrayList<String>();
	for (int i = 0; i < a.length; i++) {
	    if (!list.contains(a[i])) {
		list.add(a[i]);
	    }
	}
	String result = "";
	String[] results = (String[]) list.toArray(new String[list.size()]);
	for (String s : results) {
	    if ("null".equals(s))
		continue;
	    result += s + interval;
	}
	if (result.endsWith(interval)) {
	    result = result.substring(0, result.length() - 1);
	}

	return result;
    }
    /**
     * 获取数组第一个值
     * @作者 Administrator
     * @创建时间 2015年12月15日下午5:17:46
     * @param arr
     * @return
     */
    public static String getStringFormArray(String[] arr) {
	List<String> lists = new ArrayList<String>();
	for (String str : arr) {
	    if (!"".equals(str)) {
		lists.add(str);
	    }
	}
	if (lists.size() > 0) {
	    return lists.get(0);
	}
	return "";
    }
}

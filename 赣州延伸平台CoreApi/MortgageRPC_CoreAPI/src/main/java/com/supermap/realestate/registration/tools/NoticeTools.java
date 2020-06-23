package com.supermap.realestate.registration.tools;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.CmsArticle;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

public class NoticeTools {

    /**
     * @Description:公告信息通用服务类
     * @author 俞学斌
     * @date 2017年03月15日 10:30:14
     * @Copyright SuperMap
     */
    public static HashMap<String, Object> GetNoticeInfo(String project_id, String noticetype) {
        CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
        HashMap<String, Object> info = new HashMap<String, Object>();
        BDCS_XMXX xmxx = Global.getXMXX(project_id);
        if (xmxx == null) {
            return info;
        }
        if ("cq_cs".equals(noticetype)) {//产权初始登记公告
            List<BDCS_DJDY_GZ> list_djdy = dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmxx.getId() + "'");
            List<Rights> list_rights = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmxx.getId() + "'");
            if (list_rights == null || list_rights.size() <= 0) {
                return info;
            }
            List<HashMap<String, Object>> list_dyinfo = new ArrayList<HashMap<String, Object>>();
            for (Rights rights : list_rights) {
                String qllxmc = QLLX.initFrom(rights.getQLLX()).Name;
                String qllx = QLLX.initFrom(rights.getQLLX()).Value;
                String qlrmc = "";
                String zl = "";
                String bdcdyh = "";
                Double mj = null;
                String yt = "";
                List<RightsHolder> list_rightsholder = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, rights.getId());
                List<String> list_qlrmc = new ArrayList<String>();
                if (list_rightsholder != null && list_rightsholder.size() > 0) {
                    for (RightsHolder rightsholder : list_rightsholder) {
                        list_qlrmc.add(rightsholder.getQLRMC());
                    }
                }
                qlrmc = StringHelper.formatList(list_qlrmc, "、");
                BDCS_DJDY_GZ djdy = null;
                if (list_djdy != null && list_djdy.size() > 0) {
                    for (BDCS_DJDY_GZ djdy_ex : list_djdy) {
                        if (djdy_ex.getDJDYID().equals(rights.getDJDYID())) {
                            djdy = djdy_ex;
                            break;
                        }
                    }
                }
                if (djdy != null) {
                    RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
                    if (unit != null) {
                        zl = unit.getZL();
                        bdcdyh = unit.getBDCDYH();
                        mj = unit.getMJ();
                        if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX())) {
                            House h = (House) unit;
                            yt = h.getGHYTName();
                        } else if (BDCDYLX.LD.Value.equals(djdy.getBDCDYLX())) {
                            Forest forest = (Forest) unit;
                            yt = ConstHelper.getNameByValue("TDYT", forest.getTDYT());
                        } else if (BDCDYLX.NYD.Value.equals(djdy.getBDCDYLX())) {
                            AgriculturalLand nyd = (AgriculturalLand) unit;
                            yt = ConstHelper.getNameByValue("TDYT", nyd.getYT());
                            ;
                        } else if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())) {
                            UseLand useland = (UseLand) unit;
                            if (useland.getTDYTS() != null && useland.getTDYTS().size() > 0) {
                                List<String> tdytmclist = new ArrayList<String>();
                                for (TDYT tdyt : useland.getTDYTS()) {
                                    String ytvalue = tdyt.getTDYT();
                                    if (!StringHelper.isEmpty(ytvalue)) {
                                        String tdytmc = ConstHelper.getNameByValue("TDYT",
                                                ytvalue.trim());
                                        if (!StringHelper.isEmpty(tdytmc)
                                                && !tdytmclist.contains(tdytmc)) {
                                            tdytmclist.add(tdytmc);
                                        }
                                    }
                                }
                                if (tdytmclist != null && tdytmclist.size() > 0) {
                                    yt = StringHelper.formatList(tdytmclist, "、");
                                }
                            }
                        }
                    }
                }
                HashMap<String, Object> dyinfo = new HashMap<String, Object>();
                dyinfo.put("QLRMC", qlrmc);
                dyinfo.put("QLLXMC", qllxmc);
                dyinfo.put("QLLX", qllx);
                dyinfo.put("ZL", zl);
                dyinfo.put("BDCDYH", bdcdyh);
                dyinfo.put("MJ", mj);
                dyinfo.put("YT", yt);
                list_dyinfo.add(dyinfo);
            }
            info.put("DYINFOLIST", list_dyinfo);
        } else if ("cq_gz".equals(noticetype)) {//产权更正登记公告
            List<BDCS_DJDY_GZ> list_djdy = dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmxx.getId() + "'");
            List<Rights> list_rights = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmxx.getId() + "'");
            if (list_rights == null || list_rights.size() <= 0) {
                return info;
            }
            List<HashMap<String, Object>> list_dyinfo = new ArrayList<HashMap<String, Object>>();
            for (Rights rights : list_rights) {
                String zl = "";
                String fj = rights.getFJ();
                String djyy = rights.getDJYY();
                BDCS_DJDY_GZ djdy = null;
                if (list_djdy != null && list_djdy.size() > 0) {
                    for (BDCS_DJDY_GZ djdy_ex : list_djdy) {
                        if (djdy_ex.getDJDYID().equals(rights.getDJDYID())) {
                            djdy = djdy_ex;
                            break;
                        }
                    }
                }
                if (djdy != null) {
                    RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
                    if (unit != null) {
                        zl = unit.getZL();
                    }
                }
                HashMap<String, Object> dyinfo = new HashMap<String, Object>();
                dyinfo.put("ZL", zl);
                dyinfo.put("FJ", fj);
                dyinfo.put("DJYY", djyy);
                list_dyinfo.add(dyinfo);
            }
            info.put("DYINFOLIST", list_dyinfo);
        }
        return info;
    }

    /**
     * 生成公告实体对象
     *
     * @param project_id
     * @param type
     * @param noticeHtml
     * @return
     * @author 余国伟
     */
    public static CmsArticle createNotice(String project_id, String type, String noticeHtml) {
        CmsArticle notice = new CmsArticle();
        notice.setId(UUID.randomUUID().toString().replace("-", ""));
		noticeHtml = "<div class='inline-notice'><style>.inline-notice td{ padding:5px 10px; }</style>" + noticeHtml;
		noticeHtml += "</div>";
		notice.setContent(noticeHtml);
        Map<String, Object> noticeInfo = GetNoticeInfo(project_id, type);
        List<HashMap<String, Object>> info = (List<HashMap<String, Object>>) noticeInfo.get("DYINFOLIST");
        if (info != null && info.size() > 0) {
            notice.setLocate(String.valueOf(info.get(0).get("ZL")));
            notice.setUsefor(String.valueOf(info.get(0).get("YT")));
            notice.setQlr(String.valueOf(info.get(0).get("QLRMC")));
            notice.setRightType(String.valueOf(info.get(0).get("QLLX")));
            notice.setRightTypeName(String.valueOf(info.get(0).get("QLLXMC")));
            notice.setEstateNo(String.valueOf(info.get(0).get("BDCDYH")));
        }
        notice.setNoticeContent(JSON.toJSONString(info));
        BDCS_XMXX project = Global.getXMXX(project_id);
        if (project != null) {
            notice.setYwlsh(project.getYWLSH());
            notice.setProjectName(project.getXMMC());
            notice.setSlsj(new java.sql.Date(project.getSLSJ().getTime()));
        }
        notice.setPublishat(System.currentTimeMillis() / 1000);
        return createCmsArticle(type, notice);
    }

    private static CmsArticle createCmsArticle(String type, CmsArticle notice) {
        if ("cq_cs".equals(type)) {
            notice.setChannelid("scgg");
            notice.setNoticeType("scgg");
            notice.setNoticeTypeName("首次公告");
            notice.setTitle("不动产首次登记公告");
        } else if ("cq_gz".equals(type)) {
            notice.setChannelid("gzgg");
            notice.setNoticeType("gzgg");
            notice.setNoticeTypeName("更正公告");
            notice.setTitle("不动产更正登记公告");
        }
        return notice;
    }
}

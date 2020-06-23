package com.supermap.realestate.registration.service.impl;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.CmsArticle;
import com.supermap.realestate.registration.model.MaxNoticeSn;
import com.supermap.realestate.registration.service.InlineService;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.synchroinline.util.DButil;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import oracle.sql.CLOB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * ClassName : InlineServiceImpl
 * <p>
 * Description :
 * </p>
 *
 * @author YuGuowei
 * @date 2017-03-20 14:07
 **/
@Service
public class InlineServiceImpl implements InlineService {

    @Autowired
    private CommonDao dao;

    @Autowired
    private SmActInst smActInst;

    @Autowired
    private SmHoliday smHoliday;

    @Autowired
    private SmProInst smProInst;

    @Autowired
    private SmProInstService smProInstService;

    @Autowired
    private OperationService operationService;

    @Override
    public boolean sendNotice(CmsArticle notice, Map<String, String> params) throws Exception {
        boolean sendSuccess = false;
        String project_id = params.get("project_id");
        Wfi_ProInst proinst = smProInst.GetProInstByFileNumber(project_id);
        Wfi_ActInst actinst = smProInstService.GetNewActInst(proinst.getProinst_Id());
        Wfd_Actdef def = smActInst.GetActDef(actinst.getActinst_Id());
        Date lockDate = new Date();
        notice.setNoticeStart(new java.sql.Date(lockDate.getTime()));
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(lockDate);
        Date unLockDate = smHoliday.addDateByWorkDay(cal, Integer.parseInt(def.getLockTime() == null ? "0" : def.getLockTime()));
        notice.setNoticeEnd(new java.sql.Date(unLockDate.getTime()));
        //保存公告信息
        boolean isSuccess = this.saveNotice(notice);
        if (isSuccess) {
            try {
                //锁定项目
                operationService.delLockProject(actinst.getActinst_Id(), params.get("msg"), params.get("lockType"));
                //更新项目信息
                BDCS_XMXX project = Global.getXMXX(project_id);
                project.setSFFBGG("1");
                this.dao.update(project);
                this.dao.flush();
                sendSuccess = true;
            } catch (Exception e) {
                this.deleteNotice(notice);
                throw e;
            }
        }
        return sendSuccess;
    }

    @Override
    public boolean saveNotice(CmsArticle notice) throws Exception {
        boolean isSuccess = false;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DButil.getConnection();
            if (connection == null) {
                throw new Exception("获取连接失败！");
            }
            String sql = "INSERT INTO CMS_ARTICLE(ID, SHOPID, TITLE, INFO, AUTHOR, LOCATE, USEFOR, QLR, FBJG, " +
                    "PICURL, CONTENT, DISABLED, PUBLISHAT, CHANNELID, OPBY, OPAT, DELFLAG, NOTICENUMBER, NOTICESTART, " +
                    "NOTICEEND, MASTERADDRESS, RIGHTTYPE, RIGHTTYPENAME, ESTATENO, AREA, CORRECTCONTENT, DISSENTPSN, PROVENO, CTATEPRONO, " +
                    "REMARK, NOTICETYPE, NOTICETYPENAME, LANDSEANO, ISSYNCHRO, LOCATION, YWLSH, PROJECTNAME, SLSJ, NOTICECONTENT) VALUES (?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            CLOB content = CLOB.createTemporary(connection, false, CLOB.DURATION_SESSION);
            content.setString(1, notice.getContent());
            statement.setClob(11, content);
            CLOB noticeContent = CLOB.createTemporary(connection, false, CLOB.DURATION_SESSION);
            noticeContent.setString(1, notice.getNoticeContent());
            statement.setClob(39, noticeContent);
            setStatementInfo(notice, statement);
            statement.executeUpdate();
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DButil.close(null, statement, connection);
        }
        return isSuccess;
    }

    private void setStatementInfo(CmsArticle notice, PreparedStatement statement) throws SQLException {
        statement.setString(1, notice.getId());
        statement.setString(2, notice.getShopId());
        statement.setString(3, notice.getTitle());
        statement.setString(4, notice.getInfo());
        statement.setString(5, notice.getAuthor());
        statement.setString(6, notice.getLocate());
        statement.setString(7, notice.getUsefor());
        statement.setString(8, notice.getQlr());
        statement.setString(9, notice.getFbjg());

        statement.setString(10, notice.getPicurl());
        statement.setString(12, notice.getDisabled());
        statement.setLong(13, notice.getPublishat());
        statement.setString(14, notice.getChannelid());
        statement.setString(15, notice.getOpby());
        statement.setLong(16, notice.getOpat());
        statement.setString(17, notice.getDelflag());
        statement.setString(18, notice.getNoticeNumber());
        statement.setDate(19, notice.getNoticeStart());
        statement.setDate(20, notice.getNoticeEnd());

        statement.setString(21, notice.getMasterAddress());
        statement.setString(22, notice.getRightType());
        statement.setString(23, notice.getRightTypeName());
        statement.setString(24, notice.getEstateNo());
        statement.setLong(25, notice.getArea());
        statement.setString(26, notice.getCorrectContent());
        statement.setString(27, notice.getDissentpsn());
        statement.setString(28, notice.getProveno());
        statement.setString(29, notice.getCtateProno());
        statement.setString(30, notice.getRemark());

        statement.setString(31, notice.getNoticeType());
        statement.setString(32, notice.getNoticeTypeName());
        statement.setString(33, notice.getLandSeano());
        statement.setLong(34, notice.getIsSynchro());
        statement.setLong(35, notice.getLocation());
        statement.setString(36, notice.getYwlsh());
        statement.setString(37, notice.getProjectName());
        statement.setDate(38, notice.getSlsj());
    }

    @Override
    public Integer deleteNotice(CmsArticle notice) throws Exception {
        Integer result = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DButil.getConnection();
            statement = connection.prepareStatement("DELETE FROM CMS_ARTICLE WHERE ID=?");
            statement.setString(1, notice.getId());
            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DButil.close(null, statement, connection);
        }
        return result;
    }

    public Map<String, Integer> getLockTime(String prolsh) {
        Map<String, Integer> dates = new HashMap<String, Integer>();
        Wfi_ProInst proinst = smProInst.GetProInstByFileNumber(prolsh);
        Wfi_ActInst actinst = smProInstService.GetNewActInst(proinst.getProinst_Id());
        Wfd_Actdef def = smActInst.GetActDef(actinst.getActinst_Id());
        GregorianCalendar cal = new GregorianCalendar();
        Date lockDate = new Date();
        cal.setTime(lockDate);
        dates.put("lockYear", cal.get(Calendar.YEAR));
        dates.put("lockMonth", cal.get(Calendar.MONTH) + 1);
        dates.put("lockDay", cal.get(Calendar.DAY_OF_MONTH));
        Integer lockTime = Integer.parseInt(def.getLockTime() == null ? "0" : def.getLockTime());
        dates.put("lockTime", lockTime);
        Date unLockDate = smHoliday.addDateByWorkDay(cal, lockTime);
        cal.setTime(unLockDate);
        dates.put("unLockYear", cal.get(Calendar.YEAR));
        dates.put("unLockMonth", cal.get(Calendar.MONTH) + 1);
        dates.put("unLockDay", cal.get(Calendar.DAY_OF_MONTH));
        return dates;
    }

    public String createNoticeSn(String year, String xzqhdm) {
        String maxSn = "0";
        MaxNoticeSn maxNoticeSn;
        List<MaxNoticeSn> maxNoticeSns = this.dao.getDataList(MaxNoticeSn.class, "ND='" + year + "'");
        if (maxNoticeSns != null && maxNoticeSns.size() > 0) {
            maxNoticeSn = maxNoticeSns.get(0);
            maxSn = String.valueOf(maxNoticeSn.getXh());
        } else {
            maxNoticeSn = new MaxNoticeSn();
            maxNoticeSn.setBsm(System.currentTimeMillis());
            maxNoticeSn.setNd(year);
            maxNoticeSn.setXzqhdm(xzqhdm);
        }
        int length = 4;
        maxSn = StringHelper.getCountByParamter(maxSn, length, 1);
        maxNoticeSn.setXh(Integer.parseInt(maxSn));
        this.dao.saveOrUpdate(maxNoticeSn);
        return maxSn;
    }

    public String getAreaCodeByProjectID(String project_id) {
        String code = "";
        if (project_id != null) {
            if (project_id.contains("-")) {
                String[] strs = project_id.split("-");
                if (strs != null && strs.length > 0) {
                    code = strs[1];
                }
            }
        }
        return code;
    }
}

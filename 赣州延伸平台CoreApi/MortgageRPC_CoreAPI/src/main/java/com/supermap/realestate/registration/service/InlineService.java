package com.supermap.realestate.registration.service;

import com.supermap.realestate.registration.model.CmsArticle;

import java.util.Map;

/**
 * ClassName : InlineService
 * <p>
 * Description : 在线受理相关
 * </p>
 *
 * @author YuGuowei
 * @date 2017-03-20 9:47
 **/
public interface InlineService {

    /**
     * 发布公告
     * @return
     */
    boolean sendNotice(CmsArticle notice, Map<String, String> params) throws Exception;

    /**
     * 向数据库中插入公告信息
     * @param notice
     * @return
     * @throws Exception
     */
    boolean saveNotice(CmsArticle notice) throws Exception;

    /**
     * 删除公告记录信息
     * @param notice
     * @return
     * @throws Exception
     */
    Integer deleteNotice(CmsArticle notice) throws Exception;

    /**
     * 获取项目锁定和解锁时间
     * @param prolsh
     * @return
     */
    Map<String, Integer> getLockTime(String prolsh);

    /**
     * 生成公告编号
     * @return
     */
    String createNoticeSn(String year, String xzqhdm);

    /**
     * 从项目编号中提取行政区划代码
     * @param project_id
     * @return
     */
    String getAreaCodeByProjectID(String project_id);
}

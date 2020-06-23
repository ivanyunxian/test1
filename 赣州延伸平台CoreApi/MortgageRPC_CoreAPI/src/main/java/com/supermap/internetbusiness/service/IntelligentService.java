package com.supermap.internetbusiness.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author NOTEBAO
 * @date 2019年3月4日10:56:22
 * 智能审批实现接口
 */
public interface IntelligentService {

    /**
     * 创建智能审批报告
     * @return
     * @param req
     */
    Map<String, Object> createReport(String file_number) ;

    /**
     *
     * @param req
     * @param rep
     */

    void getQrcode(HttpServletRequest req, HttpServletResponse rep) throws IOException;

    /**
     * 获取智能审批报告结果内容
     */
    Map<String, Object> getReport(HttpServletRequest req) ;
}

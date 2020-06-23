package com.supermap.realestate.registration.service;

import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 短信服务Controller
 * @ClassName: SmsManagerService
 * @author taochunda
 * @date 2019年05月15日 22:34:33
 */
public interface SmsManagerService {

    /**
     * 新增短信模板
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public ResultMessage AddTemplate(HttpServletRequest request) throws UnsupportedEncodingException;

    /**
     * 编辑短信模板
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public ResultMessage UpdateTemplate(HttpServletRequest request) throws UnsupportedEncodingException;

    /**
     * 删除短信模板
     * @param templateid
     * @param request
     * @return
     */
    public ResultMessage RemoveTemplate(String templateid, HttpServletRequest request);

    /**
     * 分页获取短信模板
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public Message GetSmsTempletList(HttpServletRequest request) throws UnsupportedEncodingException;

    /**
     * 获取短信推送记录列表
     * @param xmbh
     * @param request
     * @return
     */
    public Message GetSmsPushList(String xmbh, HttpServletRequest request);

    /**
     * 短信推送
     * @param xmbh
     * @param request
     * @return
     */
    public Message SmsPush(String xmbh, HttpServletRequest request) throws UnsupportedEncodingException;

    /**
     * 短信推送
     * @param request
     * @return
     */
    public JSONObject sendSms(HttpServletRequest request);

    @SuppressWarnings("rawtypes")
    public Message GetSmsPushList(HttpServletRequest request) throws UnsupportedEncodingException;

    @SuppressWarnings("rawtypes")
    public JSONObject GetSmsPushList_HZ(HttpServletRequest request) throws UnsupportedEncodingException;
}

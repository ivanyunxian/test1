package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.CmsArticle;
import com.supermap.realestate.registration.service.InlineService;
import com.supermap.realestate.registration.tools.NoticeTools;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.web.ResultMessage;
import org.apache.tools.ant.taskdefs.ManifestTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ClassName : Inline
 * <p>
 * Description : 不动产在线受理系统相关
 * </p>
 *
 * @author YuGuowei
 * @date 2017-03-17 17:15
 **/
@Controller
@RequestMapping("/inline")
public class InlineController {

    private final String prefix = "/realestate/registration/inline/";

    @Autowired
    private InlineService inlineService;

    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    public String notice(){
        return prefix + "notice";
    }

    @RequestMapping(value = "/sendNotice", method = RequestMethod.POST)
    @ResponseBody
    public ResultMessage sendNotice(HttpServletRequest request){
        ResultMessage message = new ResultMessage();
        try {
            Map<String, String> params = RequestHelper.getParameterMap(request);
            CmsArticle notice = NoticeTools.createNotice(params.get("project_id"), params.get("noticeType"), params.get("noticeHtml"));
            boolean isSuccess = this.inlineService.sendNotice(notice, params);
            message.setSuccess(String.valueOf(isSuccess));
            message.setMsg("发布公告成功！");
        } catch (Exception e) {
            message.setSuccess("false");
            message.setMsg("发布公告失败，详情：" + e.getMessage());
        }
        return message;
    }
}

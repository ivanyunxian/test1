package com.supermap.wisdombusiness.synchroinline.web;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.synchroinline.model.DicInfo;
import com.supermap.wisdombusiness.synchroinline.model.JsonMessage;
import com.supermap.wisdombusiness.synchroinline.model.PageRole;
import com.supermap.wisdombusiness.synchroinline.model.Pro_actdef;
import com.supermap.wisdombusiness.synchroinline.service.InlineRoleService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LeiTing on 2017/7/7.
 */
@Controller
@RequestMapping(value = "/inlinerole")
public class InlineRoleController {

    @Autowired
    InlineRoleService inlineRoleService;

    @Autowired
    private SmStaff smStaff;

    //跳转地址
    private final String prefix = "/realestate/registration/inline/";

    /**
     * 获取当前登陆人员，允许办理的业务流程list
     */
    @RequestMapping(value = "/queryStaffProdefId")
    public @ResponseBody
    JsonMessage queryStaffProdefId(HttpServletRequest request, HttpServletResponse response)
    {
        JsonMessage result = new JsonMessage();
        try
        {
            String userid = smStaff.getCurrentWorkStaffID();
            result.setData(inlineRoleService.QueryStaffProdefId(userid));
            result.setState(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result.setState(false);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/getrolelist")
    public @ResponseBody
    JsonMessage getRoleList(HttpServletRequest request, HttpServletResponse response) {
        Integer page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        Integer rows = 10;
        if (request.getParameter("rows") != null) {
            rows = Integer.parseInt(request.getParameter("rows"));
        }
        JsonMessage result = new JsonMessage();
        try
        {
            result.setData(inlineRoleService.findAll());
            result.setState(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result.setState(false);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/queryStaffRole")
    public @ResponseBody
    JsonMessage queryStaffRole ()
    {
        JsonMessage result = new JsonMessage();
        try
        {
            String userid = smStaff.getCurrentWorkStaffID();
            result.setData(inlineRoleService.QueryStaffProdefId(userid));
            result.setState(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result.setState(false);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    /**
     * 查询所有流程
     * 当PRO_PRODEF表的roleid字段为'0'时，才查询到
     */
    @RequestMapping(value = "/queryAllProdef")
    public @ResponseBody
    PageRole queryAllProdef ()
    {
        PageRole result = new PageRole();
        try
        {
            result.setRows(inlineRoleService.findAllProdef());
            //TODO :设置查询总数，设置分页查询
            result.setTotal(10);
            result.setState(true);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result.setState(false);
            result.setMsg(ex.getMessage());
        }
        return result;
    }


    /**
     * 编辑角色权限，流程关联角色
     * @param model
     * @return
     */
    @RequestMapping(value = "/rolemanage")
    public String editRole(HttpServletRequest req, HttpServletResponse response,Model model)
    {
        //List<Map> roles= inlineRoleService.findAll();
        //model.addAttribute("roles",roles);
        String isAble = inlineRoleService.getIsAble();
        model.addAttribute("isAble",isAble);
        return "/realestate/registration/inline/Role_manage";
    }

    /**
     * 跳转编辑页面
     *
     * @param model
     * @author
     * @return
     */
    @RequestMapping(value = "/role/edit", method = RequestMethod.GET)
    public String ShowEditPage(Model model, HttpServletRequest request,
                               HttpServletResponse response) {
        Pro_actdef role = new Pro_actdef();
        String id = request.getParameter("id");
        String url = request.getParameter("url");
        String method = "post";
        if (!StringUtils.isEmpty(id)) {
            role = inlineRoleService.findById(id);
            method = "put";
        }
        model.addAttribute("roleattribute", role);
        model.addAttribute("url", url);
        model.addAttribute("method", method);
        return prefix + "Role_edit";
    }

    /**
     * 提交并保存新的流程
     * */
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage saveRole(
            @ModelAttribute("roleattribute") Pro_actdef role, BindingResult result,
            SessionStatus status, HttpServletRequest request,
            HttpServletResponse response) {
        ResultMessage msg = new ResultMessage();
        if (result.hasErrors()) {
            //logger.error(result);
            msg.setMsg(result.toString());
        } else {
            try {
                if(role.getId() == null || role.getId().length() < 1){
                    role.setId(String.valueOf(System.currentTimeMillis()));
                }
                if(role.getRoleid()==null || role.getRoleid().length()<1){
                    role.setRoleid("0");
                }
                inlineRoleService.saveProdef(role);
                status.setComplete();
                msg.setMsg(role.getId());
                msg.setSuccess("true");
                YwLogUtil.addYwLog("添加在线受理流程角色", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
            } catch (Exception e) {
                //logger.error(e);
                msg.setMsg(e.getMessage());
                YwLogUtil.addYwLog("添加在线受理流程角色", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
            }
        }
        return msg;
    }

    /**
     * 更新流程
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public @ResponseBody ResultMessage updateRole(
            @PathVariable("id") String id, @ModelAttribute("roleattribute") Pro_actdef role, BindingResult result,
            SessionStatus status, HttpServletRequest request,
            HttpServletResponse response) {
        ResultMessage msg = new ResultMessage();
        if (result.hasErrors()) {
            //logger.error(result);
            msg.setMsg(result.toString());
        } else {
            try {
                role.setId(id);
                inlineRoleService.updateProdef(role);
                msg.setMsg(id);
                msg.setSuccess("true");
                YwLogUtil.addYwLog("更新在线受理流程角色", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
            } catch (Exception e) {
                //logger.error(e);
                msg.setMsg(e.getMessage());
                YwLogUtil.addYwLog("更新在线受理流程角色", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
            }
        }
        return msg;
    }


    /**
     * 删除流程
     *
     * */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public @ResponseBody ResultMessage deleteRole(
            @PathVariable("id") String id, HttpServletRequest request,
            HttpServletResponse response) {
        ResultMessage msg = new ResultMessage();
        if (StringUtils.hasLength(id)) {
            try {
                inlineRoleService.deleteProdefByActId(id);
                msg.setSuccess("true");
                YwLogUtil.addYwLog("删除在线受理流程角色", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
            } catch (Exception e) {
                //logger.error(e);
                msg.setMsg(e.getMessage());
                YwLogUtil.addYwLog("删除在线受理流程角色", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
            }
        } else {
            msg.setMsg("无该流程");
        }
        return msg;
    }

    /**
     * 设置角色tree
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/role/setRoles")
    public @ResponseBody List<Tree> setRoles(HttpServletRequest request,
                                             HttpServletResponse response) {
        List<Tree> list = inlineRoleService.setRoleTree(request.getParameter("id"));
        return list;
    }

    /**
     * 保存流程角色信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/role/saveRole", method = RequestMethod.POST)
    public @ResponseBody ResultMessage saveUserRole(HttpServletRequest request,
                                                    HttpServletResponse response) {
        ResultMessage msg = new ResultMessage();
        String prodefId = request.getParameter("prodefId");
        String shzt_tmp = request.getParameter("shzt");
        String roleIds = request.getParameter("roleIds");
        if (roleIds != null && !roleIds.equals(""))
            roleIds.substring(0, roleIds.length() - 1);
        //recordsMaintian.MaintainStaffRole(userId, userService.CompareDiffRoleInStaff(userId, roleIds));
        Integer shzt = Integer.parseInt(shzt_tmp);
        try {

            //删除当前流程、当前审核环节所有数据;
            inlineRoleService.deleteProdef(prodefId,shzt);
            //添加新的流程
            inlineRoleService.saveProdefs(prodefId, shzt, roleIds);

            msg.setSuccess("true");
            YwLogUtil.addYwLog("保存用户角色-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
        } catch (Exception e) {
            //logger.error(e);
            msg.setMsg(e.getMessage());
            YwLogUtil.addYwLog("保存用户角色-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
        }
        return msg;
    }

    //设置全局变量
    @RequestMapping(value = "/role/enable", method = RequestMethod.POST)
    public @ResponseBody JsonMessage roleEnable(HttpServletRequest request,
                                                    HttpServletResponse response) {
        JsonMessage msg = new JsonMessage();
        String isAble = request.getParameter("IsAble");
        try {
            inlineRoleService.setIsAble(isAble);
            msg.setData(inlineRoleService.getIsAble());
            msg.setState(true);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setState(false);
            msg.setMsg(e.getMessage());
        }
        return msg;
    }

}

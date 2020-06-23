package org.jeecg.modules.mortgagerpc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zrz;
import org.jeecg.modules.mortgagerpc.model.BdcZrzTreeModel;
import org.jeecg.modules.mortgagerpc.service.IBdc_shyqzdService;
import org.jeecg.modules.mortgagerpc.service.IBdc_zrzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags="宗地信息表")
@RestController
@RequestMapping("/modules/yspt/bdc_zrz")
public class Bdc_zrzController {
	
	@Autowired
	private IBdc_zrzService bdc_zrzService;
	
	@Autowired
	private IBdc_shyqzdService Bdc_shyqzdService;
	
	/**
     * 查询数据 查出用户所属的所有自然幢,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryZrzByUserTreeList", method = RequestMethod.GET)
    public Result<List<BdcZrzTreeModel>> queryZrzByUserTreeList(HttpServletRequest request) {
        Result<List<BdcZrzTreeModel>> result = new Result<>();
		// 获取登录用户信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        try {
            List<BdcZrzTreeModel> list = new ArrayList<>();
            //首先查询出第一级 宗地信息 并转化为model
            if(StringHelper.isEmpty(sysUser.getEnterpriseId())) {
            	result.setSuccess(false);
    			result.setMessage("该用户没有关联企业");
    			return result;
            }
            List<Bdc_shyqzd> zdlist = Bdc_shyqzdService.queryByEnterpriseId(sysUser.getEnterpriseId());
            list = bdc_zrzService.queryZrzById(zdlist);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(e.toString());
            log.error(e.getMessage(),e);
        }
        return result;
    }

}

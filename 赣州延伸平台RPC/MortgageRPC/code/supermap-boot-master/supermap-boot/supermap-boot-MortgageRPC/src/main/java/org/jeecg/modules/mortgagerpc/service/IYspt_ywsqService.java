package org.jeecg.modules.mortgagerpc.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.modules.system.entity.YsptEnterprise;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IYspt_ywsqService extends IService<YsptEnterprise>{

	void submitProject(String enterpriseid, String type);

	IPage<Map> projectlist(Page<Map> page, HttpServletRequest req);

	JSONObject searchBdcdyh(String bdcdyh);

	void houseSearch(Map<String, String> param);

	void selectzd(JSONObject zdsdata);

	void removehouse(String zdid, String enterpriseid);

}

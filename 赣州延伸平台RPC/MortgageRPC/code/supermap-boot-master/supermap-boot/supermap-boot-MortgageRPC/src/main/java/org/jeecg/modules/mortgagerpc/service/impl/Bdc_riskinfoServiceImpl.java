package org.jeecg.modules.mortgagerpc.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.modules.mapper.Bdc_riskinfoMapper;
import org.jeecg.modules.mapper.Wfi_proinstMapper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_riskinfo;
import org.jeecg.modules.mortgagerpc.service.IBdc_riskinfoService;
import org.jeecg.modules.mortgagerpc.service.ISys_configService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 风险防控
 * @Author: jeecg-boot
 * @Date:   2019-09-28
 * @Version: V1.0
 */
@Service
public class Bdc_riskinfoServiceImpl extends ServiceImpl<Bdc_riskinfoMapper, Bdc_riskinfo> implements IBdc_riskinfoService {

    @Autowired
    ISys_configService sys_configService;

    @Autowired
    Wfi_proinstMapper proinstMapper;

    @Override
    public JSONObject getLimitUnitInfo(JSONObject requestparam) throws ParseException {

        String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
        if(StringHelper.isEmpty(coreQueryUrl)) {
            throw new SupermapBootException("未配置进度查询接口地址，请联系管理员进行配置");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, String> userDepart = proinstMapper.getUserDepart(sysUser.getDeptId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String numbers = RandomUtil.randomNumbers(6);
        String timeflag = format + numbers;
        JSONObject object = new JSONObject();
        object.put("requestcode", ConstValueMrpc.RequestcodeEnum.DYXZ.Value);
        object.put("requestseq", timeflag);
        JSONArray dataArray = new JSONArray();
        Map<String, Object> map = new HashMap<>();
        map.put("qlrmc", userDepart.get("NAME"));
        map.put("zjh", userDepart.get("ZJH"));
        map.put("bdcqzh", requestparam.getString("bdcqzh"));
        map.put("dyr", requestparam.getString("dyr"));
        if(!StringHelper.isEmpty(requestparam.getString("slsj_q"))) {
            map.put("slsj_q", requestparam.getString("slsj_q"));
            map.put("slsj_z", requestparam.getString("slsj_z"));
        }

        map.put("currentpage", requestparam.getString("currentpage"));
        map.put("pageSize",  requestparam.getString("pageSize"));
        dataArray.add(map);
        object.put("data", dataArray);

        String jsonstr = HttpClientUtil.requestPost( object.toJSONString(), coreQueryUrl);
        if(jsonstr == null) {
            throw new SupermapBootException("接口返回数据为空，请检查单元限制查询接口是否异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        return jsonObject;
    }

	@Override
	public JSONObject getUnitStatusInfo(JSONObject requestparam) {
		String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
        if(StringHelper.isEmpty(coreQueryUrl)) {
            throw new SupermapBootException("未配置单元状态接口地址，请联系管理员进行配置");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, String> userDepart = proinstMapper.getUserDepart(sysUser.getDeptId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String numbers = RandomUtil.randomNumbers(6);
        String timeflag = format + numbers;
        // 请求JSON数据组装
        JSONObject object = new JSONObject();
        object.put("requestcode", ConstValueMrpc.RequestcodeEnum.DYZT.Value);
        object.put("requestseq", timeflag);
        JSONArray dataArray = new JSONArray();
        Map<String, Object> map = new HashMap<>();
        map.put("bdcqzh", requestparam.getString("bdcqzh"));
        map.put("bdcdylx", requestparam.getString("bdcdylx"));
        map.put("currentpage", requestparam.getString("currentpage"));
        map.put("pageSize",  requestparam.getString("pageSize"));
        // 内部调用，使用机构代码为90
        dataArray.add(map);
        object.put("data", dataArray);

        String jsonstr = HttpClientUtil.requestPost(object.toJSONString(), coreQueryUrl);

        if(StringHelper.isEmpty(jsonstr)) {
            throw new SupermapBootException("单元状态接口暂时无法访问，请联系管理员排查。");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        if(jsonObject == null) {
            throw new SupermapBootException("单元状态接口异常，返回为空，请联系管理员排查");
        }
		return jsonObject;
	}
}

package org.jeecg.modules.mortgagerpc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.mortgagerpc.entity.Bdc_riskinfo;

import java.text.ParseException;

/**
 * @Description: 风险防控
 * @Author: jeecg-boot
 * @Date:   2019-09-28
 * @Version: V1.0
 */
public interface IBdc_riskinfoService extends IService<Bdc_riskinfo> {

    JSONObject getLimitUnitInfo(JSONObject requestparam) throws ParseException;

	JSONObject getUnitStatusInfo(JSONObject requestparam);
}

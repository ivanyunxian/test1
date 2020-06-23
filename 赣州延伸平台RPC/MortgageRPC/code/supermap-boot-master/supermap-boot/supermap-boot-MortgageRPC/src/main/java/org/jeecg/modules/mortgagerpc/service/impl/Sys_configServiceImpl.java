package org.jeecg.modules.mortgagerpc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.mapper.Sys_configMapper;
import org.jeecg.modules.mortgagerpc.entity.Sys_config;
import org.jeecg.modules.mortgagerpc.service.ISys_configService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description: 系统参数
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Service
public class Sys_configServiceImpl extends ServiceImpl<Sys_configMapper, Sys_config> implements ISys_configService {

    @Override
    @Cacheable(value = CacheConstant.SYS_CONFIG_CACHE,key = "#key")
    public String getConfigByKey(String key) {
        Sys_config sysconfig = getOne(new QueryWrapper<Sys_config>().eq("CONFIG_KEY", key));
        if(sysconfig != null) {
            return sysconfig.getValue();
        }
        return "";
    }
}

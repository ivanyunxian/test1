package org.jeecg.modules.mortgagerpc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.mortgagerpc.entity.Sys_config;

/**
 * @Description: 系统参数
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
public interface ISys_configService extends IService<Sys_config> {

    public String getConfigByKey(String key);

}

package org.jeecg.modules.mortgagerpc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.mortgagerpc.entity.Bdc_fsql;

import java.util.List;

/**
 * @Description: 附属权利表
 * @Author: jeecg-boot
 * @Date:   2019-08-28
 * @Version: V1.0
 */
public interface IBdc_fsqlService extends IService<Bdc_fsql> {

    List<Bdc_fsql> getFsqlBydyid(String prolsh, String dyid);

}

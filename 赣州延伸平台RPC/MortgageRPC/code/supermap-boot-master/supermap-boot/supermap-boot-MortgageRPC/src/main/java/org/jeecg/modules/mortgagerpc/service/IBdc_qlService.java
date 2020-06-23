package org.jeecg.modules.mortgagerpc.service;

import org.jeecg.modules.mortgagerpc.entity.Bdc_ql;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 权利相关
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IBdc_qlService extends IService<Bdc_ql> {

    List<Bdc_ql> getQlBydyid(String prolsh,String dyid);

}

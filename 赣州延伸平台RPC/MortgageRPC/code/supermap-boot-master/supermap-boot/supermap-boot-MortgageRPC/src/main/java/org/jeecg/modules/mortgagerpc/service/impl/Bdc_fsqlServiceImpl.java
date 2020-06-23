package org.jeecg.modules.mortgagerpc.service.impl;

import org.jeecg.modules.mapper.Bdc_fsqlMapper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_fsql;
import org.jeecg.modules.mortgagerpc.service.IBdc_fsqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 附属权利表
 * @Author: jeecg-boot
 * @Date:   2019-08-28
 * @Version: V1.0
 */
@Service
public class Bdc_fsqlServiceImpl extends ServiceImpl<Bdc_fsqlMapper, Bdc_fsql> implements IBdc_fsqlService {

    @Autowired
    Bdc_fsqlMapper bdc_fsqlMapper;

    @Override
    public List<Bdc_fsql> getFsqlBydyid(String prolsh, String dyid) {
        return bdc_fsqlMapper.getFsqlBydyid(prolsh, dyid);
    }
}

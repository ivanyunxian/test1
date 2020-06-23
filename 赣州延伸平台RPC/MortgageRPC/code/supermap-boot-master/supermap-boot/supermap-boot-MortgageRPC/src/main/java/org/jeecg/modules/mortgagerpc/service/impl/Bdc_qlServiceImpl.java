package org.jeecg.modules.mortgagerpc.service.impl;

import org.jeecg.modules.mortgagerpc.entity.Bdc_ql;
import org.jeecg.modules.mapper.Bdc_qlMapper;
import org.jeecg.modules.mortgagerpc.service.IBdc_qlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 权利相关
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class Bdc_qlServiceImpl extends ServiceImpl<Bdc_qlMapper, Bdc_ql> implements IBdc_qlService {

    @Autowired
    Bdc_qlMapper bdc_qlMapper;


    @Override
    public List<Bdc_ql> getQlBydyid(String prolsh, String dyid) {
        return bdc_qlMapper.getQlBydyid(prolsh, dyid);
    }
}

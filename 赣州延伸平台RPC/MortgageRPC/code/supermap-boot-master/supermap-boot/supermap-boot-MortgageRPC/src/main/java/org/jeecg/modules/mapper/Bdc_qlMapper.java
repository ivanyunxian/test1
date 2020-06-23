package org.jeecg.modules.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.mortgagerpc.entity.Bdc_ql;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 权利相关
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface Bdc_qlMapper extends BaseMapper<Bdc_ql> {

    List<Bdc_ql> getQlBydyid(@Param("prolsh") String prolsh, @Param("dyid") String dyid);
}

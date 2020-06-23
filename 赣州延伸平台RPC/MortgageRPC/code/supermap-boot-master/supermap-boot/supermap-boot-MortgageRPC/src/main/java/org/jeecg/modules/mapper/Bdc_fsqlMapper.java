package org.jeecg.modules.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_fsql;

/**
 * @Description: 附属权利表
 * @Author: jeecg-boot
 * @Date:   2019-08-28
 * @Version: V1.0
 */
public interface Bdc_fsqlMapper extends BaseMapper<Bdc_fsql> {

    List<Bdc_fsql> getFsqlBydyid(@Param("prolsh") String prolsh, @Param("dyid") String dyid);

}

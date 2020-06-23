package org.jeecg.modules.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface Bdc_shyqzdMapper extends BaseMapper<Bdc_shyqzd> {

	List<Bdc_shyqzd> queryByEnterpriseId(@Param("enterpriseId")String enterpriseId);

}

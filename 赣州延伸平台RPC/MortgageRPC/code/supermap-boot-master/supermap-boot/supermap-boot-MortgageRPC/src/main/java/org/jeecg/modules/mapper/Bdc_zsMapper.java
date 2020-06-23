package org.jeecg.modules.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 证书信息
 * @Author: jeecg-boot
 * @Date:   2019-08-04
 * @Version: V1.0
 */
public interface Bdc_zsMapper extends BaseMapper<Bdc_zs> {
    List<Map> zslist(Page<Map> page, @Param("param") Map<String,String> param);
}

package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.YsptEnterprise;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface YsptEnterpriseMapper extends BaseMapper<YsptEnterprise>{

	YsptEnterprise getEnterpriseByName(@Param("enterprisename") String enterprisename);

	YsptEnterprise getEnterpriseByPhone(@Param("phone") String phone);

	List<Map> projectlist(Page<Map> page, @Param("param") Map<String,String> param);

}

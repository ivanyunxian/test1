package org.jeecg.modules.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;

/**
 * @Description: 工作流资料目录
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
public interface Wfi_materclassMapper extends BaseMapper<Wfi_materclass> {

    List<Wfi_materclass> getMaterlist(@Param("prolsh") String prolsh);

	List<String> getIdsByRyMaterclass();

}

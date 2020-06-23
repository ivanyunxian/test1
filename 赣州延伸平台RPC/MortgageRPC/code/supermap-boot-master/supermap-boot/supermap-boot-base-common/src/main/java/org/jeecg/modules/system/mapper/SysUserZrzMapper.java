package org.jeecg.modules.system.mapper;

import java.util.List;

import org.jeecg.modules.system.entity.SysUserZrz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.lettuce.core.dynamic.annotation.Param;

public interface SysUserZrzMapper extends BaseMapper<SysUserZrz>{

	List<SysUserZrz> getUserDepartByUid(@Param("userId") String userId);
}

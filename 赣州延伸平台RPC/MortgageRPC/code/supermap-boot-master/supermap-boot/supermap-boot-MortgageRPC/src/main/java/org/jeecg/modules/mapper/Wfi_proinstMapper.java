package org.jeecg.modules.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.mortgagerpc.entity.Wfi_proinst;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2019-07-29
 * @Version: V1.0
 */
public interface Wfi_proinstMapper extends BaseMapper<Wfi_proinst> {

    List<Map> projectlist(Page<Map> page, @Param("param") Map<String,String> param);

    List<Map> projectlistendbox(Page<Map> page, @Param("param") Map<String,String> param);

    void querypromaxid(@Param("param")Map param);

    SysUser getSysuser(@Param("userid")String userid);

    Map<String,String> getUserDepart(@Param("departid")String departid);
}

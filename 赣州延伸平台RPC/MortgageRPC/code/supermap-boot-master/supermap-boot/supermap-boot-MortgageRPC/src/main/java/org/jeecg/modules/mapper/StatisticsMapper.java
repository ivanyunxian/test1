package org.jeecg.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 统计配置
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface StatisticsMapper extends BaseMapper<Map> {

    public List<Map> countProjectName(@Param("param")Map param);

    public List<Map> countMonthMortgageAmount(@Param("param")Map param);

    public List<Map> countdyje(@Param("param")Map param);

    public List<Map> monthdyje(@Param("param")Map param);

    public List<Map> countProject(@Param("param")Map param);

    public List<Map> monthProject(@Param("param")Map param);

    public List<Map> countProportion(@Param("param")Map param);

    public List<Map> queryDataCount(@Param("param")Map param);

    public List<Map> queryData(@Param("param")Map param);

    public List<Map> projectMortgageAmount(@Param("param")Map param);

    public List<Map> areaMortgageAmount(@Param("param")Map param);

	public List<Map> personalCountDYJE(@Param("param")Map querymap);

	public List<Map> personalMonthdyje(@Param("param")Map querymap);

	public List<Map> personalMonthproject(@Param("param")Map querymap);

	public List<Map> personalCountProject(@Param("param")Map querymap);

	public List<Map> personalCountProjectName(@Param("param")Map querymap);

	public List<Map> personalCountMonthMortgageAmount(@Param("param")Map querymap);

	public List<Map> personalSHZTProportion(@Param("param")Map querymap);

	public List<Map> personalProjectMortgageAmount(@Param("param")Map querymap);

}

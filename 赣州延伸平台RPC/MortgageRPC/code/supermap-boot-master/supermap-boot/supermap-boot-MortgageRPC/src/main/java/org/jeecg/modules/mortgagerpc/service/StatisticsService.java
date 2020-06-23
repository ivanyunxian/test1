package org.jeecg.modules.mortgagerpc.service;


import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 统计配置
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface StatisticsService extends IService<Map> {

    //获取抵押金额
    public List<Map> getCountDYJE(Map data);

    public List<Map> getMonthdyje(Map data);

    //获取业务名称的数量
    public List<Map> getCountProjectName(Map data);

    public List<Map> getCountMonthMortgageAmount(Map data);

    //获取业务数量
    public List<Map> getCountProject(Map data);

    public List<Map> getMonthproject(Map data);

    public List<Map> getSHZTProportion(Map data);

    public List<Map> GetQueryDataCount(Map data);

    public List<Map> GetQueryData(Map data);

    public List<Map> GetProjectMortgageAmount(Map data);

    public List<Map> getAreaMortgageAmount(Map data);

	public List<Map> getPersonalCountDYJE(Map querymap);

	public List<Map> getPersonalMonthdyje(Map querymap);

	public List<Map> getPersonalMonthproject(Map querymap);

	public List<Map> getPersonalCountProject(Map querymap);

	public List<Map> getPersonalCountProjectName(Map querymap);

	public List<Map> getPersonalCountMonthMortgageAmount(Map querymap);

	public List<Map> getPersonalSHZTProportion(Map querymap);

	public List<Map> GetPersonalProjectMortgageAmount(Map querymap);
}

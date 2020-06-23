package org.jeecg.modules.mortgagerpc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.mapper.StatisticsMapper;
import org.jeecg.modules.mortgagerpc.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 统计配置
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Map> implements StatisticsService {

    @Autowired
    StatisticsMapper statisticsMapper;

    @Override
    public List<Map> getCountDYJE(Map data) {
        List<Map> datas = statisticsMapper.countdyje(data);
        return datas;
    }

    @Override
    public List<Map> getMonthdyje(Map data) {
        List<Map> datas = statisticsMapper.monthdyje(data);
        return datas;
    }

    @Override
    public List<Map> getCountProjectName(Map data) {
        List<Map> datas = statisticsMapper.countProjectName(data);
        return datas;
    }@Override

    public List<Map> getCountMonthMortgageAmount(Map data) {
        List<Map> datas = statisticsMapper.countMonthMortgageAmount(data);
        return datas;
    }

    @Override
    public List<Map> getCountProject(Map data) {
        List<Map> datas = statisticsMapper.countProject(data);
        return datas;
    }

    @Override
    public List<Map> getMonthproject(Map data) {
        List<Map> datas = statisticsMapper.monthProject(data);
        return datas;
    }

    @Override
    public List<Map> getSHZTProportion(Map data) {
        List<Map> datas = statisticsMapper.countProportion(data);
        return datas;
    }

    @Override
    public List<Map> GetQueryDataCount(Map data) {
        List<Map> datas = statisticsMapper.queryDataCount(data);
        return datas;
    }

    @Override
    public List<Map> GetQueryData(Map data) {
        List<Map> datas = statisticsMapper.queryData(data);
        return datas;
    }

    @Override
    public List<Map> GetProjectMortgageAmount(Map data) {
        List<Map> datas = statisticsMapper.projectMortgageAmount(data);
        return datas;
    }

    @Override
    public List<Map> getAreaMortgageAmount(Map data) {
        List<Map> datas = statisticsMapper.areaMortgageAmount(data);
        return datas;
    }

    @Override
	public List<Map> getPersonalCountDYJE(Map querymap) {
		List<Map> datas = statisticsMapper.personalCountDYJE(querymap);
        return datas;
	}

	@Override
	public List<Map> getPersonalMonthdyje(Map querymap) {
		List<Map> datas = statisticsMapper.personalMonthdyje(querymap);
        return datas;
	}

	@Override
	public List<Map> getPersonalMonthproject(Map querymap) {
		List<Map> datas = statisticsMapper.personalMonthproject(querymap);
        return datas;
	}

	@Override
	public List<Map> getPersonalCountProject(Map querymap) {
		List<Map> datas = statisticsMapper.personalCountProject(querymap);
        return datas;
	}

	@Override
	public List<Map> getPersonalCountProjectName(Map querymap) {
		List<Map> datas = statisticsMapper.personalCountProjectName(querymap);
        return datas;
	}

	@Override
	public List<Map> getPersonalCountMonthMortgageAmount(Map querymap) {
		List<Map> datas = statisticsMapper.personalCountMonthMortgageAmount(querymap);
        return datas;
	}

	@Override
	public List<Map> getPersonalSHZTProportion(Map querymap) {
		List<Map> datas = statisticsMapper.personalSHZTProportion(querymap);
        return datas;
	}

	@Override
	public List<Map> GetPersonalProjectMortgageAmount(Map querymap) {
		List<Map> datas = statisticsMapper.personalProjectMortgageAmount(querymap);
        return datas;
	}



}

package org.jeecg.modules.mortgagerpc.service.impl;

import java.util.List;

import org.jeecg.modules.mapper.Bdc_shyqzdMapper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.service.IBdc_shyqzdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class Bdc_shyqzdServiceImpl extends ServiceImpl<Bdc_shyqzdMapper, Bdc_shyqzd> implements IBdc_shyqzdService{

	@Autowired
	private Bdc_shyqzdMapper mapper;
	@Override
	public List<Bdc_shyqzd> queryByEnterpriseId(String enterpriseId) {
		return mapper.queryByEnterpriseId(enterpriseId);
	}

}

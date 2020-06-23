package org.jeecg.modules.mortgagerpc.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.jeecg.modules.mapper.Bdc_zrzMapper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zrz;
import org.jeecg.modules.mortgagerpc.model.BdcZrzTreeModel;
import org.jeecg.modules.mortgagerpc.service.IBdc_zrzService;
import org.jeecg.modules.mortgagerpc.util.FindZdZrzChildrenUtil;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class Bdc_zrzServiceImpl extends ServiceImpl<Bdc_zrzMapper, Bdc_zrz> implements IBdc_zrzService{

	@Override
	public List<BdcZrzTreeModel> queryZrzById(List<Bdc_shyqzd> zdlist) {
		
		LambdaQueryWrapper<Bdc_zrz> query = new LambdaQueryWrapper<Bdc_zrz>();
		List<String> list = zdlist.stream().map(e -> e.getId()).collect(Collectors.toList());
		query.in(Bdc_zrz::getZdid, list);
		List<Bdc_zrz> zrzList = this.list(query);
		return FindZdZrzChildrenUtil.wrapTreeDataToTreeList(zdlist,zrzList);
	}

}

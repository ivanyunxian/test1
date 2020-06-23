package org.jeecg.modules.system.service.impl;

import java.util.Date;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.system.entity.YsptEnterprise;
import org.jeecg.modules.system.mapper.YsptEnterpriseMapper;
import org.jeecg.modules.system.service.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegisterServiceImpl extends ServiceImpl<YsptEnterpriseMapper, YsptEnterprise> implements IRegisterService{
	@Autowired
	private YsptEnterpriseMapper enterpriseMapper;
	
	
	@Override
	public YsptEnterprise getEnterpriseByName(String enterprisename) {
		return enterpriseMapper.getEnterpriseByName(enterprisename);
	}

	@Override
	public YsptEnterprise getEnterpriseByPhone(String phone) {
		return enterpriseMapper.getEnterpriseByPhone(phone);
	}

	@Override
	public boolean delete(String id) {
		return this.delete(id);
	}

	@Override
	public boolean updateEnterpriseDataById(YsptEnterprise enterprise, String username) {
		if (enterprise != null && username != null) {
			enterprise.setUpdateTime(new Date());
			enterprise.setUpdateBy(username);
			boolean b = this.updateById(enterprise);
			return b;
		} else {
			return false;
		}
	}

	@Override
	public void saveEnterpriseData(YsptEnterprise enterprise) {
		enterprise.setCreateTime(new Date());
		enterprise.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
		enterprise.setStatus("0");
		this.saveOrUpdate(enterprise);
	}

}

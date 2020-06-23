package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.YsptEnterprise;

import com.baomidou.mybatisplus.extension.service.IService;

public interface IRegisterService extends IService<YsptEnterprise>{

	/**
	 * 
	 * @param enterprisename
	 * @return
	 */
	YsptEnterprise getEnterpriseByName(String enterprisename);
	
	/**
	 * 
	 * @param phone
	 * @return
	 */
	YsptEnterprise getEnterpriseByPhone(String phone);

	boolean delete(String id);

	boolean updateEnterpriseDataById(YsptEnterprise enterprise, String username);

	void saveEnterpriseData(YsptEnterprise enterprise);

	
}

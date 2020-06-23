package com.supermap.realestate.registration.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_SFDY;
import com.supermap.realestate.registration.model.BDCS_SFRELATION;
import com.supermap.realestate.registration.service.DJSFService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ui.Page;

@Service("djsfService")
public class DJSFServiceImpl implements DJSFService {
	
	@Autowired
	private CommonDao baseCommonDao;

	// 得到所有收费定义
	@Override
	public List<BDCS_SFDY> GetSFDY() {
		List<BDCS_SFDY> listSFDJ = baseCommonDao.getDataList(BDCS_SFDY.class, "ID is not null ORDER BY SFBMMC,SFXLMC,SFKMMC");
		return listSFDJ;
	}

	// 根据流程id得到收费信息
	@Override
	public List<BDCS_SFDY> GetSF(String prodefid) {
		// 根据流程ID得到该流程对应的登记收费关系
		String sqlStr = "prodef_id = '" + prodefid + "'";
		String fleld = "";
		List<BDCS_SFRELATION> listRela = baseCommonDao.getDataList(BDCS_SFRELATION.class, sqlStr);
		int num = listRela.size();
		if (num >= 1) {
			for (int i = 0; i < num; i++) {
				String sfdyid = listRela.get(i).getSFDYID();
				if (fleld == "") {
					fleld = "'" + sfdyid + "'";
				} else {
					fleld = fleld + "," + "'" + sfdyid + "'";
				}
			}
		} else {
			fleld = "''";
		}
		// 根据登记收费关系得到 收费定义
		sqlStr = "ID IN(" + fleld + ")";
		List<BDCS_SFDY> listSfdj = baseCommonDao.getDataList(BDCS_SFDY.class, sqlStr);
		return listSfdj;
	}

	// 获取所有收费定义 分页
	@Override
	public Page GetSFDY(int page, int rows) {
		Page listSfdj = baseCommonDao.getPageDataByHql(BDCS_SFDY.class, "ID is not null ORDER BY SFBMMC,SFXLMC,SFKMMC", page, rows);
		return listSfdj;
	}

	// 根据流程ID和登记收费定义ID，向登记关系表写入数据
	@Override
	public boolean addSFRela(String prodefid, String dyid) {
		boolean result = false;
		BDCS_SFRELATION sfreal = new BDCS_SFRELATION();
		sfreal.setId(getPrimaryKey());
		sfreal.setPRODEF_ID(prodefid);
		sfreal.setSFDYID(dyid);
		sfreal.setCREAT_TIME(new Date());
		try {
			baseCommonDao.save(sfreal);
			baseCommonDao.flush();
			YwLogUtil.addYwLog("登记收费配置-更新", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			result = true;
		} catch (Exception e) {
			YwLogUtil.addYwLog("登记收费配置-更新", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			e.printStackTrace();
		}
		return result;
	}

	// 根据流程ID和登记收费定义ID，删除登记关系表的对应数据
	@Override
	public boolean deleteSFRela(String prodefid) {
		boolean result = false;
		String sqlWhere = "PRODEF_ID = '" + prodefid + "'";
		try {
			baseCommonDao.deleteEntitysByHql(BDCS_SFRELATION.class, sqlWhere);
			baseCommonDao.flush();
			YwLogUtil.addYwLog("登记收费配置-删除", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			result = true;
		} catch (Exception e) {
			YwLogUtil.addYwLog("登记收费配置-删除", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
			e.printStackTrace();
		}
		return result;
	}

	// 根据流程ID和登记收费定义ID，查询是否存在对应数据
	public boolean IsExistSFRela(String prodefid, String dyid) {
		boolean result = false;
		String sqlWhere = "PRODEF_ID = '" + prodefid + "' and SFDYID = '" + dyid + "'";
		List<BDCS_SFRELATION> listRela = baseCommonDao.getDataList(BDCS_SFRELATION.class, sqlWhere);
		if (listRela.size() >= 1) {
			result = true;
		}
		return result;
	}

	// 获得主键ID
	protected static String getPrimaryKey() {
		String _id = SuperHelper.GeneratePrimaryKey();
		return _id;
	}

	@Override
	public boolean AddSFDY(BDCS_SFDY sfdy) {
		boolean result = false;
		sfdy.setCREAT_TIME(new Date());
		sfdy.setId(getPrimaryKey());
		try {
			baseCommonDao.save(sfdy);
			baseCommonDao.flush();
			YwLogUtil.addYwLog("登记收费配置-添加", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			result = true;
		} catch (Exception e) {
			result = false;
			YwLogUtil.addYwLog("登记收费配置-添加", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean EditSFDY(BDCS_SFDY sfdy) {
		boolean result = false;
		if(null != sfdy && !StringUtils.isEmpty(sfdy.getId())){
			BDCS_SFDY oldsfdy = baseCommonDao.get(BDCS_SFDY.class, sfdy.getId());
			sfdy.setCREAT_TIME(oldsfdy.getCREAT_TIME());
		}
		if (sfdy != null) {
			try {
				baseCommonDao.update(sfdy);
				baseCommonDao.flush();
				result = true;
			} catch (Exception e) {
				result = false;
                e.printStackTrace();
			}
		}
		return result;
	}

	// 根据收费定义ID，删除收费定义表的对应数据
	@Override
	public String deleteSFDY(String sfdyid) {
		String result = "删除成功";
		// 首先判断流程与收费项关系表中是否存在该收费定义，如果存在不能删除
		if (!IsExistSFDY(sfdyid)) {
			try {
				baseCommonDao.delete(BDCS_SFDY.class, sfdyid);
				baseCommonDao.flush();
				YwLogUtil.addYwLog("登记收费配置-删除该收费项目", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			} catch (Exception e) {
				YwLogUtil.addYwLog("登记收费配置-删除该收费项目-删除失败", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
				result = "删除失败";
			}
		} else {
			result = "该收费项目已经与流程绑定，请先删除流程与收费项目的关系后再删除！";
		}

		return result;
	}

	// 在删除收费定义数据之前，判断流程与收费项目关系表中是否存在记录
	public boolean IsExistSFDY(String sfdyid) {
		boolean result = false;
		String sqlWhere = "SFDYID = '" + sfdyid + "'";
		List<BDCS_SFRELATION> listRela = baseCommonDao.getDataList(
				BDCS_SFRELATION.class, sqlWhere);
		if (listRela.size() >= 1) {
			result = true;
		}
		return result;
	}

}

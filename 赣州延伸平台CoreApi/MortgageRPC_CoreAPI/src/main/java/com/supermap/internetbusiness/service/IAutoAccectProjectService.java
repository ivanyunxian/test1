package com.supermap.internetbusiness.service;

import com.supermap.internetbusiness.model.XS24HZDBJ;
import com.supermap.internetbusiness.util.ResultData;
import com.supermap.resources.util.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IAutoAccectProjectService {
    ResultData autoCreateProject(String wlsh, HttpServletRequest request) ;

	XS24HZDBJ saveXS24HZDBJ(XS24HZDBJ xs24HZDBJ);

    /**
     *  新增即时办结流程配置
     * @author:taochunda
     * @date：2018年12月23日 下午14:19:33
     * @param
     * @return
     */
    //配置新增
    Message addAutoProjectConfig(Map<String, String> mapCondition);
    //配置查询
    Message autoProjectConfigQueryService(String djcode,int page,int size);
	//配置修改
	Message autoProjectConfigUpdateService(Map<String, String> mapCondition);
    //配置删除
    Message autoProjectConfigDeleteService(String id);
	/**
	 * //24小时记录查询
	 * @param zwlsh
	 * @param djlsh
	 * @param sjpx
	 * @param dqzt
	 * @param sfxxbl
	 * @param cssj
	 * @param zzsj
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	Message search24Hzdbjxx(String zwlsh, String djlsh, String sjpx, String dqzt, String sfxxbl, String cssj,
								 String zzsj, String pageIndex, String pageSize);

	String failresult(String zwlsh);
	void checkAccectConstraint(String wlsh,HttpServletRequest request, XS24HZDBJ bjlog) throws Exception;
}

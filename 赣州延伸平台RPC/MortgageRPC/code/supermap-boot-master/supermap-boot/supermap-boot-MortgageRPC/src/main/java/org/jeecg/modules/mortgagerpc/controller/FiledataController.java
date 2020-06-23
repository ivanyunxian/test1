package org.jeecg.modules.mortgagerpc.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.modules.mortgagerpc.entity.Filedata;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materdata;
import org.jeecg.modules.mortgagerpc.service.IFiledataService;
import org.jeecgframework.poi.excel.view.JeecgTemplateExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags="附件资料表")
@RestController
@RequestMapping("/mortgagerpc/filedata")
public class FiledataController {

	@Autowired
	private IFiledataService filedataService;
	
	/**
	  * 分页列表查询
	 * @param Filedata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "附件资料表-分页列表查询")
	@ApiOperation(value="附件资料表-分页列表查询", notes="附件资料表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Filedata>> queryPageList(Filedata filedata,
													  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													  HttpServletRequest req) {
		Result<IPage<Filedata>> result = new Result<IPage<Filedata>>();
		QueryWrapper<Filedata> queryWrapper = QueryGenerator.initQueryWrapper(filedata, req.getParameterMap());
		Page<Filedata> page = new Page<Filedata>(pageNo, pageSize);
		if(!StringHelper.isEmpty(req.getParameter("filename"))) {
			queryWrapper.like("name", req.getParameter("filename"));
		}
		if(!StringHelper.isEmpty(req.getParameter("wjlx"))) {
			queryWrapper.like("status", req.getParameter("wjlx"));
		}
		IPage<Filedata> pageList = filedataService.page(page, queryWrapper);
		
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "附件资料表-通过id删除")
	@ApiOperation(value="附件资料表-通过id删除", notes="附件资料表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			filedataService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "附件资料表-批量删除")
	@ApiOperation(value="附件资料表-批量删除", notes="附件资料表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Filedata> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Filedata> result = new Result<Filedata>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.filedataService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
}

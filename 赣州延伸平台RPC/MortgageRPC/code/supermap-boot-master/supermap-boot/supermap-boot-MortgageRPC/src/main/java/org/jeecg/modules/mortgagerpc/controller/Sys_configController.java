package org.jeecg.modules.mortgagerpc.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mortgagerpc.entity.Sys_config;
import org.jeecg.modules.mortgagerpc.service.ISys_configService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 系统参数
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="系统参数")
@RestController
@RequestMapping("/mortgagerpc/sys_config")
public class Sys_configController {
	@Autowired
	private ISys_configService sys_configService;
	
	/**
	  * 分页列表查询
	 * @param sys_config
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "系统参数-分页列表查询")
	@ApiOperation(value="系统参数-分页列表查询", notes="系统参数-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Sys_config>> queryPageList(Sys_config sys_config,
												   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												   HttpServletRequest req) {
		Result<IPage<Sys_config>> result = new Result<IPage<Sys_config>>();
		QueryWrapper<Sys_config> queryWrapper = QueryGenerator.initQueryWrapper(sys_config, req.getParameterMap());
		Page<Sys_config> page = new Page<Sys_config>(pageNo, pageSize);
		IPage<Sys_config> pageList = sys_configService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sys_config
	 * @return
	 */
	@AutoLog(value = "系统参数-添加")
	@ApiOperation(value="系统参数-添加", notes="系统参数-添加")
	@PostMapping(value = "/add")
	public Result<Sys_config> add(@RequestBody Sys_config sys_config) {
		Result<Sys_config> result = new Result<Sys_config>();
		try {
			sys_configService.save(sys_config);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sys_config
	 * @return
	 */
	@AutoLog(value = "系统参数-编辑")
	@ApiOperation(value="系统参数-编辑", notes="系统参数-编辑")
	@PutMapping(value = "/edit")
	public Result<Sys_config> edit(@RequestBody Sys_config sys_config) {
		Result<Sys_config> result = new Result<Sys_config>();
		Sys_config sys_configEntity = sys_configService.getById(sys_config.getId());
		if(sys_configEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sys_configService.updateById(sys_config);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统参数-通过id删除")
	@ApiOperation(value="系统参数-通过id删除", notes="系统参数-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			sys_configService.removeById(id);
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
	@AutoLog(value = "系统参数-批量删除")
	@ApiOperation(value="系统参数-批量删除", notes="系统参数-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Sys_config> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Sys_config> result = new Result<Sys_config>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sys_configService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统参数-通过id查询")
	@ApiOperation(value="系统参数-通过id查询", notes="系统参数-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Sys_config> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Sys_config> result = new Result<Sys_config>();
		Sys_config sys_config = sys_configService.getById(id);
		if(sys_config==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sys_config);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")

  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<Sys_config> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Sys_config sys_config = JSON.parseObject(deString, Sys_config.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sys_config, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Sys_config> pageList = sys_configService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "系统参数列表");
      mv.addObject(NormalExcelConstants.CLASS, Sys_config.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("系统参数列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<Sys_config> listSys_configs = ExcelImportUtil.importExcel(file.getInputStream(), Sys_config.class, params);
              sys_configService.saveBatch(listSys_configs);
              return Result.ok("文件导入成功！数据行数:" + listSys_configs.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}

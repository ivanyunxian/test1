package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartConfig;
import org.jeecg.modules.system.service.ISysDepartConfigService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 部门配置表
 * @Author: jeecg-boot
 * @Date:   2019-09-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="部门配置表")
@RestController
@RequestMapping("/sys/sysDepartConfig")
public class SysDepartConfigController {
	@Autowired
	private ISysDepartConfigService sysDepartConfigService;

	 @Autowired
	 private ISysDepartService sysDepartService;
	/**
	  * 分页列表查询
	 * @param sysDepartConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "部门配置表-分页列表查询")
	@ApiOperation(value="部门配置表-分页列表查询", notes="部门配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysDepartConfig>> queryPageList(SysDepartConfig sysDepartConfig,
														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														HttpServletRequest req) {
		Result<IPage<SysDepartConfig>> result = new Result<IPage<SysDepartConfig>>();
		QueryWrapper<SysDepartConfig> queryWrapper = QueryGenerator.initQueryWrapper(sysDepartConfig, req.getParameterMap());
		Page<SysDepartConfig> page = new Page<SysDepartConfig>(pageNo, pageSize);
		IPage<SysDepartConfig> pageList = sysDepartConfigService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysDepartConfig
	 * @return
	 */
	@AutoLog(value = "部门配置表-添加")
	@ApiOperation(value="部门配置表-添加", notes="部门配置表-添加")
	@PostMapping(value = "/add")
	public Result<SysDepartConfig> add(@RequestBody SysDepartConfig sysDepartConfig) {
		Result<SysDepartConfig> result = new Result<SysDepartConfig>();
		try {
			String deptId = sysDepartConfig.getDeptId();
			SysDepart depart = sysDepartService.getById(deptId);
			if(depart==null) {
				throw new SupermapBootException("查无机构信息");
			}
			sysDepartConfig.setDivisionCode(depart.getDivisionCode());
			sysDepartConfig.setCreatetime(new Date());
			sysDepartConfigService.save(sysDepartConfig);
			result.success("添加成功！");
		} catch (SupermapBootException e) {
			log.error(e.getMessage(),e);
			result.error500(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysDepartConfig
	 * @return
	 */
	@AutoLog(value = "部门配置表-编辑")
	@ApiOperation(value="部门配置表-编辑", notes="部门配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<SysDepartConfig> edit(@RequestBody SysDepartConfig sysDepartConfig) {
		Result<SysDepartConfig> result = new Result<SysDepartConfig>();
		SysDepartConfig sysDepartConfigEntity = sysDepartConfigService.getById(sysDepartConfig.getId());
		if(sysDepartConfigEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysDepartConfigService.updateById(sysDepartConfig);
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
	@AutoLog(value = "部门配置表-通过id删除")
	@ApiOperation(value="部门配置表-通过id删除", notes="部门配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			sysDepartConfigService.removeById(id);
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
	@AutoLog(value = "部门配置表-批量删除")
	@ApiOperation(value="部门配置表-批量删除", notes="部门配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysDepartConfig> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysDepartConfig> result = new Result<SysDepartConfig>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysDepartConfigService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "部门配置表-通过id查询")
	@ApiOperation(value="部门配置表-通过id查询", notes="部门配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysDepartConfig> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysDepartConfig> result = new Result<SysDepartConfig>();
		SysDepartConfig sysDepartConfig = sysDepartConfigService.getById(id);
		if(sysDepartConfig==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysDepartConfig);
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
      QueryWrapper<SysDepartConfig> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysDepartConfig sysDepartConfig = JSON.parseObject(deString, SysDepartConfig.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysDepartConfig, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysDepartConfig> pageList = sysDepartConfigService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "部门配置表列表");
      mv.addObject(NormalExcelConstants.CLASS, SysDepartConfig.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("部门配置表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<SysDepartConfig> listSysDepartConfigs = ExcelImportUtil.importExcel(file.getInputStream(), SysDepartConfig.class, params);
              sysDepartConfigService.saveBatch(listSysDepartConfigs);
              return Result.ok("文件导入成功！数据行数:" + listSysDepartConfigs.size());
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

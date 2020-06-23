package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.Sys_apis;
import org.jeecg.modules.system.service.ISys_apisService;
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
 * @Description: 接口注册管理表
 * @Author: jeecg-boot
 * @Date:   2019-09-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="接口注册管理表")
@RestController
@RequestMapping("/system/sys_apis")
public class Sys_apisController {
	@Autowired
	private ISys_apisService sys_apisService;
	
	/**
	  * 分页列表查询
	 * @param sys_apis
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "接口注册管理表-分页列表查询")
	@ApiOperation(value="接口注册管理表-分页列表查询", notes="接口注册管理表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Sys_apis>> queryPageList(Sys_apis sys_apis,
												 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												 HttpServletRequest req) {
		Result<IPage<Sys_apis>> result = new Result<IPage<Sys_apis>>();
		QueryWrapper<Sys_apis> queryWrapper = QueryGenerator.initQueryWrapper(sys_apis, req.getParameterMap());
		Page<Sys_apis> page = new Page<Sys_apis>(pageNo, pageSize);
		IPage<Sys_apis> pageList = sys_apisService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sys_apis
	 * @return
	 */
	@AutoLog(value = "接口注册管理表-添加")
	@ApiOperation(value="接口注册管理表-添加", notes="接口注册管理表-添加")
	@PostMapping(value = "/add")
	public Result<Sys_apis> add(@RequestBody Sys_apis sys_apis) {
		Result<Sys_apis> result = new Result<Sys_apis>();
		try {
			sys_apisService.save(sys_apis);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sys_apis
	 * @return
	 */
	@AutoLog(value = "接口注册管理表-编辑")
	@ApiOperation(value="接口注册管理表-编辑", notes="接口注册管理表-编辑")
	@PutMapping(value = "/edit")
	public Result<Sys_apis> edit(@RequestBody Sys_apis sys_apis) {
		Result<Sys_apis> result = new Result<Sys_apis>();
		Sys_apis sys_apisEntity = sys_apisService.getById(sys_apis.getId());
		if(sys_apisEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sys_apisService.updateById(sys_apis);
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
	@AutoLog(value = "接口注册管理表-通过id删除")
	@ApiOperation(value="接口注册管理表-通过id删除", notes="接口注册管理表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			sys_apisService.removeById(id);
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
	@AutoLog(value = "接口注册管理表-批量删除")
	@ApiOperation(value="接口注册管理表-批量删除", notes="接口注册管理表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Sys_apis> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Sys_apis> result = new Result<Sys_apis>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sys_apisService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "接口注册管理表-通过id查询")
	@ApiOperation(value="接口注册管理表-通过id查询", notes="接口注册管理表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Sys_apis> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Sys_apis> result = new Result<Sys_apis>();
		Sys_apis sys_apis = sys_apisService.getById(id);
		if(sys_apis==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sys_apis);
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
      QueryWrapper<Sys_apis> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Sys_apis sys_apis = JSON.parseObject(deString, Sys_apis.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sys_apis, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Sys_apis> pageList = sys_apisService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "接口注册管理表列表");
      mv.addObject(NormalExcelConstants.CLASS, Sys_apis.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("接口注册管理表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Sys_apis> listSys_apiss = ExcelImportUtil.importExcel(file.getInputStream(), Sys_apis.class, params);
              sys_apisService.saveBatch(listSys_apiss);
              return Result.ok("文件导入成功！数据行数:" + listSys_apiss.size());
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

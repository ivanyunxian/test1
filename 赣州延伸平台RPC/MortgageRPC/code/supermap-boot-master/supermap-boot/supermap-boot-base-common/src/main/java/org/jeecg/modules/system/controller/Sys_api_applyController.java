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

import org.jeecg.modules.system.entity.Sys_api_apply;
import org.jeecg.modules.system.service.ISys_api_applyService;
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
 * @Description: 接口申请内容表
 * @Author: jeecg-boot
 * @Date:   2019-09-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="接口申请内容表")
@RestController
@RequestMapping("/system/sys_api_apply")
public class Sys_api_applyController {
	@Autowired
	private ISys_api_applyService sys_api_applyService;
	
	/**
	  * 分页列表查询
	 * @param sys_api_apply
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "接口申请内容表-分页列表查询")
	@ApiOperation(value="接口申请内容表-分页列表查询", notes="接口申请内容表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Sys_api_apply>> queryPageList(Sys_api_apply sys_api_apply,
													  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													  HttpServletRequest req) {
		Result<IPage<Sys_api_apply>> result = new Result<IPage<Sys_api_apply>>();
		QueryWrapper<Sys_api_apply> queryWrapper = QueryGenerator.initQueryWrapper(sys_api_apply, req.getParameterMap());
		Page<Sys_api_apply> page = new Page<Sys_api_apply>(pageNo, pageSize);
		IPage<Sys_api_apply> pageList = sys_api_applyService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sys_api_apply
	 * @return
	 */
	@AutoLog(value = "接口申请内容表-添加")
	@ApiOperation(value="接口申请内容表-添加", notes="接口申请内容表-添加")
	@PostMapping(value = "/add")
	public Result<Sys_api_apply> add(@RequestBody Sys_api_apply sys_api_apply) {
		Result<Sys_api_apply> result = new Result<Sys_api_apply>();
		try {
			sys_api_applyService.save(sys_api_apply);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sys_api_apply
	 * @return
	 */
	@AutoLog(value = "接口申请内容表-编辑")
	@ApiOperation(value="接口申请内容表-编辑", notes="接口申请内容表-编辑")
	@PutMapping(value = "/edit")
	public Result<Sys_api_apply> edit(@RequestBody Sys_api_apply sys_api_apply) {
		Result<Sys_api_apply> result = new Result<Sys_api_apply>();
		Sys_api_apply sys_api_applyEntity = sys_api_applyService.getById(sys_api_apply.getId());
		if(sys_api_applyEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sys_api_applyService.updateById(sys_api_apply);
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
	@AutoLog(value = "接口申请内容表-通过id删除")
	@ApiOperation(value="接口申请内容表-通过id删除", notes="接口申请内容表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			sys_api_applyService.removeById(id);
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
	@AutoLog(value = "接口申请内容表-批量删除")
	@ApiOperation(value="接口申请内容表-批量删除", notes="接口申请内容表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Sys_api_apply> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Sys_api_apply> result = new Result<Sys_api_apply>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sys_api_applyService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "接口申请内容表-通过id查询")
	@ApiOperation(value="接口申请内容表-通过id查询", notes="接口申请内容表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Sys_api_apply> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Sys_api_apply> result = new Result<Sys_api_apply>();
		Sys_api_apply sys_api_apply = sys_api_applyService.getById(id);
		if(sys_api_apply==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sys_api_apply);
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
      QueryWrapper<Sys_api_apply> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Sys_api_apply sys_api_apply = JSON.parseObject(deString, Sys_api_apply.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sys_api_apply, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Sys_api_apply> pageList = sys_api_applyService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "接口申请内容表列表");
      mv.addObject(NormalExcelConstants.CLASS, Sys_api_apply.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("接口申请内容表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Sys_api_apply> listSys_api_applys = ExcelImportUtil.importExcel(file.getInputStream(), Sys_api_apply.class, params);
              sys_api_applyService.saveBatch(listSys_api_applys);
              return Result.ok("文件导入成功！数据行数:" + listSys_api_applys.size());
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

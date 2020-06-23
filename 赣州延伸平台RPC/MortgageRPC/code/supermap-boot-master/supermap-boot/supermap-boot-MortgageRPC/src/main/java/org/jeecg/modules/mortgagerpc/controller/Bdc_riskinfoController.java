package org.jeecg.modules.mortgagerpc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mortgagerpc.entity.Bdc_riskinfo;
import org.jeecg.modules.mortgagerpc.service.IBdc_riskinfoService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 风险防控
 * @Author: jeecg-boot
 * @Date:   2019-09-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="风险防控")
@RestController
@RequestMapping("/modules/bdc_riskinfo")
public class Bdc_riskinfoController {
	@Autowired
	private IBdc_riskinfoService bdc_riskinfoService;
	
	/**
	  * 分页列表查询
	 * @param bdc_riskinfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "风险防控-分页列表查询")
	@ApiOperation(value="风险防控-分页列表查询", notes="风险防控-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Bdc_riskinfo>> queryPageList(Bdc_riskinfo bdc_riskinfo,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		Result<IPage<Bdc_riskinfo>> result = new Result<IPage<Bdc_riskinfo>>();
		QueryWrapper<Bdc_riskinfo> queryWrapper = QueryGenerator.initQueryWrapper(bdc_riskinfo, req.getParameterMap());
		Page<Bdc_riskinfo> page = new Page<Bdc_riskinfo>(pageNo, pageSize);
		IPage<Bdc_riskinfo> pageList = bdc_riskinfoService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param bdc_riskinfo
	 * @return
	 */
	@AutoLog(value = "风险防控-添加")
	@ApiOperation(value="风险防控-添加", notes="风险防控-添加")
	@PostMapping(value = "/add")
	public Result<Bdc_riskinfo> add(@RequestBody Bdc_riskinfo bdc_riskinfo) {
		Result<Bdc_riskinfo> result = new Result<Bdc_riskinfo>();
		try {
			bdc_riskinfoService.save(bdc_riskinfo);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param bdc_riskinfo
	 * @return
	 */
	@AutoLog(value = "风险防控-编辑")
	@ApiOperation(value="风险防控-编辑", notes="风险防控-编辑")
	@PutMapping(value = "/edit")
	public Result<Bdc_riskinfo> edit(@RequestBody Bdc_riskinfo bdc_riskinfo) {
		Result<Bdc_riskinfo> result = new Result<Bdc_riskinfo>();
		Bdc_riskinfo bdc_riskinfoEntity = bdc_riskinfoService.getById(bdc_riskinfo.getId());
		if(bdc_riskinfoEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bdc_riskinfoService.updateById(bdc_riskinfo);
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
	@AutoLog(value = "风险防控-通过id删除")
	@ApiOperation(value="风险防控-通过id删除", notes="风险防控-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			bdc_riskinfoService.removeById(id);
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
	@AutoLog(value = "风险防控-批量删除")
	@ApiOperation(value="风险防控-批量删除", notes="风险防控-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Bdc_riskinfo> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Bdc_riskinfo> result = new Result<Bdc_riskinfo>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bdc_riskinfoService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "风险防控-通过id查询")
	@ApiOperation(value="风险防控-通过id查询", notes="风险防控-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Bdc_riskinfo> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Bdc_riskinfo> result = new Result<Bdc_riskinfo>();
		Bdc_riskinfo bdc_riskinfo = bdc_riskinfoService.getById(id);
		if(bdc_riskinfo==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bdc_riskinfo);
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
      QueryWrapper<Bdc_riskinfo> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Bdc_riskinfo bdc_riskinfo = JSON.parseObject(deString, Bdc_riskinfo.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bdc_riskinfo, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Bdc_riskinfo> pageList = bdc_riskinfoService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "风险防控列表");
      mv.addObject(NormalExcelConstants.CLASS, Bdc_riskinfo.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("风险防控列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Bdc_riskinfo> listBdc_riskinfos = ExcelImportUtil.importExcel(file.getInputStream(), Bdc_riskinfo.class, params);
              bdc_riskinfoService.saveBatch(listBdc_riskinfos);
              return Result.ok("文件导入成功！数据行数:" + listBdc_riskinfos.size());
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

	 /**
	  * 获取时间段内的单元限制列表
	  * param wfi_proinst
	  *
	  * @return
	  */
	 @AutoLog(value = "信息查询-获取时间段内的单元限制列表")
	 @ApiOperation(value = "信息查询-获取时间段内的单元限制列表", notes = "信息查询-获取时间段内的单元限制列表")
	 @PostMapping(value = "/getLimitUnitInfo")
	 public Result<JSONObject> getLimitUnitInfo(@RequestBody JSONObject requestparam) {
		 Result<JSONObject> result = new Result<JSONObject>();
		 try {
			 JSONObject info = bdc_riskinfoService.getLimitUnitInfo(requestparam);
			 result.setResult(info);
			 result.setSuccess(true);
		 } catch (SupermapBootException e) {
			 log.error(e.getMessage(), e);
			 result.error500("获取失败：" + e.getMessage());
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("获取失败");
		 }
		 return result;
	 }
	 
	 /**
	   * 获取房源信息接口
	   * 获取到房源信息后
	   *
	   * @return
	   */
	  @AutoLog(value = "信息查询-获取单元限制状态")
	  @ApiOperation(value = "信息查询-获取单元限制状态", notes = "信息查询-获取单元限制状态")
	  @PostMapping(value = "/getUnitStatusInfo")
	  public Result<JSONObject> getUnitStatusInfo(@RequestBody JSONObject requestparam) {
		  Result<JSONObject> result = new Result<JSONObject>();
		  try {
             JSONObject obj = bdc_riskinfoService.getUnitStatusInfo(requestparam);
             result.setResult(obj);
             result.setSuccess(true);
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败：" + e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("单元状态接口异常，请联系管理员核查问题");
		  }
		  return result;
	  }

}

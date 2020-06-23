package org.jeecg.modules.mortgagerpc.controller;

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

import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;
import org.jeecg.modules.mortgagerpc.service.IWfi_materclassService;
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
 * @Description: 工作流资料目录
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="工作流资料目录")
@RestController
@RequestMapping("/ad/wfi_materclass")
public class Wfi_materclassController {
	@Autowired
	private IWfi_materclassService wfi_materclassService;
	
	/**
	  * 分页列表查询
	 * @param wfi_materclass
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "工作流资料目录-分页列表查询")
	@ApiOperation(value="工作流资料目录-分页列表查询", notes="工作流资料目录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Wfi_materclass>> queryPageList(Wfi_materclass wfi_materclass,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		Result<IPage<Wfi_materclass>> result = new Result<IPage<Wfi_materclass>>();
		QueryWrapper<Wfi_materclass> queryWrapper = QueryGenerator.initQueryWrapper(wfi_materclass, req.getParameterMap());
		Page<Wfi_materclass> page = new Page<Wfi_materclass>(pageNo, pageSize);
		IPage<Wfi_materclass> pageList = wfi_materclassService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param wfi_materclass
	 * @return
	 */
	@AutoLog(value = "工作流资料目录-添加")
	@ApiOperation(value="工作流资料目录-添加", notes="工作流资料目录-添加")
	@PostMapping(value = "/add")
	public Result<Wfi_materclass> add(@RequestBody Wfi_materclass wfi_materclass) {
		Result<Wfi_materclass> result = new Result<Wfi_materclass>();
		try {
			wfi_materclassService.save(wfi_materclass);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param wfi_materclass
	 * @return
	 */
	@AutoLog(value = "工作流资料目录-编辑")
	@ApiOperation(value="工作流资料目录-编辑", notes="工作流资料目录-编辑")
	@PutMapping(value = "/edit")
	public Result<Wfi_materclass> edit(@RequestBody Wfi_materclass wfi_materclass) {
		Result<Wfi_materclass> result = new Result<Wfi_materclass>();
		Wfi_materclass wfi_materclassEntity = wfi_materclassService.getById(wfi_materclass.getId());
		if(wfi_materclassEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = wfi_materclassService.updateById(wfi_materclass);
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
	@AutoLog(value = "工作流资料目录-通过id删除")
	@ApiOperation(value="工作流资料目录-通过id删除", notes="工作流资料目录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			wfi_materclassService.removeById(id);
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
	@AutoLog(value = "工作流资料目录-批量删除")
	@ApiOperation(value="工作流资料目录-批量删除", notes="工作流资料目录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Wfi_materclass> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Wfi_materclass> result = new Result<Wfi_materclass>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.wfi_materclassService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工作流资料目录-通过id查询")
	@ApiOperation(value="工作流资料目录-通过id查询", notes="工作流资料目录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Wfi_materclass> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Wfi_materclass> result = new Result<Wfi_materclass>();
		Wfi_materclass wfi_materclass = wfi_materclassService.getById(id);
		if(wfi_materclass==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(wfi_materclass);
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
      QueryWrapper<Wfi_materclass> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Wfi_materclass wfi_materclass = JSON.parseObject(deString, Wfi_materclass.class);
              queryWrapper = QueryGenerator.initQueryWrapper(wfi_materclass, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Wfi_materclass> pageList = wfi_materclassService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "工作流资料目录列表");
      mv.addObject(NormalExcelConstants.CLASS, Wfi_materclass.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("工作流资料目录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Wfi_materclass> listWfi_materclasss = ExcelImportUtil.importExcel(file.getInputStream(), Wfi_materclass.class, params);
              wfi_materclassService.saveBatch(listWfi_materclasss);
              return Result.ok("文件导入成功！数据行数:" + listWfi_materclasss.size());
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

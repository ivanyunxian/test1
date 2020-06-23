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
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mortgagerpc.entity.Wfi_slxmsh;
import org.jeecg.modules.mortgagerpc.service.IWfi_slxmshService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 审核过程意见表
 * @Author: jeecg-boot
 * @Date:   2019-08-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="审核过程意见表")
@RestController
@RequestMapping("/ac/wfi_slxmsh")
public class Wfi_slxmshController {
	@Autowired
	private IWfi_slxmshService wfi_slxmshService;
	
	/**
	  * 分页列表查询
	 * @param wfi_slxmsh
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "审核过程意见表-分页列表查询")
	@ApiOperation(value="审核过程意见表-分页列表查询", notes="审核过程意见表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Wfi_slxmsh>> queryPageList(Wfi_slxmsh wfi_slxmsh,
												   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												   HttpServletRequest req) {
		Result<IPage<Wfi_slxmsh>> result = new Result<IPage<Wfi_slxmsh>>();
		QueryWrapper<Wfi_slxmsh> queryWrapper = QueryGenerator.initQueryWrapper(wfi_slxmsh, req.getParameterMap());
		Page<Wfi_slxmsh> page = new Page<Wfi_slxmsh>(pageNo, pageSize);
		IPage<Wfi_slxmsh> pageList = wfi_slxmshService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param wfi_slxmsh
	 * @return
	 */
	@AutoLog(value = "审核过程意见表-添加")
	@ApiOperation(value="审核过程意见表-添加", notes="审核过程意见表-添加")
	@PostMapping(value = "/add")
	public Result<Wfi_slxmsh> add(@RequestBody Wfi_slxmsh wfi_slxmsh) {
		Result<Wfi_slxmsh> result = new Result<Wfi_slxmsh>();
		try {
			wfi_slxmshService.save(wfi_slxmsh);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param wfi_slxmsh
	 * @return
	 */
	@AutoLog(value = "审核过程意见表-编辑")
	@ApiOperation(value="审核过程意见表-编辑", notes="审核过程意见表-编辑")
	@PutMapping(value = "/edit")
	public Result<Wfi_slxmsh> edit(@RequestBody Wfi_slxmsh wfi_slxmsh) {
		Result<Wfi_slxmsh> result = new Result<Wfi_slxmsh>();
		Wfi_slxmsh wfi_slxmshEntity = wfi_slxmshService.getById(wfi_slxmsh.getId());
		if(wfi_slxmshEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = wfi_slxmshService.updateById(wfi_slxmsh);
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
	@AutoLog(value = "审核过程意见表-通过id删除")
	@ApiOperation(value="审核过程意见表-通过id删除", notes="审核过程意见表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			wfi_slxmshService.removeById(id);
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
	@AutoLog(value = "审核过程意见表-批量删除")
	@ApiOperation(value="审核过程意见表-批量删除", notes="审核过程意见表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Wfi_slxmsh> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Wfi_slxmsh> result = new Result<Wfi_slxmsh>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.wfi_slxmshService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核过程意见表-通过id查询")
	@ApiOperation(value="审核过程意见表-通过id查询", notes="审核过程意见表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Wfi_slxmsh> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Wfi_slxmsh> result = new Result<Wfi_slxmsh>();
		Wfi_slxmsh wfi_slxmsh = wfi_slxmshService.getById(id);
		if(wfi_slxmsh==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(wfi_slxmsh);
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
      QueryWrapper<Wfi_slxmsh> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Wfi_slxmsh wfi_slxmsh = JSON.parseObject(deString, Wfi_slxmsh.class);
              queryWrapper = QueryGenerator.initQueryWrapper(wfi_slxmsh, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Wfi_slxmsh> pageList = wfi_slxmshService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "审核过程意见表列表");
      mv.addObject(NormalExcelConstants.CLASS, Wfi_slxmsh.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("审核过程意见表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Wfi_slxmsh> listWfi_slxmshs = ExcelImportUtil.importExcel(file.getInputStream(), Wfi_slxmsh.class, params);
              wfi_slxmshService.saveBatch(listWfi_slxmshs);
              return Result.ok("文件导入成功！数据行数:" + listWfi_slxmshs.size());
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

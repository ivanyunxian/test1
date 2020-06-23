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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.mortgagerpc.entity.Bdc_last_ql;
import org.jeecg.modules.mortgagerpc.service.IBdc_last_qlService;
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
 * @Description: 上一手权利信息
 * @Author: jeecg-boot
 * @Date:   2019-09-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="上一手权利信息")
@RestController
@RequestMapping("/asdf/bdc_last_ql")
public class Bdc_last_qlController {
	@Autowired
	private IBdc_last_qlService bdc_last_qlService;
	
	/**
	  * 分页列表查询
	 * @param bdc_last_ql
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "上一手权利信息-分页列表查询")
	@ApiOperation(value="上一手权利信息-分页列表查询", notes="上一手权利信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Bdc_last_ql>> queryPageList(Bdc_last_ql bdc_last_ql,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Bdc_last_ql>> result = new Result<IPage<Bdc_last_ql>>();
		QueryWrapper<Bdc_last_ql> queryWrapper = QueryGenerator.initQueryWrapper(bdc_last_ql, req.getParameterMap());
		Page<Bdc_last_ql> page = new Page<Bdc_last_ql>(pageNo, pageSize);
		IPage<Bdc_last_ql> pageList = bdc_last_qlService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param bdc_last_ql
	 * @return
	 */
	@AutoLog(value = "上一手权利信息-添加")
	@ApiOperation(value="上一手权利信息-添加", notes="上一手权利信息-添加")
	@PostMapping(value = "/add")
	public Result<Bdc_last_ql> add(@RequestBody Bdc_last_ql bdc_last_ql) {
		Result<Bdc_last_ql> result = new Result<Bdc_last_ql>();
		try {
			bdc_last_qlService.save(bdc_last_ql);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param bdc_last_ql
	 * @return
	 */
	@AutoLog(value = "上一手权利信息-编辑")
	@ApiOperation(value="上一手权利信息-编辑", notes="上一手权利信息-编辑")
	@PutMapping(value = "/edit")
	public Result<Bdc_last_ql> edit(@RequestBody Bdc_last_ql bdc_last_ql) {
		Result<Bdc_last_ql> result = new Result<Bdc_last_ql>();
		Bdc_last_ql bdc_last_qlEntity = bdc_last_qlService.getById(bdc_last_ql.getId());
		if(bdc_last_qlEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bdc_last_qlService.updateById(bdc_last_ql);
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
	@AutoLog(value = "上一手权利信息-通过id删除")
	@ApiOperation(value="上一手权利信息-通过id删除", notes="上一手权利信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			bdc_last_qlService.removeById(id);
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
	@AutoLog(value = "上一手权利信息-批量删除")
	@ApiOperation(value="上一手权利信息-批量删除", notes="上一手权利信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Bdc_last_ql> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Bdc_last_ql> result = new Result<Bdc_last_ql>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bdc_last_qlService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "上一手权利信息-通过id查询")
	@ApiOperation(value="上一手权利信息-通过id查询", notes="上一手权利信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Bdc_last_ql> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Bdc_last_ql> result = new Result<Bdc_last_ql>();
		Bdc_last_ql bdc_last_ql = bdc_last_qlService.getById(id);
		if(bdc_last_ql==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bdc_last_ql);
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
      QueryWrapper<Bdc_last_ql> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Bdc_last_ql bdc_last_ql = JSON.parseObject(deString, Bdc_last_ql.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bdc_last_ql, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Bdc_last_ql> pageList = bdc_last_qlService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "上一手权利信息列表");
      mv.addObject(NormalExcelConstants.CLASS, Bdc_last_ql.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("上一手权利信息列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Bdc_last_ql> listBdc_last_qls = ExcelImportUtil.importExcel(file.getInputStream(), Bdc_last_ql.class, params);
              bdc_last_qlService.saveBatch(listBdc_last_qls);
              return Result.ok("文件导入成功！数据行数:" + listBdc_last_qls.size());
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

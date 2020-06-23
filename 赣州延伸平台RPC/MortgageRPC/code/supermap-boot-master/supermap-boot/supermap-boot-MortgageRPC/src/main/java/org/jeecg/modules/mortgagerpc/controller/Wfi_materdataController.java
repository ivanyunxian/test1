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
import org.jeecg.modules.mortgagerpc.entity.Wfi_materdata;
import org.jeecg.modules.mortgagerpc.service.IWfi_materdataService;
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
 * @Description: 附件资料表
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="附件资料表")
@RestController
@RequestMapping("/mortgagerpc/wfi_materdata")
public class Wfi_materdataController {
	@Autowired
	private IWfi_materdataService wfi_materdataService;
	
	/**
	  * 分页列表查询
	 * @param wfi_materdata
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "附件资料表-分页列表查询")
	@ApiOperation(value="附件资料表-分页列表查询", notes="附件资料表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Wfi_materdata>> queryPageList(Wfi_materdata wfi_materdata,
													  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													  HttpServletRequest req) {
		Result<IPage<Wfi_materdata>> result = new Result<IPage<Wfi_materdata>>();
		QueryWrapper<Wfi_materdata> queryWrapper = QueryGenerator.initQueryWrapper(wfi_materdata, req.getParameterMap());
		Page<Wfi_materdata> page = new Page<Wfi_materdata>(pageNo, pageSize);
		IPage<Wfi_materdata> pageList = wfi_materdataService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param wfi_materdata
	 * @return
	 */
	@AutoLog(value = "附件资料表-添加")
	@ApiOperation(value="附件资料表-添加", notes="附件资料表-添加")
	@PostMapping(value = "/add")
	public Result<Wfi_materdata> add(@RequestBody Wfi_materdata wfi_materdata) {
		Result<Wfi_materdata> result = new Result<Wfi_materdata>();
		try {
			wfi_materdataService.save(wfi_materdata);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param wfi_materdata
	 * @return
	 */
	@AutoLog(value = "附件资料表-编辑")
	@ApiOperation(value="附件资料表-编辑", notes="附件资料表-编辑")
	@PutMapping(value = "/edit")
	public Result<Wfi_materdata> edit(@RequestBody Wfi_materdata wfi_materdata) {
		Result<Wfi_materdata> result = new Result<Wfi_materdata>();
		Wfi_materdata wfi_materdataEntity = wfi_materdataService.getById(wfi_materdata.getId());
		if(wfi_materdataEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = wfi_materdataService.updateById(wfi_materdata);
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
	@AutoLog(value = "附件资料表-通过id删除")
	@ApiOperation(value="附件资料表-通过id删除", notes="附件资料表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			wfi_materdataService.removeById(id);
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
	public Result<Wfi_materdata> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Wfi_materdata> result = new Result<Wfi_materdata>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.wfi_materdataService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "附件资料表-通过id查询")
	@ApiOperation(value="附件资料表-通过id查询", notes="附件资料表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Wfi_materdata> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Wfi_materdata> result = new Result<Wfi_materdata>();
		Wfi_materdata wfi_materdata = wfi_materdataService.getById(id);
		if(wfi_materdata==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(wfi_materdata);
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
      QueryWrapper<Wfi_materdata> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Wfi_materdata wfi_materdata = JSON.parseObject(deString, Wfi_materdata.class);
              queryWrapper = QueryGenerator.initQueryWrapper(wfi_materdata, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Wfi_materdata> pageList = wfi_materdataService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "附件资料表列表");
      mv.addObject(NormalExcelConstants.CLASS, Wfi_materdata.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("附件资料表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Wfi_materdata> listWfi_materdatas = ExcelImportUtil.importExcel(file.getInputStream(), Wfi_materdata.class, params);
              wfi_materdataService.saveBatch(listWfi_materdatas);
              return Result.ok("文件导入成功！数据行数:" + listWfi_materdatas.size());
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

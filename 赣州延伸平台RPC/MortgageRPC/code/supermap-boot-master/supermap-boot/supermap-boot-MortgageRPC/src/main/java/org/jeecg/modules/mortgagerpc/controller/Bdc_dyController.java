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
import org.jeecg.modules.mortgagerpc.entity.Bdc_dy;
import org.jeecg.modules.mortgagerpc.service.IBdc_dyService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 单元信息表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="单元信息表")
@RestController
@RequestMapping("/modules/bdc_dy")
public class Bdc_dyController {
	@Autowired
	private IBdc_dyService bdc_dyService;
	
	/**
	  * 分页列表查询
	 * @param bdc_dy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "单元信息表-分页列表查询")
	@ApiOperation(value="单元信息表-分页列表查询", notes="单元信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Bdc_dy>> queryPageList(Bdc_dy bdc_dy,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Bdc_dy>> result = new Result<IPage<Bdc_dy>>();
		QueryWrapper<Bdc_dy> queryWrapper = QueryGenerator.initQueryWrapper(bdc_dy, req.getParameterMap());
		Page<Bdc_dy> page = new Page<Bdc_dy>(pageNo, pageSize);
		IPage<Bdc_dy> pageList = bdc_dyService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param bdc_dy
	 * @return
	 */
	@AutoLog(value = "单元信息表-添加")
	@ApiOperation(value="单元信息表-添加", notes="单元信息表-添加")
	@PostMapping(value = "/add")
	public Result<Bdc_dy> add(@RequestBody Bdc_dy bdc_dy) {
		Result<Bdc_dy> result = new Result<Bdc_dy>();
		try {
			bdc_dyService.save(bdc_dy);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param bdc_dy
	 * @return
	 */
	@AutoLog(value = "单元信息表-编辑")
	@ApiOperation(value="单元信息表-编辑", notes="单元信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<Bdc_dy> edit(@RequestBody Bdc_dy bdc_dy) {
		Result<Bdc_dy> result = new Result<Bdc_dy>();
		Bdc_dy bdc_dyEntity = bdc_dyService.getById(bdc_dy.getId());
		if(bdc_dyEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bdc_dyService.updateById(bdc_dy);
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
	@AutoLog(value = "单元信息表-通过id删除")
	@ApiOperation(value="单元信息表-通过id删除", notes="单元信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			bdc_dyService.removeById(id);
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
	@AutoLog(value = "单元信息表-批量删除")
	@ApiOperation(value="单元信息表-批量删除", notes="单元信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Bdc_dy> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Bdc_dy> result = new Result<Bdc_dy>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bdc_dyService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "单元信息表-通过id查询")
	@ApiOperation(value="单元信息表-通过id查询", notes="单元信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Bdc_dy> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Bdc_dy> result = new Result<Bdc_dy>();
		Bdc_dy bdc_dy = bdc_dyService.getById(id);
		if(bdc_dy==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bdc_dy);
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
      QueryWrapper<Bdc_dy> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Bdc_dy bdc_dy = JSON.parseObject(deString, Bdc_dy.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bdc_dy, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Bdc_dy> pageList = bdc_dyService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "单元信息表列表");
      mv.addObject(NormalExcelConstants.CLASS, Bdc_dy.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("单元信息表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Bdc_dy> listBdc_dys = ExcelImportUtil.importExcel(file.getInputStream(), Bdc_dy.class, params);
              bdc_dyService.saveBatch(listBdc_dys);
              return Result.ok("文件导入成功！数据行数:" + listBdc_dys.size());
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
	  * 通过流水号查询该项目的单元列表
	  * @return
	  */
	 @AutoLog(value = "单元信息-通过流水号查询该项目的单元列表")
	 @ApiOperation(value="单元信息-通过流水号查询该项目的单元列表", notes="单元信息-通过流水号查询该项目的单元列表")
	 @GetMapping(value = "/queryByLsh")
	 public Result<List<Bdc_dy>> queryByLsh(@RequestParam(name="prolsh",required=true) String prolsh) {
		 Result<List<Bdc_dy>> result = new Result<List<Bdc_dy>>();
		 try {
			 List<Bdc_dy> list = bdc_dyService.list(new QueryWrapper<Bdc_dy>().eq("prolsh", prolsh));
			 result.setSuccess(true);
			 result.setResult(list);
		 } catch (Exception e) {
			 e.printStackTrace();
			 log.error("根据流水号查询单元列表失败",e);
			 result.setSuccess(false);
			 result.setMessage("根据流水号查询单元列表失败");
		 }

		 return result;
	 }




}

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
import org.jeecg.modules.mortgagerpc.entity.Bdc_zs;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.mortgagerpc.service.IBdc_zsService;
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
import org.jeecg.modules.mortgagerpc.entity.Bdc_zs;

 /**
 * @Description: 证书信息
 * @Author: jeecg-boot
 * @Date:   2019-08-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="证书信息")
@RestController
@RequestMapping("/modules/bdc_zs")
public class Bdc_zsController {
	@Autowired
	private IBdc_zsService bdc_zsService;
	
	/**
	  * 分页列表查询
	 * @param bdc_zs
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "证书信息-分页列表查询")
	@ApiOperation(value="证书信息-分页列表查询", notes="证书信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Bdc_zs>> queryPageList(Bdc_zs bdc_zs,
											   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
											   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
											   HttpServletRequest req) {
		//通过申请人姓名查询出申请人id
		Result<IPage<Bdc_zs>> result = new Result<IPage<Bdc_zs>>();
		QueryWrapper<Bdc_zs> queryWrapper = QueryGenerator.initQueryWrapper(bdc_zs, req.getParameterMap());
		Page<Bdc_zs> page = new Page<Bdc_zs>(pageNo, pageSize);
		IPage<Bdc_zs> pageList = bdc_zsService.page(page, queryWrapper);
		result.setSuccess(true);
		if(pageList.getTotal()==0) {
			//如果直接获取证书表没有数据，则手动调用coreapi接口进行获取
			boolean zsExist = bdc_zsService.pushZSToMRPC(bdc_zs.getProlsh());
			if(zsExist) {
				pageList = bdc_zsService.page(page, queryWrapper);
			}
		}
		result.setResult(pageList);
		return result;
	}

	 /**
	  * 分页列表查询
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "项目信息-分页列表查询")
	 @ApiOperation(value="项目信息-分页列表查询", notes="项目信息-分页列表查询")
	 @GetMapping(value = "/zslist")
	 public Result<IPage<Map>> queryPageProjectlist(
			 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			 @RequestParam(name="pageSize", defaultValue="100") Integer pageSize,
			 HttpServletRequest req) {
		 Result<IPage<Map>> result = new Result<IPage<Map>>();
		 Page<Map> page = new Page<Map>(pageNo, pageSize);
		 IPage<Map> pageList = bdc_zsService.zslist(page, req);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	/**
	  *   添加
	 * @param bdc_zs
	 * @return
	 */
	@AutoLog(value = "证书信息-添加")
	@ApiOperation(value="证书信息-添加", notes="证书信息-添加")
	@PostMapping(value = "/add")
	public Result<Bdc_zs> add(@RequestBody Bdc_zs bdc_zs) {
		Result<Bdc_zs> result = new Result<Bdc_zs>();
		try {
			bdc_zsService.save(bdc_zs);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param bdc_zs
	 * @return
	 */
	@AutoLog(value = "证书信息-编辑")
	@ApiOperation(value="证书信息-编辑", notes="证书信息-编辑")
	@PutMapping(value = "/edit")
	public Result<Bdc_zs> edit(@RequestBody Bdc_zs bdc_zs) {
		Result<Bdc_zs> result = new Result<Bdc_zs>();
		Bdc_zs bdc_zsEntity = bdc_zsService.getById(bdc_zs.getZsid());
		if(bdc_zsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bdc_zsService.updateById(bdc_zs);
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
	@AutoLog(value = "证书信息-通过id删除")
	@ApiOperation(value="证书信息-通过id删除", notes="证书信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			bdc_zsService.removeById(id);
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
	@AutoLog(value = "证书信息-批量删除")
	@ApiOperation(value="证书信息-批量删除", notes="证书信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Bdc_zs> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Bdc_zs> result = new Result<Bdc_zs>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bdc_zsService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "证书信息-通过id查询")
	@ApiOperation(value="证书信息-通过id查询", notes="证书信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Bdc_zs> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Bdc_zs> result = new Result<Bdc_zs>();
		Bdc_zs bdc_zs = bdc_zsService.getById(id);
		if(bdc_zs==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bdc_zs);
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
      QueryWrapper<Bdc_zs> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Bdc_zs bdc_zs = JSON.parseObject(deString, Bdc_zs.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bdc_zs, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Bdc_zs> pageList = bdc_zsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "证书信息列表");
      mv.addObject(NormalExcelConstants.CLASS, Bdc_zs.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("证书信息列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Bdc_zs> listBdc_zss = ExcelImportUtil.importExcel(file.getInputStream(), Bdc_zs.class, params);
              bdc_zsService.saveBatch(listBdc_zss);
              return Result.ok("文件导入成功！数据行数:" + listBdc_zss.size());
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

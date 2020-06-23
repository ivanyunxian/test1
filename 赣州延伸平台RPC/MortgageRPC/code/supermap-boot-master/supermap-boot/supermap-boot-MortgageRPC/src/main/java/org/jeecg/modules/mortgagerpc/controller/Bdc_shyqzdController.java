package org.jeecg.modules.mortgagerpc.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.service.IBdc_shyqzdService;
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

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "宗地信息表")
@RestController
@RequestMapping("/modules/yspt/shyqzd")
public class Bdc_shyqzdController {

	@Autowired
	private IBdc_shyqzdService Bdc_shyqzdService;

	/**
	 * 分页列表查询
	 * 
	 * @param bdc_dy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "宗地信息表-分页列表查询")
	@ApiOperation(value = "宗地信息表-分页列表查询", notes = "宗地信息表-分页列表查询")
	@GetMapping(value = "/bdc_shyqzd/list")
	public Result<IPage<Bdc_shyqzd>> queryPageList(Bdc_shyqzd bdc_shyqzd,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<Bdc_shyqzd>> result = new Result<IPage<Bdc_shyqzd>>();
		QueryWrapper<Bdc_shyqzd> queryWrapper = QueryGenerator.initQueryWrapper(bdc_shyqzd, req.getParameterMap());
		Page<Bdc_shyqzd> page = new Page<Bdc_shyqzd>(pageNo, pageSize);
		IPage<Bdc_shyqzd> pageList = Bdc_shyqzdService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 分页列表查询
	 * 
	 * @param bdc_dy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "宗地信息表-分页列表查询")
	@ApiOperation(value = "宗地信息表-分页列表查询", notes = "宗地信息表-分页列表查询")
	@GetMapping(value = "/bllist")
	public Result<IPage<Bdc_shyqzd>> queryPageBlList(Bdc_shyqzd bdc_shyqzd,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<Bdc_shyqzd>> result = new Result<IPage<Bdc_shyqzd>>();
		QueryWrapper<Bdc_shyqzd> queryWrapper = QueryGenerator.initQueryWrapper(bdc_shyqzd, req.getParameterMap());
		Page<Bdc_shyqzd> page = new Page<Bdc_shyqzd>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		queryWrapper.eq("ENTERPRISEID", sysUser.getEnterpriseId()).ne("STATUS", "1");
		IPage<Bdc_shyqzd> pageList = Bdc_shyqzdService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		result.setMessage(sysUser.getEnterpriseId());
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param bdc_shyqzd
	 * @return
	 */
	@AutoLog(value = "宗地信息表-添加")
	@ApiOperation(value = "宗地信息表-添加", notes = "宗地信息表-添加")
	@PostMapping(value = "/bdc_shyqzd/add")
	public Result<Bdc_shyqzd> add(@RequestBody Bdc_shyqzd bdc_shyqzd) {
		Result<Bdc_shyqzd> result = new Result<Bdc_shyqzd>();
		try {
			Bdc_shyqzdService.save(bdc_shyqzd);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 编辑
	 * 
	 * @param bdc_shyqzd
	 * @return
	 */
	@AutoLog(value = "宗地信息表-编辑")
	@ApiOperation(value = "宗地信息表-编辑", notes = "宗地信息表-编辑")
	@PutMapping(value = "/bdc_shyqzd/edit")
	public Result<Bdc_shyqzd> edit(@RequestBody Bdc_shyqzd bdc_shyqzd) {
		Result<Bdc_shyqzd> result = new Result<Bdc_shyqzd>();
		Bdc_shyqzd bdc_dyEntity = Bdc_shyqzdService.getById(bdc_shyqzd.getId());
		if (bdc_dyEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = Bdc_shyqzdService.updateById(bdc_shyqzd);
			// TODO 返回false说明什么？
			if (ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宗地信息表-通过id删除")
	@ApiOperation(value = "宗地信息表-通过id删除", notes = "宗地信息表-通过id删除")
	@DeleteMapping(value = "/bdc_shyqzd/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		try {
			Bdc_shyqzdService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败", e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "宗地信息表-批量删除")
	@ApiOperation(value = "宗地信息表-批量删除", notes = "宗地信息表-批量删除")
	@DeleteMapping(value = "/bdc_shyqzd/deleteBatch")
	public Result<Bdc_shyqzd> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<Bdc_shyqzd> result = new Result<Bdc_shyqzd>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			this.Bdc_shyqzdService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "宗地信息表-通过id查询")
	@ApiOperation(value = "宗地信息表-通过id查询", notes = "宗地信息表-通过id查询")
	@GetMapping(value = "/bdc_shyqzd/queryById")
	public Result<Bdc_shyqzd> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<Bdc_shyqzd> result = new Result<Bdc_shyqzd>();
		Bdc_shyqzd bdc_shyqzd = Bdc_shyqzdService.getById(id);
		if (bdc_shyqzd == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(bdc_shyqzd);
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
	@RequestMapping(value = "/bdc_shyqzd/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
		// Step.1 组装查询条件
		QueryWrapper<Bdc_shyqzd> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				Bdc_shyqzd bdc_shyqzd = JSON.parseObject(deString, Bdc_shyqzd.class);
				queryWrapper = QueryGenerator.initQueryWrapper(bdc_shyqzd, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<Bdc_shyqzd> pageList = Bdc_shyqzdService.list(queryWrapper);
		// 导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "宗地信息表列表");
		mv.addObject(NormalExcelConstants.CLASS, Bdc_shyqzd.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("宗地信息表列表数据", "导出人:Jeecg", "导出信息"));
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
	@RequestMapping(value = "/bdc_shyqzd/importExcel", method = RequestMethod.POST)
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
				List<Bdc_shyqzd> listBdc_dys = ExcelImportUtil.importExcel(file.getInputStream(), Bdc_shyqzd.class,
						params);
				Bdc_shyqzdService.saveBatch(listBdc_dys);
				return Result.ok("文件导入成功！数据行数:" + listBdc_dys.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
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
	 * 通过流水号查询该项目的宗地列表
	 * 
	 * @return
	 */
	@AutoLog(value = "宗地信息-通过企业id查询该项目的宗地列表")
	@ApiOperation(value = "宗地信息-通过企业id查询该项目的宗地列表", notes = "宗地信息-通过企业id查询该项目的宗地列表")
	@GetMapping(value = "/bdc_shyqzd/queryByEnterpriseId")
	public Result<List<Bdc_shyqzd>> queryByEnterpriseId(
			@RequestParam(name = "enterpriseid", required = true) String enterpriseid) {
		Result<List<Bdc_shyqzd>> result = new Result<List<Bdc_shyqzd>>();
		try {
			List<Bdc_shyqzd> list = Bdc_shyqzdService
					.list(new QueryWrapper<Bdc_shyqzd>().eq("ENTERPRISEID", enterpriseid));
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("根据企业id查询宗地列表失败", e);
			result.setSuccess(false);
			result.setMessage("根据企业id查询宗地列表失败");
		}

		return result;
	}

	/**
	 * 通过流水号查询该项目的宗地列表
	 * 
	 * @return
	 */
	@AutoLog(value = "宗地信息-通过企业id查询该项目的宗地列表")
	@ApiOperation(value = "宗地信息-通过企业id查询该项目的宗地列表", notes = "宗地信息-通过企业id查询该项目的宗地列表")
	@GetMapping(value = "/queryByEnterpriseId")
	public Result<List<Bdc_shyqzd>> queryByEnterpriseIdNoPass(
			@RequestParam(name = "enterpriseid", required = true) String enterpriseid) {
		Result<List<Bdc_shyqzd>> result = new Result<List<Bdc_shyqzd>>();
		try {
			// 新增只查未提交的
			List<Bdc_shyqzd> list = Bdc_shyqzdService
					.list(new QueryWrapper<Bdc_shyqzd>().eq("ENTERPRISEID", enterpriseid).eq("STATUS", "-1"));
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("根据企业id查询宗地列表失败", e);
			result.setSuccess(false);
			result.setMessage("根据企业id查询宗地列表失败");
		}

		return result;
	}

}

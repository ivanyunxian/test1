package org.jeecg.modules.mortgagerpc.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mortgagerpc.entity.*;
import org.jeecg.modules.mortgagerpc.service.IWfi_proinstService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
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
import com.alibaba.fastjson.JSONArray;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;

  /**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2019-07-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="项目信息")
@RestController
@RequestMapping("/modules/wfi_proinst")
public class Wfi_proinstController {
	@Autowired
	private IWfi_proinstService wfi_proinstService;

	@Autowired
	private ISysDepartService sysDepartService;


	 /**
	  * 分页列表查询
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @SuppressWarnings("rawtypes")
	@AutoLog(value = "项目信息-分页列表查询")
	 @ApiOperation(value="项目信息-分页列表查询", notes="项目信息-分页列表查询")
	 @GetMapping(value = "/projectlist")
	 public Result<IPage<Map>> queryPageProjectlist(
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		 Result<IPage<Map>> result = new Result<IPage<Map>>();
		 Page<Map> page = new Page<Map>(pageNo, pageSize);
		 IPage<Map> pageList = wfi_proinstService.projectlist(page, req);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	/**
	  * 分页列表查询
	 * @param wfi_proinst
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目信息-分页列表查询")
	@ApiOperation(value="项目信息-分页列表查询", notes="项目信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Wfi_proinst>> queryPageList(Wfi_proinst wfi_proinst,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Wfi_proinst>> result = new Result<IPage<Wfi_proinst>>();
		QueryWrapper<Wfi_proinst> queryWrapper = QueryGenerator.initQueryWrapper(wfi_proinst, req.getParameterMap());
		Page<Wfi_proinst> page = new Page<Wfi_proinst>(pageNo, pageSize);
		IPage<Wfi_proinst> pageList = wfi_proinstService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param wfi_proinst
	 * @return
	 */
	@AutoLog(value = "项目信息-添加")
	@ApiOperation(value="项目信息-添加", notes="项目信息-添加")
	@PostMapping(value = "/add")
	public Result<Wfi_proinst> add(@RequestBody Wfi_proinst wfi_proinst) {
		Result<Wfi_proinst> result = new Result<Wfi_proinst>();
		try {
			wfi_proinstService.save(wfi_proinst);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  *  编辑
	 * @param wfi_proinst
	 * @return
	 */
	@AutoLog(value = "项目信息-编辑")
	@ApiOperation(value="项目信息-编辑", notes="项目信息-编辑")
	@PutMapping(value = "/edit")
	public Result<Wfi_proinst> edit(@RequestBody Wfi_proinst wfi_proinst) {
		Result<Wfi_proinst> result = new Result<Wfi_proinst>();
		Wfi_proinst wfi_proinstEntity = wfi_proinstService.getById(wfi_proinst.getProinstId());
		if(wfi_proinstEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = wfi_proinstService.updateById(wfi_proinst);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
	  *   通过id删除
	 * @return
	 */
	@AutoLog(value = "项目信息-通过id删除")
	@ApiOperation(value="项目信息-通过id删除", notes="项目信息-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="prolsh",required=true) String prolsh) {
		try {
			Wfi_proinst wfi_proinst = wfi_proinstService.getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));
			if(wfi_proinst ==  null) {
				throw new SupermapBootException("未找到对应的项目信息-流水号："+prolsh);
			}
			boolean canEdit = wfi_proinstService.canEdit(wfi_proinst);
			if(canEdit) {
				throw new SupermapBootException("该项目已经提交，不能删除");
			}
			wfi_proinstService.deleteProject(wfi_proinst);

		} catch(SupermapBootException e) {
			log.error(e.getMessage(), e);
			return Result.error("操作失败："+e.getMessage());
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
	@AutoLog(value = "项目信息-批量删除")
	@ApiOperation(value="项目信息-批量删除", notes="项目信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Wfi_proinst> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Wfi_proinst> result = new Result<Wfi_proinst>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.wfi_proinstService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目信息-通过id查询")
	@ApiOperation(value="项目信息-通过id查询", notes="项目信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Wfi_proinst> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Wfi_proinst> result = new Result<Wfi_proinst>();
		Wfi_proinst wfi_proinst = wfi_proinstService.getById(id);
		if(wfi_proinst==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(wfi_proinst);
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
      QueryWrapper<Wfi_proinst> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Wfi_proinst wfi_proinst = JSON.parseObject(deString, Wfi_proinst.class);
              queryWrapper = QueryGenerator.initQueryWrapper(wfi_proinst, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Wfi_proinst> pageList = wfi_proinstService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "项目信息列表");
      mv.addObject(NormalExcelConstants.CLASS, Wfi_proinst.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("项目信息列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Wfi_proinst> listWfi_proinsts = ExcelImportUtil.importExcel(file.getInputStream(), Wfi_proinst.class, params);
              wfi_proinstService.saveBatch(listWfi_proinsts);
              return Result.ok("文件导入成功！数据行数:" + listWfi_proinsts.size());
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
	  * 保存申报信息，（暂存）
	  *
	  * @return
	  */
	 @SuppressWarnings("rawtypes")
	@AutoLog(value = "项目信息-暂存")
	 @ApiOperation(value = "项目信息-暂存", notes = "项目信息-暂存")
	 @PostMapping(value = "/saveproejct")
	 public Result<Map> saveproejct(@RequestBody JSONObject projectData) {
		 Result<Map> result = new Result<Map>();
		 try {
			 JSONObject projectDataJson = projectData.getJSONObject("projectdata");
			 wfi_proinstService.saveproject(projectDataJson);
			 result.success("保存成功！");
		 } catch (SupermapBootException e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败："+e.getMessage());
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	  /**
	   * 通过流水号获取项目信息
	   *
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	  @AutoLog(value = "项目信息-通过流水号获取项目信息")
	  @ApiOperation(value = "项目信息-通过流水号获取项目信息", notes = "项目信息-通过流水号获取项目信息")
	  @GetMapping(value = "/projectMessage")
	  public Result<Map> projectMessage(String prolsh,String dyid) {
		  Result<Map> result = new Result<Map>();
		  try {
              Map<String, Object> resultmap = wfi_proinstService.getProjectMessage(prolsh,dyid);
              result.setResult(resultmap);
              result.success("获取成功！");
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败："+e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败");
		  }
		  return result;
	  }


	 /**
	  *  创建proinst项目接口
	  * param wfi_proinst
	  * @return
	  */
	 @AutoLog(value = "项目信息-创建")
	 @ApiOperation(value="项目信息-创建", notes="项目信息-创建")
	 @GetMapping(value = "/createProject")
	 public Result<?> createProject(@RequestParam(name="id",required=true) String  wfi_prodefid,@RequestParam(name="code",required=true) String  code){

		 try {
			 String prolsh = wfi_proinstService.createProject(wfi_prodefid,code);
			 return Result.ok(prolsh);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 return Result.error("创建失败："+e.getMessage());
		 }
	 }
	 
	 /**
	  *  创建proinst项目接口
	  * param wfi_proinst
	  * @return
	  */
	 @AutoLog(value = "用户信息-获取")
	 @ApiOperation(value="用户信息-获取", notes="用户信息-获取")
	 @GetMapping(value = "/userInfo")
	 public Result<?> userInfo(@RequestParam(name="prolsh",required=true) String  prolsh,HttpServletRequest request, HttpServletResponse response){
		 String djlx = "";
		 LoginUser sysUser = new LoginUser();
		 SysDepart depart = new SysDepart();
		 JSONObject json = wfi_proinstService.getProjectByProlsh(prolsh);
		 if(json.getJSONObject("prodef")!=null) {
			 djlx = json.getJSONObject("prodef").getString("djlx") == null?"":json.getJSONObject("prodef").getString("djlx");
		 }
		 if(djlx.equals("300")|| djlx.equals("400")) {
//			 String username = JwtUtil.getUserNameByToken(request);
//			 SysUser user = sysUserService.getUserByName(username);
//			 List<DepartIdModel> DepartModels = sysUserDepartService.queryDepartIdsOfUser(user.getId());
//			 for(DepartIdModel departmodels:DepartModels) {
//				 department.add(sysDepartService.getById(departmodels.getKey()));
//			 }
			 sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
			 depart = sysDepartService.getById(sysUser.getDeptId());
		 }
		 return Result.ok(depart);
	 }


	  /**
	   * 已办箱：分页列表查询
	   * @param pageNo
	   * @param pageSize
	   * @param req
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	@AutoLog(value = "已办箱：分页列表查询")
	  @ApiOperation(value="已办箱：分页列表查询", notes="已办箱：分页列表查询")
	  @GetMapping(value = "/projectlistendbox")
	  public Result<IPage<Map>> queryPageProjectListEndBox(
			  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			  HttpServletRequest req) {
		  Result<IPage<Map>> result = new Result<IPage<Map>>();
		  Page<Map> page = new Page<Map>(pageNo, pageSize);
		  IPage<Map> pageList = wfi_proinstService.projectlistendbox(page, req);
		  result.setSuccess(true);
		  result.setResult(pageList);
		  return result;
	  }

	  /**
	   * 通过流水号获取项目信息
	   *
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	@AutoLog(value = "项目信息-通过流水号获取项目信息")
	  @ApiOperation(value = "项目信息-通过流水号获取项目信息", notes = "项目信息-通过流水号获取项目信息")
	  @GetMapping(value = "/submitProject")
	  public Result<Map> submitProject(String prolsh) {
		  Result<Map> result = new Result<Map>();
		  try {
			  wfi_proinstService.submitProject(prolsh);
			  result.success("提交成功！");
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("提交失败："+e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("提交失败");
		  }
		  return result;
	  }


	  /**
	   * 获取房源信息接口
	   * 获取到房源信息后
	   *
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	  @AutoLog(value = "项目信息-获取房源信息接口")
	  @ApiOperation(value = "项目信息-获取房源信息接口", notes = "项目信息-获取房源信息接口")
	  @GetMapping(value = "/houseSearch")
	  public Result<Map> houseSearch(String bdcqzh, String qlrmc, String qlrzjh,String prolsh) {
		  Result<Map> result = new Result<Map>();
		  try {
              Map<String, String> param = new HashMap<>();
              param.put("bdcqzh", bdcqzh);
              param.put("qlrmc", qlrmc);
              param.put("qlrzjh", qlrzjh);
			  param.put("prolsh",prolsh);
              wfi_proinstService.houseSearch(param);
			  result.success("获取成功！");
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败：" + e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("房源核查接口异常，请联系管理员核查问题");
		  }
		  return result;
	  }
	  
	  /**
	   *  创建proinst项目接口
	   * param wfi_proinst
	   * @return
	   */
	  @AutoLog(value = "项目信息-根据房源核验返回的数据创建项目")
	  @ApiOperation(value="项目信息-根据房源核验返回的数据创建项目", notes="项目信息-根据房源核验返回的数据创建项目")
	  @PostMapping(value = "/createProjectByData")
	  public Result<String> createProjectByData(@RequestBody JSONObject projectData){
		  Result<String> result = new Result<String>();
		  try {
			  String prolsh = wfi_proinstService.createProject(projectData);
			  result.setResult(prolsh);
			  result.setSuccess(true);
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("创建失败：" + e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("创建失败");
		  }
		  return result;
	  }

	  /**
	   *  从第二步返回第一步重新选择单元情况下，把表里的单元换成新的
	   * param wfi_proinst
	   * @return
	   */
	  @AutoLog(value = "项目信息- 从第二步返回第一步重新选择单元情况下，把表里的单元换成新的")
	  @ApiOperation(value="项目信息- 从第二步返回第一步重新选择单元情况下，把表里的单元换成新的", notes="项目信息- 从第二步返回第一步重新选择单元情况下，把表里的单元换成新的")
	  @PostMapping(value = "/updataBdcDy")
	  public Result<String> updataBdcDy(@RequestBody JSONObject projectjson){
		  Result<String> result = new Result<String>();
		  try {
			  wfi_proinstService.updataBdcDy(projectjson);
			  result.setSuccess(true);
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("创建失败：" + e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("创建失败");
		  }
		  return result;
	  }


      /**
       *  分步受理表单第二步保存项目
       * param wfi_proinst
       * @return
       */
      @AutoLog(value = "项目信息-分步受理表单第二步保存项目")
      @ApiOperation(value="项目信息-分步受理表单第二步保存项目", notes="项目信息-分步受理表单第二步保存项目")
      @PostMapping(value = "/saveProjectForStep2")
      public Result<String> saveProjectForStep2(@RequestBody JSONObject projectData){
          Result<String> result = new Result<String>();
          try {
              wfi_proinstService.saveProjectForStep2(projectData);
              result.setSuccess(true);
          } catch (SupermapBootException e) {
              log.error(e.getMessage(), e);
              result.error500("创建失败：" + e.getMessage());
          } catch (Exception e) {
              log.error(e.getMessage(), e);
              result.error500("创建失败");
          }
          return result;
      }

	  /**
	   *  获取项目进度
	   * param wfi_proinst
	   * @return
	   */
	  @AutoLog(value = "项目信息-获取项目进度")
	  @ApiOperation(value="项目信息-获取项目进度", notes="项目信息-获取项目进度")
	  @GetMapping(value = "/getProjectProgress")
	  public Result<JSONObject> getProjectProgress(String prolsh){
		  Result<JSONObject> result = new Result<JSONObject>();
		  try {
			  JSONObject process = wfi_proinstService.getProjectProgress(prolsh);
			  result.setResult(process);
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
       *  选择单元
       * param wfi_proinst
       * @return
       */
      @AutoLog(value = "项目信息-选择单元")
      @ApiOperation(value="项目信息-选择单元", notes="项目信息-选择单元")
      @PostMapping(value = "/selecthouse")
      public Result<JSONObject> selecthouse(@RequestBody JSONObject housesdata){
          Result<JSONObject> result = new Result<JSONObject>();
          try {
              wfi_proinstService.selecthouse(housesdata);
              result.setSuccess(true);
          } catch (SupermapBootException e) {
              log.error(e.getMessage(), e);
              result.error500("添加失败：" + e.getMessage());
          } catch (Exception e) {
              log.error(e.getMessage(), e);
              result.error500("添加失败");
          }
          return result;
      }


	  /**
	   *  移除单元
	   * param wfi_proinst
	   * @return
	   */
	  @AutoLog(value = "项目信息-移除单元")
	  @ApiOperation(value="项目信息-移除单元", notes="项目信息-移除单元")
	  @GetMapping(value = "/removehouse")
	  public Result<JSONObject> removehouse(String houseid,String prolsh){
		  Result<JSONObject> result = new Result<JSONObject>();
		  try {
			  wfi_proinstService.removehouse(houseid, prolsh);
			  result.setSuccess(true);
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("移除单元失败：" + e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("移除单元失败");
		  }
		  return result;
	  }


	  /**
	   *  重新设置项目名称
	   * param wfi_proinst
	   * @return
	   */
	  @AutoLog(value = "项目信息-重新设置项目名称")
	  @ApiOperation(value="项目信息-重新设置项目名称", notes="项目信息-重新设置项目名称")
	  @GetMapping(value = "/setNameAndInitDYQR")
	  public Result<JSONObject> setNameAndInitDYQR(String prolsh){
		  Result<JSONObject> result = new Result<JSONObject>();
		  try {
			  wfi_proinstService.setNameAndInitDYQR(prolsh);
			  result.setSuccess(true);
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("重新设置项目名称：" + e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("重新设置项目名称");
		  }
		  return result;
	  }


	  /**
	   *  从第二步返回第一步重新选择单元情况下，把表里的单元换成新的
	   * param wfi_proinst
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	  @AutoLog(value = "申报成功-回执单数据")
	  @ApiOperation(value="申报成功-回执单数据", notes="申报成功-回执单数据")
	  @GetMapping(value = "/getRecipientData")
	  public Result<Map> getRecipientData(@RequestParam(name="prolsh",required=true) String prolsh){
		  Result<Map> result = new Result<Map>();
		  try {
			  Map<String, Object> resultmap = wfi_proinstService.getRecipientData(prolsh);
			  result.setResult(resultmap);
			  result.success("获取成功！");
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败："+e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败");
		  }
		  return result;
	  }


	  /**
	   *  房源核验，第一步权证号检索
	   * param wfi_proinst
	   * @return
	   */
	  @AutoLog(value = "房源核验-检索权证号")
	  @ApiOperation(value="房源核验-检索权证号", notes="房源核验-检索权证号")
	  @GetMapping(value = "/searchBdcqzh")
	  public Result<JSONObject> searchBdcqzh(@RequestParam(name="bdcqzh",required=true) String bdcqzh){
		  Result<JSONObject> result = new Result<JSONObject>();
		  try {
			  JSONObject resultmap = wfi_proinstService.searchBdcqzh(bdcqzh);
			  result.setResult(resultmap);
			  result.success("检索成功！");
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("检索失败："+e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("检索失败");
		  }
		  return result;
	  }


	  /**
	   * 根据流水号获取基本项目信息
	   * param wfi_proinst
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	  @AutoLog(value = "获取项目信息-根据流水号获取基本项目信息")
	  @ApiOperation(value="获取项目信息-根据流水号获取基本项目信息", notes="获取项目信息-根据流水号获取基本项目信息")
	  @GetMapping(value = "/getProjectByProlsh")
	  public Result<Map> getProjectByProlsh(@RequestParam(name="prolsh",required=true) String prolsh){
		  Result<Map> result = new Result<Map>();
		  try {
			  JSONObject resultmap = wfi_proinstService.getProjectByProlsh(prolsh);
			  result.setResult(resultmap);
			  result.success("获取成功！");
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败："+e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败");
		  }
		  return result;
	  }

  }

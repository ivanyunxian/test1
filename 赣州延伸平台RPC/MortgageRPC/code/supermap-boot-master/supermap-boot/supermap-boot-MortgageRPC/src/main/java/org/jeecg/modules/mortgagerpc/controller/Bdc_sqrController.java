package org.jeecg.modules.mortgagerpc.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mortgagerpc.entity.Bdc_fsql;
import org.jeecg.modules.mortgagerpc.entity.Bdc_qldy;
import org.jeecg.modules.mortgagerpc.entity.Bdc_sqr;
import org.jeecg.modules.mortgagerpc.entity.Wfi_proinst;
import org.jeecg.modules.mortgagerpc.service.IBdc_fsqlService;
import org.jeecg.modules.mortgagerpc.service.IBdc_qldyService;
import org.jeecg.modules.mortgagerpc.service.IBdc_sqrService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.mortgagerpc.service.IWfi_proinstService;
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
 * @Description: 申请人表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="申请人表")
@RestController
@RequestMapping("/modules/bdc_sqr")
public class Bdc_sqrController {
	@Autowired
	private IBdc_sqrService bdc_sqrService;
	@Autowired
	private IWfi_proinstService wfi_proinstService;
	 @Autowired
	 IBdc_qldyService bdc_qldyService;
	 @Autowired
	 IBdc_fsqlService bdc_fsqlService;
	/**
	  * 分页列表查询
	 * @param bdc_sqr
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "申请人表-分页列表查询")
	@ApiOperation(value="申请人表-分页列表查询", notes="申请人表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Bdc_sqr>> queryPageList(Bdc_sqr bdc_sqr,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Bdc_sqr>> result = new Result<IPage<Bdc_sqr>>();
		QueryWrapper<Bdc_sqr> queryWrapper = QueryGenerator.initQueryWrapper(bdc_sqr, req.getParameterMap());
		Page<Bdc_sqr> page = new Page<Bdc_sqr>(pageNo, pageSize);
		IPage<Bdc_sqr> pageList = bdc_sqrService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 /**
	  * 申请人列表，根据业务流水号，申请人类型查找对应的申请人
	  * @return
	  */
	 @AutoLog(value = "申请人列表，根据业务流水号，申请人类型查找对应的申请人")
	 @ApiOperation(value="申请人列表，根据业务流水号，申请人类型查找对应的申请人", notes="申请人列表，根据业务流水号，申请人类型查找对应的申请人")
	 @GetMapping(value = "/sqrlist")
	 public Result<List<Bdc_sqr>> queryProSqrList(String prolsh, String sqrlb,String dyid) {
		 Result<List<Bdc_sqr>> result = new Result<List<Bdc_sqr>>();
		 String insql = "SELECT T.SQRID FROM BDC_MRPC.BDC_QLDY T WHERE T.DYID='"+dyid+"'";
		 List<Bdc_sqr> pageList = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("prolsh",prolsh).eq("sqrlb",sqrlb).inSql("ID",insql));
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }



	 /**
	  *   添加
	 * @return
	 */
	@AutoLog(value = "申请人表-添加")
	@ApiOperation(value="申请人表-添加", notes="申请人表-添加")
	@PostMapping(value = "/add")
	public Result<Bdc_sqr> add(@RequestBody JSONObject dyjson) {
		Result<Bdc_sqr> result = new Result<Bdc_sqr>();
		try {
			String dyid = dyjson.getString("dyid");
			Bdc_sqr bdc_sqr = dyjson.toJavaObject(Bdc_sqr.class);
			bdc_sqr.setId(StringHelper.getUUID());
			String prolsh = bdc_sqr.getProlsh();
			Wfi_proinst wfi_proinst = wfi_proinstService.getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));
			if(wfi_proinst ==  null) {
				throw new SupermapBootException("未找到对应的项目信息-流水号："+prolsh);
			}
			boolean canEdit = wfi_proinstService.canEdit(wfi_proinst);
			if(canEdit) {
				throw new SupermapBootException("该项目已经提交，不能重复操作");
			}

			// 新增申请人添加构建关系记录
			List<Bdc_fsql> fsqlBydyid = bdc_fsqlService.getFsqlBydyid(prolsh, dyid);
			String fsqlid = "";
			if (!fsqlBydyid.isEmpty()) {
				fsqlid = fsqlBydyid.get(0).getFsqlid();
			}
			Bdc_qldy qldy = new Bdc_qldy();
			qldy.setSqrid(bdc_sqr.getId());
			qldy.setDyid(dyid);
			qldy.setProlsh(prolsh);
			qldy.setFsqlid(fsqlid);
			qldy.setCreatetime(new Date());
			bdc_qldyService.save(qldy);
			bdc_sqrService.save(bdc_sqr);
			result.success("添加成功！");
		} catch (SupermapBootException e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败："+e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param bdc_sqr
	 * @return
	 */
	@AutoLog(value = "申请人表-编辑")
	@ApiOperation(value="申请人表-编辑", notes="申请人表-编辑")
	@PutMapping(value = "/edit")
	public Result<Bdc_sqr> edit(@RequestBody Bdc_sqr bdc_sqr) {
		Result<Bdc_sqr> result = new Result<Bdc_sqr>();
		try {
			Bdc_sqr bdc_sqrEntity = bdc_sqrService.getById(bdc_sqr.getId());
			if(bdc_sqrEntity==null) {
				throw new SupermapBootException("未找到对应实体");
			}

			String prolsh = bdc_sqr.getProlsh();
			Wfi_proinst wfi_proinst = wfi_proinstService.getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));
			if(wfi_proinst ==  null) {
				throw new SupermapBootException("未找到对应的项目信息-流水号："+prolsh);
			}
			boolean canEdit = wfi_proinstService.canEdit(wfi_proinst);
			if(canEdit) {
				throw new SupermapBootException("该项目已经提交，不能重复操作");
			}
			
			boolean ok = bdc_sqrService.updateById(bdc_sqr);
			if(bdc_sqr.getDlrxm()!=null) {
				List<Bdc_sqr> sqrs = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("prolsh", prolsh).eq("sqrlb", bdc_sqr.getSqrlb()));
				for(Bdc_sqr sqr:sqrs) {
					sqr.setDlrxm(bdc_sqr.getDlrxm());
					sqr.setDlrzjlx(bdc_sqr.getDlrzjlx());
					sqr.setDlrzjhm(bdc_sqr.getDlrzjhm());
					sqr.setDlrlxdh(bdc_sqr.getDlrlxdh());
					bdc_sqrService.updateById(sqr);
				}
			}
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}

		} catch (SupermapBootException e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败："+e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "申请人表-通过id删除")
	@ApiOperation(value="申请人表-通过id删除", notes="申请人表-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			Bdc_sqr bdc_sqrEntity = bdc_sqrService.getById(id);
			if(bdc_sqrEntity==null) {
				throw new SupermapBootException("未找到对应实体");
			}

			String prolsh = bdc_sqrEntity.getProlsh();
			Wfi_proinst wfi_proinst = wfi_proinstService.getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));
			if(wfi_proinst ==  null) {
				throw new SupermapBootException("未找到对应的项目信息-流水号："+prolsh);
			}
			boolean canEdit = wfi_proinstService.canEdit(wfi_proinst);
			if(canEdit) {
				throw new SupermapBootException("该项目已经提交，不能重复操作");
			}

			bdc_sqrService.removeById(id);
		} catch (SupermapBootException e) {
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
	@AutoLog(value = "申请人表-批量删除")
	@ApiOperation(value="申请人表-批量删除", notes="申请人表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Bdc_sqr> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Bdc_sqr> result = new Result<Bdc_sqr>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bdc_sqrService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "申请人表-通过id查询")
	@ApiOperation(value="申请人表-通过id查询", notes="申请人表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Bdc_sqr> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Bdc_sqr> result = new Result<Bdc_sqr>();
		Bdc_sqr bdc_sqr = bdc_sqrService.getById(id);
		if(bdc_sqr==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bdc_sqr);
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
      QueryWrapper<Bdc_sqr> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Bdc_sqr bdc_sqr = JSON.parseObject(deString, Bdc_sqr.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bdc_sqr, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Bdc_sqr> pageList = bdc_sqrService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "申请人表列表");
      mv.addObject(NormalExcelConstants.CLASS, Bdc_sqr.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("申请人表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Bdc_sqr> listBdc_sqrs = ExcelImportUtil.importExcel(file.getInputStream(), Bdc_sqr.class, params);
              bdc_sqrService.saveBatch(listBdc_sqrs);
              return Result.ok("文件导入成功！数据行数:" + listBdc_sqrs.size());
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

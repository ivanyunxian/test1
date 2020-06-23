package org.jeecg.modules.mortgagerpc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.modules.mortgagerpc.service.IYspt_ywsqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "项目信息")
@RestController
@RequestMapping("/modules/yspt")
public class Yspt_ywsqController {

	@Autowired
	private IYspt_ywsqService yspt_ywsqService;

	
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
		 IPage<Map> pageList = yspt_ywsqService.projectlist(page, req);
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
	public @ResponseBody Result<Map> submitProject(String enterpriseid,String type) {
		Result<Map> result = new Result<Map>();
		try {
			yspt_ywsqService.submitProject(enterpriseid,type);
			result.success("提交成功！");
		} catch (SupermapBootException e) {
			log.error(e.getMessage(), e);
			result.error500("提交失败：" + e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("提交失败");
		}
		return result;
	}
	
	/**
	   *  房源核验，第二步宗地号检索
	   * param 
	   * @return
	   */
	  @AutoLog(value = "宗地核验-检索宗地号")
	  @ApiOperation(value="宗地核验-检索宗地号", notes="宗地核验-检索宗地号")
	  @GetMapping(value = "/searchBdcdyh")
	  public Result<JSONObject> searchBdcdyh(@RequestParam(name="bdcdyh",required=true) String bdcdyh){
		  Result<JSONObject> result = new Result<JSONObject>();
		  try {
			  JSONObject resultmap = yspt_ywsqService.searchBdcdyh(bdcdyh);
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
	   * 获取房源信息接口
	   * 获取到房源信息后
	   *
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	  @AutoLog(value = "项目信息-获取宗地信息接口")
	  @ApiOperation(value = "项目信息-获取宗地信息接口", notes = "项目信息-获取宗地信息接口")
	  @GetMapping(value = "/zdSearch")
	  public Result<Map> zdSearch(String bdcdyh, String qlrmc, String qlrzjh,String enterpriseid) {
		  Result<Map> result = new Result<Map>();
		  try {
              Map<String, String> param = new HashMap<>();
              param.put("bdcdyh", bdcdyh);
              param.put("qlrmc", qlrmc);
              param.put("qlrzjh", qlrzjh);
			  param.put("enterpriseid",enterpriseid);
			  yspt_ywsqService.houseSearch(param);
			  result.success("获取成功！");
		  } catch (SupermapBootException e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取失败：" + e.getMessage());
		  } catch (Exception e) {
			  log.error(e.getMessage(), e);
			  result.error500("获取宗地信息接口异常，请联系管理员核查问题");
		  }
		  return result;
	  }
	  
	  /**
	     *  选择宗地
	     * param wfi_proinst
	     * @return
	     */
	    @AutoLog(value = "项目信息-选择宗地")
	    @ApiOperation(value="项目信息-选择宗地", notes="项目信息-选择宗地")
	    @PostMapping(value = "/selectzd")
	    public Result<JSONObject> selectzd(@RequestBody JSONObject zdsdata){
	        Result<JSONObject> result = new Result<JSONObject>();
	        try {
	        	yspt_ywsqService.selectzd(zdsdata);
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
		   *  移除宗地
		   * param wfi_proinst
		   * @return
		   */
		  @AutoLog(value = "项目信息-移除宗地")
		  @ApiOperation(value="项目信息-移除宗地", notes="项目信息-移除宗地")
		  @GetMapping(value = "/removehouse")
		  public Result<JSONObject> removehouse(String zdid,String enterpriseid){
			  Result<JSONObject> result = new Result<JSONObject>();
			  try {
				  yspt_ywsqService.removehouse(zdid, enterpriseid);
				  result.setSuccess(true);
			  } catch (SupermapBootException e) {
				  log.error(e.getMessage(), e);
				  result.error500("移除宗地失败：" + e.getMessage());
			  } catch (Exception e) {
				  log.error(e.getMessage(), e);
				  result.error500("移除宗地失败");
			  }
			  return result;
		  }

}

package org.jeecg.modules.mortgagerpc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mortgagerpc.service.StatisticsService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* Description: 统计用实现类入口
* Author: liangqin
* Date:   2019-07-29
*
*/
@Slf4j
@Api(tags="统计")
@RestController
@RequestMapping("/modules/statistics")
public class StatisticsController {
   @Autowired
   private StatisticsService statisticsService;

    /**
     * 获取抵押金额
     * param datas
     * @return
     */
    @GetMapping(value = "/getCountMortgageAmount")
    public Map getCountMortgageAmount(@RequestParam(name="type",required=true) String  type){
        //  JSONObject datas = JSONObject.parseObject(requestdatas);
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if("1".equals(type)){
                String  ttime = StringHelper.FormatByDatetime_(new Date()); //获取当前时间
                querymap.put("stime",ttime + " 00:00:00");
                querymap.put("etime",ttime + " 23:59:59");
            }
            List<Map> datas = statisticsService.getCountDYJE(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }
    
    /**
     * 获取抵押金额--个人
     * param datas
     * @return
     */
    @GetMapping(value = "/getPersonalCountMortgageAmount")
    public Map getPersonalCountMortgageAmount(@RequestParam(name="type",required=true) String  type){
        //  JSONObject datas = JSONObject.parseObject(requestdatas);
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if("1".equals(type)){
                String  ttime = StringHelper.FormatByDatetime_(new Date()); //获取当前时间
                querymap.put("stime",ttime + " 00:00:00");
                querymap.put("etime",ttime + " 23:59:59");
            }
            List<Map> datas = statisticsService.getPersonalCountDYJE(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }

    /**
     * 获取今年每月抵押金额
     * param datas
     * @return
     */
    @GetMapping(value = "/getMonthMortgageAmount")
    public Map getMonthMortgageAmount(@RequestParam(name="stime",required=true) String  stime,
    @RequestParam(name="etime",required=true) String  etime, @RequestParam(name="divisionc",required=true) String  divisioncode){
        //  JSONObject datas = JSONObject.parseObject(requestdatas);
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if(!"0".equals(stime) || !"0".equals(etime)){
                if(!"0".equals(stime) && !"0".equals(etime)){
                    querymap.put("stime",stime);
                    querymap.put("etime",etime);
                }else if(!"0".equals(stime)){
                    querymap.put("stime",stime);
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-12-31";
                    querymap.put("etime",times);
                }else{
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01";
                    querymap.put("stime",times);
                    querymap.put("etime",etime);
                }
            }else{
                String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01";
                querymap.put("time",times);
            }

            if(!"0".equals(divisioncode)){
                querymap.put("divisioncode",divisioncode.split(";")[0]);
            }

            List<Map> datas = statisticsService.getMonthdyje(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }
    
    /**
     * 获取今年每月抵押金额--个人
     * param datas
     * @return
     */
    @GetMapping(value = "/getPersonalMonthMortgageAmount")
    public Map getPersonalMonthMortgageAmount(@RequestParam(name="stime",required=true) String  stime,
    @RequestParam(name="etime",required=true) String  etime, @RequestParam(name="divisionc",required=true) String  divisioncode){
        //  JSONObject datas = JSONObject.parseObject(requestdatas);
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if(!"0".equals(stime) || !"0".equals(etime)){
                if(!"0".equals(stime) && !"0".equals(etime)){
                    querymap.put("stime",stime);
                    querymap.put("etime",etime);
                }else if(!"0".equals(stime)){
                    querymap.put("stime",stime);
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-12-31";
                    querymap.put("etime",times);
                }else{
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01";
                    querymap.put("stime",times);
                    querymap.put("etime",etime);
                }
            }else{
                String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01";
                querymap.put("time",times);
            }

            if(!"0".equals(divisioncode)){
                querymap.put("divisioncode",divisioncode.split(";")[0]);
            }

            List<Map> datas = statisticsService.getPersonalMonthdyje(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }


    /**
     * 获取今年每月业务数量
     * param datas
     * @return
     */
    @GetMapping(value = "/getMonthProject")
    public Map getMonthProject(){
        //  JSONObject datas = JSONObject.parseObject(requestdatas);
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01";
            querymap.put("time",times);
            List<Map> datas = statisticsService.getMonthproject(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }

    /**
     * 获取今年每月业务数量--个人
     * param datas
     * @return
     */
    @GetMapping(value = "/getPersonalMonthProject")
    public Map getPersonalMonthProject(){
        //  JSONObject datas = JSONObject.parseObject(requestdatas);
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01";
            querymap.put("time",times);
            List<Map> datas = statisticsService.getPersonalMonthproject(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }

    /**
     * 获取业务数量
     * param datas
     * @return
     */
    @GetMapping(value = "/getCountProject")
    public Map getCountProject(@RequestParam(name="type",required=true) String  type,
                                @RequestParam(name="shzt",required=true) String  shzt){
         Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if("1".equals(type)){
                String  ttime = StringHelper.FormatByDatetime_(new Date()); //获取当前时间
                querymap.put("stime",ttime + " 00:00:00");
                querymap.put("etime",ttime + " 23:59:59");
            }
            if(!"99".equals(shzt)){
                querymap.put("shzt",shzt);
            }
            List<Map> datas = statisticsService.getCountProject(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }
    
    /**
     * 获取业务数量--个人
     * param datas
     * @return
     */
    @GetMapping(value = "/getPersonalCountProject")
    public Map getPersonalCountProject(@RequestParam(name="type",required=true) String  type,
                                @RequestParam(name="shzt",required=true) String  shzt){
         Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if("1".equals(type)){
                String  ttime = StringHelper.FormatByDatetime_(new Date()); //获取当前时间
                querymap.put("stime",ttime + " 00:00:00");
                querymap.put("etime",ttime + " 23:59:59");
            }
            if(!"99".equals(shzt)){
                querymap.put("shzt",shzt);
            }
            List<Map> datas = statisticsService.getPersonalCountProject(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }



    /**
     * 获取业务类型数量
     * param datas
     * @return
     */
    @GetMapping(value = "/getCountProjectName")
    public Map getCountProjectName(){
         Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            List<Map> datas = statisticsService.getCountProjectName(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }

    /**
     * 获取业务类型数量--个人
     * param datas
     * @return
     */
    @GetMapping(value = "/getPersonalCountProjectName")
    public Map getPersonalCountProjectName(){
         Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            List<Map> datas = statisticsService.getPersonalCountProjectName(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }

    /**
     * 获取每月抵押金额
     * param datas
     * @return
     */
    @GetMapping(value = "/getCountMonthMortgageAmount")
    public Map getCountMonthMortgageAmount(){
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            List<Map> datas = statisticsService.getCountMonthMortgageAmount(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }
    
    /**
     * 获取每月抵押金额--个人
     * param datas
     * @return
     */
    @GetMapping(value = "/getPersonalCountMonthMortgageAmount")
    public Map getPersonalCountMonthMortgageAmount(){
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            List<Map> datas = statisticsService.getPersonalCountMonthMortgageAmount(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }


   /**
     * 获取审核状态业务
     * param datas
     * @return
     */
    @GetMapping(value = "/getSHZTProportionServer")
    public Map getSHZTProportionServer(@RequestParam(name="type",required=true) String  type){
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if("1".equals(type)){
                String  ttime = StringHelper.FormatByDatetime_(new Date()); //获取当前时间
                querymap.put("stime",ttime + " 00:00:00");
                querymap.put("etime",ttime + " 23:59:59");
            }
            List<Map> datas = statisticsService.getSHZTProportion(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }

    
    /**
     * 获取审核状态业务--个人
     * param datas
     * @return
     */
    @GetMapping(value = "/getPersonalSHZTProportionServer")
    public Map getPersonalSHZTProportionServer(@RequestParam(name="type",required=true) String  type){
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if("1".equals(type)){
                String  ttime = StringHelper.FormatByDatetime_(new Date()); //获取当前时间
                querymap.put("stime",ttime + " 00:00:00");
                querymap.put("etime",ttime + " 23:59:59");
            }
            List<Map> datas = statisticsService.getPersonalSHZTProportion(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }


    /**
     * 查询统计数据
     * param jsonObject
     * param request
     * param response
     * return
     */
    @PostMapping(value = "/queryProjectData")
    public Map getSHZTProportionServer(@RequestBody JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response){
        //参数值:xmmc=项目名称，city=城市,area=城区,shzt=审核状态,lclx=业务类型,sDate=开始时间,eDate=结束时间
        //下拉框字典的值为 字典代码;字典内容
        String projectname = jsonObject.getString("xmmc");
        String city = jsonObject.getString("city");
        String area = jsonObject.getString("area");
        String divisioncode = jsonObject.getString("divisioncode");
        String shzt = jsonObject.getString("shzt");
        String lclx = jsonObject.getString("lclx");
        String starttime = jsonObject.getString("sDate");
        String endtime = jsonObject.getString("eDate");
        String page = jsonObject.getString("page");
        String size = jsonObject.getString("size");
        Map remap = new HashMap<>();
        Map quertparams = new HashMap<>();

        if(!StringHelper.isEmpty(projectname)){
            quertparams.put("projectname",projectname);
        }

        if(!StringHelper.isEmpty(city) &&!StringHelper.isEmpty(area)){
            String citycode = city.split(";")[0];
            String areacode = area.split(";")[0];
            quertparams.put("divisioncode",Integer.parseInt(citycode+areacode));
        }else if(!StringHelper.isEmpty(city)){
            String citycode = city.split(";")[0];
            quertparams.put("cityname",Integer.parseInt(citycode));
        }

        if(!StringHelper.isEmpty(divisioncode)){
            String dcode = divisioncode.split(";")[0];
            quertparams.put("divisioncode",Integer.parseInt(dcode));
        }


        if(!StringHelper.isEmpty(shzt)){
            String shztcode = shzt.split(";")[0];
            quertparams.put("shztname",Integer.parseInt(shztcode));
        }
        if(!StringHelper.isEmpty(lclx)){
            String lclxname = lclx.split(";")[0];
            quertparams.put("lclxname",lclxname);
        }
        if(!StringHelper.isEmpty(starttime)){
            quertparams.put("starttime",starttime);
        }
        if(!StringHelper.isEmpty(endtime)){
            quertparams.put("endtime",endtime);
        }

        List<Map> counts = statisticsService.GetQueryDataCount(quertparams); //全部查询
        if(counts.size() > 0){
            int bsize = Integer.parseInt(size);
            int bindex = Integer.parseInt(page);
            int bnr = bsize*(bindex -1)+1;
            quertparams.put("bnr",bnr);
            quertparams.put("bsize",bsize * bindex);
            //分页查询
            List<Map> datas = statisticsService.GetQueryData(quertparams); //全部查询
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
            remap.put("total",counts.get(0).get("TOTAL"));
        }else{
            remap.put("success",false);
            remap.put("data","");
            remap.put("msg","查询失败！");
        }
        return remap;
    }


    /**
     * 获取已完成业务的金额和未完成业务的金额
     * param datas
     * return
     */
    @GetMapping(value = "/getProjectMortgageAmount")
    public Map getProjectMortgageAmount(@RequestParam(name="stime",required=true) String  stime,
                                        @RequestParam(name="etime",required=true) String  etime,
                                        @RequestParam(name="divisionc",required=true) String  divisioncode){
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if(!"0".equals(stime) || !"0".equals(etime)){
                if(!"0".equals(stime) && !"0".equals(etime)){
                    querymap.put("stime",stime);
                    querymap.put("etime",etime);
                }else if(!"0".equals(stime)){
                    querymap.put("stime",stime);
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-12-31 23:59:59";
                    querymap.put("etime",times);
                }else{
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01 00:00:00";
                    querymap.put("stime",times);
                    querymap.put("etime",etime);
                }
            }
            if(!"0".equals(divisioncode)){
                querymap.put("divisioncode",divisioncode.split(";")[0]);
            }
            List<Map> datas = statisticsService.GetProjectMortgageAmount(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }
    
    /**
     * 获取已完成业务的金额和未完成业务的金额 --个人
     * param datas
     * return
     */
    @GetMapping(value = "/getPersonalProjectMortgageAmount")
    public Map getPersonalProjectMortgageAmount(@RequestParam(name="stime",required=true) String  stime,
                                        @RequestParam(name="etime",required=true) String  etime,
                                        @RequestParam(name="divisionc",required=true) String  divisioncode){
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if(!"0".equals(stime) || !"0".equals(etime)){
                if(!"0".equals(stime) && !"0".equals(etime)){
                    querymap.put("stime",stime);
                    querymap.put("etime",etime);
                }else if(!"0".equals(stime)){
                    querymap.put("stime",stime);
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-12-31 23:59:59";
                    querymap.put("etime",times);
                }else{
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01 00:00:00";
                    querymap.put("stime",times);
                    querymap.put("etime",etime);
                }
            }
            if(!"0".equals(divisioncode)){
                querymap.put("divisioncode",divisioncode.split(";")[0]);
            }
            List<Map> datas = statisticsService.GetPersonalProjectMortgageAmount(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }


    /**
     * 获取各区域的总抵押金额
     * param datas
     * return
     */
    @GetMapping(value = "/getAreaMortgageAmount")
    public Map getAreaMortgageAmount(@RequestParam(name="stime",required=true) String  stime,
                                     @RequestParam(name="etime",required=true) String  etime,
                                     @RequestParam(name="divisionc",required=true) String  divisioncode
                                     ){
        Map remap = new HashMap<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Map querymap = new HashMap<>();
            querymap.put("userid",sysUser.getId());
            if(!"0".equals(stime) || !"0".equals(etime)){
                if(!"0".equals(stime) && !"0".equals(etime)){
                    querymap.put("stime",stime);
                    querymap.put("etime",etime);
                }else if(!"0".equals(stime)){
                    querymap.put("stime",stime);
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-12-31 23:59:59";
                    querymap.put("etime",times);
                }else{
                    String times = StringHelper.FormatDateOnType(new Date(),"yyyy") + "-01-01 00:00:00";
                    querymap.put("stime",times);
                    querymap.put("etime",etime);
                }
            }

            if(!"0".equals(divisioncode)){
                querymap.put("divisioncode",divisioncode.split(";")[0]);
            }
            List<Map> datas = statisticsService.getAreaMortgageAmount(querymap);
            remap.put("success",true);
            remap.put("data",datas);
            remap.put("msg","成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            remap.put("success","false");
            remap.put("msg","获取信息失败！");
        }
        return remap;
    }



}

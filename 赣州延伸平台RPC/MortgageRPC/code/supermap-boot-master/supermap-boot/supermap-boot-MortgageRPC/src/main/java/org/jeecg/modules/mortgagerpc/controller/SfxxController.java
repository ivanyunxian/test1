package org.jeecg.modules.mortgagerpc.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mortgagerpc.service.SfxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * @author 李海东
 * @created by 2019/9/23 17:09
 */
@RestController
@RequestMapping("/sfxx")
public class SfxxController {
    @Autowired
    private SfxxService sfxxService;

    @PostMapping
    public Result update(@RequestParam String[] ids, @RequestParam String ywlsh) {
        return sfxxService.update(ids, ywlsh);
    }

    @GetMapping
    public Result list(@RequestParam String ywlsh) {
        return sfxxService.list(ywlsh);
    }

    @DeleteMapping
    public Result delete(@RequestParam String id,@RequestParam String ywlsh) {
        return sfxxService.delete(id,ywlsh);
    }
}

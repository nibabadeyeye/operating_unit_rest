package com.gpdi.operatingunit.controller.home;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.service.home.MainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhb
 * @date 2019/12/16 10:37
 **/
@RestController
@RequestMapping("/home")
public class MainController {

    @Autowired
    private MainService mainService;

    @PostMapping("/getData")
    @ApiOperation("获取首页展示数据")
    public R getData(Integer month, boolean firstTime) {
        return R.ok(mainService.getData(month,firstTime));
    }
}

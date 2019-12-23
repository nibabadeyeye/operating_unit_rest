package com.gpdi.operatingunit.controller.system;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.system.SysDict;
import com.gpdi.operatingunit.entity.system.SysSapAccount;
import com.gpdi.operatingunit.service.system.SapAccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zyb
 * @Date: 2019/11/28 11:05
 */
@RestController
@RequestMapping("/sapAccount")
public class SapAccountController {

    @Autowired
    private SapAccountService sapAccountService;

    @RequestMapping(value = "/getTypeList",method = {RequestMethod.POST})
    @ApiOperation("获取账号类型")
    public R getTypeList(){
        Map<String,Object> result = new HashMap<>();
        List<SysDict> typeList = sapAccountService.getTypeList();
        result.put("typeList",typeList);
        List<SysDict> typeLists = new ArrayList<>();
        typeLists.addAll(typeList);
        typeLists.remove(0);
        result.put("typeLists",typeLists);
        return R.ok(result);
    }

    @RequestMapping(value = "/getAccountList",method = {RequestMethod.POST})
    @ApiOperation("获取账号集合")
    public R getAccountList(String type){
        return R.ok(sapAccountService.getAccountList(type.trim()));
    }

    @RequestMapping(value = "/addAccount",method = {RequestMethod.POST})
    @ApiOperation("新增一个账号")
    public R addAccount(SysSapAccount sysSapAccount){
        return R.ok(sapAccountService.addAccount(sysSapAccount));
    }

    @RequestMapping(value = "/updateAccount",method = {RequestMethod.POST})
    @ApiOperation("修改账号密码")
    public R updateAccount(SysSapAccount sysSapAccount){
        return  R.ok(sapAccountService.updateAccount(sysSapAccount));
    }

    @RequestMapping(value = "/deleteAccount",method = {RequestMethod.POST})
    @ApiOperation("删除账号信息")
    public R deleteAccount(Integer id){
        return R.ok(sapAccountService.deleteAccount(id));
    }

    @RequestMapping(value = "/startGetSapValue",method = {RequestMethod.POST})
    @ApiOperation("触发点击下载sap数据")
    public R startGetSapValue(){
        return R.ok(sapAccountService.startGetSapValue());
    }
}

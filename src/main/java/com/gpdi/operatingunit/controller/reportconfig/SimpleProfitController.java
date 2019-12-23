package com.gpdi.operatingunit.controller.reportconfig;

import com.alibaba.fastjson.JSONArray;
import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitReportDetailsEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity;
import com.gpdi.operatingunit.entity.system.SysSapSubject;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.reportconfig.SimpleProfitService;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author：long
 * @Date：2019/12/3 14:09
 * @Description：简易边际利润
 */
@RestController
@RequestMapping("simpleProfitController")
public class SimpleProfitController {

    @Autowired
    private SimpleProfitService simpleProfitService;
    @PostMapping("/findAll")
    @ApiOperation("获取表头")
    public R findAll(){
        List<Map<Object, Object>> all = simpleProfitService.findAll();
        return R.ok(all);
    }

    @PostMapping("/findByTopOrgCode")
    @ApiOperation("获取当前用户可查看的利润数据")
    public R findByTopOrgCode(Long topOrgCode,int pageNumber,int pageSize){

        Map map = simpleProfitService.findByTopOrgCode(topOrgCode, (pageNumber - 1) * pageSize, pageSize);
        return R.ok(map);
    }

    @PostMapping("/delSimpleProfitsReportAndId")
    @ApiOperation("数据删除")
    public R delSimpleProfitsReportAndId(String json){
        /*List<SimpleProfitsReportEntity> list = new ArrayList<>();
        list.add(simpleProfitsReportEntity);*/
        List<SimpleProfitsReportEntity> list = JSONArray.parseArray(json, SimpleProfitsReportEntity.class);
        simpleProfitService.delSimpleProfitsReportAndId(list);
        return R.ok("message","删除成功");
    }

    @PostMapping("/findSimpleProfitAll")
    @ApiOperation("获取需要展示的行")
    public R findSimpleProfitAll(Long topOrgCode,Integer id){
        Map map = new HashMap();
        // 表头
        List<Map<Object, Object>> all = simpleProfitService.findAll();
        map.put("tableHead",all);
        // 需要展示的行
        List<SimpleProfitEntity> simpleProfitAll = simpleProfitService.findSimpleProfitAll();
        map.put("simpleProfitAll",simpleProfitAll);
        // 获取详细数据
        List<SimpleProfitsReportEntity> list = simpleProfitService.findByTopOrgCodeAndId(topOrgCode, id);
        map.put("list",list);
        return R.ok(map);
    }

    @PostMapping("/insertSimpleProfitReportDetails")
    @ApiOperation("数据添加")
    public R insertSimpleProfitReportDetails(String chelList){
        SysUser user = ShiroUtils.getUser();
        List<SimpleProfitReportDetailsEntity> list = JSONArray.parseArray(chelList, SimpleProfitReportDetailsEntity.class);
        List<SimpleProfitReportDetailsEntity> lists = new ArrayList<>();
        for(SimpleProfitReportDetailsEntity simpleProfitReportDetailsEntity : list){
            if(!isNumeric(simpleProfitReportDetailsEntity.getArpu())){return R.ok("index",-5);}
            if(!isNumeric(simpleProfitReportDetailsEntity.getRetentionRate())){return R.ok("index",-5);}
            if(!isNumeric(simpleProfitReportDetailsEntity.getNumberOfPersonnel())){return R.ok("index",-5);}
            if(!isNumeric(simpleProfitReportDetailsEntity.getAverageMonthlySalary())){return R.ok("index",-5);}
            if(!isNumeric(simpleProfitReportDetailsEntity.getArea())){return R.ok("index",-5);}
            if(!isNumeric(simpleProfitReportDetailsEntity.getUnitPriceOfRent())){return R.ok("index",-5);}
            if(!isNumeric(simpleProfitReportDetailsEntity.getMonthlyAverageIntoNetwork())) {return R.ok("index",-5);}
            simpleProfitReportDetailsEntity.setUserId(user.getId());
            lists.add(simpleProfitReportDetailsEntity);
        }
        int i = simpleProfitService.insertSimpleProfitReportDetails(lists);
        return R.ok("index",i);
    }

    public static boolean isNumeric(String str) {
        //Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");//这个有问题，一位的整数不能通过
        //^([1-9]\d{1,9}|\d)(\.\d{1,2})?$
        if(str == null){
            return false;
        }
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");//这个是对的
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}

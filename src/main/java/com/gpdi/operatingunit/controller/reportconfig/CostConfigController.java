package com.gpdi.operatingunit.controller.reportconfig;

import com.alibaba.fastjson.JSONArray;
import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.dao.reportconfig.RelOrgReportColumnDao;
import com.gpdi.operatingunit.entity.common.SapList;
import com.gpdi.operatingunit.entity.relation.RelOrgReportColumn;
import com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity;
import com.gpdi.operatingunit.entity.reportconfig.SysCostItem;
import com.gpdi.operatingunit.entity.system.RelSapCostItemSubitem;
import com.gpdi.operatingunit.entity.system.SysSapSubject;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.reportconfig.CostConfigService;
import com.gpdi.operatingunit.utils.DataGenerateUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zyb
 * @Date: 2019/10/25 11:32
 */
@RestController
@RequestMapping("/costConfiguration")
public class CostConfigController {

    @Autowired
    private CostConfigService configService;
    @Autowired
    private DataGenerateUtils dataGenerateUtils;

    @Autowired
    private RelOrgReportColumnDao relOrgReportColumnDao;

    @RequestMapping(value = "/getTableDataList", method = {RequestMethod.POST})
    @ApiOperation("获取成本表的费用类别")
    public R getCostItem(Integer reportId) {
        SysUser user = ShiroUtils.getUser();
        Map<String, Object> map = new HashMap<>(2);
        map.put("costItem", configService.getCostItem(reportId, user.getTopOrgCode()));
        map.put("MonthList", configService.getMonthList(user.getTopOrgCode()));
        return R.ok(map);
    }

    @RequestMapping(value = "/getCountryItem", method = {RequestMethod.POST})
    @ApiOperation("获取区县报表的费用类别")
    public R getCountyItem(Integer reportId) {
        SysUser user = ShiroUtils.getUser();
        Map<String, Object> map = new HashMap<>(2);
        map.put("countryItem", configService.getCountyItem(reportId, user.getTopOrgCode()));
        map.put("MonthList", configService.getMonthList(user.getTopOrgCode()));
        return R.ok(map);
    }

    @RequestMapping(value = "/getSapSubject", method = {RequestMethod.POST})
    @ApiOperation("获取成本项")
    public R getSapSubject(Integer id, Integer reportId) {
        Map<String, Object> map = new HashMap<>(1);
        //获取匹配好的成本项
        map.put("sapSubject", configService.getSapSubject(id, reportId));
        return R.ok(map);
    }

    @RequestMapping(value = "/getAllSapSubject", method = {RequestMethod.POST})
    public R getAllSapSubject() {
        Map<String, Object> map = new HashMap<>(3);
        //获取sap的字段名
        List<SapList> sapNameList = configService.getSapNameList();
        sapNameList.remove(0);
        map.put("sapName", sapNameList);
        //获取大于等于等条件
        map.put("selection", configService.getSelections());
        //提供能齐全的选择的成本
        map.put("allSubject", configService.getAllSubject());
        return R.ok(map);
    }

    @RequestMapping(value = "/getSapSubjectList", method = {RequestMethod.POST})
    @ApiOperation("获取已经匹配好的成本项条件")
    public R getSapSubjectList(Long sid, Long orgCode, Integer year, Long sapCode, Integer reportId) {
        return R.ok(configService.getRelSapCostItemSubmitemList(sid, orgCode, year, sapCode, reportId));
    }

    @RequestMapping(value = "/insertSubmitem", method = {RequestMethod.POST})
    @ApiOperation("插入匹配关系到subitem表")
    public R insertSubmitem(Long sid, Long orgCode, Integer year, Long sapCode, String submitemList, Integer reportId) {
        List<RelSapCostItemSubitem> relSapCostItemSubitems = JSONArray.parseArray(submitemList, RelSapCostItemSubitem.class);
        List<RelSapCostItemSubitem> result = configService.insertSubmitem(sid, orgCode, year, sapCode, relSapCostItemSubitems, reportId);
        return R.ok(result);
    }

    @RequestMapping(value = "/updateRelSapCostItem", method = {RequestMethod.POST})
    @ApiOperation("修改成本项费用到rel_sap_cost_item表")
    public R updateRelSapCostItem(Integer id, String item, Integer reportId, String itemList) {
        SysUser user = ShiroUtils.getUser();
        List<SysSapSubject> sysSapSubjects = JSONArray.parseArray(itemList, SysSapSubject.class);
        Object object = configService.updateRelSapCostItem(user, id, item, reportId, sysSapSubjects);
        return R.ok(object);
    }

    @RequestMapping(value = "/deleteRelSapCostItem", method = {RequestMethod.POST})
    @ApiOperation("删除匹配好的成本项费用")
    public R deleteRelSapCostItem(Long sid, Long orgCode, Integer year, Long sapCode, Integer reportId) {
        Integer number = configService.deleteRelSapCostItem(sid);
        return R.ok(number);
    }

    @RequestMapping(value = "/deleteSubmitem", method = {RequestMethod.POST})
    @ApiOperation("删除匹配好的sap科目")
    public R deleteSubmitem(Integer id) {
        Integer number = configService.deleteSubmitem(id);
        return R.ok(number);
    }

    @RequestMapping(value = "/deleteAllSubitem", method = {RequestMethod.POST})
    @ApiOperation("将匹配好的sap科目全部删除")
    public R deleteAllSubitem(Integer id) {
        return R.ok(configService.deleteAllSubitem(id));
    }

    @RequestMapping(value = "/getCostItemByReportId", method = {RequestMethod.POST})
    @ApiOperation("查询化小成本表费用用于处理区县配置")
    public R getCostItemByReportId(Integer reportId) {
        SysUser user = ShiroUtils.getUser();
        List<SysCostItem> result = configService.getCostItemByReportId(reportId, user.getTopOrgCode());
        return R.ok(result);
    }

    @RequestMapping(value = "/setCountrySapSubject", method = {RequestMethod.POST})
    @ApiOperation("给区县表配置划小成本的成本项")
    public R setCountrySapSubject(Integer cid, String item, Integer sid, Integer reportId) {
        Boolean flag = false;
        try {
            SysUser user = ShiroUtils.getUser();
            String str = configService.setCountrySapSubject(cid, item, sid, reportId, user.getTopOrgCode());
            if (str.equals("success")) {
                flag = true;
            }
            return R.ok(str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!flag) {
                return R.ok("fail");
            }
        }
    }

    @RequestMapping(value = "/insertValue", method = {RequestMethod.POST})
    @ApiOperation("给区县及化小成本表生成数据")
    public R insertValue(Integer month, Integer reportId) {
        return dataGenerateUtils.insertRsCostReportValue(month, reportId);
    }

    @RequestMapping(value = "/choseItemByReportId", method = {RequestMethod.POST})
    @ApiOperation("回显树状的费用列表")
    public R choseItemByReportId(Integer reportId) {
        List<SysCostItem> result = configService.choseItemByReportId(reportId);
        return R.ok(result);
    }

    @RequestMapping(value = "/updateItemName", method = {RequestMethod.POST})
    @ApiOperation("修改费用名")
    public R updateItemName(Integer id, String name) {
        Integer number = configService.updateItemName(id, name);
        return R.ok(number);
    }

    @RequestMapping(value = "deleteItemById", method = {RequestMethod.POST})
    @ApiOperation("删除费用名")
    public R deleteItemById(Integer id) {
        Integer number = configService.deleteItemById(id);
        return R.ok(number);
    }

    @RequestMapping(value = "/choseItem", method = {RequestMethod.POST})
    @ApiOperation("选择要新建的费用所属父级")
    public R choseItem(Integer reportId) {
        Map<String, Object> map = new HashMap<>();
        map.put("choseItem", configService.choseItem(reportId));
        map.put("dataFrom", configService.getDataFrom());
        return R.ok(map);
    }

    @RequestMapping(value = "/saveItem", method = {RequestMethod.POST})
    @ApiOperation("保存新建费用")
    public R saveItem(SysCostItem sysCostItem) {
        Integer number = 0;
        if (sysCostItem.getReportId() == 10001) {
            number = configService.saveItemByCost(sysCostItem);
        } else if (sysCostItem.getReportId() == 20001) {
            number = configService.saveItemByCounty(sysCostItem);
        }
        return R.ok(number);
    }

    @PostMapping("/queryTitleSettingData")
    @ApiOperation("查询表头配置数据")
    public R queryTitleSettingData(Integer reportId) {
        List<RelOrgReportColumn> relOrgReportColumns = configService.queryTitleSettingData(reportId);
        return R.ok(relOrgReportColumns);
    }

    @PostMapping("/queryTreeEnableData")
    @ApiOperation("查询表头配置可用数据用于回显")
    public R queryTreeEnableData(Integer reportId) {
        List<RelOrgReportColumn> relOrgReportColumns = configService.queryTreeEnableData(reportId);
        Object[] ids = relOrgReportColumns.stream().map(RelOrgReportColumn::getId).collect(Collectors.toList()).toArray();
        return R.ok(ids);
    }

    @PostMapping("/updateSeqAndStatus")
    @ApiOperation("保存修改表头配置排序和可用")
    public R updateSeqAndStatus(Integer reportId, Integer[] ids) {
        Long userTopOrgCode = ShiroUtils.getUser().getTopOrgCode();
        int i = configService.updateSeqAndStatus(reportId, ids, userTopOrgCode);
        long reportId1 = (Integer) reportId;
        List<RelOrgReportColumnEntity> relOrgReportColumnEntities = null;
        long topOrgCode = 0;
        if (userTopOrgCode != -1) {
            //获取地市的编码
            topOrgCode = ShiroUtils.getUser().getTopOrgCode();
        }
        relOrgReportColumnEntities = relOrgReportColumnDao.queryByReportIdAndTopOrgCode(reportId1, topOrgCode);
        return R.ok(relOrgReportColumnEntities);
    }


    @RequestMapping(value = "/updateSapData", method = {RequestMethod.POST})
    @ApiOperation("重新生成sap数据")
    public R updateSapData(Integer month, Integer reportId) {
        if(reportId == 10001){
            return R.ok(dataGenerateUtils.insertRsCostReportValue(month, reportId));
        }else{
            return R.ok(dataGenerateUtils.insertQingYuanCountyValue(month,reportId));
        }
    }

    @PostMapping("/getCloumTitle")
    @ApiOperation("获取成本项和区县表头配置")
    public R getCloum(Integer reportId) {
        Long userTopOrgCode = ShiroUtils.getUser().getTopOrgCode();
        long reportId1 = (Integer) reportId;
        long  topOrgCode=0;
        List<RelOrgReportColumnEntity> relOrgReportColumnEntities = null;
        if (userTopOrgCode != -1) {
            //获取地市的编码
          topOrgCode = ShiroUtils.getUser().getTopOrgCode();
        }
        relOrgReportColumnEntities = relOrgReportColumnDao.queryByReportIdAndTopOrgCode(reportId1, topOrgCode);
        return R.ok(relOrgReportColumnEntities);
    }


}

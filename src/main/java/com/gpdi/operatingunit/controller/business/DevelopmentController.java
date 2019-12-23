package com.gpdi.operatingunit.controller.business;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.entity.business.DataDevelopment;
import com.gpdi.operatingunit.entity.reportconfig.DataSapListEntity;
import com.gpdi.operatingunit.entity.system.SysReportColumn;
import com.gpdi.operatingunit.service.business.DevelopmentService;
import com.gpdi.operatingunit.utils.DateUtils;
import com.gpdi.operatingunit.utils.ExcelUtils;
import com.gpdi.operatingunit.utils.ProductionExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 业务量展示报表
 * @author: Lxq
 * @date: 2019/11/18 14:30
 */
@RestController
@Api(description = "业务量展示报表管理控制器类")
@RequestMapping("/development")//development/upload
public class DevelopmentController {

    @Autowired
    private DevelopmentService developmentService;

    @RequestMapping("/upload")
    public R uploadExcel(MultipartFile file) throws Exception {
        List<Object> objectList = ExcelUtils.readLessThan1000RowBySheetAndStream(file.getInputStream(), null);
        if (objectList.size() <= 0) {
            return R.error("数据为空");
        }
        List<DataDevelopment> listEntityList = new ArrayList<>();
        for (int i = 2; i < objectList.size(); i++) {
            DataDevelopment d = new DataDevelopment();
            List list = (List) objectList.get(i);
            if (list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    listEntityList.add(d);
                }
            }
            developmentService.insertExcelList(listEntityList);

        }

        return R.ok("请求成功");
    }


    @GetMapping("/getColumn")
    @ApiOperation("业务量展示报表表头数据")
    public R getColumn() {
        List<SysReportColumn> sysReportColumns = developmentService.queryReportColumn();
        return R.ok(sysReportColumns);
    }

    /**
     * 获取最新月份
     *
     * @return
     */
    @GetMapping("/getNewMonth")
    public R getNewMonth() {
        List<Integer> list = developmentService.monthList();
        Object[] objects = list.stream().toArray();
        System.out.println(objects);
        return R.ok(objects);
    }

    @PostMapping("/getTableData")
    @ApiOperation("业务发展报表数据")
    public R getTableData(Integer time) {
        List<Map<Object, Object>> mapList = developmentService.queryDevelopmentData(time);
        return R.ok(mapList);
    }

    @PostMapping("downLoadExcel")
    public R downLoadExcel(HttpServletRequest request, HttpServletResponse response, Integer month) {
        try {
            File report = new File("report");
            //如果文件夹不存在
            if (!report.exists()) {
                //创建文件夹
                report.mkdir();
            }
            List<Map<Object, Object>> excelData = developmentService.queryDevelopmentData(month);
            List<SysReportColumn> reportTitleColumn = developmentService.queryReportColumn();
            // 生成excel
            ProductionExcelUtils.generalProductionExcelByMap(reportTitleColumn, excelData, report, "业务量发展报表" + month + ".xlsx");
            File file = new File(report + File.separator + "业务量发展报表" + month + ".xlsx");
            ProductionExcelUtils.downLoadFile(request, response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

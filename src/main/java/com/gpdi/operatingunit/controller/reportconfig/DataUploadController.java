package com.gpdi.operatingunit.controller.reportconfig;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.service.reportconfig.RelSapCostItemService;
import com.gpdi.operatingunit.service.reportconfig.SysCostItemService;
import com.gpdi.operatingunit.utils.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @description: 数据上传控制类
 * @author: Lxq
 * @date: 2019/10/25 15:06
 */
@RestController
@RequestMapping("/upload")
@Api(description = "数据上传控制类")
public class DataUploadController {


    @Autowired
    private SysCostItemService sysCostItemService;

    @Autowired
    private RelSapCostItemService relSapCostItemService;

    @PostMapping("/relSapCostItemUpload")
    @ApiOperation("SAP科目与成本项目关系表数据上传")
    public R relSapCostItemUpload(@RequestParam(value = "file") MultipartFile file, Integer reportId) {
        try {
            InputStream inputStream = file.getInputStream();
            List<Object> objects = ExcelUtils.readLessThan1000RowBySheetAndStream(inputStream, null);
            int i = relSapCostItemService.relSapCostItemUpload(objects, reportId);
            inputStream.close();
            return R.ok(i);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/countyUpload")
    @ApiOperation("区县报表表数据上传")
    public R countyUpload(@RequestParam(value = "file") MultipartFile file, Integer reportId) {
        try {
            InputStream inputStream = file.getInputStream();
            List<Object> objects = ExcelUtils.readLessThan1000RowBySheetAndStream(inputStream, null);
            R result = sysCostItemService.countyUpload(objects, reportId);
            inputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/costItemUpload")
    @ApiOperation("划小成本项目表数据上传")
    public R costItemUpload(@RequestParam(value = "file") MultipartFile file, Integer reportId) {
        try {
            //读取excel数据
            InputStream inputStream = file.getInputStream();
            List<Object> objects = ExcelUtils.readLessThan1000RowBySheetAndStream(inputStream, null);
            R result = sysCostItemService.costItemUpload(objects, reportId);
            inputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

}

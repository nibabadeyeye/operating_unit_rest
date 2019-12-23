package com.gpdi.operatingunit.controller.reportconfig;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.service.reportconfig.DataSapService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author：long
 * @Date：2019/10/29 8:38
 * @Description：sap数据导入
 */
@RestController
@RequestMapping("/sapUploadController")
public class SapUploadController {

    @Autowired
    private DataSapService sapUploadService;

    /**
     * description: sap数据导入
     *
     * @param file
     * @param month
     * @return com.gpdi.operatingunit.config.R
     */
    @PostMapping("/uploadExcel")
    @ApiOperation("sap数据导入")
    public R uploadExcel(@RequestParam(value = "file") MultipartFile file,String month) throws IOException {
        InputStream inputStream = file.getInputStream();
        InputStream sheetInputStream = file.getInputStream();
        int i = sapUploadService.dataUpload(inputStream, sheetInputStream, month);
        inputStream.close();
        sheetInputStream.close();
        return R.ok("message",i);
    }

}

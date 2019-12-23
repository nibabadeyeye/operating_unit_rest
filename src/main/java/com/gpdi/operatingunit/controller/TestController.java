package com.gpdi.operatingunit.controller;

import com.google.gson.Gson;
import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.utils.ExcelUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description:
 * @Author: Lxq
 * @Date: 2019/10/22 9:29
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @PostMapping("/uploadExcel")
    public R userLogout(@RequestParam(value = "file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<Object> objects = ExcelUtils.readLessThan1000RowBySheetAndStream(inputStream, null);
        for (int i = 2; i < objects.size(); i++) {
            List<Object> list = (List<Object>) objects.get(i);
            for (int j = 0; j < list.size(); j++) {
                System.out.println(list.get(j));
            }
        }

        return R.ok("success");
    }
}

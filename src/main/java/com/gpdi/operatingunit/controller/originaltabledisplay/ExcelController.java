package com.gpdi.operatingunit.controller.originaltabledisplay;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.service.originaltabledisplay.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@RestController
@Api("excel控制类")
@RequestMapping("/excel")
public class ExcelController {

    @Transactional
    @PostMapping("/uploadExcel")
    @ApiOperation("导入市、区、营服数据到数据库")
    public R uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return excelService.uploadFile(file, request);
    }

    @ApiOperation(value = "根据ExcelId获取Excel信息")
    @PostMapping("/getExcelData")
    public R getExcelDataById(int excelId) {
        return excelService.getExcelDataById(excelId);
    }

    @ApiOperation("从服务器中下载Excel")
    @PostMapping("downLoadExcel")
    public R downLoadExcel(HttpServletRequest request, HttpServletResponse response, String fileName) {
        return excelService.downLoadExcelFileFromServer(request, response, fileName);
    }


    @Transactional
    @ApiOperation("/获取全部的ExcelMessage信息")
    @RequestMapping("/getAllCostMessage")
    public R getUploadExcelMessage(int orgCode,int code, int pageNumber, int pageSize) {
        return excelService.getUploadExcelMessage(orgCode,code, pageNumber, pageSize);
    }

    @Transactional
    @ApiOperation("/获取市报表数据")
    @RequestMapping("/getCityCostReport")
    public R getCityCostReport() {
        return excelService.getCityCostReport();
    }

    @ApiOperation("根据条件查询Excel信息")
    @PostMapping("/getAListOfExcelMessage")
    public R getListOfExcelMessage(int pageNumber, int pageSize) {
        return excelService.getListOfExcelMessage(pageNumber, pageSize);
    }


//    @Transactional
//    @ApiOperation("/查询所有的区信息")
//    @RequestMapping("/getDistrictByUser")
//    public R getDistrictByUser(HttpServletRequest request) {
//        List<SysOrganization> list = new Vector<>();
//        //查询对应的组织结构
//        SysUser sysUser = ShiroUtils.getUser();
//        String name = sysUser.getName();
//        //查询用户所属组织级别
//        int level = reportShowService.getOrgCodeLevelByOrgCode(sysUser.getOrgCode().toString());
//        if (level == 1) { //当前级别为营服
//            return R.ok("管理员不能查看市数据");
//        } else if (level == 2) {//当前用户为市、查询市下所有营服
//            String sql = "select * from sys_organization where parent_code=" + sysUser.getOrgCode();
//            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysOrganization.class));
//        } else if (level == 3) { //当前用户为区
//            String sql = "select * from sys_organization where code=" + sysUser.getOrgCode();
//            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysOrganization.class));
//        } else if (level == 4) { //当前级别为营服
//            return R.ok("营服数据不能查看市数据");
//        }
//        return R.ok(list);
//    }

//    @Transactional
//    @ApiOperation("/查询区对应的成本信息")
//    @RequestMapping("/getCostByDistrict")
//    public R getCostByDistrict(HttpServletRequest request, String code) {
//        //查询对应的组织结构
//        SysUser sysUser = ShiroUtils.getUser();
//        String name = sysUser.getName();
//        //查询用户所属组织级别
//        String sql = "select * from excel_message where code=" + sysUser.getOrgCode();
//        List<ExcelMessage> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExcelMessage.class));
//        return R.ok(list);
//    }


//    @Transactional
//    @ApiOperation("/查询所有的营服信息")
//    @RequestMapping("/getServiceByUser")
//    public R getServiceByUser(HttpServletRequest request) {
//        List<SysOrganization> list = new Vector<>();
//        //查询对应的组织结构
//        SysUser sysUser = ShiroUtils.getUser();
//        String name = sysUser.getName();
//        //查询用户所属组织级别
//        int level = reportShowService.getOrgCodeLevelByOrgCode(sysUser.getOrgCode().toString());
//        if (level == 1) { //当前级别为营服
//            return R.ok("管理员不能查看营服数据");
//        } else if (level == 2) {//当前用户为市、查询市下所有营服
//            String sql = "select * from sys_organization where top_org_code=" + sysUser.getTopOrgCode();
//            List<SysOrganization> bigList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysOrganization.class));
//            //封装一个树形集合
//            //  List<SysOrganization>secondList=new ArrayList<>();
//            for (int i = 0; i < bigList.size(); i++) {
//                if (bigList.get(i).getParentCode() == sysUser.getOrgCode()) {
//                    list.add(bigList.get(i));
//                    bigList.remove(list.get(i));
//                }
//            }
//            //找到区下面的营服
//            for (int i = 0; i < bigList.size(); i++) {
//                List<SysOrganization> reportShowList = new ArrayList<>(1000);
//                for (int j = 0; j < list.size(); j++) {
//                    try {
//                        if (list.get(j).getChildren().size() != 0) {
//                            reportShowList = list.get(j).getChildren();
//                        }
//                    } catch (Exception e) {
//
//                    }
//
//                }
//            }
//
//        } else if (level == 3) { //当前用户为区
//            String sql = "select * from sys_organization where code=" + sysUser.getOrgCode();
//            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysOrganization.class));
//        } else if (level == 4) { //当前级别为营服
//            return R.ok("营服数据不能查看市数据");
//        }
//        return R.ok(list);
//    }


//    @Transactional
//    @ApiOperation("/导入成本收入数据")
//    @RequestMapping("/getAllOrganizedByOrgCode")
//    public R getAllOrganizedByOrgCode(HttpServletRequest request) {
//        //拿到用户信息、判断是否输入省级用户，并且是否为管理，否则不展示目录结构信息
//        SysUser sysUser = ShiroUtils.getUser();
//        String name = sysUser.getName();
//        //查询用户所属组织级别
//        int level = reportShowService.getOrgCodeLevelByOrgCode(sysUser.getOrgCode().toString());
//        SysRole sysRole = reportShowService.getRoleByUserId(sysUser.getId());
//        if (level != 2) {
//            return R.ok("当前账号不是市的账号");
//        } else if (sysRole != null) {
//            List<SysOrganization> list = reportShowService.getAllSysOrganizedByTopOrgCode(sysUser.getTopOrgCode());
//            List<SysOrganization> secondList = new ArrayList<>();
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getParentCode().toString().equals(sysUser.getOrgCode().toString())) {
//                    secondList.add(list.get(i));
//                    list.remove(list.get(i));
//                }
//            }
//            for (int j = 0; j < secondList.size(); j++) {
//                List<SysOrganization> reportShowList = new ArrayList<>(1000);
//                try {
//                    if (secondList.get(j).getChildren().size() != 0) {
//                        reportShowList = secondList.get(j).getChildren();
//                    }
//                } catch (Exception e) {
//
//                }
//                for (int i = 0; i < list.size(); i++) {
//                    if (secondList.get(j).getCode().toString().equals(list.get(i).getParentCode().toString())) {
//                        try {
//                            if (list.get(i) != null) {
//                                reportShowList.add(list.get(i));
//                                secondList.get(j).setChildren(reportShowList);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            return R.ok(secondList);
//        } else {
//            return R.ok("当前账号不是市管理员账号");
//        }
//    }


    @Autowired
    private ExcelService excelService;





}

package com.gpdi.operatingunit.service.originaltabledisplay.impl;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.originaltabledisplay.DeleteExcelUtil;
import com.gpdi.operatingunit.controller.originaltabledisplay.DeletePara;
import com.gpdi.operatingunit.controller.originaltabledisplay.PoiUtil;
import com.gpdi.operatingunit.controller.originaltabledisplay.TableUtil;
import com.gpdi.operatingunit.dao.costreport.ReportShowMapper;
import com.gpdi.operatingunit.dao.originaltabledisplay.ExcelMapper;
import com.gpdi.operatingunit.entity.originaltabledisplay.CrossUtil;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelData;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelMessage;
import com.gpdi.operatingunit.entity.originaltabledisplay.ExcelUtil;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.originaltabledisplay.ExcelService;
import com.gpdi.operatingunit.utils.ProductionExcelUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ExcelImpl implements ExcelService {
    @Override
    public R getExcelDataById(int excelId) {
        List<ExcelData> excelDataList1 = excelMapper.getExcelData(excelId);
        Map<Integer, List<ExcelData>> listMap = new HashMap<>();
        excelDataList1.forEach(a -> {
            if (listMap.containsKey(a.getRowNumber())) {
                List<ExcelData> excelDataList = listMap.get(a.getRowNumber());
                excelDataList.add(a);
                excelDataList.sort((ExcelData a1, ExcelData a2) -> a1.getId() - a2.getId());
                listMap.put(a.getRowNumber(), excelDataList);
            } else {
                List<ExcelData> excelDataList = new ArrayList<>();
                excelDataList.add(a);
                excelDataList.sort((ExcelData a1, ExcelData a2) -> a1.getId() - a2.getId());
                listMap.put(a.getRowNumber(), excelDataList);
            }
        });

        return new R(200, "请求成功", listMap);
    }

    @Override
    public R getListOfExcelMessage(int pageNumber, int pageSize) {
        int count = excelMapper.getExcelNumber("select count(*) from excel_message where 1=1");
        if (pageSize >= 1) {
            pageSize = (pageSize - 1) * pageNumber;
        }
        String pageSql = " limit " + pageSize + "," + pageNumber + "";
        List<ExcelMessage> list = excelMapper.getAListOfExcelMessage("select * from excel_message " + pageSql);
        Map map = new HashMap();
        map.put("data", list);
        map.put("count", count);
        return new R(200, "请求成功", map);
    }

    @Override
    public R getUploadExcelMessage(int organizedCode, int code, int pageNumber, int pageSize) {
        SysUser sysUser = ShiroUtils.getUser();
        String name = sysUser.getName();
        //查询用户所属组织级别
        int level = reportShowMapper.getOrgCodeLevelByOrgCode(sysUser.getOrgCode().toString());
        if (pageSize >= 1) {
            pageSize = (pageSize - 1) * pageNumber;
        }
        if (organizedCode == level) {
            String sql = "select * from excel_message where org_code=" + sysUser.getOrgCode() + " and code" + code + " limit " + pageSize + " ," + pageNumber;
            System.out.println(sql);
            List<ExcelMessage> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExcelMessage.class));
            return R.ok("请求成功", list);
        } else {
            return R.ok("您不能查询你身份外的信息");
        }
    }

    @Override
    public R getCityCostReport() {
        //查询对应的组织结构
        SysUser sysUser = ShiroUtils.getUser();
        int level = reportShowMapper.getOrgCodeLevelByOrgCode(sysUser.getOrgCode().toString());
        if (level == 2) {
            String sql = "select * from excel_message where code=" + sysUser.getOrgCode();
            List<ExcelMessage> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExcelMessage.class));
        } else {
            R.ok("你当前没有权限");
        }
        return R.ok("aa");
    }

    @Override
    public R downLoadExcelFileFromServer(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            File file = new File(uploadFile + "/" + fileName);
            if (!file.exists()) {
                file.mkdir();
            }
            ProductionExcelUtils.downLoadFile(request, response, String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public R uploadFile(MultipartFile file, HttpServletRequest request) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            return R.ok("上传文件不能为空");
        }
        String uuid = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        File dest = new File(uploadFile + uuid + suffixName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {

        }
        SysUser sysUser = ShiroUtils.getUser();
        int level = reportShowMapper.getOrgCodeLevelByOrgCode(sysUser.getOrgCode().toString());
        if (level == 2) {
            String name = sysUser.getName();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String time = s.format(date);
            if (file.getName() != null) {
                ExcelMessage excelMessage = new ExcelMessage();
                excelMessage.setMonth(time);
                String excelName = file.getOriginalFilename();
                excelMessage.setName(excelName);
                String orgCode = request.getParameter("orgCode");
                excelMessage.setOrgCode(sysUser.getOrgCode());
                excelMessage.setUsername(name);
                excelMessage.setUuid(uuid + suffixName);
                excelMessage.setCode(Long.parseLong(orgCode));
                excelMapper.addExcelMessage(excelMessage);
            }
            int excelId = excelMapper.getExcelIdByUUID(uuid + suffixName);
            try {
                Workbook workbook = null;
                //获取Excel工作薄对象
                Sheet sheet = null;
                if (file.getOriginalFilename().endsWith("xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                    sheet = workbook.getSheetAt(0);
                } else if (file.getOriginalFilename().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                    sheet = workbook.getSheetAt(0);

                }
                int firstRowNum = 0;
                // 一直读到最后一行
                int lastRowNum = 0;
                try {
                    lastRowNum = sheet.getLastRowNum();
                } catch (Exception e) {
                }
                //获取合并的单元格个数
                int countMergeNumber = 0;
                try {
                    countMergeNumber = sheet.getNumMergedRegions();
                } catch (Exception e) {
                }

                List<PoiUtil> poiUtilList = new ArrayList<>();
                List<CrossUtil> crossUtilList = new ArrayList<>();
                for (int i = 0; i < countMergeNumber; i++) {
                    CellRangeAddress range = sheet.getMergedRegion(i);
                    //第一列
                    int firstColumn = 0;
                    try {
                        firstColumn = range.getFirstColumn();
                    } catch (Exception e) {
                    }

                    //最后一列
                    int lastColumn = 0;
                    try {
                        lastColumn = range.getLastColumn();
                    } catch (Exception e) {
                    }

                    //第一行
                    int firstRow = 0;
                    try {
                        firstRow = range.getFirstRow();
                    } catch (Exception e) {
                    }

                    //最后一行
                    int lastRow = 0;
                    try {
                        lastRow = range.getLastRow();
                    } catch (Exception e) {
                    }

                    poiUtilList.add(new PoiUtil((firstRow + 1), (lastRow + 1), (firstColumn + 1), (lastColumn + 1)));
                    String rowAddColumn = (firstRow + 1) + "," + (firstColumn + 1);
                    int crossRomNumber = lastRow - firstRow;
                    int crossColumnNumber = lastColumn - firstColumn;
                    crossUtilList.add(new CrossUtil(rowAddColumn, crossRomNumber, crossColumnNumber));
                }
                Map<String, CrossUtil> map = new HashMap<>();
                crossUtilList.forEach((a) -> {
                    map.put(a.getRowAddLine(), a);
                });
                List<ExcelData> excelDataList = new ArrayList<>();
                //获取数据行
                for (int i = 0; i <= lastRowNum; i++) {
                    Row row = null;
                    try {
                        row = sheet.getRow(i);
                    } catch (Exception e) {
                    }

                    int lastCellNum = 0;
                    try {
                        lastCellNum = row.getLastCellNum();
                    } catch (Exception e) {
                    }
                    for (int j = 0; j < lastCellNum; j++) {
                        Cell cell = (Cell) row.getCell(j);
                        int width = 10;
                        int height = 20;
                        try {
                            width = (int) sheet.getColumnWidthInPixels(cell.getColumnIndex());
                        } catch (Exception e) {

                        }
                        try {
                            height = (int) (row.getHeightInPoints() / 72) * 96;
                        } catch (Exception e) {

                        }

                        String value = "";
                        try {
                            //  cell.getCellTypeEnum().compareTo(CellType.STRING);
                            //  value=  cell.getStringCellValue();

                            value = excelUtil.getCellValue(cell);
                        } catch (Exception e) {
                        }
                        int crossRow = 0;
                        int crossColumn = 0;
                        String rowAddColumn = (i + 1) + "," + (j + 1);
                        if (map.containsKey(rowAddColumn)) {
                            crossRow = map.get(rowAddColumn).getCrossRowNumber() + 1;
                            crossColumn = map.get(rowAddColumn).getCrossColumnNumber() + 1;
                        }
                        if (value.trim().contains("E")) {
                            try {
                                value = new BigDecimal(fileName.trim()).toPlainString();
                            } catch (Exception e) {

                            }

                        }
                        if (i == 0) {
                            excelDataList.add(new ExcelData(1, excelId, value, crossColumn, crossRow, (i + 1), (j + 1), 1, height, width));
                        } else {
                            excelDataList.add(new ExcelData(1, excelId, value, crossColumn, crossRow, (i + 1), (j + 1), 2, height, width));
                        }
                    }
                }

                excelDataList.forEach(a -> {

                    if (a.getCoreData().contains(".") && a.getCoreData().substring((a.getCoreData().lastIndexOf(".") + 1), a.getCoreData().length()).length() > 2) {
                        a.setCoreData(a.getCoreData().substring(0, a.getCoreData().lastIndexOf(".") + 3));
                    }
                });
                if (excelDataList.size() > 0) {
                    //将Excel数据存入数据库
                    excelMapper.addExcelList(excelDataList);
                    List<TableUtil> list = deleteExcelUtil.getDeleteData(poiUtilList);
                    list.forEach((a -> {
                        excelMapper.delExcelData(new DeletePara(a.getRowNumber(), a.getColumnNumber(), excelId));

                    }));
                } else {
                    return new R(200, "获取不到Excel数据，请调整后再上传数据", null);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return new R(200, "非市用户不能上传", null);
        }


        return new R(200, "上传成功", null);
    }


    @Value("${file.uploadFolder}")
    private String uploadFile;

    @Value("${file.staticAccessPath}")
    private String staticAccessPath;


    @Autowired
    private ExcelMapper excelMapper;

    ExcelUtil excelUtil = new ExcelUtil();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReportShowMapper reportShowMapper;


    DeleteExcelUtil deleteExcelUtil = new DeleteExcelUtil();
}

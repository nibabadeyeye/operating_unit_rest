package com.gpdi.operatingunit.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.controller.system.support.SysConstant;
import com.gpdi.operatingunit.dao.system.SysCostCenterMapper;
import com.gpdi.operatingunit.entity.system.SysCostCenter;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.CostCenterService;
import com.gpdi.operatingunit.utils.ExcelUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhb
 * @date 2019/10/28 17:49
 **/
@Service
public class CostCenterServiceImpl implements CostCenterService {

    private final static String[] COLUMNS = {"成本中心", "范围", "公司", "CCtC", "负责人",
            "负责的用户", "简要说明", "语言", "有效期自", "至", "归属区局", "归属营服中心", "id"};

    @Autowired
    private SysCostCenterMapper sysCostCenterMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public R uploadExcel(MultipartFile file) throws Exception {
        if (file == null) {
            return R.error("请选择上传的文件");
        }

        String filename = file.getOriginalFilename().toUpperCase();
        if (!filename.endsWith(".XLSX") && !filename.endsWith(".XLS")) {
            return R.error("请选择excel文件(.xls、.xlsx)");
        }

        //读取excel
        List<Object> list = ExcelUtils.readLessThan1000RowBySheetAndStream(file.getInputStream(), null);
        if (list.size() == 0) {
            return R.error("请勿上传空文件");
        }
        //读取表头
        List<String> cols = (List) list.get(0);
        for (int i = 0; i < cols.size(); i++) {
            if (!cols.get(i).equals(COLUMNS[i])) {
                return R.error("请按模板文件格式上传");
            }
        }
        //获取用户信息
        SysUser sysUser = ShiroUtils.getUser();
        //读取数据
        list.remove(0);
        for (Object object : list) {
            List<String> objects = (List) object;
            SysCostCenter sysCostCenter = new SysCostCenter();
            sysCostCenter.setCode(objects.get(0));
            sysCostCenter.setScope(objects.get(1));
            sysCostCenter.setCompany(objects.get(2));
            sysCostCenter.setCctc(objects.get(3));
            sysCostCenter.setHead(objects.get(4));
            sysCostCenter.setResponsibleUser(objects.get(5));
            sysCostCenter.setIntro(objects.get(6));
            sysCostCenter.setLanguage(objects.get(7));
            sysCostCenter.setFromDate(objects.get(8));
            sysCostCenter.setToDate(objects.get(9));
            sysCostCenter.setBelongDistrict(objects.get(10));
            sysCostCenter.setBelongCenter(objects.get(11));
            if (objects.get(12) == null) {
                sysCostCenter.setOrgCode(null);
            } else {
                sysCostCenter.setOrgCode(Long.parseLong(objects.get(12)));
            }
            sysCostCenter.setEnable(1);
            sysCostCenter.setCreateOperId(sysUser.getId());
            sysCostCenter.setCreateTime(new Date());
            sysCostCenterMapper.saveCostCenter(sysCostCenter);
        }
        return R.ok();
    }

    @Override
    public Map<String, Object> pageQuery(QueryParams queryParams) {
        Map<String, Object> map = new HashMap<>(2);
        QueryWrapper<SysCostCenter> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("enable", SysConstant.YES)
                .eq("top_org_code", ShiroUtils.getTopOrgCode())
                .and(wapper -> wapper.like("intro", queryParams.getSearchText())
                        .or().like("head", queryParams.getSearchText())
                        .or().like("code", queryParams.getSearchText())
                ).orderByDesc("id");
        // 查询第1页，每页返回5条
        Page<SysCostCenter> page = new Page<>(queryParams.getPageNumber(), queryParams.getPageSize());
        IPage<SysCostCenter> iPage = sysCostCenterMapper.selectPage(page, queryWrapper);
        map.put("records", iPage.getRecords());
        map.put("total", iPage.getTotal());
        return map;
    }

    @Override
    public SysCostCenter getById(Long id) {
        return sysCostCenterMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysCostCenter sysCostCenter) {
        SysUser user = ShiroUtils.getUser();
        UpdateWrapper<SysCostCenter> updateWrapper = new UpdateWrapper<>();
        if (sysCostCenter.getId() == null) {
            //id为空 代表新增
            sysCostCenter.setCreateTime(new Date());
            sysCostCenter.setCreateOperId(user.getId());
            sysCostCenter.setTopOrgCode(757000000L);
            sysCostCenter.setEnable(1);

//            sysCostCenterMapper.insert(sysCostCenter);
            sysCostCenterMapper.saveCostCenter(sysCostCenter);
        } else {
            //修改
            sysCostCenter.setUpdateTime(new Date());
            sysCostCenter.setUpdateOperId(user.getId());
//            updateWrapper.eq("id", sysCostCenter.getId());
            int updates = sysCostCenterMapper.updateById(sysCostCenter);
            System.out.println(updates);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        sysCostCenterMapper.deleteBatchIds(ids);
    }

}

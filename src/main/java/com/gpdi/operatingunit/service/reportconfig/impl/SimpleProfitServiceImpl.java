package com.gpdi.operatingunit.service.reportconfig.impl;

import com.gpdi.operatingunit.dao.reportconfig.SimpleProfitColumMapper;
import com.gpdi.operatingunit.dao.reportconfig.SimpleProfitsReportMapper;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitColumEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitReportDetailsEntity;
import com.gpdi.operatingunit.entity.reportconfig.SimpleProfitsReportEntity;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.reportconfig.SimpleProfitService;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author：long
 * @Date：2019/12/3 13:22
 * @Description：
 */
@Service
public class SimpleProfitServiceImpl implements SimpleProfitService {

    @Autowired
    private SimpleProfitColumMapper simpleProfitColumMapper;

    @Autowired
    private SimpleProfitsReportMapper simpleProfitsReportMapper;
    /**
     * description: 获取表头
     *
     * @param
     * @return java.util.List<java.util.Map<java.lang.Object,java.lang.Object>>
     */
    @Override
    public List<Map<Object,Object>> findAll() {
        List<Map<Object,Object>> list = new ArrayList<>();
        Map map = new HashMap();
        map.put("name","编码");
        map.put("coding","-1");
        map.put("width","50");
        map.put("child",null);
        list.add(map);

        Map mapl = new HashMap();
        mapl.put("name","项目");
        mapl.put("coding","-2");
        mapl.put("width","170");
        mapl.put("child",null);
        list.add(mapl);
        Map maps = new HashMap();
        maps.put("name","12期金额");
        maps.put("coding","-3");
        maps.put("width","380");
        maps.put("child",null);
        list.add(maps);
        List<SimpleProfitColumEntity> all = simpleProfitColumMapper.findAll();
        Map mapss = new HashMap();
        mapss.put("name","业务要素");
        mapss.put("coding","-4");
        mapss.put("child",all);
        list.add(mapss);
        return list;
    }

    /**
     * description: 查看用户可显示的数据
     *
     * @param topOrgCode    地市编码
     * @param pageNumber    页数
     * @param pageSize      显示行数
     * @return java.util.Map
     */
    @Override
    public Map findByTopOrgCode(Long topOrgCode,int pageNumber,int pageSize){
        List<SimpleProfitsReportEntity> list = simpleProfitsReportMapper.findByTopOrgCode(topOrgCode,pageNumber,pageSize);
        List<SimpleProfitsReportEntity> byTopOrgCodeCount = simpleProfitsReportMapper.findByTopOrgCodeCount(topOrgCode);
        Map map = new HashMap();
        map.put("total",byTopOrgCodeCount.size());
        map.put("rows",list);

        return map;
    }

    /**
     * description: 数据删除
     *
     * @param list
     * @return void
     */
    @Override
    public void delSimpleProfitsReportAndId(List<SimpleProfitsReportEntity> list){
        simpleProfitsReportMapper.delSimpleProfitsReportAndId(list);
    }

    /**
     * description: 获取需要展示的行
     *
     * @param
     * @return java.util.List<com.gpdi.operatingunit.entity.reportconfig.SimpleProfitEntity>
     */
    @Override
    public List<SimpleProfitEntity> findSimpleProfitAll(){
        List<SimpleProfitEntity> simpleProfitAll = simpleProfitsReportMapper.findSimpleProfitAll();
        return simpleProfitAll;
    }

    @Override
    public List<SimpleProfitsReportEntity> findByTopOrgCodeAndId(Long topOrgCode,Integer id){
        List<SimpleProfitsReportEntity> byTopOrgCodeAndId = simpleProfitsReportMapper.findByTopOrgCodeAndId(topOrgCode, id);
        return byTopOrgCodeAndId;
    }

    @Transactional
    @Override
    public int insertSimpleProfitReportDetails(List<SimpleProfitReportDetailsEntity> list){
        SimpleProfitsReportEntity simpleProfitsReportEntity = new SimpleProfitsReportEntity();
        SysUser user = ShiroUtils.getUser();
        Date date = new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        String name = "简易边际利润表" + sdf.format(date);
        simpleProfitsReportEntity.setName(name);
        simpleProfitsReportEntity.setCreateOperId(user.getId());
        simpleProfitsReportEntity.setUpdateOperId(user.getId());
        simpleProfitsReportEntity.setCreateTime(new Date());
        simpleProfitsReportEntity.setUpdateTime(new Date());
        Long topOrgCode = user.getTopOrgCode();
        simpleProfitsReportEntity.setTopOrgCode(topOrgCode);
        simpleProfitsReportEntity.setCreateOperUserName(user.getUsername());
        int i1 = simpleProfitsReportMapper.insertTimpleProfitsReport(simpleProfitsReportEntity);

        for(SimpleProfitReportDetailsEntity simpleProfitReportDetailsEntity : list){
            simpleProfitReportDetailsEntity.setSimpleProfitsReportId(simpleProfitsReportEntity.getId()+"");
        }
        int i = simpleProfitsReportMapper.insertSimpleProfitReportDetails(list);

        return i;
    }
}

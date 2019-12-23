package com.gpdi.operatingunit.service.reportconfig.impl;

import com.gpdi.operatingunit.dao.reportconfig.RelOrgReportColumnDao;
import com.gpdi.operatingunit.dao.system.SysOrganizationMapper;
import com.gpdi.operatingunit.dao.system.SysRoleMapper;
import com.gpdi.operatingunit.entity.reportconfig.RelOrgReportColumnEntity;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysRole;
import com.gpdi.operatingunit.service.reportconfig.RelOrgReportColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author：long
 * @Date：2019/11/14 15:45
 * @Description：
 */
@Service
public class RelOrgReportColumnServiceImpl implements RelOrgReportColumnService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    private RelOrgReportColumnDao relOrgReportColumnDao;

    @Autowired
    private RelOrgReportColumnService relOrgReportColumnService;

    @Override
    public List<RelOrgReportColumnEntity> queryByReportIdAndTopOrgCode(Long reportId,Long topOrgCode) {
        List<RelOrgReportColumnEntity> relOrgReportColumnEntities = null;
        try {
            /*Long userTopOrgCode = ShiroUtils.getUser().getTopOrgCode();
            if(userTopOrgCode != -1){
                //获取地市的编码
                topOrgCode = ShiroUtils.getUser().getTopOrgCode();
            }*/

            relOrgReportColumnEntities = relOrgReportColumnDao.queryByReportIdAndTopOrgCode(reportId,topOrgCode);

            for(RelOrgReportColumnEntity relOrgReportColumnEntity : relOrgReportColumnEntities){
                String str = relOrgReportColumnEntity.getProp();
                // 将下划线后第一个字母进行转换
                StringBuilder builder = new StringBuilder();
                Arrays.asList(str.split("_")).forEach(temp -> builder.append(StringUtils.capitalize(temp)));
                relOrgReportColumnEntity.setProp(StringUtils.uncapitalize(builder.toString()));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return relOrgReportColumnEntities;
    }

    @Override
    public List<SysRole> queryById(int userId) {
        List<SysRole> sysRoles = null;
        try {
            sysRoles = sysRoleMapper.queryById(userId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return sysRoles;
    }

    @Override
    public List<Map<Object, Object>> queryAllByReportIdAndTopOrgCode(String level,Long reportId, Long topOrgCode, Long orgCode){
        // 获取表头字段
        List<RelOrgReportColumnEntity> relOrgReportColumnEntities = relOrgReportColumnService.queryByReportIdAndTopOrgCode(reportId, topOrgCode);
        // 获取所有区
        List<SysOrganization> list = sysOrganizationMapper.getOrganization(level,topOrgCode);

        List<SysOrganization> organization = new ArrayList<>();
        // 判断显示多少区
        for(SysOrganization sysOrganization : list){
            Long code = sysOrganization.getCode();
            if(code.equals(orgCode)){
                organization.add(sysOrganization);
                break;
            }
        }
        if(organization.size() > 0){
            list = organization;
        }

        List<Map<Object, Object>> result = new ArrayList<>();
        for(SysOrganization sysOrganization : list){
            Map<Object,Object> map = new HashMap<>();
            map.put("dataItem",sysOrganization.getCode().toString());
            map.put("dataName",sysOrganization.getAlias());

            List<Map<Object, Object>> columList = new ArrayList<>();
            for(RelOrgReportColumnEntity relOrgReportColumnEntity : relOrgReportColumnEntities){
                Map<Object, Object> columMap = new HashMap<>();
                String prop = relOrgReportColumnEntity.getProp();
                String tip = "";
                if(prop.equals("diffLastMonth")){
                    tip = "比上月增减(万元) = 本月数-上月数";
                } else if(prop.equals("percLastMonth")){
                    tip = "比上月增减(%) = (本月数-上月数)/上月数";
                } else if(prop.equals("diffLastYear")){
                    tip = "比上年增减(万元) = 本年累计-上年累计";
                } else if(prop.equals("percLastYear")){
                    tip = "比上年增减(%) = (本年累计-上年累计)/上年累计";
                }
                columMap.put("dataItem",relOrgReportColumnEntity.getProp());
                columMap.put("dataName",relOrgReportColumnEntity.getName());
                columMap.put("data","");
                columMap.put("width",200);
                columMap.put("tip",tip);
                columList.add(columMap);
            }
            map.put("tableData",columList);
            result.add(map);
        }
        System.out.println(result);
        return result;
    }
}

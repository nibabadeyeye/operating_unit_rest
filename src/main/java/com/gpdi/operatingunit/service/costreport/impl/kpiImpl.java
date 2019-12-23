package com.gpdi.operatingunit.service.costreport.impl;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.dao.costreport.KpiMapper;
import com.gpdi.operatingunit.dao.costreport.ReportShowMapper;
import com.gpdi.operatingunit.entity.costreport.Kpi;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.costreport.KpiService;
import com.gpdi.operatingunit.utils.ExcelUtils;
import com.gpdi.operatingunit.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
@Service
public class kpiImpl implements KpiService {

    @Override
    public R getServiceApiByServiceName(String serviceName) {
        return new R(200, "请求成功", kpiMapper.getServiceApiByServiceName(serviceName));
    }

    @Override
    public R uploadKpiData(InputStream in) {
        List<Object> objectList = new Vector<>();
        try {
            objectList = ExcelUtils.readLessThan1000RowBySheetAndStream(in, null);
        } catch (Exception e) {

        }
        if (objectList.size() <= 0) {
            return R.error("数据为空");
        }
        List<Kpi> listEntityList = new ArrayList<>();
        for (int i = 1; i < objectList.size(); i++) {
            Kpi d = new Kpi();
            List list = (List) objectList.get(i);
            if (list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        try {
                            d.setDistrict(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 1) {
                        try {
                            d.setServiceName(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }

                    if (j == 2) {
                        try {
                            d.setIncomeIndicators(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 3) {
                        try {
                            d.setYear(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 4) {
                        try {
                            d.setRateIncrease(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 5) {
                        try {
                            d.setStockLaborCost(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 6) {
                        try {
                            d.setStockCostDeducted(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 7) {
                        try {
                            d.setAddCost(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 8) {
                        try {
                            d.setLaborBaseCostAfterClaim(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 9) {
                        try {
                            d.setCumulativeImplementation(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 10) {
                        try {
                            d.setCumulativeDistribution(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 11) {
                        try {
                            d.setDistributionProgress(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 12) {
                        try {
                            d.setMaxDistributionProgress(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 13) {
                        try {
                            d.setSpaceAmount(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 14) {
                        try {
                            d.setSettlementAmount(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 15) {
                        try {
                            d.setCeoAmount(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 16) {
                        try {
                            d.setStockCostPayable(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 17) {
                        try {
                            d.setIncreasedCosts(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 18) {
                        try {
                            d.setIncrementalAward(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 19) {
                        try {
                            d.setMaintenancePerformanceAward(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 20) {
                        try {
                            d.setTotalPay(list.get(j).toString());
                        } catch (Exception e) {
                        }
                    }
                    if (j == 20) {
                        listEntityList.add(d);
                    }
                }
            }

        }
        return R.ok("上传数据成功");
    }

    @Override
    public R getSysOrgTree() {
        SysUser sysUser = (SysUser) ShiroUtils.getUser();
        String sql = "select * from sys_organization ";
        List<SysOrganization> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysOrganization.class));
        //获取完整树
        SysOrganization sysOrganization = new SysOrganization();
        int level = reportShowMapper.getOrgCodeLevelByOrgCode(sysUser.getOrgCode().toString());

        //初始化树的根节点
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().toString().equals(sysUser.getOrgCode().toString())) {
                sysOrganization = list.get(i);
                break;
            }
        }
        if (level == 1) {
            //拼接二级节点
            List<SysOrganization> secondList = new Vector<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getParentCode().toString().equals(sysOrganization.getCode())) {
                    //将信息加入children
                    try {
                        List<SysOrganization> childrenList = sysOrganization.getChildren();
                        if (childrenList.size() != 0) {
                            secondList = childrenList;
                        }
                    } catch (Exception e) {

                    }
                    secondList.add(list.get(i));
                    sysOrganization.setChildren(secondList);
                    list.remove(list.get(i));
                    i--;

                }
            }

            //拼接三级区
            for (int i = 0; i < list.size(); i++) {
                List<SysOrganization> threeOrganizedList = new Vector<>();
                for (int j = 0; j < secondList.size(); j++) {
                    if (secondList.get(j).getCode().toString().trim().equals(list.get(i).getParentCode().toString().trim())) {
                        try {
                            List<SysOrganization> childrenList = secondList.get(j).getChildren();
                            if (childrenList.size() != 0) {
                                threeOrganizedList = childrenList;
                            }
                        } catch (Exception e) {

                        }
                        threeOrganizedList.add(list.get(i));
                        secondList.get(j).setChildren(threeOrganizedList);
                    }

                }
            }
            //拼接四级
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < secondList.size(); j++) {
                    List<SysOrganization> threeOrganizedList = new Vector<>();
                    for (int k = 0; k < secondList.get(j).getChildren().size(); k++) {
                        if (secondList.get(j).getChildren().get(k).getCode().toString().trim().equals(list.get(i).getParentCode().toString().trim())) {
                            try {
                                List<SysOrganization> childrenList = secondList.get(j).getChildren().get(k).getChildren();
                                if (childrenList.size() != 0) {
                                    threeOrganizedList = childrenList;
                                }
                            } catch (Exception e) {
                            }
                            threeOrganizedList.add(list.get(i));
                            secondList.get(j).getChildren().get(k).setChildren(threeOrganizedList);
                        }
                    }

                }
            }
        } else if (level == 2) {
            //装入当前市中对应的区信息
            List<SysOrganization> secondList = new Vector<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getParentCode().toString().equals(sysOrganization.getCode().toString())) {
                    //将信息加入children
                    try {
                        List<SysOrganization> childrenList = sysOrganization.getChildren();
                        if (childrenList.size() != 0) {
                            secondList = childrenList;
                        }
                    } catch (Exception e) {

                    }
                    secondList.add(list.get(i));
                    sysOrganization.setChildren(secondList);
                    list.remove(list.get(i));
                    i--;

                }
            }
            //装入当前区对应的营服信息
            for (int i = 0; i < list.size(); i++) {
                List<SysOrganization> threeOrganizedList = new Vector<>();
                for (int j = 0; j < secondList.size(); j++) {
                    if (secondList.get(j).getCode().toString().trim().equals(list.get(i).getParentCode().toString().trim())) {
                        try {
                            List<SysOrganization> childrenList = secondList.get(j).getChildren();
                            if (childrenList.size() != 0) {
                                threeOrganizedList = childrenList;
                            }
                        } catch (Exception e) {

                        }
                        threeOrganizedList.add(list.get(i));
                        secondList.get(j).setChildren(threeOrganizedList);
                    }

                }
            }

        } else if (level == 3) {
            //装入当前区对应的营服信息
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCode().toString().equals(sysUser.getOrgCode().toString())) {
                    sysOrganization = list.get(i);
                }
            }
            //把营服的信息装入区中
            List<SysOrganization> secondList = new Vector<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getParentCode().toString().equals(sysOrganization.getCode().toString())) {
                    //将信息加入children
                    try {
                        List<SysOrganization> childrenList = sysOrganization.getChildren();
                        if (childrenList.size() != 0) {
                            secondList = childrenList;
                        }
                    } catch (Exception e) {

                    }
                    secondList.add(list.get(i));
                    sysOrganization.setChildren(secondList);
                    list.remove(list.get(i));
                    i--;
                }
            }

        }

        return new R(200, "请求成功", sysOrganization);
    }

    @Autowired
    private KpiMapper kpiMapper;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReportShowMapper reportShowMapper;


}

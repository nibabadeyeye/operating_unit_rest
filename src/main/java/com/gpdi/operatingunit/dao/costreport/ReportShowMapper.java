package com.gpdi.operatingunit.dao.costreport;

import com.gpdi.operatingunit.entity.costreport.BadCost;
import com.gpdi.operatingunit.entity.costreport.CostIndex;
import com.gpdi.operatingunit.entity.costreport.MonthIncome;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import com.gpdi.operatingunit.entity.system.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Desc :个性化报表展示
 * @author: whs
 */
public interface ReportShowMapper {

    /**
     * @desc: 初始化所有的组织信息
     */
    public List<SysOrganization> getAllOrganized();


    /**
     * @desc: 根据月份和营服Id统计当前营服的所有项目总费用
     */
    public String sumAllItemsCost(@Param("month") int month, @Param("organizedId") int organizedId);

    /**
     * @desc: 查询成本明细
     */
    public List<Map<Object, Object>> getAllCostItemsByOrganizedIdAndMonth(@Param("month") int month, @Param("organizedId") int organizedId);


    /**
     * @desc: 导入坏账
     */
    public void add(List<BadCost> list);

    /**
     * @desc:查询坏账金额
     */
    public String getBadCodeFromMonthAndOrganized(@Param("month") int month, @Param("organized") int organized);

    /**
     * @desc: 查询当月收入
     */
    public String getMonthIncomeByMonthAndServiceName(@Param("month") int month, @Param("serviceName") String serviceName);

    /**
     * @desc: 根据组织id, 查询组织名称（营服中心名称）
     */
    public String getOrganizedServiceName(int code);


    /**
     * @desc: 根据月份和营服名称查询成本信息（总费用，月度累计消费费用）
     */
    public int getCostIndexByMonthAndCenterService(@Param("month") int month, @Param("serviceName") String serviceName);


    /**
     * @desc: 根据具体月份查询所有的成本报表
     */

    public List<CostIndex> getCostIndexBySQL(String sql);

    /**
     * 根据code查询组织名称
     */
    String getOranziedByCode(String code);


    //批量插入坏账数据
    public void addBadCostlList(List<BadCost> list);

    //批量插入月度成本数据
    public void addCostIndexlList(List<CostIndex> list);

    //批量插入成本数据
    public void addCostIncomeList(List<MonthIncome> list);


    //根据组织编号查询所属组织级别
    public int getOrgCodeLevelByOrgCode(String code);

    //查询对应的角色信息
    public SysRole getRoleByUserId(int id);

    //根据顶级组织编号查询所有的本市的结构信息
    public List<SysOrganization> getAllSysOrganizedByTopOrgCode(long code);
}

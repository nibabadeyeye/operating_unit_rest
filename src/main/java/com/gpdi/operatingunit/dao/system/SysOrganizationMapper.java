package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.SysOrganization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhb
 * @date 2019/10/29 9:22
 **/
@Repository
public interface SysOrganizationMapper extends BaseMapper<SysOrganization> {

    /**
     * description: 获取划小下拉框数据
     *
     * @param level
     * @param parentCode
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    List<SysOrganization> getOrganization(@Param("level") String level, @Param("parentCode") Long parentCode);

    /**
     * 获取市下的所有数据
     *
     * @param topOrgCode
     * @return
     */
    List<SysOrganization> getAllCenter(String topOrgCode);


    /**
     * 获取全部组织
     *
     * @param topOrgCode 顶级组织编码
     * @return 组织列表
     */
    List<SysOrganization> queryList(@Param("topOrgCode") Long topOrgCode);

    /**
     * description: 获取所属区下的营服
     *
     * @param orgCode
     * @return java.util.List<com.gpdi.operatingunit.entity.system.SysOrganization>
     */
    List<SysOrganization> getParentCode(@Param("orgCode") Long orgCode);

    /**
     * 根据code修改enable
     *
     * @param codes        编码列表
     * @param updateOperId 操作人id
     * @param enable       启用状态
     */
    void updateEnableByCodes(@Param("codes") List<Long> codes,
                             @Param("updateOperId") Integer updateOperId,
                             @Param("enable") Integer enable);

    /**
     * 根据code获取组织对象
     *
     * @param code 组织编码
     * @return 组织对象
     */
    SysOrganization getByCode(@Param("code") Long code);

    /**
     * 根据等级和父级编码获取最大排序值
     *
     * @param level      等级
     * @param parentCode 父级编码
     * @return 排序值
     */
    Integer getMaxSeqByLevelAndParentCode(@Param("level") Integer level, @Param("parentCode") Long parentCode);

    /**
     * 查询所有关联的子项
     *
     * @param code 编码
     * @return 所有子项编码列表
     */
    List<Long> queryAllChildCodes(@Param("code") Long code);

    /**
     * 根据组织编码列表删除
     *
     * @param codes 组织编码列表
     * @return 影响条数
     */
    int deleteByCodes(@Param("codes") List<Long> codes);

    /**
     * description: 获取营服中心
     *
     * @param topOrgCode
     * @param parentCode
     * @return com.gpdi.operatingunit.entity.system.SysOrganization
     */
    SysOrganization getByTopOrgCodeAndParentCode(@Param("topOrgCode") Long topOrgCode, @Param("parentCode") Long parentCode);

    /**
     * description: 获取角色所属区域
     *
     * @param topOrgCode
     * @param orgCode
     * @return com.gpdi.operatingunit.entity.system.SysOrganization
     */
    List<SysOrganization> getByTopOrgCodeAndOrgCode(@Param("topOrgCode") Long topOrgCode, @Param("orgCode") Long orgCode);

    /**
     * description: 获取角色所属区域
     *
     * @param topOrgCode
     * @param orgCode
     * @return com.gpdi.operatingunit.entity.system.SysOrganization
     */
    List<SysOrganization> getByTopOrgCodeAndOrgCodeCity(@Param("topOrgCode") Long topOrgCode, @Param("orgCode") Long orgCode);
}

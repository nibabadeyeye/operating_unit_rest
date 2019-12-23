package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.SysSapSubject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SAP科目表
 * 
 * @author Lxq
 * @email 836745803@qq.com
 * @date 2019-10-27 21:17:01
 */
@Repository
public interface SysSapSubjectMapper extends BaseMapper<SysSapSubject> {

    /**
     * 获取提供选择的sap项目
     * @param id
     * @param reportId
     * @param year
     * @return
     */
    List<SysSapSubject> getSapSubject(@Param("id") Integer id, @Param("reportId") Integer reportId,@Param("year") Integer year);
}

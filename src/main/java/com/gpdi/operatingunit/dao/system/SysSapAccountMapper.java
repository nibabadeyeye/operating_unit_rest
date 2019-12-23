package com.gpdi.operatingunit.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpdi.operatingunit.entity.system.SysSapAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author zyb
 * @date 2019-11-27 21:57:54
 */
@Repository
public interface SysSapAccountMapper extends BaseMapper<SysSapAccount> {

    /**
     * 获取市所属的sap账号
     *
     * @param type
     * @param topOrgCode
     * @return
     */
    List<SysSapAccount> getAccountList(@Param("type") String type, @Param("topOrgCode") Long topOrgCode);
}

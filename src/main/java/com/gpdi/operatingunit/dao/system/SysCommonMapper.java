package com.gpdi.operatingunit.dao.system;

import com.gpdi.operatingunit.service.system.support.ColumnInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhb
 * @date 2019/10/30 10:57
 **/
@Repository
public interface SysCommonMapper {

    List<ColumnInfo> queryColumnInfo(String tableName, String tableSchema);
}

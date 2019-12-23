package com.gpdi.operatingunit.service.system;

import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.controller.system.support.QueryParams;
import com.gpdi.operatingunit.entity.system.SysCostCenter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Zhb
 * @date 2019/10/28 17:49
 **/
public interface CostCenterService {

    /**
     * 保存成本中心数据
     *
     * @param file excel文件
     * @return R 结果
     * @throws Exception 异常
     */
    R uploadExcel(MultipartFile file) throws Exception;

    /**
     * 分页查询成本中心数据
     *
     * @param queryParams 多个参数
     * @return 分页查询结果
     */
    Map<String, Object> pageQuery(QueryParams queryParams);

    /**
     * 根据id获取成本中心
     *
     * @param id 主键
     * @return 成本中心对象
     */
    SysCostCenter getById(Long id);

    /**
     * 保存成本中心
     *
     * @param sysCostCenter 成本中心对象
     */
    void save(SysCostCenter sysCostCenter);

    /**
     * 删除成本中心
     *
     * @param ids 主键列表
     */
    void deleteByIds(List<Long> ids);
}

package com.gpdi.operatingunit.entity.originaltabledisplay;

import lombok.Data;

/**
 * @desc: created by whs on 2019/00/14
 * <p>
 * Excel表信息
 */
@Data
public class ExcelMessage {
    //表格主键
    private int id;
    //Excel表名称
    private String name;
    //Excel上传时间
    private String month;
    //营服名称
    private String serviceName;
    //上传用户名称
    private String username;
    //随机生产的UUID
    private String uuid;
    //所属组织编码
    private long code;
//    //所属市、区、营服
//    private  int level;
    private long orgCode;



}

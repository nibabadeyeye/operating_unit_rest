package com.gpdi.operatingunit.entity.common;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @Author: zyb
 * @Date: 2019/10/25 16:14
 */

public class SapList {

    @TableId
    private Long id;
    private String columnName;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

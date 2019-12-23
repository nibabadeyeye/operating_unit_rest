package com.gpdi.operatingunit.controller.system.support;

import java.io.Serializable;

/**
 * @author Zhb
 * @date 2019/10/30 11:10
 **/
public class QueryParams implements Serializable {

    private Long topOrgCode;
    private Long orgCode;
    private String searchText;

    /**
     * page settings
     */
    private int pageNumber;
    private int total;
    private int pageSize;

    public Long getTopOrgCode() {
        return topOrgCode;
    }

    public void setTopOrgCode(Long topOrgCode) {
        this.topOrgCode = topOrgCode;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public int getPageNumber() {
        return pageNumber <= 0 ? 1 : pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize <= 0 ? 10 : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

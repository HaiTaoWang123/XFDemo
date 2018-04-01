package com.example.dxc.xfdemo.model;

import java.util.List;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-7:11 PM.
 * @Version 1.0
 */

public class BaseStockMdl {
    private String totalCount;//总条数
    private String page;//当前页
    private String num;//显示条数
    private List<Stock> data;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<Stock> getData() {
        return data;
    }

    public void setData(List<Stock> data) {
        this.data = data;
    }
}

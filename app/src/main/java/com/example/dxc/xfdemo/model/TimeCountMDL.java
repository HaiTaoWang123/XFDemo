package com.example.dxc.xfdemo.model;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/29/2018-9:47 AM.
 * @Version 1.0
 */

public class TimeCountMDL {

    private String time;
    private String intervalTime;

    public TimeCountMDL(String time, String intervalTime) {
        this.time = time;
        this.intervalTime = intervalTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }
}
package com.example.dxc.xfdemo.network.base;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-6:33 PM.
 * @Version 1.0
 */

public interface NetWorkListener<D> {
    void onSuccess(D data);
    void onFailed(String errMsg);
}

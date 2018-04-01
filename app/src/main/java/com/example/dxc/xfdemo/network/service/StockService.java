package com.example.dxc.xfdemo.network.service;

import com.example.dxc.xfdemo.common.StringConfig;
import com.example.dxc.xfdemo.model.BaseStockMdl;
import com.example.dxc.xfdemo.network.api.StockApi;
import com.example.dxc.xfdemo.network.base.BaseCallBack;
import com.example.dxc.xfdemo.network.base.BaseResponse;
import com.example.dxc.xfdemo.network.base.BaseService;
import com.example.dxc.xfdemo.network.base.NetWorkListener;

import retrofit2.Call;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-6:58 PM.
 * @Version 1.0
 */

public class StockService {
    private static StockApi stockApi;

    /**
     * 获取沪市或者深市股票实时列表
     * @param stockType A：A股/B:B股
     * @param pageIndex 第几页
     * @param num 每页数量：1-20条；2-40条；3-60条；4-80条
     * @param listener
     */
    public static void getStockList(String stockType, int pageIndex, int num, NetWorkListener listener){
        if (null == stockApi){
            stockApi = BaseService.getStockApi();
        }

        Call<BaseResponse<BaseStockMdl>> call = stockApi.getAllSHStock(StringConfig.NETWORK_KEY,stockType,pageIndex,num);
        call.enqueue(new BaseCallBack<BaseStockMdl>(listener));
    }
}

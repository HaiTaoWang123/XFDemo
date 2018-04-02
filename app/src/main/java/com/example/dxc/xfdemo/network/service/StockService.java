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
     * @param flag 0-沪市；1-深市
     * @param stockType A：A股/B:B股
     * @param pageIndex 第几页
     * @param type 每页数量：1-20条；2-40条；3-60条；4-80条
     * @param listener
     */
    public static void getStockList(int flag,String stockType, int pageIndex, int type, NetWorkListener listener){
        if (null == stockApi){
            stockApi = BaseService.getStockApi();
        }
        Call<BaseResponse<BaseStockMdl>> call;
        if (flag == 0) {
            call = stockApi.getAllSHStock(StringConfig.NETWORK_KEY, stockType, pageIndex, type);
        }else {
            call = stockApi.getAllSZStock(StringConfig.NETWORK_KEY, stockType, pageIndex, type);
        }
        call.enqueue(new BaseCallBack<BaseStockMdl>(listener));
    }

//    public static void getWeatherList(String stockType, int pageIndex, int num, NetWorkListener listener){
//        if (null == stockApi){
//            stockApi = BaseService.getStockApi();
//        }
//
//        Call<BaseResponse<String>> call = stockApi.currentWeather(StringConfig.WEATHER_KEY,"武汉","zh-Hans","c",0,5);
//        call.enqueue(new BaseCallBack<String>(listener));
//    }
}

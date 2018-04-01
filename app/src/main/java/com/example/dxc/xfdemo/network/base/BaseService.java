package com.example.dxc.xfdemo.network.base;

import com.example.dxc.xfdemo.network.api.StockApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-5:25 PM.
 * @Version 1.0
 */

public class BaseService {
    private static String URL = "http://web.juhe.cn:8080/finance/stock/";
    //静态的 Retrofit 实例
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofitClient(){
        if (null == retrofit){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static StockApi getStockApi(){
        return getRetrofitClient().create(StockApi.class);
    }
}

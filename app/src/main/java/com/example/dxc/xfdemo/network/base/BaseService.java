package com.example.dxc.xfdemo.network.base;

import com.example.dxc.xfdemo.common.StringConfig;
import com.example.dxc.xfdemo.network.api.StockApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-5:25 PM.
 * @Version 1.0
 */

public class BaseService {

    //静态的 Retrofit 实例
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofitClient(){
        if (null == retrofit){
            retrofit = new Retrofit.Builder()
                    .baseUrl(StringConfig.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static StockApi getStockApi(){
        return getRetrofitClient().create(StockApi.class);
    }
}

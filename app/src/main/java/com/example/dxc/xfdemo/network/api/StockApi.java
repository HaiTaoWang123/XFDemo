package com.example.dxc.xfdemo.network.api;

import com.example.dxc.xfdemo.model.BaseStockMdl;
import com.example.dxc.xfdemo.network.base.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-5:44 PM.
 * @Version 1.0
 */

public interface StockApi {
    @FormUrlEncoded
    @POST("shall")
    Call<BaseResponse<BaseStockMdl>> getAllSHStock(@Field("key") String key,
                                                   @Field("stock") String stock,
                                                   @Field("page") int page,
                                                   @Field("type") int type);

    @FormUrlEncoded
    @POST("szall")
    Call<BaseResponse<BaseStockMdl>> getAllSZStock(@Field("key") String key,
                               @Field("stock") String stock,
                               @Field("page") int page,
                               @Field("type") int type);

    @GET("now.json")
    Call<BaseResponse<String>> currentWeather(@Query("key") String key,
                                              @Query("location") String city,
                                              @Query("language") String language,
                                              @Query("unit") String unit,
                                              @Query("start") int start,
                                              @Query("days") int days);
}

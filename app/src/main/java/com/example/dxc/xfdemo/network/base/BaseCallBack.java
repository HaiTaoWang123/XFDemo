package com.example.dxc.xfdemo.network.base;

import com.example.dxc.xfdemo.common.StringConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-6:28 PM.
 * @Version 1.0
 */
@SuppressWarnings("unchecked")
public class BaseCallBack<T> implements Callback<BaseResponse<T>> {
    private NetWorkListener netWorkListener;

    public BaseCallBack(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
    }



    @Override
    public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        if (response.isSuccessful()){
            BaseResponse<T> result = response.body();
            if (null != result){
                if (result.getError_code().equals(StringConfig.NETWORK_CODE)
                        && result.getReason().equals(StringConfig.NETWORK_SUCCESS)){
                        netWorkListener.onSuccess(result.getResult());
                }else {
                    netWorkListener.onFailed(result.getReason());
                }
            }else {
                netWorkListener.onFailed(response.errorBody().toString());
            }
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        //请求服务器失败
        netWorkListener.onFailed("请求失败");
    }
}

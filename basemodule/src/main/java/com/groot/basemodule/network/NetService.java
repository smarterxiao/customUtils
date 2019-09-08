package com.groot.basemodule.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * author: 肖雷
 * created on: 2019/9/8
 * description:
 */
public interface NetService {

    @GET("group/{id}/users")
    Observable<String> login();


    @GET("group/{id}/users")
    Call<List<String>> groupList(@Path("id") int groupId);
}

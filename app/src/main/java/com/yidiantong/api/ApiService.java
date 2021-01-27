package com.yidiantong.api;


import com.yidiantong.bean.VersionBean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiService {
    @POST("getLatestVersion")
    Observable<VersionBean> getVersion();
}

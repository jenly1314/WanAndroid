package com.king.wanandroid.app.base;

import com.king.wanandroid.api.ApiService;
import com.king.wanandroid.bean.BaseResult;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class BaseRepository {

    private ApiService apiService;

    public BaseRepository(ApiService apiService){
        this.apiService = apiService;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public boolean isSuccess(BaseResult result){
        return result!=null && result.isSuccess();
    }


}

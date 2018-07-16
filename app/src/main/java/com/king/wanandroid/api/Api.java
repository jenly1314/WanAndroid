package com.king.wanandroid.api;

import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.BaseResult;
import com.king.wanandroid.bean.DataBean;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Callback;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class Api {

    private ApiService apiService;

    @Inject
    public Api(ApiService apiService){
        this.apiService = apiService;
    }

    public void getArticlesList(int curPage, Callback<BaseResult<DataBean<List<ArticleBean>>>> callback){
        apiService.getArticlesList(curPage).enqueue(callback);
    }


}

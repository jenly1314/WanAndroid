package com.king.wanandroid.app.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.king.wanandroid.api.ApiCallback;
import com.king.wanandroid.api.ApiService;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.BannerBean;
import com.king.wanandroid.bean.BaseResult;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.HotKeyBean;
import com.king.wanandroid.bean.NaviBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.bean.Result;
import com.king.wanandroid.bean.SearchHistory;
import com.king.wanandroid.bean.TreeBean;
import com.king.wanandroid.bean.User;
import com.king.wanandroid.bean.VersionBean;
import com.king.wanandroid.dao.DataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Singleton
public class DataRepository extends BaseRepository {

    @Inject
    DataSource mDataSource;

    @Inject
    public DataRepository(ApiService apiService) {
        super(apiService);
    }

    /**
     * 首页文章列表
     * @param curPage
     * @return
     */
    public LiveData<Resource<DataBean<List<ArticleBean>>>> getArticlesList(int curPage){

        final MutableLiveData<Resource<DataBean<List<ArticleBean>>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getArticlesList(curPage -1).enqueue(new ApiCallback<BaseResult<DataBean<List<ArticleBean>>>>() {

            @Override
            public void onResponse(Call<BaseResult<DataBean<List<ArticleBean>>>> call, BaseResult<DataBean<List<ArticleBean>>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<DataBean<List<ArticleBean>>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    /**
     * 首页Banner
     * @return
     */
    public LiveData<Resource<List<BannerBean>>> getBanner(){
        final MutableLiveData<Resource<List<BannerBean>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getBanner().enqueue(new ApiCallback<BaseResult<List<BannerBean>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<BannerBean>>> call, BaseResult<List<BannerBean>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<List<BannerBean>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public LiveData<Resource<User>> login(String username,String password){
        final MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        getApiService().login(username,password).enqueue(new ApiCallback<BaseResult<User>>() {
            @Override
            public void onResponse(Call<BaseResult<User>> call, BaseResult<User> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<User>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    public LiveData<Resource<User>> register(String username, String password){
        final MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        getApiService().register(username,password,password).enqueue(new ApiCallback<BaseResult<User>>() {
            @Override
            public void onResponse(Call<BaseResult<User>> call, BaseResult<User> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<User>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    //-------------------------------------------------

    /**
     * 收藏内站文章
     * @param id
     * @return
     */
    public LiveData<Resource<CollectBean>> collect(int id){
        final MutableLiveData<Resource<CollectBean>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().collect(id).enqueue(new ApiCallback<BaseResult<CollectBean>>() {
            @Override
            public void onResponse(Call<BaseResult<CollectBean>> call, BaseResult<CollectBean> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<CollectBean>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }

        });
        return liveData;
    }

    /**
     * 收藏外站文章
     * @param title
     * @param author
     * @param link
     * @return
     */
    public LiveData<Resource<CollectBean>> collect(String title,String author,String link){
        final MutableLiveData<Resource<CollectBean>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().collect(title,author,link).enqueue(new ApiCallback<BaseResult<CollectBean>>() {
            @Override
            public void onResponse(Call<BaseResult<CollectBean>> call, BaseResult<CollectBean> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<CollectBean>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }

        });
        return liveData;
    }


    /**
     * 取消收藏
     * @param id
     * @return
     */
    public LiveData<Resource> unCollect(int id){
        final MutableLiveData<Resource> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().unCollect(id).enqueue(new ApiCallback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Result result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<Result> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }

        });
        return liveData;
    }

    /**
     * 获取我的收藏列表
     * @param curPage
     * @return
     */
    public LiveData<Resource<DataBean<List<CollectBean>>>> getCollectList(int curPage){
        final MutableLiveData<Resource<DataBean<List<CollectBean>>>>  liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getCollectList(curPage -1).enqueue(new ApiCallback<BaseResult<DataBean<List<CollectBean>>>>() {
            @Override
            public void onResponse(Call<BaseResult<DataBean<List<CollectBean>>>> call, BaseResult<DataBean<List<CollectBean>>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<DataBean<List<CollectBean>>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });
        return liveData;
    }

    /**
     * 取消收藏(我的收藏界面用)
     * @param id
     * @return
     */
    public LiveData<Resource> unMyCollect(int id,int originId){
        final MutableLiveData<Resource> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().unMyCollect(id,originId).enqueue(new ApiCallback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Result result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<Result> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }

        });
        return liveData;
    }

    //-------------------------------------------------

    /**
     * 知识体系
     * @return
     */
    public LiveData<Resource<List<TreeBean>>> getSystem(){
        final MutableLiveData<Resource<List<TreeBean>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getSystem().enqueue(new ApiCallback<BaseResult<List<TreeBean>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<TreeBean>>> call, BaseResult<List<TreeBean>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<List<TreeBean>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });
        return liveData;
    }

    /**
     * 项目分类
     * @return
     */
    public LiveData<Resource<List<TreeBean>>> getProject(){
        final MutableLiveData<Resource<List<TreeBean>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getProject().enqueue(new ApiCallback<BaseResult<List<TreeBean>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<TreeBean>>> call, BaseResult<List<TreeBean>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<List<TreeBean>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });
        return liveData;
    }

    /**
     * 获取项目列表
     * @param curPage
     * @param id
     * @return
     */
    public LiveData<Resource<DataBean<List<ArticleBean>>>> getProjectList(int curPage,int id){

        final MutableLiveData<Resource<DataBean<List<ArticleBean>>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getProjectList(curPage,id).enqueue(new ApiCallback<BaseResult<DataBean<List<ArticleBean>>>>() {

            @Override
            public void onResponse(Call<BaseResult<DataBean<List<ArticleBean>>>> call, BaseResult<DataBean<List<ArticleBean>>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<DataBean<List<ArticleBean>>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    /**
     * 文章列表（通过分类Id）
     * @param curPage
     * @param id
     * @return
     */
    public LiveData<Resource<DataBean<List<ArticleBean>>>> getArticlesList(int curPage,int id){

        final MutableLiveData<Resource<DataBean<List<ArticleBean>>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getArticlesList(curPage-1,id).enqueue(new ApiCallback<BaseResult<DataBean<List<ArticleBean>>>>() {

            @Override
            public void onResponse(Call<BaseResult<DataBean<List<ArticleBean>>>> call, BaseResult<DataBean<List<ArticleBean>>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<DataBean<List<ArticleBean>>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    /**
     * 获取文章列表（通过关键字搜索）
     * @param curPage
     * @param key
     * @return
     */
    public LiveData<Resource<DataBean<List<ArticleBean>>>> getArticlesList(int curPage,String key){

        final MutableLiveData<Resource<DataBean<List<ArticleBean>>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getArticlesList(curPage-1,key).enqueue(new ApiCallback<BaseResult<DataBean<List<ArticleBean>>>>() {

            @Override
            public void onResponse(Call<BaseResult<DataBean<List<ArticleBean>>>> call, BaseResult<DataBean<List<ArticleBean>>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<DataBean<List<ArticleBean>>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }
    //-------------------------------------------------

    /**
     * 获取搜索热词
     * @return
     */
    public LiveData<Resource<List<HotKeyBean>>> getHotkey(){
        final MutableLiveData<Resource<List<HotKeyBean>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getHotkey().enqueue(new ApiCallback<BaseResult<List<HotKeyBean>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<HotKeyBean>>> call, BaseResult<List<HotKeyBean>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<List<HotKeyBean>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    /**
     * 获取导航
     * @return
     */
    public LiveData<Resource<List<NaviBean>>> getNavi(){
        final MutableLiveData<Resource<List<NaviBean>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().getNavi().enqueue(new ApiCallback<BaseResult<List<NaviBean>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<NaviBean>>> call, BaseResult<List<NaviBean>> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<List<NaviBean>>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }

    /**
     * 版本检测
     * @return
     */
    public LiveData<Resource<VersionBean>> checkVersion(){
        final MutableLiveData<Resource<VersionBean>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading());
        getApiService().checkVersion().enqueue(new ApiCallback<BaseResult<VersionBean>>() {
            @Override
            public void onResponse(Call<BaseResult<VersionBean>> call, BaseResult<VersionBean> result) {
                liveData.setValue(Resource.response(result));
            }

            @Override
            public void onError(Call<BaseResult<VersionBean>> call, Throwable t) {
                liveData.setValue(Resource.error(t));
            }
        });

        return liveData;
    }


    /**
     * 获取搜索历史
     * @param count
     * @return
     */
    public LiveData<List<SearchHistory>> getSearchHistory(int count){
        return mDataSource.getSearchHistory(count);
    }

    /**
     * 添加搜索历史
     * @param key
     */
    public void addHistory(String key){
        mDataSource.addHistory(key);

    }

    /**
     * 清空历史
     */
    public void deleteAllHistory(){
        mDataSource.deleteAllHistory();
    }

}

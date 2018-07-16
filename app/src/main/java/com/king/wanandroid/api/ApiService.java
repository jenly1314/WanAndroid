package com.king.wanandroid.api;

import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.BannerBean;
import com.king.wanandroid.bean.BaseResult;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.HotKeyBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.NaviBean;
import com.king.wanandroid.bean.Result;
import com.king.wanandroid.bean.TreeBean;
import com.king.wanandroid.bean.User;
import com.king.wanandroid.bean.VersionBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface ApiService {

    /**
     * 获取文章列表
     * @param curPage 页码从0开始
     * @return
     */
    @GET("article/list/{curPage}/json")
    Call<BaseResult<DataBean<List<ArticleBean>>>> getArticlesList(@Path("curPage") int curPage);

    /**
     * 首页Banner
     * @return
     */
    @GET("banner/json")
    Call<BaseResult<List<BannerBean>>> getBanner();

    /**
     * 搜索热词
     * @return
     */
    @GET("hotkey/json")
    Call<BaseResult<List<HotKeyBean>>> getHotKey();

    /**
     * 知识体系
     * @return
     */
    @GET("tree/json")
    Call<BaseResult<List<TreeBean>>> getSystem();

    /**
     * 项目分类 从第1页开始
     * @return
     */
    @GET("project/tree/json")
    Call<BaseResult<List<TreeBean>>> getProject();

    /**
     * 项目列表
     * @param curPage 从第0页开始
     * @param id
     * @return
     */
    @GET("project/list/{curPage}/json")
    Call<BaseResult<DataBean<List<ArticleBean>>>> getProjectList(@Path("curPage")int curPage, @Query("cid")int id);


    /**
     * 获取文章列表
     * @param curPage
     * @param id 文章分类id
     * @return
     */
    @GET("article/list/{curPage}/json")
    Call<BaseResult<DataBean<List<ArticleBean>>>> getArticlesList(@Path("curPage")int curPage, @Query("cid")int id);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Call<BaseResult<User>> login(@Field("username") String username,@Field("password")String password);

    /**
     * 注册
     * @param username
     * @param password
     * @param repassword
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Call<BaseResult<User>> register(@Field("username")String username,@Field("password")String password,@Field("repassword")String repassword);

    /**
     * 收藏站内文章
     * @param id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Call<BaseResult<CollectBean>> collect(@Path("id") int id);

    /**
     * 收藏站外文章
     * @param title
     * @param author
     * @param link
     * @return
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    Call<BaseResult<CollectBean>> collect(@Field("title")String title,@Field("author")String author,@Field("link")String link);

    /**
     * 获取收藏列表
     * @param curPage 从第0页开始
     * @return
     */
    @GET("lg/collect/list/{curPage}/json")
    Call<BaseResult<DataBean<List<CollectBean>>>> getCollectList(@Path("curPage") int curPage);

    /**
     * 取消文章列表中的收藏
     * @param id
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Call<Result> unCollect(@Path("id") int id);

    /**
     * 取消我的收藏中的收藏
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Call<Result> unMyCollect(@Path("id") int id,@Field("originId")int originId);

    /**
     * 搜索热词
     * @return
     */
    @GET("hotkey/json")
    Call<BaseResult<List<HotKeyBean>>> getHotkey();

    /**
     * 搜索
     * @param curPage
     * @param key
     * @return
     */
    @FormUrlEncoded
    @POST("article/query/{curPage}/json")
    Call<BaseResult<DataBean<List<ArticleBean>>>> getArticlesList(@Path("curPage")int curPage, @Field("k")String key);

    /**
     * 导航
     * @return
     */
    @GET("navi/json")
    Call<BaseResult<List<NaviBean>>> getNavi();

    /**
     * 版本检测
     * @return
     */
    @GET("tools/mockapi/3273/WanAndroid")
    Call<BaseResult<VersionBean>> checkVersion();

}

package com.king.wanandroid.app.home;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.king.wanandroid.app.base.DataRepository;
import com.king.wanandroid.app.base.DataViewModel;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.BannerBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.bean.VersionBean;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class HomeViewModel extends DataViewModel {

    @Inject
    public HomeViewModel(@NonNull Application application,DataRepository repository) {
        super( application,repository);
    }

    public LiveData<Resource<DataBean<List<ArticleBean>>>> getArticlesList(int curPage){
        return getRepository().getArticlesList(curPage);
    }

    public LiveData<Resource<List<BannerBean>>> getBanner(){
        return getRepository().getBanner();
    }

    public LiveData<Resource<VersionBean>> checkVersion(){
        return getRepository().checkVersion();
    }
}

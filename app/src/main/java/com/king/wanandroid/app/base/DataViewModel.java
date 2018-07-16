package com.king.wanandroid.app.base;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.Resource;

import javax.inject.Inject;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class DataViewModel extends BaseViewModel<DataRepository> {

    @Inject
    public DataViewModel(@NonNull Application application, DataRepository repository) {
        super(application, repository);
    }

    public LiveData<Resource<CollectBean>> collect(ArticleBean data){
        int id = data.getId();
        //收藏站内文章
        if(id>0){
            return collect(id);
        }
        //收藏站外文章
        return getRepository().collect(data.getTitle(),data.getAuthor(),data.getLink());
    }

    public LiveData<Resource<CollectBean>> collect(int id){
        return getRepository().collect(id);
    }

    public LiveData<Resource> unCollect(int id){
        return getRepository().unCollect(id);
    }
}

package com.king.wanandroid.app.collect;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.king.wanandroid.app.base.BaseViewModel;
import com.king.wanandroid.app.base.DataRepository;
import com.king.wanandroid.app.base.DataViewModel;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class CollectViewModel extends DataViewModel {

    @Inject
    public CollectViewModel(@NonNull Application application, DataRepository repository) {
        super(application, repository);
    }

    public LiveData<Resource<DataBean<List<CollectBean>>>> getCollectList(int curPage){
        return getRepository().getCollectList(curPage);
    }

    public LiveData<Resource> unMyCollect(int id,int originId){
        return getRepository().unMyCollect(id,originId);
    }
}

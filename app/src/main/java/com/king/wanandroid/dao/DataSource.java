package com.king.wanandroid.dao;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.king.wanandroid.bean.SearchHistory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Singleton
public class DataSource {

    private Context mContext;

    @Inject
    public DataSource(@NonNull Application application){
        this.mContext = application;
    }

    /**
     * 获取搜索历史
     * @param count
     * @return
     */
    public LiveData<List<SearchHistory>> getSearchHistory(int count){
        return AppDatabase.getInstance(mContext).searchHistoryDao().getHistory(count);
    }

    /**
     * 添加搜索历史
     * @param key
     */
    public void addHistory(String key){
        Observable.just(key)
                .subscribeOn(Schedulers.io())
                .subscribe(s ->
                        AppDatabase.getInstance(mContext).searchHistoryDao().insert(new SearchHistory(s))
                );

    }

    /**
     * 清空历史
     */
    public void deleteAllHistory(){
        Observable.just(1).subscribeOn(Schedulers.io())
                .subscribe(integer -> {
                    AppDatabase.getInstance(mContext).searchHistoryDao().deleteAll();
                });
    }
}

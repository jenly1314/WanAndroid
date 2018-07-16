package com.king.wanandroid.app.search;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.king.wanandroid.app.base.DataRepository;
import com.king.wanandroid.app.base.DataViewModel;
import com.king.wanandroid.bean.HotKeyBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.bean.SearchHistory;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class SearchViewModel extends DataViewModel {
    @Inject
    public SearchViewModel(@NonNull Application application, DataRepository repository) {
        super(application, repository);
    }

    public LiveData<Resource<List<HotKeyBean>>> getHotkey(){
        return getRepository().getHotkey();
    }

    public LiveData<List<SearchHistory>> getSearchHistory(int count){
        return getRepository().getSearchHistory(count);
    }

    /**
     * 添加历史
     * @param key
     */
    public void addHistory(String key){
        getRepository().addHistory(key);
    }

    /**
     * 删除历史
     */
    public void deleteAllHistory(){
        getRepository().deleteAllHistory();
    }
}

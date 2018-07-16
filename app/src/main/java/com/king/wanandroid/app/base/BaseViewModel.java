package com.king.wanandroid.app.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.king.wanandroid.App;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class BaseViewModel<T extends BaseRepository> extends AndroidViewModel{


    private T repository;

    public BaseViewModel(@NonNull Application application,T repository) {
        super(application);
        this.repository = repository;
    }

    public App getApp(){
        return getApplication();
    }

    public T getRepository(){
        return repository;
    }

}

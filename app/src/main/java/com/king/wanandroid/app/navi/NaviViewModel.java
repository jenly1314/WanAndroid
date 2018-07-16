package com.king.wanandroid.app.navi;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.king.wanandroid.app.base.DataRepository;
import com.king.wanandroid.app.base.DataViewModel;
import com.king.wanandroid.bean.NaviBean;
import com.king.wanandroid.bean.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class NaviViewModel extends DataViewModel {

    @Inject
    public NaviViewModel(@NonNull Application application, DataRepository repository) {
        super(application, repository);
    }

    /**
     * 获取导航
     * @return
     */
    public LiveData<Resource<List<NaviBean>>> getNavi(){
        return getRepository().getNavi();
    }
}

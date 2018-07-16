package com.king.wanandroid.app.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class MVVMActivity<VM extends ViewModel,VDB extends ViewDataBinding> extends BindingActivity<VDB>{

    @Inject
    protected ViewModelProvider.Factory mViewModelFactory;

    protected VM mViewModel;

    @Override
    public void initView() {
        mViewModel = createViewModel();
        super.initView();
    }

    public <T extends ViewModel> T getViewModel(@NonNull Class<T> modelClass){
        return ViewModelProviders.of(this,mViewModelFactory).get(modelClass);
    }

    public <T extends ViewModel> T getViewModel(@NonNull Class<T> modelClass,@Nullable ViewModelProvider.Factory factory){
        return ViewModelProviders.of(this,factory).get(modelClass);
    }

    public ViewModelProvider.Factory getViewModelFactory(){
        return mViewModelFactory;
    }

    protected abstract VM createViewModel();

}

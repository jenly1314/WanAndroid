package com.king.wanandroid.di.component;

import com.king.wanandroid.app.base.BaseActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseActivitySubComponent extends AndroidInjector<BaseActivity>{


    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BaseActivity>{}

}

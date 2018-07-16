package com.king.wanandroid.di.component;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.king.wanandroid.App;
import com.king.wanandroid.api.ApiService;
import com.king.wanandroid.app.base.ViewModelFactory;
import com.king.wanandroid.di.module.ActivityModule;
import com.king.wanandroid.di.module.AppModule;
import com.king.wanandroid.di.module.HttpModule;
import com.king.wanandroid.di.module.ViewModelFactoryModule;
import com.king.wanandroid.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class, ActivityModule.class,ViewModelFactoryModule.class, AndroidInjectionModule.class,AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<App> {


    void inject(App app);

    Application getApplication();

    Context getContext();

    ApiService getApiService();

    PersistentCookieJar getCookieJar();

}

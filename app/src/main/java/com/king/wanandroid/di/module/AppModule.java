package com.king.wanandroid.di.module;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Module(includes = {ViewModelModule.class})
public class AppModule {

    private Application app;

    public AppModule(Application app){
        this.app = app;
    }

    @Singleton
    @Provides
    public Application provideApp(){
        return app;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return app;
    }

    @Singleton
    @Provides
    Map<String, Object> provideExtras() {
        return new ArrayMap<>();
    }
}

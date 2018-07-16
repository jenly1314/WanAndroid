package com.king.wanandroid;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.king.base.util.SharedPreferencesUtils;
import com.king.thread.nevercrash.NeverCrash;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.bean.User;
import com.king.wanandroid.di.component.AppComponent;
import com.king.wanandroid.di.component.DaggerAppComponent;
import com.king.wanandroid.di.module.AppModule;
import com.king.wanandroid.di.module.HttpModule;
import com.king.wanandroid.util.AES;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/**
 * @author ${USER} <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class App extends Application implements HasActivityInjector{

    @Inject
    DispatchingAndroidInjector<Activity> injector;

    private AppComponent appComponent;

    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .httpModule(new HttpModule(Constants.BASE_URL))
                .build();
        appComponent.inject(this);

        LeakCanary.install(this);

        Bugly.init(this, Constants.BUGLY_APP_ID, BuildConfig.DEBUG);

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {

                }
            });
        }

        NeverCrash.init((t, e) -> {

            if(!BuildConfig.DEBUG){
                Timber.w(e);
                CrashReport.postCatchedException(e);
            }
        });



        user = getCacheUser();
        ARouter.init(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }

    public User getUser() {
        return user;
    }

    public boolean isLogin(){
        return user!=null;
    }

    private void cacheUser(User user){
        SharedPreferencesUtils.getSharedPreferences(this,Constants.KEY_USER).edit()
                .putInt(Constants.KEY_USER_ID,user.getId())
                .putString(Constants.KEY_USERNAME,user.getUsername())
                .putString(Constants.KEY_PASSWORD,AES.INSTANCE.encrypt(user.getPassword()))
                .putString(Constants.KEY_EMAIL,user.getEmail())
                .putString(Constants.KEY_ICON,user.getIcon())
                .putInt(Constants.KEY_TYPE,user.getType())
                .commit();
    }


    public User getCacheUser(){
        User user = null;
        SharedPreferences cache = SharedPreferencesUtils.getSharedPreferences(this,Constants.KEY_USER);
        if(cache!=null && cache.contains(Constants.KEY_USERNAME)){
            user = new User();
            user.setId(cache.getInt(Constants.KEY_ID,0));
            user.setUsername(cache.getString(Constants.KEY_USERNAME,null));
            user.setEmail(cache.getString(Constants.KEY_EMAIL,null));
            user.setPassword(AES.INSTANCE.decrypt(cache.getString(Constants.KEY_PASSWORD,null)));
            user.setIcon(cache.getString(Constants.KEY_ICON,null));
            user.setType(cache.getInt(Constants.KEY_TYPE,0));
        }
        return user;
    }

    /**
     * 登录
     * @param user
     */
    public void login(@NonNull User user){
        login(user,true);
    }

    /**
     * 登录
     * @param user
     * @param isCache
     */
    public void login(@NonNull User user,boolean isCache){
        this.user = user;
        if(isCache){
            cacheUser(user);
        }
    }

    /**
     * 退出登录
     */
    public void logout(){
        user = null;
        SharedPreferencesUtils.getSharedPreferences(this,Constants.KEY_USER).edit().clear().commit();
        getAppComponent().getCookieJar().clear();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return injector;
    }
}

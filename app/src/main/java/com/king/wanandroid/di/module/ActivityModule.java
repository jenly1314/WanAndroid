package com.king.wanandroid.di.module;

import com.king.wanandroid.app.about.AboutActivity;
import com.king.wanandroid.app.account.LoginActivity;
import com.king.wanandroid.app.account.RegisterActivity;
import com.king.wanandroid.app.collect.CollectActivity;
import com.king.wanandroid.app.home.HomeActivity;
import com.king.wanandroid.app.navi.NaviActivity;
import com.king.wanandroid.app.search.SearchActivity;
import com.king.wanandroid.app.splash.SplashActivity;
import com.king.wanandroid.app.tree.TreeActivity;
import com.king.wanandroid.app.tree.TreeChildrenActivity;
import com.king.wanandroid.app.web.WebActivity;
import com.king.wanandroid.di.component.BaseActivitySubComponent;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Module(subcomponents = BaseActivitySubComponent.class)
public abstract class   ActivityModule {

//
//    @Binds
//    @IntoMap
//    @ActivityKey(HomeActivity.class)
//    public abstract AndroidInjector<? extends Activity> bindHomeActivityInjectorFactory(BaseActivitySubComponent.Builder builder);


    @ContributesAndroidInjector
    abstract SplashActivity contributesSplashActivity();

    @ContributesAndroidInjector
    abstract HomeActivity contributesHomeActivity();

    @ContributesAndroidInjector
    abstract WebActivity contributesWebActivity();

    @ContributesAndroidInjector
    abstract LoginActivity contributesLoginActivity();

    @ContributesAndroidInjector
    abstract RegisterActivity contributesRegisterActivity();

    @ContributesAndroidInjector
    abstract CollectActivity contributesCollectActivity();

    @ContributesAndroidInjector
    abstract TreeActivity contributesTreeActivity();

    @ContributesAndroidInjector
    abstract TreeChildrenActivity contributesTreeChildrenActivity();

    @ContributesAndroidInjector
    abstract SearchActivity contributesSearchActivity();

    @ContributesAndroidInjector
    abstract NaviActivity contributesNaviActivity();

    @ContributesAndroidInjector
    abstract AboutActivity contributesAboutActivity();
}

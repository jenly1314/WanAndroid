package com.king.wanandroid.di.module;

import android.arch.lifecycle.ViewModel;

import com.king.wanandroid.app.account.LoginViewModel;
import com.king.wanandroid.app.base.DataViewModel;
import com.king.wanandroid.app.collect.CollectViewModel;
import com.king.wanandroid.app.home.HomeViewModel;
import com.king.wanandroid.app.navi.NaviViewModel;
import com.king.wanandroid.app.search.SearchViewModel;
import com.king.wanandroid.app.tree.TreeViewModel;
import com.king.wanandroid.di.scope.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DataViewModel.class)
    abstract ViewModel bindDataViewModel(DataViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CollectViewModel.class)
    abstract ViewModel bindCollectViewModel(CollectViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TreeViewModel.class)
    abstract ViewModel bindTreeViewModel(TreeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NaviViewModel.class)
    abstract ViewModel bindNaviViewModel(NaviViewModel viewModel);


}

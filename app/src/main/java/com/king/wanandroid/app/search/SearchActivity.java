package com.king.wanandroid.app.search;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.base.util.StringUtils;
import com.king.wanandroid.R;
import com.king.wanandroid.app.adapter.HistoryTagAdapter;
import com.king.wanandroid.app.adapter.HotKeyTagAdapter;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.bean.HotKeyBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.bean.SearchHistory;
import com.king.wanandroid.databinding.SearchActivityBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_SEARCH)
public class SearchActivity extends MVVMActivity<SearchViewModel,SearchActivityBinding> {

    private HistoryTagAdapter mHistoryAdapter;

    private List<SearchHistory> listHistory;

    private HotKeyTagAdapter mAdapter;

    private List<HotKeyBean> listData;

    @Override
    protected SearchViewModel createViewModel() {
        return getViewModel(SearchViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.search_activity;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initData() {

        listHistory = new ArrayList<>();
        mHistoryAdapter = new HistoryTagAdapter(getContext(),listHistory);
        mBinding.tflHistory.setAdapter(mHistoryAdapter);

        listData = new ArrayList<>();
        mAdapter = new HotKeyTagAdapter(getContext(),listData);
        mBinding.tflHot.setAdapter(mAdapter);

        mBinding.tflHistory.setOnTagClickListener((view, position, parent) -> {
            search(mHistoryAdapter.getItem(position).getName());
            return true;
        });

        mBinding.tflHot.setOnTagClickListener((view, position, parent) -> {
            search(mAdapter.getItem(position).getName());
            return true;
        });

        mBinding.setViewModel(mViewModel);

        mBinding.srl.setColorSchemeResources(R.color.colorPrimary);
        mBinding.srl.setOnRefreshListener(() ->{
            mViewModel.getSearchHistory(20).observe(this,mHistoryObserver);
            mViewModel.getHotkey().observe(this,mListObserver);
        });
        mBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBinding.etSearch.setSelected(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clickRightClear(mBinding.etSearch);

        mBinding.etSearch.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                search(mBinding.etSearch.getText().toString());
                return true;
            }
            return false;
        });

        mBinding.ivDeleteHistory.setOnClickListener(v -> {
            mViewModel.deleteAllHistory();
            mBinding.ivDeleteHistory.setVisibility(View.INVISIBLE);

        });
        mBinding.srl.setRefreshing(true);
        mViewModel.getHotkey().observe(this,mListObserver);

        mViewModel.getSearchHistory(20).observe(this,mHistoryObserver);

    }

    private void refreshSearchHistory(List<SearchHistory> data){
        listHistory.clear();
        if(data!=null && data.size() > 0){
            listHistory.addAll(data);
            mBinding.ivDeleteHistory.setVisibility(View.VISIBLE);
        }else{
            mBinding.ivDeleteHistory.setVisibility(View.INVISIBLE);
        }
        mHistoryAdapter.notifyDataChanged();
    }

    private void refreshListView(List<HotKeyBean> data){
        if(data!=null){
            listData.clear();
            listData.addAll(data);
            mAdapter.notifyDataChanged();
        }
    }

    private void search(String key){
        if(StringUtils.isNotBlank(key)){
            mViewModel.addHistory(key);
            startTreeChildren(key,key);
        }else{
            showToast(R.string.tips_search_key_is_empty);
        }
    }

    private Observer mListObserver = new Observer<Resource<List<HotKeyBean>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<HotKeyBean>> resource) {
            resource.handle(new OnCallback<List<HotKeyBean>>() {
                @Override
                public void onSuccess(List<HotKeyBean> data) {
                    refreshListView(data);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    mBinding.srl.setRefreshing(false);

                }
            });
        }
    };

    private Observer mHistoryObserver = new Observer<List<SearchHistory>>() {
        @Override
        public void onChanged(@Nullable List<SearchHistory> data) {
            refreshSearchHistory(data);
        }
    };
}

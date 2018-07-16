package com.king.wanandroid.app.navi;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.base.adapter.divider.DividerItemDecoration;
import com.king.wanandroid.R;
import com.king.wanandroid.app.adapter.ArticleAdapter;
import com.king.wanandroid.app.adapter.SimpleBindingAdapter;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.NaviBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.databinding.NaviActivityBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_NAVI)
public class NaviActivity extends MVVMActivity<NaviViewModel,NaviActivityBinding> {

    private SimpleBindingAdapter<NaviBean> mNaviAdapter;

    private List<NaviBean> listData;

    private ArticleAdapter mAdapter;

    private List<ArticleBean> listEmpty = new ArrayList<>();

    private int mNaviPosition;

    private NaviBean mNaviBean;

    private int mCurPosition;

    private ArticleBean mArticleBean;


    @Override
    protected NaviViewModel createViewModel() {
        return getViewModel(NaviViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.navi_activity;
    }

    @Override
    public void initData() {
        setToolbarTitle(R.string.menu_navi);
        setRightImage(R.drawable.btn_navi_selector);
        mBinding.setViewModel(mViewModel);

        mBinding.srl.setColorSchemeResources(R.color.colorPrimary);

        mBinding.srl.setOnRefreshListener(() -> {
            mViewModel.getNavi().observe(this,mListObserver);
        });

        listData = new ArrayList<>();
        mNaviAdapter = new SimpleBindingAdapter(getContext(),listData,R.layout.rv_navi_menu_item);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.rvMenu.setLayoutManager(linearLayoutManager);
        mBinding.rvMenu.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL,R.drawable.line_drawable));

        mBinding.rvMenu.setAdapter(mNaviAdapter);

        mNaviAdapter.setOnItemClickListener((v, position) -> {
            if(mNaviAdapter.getCount() > position){
                mNaviPosition = position;
                mNaviBean = mNaviAdapter.getListData().get(position);
                mBinding.drawerLayout.closeDrawer(Gravity.RIGHT);
                setToolbarTitle(mNaviBean.getName());
                refreshArticleView(mNaviBean.getArticles(),true);
            }
        });


        mAdapter = new ArticleAdapter(getContext(),listEmpty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rv.setLayoutManager(layoutManager);
        mBinding.rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL,R.drawable.line_drawable_8));

        mBinding.rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((v, position) -> {
            if(mAdapter.getCount() > position){
                mCurPosition = position;
                mArticleBean = mAdapter.getListData().get(position);
                if(v.getId() == R.id.ivCollect){
                    clickCollectItem(mArticleBean,position);
                }else {
                    startWeb(mArticleBean);
                }
            }
        });


        mBinding.srl.setRefreshing(true);
        mViewModel.getNavi().observe(this,mListObserver);

    }


    private synchronized void refreshListView(List<NaviBean> data){
        listData.clear();
        if(data!=null && data.size()>0){
            listData.addAll(data);
            // 非第一次进来，并且刷新的数据存在之前选择的导航分类，则遍历到其具体的位置，并替换，反之默认取第0个
            if(mNaviBean!=null && data.contains(mNaviBean)){

                int size = data.size();
                for(int i =0;i< size;i++){
                    NaviBean bean = data.get(i);
                    if(mNaviBean.equals(bean)){
                        mNaviPosition = i;
                        mNaviBean  = bean;
                        break;
                    }
                }

            }else{
                mNaviPosition = 0;
                mNaviBean = listData.get(mNaviPosition);
            }
            setToolbarTitle(mNaviBean.getName());
            refreshArticleView(mNaviBean.getArticles(),false);
        }else{
            setToolbarTitle(R.string.menu_navi);
            refreshArticleView(listEmpty,false);
        }

    }

    private void refreshArticleView(List<ArticleBean> data,boolean isNotify){
        mAdapter.setListData(data);
        if(isNotify){
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 点击列表item的收藏
     * @param data
     */
    private void clickCollectItem(ArticleBean data,int position){
        if(checkLogin()){
            if(data.isCollect()){
                unCollect(data,position);
            }else {
                collect(data,position);
            }
        }

    }

    /**
     * 收藏
     * @param data
     */
    private void collect(ArticleBean data,int position){
        mViewModel.collect(data).observe(this,resource -> {
            resource.handle(new OnCallback<CollectBean>() {
                @Override
                public void onSuccess(CollectBean bean) {
                    data.setCollect(true);
                    showToast(R.string.success_collect);
                    mAdapter.notifyItemChanged(position);
                }
            });
        });
    }

    /**
     *  取消收藏
     * @param data
     */
    private void unCollect(ArticleBean data,int position){
        mViewModel.unCollect(data.getId()).observe(this,resource -> {
            resource.handle(new OnCallback() {
                @Override
                public void onSuccess(Object obj) {
                    data.setCollect(false);
                    showToast(R.string.success_un_collect);
                    mAdapter.notifyItemChanged(position);
                }
            });

        });
    }


    private Observer mListObserver = new Observer<Resource<List<NaviBean>>>(){

        @Override
        public void onChanged(@Nullable Resource<List<NaviBean>> resource) {
            resource.handle(new OnCallback<List<NaviBean>>() {
                @Override
                public void onSuccess(List<NaviBean> data) {
                    refreshListView(data);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    mNaviAdapter.notifyDataSetChanged();
                    mAdapter.setHideEmpty(false);
                    mAdapter.notifyDataSetChanged();
                    mBinding.srl.setRefreshing(false);
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case Constants.RECODE_ARTICLE:
                    //同步收藏状态
                    if(mArticleBean!=null){
                        boolean isCollect = data.getBooleanExtra(Constants.KEY_IS_COLLECT,mArticleBean.isCollect());
                        if(isCollect != mArticleBean.isCollect()){
                            mArticleBean.setCollect(isCollect);
                            mAdapter.notifyItemChanged(mCurPosition);
                        }
                    }
                    break;
            }
        }
    }

    private void clickRight(){
        if(mBinding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            mBinding.drawerLayout.closeDrawer(Gravity.RIGHT);
        }else{
            mBinding.drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void OnClick(View v) {
        super.OnClick(v);
        switch (v.getId()){
            case R.id.ivRight:
                clickRight();
                break;
        }
    }
}

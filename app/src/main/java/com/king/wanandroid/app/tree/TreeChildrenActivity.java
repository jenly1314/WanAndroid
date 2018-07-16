package com.king.wanandroid.app.tree;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.base.adapter.divider.DividerItemDecoration;
import com.king.view.superswiperefreshlayout.SuperSwipeRefreshLayout;
import com.king.wanandroid.R;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.app.adapter.ArticleAdapter;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.app.comm.Status;
import com.king.wanandroid.app.comm.Tree;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.databinding.TreeChildrenActivityBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_TREE_CHILDREN)
public class TreeChildrenActivity extends MVVMActivity<TreeViewModel,TreeChildrenActivityBinding> {

    private ArticleAdapter mAdapter;

    private List<ArticleBean> listData;

    private int mCurPage = 1;

    /**
     * 分类Id
     */
    private int mCId;

    private ArticleBean mArticleBean;
    private int mCurPosition;

    private String mKey;
    private @Tree int mTree;


    @Override
    protected TreeViewModel createViewModel() {
        return getViewModel(TreeViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tree_children_activity;
    }

    @Override
    public void initData() {

        Intent intent = getIntent();
        mTree = intent.getIntExtra(Constants.KEY_TREE,Tree.SYSTEM);
        mCId = intent.getIntExtra(Constants.KEY_ID,-1);
        mKey = intent.getStringExtra(Constants.KEY_KEY);
        String title = intent.getStringExtra(Constants.KEY_TITLE);
        setToolbarTitle(title);

        mBinding.setViewModel(mViewModel);

        listData = new ArrayList<>();
        mAdapter = new ArticleAdapter(getContext(),listData);

        mBinding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL,R.drawable.line_drawable_8));
        mBinding.rv.setNestedScrollingEnabled(false);
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

        mBinding.ssrl.setColorSchemeResources(R.color.colorPrimary);

        mBinding.ssrl.setOnRefreshListener(direction -> {
            if(direction == SuperSwipeRefreshLayout.Direction.TOP){
                mCurPage = 1;
            }
            getTreeList();
        });
        mBinding.ssrl.setRefreshing(true);
        getTreeList();
    }


    private void getTreeList(){
        mViewModel.getTreeList(mTree,mCurPage,mCId,mKey).observe(this,mListObserver);
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

    private void refreshListView(DataBean<List<ArticleBean>> data){
        if(data.getCurPage() == 1){
            listData.clear();
        }
        mCurPage = data.getCurPage() + 1;
        listData.addAll(data.getDatas());

        if(mAdapter.getItemCount()<data.getTotal()){
            mBinding.ssrl.setDirection(SuperSwipeRefreshLayout.Direction.BOTH);
        }else{
            mBinding.ssrl.setDirection(SuperSwipeRefreshLayout.Direction.TOP);
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
            data.setCollect(false);
            showToast(R.string.success_un_collect);
            mAdapter.notifyItemChanged(position);
        });
    }

    private Observer mListObserver =  new Observer<Resource<DataBean<List<ArticleBean>>>>() {
        @Override
        public void onChanged(@Nullable Resource<DataBean<List<ArticleBean>>> resource) {
            resource.handle(new OnCallback<DataBean<List<ArticleBean>>>() {
                @Override
                public void onSuccess(DataBean<List<ArticleBean>> data) {
                    refreshListView(data);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    mAdapter.setRandomTips(mTree != Tree.SEARCH);
                    mAdapter.setHideEmpty(false);
                    mAdapter.notifyDataSetChanged();
                    mBinding.ssrl.setRefreshing(false);

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


}

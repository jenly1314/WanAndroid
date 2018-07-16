package com.king.wanandroid.app.tree;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.base.adapter.divider.DividerItemDecoration;
import com.king.wanandroid.R;
import com.king.wanandroid.app.adapter.TreeAdapter;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.app.comm.Tree;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.bean.TreeBean;
import com.king.wanandroid.databinding.TreeActivityBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_TREE)
public class TreeActivity extends MVVMActivity<TreeViewModel,TreeActivityBinding> {

    private List<TreeBean> listData;

    private TreeAdapter mAdapter;

    private @Tree int mTree;

    @Override
    protected TreeViewModel createViewModel() {
        return getViewModel(TreeViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tree_activity;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initData() {

        Intent intent = getIntent();
        mTree = intent.getIntExtra(Constants.KEY_TREE,Tree.SYSTEM);

        setToolbarTitle(intent.getStringExtra(Constants.KEY_TITLE));

        setRightImage(R.drawable.btn_search_selector);
        mBinding.setViewModel(mViewModel);

        listData = new ArrayList<>();
        mAdapter = new TreeAdapter(getContext(),listData,mTree);
        mAdapter.setOnItemClickListener((v, position) -> {
            if(mAdapter.getCount()> position){
                TreeBean bean = listData.get(position);
                startTreeChildren(mTree,bean.getName(),bean.getId());
            }
        });
        mAdapter.setOnTagClickListener((view, data, position) -> {
            TreeBean.ChildrenBean bean = data.getChildren().get(position);
            startTreeChildren(mTree,bean.getName(),bean.getId());
        });

        mBinding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL,R.drawable.line_drawable_8));
        mBinding.rv.setNestedScrollingEnabled(false);
        mBinding.rv.setAdapter(mAdapter);

        mBinding.srl.setColorSchemeResources(R.color.colorPrimary);
        mBinding.srl.setOnRefreshListener(() ->
            getTree()
        );
        mBinding.srl.setRefreshing(true);
        getTree();
    }


    private void getTree(){
        mViewModel.getTree(mTree).observe(this,mListObserver);
    }

    private void refreshListView(List<TreeBean> data){
        listData.clear();
        listData.addAll(data);
    }

    private Observer mListObserver = new Observer<Resource<List<TreeBean>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<TreeBean>> resource) {
            resource.handle(new OnCallback<List<TreeBean>>() {
                @Override
                public void onSuccess(List<TreeBean> data) {
                    refreshListView(data);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    mAdapter.setHideEmpty(false);
                    mAdapter.notifyDataSetChanged();
                    mBinding.srl.setRefreshing(false);

                }
            });
        }
    };

    private void clickSearch(){
        ARouter.getInstance()
                .build(Constants.ROUTE_SEARCH)
                .navigation(getContext());
    }

    @Override
    public void OnClick(View v) {
        super.OnClick(v);
        switch (v.getId()){
            case R.id.ivRight:
                clickSearch();
                break;
        }
    }
}

package com.king.wanandroid.app.collect;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.base.adapter.divider.DividerItemDecoration;
import com.king.view.superswiperefreshlayout.SuperSwipeRefreshLayout;
import com.king.wanandroid.R;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.app.adapter.CollectAdapter;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.databinding.CollectActivityBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_COLLECT)
public class CollectActivity extends MVVMActivity<CollectViewModel,CollectActivityBinding> {

    private CollectAdapter mAdapter;

    private List<CollectBean> listData;

    private int mCurPage = 1;

    @Override
    protected CollectViewModel createViewModel() {
        return getViewModel(CollectViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.collect_activity;
    }

    @Override
    public void initData() {
        setToolbarTitle(R.string.menu_collect);

        mBinding.setViewModel(mViewModel);

        listData = new ArrayList<>();
        mAdapter = new CollectAdapter(getContext(),listData);

        mBinding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL,R.drawable.line_drawable_8));
        mBinding.rv.setNestedScrollingEnabled(false);
        mBinding.rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((v, position) -> {
            if(mAdapter.getCount() > position){
                CollectBean bean = mAdapter.getListData().get(position);
                if(v.getId() == R.id.ivCollect){
                    unMyCollect(bean,position);
                }else {
                    ARouter.getInstance()
                            .build(Constants.ROUTE_WEB)
                            .withString(Constants.KEY_URL,bean.getLink())
                            .withString(Constants.KEY_TITLE,bean.getDesc())
                            .withString(Constants.KEY_AUTHOR,bean.getAuthor())
                            .navigation(getContext());
                }
            }
        });

        mBinding.ssrl.setColorSchemeResources(R.color.colorPrimary);

        mBinding.ssrl.setOnRefreshListener(direction -> {
            if(direction == SuperSwipeRefreshLayout.Direction.TOP){
                mCurPage = 1;
            }
            getCollectList();
        });
        mBinding.ssrl.setRefreshing(true);
        getCollectList();

    }

    /**
     * 获取我的收藏列表
     */
    private void getCollectList(){
        mViewModel.getCollectList(mCurPage).observe(this,mListObserver);
    }

    private void refreshListView(DataBean<List<CollectBean>> data){
        if(data.getCurPage() == 1){
            listData.clear();
        }
        mCurPage = data.getCurPage() + 1;
        listData.addAll(data.getDatas());
    }

    /**
     *  取消收藏
     * @param data
     */
    private void unMyCollect(CollectBean data,int position){
        mViewModel.unMyCollect(data.getId(),data.getOriginId()).observe(this,resource -> {
            resource.handle(new OnCallback() {
                @Override
                public void onSuccess(Object data) {
                    listData.remove(position);
                    mAdapter.notifyDataSetChanged();
                    showToast(R.string.success_un_collect);
                }
            });

        });
    }

    private Observer mListObserver = new Observer<Resource<DataBean<List<CollectBean>>>>() {
        @Override
        public void onChanged(@Nullable Resource<DataBean<List<CollectBean>>> resource) {
            resource.handle(new OnCallback<DataBean<List<CollectBean>>>() {
                @Override
                public void onSuccess(DataBean<List<CollectBean>> data) {
                    refreshListView(data);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    mAdapter.setRandomTips(false);
                    mAdapter.setHideEmpty(false);
                    mAdapter.notifyDataSetChanged();
                    mBinding.ssrl.setRefreshing(false);

                }
            });
        }
    };
}

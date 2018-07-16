package com.king.wanandroid.app.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.util.PermissionUtils;
import com.king.base.adapter.divider.DividerItemDecoration;
import com.king.base.util.SharedPreferencesUtils;
import com.king.base.util.StringUtils;
import com.king.view.flutteringlayout.FlutteringLayout;
import com.king.view.superslidingpanelayout.SuperSlidingPaneLayout;
import com.king.view.superswiperefreshlayout.SuperSwipeRefreshLayout;
import com.king.wanandroid.BuildConfig;
import com.king.wanandroid.R;
import com.king.wanandroid.app.adapter.ArticleAdapter;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.app.comm.Tree;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.BannerBean;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.bean.User;
import com.king.wanandroid.bean.VersionBean;
import com.king.wanandroid.databinding.HomeActivityBinding;
import com.king.wanandroid.glide.GlideApp;
import com.king.wanandroid.util.RandomUtils;
import com.king.wanandroid.util.TimeUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_HOME)
public class HomeActivity extends MVVMActivity<HomeViewModel,HomeActivityBinding> {

    private ArticleAdapter mAdapter;

    private List<ArticleBean> listData;

    private List<BannerBean> listBanner;

    private int mCurPage = 1;

    private TextView tvUser;

    private TextView tvLogout;

    private TextView tvVersion;

    private FlutteringLayout flutteringLayout;

    private ArticleBean mArticleBean;
    private int mCurPosition;
    /**
     * 是否有新版本
     */
    private boolean isNewVersion;
    /**
     * 版本检测时间
     */
    private long mCheckTime;

    private long mLastTime;

    /**
     * 彩蛋：侧滑触发飘心次数
     */
    private int mCount;

    private boolean isStartAnim;

    @Override
    protected HomeViewModel createViewModel() {
        return getViewModel(HomeViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity;
    }

    @Override
    public void initData() {

        mCheckTime = SharedPreferencesUtils.getLong(getContext(),Constants.KEY_CHECK_TIME);
        mBinding.sspl.setSliderFadeColor(ContextCompat.getColor(getContext(),R.color.black_transparent));
        mBinding.sspl.setCoveredFadeColor(ContextCompat.getColor(getContext(),R.color.transparent));
        View menu = findViewById(R.id.menu);
        ViewGroup.LayoutParams params = menu.getLayoutParams();
        params.width = (int)(getResources().getDisplayMetrics().widthPixels * 0.85f);
        menu.setLayoutParams(params);
        mBinding.sspl.setPanelSlideListener(new SuperSlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelOpened(View panel) {
                //添加彩蛋：限制自动飘心的最大次数，超过了则不再自动触发
                if(mCount<Constants.MAX_COUNT){
                    addHeart(RandomUtils.INSTANCE.random(1,5));
                }

            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });
        tvUser = mHolder.getView(R.id.tvUser);
        tvLogout = mHolder.getView(R.id.tvLogout);
        tvVersion = mHolder.getView(R.id.tvVersion);
        tvVersion.setText(String.format("v%s", BuildConfig.VERSION_NAME));
        flutteringLayout = mHolder.getView(R.id.flutteringLayout);

        mBinding.setViewModel(mViewModel);

        listData = new ArrayList<>();
        mAdapter = new ArticleAdapter(getContext(),listData);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mBinding.rv.setLayoutManager(linearLayoutManager);
        mBinding.rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL,R.drawable.line_drawable_8));

        mBinding.rv.setHasFixedSize(true);
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

                if(listBanner == null || listBanner.size() ==0){
                    getBanner();
                }
            }
            getArticlesList();


        });

        mBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBinding.banner.setImageLoader(new GlideImageLoader());
        mBinding.banner.setOnBannerListener(position -> {
            if(listBanner!=null){
                BannerBean bean = listBanner.get(position);
                startWeb(bean.getTitle(),bean.getUrl());
            }
        });

        mBinding.ssrl.setRefreshing(true);
        getBanner();

        getArticlesList();

        clickVersion(false);

        PermissionUtils.INSTANCE.verifyReadAndWritePermissions(this, com.king.app.updater.constant.Constants.RE_CODE_STORAGE_PERMISSION);
    }

    /**
     * 获取首页文章列表
     */
    private void getArticlesList(){
        mViewModel.getArticlesList(mCurPage).observe(this,mListObserver);
    }

    private void getBanner(){
        mViewModel.getBanner().observe(this, resource ->
                resource.handle(new OnCallback<List<BannerBean>>() {
                    @Override
                    public void onSuccess(List<BannerBean> data) {
                        updateBanner(data);
                    }

                })

        );
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

    /**
     * 旋转动画
     * @param view
     * @param values
     */
    private void startRotation(View view,float... values){
        if(!isStartAnim){
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation",values);
            objectAnimator.setDuration(1500);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isStartAnim = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isStartAnim = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }

    }



    private Observer mListObserver = new Observer<Resource<DataBean<List<ArticleBean>>>>() {
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

    @Override
    protected void onResume() {
        super.onResume();
        updateUser();

    }

    private void updateUser(){
        if(checkLogin(false)){
            User user = getApp().getUser();
            tvUser.setText(user.getUsername());
            tvLogout.setVisibility(View.VISIBLE);
        }else{
            tvUser.setText(R.string.click_login);
            tvLogout.setVisibility(View.GONE);
        }
    }

    /**
     * 更新banner
     * @param data
     */
    private void updateBanner(List<BannerBean> data){
        if(data!=null && data.size()>0){
            listBanner = data;
            List<String> urls = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            int size = data.size();
            for (int i = 0; i < size; i++) {
                BannerBean bean = data.get(i);
                titles.add(bean.getDesc());
                urls.add(bean.getImagePath());
            }
            mBinding.banner.setVisibility(View.VISIBLE);
            mBinding.banner.setBannerTitles(titles);
            mBinding.banner.setImages(urls);
            mBinding.banner.start();

        }else{
            mBinding.banner.setVisibility(View.GONE);
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

    private void addHeart(int count){
        mCount += count;
        for(int i=0;i<count;i++){
            flutteringLayout.addHeart();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mBinding.banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.banner.stopAutoPlay();
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if(mLastTime + 2000 < curTime ){
            showToast(R.string.tips_double_click_exit);
            mLastTime = curTime;
            return;
        }
        super.onBackPressed();
    }

    public class GlideImageLoader extends ImageLoader{

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            GlideApp.with(context).load(path).error(R.mipmap.ic_launcher).into(imageView);
        }

    }


    private void clickUser(View view, boolean isLogo){
        if(checkLogin()){
            Timber.d("已登录");
            if(isLogo){
                startRotation(view,0,360);
            }
        }
    }


    private void clickMyCollect(){
        if(checkLogin()){
            ARouter.getInstance()
                    .build(Constants.ROUTE_COLLECT)
                    .navigation(getContext());
        }
    }

    private void clickSearch(){
        ARouter.getInstance()
                .build(Constants.ROUTE_SEARCH)
                .navigation(getContext());
    }

    private void clickNavi(){
        ARouter.getInstance()
                .build(Constants.ROUTE_NAVI)
                .navigation(getContext());
    }

    private void clickAbout(){
        ARouter.getInstance()
                .build(Constants.ROUTE_ABOUT)
                .navigation(getContext());
    }

    private void clickLogout(){
        AppDialogConfig config = new AppDialogConfig();
        config.setTitle(getString(R.string.menu_logout))
                .setContent(getString(R.string.tips_logout_content))
                .setOnClickOk(v -> {
                    AppDialog.INSTANCE.dismissDialog();
                    getApp().logout();
                    updateUser();
                });
        AppDialog.INSTANCE.showDialog(getContext(),config);
    }

    private void handleVersion(final VersionBean data,boolean isClick){
        if(BuildConfig.VERSION_CODE<data.getVersionCode()){
            //有新版本
            isNewVersion = true;
            if(!isClick){
                long curTime = System.currentTimeMillis();
                //降低新版本提示频率
                if(mCheckTime + 3 * TimeUtils.ONE_HOUR > curTime){
                    return;
                }
                mCheckTime = curTime;
                SharedPreferencesUtils.put(getContext(),Constants.KEY_CHECK_TIME,mCheckTime);
            }

            String content = data.getVersionDesc();
            if(StringUtils.isBlank(content)){
                content = getString(R.string.tips_default_update_desc);
            }
            //新版本提示
            AppDialogConfig config = new AppDialogConfig();
            config.setTitle(getString(R.string.update_version_title))
                    .setContent(content)
                    .setOk(getString(R.string.update_version_ok))
                    .setOnClickOk(v -> {
                        new AppUpdater(getContext(),data.getUrl()).start();
                        AppDialog.INSTANCE.dismissDialog();
                    });
            AppDialog.INSTANCE.showDialog(getContext(),config);

        }
    }

    /**
     * 版本检测
     * @param isClick
     */
    private void clickVersion(boolean isClick){
        //主动点击触发并且启动没有检测到新版本，则触发飘心动画
        if(isClick && !isNewVersion){
            flutteringLayout.addHeart();
            return;
        }
        mViewModel.checkVersion().observe(this, resource -> resource.handle(new Resource.OnHandleCallback<VersionBean>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(VersionBean data) {
                if(data!=null){
                    handleVersion(data,isClick);
                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCompleted() {

            }
        }));
    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.ivLeft:
                mBinding.sspl.openPane();
                break;
            case R.id.ivRight:
                clickSearch();
                break;
            case R.id.ivLogo:
                clickUser(v,true);
                break;
            case R.id.tvUser:
                clickUser(v,false);
                break;
            case R.id.tvCollect:
                clickMyCollect();
                break;
            case R.id.tvSystem:
                startTree(Tree.SYSTEM,getString(R.string.menu_system));
                break;
            case R.id.tvProject:
                startTree(Tree.PROJECT,getString(R.string.menu_project));
                break;
            case R.id.tvNavi:
                clickNavi();
                break;
            case R.id.tvAbout:
                clickAbout();
                break;
            case R.id.tvLogout:
                clickLogout();
                break;
            case R.id.tvVersion:
                clickVersion(true);
                break;

        }
    }


}

package com.king.wanandroid.app.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.king.base.util.StringUtils;
import com.king.base.util.SystemUtils;
import com.king.base.util.ToastUtils;
import com.king.wanandroid.App;
import com.king.wanandroid.R;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.app.comm.Tree;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.Resource;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    public Context getContext(){
        return this;
    }

    public App getApp(){
        return (App)getApplication();
    }


    public void showToast(CharSequence text){
        if(StringUtils.isNotBlank(text)) {
            ToastUtils.showToast(getContext(), text);
        }
    }

    public void showToast(@StringRes int resId){
        ToastUtils.showToast(getContext(),resId);
    }

    public boolean checkInput(TextView tv){
        return !TextUtils.isEmpty(tv.getText());
    }

    public boolean checkInput(TextView tv,@StringRes int msg){
        return checkInput(tv,msg,true);
    }

    public boolean checkInput(TextView tv,@StringRes int msg,boolean isShake){
        if(TextUtils.isEmpty(tv.getText())){
            showToast(msg);
            if(isShake){
                startShake(tv);
            }
            return false;
        }
        return true;
    }

    public boolean checkInput(TextView tv,CharSequence msg){
        return checkInput(tv,msg,true);
    }

    public boolean checkInput(TextView tv,CharSequence msg,boolean isShake){
        if(TextUtils.isEmpty(tv.getText())){
            if(StringUtils.isNotBlank(msg)){
                showToast(msg);
            }
            if(isShake){
                startShake(tv);
            }
            return false;
        }
        return true;
    }

    public void startShake(View view){
        view.requestFocus();
        view.startAnimation( AnimationUtils.loadAnimation(getContext(),R.anim.shake));
    }


    public void clickRightClear(final TextView tv){
        tv.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    return clickRightClear(tv,event);
            }
            return false;
        });
    }

    private boolean clickRightClear(TextView tv, MotionEvent event){
        Drawable drawableRight = tv.getCompoundDrawables()[2] ;
        if (drawableRight != null && event.getRawX() >= (tv.getRight() - drawableRight.getBounds().width())) {
            tv.setText(null);
            return true;
        }
        return false;
    }

    public void startTree(@Tree int tree,String title){
        ARouter.getInstance()
                .build(Constants.ROUTE_TREE)
                .withInt(Constants.KEY_TREE,tree)
                .withString(Constants.KEY_TITLE,title)
                .navigation(getContext());
    }

    public void startTreeChildren(String title,String key){
        startTreeChildren(Tree.SEARCH,title,-1,key);
    }

    public void startTreeChildren(@Tree int tree,String title,int id){
        startTreeChildren(tree,title,id,null);
    }

    private void startTreeChildren(@Tree int tree,String title,int id,String key){
        ARouter.getInstance()
                .build(Constants.ROUTE_TREE_CHILDREN)
                .withInt(Constants.KEY_TREE,tree)
                .withString(Constants.KEY_TITLE,title)
                .withInt(Constants.KEY_ID,id)
                .withString(Constants.KEY_KEY,key)
                .navigation(getContext());
    }

    public void startWeb(String title,String url){
        startWeb(title,null,url,-1,false);
    }

    public void startWeb(ArticleBean bean){
        startWeb(bean.getTitle(),bean.getAuthor(),bean.getLink(),bean.getId(),bean.isCollect());
    }

    private void startWeb(String title,String author,String url,int id,boolean isCollect){
        ARouter.getInstance()
                .build(Constants.ROUTE_WEB)
                .withString(Constants.KEY_URL,url)
                .withString(Constants.KEY_TITLE,title)
                .withString(Constants.KEY_AUTHOR,author)
                .withInt(Constants.KEY_ID,id)
                .withBoolean(Constants.KEY_IS_COLLECT,isCollect)
                .navigation(this,Constants.RECODE_ARTICLE);
    }

    public boolean checkLogin(){
        return checkLogin(true);
    }

    /**
     * 检测登录
     * @param isJump 是否跳转到登录界面
     * @return
     */
    public boolean checkLogin(boolean isJump){
        if(!getApp().isLogin()){
            if(isJump){
                ARouter.getInstance()
                        .build(Constants.ROUTE_LOGIN)
                        .navigation(getContext());
            }
            return false;
        }

        return true;
    }

    public void OnClick(View v){
        if(v.getId() == R.id.ivLeft){
            onBackPressed();
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public abstract void initView();

    public abstract void initData();


    public abstract class OnCallback<T> implements Resource.OnHandleCallback<T>{

        @Override
        public void onLoading() {

        }

        @Override
        public void onError(Throwable e) {
            if(!SystemUtils.isNetWorkActive(getContext())){
                showToast( R.string.result_network_unavailable_error);
                return;
            }
            if(e instanceof ConnectException){
                showToast( R.string.result_connect_failed_error);
            }else if(e instanceof SocketTimeoutException){
                showToast( R.string.result_connect_timeout_error);
            }else{
                showToast( R.string.result_empty_error);
            }
        }

        @Override
        public void onFailure(String msg) {
            showToast(msg);
        }

        @Override
        public void onCompleted() {

        }
    }


}

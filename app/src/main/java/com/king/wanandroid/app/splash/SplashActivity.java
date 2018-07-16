package com.king.wanandroid.app.splash;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.wanandroid.R;
import com.king.wanandroid.app.base.BindingActivity;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.databinding.SplashActivityBinding;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_SPLASH)
public class SplashActivity extends BindingActivity<SplashActivityBinding>{
    @Override
    protected int getLayoutId() {
        return R.layout.splash_activity;
    }

    @Override
    public void initData() {
        startAnimation(mBinding.getRoot());
    }

    protected void startAnimation(View view){
        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.splash);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBinding.tvName.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(getContext(),R.anim.in,R.anim.out);
                ARouter.getInstance()
                        .build(Constants.ROUTE_HOME)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .withOptionsCompat(options)
                        .navigation(getContext());
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }
}

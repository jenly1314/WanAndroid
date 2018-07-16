package com.king.wanandroid.app.account;


import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.wanandroid.R;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.bean.User;
import com.king.wanandroid.databinding.LoginActivityBinding;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_LOGIN)
public class LoginActivity extends MVVMActivity<LoginViewModel,LoginActivityBinding> {
    @Override
    protected LoginViewModel createViewModel() {
        return getViewModel(LoginViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public void initData() {

        setToolbarTitle(R.string.login);

        mBinding.setViewModel(mViewModel);

        mBinding.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBinding.etUsername.setSelected(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clickRightClear(mBinding.etUsername);

        mBinding.btnLogin.setOnClickListener(v ->
            clickLogin()
        );
        mBinding.tvRegister.setOnClickListener(v ->{
            String username = mBinding.etUsername.getText().toString();
            ARouter.getInstance()
                    .build(Constants.ROUTE_REGISTER)
                    .withString(Constants.KEY_USERNAME,username)
                    .navigation();
        });

    }

    private void clickLogin(){
        if(checkInput(mBinding.etUsername,R.string.tips_username_is_empty) && checkInput(mBinding.etPassword,R.string.tips_password_is_empty)){
            String username = mBinding.etUsername.getText().toString();
            String password = mBinding.etPassword.getText().toString();
            mViewModel.login(username,password).observe(this, resource -> {
                resource.handle(new OnCallback<User>() {
                    @Override
                    public void onSuccess(User data) {
                        showToast(R.string.success_login);
                        getApp().login(data);
                        ARouter.getInstance()
                                .build(Constants.ROUTE_HOME)
                                .withBoolean(Constants.KEY_IS_LOGIN,true)
                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .navigation(getContext());
                    }
                });
            });
        }
    }

}

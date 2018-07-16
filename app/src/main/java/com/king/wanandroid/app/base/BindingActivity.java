package com.king.wanandroid.app.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import com.king.base.adapter.holder.ViewHolder;
import com.king.wanandroid.R;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class BindingActivity<VDB extends ViewDataBinding> extends BaseActivity {

    protected VDB mBinding;

    protected ViewHolder mHolder;

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this,getLayoutId());
        mHolder = new ViewHolder(mBinding.getRoot());
    }

    public void setToolbarTitle(@StringRes int title){
        mHolder.setText(R.id.tvTitle,title);
    }

    public void setToolbarTitle(CharSequence title){
        mHolder.setText(R.id.tvTitle,title);
    }

    public void setLeftImage(@DrawableRes int drawable){
        mHolder.setImageResource(R.id.ivLeft,drawable);
    }

    public void setRightImage(@DrawableRes int drawable){
        mHolder.setImageResource(R.id.ivRight,drawable);
    }

    protected abstract @LayoutRes int getLayoutId();
}

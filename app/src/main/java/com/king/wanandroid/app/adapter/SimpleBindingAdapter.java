package com.king.wanandroid.app.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;

import com.king.wanandroid.BR;

import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class SimpleBindingAdapter<T> extends EmptyAdapter<T> {

    private int mLayoutId;

    public SimpleBindingAdapter(Context context, List<T> listData, @LayoutRes int layoutId) {
        super(context, listData);
        this.mLayoutId = layoutId;
    }

    @Override
    public int getItemViewId() {
        return mLayoutId;
    }

    @Override
    public void bindViewData(BindingHolder holder, T data, int position) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.data,data);
        binding.executePendingBindings();
    }
}

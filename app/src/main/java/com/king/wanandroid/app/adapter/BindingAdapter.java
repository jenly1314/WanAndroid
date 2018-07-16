package com.king.wanandroid.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.base.adapter.HolderRecyclerAdapter;
import com.king.wanandroid.R;

import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class BindingAdapter<T> extends HolderRecyclerAdapter<T,BindingHolder> {

    protected final int DEFAULT_VARIABLE_ID = 0x01;

    public BindingAdapter(Context context, List<T> listData) {
        super(context, listData);
    }

    @Override
    public View buildConvertView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, getConvertViewId(), parent, false);
        if(binding == null){
            return inflate(getConvertViewId(),parent,false);
        }

        View itemView = binding.getRoot();
        itemView.setTag(R.id.dataBinding,binding);
        return  itemView;
    }


    @Override
    public BindingHolder buildHolder(View convertView, int viewType) {
        return new BindingHolder(convertView);
    }

    public abstract int getConvertViewId();
}

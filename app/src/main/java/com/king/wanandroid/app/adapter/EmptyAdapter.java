package com.king.wanandroid.app.adapter;

import android.content.Context;

import com.king.wanandroid.R;
import com.king.wanandroid.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class EmptyAdapter<T> extends BindingAdapter<T> {

    private boolean isHideEmpty = true;

    protected static final int EMPTY_VIEW = 1;
    private int viewType;

    private String[] tips;

    /**
     * 是否随机提示
     */
    private boolean isRandomTips = true;

    public EmptyAdapter(Context context, List<T> listData) {
        super(context, listData);
        tips = context.getResources().getStringArray(R.array.tips_empty_random);
    }

    public boolean isHideEmpty(){
        return isHideEmpty;
    }

    public void setHideEmpty(boolean isHideEmpty){
        this.isHideEmpty = isHideEmpty;
    }

    public boolean isRandomTips(){
        return isRandomTips;
    }

    public void setRandomTips(boolean isRandomTips){
        this.isRandomTips = isRandomTips;
    }

    @Override
    public int getItemCount() {
        return (super.getItemCount()== 0 && !isHideEmpty) ? 1 : super.getItemCount();
    }

    public int getCount(){
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(!isHideEmpty){
            if(getListData()==null || getListData().size() ==0){
                viewType = EMPTY_VIEW;
                return EMPTY_VIEW;
            }
        }
        viewType = 0;
        return super.getItemViewType( position );
    }

    @Override
    public void bindViewDatas(BindingHolder holder, T t, int position) {
        if(viewType == EMPTY_VIEW ){
            if(isRandomTips){
                holder.setText(R.id.tvEmpty,tips[RandomUtils.INSTANCE.random(0,tips.length-1)]);
            }
            return;
        }
        bindViewData(holder,t,position);
    }

    @Override
    public int getConvertViewId() {
        return viewType != EMPTY_VIEW ? getItemViewId() : R.layout.empty_layout;
    }

    public abstract int getItemViewId();
    public abstract void bindViewData(BindingHolder holder, T t, int position);
}

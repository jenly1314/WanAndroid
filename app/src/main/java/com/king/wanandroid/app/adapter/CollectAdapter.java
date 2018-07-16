package com.king.wanandroid.app.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.king.wanandroid.BR;
import com.king.wanandroid.R;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.util.TimeUtils;

import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class CollectAdapter extends EmptyAdapter<CollectBean> {
    public CollectAdapter(Context context, List<CollectBean> listData) {
        super(context, listData);
    }

    @Override
    public int getItemViewId() {
        return R.layout.rv_collect_item;
    }

    @Override
    public void bindViewData(BindingHolder holder, CollectBean data, int position) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.data,data);
        holder.setText(R.id.tvTime, TimeUtils.getUpdatedRelativeTime(getContext(),data.getPublishTime()));
        holder.setSelecteded(R.id.ivCollect,true);
        holder.setOnClickListener(R.id.ivCollect, v -> onItemClick(v,position));

        binding.executePendingBindings();
    }
}

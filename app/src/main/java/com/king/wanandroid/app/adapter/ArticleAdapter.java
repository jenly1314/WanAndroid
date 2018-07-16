package com.king.wanandroid.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.base.adapter.ViewHolderRecyclerAdapter;
import com.king.base.adapter.holder.ViewHolder;
import com.king.wanandroid.BR;
import com.king.wanandroid.R;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.util.TimeUtils;

import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class ArticleAdapter extends EmptyAdapter<ArticleBean> {
    public ArticleAdapter(Context context, List<ArticleBean> listData) {
        super(context, listData);
    }

    @Override
    public void bindViewData(BindingHolder holder, ArticleBean data, int position) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.data,data);
        holder.setText(R.id.tvTime, TimeUtils.getUpdatedRelativeTime(getContext(),data.getPublishTime()));
        holder.setSelecteded(R.id.ivCollect,data.isCollect());
        holder.setOnClickListener(R.id.ivCollect, v -> onItemClick(v,position));

        binding.executePendingBindings();
    }

    @Override
    public int getItemViewId() {
        return R.layout.rv_article_item;
    }


}

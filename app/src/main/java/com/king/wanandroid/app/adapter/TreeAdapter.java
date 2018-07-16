package com.king.wanandroid.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.wanandroid.R;
import com.king.wanandroid.app.comm.Tree;
import com.king.wanandroid.bean.TreeBean;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class TreeAdapter extends SimpleBindingAdapter<TreeBean> {

    private OnTagClickListener<TreeBean> mOnTagClick;

    private @Tree int mTree;

    public TreeAdapter(Context context, List<TreeBean> listData,@Tree int tree) {
        super(context, listData, tree == Tree.SYSTEM ? R.layout.rv_tree_item : R.layout.rv_tree_project_item);
        this.mTree = tree;
    }

    @Override
    public void bindViewData(BindingHolder holder, TreeBean data, int position) {
        super.bindViewData(holder, data, position);
        if(mTree == Tree.SYSTEM){
            TagFlowLayout flowLayout = holder.getView(R.id.flowLayout);
            flowLayout.setAdapter(new TreeTagAdapter(getContext(),data.getChildren()));
            flowLayout.setOnTagClickListener((view, pos, parent) -> {
                if(mOnTagClick!=null){
                    mOnTagClick.onTagClick(view,data, pos);
                }
                return true;
            });
        }
    }

    public void setOnTagClickListener(OnTagClickListener<TreeBean> onTagClick){
        this.mOnTagClick = onTagClick;
    }

    public interface OnTagClickListener<T>{
        void onTagClick(View view,T t,int position);
    }
}

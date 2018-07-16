package com.king.wanandroid.app.tree;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.king.wanandroid.app.base.DataRepository;
import com.king.wanandroid.app.base.DataViewModel;
import com.king.wanandroid.app.comm.Tree;
import com.king.wanandroid.bean.ArticleBean;
import com.king.wanandroid.bean.DataBean;
import com.king.wanandroid.bean.Resource;
import com.king.wanandroid.bean.TreeBean;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class TreeViewModel extends DataViewModel {
    @Inject
    public TreeViewModel(@NonNull Application application, DataRepository repository) {
        super(application, repository);
    }

    /**
     * 获取一级树
     * @param tree
     * @return
     */
    public LiveData<Resource<List<TreeBean>>> getTree(@Tree int tree) {
        if(tree == Tree.PROJECT){
            return getRepository().getProject();
        }

        return getRepository().getSystem();
    }

    public LiveData<Resource<DataBean<List<ArticleBean>>>> getTreeList(@Tree int tree,int curPage,int id,String key){
        if(tree == Tree.PROJECT){//项目列表
            return getRepository().getProjectList(curPage,id);
        }
        if(tree == Tree.SEARCH){//关键字搜索
            return getRepository().getArticlesList(curPage,key);
        }

        return getRepository().getArticlesList(curPage,id);
    }


}

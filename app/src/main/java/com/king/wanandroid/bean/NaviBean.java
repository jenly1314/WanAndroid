package com.king.wanandroid.bean;


import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class NaviBean {

    private int cid;

    private String name;

    private List<ArticleBean> articles;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticleBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleBean> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NaviBean naviBean = (NaviBean) o;

        if (cid != naviBean.cid) return false;
        return name != null ? name.equals(naviBean.name) : naviBean.name == null;
    }

    @Override
    public int hashCode() {
        int result = cid;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NaviBean{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", articles=" + articles +
                '}';
    }
}

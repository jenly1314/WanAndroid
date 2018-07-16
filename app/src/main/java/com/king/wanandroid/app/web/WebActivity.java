package com.king.wanandroid.app.web;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.just.agentweb.AgentWeb;
import com.king.base.util.StringUtils;
import com.king.wanandroid.R;
import com.king.wanandroid.app.base.DataViewModel;
import com.king.wanandroid.app.base.MVVMActivity;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.bean.CollectBean;
import com.king.wanandroid.databinding.WebActivityBinding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_WEB)
public class WebActivity extends MVVMActivity<DataViewModel,WebActivityBinding> implements PopupMenu.OnMenuItemClickListener {

    AgentWeb agentWeb;

    private int mId;
    private String mTitle;
    /**
     * 初次打开时的url
     */
    private String mUrl;
    private String mAuthor;
    /**
     * 当前url
     */
    private String mCurUrl;
    /**
     * 当前的收藏状态
     */
    private boolean isCollect;
    private boolean isArticle;
    /**
     * 用来记住最初的收藏状态
     */
    private boolean isStatus;

    PopupMenu popupMenu;


    @Override
    protected DataViewModel createViewModel() {
        return getViewModel(DataViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.web_activity;
    }

    @Override
    public void initData() {
        setRightImage(R.drawable.btn_more_selector);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Constants.KEY_TITLE);
        mUrl = intent.getStringExtra(Constants.KEY_URL);
        mAuthor = intent.getStringExtra(Constants.KEY_AUTHOR);
        mId = intent.getIntExtra(Constants.KEY_ID,-1);
        isCollect = intent.getBooleanExtra(Constants.KEY_IS_COLLECT,false);
        isStatus = isCollect;
        isArticle = mId > 0;
        setToolbarTitle(mTitle);
        loadUrl(mUrl);

    }

    @Override
    protected void onResume() {
        super.onResume();
        agentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        agentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agentWeb.getWebLifeCycle().onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(!agentWeb.back()){
            //通知首页改变收藏状态
            if(isStatus != isCollect){
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_IS_COLLECT,isCollect);
                setResult(RESULT_OK,intent);
            }

            super.onBackPressed();
        }

    }

    public void loadUrl(String url){
        if(agentWeb == null){
            agentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mBinding.ll,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultIndicator(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark),2)
                    .setWebChromeClient(new WebChromeClient(){
                        @Override
                        public void onReceivedTitle(WebView view, String title) {
                            super.onReceivedTitle(view, title);
                            if(StringUtils.isNotBlank(title)){
                                setToolbarTitle(title);
                            }
                        }

                    })
                    .setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            mCurUrl = url;
                        }
                    })
                    .createAgentWeb()
                    .go(url);
        }else{
            agentWeb.getWebCreator().getWebView().loadUrl(url);
        }


    }

    private void setIconEnable(Menu menu, boolean enable){
        try{
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //传入参数
            m.invoke(menu, enable);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void clickMenu(){
        if(popupMenu == null){
            popupMenu = new PopupMenu(getContext(),mHolder.getView(R.id.ivRight));
            popupMenu.getMenu().clear();
            setIconEnable(popupMenu.getMenu(),true);
            popupMenu.getMenuInflater().inflate(R.menu.toolbar_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
//            try {
//                Field field = popupMenu.getClass().getDeclaredField("mPopup");
//                field.setAccessible(true);
//                MenuPopupHelper helper = (MenuPopupHelper) field.get(popupMenu);
//                helper.setForceShowIcon(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }

        MenuItem item = popupMenu.getMenu().findItem(R.id.menuCollect);
        item.setVisible(isArticle);
        item.setTitle(isCollect ? R.string.toolbar_menu_un_collect : R.string.toolbar_menu_collect);

        popupMenu.show();
    }

    @Override
    public void OnClick(View v) {
        super.OnClick(v);
        switch (v.getId()){
            case R.id.ivRight:
                clickMenu();
                break;
        }
    }

    private void clickShare(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mCurUrl);
        startActivity(Intent.createChooser(intent, getString(R.string.toolbar_menu_share)));

    }

    /**
     * 收藏，通过id收藏站内文章。
     */
    private void clickCollect(){
        if(checkLogin()){
            if(isCollect){
                mViewModel.unCollect(mId).observe(this, resource ->{
                    resource.handle(new OnCallback<CollectBean>() {
                        @Override
                        public void onSuccess(CollectBean data) {
                            isCollect = false;
                            showToast(R.string.success_un_collect);
                        }
                    });
                });
            }else{
                //收藏
                mViewModel.collect(mId).observe(this, resource ->{
                    resource.handle(new OnCallback<CollectBean>() {
                        @Override
                        public void onSuccess(CollectBean data) {
                            isCollect = true;
                            showToast(R.string.success_collect);
                        }
                    });
                });
            }

        }
    }

    private void clickExplorer(){
        Uri uri = Uri.parse(mCurUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuShare:
                clickShare();
                break;
            case R.id.menuCollect:
                clickCollect();
                break;
            case R.id.menuRefresh:
                loadUrl(mCurUrl);
                break;
            case R.id.menuExplorer:
                clickExplorer();
                break;
        }
        return true;
    }
}

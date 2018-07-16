package com.king.wanandroid.app.about;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.systembar.SystemBarHelper;
import com.king.wanandroid.R;
import com.king.wanandroid.app.base.BindingActivity;
import com.king.wanandroid.app.comm.Constants;
import com.king.wanandroid.databinding.AboutActivityBinding;
import com.king.wanandroid.glide.GlideApp;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Route(path = Constants.ROUTE_ABOUT)
public class AboutActivity extends BindingActivity<AboutActivityBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.about_activity;
    }

    @Override
    public void initData() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mBinding.toolbar);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        GlideApp.with(this).load(R.drawable.ic_about_image).into(mBinding.ivAbout);
        mBinding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(getContext(),R.color.white));
        mBinding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(),R.color.white));
        mBinding.collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER);
        mBinding.tv.setText(Html.fromHtml(getString(R.string.about_desc)));
        mBinding.tv.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.fab.setOnClickListener(v -> startWeb(Constants.AUTHOR,"https://github.com/jenly1314"));
    }

    private void clickEmail(String email){
        Uri uri = Uri.parse(String.format("mailto:%s",email));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, null));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            showToast(R.string.tips_join_qq_group_exception);
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGithub:
                startWeb(Constants.AUTHOR,"https://github.com/jenly1314");
                break;
            case R.id.menuSource:
                startWeb(getString(R.string.menu_source),"https://github.com/jenly1314/WanAndroid");
                break;
            case R.id.menuIssue:
                startWeb(getString(R.string.menu_issue),"https://github.com/jenly1314/WanAndroid/issues");
                break;
            case R.id.menuEmail:
                clickEmail(Constants.GMAIL);
                break;
            case R.id.menuQQGroup:
                joinQQGroup(Constants.QQ_GROUP_KEY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

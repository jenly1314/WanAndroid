package com.king.wanandroid.bean;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class VersionBean {

    /**
     * versionCode : 2
     * versionName : 1.1
     * url : https://github.com/WanAndroid/app/release/app-release.apk
     * versionDesc : 有新的版本
     */

    private int versionCode;
    private String versionName;
    private String url;
    private String versionDesc;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }
}

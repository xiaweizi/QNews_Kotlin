package com.xiaweizi.qnews.bean;


/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.bean
 * 类名：    VersionBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/14
 * 创建时间： 10:27
 */
// https://github.com/xiaweizi/QNews/raw/master/QNews.apk
public class VersionBean{

    @Override
    public String toString() {
        return "VersionBean{" +
                "versionName='" + versionName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

    private String versionName;//你好
    private String downloadUrl;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

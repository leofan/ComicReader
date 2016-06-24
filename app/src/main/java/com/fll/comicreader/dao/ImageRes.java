package com.fll.comicreader.dao;

/**
 * Created by Administrator on 2016/6/24.
 */
public class ImageRes {
    private String name;
    private String netUrl;
    private String localUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public ImageRes(String name, String netUrl, String localUrl) {
        this.name = name;
        this.netUrl = netUrl;
        this.localUrl = localUrl;
    }
}

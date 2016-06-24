package com.fll.comicreader.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * <a title="第013集" class="list_href" rel="external" href="http://3gmanhua.com/vols/29280_228717/">第013集</a>
 * 章节信息
 * url http://3gmanhua.com/vols/29280_228717/
 * 名字第013集
 * Created by Administrator on 2016/1/27.
 */
public class Section {
    private String name;
    private String url;
    private List<ImageRes> imageList = new ArrayList<ImageRes>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addImageRes(ImageRes imageRes){
        imageList.add(imageRes);
    }
}

package com.fll.comicreader.dao;

import java.util.List;

/**
 * wget http://3gmanhua.com/comic/1829331/  ; 浏览器的地址
 * <title>
 XXXXX 完结 XXXXX 未散ソノオ 漫画信息 汗汗漫画
 </title>
 *
 * Created by Administrator on 2016/1/27.
 */
public class Comic {
    private String title;
    private List<Section> sections;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}

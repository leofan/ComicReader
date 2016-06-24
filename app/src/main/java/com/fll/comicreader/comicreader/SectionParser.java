package com.fll.comicreader.comicreader;

import android.os.Environment;
import android.util.Log;

import com.fll.comicreader.dao.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/6/8.
 */
public class SectionParser {
    private static final String TAG = SectionParser.class.getName();

    public static List<Section> getSectionInfos(String listPage){
        List<Section> sections = new ArrayList<Section>();
        Matcher m = Pattern
                .compile(
                        "<a title=\".+?\" class=\"list_href\" rel=\"external\" href=\"(.+?)\">(.+?)</a>")
                .matcher(listPage);

        while (m.find()) {
            Section oneSection = new Section();
            String url = m.group(1);
            String name = m.group(2);
            oneSection.setUrl(url);
            oneSection.setUrl(name);
            sections.add(oneSection);
            Log.d(TAG,String.format("name:%s , url:%s",name,url));
        }
        return  sections;
    }

    /**
     * 判断是section page还是 section list page
     * @return
     */
    public static boolean isSectionListPage(String url){
        return url.startsWith("http://3gmanhua.com/comic");
    }

    /**
     * 判断是section page还是 section list page
     * @return
     */
    public static boolean isSectionPage(String url){
        return url.startsWith("http://3gmanhua.com/vols");
    }

    public static String getSecDestLocation(PageEle pageEle){
        String subDir = "unknow";
        String title = pageEle.title;
        if(title != null && title.length() > 0){
            subDir = title.replaceAll(" ","/");
        }
        return CommicConstants.ROOT_DICT + subDir + "/";
    }

    /**
     * 在页面上提取出sDS，sPath，sFiles
     *
     * @param mainPage
     * @param sDsPage
     * @return
     */
    public static PageEle getPageEle(String mainPage, String sDsPage) throws Exception {
        PageEle result = new PageEle();

        Matcher m = Pattern.compile("var sFiles {0,1}= {0,1}\"(.+?)\";var sPath {0,1}= {0,1}\"([0-9]+?)\"")
                .matcher(mainPage);

        boolean hasFind = m.find();
        String sDS = null;
        int sPath = 0;

        //
        if (hasFind) {
            result.sFiles = m.group(1);
            result.sPath = Integer.parseInt(m.group(2));
        } else {
            throw new Exception("找不到对应的元素");
        }

        Matcher sDSm = Pattern.compile("var sDS {0,1}= {0,1}\"(.+?)\";")
                .matcher(sDsPage);
        hasFind = sDSm.find();
        if (hasFind) {
            result.sDS = sDSm.group(1);
        }

        Matcher titleM = Pattern.compile("id=\"hdTitle2\" value=\"(.+?)\"")
                .matcher(mainPage);
        hasFind = titleM.find();
        if (hasFind) {
            result.title = titleM.group(1);
        }

        Log.d(TAG, result.toString());
        return result;
    }

    public static List<String> getDownloadUrls(PageEle pageEle) {
        return SectionParser.getDownloadUrls(pageEle.sDS, pageEle.sPath, pageEle.sFiles);
    }

    public static List<String> getDownloadUrls(String sDS, int sPath, String sFiles) {
        List<String> result = new ArrayList<String>();
        Log.d(TAG, String.format("sDS=%s, sPath=%d", sDS, sPath));
        String domain = getDomain(sDS, sPath);
        Log.d(TAG, "domain:" + domain);
        Log.d(TAG, "sFiles:" + sFiles);
        String tailss = unsuan(sFiles);
        String[] tails = tailss.split("\\|");
        for (int i = 0; i < tails.length; i++) {
            result.add(domain + tails[i]);
        }
        Log.d(TAG, "after unsuan:" + tailss);
        return result;
    }

    private static String getDomain(String sDS, int sPath) {
        String[] arrDS = sDS.split("\\|");
        String u = "";
        for (int i = 0; i < arrDS.length; i++) {
            if (sPath == (i + 1)) {
                u = arrDS[i];
                break;
            }
        }
        return u;
    }

    /**
     * 解密
     *
     * @param s
     * @return
     */
    private static String unsuan(String s) {
        String x = s.substring(s.length() - 1);
        int xi = "abcdefghijklmnopqrstuvwxyz".indexOf(x) + 1;
        String sk = s.substring(s.length() - xi - 12, s.length() - xi - 1);
        s = s.substring(0, s.length() - xi - 12);
        String k = sk.substring(0, sk.length() - 1);
        String f = sk.substring(sk.length() - 1);
        for (int i = 0; i < k.length(); i++) {
            //eval("s=s.replace(/"+ k.substring(i,i+1) +"/g,'"+ i +"')");
            s = s.replaceAll(k.substring(i, i + 1), Integer.toString(i));
        }
        String[] ss = s.split(f);
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < ss.length; i++) {
            int ivalue = Integer.valueOf(ss[i]).intValue();
            result.append((char) ivalue);
        }
        return result.toString();
    }
}

package com.fll.comicreader.comicreader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/1/23.
 */
public class ContentParser {

    public String posturl(String url) throws IOException {
        MainActivity.sInstance.handler.sendEmptyMessage(MainActivity.SHOW_LOADING);
        InputStream is = null;
        String result = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();

        result = sb.toString();
        MainActivity.sInstance.handler.sendEmptyMessage(MainActivity.DISMISS_LOADING);
        return result;
    }

}

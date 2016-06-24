package com.fll.comicreader.comicreader;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/1/25.
 */
public class Downloader {

    public static String TAG = "Downloader";

    /**
     * download a file by an url, and save it as filePath
     *
     * @param url
     * @param filePath
     */
    public static void downloadFile(String url, String filePath) throws IOException {

        FileOutputStream fop = null;
        try {
            URL downloadUrl = new URL(url);

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }else{
                return;
            }
            fop = new FileOutputStream(file);
            URLConnection conn = downloadUrl.openConnection();

            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

            int len;
            while ((len = bis.read(buffer, 0, 1024)) != -1) {
                fop.write(buffer, 0, len);
            }
            Log.d(TAG, "finish download:"
                    + filePath);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            Log.e(TAG,"download failed",e);
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e2) {
            }
            throw e;
        }
    }

    public static InputStream getHTMLInputStream(String url) throws IOException {
        URL downloadUrl = new URL(url);
        URLConnection conn = downloadUrl.openConnection();
        return conn.getInputStream();
    }

}

package com.fll.comicreader.comicreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int GOT_PAGELISTSTR = 8;
    public static final int SHOW_LOADING = 9;
    public static final int DISMISS_LOADING = 10;
    public static MainActivity sInstance = null;
    private ProgressDialog pd1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOT_PAGELISTSTR:
                    String pageContent = (String) msg.obj;
                    SectionParser.getSectionInfos(pageContent);
                    break;
                case SHOW_LOADING:
                    pd1.show();
                    break;
                case DISMISS_LOADING:
                    pd1.hide();
                    break;
                default:
                    if (msg.obj instanceof String) {
                        Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, msg.what, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };
    private ContentParser contentParser = new ContentParser();
    private WebView myWebView;
    private String currentPageAddr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        pd1 = new ProgressDialog(this);
        pd1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd1.setCancelable(true);
        setContentView(R.layout.webview);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        myWebView.loadUrl("http://3gmanhua.com/");
    }

    private void parseSectionList(final String currentPageAddr) {
        new Thread() {
            @Override
            public void run() {
                int finalState = R.string.unknown_error;
                Message msg = new Message();
                String msgtext = null;
                try {
                    String pageListStr = contentParser.posturl(currentPageAddr);
                    Log.d(TAG,pageListStr);
                    finalState = GOT_PAGELISTSTR;
                    msgtext = pageListStr;
                } catch (Exception e) {
                    Log.e(TAG, "资源解析失败", e);
                    finalState = R.string.parse_sesion_failed;
                }
                msg.what = finalState;
                msg.obj = msgtext;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void startDownload(final String currentPageAddr) {
        MainUtils.createRootFolder();
        Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
        new Thread() {
            @Override
            public void run() {
                downloadOneSection(currentPageAddr);
            }
        }.start();

    }

    public void downloadOneSection(String url) {
        PageEle pageEle = null;
        int finalState = R.string.unknown_error;
        Message msg = new Message();
        String msgtext = null;
        try {
            String dsFile = contentParser.posturl(CommicConstants.dsFile);
            String mainPage = contentParser.posturl(currentPageAddr);
            Log.i(TAG, "ds.js============" + dsFile);
            Log.i(TAG, "index.html============" + mainPage);
            pageEle = SectionParser.getPageEle(mainPage, dsFile);
            List<String> imageFileUrls = SectionParser.getDownloadUrls(pageEle);
            String secDestLocation = SectionParser.getSecDestLocation(pageEle);
            File sectionDir = new File(secDestLocation);
            if (!sectionDir.exists()) {
                sectionDir.mkdirs();
            }
            int totleCount = imageFileUrls.size();
            int failedCount = 0;
            for (int i = 0; i < totleCount; i++) {
                String imageUrl = imageFileUrls.get(i);
                Log.i(TAG, "download url============" + imageUrl);
                String[] splitParts = imageUrl.split("\\.");
                String imageType = splitParts[splitParts.length - 1];
                try {
                    String desFile = secDestLocation + (i + 1) + "." + imageType;
                    Log.i(TAG, "save as " + desFile);
                    Downloader.downloadFile(imageUrl, desFile);
                } catch (IOException e) {
                    Log.e(TAG, "下载失败", e);
                    failedCount++;
                }
            }
            if (failedCount > 0) {
                msgtext = String.format("下载成功 %d/%d", (totleCount - failedCount), totleCount);
            } else {
                finalState = R.string.download_section_finish;
            }

        } catch (Exception e) {
            Log.e(TAG, "资源解析失败", e);
            finalState = R.string.parse_sesion_failed;
        }
        msg.what = finalState;
        msg.obj = msgtext;
        handler.sendMessage(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                final String url = myWebView.getUrl();
                if (SectionParser.isSectionPage(url)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setItems(getResources().getStringArray(R.array.download), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int index) {
                            switch (index) {
                                case 0:
                                    startDownload(url);
                                    break;
                                default:
                                    break;
                            }

                            arg0.dismiss();
                        }
                    });
                    builder.show();
                    break;
                } else if (SectionParser.isSectionListPage(url)) {
                    parseSectionList(url);
                }

            case KeyEvent.KEYCODE_BACK:
                myWebView.goBack();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        pd1.dismiss();
        super.onDestroy();
    }
}

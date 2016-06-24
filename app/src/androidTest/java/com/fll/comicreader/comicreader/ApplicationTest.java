package com.fll.comicreader.comicreader;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.os.Environment;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @SmallTest
    public void testDownloadFile() {
        new Thread(){
            @Override
            public void run() {
                String dict = Environment.getExternalStorageDirectory().getAbsolutePath();
                try{
                    Downloader.downloadFile("http://img.my.csdn.net/uploads/201303/07/1362634124_9630.png",dict+"/testfile");
                }catch(Exception e){
                    
                }

            }
        }.start();

    }
}
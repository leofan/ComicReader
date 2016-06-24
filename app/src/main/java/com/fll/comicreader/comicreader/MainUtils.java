package com.fll.comicreader.comicreader;

import java.io.File;

/**
 * Created by Administrator on 2016/6/8.
 */
public class MainUtils {
    public static void createRootFolder(){
        File file = new File(CommicConstants.ROOT_DICT);
        if (!file.exists()) {
            file.mkdirs();
        }

    }
}

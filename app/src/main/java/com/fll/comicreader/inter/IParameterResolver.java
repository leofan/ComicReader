package com.fll.comicreader.inter;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/1/27.
 */
public interface IParameterResolver {

    /**
     * 输入html的InputStream ，输出一段有用的文字
     * @param is
     * @return
     * @throws Exception
     */
    public void parseHTML(InputStream is) throws Exception;
}

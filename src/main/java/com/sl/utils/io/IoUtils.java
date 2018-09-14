package com.sl.utils.io;

import java.io.ByteArrayInputStream;

/**
 *
 */
public class IoUtils {
    /**
     *  字符串转出城输入流
     * @param str
     * @return
     */
    public static ByteArrayInputStream getInputStream(String str){
        ByteArrayInputStream in = null;
        if(str!=null){
            in= new ByteArrayInputStream(str.getBytes());
        }
        return in;
    }
}

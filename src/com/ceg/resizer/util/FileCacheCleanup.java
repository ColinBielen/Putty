package com.ceg.resizer.util;

/**
 * Created by IntelliJ IDEA.
 * User: colinb
 * Date: 9/13/11
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileCacheCleanup implements Runnable{

    private String cacheRoot = "/tmp";


    public FileCacheCleanup(String cacheRoot) {
        this.cacheRoot = cacheRoot;
    }


    @Override
    public void run() {
        //Get every file in the cache root and delete it if it's over 2 hours old.

    }
}

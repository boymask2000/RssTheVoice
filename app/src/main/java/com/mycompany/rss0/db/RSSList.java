package com.mycompany.rss0.db;

/**
 * Created by gposabella on 1/27/15.
 */
public class RSSList {
    private static String list[] = {
            "http://www.repubblica.it/rss/homepage/rss2.0.xml",
            "http://www.repubblica.it/rss/cronaca/rss2.0.xml"};

    public static String[] getList() {
        return list;
    }
}
package com.mycompany.rss0;

/**
 * Created by gposabella on 1/27/15.
 */

public class DataList {
    private long id =0;
    private String link;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String toString(){
        return title;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
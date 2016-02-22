package com.mycompany.rss0.db;

/**
 * Created by gposabella on 1/27/15.
 */
public class RssData {
    private int id;
    private String name;
    private String link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {

        return link;
    }

}
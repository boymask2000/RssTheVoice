package com.mycompany.rss0;

import android.content.Context;

import com.mycompany.rss0.com.mycompany.rss0.publish.PrintPublisher;
import com.mycompany.rss0.com.mycompany.rss0.publish.Publisher;
import com.mycompany.rss0.com.mycompany.rss0.publish.TalkPublisher;
import com.mycompany.rss0.db.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gposabella on 1/28/15.
 */
public class HotWordChecker {
    private final Context context;
    private List<String> lista = null;

    private HashMap<String, String> dette = new HashMap();
    // private Publisher publisher = new NetworkPublisher("9.87.115.114", 22000);
    private Publisher publisher;


    private List<Publisher> publisherList = new ArrayList<Publisher>();

    public HotWordChecker(Context context) {
        this.context = context;

        publisher = new TalkPublisher(context);
        publisherList.add(publisher);
        publisherList.add(new PrintPublisher());
    }


    public void check(String title) {

        lista = DBHelper.getAllHotWord(context);
        for (int i = 0; i < lista.size(); i++) {
            String tit = title.toLowerCase();
            String val = lista.get(i).toLowerCase();
            if (tit.indexOf(val) != -1) {
                if (dette.get(title) == null) {
                    dette.put(title, title);
                    for (Publisher p : publisherList)
                        p.publish(title);
                }
            }

        }
    }


}
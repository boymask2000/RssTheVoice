package com.mycompany.rss0;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

import com.mycompany.rss0.db.DBHelper;
import com.mycompany.rss0.db.RssData;

import java.net.URL;
import java.util.List;


public class RssService extends Service {

    private HotWordChecker hot;
    private Handler handler = new Handler();
    private int delay = 10000;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hot = new HotWordChecker(this);

        new ServiceParser().execute();

        return START_STICKY;
    }

    private class ServiceParser extends AsyncTask<URL, DataList, Integer> {

        @Override
        protected Integer doInBackground(URL... params) {


            handler.postDelayed(new Job(), 100);
            return null;
        }
    }

    private class Job extends Thread implements RssEventListener {
        @Override
        public void run() {
            List<RssData> lista = DBHelper.getAllRss(getBaseContext());
            for (RssData rss : lista) {

                RssParser parser = new RssParser(rss.getLink());
                parser.setListener(this);
                parser.start();

            }
            handler.postDelayed(new Job(), delay);
        }

        @Override
        public void newsFound(DataList data) {

            hot.check(data.getTitle());
        }

        @Override
        public void starting() {

        }
    }


}

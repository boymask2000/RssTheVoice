package com.mycompany.rss0;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

import com.mycompany.rss0.db.DBHelper;
import com.mycompany.rss0.db.RssData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RssService extends Service {

    private Handler handler = new Handler();
    private int delay = 10000;

    public RssService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
        public void run() {
            List<RssData> lista = DBHelper.getAllRss(getBaseContext());
            for (RssData rss : lista) {

                RssParser parser = new RssParser(rss.getLink());
                parser.setListener(this);
                parser.start();

            }
            handler.postDelayed(this, delay);
        }

        @Override
        public void newsFound(DataList data) {
            sendDataNet(data.getTitle(), "9.87.115.114");
        }

        @Override
        public void starting() {

        }
    }

    public static int sendDataNet(String text, String ip) {
        Socket s = null;
        int total = 0;
        OutputStream output = null;
        try {
            s = new Socket(ip, 22000);
            output = s.getOutputStream();
            PrintWriter bw = new PrintWriter(output);


            byte buffer[] = new byte[1024];

            int bytesRead;

            bw.write(text);

            bw.flush();
            bw.close();
            output.flush();
            output.close();

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (s != null)
                    s.close();
                if (output != null)
                    output.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return total;
    }
}

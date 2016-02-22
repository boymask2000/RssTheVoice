package com.mycompany.rss0.com.mycompany.rss0.publish;

import android.os.AsyncTask;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by gposabella on 1/28/15.
 */
public class NetworkPublisher implements Publisher {
    private final String host;
    private final int port;


    public NetworkPublisher(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void publish(String s) {
        new NetworkTask().execute(s);
    }

    @Override
    public void clean() {

    }

    private class NetworkTask extends AsyncTask<String, Integer, Integer> {
        protected Integer doInBackground(String... st) {
            try {
                Socket s = new Socket(host, port);
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                pw.println(st[0]);
                pw.flush();

                pw.close();

                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }

}

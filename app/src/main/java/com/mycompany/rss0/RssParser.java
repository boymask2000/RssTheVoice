package com.mycompany.rss0;

import android.os.AsyncTask;

import com.mycompany.rss0.com.mycompany.rss0.publish.PrintPublisher;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by gposabella on 1/27/15.
 */
public class RssParser {
    private URL url;



    private RssEventListener listener = null;

    public RssParser(String surl) {
        try {
            url = new URL(surl);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        new HttpParser().execute(url);
    }

    public void setListener(RssEventListener listener) {
        this.listener = listener;
    }

    private class HttpParser extends AsyncTask<URL, DataList, Integer> {
        private int counter = 0;

        protected Integer doInBackground(URL... urls) {

            if (listener != null) listener.starting();
            doIt(urls[0], 0);

            return 0;
        }

        private void doIt(URL url, int pos) {

            try {
                //   URL url = new URL(surl);

                URLConnection conn = url.openConnection();

                InputStream is = conn.getInputStream();

                Document doc = null;

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(is);

                doc.getDocumentElement().normalize();

                NodeList nodes = doc.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {

                    Node node = nodes.item(i);
                    if (node.getNodeType() != Node.ELEMENT_NODE)
                        continue;
                    parseNode(node, 0);
                }

                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parseNode(Node node, int lev) {

            NodeList ch = node.getChildNodes();
            for (int i = 0; i < ch.getLength(); i++) {
                Node n = ch.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (n.getNodeName().equals("item"))
                    parseItem(n, lev + 3);
                else
                    parseNode(n, lev + 3);
            }
        }

        private void parseItem(Node node, int lev) {
            String p = "";

            NodeList ch = node.getChildNodes();
            // DataList data = new DataList();
            String title = searchNode(node, "title");
            String link = searchNode(node, "link");
            if (title != null && link != null) {
                DataList data = new DataList();
                data.setCounter(++counter);
                data.setTitle(title);
                data.setLink(link);


                publishProgress(data);
            }

        }


        private String searchNode(Node node, String name) {
            String p = "";

            NodeList ch = node.getChildNodes();
            for (int i = 0; i < ch.getLength(); i++) {
                Node n = ch.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                if (n.getNodeName().equals(name)) {
                    return n.getTextContent();
                }

            }
            return null;
        }


        protected void onProgressUpdate(DataList... progress) {
            if (listener == null) return;

            listener.newsFound(progress[0]);

        }

        protected void onPostExecute(Integer result) {

        }
    }

}
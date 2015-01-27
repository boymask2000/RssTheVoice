package com.mycompany.rss0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RssShowActivity extends Activity {

    private final List<DataList> list = new ArrayList<DataList>();
    private ListView listview = null;
    private StableArrayAdapter adapter = null;
    private TextView message;
    private Handler handler = new Handler();

    private String url;

    private boolean paused = false;
    private int delay = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            url = b.getString("url");
        }
      //  url = "http://www.repubblica.it/rss/homepage/rss2.0.xml";


        setContentView(R.layout.rsshowactivity);
        listview = (ListView) findViewById(R.id.listview);
        message = (TextView) findViewById(R.id.message);
        registerForContextMenu(listview);
        adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        setAction();


        handler.postDelayed(new Job(), 100);
    }

    public class Job extends Thread {
        public void run() {
            message.setText((new Date()).toString());
            RssParser parser = new RssParser(url);
            parser.setListener(new RssEventListener() {
                @Override
                public void newsFound(DataList data) {
                    setItem(data);
                }

                @Override
                public void starting() {
                }
            });
            parser.start();

            if (!paused)
                handler.postDelayed(this, delay);
        }
    }

    @Override
    protected void onPause() {
        paused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        paused = false;
        handler.postDelayed(new Job(), delay);
        super.onResume();
    }

    private void setAction() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final DataList item = (DataList) parent
                        .getItemAtPosition(position);
                String link = item.getLink();
                message.setText(link);

                Uri uri = Uri.parse(link);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));

            }

        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void setItem(DataList data) {
        if (data.getCounter() == 1) list.clear();
        int size = list.size();
        data.setId(size + 1);
        list.add(data);
        adapter.load(list);
        adapter.notifyDataSetChanged();
    }


    private class StableArrayAdapter extends ArrayAdapter<DataList> {

        List<DataList> mIdMap = new ArrayList<DataList>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<DataList> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.add(objects.get(i));
            }
        }

        public void clean() {
            mIdMap.clear();

        }

        public void load(List<DataList> objects) {
            clean();
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.add(objects.get(i));
            }
        }

        @Override
        public long getItemId(int position) {
            if (position >= mIdMap.size())
                return 0;
            DataList item = mIdMap.get(position);
            return item.getId();

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}

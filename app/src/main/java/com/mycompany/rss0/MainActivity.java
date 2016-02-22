package com.mycompany.rss0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mycompany.rss0.db.DBHelper;
import com.mycompany.rss0.db.RssData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView listview = null;
    private StableArrayAdapter adapter = null;
    private final List<RssData> list = new ArrayList<RssData>();
    private TextView message;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper.checkAndImportDB(this);
        fillList();

        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        message = (TextView) findViewById(R.id.message);
        registerForContextMenu(listview); // TODO
        adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        setAction();

    //    message.setText("ssssssss");
    }

    private void setAction() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final RssData item = (RssData) parent
                        .getItemAtPosition(position);

                Intent intent = new Intent(getBaseContext(),
                        RssShowActivity.class);
                Bundle b = new Bundle();
                b.putString("url", item.getLink());
                intent.putExtras(b);
                startActivity(intent);
            }

        });
    }

    private List<RssData> fillList() {
        list.clear();
        List<RssData> lista = DBHelper.getAllRss(this);
        for (RssData dish : lista) {
            list.add(dish);
        }
        return lista;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private class StableArrayAdapter extends ArrayAdapter<RssData> {

        List<RssData> mIdMap = new ArrayList<RssData>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<RssData> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.add(objects.get(i));
            }
        }
        public void reload(List<RssData> ll ){
            load(ll);

        }

        public void clean() {
            mIdMap.clear();

        }

        public void load(List<RssData> objects) {
            clean();
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.add(objects.get(i));
            }
        }

        @Override
        public long getItemId(int position) {
            if (position >= mIdMap.size())
                return 0;
            RssData item = mIdMap.get(position);
            return item.getId();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) getBaseContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.feeditem, parent, false);

         //   if( position>=mIdMap.size())position=mIdMap.size()-1;
            final RssData item = mIdMap.get(position);
            String s = item.getLink();


            TextView textView = (TextView) rowView.findViewById(R.id.link);

            ImageButton delButton = (ImageButton) rowView.findViewById(R.id.deleteButton);
            delButton.setVisibility(edit ? View.VISIBLE : View.GONE);

            textView.setText(s);


            delButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DBHelper.deleteRSS(getBaseContext(), item);
                    adapter.reload(fillList());
                    adapter.notifyDataSetChanged();
                    edit = false;

                }
            });


            return rowView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_new_rss, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_feed:
                Intent i = new Intent(this, NewRssActivity.class);
                startActivityForResult(i,0);
                break;
            case R.id.edit_feeds:
                edit = !edit;
                adapter.notifyDataSetChanged();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
       // fillList();
        if (requestCode == 0) {
            adapter.reload(fillList());
            adapter.notifyDataSetChanged();
        }
    }
}
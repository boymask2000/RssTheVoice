package com.mycompany.rss0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mycompany.rss0.db.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class HotWordsHandlerActivity extends ActionBarActivity {

    private ListView listview = null;
    private ArrayAdapter adapter = null;
    private final List<String> list = new ArrayList<String>();
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_words_handler);

        fillList();

        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        message = (TextView) findViewById(R.id.message);


        listview = (ListView) findViewById(R.id.listview);
        adapter = new StableArrayAdapter(this, R.layout.listitem, list);
        listview.setAdapter(adapter);
        registerForContextMenu(listview);
    }

    private void fillList() {
        list.clear();
        List<String> lista = DBHelper.getAllHotWord(this);
        for (String dish : lista) {
            list.add(dish);
        }
        //   Heap.setLista(lista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hot_words_handler, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(list.get(info.position));
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onRestart();
        refresh();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        String[] menuItems = getResources().getStringArray(R.array.menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = list.get(info.position);


        DBHelper.deleteHotWord(this, listItemName);
        refresh();
        return true;
    }

    private void refresh() {
        fillList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_word) {
            Intent i = new Intent(this, NewHotWordActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        List<String> mIdMap = new ArrayList<String>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.add(objects.get(i));
            }
        }

        public void clean() {
            mIdMap.clear();

        }

        public void load(List<String> objects) {
            clean();
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.add(objects.get(i));
            }
        }

        @Override
        public long getItemId(int position) {
            if (position >= mIdMap.size())
                return 0;
            String item = mIdMap.get(position);
            return position;

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}

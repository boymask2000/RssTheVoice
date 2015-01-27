package com.mycompany.rss0;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mycompany.rss0.db.DBHelper;
import com.mycompany.rss0.db.RssData;


public class NewRssActivity extends Activity implements View.OnClickListener {

    private TextView nome;
    private TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_rss);
        nome = (TextView) findViewById(R.id.nome);
        link = (TextView) findViewById(R.id.link);

        setAction();
    }

    private void setAction() {
        View okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                RssData data = new RssData();
                String n = nome.getText().toString();
                String l = link.getText().toString();
                if (l.trim().length() > 0) {
                    data.setName(n);
                    data.setLink(l);
                    DBHelper.insertRSS(this, data);
                }
                finish();
                break;

        }
    }
}
package com.mycompany.rss0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mycompany.rss0.db.DBHelper;

public class NewHotWordActivity extends Activity implements View.OnClickListener {

    private TextView nome;
    private TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_hot_word);
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

                String n = nome.getText().toString();

                if (n.trim().length() > 0) {

                    DBHelper.insertHotWord(this, n);
                }
                finish();
                break;

        }
    }
}
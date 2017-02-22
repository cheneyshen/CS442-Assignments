package com.example.fshen4.CS442_Places_Near_Me;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.fshen4.CS442_Places_Near_Me.R.layout.activity_search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            doMySearch(query);
        }

        // 获得额外递送过来的值
        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            String testValue = appData.getString("KEY");
            System.out.println("extra data = " + testValue);
        }

    }

    private void doMySearch(String query) {
        // TODO 自动生成的方法存根
        TextView textView = (TextView) findViewById(R.id.tAddress);
        textView.setText(query);
        Toast.makeText(this, "do search", Toast.LENGTH_SHORT).show();
    }

}

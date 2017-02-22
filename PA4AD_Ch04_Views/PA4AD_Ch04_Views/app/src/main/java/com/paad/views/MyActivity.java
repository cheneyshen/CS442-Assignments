package com.paad.views;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import java.util.Random;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MyActivity extends Activity {
    /** Called when the activity is first created. */
    private ListView lv;
    private static int counter;
    ArrayList<String> todoItems = new ArrayList<String>();

    EditText editText;
    private ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    Random rand = new Random();
    boolean value;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //editText = (EditText)findViewById(R.id.editText);
        setContentView(R.layout.main);
        ListView myListView = (ListView)findViewById(R.id.listView2);
        final EditText myEditText = (EditText)findViewById(R.id.editText);
        final ArrayList<String> todoItems = new ArrayList<String>();
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,todoItems);
        myListView.setAdapter(aa);
        //System.out.println(value);

        for (int i = 1; i <= 5; i++) {
            if (rand.nextBoolean()) value = true;
            else value = false;
            if (value)
                todoItems.add("dish" + String.valueOf(i) + "        Price:" + 2 * i);
            else
                todoItems.add("meal" + String.valueOf(i) + "        Price:" + 2 * i);

        }
        myEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String strin = myEditText.getText().toString();
                        todoItems.add(strin + "        Price:" + String.valueOf(rand.nextInt(20) % 20 + 1));
                        myEditText.setText("");
                        aa.notifyDataSetChanged();
                        return true;
                    }
                return false;
            }
        });

        myListView.setOnItemClickListener(onclickListItem);
        myListView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // TODO Auto-generated method stub
                String info = ((android.widget.TextView) arg1).getText().toString().substring(0, 9);
                todoItems.remove(pos);
                android.widget.Toast.makeText(getBaseContext(), info + "Removed", Toast.LENGTH_SHORT).show();
                aa.notifyDataSetChanged();
                return true;
            }
        });
    }
    private OnItemClickListener onclickListItem = new OnItemClickListener() {
        //@Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (rand.nextBoolean()) value = true;
            else value = false;
            String info = ((android.widget.TextView) arg1).getText().toString().substring(0, 9);
            if (value) {
                android.widget.Toast.makeText(getBaseContext(), info + "meterials:" + String.valueOf(rand.nextInt(20)) + "   spicy:Yes", Toast.LENGTH_SHORT).show();
            }
            else {
                android.widget.Toast.makeText(getBaseContext(), info + "meterials:" + String.valueOf(rand.nextInt(20)) + "   spicy:No", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
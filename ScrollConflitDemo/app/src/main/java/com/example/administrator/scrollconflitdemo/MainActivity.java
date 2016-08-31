package com.example.administrator.scrollconflitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView lv1, lv2, lv3;
    private ArrayAdapter<String> adapter1, adapter2, adapter3;
    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<String> list3 = new ArrayList<>();

    private HorizontalScrollViewEx mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 40; i++) {
            list1.add("list1###" + (i + 1));
            list2.add("list2###" + (i + 1));
            list3.add("list3###" + (i + 1));
        }
        initView();
    }

    private void initView() {
        lv1 = (ListView) findViewById(R.id.listview1);
        lv2 = (ListView) findViewById(R.id.listview2);
        lv3 = (ListView) findViewById(R.id.listview3);
        mLayout = (HorizontalScrollViewEx) findViewById(R.id.layout);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list2);
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list3);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        lv3.setAdapter(adapter3);

    }
}

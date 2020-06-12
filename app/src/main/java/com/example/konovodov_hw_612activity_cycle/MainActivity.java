package com.example.konovodov_hw_612activity_cycle;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_1 = "one";
    public static final String KEY_2 = "two";
    public static final String KEY_LIST = "delList";

    private static String NOTE_TEXT = "note_text";

    private SharedPreferences myNoteSharedPref;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Map<String, String>> mapList = new ArrayList<>();

    private ArrayList<Integer> arrayList;

    private ListView list;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
        setContentView(R.layout.activity_list_view);
        myNoteSharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        newPrepareContent(getPref());
        initView();

        simpleAdapter =  createSimpleAdapter(mapList);
        list.setAdapter(simpleAdapter);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        arrayList.clear();
        arrayList = savedInstanceState.getIntegerArrayList(KEY_LIST);

        for (int i = 0; i < arrayList.size(); i++) {
            mapList.remove(arrayList.get(i).intValue());

        }
        simpleAdapter.notifyDataSetChanged();
    }

    @NonNull
    private SimpleAdapter createSimpleAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values, R.layout.activity_main,
                new String[]{KEY_1, KEY_2}, new int[]{R.id.oneTttv, R.id.twoTttv});

    }

    private void initView() {


        list = findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList.add(position);
                mapList.remove(position);

                simpleAdapter.notifyDataSetChanged();
            }

        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(KEY_LIST, arrayList);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    private void newPrepareContent(String str) {
        mapList.clear();

        int subStrCount = str.split("\n\n").length;

        for (int i = 0; i < subStrCount; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_1, str.split("\n\n")[i]);
            map.put(KEY_2, String.valueOf(str.split("\n\n")[i].length()));

            mapList.add(map);
        }
    }


    private String getPref() {
        if (!myNoteSharedPref.contains(NOTE_TEXT)) {
            return setPref();
        } else {
            return myNoteSharedPref.getString(NOTE_TEXT, "");
        }
    }

    private String setPref() {

        SharedPreferences.Editor myEditor = myNoteSharedPref.edit();
        String noteSave = getString(R.string.large_text);
        myEditor.putString(NOTE_TEXT, noteSave);
        myEditor.apply();
        Toast.makeText(MainActivity.this, "данные сохранены", Toast.LENGTH_LONG).show();
        return noteSave;
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                newPrepareContent(getPref());
                simpleAdapter = (SimpleAdapter) createSimpleAdapter(mapList);
                list.setAdapter(simpleAdapter);

                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);

            }
        }, 0);
    }
}

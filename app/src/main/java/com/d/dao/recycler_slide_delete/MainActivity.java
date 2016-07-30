package com.d.dao.recycler_slide_delete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerView recyclerView;
    private ArrayList<String> list = new ArrayList<>();
    private FirstAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (MyRecyclerView) findViewById(R.id.recycler);

        for (int i = 0; i < 30; i++) {
            list.add("" + i);
        }

        adapter = new FirstAdapter(MainActivity.this, list,recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
}

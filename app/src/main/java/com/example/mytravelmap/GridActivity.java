package com.example.mytravelmap;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GridActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        ArrayList<ListViewItem> list = (ArrayList<ListViewItem>) getIntent().getSerializableExtra("list");

        GridAdapter adapter = new GridAdapter(list);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        // List Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("visiable", false);
                startActivityForResult(intent, 50101);
            }
        });
    }
}

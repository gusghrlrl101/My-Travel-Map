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

        // 리스트 받아오기
        ArrayList<ListViewItem> list = (ArrayList<ListViewItem>) getIntent().getSerializableExtra("list");

        // 어댑터를 뷰에 연결
        GridAdapter adapter = new GridAdapter(list);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        // 항목 클릭 리스너
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Info 액티비티로 이동, visiable을 false로 하여 삭제 버튼 비활성
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("visiable", false);
                startActivityForResult(intent, 50101);
            }
        });
    }
}

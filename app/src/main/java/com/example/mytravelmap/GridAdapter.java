package com.example.mytravelmap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    // 저장할 데이터
    private ArrayList<ListViewItem> data;

    public GridAdapter(ArrayList<ListViewItem> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview, parent, false);
        }

        // 해당하는 id의 View 불러오기
        ImageView imgView = (ImageView) convertView.findViewById(R.id.grid_imageview);

        // List로부터 Data 가져오기
        ListViewItem listViewItem = data.get(position);

        System.out.println("%%%%%%%%%%%%%%%%%%");
        System.out.println(position + ", " + listViewItem.getId() + ", " + listViewItem.getImg());
        // 해당하는 View에 데이터 삽입
        imgView.setImageDrawable(Drawable.createFromPath(listViewItem.getImg()));

        return convertView;
    }
}

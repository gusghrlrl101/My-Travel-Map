package com.example.mytravelmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {
    // 저장할 데이터
    private ArrayList<ListViewItem> data = new ArrayList<ListViewItem>();
    private int cnt = 0;

    // 기본 생성자
    public ListViewAdapter() {
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

    public ArrayList<ListViewItem> getData(){
        return data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //  convertView에 "listview_item" Layout을 inflate 하기
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 해당하는 id의 View 불러오기
        ImageView imgView = (ImageView) convertView.findViewById(R.id.list_img);
        TextView titleView = (TextView) convertView.findViewById(R.id.list_title);
        TextView contentView = (TextView) convertView.findViewById(R.id.list_content);

        // List로부터 Data 가져오기
        ListViewItem listViewItem = data.get(position);

        // 해당하는 View에 데이터 삽입
        imgView.setImageDrawable(Drawable.createFromPath(listViewItem.getImg()));
        titleView.setText(listViewItem.getTitle());
        contentView.setText(listViewItem.getContent());

        return convertView;
    }

    // List에 데이터 추가하기
    int addData(String img, String title, String content, LatLng latLng) {
        cnt++;
        ListViewItem item = new ListViewItem(img, title, content, latLng, cnt);
        data.add(item);

        return cnt;
    }
}

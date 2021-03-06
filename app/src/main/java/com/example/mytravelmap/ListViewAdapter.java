package com.example.mytravelmap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> data = new ArrayList<ListViewItem>();

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 해당하는 id의 View 불러오기
        ImageView imgView = convertView.findViewById(R.id.list_img);
        TextView titleView = convertView.findViewById(R.id.list_title);
        TextView contentView = convertView.findViewById(R.id.list_content);
        // List로부터 Data 가져오기
        ListViewItem listViewItem = data.get(position);
        // 해당하는 View에 데이터 삽입
        imgView.setImageDrawable(Drawable.createFromPath(listViewItem.getImg()));
        titleView.setText(listViewItem.getTitle());
        contentView.setText(listViewItem.getContent());

        return convertView;
    }

    void addData(String img, String title, String content, LatLng latLng, String id) {
        ListViewItem item = new ListViewItem(img, title, content, latLng, id);
        data.add(item);
    }

    void deleteData(String id) {
        int index = 0;
        for (ListViewItem item : data) {
            if (item.getId().equals(id)) {
                data.remove(index);
                break;
            }
            index++;
        }
    }
}

package com.example.mytravelmap;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyListFragment extends Fragment {
    private ListViewAdapter adapter;
    private ListView listView = null;

    public static MyListFragment newInstance() {
        Bundle args = new Bundle();

        MyListFragment fragment = new MyListFragment();
        fragment.setArguments(args);

        return fragment;
    }


    public MyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_my_list, container, false);

        adapter = new ListViewAdapter();
        listView = (ListView) layout.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        adapter.addData(ContextCompat.getDrawable(getActivity(), R.drawable.img1), "제목1", "내용1");

        return layout;
    }

    public void addData(Drawable img, Marker marker, String title, String content){
        adapter.addData(img, title, content);
    }
}
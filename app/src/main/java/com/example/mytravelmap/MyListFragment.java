package com.example.mytravelmap;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyListFragment extends Fragment {
    private ListViewAdapter adapter;
    private ListView listView = null;
    private ListInterface mListInterface;

    public MyListFragment() {
    }

    public static MyListFragment newInstance() {
        Bundle args = new Bundle();
        MyListFragment fragment = new MyListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListInterface = (ListInterface) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_list, container, false);
        // 어댑터에 뷰 연결
        adapter = new ListViewAdapter();
        listView = layout.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        // 리스트뷰가 빈 경우 텍스트뷰 연결
        TextView textView = layout.findViewById(R.id.empty_text);
        listView.setEmptyView(textView);
        // List Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 item 정보 가져오기
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                mListInterface.moveInfo(item.getId());
            }
        });

        // 저장된 리스트 불러오기
        mListInterface.addFirst(adapter);

        return layout;
    }

    void addData(String img, String title, String content, LatLng latLng, String id) {
        // 어뎁터로 리스트에 데이터 추가
        adapter.addData(img, title, content, latLng, id);
        // UI 변경이므로 UI 쓰레드 상에서 실행
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    void deleteData(String id) {
        // 어뎁터로 리스트의 데이터 삭제
        adapter.deleteData(id);
        // UI 변경이므로 UI 쓰레드 상에서 실행
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public interface ListInterface {
        void moveInfo(String id);

        void addFirst(ListViewAdapter adapter);
    }
}
package com.example.mytravelmap;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        args.putString("id", "");

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);

        return fragment;
    }


    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println(getArguments().getString("id"));
    }
}
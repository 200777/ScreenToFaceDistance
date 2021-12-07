package com.example.screentofacedistance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;


public class MyFragment extends Fragment {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;


    public MyFragment() {

        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        listView = (ListView) view.findViewById(R.id.list_item);
        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, arrayList);
        listView.setAdapter(arrayAdapter);

        arrayAdapter.add("Mohammed Zeeshan Syed");
        arrayAdapter.add("Stephen Wahid");
        arrayAdapter.add("Roshan Chaudhari");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = arrayList.get(position);
                MainPage mainPage = (MainPage) getActivity();
                mainPage.f1(string);
            }
        });

        return view;
    }
}
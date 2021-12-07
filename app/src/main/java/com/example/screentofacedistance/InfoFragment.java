package com.example.screentofacedistance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;


public class InfoFragment extends Fragment {
TextView textView;
TextView message;


    public InfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);
        textView = view.findViewById(R.id.tex1);
        message = view.findViewById(R.id.message);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Bundle bundle = getArguments();
        if (bundle != null){
            String string = bundle.getString("string");
            textView.setText(string);
            if(string == "Mohammed Zeeshan Syed"){
                imageView.setImageResource(R.drawable.zeeshan);
                message.setText("Hello This is Zeeshan ...");
            }
            if(string == "Stephen Wahid"){
                imageView.setImageResource(R.drawable.stephen);
                message.setText("Hello This is Stephen ....");
            }
            if(string == "Roshan Chaudhari"){
                imageView.setImageResource(R.drawable.roshan);
                message.setText("Hello This is Roshan .....");
            }

        }
        return view;
    }
}
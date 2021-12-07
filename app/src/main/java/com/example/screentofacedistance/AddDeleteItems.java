package com.example.screentofacedistance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddDeleteItems extends AppCompatActivity {

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    EditText editText;
    Button button;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delete_items);

        listView = (ListView) findViewById(R.id.itemlist);
        editText = (EditText) findViewById(R.id.addtext);
        button =  (Button) findViewById(R.id.addbutton);

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(AddDeleteItems.this, android.R.layout.simple_list_item_multiple_choice, arrayList);
        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                arrayList.add(editText.getText().toString());
                editText.setText("");
                arrayAdapter.notifyDataSetChanged();
            }
        };
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
                int count = listView.getCount();
                for (int item = count-1; item >= 0 ; item--){
                    if(sparseBooleanArray.get(item)){
                        arrayAdapter.remove(arrayList.get(item));

                    }

                }
                sparseBooleanArray.clear();
                arrayAdapter.notifyDataSetChanged();

                return false;
            }

        });

        button.setOnClickListener(onClickListener);
        listView.setAdapter(arrayAdapter);
    }
}
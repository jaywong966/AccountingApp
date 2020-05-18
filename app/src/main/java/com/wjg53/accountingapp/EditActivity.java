package com.wjg53.accountingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private int inorout = 0,imageID = 0,itemID = 0, id = 0;
    private String time, value = null;
    ArrayList<String> list;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        BillsItem billsItem = new BillsItem();
        list = new ArrayList<String>();
        list = (ArrayList<String>)getIntent().getStringArrayListExtra("Value");
        TextView timetext = (TextView) findViewById(R.id.tv_time1);
        ImageView imageView = (ImageView) findViewById(R.id.ic_icon);
        TextView textView = (TextView) findViewById(R.id.tb_name);
        TextView textView1 = (TextView) findViewById(R.id.tv_text1);
        EditText editText = (EditText) findViewById(R.id.editText);
        itemID = Integer.parseInt(list.get(2));
        imageID = Integer.parseInt(list.get(1));
        value = list.get(3);
        inorout = Integer.parseInt(list.get(5));
        id = Integer.parseInt(list.get(0));
        timetext.setText(list.get(4));
        if (inorout == 0) {
            imageView.setImageResource(billsItem.setImage(imageID));
            textView.setText(billsItem.setItem(itemID));
        }
        else {
            imageView.setImageResource(billsItem.setinComeimages(imageID));
            textView.setText(billsItem.setinComeitems(itemID));
        }
        textView1.setText(value);
        //Toast.makeText(EditActivity.this, list.get(1)+list.get(2),Toast.LENGTH_SHORT).show();

        String[] ctype = new String[]{"Expenditure", "Income"};
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView3);
        scrollView.bringToFront();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = super.findViewById(R.id.spinner2);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String element = parent.getItemAtPosition(position).toString();// 得到spanner的值
                if(position==0){
                    inorout = 0;
                    GridView gridview = (GridView) findViewById(R.id.gridView1);
                    List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                    BillsItem billsItem = new BillsItem();
                    billsItem.getImage();
                    for (int i = 0; i < billsItem.getImage().length; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("image", billsItem.setImage(i));
                        map.put("title", billsItem.setItem(i));
                        list1.add(map);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(EditActivity.this, list1, R.layout.grid_item_layout,
                            new String[] { "title", "image" }, new int[] { R.id.text,
                            R.id.image, });
                    gridview.setHorizontalSpacing(30);
                    gridview.setAdapter(simpleAdapter);
                }
                else if(position==1){
                    inorout = 1;
                    GridView gridview = (GridView) findViewById(R.id.gridView1);
                    List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
                    //Map<String, Object> map = new HashMap<String, Object>();
                    BillsItem billsItem = new BillsItem();
                    billsItem.getImage();
                    for (int i = 0; i < billsItem.getinComeitems().length; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("image", billsItem.setinComeimages(i));
                        map.put("title", billsItem.setinComeitems(i));
                        list2.add(map);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(EditActivity.this, list2, R.layout.grid_item_layout,
                            new String[] { "title", "image" }, new int[] { R.id.text,
                            R.id.image, });
                    gridview.setHorizontalSpacing(30);
                    gridview.setAdapter(simpleAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GridView gridview = (GridView) findViewById(R.id.gridView1);
        List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
        billsItem.getImage();
        for (int i = 0; i < billsItem.getImage().length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", billsItem.setImage(i));
            map.put("title", billsItem.setItem(i));
            list3.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list3, R.layout.grid_item_layout,
                new String[] { "title", "image" }, new int[] { R.id.text,
                R.id.image, });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                textView1.setText(editText.getText());
                return false;
            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageID = position;
                itemID = position;
                if(inorout==0) {
                    imageView.setImageResource(billsItem.setImage(imageID));
                    textView.setText(billsItem.setItem(itemID));
                }
                else{
                    imageView.setImageResource(billsItem.setinComeimages(imageID));
                    textView.setText(billsItem.setinComeitems(itemID));
                }
            }
        });
        gridview.setHorizontalSpacing(30);
        gridview.setAdapter(simpleAdapter);

        ImageButton imagebutton = (ImageButton) findViewById(R.id.btn_edit);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                note.setId(id);
                note.setImageId(imageID);
                note.setInorout(inorout);
                note.setItemID(itemID);
                db=new DatabaseHelper(EditActivity.this);
                String value = editText.getText().toString();
                if (value.length()!=0) {
                    if (inorout == 0) {
                        note.setValue(-Double.valueOf(value));
                    } else {
                        note.setValue(Double.valueOf(value));
                    }
                    db.updateNote(note);
                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(EditActivity.this, "Please Enter Values", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

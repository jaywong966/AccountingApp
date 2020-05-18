package com.wjg53.accountingapp;
/*Create By WONG Yuk Kit*/
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BillAdd extends Fragment {
    private DatabaseHelper db;
    public SwipeMenuListView mListView;
    public SimpleAdapter simpleAdapter;
    private Context mContext;
    public List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    public Map<String,Object> notes = new HashMap<String, Object>();
    View view;
    Calendar ca = Calendar.getInstance();
    private int mYear = ca.get(Calendar.YEAR);
    private int mMonth = ca.get(Calendar.MONTH);
    private int mDay = ca.get(Calendar.DAY_OF_MONTH);
    String test = String.valueOf(mYear)+"-"+ String.valueOf(mMonth+1) +"-"+ String.valueOf(mDay);
    public BillAdd(){
        //empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.app_bill_add,container,false);
        db = new DatabaseHelper(getActivity());
        mContext = getActivity();
        list = db.getAllNotes();
        simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.app_bill_list_item,
                new String[] { "title", "image" ,"value","time"}, new int[] { R.id.tv_name,
                R.id.iv_icon,R.id.tv_text,R.id.tv_time });
        mListView = (SwipeMenuListView) view.findViewById(R.id.swipelistView);
        mListView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setTitle("Delete");
                // add to menu
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        //insert Edit function
                        Map map=(Map)list.get(position);
                        Iterator iterator = map.keySet().iterator();
                        long id = 0;
                        //Use iterator to iterate all map, find the value which the key is "id"
                        while (iterator.hasNext())
                        {
                            String key = (String) iterator.next();
                            Object object = map.get("id");  //store value which the key is "id" as object
                            String str1 = String.valueOf(object);
                            id = Long.valueOf(str1);
                        }
                        Intent intent = new Intent(getActivity(),EditActivity.class);
                        ArrayList<String> edits = new ArrayList<String>();
                        edits = db.getStringNote(id);
                        intent.putStringArrayListExtra("Value",edits);
                        startActivity(intent);
                        break;

                    case 1:
                        Map map1=(Map)list.get(position);
                        Iterator iterator1 = map1.keySet().iterator();
                        while (iterator1.hasNext())
                        {
                            String key = (String) iterator1.next();
                            Object object = map1.get("id");
                            String str1 = String.valueOf(object);
                            int getid = Integer.parseInt(str1);
                            db.deleteNote(getid);       //delete bill by id
                        }
                        list.remove(position);      //remove item from list
                        simpleAdapter.notifyDataSetChanged();
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updateValue(0);        //update main activity value
                        mainActivity.updateValue(1);
                        //Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
        return view;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}

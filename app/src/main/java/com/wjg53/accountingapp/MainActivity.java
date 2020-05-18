package com.wjg53.accountingapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.location.LocationListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener {

    String income = "0";
    String outcome = "0";
    String username = "Welcome back";
    Calendar ca = Calendar.getInstance();
    private int mYear = ca.get(Calendar.YEAR);
    private int mMonth = ca.get(Calendar.MONTH);
    private int mDay = ca.get(Calendar.DAY_OF_MONTH);
    String test = String.valueOf(mYear)+"-"+ String.valueOf(appendZero(mMonth+1)) +"-"+ String.valueOf(appendZero(mDay));
    private DatabaseHelper db;
    private int imageID = 0;
    private int itemID = 0;
    private int inorout = 0;
    SoundPool sp;
    HashMap<Integer, Integer> hm;
    int currStaeamId;

    public String appendZero (int mMonth) {
        if (mMonth < 10) {
            return '0' + String.valueOf(mMonth);
        } else {
            return String.valueOf(mMonth);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Money Keeper");
        updateValue(0);     //update expenditure value
        updateValue(1);     //update income value
        initSoundPool();        //initialize sound pool

        //ImageButton
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.btn_choose);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
                layout.setPanelState(PanelState.EXPANDED);      //Open slide up panel
            }
        });
        ImageButton button = (ImageButton) findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText);
                String value = editText.getText().toString();       //get value from text box
                long id = 0;
                db=new DatabaseHelper(MainActivity.this);
                if(value.length() != 0) {
                    if(inorout == 0){
                        id = db.insertNote(itemID, imageID, Double.valueOf(value) ,inorout);    //insert item id, image id, value, boolean value of cost or in
                    }
                    else if(inorout == 1) {
                        id = db.insertInNote(itemID, imageID, Double.valueOf(value), inorout);
                        playSound(1, 0);
                    }
                    /*Use fragment manager to add new list item to list which in the fragment*/
                    BillAdd billAdd = (BillAdd)getSupportFragmentManager().findFragmentById(R.id.fragment);
                    billAdd.list.add(0,db.getNote(id));
                    billAdd.simpleAdapter.notifyDataSetChanged();
                    editText.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    updateValue(inorout);       //update screen by inorout
                    //finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Please Enter Values", Toast.LENGTH_SHORT).show();
                    /*Stop input method*/
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        });

        //DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);     //create navigation View on click listener

        //Spinner
        String[] ctype = new String[]{"Expenditure", "Income"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = super.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListenerImpl());        //create spinner on selected listener


        //GridView
        GridView gridview = (GridView) findViewById(R.id.gridView);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BillsItem billsItem = new BillsItem();
        billsItem.getImage();
        for (int i = 0; i < billsItem.getImage().length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", billsItem.setImage(i));
            map.put("title", billsItem.setItem(i));
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.grid_item_layout,
                new String[] { "title", "image" }, new int[] { R.id.text,
                R.id.image, });
        gridview.setOnItemClickListener((AdapterView.OnItemClickListener) this); //Create grid view on item click listener
        gridview.setHorizontalSpacing(30);
        gridview.setAdapter(simpleAdapter);
    }

    private class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String element = parent.getItemAtPosition(position).toString();
            if(position==0){
                inorout = 0;        //If position == 0, boolean value of cost or income = 0 (expenditure == 0)
                /*Reset expenditure adapter*/
                GridView gridview = (GridView) findViewById(R.id.gridView);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                BillsItem billsItem = new BillsItem();
                billsItem.getImage();
                for (int i = 0; i < billsItem.getImage().length; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("image", billsItem.setImage(i));
                    map.put("title", billsItem.setItem(i));
                    list.add(map);
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.grid_item_layout,
                        new String[] { "title", "image" }, new int[] { R.id.text,
                        R.id.image, });
                gridview.setHorizontalSpacing(30);
                gridview.setAdapter(simpleAdapter);
            }
            else if(position==1){
                inorout = 1;        //If position == 1, boolean value of cost or income = 1(income == 1)
                /*Reset to income adapter*/
                GridView gridview = (GridView) findViewById(R.id.gridView);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                //Map<String, Object> map = new HashMap<String, Object>();
                BillsItem billsItem = new BillsItem();
                billsItem.getImage();
                for (int i = 0; i < billsItem.getinComeitems().length; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("image", billsItem.setinComeimages(i));
                    map.put("title", billsItem.setinComeitems(i));
                    list.add(map);
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.grid_item_layout,
                        new String[] { "title", "image" }, new int[] { R.id.text,
                        R.id.image, });
                gridview.setHorizontalSpacing(30);
                gridview.setAdapter(simpleAdapter);
            }
            Toast.makeText(MainActivity.this,
                    element,Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }


    //GridView  OnclickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        imageID = position;     //assign position value to image id
        itemID = position;      //assign position value to item id
        String show = String.valueOf(itemID);
        Toast.makeText(this, show, Toast.LENGTH_SHORT).show();
        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        layout.setPanelState(PanelState.COLLAPSED);
    }

    /*Press back to close slide up panel*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        SlidingUpPanelLayout  layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if(layout.getPanelState() == PanelState.EXPANDED) {
            layout.setPanelState(PanelState.COLLAPSED);
            if (drawer.isDrawerOpen(GravityCompat.START))
               drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    /*Close keyboard by touch empty place*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            SlidingUpPanelLayout  layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.month_toolbar, menu);
        TextView user = (TextView)findViewById(R.id.userbar);
        user.setText(username);
        return true;
    }


    /*Function of update screen value*/
    public void updateValue(int choice){
        db=new DatabaseHelper(MainActivity.this);
        TextView t_outcome = (TextView) findViewById(R.id.t_outcome);
        TextView t_income = (TextView) findViewById(R.id.t_income);
        if(choice == 0) {
            outcome = db.countValue(choice);    //return value from database
            t_outcome.setText(outcome);
        }
        else if(choice == 1){
            income = db.countValue(choice);
            t_income.setText(income);
        }
        String remain = String.valueOf(Double.valueOf(income)+Double.valueOf(outcome));     //calculate remain value
        TextView t_total = (TextView) findViewById(R.id.t_total);
        t_total.setText(remain);
        TextView header_money = (TextView) findViewById(R.id.header_money);
        String currentValue = "Expenditure:"+db.countCurrenDayValue(0,test)+"   Income:"+db.countCurrenDayValue(1,test);
        header_money.setText(currentValue);
        TextView header_date = (TextView) findViewById(R.id.header_date);
        header_date.setText(test);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DatePickerDialog on Toolbar
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        test = String.valueOf(mYear)+"-"+ String.valueOf(mMonth+1) +"-"+ String.valueOf(mDay);      //get the date which be chosen
                        Toast.makeText(MainActivity.this, test,Toast.LENGTH_SHORT).show();
                        BillAdd billAdd = (BillAdd)getSupportFragmentManager().findFragmentById(R.id.fragment);
                        db = new DatabaseHelper(MainActivity.this);
                        billAdd.list = db.getbillbydate(test);      //get all bill by date
                        /*Refresh fragment's list*/
                        billAdd.simpleAdapter = new SimpleAdapter(MainActivity.this, billAdd.list, R.layout.app_bill_list_item,
                                new String[] { "title", "image" ,"value","time"}, new int[] { R.id.tv_name,
                                R.id.iv_icon,R.id.tv_text,R.id.tv_time });
                        billAdd.mListView.setAdapter(billAdd.simpleAdapter);
                        billAdd.simpleAdapter.notifyDataSetChanged();
                        updateValue(0);     //update screen value
                        updateValue(1);
                    }
                },
                mYear, mMonth, mDay);
        DatePicker dp = datePickerDialog.getDatePicker();
        datePickerDialog.show();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_chart) {
            //Start chart activity
            Intent intent = new Intent(MainActivity.this,ChartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_friend){
            //Start share function
            db = new DatabaseHelper(this);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"I spent a total of "+db.countCurrenDayValue(0,test)+" today, how about you");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"Choose"));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void playSound(int sound, int loop) {
        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        currStaeamId = sp.play(hm.get(sound), volume, volume, 1, loop, 1.0f);
    }

    private void initSoundPool() {
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        hm = new HashMap<Integer, Integer>();
        hm.put(1, sp.load(this, R.raw.message, 1));
    }


}

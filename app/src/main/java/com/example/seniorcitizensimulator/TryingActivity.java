package com.example.seniorcitizensimulator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TryingActivity extends AppCompatActivity implements View.OnClickListener {

    static ArrayList<String> nameList = new ArrayList<>();
    static ArrayList<String> skypeIDList = new ArrayList<>();
    static ArrayList<String> colorCode = new ArrayList<>();
    static ArrayList<String> idArray = new ArrayList<>();

    static double linearLayout_weight=0, Running_task=0;
    static String[] ColorsArray = new String []{"#0EAFFE"};
    static int Looping, universal, nameList_int, button_number, five_columns=0, three_cloumns =0, four_columns=0;
    LinearLayout Parent_linear;
    androidx.appcompat.widget.Toolbar toolbar;
    final Handler ha=new Handler();
    LinearLayout parent;
    Thread thread_Start;
    int textSize=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trying);



        if(isMyServiceRunning(SkypeService.class)){

            Log.e("Service", "Running ---------");
            stopService(new Intent(TryingActivity.this, SkypeService.class));
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Parent_linear = findViewById(R.id.parent_linear);

        StartServerFile();

    }


    public void StartServerFile()
    {

        String url = "https://pula.tech/connectapi/AndroidApi/listChildUsers.php?userId=2";

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output)
            {

                try
                {
                    //Log.e("API", output);
                    nameList.clear();
                    skypeIDList.clear();
                    colorCode.clear();
                    idArray.clear();
                    ConvertFromJSON(output);
                    PostExecute();

               //     ha.removeCallbacksAndMessages(null);
                 //   ha.removeCallbacks(null);

                //    RunnableFuntion();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    @SuppressLint("LongLogTag")
    public void PostExecute()
    {

        Parent_linear.removeAllViews();

        nameList_int =0;
        linearLayout_weight = 0;
        button_number = nameList.size();

        Log.e("NameList", String.valueOf(nameList.size()));

        /*if(nameList.size() == 1 || nameList.size() == 2) {

            linearLayout_weight = 120;
            Looping = 1;
        }

        else if(nameList.size() >= 3 && nameList.size() < 5) {

            linearLayout_weight = 60;
            Looping = 2;
        }

        else if(nameList.size() >= 5 && nameList.size() < 7) {

            linearLayout_weight = 40;
            Looping = 3;

        }

        else if(nameList.size() >= 7 && nameList.size() < 9) {

            linearLayout_weight = 30;
            Looping = 4;

        }

        else if(nameList.size() >= 9 && nameList.size() < 11) {

            linearLayout_weight = 24;
            Looping = 5;

        }

        else if(nameList.size() >= 11 && nameList.size() < 13) {

            linearLayout_weight = 20;
            Looping = 6;

        }

        else if(nameList.size() >= 13 && nameList.size() < 15) {

            linearLayout_weight = 17.14;
            Looping = 7;

        }

        else if(nameList.size() >= 15 && nameList.size() < 17) {

            linearLayout_weight = 15;
            Looping = 8;

        }

        else if(nameList.size() >= 17 && nameList.size() < 19) {

            linearLayout_weight = 13.33;
            Looping = 9;

        }

        else if(nameList.size() >= 19 && nameList.size() < 21) {

            linearLayout_weight = 12;
            Looping = 10;

        }

        else if(nameList.size() >= 21 && nameList.size() < 23) {

            linearLayout_weight = 10.9;
            Looping = 11;

        }

        else if(nameList.size() >= 23 && nameList.size() < 25) {

            linearLayout_weight = 10;
            Looping = 12;

        }

        else if(nameList.size() >= 25 && nameList.size() < 27) {

            linearLayout_weight = 9.23;
            Looping = 13;

        }

        else if(nameList.size() >= 27 && nameList.size() < 29) {

            linearLayout_weight = 8.57;
            Looping = 14;

        }*/

        if(nameList.size() < 11){

            Log.e("Value of looping", "2");
            linearLayout_weight = 60;
            Looping = 2;
            textSize=23;

        }

        else if(nameList.size() >= 11 && nameList.size() < 16){

            Log.e("Value of looping", "3");
            linearLayout_weight = 40;
            Looping = 3;
            textSize=20;

        }

        else if(nameList.size() >= 16 && nameList.size() < 21){

            Log.e("Value of looping", "4");
            linearLayout_weight = 30;
            Looping = 4;
            textSize=18;

        }

        else{

            linearLayout_weight = 24;
            Looping = 5;
            textSize=15;

        }

        universal=0;
        for(int j=0; j< Looping; j++) {

            parent = new LinearLayout(this);
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float) linearLayout_weight));
            parent.setWeightSum(120);
            parent.setOrientation(LinearLayout.HORIZONTAL);
            parent.setPadding(30, 20, 30, 20);
            //parent.removeAllViews();

            Log.e("Size Looping", String.valueOf(Looping));

            if(button_number % 6 ==0 && nameList.size() != 6 && nameList.size() != 12
                    && nameList.size() != 18 && nameList.size() != 24){

                Log.e("Divisible by 6", String.valueOf(button_number));

                for(int i=0; i<6; i++){

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 20);
                    params.setMargins(15, 0, 15, 0);
                    contacts.setLayoutParams(params);
                    Log.e("Name List Integer", String.valueOf(nameList_int));
                    contacts.setText(nameList.get(nameList_int));
                    //contacts.setTextSize((17-Looping)*4);
                    contacts.setTextSize(textSize);
                    contacts.setTextColor(Color.WHITE);

                    contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                    contacts.setId(nameList_int);
                    contacts.setOnClickListener(this);
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;

                }

            }

            else if(button_number % 5 ==0 && button_number % 6 != 0 && nameList.size() != 5) {

                Log.e("Divisible by 5", String.valueOf(button_number));

                for(int i=0; i<5; i++){

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 24);
                    params.setMargins(15, 0, 15, 0);
                    contacts.setLayoutParams(params);
                    Log.e("Name List Integer", String.valueOf(nameList_int));
                    contacts.setText(nameList.get(nameList_int));
                    //contacts.setTextSize((17-Looping)*4);
                    contacts.setTextSize(textSize);
                    contacts.setTextColor(Color.WHITE);

                    contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                    contacts.setId(nameList_int);
                    contacts.setOnClickListener(this);
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;

                }

            }

            else if(button_number % 4 ==0 && button_number % 6 != 0 && nameList.size() != 4 && nameList.size()!= 16) {

                Log.e("Divisible by 4", String.valueOf(button_number));

                for(int i=0; i<4; i++){

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 30);
                    params.setMargins(15, 0, 15, 0);
                    contacts.setLayoutParams(params);
                    Log.e("Name List Integer", String.valueOf(nameList_int));
                    contacts.setText(nameList.get(nameList_int));
                    contacts.setTextSize(textSize);
                    //contacts.setTextSize((17-Looping)*4);
                    contacts.setTextColor(Color.WHITE);

                    contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                    contacts.setId(nameList_int);
                    contacts.setOnClickListener(this);
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;

                }

            }

            else if(button_number % 3 ==0 && button_number % 6 != 0 && nameList.size() != 3 && nameList.size()!= 21) {

                Log.e("Divisible by 3", String.valueOf(button_number));

                for(int i=0; i<3; i++){

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 40);
                    params.setMargins(15, 0, 15, 0);
                    contacts.setLayoutParams(params);
                    Log.e("Name List Integer", String.valueOf(nameList_int));
                    contacts.setText(nameList.get(nameList_int));
                    contacts.setTextSize(textSize);
                    //contacts.setTextSize((17-Looping)*4);
                    contacts.setTextColor(Color.WHITE);

                    contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                    contacts.setId(nameList_int);
                    contacts.setOnClickListener(this);
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;

                }

            }

            else{

                if(button_number < 7){

                    Log.e("Less than 7", String.valueOf(button_number));

                    if(button_number % 3 ==0 && nameList.size() != 3){

                        Log.e("Less than 7 and divisible by 3", String.valueOf(button_number));

                        for(int i=0; i<3; i++){

                            Button contacts = new Button(this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 60);
                            params.setMargins(15, 0, 15, 0);
                            contacts.setLayoutParams(params);
                            Log.e("Name List Integer", String.valueOf(nameList_int));
                            contacts.setText(nameList.get(nameList_int));
                            contacts.setTextSize(textSize);
                            //contacts.setTextSize((17-Looping)*4);
                            contacts.setTextColor(Color.WHITE);

                            contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                            contacts.setId(nameList_int);
                            contacts.setOnClickListener(this);
                            universal++;
                            parent.addView(contacts);
                            nameList_int ++;
                            button_number = button_number - 1;

                        }

                    }

                    else if(button_number % 2 ==0){

                        Log.e("Divisible by 2", String.valueOf(button_number));

                        for(int i=0; i<2; i++){

                            Button contacts = new Button(this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 60);
                            params.setMargins(15, 0, 15, 0);
                            contacts.setLayoutParams(params);
                            Log.e("Name List Integer", String.valueOf(nameList_int));
                            contacts.setText(nameList.get(nameList_int));
                            contacts.setTextSize(textSize);
                            //contacts.setTextSize((17-Looping)*4);
                            contacts.setTextColor(Color.WHITE);

                            contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                            contacts.setId(nameList_int);
                            contacts.setOnClickListener(this);
                            universal++;
                            parent.addView(contacts);
                            nameList_int ++;
                            button_number = button_number - 1;

                        }

                    }

                    else {

                        Log.e("Divisible by 1", String.valueOf(button_number));

                        Button contacts = new Button(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 120);
                        params.setMargins(15, 0, 15, 0);
                        contacts.setLayoutParams(params);
                        contacts.setText(nameList.get(nameList_int));
                        contacts.setTextSize(textSize);
                        //contacts.setTextSize((17-Looping)*4);
                        contacts.setTextColor(Color.WHITE);

                        contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                        universal++;
                        parent.addView(contacts);
                        nameList_int ++;
                        button_number = button_number - 1;

                    }

                }

                else if(button_number > 20 && button_number < 25 && five_columns == 0 && four_columns ==0 && three_cloumns ==0){

                    Log.e("Less than 25", String.valueOf(button_number));

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 120);
                    params.setMargins(15, 0, 15, 0);
                    contacts.setLayoutParams(params);
                    contacts.setText(nameList.get(nameList_int));
                    contacts.setTextSize(textSize);
                    //contacts.setTextSize((17-Looping)*4);
                    contacts.setTextColor(Color.WHITE);

                    contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;
                    five_columns++;

                }

                else if(button_number > 10 && button_number < 13 && five_columns == 0 && four_columns ==0 && three_cloumns ==0){

                    Log.e("Less than 13", String.valueOf(button_number));

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 120);
                    params.setMargins(15, 0, 15, 0);
                    contacts.setLayoutParams(params);
                    contacts.setText(nameList.get(nameList_int));
                    contacts.setTextSize(textSize);
                    //contacts.setTextSize((17-Looping)*4);
                    contacts.setTextColor(Color.WHITE);

                    contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;
                    three_cloumns++;

                }

                else if(button_number > 15 && button_number < 18 && five_columns == 0 && four_columns ==0 && three_cloumns ==0){

                    Log.e("Less than 18", String.valueOf(button_number));

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 120);
                    params.setMargins(15, 0, 15, 0);
                    contacts.setLayoutParams(params);
                    contacts.setText(nameList.get(nameList_int));
                    contacts.setTextSize(textSize);
                    //contacts.setTextSize((17-Looping)*4);
                    contacts.setTextColor(Color.WHITE);

                    contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;
                    four_columns++;

                }

                else {

                    Log.e("Compulsary by 6", String.valueOf(button_number));

                    for(int i=0; i<6; i++){

                        Button contacts = new Button(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 20);
                        params.setMargins(15, 0, 15, 0);
                        contacts.setLayoutParams(params);
                        Log.e("Name List Integer", String.valueOf(nameList_int));
                        contacts.setText(nameList.get(nameList_int));
                        contacts.setTextSize(textSize);
                        //contacts.setTextSize((17-Looping)*4);
                        contacts.setTextColor(Color.WHITE);

                        contacts.setBackgroundColor(Color.parseColor(colorCode.get(universal)));
                        contacts.setId(nameList_int);
                        contacts.setOnClickListener(this);
                        universal++;
                        parent.addView(contacts);
                        nameList_int ++;
                        button_number = button_number - 1;

                    }

                }

            }

            /*if(button_number % 2 ==0){

                for(int i=0; i<2; i++){

                    Button contacts = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 50);
                    params.setMargins(50, 0, 50, 0);
                    contacts.setLayoutParams(params);
                    Log.e("Name List Integer", String.valueOf(nameList_int));
                    contacts.setText(nameList.get(nameList_int));
                    contacts.setTextSize((17-Looping)*4);
                    contacts.setTextColor(Color.WHITE);
                    if(universal == 5)
                        universal = 0;
                    contacts.setBackgroundColor(Color.parseColor(ColorsArray[universal]));
                    contacts.setId(nameList_int);
                    contacts.setOnClickListener(this);
                    universal++;
                    parent.addView(contacts);
                    nameList_int ++;
                    button_number = button_number - 1;

                }

            }

            else {

                Button contacts = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 50);
                params.setMargins(50, 0, 50, 0);
                contacts.setLayoutParams(params);
                contacts.setText(nameList.get(nameList_int));
                contacts.setTextSize((17-Looping)*4);
                contacts.setTextColor(Color.WHITE);
                if(universal == 5)
                    universal = 0;
                contacts.setBackgroundColor(Color.parseColor(ColorsArray[universal]));
                universal++;
                parent.addView(contacts);
                nameList_int ++;
                button_number = button_number - 1;

            }*/

            Parent_linear.addView(parent);

        }

        /*thread_Start = new Thread()
        {
            public void run() {

                if(!thread_Start.isInterrupted()) {

                    try {

                        Thread.sleep(15000);
                        Log.e("Thread Started", "Thanks");
                        StartServerFile();
                        stopThread();

                    }
                    catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                }
            }
        };

        thread_Start.start();*/


    }

    private void ConvertFromJSON(String json)
    {
        try
        {

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                nameList.add(obj.getString("firstName"));
                colorCode.add(obj.getString("colorCode"));
                idArray.add(obj.getString("id"));
                skypeIDList.add(obj.getString("skypeId"));
                Log.e("UniversalInConvertJSON",colorCode.get(i)+" - "+universal);

            }

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /*public void Toolbar(){

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View ViewInflator;
        ViewInflator = inflater.inflate(R.layout.activity_trying, null);

        toolbar = ViewInflator.findViewById(R.id.toolbarId);
        toolbar.setTitle("Meety");
        Log.e("Got into Toolbar", "YEs");
        toolbar.inflateMenu(R.menu.action_bar_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.wifi)
                {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }

                return false;

            }
        });

    }*/

    @Override
    public void onClick(View v) {

        //stopThread();

        if (nameList.get(v.getId()).equals("Support")) {

            Log.e("Clicked", String.valueOf(v.getId()));
            startActivity(new Intent(getApplicationContext(), SupportActivity.class));

        } else {

            Log.e("Clicked", String.valueOf(v.getId()));

            Intent serviceIntent = new Intent(getApplicationContext(), SkypeService.class);
            serviceIntent.putExtra("CallerID", nameList.get(v.getId()));
            Log.e("Service Name", nameList.get(v.getId()));
            startService(serviceIntent);

            try {

            /*Intent sky = new Intent("android.intent.action.VIEW");
            sky.setData(Uri.parse("skype:" + skypeIDList.get(v.getId())));
            startActivity(sky);*/

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("skype:" + skypeIDList.get(v.getId())));
                startActivity(i);


            } catch (ActivityNotFoundException e) {
                Log.e("SKYPE CALL", "Skype failed", e);
            }

        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.wifi){

            Log.e("Yes", "Wifi has been tapped");

        }

        return true;

    }*/

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopThread(){

        try{

            thread_Start.stop();
            thread_Start.destroy();
            thread_Start.interrupt();

        }
        catch (Exception Ex){}

        Log.e("Thread Stopped", "Thanks");

    }


 }

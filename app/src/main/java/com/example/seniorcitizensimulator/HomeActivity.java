package com.example.seniorcitizensimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    static ArrayList<NameListModel> nameList = new ArrayList<>();
    static ArrayList<NameListModel> results = new ArrayList<>();
    static ArrayList<NameListModel> ColorList = new ArrayList<>();
    static ArrayList<NameListModel> Color_results = new ArrayList<>();
    private HomeRecyclerAdapter mAdapter;
    RecyclerView recyclerView;
    static int JSONLength;
    static int universal;
    static String[] FirstName = new String[1000];
    static String[] LastName = new String[1000];
    static String[] ColorsArray = new String []{"#b31302", "#b06402", "#24c704", "#0581b3", "#0581b3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        //overLayButton();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        /*recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }

        StartServerFile();
    }


    public void StartServerFile()
    {

        String url = "https://pula.tech/connectapi/AndroidApi/listChildUsers.php?userId=2" ;

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output)
            {

                try
                {
                    ConvertFromJSON(output);
                    PostExecute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    public void PostExecute()
    {
        nameList.clear();
        universal = 0;

        for (int i=0; i<JSONLength; i++) {

            nameList = GetListDetailing();

        }

        mAdapter = new HomeRecyclerAdapter(nameList);
        GridLayoutManager gridLayoutManager;

        if(nameList.size() <= 1)

            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);


        else

            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    private void ConvertFromJSON(String json)
    {
        try
        {
            //Log.e("JSON Length------------", json);
            JSONArray jsonArray = new JSONArray(json);
            JSONLength = jsonArray.length();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                FirstName[i]=obj.getString("firstName");
                LastName[i] = obj.getString("lastName");

            }

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static ArrayList<NameListModel> GetListDetailing()
    {

        NameListModel modelclass = new NameListModel();                                     // Looping will be extended till JSONLength
        //modelclass.setName(FirstName[i]+" "+LastName[i]);
        modelclass.setName(FirstName[universal]);
        results.add(modelclass);
        universal++;

        return results;
    }

    private static ArrayList<NameListModel> GetColorListing()
    {

        NameListModel modelclass = new NameListModel();                                     // Looping will be extended till JSONLength
        //modelclass.setName(FirstName[i]+" "+LastName[i]);
        modelclass.setName(ColorsArray[universal]);
        Color_results.add(modelclass);
        universal++;

        return Color_results;
    }


}
package com.example.seniorcitizensimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class SupportActivity extends AppCompatActivity {

    TextView tvWebsite,tvEmail,tvSkypeId,tvPhone,tvError;
    Button wifiBtn,backBtn;
    //TextView tvWebsite=findViewById(R.id.tv);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        tvWebsite=findViewById(R.id.tvWebsite);
        tvEmail=findViewById(R.id.tvEmail);
        tvSkypeId=findViewById(R.id.tvSkypeId);
        tvPhone=findViewById(R.id.tvPhone);
        tvError=findViewById(R.id.tvError);
        wifiBtn=findViewById(R.id.wifi_settings);
        backBtn=findViewById(R.id.back_button);
        Intent in =getIntent();
        String error = in.getStringExtra("error");
        tvError.setText(error);
        wifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SupportActivity.this,TryingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        StartServerFile();

    }
    public void StartServerFile()
    {

        String url = "https://meety.se/api/AndroidApi/supportInformation.php";

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output)
            {

                try
                {
                    ConvertFromJSON(output);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }
    private void ConvertFromJSON(String json)
    {
        try
        {

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                tvEmail.setText(obj.getString("email"));
                tvWebsite.setText(obj.getString("website"));
                tvSkypeId.setText(obj.getString("skypeId"));
                tvPhone.setText(obj.getString("phone"));
            }

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
package com.example.seniorcitizensimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LoginActivity extends AppCompatActivity {

    Button Login;
    EditText Username, Password;
    boolean notification, overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(isMyServiceRunning(SkypeService.class)){

            Log.e("Service", "Running ---------");
            stopService(new Intent(LoginActivity.this, SkypeService.class));

        }

        if (Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName()))
        {
            notification = true;

        } else {
            //service is not enabled try to enabled by calling...
            Intent revert = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            revert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(revert);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Settings.canDrawOverlays(this)) {

                overlay = true;

            }

            else {

                int REQUEST_CODE = 101;
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                myIntent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(myIntent, REQUEST_CODE);

            }

        }


        if(notification && overlay){

            if(file_retreive().equals("error") == true ||file_retreive().equals("") == true || file_retreive() == null){

            }
            else{

                Intent success = new Intent(LoginActivity.this, TryingActivity.class);
                success.putExtra("Login", file_retreive());
                startActivity(success);

                finish();

            }

            Password = findViewById(R.id.password);
            Username = findViewById(R.id.username);
            Login = findViewById(R.id.login);

            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Username.getText().toString().trim().equalsIgnoreCase("")){

                        Username.setError("This field can not be empty");

                    }

                    else if(Password.getText().toString().trim().equalsIgnoreCase("")){

                        Password.setError("This field can not be empty");

                    }

                    else

                        StartProcessLogin();


                }
            });

        }

    }

    public void StartProcessLogin(){
        //progdialog();
        String url = "https://pula.tech/connectapi/AndroidApi/login.php?emailId="+Username.getText().toString()+"&password="+Password.getText().toString();

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
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

    public void ConvertFromJSON(String json){

        try
        {
            Log.e("Status", json);

            if(json.contains("Success")){

                //JSONArray jsonArray = new JSONArray(json);
                JSONObject object = new JSONObject(json);
                JSONArray jsonArray  = object.getJSONArray("users");
                Log.e("JSONARRAY", jsonArray.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    file_write_url(obj.getString("id"));
                    file_write_firstname(obj.getString("firstName"));

                    Intent main = new Intent(getApplicationContext(), TryingActivity.class);
                    main.putExtra("Login", Username.getText().toString());
                    startActivity(main);
                    finish();
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void file_write_url(String username)
    {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("UserID", Context.MODE_PRIVATE);
            outputStream.write(username.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String file_retreive()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput("UserID");
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int n;
            while (( n = inputStream.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }

            inputStream.close();
            return fileContent.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "error";
        }
    }

    private void file_write_firstname(String username)
    {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("FirstName", Context.MODE_PRIVATE);
            outputStream.write(username.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
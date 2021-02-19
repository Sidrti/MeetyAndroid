package com.example.seniorcitizensimulator;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)

public class SkypeService extends NotificationListenerService {

    boolean callPicked=false;
    WindowManager wm, wm_name, wm_ringing;
    View myView, myView_name, myView_ringing;
    Thread thread;
    int notification=0;
    int LAYOUT_FLAG;
    String CallerID;

    public SkypeService() {


    }
    @Override
    public void onStart(Intent intent, int startId) {

        Log.e("OnStart", "Service started");

        CallerID = intent.getStringExtra("CallerID");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }


         /*WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSPARENT);

        params.gravity = Gravity.BOTTOM;
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myView = inflater.inflate(R.layout.overlay_layout, null);
        Button overlay = myView.findViewById(R.id.fabHead);


        try {

            wm.addView(myView, params);

        }catch (Exception exception){
            exception.getStackTrace();
        }*/                                                                                           // End of one First Window Manager


        /*overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (myView != null)
                        wm.removeView(myView);

                    if (myView_name != null)
                        wm_name.removeView(myView_name);

                    if(myView_ringing!=null)
                        wm_ringing.removeView(myView_ringing);

                }
                catch (Exception Ex){}

                Log.d("TAG", "touch me");

                Intent revert = new Intent(getApplicationContext(), LoginActivity.class);
                revert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(revert);

            }
        });*/
        threadCheckUnPickedCall();
        createRingingOverlay();


        super.onStart(intent, startId);
    }
    public void threadCheckUnPickedCall() {
        thread = new Thread()
        {

            @Override
            public void run() {

                if(!thread.isInterrupted()) {

                    Log.e("Thread Service Started", "Thanks");

                    try
                    {
                        Thread.sleep(36000);
                        Intent revert = new Intent(getApplicationContext(), LoginActivity.class);
                        revert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(revert);

                        try
                        {
                            if (myView_ringing != null)
                                wm_ringing.removeView(myView_ringing);

                            if (myView_name != null)
                                wm_name.removeView(myView_name);
                        }
                        catch (Exception e){e.printStackTrace();}


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    KillSkypeProcess();
                }
            }
        };
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public void createRingingOverlay(){
        WindowManager.LayoutParams params_ringing = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSPARENT);

        params_ringing.gravity = Gravity.TOP;
        wm_ringing = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater_name = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myView_ringing = inflater_name.inflate(R.layout.overlay_ringing, null);
        Button overlay_name = myView_ringing.findViewById(R.id.fabHead);
        Button overlay_endcall = myView_ringing.findViewById(R.id.fabEndBtn);
        overlay_name.setText("Calling "+CallerID+"..");

        overlay_endcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    if (myView != null)
                        wm.removeView(myView);

                    if (myView_name != null)
                        wm_name.removeView(myView_name);

                    if(myView_ringing!=null)
                        wm_ringing.removeView(myView_ringing);

                }
                catch (Exception Ex){}
                KillSkypeProcess();
                Log.d("TAG", "touch me");

                Intent revert = new Intent(getApplicationContext(), LoginActivity.class);
                revert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(revert);
                stopThread();
            }
        });

        try {
            wm_ringing.addView(myView_ringing, params_ringing);
        }
        catch (Exception exception){
        }

    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.e("Great", "Service created");

    }

    @Override
    public IBinder onBind(Intent intent) {

        return super.onBind(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("CALLed","Service Stopped");

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();
        Log.e("TAG", "onNotificationPosted " + packageName);

        //stopThread();

        if(packageName != null && packageName.equals("com.skype.raider")) {

            try {

                if (myView_ringing != null){
                    Log.e("TAG", "Noti Ringing Posted");
                    wm_ringing.removeView(myView_ringing);
                    OnPickupSkype();
                }


            } catch (Exception Ex) {
            }


        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

        String packageName = sbn.getPackageName();

        Log.e("TAG", "onNotificationRemoved " + packageName);

        if(packageName.equals("com.skype.raider")) {

            try {
                if (myView_name != null)
                    wm_name.removeView(myView_name);

                if(myView_ringing!=null)
                    wm_ringing.removeView(myView_ringing);

            }
            catch (Exception e){
                e.printStackTrace();
            }

            Intent revert = new Intent(this, LoginActivity.class);
            revert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(revert);
            stopThread();

        }

    }


    private void killAppBypackage(String packageTokill){

        Log.e("Killing", "Started");
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);


        ActivityManager mActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String myPackage = getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {

            if(packageInfo.packageName.equals(packageTokill)) {
                Log.e("PackageKill","Kill - "+packageTokill);
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);

            }

        }

        Log.e("Killing", "Executed");

    }
    public void KillSkypeProcess() {
        List<ActivityManager.RunningAppProcessInfo> processes;
        ActivityManager amg;
        amg = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Log.e("Proceess",amg+"-------------------");
// list all running process
        processes = amg.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes) {
          Log.e("Proceess",info.processName);
          if(info.processName.equals("com.skype.raider")){
              // kill selected process
               android.os.Process.killProcess(info.pid);
               android.os.Process.sendSignal(info.pid, android.os.Process.SIGNAL_KILL);
               amg.killBackgroundProcesses(info.processName);
               Log.e("Proceess","Completed..");
            }
        }
    }

    public void OnPickupSkype(){
        stopThread();

        try {
            wm_name.removeView(myView_name);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

            WindowManager.LayoutParams params_name = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSPARENT);

            params_name.gravity = Gravity.TOP;
            wm_name = (WindowManager) getSystemService(WINDOW_SERVICE);
            LayoutInflater inflater_name = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            myView_name = inflater_name.inflate(R.layout.overlay_layout_for_name, null);
            Button overlay_name = myView_name.findViewById(R.id.fabHead);
            Button overlay_endCall = myView_name.findViewById(R.id.fabEnd);
            overlay_name.setText("Calling " + CallerID + " ....");

            wm_name.addView(myView_name, params_name);

            overlay_endCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        if (myView_name != null)
                            wm_name.removeView(myView_name);

                        if (myView_ringing != null)
                            wm_ringing.removeView(myView_ringing);

                    } catch (Exception Ex) {
                    }
                    KillSkypeProcess();
                    Log.d("TAG", "touch me");

                    Intent revert = new Intent(getApplicationContext(), LoginActivity.class);
                    revert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplicationContext().startActivity(revert);

                }
            });


    }

    public void stopThread(){

        try{
            thread.interrupt();
        }
        catch (Exception e){e.printStackTrace();}
    }

}
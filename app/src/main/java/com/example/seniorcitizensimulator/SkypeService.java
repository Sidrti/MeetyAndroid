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

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)

public class SkypeService extends NotificationListenerService {

    boolean mSkypeConnected;
    WindowManager wm, wm_name;
    View myView, myView_name;
    Thread thread;
    int notification=0;

    public SkypeService() {


    }



    @Override
    public void onStart(Intent intent, int startId) {

        Log.e("OnStart", "Service started");

        String CallerID = intent.getStringExtra("CallerID");

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
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
        ImageButton overlay = myView.findViewById(R.id.fabHead);


        try {

            wm.addView(myView, params);

        }catch (Exception exception){


        }                                                                                           // End of one First Window Manager

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
        overlay_name.setText("Calling "+CallerID+" ....");

        try {

            wm_name.addView(myView_name, params_name);

        }catch (Exception exception){


        }

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (myView != null)
                        wm.removeView(myView);

                    if (myView_name != null)
                        wm_name.removeView(myView_name);

                }
                catch (Exception Ex){}

                Log.d("TAG", "touch me");

                Intent revert = new Intent(getApplicationContext(), LoginActivity.class);
                revert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(revert);

            }
        });

        /*thread = new Thread()
        {

            @Override
            public void run() {

                if(!thread.isInterrupted()) {

                    Log.e("Thread Service Started", "Thanks");

                    try {

                        Thread.sleep(36000);
                        killAppBypackage("com.skype.m2");
                        Intent revert = new Intent(getApplicationContext(), LoginActivity.class);
                        revert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(revert);

                        try {

                            if (myView != null)
                                wm.removeView(myView);

                            if (myView_name != null)
                                wm_name.removeView(myView_name);


                        }
                        catch (Exception Ex){}

                        stopThread();


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        thread.start();*/

        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.e("Great", "Service created");
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.NOTIFICATION_LISTENER");
        LocalBroadcastManager.getInstance(this).registerReceiver(nlServiceReceiver, filter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(nlServiceReceiver);


    }

    @Override
    public IBinder onBind(Intent intent) {

        return super.onBind(intent);

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();
        Log.e("TAG", "onNotificationPosted " + packageName);

        stopThread();

        if(packageName != null && packageName.equals("com.skype.m2")) {

            Intent intent = new Intent("com.example.NOTIFICATION_LISTENER");
            intent.putExtra("connected", true);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            notification++;

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

        String packageName = sbn.getPackageName();

        Log.e("TAG", "onNotificationRemoved " + packageName);

        if(packageName.equals("com.skype.m2")) {

            if(notification%2 !=0) {

                Log.e("Notification Value", String.valueOf(notification));

                try {

                    if (myView != null)

                        wm.removeView(myView);

                    if (myView_name != null)
                        wm_name.removeView(myView_name);

                } catch (Exception Ex) {
                }

                Intent revert = new Intent(this, LoginActivity.class);
                revert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(revert);

                Intent intent = new Intent("com.example.NOTIFICATION_LISTENER");
                intent.putExtra("connected", false);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }

        }

    }



    @Override
    public StatusBarNotification[] getActiveNotifications() {
        return super.getActiveNotifications();
    }

    BroadcastReceiver nlServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null) {
                boolean connected = intent.getBooleanExtra("connected", false);
                Intent skypeIntent;
                skypeIntent = new Intent(SyncStateContract.Constants.ACCOUNT_NAME);
                if(connected && !mSkypeConnected) {
                    mSkypeConnected = true;
                    skypeIntent.putExtra("connected", true);
                } else if(!connected) {
                    mSkypeConnected = false;
                    Log.d("TAG", "send broadcast disconnected");
                    skypeIntent.putExtra("connected", false);
                }
                sendBroadcast(skypeIntent);
            }
        }
    };

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

            if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1) {
                continue;
            }
            if(packageInfo.packageName.equals(myPackage)) {
                continue;
            }
            if(packageInfo.packageName.equals(packageTokill)) {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            }

        }

        Log.e("Killing", "Executed");

    }

    public void stopThread(){

        try{

            thread.stop();
            thread.destroy();
            thread.interrupt();

        }
        catch (Exception Ex){}

        Log.e("Thread Service Stopped", "Thanks");

    }

}
package com.example.wepopandroid1.myapplicationtest;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmActivity extends Activity {
    MediaPlayer mp=null ;
    Vibrator v;


    JSONObject jspost=null;
    String StartTime,EndTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm);

        Button stopAlarm = (Button) findViewById(R.id.button);
        mp = MediaPlayer.create(getBaseContext(),R.raw.classic);
        mp.setLooping(true);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c=Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
//                mp.start();

                EndTime=formattedDate;
                Log.e("EndTime", "" + EndTime);

                JSONPost JsnPst=new JSONPost();
                try {
                    jspost=JsnPst.AlarmTiming(StartTime,EndTime);
                    Log.e("jspost", "" + jspost);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                Intent Intnt=new Intent(getApplicationContext(),MapsActivity.class);
//                Intnt.putExtra("AlarmStopTime",formattedDate);
//                startActivity(Intnt);

                stop();
//                mp.stop();
//                finish();
            }
        });
        playSound(this, getAlarmUri());
    }

    private void stop() {
        mp.stop();
        v.cancel();
        finish();
    }

    private void playSound(final Context context, Uri alert) {


        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    Calendar c=Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    mp.start();

                    StartTime=formattedDate;
                    Log.e("StartTime", "" + StartTime);

//                    Intent In=new Intent(getApplicationContext(),MapsActivity.class);
//                    In.putExtra("AlarmStartTime",formattedDate);
//                    startActivity(In);

//                    JSONPost JsnPst=new JSONPost();
//                    jspost=JsnPst.AlarmTiming(StartTime,EndTime);


                    v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {5, 1000, 1000};
                    v.vibrate(pattern, 0);

                } catch (Throwable t) {
                    Log.i("Animation", "Thread  exception "+t);
                }
            }
        });
        background.start();
    }
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        v.cancel();
    }



    private Uri getAlarmUri() {

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}

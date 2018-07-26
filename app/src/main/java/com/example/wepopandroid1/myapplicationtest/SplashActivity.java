package com.example.wepopandroid1.myapplicationtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String status = SharedHelper.getKey(SplashActivity.this, "login_type");
                if (status.equalsIgnoreCase("Logged")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                String bbb = SharedHelper.getKey(SplashActivity.this, "loginvalue");
                String Usersession = SharedHelper.getKey(SplashActivity.this, "SessionID");
                String Userid = SharedHelper.getKey(SplashActivity.this, "UserID");
                String UName = SharedHelper.getKey(SplashActivity.this, "LName");
                if (bbb.equalsIgnoreCase("1")) {
                    Intent mainIntent = new Intent(SplashActivity.this, MapsActivity.class);
//                    SharedHelper.putKey(getApplicationContext(),"SessionID", Usersession);
//                    SharedHelper.putKey(getApplicationContext(),"UserID", Userid);
                    mainIntent.putExtra("SessionID", Usersession);
                    mainIntent.putExtra("UserID", Userid);
                    mainIntent.putExtra("LName",UName);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }else if (bbb.equalsIgnoreCase("2")){
                    Intent mainIntent = new Intent(SplashActivity.this, HeadMap.class);
//                    SharedHelper.putKey(getApplicationContext(),"SessionID", Usersession);
//                    SharedHelper.putKey(getApplicationContext(),"UserID", Userid);
                    mainIntent.putExtra("SessionID", Usersession);
                    mainIntent.putExtra("UserID", Userid);
                    mainIntent.putExtra("LName",UName);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }

                else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }

            }
        }, 5000);
    }
}

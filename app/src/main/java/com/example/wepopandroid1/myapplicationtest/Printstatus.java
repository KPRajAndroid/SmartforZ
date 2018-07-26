package com.example.wepopandroid1.myapplicationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Printstatus extends Activity {

    TextView Tv;
    String Stng;
    Button B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printstatus);

        Tv=(TextView)findViewById(R.id.PrintStatus);
        B=(Button)findViewById(R.id.BtnToPrint);

        Stng=SharedHelper.getKey(getApplicationContext(),"PrntSts");

        Tv.setText(Stng);

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Printstatus.this.finish();
                Toast.makeText(getApplicationContext(),"Contents sent to Printer",Toast.LENGTH_LONG).show();
            }
        });

    }

}

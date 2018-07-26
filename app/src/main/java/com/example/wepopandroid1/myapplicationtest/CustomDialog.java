package com.example.wepopandroid1.myapplicationtest;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;


/**
 * Created by User on 13-06-2016.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.custom_dialog);
    }
}

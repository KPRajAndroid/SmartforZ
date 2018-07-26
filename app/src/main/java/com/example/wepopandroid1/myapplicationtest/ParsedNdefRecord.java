package com.example.wepopandroid1.myapplicationtest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 1/9/2017.
 */
public interface ParsedNdefRecord {
    public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent,
                        int offset);

}

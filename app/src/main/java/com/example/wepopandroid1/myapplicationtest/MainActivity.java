package com.example.wepopandroid1.myapplicationtest;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wepopandroid1.myapplicationtest.Gcm.GCMRegistrationIntentService;
import com.example.wepopandroid1.myapplicationtest.Gcm.TelephonyInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks{
    model modelData;
    Button start_but, sign_but;
    EditText Username, password,OrgId;
    String username, logpassword, android_id, Usersession, Userid,Dtoken;
    String result,login_type;
    TextView error;
    TextView tv;
    String devices;
    String u_name, pass,O_Id;
    ProgressDialog mProgressDialog;
    int progressStatus = 0;
    private Handler handler = new Handler();
    JSONObject login_object = null;

    private static final int PERMISSION_ALL = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox remember;
    Boolean SharedValue;
    GoogleApiClient mGoogleApiClient;
    static MapsActivity mact;
    String token = "", imeiSIM2, imeiSIM1, local = "";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.abs_layout);
        setContentView(R.layout.activity_main);

        mact=new MapsActivity();

//        MultiDex.install(this);

//        if (getIntent().getBooleanExtra("EXIT", false)) {
//            finish();
//        }

        modelData = model.getInstance();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_SMS, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_COARSE_LOCATION
                , android.Manifest.permission.READ_PHONE_STATE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//            isDualSimOrNot();
        }else {
//            isDualSimOrNot();
        }

        int resCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (ConnectionResult.SUCCESS != resCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }


//        android_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        start_but = (Button) findViewById(R.id.but_start);
        Username = (EditText) findViewById(R.id.edt_username);
        password = (EditText) findViewById(R.id.edt_password);
        OrgId=(EditText)findViewById(R.id.edt_OrganId);
        sign_but = (Button) findViewById(R.id.but_signIn);
        sign_but.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                u_name = Username.getText().toString();
                SharedHelper.putKey(MainActivity.this,"NameOfSH",u_name);
                pass = password.getText().toString();
                O_Id=OrgId.getText().toString();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {


                   // checkLogin();


//                    new login_active().execute();



                    new login_active().execute();

                } else if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) && (ni != null)){
//                    Toast.makeText(getApplicationContext(),"Please ON the gps and data connection",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                    adb.setMessage("GPS is Disabled on Your Mobile, Do You Want to Proceed...?");
                    adb.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                Intent inn=new Intent("android.location.GPS_ENABLED_CHANGE");
//                inn.putExtra("enabled",true);
//                sendBroadcast(inn);
                            new login_active().execute();
                        }
                    });
                    adb.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
//                            android.os.Process.killProcess(android.os.Process.myPid());
//                            (MainActivity.this).finish();
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.putExtra("EXIT", true);
//                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });


//        remember = (CheckBox) findViewById(R.id.checkBox2);
        sharedPreferences = getSharedPreferences("LoginKey", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        SharedValue = sharedPreferences.getBoolean("SharedValue", false);

        if (SharedValue == true) {
            Username.setText(sharedPreferences.getString("SaveLoginEmail", ""));
            password.setText(sharedPreferences.getString("SaveLoginPass", ""));
            OrgId.setText(sharedPreferences.getString("SaveLoginOrgId",""));
//            Intent in=new Intent(MainActivity.this,MapsActivity.class);
//            startActivity(in);
            remember.setChecked(true);
        }
    }

    public void isDualSimOrNot() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

        imeiSIM1 = telephonyInfo.getImsiSIM1();
        imeiSIM2 = telephonyInfo.getImsiSIM2();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        boolean isDualSIM = telephonyInfo.isDualSIM();
        Log.i("Dual = ", " IME1 : " + imeiSIM1 + "\n" +
                " IME2 : " + imeiSIM2 + "\n" +
                " IS DUAL SIM : " + isDualSIM + "\n" +
                " IS SIM1 READY : " + isSIM1Ready + "\n" +
                " IS SIM2 READY : " + isSIM2Ready + "\n");

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


     private class login_active extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            // Set progressdialog message
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonarray;

            try {

                Dtoken=SharedHelper.getKey(getApplicationContext(),"TokenVal");
                android_id=Dtoken;

                JSONPost jPost = new JSONPost();
                login_object = jPost.login_id(O_Id,u_name, pass, android_id, "Android");
                Log.e("login_object", "" + login_object);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                login_type=login_object.getString("LoginType");
                String msg = login_object.getString("Message");
                result = login_object.getString("Result");
                String session_id = login_object.getString("SessionID");
                Usersession = session_id;
//                SharedHelper.putKey(MainActivity.this, "userIdentity", login_object.getString("Usersession"));

                modelData.userId = login_object.getString("UserIdentity");
                SharedHelper.putKey(MainActivity.this, "userID", login_object.getString("UserIdentity"));
                Userid = modelData.userId;
                Log.e("LATLNG", "" + login_type + msg + result + session_id + modelData.userId);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //   filterList = new ArrayList<HashMap<String, String>>(Songsarraylist);
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                if (result.equalsIgnoreCase("1") && login_type.equalsIgnoreCase("security")) {

                    SharedHelper.putKey(getApplicationContext(),"loginvalue", "1");

                    //Intent intent1 = new Intent(getApplicationContext(),MapsActivity.class );
                    Intent intent1 = new Intent(getApplicationContext(), MapsActivity.class);
                    SharedHelper.putKey(getApplicationContext(),"SessionID", Usersession);
//                    SharedHelper.putKey(getApplicationContext(),"UserID", Userid);
                    SharedHelper.putKey(getApplicationContext(),"LName",u_name);
                    SharedHelper.putKey(getApplicationContext(),"OrgId",O_Id);
                    intent1.putExtra("SessionID", Usersession);
                    intent1.putExtra("UserID", Userid);
                    intent1.putExtra("LName",u_name);
                    intent1.putExtra("OrgId",O_Id);
                    startActivity(intent1);
                }else if (result.equalsIgnoreCase("1") && login_type.equalsIgnoreCase("Security Head")){
                    SharedHelper.putKey(getApplicationContext(),"loginvalue", "2");

                    //Intent intent1 = new Intent(getApplicationContext(),MapsActivity.class );
                    Intent intent1 = new Intent(getApplicationContext(), HeadMap.class);
                    SharedHelper.putKey(getApplicationContext(),"SessionID", Usersession);
//                    SharedHelper.putKey(getApplicationContext(),"UserID", Userid);
                    SharedHelper.putKey(getApplicationContext(),"LName",u_name);
                    SharedHelper.putKey(getApplicationContext(),"OrgId",O_Id);
                    intent1.putExtra("SessionID", Usersession);
                    intent1.putExtra("UserID", Userid);
                    intent1.putExtra("LName",u_name);
                    intent1.putExtra("OrgId",O_Id);
                    startActivity(intent1);
                }else {
                    Toast.makeText(getApplicationContext(), "Please check your username or password", Toast.LENGTH_LONG).show();

                }
                mProgressDialog.dismiss();
//                if (remember.isChecked()) {
//                    editor.putBoolean("SharedValue", true);
//                    editor.putString("SaveLoginEmail", u_name);
//                    editor.putString("SaveLoginPass", pass);
//                    editor.putString("SaveLoginOrgId",O_Id);
//                    editor.commit();
//                } else {
//                    editor.clear().commit();
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
//        else {
//            mact.isDualSimOrNot();
//        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}


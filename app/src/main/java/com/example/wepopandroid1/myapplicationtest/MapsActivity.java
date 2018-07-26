package com.example.wepopandroid1.myapplicationtest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wepopandroid1.myapplicationtest.Gcm.GCMRegistrationIntentService;
import com.example.wepopandroid1.myapplicationtest.Gcm.NoteList;
import com.example.wepopandroid1.myapplicationtest.Gcm.NotificationCounter;
import com.example.wepopandroid1.myapplicationtest.Gcm.TelephonyInfo;
import com.example.wepopandroid1.myapplicationtest.SCam.CameraStreamer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MapsActivity extends AppCompatActivity implements SensorEventListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,SurfaceHolder.Callback {
    JSONObject scanobject = null;
    JSONObject Alarmobject=null;
    String format, contents;
    model modelData;
    Button start, qr_but, stop_but, attendance, incidentreport, panic;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    GPSTracker gps;
    double stringLatitude, stringLongitude;
    Location currentlocation;
    ProgressDialog mProgressDialog;
    int progressStatus = 0;
    JSONObject loginobject, endpatrolobject, event_object, attendance_object = null;
    private boolean stopsound = false;
    String lng = "", lat = "", current_lat = "", current_lng = "";
    ImageView img_menu;
    MediaPlayer mp;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    private String stepcount;
    private TextView count;
    boolean activityRunning;
    Bundle extras;
    boolean startpatrol;
    private static final String TAG = MapsActivity.class.getSimpleName();
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    Polyline line;
//-----------------------------Alarm-------------------------
    GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    PendingIntent pInt;
    AlarmManager Am;
    Handler Alarmhand,Alarmhand2;
    Runnable AlarmThread,AlarmThread2;
    String Camera,Camera1;
    String Vibrate=null;
    String Vibrate1=null;
    String time,time1;
    Long RepeatTime;
//    String UserName;
//-------------------------------------------------------------


    //--------------------------Camera-----------------------------
    private static final String TAG2 = "AndroidCameraApi";
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    Handler Camhandler;
    Runnable CamThread;
    MultipartEntity multipartEntity;
    File ImgFile;

    //--------------------------------------------------------------
    private static final int PERMISSION_ALL = 1;
    Handler hand,SHhandler,Lochand,Locationhand,Notehand;
    Runnable tred,SHThread,LocThread,LocationThread,NoteThread;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String token = "", imeiSIM2, imeiSIM1, local = "";
    private int Ncount=NotificationCounter.getPendingNotificationsCount();
    String NcountValue;
    int iin;
    private int Note_count;
    private boolean Shiftmode = false;
    MenuItem shift,ParkOP;
    String S="";

//    _____________________________________________________________

    private static final String WAKE_LOCK_TAG = "peepers";
    private static final String PREF_CAMERA = "camera";
    private static final int PREF_CAMERA_INDEX_DEF = 0;
    private static final String PREF_FLASH_LIGHT = "flash_light";
    private static final boolean PREF_FLASH_LIGHT_DEF = false;
    private static final String PREF_PORT = "port";
    private static final int PREF_PORT_DEF = 8080;
    private static final String PREF_JPEG_SIZE = "size";
    private static final String PREF_JPEG_QUALITY = "jpeg_quality";
    private static final int PREF_JPEG_QUALITY_DEF = 40;
    private static final int PREF_PREVIEW_SIZE_INDEX_DEF = 0;

    private boolean mRunning = false;
    private boolean mPreviewDisplayCreated = false;
    private SurfaceHolder mPreviewDisplay = null;
    private CameraStreamer mCameraStreamer = null;

    private String mIpAddress = "";
    private int mCameraIndex = PREF_CAMERA_INDEX_DEF;
    private boolean mUseFlashLight = PREF_FLASH_LIGHT_DEF;
    private int mPort = PREF_PORT_DEF;
    private int mJpegQuality = PREF_JPEG_QUALITY_DEF;
    private int mPrevieSizeIndex = PREF_PREVIEW_SIZE_INDEX_DEF;
    private TextView mIpAddressView = null;
    private LoadPreferencesTask mLoadPreferencesTask = null;
    private SharedPreferences mPrefs = null;
    private MenuItem mSettingsMenuItem = null;
    private PowerManager.WakeLock mWakeLock = null;

    String StrmStrng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_cpy);
       /* textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);*/
        Camhandler=new Handler();
        Alarmhand=new Handler();
        Alarmhand2=new Handler();
        SHhandler=new Handler();
        Lochand=new Handler();
        Locationhand=new Handler();
        Notehand=new Handler();
        final Handler handlerStream = new Handler();

        new LoadPreferencesTask().execute();

        mPreviewDisplay = ((SurfaceView) findViewById(R.id.camera_cpy)).getHolder();
//        mPreviewDisplay.lockCanvas().rotate(90);
        mPreviewDisplay.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPreviewDisplay.addCallback(this);

        mIpAddress = tryGetIpV4Address();
        mIpAddressView = (TextView) findViewById(R.id.ip_address_cpy);
        updatePrefCacheAndUi();

        final PowerManager powerManager =
                (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
                WAKE_LOCK_TAG);

        handlerStream.postDelayed(new Runnable() {
            @Override
            public void run() {

                stopstreaming();

                //Do something after 100ms
            }
        }, 2000);


        ToTurnOnGps();

        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            isDualSimOrNot();
        }else {
            isDualSimOrNot();
        }
        local = SharedHelper.getKey(MapsActivity.this, "login");
        Log.e("local",""+local);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {


            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully

//                int pendingNotificationsCount = NotificationCounter.getPendingNotificationsCount() + 1;
//                int pendingNotificationsCount = Ncount + 1;
//                NotificationCounter.setPendingNotificationsCount(pendingNotificationsCount);
//                Ncount=pendingNotificationsCount;
//                Note_count=pendingNotificationsCount;



                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    Log.e("inner_token", "" + token);

                    //  Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    if (local.equalsIgnoreCase("") || local.equalsIgnoreCase(null) || local.equalsIgnoreCase("false")) {

                        Log.e("empty_token", "" + token);
                        Log.e("empty_imei", "" + imeiSIM1 + "," + imeiSIM2);
                        new Upload_detail().execute();
                        local = "install";
                        SharedHelper.putKey(MapsActivity.this, "login", local);
                    } else{
                        Log.e("install_token", "" + token);
                        Log.e("install_imei", "" + imeiSIM1 + "," + imeiSIM2);


                    }
                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

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


//        UserName = SharedHelper.getKey(getApplicationContext(), "LName");

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);



        new totrigalarm().execute();

//        AlarmThread=new Runnable() {
//            @Override
//            public void run() {
//                String SName = SharedHelper.getKey(getApplicationContext(), "SessionID");
//                String UName = SharedHelper.getKey(getApplicationContext(), "UserID");
////                String UserName = SharedHelper.getKey(getApplicationContext(), "LName");
//                JSONPost jPost = new JSONPost();
//                try {
//                    Alarmobject = jPost.alarmTriggerId(UserName);
//                    Log.e("Alarmobject", "" + Alarmobject);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (Alarmobject != null) {
////                    Toast.makeText(getApplicationContext(), "JSON Object is null", Toast.LENGTH_SHORT).show();
//                try {
//                    Vibrate = Alarmobject.getString("Vibrate");
//                    Camera = Alarmobject.getString("Camera");
////                    RepeatTime= Long.valueOf(Alarmobject.getString("Duration"));
//                    time=Alarmobject.getString("Duration");
//
//                    String Session_Id = SName;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (Vibrate!=null&&Vibrate.equalsIgnoreCase("True")) {
//                    try {
//                        Intent In = new Intent(MapsActivity.this, AlarmActivity.class);
//                        pInt = PendingIntent.getActivities(MapsActivity.this, 12345, new Intent[]{In}, PendingIntent.FLAG_CANCEL_CURRENT);
////            PendingIntent Pint=PendingIntent.getBroadcast(this,0,In,0);
//                        Am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
////            Am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),2*60*1000,pInt);
////            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR,pInt);
////                        Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),30*60*1000,pInt);
//                        if (RepeatTime==null) {
//                            Am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pInt);
//                        }else {
//                            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), Long.parseLong(time),pInt);
//                        }
////                        Toast.makeText(getApplicationContext(),"Alarm will start after 30 Mins", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//
//                    }
//                }else {
////                    Toast.makeText(getApplicationContext(),"Vibrate Object is null",Toast.LENGTH_SHORT).show();
//                }
//
//                Alarmhand.postDelayed(this, 20 * 60 * 1000);
////                Toast.makeText(getApplicationContext(),"AlarmThread is started",Toast.LENGTH_SHORT).show();
//            }else{
//
//                }
//            }
//        };Alarmhand.post(AlarmThread);


//        AlarmThread2=new Runnable() {
//            @Override
//            public void run() {
//                String SName = SharedHelper.getKey(getApplicationContext(), "SessionID");
//                String UName = SharedHelper.getKey(getApplicationContext(), "UserID");
//                UserName = SharedHelper.getKey(getApplicationContext(), "LName");
//                JSONPost jPost = new JSONPost();
//                try {
//                    Alarmobject = jPost.alarmTriggerId(UserName);
//                    Log.e("Alarmobject", "" + Alarmobject);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (Alarmobject !=null) {
////                    Toast.makeText(getApplicationContext(), "JSON Object is null", Toast.LENGTH_SHORT).show();
//
//                    try {
//                        Vibrate = Alarmobject.getString("Vibrate");
//                        Camera = Alarmobject.getString("Camera");
////                        RepeatTime= Long.valueOf(Alarmobject.getString("Duration"));
//                        time=Alarmobject.getString("Duration");
//
//                        String Session_Id = SName;
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (Vibrate!=null&&Vibrate.equalsIgnoreCase("True")) {
//                        try {
//                            Intent In = new Intent(MapsActivity.this, AlarmActivity.class);
//                            pInt = PendingIntent.getActivities(MapsActivity.this, 12345, new Intent[]{In}, PendingIntent.FLAG_CANCEL_CURRENT);
////            PendingIntent Pint=PendingIntent.getBroadcast(this,0,In,0);
//                            Am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
////            Am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),2*60*1000,pInt);
////            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR,pInt);
////                        Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),30*60*1000,pInt);
//                            if (RepeatTime==null){
//                            Am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pInt);
//                            }else {
//                                Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), Long.parseLong(time),pInt);
//                            }
////                        Toast.makeText(getApplicationContext(),"Alarm will start after 30 Mins", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//
//                        }
//                    }else {
////                        Toast.makeText(getApplicationContext(),"Vibrate Object is null",Toast.LENGTH_SHORT).show();
//                    }
//
//                    Alarmhand2.postDelayed(this, 20 * 1000);
////                Toast.makeText(getApplicationContext(),"AlarmThread2 is started",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(),"AlarmThread2 is started",Toast.LENGTH_SHORT).show();
//                }
//            }
//        };



//        try {
//            Intent In = new Intent(this, AlarmActivity.class);
//            pInt = PendingIntent.getActivities(this, 12345, new Intent[]{In}, PendingIntent.FLAG_CANCEL_CURRENT);
////            PendingIntent Pint=PendingIntent.getBroadcast(this,0,In,0);
//            Am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
////            Am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),2*60*1000,pInt);
////            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR,pInt);
//            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30*60*1000,30*60*1000,pInt);
//            Toast.makeText(getApplicationContext(),"Alarm will start after 30 Mins", Toast.LENGTH_SHORT).show();
//        }catch (Exception e) {
//
//        }


        SHThread=new Runnable() {
            @Override
            public void run() {
                new performBackgroundTask1().execute();
                SHhandler.postDelayed(SHThread,5000);
            }
        };

        LocationThread=new Runnable() {
            @Override
            public void run() {
                new performBackgroundTask4().execute();
//                currentlocation1();
                Locationhand.postDelayed(LocationThread,5000);
            }
        };






       LocThread=new Runnable() {
            @Override
            public void run() {
                new performBackgroundTask3().execute();
//                currentlocation1();
                Lochand.postDelayed(LocThread,5000);
            }
        };

            extras = getIntent().getExtras();

            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            setUpMapIfNeeded();
            modelData = model.getInstance();

            start = (Button) findViewById(R.id.but_start);
            qr_but = (Button) findViewById(R.id.but_qr);
            stop_but = (Button) findViewById(R.id.but_stop);
            attendance = (Button) findViewById(R.id.but_attendance);
            incidentreport = (Button) findViewById(R.id.but_incident);
            panic = (Button) findViewById(R.id.but_panic);
            mp = MediaPlayer.create(this, R.raw.panicalarm);
            mp.setLooping(true);
            // img_menu = (ImageView) findViewById(R.id.img_menu);
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            stop_but.setEnabled(false);
            start.setEnabled(true);
            qr_but.setEnabled(false);
            attendance.setEnabled(true);
            incidentreport.setEnabled(true);
            panic.setEnabled(true);

            stop_but.setBackgroundResource(R.drawable.end_patrolgray);
            qr_but.setBackgroundResource(R.drawable.scan_qr_96pxgray);
            attendance.setBackgroundResource(R.drawable.attentance);
            incidentreport.setBackgroundResource(R.drawable.icident_report);
            panic.setBackgroundResource(R.drawable.panic_alarm);

            currentlocation();


            start.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    stop_but.setEnabled(true);
                    qr_but.setEnabled(true);
//                attendance.setEnabled(true);
//                incidentreport.setEnabled(true);
//                panic.setEnabled(true);

                    Lochand.removeCallbacks(LocThread);

                    qr_but.setBackgroundResource(R.drawable.scan_qr_96px);
                    stop_but.setBackgroundResource(R.drawable.end_patrol);
//                attendance.setBackgroundResource(R.drawable.attentance);
                    incidentreport.setBackgroundResource(R.drawable.icident_report);
                    panic.setBackgroundResource(R.drawable.panic_alarm);

                    start.setEnabled(false);
                    start.setBackgroundResource(R.drawable.start_patrolgray);
                /*Toast.makeText(getApplicationContext(),
                       "lattitude"+lat+"longitude"+lng+"userId"+modelData.userId,
new

                        Toast.LENGTH_LONG).show();*/

                    try {
                        startpatrol = true;
                        clearmap();
                        currentlocation();

                        // gps = new GPSTracker(MapsActivity.this);
                        stringLatitude = currentlocation.getLatitude();
                        stringLongitude = currentlocation.getLongitude();
                        lat = String.valueOf(stringLatitude);
                        lng = String.valueOf(stringLongitude);

                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
// Register the listener with the Location Manager to receive location updates

                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {
                            new startqr().execute();
                            // callAsynchronousTask();
                            // new performBackgroundTask().execute();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Please ON the gps and data connection",
                                    Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception ee) {

                        Toast.makeText(getApplicationContext(),
                                "Please ON the gps and data connection2",
                                Toast.LENGTH_LONG).show();

                    }


                }

            });

            qr_but.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    start.setEnabled(false);
                    qr_but.setEnabled(true);

                    initiatePopupWindow();

//                    try {
//                        //currentlocation();
//                        //gps = new GPSTracker(MapsActivity.this);
//                        stringLatitude = currentlocation.getLatitude();
//                        stringLongitude = currentlocation.getLongitude();
//                        lat = String.valueOf(stringLatitude);
//                        lng = String.valueOf(stringLongitude);
//                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                        NetworkInfo ni = cm.getActiveNetworkInfo();
//                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {
//                            try {
//
//                                IntentIntegrator integrator = new IntentIntegrator(MapsActivity.this);
//                                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                                integrator.setPrompt("Scan a QRCode");
//                                integrator.setResultDisplayDuration(0);
//                                // integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
//                                integrator.setCameraId(0);  // Use a specific camera of the device
//                                integrator.initiateScan();
//
//                                // Intent intent = new Intent(ACTION_SCAN);
//                                // intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                                // startActivityForResult(intent, 0);
//                            } catch (ActivityNotFoundException anfe) {
//                                showDialog(MapsActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
//                            }
//
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Please ON the gps and data connection", Toast.LENGTH_LONG).show();
//
//                        }
//                    } catch (Exception ex) {
//
//                        Toast.makeText(getApplicationContext(),
//                                "Please ON the gps and data connection",
//                                Toast.LENGTH_LONG).show();
//
//                    }


                }
            });
            attendance.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                stop_but.setEnabled(false);
//                qr_but.setEnabled(false);
                    incidentreport.setEnabled(true);
                    panic.setEnabled(true);

//                qr_but.setBackgroundResource(R.drawable.scan_qr_96pxgray);
//                stop_but.setBackgroundResource(R.drawable.end_patrolgray);
                    incidentreport.setBackgroundResource(R.drawable.icident_report);
                    panic.setBackgroundResource(R.drawable.panic_alarm);

                    attendance.setEnabled(false);
                    attendance.setBackgroundResource(R.drawable.attentancegray);

//                start.setEnabled(false);
//                start.setBackgroundResource(R.drawable.start_patrolgray);
                    try {
                        //gps = new GPSTracker(MapsActivity.this);
                        currentlocation();
                        stringLatitude = currentlocation.getLatitude();
                        stringLongitude = currentlocation.getLongitude();
                        lat = String.valueOf(stringLatitude);
                        lng = String.valueOf(stringLongitude);
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {
                            new getsession().execute();
                            try {
                                new performBackgroundattendanceTask().execute();

                                StrmStrng = SharedHelper.getKey(getApplicationContext(), "StreamVal");

                                if (!StrmStrng.isEmpty() && StrmStrng.equalsIgnoreCase("1")){

                                    SharedHelper.putKey(getApplicationContext(),"StreamVal","0");
                                }else {

                                    SharedHelper.putKey(getApplicationContext(),"StreamVal","1");

                                }


                                Toast.makeText(getApplicationContext(),
                                        "Attendance Submitted",
                                        Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Please ON the gps and data connection",
                                    Toast.LENGTH_LONG).show();


                        }

                        /*S=SharedHelper.getKey(getApplicationContext(),"spcval");

                        if (S.equalsIgnoreCase("1")){
                            SharedHelper.putKey(getApplicationContext(),"spcval","0");
//                            Intent Inten=new Intent(getApplicationContext(),spcMainActivity.class);
//                            startActivity(Inten);

//                            spcMainActivity.fa.finish();

                        }else {
                            SharedHelper.putKey(getApplicationContext(),"spcval","1");
//                            Intent Inte=new Intent(getApplicationContext(),spcMainActivity.class);
//                            startActivity(Inte);
                        }*/



                    } catch (Exception ex) {

                        Toast.makeText(getApplicationContext(),
                                "Please ON the gps and data connection",
                                Toast.LENGTH_LONG).show();

                    }

                }
            });

            incidentreport.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent intent = new Intent(MapsActivity.this, Incident_Report.class);
                 /*   stringLatitude = currentlocation.getLatitude();
                    stringLongitude = currentlocation.getLongitude();*/
                        stringLatitude = gps.getLatitude();
                        stringLongitude = gps.getLongitude();
                        lat = String.valueOf(stringLatitude);
                        lng = String.valueOf(stringLongitude);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        modelData.session_id = extras.getString("SessionID");
                        intent.putExtra("SessionID", modelData.session_id);
                        startActivity(intent);

                    } catch (Exception ex) {

                        Toast.makeText(getApplicationContext(),
                                "Please Check the gps and data connection",
                                Toast.LENGTH_LONG).show();

                    }


                }
            });


            panic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (stopsound) {
                            mp.pause();



                            Camhandler.removeCallbacks(CamThread);
                            new totrigalarm().execute();
//                            Toast.makeText(getApplicationContext(),"Camera stopped",Toast.LENGTH_SHORT).show();

                            /*SharedHelper.putKey(getApplicationContext(),"spcval","0");

                            Intent Inte=new Intent(getApplicationContext(),spcMainActivity.class);
                            startActivity(Inte);*/

                            stopstreaming();

                            stopsound = false;
                        } else {

                            AlertDialog.Builder aldb = new AlertDialog.Builder(MapsActivity.this);
                            aldb.setMessage("Activate Alarm");
                            aldb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //gps = new GPSTracker(MapsActivity.this);
                                    currentlocation();
                                    stringLatitude = currentlocation.getLatitude();
                                    stringLongitude = currentlocation.getLongitude();
                                    lat = String.valueOf(stringLatitude);
                                    lng = String.valueOf(stringLongitude);
                                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo ni = cm.getActiveNetworkInfo();
                                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {
                                        new startqr().execute();
                                        try {
                                            new performBackgroundpanicTask().execute();
                                            mp.start();



//                                            if (!time.equalsIgnoreCase(null)){
//                                                Am.cancel(pInt);
//                                            }
//                                            Am.cancel(pInt);
                                            new totrigalarm2().execute();
//                                            if (Camera.equalsIgnoreCase("True")&&Camera!=null){
//                                                CamThread = new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        takePicture();
//                                                        Camhandler.postDelayed(this, 3000);
//                                                    }
//                                                };
//                                                Camhandler.post(CamThread);
//
//                                             } else  {
//
////                                                Toast.makeText(getApplicationContext(),"Alarm Object Null",Toast.LENGTH_SHORT).show();
//                                            }


                                            Toast.makeText(getApplicationContext(),
                                                    "Panic Alarm Activated",
                                                    Toast.LENGTH_LONG).show();

                                            startstreaming();

                                            Toast.makeText(getApplicationContext(),"http://" + mIpAddress + ":" + mPort + "/",Toast.LENGTH_LONG).show();


                                            /*SharedHelper.putKey(getApplicationContext(),"spcval","1");

                                            Intent Inte=new Intent(getApplicationContext(),spcMainActivity.class);
                                            startActivity(Inte);*/


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    stopsound = true;
                                }

                            });
                            aldb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(), "Alarm Aborted", Toast.LENGTH_LONG).show();
                                }
                            });
                            AlertDialog alertDialog = aldb.create();
                            alertDialog.show();
                        }

                    } catch (Exception ex) {

                        Toast.makeText(getApplicationContext(),
                                "Please ON the gps and data connection",
                                Toast.LENGTH_LONG).show();
                    }
                }

            });
            stop_but.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    start.setEnabled(true);
                    qr_but.setEnabled(false);
//                attendance.setEnabled(false);
                    if (Shiftmode==true) {
                        Lochand.post(LocThread);
                    }
                    incidentreport.setEnabled(true);
                    panic.setEnabled(true);
                    stop_but.setEnabled(false);

                    start.setBackgroundResource(R.drawable.start_patrol);
                    qr_but.setBackgroundResource(R.drawable.scan_qr_96pxgray);
//                attendance.setBackgroundResource(R.drawable.attentancegray);
//                incidentreport.setBackgroundResource(R.drawable.icident_reportgray);
//                panic.setBackgroundResource(R.drawable.panic_alarmgray);
                    stop_but.setBackgroundResource(R.drawable.end_patrolgray);

                    try {
                        // gps = new GPSTracker(MapsActivity.this);
                        startpatrol = false;
                        currentlocation();
                        stringLatitude = currentlocation.getLatitude();
                        stringLongitude = currentlocation.getLongitude();
                        lat = String.valueOf(stringLatitude);
                        lng = String.valueOf(stringLongitude);
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {
                            new stoppatrol().execute();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Please ON the gps and data connection",
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception cc) {

                        Toast.makeText(getApplicationContext(),
                                "Please ON the gps and data connection",
                                Toast.LENGTH_LONG).show();
                    }

                    //new performBackgroundTask().cancel(true);
                }
            });

        }

    private void startstreaming() {

        startBackgroundThread();

        mRunning = true;
        if (mPrefs != null)
        {
            mPrefs.registerOnSharedPreferenceChangeListener(
                    mSharedPreferenceListener);
        } // if
        updatePrefCacheAndUi();
        tryStartCameraStreamer();

        /*new LoadPreferencesTask().execute();


        mPreviewDisplay.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPreviewDisplay.addCallback(this);

        mIpAddress = tryGetIpV4Address();

        updatePrefCacheAndUi();*/

    }

    private void stopstreaming() {
        mRunning = false;
        if (mPrefs != null)
        {
            mPrefs.unregisterOnSharedPreferenceChangeListener(
                    mSharedPreferenceListener);
        } // if
        ensureCameraStreamerStopped();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private final void updatePrefCacheAndUi() {
        mCameraIndex = getPrefInt(PREF_CAMERA, PREF_CAMERA_INDEX_DEF);
        if (hasFlashLight())
        {
            if (mPrefs != null)
            {
                mUseFlashLight = mPrefs.getBoolean(PREF_FLASH_LIGHT,
                        PREF_FLASH_LIGHT_DEF);
            } // if
            else
            {
                mUseFlashLight = PREF_FLASH_LIGHT_DEF;
            } // else
        } //if
        else
        {
            mUseFlashLight = false;
        } // else

        // XXX: This validation should really be in the preferences activity.
        mPort = getPrefInt(PREF_PORT, PREF_PORT_DEF);
        // The port must be in the range [1024 65535]
        if (mPort < 1024)
        {
            mPort = 1024;
        } // if
        else if (mPort > 65535)
        {
            mPort = 65535;
        } // else if

        mPrevieSizeIndex = getPrefInt(PREF_JPEG_SIZE, PREF_PREVIEW_SIZE_INDEX_DEF);
        mJpegQuality = getPrefInt(PREF_JPEG_QUALITY, PREF_JPEG_QUALITY_DEF);
        // The JPEG quality must be in the range [0 100]
        if (mJpegQuality < 0)
        {
            mJpegQuality = 0;
        } // if
        else if (mJpegQuality > 100)
        {
            mJpegQuality = 100;
        } // else if
        mIpAddressView.setText("http://" + mIpAddress + ":" + mPort + "/");

//        Toast.makeText(getApplicationContext(),mIpAddress,Toast.LENGTH_LONG).show();

//        Toast.makeText(getApplicationContext(),"http://" + mIpAddress + ":" + mPort + "/",Toast.LENGTH_LONG).show();
    }

    private final int getPrefInt(String prefCamera, int prefCameraIndexDef) {
        try
        {
            return Integer.parseInt(mPrefs.getString(prefCamera, null /* defValue */));
        } // try
        catch (final NullPointerException e)
        {
            return prefCameraIndexDef;
        } // catch
        catch (final NumberFormatException e)
        {
            return prefCameraIndexDef;
        }
    }

    private static String tryGetIpV4Address() {
        try
        {
            final Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements())
            {
                final NetworkInterface intf = en.nextElement();
                final Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

                while (enumIpAddr.hasMoreElements())
                {
                    final InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        final String addr = inetAddress.getHostAddress().toUpperCase();
                        if (InetAddressUtils.isIPv4Address(addr))
                        {
                            return addr;
                        }
                    } // if
                } // while
            } // for
        } // try
        catch (final Exception e)
        {
            // Ignore
        } // catch
        return null;
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

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            // Transform you image captured size according to the surface width and height

        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
//            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
//            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(MapsActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
//            createCameraPreview();
        }
    };
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String root = Environment.getExternalStorageDirectory().toString();
    File myDir = new File(root + "/saved_images");
    int n = 10000;

    protected void takePicture() {
        myDir.mkdirs();
        String num= String.valueOf(n++);
        String fname = "IMG_" + num + ".jpg";

        if(cameraDevice==null) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            final File file = new File(myDir,fname);
            if (file.exists()){
                file.delete();
            }
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MapsActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();

                    final String Id = SharedHelper.getKey(MapsActivity.this, "userID");

//                    new TosendImage().execute();

                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                    executorService.execute(new Runnable() {
                        public void run() {

                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://aflaree.com/tetrasecurity/securityservice/getImages");
                            multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                            ImgFile=new File(String.valueOf(file));
                            Log.d("ImgFile", String.valueOf(ImgFile));
                            try {
                                multipartEntity.addPart("UserID", new StringBody(Id));
                                multipartEntity.addPart("ImageURL", new FileBody(ImgFile));
                                httppost.setEntity((HttpEntity) multipartEntity);
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity r_entity = response.getEntity();
                                int statusCode = response.getStatusLine().getStatusCode();
                                if (statusCode == 200) {
                                    String res = EntityUtils.toString(r_entity);
                                    Log.e("res", "" + res);
//                                    return res;
                                } else {
//                                    return "Error";
                                }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (ClientProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            System.out.println("Asynchronous task");
                        }
                    });

                    executorService.shutdown();

//                    new AsyncTask<String, String, String>() {
//                        @Override
//                        protected String doInBackground(String... params) {
//                            HttpClient httpclient = new DefaultHttpClient();
//                            HttpPost httppost = new HttpPost(" http://aflaree.com/tetrasecurity/securityservice/getImages");
//                            multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                            ImgFile=new File(String.valueOf(file));
//                            Log.d("ImgFile", String.valueOf(ImgFile));
//                            try {
//                                multipartEntity.addPart("UserID", new StringBody(Id));
//                                multipartEntity.addPart("ImageURL", new FileBody(ImgFile));
//                                httppost.setEntity(multipartEntity);
//                                HttpResponse response = httpclient.execute(httppost);
//                                HttpEntity r_entity = response.getEntity();
//                                int statusCode = response.getStatusLine().getStatusCode();
//                                if (statusCode == 200) {
//                                    String res = EntityUtils.toString(r_entity);
//                                    Log.e("res", "" + res);
//                                    return res;
//                                } else {
//                                    return "Error";
//                                }
//
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            } catch (ClientProtocolException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            return null;
//                        }
//                    }.execute();

//                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }


    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MapsActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }else {
                isDualSimOrNot();
            }
        }
    }


    private void ToTurnOnGps() {
        if(mGoogleApiClient==null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MapsActivity.this,1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android M Permission check
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                setUpMapIfNeeded();
                MapsInitializer.initialize(MapsActivity.this);
            }
        }
    }

    private PopupWindow pwindo;
    private void initiatePopupWindow() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.loyout_popup, null);

        // Creating the PopupWindow
        pwindo = new PopupWindow(this);
        pwindo.setContentView(layout);
        pwindo.setWidth(width);
        pwindo.setHeight(height);
        pwindo.setFocusable(true);
        pwindo.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pwindo.setOutsideTouchable(true);

        // prevent clickable background
        pwindo.setBackgroundDrawable(null);
        pwindo.showAtLocation(layout, Gravity.CENTER, 1, 1);


        // Getting a reference to button one and do something
        Button butOne = (Button) layout.findViewById(R.id.layout_popup_butOne);
        butOne.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Do Something
                Intent in=new Intent(MapsActivity.this,ToReadNFC.class);
                startActivity(in);

                //Close Window
                pwindo.dismiss();
            }
        });

        // Getting a reference to button two and do something
        Button butTwo = (Button) layout.findViewById(R.id.layout_popup_butTwo);
        butTwo.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Do Something
                try {
                        //currentlocation();
                        //gps = new GPSTracker(MapsActivity.this);
                        stringLatitude = currentlocation.getLatitude();
                        stringLongitude = currentlocation.getLongitude();
                        lat = String.valueOf(stringLatitude);
                        lng = String.valueOf(stringLongitude);
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {
                            try {

                                IntentIntegrator integrator = new IntentIntegrator(MapsActivity.this);
                                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                integrator.setPrompt("Scan a QRCode");
                                integrator.setResultDisplayDuration(0);
                                // integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                                integrator.setCameraId(0);  // Use a specific camera of the device
                                integrator.initiateScan();

                                // Intent intent = new Intent(ACTION_SCAN);
                                // intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                                // startActivityForResult(intent, 0);
                            } catch (ActivityNotFoundException anfe) {
                                showDialog(MapsActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please ON the gps and data connection", Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception ex) {

                        Toast.makeText(getApplicationContext(),
                                "Please ON the gps and data connection",
                                Toast.LENGTH_LONG).show();

                    }
                //Close Window
                pwindo.dismiss();
            }
        });
    }


    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                // Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=me.scan.android.client&hl=en");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);


        if (scanningResult != null) {
//we have a result
            contents = scanningResult.getContents();
            format = scanningResult.getFormatName();

// display it on screen
//            Toast.makeText(getApplicationContext(),
//                    "Scanned Qr Sucessfully",
//                    Toast.LENGTH_LONG).show();

            Log.e("scancode", "Content:" + contents + " Format:" + format);


            new scanqrt().execute();


        } else if (scanningResult == null && resultCode==RESULT_OK){

            final Handler hndle = new Handler();
            hndle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
//                    Toast.makeText(getApplicationContext(),"Toast",Toast.LENGTH_SHORT).show();
                    setUpMap2();
                }
            }, 3000);

            Toast.makeText(getApplicationContext(), "GpS Enabled", Toast.LENGTH_SHORT).show();

        }else if (scanningResult == null && resultCode==RESULT_CANCELED){

            Toast.makeText(getApplicationContext(), "GpS Enabling Aborted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "No Scaning data received", Toast.LENGTH_SHORT).show();
        }
    }

    private void toOnGprs() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
            dataMtd.invoke(cm, true);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void updateNote() {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPreviewDisplayCreated = true;
//        tryStartCameraStreamer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPreviewDisplayCreated = false;
//        ensureCameraStreamerStopped();
    }

    private class scanqrt extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONPost jPost = new JSONPost();
                scanobject = jPost.sacn_id(modelData.session_id, contents, lat, lng, stepcount);
                Log.e("scan_loginobject", "" + scanobject);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = scanobject.getString("Message");
                String userid = scanobject.getString("Result");
                String sessin = scanobject.getString("SessionID");
                String user_identity = scanobject.getString("UserIdentity");

                Log.e("scanLATLNG", "" + msg + userid + sessin + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class performBackgroundattendanceTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();
                String id = SharedHelper.getKey(MapsActivity.this, "userID");
                attendance_object = jPost.attendance_id(id, lat, lng);
                Log.e("attendanceobject", "" + attendance_object);

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                String msg = attendance_object.getString("Message");
                String result = attendance_object.getString("Result");
                //  modelData.session_id = event_object.getString("SessionID");
                String user_identity = attendance_object.getString("UserIdentity");

                Log.e("event", "" + msg + result + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    private boolean hasFlashLight()
    {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);
    }


    private class performBackgroundpanicTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();

//                String Uid=SharedHelper.getKey(MapsActivity.this,"UserID");
                String bbb = SharedHelper.getKey(MapsActivity.this, "userID");


                attendance_object = jPost.panic_id(bbb, lat, lng);

                Log.e("attendanceobject", "" + attendance_object);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = attendance_object.getString("Message");
                String result = attendance_object.getString("Result");
                //  modelData.session_id = event_object.getString("SessionID");
                String user_identity = attendance_object.getString("UserIdentity");

                Log.e("event", "" + msg + result + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    private class performBackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();

                event_object = jPost.event_id(modelData.session_id, lat, lng, stepcount);


                Log.e("eventobject", "" + event_object);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = event_object.getString("Message");
                String result = event_object.getString("Result");
                //  modelData.session_id = event_object.getString("SessionID");
                String user_identity = event_object.getString("UserIdentity");

                Log.e("event", "" + msg + result + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }


    private class stoppatrol extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MapsActivity.this);
            // Set progressdialog title

            // Set progressdialog message
            mProgressDialog.setMessage("Stop Patrol Please Wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            //mProgressDialog.setCancelable(false);
            //mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // Show progressdialog
            // mProgressDialog.setMax(100);
            mProgressDialog.show();

        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... params) {
//            String id = SharedHelper.getKey(MapsActivity.this, "userID");
            try {
                JSONPost jPost = new JSONPost();
                endpatrolobject = jPost.endpatrol("Sessionend", modelData.session_id, current_lat, current_lng);

                Log.e("songs_endpatrolobject", "" + endpatrolobject);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = endpatrolobject.getString("Message");
                String userid = endpatrolobject.getString("Result");
                modelData.session_id = endpatrolobject.getString("SessionID");
                String user_identity = endpatrolobject.getString("UserIdentity");

                Log.e("LATLNG", "" + msg + userid + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                mProgressDialog.dismiss();
                mMap.clear();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class startqr extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MapsActivity.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            //mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();
                String id = SharedHelper.getKey(MapsActivity.this, "userID");
                loginobject = jPost.songs_id("Sessionstart", id, lat, lng);

                Log.e("songs_loginobject", "" + loginobject);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = loginobject.getString("Message");
                String userid = loginobject.getString("Result");
                modelData.session_id = loginobject.getString("SessionID");
                String user_identity = loginobject.getString("UserIdentity");
                Log.e("LATLNG", "" + msg + userid + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void args) {
            try {
                mProgressDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class getsession extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MapsActivity.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            //mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();
                loginobject = jPost.songs_id("attendance", modelData.userId, lat, lng);
                Log.e("songs_loginobject", "" + loginobject);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = loginobject.getString("Message");
                String userid = loginobject.getString("Result");
                modelData.session_id = loginobject.getString("SessionID");
                String user_identity = loginobject.getString("UserIdentity");
                Log.e("LATLNG", "" + msg + userid + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void args) {
            try {
                mProgressDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
//        mWakeLock.release();
        super.onPause();

//        if (!StrmStrng.isEmpty() && StrmStrng.equalsIgnoreCase("1")) {

        if (mRunning==true){

            startBackgroundThread();

            mRunning = true;
            if (mPrefs != null) {
                mPrefs.registerOnSharedPreferenceChangeListener(
                        mSharedPreferenceListener);
            } // if
            updatePrefCacheAndUi();
            tryStartCameraStreamer();
        }else {
//            stopBackgroundThread();

            mRunning = false;
            if (mPrefs != null)
            {
                mPrefs.unregisterOnSharedPreferenceChangeListener(
                        mSharedPreferenceListener);
            } // if
            ensureCameraStreamerStopped();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        }

//        stopBackgroundThread();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private void ensureCameraStreamerStopped()
    {
        if (mCameraStreamer != null)
        {
            mCameraStreamer.stop();
            mCameraStreamer = null;
        } // if
    }



    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        } else {
            isDualSimOrNot();

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setUpMapIfNeeded();

//        new totrigalarm().execute();
//        startBackgroundThread();
       /* if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }*/
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
//        hand.post(tred);

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));

        SHhandler.removeCallbacks(SHThread);
        Locationhand.removeCallbacks(LocationThread);

//        if (!StrmStrng.isEmpty() && StrmStrng.equalsIgnoreCase("1")) {

            if (mRunning==true){

            startBackgroundThread();

            mRunning = true;
            if (mPrefs != null) {
                mPrefs.registerOnSharedPreferenceChangeListener(
                        mSharedPreferenceListener);
            } // if
            updatePrefCacheAndUi();
            tryStartCameraStreamer();
        }else {
//            stopBackgroundThread();

            mRunning = false;
            if (mPrefs != null)
            {
                mPrefs.unregisterOnSharedPreferenceChangeListener(
                        mSharedPreferenceListener);
            } // if
            ensureCameraStreamerStopped();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        }

    }

    private void tryStartCameraStreamer()
    {
        if (mRunning && mPreviewDisplayCreated && mPrefs != null)
        {
            mCameraStreamer = new CameraStreamer(mCameraIndex, mUseFlashLight, mPort,
                    mPrevieSizeIndex, mJpegQuality, mPreviewDisplay);
            mCameraStreamer.start();
        } // if
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceListener =
            new SharedPreferences.OnSharedPreferenceChangeListener()
            {
                @Override
                public void onSharedPreferenceChanged(final SharedPreferences prefs,
                                                      final String key)
                {
                    updatePrefCacheAndUi();
                } // onSharedPreferenceChanged(SharedPreferences, String)

            };


    @Override
    protected void onStop() {
        super.onStop();
        if (startpatrol) {
            SHhandler.post(SHThread);
        }
//        else if (Shiftmode){
////            Locationhand.post(LocationThread);
//        }
        else {

        }


//        if (!StrmStrng.isEmpty() && StrmStrng.equalsIgnoreCase("1")) {

        if (mRunning==true){

            startBackgroundThread();

            mRunning = true;
            if (mPrefs != null) {
                mPrefs.registerOnSharedPreferenceChangeListener(
                        mSharedPreferenceListener);
            } // if
            updatePrefCacheAndUi();
            tryStartCameraStreamer();
        }else {
//            stopBackgroundThread();

            mRunning = false;
            if (mPrefs != null)
            {
                mPrefs.unregisterOnSharedPreferenceChangeListener(
                        mSharedPreferenceListener);
            } // if
            ensureCameraStreamerStopped();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        }

        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (time.equalsIgnoreCase("0")) {

        if (time.isEmpty()){

        }else{
            Am.cancel(pInt);
        }

//        stopBackgroundThread();

        mRunning = false;
        if (mPrefs != null)
        {
            mPrefs.unregisterOnSharedPreferenceChangeListener(
                    mSharedPreferenceListener);
        } // if
        ensureCameraStreamerStopped();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);


//        Toast.makeText(MapsActivity.this,"Smart 4z Alarm Canceled",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();}
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        gps = new GPSTracker(MapsActivity.this);
        currentlocation();
       /* stringLatitude = currentlocation.getLatitude();
        stringLongitude = currentlocation.getLongitude();
*/
        stringLatitude = gps.getLatitude();
        stringLongitude = gps.getLongitude();
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng ll = new LatLng(stringLatitude, stringLongitude);
        // mMap.addMarker(new MarkerOptions().position(new LatLng(stringLatitude,stringLongitude).title("Marker")));
        // mMap.addMarker(new MarkerOptions().position(new LatLng(stringLatitude, stringLongitude)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 21));

      /*  mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(stringLatitude, stringLongitude))
                      //  .snippet(String.valueOf(cacmera_address))
                        // .icon(BitmapDescriptorFactory.fromResource(R.drawable.markers))
                        .flat(true)
                        //  .title(Title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
*/
    }

    private void setUpMap2() {
        gps = new GPSTracker(MapsActivity.this);
//        currentlocation();
       /* stringLatitude = currentlocation.getLatitude();
        stringLongitude = currentlocation.getLongitude();
*/

        stringLatitude = gps.getLatitude();
        stringLongitude = gps.getLongitude();
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng ll = new LatLng(stringLatitude, stringLongitude);

        // mMap.addMarker(new MarkerOptions().position(new LatLng(stringLatitude,stringLongitude).title("Marker")));

        // mMap.addMarker(new MarkerOptions().position(new LatLng(stringLatitude, stringLongitude)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(ll).zoom(21).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16));

      /*  mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(stringLatitude, stringLongitude))
                      //  .snippet(String.valueOf(cacmera_address))
                        // .icon(BitmapDescriptorFactory.fromResource(R.drawable.markers))
                        .flat(true)
                        //  .title(Title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
*/
    }

    public void clearmap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    public Location currentlocation1() {

        if (mMap != null) {
            // Try to obtain the map from the SupportMapFragment.
            gps = new GPSTracker(MapsActivity.this);
            currentlocation = gps.getLocation();

            if (currentlocation != null) {
                current_lat = String.valueOf(currentlocation.getLatitude());
                current_lng = String.valueOf(currentlocation.getLongitude());
                Log.e("current_lat", current_lat);
                Log.e("current_lng", current_lng);
                SharedHelper.putKey(MapsActivity.this, "current_lat", current_lat);
                SharedHelper.putKey(MapsActivity.this, "current_lng", current_lng);
            }

            //noinspection deprecation
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {

                    String UName = SharedHelper.getKey(getApplicationContext(), "LName");

                    // TODO Auto-generated method stub
//                    if (startpatrol) {
//                        mMap.clear();
//                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title(UName));
                           /* line = mMap.addPolyline(new PolylineOptions().add(new LatLng(stringLatitude, stringLongitude), new LatLng(arg0.getLatitude(), arg0.getLongitude()))
                                    .width(10)
                                    .color(Color.MAGENTA));*/
                        stringLatitude = arg0.getLatitude();
                        stringLongitude = arg0.getLongitude();
                        lat = String.valueOf(stringLatitude);
                        lng = String.valueOf(stringLongitude);

                        new performBackgroundTask().execute();
//                    }
                    currentlocation = arg0;
                }
            });


        }
        return currentlocation;
    }


    public Location currentlocation() {

        if (mMap != null) {
            // Try to obtain the map from the SupportMapFragment.
            gps = new GPSTracker(MapsActivity.this);
            currentlocation = gps.getLocation();

            if (currentlocation != null) {
                current_lat = String.valueOf(currentlocation.getLatitude());
                current_lng = String.valueOf(currentlocation.getLongitude());
                Log.e("current_lat", current_lat);
                Log.e("current_lng", current_lng);
                SharedHelper.putKey(MapsActivity.this, "current_lat", current_lat);
                SharedHelper.putKey(MapsActivity.this, "current_lng", current_lng);
            }

            //noinspection deprecation
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {

                    String UName = SharedHelper.getKey(getApplicationContext(), "LName");

                    // TODO Auto-generated method stub
                    if (startpatrol) {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title(UName));
                           /* line = mMap.addPolyline(new PolylineOptions().add(new LatLng(stringLatitude, stringLongitude), new LatLng(arg0.getLatitude(), arg0.getLongitude()))
                                    .width(10)
                                    .color(Color.MAGENTA));*/
                        stringLatitude = arg0.getLatitude();
                        stringLongitude = arg0.getLongitude();
                        lat = String.valueOf(stringLatitude);
                        lng = String.valueOf(stringLongitude);

                        new performBackgroundTask().execute();
                    }
                    currentlocation = arg0;
                }
            });


        }
        return currentlocation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        final MenuItem menuItem = menu.findItem(R.id.testAction);
        shift=menu.findItem(R.id.ShiftStart);
        ParkOP=menu.findItem(R.id.ParkOptn);
        if (Shiftmode){
            shift.setTitle("End Shift");
        }else {
            shift.setTitle("Start Shift");
        }

        NoteThread=new Runnable() {
            @Override
            public void run() {
//                new performBackgroundTask4().execute();
                NcountValue=SharedHelper.getKey(getApplicationContext(),"NcountVal");
                if (NcountValue.isEmpty()){
                    iin=0;
                }else {
                    iin = Integer.parseInt(NcountValue);
                }
//                currentlocation1();
                Notehand.postDelayed(NoteThread,3000);
                menuItem.setIcon(buildCounterDrawable(iin, R.drawable.nobell));
//                Log.e("iin", "" + iin);

            }
        };Notehand.post(NoteThread);


        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NotificationCounter.setPendingNotificationsCount(0);
                SharedHelper.putKey(getApplicationContext(),"NcountVal", "0");
                Intent in=new Intent(getApplicationContext(),NoteList.class);
                startActivity(in);
//                Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

//            case R.id.testAction:
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                Toast.makeText(getApplicationContext(), "Test Notification", Toast.LENGTH_SHORT).show();

            case R.id.ShiftStart:
                if (Shiftmode){
                    Shiftmode=false;
                    Toast.makeText(getApplicationContext(),"Ending Shift",Toast.LENGTH_SHORT).show();
//                  Lochand.removeCallbacks(LocThread);
                    Lochand.removeCallbacks(LocThread);
//                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    shift.setTitle("Start Shift");
                    return true;
                }else {
                    Shiftmode=true;
                    Toast.makeText(getApplicationContext(),"Starting Shift",Toast.LENGTH_SHORT).show();
//                    SharedHelper.putKey(getApplicationContext(),"ShiftStatus","1");
                    Lochand.post(LocThread);
//                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    shift.setTitle("End Shift");
                    return true;
                }

            case R.id.ParkOptn:

                Intent insidentCopy=new Intent(getApplicationContext(),Incident_Report_copy.class);
                startActivity(insidentCopy);
                return true;

            case R.id.maptypeHYBRID:
                if (mMap != null) {
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    return true;
                }
            case R.id.maptypeNONE:
                if (mMap != null) {
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    return true;
                }
            case R.id.maptypeNORMAL:
                if (mMap != null) {
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    return true;
                }
            case R.id.maptypeSATELLITE:
                if (mMap != null) {
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    return true;
                }
            case R.id.maptypeTERRAIN:
                if (mMap != null) {
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    return true;
                }
//            case R.id.logout:
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                modelData.session_id = "";

        }
        return super.onOptionsItemSelected(item);

        // return true;
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // textView.setText("Step Counter Detected : " + value);
            stepcount = String.valueOf(value);
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            // textView.setText("Step Detector Detected : " + value);
        }
    }

    private class performBackgroundTask1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();

                event_object = jPost.event_id(modelData.session_id, lat, lng, stepcount);


                Log.e("eventobject1", "" + event_object);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = event_object.getString("Message");
                String result = event_object.getString("Result");
                //  modelData.session_id = event_object.getString("SessionID");
                String user_identity = event_object.getString("UserIdentity");

                Log.e("event", "" + msg + result + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    private class performBackgroundTask3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            lat= String.valueOf(currentlocation.getLatitude());
            lng= String.valueOf(currentlocation.getLongitude());
            String SSnId= String.valueOf(0);


            try {
                JSONPost jPost = new JSONPost();

                event_object = jPost.event_id(SSnId, lat, lng, stepcount);


                Log.e("eventobject3", "" + event_object);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = event_object.getString("Message");
                String result = event_object.getString("Result");
                //  modelData.session_id = event_object.getString("SessionID");
                String user_identity = event_object.getString("UserIdentity");

                Log.e("event3", "" + msg + result + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    private class performBackgroundTask4 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            lat= String.valueOf(currentlocation.getLatitude());
            lng= String.valueOf(currentlocation.getLongitude());
            String SSnId= String.valueOf(0);

            try {
                JSONPost jPost = new JSONPost();

                event_object = jPost.event_id(SSnId, lat, lng, stepcount);


                Log.e("eventobject1", "" + event_object);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = event_object.getString("Message");
                String result = event_object.getString("Result");
                //  modelData.session_id = event_object.getString("SessionID");
                String user_identity = event_object.getString("UserIdentity");

                Log.e("event", "" + msg + result + modelData.session_id + user_identity);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }



    private class totrigalarm extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Alarmhand2.removeCallbacks(AlarmThread2);
        }

        @Override
        protected Void doInBackground(Void... params) {
            AlarmThread=new Runnable() {
            @Override
            public void run() {
                String SName = SharedHelper.getKey(getApplicationContext(), "SessionID");
                String UName = SharedHelper.getKey(getApplicationContext(), "UserID");
                String UserName = SharedHelper.getKey(getApplicationContext(), "LName");
                JSONPost jPost = new JSONPost();
                try {
                    Alarmobject = jPost.alarmTriggerId(UserName);
                    Log.e("Alarmobject", "" + Alarmobject);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (Alarmobject != null) {
//                    Toast.makeText(getApplicationContext(), "JSON Object is null", Toast.LENGTH_SHORT).show();
                try {
                    Vibrate = Alarmobject.getString("Vibrate");
                    Camera = Alarmobject.getString("Camera");
//                    RepeatTime= Long.valueOf(Alarmobject.getString("Duration"));
                    time=Alarmobject.getString("Duration");

                    String Session_Id = SName;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Vibrate!=null&&Vibrate.equalsIgnoreCase("True")) {
                    try {
                        Intent In = new Intent(MapsActivity.this, AlarmActivity.class);
                        pInt = PendingIntent.getActivities(MapsActivity.this, 12345, new Intent[]{In}, PendingIntent.FLAG_CANCEL_CURRENT);
//            PendingIntent Pint=PendingIntent.getBroadcast(this,0,In,0);
                        Am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
//            Am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),2*60*1000,pInt);
//            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR,pInt);
//                        Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),30*60*1000,pInt);

//                        if (time.equalsIgnoreCase("0")) {

                        if (time.isEmpty()){

                            Am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pInt);
                        }else {
                            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), Long.parseLong(time),pInt);
                        }
//                        Toast.makeText(getApplicationContext(),"Alarm will start after 30 Mins", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }else {
//                    Toast.makeText(getApplicationContext(),"Vibrate Object is null",Toast.LENGTH_SHORT).show();
                }

                Alarmhand.postDelayed(this, 20 * 60 * 1000);
//                Toast.makeText(getApplicationContext(),"AlarmThread is started",Toast.LENGTH_SHORT).show();
            }else{

                }
            }
        };Alarmhand.post(AlarmThread);
            return null;
        }
    }

    private class totrigalarm2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Alarmhand.removeCallbacks(AlarmThread);
//            Am.cancel(pInt);
        }

        @Override
        protected Void doInBackground(Void... params) {



            AlarmThread2=new Runnable() {
                @Override
                public void run() {
                    String SName = SharedHelper.getKey(getApplicationContext(), "SessionID");
                    String UName = SharedHelper.getKey(getApplicationContext(), "UserID");
                    String UserName = SharedHelper.getKey(getApplicationContext(), "LName");
                    JSONPost jPost = new JSONPost();
                    try {
                        Alarmobject = jPost.alarmTriggerId(UserName);
                        Log.e("Alarmobject", "" + Alarmobject);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (Alarmobject !=null) {
//                    Toast.makeText(getApplicationContext(), "JSON Object is null", Toast.LENGTH_SHORT).show();

                        try {
                            Vibrate1 = Alarmobject.getString("Vibrate");
                            Camera1 = Alarmobject.getString("Camera");
//                        RepeatTime= Long.valueOf(Alarmobject.getString("Duration"));
                            time1=Alarmobject.getString("Duration");

                            String Session_Id = SName;





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        if (Vibrate!=null&&Vibrate.equalsIgnoreCase("True")) {
//                            try {
//                                Intent In = new Intent(MapsActivity.this, AlarmActivity.class);
//                                pInt = PendingIntent.getActivities(MapsActivity.this, 12345, new Intent[]{In}, PendingIntent.FLAG_CANCEL_CURRENT);
////            PendingIntent Pint=PendingIntent.getBroadcast(this,0,In,0);
//                                Am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
////            Am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),2*60*1000,pInt);
////            Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR,pInt);
////                        Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),30*60*1000,pInt);
//                                if (time.equalsIgnoreCase("0")){
//                                    Am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pInt);
//                                }else {
//                                    Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), Long.parseLong(time),pInt);
//                                }
////                        Toast.makeText(getApplicationContext(),"Alarm will start after 30 Mins", Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//
//                            }
//                        }else {
////                        Toast.makeText(getApplicationContext(),"Vibrate Object is null",Toast.LENGTH_SHORT).show();
//                        }

                        Alarmhand2.postDelayed(this, 20 * 1000);
//                Toast.makeText(getApplicationContext(),"AlarmThread2 is started",Toast.LENGTH_SHORT).show();
                    }else{
//                        Toast.makeText(getApplicationContext(),"AlarmThread2 is started",Toast.LENGTH_SHORT).show();
                    }
                }
            };Alarmhand2.post(AlarmThread2);
            return null;
        }
    }

    private class Upload_detail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   preloader.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONPost jsonPost = new JSONPost();

            try {
                JSONObject jsonObject = jsonPost.Gcm_Notification_method("Android", token, imeiSIM1 + "," + imeiSIM2);
                Log.e("GCm_json_object", "" + jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


        }

    }

    private final class LoadPreferencesTask extends AsyncTask<Void, Void, SharedPreferences> {

        private LoadPreferencesTask()
        {
            super();
        }

        @Override
        protected SharedPreferences doInBackground(final Void... params) {
            return PreferenceManager.getDefaultSharedPreferences(
                    MapsActivity.this);
        }

        @Override
        protected void onPostExecute(final SharedPreferences sharedPreferences) {
            MapsActivity.this.mPrefs = sharedPreferences;
            sharedPreferences.registerOnSharedPreferenceChangeListener(
                    mSharedPreferenceListener);
            updatePrefCacheAndUi();
            tryStartCameraStreamer();
        }
    }

}

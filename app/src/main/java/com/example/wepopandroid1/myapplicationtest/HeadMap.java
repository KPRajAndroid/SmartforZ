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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.wepopandroid1.myapplicationtest.MainActivity.hasPermissions;
import static com.example.wepopandroid1.myapplicationtest.R.id.maptype1;
import static com.example.wepopandroid1.myapplicationtest.R.id.maptypeHYBRID;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class HeadMap extends AppCompatActivity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    JSONObject scanobject = null;
    JSONObject Alarmobject = null;
    String format, contents;
    model modelData;
    Button start, qr_but, stop_but, attendance, incidentreport, panic;
    //    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
//    GPSTracker gps;
//    double stringLatitude, stringLongitude;
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
//    GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    PendingIntent pInt;
    AlarmManager Am;
    Handler Alarmhand, Alarmhand2;
    Runnable AlarmThread, AlarmThread2;
    String Camera, Camera1;
    String Vibrate = null;
    String Vibrate1 = null;
    String time, time1;
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

    //--------------------------------Security head--------------
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    GPSTracker gps;
    double stringLatitude, stringLongitude;
    double stringLatitude1, stringLongitude1;
    JSONObject SHead_Object = null;
    JSONObject SHNotification_Object = null;
    String msg, result;
    JSONArray securitylist;
    Double d1, d2;
    LatLng llt;
    Marker mark, mark1, mark2, mark3;
    String Name, num, SStatus, SHname, SLid;
    Handler hand, SHhandler;
    Runnable tred, SHThread;
    private Context mContext;
    LatLngBounds.Builder builder;
    CameraUpdate cu;
    List<Marker> Mlist;
    Location CLocation;
    private String ScanQrString;
    private String ScanQrSt;
    EditText et;
    Button B;
    PopupWindow popwindo;
    EditText ed;
    String ch;
    AlertDialog ad;
    private PopupWindow pwindo2;

//----------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mContext = this;

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        hand = new Handler();
        SHhandler = new Handler();

//        ToTurnOnGps();

        tred = new Runnable() {
            @Override
            public void run() {
                setUpMapIfNeededH();
                hand.postDelayed(tred, 20 * 1000);
//                Toast.makeText(getApplicationContext(),"toastthread",Toast.LENGTH_SHORT).show();
            }
        };
        hand.post(tred);


        SHThread = new Runnable() {
            @Override
            public void run() {
                new performBackgroundTask1().execute();
                SHhandler.postDelayed(SHThread, 5000);
            }
        };

        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        Camhandler = new Handler();
        Alarmhand = new Handler();
        Alarmhand2 = new Handler();

        ToTurnOnGps();

        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        new totrigalarm().execute();

        extras = getIntent().getExtras();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
//        setUpMapIfNeededH();
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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedHelper.putKey(getApplicationContext(), "StPtrlSts", "1");

                stop_but.setEnabled(true);
                qr_but.setEnabled(true);
//                attendance.setEnabled(true);
//                incidentreport.setEnabled(true);
//                panic.setEnabled(true);

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

        qr_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                qr_but.setEnabled(true);

                initiatePopupWindow();

            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
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

                } catch (Exception ex) {

                    Toast.makeText(getApplicationContext(),
                            "Please ON the gps and data connection",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

        incidentreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(HeadMap.this, Incident_Report.class);
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

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (stopsound) {
                        mp.pause();
                        Camhandler.removeCallbacks(CamThread);
                        new totrigalarm().execute();
//                            Toast.makeText(getApplicationContext(),"Camera stopped",Toast.LENGTH_SHORT).show();
                        stopsound = false;
                    } else {

                        AlertDialog.Builder aldb = new AlertDialog.Builder(HeadMap.this);
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
                                        /*if (Camera.equalsIgnoreCase("True") && Camera != null) {
                                            CamThread = new Runnable() {
                                                @Override
                                                public void run() {
                                                    takePicture();
                                                    Camhandler.postDelayed(this, 3000);
                                                }
                                            };
                                            Camhandler.post(CamThread);

                                        } else {

//                                                Toast.makeText(getApplicationContext(),"Alarm Object Null",Toast.LENGTH_SHORT).show();
                                        }*/


                                        Toast.makeText(getApplicationContext(),
                                                "Panic Alarm Activated",
                                                Toast.LENGTH_LONG).show();

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

        stop_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedHelper.putKey(getApplicationContext(), "StPtrlSts", "2");

                start.setEnabled(true);
                qr_but.setEnabled(false);
//                attendance.setEnabled(false);
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


//        gps = new GPSTracker(HeadMap.this);
//        LatLng ll = new LatLng(gps.getLatitude(), gps.getLongitude());
//
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(ll).zoom(16).build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        setUpMap2();

//        TosetMarker();

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
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(HeadMap.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
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
        String num = String.valueOf(n++);
        String fname = "IMG_" + num + ".jpg";

        if (cameraDevice == null) {
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
            final File file = new File(myDir, fname);
            if (file.exists()) {
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
                    Toast.makeText(HeadMap.this, "Saved:" + file, Toast.LENGTH_SHORT).show();

                    final String Id = SharedHelper.getKey(HeadMap.this, "userID");

//                    new TosendImage().execute();

                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                    executorService.execute(new Runnable() {
                        public void run() {

                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://aflaree.com/tetrasecurity/securityservice/getImages");
                            multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                            ImgFile = new File(String.valueOf(file));
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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HeadMap.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
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
                Toast.makeText(HeadMap.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
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
        butOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Something
                Intent in = new Intent(HeadMap.this, ToReadNFC.class);
                startActivity(in);

                //Close Window
                pwindo.dismiss();
            }
        });

        // Getting a reference to button two and do something
        Button butTwo = (Button) layout.findViewById(R.id.layout_popup_butTwo);
        butTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedHelper.putKey(HeadMap.this, "ScanKey", "1");

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

                            IntentIntegrator integrator = new IntentIntegrator(HeadMap.this);
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
                            showDialog(HeadMap.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
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

        String Sv = SharedHelper.getKey(getApplicationContext(), "ScanKey");


        if (scanningResult != null) {

            if (Sv.equalsIgnoreCase("1")) {
                contents = scanningResult.getContents();
                format = scanningResult.getFormatName();

                Log.e("scancode", "Content:" + contents + " Format:" + format);


                new scanqrt().execute();

            } else if (Sv.equalsIgnoreCase("2")) {

                contents = scanningResult.getContents();
                format = scanningResult.getFormatName();

                if (contents != null) {
                    initCusAlert();
                } else {

                }

                Log.e("scancode", "Content:" + contents + " Format:" + format);

                new scanqrt1().execute();
            }
        } else if (scanningResult == null && resultCode == RESULT_OK) {

            final Handler hndle = new Handler();
            hndle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpMapIfNeededH();
                }
            }, 3000);

            Toast.makeText(getApplicationContext(), "GpS Enabled", Toast.LENGTH_SHORT).show();

        } else if (scanningResult == null && resultCode == RESULT_CANCELED) {

            Toast.makeText(getApplicationContext(), "GpS Enabling Aborted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Scaning data received", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCusAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.tolocation, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Enter location Name");

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ScanQrSt = edt.getText().toString();

                if (ScanQrSt.isEmpty()) {
                    initCusAlert();
                    Toast.makeText(getApplicationContext(), "Location Value Can't Be Empty", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.show();
    }


    private void TosetMarker() {


        if (securitylist != null && securitylist.length() > 0) {
            mMap.clear();

            Mlist = new ArrayList<Marker>();
            int m;

            String UsName = SharedHelper.getKey(HeadMap.this, "LName");
            SHname = SharedHelper.getKey(HeadMap.this, "NameOfSH");

            gps = new GPSTracker(HeadMap.this);
//            CLocation=gps.getLocation();

            if (startpatrol) {
                mark1 = mMap.addMarker(new MarkerOptions().position(new LatLng(CLocation.getLatitude(), CLocation.getLongitude())).title(UsName));

                Mlist.add(mark1);
            }

//            Mlist.add(mark2);

            for (int i = 0; i < securitylist.length(); i++) {

                m = i;

                try {
                    JSONObject JObj = securitylist.getJSONObject(i);
                    d1 = Double.valueOf(JObj.optString("Lat"));
                    d2 = Double.valueOf(JObj.optString("lng"));
                    Name = JObj.optString("Name");
                    num = JObj.optString("mobileno");
                    SLid = JObj.optString("Loginid");
                    SStatus = JObj.optString("securitystatus");
                    llt = new LatLng(d1, d2);
                    if (SStatus.equalsIgnoreCase("Offline")) {
                        mark = mMap.addMarker(new MarkerOptions().position(llt).snippet(Name + "\n" + "Number: " + num + "\n" + "RoamingStatus: " + SStatus).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));


                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                LinearLayout info = new LinearLayout(mContext);
                                info.setOrientation(LinearLayout.VERTICAL);


                                TextView Tv1 = new TextView(mContext);
                                Tv1.setGravity(Gravity.CENTER);
                                Tv1.setTypeface(null, Typeface.BOLD);
                                Tv1.setText(marker.getTitle());

                                TextView Tv2 = new TextView(mContext);
                                Tv2.setGravity(Gravity.CENTER);
                                Tv2.setTextColor(Color.BLACK);
                                Tv2.setText(marker.getSnippet());

//                                Button B=new Button(mContext);
//                                B.setText("Msg");
//                                B.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Toast.makeText(getApplicationContext(),"Toast__1",Toast.LENGTH_SHORT).show();
//                                    }
//                                });

                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {

                                        optionPopup();

//                                        Toast.makeText(getApplicationContext(),"Toast__1",Toast.LENGTH_SHORT).show();
                                        marker.hideInfoWindow();
                                    }
                                });

                                info.addView(Tv1);
                                info.addView(Tv2);
//                                info.addView(B);

                                return info;
                            }
                        });

                    } else {
                        mark = mMap.addMarker(new MarkerOptions().position(llt).snippet(Name + "\n" + "Number: " + num + "\n" + "RoamingStatus: " + SStatus).icon(BitmapDescriptorFactory.fromResource(R.drawable.place)));

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(mContext);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView Tv1 = new TextView(mContext);
                                Tv1.setGravity(Gravity.CENTER);
                                Tv1.setTypeface(null, Typeface.BOLD);
                                Tv1.setText(marker.getTitle());

                                TextView Tv2 = new TextView(mContext);
                                Tv2.setGravity(Gravity.CENTER);
                                Tv2.setTextColor(Color.BLACK);
                                Tv2.setText(marker.getSnippet());

//                                Button b=new Button(mContext);
//                                B.setText("Msg");
//                                b.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Toast.makeText(getApplicationContext(),"Toast__2",Toast.LENGTH_SHORT).show();
//                                    }
//                                });


                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {

                                        optionPopup();

//                                        Toast.makeText(getApplicationContext(),"Toast__2",Toast.LENGTH_SHORT).show();
                                        marker.hideInfoWindow();
                                    }
                                });


                                info.addView(Tv1);
                                info.addView(Tv2);
//                                info.addView(b);

                                return info;
                            }
                        });


                    }

                    Mlist.add(mark);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            builder = new LatLngBounds.Builder();
            for (Marker M : Mlist) {
                builder.include(M.getPosition());
            }
            int padding = 50;
            LatLngBounds bounds = builder.build();
            cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(cu);
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_SHORT).show();
            LatLng ll = new LatLng(stringLatitude, stringLongitude);
            Log.e("ll", "" + ll);
            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(16).target(ll).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.clear();
        }
    }

    private void optionPopup() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popuptocallmsg, null);

        // Creating the PopupWindow
        pwindo2 = new PopupWindow(this);
        pwindo2.setContentView(layout);
        pwindo2.setWidth(width);
        pwindo2.setHeight(height);
        pwindo2.setFocusable(true);
        pwindo2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pwindo2.setOutsideTouchable(true);

        // prevent clickable background
        pwindo2.setBackgroundDrawable(null);
        pwindo2.showAtLocation(layout, Gravity.CENTER, 1, 1);


        // Getting a reference to button one and do something
        Button butOne = (Button) layout.findViewById(R.id.messages);
        butOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Something
                initiatePopup1();
                //Close Window
                pwindo2.dismiss();
            }
        });

        // Getting a reference to button two and do something
        Button butTwo = (Button) layout.findViewById(R.id.calls);
        butTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Something
                Intent intent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:" + num));
                if (ActivityCompat.checkSelfPermission(HeadMap.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                //Close Window
                pwindo2.dismiss();
            }
        });
    }

    private void initiatePopup1() {
        AlertDialog.Builder Adb=new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater Linf=this.getLayoutInflater();
        final View aview=Linf.inflate(R.layout.cusalert2,null);
        Adb.setView(aview);

        final EditText eddt=(EditText) aview.findViewById(R.id.succes_id);
//        eddt.setBackgroundResource(R.drawable.backtext);
        Button B1=(Button)aview.findViewById(R.id.send_button);
        Button B2=(Button)aview.findViewById(R.id.cancelbutton);
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SHid = SharedHelper.getKey(HeadMap.this, "userID");
                String S=eddt.getText().toString();
                JSONPost Jpst = new JSONPost();
                SHNotification_Object = Jpst.Notification_id(SLid, S, SHid);
                Log.e("SHNotification_Object", "" + SHNotification_Object);

                try{
                    msg = SHNotification_Object.getString("Message");
                    result = SHNotification_Object.getString("Result");

                    Log.e("SHNotification", "" + msg + result);
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(),"Test Alert:"+S,Toast.LENGTH_SHORT).show();
                ad.dismiss();
            }
        });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
//        final TextView Txtvw=(TextView)findViewById(R.id.alertTv);

//        Adb.setTitle("Enter Your Message");
//        Adb.setMessage("Enter Your Message");
//        Adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                edd.setBackgroundColor(Color.BLACK);
////                Txtvw.setText(edd.getText().toString());
//
//                String S=eddt.getText().toString();
//
//Toast.makeText(getApplicationContext(),"Test Alert:"+S,Toast.LENGTH_SHORT).show();
//            }
//        });
        ad=Adb.create();
        ad.show();
        ad.setCanceledOnTouchOutside(false);
    }

    private void initiatePopup() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popupwithedit, null);

        // Creating the PopupWindow
        popwindo = new PopupWindow(this);
        popwindo.setContentView(layout);
        popwindo.setWidth(width);
//        popwindo.setHeight(height);
        popwindo.setFocusable(true);
        popwindo.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popwindo.setOutsideTouchable(true);

        // prevent clickable background
        popwindo.setBackgroundDrawable(null);
        popwindo.showAtLocation(layout, Gravity.CENTER, 1, 1);
        popwindo.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popwindo.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        // Getting a reference to button one and do something
        // Getting a reference to button two and do something
        final EditText edd=(EditText)layout.findViewById(R.id.Popedit);
//        final TextView tv=(TextView)findViewById(R.id.PopTv);
        Button butTwo = (Button) layout.findViewById(R.id.popup_send_but);

        butTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Do Something
                //Close Window
//                ed.setBackgroundColor(Color.BLACK);
//                tv.setText(ed.getText().toString());
//                ed.setText(ch);
                String cch=edd.getText().toString();
                Toast.makeText(getApplicationContext(),"Test:"+ cch,Toast.LENGTH_SHORT).show();
                popwindo.dismiss();
            }
        });
//        Toast.makeText(getApplicationContext(),"Test:"+cch,Toast.LENGTH_SHORT).show();
    }

    private void setUpMapIfNeededH() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            setUpMap();

            new Topost().execute();

        }

    }

    private void setUpMap() {
        gps = new GPSTracker(HeadMap.this);

        stringLatitude = gps.getLatitude();
        stringLongitude = gps.getLongitude();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng ll = new LatLng(stringLatitude, stringLongitude);

    }

    private void setUpMap2() {
        gps = new GPSTracker(HeadMap.this);

        stringLatitude = gps.getLatitude();
        stringLongitude = gps.getLongitude();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng ll = new LatLng(stringLatitude, stringLongitude);

        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(16).target(ll).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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
//            SharedHelper.putKey(getApplicationContext(),"StepsVal",stepcount);
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            // textView.setText("Step Detector Detected : " + value);
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
                                Intent In = new Intent(HeadMap.this, AlarmActivity.class);
                                pInt = PendingIntent.getActivities(HeadMap.this, 12345, new Intent[]{In}, PendingIntent.FLAG_CANCEL_CURRENT);
                                Am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                                if (time.equalsIgnoreCase("0")) {
                                    Am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pInt);
                                }else {
                                    Am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), Long.parseLong(time),pInt);
                                }
                            } catch (Exception e) {

                            }
                        }else {
                        }

                        Alarmhand.postDelayed(this, 20 * 60 * 1000);
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

                        try {
                            Vibrate1 = Alarmobject.getString("Vibrate");
                            Camera1 = Alarmobject.getString("Camera");
                            time1=Alarmobject.getString("Duration");

                            String Session_Id = SName;





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Alarmhand2.postDelayed(this, 20 * 1000);
                    }else{
                    }
                }
            };Alarmhand2.post(AlarmThread2);
            return null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    private class scanqrt1 extends AsyncTask<Void, Void, Void> {

        String Userid = SharedHelper.getKey(HeadMap.this, "userID");


        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONPost jPost = new JSONPost();

                scanobject = jPost.sacn_id1(Userid, contents, lat, lng,ScanQrSt);

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
                String id = SharedHelper.getKey(HeadMap.this, "userID");
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

    private class performBackgroundpanicTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();

//                String Uid=SharedHelper.getKey(MapsActivity.this,"UserID");
                String bbb = SharedHelper.getKey(HeadMap.this, "userID");


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

    public class performBackgroundTask1 extends AsyncTask<Void, Void, Void> {

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
            mProgressDialog = new ProgressDialog(HeadMap.this);
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class startqr1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            mProgressDialog = new ProgressDialog(HeadMap.this);
//            mProgressDialog.setMessage("Please wait...");
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setCanceledOnTouchOutside(true);
//            //mProgressDialog.setCancelable(false);
//            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();
                String id = SharedHelper.getKey(HeadMap.this, "userID");
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
//                mProgressDialog.dismiss();

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
            mProgressDialog = new ProgressDialog(HeadMap.this);
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
                String id = SharedHelper.getKey(HeadMap.this, "userID");
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
            mProgressDialog = new ProgressDialog(HeadMap.this);
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





    private class Topost extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONPost jPost = new JSONPost();
                String id = SharedHelper.getKey(HeadMap.this, "userID");
//                String iid="135";
                SHead_Object=jPost.headmap_id(id);
                Log.e("SHead_Object", "" + SHead_Object);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                msg = SHead_Object.getString("Message");
                result = SHead_Object.getString("Result");
                securitylist=SHead_Object.getJSONArray("securityli");

                Log.e("SHead", "" + msg + result + securitylist);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TosetMarker();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setUpMapIfNeededH();

        String Psts=SharedHelper.getKey(getApplicationContext(),"StPtrlSts");
        if (Psts.equalsIgnoreCase("1")){
            dopatrol();
        }

        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
        hand.post(tred);
        SHhandler.removeCallbacks(SHThread);
//        setUpMapIfNeeded();
//        new Topost().execute();
    }

    private void dopatrol() {

        SharedHelper.putKey(getApplicationContext(),"StPtrlSts","1");

        stop_but.setEnabled(true);
        qr_but.setEnabled(true);
//                attendance.setEnabled(true);
//                incidentreport.setEnabled(true);
//                panic.setEnabled(true);

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
                new startqr1().execute();
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

    @Override
    public void onPause() {
        super.onPause();
//        stopBackgroundThread();
        hand.removeCallbacks(tred);
//        SHhandler.post(SHThread);
        Log.e("LockStatus", "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

//        mSensorManager.unregisterListener(this, mStepCounterSensor);
//        mSensorManager.unregisterListener(this, mStepDetectorSensor);

        if (startpatrol) {
            SHhandler.post(SHThread);
        }
//        hand.removeCallbacks(tred);
        Log.e("LockStatus", "OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Am.cancel(pInt);
        hand.removeCallbacks(tred);
    }

    public void clearmap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    public Location currentlocation() {

        if (mMap != null) {
            // Try to obtain the map from the SupportMapFragment.
            gps = new GPSTracker(HeadMap.this);
            currentlocation = gps.getLocation();

            if (currentlocation != null) {
                current_lat = String.valueOf(currentlocation.getLatitude());
                current_lng = String.valueOf(currentlocation.getLongitude());
                Log.e("current_lat", current_lat);
                Log.e("current_lng", current_lng);
                SharedHelper.putKey(HeadMap.this, "current_lat", current_lat);
                SharedHelper.putKey(HeadMap.this, "current_lng", current_lng);
            }

            //noinspection deprecation
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {

                    String UName = SharedHelper.getKey(getApplicationContext(), "LName");
                    CLocation=arg0;


                    // TODO Auto-generated method stub
                    if (startpatrol) {

                    CameraPosition Campos= new CameraPosition.Builder().zoom(30).target(new LatLng(arg0.getLatitude(), arg0.getLongitude())).build();
                        String S= SharedHelper.getKey(getApplicationContext(),"MarkVal");

//                        if (S.equalsIgnoreCase("1")){
//                            mark2.remove();
//
//                        }

//                        if (mark2==null){
//                            mark2 = mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title(UName));
//                        }else {
//                            mark2.remove();
//                            mark2=null;
//
//                        }
////                        mark3=mark2;
////                        mark3.setVisible(false);
//
//                        SharedHelper.putKey(getApplicationContext(),"MarkVal","1");

//                       mark2= mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title(UName));
//                        Mlist.add(mark2);


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
        getMenuInflater().inflate(R.menu.menu_maps_1, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case maptype1:
                toScanqr();

            case R.id.maptypeNORMAL:
                if (mMap != null) {
                    SharedHelper.putKey(HeadMap.this, "MapMode","NORMAL");
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    return true;
                }
            case maptypeHYBRID:
                if (mMap != null) {
                    SharedHelper.putKey(HeadMap.this, "MapMode","HYBRID");
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    return true;
                }
            case R.id.maptypeNONE:
                if (mMap != null) {
                    SharedHelper.putKey(HeadMap.this, "MapMode","NONE");
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    return true;
                }
            case R.id.maptypeSATELLITE:
                if (mMap != null) {
                    SharedHelper.putKey(HeadMap.this, "MapMode","SATELLITE");
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    return true;
                }
            case R.id.maptypeTERRAIN:
                if (mMap != null) {
                    SharedHelper.putKey(HeadMap.this, "MapMode","TERRAIN");
                    item.setChecked(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    return true;
                }

        }
        return super.onOptionsItemSelected(item);

    }

    private void toScanqr() {
            try {

                SharedHelper.putKey(HeadMap.this, "ScanKey", "2");

                stringLatitude = currentlocation.getLatitude();
                stringLongitude = currentlocation.getLongitude();
                lat = String.valueOf(stringLatitude);
                lng = String.valueOf(stringLongitude);
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ni != null)) {
                    try {

                        IntentIntegrator integrator = new IntentIntegrator(HeadMap.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt("Scan a QRCode");
                        integrator.setResultDisplayDuration(0);
                        // integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                        integrator.setCameraId(0);  // Use a specific camera of the device
                        integrator.initiateScan();

                    } catch (ActivityNotFoundException anfe) {
                        showDialog(HeadMap.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please ON the gps and data connection", Toast.LENGTH_LONG).show();

                }
            } catch (Exception ex) {

                Toast.makeText(getApplicationContext(),
                        "Please ON the gps and data connection",
                        Toast.LENGTH_LONG).show();

            }

    }


    private void ToTurnOnGps() {
        if(mGoogleApiClient==null) {
            mGoogleApiClient = new GoogleApiClient.Builder(HeadMap.this)
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
                                status.startResolutionForResult(HeadMap.this,1000);
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
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                setUpMapIfNeededH();
                MapsInitializer.initialize(HeadMap.this);
            }
        }
    }

}

package com.example.wepopandroid1.myapplicationtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Incident_Report extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY_NAME = "TETRA FILE UPLOAD";
    String mCurrentPhotoPath, imagesPath1 = "";
    int status;
    static final int REQUEST_TAKE_PHOTO = 1;
    File photoFile = null, image;
    static final String FTP_HOST = "308.3d8.myftpupload.com";
    static final String FTP_USER = "wepopusers";
    static final String FTP_PASS = "Chandru@123";
    private ImageView view_image;
    String URL = "http://wepopar.com/wepopusers/Rajkumar/";
    private final int requestCode = 20;
    Intent photoCaptureIntent;
    String URL_PATH, imageFileName;
    ListView list_view;
    ProgressDialog progressDialog;
    static final String TITLE = "title";
    ArrayList<HashMap<String, String>> arrayList;
    ArrayList<String> incidents = new ArrayList<String>();
    Incident_details.Incident_Adapter adapter;
    Button incident_btn, sign_reg;
    Bundle extras;
    String lat, lng, SessionID;
    private Uri imageToUploadUri;
    JSONObject incident_object, allproduct_obj;
    public static final int SIGNATURE_ACTIVITY = 1;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    long totalSize = 0;
    ProgressDialog mProgressDialog;
    String FILE_UPLOAD_URL ="http://aflaree.com/tetrasecurity/securityservice/Incidents";
//            "http://ec2-52-66-74-239.ap-south-1.compute.amazonaws.com/web/home/Incidents";
    //http://ec2-52-66-98-212.ap-south-1.compute.amazonaws.com/web
    //http://ec2-52-66-74-239.ap-south-1.compute.amazonaws.com/Services/Service1.svc
    JSONArray jsonarray;
    String path = "", request_id = "", value = "";
    ImageView ivImage;
    Button capt_btn;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0;
    File destination, save_file, destination2;

    GPSTracker gps;
    Double lattitude, longitude;
    String strlat = "", strlong = "", fname;
    model modelData;
    ArrayList<String> MAP;


    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    private EditText yourName;
    MultipartEntity multipartEntity;
    File sourceFile, signature_image;
    Helper1 h1;
    ScrollView Sv;
    String bb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        modelData = model.getInstance();
        extras = getIntent().getExtras();

        h1=new Helper1();

        getSupportActionBar().setCustomView(R.layout.abs_layout);
        setContentView(R.layout.incident_report);
        MAP = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        capt_btn = (Button) findViewById(R.id.capture_btn);
        // view_image = (ImageView) findViewById(R.id.image_view);
        list_view = (ListView) findViewById(R.id.listview);
        incident_btn = (Button) findViewById(R.id.button);
        sign_reg = (Button) findViewById(R.id.sign_reg);
//        Sv=(ScrollView)findViewById(R.id.ScId);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        capt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        //incidentbuttonpressed();
        Log.e("USerID", modelData.userId);
        // new Incident_details().execute();
        new Incident_details().execute("http://aflaree.com/patrolservice/Service1.svc/Incidentlists");

//        http://ec2-52-66-74-239.ap-south-1.compute.amazonaws.com/Services/Service1.svc/Incidentlists
        //  http://ec2-52-66-98-212.ap-south-1.compute.amazonaws.com/web/home/Incidents

        sign_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Incident_Report.this, CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);*/

                final CustomDialog customDialog = new CustomDialog(Incident_Report.this);
                customDialog.setContentView(R.layout.signature);
                customDialog.setCancelable(false);
                customDialog.show();

                mContent = (LinearLayout) customDialog.findViewById(R.id.linearLayout);
                mSignature = new signature(Incident_Report.this, null);
                mSignature.setBackgroundColor(Color.WHITE);
                //noinspection deprecation
                mContent.addView(mSignature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                mClear = (Button) customDialog.findViewById(R.id.clear);
                mGetSign = (Button) customDialog.findViewById(R.id.getsign);
                mGetSign.setEnabled(false);
                mCancel = (Button) customDialog.findViewById(R.id.cancel);
                yourName = (EditText) customDialog.findViewById(R.id.yourName);

                mGetSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yourName.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(Incident_Report.this, "Please enter the name", Toast.LENGTH_SHORT).show();
                        } else {
                            mContent.setDrawingCacheEnabled(true);
                            save(mContent, customDialog);
                        }

                    }
                });
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
                mClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSignature.clear();
                        mGetSign.setEnabled(false);
                    }
                });

            }
        });

        incident_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (destination == null) {
                    Toast.makeText(Incident_Report.this, "Please capture incident image", Toast.LENGTH_SHORT).show();
                } else if (signature_image == null) {
                    Toast.makeText(Incident_Report.this, "Please put your signature", Toast.LENGTH_SHORT).show();
                }
                else if (MAP.size() == 0) {
                    Toast.makeText(Incident_Report.this, "Please select the incident list", Toast.LENGTH_SHORT).show();
                } else {
                    incidentbuttonpressed();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                }
                break;
        }
    }

    public void save(View v, CustomDialog customDialog) {
        Log.v("log_tag", "Width: " + v.getWidth());
        Log.v("log_tag", "Height: " + v.getHeight());
        Bitmap bitmap = v.getDrawingCache();

        File root = Environment.getExternalStorageDirectory();
        File myDir = new File(root + "/saved_images");
        if (!myDir.isDirectory())
            myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image_" + n + ".jpg";
        save_file = new File(myDir, fname);
        Canvas canvas = new Canvas(bitmap);
        if (save_file.exists()) save_file.delete();
        try {
            FileOutputStream out = new FileOutputStream(save_file);
            v.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        customDialog.dismiss();
        signature_image = new File(String.valueOf(save_file));

    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }


        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        destination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".jpg");
        Log.d("destinatiooonss", String.valueOf(destination));
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
        sourceFile = new File(String.valueOf(destination));
    }

    private class Incident_details extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {

            JSONParser jParser = new JSONParser();


            HttpGet httppost = new HttpGet(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // StatusLimodelDatane stat = response.getStatusLine();
            try {
                status = response.getStatusLine().getStatusCode();
            } catch (Exception ee) {

            }
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = null;
                try {
                    data = EntityUtils.toString(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    incident_object = new JSONObject(data);

                    Log.e("jsonobject_product", "" + incident_object);

                    arrayList = new ArrayList<HashMap<String, String>>();
                    arrayList.clear();


                    if (incident_object != null) {
                        try {
                            jsonarray = incident_object.getJSONArray("Incidentli");
                            for (int n = 0; n < jsonarray.length(); n++) {
                                allproduct_obj = jsonarray.getJSONObject(n);

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("ID", allproduct_obj.getString("ID"));
                                map.put("Incidents", allproduct_obj.getString("Incidents"));

                                Log.e("map", "" + map);
                                arrayList.add(map);
                                Log.e("productlist", "" + arrayList);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
                // filterList = new ArrayList<HashMap<String, String>>(Songsarraylist);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                adapter = new Incident_Adapter(Incident_Report.this, arrayList);

                list_view.setAdapter(adapter);
                h1.getListViewSize(list_view);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public class Incident_Adapter extends BaseAdapter {
            Context context;
            boolean[] checkBoxState;
            LayoutInflater inflater;
            ArrayList<HashMap<String, String>> data;
            // ImageLoad imageLoad;
            String message, status;
            HashMap<String, String> display = new HashMap<String, String>();

            public Incident_Adapter(Context context, ArrayList<HashMap<String, String>> arrayList) {

                this.context = context;

                data = arrayList;

                // imageLoad = new ImageLoad(context);
            }

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View itemView = inflater.inflate(R.layout.list_item, parent, false);
                final Holder holder = new Holder();
                display = data.get(position);
              /*  TextView text=(TextView)vi.findViewById(R.id.header);
                final CheckBox check=(CheckBox)vi.findViewById(R.id.checkBox);;*/
                holder.text = (TextView) itemView.findViewById(R.id.header);
                holder.checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
                int ins=0;
                final int in=ins++;
                String description = display.get("Incidents");
                String productId = display.get("ID");
                Log.d("productId", productId);
                holder.text.setText(description);
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (holder.checkBox.isChecked()) {
                            ArrayList<String> MAAP = new ArrayList<String>();
                            display = data.get(position);
                            bb = display.get("ID");
//                            MAP.add(bb);
                            MAP.add(in,bb);
//                            SharedHelper.putKey(getApplicationContext(),"Chkbxsts","1");
//                            Toast.makeText(getApplicationContext(),"checked",Toast.LENGTH_SHORT).show();
                            Log.d("MAAAPmaaap", String.valueOf(MAP));
                        } else {

//                            bb="";
                            MAP.remove(in);
//                            SharedHelper.putKey(getApplicationContext(),"Chkbxsts","2");
//                            Toast.makeText(getApplicationContext(),"Unchecked",Toast.LENGTH_SHORT).show();

//                            bb = display.get("ID");
//                            MAP.remove(bb);
                            Log.d("displaybbbbbb", bb);
                        }

//                        String s=SharedHelper.getKey(getApplicationContext(),"Chkbxsts");
//                        if (s.equalsIgnoreCase("1")) {
//                            MAP.add(bb);
//                        }else {
//                            MAP.remove(bb);
//                        }

                    }
                });
                return itemView;
            }

            public class Holder {
                TextView text;
                CheckBox checkBox;
            }

        }
    }


    public void incidentbuttonpressed() {
        try {
            strlat = SharedHelper.getKey(Incident_Report.this, "current_lat");
            strlong = SharedHelper.getKey(Incident_Report.this, "current_lng");
            final String Id = SharedHelper.getKey(Incident_Report.this, "userID");
//            final String userIdentity=SharedHelper.getKey(Incident_Report.this, "userIdentity");

            new AsyncTask<Void, Integer, String>() {
                ProgressDialog progressBar;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressBar = new ProgressDialog(Incident_Report.this);
                    progressBar.setCancelable(false);
                    progressBar.setMessage("Please wait...");
                    progressBar.show();
                }

                @SuppressWarnings("deprecation")
                @Override
                protected String doInBackground(Void... params) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);
                    multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    try {
                        signature_image = new File(String.valueOf(save_file));
                        sourceFile = new File(String.valueOf(destination));
                        Log.d("signature_image", String.valueOf(signature_image));
                        Log.d("destinatiooonss343", String.valueOf(sourceFile));
                        multipartEntity.addPart("ImageURL", new FileBody(sourceFile));
                        Log.e("ImageURL", "" + sourceFile);
                        multipartEntity.addPart("SignURL", new FileBody(signature_image));
                        Log.e("SignURL", "" + signature_image);
                        multipartEntity.addPart("Incidents", new StringBody(MAP.toString()));
                        Log.e("Incidents", "" + MAP);
                        multipartEntity.addPart("lat", new StringBody(strlat));
                        Log.e("lat", "" + strlat);
                        multipartEntity.addPart("lng", new StringBody(strlong));
                        Log.e("lng", "" + strlong);
//                        multipartEntity.addPart("Username", new StringBody(userIdentity));
                        multipartEntity.addPart("Username", new StringBody(Id));
                        httppost.setEntity(multipartEntity);
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity r_entity = response.getEntity();
                        int statusCode = response.getStatusLine().getStatusCode();
                        if (statusCode == 200) {
                            String res = EntityUtils.toString(r_entity);
                            Log.e("res", "" + res);
                            return res;
                        } else {
                            return "Error";
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;

                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    progressBar.dismiss();
                    Log.e("result", "" + result);
                    if (result.equalsIgnoreCase("Error")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Incident_Report.this);
                        builder.setMessage("Server timeout");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            Toast.makeText(Incident_Report.this, jsonObject.optString("Message"), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),Incident_Report.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Incident_Report.this.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
    }*/
}
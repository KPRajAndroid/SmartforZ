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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;
import java.util.Random;


public class Incident_Report_copy extends AppCompatActivity {
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
//    ListView list_view;
    ProgressDialog progressDialog;
    static final String TITLE = "title";
    ArrayList<HashMap<String, String>> arrayList,JPark,JLevel,JRow;
    ArrayList<String> incidents = new ArrayList<String>();
    Incident_details.Incident_Adapter adapter;
    Button incident_btn, sign_reg;
    Bundle extras;
    String lat, lng, SessionID;
    private Uri imageToUploadUri;
    JSONObject incident_object, allproduct_obj,ObforPark,ObforLevel,ObforRow;
    public static final int SIGNATURE_ACTIVITY = 1;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    long totalSize = 0;
    ProgressDialog mProgressDialog;
    String FILE_UPLOAD_URL ="http://aflaree.com/tetrasecurity/home/Parkingissue";
//            "http://ec2-52-66-74-239.ap-south-1.compute.amazonaws.com/web/home/Incidents";
    //http://ec2-52-66-98-212.ap-south-1.compute.amazonaws.com/web
    //http://ec2-52-66-74-239.ap-south-1.compute.amazonaws.com/Services/Service1.svc
    JSONArray jsonarray,JarrayPark,JarrayLevel,JarrayRow;
    String path = "", request_id = "", value = "";
    ImageView ivImage;
    Button capt_btn;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0;
    File Parkdestination, save_file, Parkdestination2;

    GPSTracker gps;
    Double lattitude, longitude;
    String strlat = "", strlong = "", fname,UName,CNum;
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
    Spinner Sp1,Sp2,Sp3,Sp4;
    EditText E;
    ArrayList<String> ArrayLstPark,ArrayLstLevel,ArrayLstRow;
    JSONArray JArray;
    List<String> itemlist;
    String[] SArray,ASpin,ASPark,ASLevel,ASRow;
    int[] SAid;
    String IdVal,parkvl,levelvl,rowvl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        modelData = model.getInstance();
        extras = getIntent().getExtras();

        h1=new Helper1();

        getSupportActionBar().setCustomView(R.layout.abs_layout);
        setContentView(R.layout.incident_report_copy);
        MAP = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        capt_btn = (Button) findViewById(R.id.ParkIssuecapture_btn);
        // view_image = (ImageView) findViewById(R.id.image_view);
//        list_view = (ListView) findViewById(R.id.listview);
        incident_btn = (Button) findViewById(R.id.ParkIssuebutton);
        E=(EditText)findViewById(R.id.CN_Edit);
        Sp1=(Spinner)findViewById(R.id.IssueSpin);
        Sp2=(Spinner)findViewById(R.id.ParkSpin);
        Sp3=(Spinner)findViewById(R.id.LvlSpin);
        Sp4=(Spinner)findViewById(R.id.RowSpin);


//        sign_reg = (Button) findViewById(R.id.sign_reg);
//        Sv=(ScrollView)findViewById(R.id.ScId);

        ivImage = (ImageView) findViewById(R.id.ParkIssueivImage);
        capt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        //incidentbuttonpressed();
        Log.e("USerID", modelData.userId);
        // new Incident_details().execute();
        new Incident_details().execute("http://aflaree.com/tetrasecurity/home/Parkingdropdowns");

//        http://ec2-52-66-74-239.ap-south-1.compute.amazonaws.com/Services/Service1.svc/Incidentlists
        //  http://ec2-52-66-98-212.ap-south-1.compute.amazonaws.com/web/home/Incidents

//        sign_reg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               /* Intent intent = new Intent(Incident_Report.this, CaptureSignature.class);
//                startActivityForResult(intent, SIGNATURE_ACTIVITY);*/
//
//                final CustomDialog customDialog = new CustomDialog(Incident_Report_copy.this);
//                customDialog.setContentView(R.layout.signature);
//                customDialog.setCancelable(false);
//                customDialog.show();
//
//                mContent = (LinearLayout) customDialog.findViewById(R.id.linearLayout);
//                mSignature = new signature(Incident_Report_copy.this, null);
//                mSignature.setBackgroundColor(Color.WHITE);
//                //noinspection deprecation
//                mContent.addView(mSignature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//                mClear = (Button) customDialog.findViewById(R.id.clear);
//                mGetSign = (Button) customDialog.findViewById(R.id.getsign);
//                mGetSign.setEnabled(false);
//                mCancel = (Button) customDialog.findViewById(R.id.cancel);
//                yourName = (EditText) customDialog.findViewById(R.id.yourName);
//
//                mGetSign.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (yourName.getText().toString().equalsIgnoreCase("")) {
//                            Toast.makeText(Incident_Report_copy.this, "Please enter the name", Toast.LENGTH_SHORT).show();
//                        } else {
//                            mContent.setDrawingCacheEnabled(true);
//                            save(mContent, customDialog);
//                        }
//
//                    }
//                });
//                mCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        customDialog.dismiss();
//                    }
//                });
//                mClear.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mSignature.clear();
//                        mGetSign.setEnabled(false);
//                    }
//                });
//
//            }
//        });

        incident_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    incidentbuttonpressed();
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

        Parkdestination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".jpg");
        Log.d("destinatiooonss", String.valueOf(Parkdestination));
        FileOutputStream fo;
        try {
            Parkdestination.createNewFile();
            fo = new FileOutputStream(Parkdestination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
        sourceFile = new File(String.valueOf(Parkdestination));
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
                    /*JarrayPark= new JSONArray();
                    JarrayLevel=new JSONArray();
                    JarrayRow=new JSONArray();*/
                    JPark=new ArrayList<HashMap<String, String>>();
                    JPark.clear();
                    JLevel=new ArrayList<HashMap<String, String>>();
                    JLevel.clear();
                    JRow=new ArrayList<HashMap<String, String>>();
                    JRow.clear();


                    if (incident_object != null) {
                        try {
                            jsonarray = incident_object.getJSONArray("category");
                            for (int n = 0; n < jsonarray.length(); n++) {
                                allproduct_obj = jsonarray.getJSONObject(n);

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("ID", allproduct_obj.getString("ID"));
                                map.put("Description", allproduct_obj.getString("Description"));
                                map.put("Status", allproduct_obj.getString("Status"));

                                Log.e("map", "" + map);
                                arrayList.add(map);
                                Log.e("productlist", "" + arrayList);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JarrayPark=incident_object.getJSONArray("parklevels");
                        for (int n = 0; n < JarrayPark.length(); n++){
                            ObforPark=JarrayPark.getJSONObject(n);

                            HashMap<String,String> map=new HashMap<String, String>();
                            map.put("ID", ObforPark.getString("ID"));
                            map.put("Leveldetails", ObforPark.getString("Leveldetails"));
//                            map.put("Status", ObforPark.getString("Status"));

                            Log.e("map", "" + map);
                            JLevel.add(map);
                            Log.e("JLevel", "" + JLevel);
                        }

                        JarrayLevel=incident_object.getJSONArray("parkdetail");
                        for (int n = 0; n < JarrayLevel.length(); n++){
                            ObforLevel=JarrayLevel.getJSONObject(n);

                            HashMap<String,String> map=new HashMap<String, String>();
                            map.put("ID", ObforLevel.getString("ID"));
                            map.put("Parkingarea", ObforLevel.getString("Parkingarea"));
//                            map.put("Status", ObforLevel.getString("Status"));

                            Log.e("map", "" + map);
                            JPark.add(map);
                            Log.e("JPark","" + JPark);
                        }

                        JarrayRow=incident_object.getJSONArray("parkrows");
                        for (int n = 0; n < JarrayRow.length(); n++){
                            ObforRow=JarrayRow.getJSONObject(n);

                            HashMap<String,String> map=new HashMap<String, String>();
                            map.put("ID", ObforRow.getString("ID"));
                            map.put("Rowdetails", ObforRow.getString("Rowdetails"));
//                            map.put("Status", ObforRow.getString("Status"));

                            Log.e("map", "" + map);
                            JRow.add(map);
                            Log.e("JRow",""+ JRow);
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

//                String[] arrSpin = new String[model.getInstance().arrayList.size()];
                ASpin=new String[arrayList.size()];

                for (int ind=0;ind<arrayList.size();ind++){
                    HashMap<String, String> Hmap = arrayList.get(ind);

                    ASpin[ind]=Hmap.get("Description");
                    Log.e("ASpinn",""+Hmap.get("Description"));
                }

                ArrayAdapter<String> AAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinitem,ASpin);
                AAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_test2);
                Sp1.setAdapter(AAdapter);
                Sp1.setSelection(0);

                ASPark=new String[JPark.size()];

                for (int in=0;in<JPark.size();in++){
                    HashMap<String,String> hhmap=JPark.get(in);

                    ASPark[in]=hhmap.get("Parkingarea");
                    Log.e("Parkingarea",""+ hhmap.get("Parkingarea"));
                }

                ArrayAdapter<String> AAdapterPark=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinitem,ASPark);
                AAdapterPark.setDropDownViewResource(R.layout.spinner_dropdown_item_test2);
                Sp2.setAdapter(AAdapterPark);

                ASLevel=new String[JLevel.size()];

                for (int inn=0;inn<JLevel.size();inn++){
                    HashMap<String,String> hlmap=JLevel.get(inn);

                    ASLevel[inn]=hlmap.get("Leveldetails");
                    Log.e("Leveldetails",""+ hlmap.get("Leveldetails"));
                }

                ArrayAdapter<String> AAdapterLevel=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinitem,ASLevel);
                AAdapterLevel.setDropDownViewResource(R.layout.spinner_dropdown_item_test2);
                Sp3.setAdapter(AAdapterLevel);

                ASRow=new String[JRow.size()];

                for (int iin=0;iin<JRow.size();iin++){
                    HashMap<String,String> hrmap=JRow.get(iin);

                    ASRow[iin]=hrmap.get("Rowdetails");
                    Log.e("Rowdetails",""+hrmap.get("Rowdetails"));
                }

                ArrayAdapter<String> AAdapterRow=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinitem,ASRow);
                AAdapterRow.setDropDownViewResource(R.layout.spinner_dropdown_item_test2);
                Sp4.setAdapter(AAdapterRow);


                Sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String Spinnm = Sp1.getSelectedItem().toString();
                        Log.e("Spinnm",""+Spinnm);
//                        for (position=0;position<arrayList.size();position++){
                            HashMap<String,String> map=arrayList.get(position);

//                            String model_id  = map.get("ID");
                            if (Spinnm.equalsIgnoreCase(map.get("Description"))){
                                IdVal  = map.get("ID");
                                Log.e("IdVal",""+IdVal);
                            }
//                        }

//                        Toast.makeText(getApplicationContext(),""+IdVal,Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        parkvl=Sp2.getSelectedItem().toString();

//                        Toast.makeText(getApplicationContext(),""+ parkvl,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        levelvl=Sp3.getSelectedItem().toString();

//                        Toast.makeText(getApplicationContext(),""+ levelvl,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        rowvl=Sp4.getSelectedItem().toString();

//                        Toast.makeText(getApplicationContext(),""+ rowvl,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

//                adapter = new Incident_Adapter(Incident_Report_copy.this, arrayList);
//                ArrayAdapter<HashMap<Integer, String>> Adapt=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item);
//                Adapt.add(SpinMap);
//                Adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                Sp1.setAdapter(Adapt);
//                list_view.setAdapter(adapter);
//                h1.getListViewSize(list_view);

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
            strlat = SharedHelper.getKey(Incident_Report_copy.this, "current_lat");
            strlong = SharedHelper.getKey(Incident_Report_copy.this, "current_lng");
            CNum=E.getText().toString();

            UName = SharedHelper.getKey(getApplicationContext(), "UserID");

            final String Id = SharedHelper.getKey(Incident_Report_copy.this, "userID");
//            final String userIdentity=SharedHelper.getKey(Incident_Report.this, "userIdentity");

            new AsyncTask<Void, Integer, String>() {
                ProgressDialog progressBar;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressBar = new ProgressDialog(Incident_Report_copy.this);
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
//                        signature_image = new File(String.valueOf(save_file));
                        sourceFile = new File(String.valueOf(Parkdestination));
//                        Log.d("signature_image", String.valueOf(signature_image));
                        Log.d("destinatiooonss343", String.valueOf(sourceFile));
                        multipartEntity.addPart("ImageURL", new FileBody(sourceFile));
                        Log.e("ImageURL", "" + sourceFile);

//                        multipartEntity.addPart("SignURL", new FileBody(signature_image));
//                        Log.e("SignURL", "" + signature_image);

//                        multipartEntity.addPart("Incidents", new StringBody(MAP.toString()));
//                        Log.e("Incidents", "" + MAP);

                        multipartEntity.addPart("UserID",new StringBody(UName));
                        Log.e("UserID",""+ UName);

                        multipartEntity.addPart("issuetype",new StringBody(IdVal));
                        Log.e("issuetype",""+ IdVal);

                        multipartEntity.addPart("carno",new StringBody(CNum));
                        Log.e("carno",""+ CNum);

                        multipartEntity.addPart("Parking", new StringBody(parkvl));
                        Log.e("Parking", "" + parkvl);

                        multipartEntity.addPart("leve",new StringBody(levelvl));
                        Log.e("leve",""+ levelvl);

                        multipartEntity.addPart("rows", new StringBody(rowvl));
                        Log.e("rows", "" + rowvl);


//                        multipartEntity.addPart("Username", new StringBody(userIdentity));
//                        multipartEntity.addPart("Username", new StringBody(Id));
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Incident_Report_copy.this);
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
                            String SStrn=jsonObject.optString("Message");
//                            Toast.makeText(Incident_Report_copy.this, SStrn, Toast.LENGTH_SHORT).show();



                            SharedHelper.putKey(getApplicationContext(),"PrntSts",SStrn);

                            Intent inta=new Intent(getApplicationContext(),Printstatus.class);
                            startActivity(inta);

//                            Intent intent=new Intent(getApplicationContext(),Incident_Report_copy.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                            Incident_Report_copy.this.finish();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")){
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKey(this);
        }

        return super.dispatchTouchEvent(ev);
    }

    private static void hideKey(Activity printstatus) {
        InputMethodManager imm = (InputMethodManager) printstatus.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(printstatus.getWindow().getDecorView().getWindowToken(), 0);

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
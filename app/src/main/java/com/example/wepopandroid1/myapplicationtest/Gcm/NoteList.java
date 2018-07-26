package com.example.wepopandroid1.myapplicationtest.Gcm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wepopandroid1.myapplicationtest.JSONPost;
import com.example.wepopandroid1.myapplicationtest.MapsActivity;
import com.example.wepopandroid1.myapplicationtest.R;
import com.example.wepopandroid1.myapplicationtest.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteList extends AppCompatActivity {

    ListView lv;
    ProgressDialog mProgressDialog;
    JSONObject NoteObj,AllNoteObj ;
    JSONArray Notelist,AllNotelist;
    String Name;
    int Mid;
    ArrayList<HashMap<String, String>> N_arrayList;
    ArrayList<String> AList,N_AList;
    Button B;
    ArrayList<HashMap<String,String>> Not_ArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> all_Note_ArrayList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        lv=(ListView)findViewById(R.id.Notlist);

        B=new Button(this);
        B.setText("Load More");
        lv.addFooterView(B);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getallnotification().execute();
            }
        });

        new getnotification().execute();

    }

    private class getnotification extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(NoteList.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            //mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONPost jPost = new JSONPost();
            String id = SharedHelper.getKey(NoteList.this, "userID");
            NoteObj = jPost.NoteLlist(id);
            Log.e("NoteObj", "" + NoteObj);

            try {
                String msg = NoteObj.getString("Message");
                String userid = NoteObj.getString("Result");
                Notelist = NoteObj.getJSONArray("Notificationlist");
                Log.e("LATLNG", "" + msg + userid + Notelist );

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            setlist();
        }
    }

    private void setlist1() {
        N_AList=new ArrayList<String>();
        if (AllNotelist!=null && AllNotelist.length()>0){
            for (int i=0;i<AllNotelist.length();i++){
                HashMap<String, String> map= new HashMap<String, String>();
                try {
                    JSONObject Job=AllNotelist.getJSONObject(i);
                    Log.e("JsonArray2",""+Job);

                    map.put("Time",Job.optString("Createdat"));
                    map.put("SName",Job.optString("SecurityHead"));
                    map.put("Note_message",Job.optString("Message"));

                    all_Note_ArrayList.add(map);
                    Log.e("map2",""+map);

                    Mid=Job.getInt("ID");
                    SharedHelper.putKey(getApplicationContext(), "NoteiD", String.valueOf(Mid));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    JSONObject JSob = AllNotelist.getJSONObject(i);
//                    Name = JSob.optString("Message");
//                    Mid = JSob.getInt("ID");
//                    SharedHelper.putKey(getApplicationContext(), "NoteiD", String.valueOf(Mid));
//                    N_AList.add(Name);
//                    Log.d(Name, "Output");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

        }else {
            B.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"No More Data Available",Toast.LENGTH_SHORT).show();
        }

//        AList.addAll(N_AList);
        Not_ArrayList.addAll(all_Note_ArrayList);
//        ArrayAdapter<String> AAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,AList);
        Active_adapter1 adapt=new Active_adapter1(getApplicationContext(),Not_ArrayList);
        lv.setAdapter(adapt);
//        lv.setAdapter(AAdapter);

    }

    private void setlist() {

        AList=new ArrayList<String>();
if (Notelist!=null && Notelist.length()>0){
        for (int i=0;i<Notelist.length();i++) {

            HashMap<String, String> map= new HashMap<String, String>();
            try {
                JSONObject Jo=Notelist.getJSONObject(i);
                Log.e("JsonArray",""+Jo);

                map.put("Time",Jo.optString("Createdat"));
                map.put("SName",Jo.optString("SecurityHead"));
                map.put("Note_message",Jo.optString("Message"));

                Not_ArrayList.add(map);
                Log.e("map",""+map);

                Mid=Jo.getInt("ID");
                SharedHelper.putKey(getApplicationContext(), "NoteiD", String.valueOf(Mid));

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            try {
//                JSONObject Jo = Notelist.getJSONObject(i);
//                Name = Jo.optString("Message");
//                Mid = Jo.getInt("ID");
//                SharedHelper.putKey(getApplicationContext(), "NoteiD", String.valueOf(Mid));
//                AList.add(Name);
//                Log.d(Name, "Output");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

        }else {
    B.setVisibility(View.GONE);
    Toast.makeText(getApplicationContext(),"Notification List Empty",Toast.LENGTH_SHORT).show();
        }

//        ArrayAdapter<String> AAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,AList);
        Active_adapter adapt=new Active_adapter(getApplicationContext(),Not_ArrayList);
        lv.setAdapter(adapt);
//        lv.setAdapter(AAdapter);

    }

    private class getallnotification extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(NoteList.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            //mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONPost jPost = new JSONPost();
            String S_Uid = SharedHelper.getKey(NoteList.this, "userID");
            String N_id=SharedHelper.getKey(NoteList.this,"NoteiD");
            AllNoteObj=jPost.AllNotelist_id(S_Uid,N_id);
            Log.e("AllNoteObj", "" + AllNoteObj);

            try {
                String msg = AllNoteObj.getString("Message");
                String res = AllNoteObj.getString("Result");
                AllNotelist = AllNoteObj.getJSONArray("Notificationlist");
                Log.e("LATLNG", "" + msg + res + AllNotelist );

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            setlist1();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intnt=new Intent(getApplicationContext(), MapsActivity.class);
//        intnt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intnt);
    }

    private class Active_adapter extends BaseAdapter {
        Context context;
        ArrayList<HashMap<String, String>> data;
        LayoutInflater inflater;


        public Active_adapter(Context applicationContext, ArrayList<HashMap<String, String>> not_arrayList) {
            this.context=applicationContext;
            data=not_arrayList;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View items=inflater.inflate(R.layout.list_for_note,parent,false);

            TextView Tv_Name,Tv_Time,Tv_Message;

            Tv_Name=(TextView)items.findViewById(R.id.Name);
            Tv_Time=(TextView)items.findViewById(R.id.TimeStamp);
            Tv_Message=(TextView)items.findViewById(R.id.Message);

            Tv_Name.setText(data.get(position).get("SName"));
            Tv_Time.setText(data.get(position).get("Time"));
            Tv_Message.setText(data.get(position).get("Note_message"));

            return items;
        }
    }

    private class Active_adapter1 extends BaseAdapter {
        Context context;
        ArrayList<HashMap<String, String>> data;
        LayoutInflater inflater;


        public Active_adapter1(Context applicationContext, ArrayList<HashMap<String, String>> not_arrayList) {
            this.context=applicationContext;
            data=not_arrayList;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View items=inflater.inflate(R.layout.list_for_note,parent,false);

            TextView Tv_Name,Tv_Time,Tv_Message;

            Tv_Name=(TextView)items.findViewById(R.id.Name);
            Tv_Time=(TextView)items.findViewById(R.id.TimeStamp);
            Tv_Message=(TextView)items.findViewById(R.id.Message);

            Tv_Name.setText(data.get(position).get("SName"));
            Tv_Time.setText(data.get(position).get("Time"));
            Tv_Message.setText(data.get(position).get("Note_message"));

            return items;
        }
    }

}

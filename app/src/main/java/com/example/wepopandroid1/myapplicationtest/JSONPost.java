package com.example.wepopandroid1.myapplicationtest;

/**
 * Created by wepopandroid1 on 2/6/16.

 */
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("ALL")
public class JSONPost {
    InputStream is = null;
    JSONObject jObj = null;
    String json = null;
    String json1 = null;
    String result = "";

    DefaultHttpClient defaultHttpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;
    StringBuilder stringBuilder;
    JSONObject object;


    static JSONObject citywise= null;
    String url="http://aflaree.com/patrolservice/Service1.svc/";

    String Surl="http://aflaree.com//Service1.svc/AddQrlocation";

    String SONGS_URL=url+"Patrolls";
    String login_url=url+"getlogin";
    String scan_url=url+"qrscan";
    String endpatrol_url=url+"Patrolls";
    String event_url=url+"Events";
    String attendance_url=url+"Attendance";
    String panic_url=url+"Panicalarm";
    String title_url=url+"Incidentlists";
    String incident_url=url+"Incidents";

    String Securityhome_url=url+"Securityhome";
    String SecurityVibrations_url=url+"SecurityVibrations";
    String Securityheadhome_url=url+"Securityheadhome";
    String SecurityHeadNotify_url=url+"NotifySecurity";
    String SecurityNotification_url=url+"Notification";


//http://beta.json-generator.com/api/json/get/NJolbXAVZ
    String TRAILER_URL = "http://aflaree.com/soappService/Login.svc/Trailer";
    String POSTER_URL = "http://aflaree.com/soappService/Login.svc/Trailer";

    public JSONObject songs_id(String ID,String VAL,String HH,String NN) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SONGS_URL);
            Log.e("", "url checking" + SONGS_URL);

            JSONObject isregobj = new JSONObject();
            isregobj.put("Eventactivity", ID);
            isregobj.put("UserID", VAL);
            isregobj.put("lat", HH);
            isregobj.put("lng", NN);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }
    public JSONObject incident_id(String ID,String VAL,String HH,String NN,String Title,String description) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(incident_url);
            Log.e("", "url checking" + incident_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("SessionID", ID);
            isregobj.put("ImageURL", VAL);
            isregobj.put("lat", HH);
            isregobj.put("lng", NN);
            isregobj.put("Title", Title);
            isregobj.put("Description", description);
            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {
        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {
        }

        return citywise;
    }

    public JSONObject detail() throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(title_url);
            Log.e("", "url checking" + title_url);

            JSONObject isregobj = new JSONObject();
        //    isregobj.put("SessionID", title);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }


    public JSONObject sacn_id1(String ID,String VAL,String HH,String NN,String stepscount) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Surl);
            Log.e("", "url checking" + Surl);

            JSONObject isregobj = new JSONObject();
            isregobj.put("UserID", ID);
            isregobj.put("Qrresponsecode", VAL);
            isregobj.put("lat", HH);
            isregobj.put("lng", NN);
            isregobj.put("Title", stepscount);
            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }


    public JSONObject sacn_id(String ID,String VAL,String HH,String NN,String stepscount) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(scan_url);
            Log.e("", "url checking" + scan_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("SessionID", ID);
            isregobj.put("Qrresponsecode", VAL);
            isregobj.put("lat", HH);
            isregobj.put("lng", NN);
            isregobj.put("steps", stepscount);
            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }


    public JSONObject event_id(String ID,String VAL,String HH,String Stepscount) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(event_url);
            Log.e("", "url checking" + event_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("SessionID", ID);
            isregobj.put("lat", VAL);
            isregobj.put("lng", HH);
            isregobj.put("steps", Stepscount);
            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject attendance_id(String ID,String VAL,String HH) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(attendance_url);
            Log.e("", "url checking" + attendance_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("UserID", ID);
            isregobj.put("lat", VAL);
            isregobj.put("lng", HH);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }
    public JSONObject panic_id(String ID,String VAL,String HH) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(panic_url);
            Log.e("", "url checking" + panic_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("UserID", ID);
            isregobj.put("lat", VAL);
            isregobj.put("lng", HH);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }



    public JSONObject endpatrol(String ID,String VAL,String HH,String NN) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(endpatrol_url);
            Log.e("", "url checking" + endpatrol_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("Eventactivity", ID);
            isregobj.put("SessionID", VAL);
            isregobj.put("lat", HH);
            isregobj.put("lng", NN);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the endpatrol json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the endpatrol" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject login_id(String Oid,String username,String pass,String device_id,String mob_type) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(login_url);
            Log.e("", "url checking" + login_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("Organization", Oid);
            isregobj.put("Username", username);
            isregobj.put("Password", pass);
            isregobj.put("DeviceID", device_id);
            isregobj.put("Mobiletype", mob_type);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject alarmTriggerId(String username) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Securityhome_url);
            Log.e("", "url checking" + Securityhome_url);

            JSONObject Alarmtrig = new JSONObject();
            Alarmtrig.put("UserID", username);

            StringEntity ent = new StringEntity(Alarmtrig.toString());
            System.out.println("the json object" + Alarmtrig.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject AlarmTiming(String start,String end) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SecurityVibrations_url);
            Log.e("", "url checking" + SecurityVibrations_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("starttime", start);
            isregobj.put("endtime", end);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject headmap_id(String ID) throws IOException {
        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Securityheadhome_url);
            Log.e("", "url checking" + Securityheadhome_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("UserID", ID);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject Gcm_Notification_method(String device_type, String token, String imei_id) throws JSONException {
        try {
//            String url = (URLHelper.Notification_news);

            defaultHttpClient = new DefaultHttpClient();
            httpPost = new HttpPost();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Mobiletype", device_type);
            jsonObject.put("DeviceID", token);
            jsonObject.put("IMEI_NO", imei_id);

            Log.e("User login jsonObject Result", "^^^^^^^^" + jsonObject);

            StringEntity stringEntity = new StringEntity(jsonObject.toString());

            Log.e("User login jsonObject Result123", "^^^^^^^^" + stringEntity);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

            httpResponse = defaultHttpClient.execute(httpPost);


            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                httpEntity = httpResponse.getEntity();
                Log.e("User login jsonObject Result124376563", "^^^^^^^^" + httpResponse.getEntity());

                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity);
                    System.out.println("New User" + result);
                    Log.d("Login RESPONSE from JsonPOST Method", "#############" + result);
                }

                try {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(result);
                    json = stringBuilder.toString();

                    Log.e("Value check3", "" + result);
                    Log.e("Value checkjson", "" + json);

                } catch (Exception e) {

                }

                try {
                    object = new JSONObject(json);
                } catch (Exception e) {

                }
            } else {
                object = null;
            }


        } catch (Exception ex) {

        }

        return object;
    }

    public JSONObject Notification_id(String sLid, String s, String sHid) {

        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SecurityHeadNotify_url);
            Log.e("", "url checking" + SecurityHeadNotify_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("UserID", sLid);
            isregobj.put("Message", s);
            isregobj.put("SecurityHead", sHid);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject NoteLlist(String id) {

        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SecurityNotification_url);
            Log.e("", "url checking" + SecurityNotification_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("UserID", id);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

    public JSONObject AllNotelist_id(String S_Uid,String N_id) {

        try {

            HttpClient httpcli = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SecurityNotification_url);
            Log.e("", "url checking" + SecurityNotification_url);

            JSONObject isregobj = new JSONObject();
            isregobj.put("UserID", S_Uid);
            isregobj.put("ID", N_id);

            StringEntity ent = new StringEntity(isregobj.toString());
            System.out.println("the json object" + isregobj.toString());
            Log.e("if part checking", "url checking3");
            ent.setContentType("application/json");
            Log.e("if part checking", "url checking4");
            httppost.setEntity(ent);
            HttpResponse httpres = httpcli.execute(httppost);
            Log.e("if part checking", "url checking5" );
            HttpEntity httpent = httpres.getEntity();
            Log.e("if part checking", "url checking6");

            if (httpent != null) {
                Log.e("if part checking", "url checking" );
                result = EntityUtils.toString(httpent);
                System.out.println("the is register method" + result);
            }

        } catch (Exception ex) {
            Log.e("else part checking", "url checking" );
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            json = sb.toString();
        }

        catch (Exception e) {

        }

        try {
            citywise = new JSONObject(json);
        } catch (Exception e) {

        }

        return citywise;
    }

}



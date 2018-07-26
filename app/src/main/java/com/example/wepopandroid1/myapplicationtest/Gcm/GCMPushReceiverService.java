package com.example.wepopandroid1.myapplicationtest.Gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.wepopandroid1.myapplicationtest.MapsActivity;
import com.example.wepopandroid1.myapplicationtest.R;
import com.example.wepopandroid1.myapplicationtest.SharedHelper;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.wepop.swasthik_tv.MainActivity;
//import com.wepop.swasthik_tv.R;


/**
 * Created by Belal on 4/15/2016.
 */

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService {

    MapsActivity mpsact;

    int Note_count=NotificationCounter.getPendingNotificationsCount();

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        Note_count++;
        NotificationCounter.setPendingNotificationsCount(Note_count);

        SharedHelper.putKey(getApplicationContext(),"NcountVal", String.valueOf(Note_count));

//        mpsact.updateNote();


        String message = data.getString("message");
        Log.e("data",""+data);
        Log.e("Note_count",""+Note_count);
     //   String image_url = data.getString("image");
      //  Log.e("data",""+data);
      //  String news_id = data.getString("id");
       // Log.e("news_id",""+news_id);

        //Displaying a notiffication with the message
        sendNotification(message);
    }
    // http://stackoverflow.com/questions/35402305/gcm-push-notification-is-not-showing-in-some-devices-when-app-is-not-running
    // https://support.hike.in/entries/55998480-I-m-not-getting-notification-on-my-Xiaomi-Phone-For-MIUI-6-

    //This method is generating a notification and displaying the notification
    private void sendNotification(String message) {
        Context context = getBaseContext();
        String news_id="";
      //  Bitmap bitmap = getBitmapFromURL(image);
     //   NotificationCompat.BigPictureStyle notifystyle = new NotificationCompat.BigPictureStyle();
     //   notifystyle.bigPicture(bitmap);
     /*  if(!(newsid ==null))
        {
            news_id = newsid;

        }else
        {
            news_id="24360";
        }*/
       /// newsid = String.valueOf(24360);
        Intent intent = new Intent(this, MapsActivity.class);
     //   SharedData.putKey(GCMPushReceiverService.this,"name",news_id);
    //    Log.e("news_id",""+news_id);
    //    Log.e("newsid",""+newsid);
     //   intent.putExtra("Status","1");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int i = (int) System.currentTimeMillis();

        int requestCode = i;
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.smartz);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Smart 4Z")
                .setSmallIcon(getNotificationIcon())//.setStyle(notifystyle)
                .setLargeIcon(largeIcon)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker(message).setContentText(message)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(sound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // http://stackoverflow.com/questions/18102052/how-to-display-multiple-notifications-in-android
        notificationManager.notify(i, noBuilder.build());

         //0 = ID of notification
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.smartz : R.drawable.smartz;
    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
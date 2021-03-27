package com.inspireuser.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.inspireuser.app.MainActivity2.CHANNEL_1_ID;
import static com.inspireuser.app.MainActivity2.CHANNEL_2_ID;
import static com.inspireuser.app.MainActivity2.CHANNEL_3_ID;
import static com.inspireuser.app.MainActivity2.CHANNEL_4_ID;
import static com.inspireuser.app.MainActivity2.CHANNEL_5_ID;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
private NotificationManagerCompat notificationManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
        //mediaSessionCompat = new MediaSessionCompat(this,"media");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.notification);
        final long[] VIBRATE_PATTERN    = {0, 100,300,300};

        Map<String, String> data = remoteMessage.getData();
        final int noti_id=Integer.parseInt(data.get("noti_id"));
        String type = data.get("type");
        String type1 = "no_icon";
        String type2 = "right_icon";
        String type3 = "left_icon";
        String type4 = "right_icon_long";
        String type5 = "no_icon_long";
        String type6 = "no_icon_image";
        String type7 = "right_icon_image_hide";
        String type8 = "right_icon_image_show";
        String type9 = "no_icon_lines";
        String type10 = "right_icon_lines";
        int var1 = type.compareTo( type1 );
        int var2 = type.compareTo( type2 );
        int var3 = type.compareTo( type3 );
        int var4 = type.compareTo( type4 );
        int var5 = type.compareTo( type5 );
        int var6 = type.compareTo( type6 );
        int var7 = type.compareTo( type7 );
        int var8 = type.compareTo( type8 );
        int var9 = type.compareTo( type9 );
        int var10 = type.compareTo( type10 );

if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O ){
    if(icChannelBlocked((CHANNEL_1_ID))){openChannelSettings(CHANNEL_1_ID);return;}
    if(icChannelBlocked((CHANNEL_2_ID))){openChannelSettings(CHANNEL_2_ID);return;}
    if(icChannelBlocked((CHANNEL_3_ID))){openChannelSettings(CHANNEL_3_ID);return;}
    if(icChannelBlocked((CHANNEL_4_ID))){openChannelSettings(CHANNEL_4_ID);return;}
    if(icChannelBlocked((CHANNEL_5_ID))){openChannelSettings(CHANNEL_5_ID);return;}
}
        if(var1==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var2==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setLargeIcon(getBitmapFromURL(data.get("icon")))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var3==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setLargeIcon(getBitmapFromURL(data.get("icon")))
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var4==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setLargeIcon(getBitmapFromURL(data.get("icon")))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("body_long")).setBigContentTitle(data.get("title_long")).setSummaryText(data.get("summary")))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var5==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("body_long")).setBigContentTitle(data.get("title_long")).setSummaryText(data.get("summary")))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var6==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(data.get("image"))))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var7==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setLargeIcon(getBitmapFromURL(data.get("icon")))
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(data.get("image"))).bigLargeIcon(null))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var8==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setLargeIcon(getBitmapFromURL(data.get("icon")))
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(data.get("image"))))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var9==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine(data.get("body_line1"))
                            .addLine(data.get("body_line2"))
                            .addLine(data.get("body_line3"))
                            .addLine(data.get("body_line4"))
                            .addLine(data.get("body_line5"))
                            .addLine(data.get("body_line6"))
                            .addLine(data.get("body_line7"))
                            .addLine(data.get("body_line8"))
                            .addLine(data.get("body_line9"))
                            .addLine(data.get("body_line10"))
                            .setBigContentTitle(data.get("title_line"))
                            .setSummaryText("summary"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }
        else if(var10==0){
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, data.get("channel_id"))
                    .setSmallIcon(R.drawable.smallicon1)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setLargeIcon(getBitmapFromURL(data.get("icon")))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine(data.get("body_line1"))
                            .addLine(data.get("body_line2"))
                            .addLine(data.get("body_line3"))
                            .addLine(data.get("body_line4"))
                            .addLine(data.get("body_line5"))
                            .addLine(data.get("body_line6"))
                            .addLine(data.get("body_line7"))
                            .addLine(data.get("body_line8"))
                            .addLine(data.get("body_line9"))
                            .addLine(data.get("body_line10"))
                            .setBigContentTitle(data.get("title_line"))
                            .setSummaryText("summary"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setVibrate(VIBRATE_PATTERN)
                    .setColor(Color.rgb(255,136,0))
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(noti_id, builder.build());
        }


    }

        @RequiresApi(26)
        public boolean icChannelBlocked(String channelID){
        NotificationManager manager =getSystemService(NotificationManager.class);
        NotificationChannel channel = manager.getNotificationChannel(channelID);
        return channel != null &&
                channel.getImportance() ==NotificationManager.IMPORTANCE_NONE;
        }
      @RequiresApi(26)
       private void openChannelSettings(String channelD){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelD);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
     }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
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

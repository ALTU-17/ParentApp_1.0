package in.aceventura.evolvuschool.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import in.aceventura.evolvuschool.NewLoginPage;
import in.aceventura.evolvuschool.ParentDashboard;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.RemarkActivity;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.StudentDashboard;
import in.aceventura.evolvuschool.utils.ConstantsFile;
import in.aceventura.evolvuschool.utils.FirebaseNotificationUtils;


public class FirebaseMessageReceiver extends FirebaseMessagingService {

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

       /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MyFirebaseMessagingService.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refreshedToken = instanceIdResult.getToken();
                Log.d("", "Refreshed token: " + refreshedToken);
                storeToken(refreshedToken);
            }
        });


*/
        Log.e("NotificationValue", "TokanValueFilrebase>" + s);

    }

    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {
        //   Log.e("LOLO", "Kkkffkfkfk+>>kkkkkk" + remoteMessage.toIntent().getStringExtra("gcm.notification.remark_id"));
        //   ConstantsFile.Remark_id = remoteMessage.toIntent().getStringExtra("gcm.notification.remark_id");


        Log.e("LOL>", "Kkkffkfkfk+>>" + remoteMessage.getNotification().getTitle());
        Log.e("LOL>", "Notification>>>" + remoteMessage.getData());
        try {

            String stringJson = remoteMessage.getData().toString();
            JSONObject jsonObject = new JSONObject(stringJson);
            String Values = jsonObject.getString("activity");
            Log.e("flags", "values??" + SharedPrefManager.getInstance(getApplicationContext()).getActivityName());
            SharedPrefManager.getInstance(getApplicationContext()).setActivityName(Values);
            if (Values.equals("remark")) {
                FirebaseNotificationUtils.Remarkremark_id = jsonObject.getString("remark_id");
                FirebaseNotificationUtils.Remarkstud_id = jsonObject.getString("stud_id");
                FirebaseNotificationUtils.Remarksection_id = jsonObject.getString("section_id");
                FirebaseNotificationUtils.Remarkclass_id = jsonObject.getString("class_id");
                FirebaseNotificationUtils.Remarkparent_id = jsonObject.getString("parent_id");
            }
            if (Values.equals("homework")) {
                FirebaseNotificationUtils.HomeWorkhomework_id = jsonObject.getString("homework_id");
                FirebaseNotificationUtils.HomeWorkstud_id = jsonObject.getString("stud_id");
                FirebaseNotificationUtils.HomeWorksection_id = jsonObject.getString("section_id");
                FirebaseNotificationUtils.HomeWorkclass_id = jsonObject.getString("class_id");
                FirebaseNotificationUtils.HomeWorkparent_id = jsonObject.getString("parent_id");

            }
            if (Values.equals("note")) {

                FirebaseNotificationUtils.Notenotes_id = jsonObject.getString("notes_id");
                FirebaseNotificationUtils.Notestud_id = jsonObject.getString("stud_id");
                FirebaseNotificationUtils.Notesection_id = jsonObject.getString("section_id");
                FirebaseNotificationUtils.Noteclass_id = jsonObject.getString("class_id");
                FirebaseNotificationUtils.Noteparent_id = jsonObject.getString("parent_id");

            }
            if (Values.equals("notice")) {

                FirebaseNotificationUtils.Noticenotice_id = jsonObject.getString("notice_id");
                FirebaseNotificationUtils.Noticestud_id = jsonObject.getString("stud_id");
                FirebaseNotificationUtils.Noticesection_id = jsonObject.getString("section_id");
                FirebaseNotificationUtils.Noticeclass_id = jsonObject.getString("class_id");
                FirebaseNotificationUtils.Noticeparent_id = jsonObject.getString("parent_id");

            }


        } catch (Exception e) {
            Log.e("JsonValue", "Vza>>" + e.getMessage());

        }


        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.icon);
        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title,
                                 String message) {


        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(this, ParentDashboard.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,


                PendingIntent.FLAG_ONE_SHOT);
      /*  PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            pendingIntent = PendingIntent.getActivity(
                    this, 0, intent,


                    PendingIntent.FLAG_IMMUTABLE
            );
        } else {
            pendingIntent = PendingIntent.getActivity(
                    this, 0, intent,


                    PendingIntent.FLAG_UPDATE_CURRENT

            );

        }
*/

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(
                    getCustomDesign(title, message));
        } // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.icon);
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }
}
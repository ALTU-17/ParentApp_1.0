package in.aceventura.evolvuschool.activities;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.aceventura.evolvuschool.SharedPrefManager;

/**
 * Created by Admin on 7/11/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

/*
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MyFirebaseMessagingService.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refreshedToken = instanceIdResult.getToken();
                Log.d("", "Refreshed token: " + refreshedToken);
                storeToken(refreshedToken);
            }
        });

*/

    }



    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains fetchData payload
        //It is a map of custom keyvalues
        //we can read it easily
        if (remoteMessage.getData().size() > 0) {
            //handle the fetchData message here
        }

        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        MyNotificationManager.getInstance(this).displayNotification(title, body);

        //then here we can use the title and body to build a notification
    }
}

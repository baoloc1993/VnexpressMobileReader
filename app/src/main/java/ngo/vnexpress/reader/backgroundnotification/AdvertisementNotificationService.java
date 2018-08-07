package ngo.vnexpress.reader.backgroundnotification;

/**
 * Background Service
 * Display the advertisement
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.LinkedHashMap;

import ngo.vnexpress.reader.R;

public class AdvertisementNotificationService extends Service {

    private static final int TIME_PERIOD_HOUR = 10;
    public static LinkedHashMap<String, Integer> newArticlePerCate = new LinkedHashMap<>();
    public static int numberNewPost = 0;
    //public static Application service = new NotificationService().getApplication();

    private CountDownTimer countDownTimer;
    private int notificationIdOne;

    @Override
    public void onCreate() {
        //Intent i = new Intent(this, MainActivity.class);
        //Log.d("DEBUG", "CATE new " + String.valueOf(MainActivity.numberNewPost) );
//		 Toast.makeText(this, "Congrats! MyService Created",
//		 Toast.LENGTH_LONG).show();

        // Set timer for update notification
        countDownTimer = new CountDownTimer(TIME_PERIOD_HOUR * 3600 * 1000,
                TIME_PERIOD_HOUR * 3500 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                //Log.d("DEBUG", "TICK " + MainActivity.stopService);
                // Display notification
                //MainActivity.numberNewPost = 0;
//				if (!MainActivity.stopService){
//					//Log.d("DEBUG" , "TICK LoadRSS");
//					new LoadRSSFeedItemsService().execute();
//				}
                displayNotification();
            }

            @Override
            public void onFinish() {
                // Create infinite loop of timer
                // load new articles from RSS
                // Log.d("DEBUG", "TICK new " + String.valueOf(numberNewPost) );
                //if (numberNewPost > 0) {
                displayNotification();

                //}

                startTimer();
                // TODO Auto-generated method stub

            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTimer();
        //START_STICKY  run service forever
        return (START_STICKY);
    }

    @Override
    public void onDestroy() {
        // Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        //Log.d("DESTROY", "onDestroy");

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    void displayNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this, NotificationChannel.DEFAULT_CHANNEL_ID);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Mặt nạ thoát hiểm khi hỏa hoạn");
        inboxStyle.addLine("Điện thoại: 097.406.9999");
        inboxStyle.addLine("Website: www.matnathoathiem.com");

        //for (int i=0; i < events.length; i++) {

        //    inboxStyle.addLine(events[i]);
        //}
        //Set parameter of Notification which is displayed
        //String time = getCurrentTime();
        // Log.d("DEBUG", "DATE  = " + time);
        mBuilder.setContentTitle("Mặt nạ thoát hiểm khi hỏa hoạn");
        mBuilder.setContentText("Mặt nạ chống khói độc");
        mBuilder.setTicker("Giúp con người có thể thoát hiểm trong những đám cháy trên nhà cao tầng");
        mBuilder.setSmallIcon(R.drawable.vnexpress);


        mBuilder.setStyle(inboxStyle);
        //Notification disappear when click to notification
        mBuilder.setAutoCancel(true);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://matnathoathiem.com"));
        //startActivity(browserIntent);
        //Intent i = new Intent(this, MainActivity.class);

        // This ensures that navigating backward from the Activity leads out of
        // the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.
        // Adds the back stack for the Intent
        // stackBuilder.addParentStack(D.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(i);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_CANCEL_CURRENT // can only be used once
        );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        if (myNotificationManager != null) {
            myNotificationManager.notify(notificationIdOne, mBuilder.build());
        }

        //myNotificationManager.
        Notification fakeNotification = new Notification();
        //fakeNotification.
//		fakeNotification.flags = Notification.DEFAULT_LIGHTS
//				| Notification.FLAG_AUTO_CANCEL;
        startForeground(1, fakeNotification);
        // note.
        stopForeground(true);
    }

    public void startTimer() {
        countDownTimer.start();
    }

}

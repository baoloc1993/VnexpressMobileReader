package ngo.vnexpress.reader.backgroundnotification;

/**
 * Background Service
 */
import java.util.Calendar;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.RSS.LoadRSSFeedItemsService;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class NotificationService extends Service {

	private static final int TIME_PERIOD_HOUR = 1;
	private CountDownTimer countDownTimer;

	private int notificationIdOne;
	private NotificationManager myNotificationManager;

	//public static Application service = new NotificationService().getApplication();
	public static Context mContext; 
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		//Intent i = new Intent(this, MainActivity.class);
		mContext = this.getApplicationContext();
		//Log.d("DEBUG", "CATE new " + String.valueOf(MainActivity.numberNewPost) );
//		 Toast.makeText(this, "Congrats! MyService Created",
//		 Toast.LENGTH_LONG).show();

		// Set timer for update notification
		countDownTimer = new CountDownTimer(TIME_PERIOD_HOUR * 300 * 1000,
				TIME_PERIOD_HOUR * 250 * 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

				// Display notification
				//MainActivity.numberNewPost = 0;
				new LoadRSSFeedItemsService().execute();

			}

			@Override
			public void onFinish() {
				// Create infinite loop of timer
				// load new articles from RSS
				// Log.d("DEBUG", "CATE new " + String.valueOf(MainActivity.numberNewPost) );
				if (MainActivity.numberNewPost > 0) {
					displayNotification();

				}
				
				
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

	
	public void startTimer() {
		countDownTimer.start();
	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		 //Log.d("DESTROY", "onDestroy");
		
	}

	// Get and format current time
	private String getCurrentTime() {
		String time = "";
		Calendar c = Calendar.getInstance();
		int seconds = c.get(Calendar.SECOND);
		int mins = c.get(Calendar.MINUTE);
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int days = c.get(Calendar.DAY_OF_MONTH);
		int months = c.get(Calendar.MONTH);
		int years = c.get(Calendar.YEAR);
		time = String.valueOf(hours) + ":" + String.valueOf(mins) + ":"
				+ String.valueOf(seconds) + " " + String.valueOf(days) + " / "
				+ String.valueOf(months) + " / " + String.valueOf(years);
		return time;
	}

	void displayNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);

		//Set parameter of Notification which is displayed
		String time = getCurrentTime();
		// Log.d("DEBUG", "DATE  = " + time);
		mBuilder.setContentTitle("Vnexpress Reader");
		mBuilder.setContentText(String.valueOf(MainActivity.numberNewPost)
				+ " " + getString(R.string.articles) + " " + time);
		mBuilder.setTicker(String.valueOf(MainActivity.numberNewPost) + " "
				+ getString(R.string.articles) + " " + time);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		
		//Notification disappear when click to notification
		mBuilder.setAutoCancel(true);
		
		Intent i = new Intent(this, MainActivity.class);
		
		// This ensures that navigating backward from the Activity leads out of
		// the app to Home page
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent
		// stackBuilder.addParentStack(D.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(i);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_CANCEL_CURRENT // can only be used once
				);
		// start the activity when the user clicks the notification text
		mBuilder.setContentIntent(resultPendingIntent);
		myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// pass the Notification object to the system
		myNotificationManager.notify(notificationIdOne, mBuilder.build());

		//myNotificationManager.
		Notification fakeNotification = new Notification();
//		fakeNotification.flags = Notification.DEFAULT_LIGHTS
//				| Notification.FLAG_AUTO_CANCEL;
		startForeground(1, fakeNotification);
		// note.
		stopForeground(true);
	}

}

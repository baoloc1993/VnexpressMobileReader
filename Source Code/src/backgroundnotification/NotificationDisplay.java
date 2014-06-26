package backgroundnotification;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class NotificationDisplay extends Activity implements OnClickListener {
	 public void onCreate(Bundle savedInstanceState)
	   {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.notification_one);
	      CharSequence s = "Inside the activity of Notification one ";
	      int id=0;
	      Log.d("DEBUG", "ON CLICK");
	      Bundle extras = getIntent().getExtras();
			if (extras == null) {
				s = "error";
			}
			else {
				id = extras.getInt("notificationId");
			}
			TextView t = (TextView) findViewById(R.id.text1);
			s = s+"with id = "+id;
			t.setText(s);
			NotificationManager myNotificationManager = 
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			
			// remove the notification with the specific id
			myNotificationManager.cancel(id);
			
	   }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("DEBUG", "ON CLICK");
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
		
	}
}

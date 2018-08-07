package ngo.vnexpress.reader.backgroundnotification;

/**
 * Service auto start after reboot
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AdvertismentBootCompletedIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, AdvertisementNotificationService.class);
            //pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Log.d("DEBUG", "TICK START");
            context.startService(pushIntent);
        }
    }

}

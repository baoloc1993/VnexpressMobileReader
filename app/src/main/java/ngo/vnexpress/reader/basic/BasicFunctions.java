package ngo.vnexpress.reader.basic;

import android.content.res.Resources;

import java.net.InetAddress;

/**
 * @author Fabio Ngo Class store basic functions ( most-used functions)
 */
public class BasicFunctions {

    /**
     * Convert DP to PX
     */
    public static int dpToPx(int dp, Resources resources) {
        float density = resources.getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    // CHECK IF IS CONNECTION TO INTERNET OR NOT
    public static boolean isConnectingToInternet() {

        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


}

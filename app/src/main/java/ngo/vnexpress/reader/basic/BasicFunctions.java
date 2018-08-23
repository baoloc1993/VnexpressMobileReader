package ngo.vnexpress.reader.basic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
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
    @SuppressLint("AddJavascriptInterface")
    public static void fetchHtml(String url, OnFetchedHtmlListener onFetchedHtmlListener, Context context){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            onFetchedHtmlListener.onFetchedHtml("");
            return;
        }
        onFetchedHtmlListener.onFetchedHtml(doc.body().html());
    }
    private static class CustomJavaScriptInterface {
    
    }
}

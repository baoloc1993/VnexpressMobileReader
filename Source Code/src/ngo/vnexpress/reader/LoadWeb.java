package ngo.vnexpress.reader;

import java.io.IOException;

import ngo.vnexpress.reader.Fragments.DisplayWebNewsFragment;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.analytics.tracking.android.Log;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class LoadWeb extends AsyncTask<String, String, String> {
	String link;
	
	private Document doc;
	private ProgressDialog pDialog;
	private WebView webView;
	public LoadWeb(String link, WebView webView) {
		this.link= link;
		this.webView = webView;
	}
	@Override
	protected String doInBackground(String... params) {
		
		try {
			setDoc(org.jsoup.Jsoup.connect(link).get());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(new Exception("not connect"));
		}
		
		

		Element title = doc.body().getElementsByClass("title_news").first();
		if (title != null) { // in normal page
			Element content = doc.body().getElementsByClass("fck_detail")
					.first();
			Element short_des = doc.body().getElementsByClass("short_intro")
					.first();
			
			
			short_des.attr("style", "font-weight: 900");
			Elements script = doc.body().getElementsByTag("script").remove();
			Elements imgs = content.getElementsByTag("img");
			for (Element img : imgs) {

				img.attr("width", "100%");
			}
			//tv.setText(doc.body().html());

			DisplayWebNewsFragment.htmlContent = title.html()+short_des.html()+content.html();
			 
		} else { //in video page
			
			
			DisplayWebNewsFragment.htmlContent = "";
			
		}

		return null;
		// TODO Auto-generated method stub
		
	}
	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(String args) {
		// dismiss the dialog after getting all products
		if (pDialog != null) {
			pDialog.dismiss();
		} else {

		}
		if(DisplayWebNewsFragment.htmlContent=="") {
			webView.loadUrl(link);
		}else {
			webView.loadData(DisplayWebNewsFragment.htmlContent, "text/html; charset=UTF-8", null);
		}
		

	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
			pDialog = new ProgressDialog(MainActivity.activity);
			pDialog.setMessage("Đang tải bài báo ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		
		

	}
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}

}

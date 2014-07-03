package ngo.vnexpress.reader.Adapters;

/**
 * Adapter of GridView
 */
import java.util.ArrayList;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.Imageloader.ImageLoader;
import ngo.vnexpress.reader.RSS.RSSItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridNewsItemAdapter extends ArrayAdapter<RSSItem> {
	public GridNewsItemAdapter(Context context, int resource) {
		super(context, resource);

		// TODO Auto-generated constructor stub
	}

	// declaring our ArrayList of items
	private ArrayList<RSSItem> objects;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public GridNewsItemAdapter(Context context, int textViewResourceId,
			ArrayList<RSSItem> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater
					.inflate(R.layout.preview_single_news_grid_layout, null);

		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
		RSSItem i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView title = (TextView) v.findViewById(R.id.title);
			ImageView icon = (ImageView) v.findViewById(R.id.icon);
			// TextView content = (TextView)v.findViewById(R.id.content);
			// TextView timestamp = (TextView)v.findViewById(R.id.timestamp);

			// check to see if each individual textview is null.
			// if not, assign some text!
			// Config parameter of each textview and imageview(Resize)
			// For TITLE, ICON, CONTENT, TIMESTAMP

			if (title != null) {
				title.setText(i.getTitle());
			}
			//
			if (icon != null) {
				int size = MainActivity.getStandardSize() / 3;
				// icon.getLayoutParams().width = size;
				icon.getLayoutParams().height = size;

				ImageLoader imgLoader = new ImageLoader(getContext());
				// Loader image - will be shown before loading image
				int loader = R.drawable.image_not_found;

				imgLoader.DisplayImage(i.getImgUrl(), loader, icon);
				// icon
				// icon.getLayoutParams().height = icon.getLayoutParams().width;
				// icon.getLayoutParams().height = size;
				// String url_img = i.getImgUrl();
				// icon.setWebViewClient(new WebViewClient());
				// icon.getSettings().setJavaScriptEnabled(true);

				// Convert to html code
				// String img_html =
				// "<html>"
				// + "<body style='margin:0;padding:0;'>"
				// + "<img src=\""
				// + url_img
				// + "\" width=\""
				// + "100"
				// + "%\" height=\""
				// + String.valueOf(size)
				// + "px\" >"
				// + "</body>"
				// + "</html>";
				// icon.loadUrl("javascript:document.body.style.zoom = "+String.valueOf(size)+";");
				// icon.setInitialScale(scaleInPercent);
				// icon.setInitialScale(100);
				// icon.loadData(img_html, "text/html", null);

			}
			// if (content != null){
			// content.setText(i.getDescription());
			// }
			// if (timestamp != null){
			// timestamp.setText(i.getPubdate());
			// }

		}

		// the view must be returned to our activity
		return v;

	}

}

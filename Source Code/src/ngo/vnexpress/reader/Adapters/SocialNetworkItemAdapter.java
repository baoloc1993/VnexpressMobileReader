package ngo.vnexpress.reader.Adapters;
/**
 * Adatper for Social Fragment
 */
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.BasicFunctions.BasicFunctions;
import ngo.vnexpress.reader.Items.SocialNetworkItem;

public class SocialNetworkItemAdapter extends ArrayAdapter<SocialNetworkItem> {
	
	public ProgressBar progressBar;

	public SocialNetworkItemAdapter(Context context, int resource) {
		super(context, resource);

		// TODO Auto-generated constructor stub
	}

	// declaring our ArrayList of items
	private ArrayList<SocialNetworkItem> objects;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 * 
	 * @params orientation to be used in setting up components' layout
	 */
	public SocialNetworkItemAdapter(Context context, int textViewResourceId,
			ArrayList<SocialNetworkItem> objects) {
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
			v = inflater.inflate(R.layout.single_social_network_header, null);

		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		SocialNetworkItem i = objects.get(position);

		if (i != null) {

			/**
			 * set Icon resource and size
			 */
			ImageView icon = (ImageView) v.findViewById(R.id.icon);
			if (icon != null) {
				icon.setImageResource(i.getImageID());
				/**
				 * get suitable size to scale image
				 */
				int size = MainActivity.getStandardSize()/objects.size()/3;
				BasicFunctions.ResizeImageView(size, icon);
				
			}
			/**
			 * Set background's color
			 */
			RelativeLayout relativeLayout = (RelativeLayout) v
					.findViewById(R.id.background);
			relativeLayout.setBackgroundColor(i.getBackground());
			
			/**
			 * 
			 * Progress Bar
			 */
			progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
			progressBar.setMax(100);
			int color = Color.rgb(247, 193, 0);
			progressBar.getProgressDrawable().setColorFilter(color,
					Mode.MULTIPLY);
			;

		}

		// the view must be returned to our activity
		return v;

	}

}

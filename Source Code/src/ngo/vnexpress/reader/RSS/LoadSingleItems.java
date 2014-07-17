package ngo.vnexpress.reader.RSS;
import ngo.vnexpress.reader.Constant;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.Fragments.DisplaySwipeViewNewsFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;

public class LoadSingleItems extends AsyncTask<String, String, String>{
		//List<RSSItem> rssItems = new ArrayList<RSSItem>();
		private ProgressDialog pDialog;
		private AdapterView<?> parent;
		int position;
		long id;
		//RSSParser rssParser = new RSSParser();
		//private ViewGroup viewGroup;
		public LoadSingleItems(AdapterView<?> parent, int position, long id) {
			// TODO Auto-generated constructor stub
			this.id = id;
			this.parent = parent;
			this.position = position;
		}
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(
					MainActivity.activity);
			pDialog.setMessage("Loading websites ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
					MainActivity.activity);
			RSSItem rss_item = (RSSItem) parent.getItemAtPosition(position);
			WebSite website = rssDb.getSiteByLink(rss_item.getLink());
			// List<WebSite> websites = rssDb.getAllSitesByID();

			// transfer link of current Item to other fragment
			Bundle args = new Bundle();
			args.putInt(DisplaySwipeViewNewsFragment.ARG_ID,
					website.getId());
			// args.putString(DisplayFullNewsFragment.ARG_TYPE_FRAGMENT,
			// getClass().toString());
			// args.putInt(DisplayFullNewsFragment.ARG_SIZE,
			// websites.size());
			// Log.d("SET ON ITEM CLICK LISTENER",
			// String.valueOf(website.getId()));

			FragmentManager fragmentManager = MainActivity.activity
					.getFragmentManager();
			DisplaySwipeViewNewsFragment displaySwipeViewNewsFragment = new DisplaySwipeViewNewsFragment();

			displaySwipeViewNewsFragment.setArguments(args);

			// Go to DisplayFullNewsFragment
			displaySwipeViewNewsFragment.setHasOptionsMenu(true);
			fragmentManager.beginTransaction()
					.replace(R.id.container, displaySwipeViewNewsFragment)
					.commit();
			MainActivity.currentFragment = Constant.Web;
			return null;
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
			
		}
	}
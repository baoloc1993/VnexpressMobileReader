//package ngo.vnexpress.reader.rss;
//
//import android.view.ViewGroup;
//
//import ngo.vnexpress.reader.MainActivity;
//import ngo.vnexpress.reader.backgroundnotification.NotificationService;
//import ngo.vnexpress.reader.items.RSSItem;
//
//public class LoadRSSFeedItemsService extends LoadRSSFeedItems {
//
//    public LoadRSSFeedItemsService(ViewGroup viewGroup) {
//        super(viewGroup);
//        // TODO Auto-generated constructor stub
//    }
//
//    public LoadRSSFeedItemsService() {
//        super(null);
//        // TODO Auto-generated constructor stub
//    }
//
//    /**
//     * Before starting background thread Show Progress Dialog
//     */
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//    }
//
//    /**
//     * getting all recent articles and showing them in listview
//     */
//    @Override
//    protected String doInBackground(String... args) {
//        // MainActivity.newArticlePerCate.clear();
//
//        // newArticlePerCate
//        // Go through all categories inside an Asyntask
//        for (final String name : RSSItem.RSSCategory.values()) {
//            //Log.d("DEBUG", "CATE " + name.toString());
//
//            //Do not load RSS when activity start
//            if (MainActivity.stopService) {
//                return null;
//            }
//            // Delay 2s to synchronize 2 threads
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            MainActivity.nameCategory = name;
//
////			if (BasicFunctions.isConnectingToInternet(
////					.getApplicationContext())) {
//
//            rssItems = rssParser.getRSSFeedItems(getRssUrl());
////            Collections.reverse(rssItems);
//            //}
//
//            // updating UI from Background Thread
//
//            //Handler handler = new Handler(Looper.getMainLooper());
//            //Stop load database when main activity start
//
//            Thread background = new Thread((() -> {
//                if (MainActivity.stopService) {
//                    return;
//                }
//
//                //rssDb.close();
//                // The number of new article
//                int newArticle = 0;
//                NotificationService.numberNewPost += rssItems.size();
//                if (NotificationService.newArticlePerCate.get(name) != null) {
//                    newArticle = NotificationService.newArticlePerCate.get(name) + rssItems.size();
//
//                }
//                NotificationService.newArticlePerCate.put(name, newArticle);
//
////						Log.d("DEBUG",
////								"CATE + " + MainActivity.nameCategory.toString()
////										+ "  " + String.valueOf(newSizeDatabase)
////										+ " - " + String.valueOf(oldSizeDatabase));
//                // MainActivity.rssItems = rssItems;
//
//            }));
//            background.start();
//            background.interrupt();
//        }
//
//        return null;
//    }
//
//    /**
//     * After completing background task Dismiss the progress dialog
//     **/
//    @Override
//    protected void onPostExecute(String args) {
//        // dismiss the dialog after getting all products
//
//    }
//
//    @Override
//    public String getRssUrl() {
//        String url_name;
//        //NotificationService service = new NotificationService();
//        switch (MainActivity.nameCategory) {
//
//            case Homepage:
//                url_name = "http://vnexpress.net/rss/tin-moi-nhat.rss";
//                break;
//            case Business:
//                url_name = "http://vnexpress.net/rss/kinh-doanh.rss";
//                break;
//            case Car:
//                url_name = "http://vnexpress.net/rss/oto-xe-may.rss";
//                break;
//            case Chat:
//                url_name = "http://vnexpress.net/rss/tam-su.rss";
//                break;
//            case Digital:
//                url_name = "http://vnexpress.net/rss/so-hoa.rss";
//                break;
//            case Entertainment:
//                url_name = "http://vnexpress.net/rss/giai-tri.rss";
//                break;
//            case Sports:
//                url_name = "http://vnexpress.net/rss/the-thao.rss";
//                break;
//            case Funny:
//                url_name = "http://vnexpress.net/rss/cuoi.rss";
//                break;
//            case Laws:
//                url_name = "http://vnexpress.net/rss/phap-luat.rss";
//                break;
//            case Life:
//                url_name = "http://vnexpress.net/rss/doi-song.rss";
//                break;
//            case News:
//                url_name = "http://vnexpress.net/rss/thoi-su.rss";
//                break;
//            case Science:
//                url_name = "http://vnexpress.net/rss/khoa-hoc.rss";
//                break;
//            case Social:
//                url_name = "http://vnexpress.net/rss/cong-dong.rss";
//                break;
//            case Travelling:
//                url_name = "http://vnexpress.net/rss/du-lich.rss";
//                break;
//            case World:
//                url_name = "http://vnexpress.net/rss/the-gioi.rss";
//                break;
//            default:
//                url_name = "http://vnexpress.net/rss/tin-moi-nhat.rss";
//
//        }
//        return url_name;
//        //return super.getRssUrl();
//
//    }
//
//}

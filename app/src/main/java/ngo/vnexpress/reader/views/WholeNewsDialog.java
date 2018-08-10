package ngo.vnexpress.reader.views;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.managers.RSSItemManager;

import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

public class WholeNewsDialog extends DialogFragment implements LoadingComponent {
    public static final String TAG = "WholeNewsDialog";
    private Toolbar toolbar;
    private RSSItem news;
    private View rootView;
    private View newsContainer;
    private ProgressBar progressBar;
    public static int width;
    public static int height;
    public WholeNewsDialog(RSSItem news) {
        this.news = news;
    }

    public static void loadBackground(WholeNewsDialog dialog) {
        View view = dialog.rootView;
        RSSItem newsItem = dialog.news;

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                newsItem.fetchContent();
                return "fetched";
            }

            @Override
            protected void onPreExecute() {
                dialog.showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                dialog.hideLoading();
                TextView title = view.findViewById(R.id.news_title);
                TextView description = view.findViewById(R.id.news_description);
                TextView readMore = view.findViewById(R.id.news_read_more);
                ImageView newsImage = view.findViewById(R.id.news_image);
                ScrollView scrollView = view.findViewById(R.id.news_scrollView);
                scrollView.scrollTo(0, 0);
                title.setText(newsItem.getTitle());
                description.setText(Html.fromHtml(newsItem.getDescription()));
                ImageView placeholder = view.findViewById(R.id.content_image_placeholder);

                SpannableStringBuilder content = newsItem.getContent(dialog.getContext(), readMore::setText);

                readMore.setText(content);


                int width = MainActivity.screenWidth;
                int height = (int) (width * 1080 / 1920.0);
                if (newsItem.getImgUrl().equals("")) {
                    newsImage.setVisibility(View.GONE);
                } else {
                    Picasso.get().load(newsItem.getImgUrl()).error(R.drawable.image_not_found).resize(width, height).into(newsImage);
                }
            }
        }.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            RSSItemManager rssItemManager = RSSItemManager.getInstance(MainActivity.currentSource);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setStatusBarColor(Color.parseColor(rssItemManager.getHeaderBgColor()));
            }
            showArticle();



        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = getActivity().getLayoutInflater().inflate(R.layout.whole_news_layout, container, false);
        newsContainer = rootView.findViewById(R.id.whole_news_container);
        progressBar = rootView.findViewById(R.id.dialog_progressbar);
        toolbar = rootView.findViewById(R.id.news_toolbar);
        RSSItemManager rssItemManager = RSSItemManager.getInstance(MainActivity.currentSource);
        int bgColor = Color.parseColor(rssItemManager.getHeaderBgColor());
        int textColor = Color.parseColor(rssItemManager.getToolbarTextColor());
        toolbar.setBackgroundColor(bgColor);
        toolbar.setSubtitleTextColor(textColor);
        toolbar.setTitleTextColor(textColor);

        toolbar.setTitle(news.getSource());


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.getNavigationIcon().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
//        DrawableCompat.setTint(toolbar.getNavigationIcon().mutate(),textColor);
//        DrawableCompat.setTintMode(toolbar.getNavigationIcon(), PorterDuff.Mode.SRC_IN);
        toolbar.setSubtitle(news.getCategory());

        toolbar.inflateMenu(R.menu.webview);
        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            MenuItemCompat.setIconTintList(toolbar.getMenu().getItem(i), ColorStateList.valueOf(textColor));
        }

        toolbar.setNavigationOnClickListener(item -> this.dismiss());
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.next_article:
                    goNextArticle();
                    break;
                case R.id.prev_article:
                    goPrevArticle();
                    break;
                case R.id.share:
                    share();
                    break;
            }
            return true;

        });
//        MainActivity.activity.setSupportActionBar(toolbar);

        return rootView;

    }

    private void share() {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, "Send from app: " + news.getLink());
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");


    }

    private void goNextArticle() {
        List<RSSItem> listNews = RSSItemManager.getInstance(MainActivity.currentSource).getItemByCategory(news.getCategory());
        int currentIndex = listNews.indexOf(news);
        if (currentIndex == 0) {
            Toast.makeText(getContext(), "No later article", Toast.LENGTH_SHORT).show();
        } else {
            news = listNews.get(currentIndex - 1);
            showArticle();
        }
    }

    private void goPrevArticle() {
        List<RSSItem> listNews = RSSItemManager.getInstance(MainActivity.currentSource).getItemByCategory(news.getCategory());
        int currentIndex = listNews.indexOf(news);
        if (currentIndex + 1 == listNews.size()) {
            Toast.makeText(getContext(), "No older article", Toast.LENGTH_SHORT).show();

        } else {
            news = listNews.get(currentIndex + 1);
            showArticle();
        }

    }

    private void showArticle() {

        if (rootView == null) {
            return;
        }
        loadBackground(this);


    }


    @Override
    public void showLoading() {
        newsContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        newsContainer.setVisibility(View.VISIBLE);
    }
}

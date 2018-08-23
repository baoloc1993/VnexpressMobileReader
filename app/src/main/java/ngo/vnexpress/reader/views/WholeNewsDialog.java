package ngo.vnexpress.reader.views;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.MetricAffectingSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.annimon.stream.Stream;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.basic.BasicFunctions;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.managers.RSSItemManager;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

public class WholeNewsDialog extends DialogFragment implements LoadingComponent {
    public static final String TAG = "WholeNewsDialog";
    private Toolbar toolbar;
    private RSSItem news;
    private ViewGroup rootView;
    private ViewGroup newsContainer;
    private ProgressBar progressBar;
    public static int width;
    public static int height;
    public WholeNewsDialog(RSSItem news) {
        this.news = news;
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

        rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.whole_news_layout, container, false);
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
        BasicFunctions.fetchHtml(news.getLink(), (html) -> {
            news.fetchContent(html);
            getActivity().runOnUiThread(() -> {
                this.hideLoading();
                TextView title = rootView.findViewById(R.id.news_title);
                TextView description = rootView.findViewById(R.id.news_description);
                FeatureMedia newsMedia = rootView.findViewById(R.id.news_media);
                ScrollView scrollView = rootView.findViewById(R.id.news_scrollView);
                ViewGroup readmoreSection = rootView.findViewById(R.id.read_more_section);
                readmoreSection.removeAllViews();
                scrollView.scrollTo(0, 0);
                title.setText(news.getTitle());
                description.setText(Html.fromHtml(news.getDescription(),Html.FROM_HTML_MODE_LEGACY));
                ArrayList<View> readMore = new ArrayList<>();
    
                news.getContent(getContext(),spannableStringBuilder -> {
                    MetricAffectingSpan[] spans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), MetricAffectingSpan.class);
                    AtomicInteger index = new AtomicInteger();
                    AtomicInteger offset = new AtomicInteger();
                    final SpannableStringBuilder[] spannableStringBuilder1 = {new SpannableStringBuilder()};
                    Stream.of(spans).sorted(Comparator.comparingInt(spannableStringBuilder::getSpanStart)).forEach((MetricAffectingSpan span) -> {
                        int start = spannableStringBuilder.getSpanStart(span);
                        int end = spannableStringBuilder.getSpanEnd(span);
                        int flag = spannableStringBuilder.getSpanFlags(span);
            
                        if(index.get() < start){
                
                            char[] text = new char[start - index.get()];
                            spannableStringBuilder.getChars(index.get(),start,text,0);
                            String textString = String.valueOf(text).replaceAll("^\\s*(\n)+", "");
                            spannableStringBuilder1[0].append(textString);
                            index.set(end);
                        }
                        if(!(span instanceof MediaSpan)) {
                            char[] text = new char[end - start];
                            spannableStringBuilder.getChars(start, end, text, 0);
                            int newOffset = spannableStringBuilder1[0].length();
                            String textString = String.valueOf(text).replaceAll("^(\n)+", "");
                            spannableStringBuilder1[0].append(textString);
                            spannableStringBuilder1[0].setSpan(span,newOffset,spannableStringBuilder1[0].length(),flag);
                        }else{
                
                            TextView tv = createTextView(spannableStringBuilder1[0]);
                
                            if(tv.getText().length() >0){
                                readMore.add(tv);
                            }
                            spannableStringBuilder1[0] = new SpannableStringBuilder();
                            FeatureMedia featureMedia = new FeatureMedia(getContext());
                            featureMedia.setMediaUrl(((MediaSpan) span).getMediaUrl());
                
                
                            readMore.add(featureMedia);
                
                            offset.set(end);
                            index.set(end);
                        }
                    });
                    char[] text = new char[spannableStringBuilder.length() - index.get()];
                    spannableStringBuilder.getChars(index.get(),spannableStringBuilder.length(),text,0);
                    String textString = String.valueOf(text).replaceAll("^(\n)+", "");
                    spannableStringBuilder1[0].append(textString);
        
                    TextView tv = createTextView(spannableStringBuilder1[0]);
        
                    if(tv.getText().length() >0){
                        readMore.add(tv);
                    }
                    for(int i = 0 ; i < readMore.size();i++){
                        ViewGroup view = this.newsContainer;
                        View mView = readMore.get(i);
                        mView.setId(View.generateViewId());
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
                        layoutParams.setMargins(15,15,15,0);
                        mView.setPadding(0,0,0,0);
                        if(i == 0){
                            layoutParams.addRule(RelativeLayout.BELOW,description.getId());
                        }else{
                            layoutParams.addRule(RelativeLayout.BELOW,readMore.get(i-1).getId());
                        }
                        mView.setLayoutParams(layoutParams);
                        readmoreSection.addView(mView);
                    }
                });
    
    
                newsMedia.setMediaUrl(news.getImgUrl());
            });
            
        },getDialog().getContext());
        

    }
    
    TextView createTextView(SpannableStringBuilder spannableStringBuilder ){
        TextView textView = new TextView(getContext());
        textView.setText(spannableStringBuilder);
        textView.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        textView.setTextColor(Color.BLACK);
        return textView;
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

/**
 * handles individual article information like title, link, pubDate and description.
 */
package ngo.vnexpress.reader.items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.ReplacementSpan;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ngo.vnexpress.reader.basic.MyHtml;
import ngo.vnexpress.reader.basic.TagSpan;
import ngo.vnexpress.reader.views.MediaSpan;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;


public abstract class RSSItem extends Item {
    // All <item> node name
    protected String link = "";
    protected String description = "";
    protected String pubDate = "";
    protected String imgUrl = "";
    protected String content = "";
    protected String category = "";
    protected boolean isCached = false;
    protected String source = "";
    protected String htmlSource = "";
    protected String[] ignoreTags;
    private String baseUrl = "";
    private Stack<TagSpan> tagSpans;
    private transient ArrayList<ContentMedium> contentMedia = new ArrayList<>();
    private transient int numLoadedItem = 0;
    private transient SpannableStringBuilder spannableStringBuilder;
    private transient boolean isInIgnoredTag = false;
    private OnAllContentImagesLoadedListener onAllContentImagesLoadedListener;
    
    // constructor with parameters
    public RSSItem() {
        super("", UUID.randomUUID().toString());
        this.ignoreTags = this.setIgnoreTag();
    }
    
    protected abstract String[] setIgnoreTag();
    
    private RSSItem(String title) {
        super(title);
        baseUrl = getBaseUrl();
    }
    
    protected abstract String getBaseUrl();
    
    public String getContent() {
        return this.content;
    }
    
    public SpannableStringBuilder getContent(Context placeholder, OnAllContentImagesLoadedListener onAllContentImagesLoadedListener) {
        this.contentMedia = new ArrayList<>();
        this.tagSpans = new Stack<>();
        this.onAllContentImagesLoadedListener = onAllContentImagesLoadedListener;
        spannableStringBuilder = (SpannableStringBuilder) MyHtml.fromHtml(content, Html.FROM_HTML_MODE_LEGACY, s -> this.getDrawable(s, placeholder), this::handleTag, this::getVideo);
        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        
        for (int i = 0; i < contentMedia.size(); i++) {
//            contentImages.get(i).setBounds(0,0,500,600);
            MediaSpan mediaSpan = new MediaSpan(contentMedia.get(i).url);
//            MediaSpan mediaSpan = new MediaSpan(imageSpan.getDrawable().getIntrinsicWidth(),imageSpan.getDrawable().getIntrinsicHeight());
            
            int start = spannableStringBuilder.getSpanStart(imageSpans[i]);
            int end = spannableStringBuilder.getSpanEnd(imageSpans[i]);
            
            spannableStringBuilder.removeSpan(imageSpans[i]);
            
            spannableStringBuilder.setSpan(mediaSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
        }
        this.onAllContentImagesLoadedListener.onAllContentImagesLoaded(spannableStringBuilder);
        return spannableStringBuilder;
    }
    
    private void getVideo(String s) {
        if (isInIgnoredTag) {
            return;
        }
    
    
        ContentMedium contentMedium = new ContentMedium(s);
        contentMedia.add(contentMedium);
    }
    
    protected Drawable getDrawable(String s, Context context) {
        if (isInIgnoredTag) {
            return null;
        }
        
        
        ContentMedium contentMedium = new ContentMedium(s);
        contentMedia.add(contentMedium);


//        while(!loaded[0]){
//
//        }
        return null;
    }
    
    
    protected void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        TagSpan tagSpan = null;
        if (opening) {
            tagSpans.push(new TagSpan(tag, output.length()));
        } else {
            if (tagSpans.peek().tag.equals(tag)) {
                tagSpan = tagSpans.pop();
            }
            
        }
        
        for (int i = 0; i < ignoreTags.length; i++) {
            if (tag.equals(ignoreTags[i])) {
                if (opening) {
                    isInIgnoredTag = true;
                    
                    
                } else {
                    
                    output.delete(tagSpan.start, output.length());
                    
                    isInIgnoredTag = false;
                    
                }
            }
        }
        
        
        
        
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * Fetch news Image and content from element
     *
     * @param item
     */
    public abstract void fetchRSS(Element item);
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    // constructor
    
    /**
     * All GET methods
     */
    
    public String getId() {
        return id;
    }
    
    @Override
    public void onLoaded() {
        this.contentMedia = new ArrayList<>();
    }
    
    public String getLink() {
        return this.link;
    }
    
    /**
     * All SET methods
     */
    
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    // public void setGuid(String guid){
    // this._guid = guid;
    // }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPubDate() {
        return this.pubDate;
    }
    
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    
    public String getImgUrl() {
        return imgUrl;
    }
    
    public void setImgUrl(String _img_url) {
        this.imgUrl = _img_url;
        
    }
    
    // public String getGuid(){
    // return this._guid;
    // }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    
    public abstract void fetchContent(String html);
    
    public String getHtmlSource() {
        return htmlSource;
    }
    
    public interface OnAllContentImagesLoadedListener {
        void onAllContentImagesLoaded(SpannableStringBuilder spannableStringBuilder);
    }
    
    public class IgnoredTagSpan extends TagSpan {
        
        public IgnoredTagSpan(String tag, int start) {
            super(tag, start);
        }
    }
    
    protected class ContentMedium {
        String url;
        
        ContentMedium(String url) {
            this.url = url;
        }
        
    }
    
    
}

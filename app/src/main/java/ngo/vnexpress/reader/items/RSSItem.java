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
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;
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
    private String baseUrl ="";
    private Stack<IgnoredTagSpan> ignoredTagSpans;
    private transient ArrayList<ContentImage> contentImages = new ArrayList<>();
    private transient int numLoadedItem = 0;
    private transient SpannableStringBuilder spannableStringBuilder;
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
    
    public String getContent(){
        return this.content;
    }
    public SpannableStringBuilder getContent(Context placeholder, OnAllContentImagesLoadedListener onAllContentImagesLoadedListener) {
        this.contentImages = new ArrayList<>();
        this.ignoredTagSpans = new Stack<>();
        this.onAllContentImagesLoadedListener = onAllContentImagesLoadedListener;
        spannableStringBuilder = (SpannableStringBuilder) Html.fromHtml(content,Html.FROM_HTML_MODE_LEGACY, s -> RSSItem.this.getDrawable(s, placeholder), RSSItem.this::handleTag);
        
        return spannableStringBuilder;
    }

    protected Drawable getDrawable(String s, Context context) {
        ImageView imageView = new ImageView(context);


        int width = (int) (MainActivity.screenWidth * 0.9);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));



        ContentImage contentImage = new ContentImage(s);
        contentImages.add(contentImage);
        Picasso.get().load(s).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

                Bitmap original = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                int height = (int) ((double) width * original.getHeight() / original.getWidth());
                Bitmap bitmap = Bitmap.createScaledBitmap(original, width, height, false);
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                drawable.setBounds(0, 0, width, height);
                contentImage.setImageSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM));

                numLoadedItem++;
                checkAllLoaded();


            }

            @Override
            public void onError(Exception e) {
                contentImage.setImageSpan(null);
                numLoadedItem++;
                checkAllLoaded();
            }
        });

//        while(!loaded[0]){
//
//        }
        return null;
    }

    protected void checkAllLoaded() {

        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        if (numLoadedItem < imageSpans.length) {
            return;
        }
        for (int i = 0; i < contentImages.size(); i++) {
//            contentImages.get(i).setBounds(0,0,500,600);
            ImageSpan imageSpan = contentImages.get(i).imageSpan;

            int start = spannableStringBuilder.getSpanStart(imageSpans[i]);
            int end = spannableStringBuilder.getSpanEnd(imageSpans[i]);

            spannableStringBuilder.removeSpan(imageSpans[i]);

            spannableStringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        this.onAllContentImagesLoadedListener.onAllContentImagesLoaded(spannableStringBuilder);

    }

    protected void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        
        for (int i = 0; i < ignoreTags.length; i++) {
            if (tag.equals(ignoreTags[i])) {
                if (opening) {
                    ignoredTagSpans.push(new IgnoredTagSpan(tag,output.length()));
                    
                } else {
                    IgnoredTagSpan ignoredTag = ignoredTagSpans.pop();
                    output.delete(ignoredTag.start,output.length());
                    

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
        this.contentImages = new ArrayList<>();
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

    public abstract void fetchContent();

    public String getHtmlSource() {
        return htmlSource;
    }

    public interface OnAllContentImagesLoadedListener {
        void onAllContentImagesLoaded(SpannableStringBuilder spannableStringBuilder);
    }

    public class IgnoredTagSpan {
        String tag;
    
        public IgnoredTagSpan(String tag, int start) {
            this.tag = tag;
            this.start = start;
        }
    
        int start;
        int end;
    }

    protected class ContentImage {
        String url;
        ImageSpan imageSpan;

        ContentImage(String url) {
            this.url = url;
        }

        public void setImageSpan(ImageSpan imageSpan) {
            this.imageSpan = imageSpan;
        }

    }
}

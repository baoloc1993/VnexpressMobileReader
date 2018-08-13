package ngo.vnexpress.reader.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;

import android.text.Html;
import android.text.SpannableStringBuilder;
import com.annimon.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;

import ngo.vnexpress.reader.managers.RSSItemManager;

import java.util.Stack;

public class ChannelNewsAsiaItem extends RSSItem {
    private Stack<String> pictureUrl;
    @Override
    public void fetchRSS(Element item) {
        title = item.getElementsByTag("title").text();
        pubDate = item.getElementsByTag("pubDate").text();
        id = item.getElementsByTag("guid").text();
        link = id;
        description = item.getElementsByTag("description").text();
        Element e = item.getElementsByTag("media:thumbnail").get(0);
        imgUrl = e.attr("url");
    }

    @Override
    public void fetchContent() {
        if (isCached) {
            return;
        }
        Document document;
        try {
            document = Jsoup.connect(getLink()).get();
            System.out.println(getLink());

            Element body = document.body();
            Element articleRoot = body.getElementsByTag("article").get(0);
            Element readMoreSection = articleRoot.getElementsByAttributeValueContaining("class","c-rte--article").get(0);
            Elements pictures = readMoreSection.getElementsByTag("picture");
            for(int i = 0;i < pictures.size();i++){
                Element picture = pictures.get(i);
                String imageUrlSet = picture.getElementsByTag("source").get(0).attr("data-srcset");
                String[] urls = imageUrlSet.split(",");
                String url = urls[urls.length-1].trim().split(" ")[0];
                url = getBaseUrl() + url;
                picture.getElementsByTag("img").get(0).attr("src",url);
            }
            
            
            
            setContent(readMoreSection.html());

        } catch (Exception e) {
            setContent(e.getMessage());
        } finally {
            isCached = true;
            RSSItemManager.getInstance(this.getClass()).saveItems();
        }
    }
    
    @Override
    public SpannableStringBuilder getContent(Context placeholder, OnAllContentImagesLoadedListener onAllContentImagesLoadedListener) {
        pictureUrl = new Stack<>();
        return super.getContent(placeholder, onAllContentImagesLoadedListener);
    }
    
    
    
    @Override
    protected String[] setIgnoreTag() {
        return new String[]{
                "aside","source"
        };
    }
    
    @Override
    protected String getBaseUrl() {
        return "https://www.channelnewsasia.com";
    }
    
    
}

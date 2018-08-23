package ngo.vnexpress.reader.basic;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Fabio Ngo Class store basic functions ( most-used functions)
 */
public class MyHtml {
    public interface VideoGetter{
        void getVideo(String s);
    }
    public interface ImageGetter{
        Drawable getDrawable(String s);
    }
    public interface TagHandler{
        void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader);
    }
    
    public static Spanned fromHtml(String content, int mode, ImageGetter imageGetter, TagHandler tagHandler, VideoGetter videoGetter){
        AtomicReference<TagSpan> videoSpan = new AtomicReference<>();
        Document doc = Jsoup.parse(content);
        Elements videos = doc.select("video");
        for(int i = 0 ; i < videos.size();i++){
            String videoUrl = videos.get(i).attr("src");
            videos.get(i).prependText(videoUrl);
        }
        
        return Html.fromHtml(doc.html(), mode, imageGetter::getDrawable, (opening, tag, output, xmlReader) -> {
            if(tag.equals("video")){
                if(opening){
                    videoSpan.set(new TagSpan(tag, output.length()));
                }else{
                    char[] text = new char[output.length()-videoSpan.get().start];
                    output.getChars(videoSpan.get().start,output.length(),text,0);
                    
                    output.delete(videoSpan.get().start,output.length());
                    output.append("\n");
                    output.setSpan(new ImageSpan((Drawable)null),videoSpan.get().start,output.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    videoGetter.getVideo(String.valueOf(text));
                    
                }
            }else{
                tagHandler.handleTag(opening,tag,output,xmlReader);
            }
        });
    }
    
    
}

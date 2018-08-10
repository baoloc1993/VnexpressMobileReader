package ngo.vnexpress.reader.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;

import com.annimon.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xml.sax.XMLReader;

import ngo.vnexpress.reader.managers.RSSItemManager;

public class ChannelNewsAsiaItem extends RSSItem {
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


            final String[] content = {""};
            Stream.of(articleRoot.getElementsByTag("p").eachText()).forEach( s -> content[0] += (s + "\n\n"));
            setContent(content[0]);

        } catch (Exception e) {
            setContent(e.getMessage());
        } finally {
            isCached = true;
            RSSItemManager.getInstance(this.getClass()).saveItems();
        }
    }

    @Override
    public Drawable getDrawable(String s, Context placeholder) {
        return null;
    }

    @Override
    public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

    }
}

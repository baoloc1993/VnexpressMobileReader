package ngo.vnexpress.reader.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;

import ngo.vnexpress.reader.managers.RSSItemManager;

public class StraightTimesItem extends RSSItem {

    @Override
    public void fetchRSS(Element item) {
        title = item.getElementsByTag("title").text();
        pubDate = item.getElementsByTag("pubDate").text();
        id = item.getElementsByTag("guid").text();
        link = id;
        description = item.getElementsByTag("description").text();
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
            Element articleRoot = body.getElementsByAttributeValueContaining("class", "node-article").get(0);
            Elements mediaEntities = articleRoot.getElementsByAttributeValueContaining("class", "media-entity");
            if (mediaEntities.size() > 0) {
                setImgUrl(mediaEntities.get(0).attr("resource"));
            }

            Element readmoreSection = articleRoot.getElementsByAttributeValueContaining("itemprop", "articleBody").get(0);
//            final String[] content = {""};
//            Stream.of(readmoreSection.getElementsByTag("p").eachText()).forEach((String s) -> content[0] += (s + "\n\n"));
            setContent(readmoreSection.html());

        } catch (Exception e) {
            setContent("This article is premium");
        } finally {
            isCached = true;
            RSSItemManager.getInstance(this.getClass()).saveItems();
        }
    }


    @Override
    public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

    }

    @Override
    protected Drawable getDrawable(String s, Context placeholder) {
        return null;
    }
}

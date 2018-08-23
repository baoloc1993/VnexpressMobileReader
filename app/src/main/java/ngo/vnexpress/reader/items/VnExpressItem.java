package ngo.vnexpress.reader.items;

import android.text.Editable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xml.sax.XMLReader;

public class VnExpressItem extends RSSItem {
    @Override
    protected String[] setIgnoreTag() {
        return new String[]{"noscript","script","style","iframe"};
    }
    
    @Override
    protected String getBaseUrl() {
        return "https://vnexpress.net/";
    }
    
    

    @Override
    public void fetchRSS(Element item) {
        title = item.getElementsByTag("title").text();
        pubDate = item.getElementsByTag("pubDate").text();
        id = item.getElementsByTag("guid").text();
        link = id;
        String descriptionPart = item.getElementsByTag("description").text();
        Document des = Jsoup.parse(descriptionPart);
        imgUrl = des.select("img").attr("src");
        description = des.text();

    }

    @Override
    public void fetchContent(String html) {
        if (isCached) {
            return;
        }
        Document document;
        try {
            document = Jsoup.parse(html);

            Element body = document.body();
            Element articleRoot = body.getElementsByAttributeValueContaining("class","fck_detail").get(0);

            htmlSource = articleRoot.html();
            setContent(htmlSource);
        } catch (Exception e) {
            setContent(e.getMessage());
        } finally {
            isCached = true;
        }
    }
}

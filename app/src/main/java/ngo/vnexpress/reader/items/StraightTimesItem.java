package ngo.vnexpress.reader.items;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
//            Elements scripts = readmoreSection.getElementsByTag("script");
            String readmoreHtml = readmoreSection.html();
//            for(int i = 0 ; i < scripts.size();i++){
//                readmoreHtml = readmoreHtml.replace(scripts.get(i).outerHtml(),"");
//            }
//            final String[] content = {""};
//            Stream.of(readmoreSection.getElementsByTag("p").eachText()).forEach((String s) -> content[0] += (s + "\n\n"));
            setContent(readmoreHtml);

        } catch (Exception e) {
            setContent(e.getMessage());
        } finally {
            isCached = true;
//            RSSItemManager.getInstance(this.getClass()).saveItems();
        }
    }




    @Override
    protected String[] setIgnoreTag() {
        return new String[]{"aside","script"};

    }
    
    @Override
    protected String getBaseUrl() {
        return "https://http://straitstimes.com/";
    }
}

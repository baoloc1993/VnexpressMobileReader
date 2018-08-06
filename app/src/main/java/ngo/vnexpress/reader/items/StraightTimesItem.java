package ngo.vnexpress.reader.items;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ngo.vnexpress.reader.MainActivity;
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
            final String[] content = {""};
            readmoreSection.getElementsByTag("p").eachText().forEach(s -> content[0] += (s + "\n\n"));
            setContent(content[0]);

        } catch (Exception e) {
            setContent("This article is premium");
        }finally {
            isCached = true;
            RSSItemManager.getInstance(this.getClass()).saveItems(MainActivity.activity);
        }
    }
}

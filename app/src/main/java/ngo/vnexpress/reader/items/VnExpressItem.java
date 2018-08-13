package ngo.vnexpress.reader.items;

import android.text.Editable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xml.sax.XMLReader;

public class VnExpressItem extends RSSItem {
    @Override
    protected String[] setIgnoreTag() {
        return new String[0];
    }
    
    @Override
    protected String getBaseUrl() {
        return "https://vnexpress.net/";
    }
    
    @Override
    public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {
        System.out.println("Handle tag: " + s);
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
    public void fetchContent() {
        if (isCached) {
            return;
        }
        Document document;
        try {
            document = Jsoup.connect(getLink()).get();
            System.out.println(getLink());

            Element body = document.body();
            Element articleRoot = body.getElementsByAttributeValueContaining("class","fck_detail").get(0);

            htmlSource = articleRoot.html();
//            final String[] content = {""};
//            Stream.of(articleRoot.getElementsByTag("p").eachText()).forEach(s -> content[0] += (s + "\n\n"));
//            setContent(content[0]);
            setContent(htmlSource);
//            Html.fromHtml(htmlSource, s -> {
//                ImageView imageView = new ImageView(context);
//                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                return VnExpressItem.this.getDrawable(s,imageView);
//            }, null);
        } catch (Exception e) {
            setContent(e.getMessage());
        } finally {
            isCached = true;
//            RSSItemManager.getInstance(this.getClass()).saveItems();
        }
    }
}

/**
 * handles individual article information like title, link, pubDate and description.
 */
package ngo.vnexpress.reader.items;

import org.jsoup.nodes.Element;

import java.util.UUID;


public abstract class RSSItem extends Item {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Fetch news Image and content from element
     * @param item
     */
    public abstract void fetchRSS(Element item);

    // All <item> node name
    protected String link="";
    protected String description="";
    protected String pubDate="";
    protected String imgUrl="";
    protected String content="";
    protected String category="";
    protected boolean isCached = false;
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    protected String source="";
    // constructor


    // constructor with parameters
    public RSSItem() {
        super("", UUID.randomUUID().toString());
    }



    private RSSItem(String title) {
        super(title);
    }

    /**
     * All SET methods
     */



    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    // public void setGuid(String guid){
    // this._guid = guid;
    // }

    public void setImgUrl(String _img_url) {
        this.imgUrl = _img_url;
    }


    /**
     * All GET methods
     */

    public String getId() {
        return id;
    }


    public String getLink() {
        return this.link;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPubDate() {
        return this.pubDate;
    }

    // public String getGuid(){
    // return this._guid;
    // }





    public String getImgUrl() {
        return imgUrl;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public abstract void fetchContent();
}

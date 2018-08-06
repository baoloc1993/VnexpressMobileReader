/**
 * The main purpose of RSS Parser class is to parse the rss xml
 * and return RSSFeed object. When user enters a website url this class will do following tasks
 * <p>
 * -> Will get the html source code of the website
 * -> Parse the html source code and will get rss url
 * -> After getting rss url will get rss xml and parse the xml.
 * -> Once rss xml parsing is done will return RSSFeed object of the rss xml.
 */
package ngo.vnexpress.reader.rss;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.managers.RSSItemManager;

public class RSSParser {

    // RSS XML document CHANNEL tag
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_LANGUAGE = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static int DEFAULT_ID = 0;

    // constructor
    public RSSParser() {

    }

//	/***
//	 * Get RSS feed from url
//	 *
//	 * @param url
//	 *            - is url of the website
//	 * @return RSSFeed class object
//	 */
//	public RSSFeed getRSSFeed(String url) {
//		RSSFeed rssFeed = null;
//		String rss_feed_xml = null;
//
//		// getting rss link from html source code
//		String rss_url = this.getRSSLinkFromURL(url);
//
//		// check if rss_link is found or not
//		if (rss_url != null) {
//			// RSS url found
//			// get RSS XML from rss url
//			rss_feed_xml = this.getXmlFromUrl(rss_url);
//			// check if RSS XML fetched or not
//			if (rss_feed_xml != null) {
//				// successfully fetched rss xml
//				// parse the xml
//				try {
//					Document doc = this.getDomElement(rss_feed_xml);
//					NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
//					Element e = (Element) nodeList.item(0);
//
//					// RSS nodes
//					String title = this.getValue(e, TAG_TITLE);
//					String link = this.getValue(e, TAG_LINK);
//					String description = this.getValue(e, TAG_DESRIPTION);
//					String language = this.getValue(e, TAG_LANGUAGE);
//
//					// Creating new RSS Feed
//					rssFeed = new RSSFeed(title, description, link, rss_url,
//							language);
//				} catch (Exception e) {
//					// Check log for errors
//					e.printStackTrace();
//				}
//
//			} else {
//				// failed to fetch rss xml
//			}
//		} else {
//			// no RSS url found
//		}
//		return rssFeed;
//	}

    /**
     * Getting RSS feed items <item>
     *
     * @param - rss link url of the website
     * @return - List of RSSItem class objects
     */
    public List<RSSItem> getRSSFeedItems(String rss_url) {

        // get RSS XML from rss url


        // check if RSS XML fetched or not


        // successfully fetched rss xml
        // parse the xml
        try {

            org.jsoup.nodes.Document doc = Jsoup.connect(rss_url).get();
            RSSItemManager.getInstance(MainActivity.currentSource).fetchRSS(doc);

        } catch (Exception e) {
            // Check log for errors

            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            Log.e("EXCEPTION", stringWriter.toString());

        }


        // return item list

//        rssItemsManager.saveItems(MainActivity.activity.getApplicationContext());

        return RSSItemManager.getInstance(MainActivity.currentSource).getItemByCurrentCategory();
    }

    /**
     * Getting RSS feed link from HTML source code
     *
     * @returns url of rss link of website
     * */


    /**
     * Method to get xml content from url HTTP Get request
     * */


    /**
     * Getting XML DOM element
     */
    public org.jsoup.nodes.Document getDomElement(String xml) {
        org.jsoup.nodes.Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = Jsoup.parse(xml);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    /**
     * Getting node value
     *
     * @param elem element
     */
    public final String getElementValue(Node elem) {
        return elem.toString();
//		return "";
    }

    /**
     * Getting node value
     */
    public String getValue(Element item, String str) {
        Elements temp = item.select(str);
        return temp.text();
//		NodeList n = item.getElementsByTagName(str);
//		return this.getElementValue(n.item(0));
    }

}

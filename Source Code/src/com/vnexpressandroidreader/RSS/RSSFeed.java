/**
 * This class file used to create an object for rss feed 
 * which handles website basic information 
 * like title, description, link, rss link, 
 * language and an array of rss items.
 */
package com.vnexpressandroidreader.RSS;

import java.util.List;

public class RSSFeed {
	
	private String _title;
    private String _description;
    private String _link;
    private String _rss_link;
    private String _language;
    List<RSSItem> _items;

 // constructor
    public RSSFeed(String title, String description, String link,
            String rss_link, String language) {
        this.set_title(title);
        this.set_description(description);
        this.set_link(link);
        this.set_rss_link(rss_link);
        this.set_language(language);
    }

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_descriptison() {
		return _description;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	public String get_link() {
		return _link;
	}

	public void set_link(String _link) {
		this._link = _link;
	}

	public String get_rss_link() {
		return _rss_link;
	}

	public void set_rss_link(String _rss_link) {
		this._rss_link = _rss_link;
	}

	public String get_language() {
		return _language;
	}

	public void set_language(String _language) {
		this._language = _language;
	}

}

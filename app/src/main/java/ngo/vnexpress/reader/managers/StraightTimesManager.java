package ngo.vnexpress.reader.managers;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.items.StraightTimesItem;

public class StraightTimesManager extends RSSItemManager {
    private static StraightTimesManager instance;

    private StraightTimesManager(Class<? extends StraightTimesItem> type) {
        super(type);
    }

    public synchronized static StraightTimesManager getInstance() {
        if (instance == null) {
            instance = new StraightTimesManager(StraightTimesItem.class);
        }
        return instance;
    }

    @Override
    protected Map<String, String> fetchCategories() {
        HashMap<String, String> categories = new HashMap<>();
        categories.put("Singapore", "https://www.straitstimes.com/news/singapore/rss.xml");
        categories.put("Asia", "https://www.straitstimes.com/news/asia/rss.xml");
        categories.put("World", "https://www.straitstimes.com/news/world/rss.xml");
        categories.put("Business", "https://www.straitstimes.com/news/business/rss.xml");
        categories.put("Sport", "https://www.straitstimes.com/news/sport/rss.xml");

        return categories;

    }

    @Override
    protected String setSource() {
        return "The Straights Times";
    }

    @NonNull
    @Override
    protected String setHeaderBgColor() {
        return "#124284";
    }

    @NonNull
    @Override
    protected String setToolbarTextColor() {
        return "#FFFFFF";
    }

    @Override
    protected RSSItem createItem() {
        return new StraightTimesItem();
    }
}

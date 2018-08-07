package ngo.vnexpress.reader.managers;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ngo.vnexpress.reader.items.ChannelNewsAsiaItem;
import ngo.vnexpress.reader.items.RSSItem;

public class ChannelNewsAsiaManager extends RSSItemManager {
    private static ChannelNewsAsiaManager instance;

    private ChannelNewsAsiaManager(Class<? extends RSSItem> type) {
        super(type);
    }

    public synchronized static ChannelNewsAsiaManager getInstance() {
        if (instance == null) {
            instance = new ChannelNewsAsiaManager(ChannelNewsAsiaItem.class);
        }
        return instance;
    }

    @Override
    protected Map<String, String> fetchCategories() {
        HashMap<String, String> categories = new HashMap<>();
        categories.put("Latest News", "https://www.channelnewsasia.com/rssfeeds/8395986");
        categories.put("Asia Pacific", "https://www.channelnewsasia.com/rssfeeds/8395744");
        categories.put("Business", "https://www.channelnewsasia.com/rssfeeds/8395954");
        categories.put("Singapore", "https://www.channelnewsasia.com/rssfeeds/8396082");
        categories.put("Sport", "https://www.channelnewsasia.com/rssfeeds/8395838");
        categories.put("World", "https://www.channelnewsasia.com/rssfeeds/8395884");

        return categories;
    }

    @Override
    protected String setSource() {
        return "Channel News Asia";
    }

    @NonNull
    @Override
    protected String setHeaderBgColor() {
        return "#671E20";
    }

    @NonNull
    @Override
    protected String setToolbarTextColor() {
        return "#ffffff";
    }

    @Override
    protected RSSItem createItem() {
        return new ChannelNewsAsiaItem();
    }
}

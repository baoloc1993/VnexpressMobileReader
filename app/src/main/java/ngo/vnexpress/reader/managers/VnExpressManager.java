package ngo.vnexpress.reader.managers;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.items.VnExpressItem;

public class VnExpressManager extends RSSItemManager {
    private static VnExpressManager instance;

    private VnExpressManager(Class<? extends RSSItem> type) {
        super(type);
    }

    public synchronized static VnExpressManager getInstance() {
        if (instance == null) {
            instance = new VnExpressManager(VnExpressItem.class);
        }
        return instance;
    }

    @Override
    protected Map<String, String> fetchCategories() {
        HashMap<String, String> categories = new HashMap<>();
        categories.put("Homepage", "https://vnexpress.net/rss/tin-moi-nhat.rss");
        categories.put("Headlines", "https://vnexpress.net/rss/thoi-su.rss");
        categories.put("Health", "https://vnexpress.net/rss/suc-khoe.rss");
        categories.put("World", "https://vnexpress.net/rss/the-gioi.rss");
        categories.put("Business", "https://vnexpress.net/rss/kinh-doanh.rss");
        categories.put("Sport", "https://vnexpress.net/rss/the-thao.rss");

        return categories;
    }

    @Override
    protected String setSource() {
        return "VnExpress";
    }

    @NonNull
    @Override
    protected String setHeaderBgColor() {
        return "#932048";
    }

    @NonNull
    @Override
    protected String setToolbarTextColor() {
        return "#ffffff";
    }

    @Override
    protected RSSItem createItem() {
        return new VnExpressItem();
    }
}

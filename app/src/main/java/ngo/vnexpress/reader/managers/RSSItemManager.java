package ngo.vnexpress.reader.managers;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ngo.vnexpress.reader.items.ChannelNewsAsiaItem;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.items.StraightTimesItem;
import ngo.vnexpress.reader.items.VnExpressItem;

public abstract class RSSItemManager extends ItemManager<RSSItem> {


    public static ArrayList<Class> getRSSItemTypes(){
        ArrayList<Class> temp = new ArrayList<>();
        temp.add(StraightTimesItem.class);
        temp.add(VnExpressItem.class);
        temp.add(ChannelNewsAsiaItem.class);
        return temp;
    }
    private static RSSItemManager instance;
    private String currentCategory;
    private Map<String, String> categories;

    public String getSource() {
        return source;
    }

    private String headerBgColor;
    private String toolbarTextColor;
    private String source;
    protected abstract Map<String,String> fetchCategories();
    protected abstract String setSource();
    @NonNull
    public static synchronized RSSItemManager getInstance(Class<? extends RSSItem> type) {
        if(type == StraightTimesItem.class){
            return StraightTimesManager.getInstance();
        }
        if(type == VnExpressItem.class){
            return VnExpressManager.getInstance();
        }
        if(type == ChannelNewsAsiaItem.class){
            return ChannelNewsAsiaManager.getInstance();
        }
        return null;
    }
    RSSItemManager(Class<? extends RSSItem> type) {
        super(type);
        this.categories = fetchCategories();
        this.currentCategory = Stream.of(this.getCategories()).findFirst().orElse("");
        this.source = this.setSource();
        this.headerBgColor = this.setHeaderBgColor();
        this.toolbarTextColor = this.setToolbarTextColor();
    }
    public void fetchRSS(Document document){
        Elements items = document.getElementsByTag("item");
        for(int i = 0 ;i<items.size();i++){
            RSSItem rssItem = createItem();
            rssItem.fetchRSS(items.get(i));
            rssItem.setCategory(currentCategory);
            rssItem.setSource(this.source);
            this.add(rssItem);

        }
//        saveItems();
    }

    protected abstract RSSItem createItem();

    public List<RSSItem> getItemByCategory(String category) {
//        loadItems();
        return Stream.of(items).filter(item -> item.getCategory().equals(category)).sorted((o1, o2) -> {
            SimpleDateFormat date1 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            SimpleDateFormat date2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            try {
                Date date11 = date1.parse(o1.getPubDate());
                Date date21 = date2.parse(o2.getPubDate());
                return -date11.compareTo(date21);


            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }).collect(com.annimon.stream.Collectors.toList());
    }

    public List<RSSItem> getItemByCurrentCategory() {
        return getItemByCategory(currentCategory);
    }
    public Set<String> getCategories(){

        return this.categories.keySet();
    }
    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public String getCurrentUrl() {
        return this.categories.get(this.currentCategory);
    }

    public String getHeaderBgColor() {
        return headerBgColor;
    }
    @NonNull
    protected abstract String  setHeaderBgColor();

    public String getToolbarTextColor() {
        return toolbarTextColor;
    }
    @NonNull
    protected abstract String setToolbarTextColor();
}

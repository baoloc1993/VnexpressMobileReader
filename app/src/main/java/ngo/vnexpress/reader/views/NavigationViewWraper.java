package ngo.vnexpress.reader.views;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.managers.RSSItemManager;

public class NavigationViewWraper {
    private View header;
    public NavigationViewWraper(NavigationView navigationView) {
        this.navigationView = navigationView;
    }
    public void iniNavigationView(){
        header = navigationView.getHeaderView(0);
        header.findViewById(R.id.navigation_header_container);
        Menu menu = navigationView.getMenu();
        ArrayList<Class> newsSources = RSSItemManager.getRSSItemTypes();
        newsSources.stream().forEach((Class item) ->{
            int index = newsSources.indexOf(item);
            menu.add(0,index,index,RSSItemManager.getInstance(item).getSource());
        });
        navigationView.setNavigationItemSelectedListener(item -> {

            MainActivity.activity.changeSource(newsSources.get(item.getItemId()));
            return true;
        });
    }
    public void updateNavigationView(){

        TextView title = header.findViewById(R.id.nav_header_title);
        RSSItemManager rssItemManager  = RSSItemManager.getInstance(MainActivity.currentSource);
        title.setText(rssItemManager.getSource());
        header.findViewById(R.id.nav_header_img).setBackgroundColor(Color.parseColor(rssItemManager.getHeaderBgColor()));
    }
    NavigationView navigationView;
}

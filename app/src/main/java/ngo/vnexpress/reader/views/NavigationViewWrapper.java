package ngo.vnexpress.reader.views;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.ArrayList;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.managers.RSSItemManager;

public class NavigationViewWrapper {
    private View header;
    private NavigationView navigationView;

    public NavigationViewWrapper(NavigationView navigationView) {
        this.navigationView = navigationView;
    }

    public void iniNavigationView(MainActivity activity) {
        header = navigationView.getHeaderView(0);
        header.findViewById(R.id.navigation_header_container);
        Menu menu = navigationView.getMenu();
        final ArrayList<Class> newsSources = RSSItemManager.getRSSItemTypes();
        Stream.of(newsSources).forEach((Class newsSource) -> {
            int index = newsSources.indexOf(newsSource);
            menu.add(0, index, index, RSSItemManager.getInstance(newsSource).getSource());
        });
        navigationView.setNavigationItemSelectedListener((MenuItem item) -> {

            activity.changeSource(newsSources.get(item.getItemId()));
            return true;
        });
    }

    public void updateNavigationView() {

        TextView title = header.findViewById(R.id.nav_header_title);
        RSSItemManager rssItemManager = RSSItemManager.getInstance(MainActivity.currentSource);
        title.setText(rssItemManager.getSource());
        header.findViewById(R.id.nav_header_img).setBackgroundColor(Color.parseColor(rssItemManager.getHeaderBgColor()));
    }


}

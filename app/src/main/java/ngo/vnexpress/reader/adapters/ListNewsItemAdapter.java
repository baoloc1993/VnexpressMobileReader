package ngo.vnexpress.reader.adapters;

/**
 * Adapter of List Fragment
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.views.WholeNewsDialog;

public class ListNewsItemAdapter extends RecyclerView.Adapter<ListNewsItemAdapter.ViewHolder> {


    // declaring our ArrayList of items
    private List<RSSItem> objects;

    /**
     * @param objects
     */
    public ListNewsItemAdapter(List<RSSItem> objects) {
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_single_news_list_layout, parent, false);




        /*
         * Recall that the variable position is sent in as an argument to this
         * method. The variable simply refers to the position of the current
         * object in the list. (The ArrayAdapter iterates through the list we
         * sent it)
         *
         * Therefore, i refers to the current Item object.
         */


        // This is how you obtain a reference to the TextViews.
        // These TextViews are created in the XML files we defined.

        TextView title = v.findViewById(R.id.title);
        ImageView icon = v.findViewById(R.id.icon);
        TextView content = v.findViewById(R.id.content);
        TextView timestamp = v.findViewById(R.id.timestamp);

        // check to see if each individual textview is null.
        // if not, assign some text!
        // Config parameter of each textview and imageview(Resize)
        // For TITLE, ICON, CONTENT, TIMESTAMP
        return new ViewHolder(v,title, icon, content, timestamp);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RSSItem item = objects.get(position);

        TextView title = holder.title;
        ImageView icon = holder.icon;
        TextView content = holder.content;
        TextView timestamp = holder.timestamp;

        holder.setNews(item);
        title.setText(item.getTitle());

        int size = (int) (MainActivity.getStandardSize() * 0.3);
        icon.getLayoutParams().width = size;
        icon.getLayoutParams().height = size;

        // Loader image - will be shown before loading image
        int loader = R.drawable.image_not_found;
        if (!item.getImgUrl().equals("")) {
            Picasso.get().load(item.getImgUrl()).error(loader).placeholder(loader).fit().into(icon);
        }


        content.setText(Html.fromHtml(item.getDescription()));
        timestamp.setText(item.getPubDate());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        TextView content;
        TextView timestamp;
        View container;
        public RSSItem getNews() {
            return news;
        }

        public void setNews(RSSItem news) {
            this.news = news;
        }

        RSSItem news;

        ViewHolder(View container,TextView title, ImageView icon, TextView content, TextView timestamp) {
            super(container);
            container.setOnClickListener(view -> {
                WholeNewsDialog dialog = new WholeNewsDialog(news);
                android.app.FragmentTransaction ft = MainActivity.activity.getFragmentManager().beginTransaction();
                dialog.show(ft, WholeNewsDialog.TAG);
            });
            this.title = title;
            this.icon = icon;
            this.content = content;
            this.timestamp = timestamp;
            this.container = container;
        }
    }
}

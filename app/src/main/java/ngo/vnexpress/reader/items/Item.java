package ngo.vnexpress.reader.items;

import java.io.Serializable;
import java.util.UUID;

public abstract class Item implements Serializable {
    protected String title;

    public String getId() {
        return id;
    }

    protected String id;

    public Item(String title) {
        this.title = title;
        id= UUID.randomUUID().toString();
    }
    public Item(String title,String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Item && this.getId().equals(((Item) obj).getId());
    }
}

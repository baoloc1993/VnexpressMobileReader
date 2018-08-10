package ngo.vnexpress.reader.managers;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.items.Item;

public class ItemManager<T extends Item> implements Collection<T> {
    protected ArrayList<T> items;
    private String fileName;
    private File fileDir;


    ItemManager(Class<? extends T> type) {
        this.fileDir = MainActivity.fileDir;
        items = new ArrayList<>();
        fileName = type.getName() + ".txt";
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return items.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return items.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return items.toArray(a);
    }

    @Override
    public boolean add(T t) {
        if (!this.contains(t)) {
            items.add(0, t);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean remove(Object o) {
        return items.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return items.contains(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return items.removeAll(c);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return items.retainAll(c);
    }

    @Override
    public void clear() {
        items.clear();
    }

    public ArrayList<T> loadItems() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(fileDir, fileName));
            ObjectInputStream out = new ObjectInputStream(fileInputStream);
            items = (ArrayList<T>) out.readObject();
            for (T item : items) {
                item.onLoaded();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            items = new ArrayList<>();
        }
        return items;
    }

    public void updateItem(T item) {
        for (int i = 0; i < items.size(); i++) {
            if (item.getId().equals(items.get(i).getId())) {
                items.set(i, item);
                saveItems();
                break;
            }
        }
    }

    public void saveItems() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileDir, fileName));
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            out.writeObject(items);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T getItemById(String id) throws NoSuchElementException {
        return Stream.of(items).filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }
}

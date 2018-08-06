package ngo.vnexpress.reader.managers;

import android.content.Context;
import android.support.annotation.NonNull;

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
import java.util.UUID;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.items.Item;

public class ItemManager<T extends Item> implements Collection<T> {
    protected ArrayList<T> items;
    private String fileName;


    ItemManager(Class<? extends T> type) {
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
        if(!this.contains(t)){
            items.add(0,t);
            return true;
        }else{
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

    public void saveItems(Context context) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getExternalFilesDir(null), fileName));
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(items);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<T> loadItems(Context context) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(context.getExternalFilesDir(null), fileName));
            ObjectInputStream out = new ObjectInputStream(fileInputStream);
            items = (ArrayList<T>) out.readObject();

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
                saveItems(MainActivity.activity);
                break;
            }
        }
    }
    public T getItemById(UUID id) throws NoSuchElementException{
        return items.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }
}

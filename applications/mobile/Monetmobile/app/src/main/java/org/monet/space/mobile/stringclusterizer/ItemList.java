package org.monet.space.mobile.stringclusterizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ItemList<T> implements Iterable<Item<T>> {
    private List<Item<T>> items = new ArrayList();

    public ItemList() {
    }

    public Item<T> get(int index) {
        return (Item)this.items.get(index);
    }

    public Iterator<Item<T>> iterator() {
        return this.items.iterator();
    }

    public void sort(Comparator<T> tComparator) {
        Iterator var2 = this.items.iterator();

        while(var2.hasNext()) {
            Item<T> item = (Item)var2.next();
            item.items().sort(tComparator);
        }

        Collections.sort(this.items, this.buildComparator(tComparator));
    }

    public List<Item<T>> edit() {
        return this.items;
    }

    public Item parent() {
        return this.items.isEmpty() ? NullItem.instance() : ((Item)this.items.get(0)).parent();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    private Comparator<Item<T>> buildComparator(final Comparator<T> tComparator) {
        return new Comparator<Item<T>>() {
            public int compare(Item<T> o1, Item<T> o2) {
                T i1 = this.getElement(o1);
                T i2 = this.getElement(o2);
                return tComparator.compare(i1, i2);
            }

            public T getElement(Item<T> item) {
                Item<T> toReturn;
                for(toReturn = item; toReturn.isGroup(); toReturn = toReturn.items().get(0)) {
                }

                return toReturn.get();
            }
        };
    }

    public void remove(Item<T> item) {
        this.items.remove(item);
    }

    public void add(Item<T> item) {
        this.items.add(item);
    }

    public int size() {
        return this.items.size();
    }

    public String toString() {
        return this.toString(this.toArray(), "");
    }

    String toString(Item[] items, String prefix) {
        String result = "";
        Item[] var4 = items;
        int var5 = items.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Item item = var4[var6];
            result = result + prefix + item.id() + "\n" + this.toString(item.items().toArray(), prefix + "\t");
        }

        return result;
    }

    private Item[] toArray() {
        return (Item[])this.items.toArray(new Item[this.items.size()]);
    }
}

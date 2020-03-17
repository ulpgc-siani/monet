package org.monet.space.mobile.stringclusterizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class Cluster<T> implements Item {
    private String id;
    private Item parent;
    private final ItemList<T> items;
    private StringExtractor<T> extractor;

    protected Cluster(String id, List<T> items, StringExtractor<T> extractor) {
        this.id = id;
        this.parent = NullItem.instance();
        this.items = this.buildItems(items);
        this.extractor = extractor;
    }

    public String id() {
        return this.id;
    }

    public Cluster get() {
        return this;
    }

    public Item<T> parent() {
        return this.parent;
    }

    public ItemList<T> items() {
        return this.items;
    }

    public ItemList group() {
        return this.parent().items();
    }

    List<Item<T>> elements() {
        List<Item<T>> elements = new ArrayList();
        Iterator var2 = this.items.iterator();

        while(var2.hasNext()) {
            Item item = (Item)var2.next();
            if (!item.isGroup()) {
                elements.add(item);
            } else {
                elements.addAll(((Cluster)item.get()).elements());
            }
        }

        return Collections.unmodifiableList(elements);
    }

    List<Cluster<T>> clusters() {
        List<Cluster<T>> clusters = new ArrayList();
        Iterator var2 = this.items.iterator();

        while(var2.hasNext()) {
            Item item = (Item)var2.next();
            if (item.isGroup()) {
                clusters.add((Cluster)item);
            }
        }

        return Collections.unmodifiableList(clusters);
    }

    public boolean isGroup() {
        return true;
    }

    void id(String id) {
        this.id = id;
    }

    List<Cluster<T>> allClusters() {
        List<Cluster<T>> clusters = new ArrayList();
        clusters.add(this);
        Iterator var2 = this.clusters().iterator();

        while(var2.hasNext()) {
            Cluster<T> cluster = (Cluster)var2.next();
            clusters.addAll(cluster.allClusters());
        }

        return clusters;
    }

    void add(Cluster<T> cluster) {
        cluster.parent = this;
        Iterator var2 = cluster.elements().iterator();

        while(var2.hasNext()) {
            Item<T> item = (Item)var2.next();
            this.items.remove(item);
        }

        this.items.add(cluster);
    }

    private ItemList<T> buildItems(List<T> items) {
        ItemList<T> itemList = new ItemList();
        Iterator<T> var3 = items.iterator();

        while(var3.hasNext()) {
            T item = var3.next();
            itemList.add(this.buildItem(item));
        }

        return itemList;
    }

    private Item<T> buildItem(final T item) {
        return new Item<T>() {
            public String id() {
                return Cluster.this.extractor.extract(item);
            }

            public T get() {
                return item;
            }

            public Item<T> parent() {
                return Cluster.this;
            }

            public ItemList<T> items() {
                return new ItemList();
            }

            public ItemList<T> group() {
                return this.parent().items();
            }

            public boolean isGroup() {
                return false;
            }

            public boolean equals(Object obj) {
                return obj instanceof Item && item.equals(((Item)obj).get());
            }

            public String toString() {
                return this.id();
            }
        };
    }
}

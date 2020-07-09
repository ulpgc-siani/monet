package org.monet.space.mobile.stringclusterizer;

public interface Item<T> {
    String id();

    T get();

    Item<T> parent();

    ItemList<T> items();

    ItemList<T> group();

    boolean isGroup();
}

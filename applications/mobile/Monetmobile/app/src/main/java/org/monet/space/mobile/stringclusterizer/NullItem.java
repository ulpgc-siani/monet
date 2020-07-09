package org.monet.space.mobile.stringclusterizer;

public class NullItem implements Item {
    private static NullItem instance = new NullItem();

    public static Item instance() {
        return instance;
    }

    private NullItem() {
    }

    public String id() {
        return "null";
    }

    public Object get() {
        return this;
    }

    public Item parent() {
        return instance();
    }

    public ItemList items() {
        return new ItemList();
    }

    public ItemList group() {
        return new ItemList();
    }

    public boolean isGroup() {
        return false;
    }
}

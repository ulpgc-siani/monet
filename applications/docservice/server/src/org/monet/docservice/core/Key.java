package org.monet.docservice.core;

public class Key {

    private String space;
    private String id;

    public Key(String space, String id) {
        this.space = space;
        this.id = id;
    }

    public static Key from(String space, String id) {
        return new Key(space, id);
    }

    public static Key from(String id) {
        return id.contains("#") ? new Key(id.split("#")[0], id.split("#")[1]) : new Key(null, id);
    }

    public static boolean containsSpace(String key) {
        return key.contains("#");
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return (space != null ? space + '#' : "") + id;
    }
}

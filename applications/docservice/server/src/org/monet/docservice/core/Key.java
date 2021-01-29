package org.monet.docservice.core;

public class Key {

    private String space;
    private String id;

    public Key(String space, String id) {
        this.space = space;
        this.id = id;
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
}

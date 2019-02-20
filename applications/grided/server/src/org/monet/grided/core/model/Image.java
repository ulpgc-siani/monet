package org.monet.grided.core.model;

import java.io.File;

public class Image {

    private String name;
    private File file;

    public Image(String name, File file) {
        this.name = name;
        this.file = file;
    }
    
    public String getName() {
        return this.name;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }
}


package org.monet.grided.core.persistence.filesystem.impl;

import java.io.File;

class DataFile {

    private File file;

    public DataFile(File file) {
        this.file = file;
    }

    public DataFile(String filename) {
        this(new File(filename));
    }
    
    public String getName() {
        return this.file.getName();        
    }

    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }
}


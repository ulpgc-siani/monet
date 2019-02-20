package org.monet.grided.core.persistence.filesystem.readers;

import java.io.File;

import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.serializers.xml.ModelData;
import org.monet.grided.core.serializers.xml.model.impl.XMLModelDataSerializer;
import org.monet.grided.library.LibraryZip;

public class ModelDataReader implements DataReader<ModelData> {

    private XMLModelDataSerializer serializer;
    private String tempPath;
    private Filesystem filesystem;

    public ModelDataReader(String tempPath, Filesystem filesystem, XMLModelDataSerializer serializer) {
        this.tempPath = tempPath;
        this.filesystem = filesystem;                
        this.serializer = serializer;
    }
    
    public ModelData read(File zipFile) {         
        LibraryZip.decompress(zipFile, this.tempPath);
        String filename = this.tempPath + Filesystem.FILE_SEPARATOR + "model.xml";
        String xml = this.filesystem.readFile(filename);
        return this.serializer.unSerialize(xml);        
    }
}


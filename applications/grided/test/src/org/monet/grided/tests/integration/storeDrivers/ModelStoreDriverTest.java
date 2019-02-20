package org.monet.grided.tests.integration.storeDrivers;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.lib.FilesystemImpl;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.persistence.filesystem.impl.DataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.ModelStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.StoreConfig;
import org.monet.grided.core.serializers.xml.ModelData;
import org.monet.grided.core.serializers.xml.model.impl.XMLModelDataSerializer;

public class ModelStoreDriverTest {

 private static final String FOLDER = "resources/models";
    
    private Filesystem fs;
    private ModelStoreConfig modelConfig;    
    private Map<String, String> paths;
    private Logger logger;
    
    @Before
    public void setup() throws MalformedURLException {                
        this.logger = Logger.getLogger("test");
        this.fs = new FilesystemImpl();
        this.paths = new HashMap<String, String>();
        paths.put(ModelStoreConfig.STORE_PATH, "c:/temp/store-test");
        paths.put(ModelStoreConfig.TEMP_STORE_PATH, "c:/temps");
        paths.put(ModelStoreConfig.VERSIONS_PATH, "versions");

        modelConfig = new ModelStoreConfig(paths);        
        modelConfig.addPath(StoreConfig.ROOT, "models");
    }
    
    @After
    public void tearDown() throws MalformedURLException {                     
        this.fs = new FilesystemImpl();                   
        this.createFixture();
    }
               
    @Test
    public void testSaveModelFolder() throws MalformedURLException {    
      XMLModelDataSerializer serializer = new XMLModelDataSerializer();
      DataStoreDriverImpl<Model, ModelData> driver = new DataStoreDriverImpl<Model, ModelData>(modelConfig, this.fs, serializer, this.logger);           

      Model model = new Model(1, "coordinacion");
            
      ModelData modelData = new ModelData("coordinacion", "label coordinacion");
      model.setData(modelData);      
                  
      String dataPath = "M1";
      File file = new File(FOLDER);
      driver.save(dataPath, model, file); 
      
      File newFile = new File("c:/temp/store-test/models/M1/M1.xml");
            
      assertTrue(newFile.exists());                       
    }
    
    @Test
    public void testLoadModel() {
        XMLModelDataSerializer serializer = new XMLModelDataSerializer();
        DataStoreDriverImpl<Model, ModelData> driver = new DataStoreDriverImpl<Model, ModelData>(modelConfig, this.fs, serializer, this.logger);
        File file = new File(FOLDER);      
               
        ModelData data = driver.load(file);
        assertTrue(data != null);                 
    }    
    
    @Test
    public void testDeleteModelFolder() {
        XMLModelDataSerializer serializer = new XMLModelDataSerializer();
        DataStoreDriverImpl<Model, ModelData> driver = new DataStoreDriverImpl<Model, ModelData>(modelConfig, this.fs, serializer, this.logger);
                         
        String dataPath = "M1";
        driver.delete(dataPath);
        
        File newFile = new File("c:/temp/store-test/models/M1/M1.xml");
        File images  = new File("c:/temp/store-test/models/M1/M1/images");
        
        assertFalse(newFile.exists());      
        assertFalse(images.exists());
    }
    
    private void createFixture() throws MalformedURLException {
        XMLModelDataSerializer serializer = new XMLModelDataSerializer();
        DataStoreDriverImpl<Model, ModelData> driver = new DataStoreDriverImpl<Model, ModelData>(modelConfig, this.fs, serializer, this.logger);           

        Model model = new Model(1, "coordinacion");
        
        ModelData modelData = new ModelData("coordinacion", "label coordinacion");
        model.setData(modelData);      
                    
        String dataPath = "M1";
        File file = new File(FOLDER);
        driver.save(dataPath, model, file);         
    }

}


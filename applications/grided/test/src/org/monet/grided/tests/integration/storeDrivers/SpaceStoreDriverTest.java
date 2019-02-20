package org.monet.grided.tests.integration.storeDrivers;


import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.lib.FilesystemImpl;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.persistence.filesystem.impl.DataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.ModelStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.SpaceStoreConfig;
import org.monet.grided.core.serializers.xml.Logo;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.space.SpaceData;
import org.monet.grided.core.serializers.xml.space.SpaceData.FederationStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationServiceStatement;
import org.monet.grided.core.serializers.xml.space.impl.XMLSpaceDataSerializer;

public class SpaceStoreDriverTest {
    
    private static final String FOLDER = "resources/spaces";
    
    private Filesystem fs;
    private SpaceStoreConfig spaceConfig;    
    private Logger logger;
            
    @Before
    public void setup() {  
        this.logger = Logger.getLogger("test");
        this.fs = new FilesystemImpl();           
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(ModelStoreConfig.STORE_PATH, "c:/temp/store-test");
        paths.put(ModelStoreConfig.TEMP_STORE_PATH, "c:/temps");
        paths.put(ModelStoreConfig.VERSIONS_PATH, "versions");

        spaceConfig = new SpaceStoreConfig(paths);
    }    
    
    @Test
    public void testSaveSpaceFolder() {    
      XMLSpaceDataSerializer serializer = new XMLSpaceDataSerializer();
      DataStoreDriverImpl<Space, SpaceData> driver = new DataStoreDriverImpl<Space, SpaceData>(spaceConfig, this.fs, serializer, this.logger);
      File file = new File(FOLDER);      
      
      Server server = new Server(0, "aldebarán", "127.0.0.1");      
      Federation federation = new Federation(server, 1);
            
      Space space = new Space(federation, 1);
      space.setData(this.createData());  
                  
      String dataPath = "S001/F1/B1";
      driver.save(dataPath, space, file); 
      
      File newFile = new File("c:/temp/store-test/S001/F1/B1/B1.xml");
      File images  = new File("c:/temp/store-test/S001/F1/B1/images");
      
      assertTrue(newFile.exists());      
      assertTrue(images.exists());            
    }
    
    @Test
    public void testLoadSpace() {
        XMLSpaceDataSerializer serializer = new XMLSpaceDataSerializer();
        DataStoreDriverImpl<Space, SpaceData> driver = new DataStoreDriverImpl<Space, SpaceData>(spaceConfig, this.fs, serializer, this.logger);
        File file = new File(FOLDER);      
               
        SpaceData data = driver.load(file);
        assertTrue(data != null);                 
    }
        
    @Test
    public void testDeleteSpaceFolder() {
        XMLSpaceDataSerializer serializer = new XMLSpaceDataSerializer();
        DataStoreDriverImpl<Space, SpaceData> driver = new DataStoreDriverImpl<Space, SpaceData>(spaceConfig, this.fs, serializer, this.logger);
                         
        String dataPath = "S001/F1";
        driver.delete(dataPath);
        
        File newFile = new File("c:/temp/store-test/S001/F1/B1/F1.xml");
        File images  = new File("c:/temp/store-test/S001/F1/B1/images");
        
        assertFalse(newFile.exists());      
        assertFalse(images.exists());
    }
    
    private SpaceData createData() {
        SpaceData data = new SpaceData();
        
        Organization organization = new Organization("space1", "http://localhost", new Logo("logo.png", "images"));
        organization.setLabel("Fuentes ornamentales");
        data.setOrganization(organization);
        
        FederationStatement statement = new FederationStatement("localhost", "federation1");
        data.setFederationStatement(statement);        
        data.addPublicationServiceStatement(new PublicationServiceStatement("servicio.coordinacion"));
        
        return data;               
    }
}


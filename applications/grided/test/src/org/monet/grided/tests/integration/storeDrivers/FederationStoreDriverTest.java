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
import org.monet.grided.core.persistence.filesystem.impl.DataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.FederationStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.StoreConfig;
import org.monet.grided.core.serializers.xml.Logo;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.federation.DatabaseConnection;
import org.monet.grided.core.serializers.xml.federation.DatabaseConnection.DatabaseConnectionConfiguration;
import org.monet.grided.core.serializers.xml.federation.FederationData;
import org.monet.grided.core.serializers.xml.federation.FederationData.Authentication;
import org.monet.grided.core.serializers.xml.federation.impl.XMLFederationDataSerializer;

public class FederationStoreDriverTest {
    
    private static final String FEDERATION_MOCK_FOLDER = "resources/federations/mock";        
    private static final String FEDERATION_DATABASE_FOLDER = "resources/federations/database";        
    private static final String FEDERATION_LDAP_FOLDER = "resources/federations/ldap";
    
    private Filesystem fs;
    private FederationStoreConfig federationConfig;
    private Logger logger;    
            
    @Before
    public void setup() {                   
        this.logger = Logger.getLogger("test");
        this.fs = new FilesystemImpl();           
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(StoreConfig.STORE_PATH, "c:/temp/store-test");
        paths.put(StoreConfig.TEMP_STORE_PATH, "c:/temps");
        
        federationConfig = new FederationStoreConfig(paths);
        federationConfig.addPath(StoreConfig.ROOT, "servers");
    }    
    
    @Test
    public void testSaveFederationFolder() {    
      XMLFederationDataSerializer serializer = new XMLFederationDataSerializer();
      DataStoreDriverImpl<Federation, FederationData> driver = new DataStoreDriverImpl<Federation, FederationData>(federationConfig, this.fs, serializer, this.logger);
      File file = new File(FEDERATION_MOCK_FOLDER);      
      
      Server server = new Server(0, "aldebarán", "127.0.0.1");
      Federation federation = new Federation(server, 1);
      federation.setData(this.createData());  
      
      String dataPath = "S001/F1";
      driver.save(dataPath, federation, file); 
      
      File newFile = new File("c:/temp/store-test/servers/S001/F1/F1.xml");
      File images  = new File("c:/temp/store-test/servers/S001/F1/images");
      
      assertTrue(newFile.exists());      
      assertTrue(images.exists());            
    }
    
    @Test
    public void testLoadFederationWithMockConnection() {
        XMLFederationDataSerializer serializer = new XMLFederationDataSerializer();
        DataStoreDriverImpl<Federation, FederationData> driver = new DataStoreDriverImpl<Federation, FederationData>(federationConfig, this.fs, serializer, this.logger);
        File file = new File(FEDERATION_MOCK_FOLDER);      
               
        FederationData data = driver.load(file);
        assertTrue(data != null);                 
    }
    
    @Test
    public void testLoadFederationWithDatabaseConnection() {
        XMLFederationDataSerializer serializer = new XMLFederationDataSerializer();
        DataStoreDriverImpl<Federation, FederationData> driver = new DataStoreDriverImpl<Federation, FederationData>(federationConfig, this.fs, serializer, this.logger);
        File file = new File(FEDERATION_DATABASE_FOLDER);      
               
        FederationData data = driver.load(file);
        assertTrue(data != null);                 
    }
    
    @Test
    public void testLoadFederationWithLDAPConnection() {
        XMLFederationDataSerializer serializer = new XMLFederationDataSerializer();
        DataStoreDriverImpl<Federation, FederationData> driver = new DataStoreDriverImpl<Federation, FederationData>(federationConfig, this.fs, serializer, this.logger);
        File file = new File(FEDERATION_LDAP_FOLDER);      
               
        FederationData data = driver.load(file);
        assertTrue(data != null);                 
    }
    
    @Test
    public void testDeleteFederationFolder() {
        XMLFederationDataSerializer serializer = new XMLFederationDataSerializer();
        DataStoreDriverImpl<Federation, FederationData> driver = new DataStoreDriverImpl<Federation, FederationData>(federationConfig, this.fs, serializer, this.logger);
                         
        String dataPath = "S001/F1";
        driver.delete(dataPath);
        
        File newFile = new File("c:/temp/store-test/servers/S001/F1/F1.xml");
        File images  = new File("c:/temp/store-test/servers/S001/F1/images");
        
        assertFalse(newFile.exists());      
        assertFalse(images.exists());
    }
    
    private FederationData createData() {
        Organization organization = new Organization("federation1", "http://localhost", new Logo("logo", ""));
        organization.setLabel("Ayuntamiento de Las Palmas");        
        
        DatabaseConnection conn = new DatabaseConnection("http://localhost", "user1", "password");
        DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration("mysql");
        conn.setConfiguration(config);
                                        
        Authentication auth = new Authentication();
        auth.setCertificate(true);
                        
        FederationData data = new FederationData();
        data.setOrganization(organization);
        data.setConnection(conn);
        data.setAuthentication(auth);
                
        return data;               
    }
}
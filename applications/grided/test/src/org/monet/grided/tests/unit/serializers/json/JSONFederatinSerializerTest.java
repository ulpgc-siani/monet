package org.monet.grided.tests.unit.serializers.json;

import static org.junit.Assert.assertTrue;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.serializers.json.impl.JSONFederationSerializer;
import org.monet.grided.core.serializers.xml.Logo;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.federation.DatabaseConnection;
import org.monet.grided.core.serializers.xml.federation.DatabaseConnection.DatabaseConnectionConfiguration;
import org.monet.grided.core.serializers.xml.federation.FederationData;
import org.monet.grided.core.serializers.xml.federation.FederationData.Authentication;

public class JSONFederatinSerializerTest {

    public static FederationData dataWithDatabaseConnection;
    private JSONFederationSerializer serializer;
    private Configuration config = new Configuration() {
        @Override
        public String getImagesPath() {return "";}

        @Override
        public String getProperty(String key) {return "en";}

        @Override
        public String getLabel(String key) {return "";}        
    };
    
    @BeforeClass
    public static void setupClass() {
        dataWithDatabaseConnection = createData();
    }   
    
    @Before
    public void setup() {            
        this.serializer = new JSONFederationSerializer(config);
    }       
    
    @Test
    public void serializeFederationWithDatabaseConnection() {
        Server server = new Server(0, "aldebarán", "127.0.0.1");
        Federation federation = new Federation(server, 1);
        federation.setData(dataWithDatabaseConnection);
                
        JSONObject json = this.serializer.serialize(federation);
        
        assertTrue(json.getString("config") != null);
    }
    
    private static FederationData createData() {
        Organization organization = new Organization("Fedaration1", "http://localhost", new Logo("default.png", "images"));
        organization.setLabel("Ayuntamiento de Las Palmas");
                                
        DatabaseConnection conn = new DatabaseConnection("http://localhost", "user1", "password");
        DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration("mysql");                        
        conn.setConfiguration(config);
                                        
        Authentication auth = new Authentication();
        auth.setPassword(true);
                
        FederationData data = new FederationData();
        data.setOrganization(organization);
        data.setConnection(conn);        
        data.setAuthentication(auth);
                        
        return data;               
    }
}


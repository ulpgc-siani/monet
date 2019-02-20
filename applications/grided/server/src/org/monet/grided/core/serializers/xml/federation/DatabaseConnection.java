package org.monet.grided.core.serializers.xml.federation;

import org.monet.grided.core.serializers.xml.federation.FederationData.Connection;
import org.monet.grided.core.serializers.xml.federation.FederationData.ConnectionTypes;
import org.monet.grided.core.serializers.xml.federation.FederationData.DatabaseTypes;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="connection", strict=false)
public class DatabaseConnection implements Connection {
    private String type;
    @Attribute(name="url") private String url;
    @Attribute(name="user") private String user;
    @Attribute(name="password") private String password;
    @Element(name="setup") DatabaseConnectionConfiguration configuration;
                
    public DatabaseConnection( @Attribute(name="url") String url, @Attribute(name="user") String user, @Attribute(name="password") String password) {        
        this.type = ConnectionTypes.DATABASE;
        this.configuration = new DatabaseConnectionConfiguration(DatabaseTypes.MYSQL);
        this.url = url;
        this.user = user;
        this.password = password;       
    }

    public String getUrl() { return url; }
    public void setUrl(String url) {this.url = url; }

    public String getUser() { return user; }
    public  void setUser(String user) { this.user = user;} 

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }            


    public DatabaseConnectionConfiguration getConfiguration() {
        return this.configuration;
    }


    public void setConfiguration(DatabaseConnectionConfiguration configuration) {
        this.configuration = configuration;          
    }


    public static class DatabaseConnectionConfiguration {               
        @Element(name="database-type") private String databaseType;
                
        public DatabaseConnectionConfiguration(@Element(name="database-type") String databaseType) {
            this.databaseType = databaseType;
        }

        public String getDatabaseType() {
            return this.databaseType;
        }

        public void setDatabaseType(String databaseType) {
            this.databaseType = databaseType;
        }
    }
}

      
    

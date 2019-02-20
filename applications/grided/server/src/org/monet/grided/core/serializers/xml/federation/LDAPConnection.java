package org.monet.grided.core.serializers.xml.federation;

import java.util.ArrayList;
import java.util.List;

import org.monet.grided.core.serializers.xml.federation.FederationData.Connection;
import org.monet.grided.core.serializers.xml.federation.FederationData.ConnectionTypes;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="connection", strict=false)
public class LDAPConnection implements Connection {
    private String type;
    @Attribute(name="url") private String url;
    @Attribute(name="user") private String user;
    @Attribute(name="password") private String password;    
    @Element(name="setup") LDAPConnectionConfiguration configuration;
            
    public LDAPConnection(@Attribute(name="url") String url, @Attribute(name="user") String user, @Attribute(name="password") String password) {            
        this.type = ConnectionTypes.LDAP;
        this.url = url;
        this.user = user;
        this.password = password;
        this.configuration = new LDAPConnectionConfiguration("");
    }

    public String getUrl() { return url; }
    public void setUrl(String url) {this.url = url; }

    public String getUser() { return user; }
    public  void setUser(String user) { this.user = user;} 

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }     
    
    public void setConfiguration(LDAPConnectionConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public LDAPConnectionConfiguration getConfiguration() {
        return this.configuration;
    }

    public static class LDAPConnectionConfiguration {
        @Element(name="schema", required=true) private String schema;
        @ElementList(entry="parameter", inline=true, required=true) private List<Parameter> parameters;
                                 
        public LDAPConnectionConfiguration(@Element(name="schema", required=true) String schema) {
            this.schema = schema;        
            this.parameters = new ArrayList<Parameter>();
        }
        
        public String getSchema() { 
            return schema; 
        }
        
        public void setSchema(String schema) {
            this.schema = schema;
        }
                        
        public List<Parameter> getParameters() {
            return this.parameters;            
        }
                
        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }
        
        
        public static class Parameter {
            @Attribute(name="name", required=true) private String name;
            @Attribute(name="as", required=true) private String as;
            
            public Parameter(@Attribute(name="name", required=true) String name, @Attribute(name="as", required=true) String as) {
                this.name = name;
                this.as = as;
            }
            
            public String getName() {
                return this.name;
            }
            
            public String getAs() {
                return this.as;
            }            
        }
    }           
}

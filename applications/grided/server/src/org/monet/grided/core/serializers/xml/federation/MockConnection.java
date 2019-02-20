package org.monet.grided.core.serializers.xml.federation;

import org.monet.grided.core.serializers.xml.federation.FederationData.Connection;
import org.monet.grided.core.serializers.xml.federation.FederationData.ConnectionTypes;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="connection", strict=false)
public class MockConnection implements Connection {
    private String type;
    @Attribute(name="url") private String url;
    @Attribute(name="user") private String user;
    @Attribute(name="password") private String password;
    
    public MockConnection(@Attribute(name="url") String url, @Attribute(name="user") String user, @Attribute(name="password") String password) {        
        this.type = ConnectionTypes.MOCK;
        this.url = url;
        this.user = user;
        this.password = password;      
    }
    
    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;        
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;        
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;        
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;        
    }
}

      
    

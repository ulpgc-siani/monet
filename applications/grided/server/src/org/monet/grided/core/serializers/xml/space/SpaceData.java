package org.monet.grided.core.serializers.xml.space;

import java.util.ArrayList;
import java.util.List;

import org.monet.grided.core.persistence.filesystem.Data;
import org.monet.grided.core.serializers.xml.Logo;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationCubeStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationMapStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationServiceStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationThesaurusStatement;
import org.monet.grided.core.services.space.data.ServiceType;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="space-manifest", strict=false)
public class SpaceData implements Data {

    @ElementList(inline=true, required=false)    
    private List<PublicationServiceStatement> serviceStatements;

    @ElementList(inline=true, required=false)    
    private List<PublicationThesaurusStatement> thesaurusStatements;

    @ElementList(inline=true, required=false)    
    private List<PublicationMapStatement> mapStatements;

    @ElementList(inline=true, required=false)    
    private List<PublicationCubeStatement> cubeStatements;
        
    @Element(name="federation", required=true)
    private FederationStatement federationStatement;
    
    @Element(name="datawarehouse", required=true)
    private boolean datawarehouse;

    @Element(name="organization", required=true)
    private Organization organization;

    public SpaceData() {    
        this.serviceStatements = new ArrayList<PublicationServiceStatement>();
        this.thesaurusStatements = new ArrayList<PublicationThesaurusStatement>();
        this.mapStatements       = new ArrayList<PublicationMapStatement>();
        this.cubeStatements      = new ArrayList<PublicationCubeStatement>();
        
        this.federationStatement = new FederationStatement("", "");
        this.organization = new Organization();
        this.datawarehouse = false;
    }
    
    public String getLogoFilename() {
        Logo logo = this.organization.getLogo();
        return (logo != null)? logo.getName() : "";
    }
    
    public String getName() {
        return this.organization.getName();
    }
    
    public String getLabel() {
        return this.getOrganization().getLabel();
    }
    
    public String getUrl() {
        return this.organization.getUrl();
    }
    
    public void setDatawarhouse(boolean value) {
        this.datawarehouse = value;
    }
    public boolean getDatawarehouse() {        
        return this.datawarehouse;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    
    public Organization getOrganization() {
        return this.organization;
    }
            
    public void setFederationStatement(FederationStatement statement) {
        this.federationStatement = statement;
    }
    
    public FederationStatement getFederationStatement() {
        return this.federationStatement;
    }

    public void addPublicationServiceStatement(PublicationServiceStatement statement) {
        this.serviceStatements.add(statement);
    }

    public void addPublicationThesaurusStatement(PublicationThesaurusStatement statement) {
        this.thesaurusStatements.add(statement);
    }

    public void addPublicationMapStatement(PublicationMapStatement statement) {
        this.mapStatements.add(statement);
    }

    public void addPublicationCubeStatements(PublicationCubeStatement statement) {
        this.cubeStatements.add(statement);
    }

    public List<? extends PublicationFrontComponentStatement> getPublicationFrontComponentStatements(FrontComponentType type) {
        List<? extends PublicationFrontComponentStatement> statements = null;
        switch (type) {
        case Service: statements = this.serviceStatements; break;
        case Thesaurus: statements = this.thesaurusStatements; break;
        case Map: statements = this.mapStatements; break;
        case Cube: statements = this.cubeStatements; break;
        }
        return statements;
    }

    public List<? extends PublicationFrontComponentStatement> getPublicationFrontComponentStatements() {
        List<PublicationFrontComponentStatement> statements = new ArrayList<PublicationFrontComponentStatement>();
        statements.addAll(getPublicationFrontComponentStatements(FrontComponentType.Service));
        statements.addAll(getPublicationFrontComponentStatements(FrontComponentType.Thesaurus));
        statements.addAll(getPublicationFrontComponentStatements(FrontComponentType.Map));
        statements.addAll(getPublicationFrontComponentStatements(FrontComponentType.Cube));     
        return statements;
    }

  
    @Root
    public static class FederationStatement {
        private @Attribute(name="server") String serverIP;
        private @Attribute(name="name") String name;
                     
        public FederationStatement(@Attribute(name="server") String serverIP, @Attribute(name="name") String name) {
            this.serverIP = serverIP;
            this.name = name;
        }
        
        public String getServerIP() {
            return this.serverIP;
        }
        
        public String getName() {
            return this.name;
        }
    }

    @Root 
    public static class PublicationFrontComponentStatement {
        private @Attribute(name="name") String name;        
        
        public PublicationFrontComponentStatement(@Attribute(name="name") String name) {
            this.name = name;      
        }

        public String getName() {
            return this.name;
        }    
        
        public void setName(String name) {
            this.name = name;
        }
        

        public ServiceType getType() {
            ServiceType type = null;
            
            if (this instanceof PublicationServiceStatement) type = ServiceType.service;
            else if (this instanceof PublicationThesaurusStatement) type = ServiceType.thesaurus;
            else if (this instanceof PublicationMapStatement) type = ServiceType.map;
            else if (this instanceof PublicationCubeStatement) type = ServiceType.cube;
            
            return type; 
        }

        @Root(name="service")
        public static class PublicationServiceStatement extends PublicationFrontComponentStatement {
            public PublicationServiceStatement(@Attribute(name="name") String name) {
                super(name);      
            }   
        }

        @Root(name="thesaurus")
        public static class PublicationThesaurusStatement extends PublicationFrontComponentStatement {
            public PublicationThesaurusStatement(@Attribute(name="name") String name) {
                super(name);      
            }   
        }

        @Root(name="map")
        public static class PublicationMapStatement extends PublicationFrontComponentStatement {
            public PublicationMapStatement(@Attribute(name="name") String name) {
                super(name);      
            }   
        }

        @Root(name="cube")
        public static class PublicationCubeStatement extends PublicationFrontComponentStatement {
            public PublicationCubeStatement(@Attribute(name="name") String name) {
                super(name);      
            }   
        }
    }
}

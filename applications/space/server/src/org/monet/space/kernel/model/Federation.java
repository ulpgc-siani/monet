package org.monet.space.kernel.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "federation")
public class Federation {

    @Attribute(name = "uri")
    private String uri;
    @Attribute(name = "secret")
    private String secret;
    private String name;
    private String label;
    private String logoPath;

    public static class Service {
        private
        @Attribute(name = "name")
        String name = "";
        private
        @Attribute(name = "label")
        String label = "";
        private
        @Attribute(name = "ontology")
        String ontology = "";

        public String getName() {
            return this.name;
        }

        public String getLabel() {
            return this.label;
        }

        public String getOntology() {
            return this.ontology;
        }

        public Service(String name, String label, String ontology) {
            this.name = name;
            this.label = label;
            this.ontology = ontology;
        }
    }

    public static class Feeder {
        private
        @Attribute(name = "id")
        String id = "";
        private
        @Attribute(name = "label")
        String label = "";
        private
        @Attribute(name = "ontology")
        String ontology = "";

        public String getId() {
            return this.id;
        }

        public String getLabel() {
            return this.label;
        }

        public String getOntology() {
            return this.ontology;
        }

        public Feeder(String id, String label, String ontology) {
            this.id = id;
            this.label = label;
            this.ontology = ontology;
        }
    }

    public Federation(@Attribute(name = "uri") String uri, @Attribute(name = "secret") String secret) {
        this.uri = uri;
        this.secret = secret;
        this.name = "";
        this.label = "";
        this.logoPath = "";
    }

    public String getUri() {
        return this.uri;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLogoUrl() {
        return this.uri + "/" + this.logoPath;
    }

    public boolean equals(Federation federation) {
        if (this.uri == null || !this.uri.equals(federation.getUri()))
            return false;
        if (this.secret == null || !this.secret.equals(federation.getSecret()))
            return false;
        return true;
    }

}
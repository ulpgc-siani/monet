package org.monet.grided.core.model;

public class MetaModelVersion implements MonetVersion {

    private String value;
    private String[] tokens;

    public MetaModelVersion(String value) {
        this.value = value;
        this.tokens = this.value.split("\\.");
        if (this.tokens.length < 2) throw new IllegalArgumentException("Token must to have fomat d.d"); 
    }

    @Override
    public String getValue() {
        return this.value;
    }
    
    @Override
    public boolean isCompatible(String value) {
        boolean result = false;
        try {
            String[]   tokens   = value.split("\\.");
            result = this.tokens[0].equals(tokens[0]) && this.tokens[1].equals(tokens[1]); 
        } catch (Exception ex) {
            return false;
        }
        return result;
    }
}





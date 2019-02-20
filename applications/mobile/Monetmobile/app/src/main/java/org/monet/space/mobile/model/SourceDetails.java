package org.monet.space.mobile.model;

public class SourceDetails {

    public long id;
    public String label;
    public String accountName;
    public String title;
    public String subtitle;

    public SourceDetails(long id, String label, String accountName, String title, String subtitle) {
        this.id = id;
        this.label = label;
        this.accountName = accountName;
    }
}

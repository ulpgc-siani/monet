package client.core.system;

import client.core.model.definition.entity.SourceDefinition;
import client.core.model.types.TermList;

public class Source<Definition extends SourceDefinition> extends Entity<Definition> implements client.core.model.Source<Definition> {
    private TermList termList;
    private String url;

    public Source() {
        this.url = null;
    }

    public Source(String id, String label) {
        super(id, label);
        this.url = null;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public TermList getTerms() {
        return this.termList;
    }

    public void setTerms(TermList termList) {
        this.termList = termList;
    }

    @Override
    public final ClassName getClassName() {
        return client.core.model.Source.CLASS_NAME;
    }
}
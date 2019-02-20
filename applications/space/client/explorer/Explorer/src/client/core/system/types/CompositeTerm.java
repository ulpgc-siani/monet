package client.core.system.types;

import client.core.model.types.TermList;

public abstract class CompositeTerm extends Term implements client.core.model.types.CompositeTerm {

    protected TermList terms;

    public CompositeTerm() {
        terms = new client.core.system.types.TermList();
    }

    public CompositeTerm(String value, String label) {
        super(value, label);
        terms = new client.core.system.types.TermList();
    }

    @Override
    public client.core.model.types.TermList getTerms() {
        return terms;
    }

    @Override
    public void setTerms(client.core.model.types.TermList terms) {
        this.terms = terms;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}

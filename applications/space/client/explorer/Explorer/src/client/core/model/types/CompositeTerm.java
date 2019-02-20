package client.core.model.types;

public interface CompositeTerm extends Term {

    TermList getTerms();
    void setTerms(TermList terms);
    boolean isSelectable();
}

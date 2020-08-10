package org.monet.bpi;

import org.monet.bpi.types.Term;

import java.util.List;

public interface FieldSelect extends Field<Term> {
    String getSource();
    String getSourceDefinitionCode();
    void setInlineTerms(List<Term> termList);
}
package client.core.model;

import client.core.model.definition.entity.SourceDefinition;
import client.core.model.types.TermList;

public interface Source<Definition extends SourceDefinition> extends Entity<Definition> {

    ClassName CLASS_NAME = new ClassName("Source");

    String getUrl();
    TermList getTerms();
    ClassName getClassName();

}
package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ThesaurusProviderDefinitionSemanticChecks extends ProviderDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ThesaurusProviderDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ThesaurusProviderDefinition metaitem = (ThesaurusProviderDefinition) definition;

  

  }
}



















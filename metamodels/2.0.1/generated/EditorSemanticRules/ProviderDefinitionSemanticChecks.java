package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ProviderDefinitionSemanticChecks extends InteroperabilityDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ProviderDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ProviderDefinition metaitem = (ProviderDefinition) definition;

  

  }
}



















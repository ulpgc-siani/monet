package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class CubeProviderDefinitionSemanticChecks extends ProviderDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof CubeProviderDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      CubeProviderDefinition metaitem = (CubeProviderDefinition) definition;

  

  }
}



















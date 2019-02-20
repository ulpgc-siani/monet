package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ModelSemanticChecks extends DeclarationRule {

  public void check(Model metaitem) {
  if(metaitem == null) return;
  
  

  if (metaitem.getCode() == null || metaitem.getCode().isEmpty())
    metaitem.setCode(this.getCode(metaitem.getName(), metaitem.getClass()));
  else
    this.checkUnique(metaitem.getCode());
    
                      
                      
            if(metaitem.getVersion() != null) {
      Model.Version item = metaitem.getVersion();
                                                      }
                  GenericDeclarationSemanticChecks genericDeclaration = new GenericDeclarationSemanticChecks();
      genericDeclaration.setProblems(this.getProblems());
      genericDeclaration.setModule(this.getModule());
      for(GenericDeclaration include : metaitem.getGenericDeclarationList()) {
        genericDeclaration.check(include);
      }
                  ResolveDeclarationSemanticChecks resolveDeclaration = new ResolveDeclarationSemanticChecks();
      resolveDeclaration.setProblems(this.getProblems());
      resolveDeclaration.setModule(this.getModule());
      for(ResolveDeclaration include : metaitem.getResolveDeclarationList()) {
        resolveDeclaration.check(include);
      }
        }
}


















